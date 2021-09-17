package GamePedia.dal;

import GamePedia.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class GivenReviewsDao {
	protected static ConnectionManager connectionManager;

	private static GivenReviewsDao instance = null;
	protected GivenReviewsDao() {
		connectionManager = new ConnectionManager();
	}
	public static GivenReviewsDao getInstance() {
		if(instance == null) {
			instance = new GivenReviewsDao();
		}
		return instance;
	}
	
	public static int getNextGivenReviewId() throws SQLException {
		String findBiggestId = "SELECT MAX(GivenReviewId) AS GivenReviewId FROM GivenReviews";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(findBiggestId);
			results = selectStmt.executeQuery();
			if(results.next()) {
				return (1 + results.getInt("GivenReviewId"));
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

	public GivenReviews create(GivenReviews review) throws SQLException {
		String insertReview =
			"INSERT INTO GivenReviews(GivenReviewId,GameId,PositiveRatings,NegativeRatings)" +
			"VALUES(?,?,?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertReview);
			int nextId = getNextGivenReviewId();

			insertStmt.setInt(1, nextId);
			insertStmt.setInt(2, review.getGameId());
			insertStmt.setInt(3, review.getPositiveRatings());
			insertStmt.setInt(4, review.getNegativeRatings());
			insertStmt.executeUpdate();
			
			review.setGivenReviewId(nextId);
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


	public GivenReviews delete(GivenReviews review) throws SQLException {
		String deleteReview = "DELETE FROM GivenReviews WHERE GivenReviewId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteReview);
			deleteStmt.setInt(1, review.getGivenReviewId());
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


	public GivenReviews getGivenReviewById(int reviewId) throws SQLException {
		String selectGivenReview = "SELECT GivenReviewId,GameId,PositiveRatings,NegativeRatings" +
				"FROM GivenReviews WHERE GivenReviewId=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectGivenReview);
			selectStmt.setInt(1, reviewId);

			results = selectStmt.executeQuery();

			if(results.next()) {
				int resultGivenReviewId = results.getInt("GivenReviewId");
				int gameId = results.getInt("GameId");
				int positiveRatings = results.getInt("PositiveRatings");
				int negativeRatings = results.getInt("NegativeRatings");
				
				GivenReviews review = new GivenReviews(resultGivenReviewId, gameId, positiveRatings, negativeRatings);
				return review;
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


	public GivenReviews getReviewsByGameId(int gameId) throws SQLException {
		String selectReviews =
			"SELECT GivenReviewId,GameId,PositiveRatings,NegativeRatings " +
			"FROM GivenReviews " +
			"WHERE gameId=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectReviews);
			selectStmt.setInt(1, gameId);
			results = selectStmt.executeQuery();

			if(results.next()) {
				int reviewId = results.getInt("GivenReviewId");
				int resultGameId = results.getInt("GameId");
				int positiveRatings = results.getInt("PositiveRatings");
				int negativeRatings = results.getInt("NegativeRatings");
				
				GivenReviews review = new GivenReviews(reviewId, resultGameId, positiveRatings, negativeRatings);
				return review;
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
	
	// Invoked whenever an individual UserReview is created or updated to have a new sentiment
	public static void addToReviewTotals(UserReviews review) throws SQLException {
		String updateReviewTotals = "UPDATE GivenReviews SET PositiveRatings=PositiveRatings + 1 WHERE GivenReviewId=?;";
		if (!review.isPositive()) {
			updateReviewTotals = "UPDATE GivenReviews SET NegativeRatings=NegativeRatings + 1 WHERE GivenReviewId=?;";;
		}
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updateReviewTotals);
			updateStmt.setInt(1, review.getGivenReviewsId());
			updateStmt.executeUpdate();
			
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
	
	// Invoked whenever an individual UserReview is deleted or updated to have a new sentiment
	public static void subtractFromReviewTotals(UserReviews review) throws SQLException {
		String updateReviewTotals = "UPDATE GivenReviews SET PositiveRatings=PositiveRatings - 1 WHERE GivenReviewId=?;";
		if (!review.isPositive()) {
			updateReviewTotals = "UPDATE GivenReviews SET NegativeRatings=NegativeRatings - 1 WHERE GivenReviewId=?;";;
		}
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updateReviewTotals);
			updateStmt.setInt(1, review.getGivenReviewsId());
			updateStmt.executeUpdate();
			
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
	
}


