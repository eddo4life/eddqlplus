package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.mysql.MySQLConnection;
import view.tools.Tools;

public class Back {
	public static int bckNxt = 0;
	static boolean homevar = false;
	public static int currentVal = 0;

	public Back() {

		Home.backLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (Home.backLabel.isEnabled()) {
					Home.nextLabel.setEnabled(true);
					try {
						if (currentVal > 1) {
							_switch(history.get(currentVal - 2));
						} else {
							Home.backLabel.setEnabled(false);
							_switch(0);
						}
					} catch (IndexOutOfBoundsException e12) {
						e12.printStackTrace();
					}
					currentVal -= 1;
				}
			}
		});

		Home.nextLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (Home.nextLabel.isEnabled()) {
					currentVal += 1;
					Home.backLabel.setEnabled(true);
					if (history.size() - currentVal == 1) {
						Home.nextLabel.setEnabled(false);
						_switch(history.get(currentVal));
					} else {
						try {
							if (currentVal > 0)
								_switch(history.get(currentVal - 1));
							else
								Home.nextLabel.setEnabled(false);
						} catch (IndexOutOfBoundsException e12) {
							e12.printStackTrace();
						}
					}
				}
			}
		});
	}

	public Back(Object back) {

	}

	public static ArrayList<Integer> history = new ArrayList<>();

	public void setHistory(int key) {
		Back.history.add(key);
	}

	public ArrayList<Integer> getHistory() {
		return history;
	}

	public void _switch(int key) {
		try {
			if (key != 0) {
				homevar = false;
			}
			switch (key) {
			case 0 -> {
				homevar = true;
				Home.editor();
			}
			case 1 -> {
				try {
					new DataBase_section().dataBases();
				} catch (SQLException e1) {
					new MySQLConnection().setDbName("");
				}
			}
			case 2 -> new TablesSections().saisie();
			case 3 -> new CreateTable().__init__();
			//case 4 -> new Insertion().__init__();
			//case 5 -> new Modification().__init__(); to figureoutlaterintableselectorclass
			case 6 -> new Tools().menu();
			default -> throw new IllegalArgumentException("Unexpected value: " + key);
			}
		} catch (NullPointerException e) {

		}
	}

}