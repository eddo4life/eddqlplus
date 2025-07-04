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
    private JButton createButton, exitButton, addColumnButton;
    private JPanel mainPanel;
    private JLabel tableName;
    static JPanel pane;
    private String query = "";
    private JTextField limitTextField;
    private JTextField tableNameField, columnNameField;
    private final ArrayList<String> dataTable = new ArrayList<>();
    private final ConstraintManager constraintManager = new ConstraintManager();
    private final String[] constraints = {"Not null", "Unique", "Primary key", "Foreign key", "Check", "Default"};
    private final JComboBox<String> constraintPickerComboBox = new JComboBox<>(constraints);
    private CreateTableModel columnToModify = null;

    private final ArrayList<CreateTableModel> dataLine = new ArrayList<>();
    private String[] tabs;

    private CreateTableModel ctm = new CreateTableModel();
    private JPanel foreignKeySectionPanel;
    private GridBagConstraints c2;
    private JComboBox<String> tablesComboBox;
    private JPanel foreignKeyFieldPanel;
    private JComboBox<String> columnsComboBox;
    private boolean foreignKeyPanelActive = false;
    private String selectedReferenceTable = "";

    public CreateTable() {
    }

    /*
     *
     * ========================================================
     *
     */

    public void initialize() {
        constraintManager.clear();

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
                                    disableButton();
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
                            for (CreateTableModel createTableModel : dataLine) {
                                if (createTableModel.getName().equals(id)) {
                                    boolean exist = false;
                                    String[] constraints = {};
                                    if (createTableModel.getConstraintAff().contains("Not null")) {
                                        createTableModel.setConstraintAff(createTableModel.getConstraintAff().replace("Not null", ""));
                                        constraints = createTableModel.getConstraintAff().split(" ");
                                        exist = true;
                                    } else {
                                        constraints = createTableModel.getConstraintAff().trim().split(" ");
                                    }
                                    for (String constraint : constraints) {
                                        constArrayList(constraint);
                                    }
                                    if (exist) {
                                        constArrayList("Not null");
                                    }
                                    if (createTableModel.getKey() != null) {
                                        if (createTableModel.getKey().equals("Foreign key")) {
                                            tablesComboBox.setSelectedItem(createTableModel.getTabSelectForReference());
                                            columns.setSelectedItem(createTableModel.getReferences());
                                            foreignAssociated();
                                        }
                                    }
                                    if (createTableModel.getKey() == null || createTableModel.getKey().equals("Primary key")) {
                                        if (foreignKeySectionPanel != null) {
                                            createTableEntryMainPanel.remove(foreignKeySectionPanel);
                                            Home.content.revalidate();
                                            Home.content.repaint();
                                        }
                                    }
                                    columnToModify = createTableModel;
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

        if (e.getSource() == addColumnButton) {
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
        if (createButton != null) {
            createButton.setEnabled(dataLine.size() > 0); //if there's at least one column we can't disable the create button
            addColumnButton.setEnabled(false);
        }
    }

    /*
     *
     * ========================================================
     *
     */

    public void enableButton() {
        if (createButton != null) {
            createButton.setEnabled(dataLine.size() > 0); //create table button can be enabled if only one column is registered
            addColumnButton.setEnabled(true);
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


    private JPanel createTableEntryMainPanel;

    private void create() {
        createButton = new JButton("Create");
        createButton.setEnabled(false);
        createButton.setPreferredSize(new Dimension(70, 30));

        createButton.addActionListener((ActionEvent e) -> {
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

        createTableEntryMainPanel = new JPanel();
        createTableEntryMainPanel.setLayout(new BorderLayout());

        JPanel northPanel, westPanel;
        northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());
        westPanel = new JPanel();
        westPanel.setLayout(new FlowLayout());

        createTableEntryMainPanel.add(buildColumnDefinitionPanel(), BorderLayout.CENTER);
        new Resize(createTableEntryMainPanel, "tabData");

        Home.content.add(createTableEntryMainPanel, BorderLayout.NORTH);

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

    private boolean internAction = false;

    private JPanel buildColumnDefinitionPanel() {
        GridBagConstraints c = new GridBagConstraints();
        JPanel panel = new JPanel();
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
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(createButton);
        panel.add(exitButton);
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
                    refreshRemoveConstraintComboBox();
                }
            }
        });
        panel.add(cname);
        panel.add(columnNameField);
        panel.add(dataTypeLabel);
        panel.add(comboBoxDataType);
        panel.add(limitLabel);
        panel.add(limitTextField);
        panel.add(addC);
        panel.add(constraintPickerComboBox);
        JLabel remC = new JLabel("Remove constraints");
        panel.add(remC);
        JPanel rcPanel = new JPanel();
        rcPanel.setLayout(new GridBagLayout());
        removeConstraintComboBox = new JComboBox<>(constraintManager.getArray()
        );
        removeConstraintComboBox.addActionListener((ActionEvent e) -> {
            String constraint = (String) removeConstraintComboBox.getSelectedItem();
            if (!internAction) {
                assert constraint != null;
                constraintManager.removeConstraint(constraint);
                refreshRemoveConstraintComboBox();
                if (constraint.equals("Foreign key")) {
                    if (foreignKeySectionPanel != null) {
                        createTableEntryMainPanel.remove(foreignKeySectionPanel);
                        Home.content.revalidate();
                        Home.content.repaint();
                    }
                }
            }
        });
        // rcPanel.add(rcBox);
        panel.add(removeConstraintComboBox);
        addColumnButton = new JButton("Add column");
        addColumnButton.setEnabled(false);
        addColumnButton.addActionListener(e -> {

            if (isInvalidForeignKey()) {
                new PopupMessages().message("Please select a reference!", new IconGenerator().messageIcon());
            } else {
                if (!isColumnExist(columnNameField.getText())) {
                    if (columnToModify != null) {
                        dataLine.remove(columnToModify);
                    }
                    columnToModify = null;
                    ctm.setName(columnNameField.getText());
                    if (hasLimit(ctm.getDatatype()))
                        ctm.setLimit(limitTextField.getText());
                    else
                        ctm.setLimit(null);
                    ctm.setConstraint(constraintAnalysis());
                    dataLine.add(ctm);
                    if (foreignKeySectionPanel != null) {
                        createTableEntryMainPanel.remove(foreignKeySectionPanel);
                        Home.content.revalidate();
                        Home.content.repaint();
                    }
                    Home.content.remove(mainPanel);
                    showTables();
                    panelBuilder.setVisible(true);
                    resetColumnInputFields();
                } else {
                    new PopupMessages().message("two columns can not have same name!", new IconGenerator().exceptionIcon());
                }
            }
        });
        c.gridx = 11;
        c.gridy = 0;
        panel.add(addColumnButton);
        return panel;
    }

    private void resetColumnInputFields() {
        ctm = new CreateTableModel();

        columnNameField.setText("");
        columnNameField.setForeground(Color.BLACK);

        limitTextField.setText("2");
        limitTextField.setForeground(Color.BLACK);

        comboBoxDataType.setSelectedIndex(0); // Reset to default (e.g., "int")
        constraintPickerComboBox.setSelectedIndex(0); // Top level element
        constraintManager.clear();
        refreshRemoveConstraintComboBox();

        disableButton();
    }


    private void refreshRemoveConstraintComboBox() {
        internAction = true;
        removeConstraintComboBox.removeAllItems();
        for (String c : constraintManager.getArray()) {
            removeConstraintComboBox.addItem(c);
        }
        internAction = false;
    }


    private boolean isInvalidForeignKey() {
        return constraintManager.contains("Foreign key") && ctm.getReferences() == null;
    }

    /*
     *
     * ========================================================
     *
     */

    private JComboBox<String> columns;

    private void foreignAssociated() {
        foreignKeyPanelActive = false;
        c2 = new GridBagConstraints();
        c2.insets = new Insets(5, 5, 5, 5);

        foreignKeySectionPanel = new JPanel(new BorderLayout());
        foreignKeyFieldPanel = new JPanel(new FlowLayout());

        tablesComboBox = new JComboBox<>(tabs);
        tablesComboBox.addActionListener((ActionEvent e) -> {
            selectedReferenceTable = Objects.requireNonNull(tablesComboBox.getSelectedItem()).toString();
            try {
                columnsComboBox = new JComboBox<>(new MySQLDaoOperation().selectColumn(selectedReferenceTable));
                ctm.setTabSelectForReference(selectedReferenceTable);

                if (foreignKeyPanelActive) {
                    foreignKeyFieldPanel.removeAll();
                    foreignKeyFieldPanel.revalidate();
                    foreignKeyFieldPanel.repaint();
                    addForeignKeySelectors();
                }

                foreignKeyFieldPanel.add(new JLabel("Column Referenced"));
                foreignKeyFieldPanel.add(columnsComboBox);
                foreignKeyPanelActive = true;

                foreignKeySectionPanel.add(foreignKeyFieldPanel, BorderLayout.EAST);
                createTableEntryMainPanel.add(foreignKeySectionPanel, BorderLayout.SOUTH);
                createTableEntryMainPanel.revalidate();
                createTableEntryMainPanel.repaint();
                Home.content.revalidate();
                Home.content.repaint();

                columnsComboBox.addActionListener((ActionEvent e1) -> {
                    ctm.setReferences(Objects.requireNonNull(columnsComboBox.getSelectedItem()).toString());
                });
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        addForeignKeySelectors();
    }


    /*
     *
     * ========================================================
     *
     */

    private void addForeignKeySelectors() {
        c2.gridx = 0;
        c2.gridy = 0;
        foreignKeyFieldPanel.add(new JLabel("Table"));
        c2.gridx = 1;
        c2.gridy = 0;
        foreignKeyFieldPanel.add(tablesComboBox);
        foreignKeySectionPanel.add(foreignKeyFieldPanel, BorderLayout.EAST);

        createTableEntryMainPanel.add(foreignKeySectionPanel, BorderLayout.SOUTH);
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
        if (constraintManager.contains(constraint)) {
            add = false;
        }
        if (isPrimary && constraintManager.contains("Foreign key")) {
            add = false;
        } else if (isForeign && constraintManager.contains("Primary key")) {
            add = false;
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


    private String queryBuilder(ArrayList<CreateTableModel> qb) {
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

    // TODO: Move this into a separate class, maybe the ConstraintManager one
    public boolean isColumnExist(String name) {
        for (CreateTableModel s : dataLine) {
            if (s.getName().equalsIgnoreCase(name) && columnToModify == null) {
                return true;
            }
        }
        return false;
    }
}
