package dao.sqlite;

import java.sql.*;

public class SQLiteINFO {
    private Connection connect = null;

    public SQLiteINFO() {

        String url = "jdbc:sqlite:lib/sqlite-tools/eddosql.db";

        try {
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connect;
    }

    public void closeConnection(ResultSet resultSet, Connection connection, PreparedStatement preparedStatement) {
        try {

            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
