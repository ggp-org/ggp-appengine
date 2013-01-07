package org.ggp.shared.statemachine.implementation.prover;

import org.ggp.shared.gdl.grammar.GdlSentence;
import org.ggp.shared.statemachine.Move;

@SuppressWarnings("serial")
public final class ProverMove extends Move
{
    public ProverMove(GdlSentence contents) {
        super(contents);
    }
}