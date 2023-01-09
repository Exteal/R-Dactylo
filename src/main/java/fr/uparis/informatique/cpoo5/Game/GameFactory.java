package fr.uparis.informatique.cpoo5.Game;

import java.io.IOException;

/**
 * Factory to create games instances
 * @author rh4
 *
 */
public class GameFactory {
	
	public static Game createGame(GameModes type, String[] args) throws IOException {
		switch(type) {
		case NORMAL:
			return new NormalMode();		
		case PLAYGROUND:
			return new PlaygroundMode();
		case MULTI:
			return new MultiMode(args[0], args[1]);
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		
		}
	}
	
}
