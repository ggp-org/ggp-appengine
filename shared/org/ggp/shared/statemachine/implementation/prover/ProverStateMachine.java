package org.ggp.shared.statemachine.implementation.prover;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ggp.shared.gdl.grammar.*;
import org.ggp.shared.prover.Prover;
import org.ggp.shared.prover.aima.AimaProver;
import org.ggp.shared.statemachine.MachineState;
import org.ggp.shared.statemachine.Move;
import org.ggp.shared.statemachine.Role;
import org.ggp.shared.statemachine.StateMachine;
import org.ggp.shared.statemachine.exceptions.GoalDefinitionException;
import org.ggp.shared.statemachine.exceptions.MoveDefinitionException;
import org.ggp.shared.statemachine.exceptions.TransitionDefinitionException;
import org.ggp.shared.statemachine.implementation.prover.query.ProverQueryBuilder;
import org.ggp.shared.statemachine.implementation.prover.result.ProverResultParser;

public class ProverStateMachine extends StateMachine
{
	private MachineState initialState;
	private Prover prover;
	private List<Role> roles;

	/**
	 * Initialize must be called before using the StateMachine
	 */
	public ProverStateMachine()
	{
		
	}
	
	public void initialize(List<Gdl> description)
	{
		prover = new AimaProver(new HashSet<Gdl>(description));
		roles = Role.computeRoles(description);
		initialState = computeInitialState();
	}

	private MachineState computeInitialState()
	{
		Set<GdlSentence> results = prover.askAll(ProverQueryBuilder.getInitQuery(), new HashSet<GdlSentence>());
		return new ProverResultParser().toState(results);
	}

	@Override
	public int getGoal(MachineState state, Role role) throws GoalDefinitionException
	{
		Set<GdlSentence> results = prover.askAll(ProverQueryBuilder.getGoalQuery(role), ProverQueryBuilder.getContext(state));

		if (results.size() != 1)
		{
			throw new GoalDefinitionException(state, role);
		}

		try
		{
			GdlRelation relation = (GdlRelation) results.iterator().next();
			GdlConstant constant = (GdlConstant) relation.get(1);

			return Integer.parseInt(constant.toString());
		}
		catch (Exception e)
		{
			throw new GoalDefinitionException(state, role);
		}
	}

	@Override
	public MachineState getInitialState()
	{
		return initialState;
	}
	
	@Override
	public List<Move> getLegalMoves(MachineState state, Role role) throws MoveDefinitionException
	{
		Set<GdlSentence> results = prover.askAll(ProverQueryBuilder.getLegalQuery(role), ProverQueryBuilder.getContext(state));

		if (results.size() == 0)
		{
			throw new MoveDefinitionException(state, role);
		}

		return new ProverResultParser().toMoves(results);
	}

	@Override
	public MachineState getNextState(MachineState state, List<Move> moves) throws TransitionDefinitionException
	{
		Set<GdlSentence> results = prover.askAll(ProverQueryBuilder.getNextQuery(), ProverQueryBuilder.getContext(state, getRoles(), moves));

		for (GdlSentence sentence : results)
		{
			if (!sentence.isGround())
			{
				throw new TransitionDefinitionException(state, moves);
			}
		}

		return new ProverResultParser().toState(results);
	}	

	@Override
	public List<Role> getRoles()
	{
		return roles;
	}

	@Override
	public boolean isTerminal(MachineState state)
	{
		return prover.prove(ProverQueryBuilder.getTerminalQuery(), ProverQueryBuilder.getContext(state));
	}

	@Override
	public MachineState getMachineStateFromSentenceList(Set<GdlSentence> sentenceList) {
		return new ProverMachineState(sentenceList);
	}

	@Override
	public Move getMoveFromSentence(GdlSentence sentence) {
		return new ProverMove(sentence);
	}

	@Override
	public Role getRoleFromProp(GdlProposition proposition) {
		return new ProverRole(proposition);
	}

}
