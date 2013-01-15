package org.ggp.galaxy.shared.prover;

import java.util.Set;

import org.ggp.galaxy.shared.gdl.grammar.GdlSentence;

public abstract class Prover
{
	public abstract Set<GdlSentence> askAll(GdlSentence query, Set<GdlSentence> context);
	public abstract GdlSentence askOne(GdlSentence query, Set<GdlSentence> context);
	public abstract boolean prove(GdlSentence query, Set<GdlSentence> context);
}
