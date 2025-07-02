package view;

import controller.library.EddoLibrary;
import controller.tables.CreateTableManager;
import dao.DBMS;
import dao.mysql.MySQLConnection;
import dao.mysql.MySQLDaoOperation;
import dao.oracle.OracleDaoOperation;
import eddql.launch.LoadData;
import model.CreateTableModel;
import model.ShowTablesModel;
import view.iconmaker.IconGenerator;
import view.pupupsmessage.PopupMessages;
import view.resize.Resize;
import view.tables.JTableUtilities;
import view.tables.Sort;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class CreateTable implements MouseListener, KeyListener {
    public String database;
    private String name;
    private JComboBox<String> removeConstraintComboBox;
    private JButton okButton, exitButton, addButton;// ,searchNextButton;
    private JPanel mainPanel;
    private JLabel tableName;
    private JPanel panel;
    static JPanel pane;
    private String query = "";
    private JTextField limitTextField;
    private final ArrayList<String> listNames = new ArrayList<>();
    private JTextField tableNameField, columnNameField;
    private final ArrayList<String> dataTable = new ArrayList<>();
    private final ConstraintManager constraintManager = new ConstraintManager();

    private final ArrayList<CreateTableModel> dataLine = new ArrayList<>();
    private String[] tabs;

    private CreateTableModel ctm = new CreateTableModel();

    public CreateTable() {
    }

    /*
     *
     * ========================================================
     *
     */

    public void initialize() {
        constraintManager.clear();

        panel = new JPanel();
        panel.setLayout(new FlowLayout());

        if (Home.content != null) {
            Home.content.removeAll();
        }

        showTables();
    }

    /*
     *
     * ========================================================
     *
     */

    public String getDatabase() {
        return database;
    }

    /*
     *
     * ========================================================
     *
     */

    public void setDatabase(String database) {
        this.database = database;
    }

    /*
     *
     * ========================================================
     *
     */

    public void showTables() {
        try {
            ArrayList<String> tablesList;
            if (DBMS.dbms == 1) {
                tablesList = new MySQLDaoOperation().showTables();
            } else {
                tablesList = new OracleDaoOperation().showTables();
            }

            String title = "(" + tablesList.size() + ") tables from " + new MySQLConnection().getDbName();
            if (tablesList.size() == 1) {
                title = "(1) table from " + new MySQLConnection().getDbName();
            }
            if (tablesList.isEmpty()) {
                title = "";
            }
            pane = new JPanel();
            pane.setLayout(new GridBagLayout());

            JTable table = new JTable();

            table.setEnabled(false);
            table.addMouseListener(this);

            JPanel intern = new JPanel();
            intern.setLayout(new BorderLayout());
            Object[] header = {"#", "Names", "Columns count", "Rows count", "Date", "Time"};
            int i = 0, j = tablesList.size(), k = 1;
            tabs = new String[j];
            Home.frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            Object[][] obj = new Object[j][6];
            for (ShowTablesModel data : LoadData.tables) {
                obj[i][0] = k;
                obj[i][1] = data.getNames();
                tabs[i] = data.getNames();
                obj[i][2] = data.getColumnCount();
                obj[i][3] = data.getRowCount();
                obj[i][4] = data.getDate();
                obj[i][5] = data.getTime();
                listNames.add(data.getNames().toUpperCase());
                i++;
                k++;
            }
            @SuppressWarnings("rawtypes")
            Class[] columnClass = new Class[]{Integer.class, String.class, Integer.class, Integer.class, String.class,
                    String.class};
            DefaultTableModel defaultTableModel = new DefaultTableModel(obj, header) {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                public Class<?> getColumnClass(int columnIndex) {
                    return columnClass[columnIndex];
                }
            };
            table.setModel(defaultTableModel);
            JTableHeader hea = table.getTableHeader();
            hea.setUI(new javax.swing.plaf.basic.BasicTableHeaderUI());

            TableSelector.tableSetup(title, intern, table, pane);
            mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(pane, BorderLayout.CENTER);
            panelBuilder = new JPanel();
            panelBuilder.setLayout(new BorderLayout());
            panelBuilder.setVisible(false);
            Object[] ob = tableBuilder();
            panelBuilder.add((JScrollPane) ob[0]);
            int length = (int) ob[1];
            if (length < 100)
                panelBuilder.setPreferredSize(new Dimension(0, length));
            else
                panelBuilder.setPreferredSize(new Dimension(0, 100));

            mainPanel.add(panelBuilder, BorderLayout.NORTH);
            Home.content.add(mainPanel, BorderLayout.CENTER);
            Home.content.revalidate();
            Home.content.repaint();
            Home.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            new Sort().tableSortFilter(table);
        } catch (SQLException e) {
            if (tableName == null) {
                Insertion.tableName.setText("Exception ...");
                Insertion.tableName.setForeground(Color.red);
                Insertion.tableNameField.setBorder(null);
                Insertion.tableNameField.setText("	" + e.getMessage());
                Insertion.tableNameField.setFocusable(false);

                warning();
            } else {
                tableName.setText("Exception ...");
                tableName.setForeground(Color.red);
                tableNameField.setBorder(null);
                tableNameField.setText("	" + e.getMessage());
                tableNameField.setFocusable(false);
                tableNameField.setEditable(false);
                // searchNextButton.setVisible(false);
                warning();
            }
        } catch (Exception e) {
            new LoadData().tablesSectionLoader();
            showTables();
        }
    }

    private JPanel panelBuilder;
    /*
     *
     * ========================================================
     *
     */

    public Object[] tableBuilder() {
        JTable table = new JTable();
        table.setRowHeight(20);
        int size = dataLine.size();
        if (table.isEnabled()) {
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (size > 0) {
                        String id = String.valueOf(table.getValueAt(table.getSelectedRow(), 0));
                        String[] reps = {"Delete", "Update", "Cancel"};

                        int rep = JOptionPane.showOptionDialog(null, "do you want to delete or update?",
                                "delete or update", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
                                new IconGenerator().questionIcon(), reps, 0);
                        if (rep == 0) {
                            for (CreateTableModel cm : dataLine) {
                                if (cm.getName().equals(id)) {
                                    dataLine.remove(cm);
                                    break;
                                }
                            }

                        } else if (rep == 1) {
                            columnNameField.setText(id);
                            comboBoxDataType.setSelectedItem(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
                            String limit = String.valueOf(table.getValueAt(table.getSelectedRow(), 2));
                            limitTextField.setText(limit);
                            if (limitTextField.getText().equals("null")) {
                                limitTextField.setVisible(false);
                                limitLabel.setVisible(false);
                                limitTextField.setText("2");
                            } else {
                                limitTextField.setVisible(true);
                                limitLabel.setVisible(true);
                            }
                            for (CreateTableModel cm : dataLine) {
                                if (cm.getName().equals(id)) {
                                    boolean exist = false;
                                    String[] constraints = {};
                                    if (cm.getConstraintAff().contains("Not null")) {
                                        cm.setConstraintAff(cm.getConstraintAff().replace("Not null", ""));
                                        constraints = cm.getConstraintAff().split(" ");
                                        exist = true;
                                    } else {
                                        constraints = cm.getConstraintAff().trim().split(" ");
                                    }
                                    for (String constraint : constraints) {
                                        constArrayList(constraint);
                                    }
                                    if (exist) {
                                        constArrayList("Not null");
                                    }
                                    if (cm.getKey() != null) {
                                        if (cm.getKey().equals("Foreign key")) {
                                            tables.setSelectedItem(cm.getTabSelectForReference());
                                            columns.setSelectedItem(cm.getReferences());
                                            foreignAssociated();
                                        }
                                    }
                                    if (cm.getKey() == null || cm.getKey().equals("Primary key")) {
                                        if (southPanel != null) {
                                            p.remove(southPanel);
                                            Home.content.revalidate();
                                            Home.content.repaint();
                                        }
                                    }
                                    toModify = cm;
                                    break;
                                }
                            }

                        }
                    }
                    Home.content.remove(mainPanel);
                    showTables();
                    panelBuilder.setVisible(true);
                }
            });
        }
        JTableHeader hea = table.getTableHeader();
        hea.setUI(new javax.swing.plaf.basic.BasicTableHeaderUI());
        hea.setBackground(Color.darkGray);
        hea.setForeground(Color.white);
        CreateTableManager model = new CreateTableManager(dataLine);
        table.setModel(model);
        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JTableUtilities.setCellsAlignment(table, SwingConstants.CENTER, 0);
        scrollPane.setViewportBorder(null);
        scrollPane.setBorder(null);
        return new Object[]{scrollPane, size * 20 + 25};
    }

    private CreateTableModel toModify = null;

    /*
     *
     * ========================================================
     *
     */

    public void openCreation(String tabName) {
        setName(tabName);
        panelBuilder.setVisible(true);
        create();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getSource() == addButton) {
            add(columnNameField.getText());
        }
    }

    /*
     *
     * ========================================================
     *
     */

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    /*
     *
     * ========================================================
     *
     */

    @Override
    public void keyReleased(KeyEvent e) {
        Object source = e.getSource();

        if (source == columnNameField) {
            handleColumnNameField();
        } else if (source == limitTextField) {
            handleLimitField();
        }
    }

    private void handleColumnNameField() {
        String columnName = columnNameField.getText().trim();

        boolean validName = EddoLibrary.isValidSqlColumnName(columnName);
        boolean notDuplicate = !isColumnExist(columnName);
        boolean limitValidOrNotNeeded = EddoLibrary.isNumber(limitTextField.getText()) || !hasLimit(ctm.getDatatype());

        if (validName && notDuplicate && limitValidOrNotNeeded) {
            columnNameField.setForeground(Color.BLACK);
            enableButton();
        } else {
            columnNameField.setForeground(Color.RED);
            disableButton();
        }
    }

    private void handleLimitField() {
        String limitText = limitTextField.getText().trim();
        boolean limitRequired = hasLimit(ctm.getDatatype());

        if (!EddoLibrary.isNumber(limitText)) {
            limitTextField.setForeground(Color.RED);
            if (limitRequired) disableButton();
            return;
        }

        if (Integer.parseInt(limitText) < 1) {
            limitTextField.setForeground(Color.RED);
            disableButton();
            return;
        }

        limitTextField.setForeground(Color.BLACK);

        // Re-evaluate column name validity to possibly re-enable
        if (!columnNameField.getText().isBlank()) {
            enableButton();
        }
    }


    /*
     *
     * ========================================================
     *
     */

    public void disableButton() {
        if (okButton != null) {
            okButton.setEnabled(false);
            addButton.setEnabled(false);
        }
    }

    /*
     *
     * ========================================================
     *
     */

    public void enableButton() {
        if (okButton != null) {
            okButton.setEnabled(true);
            addButton.setEnabled(true);
        }
    }

    /*
     *
     * ========================================================
     *
     */

    public void warning() {
        JLabel imgLabel = new JLabel();
        JPanel gr = new JPanel();
        gr.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        gr.add(imgLabel, gbc);
        mainPanel.add(gr, BorderLayout.CENTER);
        Home.content.add(mainPanel, BorderLayout.CENTER);
        Home.content.revalidate();
        Home.content.repaint();
    }

    /*
     *
     * ========================================================
     *
     */


    private JPanel p;

    public void create() {
        Home.content.remove(panel);
        okButton = new JButton("Create");
        okButton.setEnabled(false);
        okButton.setPreferredSize(new Dimension(70, 30));

        okButton.addActionListener((ActionEvent e) -> {
            if (dataLine.size() > 0) {
                try {
                    query = queryBuilder(dataLine);

                    int exec = -1;
                    if (DBMS.dbms == 1) {
                        exec = new MySQLDaoOperation().createTable(query);
                    } else if (DBMS.dbms == 2) {
                        exec = new OracleDaoOperation().createTable(query);
                    }

                    if (exec >= 0) {
                        new PopupMessages().message("Created successfully", new IconGenerator().successIcon());
                        // toUpdateTableList
                        new LoadData().tablesSectionLoader();
                        new TablesSections().options();
                    } else {
                        new PopupMessages().message("Process failed", new IconGenerator().failedIcon());
                    }
                } catch (SQLException e1) {
                    new PopupMessages().message("Operation failed, please consider giving proper name to your fields",
                            new IconGenerator().exceptionIcon());
                }

                dataLine.clear();

            } else {
                new PopupMessages().message("Please make sure adding a column", new IconGenerator().failedIcon());
            }
        });

        exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(70, 30));
        exitButton.setFocusable(false);

        exitButton.addActionListener((ActionEvent e) -> new TablesSections().options());
        exitButton.setForeground(Color.red);

        p = new JPanel();
        p.setLayout(new BorderLayout());

        JPanel northPanel, westPanel;
        northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());
        westPanel = new JPanel();
        westPanel.setLayout(new FlowLayout());

        p.add(northPanel, BorderLayout.NORTH);
        p.add(east(), BorderLayout.CENTER);
        new Resize(p, "tabData");

        Home.content.add(p, BorderLayout.NORTH);

        Home.content.revalidate();
        Home.content.repaint();
    }

    /*
     *
     * ========================================================
     *
     */

    private JComboBox<String> comboBoxDataType;
    private JLabel limitLabel;

    boolean internAction = false;

    public JPanel east() {
        GridBagConstraints c = new GridBagConstraints();
        JPanel eastPanel = new JPanel();
        JLabel cname = new JLabel("Column name");
        columnNameField = new JTextField(10);
        columnNameField.addKeyListener(this);
        limitLabel = new JLabel("Limit");
        limitTextField = new JTextField("2", 5);
        limitTextField.addKeyListener(this);
        JLabel dataTypeLabel = new JLabel("Data type");
        JLabel addC = new JLabel("Add constraints");
        String[] dataTypeArray = {"int", "varchar", "real", "blob", "decimal", "date"};
        comboBoxDataType = new JComboBox<String>(dataTypeArray);
        comboBoxDataType.addActionListener((ActionEvent e) -> {
            ctm.setDatatype(Objects.requireNonNull(comboBoxDataType.getSelectedItem()).toString());
            limitTextField.setEnabled(hasLimit(ctm.getDatatype()));
            if (!EddoLibrary.isNumber(limitTextField.getText())) {
                limitTextField.setForeground(Color.red);
                if (hasLimit(ctm.getDatatype()))
                    disableButton();
                else {
                    enableButton();
                }
            }
        });
        eastPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        eastPanel.add(okButton);
        eastPanel.add(exitButton);
        String[] constraints = {"Not null", "Unique", "Primary key", "Foreign key", "Check", "Default"};
        JComboBox<String> constraintPickerComboBox = new JComboBox<String>(constraints);
        constraintPickerComboBox.addActionListener((ActionEvent e) -> {
            String item = (String) constraintPickerComboBox.getSelectedItem();
            if (item != null) {
                if (constraintPickerComboBox.getSelectedIndex() > 3) {
                    new PopupMessages().message(item + " constraint is unavailable", new IconGenerator().messageIcon());
                } else {
                    if (item.equals("Foreign key")) {
                        /*
                         * label references, combobox tables inside the database and the column
                         * associated
                         */
                        if (tabs.length != 0) {
                            if (!constraintManager.contains("Primary key")) {
                                constArrayList(item);
                                foreignAssociated();
                            }
                        }
                    } else {
                        constArrayList(item);
                    }
                    internAction = true;
                    removeConstraintComboBox.removeAllItems();
                    for (int i = 0; i < constraintManager.getArray()
                            .length; i++) {
                        internAction = true;
                        removeConstraintComboBox.addItem(constraintManager.getArray()
                                [i]);
                    }
                    internAction = false;
                }
            }
        });
        eastPanel.add(cname);
        eastPanel.add(columnNameField);
        eastPanel.add(dataTypeLabel);
        eastPanel.add(comboBoxDataType);
        eastPanel.add(limitLabel);
        eastPanel.add(limitTextField);
        eastPanel.add(addC);
        eastPanel.add(constraintPickerComboBox);
        JLabel remC = new JLabel("Remove constraints");
        eastPanel.add(remC);
        JPanel rcPanel = new JPanel();
        rcPanel.setLayout(new GridBagLayout());
        removeConstraintComboBox = new JComboBox<>(constraintManager.getArray()
        );
        removeConstraintComboBox.addActionListener((ActionEvent e) -> {
            String constraint = (String) removeConstraintComboBox.getSelectedItem();
            if (!internAction) {
                assert constraint != null;
                constraintManager.removeConstraint(constraint);
                removeConstraintComboBox.removeAllItems();
                for (int i = 0; i < constraintManager.getArray()
                        .length; i++) {
                    removeConstraintComboBox.addItem(constraintManager.getArray()
                            [i]);
                }
                if (constraint.equals("Foreign key")) {
                    if (southPanel != null) {
                        p.remove(southPanel);
                        Home.content.revalidate();
                        Home.content.repaint();
                    }
                }
            }
            internAction = false;
        });
        // rcPanel.add(rcBox);
        eastPanel.add(removeConstraintComboBox);
        addButton = new JButton("Add column");
        addButton.setEnabled(false);
        addButton.addActionListener(e -> {

            if (constraintManager.contains("Foreign key") && ctm.getReferences() == null) {
                new PopupMessages().message("Please select a reference!", new IconGenerator().messageIcon());
            } else {
                if (!isColumnExist(columnNameField.getText())) {
                    if (toModify != null) {
                        dataLine.remove(toModify);
                    }
                    toModify = null;
                    ctm.setName(columnNameField.getText());
                    if (hasLimit(ctm.getDatatype()))
                        ctm.setLimit(limitTextField.getText());
                    else
                        ctm.setLimit(null);
                    ctm.setConstraint(constraintAnalysis());
                    dataLine.add(ctm);
                    if (southPanel != null) {
                        p.remove(southPanel);
                        Home.content.revalidate();
                        Home.content.repaint();
                    }
                    Home.content.remove(mainPanel);
                    showTables();
                    panelBuilder.setVisible(true);
                    ctm = new CreateTableModel();
                    constraintManager.clear();
                } else {
                    new PopupMessages().message("two columns can not have same name!", new IconGenerator().exceptionIcon());
                }
            }
        });
        c.gridx = 11;
        c.gridy = 0;
        eastPanel.add(addButton);
        return eastPanel;
    }

    private JPanel southPanel;
    private String tabSelect = "";
    private boolean remove = false;
    private GridBagConstraints c2;
    private JLabel ref, colref;
    private JPanel alternativePanel;
    private JComboBox<String> tables;

    /*
     *
     * ========================================================
     *
     */

    private JComboBox<String> columns;

    private void foreignAssociated() {
        remove = false;
        c2 = new GridBagConstraints();
        c2.insets = new Insets(5, 5, 5, 5);
        ref = new JLabel("Table");
        colref = new JLabel("Column Referenced");
        southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        alternativePanel = new JPanel();
        alternativePanel.setLayout(new FlowLayout());
        tables = new JComboBox<>(tabs);
        tables.addActionListener((ActionEvent e) -> {
            tabSelect = Objects.requireNonNull(tables.getSelectedItem()).toString();
            try {
                columns = new JComboBox<>(new MySQLDaoOperation().selectColumn(tabSelect));
                ctm.setTabSelectForReference(tabSelect);
                if (remove) {
                    alternativePanel.removeAll();
                    alternativePanel.revalidate();
                    alternativePanel.repaint();
                    tab();
                }
                alternativePanel.add(colref);
                alternativePanel.add(columns);
                remove = true;
                southPanel.add(alternativePanel, BorderLayout.EAST);
                p.add(southPanel, BorderLayout.SOUTH);
                p.revalidate();
                p.repaint();
                Home.content.revalidate();
                Home.content.repaint();
                columns.addActionListener((ActionEvent e1) -> {
                    ctm.setReferences(Objects.requireNonNull(columns.getSelectedItem()).toString());
                });
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        tab();
    }

    /*
     *
     * ========================================================
     *
     */

    private void tab() {
        c2.gridx = 0;
        c2.gridy = 0;
        alternativePanel.add(ref);
        c2.gridx = 1;
        c2.gridy = 0;
        alternativePanel.add(tables);
        southPanel.add(alternativePanel, BorderLayout.EAST);
        p.add(southPanel, BorderLayout.SOUTH);
        Home.content.revalidate();
        Home.content.repaint();
    }

    /*
     *
     * ========================================================
     *
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
     *
     * ========================================================
     *
     */

    private void constArrayList(String constraint) {
        boolean add = true;
        boolean isPrimary = constraint.equalsIgnoreCase("Primary key");
        boolean isForeign = constraint.equalsIgnoreCase("Foreign key");
        for (String data : constraintManager.getArray()) {
            if (constraint.equals(data)) {
                add = false;
            }
            if (isPrimary) {
                if (data.equalsIgnoreCase("Foreign key")) {
                    add = false;
                }
            } else if (isForeign) {
                if (data.equalsIgnoreCase("Primary key")) {
                    add = false;
                }
            }
        }
        if (add) {
            constraintManager.add(constraint);
        }
    }

    /*
     *
     * ========================================================
     *
     */


    private void add(String data) {
        dataTable.add(data);
    }


    public String queryBuilder(ArrayList<CreateTableModel> qb) {
        StringBuilder queryBuilder = new StringBuilder("CREATE TABLE " + getName() + " (");
        for (CreateTableModel data : qb) {
            queryBuilder.append(data.getName()).append(" ").append(data.getDatatype());
            if (data.getLimit() != null) {
                if (data.getLimit().length() > 0 && hasLimit(data.getDatatype())) {
                    queryBuilder.append("(").append(data.getLimit()).append(") ");
                }
            }
            if (data.getConstraint().length() > 0) {
                queryBuilder.append(data.getConstraint());
            }
            queryBuilder.append(",");
        }
        queryBuilder = new StringBuilder(queryBuilder.substring(0, queryBuilder.length() - 1));
        queryBuilder.append(")");//removed ; since it raised error in oracle

        return queryBuilder.toString();
    }

    /*
     *
     * ========================================================
     *
     */

    public boolean hasLimit(String dataType) {
        return dataType.equalsIgnoreCase("varchar") || dataType.equalsIgnoreCase("int");
    }

    /*
     *
     * ========================================================
     *
     */

    private String constraintAnalysis() {
        return constraintManager.analyze(ctm, dataLine);
    }

    /*
     *
     * ========================================================
     *
     */

    public boolean isColumnExist(String name) {
        for (CreateTableModel s : dataLine) {
            if (s.getName().equalsIgnoreCase(name) && toModify == null) {
                return true;
            }
        }
        return false;
    }
}
