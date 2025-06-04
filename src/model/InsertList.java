package model;

import java.util.ArrayList;

import view.Insertion;

public class InsertList {

	public static ArrayList<Object> list = Insertion.st;

	public InsertList() {
	}

	public InsertList(ArrayList<Object> list) {
		InsertList.list = list;
	}

	public Object getList(int index) {
		return list.get(index);
	}

	public void setList(Object list, int index) {
		InsertList.list.set(index, list);

	}

}
