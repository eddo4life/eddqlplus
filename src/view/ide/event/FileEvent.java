package view.ide.event;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import controller.ide.filemanager.FileManager;
import view.Editor_section;

public class FileEvent implements ActionListener, KeyListener {

	RSyntaxTextArea editor_pane = Editor_section.textArea;

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

			if (s.equals("cut")) {
				editor_pane.cut();
			} else if (s.equals("copy")) {
				editor_pane.copy();
			} else if (s.equals("paste")) {
				editor_pane.paste();
			} else if (s.equals("select All")) {
				editor_pane.selectAll();
			} else if (s.equals("clear")) {
				editor_pane.setText("");
				// lineCountStuff();
			} else if (s.equals("Save")) {
				new FileManager().saveFile();
			} else if (s.equals("Print")) {
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
			} else if (s.equals("Open")) {
				new FileManager().openFile();
			} else if (s.equals("New")) {
				editor_pane.setEditable(true);
				editor_pane.setText("");
			} else if (s.equals("Close")) {
				
				Editor_section.close_font();
			}
		}

	}

}
