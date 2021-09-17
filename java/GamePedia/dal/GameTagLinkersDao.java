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

import GamePedia.model.GameTagLinkers;
import GamePedia.model.Games;
import GamePedia.model.PopularTags;

public class GameTagLinkersDao {
	protected ConnectionManager connectionManager;
	
	// Single pattern: instantiation is limited to one object.
	private static GameTagLinkersDao instance = null;
	protected GameTagLinkersDao() {
		connectionManager = new ConnectionManager();
	}
	public static GameTagLinkersDao getInstance() {
		if(instance == null) {
			instance = new GameTagLinkersDao();
		}
		return instance;
	}
	
	public GameTagLinkers create(GameTagLinkers gameTagLinker) throws SQLException {
		String insertGameTagLinker = "INSERT INTO GameTagLinkers(GameId,TagId) VALUES(?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		ResultSet resultKey = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertGameTagLinker, Statement.RETURN_GENERATED_KEYS);
			
			insertStmt.setInt(1, gameTagLinker.getGameId());
			insertStmt.setInt(2, gameTagLinker.getTagId());
			insertStmt.executeUpdate();
			
			// Retrieve the auto-generated key and set it, so it can be used by the caller.
			resultKey = insertStmt.getGeneratedKeys();
			int gtLinkerId = -1;
			if(resultKey.next()) {
				gtLinkerId = resultKey.getInt(1);
			} else {
				throw new SQLException("Unable to retrieve auto-generated key.");
			}
			gameTagLinker.setGtId(gtLinkerId);
			return gameTagLinker;
			
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
	
	public List<Games> getGamesOfTag(PopularTags tag) throws SQLException {
		List<Games> gamesOfTag = new ArrayList<>();
		String selectMatchingGames = "SELECT Games.GameId AS GameId,GameName,Description,ReleaseDate,Price,PicLink FROM Games "
				+ "INNER JOIN GameTagLinkers on Games.GameId = GameTagLinkers.GameId "
				+ "INNER JOIN PopularTags on GameTagLinkers.TagId = PopularTags.TagId "
				+ "WHERE PopularTags.TagId=?";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectMatchingGames);
			selectStmt.setInt(1, tag.getTagId());

			results = selectStmt.executeQuery();

			while(results.next()) {
				int resultGameId = results.getInt("GameId");
				String resultGameName = results.getString("GameName");
				String resultDescription = results.getString("Description");
				Date resultReleaseDate = new Date(results.getTimestamp("ReleaseDate").getTime());
				BigDecimal resultPrice = results.getBigDecimal("Price");
				String resultPicLink = results.getString("PicLink");

				Games game = new Games(resultGameId, resultGameName, resultDescription, resultReleaseDate, resultPrice, resultPicLink);
				gamesOfTag.add(game);
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
		return gamesOfTag;
	}
	
	public List<PopularTags> getTagsOfGame(Games game) throws SQLException {
		List<PopularTags> tagsOfGame = new ArrayList<>();
		String selectMatchingGames = "SELECT PopularTags.TagId as TagId,TagName FROM Games "
				+ "INNER JOIN GameTagLinkers on Games.GameId = GameTagLinkers.GameId "
				+ "INNER JOIN PopularTags on GameTagLinkers.TagId = PopularTags.TagId "
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
				int resultTagId = results.getInt("TagId");
				String resultTagName = results.getString("TagName");
			
				PopularTags tag = new PopularTags(resultTagId, resultTagName);
				tagsOfGame.add(tag);
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
		return tagsOfGame;
	}
	
	
	public GameTagLinkers delete(GameTagLinkers linker) throws SQLException {
		String deleteGtLinker = "DELETE FROM GameTagLinkers WHERE GtId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteGtLinker);
			deleteStmt.setInt(1, linker.getGtId());
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
