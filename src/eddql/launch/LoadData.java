package eddql.launch;

import com.formdev.flatlaf.FlatDarkLaf;
import dao.DBMS;
import dao.mysql.MySQLDaoOperation;
import dao.oracle.OracleDaoOperation;
import icon.BufferedImageIO;
import model.DataBaseModel;
import model.ShowTablesModel;
import view.Home;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LoadData {

	public static ArrayList<DataBaseModel> database;

	public static ArrayList<ShowTablesModel> tables;

	private ShowTablesModel tbm;

	private DataBaseModel dbm;

	public LoadData() {
	}

	// Likely a overload
	public LoadData(String x) {
		wait = false;
		try {
			 UIManager.setLookAndFeel( new FlatDarkLaf() );
				// UIManager.setLookAndFeel( new FlatDarculaLaf() );
			}catch (Exception e) {
				e.printStackTrace();
			}
		frame = new JFrame();
		frame.setSize(400, 200);
		frame.setLayout(null);
		frame.setUndecorated(true);
		JPanel panel = new JPanel();
		frame.setIconImage(new BufferedImageIO().getIcon(500, 500, "logo.jpg").getImage());
		panel.setLayout(new BorderLayout());
		JPanel southPanel = new JPanel();
		southPanel.setBackground(Color.black);
		southPanel.setLayout(null);
		southPanel.setPreferredSize(new Dimension(600, 50));
		JLabel imgLabel = new JLabel();
		imgLabel.setIcon(new BufferedImageIO().getIcon(400, 160, "eddql.jpg"));
		
		panel.add(imgLabel, BorderLayout.NORTH);
		loadingLabel = new JLabel();
		loadingLabel.setBounds(0, 20, 250, 25);
		southPanel.add(loadingLabel);
		panel.add(southPanel, BorderLayout.SOUTH);
		panel.setBounds(0, 0, 400, 200);
		bar.setBorder(null);
		bar.setBounds(0, -1, 400, 10);
		//bar.setBackground(Color.black);
		southPanel.add(bar);
		bar.setValue(0);
		bar.setStringPainted(true);
		//bar.setFont(new Font("mv Boli", 0, 10));
		//bar.setForeground(Color.DARK_GRAY);
		//loadingLabel.setForeground(Color.blue);
		loadingLabel.setFont(new Font(null, 0, 11));

		frame.add(panel);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		loadingLabel.setText("Gathering datas connection.");
		try {
			Thread.sleep(1000);
			loadingLabel.setText("Gathering datas connection..");
			Thread.sleep(500);
			loadingLabel.setText("Gathering datas connection...");
			Thread.sleep(500);
			loadingLabel.setText("Gathering datas connection.");
			Thread.sleep(300);
			loadingLabel.setText("Gathering datas connection..");
			Thread.sleep(300);
			loadingLabel.setText("Gathering datas connection...");
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		loadingLabel.setText("Loading databases...");
		databaseSectionLoader();
		if (exit != 0) {
			loadingLabel.setText("Done  loading databases!");
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			loadingLabel.setText("Now loading tables and additional contents...");
			tablesSectionLoader();
			loadingLabel.setText("Completed!");
			//loadingLabel.setForeground(Color.cyan);
		} else {
			loadingLabel.setText("Failed, connection required!");
			loadingLabel.setForeground(Color.red);
		}
		try {
			Thread.sleep(2000);
			frame.dispose();
		} catch (InterruptedException e) {}
	}

	/*
	 * =============================================================
	 */

	JProgressBar bar = new JProgressBar();

	/*
	 * =============================================================
	 */
	public void tablesSectionLoader() {
		
		tables = new ArrayList<>();
		if (wait)
			Home.frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try {
			ArrayList<String> showTables ;
			if(DBMS.dbms==1) {
				 showTables = new MySQLDaoOperation().showTables();
			}else {
				showTables=new OracleDaoOperation().showTables();
			}
			int tourTotal = lineCount;
			lineCount += showTables.size();
			ArrayList<String> dteTime;
			if(DBMS.dbms == 1) {
			dteTime = new MySQLDaoOperation().getDate();
			}else {
				dteTime=new OracleDaoOperation().getDate();
			}
			int i = 0;
			for (String data : showTables) {
				tbm = new ShowTablesModel();
				tbm.setNames(data);
				if(DBMS.dbms==1) {
				tbm.setColumnCount(new MySQLDaoOperation().getColumn(data));
				tbm.setRowCount(new MySQLDaoOperation().getRows(data));
				}else {
					try {
					tbm.setColumnCount(new OracleDaoOperation().getColumn(data));
					tbm.setRowCount(new  OracleDaoOperation().getRows(data));
					}catch (Exception e) {
						tbm.setColumnCount(0);
						tbm.setRowCount(0);
					}
				}
				
				String[] dt = dteTime.get(i).split(" ");
				tbm.setDate(dt[0]);
				tbm.setTime(dt[1]);
				mathCalcul(lineCount, tourTotal + i, 50, true);
				i++;
				tables.add(tbm);
			}
			if (wait)
				Home.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} catch (Exception e) {
			//e.printStackTrace();
			exit = 0;
		}
	}

	/*
	 * =============================================================
	 */

	int lineCount = 0;

	/*
	 * =============================================================
	 */

	public void mathCalcul(int tourTotal, int tourEffectue, int max, boolean add) {
		float y = (tourEffectue * max) / tourTotal;
		int percent = Math.round(y);
		if (add)
			bar.setValue(percent + 51);
		else
			bar.setValue(percent);
	}

	/*
	 * =============================================================
	 */

	public static boolean wait = true;

	/*
	 * =============================================================
	 */

	public void databaseSectionLoader() {
		database = new ArrayList<>();
		if (wait)
			Home.frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		int i = 1;
		try {
			ArrayList<String> names = new MySQLDaoOperation().showDataBases();
			lineCount += names.size();
			for (String s : names) {
				dbm = new DataBaseModel();
				dbm.setName(s);
				String[] old = new MySQLDaoOperation().getDbOldestDate(String.valueOf(s)).split(" ");
				dbm.setOldestTab(old[0]);// date
				dbm.setOldestTabTime(old[1]);// time
				String[] late = new MySQLDaoOperation().getDbLatestDate(String.valueOf(s)).split(" ");
				dbm.setLatestTab(late[0]);// date
				dbm.setLatestTabTime(late[1]);// time
				dbm.setTablesCount(new MySQLDaoOperation().showTablesFrom(String.valueOf(s)));
				database.add(dbm);
				mathCalcul(lineCount, i, 50, false);
				i++;
			}
			if (wait)
				Home.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} catch (Exception e) {
			exit = 0;
		}
	}

	/*
	 * =============================================================
	 */

	int exit = 1;
	static JFrame frame = new JFrame();
	static JLabel loadingLabel;

	/*
	 * =============================================================
	 */

	public void getAll() {
		databaseSectionLoader();
		tablesSectionLoader();
	}

}
