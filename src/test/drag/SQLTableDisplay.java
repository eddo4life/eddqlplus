package test.drag;

import dao.mysql.MySQLConnection;

import javax.swing.*;
import java.sql.*;

public class SQLTableDisplay {
    public static void displayTable(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        int[] columnWidths = new int[columnCount];
        StringBuilder sb = new StringBuilder();

        // Determining the width of each column
        for (int i = 1; i <= columnCount; i++) {
            columnWidths[i - 1] = metaData.getColumnName(i).length();
          //  rs.beforeFirst();
            while (rs.next()) {
                int currentLength = rs.getString(i).length();
                if (currentLength > columnWidths[i - 1]) {
                    columnWidths[i - 1] = currentLength;
                }
            }
        }

        // Adding the headers
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            sb.append(String.format("%-" + (columnWidths[i - 1] + 3) + "s", columnName));
        }
        sb.append("\n");

        // Adding the separating line
        for (int i = 1; i <= columnCount; i++) {
            for (int j = 0; j < columnWidths[i - 1] + 3; j++) {
                sb.append("-");
            }
        }
        sb.append("\n");

        // Adding the data
       // rs.beforeFirst();
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String columnValue = rs.getString(i);
                sb.append(String.format("%-" + (columnWidths[i - 1] + 3) + "s", columnValue));
            }
            sb.append("\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane);
    }

  
  public static void main(String[] args) {
	  
//	  JFrame frame = new JFrame();
//	  JTextArea area= new JTextArea();
//	  frame.setLayout(new BorderLayout());
//
//	  frame.add(new JScrollPane(area));
//	  
//	  frame.setSize(new Dimension(700,400));
//	  
//	  
//	  
//	  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	  
//	  frame.setVisible(true);
	  
	  SQLTableDisplay tableDisplay = new SQLTableDisplay();
	 // votre JTextArea
	  try {
	  Connection connection =  new MySQLConnection().getCon(null);
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("select * from quartier;");
	
		tableDisplay.displayTable(resultSet);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
	
}
}
