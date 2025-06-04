package model;

public class ShowTablesModel {

	String names, date, time;
	int rowCount, columnCount;

	public ShowTablesModel() {
		super();
	}

	public ShowTablesModel(String names, String date, String time, int rowCount, int columnCount) {
		super();
		this.names = names;
		this.date = date;
		this.time = time;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

}
