package controller.ide.command.system;

import java.awt.Color;

import javax.swing.JColorChooser;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import view.Editor_section;
import view.Home;

public class CommandSystem {

    public CommandSystem(String cmd) {

        execute(cmd);
    }

    void execute(String cmd) {
        if (!cls(cmd)) {
            RSyntaxTextArea t = Editor_section.textArea;
            switch (cmd) {
                case "cls;" -> Editor_section.reset();
                case "clear;" -> {
                    t.setText("");
                    //Editor.lineEditorPane.setText("");
                }
                case "exit;", "quit;", "close;" -> t.setEditable(false);//welcome page...
                case "hbar;", "hide bar" -> Home.toolBar.setVisible(false);
                case "sbar;", "show bar" -> Home.toolBar.setVisible(true);
                case "light;" -> {

                    //Editor_section.resetColor();
                }
                case "dark;" -> {

                    //Editor_section.resetColor();
                }
                case "open font;", "choose font;", "pick font;" -> Editor_section.westPanel_fontChooser_parent.setVisible(true);
                case "font color;" -> {
                    Color color = JColorChooser.showDialog(Home.frame, "Choose a color", null);
                    t.setForeground(color);
                    Editor_section.editorpane_current_fgColor = color;
                }
                case "w -resize;" -> {
                    Home.frame.setResizable(!Home.frame.isResizable());
                }
                //case "create new table"->;
                //cretate new strategies fro insert....
                //case "insert into ...(to autmatically select the table..
                //case "new insertion"...
            }
        }

    }

    boolean cls(String str) {
        boolean cls = false;
        if (str.length() > 4)
            if (String.valueOf(str.charAt(str.length() - 5)).isBlank()) {
                if (str.toLowerCase().substring(str.length() - 4, str.length()).equals("cls;")) {
                    Editor_section.reset();
                    cls = true;
                }
            } else if (str.length() == 4) {
                Editor_section.reset();
                cls = true;
            }
        return cls;
    }

}

/*


 */