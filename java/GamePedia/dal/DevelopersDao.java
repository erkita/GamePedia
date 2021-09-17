package GamePedia.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import GamePedia.model.Developers;

public class DevelopersDao {
	protected ConnectionManager connectionManager;
	
	// Single pattern: instantiation is limited to one object.
	private static DevelopersDao instance = null;
	protected DevelopersDao() {
		connectionManager = new ConnectionManager();
	}
	public static DevelopersDao getInstance() {
		if(instance == null) {
			instance = new DevelopersDao();
		}
		return instance;
	}
	
	public int getNextDeveloperId() throws SQLException {
		String findBiggestId = "SELECT MAX(DeveloperId) AS DeveloperId FROM Developers";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(findBiggestId);
			results = selectStmt.executeQuery();
			if(results.next()) {
				return (1 + results.getInt("DeveloperId"));
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
	
	public Developers create(Developers dev) throws SQLException {
		String insertDev = "INSERT INTO Developers(DeveloperId,DeveloperName) VALUES(?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		try {
			int nextId = getNextDeveloperId();
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertDev);
			
			insertStmt.setInt(1, nextId);
			insertStmt.setString(2, dev.getDeveloperName());
			insertStmt.executeUpdate();
			
			dev.setDeveloperId(nextId);
			return dev;
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
	
	public Developers getDeveloperById(int developerId) throws SQLException {
		String selectDev = "SELECT DeveloperId,DeveloperName FROM Developers WHERE DeveloperId=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectDev);
			selectStmt.setInt(1, developerId);

			results = selectStmt.executeQuery();

			if(results.next()) {
				int resultDevId = results.getInt("DeveloperIdId");
				String resultDevName = results.getString("DeveloperName");

				Developers dev = new Developers(resultDevId, resultDevName);
				return dev;
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
	
	public Developers updateDeveloperName(Developers dev, String newName) throws SQLException {
		String updateName = "UPDATE Developers SET DeveloperName=? WHERE DeveloperId=?;";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updateName);
			updateStmt.setString(1, newName);
			updateStmt.setInt(2, dev.getDeveloperId());
			updateStmt.executeUpdate();
			
			// Update the param before returning to the caller.
			dev.setDeveloperName(newName);
			return dev;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(updateStmt != null) {
				updateStmt.close();
			}
		}
	}
	
	public Developers delete(Developers dev) throws SQLException {
		String deleteDev = "DELETE FROM Developers WHERE DeveloperId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteDev);
			deleteStmt.setInt(1, dev.getDeveloperId());
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
	
}