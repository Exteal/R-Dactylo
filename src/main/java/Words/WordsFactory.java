package Words;

import java.util.Random;
/**
 * Factory for words
 * Can instanciate words by color, or add radomness
 * @author rh4
 *
 */
public class WordsFactory {
	
	int chances = 20;
	
	
	public AbstractWord createWord(String word, WordColor color) {
		return switch(color) {
			case BLUE ->    new BlueWord(word);
			case RED ->     new RedWord(word);
		default -> new Word(word);
		};
	}
	
	public AbstractWord maybeColoredWord(String word, WordColor color) {
		int percent = new Random().nextInt(100)+1;
		AbstractWord ret = percent >= chances ?  createWord(word, color) : createWord(word, WordColor.DEFAULT);
		return ret;
		
	}
}
