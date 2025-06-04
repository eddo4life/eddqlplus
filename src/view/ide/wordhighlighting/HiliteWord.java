package view.ide.wordhighlighting;

public class HiliteWord {

	    int _position;  
	    String _word;

	    public HiliteWord(String word, int position) {
	        _position = position;   
	        _word = word;
	    }
	    public String getWord() {
	    	return _word;
	    }
}
