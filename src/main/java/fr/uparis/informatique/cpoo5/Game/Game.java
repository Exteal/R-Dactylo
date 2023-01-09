package fr.uparis.informatique.cpoo5.Game;

import java.util.List;

import Words.WordColor;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Game interface
 * @author rh4
 *
 */
public interface Game {
	
	int stage_width = 500;
	
	//limit of words allowed in the next words list at the same time
	final int WORDS_NUMBER_LIMIT = 15;

	String getName();	
	void endGame();
	
	default Stage getActualStage() {
		List<Window> open = Stage.getWindows().stream().filter(Window::isShowing).toList();
		return (Stage) open.get(0);
	}
	
	void initializeGame(Stage s);
	WordColor getGameModeWordColor();
}
