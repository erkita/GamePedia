package GamePedia.dal;

import GamePedia.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PopularTagsDao {
    protected ConnectionManager connectionManager;

    // Single pattern: instantiation is limited to one object.
    private static PopularTagsDao instance = null;
    protected PopularTagsDao() {
        connectionManager = new ConnectionManager();
    }
    public static PopularTagsDao getInstance() {
        if(instance == null) {
            instance = new PopularTagsDao();
        }
        return instance;
    }

    public PopularTags create(PopularTags popularTag) throws SQLException {
        String insertPopularTag = "INSERT INTO PopularTags(TagId, TagName) VALUES(?,?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertPopularTag);
            insertStmt.setInt(1, popularTag.getTagId());
            insertStmt.setString(2, popularTag.getTagName());
            insertStmt.executeUpdate();
            return popularTag;
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

    public PopularTags delete(PopularTags popularTag) throws SQLException {
        String deletePopularTag = "DELETE FROM PopularTags WHERE TagId=?;";
        Connection connection = null;
        PreparedStatement deleteStmt = null;
        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deletePopularTag);
            deleteStmt.setInt(1, popularTag.getTagId());
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

    public PopularTags getPopularTagById (int tagId) throws SQLException {
        String selectPopularTag = "SELECT TagId,TagName FROM PopularTags WHERE TagId=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectPopularTag);
            selectStmt.setInt(1, tagId);
            results = selectStmt.executeQuery();

            if(results.next()) {
                int resultTagId = results.getInt("TagId");
                String tagName = results.getString("TagName");
                PopularTags popularTag = new PopularTags(resultTagId, tagName);
                return popularTag;
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


    public List<PopularTags> getPopularTagsByTagName(String tagName) throws SQLException {
        List<PopularTags> popularTags = new ArrayList<PopularTags>();
        String selectPopularTags  =
                "SELECT TagId, TagName FROM PopularTags WHERE TagName=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectPopularTags);
            selectStmt.setString(1, tagName);
            results = selectStmt.executeQuery();
            while(results.next()) {
                int tagId = results.getInt("TagId");
                String resultTagName = results.getString("TagName");
                PopularTags popularTag = new PopularTags(tagId, resultTagName);
                popularTags.add(popularTag);
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
        return popularTags;
    }

}
