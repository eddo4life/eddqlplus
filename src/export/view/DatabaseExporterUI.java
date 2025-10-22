package export.view;

import dao.mysql.MySQLDaoOperation;
import view.Home;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseExporterUI {

    public DatabaseExporterUI(String dbName, ArrayList<String> arrays) {
        boxList.clear();
        logArea.setText("");
        // list of the tables
        JButton button = new JButton("Export");
        button.setFocusable(false);
        button.setEnabled(false);
        JDialog dialog = new JDialog();
        dialog.setIconImage(Home.frame.getIconImage());
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(new Dimension(600, 270));
        dialog.setResizable(false);
        JPanel panel = new JPanel(new GridBagLayout());
        // panel.setBorder(new TitledBorder("Export"));
        dialog.setTitle("Export");
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        JRadioButton allTableButton = new JRadioButton("All tables");
        allTableButton.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(allTableButton);
        allTableButton.addActionListener(e -> selectAll(allTableButton.isSelected()));
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(allTableButton, constraints);
        JRadioButton selectTableButton = new JRadioButton("Select");
        group.add(selectTableButton);
        selectTableButton.addActionListener(e -> selectAll(!selectTableButton.isSelected()));
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(selectTableButton, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        JRadioButton excelFileButton = new JRadioButton("Excel file");
        JRadioButton sqlFileButton = new JRadioButton("SQL file");
        excelFileButton.addActionListener((e -> button.setEnabled(excelFileButton.isSelected() || sqlFileButton.isSelected())));

        panel.add(excelFileButton, constraints);

        sqlFileButton.addActionListener((e -> button.setEnabled(excelFileButton.isSelected() || sqlFileButton.isSelected())));
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(sqlFileButton, constraints);
        panel.setPreferredSize(new Dimension(150, 0));
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JLabel("Database : " + dbName), BorderLayout.NORTH);
        mainPanel.add(panel, BorderLayout.WEST);

        JPanel tablesPanel = new JPanel(new BorderLayout());

        JPanel pane = new JPanel(new GridBagLayout());
        // pane.setPreferredSize(new Dimension(200,100));

        GridBagConstraints constraints2 = new GridBagConstraints();
        constraints2.anchor = GridBagConstraints.WEST;
        constraints2.insets = new Insets(5, 5, 5, 5);
        int y = 0;
        for (String s : arrays) {
            JCheckBox checkBox = new JCheckBox(s);
            boxList.add(checkBox);
            constraints2.gridx = 0;
            constraints2.gridy = y++;
            pane.add(checkBox, constraints2);
        }
        selectAll(true);
        JScrollPane sp = new JScrollPane(pane);
        sp.setPreferredSize(new Dimension(270, 120));
        tablesPanel.add(sp, BorderLayout.WEST);
        tablesPanel.setBorder(new TitledBorder("Tables"));
        mainPanel.add(tablesPanel);
        logPanel(mainPanel);

        button.addActionListener(e -> {

            if (excelFileButton.isSelected() || sqlFileButton.isSelected()) {
                if (excelFileButton.isSelected() && sqlFileButton.isSelected()) {

                    Thread t = new Thread(() -> {
                        sqlExport();
                        excelExport();
                    });

                    t.start();
                } else if (sqlFileButton.isSelected()) {

                    Thread t = new Thread(this::sqlExport);

                    t.start();
                } else {
                    Thread t = new Thread(this::excelExport);

                    t.start();
                }
            }

            showSelectedBox();

        });
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        JButton pathChooser = new JButton("...");
        pathChooser.addActionListener(e -> {
            String res = pathChooser();
            if (res != null) {
                pathLabel.setText(res);
            }
        });
        pathChooser.setFocusable(false);
        southPanel.add(pathLabel);
        southPanel.add(pathChooser);
        southPanel.add(button);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

    }

    private final JLabel pathLabel = new JLabel("Path : " + currentPath());

    private void excelExport() {
        try {
            for (JCheckBox checkBox : boxList) {
                if (checkBox.isSelected()) {

                    new MySQLDaoOperation().exportToExcel(path, checkBox.getText());

                    logArea.append(
                            "-exc-" + checkBox.getText() + " excel file's exported successfully " + getDate() + "\n");
                }
            }

            highlight("Excel files export complete!!!", new Color(125, 117, 0));

        } catch (Exception e1) {
            e1.printStackTrace();
            highlight(e1.getMessage(), new Color(145, 42, 42));

        }
    }

    private void sqlExport() {
        try {
            for (JCheckBox checkBox : boxList) {
                if (checkBox.isSelected()) {
                    new MySQLDaoOperation().export(path, checkBox.getText());
                    logArea.append("-SQL-" + checkBox.getText() + " exported successfully " + getDate() + "\n");
                }
            }
            highlight("SQL queries export complete!!!", new Color(125, 117, 0));
        } catch (SQLException | IOException e1) {
            highlight(e1.getMessage(), new Color(145, 42, 42));
            e1.printStackTrace();

        }

    }

    private void highlight(String msg, Color color) {

        logArea.append("--" + msg + " " + getDate() + "\n");

        // Highlight the error message
        Highlighter highlighter = logArea.getHighlighter();
        HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(color);
        int startIndex = logArea.getText().lastIndexOf(msg);
        if (startIndex != -1) {
            int endIndex = startIndex + msg.length();
            try {
                highlighter.addHighlight(startIndex, endIndex, painter);
            } catch (BadLocationException e2) {
                e2.printStackTrace();
            }
        }
    }

    private final List<JCheckBox> boxList = new ArrayList<>();

    void showSelectedBox() {
        for (JCheckBox checkBox : boxList) {
            if (checkBox.isSelected()) {
                System.out.println(checkBox.getText());
            }
        }
    }

    private void selectAll(boolean isAllSelected) {
        for (JCheckBox checkBox : boxList) {
            checkBox.setSelected(isAllSelected);
            checkBox.setEnabled(!isAllSelected);
        }

    }

    private final JTextArea logArea = new JTextArea();

    private void logPanel(JPanel panel) {
        logArea.setEditable(false);
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setPreferredSize(new Dimension(150, 0));
        logPanel.setBorder(new TitledBorder("Log"));
        panel.add(logPanel, BorderLayout.EAST);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logPanel.add(new JScrollPane(logArea));
    }

//	public static void main(String[] args) {
//		new DatabaseExporterUI("", new ArrayList<>());
//	}

    private String getDate() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return myDateObj.format(myFormatObj);
    }

    private String pathChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            path = filePath;
            path = path.replace("\\", "/");
            return "Path : " + filePath;
        }
        return null;

    }

    private String currentPath() {
        Path currentDir = Paths.get("");
        path = currentDir.toAbsolutePath().toString();
        path = path.replace("\\", "/");
        return currentDir.toAbsolutePath().toString();
    }

    String path = "";

}
