package view;

import dao.DBMS;
import dao.mysql.MySQLConnection;
import dao.mysql.MySQLDaoOperation;
import dao.oracle.OracleDaoOperation;
import view.tables.Custom;
import view.tables.JTableUtilities;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class TableToBeSelected {

	public TableToBeSelected() {
		
	}

	public static String[] head;

	@SuppressWarnings("unchecked")
	public JPanel select(String name) {
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		try {
			ArrayList<Object> selectTable=null;
			if (DBMS.dbms == 1) {
				selectTable = new MySQLDaoOperation().selectTable(name);
			} else if (DBMS.dbms == 2){
				selectTable = new OracleDaoOperation().showDataFrom(name);
			}

			JTable table = new JTable();
			table.setEnabled(false);
			JPanel intern = new JPanel();
			intern.setLayout(new BorderLayout());
			ArrayList<String> header;
			assert selectTable != null;
			header = (ArrayList<String>) selectTable.get(0);
			ArrayList<String> data;
			data = (ArrayList<String>) selectTable.get(1);

			 head = new String[header.size()];
			int m = 0;
			for (String hd : header) {
				head[m] = hd;
				m++;
			}
			int i = 0, k = 0;
			Object[][] obj = new String[data.size() / header.size()][header.size()];
			for (String d : data) {

				if (i < data.size() / header.size()) {
					obj[i][k] = Objects.requireNonNullElse(d, "null");
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
			
			JPanel titlePanel = new JPanel();
			
			JLabel titleLabel ;
			
			if(DBMS.dbms == 1) {
			 titleLabel = new JLabel(new MySQLConnection().getDbName() + "." + name);
			}else {
				//getUser
				 titleLabel = new JLabel("SCOTT" + "." + name);
			}
			
			titlePanel.add(titleLabel);
			
			pane.add(titlePanel, BorderLayout.NORTH);
			pane.add(intern, BorderLayout.CENTER);
			

		} catch (SQLException e) {
			e.printStackTrace();
			//new CreateTable().warning();
		}
		return pane;
	}
}
