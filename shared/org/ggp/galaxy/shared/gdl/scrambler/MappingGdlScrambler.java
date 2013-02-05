package org.ggp.galaxy.shared.gdl.scrambler;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import org.ggp.galaxy.shared.gdl.factory.GdlFactory;
import org.ggp.galaxy.shared.gdl.factory.exceptions.GdlFormatException;
import org.ggp.galaxy.shared.gdl.grammar.Gdl;
import org.ggp.galaxy.shared.gdl.grammar.GdlConstant;
import org.ggp.galaxy.shared.gdl.grammar.GdlVariable;
import org.ggp.galaxy.shared.symbol.factory.exceptions.SymbolFormatException;

public class MappingGdlScrambler implements GdlScrambler {	
	private Map<String,String> scrambleMapping;	
	private Map<String,String> unscrambleMapping;	
	private Random random;
	
	private int scrambledPrefix;
	private Stack<String> scrambledTokens;
	
	public MappingGdlScrambler(Random theRandom) {
		random = theRandom;
		scrambleMapping = new HashMap<String,String>();
		unscrambleMapping = new HashMap<String,String>();
		
		scrambledPrefix = 0;
		scrambledTokens = new Stack<String>();
		for (String word : WordList.words) {
			scrambledTokens.add(word);
		}
		Collections.shuffle(scrambledTokens, random);
	}
	
	private class ScramblingRenderer extends GdlRenderer {
		@Override
		protected String renderConstant(GdlConstant constant) {
			return scrambleWord(constant.getValue());
		}
		@Override
		protected String renderVariable(GdlVariable variable) {
			return scrambleWord(variable.toString());
		}		
	}
	private class UnscramblingRenderer extends GdlRenderer {
		@Override
		protected String renderConstant(GdlConstant constant) {
			return unscrambleWord(constant.getValue());
		}
		@Override
		protected String renderVariable(GdlVariable variable) {
			return unscrambleWord(variable.toString());
		}		
	}	
	
	@Override
	public String scramble(Gdl x) {
		return new ScramblingRenderer().renderGdl(x);
	}
	
	@Override
	public Gdl unscramble(String x) throws SymbolFormatException, GdlFormatException {
		return GdlFactory.create(new UnscramblingRenderer().renderGdl(GdlFactory.create(x)));
	}
	
	@Override
	public boolean scrambles() {
		return true;
	}
	
	private String scrambleWord(String realWord) {
		if (!shouldMap(realWord)) {
			return realWord;
		}
		if (!scrambleMapping.containsKey(realWord)) {
			String fakeWord = getRandomWord();
			if (realWord.startsWith("?")) {
				fakeWord = "?" + fakeWord;
			}
			scrambleMapping.put(realWord, fakeWord);
			unscrambleMapping.put(fakeWord, realWord);
		}
		return scrambleMapping.get(realWord);
	}
	
	private String unscrambleWord(String fakeWord) {
		if (!shouldMap(fakeWord)) {
			return fakeWord;
		}
		fakeWord = fakeWord.toLowerCase();
		if (!unscrambleMapping.containsKey(fakeWord)) {
			throw new RuntimeException("Could not find scramble mapping for: " + fakeWord);
		}
		return unscrambleMapping.get(fakeWord);
	}

	private String getRandomWord() {
		if (scrambledTokens.isEmpty()) {
			for (String word : WordList.words) {
				scrambledTokens.add(word + scrambledPrefix);
			}
			Collections.shuffle(scrambledTokens, random);
			scrambledPrefix++;
		}
		return scrambledTokens.pop();
	}
	
	// TODO(schreib): Factor this out so that the keyword list can be shared
	// between the GdlPool and the MappingGdlScrambler without causing problems
	// for projects that import this project to depend on it.
	private static final HashSet<String> keywords = new HashSet<String>(Arrays.asList(
    		new String[] {"init","true","next","role","does","goal","legal","terminal","base","input"}));
	private static boolean shouldMap(String token) {
		if (keywords.contains(token.toLowerCase())) {
			return false;
		}
		try {
			Integer.parseInt(token);
			return false;
		} catch (NumberFormatException e) {
			;
		}
		return true;
	}
}