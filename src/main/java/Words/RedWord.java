package Words;

import fr.uparis.informatique.cpoo5.Game.MultiMode;

/**
 * Red words to send to other players
 * @see MultiMode
 * @author rh4
 *
 */
public class RedWord extends AbstractWord {

	public RedWord(String word) {
		super(word);
	}

	@Override
	public WordColor getColor() {
		return WordColor.RED;
	}

}
