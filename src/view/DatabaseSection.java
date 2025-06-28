package view;

import controller.library.EddoLibrary;
import dao.mysql.MySQLConnection;
import dao.mysql.MySQLDaoOperation;
import eddql.launch.LoadData;
import export.view.DatabaseExporterUI;
import model.DataBaseModel;
import view.iconmaker.IconGenerator;
import view.pupupsmessage.PopupMessages;
import view.tables.Custom;
import view.tables.JTableUtilities;
import view.tables.Sort;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

//color static from the class background an stuff...

public class DatabaseSection {
    // theme for panels

    private ArrayList<String> names = new ArrayList<>();
    private JTextField dbNameField;
    private JPanel mainPanel;

    /*
     *
     * ========================================================
     *
     */

    public DatabaseSection() {

        msgLabel.setFocusable(false);
        timer();

    }

    /*
     *
     * ========================================================
     *
     */

    public void dataBases() throws SQLException {
        names = new ArrayList<>();
        ArrayList<String> dataBases = new MySQLDaoOperation().showDataBases();

        JTable table = new JTable();
        // table.setEnabled(false);

        // table.addMouseListener(this);
        JPanel intern = new JPanel();
        intern.setLayout(new BorderLayout());
        final Object[] header = {"Names", "oldest tab created", "at", "latest tab created", "at", "tables count"};
        Home.frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        int i = 0, j = dataBases.size();
        final Object[][] obj = new Object[j][6];
        try {
            for (DataBaseModel data : LoadData.database) {
                obj[i][0] = data.getName();
                obj[i][1] = data.getLatestTab();
                obj[i][2] = data.getLatestTabTime();
                obj[i][3] = data.getOldestTab();
                obj[i][4] = data.getOldestTabTime();
                obj[i][5] = data.getTablesCount();
                names.add(data.getName());
                i++;
            }
        } catch (Exception e) {
            new LoadData().databaseSectionLoader();
            dataBases();
        }

//		JTableHeader hea = table.getTableHeader();
//		hea.setUI(new javax.swing.plaf.basic.BasicTableHeaderUI());
        JTableUtilities.setCellsAlignment(table, SwingConstants.CENTER, 0);

        @SuppressWarnings("rawtypes")
        Class[] columnClass = new Class[]{String.class, String.class, String.class, String.class, String.class,
                Integer.class};

        DefaultTableModel defaultTableModel = new DefaultTableModel(obj, header) {

            public Class<?> getColumnClass(int columnIndex) {
                return columnClass[columnIndex];
            }
        };
        table.setModel(defaultTableModel);
        table.setDefaultEditor(Object.class, null);
        JTableUtilities.setCellsAlignment(table, SwingConstants.CENTER, 1);
        // horizontal lines show,vertical...lines gap, current bg, ...
        Custom tabCustom = new Custom(table, false, false, 30, null, null);
        intern.add(tabCustom.getScrollPane());

        if (Home.content != null) {
            Home.content.removeAll();
        }

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(intern, BorderLayout.CENTER);
        header();
        // dashBoard();
        Home.content.add(mainPanel, BorderLayout.CENTER);
        revalidate(Home.content);
        Home.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        new Sort().tableSortFilter(table);

        // new action implementation

        action(table);
    }

    /*
     *
     * ================================================================
     *
     */

    private void action(JTable table) {
        table.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {

                if (SwingUtilities.isRightMouseButton(e)) {
                    try {

                        int row = table.rowAtPoint(e.getPoint());
                        if (row >= 0 && row < table.getRowCount()) {
                            table.setRowSelectionInterval(row, row);
                            String selectedValue = table.getValueAt(row, 0).toString();
                            JPopupMenu popup = new JPopupMenu();
                            JMenuItem deleteItem = new JMenuItem("Delete");
                            deleteItem.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    // Delete action
                                    action("Delete", selectedValue);
                                }
                            });
                            JMenuItem connectItem = new JMenuItem("Connect");
                            connectItem.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    action("Connect", selectedValue);
                                }
                            });

                            JMenuItem createItem = new JMenuItem("↳ New");
                            createItem.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    // action("Create", selectedValue);
                                    createDatabase();
                                }
                            });
                            String dbConnected = new MySQLConnection().getDbName();
                            JMenuItem exportItem = new JMenuItem("Export");
                            exportItem.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        new DatabaseExporterUI(dbConnected, new MySQLDaoOperation().showTables());

                                    } catch (SQLException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            });

                            popup.add(connectItem);
                            popup.add(deleteItem);
                            popup.add(createItem);
                            if (dbConnected.equals(selectedValue)) {
                                popup.add(exportItem);
                            }
                            if (selectedValue != null) {
                                popup.show(e.getComponent(), e.getX(), e.getY());
                            }
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        });

    }

    private void createDatabase() {
        JDialog dialog = new JDialog();
        dialog.setIconImage(Home.frame.getIconImage());
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(new Dimension(400, 120));
        dialog.setResizable(false);
        JPanel panel = new JPanel(new GridBagLayout());
        dialog.setTitle("Create database");
        dbNameField = new JTextField(15);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("Name :"), constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(dbNameField, constraints);
        constraints.gridx = 2;
        constraints.gridy = 0;
        JButton createButton = new JButton("Create");
        panel.add(createButton, constraints);

        JLabel label = new JLabel(" ");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(label, constraints);

        createButton.setPreferredSize(new Dimension(80, 25));

        createButton.addActionListener((e) -> {
            try {
                if (new MySQLDaoOperation().create(dbNameField.getText()) > 0) {

                    label.setText(dbNameField.getText() + " created successfully");
                } else {
                    label.setText("Process failed");

                }
                DataBaseModel x = new DataBaseModel(dbNameField.getText(), "...", "...", "...", "...", 0);
                LoadData.database.add(x);
                dataBases();
            } catch (SQLException e1) {
                new PopupMessages().message(e1.getMessage(), new IconGenerator().exceptionIcon());
            }
        });

        dbNameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getSource() == dbNameField) {
                    String txt = dbNameField.getText().trim();
                    if (!txt.isEmpty()) {
                        txt = txt.toLowerCase();
                        if (names.contains(txt)) {
                            createButton.setEnabled(false);
                            label.setText("Database already existed");
                        } else if (EddoLibrary.prohibitedName().contains(txt) || txt.contains(" ")
                                || EddoLibrary.numbers().contains(txt.substring(0, 1))) {
                            createButton.setEnabled(false);
                            label.setText("Incorrect name");
                        } else {
                            createButton.setEnabled(true);
                            label.setText(" ");
                        }
                    } else {
                        createButton.setEnabled(false);
                        label.setText("Name can not be empty");
                    }
                }
            }
        });

        dialog.add(panel);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

    }

    private void header() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        JPanel searchPanel = new JPanel();
        searchPanel.setBounds(5, 10, 300, 20);
        searchPanel.setLayout(new FlowLayout());
        msgLabel.setFont(new Font("", Font.PLAIN, 11));
        searchPanel.add(msgLabel);
        panel.add(searchPanel);
        JLabel lineLabel = new JLabel(
                "_____________________________________________________________________________________________________________________________");
        lineLabel.setBounds(300, 0, 20000, 25);
        lineLabel.setForeground(new JLabel().getForeground());
        panel.add(lineLabel);
        panel.setPreferredSize(new Dimension(100, 40));

        mainPanel.add(panel, BorderLayout.NORTH);
        revalidate(panel);

    }

    private void revalidate(JPanel panel) {
        panel.revalidate();
        panel.repaint();
    }

    /*
     *
     * ========================================================
     *
     */

    @SuppressWarnings("static-access")
    private void action(String act, String dataBase) {
        try {
            switch (act) {
                case "Connect" -> {
                    new MySQLDaoOperation().use(dataBase);

                    new MySQLConnection().setDbName(dataBase);
                    Home.frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    new LoadData().tablesSectionLoader();
                    Home.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    msgLabel.setText("connected to " + dataBase);
                    // msgLabel.setForeground(Color.white);
                    Home.dbn = dataBase;
                }
                case "Delete" -> {
                    new PopupMessages().confirm("Do you really want to delete " + dataBase);
                    if (PopupMessages.getAction == 1) {
                        new MySQLDaoOperation().deleteDb(dataBase);
                        for (DataBaseModel d : LoadData.database) {
                            if (d.getName().equals(dataBase)) {
                                LoadData.database.remove(d);
                                break;
                            }
                        }
                        new PopupMessages().message("Process completed", new IconGenerator().successIcon());
                    } else {
                        new PopupMessages().message("Process canceled", new IconGenerator().messageIcon());
                    }
                }

            }
        } catch (SQLException e) {
            new PopupMessages().message(e.getMessage(), new IconGenerator().exceptionIcon());
        }
        try {
            dataBases();
        } catch (SQLException e) {
            Home.dbn = "";
            new MySQLConnection().setDbName("");
            msgLabel.setText("No more database selected!");
            try {
                Home.dbn = new MySQLConnection().getDbName();
                dataBases();
            } catch (SQLException e1) {
                new PopupMessages().message("A terrible error occurred", new IconGenerator().exceptionIcon());
                Home.frame.dispose();
                Home.frame.removeAll();
                new PopupMessages().message(
                        "if that didn't fix it, please restart the program manually or contact assistance",
                        new IconGenerator().exceptionIcon());
                new MainEddQL().main(null);
            }
        }
        Home.frame.revalidate();
        Home.frame.repaint();
    }


    /*
     *
     * ========================================================
     *
     */

    JLabel msgLabel = new JLabel();

    Timer timer = new Timer();

    public void timer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            int i = 0;

            public void run() {
                if (i == 10) {
                    msgLabel.setText("");
                    i = -1;
                }
                i++;

            }
        }, 450, 450);
    }

}
