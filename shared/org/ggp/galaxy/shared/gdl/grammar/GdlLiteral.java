package org.ggp.galaxy.shared.gdl.grammar;

@SuppressWarnings("serial")
public abstract class GdlLiteral extends Gdl
{

	@Override
	public abstract boolean isGround();

	@Override
	public abstract String toString();

}