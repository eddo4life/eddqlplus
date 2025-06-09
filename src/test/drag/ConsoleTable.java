package test.drag;

import dao.mysql.MySQLConnection;

import java.sql.*;
import java.util.Arrays;

public class ConsoleTable{
  public void displayTable(ResultSet resultSet, boolean adjustColumnWidth) throws SQLException {
    ResultSetMetaData metaData = resultSet.getMetaData();
    int columnCount = metaData.getColumnCount();
    int[] columnWidths = new int[columnCount];

    if (adjustColumnWidth) {
      for (int i = 1; i <= columnCount; i++) {
        columnWidths[i - 1] = metaData.getColumnDisplaySize(i);
      }
    } else {
      Arrays.fill(columnWidths, 15);
    }

    // Print the header
    System.out.print("+");
    for (int i = 0; i < columnCount; i++) {
      for (int j = 0; j < columnWidths[i]; j++) {
        System.out.print("-");
      }
      System.out.print("+");
    }
    System.out.println();

    for (int i = 1; i <= columnCount; i++) {
      String columnName = metaData.getColumnName(i);
      System.out.printf("| %-" + columnWidths[i - 1] + "s ", columnName);
    }
    System.out.println("|");

    System.out.print("+");
    for (int i = 0; i < columnCount; i++) {
      for (int j = 0; j < columnWidths[i]; j++) {
        System.out.print("-");
      }
      System.out.print("+");
    }
    System.out.println();

    // Print the data
    while (resultSet.next()) {
      for (int i = 1; i <= columnCount; i++) {
        String columnValue = resultSet.getString(i);
        System.out.printf("| %-" + columnWidths[i - 1] + "s ", columnValue);
      }
      System.out.println("|");
    }

    System.out.print("+");
    for (int i = 0; i < columnCount; i++) {
      for (int j = 0; j < columnWidths[i]; j++) {
        System.out.print("-");
      }
      System.out.print("+");
    }
    System.out.println();
  }

  
  public static void main(String[] args) {
	  
	  
	  try {
	      // Connect to the database and get a result set
	      Connection connection =  new MySQLConnection().getCon(null);
	      Statement statement = connection.createStatement();
	      ResultSet resultSet = statement.executeQuery("select * from quartier;");

	      // Create the SQL table display and display the result set
	      ConsoleTable tableDisplay = new  ConsoleTable();
	      tableDisplay.displayTable(resultSet,true);
	      
	    } catch (SQLException e) {
	      e.printStackTrace();
	    }
	  
	
}
}
