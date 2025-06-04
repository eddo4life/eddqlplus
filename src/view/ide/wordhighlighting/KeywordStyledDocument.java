package view.ide.wordhighlighting;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class KeywordStyledDocument extends DefaultStyledDocument {
	private static final long serialVersionUID = 1L;
	static StyleContext styleContext = new StyleContext();
	private Style defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);
	public static int category = -1;
	static Style customStyle = styleContext.addStyle("ConstantWidth", null);

	public KeywordStyledDocument() {
	}

	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		super.insertString(offset, str, a);
		refreshDocument();
	}

	public void remove(int offs, int len) throws BadLocationException {
		super.remove(offs, len);
		refreshDocument();
	}

	private synchronized void refreshDocument() throws BadLocationException {
		String text = getText(0, getLength());
		final List<HiliteWord> list = processWords(text);

		setCharacterAttributes(0, text.length(), defaultStyle, true);
		for (HiliteWord word : list) {
			if (isDatatype(word.getWord())) {
				StyleConstants.setForeground(customStyle, Color.BLUE);
				StyleConstants.setBold(customStyle, true);
				int p0 = word._position;
				setCharacterAttributes(p0, word._word.length(), customStyle, true);
			} else if (isReservedWord(word.getWord())) {
				StyleConstants.setForeground(customStyle, Color.green);
				StyleConstants.setBold(customStyle, true);
				int p0 = word._position;
				setCharacterAttributes(p0, word._word.length(), customStyle, true);
			} else if (isDataTable(word.getWord())) {
				StyleConstants.setForeground(customStyle, Color.orange);
				StyleConstants.setBold(customStyle, true);
				int p0 = word._position;
				setCharacterAttributes(p0, word._word.length(), customStyle, true);
			} else if (isDeleting(word.getWord())) {
				StyleConstants.setForeground(customStyle, Color.red);
				StyleConstants.setBold(customStyle, true);
				int p0 = word._position;
				setCharacterAttributes(p0, word._word.length(), customStyle, true);
			} else if (isClearing(word.getWord())) {
				StyleConstants.setForeground(customStyle, new Color(0x626200));
				StyleConstants.setBold(customStyle, true);
				int p0 = word._position;
				setCharacterAttributes(p0, word._word.length(), customStyle, true);
			} else if (isAgreted(word.getWord())) {
				StyleConstants.setForeground(customStyle, new Color(51, 102, 255));
				StyleConstants.setBold(customStyle, true);
				int p0 = word._position;
				setCharacterAttributes(p0, word._word.length(), customStyle, true);
			} else if (isOperator(word.getWord())) {
				StyleConstants.setForeground(customStyle, new Color(204, 153, 0));
				StyleConstants.setBold(customStyle, true);
				int p0 = word._position;
				setCharacterAttributes(p0, word._word.length(), customStyle, true);
			}

		}
	}

	/*
	 * =============================================================================
	 * ==
	 */

	private static List<HiliteWord> processWords(String content) {
		content += " ";
		List<HiliteWord> hiliteWords = new ArrayList<HiliteWord>();
		int lstwspacePos = 0;
		String word = "";
		char[] data = content.toCharArray();

		for (int index = 0; index < data.length; index++) {
			char ch = data[index];
			if (!(Character.isLetterOrDigit(ch) || ch == '_')) {
				lstwspacePos = index;
				if (word.length() > 0) {
					if (isReservedWord(word) || isDatatype(word) || isDataTable(word) || isDeleting(word)
							|| isClearing(word) || isAgreted(word) || isOperator(word)) {
						hiliteWords.add(new HiliteWord(word, (lstwspacePos - word.length())));
					}

					word = "";
				}
			} else {
				word += ch;
			}
		}
		return hiliteWords;
	}

	/*
	 * =============================================================================
	 * ==
	 */

	private static final boolean isReservedWord(String word) {
		return (isSame(word, "CURRENT_DATE") || isSame(word, "CURRENT_TIME") || isSame(word, "CURRENT_TIMESTAMP")
				|| isSame(word, "DISTINCT") || isSame(word, "EXCEPT") || isSame(word, "EXISTS") || isSame(word, "SHOW")
				|| isSame(word, "FALSE") || isSame(word, "FETCH") || isSame(word, "FOR") || isSame(word, "FROM")
				|| isSame(word, "FULL") || isSame(word, "GROUP") || isSame(word, "HAVING") || isSame(word, "INNER")
				|| isSame(word, "INTERSECT") || isSame(word, "IS") || isSame(word, "JOIN") || isSame(word, "LIKE")
				|| isSame(word, "LIMIT") || isSame(word, "MINUS") || isSame(word, "NATURAL") || isSame(word, "NOT")
				|| isSame(word, "NULL") || isSame(word, "OFFSET") || isSame(word, "ON") || isSame(word, "ORDER")
				|| isSame(word, "PRIMARY") || isSame(word, "ROWNUM") || isSame(word, "SELECT")
				|| isSame(word, "SYSDATE") || isSame(word, "SYSTIME") || isSame(word, "SYSTIMESTAMP")
				|| isSame(word, "TODAY") || isSame(word, "TRUE") || isSame(word, "UNION") || isSame(word, "UNIQUE")
				|| isSame(word, "INSERT") || isSame(word, "UPDATE") || isSame(word, "SET") || isSame(word, "INTO")
				|| isSame(word, "UNIQUE") || isSame(word, "UNIQUE") || isSame(word, "CREATE") || isSame(word, "DESC")
				|| isSame(word, "DESCRIBE") || isSame(word, "KEY") || isSame(word, "USE") || isSame(word, "FOREIGN")
				|| isSame(word, "WHERE") || isSame(word, "RESTRICT") || isSame(word, "REFERENCES"));

	}

	/*
	 * =============================================================================
	 * ==
	 */

	private static final boolean isDataTable(String word) {
		return (isSame(word, "TABLE_NAME") || isSame(word, "USER_TABLES") || isSame(word, "TABLE")
				|| isSame(word, "DATABASE") || isSame(word, "DATABASES") || isSame(word, "TABLES"));
	}

	/*
	 * =============================================================================
	 * ==
	 */
	private static final boolean isDeleting(String word) {
		return (isSame(word, "DELETE") || isSame(word, "DROP"));
	}

	/*
	 * =============================================================================
	 * ==
	 */
//misspell
	private static final boolean isAgreted(String word) {
		return (isSame(word, "IN") || isSame(word, "BETWEEN"));
	}

	/*
	 * =============================================================================
	 * ==
	 */
	private static final boolean isOperator(String word) {
		return (isSame(word, "AND") || isSame(word, "OR"));
	}

	/*
	 * =============================================================================
	 * ==
	 */
	private static final boolean isDatatype(String word) {

		return (isSame(word, "CROSS") || isSame(word, "VARCHAR") || isSame(word, "BLOB") || isSame(word, "REAL")
				|| isSame(word, "BIGIN") || isSame(word, "NCHAR") || isSame(word, "DECIMAL") || isSame(word, "CHAR")
				|| isSame(word, "NVARCHAR") || isSame(word, "UNICHAR") || isSame(word, "UNIVARCHAR")
				|| isSame(word, "UNITEXT") || isSame(word, "TEXT") || isSame(word, "NUMERIC") || isSame(word, "MONEY")
				|| isSame(word, "SAMALLMONEY") || isSame(word, "BIT") || isSame(word, "TINYINT")
				|| isSame(word, "SMALLINT") || isSame(word, "INTEGER") || isSame(word, "BIGINT")
				|| isSame(word, "UNSIGNED") || isSame(word, "FLOAT") || isSame(word, "DOUBLE")
				|| isSame(word, "PRECISION") || isSame(word, "BINARY") || isSame(word, "DATETIME")
				|| isSame(word, "SMALLDATETIME") || isSame(word, "BIGDATETIME") || isSame(word, "BIGTIME")
				|| isSame(word, "DATE") || isSame(word, "JAVA.SQL.DATE") || isSame(word, "TIME")
				|| isSame(word, "EXISTS") || isSame(word, "EXISTS") || isSame(word, "EXISTS"));

	}

	private static final boolean isClearing(String word) {

		return (isSame(word, "CLEAR") || isSame(word, "CLS") || isSame(word, "EXIT") || isSame(word, "QUIT")
				|| isSame(word, "CLOSE"));
	}

	static boolean isSame(String word1, String word2) {

		return word1.toUpperCase().trim().equals(word2);
	}

}
