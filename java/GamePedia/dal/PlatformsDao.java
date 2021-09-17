package GamePedia.dal;

import GamePedia.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PlatformsDao {
    protected ConnectionManager connectionManager;

    private static PlatformsDao instance = null;
    protected PlatformsDao() {
        connectionManager = new ConnectionManager();
    }
    public static PlatformsDao getInstance() {
        if(instance == null) {
            instance = new PlatformsDao();
        }
        return instance;
    }

    public Platforms create(Platforms platform) throws SQLException {
        String insertPlatforms =
                "INSERT INTO Platforms(PlatformId,PlatformName) " +
                        "VALUES(?,?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        ResultSet resultKey = null;
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertPlatforms,
                    Statement.RETURN_GENERATED_KEYS);
            insertStmt.setString(1, platform.getPlatformName().name());
            insertStmt.executeUpdate();

            // Retrieve the auto-generated key and set it, so it can be used by the caller.
            resultKey = insertStmt.getGeneratedKeys();
            int platformId = -1;
            if(resultKey.next()) {
                platformId = resultKey.getInt(1);
            } else {
                throw new SQLException("Unable to retrieve auto-generated key.");
            }
            platform.setPlatformId(platformId);
            return platform;
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

    public Platforms delete(Platforms platform) throws SQLException {
        String deletePlatform = "DELETE FROM Platforms WHERE PlatformId=?;";
        Connection connection = null;
        PreparedStatement deleteStmt = null;
        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deletePlatform);
            deleteStmt.setInt(1, platform.getPlatformId());
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

    public Platforms getPlatformById(int platformId) throws SQLException {
        String selectPlatform =
                "SELECT PlatformId,PlatformName " +
                        "FROM Platforms " +
                        "WHERE PlatformId=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectPlatform);
            selectStmt.setInt(1, platformId);
            results = selectStmt.executeQuery();
            if(results.next()) {
                int resultPlatformId = results.getInt("PlatformId");
                Platforms.PlatformName platformName = Platforms.PlatformName.valueOf(results.getString("PlatformName"));
                Platforms platform = new Platforms(resultPlatformId, platformName);
                return platform;
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

    /**
     * Get all the platforms with a platform name.
     */
    public List<Platforms> getPlatformsByPlatformName(Platforms.PlatformName platformName)
            throws SQLException {
        List<Platforms> platforms = new ArrayList<Platforms>();
        String selectPlatforms =
                "SELECT PlatformId, PlatformName " +
                        "FROM Platforms " +
                        "WHERE PlatformName=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectPlatforms);
            selectStmt.setString(1, platformName.name());
            results = selectStmt.executeQuery();
            while(results.next()) {
                int platformId = results.getInt("PlatformId");
                Platforms platform = new Platforms(platformId, platformName);
                platforms.add(platform);
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
        return platforms;
    }




}
