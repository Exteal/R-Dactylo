package Words;

import fr.uparis.informatique.cpoo5.Game.PlaygroundMode;

/**
 * Blue word to regain lifes <br>
 * @see PlaygroundMode
 * @author rh4
 *
 */
public class BlueWord extends AbstractWord {

	
	public BlueWord(String s) {
		super(s);		
	}

	@Override
	public WordColor getColor() {
		return WordColor.BLUE;
	}

}
