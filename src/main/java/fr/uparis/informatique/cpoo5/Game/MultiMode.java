package fr.uparis.informatique.cpoo5.Game;

import java.io.IOException;
import java.util.List;
import org.fxmisc.richtext.StyleClassedTextArea;
import Words.AbstractWord;
import Words.Word;
import Words.WordColor;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Multiplayer mode
 * @author rh4
 *
 */
public class MultiMode extends AbstractGame {
	
	private Player player;
	
	public MultiMode(String player_name, String ip_address) throws IOException {
		setPlayer(new Player(player_name, ip_address, this));
		getPlayer().start();
		initializeGame(getActualStage());
	}
	
	

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
		
	@Override
	public WordColor getGameModeWordColor() {
		return WordColor.RED;
	}
	
	/**
	 * Send red words properly typed to server
	 */
	@Override
    public void validateWord (StyleClassedTextArea textArea, List<AbstractWord> next_words_to_type_list, List<AbstractWord> words_typed_list) {
		
		int last_index = getLastIndex(words_typed_list);
		AbstractWord typed = last_index == -1 ? new Word("") : words_typed_list.get(last_index);
		AbstractWord to_type = next_words_to_type_list.get(0);
		
		super.validateWord(textArea, next_words_to_type_list, words_typed_list);
		
		if (to_type.getColor() == WordColor.RED && !to_type.wasErrorMade() && typed.getWord().equals(to_type.getWord())) {
			getPlayer().send_word_to_server(to_type.getWord());	
		}
		
	}
	
	public String getName() {
		return "Multi";
	}
		
	@Override
	public void startTimer(VBox root) {
		return;
	}
		
	/**
	 * Prevents backtrack to a previously validated word
	 */
	@Override
	public boolean backtrack(StyleClassedTextArea textArea, List<AbstractWord> words_typed, List<AbstractWord> next_words_to_type,
			List<AbstractWord> validated_words) {
		
		
		int last_typed_index = getLastIndex(words_typed);
		if(last_typed_index == -1) {
			return false;
		}
		AbstractWord last_typed = words_typed.get(last_typed_index);
		if(!last_typed.getWord().isEmpty()) {
			return super.backtrack(textArea, words_typed, next_words_to_type, validated_words);
		}
		else {
			return false;
		}
		
	}
	
	/**
	 * Prevents starting before every player is logged in
	 */
	@Override
	public EventHandler<KeyEvent> gameHandlerKeyReleased(Stage primaryStage, List<AbstractWord> words_typed,
			List<AbstractWord> next_words_to_type, VBox root, StyleClassedTextArea textArea) {
				
		var fun = super.gameHandlerKeyReleased(primaryStage, words_typed, next_words_to_type, root, textArea);
		
		return new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(!hasStarted()) return;
				fun.handle(event);
			}
		};
	}

	
	protected void manageWordReceived (AbstractWord word) {
		if(getNexts_words().size() >= WORDS_NUMBER_LIMIT) {
			Platform.runLater(() -> {
				getGameTextArea().fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, null, null, KeyCode.SPACE, false, false, false, false));
				insertServerWordToTextArea(getGameTextArea(), word.getWord()+" ");
				getNexts_words().add(word);	
			}); 
			
		}	
		else {
			insertServerWordToTextArea(getGameTextArea(), word.getWord()+" ");
			getNexts_words().add(word);
		}
		

	}
		
	protected void insertServerWordToTextArea(StyleClassedTextArea area, String typed) {
		int pos = area.getCaretPosition();
		int id = getLastIndex(getTypedWords());

		int offset = id == -1 ? 0: getTypedWords().get(id).getWord().length();

		int res = 0;
		for (var w : getNexts_words()) {
			res += w.getWord().length()+1;
		}
		area.insert(pos+res-offset, typed, "BLACK");
		area.moveTo(pos);
	}

	
	@Override
	public EventHandler<WindowEvent> closeProperly(WindowEvent t) {
		
		var fun =  super.closeProperly(t);
		return new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				try {
    				getPlayer().getSocket().close();
    			} 
    			catch (IOException e) {
    				e.printStackTrace();
    			}
    	    	finally {
					fun.handle(event);;
				}
				
			}
		
		};
	}
	
	@Override
	public void endGame() {
		try {
			this.getPlayer().getSocket().close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			super.endGame();
		}
	}

	
}
