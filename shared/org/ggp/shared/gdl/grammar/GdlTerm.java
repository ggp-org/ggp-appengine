package org.ggp.shared.gdl.grammar;

@SuppressWarnings("serial")
public abstract class GdlTerm extends Gdl
{

	@Override
	public abstract boolean isGround();

	public abstract GdlSentence toSentence();

	@Override
	public abstract String toString();

}
