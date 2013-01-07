package org.ggp.shared.statemachine.implementation.prover.result;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ggp.shared.gdl.grammar.GdlConstant;
import org.ggp.shared.gdl.grammar.GdlPool;
import org.ggp.shared.gdl.grammar.GdlProposition;
import org.ggp.shared.gdl.grammar.GdlSentence;
import org.ggp.shared.gdl.grammar.GdlTerm;
import org.ggp.shared.statemachine.MachineState;
import org.ggp.shared.statemachine.Move;
import org.ggp.shared.statemachine.Role;
import org.ggp.shared.statemachine.implementation.prover.ProverMachineState;
import org.ggp.shared.statemachine.implementation.prover.ProverMove;
import org.ggp.shared.statemachine.implementation.prover.ProverRole;

public final class ProverResultParser
{

	private final static GdlConstant TRUE = GdlPool.getConstant("true");

	public List<Move> toMoves(Set<GdlSentence> results)
	{
		List<Move> moves = new ArrayList<Move>();
		for (GdlSentence result : results)
		{
			moves.add(new ProverMove(result.get(1).toSentence()));
		}

		return moves;
	}

	public List<Role> toRoles(List<GdlSentence> results)
	{
		List<Role> roles = new ArrayList<Role>();
		for (GdlSentence result : results)
		{
			GdlProposition name = (GdlProposition) result.get(0).toSentence();
			roles.add(new ProverRole(name));
		}

		return roles;
	}

	public MachineState toState(Set<GdlSentence> results)
	{
		Set<GdlSentence> trues = new HashSet<GdlSentence>();
		for (GdlSentence result : results)
		{
			trues.add(GdlPool.getRelation(TRUE, new GdlTerm[] { result.get(0) }));
		}
		return new ProverMachineState(trues);
	}
}