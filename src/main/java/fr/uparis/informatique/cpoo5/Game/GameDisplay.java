package fr.uparis.informatique.cpoo5.Game;

import org.fxmisc.richtext.StyleClassedTextArea;

import Words.WordColor;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class GameDisplay {

	public void changeColor(StyleClassedTextArea textArea, int from, int length, WordColor color) {
		Platform.runLater(() -> { textArea.setStyleClass(from, from + length + 1 , color.toString() );});
	}
	
	public void insertToTextArea(StyleClassedTextArea area, String word) {
		int pos = area.getCaretPosition();
		area.insertText(pos, word);
		area.moveTo(pos);
	}
	
	public void displayGame(Stage primaryStage, VBox root, int stageWidth, int stageHeigth) {
		Scene scene = new Scene(root, stageWidth , stageHeigth);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle("R-Dactylo");
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	public void updateLabel (Label lab, String value) {
		Platform.runLater(() -> {lab.setText(value);});
	}
	
	public void setStyle(StyleClassedTextArea area, int length, String style) {
		area.setStyleClass(area.getCaretPosition() - length, area.getCaretPosition(), style);

	}
	
	public void colourChar (StyleClassedTextArea textArea, String color, Direction direction) {
    	int char_in_text_index = textArea.getCaretPosition();
    	switch(direction) {
    		case FORWARD: textArea.setStyleClass(char_in_text_index, char_in_text_index+1, color ); break;
    		case BACKWARDS: textArea.setStyleClass(char_in_text_index-1, char_in_text_index, color);
    	}
    }
	
	public Popup displayPopup(Stage stage, String content, double XCoord, double YCoord) {
		Popup levelUpPopup = new Popup();
		var lab = new Label(content);
		
		levelUpPopup.getContent().add(lab);
		levelUpPopup.show(stage, XCoord, YCoord);

		return levelUpPopup;
	}
	
	public void hidePopup(Popup pop) {
		Platform.runLater(() -> {pop.hide();}); 
	}
	
}
