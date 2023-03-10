package fr.uparis.informatique.cpoo5.Game;

/**
 * Player stats after the game
 * @author rh4
 *
 */
public class GameStats {
	private double speed;
	private double accuracy;
	private double consistency;
	
	public GameStats(int useful_chars, int keys_pressed, double time) {
		speed = (useful_chars* 60)/(time*5) ;
		accuracy = ((double)useful_chars / keys_pressed)*100;
		consistency = 2;
	}

	public double getSpeed() {
		return speed;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public double getConsistency() {
		return consistency;
	}
}
