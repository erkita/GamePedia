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

import GamePedia.model.GamePublisherLinkers;
import GamePedia.model.Games;
import GamePedia.model.Publishers;

public class GamePublisherLinkersDao {
	protected ConnectionManager connectionManager;
	
	// Single pattern: instantiation is limited to one object.
	private static GamePublisherLinkersDao instance = null;
	protected GamePublisherLinkersDao() {
		connectionManager = new ConnectionManager();
	}
	public static GamePublisherLinkersDao getInstance() {
		if(instance == null) {
			instance = new GamePublisherLinkersDao();
		}
		return instance;
	}
	
	public GamePublisherLinkers create(GamePublisherLinkers gamePubLinker) throws SQLException {
		String insertGamePubLinker = "INSERT INTO GamePublisherLinkers(GameId,PublisherId) VALUES(?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		ResultSet resultKey = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertGamePubLinker, Statement.RETURN_GENERATED_KEYS);
			
			insertStmt.setInt(1, gamePubLinker.getGameId());
			insertStmt.setInt(2, gamePubLinker.getPublisherId());
			insertStmt.executeUpdate();
			
			// Retrieve the auto-generated key and set it, so it can be used by the caller.
			resultKey = insertStmt.getGeneratedKeys();
			int gpLinkerId = -1;
			if(resultKey.next()) {
				gpLinkerId = resultKey.getInt(1);
			} else {
				throw new SQLException("Unable to retrieve auto-generated key.");
			}
			gamePubLinker.setGpLinkerId(gpLinkerId);
			return gamePubLinker;
			
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
	
	public List<Games> getGamesOfPublisher(Publishers publisher) throws SQLException {
		List<Games> gamesOfPublisher = new ArrayList<>();
		String selectMatchingGames = "SELECT Games.GameId AS GameId,GameName,Description,ReleaseDate,Price,PicLink FROM Games "
				+ "INNER JOIN gamepublisherlinkers on Games.GameId = gamepublisherlinkers.GameId "
				+ "INNER JOIN Publishers on gamepublisherlinkers.PublisherId = Publishers.PublisherId "
				+ "WHERE Publishers.PublisherId=?";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectMatchingGames);
			selectStmt.setInt(1, publisher.getPublisherId());

			results = selectStmt.executeQuery();

			while(results.next()) {
				int resultGameId = results.getInt("GameId");
				String resultGameName = results.getString("GameName");
				String resultDescription = results.getString("Description");
				Date resultReleaseDate = new Date(results.getTimestamp("ReleaseDate").getTime());
				BigDecimal resultPrice = results.getBigDecimal("Price");
				String resultPicLink = results.getString("PicLink");

				Games game = new Games(resultGameId, resultGameName, resultDescription, resultReleaseDate, resultPrice, resultPicLink);
				gamesOfPublisher.add(game);
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
		return gamesOfPublisher;
	}
	
	public List<Publishers> getPublishersOfGame(Games game) throws SQLException {
		List<Publishers> publishersOfGame = new ArrayList<>();
		String selectMatchingGames = "SELECT Publishers.PublisherId AS PublisherId,PublisherName FROM Games "
				+ "INNER JOIN gamepublisherlinkers on Games.GameId = gamepublisherlinkers.GameId "
				+ "INNER JOIN Publishers on gamepublisherlinkers.PublisherId = Publishers.PublisherId "
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
				int resultPublisherId = results.getInt("PublisherId");
				String resultPublisherName = results.getString("PublisherName");

				Publishers publisher = new Publishers(resultPublisherId, resultPublisherName);
				publishersOfGame.add(publisher);
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
		return publishersOfGame;
	}
	
	public GamePublisherLinkers delete(GamePublisherLinkers linker) throws SQLException {
		String deleteLinker = "DELETE FROM GamePublisherLinkers WHERE GPId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteLinker);
			deleteStmt.setInt(1, linker.getGpLinkerId());
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