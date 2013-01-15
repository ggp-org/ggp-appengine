package org.ggp.galaxy.shared.gdl.grammar;

@SuppressWarnings("serial")
public abstract class GdlTerm extends Gdl
{

	@Override
	public abstract boolean isGround();

	public abstract GdlSentence toSentence();

	@Override
	public abstract String toString();

}
