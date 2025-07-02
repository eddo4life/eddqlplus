package view;

import dao.DBMS;
import dao.mysql.MySQLConnection;
import dao.mysql.MySQLDaoOperation;
import dao.oracle.OracleDaoOperation;
import eddql.launch.LoadData;
import model.ShowTablesModel;
import view.tables.Custom;
import view.tables.JTableUtilities;
import view.tables.Sort;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;
import java.sql.SQLException;
import java.util.ArrayList;

public class TableSelector {

    public static ArrayList<String> tablesListNames = new ArrayList<>();
    public static JTable table;
    public static ArrayList<String> arr = null;
    private JPanel pane;

    public TableSelector() {
    }

    public JPanel showTables() {
        try {
            ArrayList<String> showtables;
            if (DBMS.dbms == 1) {
                showtables = new MySQLDaoOperation().showTables();
            } else {
                showtables = new OracleDaoOperation().showTables();
            }
            arr = showtables;
            String title = createTitle(showtables);

            pane = new JPanel();
            pane.setLayout(new GridBagLayout());
            table = new JTable();

            setupTableMouseListener();

            Home.frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            Object[][] tableData = createTableData(showtables.size());
            DefaultTableModel model = createTableModel(tableData);
            table.setModel(model);
            Home.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            JPanel intern = new JPanel();
            intern.setLayout(new BorderLayout());
            tableSetup(title, intern, table, pane);

            new Sort().tableSortFilter(table);

        } catch (SQLException e) {
            handleDatabaseException(e);
        } catch (Exception e) {
            reloadTablesSection();
        }
        return pane;
    }

    private void setupTableMouseListener() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    String tab = String.valueOf(table.getValueAt(table.getSelectedRow(), 1));
                    switch (TablesSections.optionChoice) {
                        case 0 -> new TablesSections().selectTable(tab);
                        case 1 -> new Insertion().selectTable(tab);
                        case 2 -> new Modification(tab);
                    }
                    table.setEnabled(false);
                } catch (Exception ignored) {
                }
            }
        });
    }

    private Object[][] createTableData(int size) {
        Object[][] obj = new Object[size][6];
        int i = 0;
        for (ShowTablesModel data : LoadData.tables) {
            obj[i][0] = i + 1;
            obj[i][1] = data.getNames();
            obj[i][2] = data.getColumnCount();
            obj[i][3] = data.getRowCount();
            obj[i][4] = data.getDate();
            obj[i][5] = data.getTime();
            tablesListNames.add(data.getNames().toUpperCase());
            i++;
        }
        return obj;
    }

    private DefaultTableModel createTableModel(Object[][] data) {
        return new DefaultTableModel(data, new Object[]{"#", "Names", "Columns count", "Rows count", "Date", "Time"}) {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return new Class[]{Integer.class, String.class, Integer.class, Integer.class,
                        String.class, String.class}[columnIndex];
            }
        };
    }

    private String createTitle(ArrayList<String> showtables) {
        if (showtables.isEmpty()) {
            return "";
        }
        String dbName = new MySQLConnection().getDbName();
        return showtables.size() == 1 ?
                "(1) table from " + dbName :
                "(" + showtables.size() + ") tables from " + dbName;
    }

    static void tableSetup(String title, JPanel intern, JTable table, JPanel pane) {
        JTableUtilities.setCellsAlignment(table, SwingConstants.CENTER, 0);
        Custom tabCustom = new Custom(table, false, false, 30, null, null);
        intern.add(tabCustom.getScrollPane());

        JPanel tableNorth = new JPanel();
        tableNorth.setLayout(new BorderLayout());
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel(title);
        titlePanel.add(titleLabel);
        tableNorth.add(titlePanel, BorderLayout.WEST);
        pane.setLayout(new BorderLayout());
        pane.add(tableNorth, BorderLayout.NORTH);
        pane.add(intern, BorderLayout.CENTER);
    }

    private void handleDatabaseException(SQLException e) {
        try {
            new DatabaseSection().dataBases();
        } catch (SQLException e1) {
            new EditorSection(e1);
        }
    }

    private void reloadTablesSection() {
        new LoadData().tablesSectionLoader();
        showTables();
    }
}