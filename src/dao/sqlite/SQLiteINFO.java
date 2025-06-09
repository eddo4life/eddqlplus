package dao.sqlite;

import java.sql.*;

public class SQLiteINFO {
	private Connection connect = null;

	public SQLiteINFO() {

		String url = "jdbc:sqlite:lib/sqlite-tools/eddosql.db";

		try {
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection(url);
			connect = connection;
//			String query="SELECT rowid, * FROM eddotest";
//			Statement st= connection.createStatement();
//		ResultSet result=	st.executeQuery(query);
//		while (result.next()) {
//			JOptionPane.showMessageDialog(null,result.getString(1)+" ---> "+result.getString(2)+" ---> "+result.getString(3) );
//			//System.out.println();
//		}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return connect;
	}

	public void closeConnection(ResultSet resultSet, Connection connection, PreparedStatement preparedStatement) {
		try {

			if (resultSet != null) {
				resultSet.close();
			}
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		
		System.out.println("Type your tring "+trim("").length());
		
//		@SuppressWarnings("resource")
//		int nb1=new Scanner(System.in).nextInt();
//		@SuppressWarnings("resource")
//		int nb2=new Scanner(System.in).nextInt();
//		
//		System.out.println("Is "+nb1+" greater than "+nb2+" ?\nR) "+get(nb1,nb2));

	}
	
	static boolean get(int x,int y) {
		return (x>y)? true:false;
	}

	static String trim(String string) {
		int indice[]= {-1,-1};
		for(int i=0;i<string.length();i++) {
			if(string.charAt(i)!=' ') {
				if(indice[0]==-1) {
				   indice[0]=i;
				}
				indice[1]=i;
			}
		}
		return string.substring(indice[0],indice[1]+1);
	}
//USAGE --> String result=trim(string);
}
