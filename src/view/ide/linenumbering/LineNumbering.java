package view.ide.linenumbering;

import javax.swing.JTextArea;
import javax.swing.text.Element;

public class LineNumbering extends JTextArea
{
    /**
	 * 
	 */
	
	private static final long serialVersionUID = 2231687506695828395L;
	private JTextArea textArea;

    public LineNumbering(JTextArea textArea)
    {
        this.textArea = textArea;
        setEditable(false);
    }

    public void updateLineNumbers()
    {
        String lineNumbersText = getLineNumbersText();
        setText(lineNumbersText);
    }

    private String getLineNumbersText()
    {
        int caretPosition = textArea.getDocument().getLength();
        Element root = textArea.getDocument().getDefaultRootElement();
        StringBuilder lineNumbersTextBuilder = new StringBuilder();
        lineNumbersTextBuilder.append("1").append(System.lineSeparator());

        for (int elementIndex = 2; elementIndex < root.getElementIndex(caretPosition) + 2; elementIndex++)
        {
            lineNumbersTextBuilder.append(elementIndex).append(System.lineSeparator());
        }

        return lineNumbersTextBuilder.toString();
    }
}