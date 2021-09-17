package GamePedia.dal;

import GamePedia.model.*;
import GamePedia.model.Platforms.PlatformName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.math.BigDecimal;


public class GamePlatformLinkersDao {
    protected ConnectionManager connectionManager;

    private static GamePlatformLinkersDao instance = null;
    protected GamePlatformLinkersDao() {
        connectionManager = new ConnectionManager();
    }
    public static GamePlatformLinkersDao getInstance() {
        if(instance == null) {
            instance = new GamePlatformLinkersDao();
        }
        return instance;
    }

    public GamePlatformLinkers create(GamePlatformLinkers gamePlatformLinker) throws SQLException {
        String insertGamePlatformLinkers =
                "INSERT INTO GamePlatformLinkers(GPId, GameId, PlatformId) " +
                        "VALUES(?,?,?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        ResultSet resultKey = null;
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertGamePlatformLinkers,
                    Statement.RETURN_GENERATED_KEYS);
            insertStmt.setInt(1, gamePlatformLinker.getGameId());
            insertStmt.setInt(1, gamePlatformLinker.getPlatformId());
            insertStmt.executeUpdate();

            // Retrieve the auto-generated key and set it, so it can be used by the caller.
            resultKey = insertStmt.getGeneratedKeys();
            int gPId = -1;
            if(resultKey.next()) {
                gPId = resultKey.getInt(1);
            } else {
                throw new SQLException("Unable to retrieve auto-generated key.");
            }
            gamePlatformLinker.setGPId(gPId);
            return gamePlatformLinker;
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
            if(resultKey != null) {
                resultKey.close();
            }
        }
    }

    public GamePlatformLinkers delete(GamePlatformLinkers linker) throws SQLException {
        String deleteGamePlatformLinkers = "DELETE FROM GamePlatformLinkers WHERE GPId=?;";
        Connection connection = null;
        PreparedStatement deleteStmt = null;
        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deleteGamePlatformLinkers);
            deleteStmt.setInt(1, linker.getGPId());
            deleteStmt.executeUpdate();

            // Return null so the caller can no longer operate on the Persons instance.
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


    public List<Games> getGamesOfPlatforms(Platforms platform) throws SQLException {
        List<Games> gamesOfPlatforms = new ArrayList<>();
        String selectMatchingGames = "SELECT Games.GameId AS GameId,GameName,Description,ReleaseDate,Price,PicLink FROM Games "
                + "INNER JOIN GamePlatformLinkers on Games.GameId = GamePlatformLinkers.GameId "
                + "INNER JOIN Platforms on GamePlatformLinkers.PlatformId = Platforms.PlatformId "
                + "WHERE Platforms.PlatformId=?";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectMatchingGames);
            selectStmt.setInt(1, platform.getPlatformId());

            results = selectStmt.executeQuery();

            while(results.next()) {
                int resultGameId = results.getInt("GameId");
                String resultGameName = results.getString("GameName");
                String resultDescription = results.getString("Description");
                Date resultReleaseDate = new Date(results.getTimestamp("ReleaseDate").getTime());
                BigDecimal resultPrice = results.getBigDecimal("Price");
                String resultPicLink = results.getString("PicLink");

                Games game = new Games(resultGameId, resultGameName, resultDescription, resultReleaseDate, resultPrice, resultPicLink);
                gamesOfPlatforms.add(game);
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
        return gamesOfPlatforms;
    }
    
    public List<Platforms> getPlatformsOfGame(Games game) throws SQLException {
        List<Platforms> platformsOfGame = new ArrayList<>();
        String selectMatchingPlatforms = "SELECT Platforms.PlatformId as PlatformId,PlatformName FROM Games "
                + "INNER JOIN GamePlatformLinkers on Games.GameId = GamePlatformLinkers.GameId "
                + "INNER JOIN Platforms on GamePlatformLinkers.PlatformId = Platforms.PlatformId "
                + "WHERE Games.GameId=?";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectMatchingPlatforms);
            selectStmt.setInt(1, game.getGameId());

            results = selectStmt.executeQuery();

            while(results.next()) {
                int resultPlatformId = results.getInt("PlatformId");
                PlatformName resultPlatformName = PlatformName.valueOf(results.getString("PlatformName"));

                Platforms platform = new Platforms(resultPlatformId, resultPlatformName);
                platformsOfGame.add(platform);
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
        return platformsOfGame;
    }
}
