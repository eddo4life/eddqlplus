package view;

import java.sql.*;

public class DatabaseMetadataExample {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/immopam";
        String username = "root";
        String password = "password";
        Connection connection = DriverManager.getConnection(url, username, password);
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, "%", new String[] { "TABLE" });
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            System.out.println("Table: " + tableName);
            ResultSet columns = metaData.getColumns(null, null, tableName, "%");
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                System.out.println("  Column: " + columnName + " (" + dataType + ")");
            }
            columns.close();
        }
        tables.close();
        connection.close();
    }
    void test() {
    	
    }
}
