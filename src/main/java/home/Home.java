package home;

import java.io.IOException;
import java.net.InetAddress;

import fr.uparis.informatique.cpoo5.Game.Game;
import fr.uparis.informatique.cpoo5.Game.GameFactory;
import fr.uparis.informatique.cpoo5.Game.GameModes;
import fr.uparis.informatique.cpoo5.Game.MultiModeServer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Home page of the application<br>
 * To select a game mode
 * @author rh4
 *
 */
public class Home extends Application{
	private HomeDisplay display;
	
	public HomeDisplay getDisplay() {
		return display;
	}


	public void setDisplay(HomeDisplay display) {
		this.display = display;
	}


	public static void main(String[] args) throws Exception {
		Application.launch(args);
    }

	
	public Button create_menu_item(Stage stage, GameModes mode,  String name) {
		Button item = new Button(name);
				
		item.setOnMouseClicked(e -> {
				try {
					var game = GameFactory.createGame(mode, null);
					game.initializeGame(stage);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			
		});
		
		item.setOnMouseEntered(e -> {
			item.getStyleClass().removeIf(style -> style.equals("itemNotSelected"));
			item.getStyleClass().add("itemSelected");
			e.consume();
		});
		
		item.setOnMouseExited(e -> {
			item.getStyleClass().removeIf(style -> style.equals("itemSelected"));
			item.getStyleClass().add("itemNotSelected");

			e.consume();
		});
		
		item.getStyleClass().add("defaultButton");
		item.getStyleClass().add("itemNotSelected");
		return item;
	}
	
	public Button create_menu_item_multi(Stage stage, GameModes mode,  String name) {
		Button item = new Button(name);
			
		item.setOnMouseClicked(e -> {
			getDisplay().displayMultiModeForm(createMultiModeForm());		
		});
		
		item.setOnMouseEntered(e -> {
			item.getStyleClass().removeIf(style -> style.equals("itemNotSelected"));
			item.getStyleClass().add("itemSelected");
			e.consume();
		});
		
		item.setOnMouseExited(e -> {
			item.getStyleClass().removeIf(style -> style.equals("itemSelected"));
			item.getStyleClass().add("itemNotSelected");
			e.consume();
		});
		
		item.getStyleClass().add("itemNotSelected");
		return item;
	}
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		VBox root = new VBox();
		setDisplay(new HomeDisplay());
		root.getStyleClass().add("defaultStyle");
		
		var normal = create_menu_item(primaryStage, GameModes.NORMAL , "Normal");
		var playground = create_menu_item(primaryStage, GameModes.PLAYGROUND , "PlayGround");
		var multi = create_menu_item_multi(primaryStage, GameModes.MULTI , "Multi");
		
		
		root.getChildren().add(normal);
		root.getChildren().add(playground);
		root.getChildren().add(multi);
		Scene scene = new Scene(root, Game.stage_width, Game.stage_heigth);
		scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.setTitle("R-Dactylo");
		primaryStage.show();
	}

	
	/**
	 * Creates form to enter multiplayer mode : <br>
	 * Name and IP fileds must be filled <br>
	 * Name cannot contain ':' and cannot be 'server' <br>
	 * If Ip is local host, a multiplayer server will start if not disponible  <br>
	 * @return
	 */
	private VBox createMultiModeForm() {
		var box = new VBox();
		box.getStyleClass().add("defaultStyle");
		
		var name = new TextField();
		var ip = new TextField();
	
		name.setPromptText("Enter your  name.");
		ip.setPromptText("Enter the server address.");
		
		
		var submit = new Button("Submit");
		
		submit.setOnAction( e -> {
			
			
									
				if(name.getText() == null || name.getText().isEmpty() 
						 ||  name.getText().contains(":") || name.getText().equals("server")) {
					name.setPromptText("Name is not valid.");
					name.clear();
					return;
				}
				
				else {
					if(ip.getText() != null && !ip.getText().isEmpty()) {
						try {
							var localhost = InetAddress.getLocalHost().toString().split("/")[0];
							if (ip.getText().equals("localhost") || ip.getText().equals(localhost)) {
								
									try {
										
										MultiModeServer serv = new MultiModeServer();
										serv.start();
										System.out.println("NEW SERV");
									}
									catch (Exception servcreat) {

									}
									
									GameFactory.createGame(GameModes.MULTI, new String[] {name.getText(), ip.getText()});
									var form =  (Stage) submit.getScene().getWindow();
									form.close();
								
							}
							
							else {
								GameFactory.createGame(GameModes.MULTI, new String[] {name.getText(), ip.getText()});
								var form =  (Stage) submit.getScene().getWindow();
								form.close();
							}
							
						} 
						catch (IOException e1) {
							e1.printStackTrace();
							ip.setPromptText("Could not reach the specified server.");
							ip.clear();
						}	
					}
					
					else {
						ip.setPromptText("Must enter an ip address.");
						ip.clear();
						return;
					}
				
					
			}	
			
		
	} );
		
		
		box.getChildren().add(name);
		box.getChildren().add(ip);
		box.getChildren().add(submit);
		return box;
		
	}
	
	
}
