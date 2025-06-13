package view;

import dao.DBMS;
import dao.InsertData;
import dao.mysql.MySQLConnection;
import dao.mysql.MySQLDaoOperation;
import dao.oracle.OracleDaoOperation;
import model.InsertList;
import view.iconmaker._Icon;
import view.pupupsmessage.PupupMessages;
import view.tables.Custom;
import view.tables.JTableUtilities;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Insertion {


    public Insertion() {
    }

    public static ArrayList<String> header;

    public void selectTable(String name) {
        tabName = name;
        try {
            ArrayList<Object> selectTable;
            if (DBMS.dbms == 1) {
                selectTable = new MySQLDaoOperation().selectTable(name);
            } else {
                selectTable = new OracleDaoOperation().showDataFrom(name);
            }

            JPanel pane = new JPanel();
            pane.setLayout(new GridBagLayout());
            JTable table = new JTable();
            table.setEnabled(false);
            JPanel intern = new JPanel();
            intern.setLayout(new BorderLayout());
            header = new ArrayList<>();
            header = (ArrayList<String>) selectTable.get(0);
            ArrayList<String> data;
            data = (ArrayList<String>) selectTable.get(1);

            String[] head = header.toArray(new String[0]);
            
            int i = 0, k = 0;
            Object[][] obj = new String[data.size() / header.size()][header.size()];
            for (String d : data) {
                if (d == null) {
                    obj[i][k] = "null";
                } else {
                    obj[i][k] = d.toLowerCase();
                }
                k++;
                if (k % header.size() == 0) {
                    i++;
                    k = 0;
                }
            }
            table.setModel(new DefaultTableModel(obj, head));
            if (header.size() > 15) {
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            }
            JTableUtilities.setCellsAlignment(table, SwingConstants.CENTER, 0);
            Custom tabCustom = new Custom(table, false, false, 30, null, null);
            intern.add(tabCustom.getScrollPane());
            pane.setLayout(new BorderLayout());
            JPanel titlePanel = new JPanel();
            JLabel titleLabel = new JLabel(new MySQLConnection().getDbName() + "/" + tabName);

            titlePanel.add(titleLabel);
            pane.add(titlePanel, BorderLayout.NORTH);
            pane.add(intern, BorderLayout.CENTER);
            mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(pane, BorderLayout.CENTER);
            Home.content.removeAll();
            Home.content.add(mainPanel, BorderLayout.CENTER);
            if (insertPanel != null) {
                insertPanel.removeAll();
                insertPanel.revalidate();
                insertPanel.repaint();
            }
            insert();
            Home.content.revalidate();
            Home.content.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
            // new CreateTable().warning();
        }

    }

    JPanel insertPanel = new JPanel();
    public static ArrayList<Object> st;

    void insert() {
        insertPanel.setLayout(new BorderLayout());
        String[] head = new String[header.size()];
        st = new ArrayList<>();
        ArrayList<InsertList> list = new ArrayList<InsertList>();
        InsertList insertList = new InsertList(st);
        list.add(insertList);

        ArrayList<String> dataType = null;
        try {
            if (DBMS.dbms == 1) {
                dataType = new MySQLDaoOperation().getDataType(tabName);
            } else if (DBMS.dbms == 2) {

                dataType = new OracleDaoOperation().getDataType(tabName);
            }
        } catch (Exception e) {
            new PupupMessages().message(e.getMessage(), new _Icon().exceptionIcon());
            new TablesSections().saisie();
        }

        LocalDate ld = LocalDate.now();
        int m = 0;
        for (String hd : header) {
            assert dataType != null;
            String data = dataType.get(m);
            head[m] = hd + " (" + data + ")";
            if (data.equalsIgnoreCase("date")) {
                st.add(ld);
            } else {
                st.add(null);
            }
            m++;
        }

        InsertData data = new InsertData(head, list);
        JTable table = new JTable(data);
        if (header.size() > 15) {
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
        table.setCellSelectionEnabled(true);
        table.setFocusable(false);
        JTableUtilities.setCellsAlignment(table, SwingConstants.CENTER, 0);
        insertPanel.setPreferredSize(new Dimension(700, 100));

        insertPanel.add(new Custom(table, false, false, 40, null, null).getScrollPane(), BorderLayout.CENTER);
        table.setShowHorizontalLines(true);
        JButton insert = new JButton("Insert");
        insert.setFocusable(false);
        insert.addActionListener((e) -> {
            new PupupMessages().confirm("Did you hit enter to save all the data?");
            if (PupupMessages.getAction == 1) {
                if (InsertList.list.contains("")) {
                    new PupupMessages().confirm("Some value(s) are however null, wanna review the insertion?");
                    if (PupupMessages.getAction == 0) {
                        doIt();
                    } else if (PupupMessages.getAction == -1) {
                        new PupupMessages().message("Insertion canceled", new _Icon().messageIcon());
                    }
                } else {
                    execute(InsertList.list);
                }
            } else if (PupupMessages.getAction == 0) {
                new PupupMessages().confirm("Do you wanna continue with the process?");
                if (PupupMessages.getAction == 1) {
                    doIt();
                } else {
                    new PupupMessages().message("Insertion canceled", new _Icon().messageIcon());
                }
            }

        });

        insertPanel.add(insert, BorderLayout.EAST);
        Home.content.add(insertPanel, BorderLayout.NORTH);
    }

    public void doIt() {
        int count = 0;
        for (Object empty : InsertList.list) {
            if (empty != null) {
                if (empty.equals("")) {
                    InsertList.list.set(count, null);
                }
            }
            count++;
        }
        execute(InsertList.list);
    }

    public void execute(ArrayList<Object> data) {
        try {
            int exec = 0;
            if (DBMS.dbms == 1) {
                exec = new MySQLDaoOperation().insert(tabName, data);
            } else if (DBMS.dbms == 2) {
                exec = new OracleDaoOperation().insert(tabName, data);
            }
            if (exec > 0) {
                new PupupMessages().message("Insertion successful!", new _Icon().succesIcon());
                selectTable(tabName);
            }
        } catch (SQLException e) {
            new PupupMessages().message("Process failed! " + e.getMessage(), new _Icon().failedIcon());
        }
    }

    /*
     *
     *
     *
     */

    public static String tabName;
    public static JLabel tableName = new JLabel();
    public static JTextField tableNameField = new JTextField();
    JPanel mainPanel;
}
