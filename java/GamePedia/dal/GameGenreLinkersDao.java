package GamePedia.dal;

import GamePedia.model.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class GameGenreLinkersDao {
    protected ConnectionManager connectionManager;

    // Single pattern: instantiation is limited to one object.
    private static GameGenreLinkersDao instance = null;
    protected GameGenreLinkersDao() {
        connectionManager = new ConnectionManager();
    }
    public static GameGenreLinkersDao getInstance() {
        if(instance == null) {
            instance = new GameGenreLinkersDao();
        }
        return instance;
    }

    public GameGenreLinkers create(GameGenreLinkers ggLinker) throws SQLException {
        String insertGameGenreLinker = "INSERT INTO GameGenreLinkers(GGLinkerId, GameId, GenreId) VALUES(?,?,?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        ResultSet resultKey = null;
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertGameGenreLinker, Statement.RETURN_GENERATED_KEYS);

            insertStmt.setInt(1, ggLinker.getGameId());
            insertStmt.setInt(2, ggLinker.getGenreId());
            insertStmt.executeUpdate();

            // Retrieve the auto-generated key and set it, so it can be used by the caller.
            resultKey = insertStmt.getGeneratedKeys();
            int ggLinkerId = -1;
            if(resultKey.next()) {
                ggLinkerId = resultKey.getInt(1);
            } else {
                throw new SQLException("Unable to retrieve auto-generated key.");
            }
            ggLinker.setGGLinkerId(ggLinkerId);
            return ggLinker;

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

    public List<Games> getGamesOfGenre(Genres genre) throws SQLException {
        List<Games> gamesOfGenre = new ArrayList<>();
        String selectMatchingGames = "SELECT Games.GameId AS GameId,GameName,Description,ReleaseDate,Price,PicLink FROM Games "
                + "INNER JOIN GameGenreLinkers on Games.GameId = GameGenreLinkers.GameId "
                + "INNER JOIN Genres on GameGenreLinkers.genreId = Genres.genreId "
                + "WHERE Genres.genreId=?";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectMatchingGames);
            selectStmt.setInt(1, genre.getGenreId());

            results = selectStmt.executeQuery();

            while(results.next()) {
                int resultGameId = results.getInt("GameId");
                String resultGameName = results.getString("GameName");
                String resultDescription = results.getString("Description");
                Date resultReleaseDate = new Date(results.getTimestamp("ReleaseDate").getTime());
                BigDecimal resultPrice = results.getBigDecimal("Price");
                String resultPicLink = results.getString("PicLink");

                Games game = new Games(resultGameId, resultGameName, resultDescription, resultReleaseDate, resultPrice, resultPicLink);
                gamesOfGenre.add(game);
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
        return gamesOfGenre;
    }
    
    public List<Genres> getGenresOfGame(Games game) throws SQLException {
        List<Genres> genresOfGame = new ArrayList<>();
        String selectMatchingGenres = "SELECT Genres.GenreId as GenreId,GenreName FROM Games "
                + "INNER JOIN GameGenreLinkers on Games.GameId = GameGenreLinkers.GameId "
                + "INNER JOIN Genres on GameGenreLinkers.genreId = Genres.genreId "
                + "WHERE Games.GameId=?";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectMatchingGenres);
            selectStmt.setInt(1, game.getGameId());

            results = selectStmt.executeQuery();

            while(results.next()) {
                int resultGenreId = results.getInt("GenreId");
                String resultGenreName = results.getString("GenreName");

                Genres genre = new Genres(resultGenreId, resultGenreName);
                genresOfGame.add(genre);
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
        return genresOfGame;
    }


    public GameGenreLinkers delete(GameGenreLinkers linker) throws SQLException {
        String deleteGGLinkers = "DELETE FROM GameGenreLinkers WHERE GGId=?;";
        Connection connection = null;
        PreparedStatement deleteStmt = null;
        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deleteGGLinkers);
            deleteStmt.setInt(1, linker.getGGLinkerId());
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
