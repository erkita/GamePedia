package GamePedia.dal;

import GamePedia.model.*;
import GamePedia.model.NumPlayers.NumberName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class NumPlayersDao {
	protected static ConnectionManager connectionManager;
	
	private static NumPlayersDao instance = null;
	protected NumPlayersDao() {
		connectionManager = new ConnectionManager();
	}
	public static NumPlayersDao getInstance() {
		if(instance == null) {
			instance = new NumPlayersDao();
		}
		return instance;
	}


	public static int getNextNumPlayerId() throws SQLException {
		String findBiggestId = "SELECT MAX(NumPlayerId) AS NumPlayerId FROM NumPlayers";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(findBiggestId);
			results = selectStmt.executeQuery();
			if(results.next()) {
				return (1 + results.getInt("NumPlayerId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(selectStmt != null) {
				selectStmt.close();
			}
			if(results != null) {
				results.close();
			}
		}
		return 0;
	}

	public static NumPlayers create(NumPlayers numPlayer) throws SQLException {
		String insertNumPlayer =
			"INSERT INTO NumPlayers(NumPlayerId,NumberName)" +
			"VALUES(?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertNumPlayer);
			int nextId = getNextNumPlayerId();

			insertStmt.setInt(1, nextId);
			insertStmt.setString(2, numPlayer.getNumberName().name());

			insertStmt.executeUpdate();
			
			numPlayer.setNumPlayersId(nextId);
			return numPlayer;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(insertStmt != null) {
				insertStmt.close();
			}
		}
	}


	public NumPlayers delete(NumPlayers numPlayer) throws SQLException {
		String deleteNumPlayer = "DELETE FROM NumPlayers WHERE NumPlayersId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteNumPlayer);
			deleteStmt.setInt(1, numPlayer.getNumPlayersId());
			deleteStmt.executeUpdate();

			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(deleteStmt != null) {
				deleteStmt.close();
			}
		}
	}
	
	public NumPlayers getNumPlayerById(int numPlayerId) throws SQLException {
		String selectNumPlayer = "SELECT NumPlayersId,NumberName FROM NumPlayers WHERE NumPlayersId=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectNumPlayer);
			selectStmt.setInt(1, numPlayerId);

			results = selectStmt.executeQuery();

			if(results.next()) {
				int resultNumPlayersId = results.getInt("NumPlayersId");
				NumberName numberName = NumPlayers.NumberName.valueOf(
						results.getString("NumberName"));

				NumPlayers numPlayer = new NumPlayers(resultNumPlayersId, numberName);
				return numPlayer;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(selectStmt != null) {
				selectStmt.close();
			}
			if(results != null) {
				results.close();
			}
		}
		return null;
	}


}
