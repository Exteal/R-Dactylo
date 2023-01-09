package fr.uparis.informatique.cpoo5.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Server managing multiple Players
 * @author rh4
 *
 */
public class MultiModeServer extends Thread {
	private Map<Socket,String> playersMap;
	

	private ServerSocket serverSocket;
	private int PORT = 15001;
	
	
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	public void setServerSocket(ServerSocket servSoc) {
		this.serverSocket = servSoc;
	}
	
	public  Map<Socket, String> getPlayersMap() {
		return playersMap;
	}
	
	public MultiModeServer() throws IOException {
		playersMap = new HashMap<>();
		ServerSocket ssock = new ServerSocket(PORT);
		setServerSocket(ssock);
	    
	}

	public void run() {
		try {
			 int players = 0;
		    	while (true) {   	
		    		Socket sock = getServerSocket().accept();
		    		BufferedReader player_reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		    		
		    	    String name = player_reader.readLine();
		    		
		    	    
		    	    getPlayersMap().put(sock, name);
		    		
		    		players++;
		    		if(players >= 2) break;
		    	   
				}
		    	
	    		for (var soc : getPlayersMap().keySet()) {
	    			Thread message_handler =new ServerMessageHandler(getPlayersMap(), soc);
					message_handler.start();
	    		}
		    	    
		    	while (true) {
		    		boolean playerleft = false;
		    		for (var player : getPlayersMap().keySet()) {
		    			if(player.isClosed()) playerleft = true;
		    		}
		    		if(playerleft) {
		    			getServerSocket().close();
		    			return;
		    			
		    		}
		    		
		    	}
		}
		
		catch(IOException e) {
			e.printStackTrace();
		}
		    	
	}
	
}
