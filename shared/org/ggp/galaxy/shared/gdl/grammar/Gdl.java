package org.ggp.galaxy.shared.gdl.grammar;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class Gdl implements Serializable
{

	public abstract boolean isGround();

	@Override
	public abstract String toString();

}
