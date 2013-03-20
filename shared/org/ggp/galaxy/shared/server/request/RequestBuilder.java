package org.ggp.galaxy.shared.server.request;

import java.util.List;

import org.ggp.galaxy.shared.gdl.grammar.Gdl;
import org.ggp.galaxy.shared.gdl.scrambler.GdlScrambler;
import org.ggp.galaxy.shared.statemachine.Move;
import org.ggp.galaxy.shared.statemachine.Role;

public final class RequestBuilder
{
	public static String getPlayRequest(String matchId, List<Move> moves, GdlScrambler scrambler)
	{
		if (moves == null) {
			return "( PLAY " + matchId + " NIL )";
		} else {
			StringBuilder sb = new StringBuilder();

			sb.append("( PLAY " + matchId + " (");
			for (Move move : moves)
			{
				sb.append(scrambler.scramble(move.getContents()) + " ");
			}
			sb.append(") )");

			return sb.toString();			
		}
	}

	public static String getStartRequest(String matchId, Role role, List<Gdl> description, int startClock, int playClock, GdlScrambler scrambler)
	{
		StringBuilder sb = new StringBuilder();

		sb.append("( START " + matchId + " " + scrambler.scramble(role.getName()) + " (");
		for (Gdl gdl : description)
		{
			sb.append(scrambler.scramble(gdl) + " ");
		}
		sb.append(") " + startClock + " " + playClock + ")");

		return sb.toString();
	}
	
	public static String getAnalyzeRequest(List<Gdl> description, int analysisClock, GdlScrambler scrambler)
	{
		StringBuilder sb = new StringBuilder();

		sb.append("( ANALYZE (");
		for (Gdl gdl : description)
		{
			sb.append(scrambler.scramble(gdl) + " ");
		}
		sb.append(") " + analysisClock + " )");

		return sb.toString();
	}	

	public static String getStopRequest(String matchId, List<Move> moves, GdlScrambler scrambler)
	{
		if (moves == null) {
			return "( STOP " + matchId + " NIL )";
		} else {
			StringBuilder sb = new StringBuilder();
	
			sb.append("( STOP " + matchId + " (");
			for (Move move : moves)
			{
				sb.append(scrambler.scramble(move.getContents()) + " ");
			}
			sb.append(") )");
	
			return sb.toString();
		}
	}
	
	public static String getAbortRequest(String matchId)
	{
		return "( ABORT " + matchId + " )";
	}
	
	public static String getInfoRequest()
	{
		return "( INFO )";
	}
}