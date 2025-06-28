package controller.sql.modification;

import dao.DBMS;
import dao.mysql.MySQLDaoOperation;
import dao.oracle.OracleDaoOperation;

import java.sql.SQLException;

public class ModificationControllerSQL {

    public ModificationControllerSQL() {
    }

    public String dropColumn(String table_name, String column_name) {
        try {

            if (DBMS.dbms == 1) {
                new MySQLDaoOperation().dropColumn(table_name, column_name);
            } else if (DBMS.dbms == 2) {
                new OracleDaoOperation().dropColumn(table_name, column_name);
            }
            return "";
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    public String addColumn(String query, String table_name) {
        try {

            if (DBMS.dbms == 1) {
                new MySQLDaoOperation().addColumn(query, table_name);
            } else if (DBMS.dbms == 2) {
                new OracleDaoOperation().addColumn(query, table_name);
            }
            return "";
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String renameTable(String old_name, String new_name) {
        try {

            if (DBMS.dbms == 1) {
                new MySQLDaoOperation().renameTable(old_name, new_name);
            } else if (DBMS.dbms == 2) {
                new OracleDaoOperation().renameTable(old_name, new_name);
            }
            return "";
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String renameColumn(String table_name, String col_name, String new_name) {
        try {

            if (DBMS.dbms == 1) {
                new MySQLDaoOperation().renameColumn(table_name, col_name, new_name);
            } else if (DBMS.dbms == 2) {
                new OracleDaoOperation().renameColumn(table_name, col_name, new_name);
            }

            return "";
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String updating(String value, String comboBox1, String field2, String comboBox2, boolean isConstraint,
                           String table_name) {

        String query = "UPDATE " + table_name;

        try {
            Double.parseDouble(value);
        } catch (Exception e) {
            value = "'" + value + "'";
        }
        query += " SET " + comboBox1 + "=" + value;
        if (isConstraint) {
            query += " WHERE " + comboBox2 + field2;
        }


        try {

            int row = -1;

            if (DBMS.dbms == 1) {
                row = new MySQLDaoOperation().update(query);
            } else if (DBMS.dbms == 2) {
                row = new OracleDaoOperation().update(query);
            }

            if (row > 0) {
                return "Updated successfully!";

            } else {
                return "Process completed, 0 row affected.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String delete_row(String tableName, String buildQuery) {

        try {
            int row = -1;

            if (DBMS.dbms == 1) {
                row = new MySQLDaoOperation().delete(tableName, buildQuery);
            } else if (DBMS.dbms == 2) {
                row = new OracleDaoOperation().delete(tableName, buildQuery);
            }
            if (row > 0) {
                return "s";

            } else {
                return "f";
            }
        } catch (SQLException e) {

            return e.getMessage();
        }

    }

}
