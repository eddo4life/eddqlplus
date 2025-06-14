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

    public TableSelector() {
        // TODO Auto-generated constructor stub
    }

    public static ArrayList<String> tablesListNames = new ArrayList<>();
    public static JTable table;
    public static ArrayList<String> arr = null;
    String[] tabs;
    JPanel pane;

    public JPanel showTables() {

        try {
            ArrayList<String> showtables;
            if (DBMS.dbms == 1) {
                showtables = new MySQLDaoOperation().showTables();
            } else {
                showtables = new OracleDaoOperation().showTables();
            }
            arr = showtables;
            String title = "(" + showtables.size() + ") tables from " + new MySQLConnection().getDbName();
            if (showtables.size() == 1) {
                title = "(1) table from " + new MySQLConnection().getDbName();
            }
            if (showtables.isEmpty()) {
                title = "";
            }
            pane = new JPanel();
            pane.setLayout(new GridBagLayout());
            table = new JTable();

            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        String tab = String.valueOf(table.getValueAt(table.getSelectedRow(), 1));
                        switch (TablesSections.optionChoice) {
                            case 0 -> new TablesSections().selectTable(tab);
                            case 1 -> new Insertion().selectTable(tab);
                            //case 2 -> new Modification().selectTable(tab);
                            case 2 -> new Modification(tab);
                        }
                        table.setEnabled(false);
                    } catch (Exception ignored) {
                    }
                }
            });

            JPanel intern = new JPanel();
            intern.setLayout(new BorderLayout());
            Object[] header = {"#", "Names", "Columns count", "Rows count", "Date", "Time"};
            int i = 0, j = showtables.size(), k = 1;
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
                tablesListNames.add(data.getNames().toUpperCase());
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
                @Serial
                private static final long serialVersionUID = 1L;

                public Class<?> getColumnClass(int columnIndex) {
                    return columnClass[columnIndex];
                }
            };
            table.setModel(defaultTableModel);
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

            Home.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            new Sort().tableSortFilter(table);

        } catch (SQLException e) {
            //	new PupupMessages().message(e.getMessage(), new _Icon().exceptionIcon());
            try {
                new DataBase_section().dataBases();
            } catch (SQLException e1) {
                new Editor_section(e1);
            }
        } catch (Exception e) {
            new LoadData().tablesSectionLoader();
            showTables();
        }
        return pane;
    }
}
