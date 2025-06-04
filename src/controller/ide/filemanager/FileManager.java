package controller.ide.filemanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import view.Editor_section;
import view.Home;

public class FileManager {
	
	RSyntaxTextArea editor_pane=Editor_section.textArea;
	
	public FileManager() {
		
	}
	
	public void saveFile() {
		JFileChooser saveFile = new JFileChooser("f:");

		int r = saveFile.showOpenDialog(Home.frame);

		if (r == JFileChooser.APPROVE_OPTION) {
			File fi = new File(saveFile.getSelectedFile().getAbsolutePath());

			try {
				FileWriter wr = new FileWriter(fi, false);
				BufferedWriter w = new BufferedWriter(wr);
				w.write(editor_pane.getText());
				w.flush();
				w.close();
			} catch (Exception evt) {
				JOptionPane.showMessageDialog(null, evt.getMessage());
			}
		}

	}


	public void openFile() {

		JFileChooser openFile = new JFileChooser("f:");
		int r = openFile.showOpenDialog(Home.frame);

		if (r == JFileChooser.APPROVE_OPTION) {
			File fi = new File(openFile.getSelectedFile().getAbsolutePath());

			try {

				String text = "", endText = "";

				FileReader fr = new FileReader(fi);

				BufferedReader br = new BufferedReader(fr);

				endText = br.readLine();

				while ((text = br.readLine()) != null) {
					endText = endText + "\n" + text;
				}

				editor_pane.setText(endText);
				br.close();
			} catch (Exception evt) {
				JOptionPane.showMessageDialog(null, evt.getMessage());
			}
		}

	}

}
