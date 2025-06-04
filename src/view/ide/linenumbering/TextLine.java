package view.ide.linenumbering;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;


public class TextLine{

	/**
	 * 
	 */

	public TextLine(JEditorPane t) {

		scrollpane = new JScrollPane(t);
		
		TextLineNumber tln1 = new TextLineNumber(t);
		scrollpane.setRowHeaderView(tln1);
		scrollpane.setViewportBorder(null);
		scrollpane.setBorder(null);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	}

	JScrollPane scrollpane ;

	public JScrollPane getScrollPane() {
		
		return scrollpane;
	}
}