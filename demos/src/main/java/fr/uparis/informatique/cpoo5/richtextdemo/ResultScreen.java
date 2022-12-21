package fr.uparis.informatique.cpoo5.richtextdemo;

import org.fxmisc.richtext.StyleClassedTextArea;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class ResultScreen {
	private Scene scene;
	
	public Scene getScene() {
		return scene;
	}
	
	public ResultScreen(GameStats stats) {
		VBox root = new VBox();
		
		StyleClassedTextArea speed_area = new StyleClassedTextArea();
		StyleClassedTextArea accuracy_area = new StyleClassedTextArea();
		StyleClassedTextArea consistency_area = new StyleClassedTextArea();
		
		speed_area.replaceText("Speed : " + stats.getSpeed());
		accuracy_area.replaceText("Accuracy : " + stats.getAccuracy());
		consistency_area.replaceText("Consistency : " + stats.getConsistency());
		
		
		root.getChildren().add(speed_area);
		root.getChildren().add(accuracy_area);
		root.getChildren().add(consistency_area);
		
		scene = new Scene(root, 500, 150);
	}
}
