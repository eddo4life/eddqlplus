package view;

import controller.library.EddoLibrary;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TablesSections {


    public TablesSections() {
    }

    CreateTable ct = new CreateTable();
    public static JLabel tableName = new JLabel();
    ArrayList<String> listOfNames;
    JPanel panel, mainPanel;
    // this variable is responsible for the choice
    public static int optionChoice;

    public void saisie() throws NullPointerException {
        optionChoice = 0;
        tableName = new JLabel("");

        panel = new JPanel();
        panel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new EtchedBorder()));

        JPanel operationsPanel = new JPanel(null);
        operationsPanel.setPreferredSize(new Dimension(400, 70));
        JLabel titleLabel = new JLabel("Options");
        titleLabel.setFont(new Font("", Font.PLAIN, 15));
        titleLabel.setBounds(10, 20, 100, 25);
        operationsPanel.add(titleLabel);

        JPanel createPanel = new JPanel(null);
        createPanel.setVisible(false);
        JLabel tabName = new JLabel("table_name :");
        tabName.setBounds(0, 2, 80, 25);
        createPanel.add(tabName);
        JButton createbtn = new JButton("Create");
        JTextField createField = new JTextField("");

        createbtn.addActionListener((e) -> {
            CreateTable ct = new CreateTable();
            ct.__init__();
            ct.openCreation(createField.getText());
        });

        createTableAccess(createbtn, createField);
        createField.setBounds(80, 2, 120, 25);
        createbtn.setBounds(200, 2, 80, 25);
        createPanel.add(createField);
        createPanel.add(createbtn);
        createPanel.setBounds(100, 0, 300, 30);
        operationsPanel.add(createPanel);

        JSeparator separator = new JSeparator();
        separator.setBounds(100, 30, 300, 5);
        operationsPanel.add(separator);
        JPanel radioPanel = new JPanel(new GridLayout(1, 4, 2, 2));
        radioPanel.setBounds(100, 35, 300, 40);

        JRadioButton display = new JRadioButton("Display");
        display.addActionListener((e) -> {
            createPanel.setVisible(false);
            optionChoice = 0;
            TableSelector.table.setEnabled(true);
        });
        JRadioButton insert = new JRadioButton("Insert");
        insert.addActionListener((e) -> {
            createPanel.setVisible(false);
            optionChoice = 1;
            TableSelector.table.setEnabled(true);

        });
        JRadioButton modify = new JRadioButton("Modify");
        modify.addActionListener((e) -> {
            createPanel.setVisible(false);
            optionChoice = 2;
            TableSelector.table.setEnabled(true);

        });
        JRadioButton create = new JRadioButton("Create");

        create.addActionListener((e) -> {
            createPanel.setVisible(true);
            TableSelector.table.setEnabled(false);

        });

        display.setSelected(true);
        ButtonGroup group = new ButtonGroup();
        group.add(create);
        group.add(insert);
        group.add(modify);
        group.add(display);

        radioPanel.add(display);
        radioPanel.add(insert);
        radioPanel.add(modify);
        radioPanel.add(create);

        operationsPanel.add(radioPanel);

        panel.add(operationsPanel);

        if (Home.content != null) {
            Home.content.removeAll();
        }

        assert Home.content != null;
        Home.content.add(panel, BorderLayout.NORTH);
        Home.content.add(new TableSelector().showTables());
        Home.content.revalidate();
        Home.content.repaint();

    }

    void createTableAccess(JButton button, JTextField field) {
        button.setVisible(false);
        button.setFocusable(false);
        ArrayList<String> listNames = TableSelector.tablesListNames;
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String tableName = field.getText();
                if (tableName.length() > 0) {
                    if (listNames != null) {
                        if (EddoLibrary.numbers().contains(tableName.substring(0, 1)) || listNames.contains(tableName.toUpperCase())
                                || tableName.contains(" ")) {
                            field.setForeground(Color.red);
                            button.setVisible(false);
                        } else {
                            field.setForeground(null);
                            button.setVisible(true);
                        }
                    }
                } else {
                    button.setVisible(false);
                }
            }
        });

    }

    public void selectTable(String name) {

        Home.content.removeAll();
        Home.content.add(new TableToBeSelected().select(name), BorderLayout.CENTER);
        Home.content.revalidate();
        Home.content.repaint();

    }

}
