package org.ggp.shared.symbol.factory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ggp.shared.symbol.factory.exceptions.SymbolFormatException;
import org.ggp.shared.symbol.grammar.*;

public final class SymbolFactory
{

	private static Symbol convert(LinkedList<String> tokens)
	{
		if (tokens.getFirst().equals("("))
		{
			return convertList(tokens);
		}
		else
		{
			return convertAtom(tokens);
		}
	}

	private static SymbolAtom convertAtom(LinkedList<String> tokens)
	{
		return SymbolPool.getAtom(tokens.removeFirst());
	}

	private static SymbolList convertList(LinkedList<String> tokens)
	{
		List<Symbol> contents = new ArrayList<Symbol>();

		tokens.removeFirst();
		while (!tokens.getFirst().equals(")"))
		{
			contents.add(convert(tokens));
		}
		tokens.removeFirst();

		return SymbolPool.getList(contents);
	}

	public static Symbol create(String string) throws SymbolFormatException
	{
		try
		{
			String preprocessed = preprocess(string);
			List<String> tokens = lex(preprocessed);
			return convert(new LinkedList<String>(tokens));
		}
		catch (Exception e)
		{
			throw new SymbolFormatException(string);
		}
	}

	private static List<String> lex(String string)
	{
		List<String> tokens = new ArrayList<String>();
		for (String token : string.split(" "))
		{
			tokens.add(token);
		}

		return tokens;
	}

	private static String preprocess(String string)
	{
		//string = string.toLowerCase();

		string = string.replaceAll("\\(", " ( ");
		string = string.replaceAll("\\)", " ) ");

		string = string.replaceAll("\\s+", " ");
		string = string.trim();

		return string;
	}

}