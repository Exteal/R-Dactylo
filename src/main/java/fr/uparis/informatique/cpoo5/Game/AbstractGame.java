package fr.uparis.informatique.cpoo5.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import org.fxmisc.richtext.StyleClassedTextArea;

import Words.AbstractWord;
import Words.Word;
import Words.WordColor;
import Words.WordsFactory;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Abstract class representing a game instance <br>
 * Contains general rules, not the specifics 
 * @author rh4
 *
 */
public abstract class AbstractGame implements Game {
	
	
	
	private GameDisplay display;
    public GameDisplay getDisplay() {
		return display;
	}

	public void setDisplay(GameDisplay display) {
		this.display = display;
	}

	private boolean has_started = false;
    private int game_length_in_seconds = 30;
    private int useful_chars = 0;
    private int keys_pressed = 0;
    private Timer game_timer;
    private WordsFactory words_factory;
    private StyleClassedTextArea gameTextArea;
    private List<AbstractWord> nextsWords;	  
	private List<AbstractWord> typedWords;

    
    public abstract WordColor getGameModeWordColor();
    
	public List<AbstractWord> getTypedWords() {
		return typedWords;
	}

	public void setTyped(List<AbstractWord> typed) {
		this.typedWords = typed;
	}

	public WordsFactory getFactory() {
		return words_factory;
	}

	public void setFactory(WordsFactory factory) {
		this.words_factory = factory;
	}

	public boolean hasStarted() {
		return has_started;
	}

	public void setStarted(boolean has_started) {
		this.has_started = has_started;
	}
	
    public Timer getGameTimer() {
		return game_timer;
	}

	public void setGameTimer(Timer game_timer) {
		this.game_timer = game_timer;
	}

    public int getKeysPressed() {
		return keys_pressed;
	}

	public void setKeysPressed(int keys_pressed) {
		this.keys_pressed = keys_pressed;
	}

	public int getGameLengthInSeconds() {
		return game_length_in_seconds;
	}

	public void setGameLengthInSeconds(int game_length_in_seconds) {
		this.game_length_in_seconds = game_length_in_seconds;
	}
	
	public void setNexts_words(List<AbstractWord> nexts_words) {
		this.nextsWords = nexts_words;
	}
	
	public List<AbstractWord> getNexts_words() {
		return nextsWords;
	}
	
	public StyleClassedTextArea getGameTextArea() {
		return gameTextArea;
	}

	public void setGameTextArea( StyleClassedTextArea area) {
		this.gameTextArea = area;
	}
	
	public int getLastIndex(List<AbstractWord> list) {
	    	if(list.isEmpty()) return -1;
	    	else return list.size() - 1;
    }
	
	public boolean isValidKey(KeyEvent e) {
		var code = e.getCode();
		return code.isLetterKey() || code == KeyCode.EXCLAMATION_MARK || code == KeyCode.PERIOD || code == KeyCode.COMMA;
	}
	 
	 public boolean isRightChar(String char_typed, String typed, String toType) {
		   
	    	if(typed.length() >= toType.length()) {
	    		return false;
	    	}   	
	    	
	    	else {
	    		return char_typed.equals(String.valueOf(toType.charAt(typed.length())));
	    	}
	    			
    }
	 
	 /**
	  * Instanciates a timer that ends game after a certain amount of time
	  * @param root
	  */
    public void startTimer(VBox root) {
        Label lab = new Label();
        Timer timer_countdown = new Timer();
        int full_time = getGameLengthInSeconds();
        
        
        getDisplay().updateLabel(lab, String.valueOf(getGameLengthInSeconds()));
		root.getChildren().add(1, lab);
		
        
        setGameTimer(timer_countdown);
        
		timer_countdown.schedule(new TimerTask() {
			@Override
			public void run() {
				if(getGameLengthInSeconds() > 0) {
					setGameLengthInSeconds(getGameLengthInSeconds() - 1);
					getDisplay().updateLabel(lab, String.valueOf(getGameLengthInSeconds()));
				}
				else {
					timer_countdown.cancel();
					game_length_in_seconds = full_time;
					Platform.runLater(() -> {endGame();});
				}
				
			}
		} , 1000, 1000);
		
		
	}
	
    
    /**
     * Validate a typed word, ie compares it to the word to type <br>
     * 
     * @param textArea
     * @param next_words_to_type_list
     * @param words_typed_list
     */
    protected void validateWord (StyleClassedTextArea textArea, List<AbstractWord> next_words_to_type_list, List<AbstractWord> words_typed_list) {
    	if(next_words_to_type_list.isEmpty()) {
    		return;
    	}
    	
    	int last_index = getLastIndex(words_typed_list);
    	String last_word_typed = last_index == -1 ? "" : words_typed_list.get(last_index).getWord();
    	String word_to_type = next_words_to_type_list.get(0).getWord();
    	
    	
    	int nb_chars_to_skip;
    	
    	if (last_word_typed.equals(word_to_type)) {
    		nb_chars_to_skip = 0;
    		getDisplay().setStyle(textArea,  word_to_type.length(), "correctWord");
    	}
    	
    	else {
    		if(hasAdditionalChars(last_word_typed, word_to_type)) {
        		nb_chars_to_skip = 0;
        		getDisplay().setStyle(textArea,  last_word_typed.length(), "incorrectWord");
    		}
        	else {
        		nb_chars_to_skip = word_to_type.length() - last_word_typed.length();
        		getDisplay().setStyle(textArea,  last_word_typed.length(), "incorrectWord");
        	}
    	}
    	
    	    	
    	textArea.moveTo(textArea.getCaretPosition() + nb_chars_to_skip +1);
    	words_typed_list.add(getFactory().createWord("", WordColor.DEFAULT));
   	
    }
    
    /**
     * To return to a previous char <br>
     * Normal Mode exclusive : allows returning to previously validated words <br>
     *
     * @param textArea
     * @param words_typed
     * @param next_words_to_type
     * @param validated_words
     * @return
     */
    protected boolean backtrack(StyleClassedTextArea textArea, List<AbstractWord> words_typed,  List<AbstractWord> next_words_to_type,  List<AbstractWord> validated_words) {
		int last_typed_index = getLastIndex(words_typed);
		if(last_typed_index == -1) {
			return false;
		}
		String last_typed = words_typed.get(last_typed_index).getWord();
		String next_to_type = next_words_to_type.get(0).getWord();
		
		int actual_pos = textArea.getCaretPosition();
		
		//go to previous AbstractWord
		if(last_typed.isEmpty()) {
			words_typed.remove(last_typed_index);
			if (last_typed_index == 0) {
				textArea.moveTo(0);
				return true;
			}

			String last_validated = validated_words.get(validated_words.size() - 1).getWord();
			
			//char_in_string_index = words_typed.get(last_typed_index - 1).length();
			int difference = words_typed.get(last_typed_index - 1).getWord().length() - last_validated.length();
			if(difference >= 0) {
				textArea.moveTo(actual_pos-1);
			}
			else {
				textArea.moveTo(actual_pos-1+difference);
			}
			return true;
		}
		//move back 1 char
		else {
			if(hasAdditionalChars(last_typed, next_to_type)) {
				textArea.deleteText(actual_pos-1, actual_pos);
				
			}
			else {
				if(wasUsefulChar(last_typed, next_to_type)) {
					useful_chars--;
				}
				getDisplay().colourChar(textArea, "DEFAULT" , Direction.BACKWARDS);
				
			}
			
			textArea.moveTo(actual_pos-1);
			words_typed.set(last_typed_index, getFactory().createWord(last_typed.substring(0, last_typed.length()-1 ), WordColor.DEFAULT));
			return false;
		}
	}
      
    private Label createNameLabel(int stage_width) {
		var nameArea = new Label(getName());
        nameArea.setMaxWidth(stage_width);
        nameArea.setAlignment(Pos.CENTER);
		return nameArea;
	}
    
    private boolean wasUsefulChar(String word_typed, String next_word_to_type) {
    	int index = word_typed.length() - 1;
		return word_typed.charAt(index) == next_word_to_type.charAt(index);
	}

	
    
    private boolean charTypedIsAdditional(List<AbstractWord> words_typed, List<AbstractWord> next_words_to_type) {
    	
    	int last_index = getLastIndex(words_typed);
    	if(last_index == -1) {
    		return false;
    	}
    	return words_typed.get(last_index).getWord().length() >= next_words_to_type.get(0).getWord().length();
    }
    
    private boolean hasAdditionalChars(String word_typed, String word_to_type) {
    	return word_typed.length() > word_to_type.length();
    }
       
    private void addCharToTyped(List<AbstractWord> words_typed, String ch) {
    	String word_to_add = "";
    	int idx = getLastIndex(words_typed);
    	if(idx >= 0) {
    		int last_word = words_typed.size()-1;
			word_to_add = words_typed.get(last_word).getWord()+ch;
			words_typed.remove(last_word);
		}
		else {
			word_to_add = ch;
		}
		words_typed.add(getFactory().createWord(word_to_add, WordColor.DEFAULT));
	}

	protected void advanceNextWordsList (List<AbstractWord> next_words_to_type, List<AbstractWord> validated_words, StringTokenizer token) {
		if (!next_words_to_type.isEmpty()) {
			var validated = next_words_to_type.remove(0);
			validated_words.add(validated);
		}
		
		if(token.hasMoreTokens()) {
			var word = getFactory().createWord(token.nextToken(), getGameModeWordColor());
			colorWord(getGameTextArea(), word.getWord(), word.getColor(), next_words_to_type, next_words_to_type);
			addNextWord(next_words_to_type, word);
		}
	}
	
	private void initializeFirstWords(StyleClassedTextArea game_area, List<AbstractWord> next_words_to_type, StringTokenizer token) {
		for (int i =0; i<3 ; i++) {
			if(token.hasMoreTokens()) { 
				var word = getFactory().maybeColoredWord(token.nextToken(), getGameModeWordColor());
				colorWord(game_area, word.getWord(), word.getColor(), next_words_to_type, next_words_to_type);
				addNextWord(next_words_to_type, word);
				
			}
        }
	}
		
	
	private void addNextWord(List<AbstractWord> next_words_to_type, AbstractWord word) {
		next_words_to_type.add(word);
	}
	
	
	private void stepBackNextWordsList (List<AbstractWord> next_words_to_type, List<AbstractWord> validated_words) {
		int last_validated_index = validated_words.size() - 1;
		next_words_to_type.add(0, validated_words.get(last_validated_index));
		validated_words.remove(last_validated_index);
	}
		
	public ArrayList<Node> createComponents(Stage primaryStage, String text, List<AbstractWord> words_typed,
			List<AbstractWord> next_words_to_type, List<AbstractWord> validated_words, StringTokenizer token, VBox root) {
		var label = createNameLabel(stage_width);
		var textArea = createTextArea(primaryStage, text, words_typed, next_words_to_type, validated_words, token, root);
		
		
		var list = new ArrayList<Node>();
		list.addAll(List.of(label, textArea));
		return list;
	}

	public void colorWord (StyleClassedTextArea textArea, String word, WordColor color ,List<AbstractWord> next_words_to_type, List<AbstractWord> typed_words) {
		int pos = textArea.getCaretPosition();
		int idTyped = getLastIndex(typed_words);
		int idNext = getLastIndex(next_words_to_type);
		
		AbstractWord typed = idTyped == -1 ? new Word("") : typed_words.get(idTyped);
		AbstractWord next = idNext == -1 ? new Word("") : next_words_to_type.get(idNext);

		int offset = next.getWord().length() - typed.getWord().length();
		if (pos == 0) offset =0;
		int tmp = 0;
		
		for (var w : next_words_to_type) {
			tmp += w.getWord().length() +1;
		}
			
		tmp -= offset;
		
		final int nb_chars_to_skip = tmp;
		display.changeColor(textArea, pos + nb_chars_to_skip, word.length() , color);
		
		textArea.moveTo(pos);
	}
	
	/**
	 * Create the game area, and add events listeners
	 * @param primaryStage
	 * @param text
	 * @param words_typed
	 * @param next_words_to_type
	 * @param validated_words
	 * @param token
	 * @param root
	 * @param stage_width
	 * @return
	 */
	protected StyleClassedTextArea createTextArea(Stage primaryStage, String text, List<AbstractWord> words_typed,
			List<AbstractWord> next_words_to_type, List<AbstractWord> validated_words, StringTokenizer token, VBox root) {
		
		StyleClassedTextArea textArea = new StyleClassedTextArea();
        textArea.replaceText(text);
        
        textArea.addEventFilter(MouseEvent.ANY, e -> {e.consume();}); 

      
        textArea.addEventFilter(KeyEvent.KEY_PRESSED, gameHandlerKeyPressed(words_typed, next_words_to_type, validated_words, token, textArea));
        
        textArea.addEventFilter(KeyEvent.KEY_RELEASED, gameHandlerKeyReleased(primaryStage, words_typed, next_words_to_type, root, textArea));
       
        textArea.addEventFilter(KeyEvent.KEY_TYPED, e-> {
        	setKeysPressed(getKeysPressed() + 1);
        	e.consume();
        } );
        
        textArea.setMaxWidth(stage_width);
        textArea.setPrefHeight(stage_heigth);
        textArea.setWrapText(true);
        textArea.moveTo(0);
        
        setGameTextArea(textArea);
        
		return textArea;
	}


	private EventHandler<KeyEvent> gameHandlerKeyPressed(List<AbstractWord> words_typed, List<AbstractWord> next_words_to_type,
			List<AbstractWord> validated_words, StringTokenizer token, StyleClassedTextArea textArea) {
			return new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent e) {
					switch(e.getCode()) {
	        		case SPACE : {
	        			validateWord(textArea, next_words_to_type, words_typed);
	        			advanceNextWordsList(next_words_to_type, validated_words, token);        			
	        			break;
	        		}
	        		case BACK_SPACE: {
	        			var went_to_previous_word = backtrack(textArea, words_typed, next_words_to_type, validated_words);
	    				if (went_to_previous_word)	stepBackNextWordsList(next_words_to_type, validated_words);
	        			break;
	        		}
	        		default: break;
	        		
	        	}
	        	e.consume();
	       
				}
			};
        	
	}
	
/**
 * To start the timer/typing... <br>
 * To see game creation, refer to initialize game
 */
	private void startGame(VBox root) {
		setStarted(true);
		startTimer(root);
	}
	
	protected EventHandler<KeyEvent> gameHandlerKeyReleased(Stage primaryStage, List<AbstractWord> words_typed, List<AbstractWord> next_words_to_type,
			VBox root, StyleClassedTextArea textArea) {
		  	
		return new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				//words_typed.forEach(w -> {System.out.println(w.getWord());});
				//next_words_to_type.forEach(w -> {System.out.println(w.getWord());});
				if(!hasStarted()) {
					startGame(root);	        		
	        	}
	        	
	        	if (!isValidKey(e)){
	        		e.consume();
	        		return;
	        	}

	        	if (next_words_to_type.isEmpty()) {
	        		endGame();
	        	}
	        	
	        	//String word_to_type = next_words_to_type.get(0);
	        	var char_typed = e.getText();
	        	
	        	//int index= get_last_index(validated_words)
	        	if(charTypedIsAdditional(words_typed, next_words_to_type)) {
	        		display.insertToTextArea(textArea, char_typed);
	        	}
	        	
	        	String alreadyTyped = words_typed.isEmpty() ? "" :  words_typed.get(getLastIndex(words_typed)).getWord();
	        	String wordToType =  next_words_to_type.get(0).getWord();

	        	if (isRightChar(char_typed, alreadyTyped, wordToType)) {
	        		useful_chars++;
	    			getDisplay().colourChar(textArea, "GREEN", Direction.FORWARD);
				}
				else {
					getDisplay().colourChar(textArea, "RED", Direction.FORWARD);
				}
	    		
	        	addCharToTyped(words_typed, char_typed);
				textArea.moveTo(textArea.getCaretPosition() +1);
	        	e.consume();        
				
			}

			
		  
		  
		};
        
	}

	
	
	
	/**
	 * Manages the game creation <br>
	 * Creates components to display, data structures, initialize first words, and displays the game
	 */
    public void initializeGame(Stage primaryStage) {
		
    	VBox root = new VBox();
    	setFactory(new  WordsFactory());   
        setDisplay(new GameDisplay());
        
        List<AbstractWord> words_typed = new ArrayList<>();
        setTyped(words_typed);
        
        List<AbstractWord> next_words_to_type = new ArrayList<>();
        setNexts_words(next_words_to_type);
        
        List<AbstractWord> validated_words = new ArrayList<>();
        
        
        
        var token = new StringTokenizer(text, " ");
       
        
        var components = createComponents(primaryStage, text, words_typed, next_words_to_type,
				validated_words, token, root);
             
       
        initializeFirstWords(getGameTextArea(), getNexts_words(), token);
        components.forEach(e -> root.getChildren().add(e));
        

        getDisplay().displayGame(primaryStage, root, stage_width, stage_heigth);
        
        primaryStage.setOnCloseRequest(t -> {closeProperly(t);});
    }

    public EventHandler<WindowEvent> closeProperly(WindowEvent t) {
    	return new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				if(getGameTimer() != null) {
					getGameTimer().cancel();
				}
		    	Platform.exit();
				
			}
		
    	}; 
    	
    }
        
	@Override
	public void endGame() {
		if(getGameTimer() != null) {
			getGameTimer().cancel();
		}
		var stats = new GameStats(useful_chars, getKeysPressed(), game_length_in_seconds);
		ResultScreen results = new ResultScreen();
		results.show_results(getActualStage(), stats);
	}
    
	
}
