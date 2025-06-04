package view;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMakerBase;
import org.fife.ui.rsyntaxtextarea.TokenTypes;

public class CustomSqlSyntaxHighlighting extends TokenMakerBase {

    public Token getTokenList(char[] text, int start, int end, int initialTokenType) {
    	
    	
        resetTokenList();

        int offset = start;
        while (offset < end) {
            char c = text[offset];
            if (Character.isWhitespace(c)) {
                addWhitespaceToken(text, offset, end - offset);
                offset = end;
            } else if (Character.isLetter(c)) {
                int startOffset = offset;
                while (offset < end && (Character.isLetter(text[offset]) || text[offset] == '_')) {
                    offset++;
                }
                String word = new String(text, startOffset, offset - startOffset);
                 if (word.equalsIgnoreCase("show") || word.equalsIgnoreCase("tables") || word.equalsIgnoreCase("databases")) {
                    addToken(text, startOffset, offset - startOffset, TokenTypes.RESERVED_WORD, 0);
                } else {
                    addToken(text, startOffset, offset - startOffset, TokenTypes.IDENTIFIER, 0);
                }
            } else {
                offset++;
            }
        }

        return firstToken;
    }

	@Override
	public Token getTokenList(Segment arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Adds a whitespace token to the token list.
	 *
	 * @param text The text being parsed.
	 * @param start The start offset of the whitespace.
	 * @param length The length of the whitespace.
	 */
	protected void addWhitespaceToken(char[] text, int start, int length) {
	    addToken(text, start, length, TokenTypes.WHITESPACE, 0);
	}
}

