
package fr.uparis.informatique.cpoo5.Game;

import Words.WordColor;

/** The basic mode <br>
 * No specific rules
 * @author rh4
 *
 */
public class NormalMode extends AbstractGame {
    
	public String getName() {
		return "Normal";
	}

	
		
	
	@Override
	public WordColor getGameModeWordColor() {
		return WordColor.DEFAULT;
	}
	
}

