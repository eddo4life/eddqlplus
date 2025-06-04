package dao.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.ConnectingToolsModel;
import model.Sys;

public class SystemDatabaseTreatment {

	SQLiteINFO sInfo = new SQLiteINFO();
	Connection connection = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;

	public SystemDatabaseTreatment() {
	}

	public boolean updateConnection(ConnectingToolsModel ctm) {
		String query = "update mysqlconection set host=?,port =?,portname =?,password=?, dbname=?";
		try {
			connection = sInfo.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, ctm.getHost());
			preparedStatement.setString(2, ctm.getPort());
			preparedStatement.setString(3, ctm.getPortNme());
			preparedStatement.setString(4, ctm.getPassword());
			preparedStatement.setString(5, ctm.getDbName());
			int x = preparedStatement.executeUpdate();
			sInfo.closeConnection(resultSet, connection, preparedStatement);
			return x > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public ConnectingToolsModel getDataConnection() {

		String query = "SELECT * FROM mysqlconection";
		ConnectingToolsModel ctm = null;
		try {
			connection = sInfo.getConnection();
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				ctm = new ConnectingToolsModel();
				ctm.setHost(resultSet.getString(1));
				ctm.setPort(resultSet.getString(2));
				ctm.setPortNme(resultSet.getString(3));
				ctm.setPassword(resultSet.getString(4));
				ctm.setDbName(resultSet.getString(5));
			}
			sInfo.closeConnection(resultSet, connection, preparedStatement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ctm;
	}

	public boolean newMySQLConnection(ConnectingToolsModel ctm) {
		String query = "INSERT INTO mysqlconection VALUES (?,?,?,?,?)";
		try {
			connection = sInfo.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, ctm.getHost());
			preparedStatement.setString(2, ctm.getPort());
			preparedStatement.setString(3, ctm.getPortNme());
			preparedStatement.setString(4, ctm.getPassword());
			preparedStatement.setString(5, ctm.getDbName());
			int x = preparedStatement.executeUpdate();
			sInfo.closeConnection(resultSet, connection, preparedStatement);
			return x > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * 
	 * =============================================================================
	 * =====
	 * 
	 */

	public boolean updateSystem(String colon, int value, String values) {

		String query = "UPDATE sys SET " + colon + "=?";
		try {
			connection = sInfo.getConnection();
			preparedStatement = connection.prepareStatement(query);
			if (values == null) {
				preparedStatement.setInt(1, value);
			} else {
				preparedStatement.setString(1, values);
			}

			boolean result = preparedStatement.executeUpdate() > 0;
			sInfo.closeConnection(null, connection, preparedStatement);
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public Sys getSystemDatas() {

		String query = "SELECT * FROM sys";
		Sys sys = null;
		try {
			connection = sInfo.getConnection();
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				sys = new Sys();
				sys.setLook(resultSet.getInt(1));
				sys.setSystemColor(resultSet.getInt(2));
				sys.setEditorColor(resultSet.getInt(3));
				sys.setCustomForeground(resultSet.getString(4));
			}
			sInfo.closeConnection(resultSet, connection, preparedStatement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sys;
	}

}
