package view.terminal;

import dao.DBMS;
import dao.mysql.MySQLDaoOperation;
import dao.oracle.OracleDaoOperation;
import view.EditorSection;
import view.Home;
import view.tables.Custom;
import view.tables.JTableUtilities;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class Terminal {
    private final JPanel mainPanel = new JPanel(new BorderLayout());
    private final JPanel messagePanel = new JPanel(null);
    private final JPanel outputPanel = new JPanel(new BorderLayout());
    private final JLabel exceptionLabel = new JLabel("");
    private final JPanel tablePanel = new JPanel(new BorderLayout());
    private final JTable table = new JTable();
    private final JTableHeader tableHeader = table.getTableHeader();

    public Terminal(String query, boolean isShow, String feedback) {

        setPanels();

        if (!feedback.isBlank()) {
            exceptionLabel.setText(feedback);
            setSize(50);
        } else {
            display(query, isShow);
        }
    }

    private void setPanels() {
        mainPanel.add(messagePanel, BorderLayout.NORTH);
        messagePanel.setPreferredSize(new Dimension(0, 30));
        mainPanel.add(outputPanel, BorderLayout.CENTER);
        outputPanel.add(tablePanel, BorderLayout.CENTER);
        outputPanel.add(exceptionLabel, BorderLayout.SOUTH);

        // Editor.components.add(messagePanel);

        JLabel titleLabel = new JLabel("Output ::");
        titleLabel.setFont(new Font("", Font.ITALIC, 13));
        exceptionLabel.setFont(new Font("", Font.PLAIN, 14));
        //titleLabel.setForeground(Color.darkGray);
        titleLabel.setBounds(5, -5, 50, 30);
        messagePanel.add(titleLabel);
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(Home.frame.getWidth() - 70, 2));
        separator.setBounds(50, 10, 4000, 2);
        messagePanel.add(separator);

    }

    private void setSize(int y) {
        EditorSection.terminPanel.removeAll();
        EditorSection.terminPanel.add(mainPanel, BorderLayout.SOUTH);
        EditorSection.terminPanel.revalidate();
        EditorSection.terminPanel.repaint();
        mainPanel.setPreferredSize(new Dimension(100, y));
    }

    @SuppressWarnings("unchecked")
    public void display(String query, boolean isShow) {
        int length = 50;
        String space = "          ";
        try {
            ArrayList<Object> selectTable = new ArrayList<>();
            if (DBMS.dbms == 2) {
                // oracle does support;
                query = query.replace(";", "");
                selectTable = new OracleDaoOperation().select(query);
            } else if (DBMS.dbms == 1) {
                selectTable = new MySQLDaoOperation().select(query);
            }

            // table.removeAll();

            ArrayList<String> header = new ArrayList<>();
            header = (ArrayList<String>) selectTable.get(0);
            ArrayList<String> data = (ArrayList<String>) selectTable.get(1);

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
                    if (length < 250) {
                        length += 30;
                    }
                }
            }


            DefaultTableModel model = new DefaultTableModel(obj, head) {
                /**
                 *
                 */

                @Override
                public boolean isCellEditable(int i, int i1) {
                    return false;
                }
            };
            table.setEnabled(false);
            table.setModel(model);
            table.setFont(new Font("Calibri", Font.CENTER_BASELINE, 15));
            if (header.size() > 15) {
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            }

            tableHeader.setUI(new javax.swing.plaf.basic.BasicTableHeaderUI());

            tableHeader.setFont(new Font("sanserif", Font.BOLD, 13));
            if (!isShow)
                JTableUtilities.setCellsAlignment(table, SwingConstants.CENTER, 0);
            else {
                JTableUtilities.setCellsAlignment(table, SwingConstants.LEFT, 0);
            }

            Custom tabCustom = new Custom(table, false, false, 30, null, null);
            if (!data.isEmpty()) {

                tablePanel.add(tabCustom.getScrollPane());
                setSize(length);
                exceptionLabel.setText("");
            } else {
                exceptionLabel.setText(space + "Empty result...");
                setSize(length);
            }
        } catch (SQLException e) {
            setSize(length);
            exceptionLabel.setText(space + e.getMessage());
        }
    }

}
