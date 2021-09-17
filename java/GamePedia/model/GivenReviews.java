package GamePedia.model;

public class GivenReviews {
	private int givenReviewId;
	private int gameId;
	private int positiveRatings;
	private int negativeRatings;
	
	public GivenReviews(int givenReviewId, int gameId, int positiveRatings, int negativeRatings) {
		super();
		this.givenReviewId = givenReviewId;
		this.gameId = gameId;
		this.positiveRatings = positiveRatings;
		this.negativeRatings = negativeRatings;
	}
	public GivenReviews(int givenReviewId) {
		super();
		this.givenReviewId = givenReviewId;
	}
	public GivenReviews(int gameId, int positiveRatings, int negativeRatings) {
		super();
		this.gameId = gameId;
		this.positiveRatings = positiveRatings;
		this.negativeRatings = negativeRatings;
	}
	public int getGivenReviewId() {
		return givenReviewId;
	}
	public void setGivenReviewId(int givenReviewId) {
		this.givenReviewId = givenReviewId;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public int getPositiveRatings() {
		return positiveRatings;
	}
	public void setPositiveRatings(int positiveRatings) {
		this.positiveRatings = positiveRatings;
	}
	public int getNegativeRatings() {
		return negativeRatings;
	}
	public void setNegativeRatings(int negativeRatings) {
		this.negativeRatings = negativeRatings;
	}

}
