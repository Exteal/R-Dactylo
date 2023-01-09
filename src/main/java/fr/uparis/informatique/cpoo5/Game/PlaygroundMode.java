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
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Playground mode <br>
 * Every incorrect validated word costs 1 life <br>
 * Correctly validate a blue word without error to gain 1 life
 * @author rh4
 *
 */
public class PlaygroundMode extends AbstractGame {
	
	//number of words before each level up
	private final int WORDS_COUNT_TO_LVL_UP = 10;
	
	//initial lifes
	private int lifes_number = 3;
	
	private int until_level_up = WORDS_COUNT_TO_LVL_UP;
	
	private double level = 1;
	private Timer add_words_timer;
	

	public double getLevel() {
		return level;
	}

	public void setLevel(double level) {
		this.level = level;
	}

	public Timer getAddWordsTimer() {
		return add_words_timer;
	}

	@Override
	public WordColor getGameModeWordColor() {
		return WordColor.BLUE;
	}	
	
	public void setAddWordsTimer(Timer add_words_timer) {
		this.add_words_timer = add_words_timer;
	}

	public String getName() {
		return "PlayGround";
	}
	
	public long get_time_between_words() {
		long sec = 1000;
		return (long) (sec * 3 * (Math.pow(0.9,getLevel())));
	}
	
	public void validate_word (StyleClassedTextArea textArea, List<AbstractWord> next_words_to_type_list, 
			List<AbstractWord> words_typed_list, StringTokenizer token, Label lifes) {
		
		int last_index = getLastIndex(words_typed_list);
		AbstractWord typed = last_index == -1 ? new Word("") : words_typed_list.get(last_index);
		AbstractWord to_type = next_words_to_type_list.get(0);
    	
		
		
		super.validateWord(textArea, next_words_to_type_list, words_typed_list);
		
		if (to_type.getColor() == WordColor.BLUE && !to_type.wasErrorMade()  && typed.getWord().equals(to_type.getWord())) {
			lifes.fireEvent(new LifeGainedEvent(LifeGainedEvent.LIFE_GAINED));	
		}
		
		
		if(typed.getWord().equals(to_type.getWord())) {
			if(until_level_up > 0) {
				until_level_up--;
			}
			else {
				level_up(getGameTimer(), next_words_to_type_list, token, textArea, words_typed_list);
				until_level_up = WORDS_COUNT_TO_LVL_UP;
			}
		}
		else {
			lifes.fireEvent(new LifeLostEvent(LifeLostEvent.LIFE_LOST));
		}
		
	}
	
	/**
	 *  backtrack to a previously validated word not allowed
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
	 * adds a label to display lifes number
	 */
	@Override
	public ArrayList<Node> createComponents(Stage primaryStage, String text, List<AbstractWord> words_typed,
			List<AbstractWord> next_words_to_type, List<AbstractWord> validated_words, StringTokenizer token, VBox root,
			int stage_width) {

		var components = super.createComponents(primaryStage, text, words_typed, next_words_to_type, validated_words, token, root, stage_width);

		var lifes_label = createLifesLabel(primaryStage, token);
		components.add(lifes_label);
		
		
		var updatedTextArea = create_text_area(primaryStage, text, words_typed, next_words_to_type, validated_words, token, root, stage_width, lifes_label) ;
        setGameTextArea(updatedTextArea);
        
		int gameAreaIdx = 1;
		components.remove(gameAreaIdx);
		components.add(gameAreaIdx, updatedTextArea);
		
		
		return components;
	}

	private Label createLifesLabel(Stage primaryStage, StringTokenizer token) {
		Label lifes_label = new Label("Lifes : " + lifes_number);
		
		lifes_label.addEventFilter(LifeLostEvent.LIFE_LOST , e -> {
			lifes_number--;
			if(lifes_number > 0) Platform.runLater(() -> { lifes_label.setText("Lifes : " + String.valueOf(lifes_number)); });
			else {
				while (token.hasMoreTokens()) token.nextToken();
				endGame();
			}
		});
		
		lifes_label.addEventFilter(LifeGainedEvent.LIFE_GAINED , e -> {
			lifes_number++;
			Platform.runLater(() -> { lifes_label.setText("Lifes : " + String.valueOf(lifes_number)); });		
		});
		
		return lifes_label;
	}
	
	public StyleClassedTextArea create_text_area(Stage primaryStage, String text, List<AbstractWord> words_typed,
			List<AbstractWord> next_words_to_type, List<AbstractWord> validated_words, StringTokenizer token, VBox root,
			int stage_width, Label lifelabel) {
		
		StyleClassedTextArea textArea = new StyleClassedTextArea();
        textArea.replaceText(text);
             
        textArea.addEventFilter(MouseEvent.ANY, e -> {e.consume();});         
        textArea.addEventFilter(KeyEvent.KEY_PRESSED, handle_key_pressed(words_typed, next_words_to_type, validated_words, token, textArea, lifelabel) );
        textArea.addEventFilter(KeyEvent.KEY_RELEASED, handle_key_released(primaryStage, words_typed, next_words_to_type, root, textArea, token) );
        textArea.addEventFilter(KeyEvent.KEY_TYPED, e-> {
        	setKeysPressed(getKeysPressed() + 1);
        	e.consume();
        } );
        
        textArea.setMaxWidth(stage_width);
        textArea.setWrapText(true);
        textArea.moveTo(0);
		return textArea;
	}
	
	

	public EventHandler<KeyEvent> handle_key_pressed(List<AbstractWord> words_typed, List<AbstractWord> next_words_to_type,
			List<AbstractWord> validated_words, StringTokenizer token, StyleClassedTextArea textArea, Label lifes_label) {
		
        	return new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent e) {
					switch(e.getCode()) {
		        		case SPACE: {
		        			validate_word(textArea, next_words_to_type, words_typed, token, lifes_label);
		        			advanceNextWordsList(next_words_to_type, validated_words, token);        			
		        			break;
		        		}
		        		case BACK_SPACE: {
		        			backtrack(textArea, words_typed, next_words_to_type, validated_words);
		        			break;
		        		}
		        		default: break;
	        		
					}
	        	e.consume();
					
				}
			};

				

	}

	
	public EventHandler<KeyEvent> handle_key_released(Stage primaryStage, List<AbstractWord> words_typed,
			List<AbstractWord> next_words_to_type, VBox root, StyleClassedTextArea textArea, StringTokenizer token) {
		
		
			var fun = super.gameHandlerKeyReleased(primaryStage, words_typed, next_words_to_type, root, textArea);
			
			return new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					
						
					if(!hasStarted()) scheduleTask(next_words_to_type, token, textArea, words_typed);
					AbstractWord toType = next_words_to_type.get(0);
		        	String alreadyTyped = words_typed.isEmpty() ? "" :  words_typed.get(getLastIndex(words_typed)).getWord();
		        	
					if (isValidKey(event) && !toType.wasErrorMade() 
							&& !isRightChar(event.getText(), alreadyTyped, toType.getWord())) {
						
						toType.setErrorMade(true);
					}					
					fun.handle(event);
					
								
				}
			};
			

		
	}
	
	/**
	 * The timer adds word periodically, depends on the actual level <br>
	 * If words number exceeds the limit, forces a validation  <br>
	 * @param next_words_to_type
	 * @param token
	 * @param textArea
	 * @param typed_words
	 */
	private void scheduleTask(List<AbstractWord> next_words_to_type, StringTokenizer token, StyleClassedTextArea textArea, List<AbstractWord> typed_words) {
		var timer_countdown = new Timer();
		timer_countdown.schedule(new TimerTask() {
			@Override
			public void run() {
				if(!token.hasMoreTokens()) {
					timer_countdown.cancel();
					return;
				}
				if (WORDS_NUMBER_LIMIT > next_words_to_type.size()) {
					var word = getFactory().maybeColoredWord(token.nextToken(), WordColor.BLUE);
					colorWord(textArea, word.getWord(), word.getColor() ,next_words_to_type, typed_words);
					next_words_to_type.add(word);
				}
				else {
					Platform.runLater(() -> {textArea.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, null, null, KeyCode.SPACE, false, false, false, false));}); 
				}
			}
		} , get_time_between_words() , get_time_between_words());
		
		setAddWordsTimer(timer_countdown);
	}
	
	private void level_up(Timer add_words_timer, List<AbstractWord> next_words, StringTokenizer token, StyleClassedTextArea textArea, List<AbstractWord> typed_words) {
		setLevel(getLevel()+1);
		getAddWordsTimer().cancel();
		scheduleTask(next_words, token, textArea, typed_words);
		
		Popup levelUpPopup = new Popup();
		var speed = new Label("Level up!! \nSpeed : "+ get_time_between_words());
		levelUpPopup.getContent().add(speed);
		
		Point2D anchorPoint = textArea.localToScreen(
				textArea.getWidth(),
				0
		);
		
		levelUpPopup.show(getActualStage(), anchorPoint.getX(), anchorPoint.getY());
		
		var tim = new Timer();
		tim.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {levelUpPopup.hide();}); 
				tim.cancel();
			}
		}, 1000*2);
		
	}
	
	
	public void initializeFirstWords(List<AbstractWord> next_words_to_type, StringTokenizer token, StyleClassedTextArea area) {
		for (int i =0; i<3 ; i++) {
			var word = getFactory().maybeColoredWord(token.nextToken(), WordColor.BLUE);
			colorWord(area, word.getWord(), word.getColor() , next_words_to_type, List.of());
        	next_words_to_type.add(word);
        }
	}
	
	@Override
	public void initializeGame(Stage primaryStage) {
		super.initializeGame(primaryStage);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
    	    @Override
    	    public void handle(WindowEvent t) {
    	        getGameTimer().cancel();
    	        getAddWordsTimer().cancel();
    	    	Platform.exit();
    	    }
    	});
		
	}

	
}


