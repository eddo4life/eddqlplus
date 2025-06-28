package dao.mysql;

import dao.sqlite.SQLiteINFO;
import dao.sqlite.SystemDatabaseTreatment;
import model.ConnectingToolsModel;
import view.iconmaker.IconGenerator;
import view.pupupsmessage.PopupMessages;
import view.tools.Tools;

import java.sql.*;

public class MySQLConnection {

    Tools setting = new Tools();

    public Connection getCon(ConnectingToolsModel ctm) throws SQLException, NullPointerException {

        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + ctm.getHost() + ":" + ctm.getPort() + "/" + ctm.getDbName();
            ;
            con = DriverManager.getConnection(url, ctm.getPortNme(), ctm.getPassword());

            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, "root", "password");
        } catch (ClassNotFoundException ex) {
            new PopupMessages().message(ex.getMessage(), new IconGenerator().exceptionIcon());
        }
        return con;
    }

    public static void closeCon(ResultSet rs, PreparedStatement ps, Connection con) throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (con != null) {
            con.close();
        }
    }

    public String getDbName() {
        return new SystemDatabaseTreatment().getDataConnection().getDbName();
    }

    public void setDbName(String dbNae) {

        String query = "UPDATE mysqlconection SET dbname=?";
        try {
            SQLiteINFO sInfo = new SQLiteINFO();
            Connection connection = sInfo.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, dbNae);
            preparedStatement.executeUpdate();
            sInfo.closeConnection(null, connection, preparedStatement);

        } catch (SQLException e) {
            e.printStackTrace();
            new PopupMessages().message(e.getMessage(), new IconGenerator().exceptionIcon());
        }

    }
}
