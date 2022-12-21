
package fr.uparis.informatique.cpoo5.richtextdemo;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.fxmisc.richtext.StyleClassedTextArea;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RichTextDemo extends Application {


    //private int string_to_add_index = 0;
    private int char_in_string_index = 0;
   // private int user_actual_string_index = 0;
	
    public void skip_word (StyleClassedTextArea textArea, List<String> words_to_type_list, List<String> words_typed_list) {
    	if (is_game_finished(words_to_type_list, words_typed_list)) return;
    	
    	/*if (has_remaining_word(words_to_type_list)) {
    		textArea.appendText(words_to_type_list.get(string_to_add_index++) + " ");
    	}
    	*/
    	
    	//user_actual_string_index++;
    	int last_index = words_typed_list.size() == 0 ? 0 : words_typed_list.size()-1;
    	//int additional_chars = words_typed_list.get(last_index).length() - words_to_type_list.get(last_index).length();
    	
    	System.out.println("totype : " + words_to_type_list.get(last_index));

    	System.out.println("typed : " + words_typed_list.get(last_index));
    	
    	int nb_chars_to_skip = words_to_type_list.get(last_index).length() - words_typed_list.get(last_index).length();
    	//if(additional_chars > 0) textArea.displaceCaret(textArea.getCaretPosition() + );
    	textArea.displaceCaret(textArea.getCaretPosition() + nb_chars_to_skip +1);
    	words_typed_list.add("");
        
    	char_in_string_index = 0;
    	
    	
    	
    }
    /*
    public void last_word() {
    	if(last_word.has_error()) {
    
    	}
    }
    */
    
    public boolean is_game_finished (List<String> words_to_type_list, List<String> words_typed) {
    	return words_typed.size() == words_to_type_list.size();
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
    /*public boolean has_remaining_word(List<String> words_to_type_list) {
    	return string_to_add_index < words_to_type_list.size() -1;
    }
    */
    /*public void colour_word (StyleClassedTextArea textArea, List<String> words_to_type_list, String colour) {
    	int idx,  char_in_text_index = 0;
		for(idx = 0; idx < user_actual_string_index; idx++) {
			char_in_text_index+= words_to_type_list.get(idx).length()+1;
		}
		int word_length = words_to_type_list.get(idx).length();
		textArea.setStyleClass(char_in_text_index, char_in_text_index+word_length+1, colour);
		
    }*/
    
    
    public void colour_char (StyleClassedTextArea textArea, List<String> words_typed_list, String colour) {
    	
    	int idx, char_in_text_index = 0;
		for(idx = 0; idx < words_typed_list.size(); idx++) {
			if (idx == words_typed_list.size()-1){
				char_in_text_index+= words_typed_list.get(idx).length();
			}
			else {
				char_in_text_index+= words_typed_list.get(idx).length()+1;
			}
		}
		textArea.setStyleClass(char_in_text_index, char_in_text_index+1, colour);
		
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

    
    @Override
    public void start(Stage primaryStage) throws Exception {

        String text = "Lorem ipsum dolor sit amet. "
        		+ "Eos minus voluptas qui ipsa animi est laudantium repellat"
        		+ " quo excepturi itaque aut sunt possimus vel dolorum distinctio";
        
        List<String> words_to_type_list = List.of(text.split(" "));
        List<String> words_typed = new ArrayList<>();
        
        /*var sb = new StringBuilder();
		sb.append(words_to_type_list.get(string_to_add_index++)).append(" ");
		sb.append(words_to_type_list.get(string_to_add_index++)).append(" ");
		sb.append(words_to_type_list.get(string_to_add_index++)).append(" ");
        */
        VBox root = new VBox();
        
        StyleClassedTextArea textArea = new StyleClassedTextArea();
        textArea.replaceText(text);
        
       

        textArea.setOnKeyReleased(e -> {e.consume();});
        
        textArea.setOnKeyPressed(e -> {
        	if (e.getCode() == KeyCode.SPACE) skip_word(textArea, words_to_type_list, words_typed);
        	e.consume();
        });
        
        
        textArea.addEventFilter(KeyEvent.KEY_TYPED, e-> {
        	if (e.getCharacter().equals(" ")){
        		e.consume(); return;
        	}
        	
        	if (is_game_finished(words_to_type_list, words_typed)) {
        		
        		var stats = new GameStats(10, 50, 30);
        		ResultScreen results = new ResultScreen(stats);
        		primaryStage.setScene(results.getScene());
        		e.consume();
        		return;
        	}
        	
        	int actual_word_index;
        	if (words_typed.isEmpty()) {
        		actual_word_index = 0;
        	}
        	else {
        		actual_word_index = words_typed.size() - 1;
        	}
        	String word_to_type = words_to_type_list.get(actual_word_index);
        	
    		//var char_in_textarea = words_to_type_list.get(user_actual_string_index).charAt(char_in_string_index);
			
    		//word typed len == word to type len
    	
    		/*if(char_in_string_index == words_to_type_list.get(user_actual_string_index).length()) {
    			
    		}*/
    		
        	
        	
        	if (is_right_char(e.getCharacter(), word_to_type)) {
    			colour_char(textArea, words_typed, "green");
			}
			else {
				colour_char(textArea, words_typed, "red");
			}
    		
        	add_char_to_typed(words_typed, e.getCharacter());
        	char_in_string_index++;
			textArea.displaceCaret(textArea.getCaretPosition() +1);
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
	
    public static void main(String[] args) {
        Application.launch(args);
    }

}

