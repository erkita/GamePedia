package GamePedia.dal;
import GamePedia.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GenresDao {
    protected ConnectionManager connectionManager;

    // Single pattern: instantiation is limited to one object.
    private static GenresDao instance = null;
    protected GenresDao() {
        connectionManager = new ConnectionManager();
    }
    public static GenresDao getInstance() {
        if(instance == null) {
            instance = new GenresDao();
        }
        return instance;
    }

    public Genres create(Genres genre) throws SQLException {
        String insertPopularTag = "INSERT INTO Genres(genreId, genreName) VALUES(?,?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertPopularTag);
            insertStmt.setInt(1, genre.getGenreId());
            insertStmt.setString(2, genre.getGenreName());
            insertStmt.executeUpdate();
            return genre;
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

    public Genres delete(Genres genre) throws SQLException {
        String deleteGenre = "DELETE FROM Genres WHERE GenreId=?;";
        Connection connection = null;
        PreparedStatement deleteStmt = null;
        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deleteGenre);
            deleteStmt.setInt(1, genre.getGenreId());
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

    public Genres getGenreById (int genreId) throws SQLException {
        String selectGenre = "SELECT GenreId,GenreName FROM Genres WHERE GenreId=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectGenre);
            selectStmt.setInt(1, genreId);
            results = selectStmt.executeQuery();

            if(results.next()) {
                int resultGenreId = results.getInt("GenreId");
                String genreName = results.getString("GenreName");
                Genres genre = new Genres(resultGenreId, genreName);
                return genre;
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


    public List<Genres> getGenresByGenreName(String genreName) throws SQLException {
        List<Genres> genres = new ArrayList<Genres>();
        String selectGenres  =
                "SELECT GenreId, GenreName FROM Genres WHERE GenreName=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectGenres);
            selectStmt.setString(1, genreName);
            results = selectStmt.executeQuery();
            while(results.next()) {
                int genreId = results.getInt("GenreId");
                String resultGenreName = results.getString("GenreName");
                Genres genre = new Genres(genreId, resultGenreName);
                genres.add(genre);
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
        return genres;
    }
}
