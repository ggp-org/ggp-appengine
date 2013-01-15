package org.ggp.galaxy.shared.symbol.grammar;

public final class SymbolAtom extends Symbol
{

	private final String value;

	SymbolAtom(String value)
	{
		this.value = value.intern();
	}

	public String getValue()
	{
		return value;
	}

	@Override
	public String toString()
	{
		return value;
	}

}
