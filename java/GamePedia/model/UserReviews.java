package GamePedia.model;

import java.sql.SQLException;

import GamePedia.dal.GamesDao;
import GamePedia.dal.GivenReviewsDao;

public class UserReviews {
	private int userReviewId;
	private int userId;
	private int givenReviewId;
	private boolean isPositive;
	protected GamesDao gamesDao;
	protected GivenReviewsDao givenReviewsDao;
	
	
	public UserReviews(int userReviewId, int userId, int givenReviewsId, boolean isPositive) {
		this.userReviewId = userReviewId;
		this.userId = userId;
		this.givenReviewId = givenReviewsId;
		this.isPositive = isPositive;
	}
	
	public void init() {
		gamesDao = GamesDao.getInstance();
		givenReviewsDao = GivenReviewsDao.getInstance();
	}

	// Use this constructor when inserting into the database for the first time
	public UserReviews(int userId, int givenReviewsId, boolean isPositive) {
		this.userId = userId;
		this.givenReviewId = givenReviewsId;
		this.isPositive = isPositive;
	}

	public int getUserReviewId() {
		return userReviewId;
	}

	public void setUserReviewId(int userReviewId) {
		this.userReviewId = userReviewId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getGivenReviewsId() {
		return givenReviewId;
	}

	public void setGivenReviewsId(int givenReviewsId) {
		this.givenReviewId = givenReviewsId;
	}

	public boolean isPositive() {
		return isPositive;
	}

	public void setPositive(boolean isPositive) {
		this.isPositive = isPositive;
	}
	
	public Games getGameFromUserReviews() throws SQLException {
		GivenReviews review = givenReviewsDao.getGivenReviewById(this.givenReviewId);
		return gamesDao.getGameById(review.getGameId());
	}
}