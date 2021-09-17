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

import GamePedia.model.GameNumPlayersLinkers;
import GamePedia.model.Games;
import GamePedia.model.NumPlayers;
import GamePedia.model.NumPlayers.NumberName;

public class GameNumPlayersLinkersDao {
	protected ConnectionManager connectionManager;
	
	// Single pattern: instantiation is limited to one object.
	private static GameNumPlayersLinkersDao instance = null;
	protected GameNumPlayersLinkersDao() {
		connectionManager = new ConnectionManager();
	}
	public static GameNumPlayersLinkersDao getInstance() {
		if(instance == null) {
			instance = new GameNumPlayersLinkersDao();
		}
		return instance;
	}
	
	public GameNumPlayersLinkers create(GameNumPlayersLinkers gnLinker) throws SQLException {
		String insertGameNumPlayersLinker = "INSERT INTO GameNumPlayersLinkers(GameId,NumPlayersId) VALUES(?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		ResultSet resultKey = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertGameNumPlayersLinker, Statement.RETURN_GENERATED_KEYS);
			
			insertStmt.setInt(1, gnLinker.getGameId());
			insertStmt.setInt(2, gnLinker.getNumPlayersId());
			insertStmt.executeUpdate();
			
			// Retrieve the auto-generated key and set it, so it can be used by the caller.
			resultKey = insertStmt.getGeneratedKeys();
			int gnLinkerId = -1;
			if(resultKey.next()) {
				gnLinkerId = resultKey.getInt(1);
			} else {
				throw new SQLException("Unable to retrieve auto-generated key.");
			}
			gnLinker.setGnLinkerId(gnLinkerId);
			return gnLinker;
			
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
	
	public List<Games> getGamesOfNumPlyers(NumPlayers numPlayers) throws SQLException {
		List<Games> gamesOfNumPlayers = new ArrayList<>();
		String selectMatchingGames = "SELECT Games.GameId AS GameId,GameName,Description,ReleaseDate,Price,PicLink FROM Games "
				+ "INNER JOIN gameNumPlayersLinkers on Games.GameId = gameNumPlayersLinkers.GameId "
				+ "INNER JOIN NumPlayers on gameNumPlayersLinkers.numPlayersId = NumPlayers.numPlayersId "
				+ "WHERE NumPlayers.numPlayersId=?";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectMatchingGames);
			selectStmt.setInt(1, numPlayers.getNumPlayersId());

			results = selectStmt.executeQuery();

			while(results.next()) {
				int resultGameId = results.getInt("GameId");
				String resultGameName = results.getString("GameName");
				String resultDescription = results.getString("Description");
				Date resultReleaseDate = new Date(results.getTimestamp("ReleaseDate").getTime());
				BigDecimal resultPrice = results.getBigDecimal("Price");
				String resultPicLink = results.getString("PicLink");

				Games game = new Games(resultGameId, resultGameName, resultDescription, resultReleaseDate, resultPrice, resultPicLink);
				gamesOfNumPlayers.add(game);
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
		return gamesOfNumPlayers;
	}
	
	public List<NumPlayers> getNumPlayersOfGame(Games game) throws SQLException {
		List<NumPlayers> numPlayersOfGame = new ArrayList<>();
		String selectMatchingNumPlayers = "SELECT NumPlayers.NumPlayersId as NumPlayersId,NumberName FROM Games "
				+ "INNER JOIN gameNumPlayersLinkers on Games.GameId = gameNumPlayersLinkers.GameId "
				+ "INNER JOIN NumPlayers on gameNumPlayersLinkers.numPlayersId = NumPlayers.numPlayersId "
				+ "WHERE Games.GameId=?";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectMatchingNumPlayers);
			selectStmt.setInt(1, game.getGameId());

			results = selectStmt.executeQuery();

			while(results.next()) {
				int resultNumPlayersId = results.getInt("NumPlayersId");
				NumberName resultName = NumberName.valueOf(
						results.getString("NumberName"));
				NumPlayers numPlayer = new NumPlayers(resultNumPlayersId, resultName);
				numPlayersOfGame.add(numPlayer);
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
		return numPlayersOfGame;
	}

	
	public GameNumPlayersLinkers delete(GameNumPlayersLinkers linker) throws SQLException {
		String deleteGnLinker = "DELETE FROM GameNumPlayersLinkers WHERE gnLinkerId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteGnLinker);
			deleteStmt.setInt(1, linker.getGnLinkerId());
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
