package GamePedia.dal;

import GamePedia.model.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UsersDao {
	protected static ConnectionManager connectionManager;
	
	private static UsersDao instance = null;
	protected UsersDao() {
		connectionManager = new ConnectionManager();
	}
	public static UsersDao getInstance() {
		if(instance == null) {
			instance = new UsersDao();
		}
		return instance;
	}


	public static Users create(Users user) throws SQLException {
		String insertUser = "INSERT INTO Users(UserId,UserName,FirstName,Passwords,Email) VALUES(?,?,?,?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertUser);

			insertStmt.setInt(1, user.getUserId());
			insertStmt.setString(2, user.getUserName());
			insertStmt.setString(3, user.getFirstName());
			insertStmt.setString(4, user.getPasswords());
			insertStmt.setString(5, user.getEmail());
			insertStmt.executeUpdate();
			
			return user;
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


	public Users delete(Users user) throws SQLException {
		String deleteUser = "DELETE FROM Users WHERE UserId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteUser);
			deleteStmt.setInt(1, user.getUserId());
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
	
	public Users updateFirstName(Users user, String newFirstName) throws SQLException {
		String updateFirstName = "UPDATE Users SET FirstName=? WHERE UserName=?;";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updateFirstName);
			updateStmt.setString(1, newFirstName);
			updateStmt.setString(2, user.getUserName());
			updateStmt.executeUpdate();
			
			// Update the person param before returning to the caller.
			user.setFirstName(newFirstName);
			return user;
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
	
	public Users updateEmail(Users user, String newEmail) throws SQLException {
		String updateEmail = "UPDATE Users SET Email=? WHERE UserName=?;";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updateEmail);
			updateStmt.setString(1, newEmail);
			updateStmt.setString(2, user.getUserName());
			updateStmt.executeUpdate();
			
			// Update the person param before returning to the caller.
			user.setEmail(newEmail);
			return user;
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
	
	public Users updatePassword(Users user, String newPassword) throws SQLException {
		String updatePassword = "UPDATE Users SET Passwords=? WHERE UserName=?;";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updatePassword);
			updateStmt.setString(1, newPassword);
			updateStmt.setString(2, user.getUserName());
			updateStmt.executeUpdate();
			
			// Update the person param before returning to the caller.
			user.setPasswords(newPassword);
			return user;
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
	
	public Users getUserByname(String userName) throws SQLException {
		
		String selectUser = "SELECT * FROM Users WHERE UserName=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectUser);
			selectStmt.setString(1, userName);

			results = selectStmt.executeQuery();

			while(results.next()) {
				int resultUserId = results.getInt("UserId");
				String resultUserName = results.getString("UserName");
				String resultEmail = results.getString("Email");
				String resultFirstName = results.getString("FirstName");			
				String resultPasswords = results.getString("Passwords");

				Users user = new Users(resultUserId, resultUserName, resultEmail, resultFirstName, resultPasswords);
				return user;
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
	
	
	public Users getUserFromUserName(String userName) throws SQLException {
		String selectUser = "SELECT * FROM Users WHERE UserName=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectUser);
			selectStmt.setString(1, userName);
			results = selectStmt.executeQuery();
			if(results.next()) {
				int userId = results.getInt("UserId");
				String resultUserName = results.getString("UserName");
				String firstName = results.getString("FirstName");
				String password = results.getString("Passwords");
				String email = results.getString("Email");
				Users user = new Users(userId, resultUserName, firstName, password, email);
				return user;
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
	
	public int getNextUserId() throws SQLException {
		String findBiggestId = "SELECT UserId FROM Users ORDER BY UserId DESC LIMIT 1";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(findBiggestId);
			results = selectStmt.executeQuery();
			if(results.next()) {
				return (1 + results.getInt("UserId"));
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
		
	public Users getUserById(int userId) throws SQLException {
		String selectUser = "SELECT * FROM Users WHERE UserId=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectUser);
			selectStmt.setInt(1, userId);

			results = selectStmt.executeQuery();

			if(results.next()) {
				int resultUserId = results.getInt("UserId");
				String resultUserName = results.getString("UserName");
				String resultFirstName = results.getString("FirstName");
				String resultPasswords = results.getString("Passwords");
				String resultEmail = results.getString("Email");

				Users user = new Users(resultUserId, resultUserName, resultFirstName, resultPasswords, resultEmail);
				return user;
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
	
	public List<Users> getUserByName(String userName) throws SQLException {
		List<Users> matchingUsers = new ArrayList<>();
		String selectUser = "SELECT * FROM Users WHERE UserName=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectUser);
			selectStmt.setString(1, userName);

			results = selectStmt.executeQuery();

			while(results.next()) {
				int resultUserId = results.getInt("UserId");
				String resultUserName = results.getString("UserName");
				String resultFirstName = results.getString("FirstName");
				String resultPasswords = results.getString("Passwords");
				String resultEmail = results.getString("Email");
				
				Users user = new Users(resultUserId, resultUserName, resultFirstName, resultPasswords, resultEmail);
				matchingUsers.add(user);
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
		return matchingUsers;
	}
	
	
	public int getUserIdFromUsername(String userName) throws SQLException {
		String selectUser = "SELECT UserId FROM Users WHERE UserName=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectUser);
			selectStmt.setString(1, userName);
			results = selectStmt.executeQuery();
			if(results.next()) {
				int userId = results.getInt("UserId");
				return userId;
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

}
