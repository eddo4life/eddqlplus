package view;

import dao.mysql.MySQLConnection;
import dao.sqlite.SystemDatabaseTreatment;
import model.ConnectingToolsModel;

import javax.swing.*;
import java.sql.*;

public class MySQLTextAreaExample extends JTextArea {

	public MySQLTextAreaExample(String query) {
		ConnectingToolsModel toolsModel = new SystemDatabaseTreatment().getDataConnection();
		MySQLConnection connection = new MySQLConnection();

		executeQuery(query, toolsModel, connection);
	}

	void executeQuery(String query, ConnectingToolsModel toolsModel, MySQLConnection connection) {
		try {
			Connection conn = connection.getCon(toolsModel);

			// Create a scrollable, insensitive ResultSet
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			// Execute the query and retrieve the result set
			ResultSet rs = stmt.executeQuery(query);

			// Move the cursor to before the first row
			rs.beforeFirst();

			// Get the column names and calculate the maximum width of each column
			ResultSetMetaData rsmd = rs.getMetaData();
			int numColumns = rsmd.getColumnCount();
			String[] columnNames = new String[numColumns];
			int[] maxColumnWidths = new int[numColumns];
			for (int i = 1; i <= numColumns; i++) {
				columnNames[i - 1] = rsmd.getColumnName(i);
				maxColumnWidths[i - 1] = columnNames[i - 1].length();
			}

			// Iterate over the rows to calculate the maximum width of each column based on
			// the longest value in the column
			while (rs.next()) {
				for (int i = 1; i <= numColumns; i++) {
					String columnValue = rs.getString(i);
					if (columnValue != null && columnValue.length() > maxColumnWidths[i - 1]) {
						maxColumnWidths[i - 1] = columnValue.length();
					}
				}
			}

			// Create the border using "+" and "-" characters
			StringBuilder borderBuilder = new StringBuilder("+");
			for (int i = 0; i < numColumns; i++) {
				borderBuilder.append("-".repeat(Math.max(0, maxColumnWidths[i] + 2)));
				borderBuilder.append("+");
			}
			borderBuilder.append("\n");
			String border = borderBuilder.toString();

			// Add the column names to the text area
			append(border);
			for (int i = 0; i < numColumns; i++) {
				String columnName = columnNames[i];
				int columnWidth = maxColumnWidths[i];
				append(String.format("| %-" + columnWidth + "s ", columnName));
			}
			append("|\n");
			append(border);

			// Add the rows to the text area
			rs.beforeFirst();
			while (rs.next()) {
				for (int i = 1; i <= numColumns; i++) {
					String columnValue = rs.getString(i);
					int columnWidth = maxColumnWidths[i - 1];
					append(String.format("| %-" + columnWidth + "s ", columnValue));
				}
				append("|\n");
			}
			append(border);

			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException ex) {
			if (ex.getErrorCode() == 0) {
				executeUpdate(query, toolsModel, connection);
			} else {
				append(ex.getMessage() + "\n");
			}

		}
	}

	void executeUpdate(String query, ConnectingToolsModel toolsModel, MySQLConnection connection) {
		try {
			Connection conn = connection.getCon(toolsModel);
			Statement statement = conn.createStatement();
			int res = statement.executeUpdate(query);
			if (res > 0) {
				append(res + " row(s) affected\n");
			}
		} catch (NullPointerException | SQLException e1) {
			append(e1.getMessage() + "\n");
		}
	}

}
