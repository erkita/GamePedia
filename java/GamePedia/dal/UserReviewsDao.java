package GamePedia.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import GamePedia.model.Games;
import GamePedia.model.UserReviews;
import GamePedia.model.Users;

public class UserReviewsDao {
	protected ConnectionManager connectionManager;
	protected GivenReviewsDao givenReviewsDao;
	
	// Single pattern: instantiation is limited to one object.
	private static UserReviewsDao instance = null;
	protected UserReviewsDao() {
		connectionManager = new ConnectionManager();
	}
	public static UserReviewsDao getInstance() {
		if(instance == null) {
			instance = new UserReviewsDao();
		}
		return instance;
	}
	
	public UserReviews create(UserReviews review) throws SQLException {
		String insertUserReview = "INSERT INTO UserReviews(UserId,GivenReviewId,IsPositive) VALUES(?,?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		ResultSet resultKey = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertUserReview, Statement.RETURN_GENERATED_KEYS);
			
			insertStmt.setInt(1, review.getUserId());
			insertStmt.setInt(2, review.getGivenReviewsId());
			insertStmt.setBoolean(3, review.isPositive());
			insertStmt.executeUpdate();
			
			
			// Retrieve the auto-generated key and set it, so it can be used by the caller.
			resultKey = insertStmt.getGeneratedKeys();
			int reviewId = -1;
			if(resultKey.next()) {
				reviewId = resultKey.getInt(1);
			} else {
				throw new SQLException("Unable to retrieve auto-generated key.");
			}
			review.setUserReviewId(reviewId);
			
			// Now that the review is made, update positive/negative tally in GivenReviews using the Dao
			GivenReviewsDao.addToReviewTotals(review);
			
			return review;
			
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
	
	// Returns a list of all reviews a user has given
	// Can be used to display all reviews
	public List<UserReviews> getAllUserReviewsOfUser(Users user) throws SQLException {
		List<UserReviews> usersReviews = new ArrayList<>();
		String selectUsersReviews = "SELECT * FROM UserReviews WHERE UserId=?";
		
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectUsersReviews);
			selectStmt.setInt(1, user.getUserId());

			results = selectStmt.executeQuery();

			while(results.next()) {
				int resultUserReviewId = results.getInt("UserReviewId");
				int resultUserId = results.getInt("UserId");
				int resultGivenReviewId = results.getInt("GivenReviewId");
				boolean resultIsPositive = results.getBoolean("IsPositive");

				UserReviews review = new UserReviews(resultUserReviewId, resultUserId, resultGivenReviewId, resultIsPositive);
				usersReviews.add(review);
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
		return usersReviews;
	}
	
	// Returns a list of user reviews which are solely positive or negative
	// Can be useful when determining recommendations - we would only want reviews that are positive
	public List<UserReviews> getUserReviewsOfUserBySentiment(Users user, boolean isPositiveReview) throws SQLException {
		List<UserReviews> usersReviews = new ArrayList<>();
		String selectUsersReviews = "SELECT * FROM UserReviews WHERE UserId=? and IsPositive=?";
		
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectUsersReviews);
			selectStmt.setInt(1, user.getUserId());
			selectStmt.setBoolean(2, isPositiveReview);

			results = selectStmt.executeQuery();

			while(results.next()) {
				int resultUserReviewId = results.getInt("UserReviewId");
				int resultUserId = results.getInt("UserId");
				int resultGivenReviewId = results.getInt("GivenReviewId");
				boolean resultIsPositive = results.getBoolean("IsPositive");

				UserReviews review = new UserReviews(resultUserReviewId, resultUserId, resultGivenReviewId, resultIsPositive);
				usersReviews.add(review);
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
		return usersReviews;
	}
	
	public int getNextUserReviewId() throws SQLException {
		String findBiggestId = "SELECT UserReviewId "
				+ "FROM UserReviews "
				+ "GROUP BY UserReviewId DESC LIMIT 1";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(findBiggestId);
			results = selectStmt.executeQuery();
			if(results.next()) {
				int greatestId = results.getInt("UserReviewId");
				return (1 + greatestId);
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
	
	public UserReviews getUserReviewById(int userReviewId) throws SQLException {
		String findId = "SELECT * "
				+ "FROM UserReviews "
				+ "WHERE UserReviewId=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(findId);
			selectStmt.setInt(1, userReviewId);
			results = selectStmt.executeQuery();
			while(results.next()) {
				int resultUserId = results.getInt("UserId");
				int resultGivenReviewId = results.getInt("GivenReviewId");
				boolean resultIsPositive = results.getBoolean("IsPositive");
				return new UserReviews(userReviewId, resultUserId, resultGivenReviewId, resultIsPositive);
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
	
	public UserReviews getUserReviewByUserAndGivenReviewId(int userId, int reviewId) throws SQLException {
		String findId = "SELECT * "
				+ "FROM UserReviews "
				+ "WHERE UserId=?, GivenReviewId=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(findId);
			selectStmt.setInt(1, userId);
			selectStmt.setInt(2, reviewId);
			results = selectStmt.executeQuery();
			while(results.next()) {
				int userReviewId = results.getInt("UserReviewId");
				int resultUserId = results.getInt("UserId");
				int resultGivenReviewId = results.getInt("GivenReviewId");
				boolean resultIsPositive = results.getBoolean("IsPositive");
				return new UserReviews(userReviewId, resultUserId, resultGivenReviewId, resultIsPositive);
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
	
	public List<UserReviews> getUserReviewsOfGame(Games game) throws SQLException {
		List<UserReviews> usersReviews = new ArrayList<>();
		String selectGameReviews = "SELECT UserReviewId,UserId,UserReviews.GivenReviewId AS GivenReviewId,IsPositive FROM UserReviews"
				+ "INNER JOIN GivenReviews ON UserReviews.GivenReviewId = GivenReviews.GivenReviewId"
				+ "INNER JOIN Games on GivenReviews.GameId = Games.GameId"
				+ "WHERE Games.GameId=?";
		
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectGameReviews);
			selectStmt.setInt(1, game.getGameId());

			results = selectStmt.executeQuery();

			while(results.next()) {
				int resultUserReviewId = results.getInt("UserReviewId");
				int resultUserId = results.getInt("UserId");
				int resultGivenReviewId = results.getInt("GivenReviewId");
				boolean resultIsPositive = results.getBoolean("IsPositive");

				UserReviews review = new UserReviews(resultUserReviewId, resultUserId, resultGivenReviewId, resultIsPositive);
				usersReviews.add(review);
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
		return usersReviews;
	}
	
	public UserReviews updateReviewSentiment(UserReviews review) throws SQLException {
		boolean originalSentiment = review.isPositive();
		String updateIsPositive = "UPDATE UserReviews SET IsPositive=? WHERE UserReviewId=?;";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updateIsPositive);
			updateStmt.setBoolean(1, !originalSentiment);
			updateStmt.setInt(2, review.getUserReviewId());
			updateStmt.executeUpdate();
			
			// Now update the GivenReviews tallys accordingly
			// Subtract from the original tally
			GivenReviewsDao.subtractFromReviewTotals(review);
			// Update the review to have the new sentiment
			review.setPositive(!originalSentiment);
			// Add the new sentiment to the tally
			GivenReviewsDao.addToReviewTotals(review);
			
			return review;
			
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
	
	public UserReviews delete(UserReviews review) throws SQLException {
		String deleteUserReview = "DELETE FROM UserReviews WHERE UserReviewId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteUserReview);
			deleteStmt.setInt(1, review.getUserReviewId());
			deleteStmt.executeUpdate();
			
			// Update the tallys
			GivenReviewsDao.subtractFromReviewTotals(review);

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