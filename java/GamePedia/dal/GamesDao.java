package GamePedia.dal;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import GamePedia.model.Games;


public class GamesDao {
	protected ConnectionManager connectionManager;
	
	// Single pattern: instantiation is limited to one object.
	private static GamesDao instance = null;
	protected GamesDao() {
		connectionManager = new ConnectionManager();
	}
	public static GamesDao getInstance() {
		if(instance == null) {
			instance = new GamesDao();
		}
		return instance;
	}
	
	public Games create(Games game) throws SQLException {
		String insertGame = "INSERT INTO Games(GameId,GameName,Description,ReleaseDate,Price,PicLink) VALUES(?,?,?,?,?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertGame);
			
			insertStmt.setInt(1, game.getGameId());
			insertStmt.setString(2, game.getGameName());
			insertStmt.setString(3, game.getDescription());
			insertStmt.setTimestamp(4, new Timestamp(game.getReleaseDate().getTime()));
			insertStmt.setBigDecimal(5, game.getPrice());
			insertStmt.setString(6, game.getPicLink());
			insertStmt.executeUpdate();
			
			return game;
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
	
	public int getNextGameId() throws SQLException {
		String findBiggestId = "SELECT GameId FROM Games ORDER BY GameId DESC LIMIT 1";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(findBiggestId);
			results = selectStmt.executeQuery();
			if(results.next()) {
				return (1 + results.getInt("GameId"));
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
			
	public Games getGameById(int gameId) throws SQLException {
		String selectGame = "SELECT GameId,GameName,Description,ReleaseDate,Price,PicLink FROM Games WHERE GameId=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectGame);
			selectStmt.setInt(1, gameId);

			results = selectStmt.executeQuery();

			if(results.next()) {
				int resultGameId = results.getInt("GameId");
				String resultGameName = results.getString("GameName");
				String resultDescription = results.getString("Description");
				Date resultReleaseDate = new Date(results.getTimestamp("ReleaseDate").getTime());
				BigDecimal resultPrice = results.getBigDecimal("Price");
				String resultPicLink = results.getString("PicLink");

				Games game = new Games(resultGameId, resultGameName, resultDescription, resultReleaseDate, resultPrice, resultPicLink);
				return game;
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
	
	public int getGameIdByName(String gamename) throws SQLException {
		String selectGame = "SELECT * FROM Games WHERE GameName=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectGame);
			selectStmt.setString(1, gamename);

			results = selectStmt.executeQuery();

			if(results.next()) {
				int resultGameId = results.getInt("GameId");
				return resultGameId;
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
	
	public List<Games> getGamesByName(String gameName) throws SQLException {
		List<Games> matchingGames = new ArrayList<>();
		String selectGame = "SELECT GameId,GameName,Description,ReleaseDate,Price,PicLink FROM Games WHERE GameName=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectGame);
			selectStmt.setString(1, gameName);

			results = selectStmt.executeQuery();

			while(results.next()) {
				int resultGameId = results.getInt("GameId");
				String resultGameName = results.getString("GameName");
				String resultDescription = results.getString("Description");
				Date resultReleaseDate = new Date(results.getTimestamp("ReleaseDate").getTime());
				BigDecimal resultPrice = results.getBigDecimal("Price");
				String resultPicLink = results.getString("PicLink");

				Games game = new Games(resultGameId, resultGameName, resultDescription, resultReleaseDate, resultPrice, resultPicLink);
				matchingGames.add(game);
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
		return matchingGames;
	}
	
	public Games updatePrice(Games game, BigDecimal newPrice) throws SQLException {
		String updatePrice = "UPDATE Games SET Price=? WHERE GameId=?;";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updatePrice);
			updateStmt.setBigDecimal(1, newPrice);
			updateStmt.setInt(2, game.getGameId());
			updateStmt.executeUpdate();
			
			// Update the param before returning to the caller.
			game.setPrice(newPrice);
			return game;
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
	
	public Games updateDescription(Games game, String newDescription) throws SQLException {
		String updateDescription = "UPDATE Games SET Description=? WHERE GameId=?;";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updateDescription);
			updateStmt.setString(1, newDescription);
			updateStmt.setInt(2, game.getGameId());
			updateStmt.executeUpdate();
			
			// Update the param before returning to the caller.
			game.setDescription(newDescription);
			return game;
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
	
	public Games updateReleaseDate(Games game, Date newDate) throws SQLException {
		String updateRelease = "UPDATE Games SET ReleaseDate=? WHERE GameId=?;";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updateRelease);
			updateStmt.setTimestamp(1, new Timestamp(newDate.getTime()));
			updateStmt.setInt(2, game.getGameId());
			updateStmt.executeUpdate();
			
			// Update the param before returning to the caller.
			game.setReleaseDate(newDate);
			return game;
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
	
	public Games updatePicLink(Games game, String newPicLink) throws SQLException {
		String updatePicLink = "UPDATE Games SET PicLink=? WHERE GameId=?;";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updatePicLink);
			updateStmt.setString(1, newPicLink);
			updateStmt.setInt(2, game.getGameId());
			updateStmt.executeUpdate();
			
			// Update the param before returning to the caller.
			game.setDescription(newPicLink);
			return game;
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
	
	public Games delete(Games game) throws SQLException {
		String deleteGame = "DELETE FROM Games WHERE GameId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteGame);
			deleteStmt.setInt(1, game.getGameId());
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
