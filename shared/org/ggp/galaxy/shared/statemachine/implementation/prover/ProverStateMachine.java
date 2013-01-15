package org.ggp.galaxy.shared.statemachine.implementation.prover;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ggp.galaxy.shared.gdl.grammar.*;
import org.ggp.galaxy.shared.logging.GamerLogger;
import org.ggp.galaxy.shared.prover.Prover;
import org.ggp.galaxy.shared.prover.aima.AimaProver;
import org.ggp.galaxy.shared.statemachine.MachineState;
import org.ggp.galaxy.shared.statemachine.Move;
import org.ggp.galaxy.shared.statemachine.Role;
import org.ggp.galaxy.shared.statemachine.StateMachine;
import org.ggp.galaxy.shared.statemachine.exceptions.GoalDefinitionException;
import org.ggp.galaxy.shared.statemachine.exceptions.MoveDefinitionException;
import org.ggp.galaxy.shared.statemachine.exceptions.TransitionDefinitionException;
import org.ggp.galaxy.shared.statemachine.implementation.prover.query.ProverQueryBuilder;
import org.ggp.galaxy.shared.statemachine.implementation.prover.result.ProverResultParser;

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
		    GamerLogger.logError("StateMachine", "Got goal results of size: " + results.size() + " when expecting size one.");
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
}