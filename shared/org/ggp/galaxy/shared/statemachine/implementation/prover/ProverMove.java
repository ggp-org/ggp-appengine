package org.ggp.galaxy.shared.statemachine.implementation.prover;

import org.ggp.galaxy.shared.gdl.grammar.GdlSentence;
import org.ggp.galaxy.shared.statemachine.Move;

@SuppressWarnings("serial")
public final class ProverMove extends Move
{
    public ProverMove(GdlSentence contents) {
        super(contents);
    }
}