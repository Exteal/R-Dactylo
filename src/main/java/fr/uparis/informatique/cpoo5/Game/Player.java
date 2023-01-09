package fr.uparis.informatique.cpoo5.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import Words.Word;
import javafx.application.Platform;




/**
 * For communication between a Multiplayer mode instance and the server <br>
 * send words to server, and receives words from other players
 * @author rh4
 *
 */
public class Player extends Thread {
	private int PORT = 15001;
	private String player_name;
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private MultiMode game;
	
	public MultiMode getGame() {
		return game;
	}

	public void setGame(MultiMode game) {
		this.game = game;
	}

	public int getPORT() {
		return PORT;
	}

	public PrintWriter getWriter() {
		return writer;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

	public BufferedReader getReader() {
		return reader;
	}

	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

	public void setPlayerName(String name) {
		this.player_name = name;
	}

	public String getPlayerName() {
		return player_name;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}


	public Player(String s, String ip_address, MultiMode game) throws IOException {
		connectToServer(s, ip_address);  
		setGame(game);
		
	}
	
	public void run() {
		try {
			String line;
			while(true) {
				
				while((line = getReader().readLine()) != null) handleReceivedWord(line);
			}
		}
		catch(IOException e) {
			System.out.println("error in player thread + " + getPlayerName());
			e.printStackTrace();
		}
	}
	
	

	private void connectToServer(String name, String ip_address) throws IOException {
		
		setPlayerName(name);		
        Socket sock = new Socket(ip_address, PORT);
        setSocket(sock);
		BufferedReader server_reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        PrintWriter server_writer = new PrintWriter(sock.getOutputStream(), true);

        setReader(server_reader);
        setWriter(server_writer);
        getWriter().println(name);
         
	}
	
	protected void send_word_to_server(String word) {
		getWriter().println(getPlayerName() +":" +word);
	}
	
	protected void handleReceivedWord(String str) {	
		var rec =str.split(":", 2);
		if (rec[0].equals("server")) {
			switch(rec[1]) {
			case "start":
				getGame().setStarted(true);
				break;
				
			default:
				break;
			}
			
		}
		else {
			Platform.runLater(() -> { if (!getPlayerName().equals(rec[0])) getGame().manageWordReceived(new Word(rec[1]));});
		}
	}

}
