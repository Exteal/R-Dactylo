package fr.uparis.informatique.cpoo5.richtextdemo;


import java.lang.reflect.InvocationTargetException;

import org.fxmisc.richtext.StyleClassedTextArea;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Home extends Application{
	
	public static void main(String[] args) {
        Application.launch(args);
    }

	public StyleClassedTextArea create_menu_item(Stage stage, Class<? extends AbstractGame> mode,  String name) {
		StyleClassedTextArea item = new StyleClassedTextArea();
		
		item.replaceText(name);
		
		item.setOnMouseClicked(e -> {
			try {
				var game = mode.getDeclaredConstructor().newInstance();
				game.start_game(stage);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e1) {

			}			
		});
		
		item.setOnMouseEntered(e -> {
			item.setStyleClass(0, name.length(), "item_selected");
			e.consume();
		});
		
		item.setOnMouseExited(e -> {
			item.clearStyle(0, name.length());
			e.consume();
		});
		
		return item;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox root = new VBox();
		
		var normal = create_menu_item(primaryStage, NormalMode.class , "Normal");
		var playground = create_menu_item(primaryStage, PlaygroundMode.class ,"Playground");
		var multi = create_menu_item(primaryStage, MultiMode.class ,"Multi");
		
		root.getChildren().add(normal);
		root.getChildren().add(playground);
		root.getChildren().add(multi);
		
		Scene scene = new Scene(root, 500, 150);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
