package Words;

/**
 * A basic word
 * @author rh4
 *
 */
public class Word extends AbstractWord {

	public Word(String word) {
		super(word);
	}

	@Override
	public WordColor getColor() {
		return WordColor.DEFAULT;
	}

	@Override
	public boolean wasErrorMade() {
		return false;
	}
	
	
}
