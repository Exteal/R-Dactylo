package fr.uparis.informatique.cpoo5.richtextdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import org.fxmisc.richtext.StyleClassedTextArea;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public abstract class AbstractGame {
	
	private enum Direction {
		FORWARD,
		BACKWARDS;
	}
	
	
    private int char_in_string_index = 0;
    private boolean has_started = false;
    private int seconds_left = 30;
    
   //private int string_to_add_index = 0;
   // private int user_actual_string_index = 0;
	
    private void start_game(Stage stage, VBox root) {
        Label lab = new Label();
		lab.setText(String.valueOf(seconds_left));
		root.getChildren().add(0, lab);
		
        Timer timer_countdown = new Timer();
		timer_countdown.schedule(new TimerTask() {
			@Override
			public void run() {
				if(seconds_left > 0) {
					seconds_left--;
					Platform.runLater(() -> {lab.setText(String.valueOf(seconds_left));});
				}
				else {
					timer_countdown.cancel();
					Platform.runLater(() -> {end_game(stage);});
				}
				
			}
		} , 1000, 1000);
		
		
	}
	
    private void end_game(Stage stage) {
		var stats = new GameStats(10, 50, 30);
		ResultScreen results = new ResultScreen();
		results.show_results(stage, stats);
	}
	
    
    public void validate_word (StyleClassedTextArea textArea, List<String> next_words_to_type_list, List<String> words_typed_list) {
    	if(next_words_to_type_list.isEmpty()) {
    		return;
    	}
    	
    	int last_index = words_typed_list.size() == 0 ? 0 : words_typed_list.size()-1;
    	
    	int nb_chars_to_skip = words_typed_list.isEmpty() ? next_words_to_type_list.get(0).length() 
    			: next_words_to_type_list.get(0).length() - words_typed_list.get(last_index).length();
    	

    	textArea.moveTo(textArea.getCaretPosition() + nb_chars_to_skip +1);
    	words_typed_list.add("");
    	char_in_string_index = 0;
   	
    }
    
    private boolean backtrack(StyleClassedTextArea textArea, List<String> words_typed, List<String> next_words_to_type,  List<String> validated_words) {
		int last_typed_index = words_typed.size()-1;
		String last_typed = last_typed_index >= 0 ?  words_typed.get(last_typed_index) : "";
		int actual_pos = textArea.getCaretPosition();
		
		
		if(last_typed.isEmpty()) {
			//go to previous word
			
			//revoir cette condition : previously == 0
			if(last_typed_index <= 0) return false;
			words_typed.remove(last_typed_index);
			char_in_string_index = words_typed.get(last_typed_index - 1).length();
			int difference = words_typed.get(last_typed_index - 1).length() - validated_words.get(validated_words.size() - 1).length();
			if(difference >= 0) {
				textArea.moveTo(actual_pos-1);
			}
			else {
				textArea.moveTo(actual_pos-1+difference);
			}
			return true;
		}
		
		else {
			//move back 1 char
			colour_char(textArea, "black", Direction.BACKWARDS);
			textArea.moveTo(actual_pos-1);
			words_typed.set(last_typed_index, last_typed.substring(0, last_typed.length()-1 ));
			char_in_string_index--;
			return false;
		}
		
	}
      
    private boolean is_valid_key(KeyEvent e) {
		var code = e.getCode();
		return code.isLetterKey() ||code == KeyCode.EXCLAMATION_MARK || code == KeyCode.PERIOD;
	}
    
    public boolean is_additional_char(String word_to_type) {
    	return char_in_string_index >= word_to_type.length();
    }
    
    public boolean is_right_char(String typed, String word_to_type) {
    	if(char_in_string_index >= word_to_type.length()) {
    		return false;
    	}
    	
    	else return typed.equals(String.valueOf(word_to_type.charAt(char_in_string_index)));
    			
    }
     
    public void colour_char (StyleClassedTextArea textArea, String colour, Direction direction) {
  
    	int char_in_text_index = textArea.getCaretPosition();
    	switch(direction) {
    		case FORWARD: textArea.setStyleClass(char_in_text_index, char_in_text_index+1, colour ); break;
    		case BACKWARDS: textArea.setStyleClass(char_in_text_index-1, char_in_text_index, colour);
    	}
    	
		
		
    }
    
    private void add_char_to_typed(List<String> words_typed, String ch) {
    	String word_to_add = "";
    	if(words_typed.isEmpty()) {
			word_to_add = ch;
		}
		else {
			int last_word = words_typed.size()-1;
			word_to_add = words_typed.get(last_word)+ch;
			words_typed.remove(last_word);
		}
		words_typed.add(word_to_add);
	}


	private void advance_next_words_list (List<String> next_words_to_type, List<String> validated_words, StringTokenizer token) {
		if (!next_words_to_type.isEmpty()) {
			String validated = next_words_to_type.remove(0);
			validated_words.add(validated);
		}
		
		if(token.hasMoreTokens()) next_words_to_type.add(token.nextToken());
	}
	
	
	private void step_back_next_words_list (List<String> next_words_to_type, List<String> validated_words) {
		int last_validated_index = validated_words.size() - 1;
		next_words_to_type.add(0, validated_words.get(last_validated_index));
		validated_words.remove(last_validated_index);
	}
	
	
    public void start_game (Stage primaryStage) {

        String text = "Lorem ipsum dolor sit amet. ";
        		//+ "Eos minus voluptas qui ipsa animi est laudantium repellat"
        		//+ " quo excepturi itaque aut sunt possimus vel dolorum distinctio";
        
        
        List<String> words_typed = new ArrayList<>();
        List<String> next_words_to_type = new ArrayList<String>();
        List<String> validated_words = new ArrayList<String>();
        
        var token = new StringTokenizer(text, " ");
        for (int i =0; i<3 ; i++) {
        	next_words_to_type.add(token.nextToken());
        }
        
        
        VBox root = new VBox();
        
        StyleClassedTextArea textArea = new StyleClassedTextArea();
        textArea.replaceText(text);
             
        textArea.addEventFilter(MouseEvent.ANY, e -> {e.consume();}); 

        textArea.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
        	if(!has_started) {
        		start_game(primaryStage, root);
        		has_started = true;
        	}
        	
        	if (!is_valid_key(e)){
        		e.consume();
        		return;
        	}

        	if (next_words_to_type.isEmpty()) {
        		end_game(primaryStage);
        	}
        	
        	String word_to_type = next_words_to_type.get(0);
    		System.out.println(word_to_type);
        	var typed = e.getText();
        	if (is_right_char(typed, word_to_type)) {
    			colour_char(textArea, "green", Direction.FORWARD);
			}
			else {
				colour_char(textArea, "red", Direction.FORWARD);
			}
    		

        	add_char_to_typed(words_typed, typed);
        	char_in_string_index++;
			textArea.moveTo(textArea.getCaretPosition() +1);
        	e.consume();
    	
        	
        	
        });
        
        textArea.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
        	switch(e.getCode()) {
        		case SPACE: {
        			validate_word(textArea, next_words_to_type, words_typed);
        			advance_next_words_list(next_words_to_type, validated_words, token);        			
        			break;
        		}
        		case BACK_SPACE: {
        			var went_to_previous_word = backtrack(textArea, words_typed, next_words_to_type, validated_words);
    				if (went_to_previous_word)	step_back_next_words_list(next_words_to_type, validated_words);
        			break;
        		}
        		default: break;
        		
        	}
        	e.consume();
        });
        
        
        textArea.addEventFilter(KeyEvent.KEY_TYPED, e-> {
        	e.consume();
        } );
        

        textArea.setMaxWidth(500);
        textArea.setWrapText(true);
        textArea.displaceCaret(0);
        root.getChildren().add(textArea);

        Scene scene = new Scene(root, 500, 150);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setTitle("R-Dactylo");
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

}
