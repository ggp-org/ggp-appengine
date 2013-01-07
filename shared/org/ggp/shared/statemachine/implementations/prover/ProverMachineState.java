package org.ggp.shared.statemachine.implementations.prover;

import java.io.Serializable;
import java.util.Set;

import org.ggp.shared.gdl.grammar.GdlSentence;
import org.ggp.shared.statemachine.MachineState;

@SuppressWarnings("serial")
public final class ProverMachineState extends MachineState implements Serializable
{
	private final Set<GdlSentence> contents;

	public ProverMachineState(Set<GdlSentence> contents)
	{
		this.contents = contents;
	}

	/* (non-Javadoc)
	 * @see util.statemachine.prover.MachineState#getContents()
	 */
	public Set<GdlSentence> getContents()
	{
		return contents;
	}
}
