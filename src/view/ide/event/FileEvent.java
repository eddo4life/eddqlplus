package view.ide.event;

import controller.ide.filemanager.FileManager;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import view.EditorSection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class FileEvent implements ActionListener, KeyListener {

    RSyntaxTextArea editor_pane = EditorSection.textArea;

    public FileEvent() {


    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (editor_pane.isEditable() && editor_pane.isEnabled()) {

            String s = e.getActionCommand();

            switch (s) {
                case "cut" -> editor_pane.cut();
                case "copy" -> editor_pane.copy();
                case "paste" -> editor_pane.paste();
                case "select All" -> editor_pane.selectAll();
                case "clear" -> editor_pane.setText("");
                case "Save" -> new FileManager().saveFile();
                case "Print" -> {
                    Color color = editor_pane.getForeground();
                    try {

                        if (color == Color.white) {
                            editor_pane.setForeground(Color.black);
                        }
                        editor_pane.print();
                    } catch (Exception evt) {
                        JOptionPane.showMessageDialog(null, evt.getMessage());
                    }
                    editor_pane.setForeground(color);
                }
                case "Open" -> new FileManager().openFile();
                case "New" -> {
                    editor_pane.setEditable(true);
                    editor_pane.setText("");
                }
                case "Close" -> EditorSection.close_font();
            }
        }

    }

}
