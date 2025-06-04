package controller.library;

import java.util.ArrayList;

public class EddoLibrary {

	public EddoLibrary() {}
	
	static public boolean isNumber(String txt) {
		try {
			Integer.parseInt(txt);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static ArrayList<String> numbers() {
		ArrayList<String> list = new ArrayList<>();
		for (int i = 0; i <= 9; i++) {
			list.add("" + i);
		}
		return list;
	}
	
	public static ArrayList<String> prohibitedName() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("drop");
		list.add("alter");
		list.add("database");
		list.add("column");
		list.add("row");
		list.add("table");
		list.add("primary key");
		list.add("foreign key");
		list.add("check");
		list.add("varchar");
		list.add("int");
		list.add("decimal");
		list.add("blob");
		list.add("index");
		return list;
	}
	
	
}
