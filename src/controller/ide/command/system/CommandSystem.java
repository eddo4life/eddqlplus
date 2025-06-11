package controller.ide.command.system;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import view.Editor_section;
import view.Home;

import javax.swing.*;
import java.awt.*;

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
                }
                case "exit;", "quit;", "close;" -> t.setEditable(false);//welcome page...
                case "hbar;", "hide bar" -> Home.toolBar.setVisible(false);
                case "sbar;", "show bar" -> Home.toolBar.setVisible(true);
                case "open font;", "choose font;", "pick font;" -> Editor_section.westPanel_fontChooser_parent.setVisible(true);
                case "font color;" -> {
                    Color color = JColorChooser.showDialog(Home.frame, "Choose a color", null);
                    t.setForeground(color);
                    Editor_section.editorPaneCurrentFgColor = color;
                }
                case "w -resize;" -> {
                    Home.frame.setResizable(!Home.frame.isResizable());
                }
                // TODO: Add commands that can interact with the SQL's GUI
            }
        }

    }

    boolean cls(String str) {
        if (isClsCommand(str)) {
            Editor_section.reset();
            return true;
        }
        return false;
    }

    private boolean isClsCommand(String str) {
        if (str.length() == 4) {
            return str.equalsIgnoreCase("cls;");
        }
        if (str.length() > 4) {
            boolean hasLeadingSpace = Character.isWhitespace(str.charAt(str.length() - 5));
            String lastFourChars = str.substring(str.length() - 4).toLowerCase();
            return hasLeadingSpace && lastFourChars.equals("cls;");
        }
        return false;
    }
}
