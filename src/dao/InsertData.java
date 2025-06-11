package dao;

import dao.mysql.MySQLDaoOperation;
import dao.oracle.OracleDaoOperation;
import model.InsertList;
import view.Insertion;
import view.iconmaker._Icon;
import view.pupupsmessage.PupupMessages;

import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.ArrayList;

public class InsertData extends AbstractTableModel {

    String[] columnNames;
    ArrayList<InsertList> list;
    InsertList insertList = new InsertList();
    private Class<?>[] columnClass = getClas();

    public Class<?>[] getClas() {
        try {

            ArrayList<String> arr = null;

            if (DBMS.dbms == 1) {
                arr = new MySQLDaoOperation().getDataType(Insertion.tabName);
            } else if (DBMS.dbms == 2) {
                arr = new OracleDaoOperation().getDataType(Insertion.tabName);
            }


            assert arr != null;
            Class<?>[] columnC = new Class[arr.size()];
            int i = 0;
            for (String x : arr) {
                switch (x.toLowerCase()) {

                    case "number", "int", "integer", "unsigned smallint" -> columnC[i] = Integer.class;
                    case "decimal", "float", "double precision", "bigdatetime" -> columnC[i] = Double.class;
                    case "real" -> columnC[i] = Float.class;
                    case "numeric", "money", "smallmoney" -> columnC[i] = java.math.BigDecimal.class;
                    case "bit" -> columnC[i] = Boolean.class;
                    case "tinyint" -> columnC[i] = Byte.class;
                    case "smallint" -> columnC[i] = Short.class;
                    case "bigint", "unsigned int" -> columnC[i] = Long.class;
                    case "unsigned bigint" -> columnC[i] = java.math.BigInteger.class;
                    case "binary", "java.sql.date", "varbinary" -> columnC[i] = Byte[].class;
                    case "image" -> columnC[i] = java.io.InputStream.class;
                    case "datetime", "smalldatetime", "bigtime" -> columnC[i] = java.sql.Timestamp.class;
                    // case "date"->{columnC[i]=java.sql.Time.class;}
                    case "date" -> columnC[i] = String.class;
                    case "time" -> columnC[i] = java.sql.Time.class;
                    default -> columnC[i] = String.class;

                }
                i++;
            }
            columnClass = columnC;

        } catch (SQLException e) {
            new PupupMessages().message(e.getMessage(), new _Icon().exceptionIcon());
        }
        return columnClass;
    }

    public InsertData(String[] columnNames, ArrayList<InsertList> list) {
        this.columnNames = columnNames;
        this.list = list;

    }

    @Override
    public Class<?> getColumnClass(int c) {
        return columnClass[c];
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return insertList.getList(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        insertList.setList(aValue, columnIndex);

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

}
