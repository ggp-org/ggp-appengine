package org.ggp.galaxy.shared.gdl.scrambler;

import org.ggp.galaxy.shared.gdl.factory.exceptions.GdlFormatException;
import org.ggp.galaxy.shared.gdl.grammar.Gdl;
import org.ggp.galaxy.shared.symbol.factory.exceptions.SymbolFormatException;

public interface GdlScrambler {	
	public String scramble(Gdl x);
	public Gdl unscramble(String x) throws SymbolFormatException, GdlFormatException;
	public boolean scrambles();
}