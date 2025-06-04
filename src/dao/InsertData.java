package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import dao.mysql.MySQLDaoOperation;
import dao.oracle.OracleDaoOperation;
import model.InsertList;
import view.Insertion;
import view.iconmaker._Icon;
import view.pupupsmessage.PupupMessages;

public class InsertData extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String columnNames[];
	ArrayList<InsertList> list;
	InsertList insertList = new InsertList();
	private Class<?>[] columnClass = getClas();

	public Class<?>[] getClas() {
		try {
			
			ArrayList<String> arr =null;
			
			if(DBMS.dbms==1) {
				 arr = new MySQLDaoOperation().getDataType(Insertion.tabName);
				}else if (DBMS.dbms == 2) {
					 arr = new OracleDaoOperation().getDataType(Insertion.tabName);
				}
			
			
			Class<?>[] columnC = new Class[arr.size()];
			int i = 0;
			for (String x : arr) {
				switch (x.toLowerCase()) {
				
				case "varchar2" -> columnC[i] = String.class;
				case "number" -> columnC[i] = Integer.class;
				case "int" -> columnC[i] = Integer.class;
				case "varchar" -> columnC[i] = String.class;
				case "decimal" -> columnC[i] = Double.class;
				case "real" -> columnC[i] = Float.class;
				case "char" -> columnC[i] = String.class;
				case "nchar" -> columnC[i] = String.class;
				case "nvarchar" -> columnC[i] = String.class;
				case "unichar" -> {
					columnC[i] = String.class;
				}
				case "univarchar" -> {
					columnC[i] = String.class;
				}
				case "unitext" -> {
					columnC[i] = String.class;
				}
				case "text" -> {
					columnC[i] = String.class;
				}
				case "numeric" -> {
					columnC[i] = java.math.BigDecimal.class;
				}
				case "money" -> {
					columnC[i] = java.math.BigDecimal.class;
				}
				case "smallmoney" -> {
					columnC[i] = java.math.BigDecimal.class;
				}
				case "bit" -> {
					columnC[i] = Boolean.class;
				}
				case "tinyint" -> {
					columnC[i] = Byte.class;
				}
				case "smallint" -> {
					columnC[i] = Short.class;
				}
				case "integer" -> {
					columnC[i] = Integer.class;
				}
				case "bigint" -> {
					columnC[i] = Long.class;
				}
				case "unsigned smallint" -> {
					columnC[i] = Integer.class;
				}
				case "unsigned int" -> {
					columnC[i] = Long.class;
				}
				case "unsigned bigint" -> {
					columnC[i] = java.math.BigInteger.class;
				}
				case "float" -> {
					columnC[i] = Double.class;
				}
				case "double precision" -> {
					columnC[i] = Double.class;
				}
				case "binary" -> {
					columnC[i] = Byte[].class;
				}
				case "varbinary" -> {
					columnC[i] = Byte[].class;
				}
				case "image" -> {
					columnC[i] = java.io.InputStream.class;
				}
				case "datetime" -> {
					columnC[i] = java.sql.Timestamp.class;
				}
				case "smalldatetime" -> {
					columnC[i] = java.sql.Timestamp.class;
				}
				case "bigdatetime" -> {
					columnC[i] = Double.class;
				}
				case "bigtime" -> {
					columnC[i] = java.sql.Timestamp.class;
				}
				// case "date"->{columnC[i]=java.sql.Time.class;}
				case "date" -> {
					columnC[i] = String.class;
				}
				case "java.sql.Date" -> {
					columnC[i] = Byte[].class;
				}
				case "time" -> {
					columnC[i] = java.sql.Time.class;
				}

				default -> {
					columnC[i] = String.class;
				}
				
				
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
