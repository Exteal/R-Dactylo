package Words;

/**
 * Base class for words
 * Contains word to type, an error made check
 * @author rh4
 *
 */
public abstract class AbstractWord {
	private String word;
	private boolean errorMade;
	
	public abstract WordColor getColor();
	
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}

	public boolean wasErrorMade() {
		return errorMade;
	}

	public void setErrorMade(boolean errorMade) {
		this.errorMade = errorMade;
	}
	
	public AbstractWord(String word) {
		setWord(word);
		setErrorMade(false);

	}
}
