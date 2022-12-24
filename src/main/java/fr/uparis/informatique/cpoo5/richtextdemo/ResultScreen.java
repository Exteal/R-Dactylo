package fr.uparis.informatique.cpoo5.richtextdemo;

import org.fxmisc.richtext.StyleClassedTextArea;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ResultScreen {
	
	public void show_results(Stage stage, GameStats stats) {
		VBox root = new VBox();
		
		StyleClassedTextArea speed_area = new StyleClassedTextArea();
		StyleClassedTextArea accuracy_area = new StyleClassedTextArea();
		StyleClassedTextArea consistency_area = new StyleClassedTextArea();
		
		speed_area.replaceText("Speed : " + stats.getSpeed());
		accuracy_area.replaceText("Accuracy : " + stats.getAccuracy());
		consistency_area.replaceText("Consistency : " + stats.getConsistency());
		
		StyleClassedTextArea return_home = new StyleClassedTextArea();
		return_home.replaceText("Return home");
		
		return_home.setOnMouseClicked(e -> {
			var home = new Home();
			try {home.start(stage);} 
			catch (Exception e1) {e1.printStackTrace();}
		});
		
		return_home.setOnMouseEntered(e -> {
			return_home.setStyleClass(0, "Return home".length(), "item_selected");
			e.consume();
		});
		
		return_home.setOnMouseExited(e -> {
			return_home.clearStyle(0, "Return home".length());
			e.consume();
		});
		
		root.getChildren().add(speed_area);
		root.getChildren().add(accuracy_area);
		root.getChildren().add(consistency_area);
		root.getChildren().forEach(child -> {
			child.addEventFilter(KeyEvent.ANY,  e -> {e.consume();});
		});

		
		
		root.getChildren().add(return_home);

		
		Scene scene = new Scene(root, 500, 150);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		stage.setScene(scene);
	}
}
