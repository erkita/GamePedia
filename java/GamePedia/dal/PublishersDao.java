package GamePedia.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import GamePedia.model.Publishers;

public class PublishersDao {
	protected ConnectionManager connectionManager;
	
	// Single pattern: instantiation is limited to one object.
	private static PublishersDao instance = null;
	protected PublishersDao() {
		connectionManager = new ConnectionManager();
	}
	public static PublishersDao getInstance() {
		if(instance == null) {
			instance = new PublishersDao();
		}
		return instance;
	}
	
	
	public int getNextPublisherId() throws SQLException {
		String findBiggestId = "SELECT MAX(PublisherId) AS PublisherId FROM Publishers";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(findBiggestId);
			results = selectStmt.executeQuery();
			if(results.next()) {
				return (1 + results.getInt("PublisherId"));
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
	
	public Publishers create(Publishers publisher) throws SQLException {
		String insertPublisher = "INSERT INTO Publishers(PublisherId,PublisherName) VALUES(?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		try {
			int nextId = getNextPublisherId();
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertPublisher);
			
			insertStmt.setInt(1, nextId);
			insertStmt.setString(2, publisher.getPublisherName());
			insertStmt.executeUpdate();
			
			// Set the id to use
			publisher.setPublisherId(nextId);
			return publisher;
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
	
	public Publishers getPublisherById(int publisherId) throws SQLException {
		String selectPublisher = "SELECT PublisherId,PublisherName FROM Publishers WHERE PublisherId=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectPublisher);
			selectStmt.setInt(1, publisherId);

			results = selectStmt.executeQuery();

			if(results.next()) {
				int resultPubId = results.getInt("PublisherId");
				String resultPubName = results.getString("PublisherName");

				Publishers pub = new Publishers(resultPubId, resultPubName);
				return pub;
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
	
	public Publishers updatePublisherName(Publishers publisher, String newName) throws SQLException {
		String updateName = "UPDATE Publishers SET PublisherName=? WHERE PublisherId=?;";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updateName);
			updateStmt.setString(1, newName);
			updateStmt.setInt(2, publisher.getPublisherId());
			updateStmt.executeUpdate();
			
			// Update the param before returning to the caller.
			publisher.setPublisherName(newName);
			return publisher;
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
	
	public Publishers delete(Publishers publisher) throws SQLException {
		String deletePublisher = "DELETE FROM Publishers WHERE PublisherId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deletePublisher);
			deleteStmt.setInt(1, publisher.getPublisherId());
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