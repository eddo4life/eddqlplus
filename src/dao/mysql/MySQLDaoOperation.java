package dao.mysql;

import com.mysql.cj.jdbc.result.ResultSetMetaData;
import dao.sqlite.SystemDatabaseTreatment;
import eddql.launch.LoadData;
import export.ExcelExporter;
import model.ConnectingToolsModel;
import view.DisplayConsole;
import view.Insertion;
import view.tools.Tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLDaoOperation {
    MySQLConnection connection = new MySQLConnection();
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public MySQLDaoOperation() {
        new Tools();
    }

    ConnectingToolsModel toolsModel = new SystemDatabaseTreatment().getDataConnection();

    public ArrayList<String> showDataBases() throws SQLException, NullPointerException {
        ArrayList<String> dataBases = new ArrayList<>();
        String requete = " SHOW DATABASES ";
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(requete);
        rs = pst.executeQuery();
        while (rs.next()) {
            dataBases.add(rs.getString(1));
        }

        MySQLConnection.closeCon(rs, pst, con);
        return dataBases;

    }

    /*
     * =============================================================
     */

    public int create(String name) throws SQLException {
        String requete = " CREATE DATABASE " + name;
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(requete);
        int x = pst.executeUpdate();
        MySQLConnection.closeCon(rs, pst, con);
        return x;
    }

    /*
     * =============================================================
     */

    public void use(String name) throws SQLException {
        String requete = " USE " + name;
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(requete);
        pst.executeUpdate();
        MySQLConnection.closeCon(rs, pst, con);
    }

    /*
     * =============================================================
     */

    public int deleteDb(String name) throws SQLException {
        String requete = " DROP DATABASE " + name;
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(requete);
        int x = pst.executeUpdate();
        MySQLConnection.closeCon(rs, pst, con);
        return x;
    }

    /*
     * =============================================================
     */

    public ArrayList<String> showTables() throws SQLException {
        ArrayList<String> dataBases = new ArrayList<>();
        String requete = " SHOW TABLES ";
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(requete);
        rs = pst.executeQuery();
        while (rs.next()) {
            dataBases.add(rs.getString(1).toLowerCase());
        }

        MySQLConnection.closeCon(rs, pst, con);
        return dataBases;

    }

    /*
     * =============================================================
     */

    public int getColumn(String name) throws SQLException {
        String requete = " DESC " + name;
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(requete);
        rs = pst.executeQuery();
        int count = 0;

        while (rs.next()) {
            count++;
        }

        MySQLConnection.closeCon(rs, pst, con);
        return count;

    }

    /*
     * =============================================================
     */

    public int getRows(String name) throws SQLException {
        String requete = "SELECT * from " + name;
        int count = 0;
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(requete);
        rs = pst.executeQuery();
        while (rs.next()) {
            count++;
        }
        MySQLConnection.closeCon(rs, pst, con);
        return count;
    }

    /*
     * =============================================================
     */

    public int createTable(String query) throws SQLException {
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(query);
        int x = pst.executeUpdate();

        MySQLConnection.closeCon(rs, pst, con);
        return x;
    }

    /*
     * =============================================================
     */
//manage the insertion section
    public ArrayList<Object> selectTable(String name) throws SQLException {
        ArrayList<Object> pack = new ArrayList<>();
        ArrayList<String> header = new ArrayList<>();
        ArrayList<String> data = new ArrayList<>();
        String requete = "SELECT * from " + name;
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(requete);
        rs = pst.executeQuery();
        if (rs != null) {
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                header.add(rs.getMetaData().getColumnName(i));
//				System.out.println("header.add(\"" + rs.getMetaData().getColumnName(i) + "\")");
            }
            while (rs.next()) {
                for (String s : header) {
                    data.add(rs.getString(s));
//					System.out.println("content.add(\"" + rs.getString(header.get(i)) + "\");");
                }
            }

        }

        Thread t2 = new Thread(() -> new DisplayConsole(header, data));

        t2.start();


        pack.add(header);
        pack.add(data);
        return pack;
    }

    /*
     * =============================================================
     */

    public String[] selectColumn(String name) throws SQLException {
        String[] arr = null;
        String requete = "SELECT * from " + name;
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(requete);
        rs = pst.executeQuery();
        if (rs != null) {

            ResultSetMetaData columns = (ResultSetMetaData) rs.getMetaData();
            int i = 0, j = columns.getColumnCount();
            arr = new String[j];
            while (i < j) {
                arr[i] = columns.getColumnName(i + 1);
                i++;
            }
        }
        return arr;
    }

    /*
     * =============================================================
     */

    public int insert(String name, ArrayList<Object> build) throws SQLException {

        StringBuilder builder = new StringBuilder();
        for (@SuppressWarnings("unused")
                Object data : build) {
            builder.append("?,");
        }
        if (!builder.toString().isBlank())
            builder = new StringBuilder(builder.substring(0, builder.length() - 1));

        String requete = " INSERT INTO " + name + " VALUES(" + builder + ");";

        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(requete);
        int count = 1;
        for (Object data : build) {
            pst.setObject(count, data);
            count++;
        }
        int i = pst.executeUpdate();
        MySQLConnection.closeCon(rs, pst, con);
        return i;
    }

    /*
     * =============================================================
     */

    public int delete(String name, String condition) throws SQLException {

        String requete = "DELETE FROM " + name + " WHERE " + condition;
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(requete);
        int x = pst.executeUpdate();
        MySQLConnection.closeCon(rs, pst, con);

        return x;
    }

    /*
     * =============================================================
     */

    public int deleteAll(String name) throws SQLException {
        String query = "DELETE FROM " + name;
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(query);
        int x = pst.executeUpdate();
        MySQLConnection.closeCon(rs, pst, con);
        return x;
    }

    /*
     * =============================================================
     */

    public int dropColumn(String name, String col) throws SQLException {

        String query = "ALTER TABLE " + name + " DROP COLUMN " + col;
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(query);
        int x = pst.executeUpdate();
        MySQLConnection.closeCon(rs, pst, con);
        return x;

    }

    /*
     * =============================================================
     */

    public int renameTable(String name1, String name2) throws SQLException {

        String query = "ALTER TABLE " + name1 + " RENAME TO " + name2;
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(query);
        int x = pst.executeUpdate();
        MySQLConnection.closeCon(rs, pst, con);
        return x;

    }

    /*
     * =============================================================
     */

    public int renameColumn(String tab, String col, String colNme) throws SQLException {
        String query = "ALTER TABLE " + tab + " RENAME COLUMN " + col + " TO " + colNme;
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(query);
        int x = pst.executeUpdate();
        MySQLConnection.closeCon(rs, pst, con);
        return x;

    }

    /*
     * =============================================================
     */

    public int addColumn(String addCol, String name) throws SQLException {

        String query = "ALTER TABLE " + name + " ADD COLUMN " + addCol;
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(query);
        int x = pst.executeUpdate();
        MySQLConnection.closeCon(rs, pst, con);
        return x;

    }

    /*
     * =============================================================
     */

    public void addConstraint() {
        // tough

    }

    /*
     * =============================================================
     */

    public void changeDataType() {

    }

    /*
     * =============================================================
     */

    public ArrayList<String> getDate() throws SQLException {
        String query = "SELECT create_time FROM INFORMATION_SCHEMA.TABLES WHERE table_schema ='"
                + new MySQLConnection().getDbName() + "';";
        ArrayList<String> date = new ArrayList<String>();
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(query);
        rs = pst.executeQuery();
        while (rs.next()) {
            date.add(rs.getString(1));
        }
        MySQLConnection.closeCon(rs, pst, con);
        return date;
    }

    /*
     * =============================================================
     */

    public ArrayList<String> getDataType(String name) throws SQLException {

        String query = "SELECT DATA_TYPE,COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where table_schema = '"
                + new MySQLConnection().getDbName() + "' and table_name = '" + name + "'" + ";";
        ArrayList<String> date = new ArrayList<String>();
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(query);
        rs = pst.executeQuery();
        while (rs.next()) {
            date.add(rs.getString(1) + " " + rs.getString(2));
        }
        MySQLConnection.closeCon(rs, pst, con);
        ArrayList<String> alternative = new ArrayList<String>();
        ArrayList<String> head = Insertion.header;
        for (String hd : head) {
            for (String x : date) {
                String[] tab = x.split(" ");
                if (hd.equals(tab[1])) {
                    alternative.add(tab[0]);
                }
            }
        }
        return alternative;
    }

    /*
     * =============================================================
     */
    public int update(String query) throws SQLException {
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(query);
        int n = pst.executeUpdate();
        MySQLConnection.closeCon(rs, pst, con);
        return n;

    }

    /*
     * =============================================================
     */

    public int showTablesFrom(String name) throws SQLException {
        String requete = "SHOW TABLES FROM " + name;
        int count = 0;
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(requete);
        rs = pst.executeQuery();
        while (rs.next()) {
            count++;
        }
        MySQLConnection.closeCon(rs, pst, con);
        return count;
    }

    /*
     * =============================================================
     */

    public String getDbOldestDate(String name) {
        String query = "SELECT table_schema AS Database_Name, MIN(create_time) AS Creation_Time FROM information_schema.tables WHERE table_schema = '"
                + name + "' Group by table_schema;";
        String date = "... ...";
        try {
            con = connection.getCon(toolsModel);
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                date = rs.getString(2);
            }
            MySQLConnection.closeCon(rs, pst, con);
        } catch (Exception ignored) {
        }
        if (date == null) {
            return "null null";
        }
        return date;
    }

    /*
     * =============================================================
     */

    public String getDbLatestDate(String name) {
        String query = "SELECT table_schema AS Database_Name, MAX(create_time) AS Creation_Time FROM information_schema.tables WHERE table_schema = '"
                + name + "' Group by table_schema;";
        String date = "... ...";
        try {
            con = connection.getCon(toolsModel);
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                date = rs.getString(2);
            }
            MySQLConnection.closeCon(rs, pst, con);
        } catch (Exception ignored) {
        }
        if (date == null) {
            return "null null";
        }
        return date;
    }

    /*
     * =============================================================
     */

    public ArrayList<Object> select(String query) throws SQLException {
        ArrayList<Object> pack = new ArrayList<>();
        ArrayList<String> header = new ArrayList<>();
        ArrayList<String> data = new ArrayList<>();
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(query);
        rs = pst.executeQuery();
        if (rs != null) {
            ResultSetMetaData columns = (ResultSetMetaData) rs.getMetaData();
            int i = 0;
            while (i < columns.getColumnCount()) {
                i++;
                header.add(columns.getColumnName(i));
            }
            while (rs.next()) {
                for (i = 0; i < header.size(); i++) {
                    data.add(rs.getString(header.get(i)));
                    // System.out.print(rs.getString(header.get(i))+"||\t");
                }
            }

        }
        pack.add(header);
        pack.add(data);
        return pack;
    }

    /*
     * =============================================================
     */

    public int executeUpdate(String query) throws SQLException {

        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(query);
        int n = pst.executeUpdate();

        MySQLConnection.closeCon(rs, pst, con);
        return n;
    }

    /*
     * =============================================================
     */
    public void dropTable(String name) throws SQLException {
        String query = "Drop table " + name;
        con = connection.getCon(toolsModel);
        pst = con.prepareStatement(query);
        pst.executeUpdate();
        MySQLConnection.closeCon(rs, pst, con);
        new LoadData().tablesSectionLoader();
    }

    public String export(String path, String table) throws SQLException, IOException {
        // Set up file output parameters
        String fileName = table + ".sql";

        File file = new File(path + "/" + fileName);
        System.out.println(file.getAbsolutePath());
        if (!file.exists()) {

            file.createNewFile();

        }

        con = connection.getCon(toolsModel);
        Statement stmt = con.createStatement();
        FileWriter writer = new FileWriter(file);
        // Get table structure
        ResultSet structureRs = stmt.executeQuery("DESCRIBE " + table);
        StringBuilder create = new StringBuilder("Create table " + table + "(\n");
        int c = 0;
        List<String> dataList = new ArrayList<>();
        dataList.add("");
        while (structureRs.next()) {
            c++;
            String field = structureRs.getString("Field");
            String type = structureRs.getString("Type");
            create.append(field).append(" ").append(type).append(",\n");
            dataList.add(type);
            // writer.write("ALTER TABLE " + table + " ADD COLUMN " + field + " " + type +
            // ";\n");
        }

        if (c != 0)
            create = new StringBuilder(create.substring(0, create.length() - 2));

        create.append("\n);\n\n");
        writer.write(create.toString());

        // Get table data
        ResultSet dataRs = stmt.executeQuery("SELECT * FROM " + table);

        while (dataRs.next()) {
            StringBuilder values = new StringBuilder();
            for (int i = 1; i <= dataRs.getMetaData().getColumnCount(); i++) {
                Object value = dataRs.getObject(i);
                if (value != null) {

                    if (dataList.get(i).contains("varchar")) {
                        values.append("'").append(value.toString().replace("'", "''")).append("',");
                    } else {
                        values.append(value.toString()).append(",");
                    }
                } else {
                    values.append("null,");
                }
            }
            values.deleteCharAt(values.length() - 1);
            writer.write("INSERT INTO " + table + " VALUES (" + values.toString() + ");\n");
        }
        writer.close();
        MySQLConnection.closeCon(rs, pst, con);
        return null;

    }

    public void exportToExcel(String path, String table) throws Exception {

        String query = "SELECT * FROM " + table;

        con = connection.getCon(toolsModel);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        ExcelExporter.export(rs, path, table);
        MySQLConnection.closeCon(rs, pst, con);
    }
}
