package controller.ide.filemanager;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import view.EditorSection;
import view.Home;

import javax.swing.*;
import java.io.*;

public class FileManager {

    RSyntaxTextArea editor_pane = EditorSection.textArea;

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

                String currentLine = "";

                FileReader fr = new FileReader(fi);

                BufferedReader br = new BufferedReader(fr);

                StringBuilder text = new StringBuilder(br.readLine());

                while ((currentLine = br.readLine()) != null) {
                    text.append("\n").append(currentLine);
                }

                editor_pane.setText(text.toString());
                br.close();
            } catch (Exception evt) {
                JOptionPane.showMessageDialog(null, evt.getMessage());
            }
        }

    }

}
