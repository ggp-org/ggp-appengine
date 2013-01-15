package org.ggp.galaxy.shared.gdl.scrambler;

import org.ggp.galaxy.shared.gdl.factory.GdlFactory;
import org.ggp.galaxy.shared.gdl.factory.exceptions.GdlFormatException;
import org.ggp.galaxy.shared.gdl.grammar.Gdl;
import org.ggp.galaxy.shared.symbol.factory.exceptions.SymbolFormatException;

public class NoOpGdlScrambler implements GdlScrambler {
	@Override
	public String scramble(Gdl x) {
		return x.toString();
	}
	@Override
	public Gdl unscramble(String x) throws SymbolFormatException, GdlFormatException {
		return GdlFactory.create(x);
	}
	@Override
	public boolean scrambles() {
		return false;
	}
}