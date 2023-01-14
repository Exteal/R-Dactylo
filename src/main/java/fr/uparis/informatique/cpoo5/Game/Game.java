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
	
	//limit of words allowed in the next words list at the same time
	final int WORDS_NUMBER_LIMIT = 15;
	
	int stage_width = 650;
	int stage_heigth = 300;
	
	String getName();	
	
	void initializeGame(Stage s);
	void endGame();
	
	default Stage getActualStage() {
		List<Window> open = Stage.getWindows().stream().filter(Window::isShowing).toList();
		return (Stage) open.get(0);
	}
	
	
	WordColor getGameModeWordColor();
	
	String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed molestie, arcu eget porttitor viverra, arcu felis iaculis augue, id tempus sem ipsum sit amet est. Nulla in iaculis eros. Nunc lectus tortor, rhoncus sit amet lectus ut, faucibus interdum ante. Cras vitae nibh ut purus tristique fringilla. Aenean vestibulum porta augue ut maximus. Integer rhoncus a neque non pretium. Curabitur aliquam eget tellus vel eleifend. Fusce dictum, felis vel semper egestas, diam nulla efficitur arcu, quis vestibulum diam nisl nec ante. Nullam lacus augue, consequat et lorem vel, condimentum tincidunt odio. Aenean diam risus, feugiat ut orci sed, dictum hendrerit neque.\r\n"
    		+ "\r\n"
    		+ "Duis a commodo quam. Phasellus nec nulla vitae risus mattis consequat sit amet non dui. Ut lacinia euismod mauris. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed laoreet dapibus ex, quis tempus eros ornare porta. Quisque porttitor metus sed sapien varius aliquam. Donec at tempor velit, vel ornare felis. Nunc iaculis mauris sit amet iaculis pharetra.\r\n"
    		+ "\r\n"
    		+ "Phasellus congue sit amet leo vitae feugiat. Phasellus ac ipsum a diam auctor hendrerit. Morbi condimentum magna mi, sit amet dapibus mi vulputate in. Quisque condimentum purus ut dignissim lacinia. Sed quis nibh vehicula, mollis quam iaculis, vehicula turpis. Nunc at est faucibus, mollis nisl eget, dictum enim. Phasellus sit amet augue tincidunt elit ullamcorper porttitor scelerisque eget urna. In sit amet sapien lectus. Duis eget aliquam sem, vitae posuere diam. Cras faucibus nisi a ante porttitor convallis. Aliquam quis erat molestie, interdum orci a, ultricies enim. Cras lobortis ut felis ut lobortis.\r\n"
    		+ "\r\n"
    		+ "Sed erat neque, hendrerit in quam dignissim, viverra facilisis quam. Morbi consequat ligula in nulla scelerisque gravida nec non augue. Aenean consectetur fermentum sapien et vehicula. Nullam scelerisque vitae lacus et sodales. Mauris id urna nec nunc bibendum pellentesque. Phasellus vel aliquet risus. In ultricies eu ante ac hendrerit. ";
    
	
}
