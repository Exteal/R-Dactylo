package fr.uparis.informatique.cpoo5.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

/**
 * Thread receiving word from a player and propagating it to others
 * @author rh4
 *
 */
public class ServerMessageHandler extends Thread {
	
	private Map<Socket, String> clientsMap;
	private Socket client;
	
	public void setMap(Map<Socket, String> map) {
		this.clientsMap = map;
	}
	
	public Map<Socket, String> getMap() {
		return clientsMap;
	}
	
	public ServerMessageHandler(Map<Socket, String> map, Socket s) {
		setMap(map);
		this.client = s;
		try {
			propagateWord("server", "start");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void propagateWord(String sender, String word) throws IOException {
		for (var s : getMap().keySet()) {
			if(!getMap().get(s).equals(sender)) {
				PrintWriter writer = new PrintWriter(s.getOutputStream(), true);
				writer.println(sender + ":" + word);
			}		
		}
	}
	
	@Override
	public void run() {
		BufferedReader read = null;
		
        try {
        	read = new BufferedReader(new InputStreamReader(client.getInputStream()));
        }
        
        catch(IOException e) {
        	try {
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        	return;
        }
        
        String line;
		while (true) {
			try {
				while((line = read.readLine()) != null) {
					String[] arr = line.split(":",2);
					propagateWord(arr[0], arr[1]);
				}
				
				
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
