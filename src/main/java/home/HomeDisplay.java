package home;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeDisplay {
	public void displayMultiModeForm(VBox form) {				
		Stage stage = new Stage();
		Scene scene = new Scene(form);
       
		scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("Multiplayer");

        stage.showAndWait();
	}
}
