package dao.oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

//import com.mysql.cj.jdbc.result.ResultSetMetaData;

import model.oracle.OracleUsers;
import view.Insertion;

public class OracleDaoOperation {

	public OracleDaoOperation() {
	}

	/*
	 * 
	 * ===================================/===============================/=========
	 * ==========
	 * 
	 */

	public ArrayList<String> showTables() {
		String query = "SELECT table_name FROM user_tables ORDER BY table_name ASC";
		ArrayList<String> arrayList = new ArrayList<String>();
		try {
			Statement st = OracleConnection.con().createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				arrayList.add(rs.getString(1));
			}
			rs.close();
			st.close();
		} catch (Exception e) {
		}
		return arrayList;
	}

	/*
	 * 
	 * ===================================/===============================/=========
	 * ==========
	 * 
	 */

	public ArrayList<Object> showDataFrom(String name) {

		ArrayList<Object> pack = new ArrayList<>();
		ArrayList<String> header = new ArrayList<>();
		ArrayList<String> data = new ArrayList<>();

		try {
			String requete = "SELECT * from " + name;
			PreparedStatement pst = OracleConnection.con().prepareStatement(requete);
			ResultSet rs = pst.executeQuery();
			if (rs != null) {
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					header.add(rs.getMetaData().getColumnName(i));
				}
				while (rs.next()) {
					for (int i = 0; i < header.size(); i++) {
						data.add(rs.getString(header.get(i)));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		pack.add(header);
		pack.add(data);
		return pack;
	}
	/*
	 * =============================================================
	 */

	public int getColumn(String name) throws SQLException {
		String requete = " select * from " + name;
		pst = OracleConnection.con().prepareStatement(requete);

		rs = pst.executeQuery();

		return rs.getMetaData().getColumnCount();

	}

	/*
	 * =============================================================
	 */
	PreparedStatement pst;
	ResultSet rs;

	public int getRows(String name) throws SQLException {
		String requete = "SELECT * from " + name;
		int count = 0;
		pst = OracleConnection.con().prepareStatement(requete);
		rs = pst.executeQuery();
		while (rs.next()) {
			count++;
		}
		return count;
	}

	/*
	 * =============================================================
	 */

	public ArrayList<String> getDate() throws SQLException {
		String user = "SCOTT";// ...getUser
		String query = "select CREATED from all_objects  where owner = '" + user + "'";
		ArrayList<String> arr = new ArrayList<>();
		pst = OracleConnection.con().prepareStatement(query);
		rs = pst.executeQuery();
		while (rs.next()) {
			arr.add(rs.getString(1));
		}
		return arr;
	}

	// String q="SELECT DATA_TYPE,COLUMN_NAME, DATA_LENGTH,
	// DATA_PRECISION,DATA_SCALE FROM ALL_TAB_COLUMNS WHERE OWNER = 'scott' AND
	// TABLE_NAME = '"+name+"';";

	public ArrayList<String> getDataType(String name) throws SQLException {

		String query = "SELECT DATA_TYPE,COLUMN_NAME FROM user_tab_columns WHERE TABLE_NAME = '" + name + "'";

		ArrayList<String> date = new ArrayList<String>();
		pst = OracleConnection.con().prepareStatement(query);
		rs = pst.executeQuery();
		while (rs.next()) {
			date.add(rs.getString(1) + " " + rs.getString(2));
		}

		ArrayList<String> alternative = new ArrayList<String>();
		ArrayList<String> head = Insertion.header;
		for (String hd : head) {
			for (String x : date) {
				String tab[] = x.split(" ");
				if (hd.equals(tab[1])) {
					alternative.add(tab[0]);
				}
			}
		}

		return alternative;
	}

	public int insert(String name, ArrayList<Object> build) throws SQLException {

		String builder = "";
		for (@SuppressWarnings("unused")
		Object data : build) {
			builder += "?,";
		}
		if (!builder.isBlank())
			builder = builder.substring(0, builder.length() - 1);

		String query = " INSERT INTO " + name + " VALUES(" + builder + ")";

		pst = OracleConnection.con().prepareStatement(query);

		int count = 1;
		for (Object data : build) {
			pst.setObject(count, data);
			count++;
		}
		int i = pst.executeUpdate();
		pst.close();
		return i;
	}

	public int createTable(String query) throws SQLException {
		pst = OracleConnection.con().prepareStatement(query);
		int x = pst.executeUpdate();
		pst.close();
		return x;
	}

	public ArrayList<OracleUsers> showUsers() {
		String query = "SELECT USER_ID, USERNAME, CREATED FROM ALL_USERS ORDER BY USER_ID";
		ArrayList<OracleUsers> arrayList = new ArrayList<>();
		try {
			Statement st = OracleConnection.con().createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				OracleUsers users = new OracleUsers();
				users.setId(rs.getString(1));
				users.setName(rs.getString(2));
				users.setDate(rs.getString(3));
				arrayList.add(users);
			}
			rs.close();
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	
	public int dropTable(String tab_name) throws SQLException {
		String query="Drop table ?";
		pst = OracleConnection.con().prepareStatement(query);
		pst.setString(1, tab_name);
		int x = pst.executeUpdate();
		pst.close();
		return x;
	}
	
	public int clearTable(String tab_name) throws SQLException {
		String query="Delete from "+tab_name;
		pst = OracleConnection.con().prepareStatement(query);
		int x = pst.executeUpdate();
		pst.close();
		return x;
	}
	
	

	public int renameTable(String name1, String name2) throws SQLException {

		String query = "ALTER TABLE " + name1 + " RENAME TO " + name2;
		pst = OracleConnection.con().prepareStatement(query);
		int x = pst.executeUpdate();
		pst.close();
		return x;

	}
	
	
	public int dropColumn(String name, String col) throws SQLException {

		String query = "ALTER TABLE " + name + " DROP COLUMN " + col;
	
		pst = OracleConnection.con().prepareStatement(query);
		int x = pst.executeUpdate();
		pst.close();
		
		return x;

	}



	/*
	 * =============================================================
	 */

	public int renameColumn(String tab, String col, String colNme) throws SQLException {
		String query = "ALTER TABLE " + tab + " RENAME COLUMN " + col + " TO " + colNme;
		pst = OracleConnection.con().prepareStatement(query);
		int x = pst.executeUpdate();
		pst.close();
		return x;

	}
	
	/*
	 * =============================================================
	 */

	public int addColumn(String addCol, String name) throws SQLException {

		String query = "ALTER TABLE " + name + " ADD " + addCol;
		
		pst = OracleConnection.con().prepareStatement(query);
		int x = pst.executeUpdate();
		pst.close();
		return x;
	}
	
	
	public int update(String query) throws SQLException {
	
		pst = OracleConnection.con().prepareStatement(query);
		int x = pst.executeUpdate();
		pst.close();
		return x;

	}
	
	
	public int delete(String name, String condition) throws SQLException {

		String query = "DELETE FROM " + name + " WHERE " + condition;
		pst = OracleConnection.con().prepareStatement(query);
		int x = pst.executeUpdate();
		pst.close();

		return x;
	}
	
	
	public ArrayList<Object> select(String query) throws SQLException {
		ArrayList<Object> pack = new ArrayList<>();
		ArrayList<String> header = new ArrayList<>();
		ArrayList<String> data = new ArrayList<>();
		pst = OracleConnection.con().prepareStatement(query);
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
				}
			}

		}
		pack.add(header);
		pack.add(data);
		return pack;
	}
	
	
	
//	
//	public ArrayList<String> getDate1() throws SQLException {
//		String user="SCOTT";//...getUser
//		String query="select CREATED from all_objects  where owner = '"+user;
//		
//		ArrayList<String> arr= new ArrayList<>();
//		pst = DaoFactory.con().prepareStatement(query);
//		pst.setString(1,user);
//		rs = pst.executeQuery();
//		while (rs.next()) {
//			arr.add(rs.getString(1));
//		}
//		return arr;
//	}

}
