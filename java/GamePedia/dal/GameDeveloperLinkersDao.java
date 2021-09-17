package GamePedia.dal;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import GamePedia.model.Developers;
import GamePedia.model.GameDeveloperLinkers;
import GamePedia.model.Games;

public class GameDeveloperLinkersDao {
	protected ConnectionManager connectionManager;
	
	// Single pattern: instantiation is limited to one object.
	private static GameDeveloperLinkersDao instance = null;
	protected GameDeveloperLinkersDao() {
		connectionManager = new ConnectionManager();
	}
	public static GameDeveloperLinkersDao getInstance() {
		if(instance == null) {
			instance = new GameDeveloperLinkersDao();
		}
		return instance;
	}
	
	public GameDeveloperLinkers create(GameDeveloperLinkers gameDevLinker) throws SQLException {
		String insertGameDevLinker = "INSERT INTO GameDeveloperLinkers(GameId,DeveloperId) VALUES(?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		ResultSet resultKey = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertGameDevLinker, Statement.RETURN_GENERATED_KEYS);
			
			insertStmt.setInt(1, gameDevLinker.getGameId());
			insertStmt.setInt(2, gameDevLinker.getDeveloperId());
			insertStmt.executeUpdate();
			
			// Retrieve the auto-generated key and set it, so it can be used by the caller.
			resultKey = insertStmt.getGeneratedKeys();
			int gdLinkerId = -1;
			if(resultKey.next()) {
				gdLinkerId = resultKey.getInt(1);
			} else {
				throw new SQLException("Unable to retrieve auto-generated key.");
			}
			gameDevLinker.setGdLinkerId(gdLinkerId);
			return gameDevLinker;
			
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
	
	public List<Games> getGamesOfDeveloper(Developers dev) throws SQLException {
		List<Games> gamesOfDev = new ArrayList<>();
		String selectMatchingGames = "SELECT Games.GameId AS GameId,GameName,Description,ReleaseDate,Price,PicLink FROM Games "
				+ "INNER JOIN gamedeveloperlinkers on Games.GameId = gamedeveloperlinkers.GameId "
				+ "INNER JOIN Developers on gamedeveloperlinkers.DeveloperId = Developers.DeveloperId "
				+ "WHERE Developers.DeveloperId=?";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectMatchingGames);
			selectStmt.setInt(1, dev.getDeveloperId());

			results = selectStmt.executeQuery();

			while(results.next()) {
				int resultGameId = results.getInt("GameId");
				String resultGameName = results.getString("GameName");
				String resultDescription = results.getString("Description");
				Date resultReleaseDate = new Date(results.getTimestamp("ReleaseDate").getTime());
				BigDecimal resultPrice = results.getBigDecimal("Price");
				String resultPicLink = results.getString("PicLink");

				Games game = new Games(resultGameId, resultGameName, resultDescription, resultReleaseDate, resultPrice, resultPicLink);
				gamesOfDev.add(game);
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
		return gamesOfDev;
	}
	
	public List<Developers> getDevelopersOfGame(Games game) throws SQLException {
		List<Developers> devsOfGame = new ArrayList<>();
		String selectMatchingGames = "SELECT Developers.DeveloperId AS DeveloperId,DeveloperName FROM Games "
				+ "INNER JOIN gamedeveloperlinkers on Games.GameId = gamedeveloperlinkers.GameId "
				+ "INNER JOIN Developers on gamedeveloperlinkers.DeveloperId = Developers.DeveloperId "
				+ "WHERE Games.GameId=?";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectMatchingGames);
			selectStmt.setInt(1, game.getGameId());

			results = selectStmt.executeQuery();

			while(results.next()) {
				int resultDevId = results.getInt("DeveloperId");
				String resultDevName = results.getString("DeveloperName");

				Developers dev = new Developers(resultDevId, resultDevName);
				devsOfGame.add(dev);
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
		return devsOfGame;
	}
	
	public GameDeveloperLinkers delete(GameDeveloperLinkers linker) throws SQLException {
		String deleteGDLinker = "DELETE FROM GameDeveloperLinkers WHERE GDId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteGDLinker);
			deleteStmt.setInt(1, linker.getGdLinkerId());
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