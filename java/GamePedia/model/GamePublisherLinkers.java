package GamePedia.model;

public class GamePublisherLinkers {
	private int gpLinkerId;
	private int gameId;
	private int publisherId;
	
	public GamePublisherLinkers(int gpLinkerId, int gameId, int publisherId) {
		this.gpLinkerId = gpLinkerId;
		this.gameId = gameId;
		this.publisherId = publisherId;
	}

	// Constructor to use when the object has not yet been inserted into the database
	public GamePublisherLinkers(int gameId, int publisherId) {
		this.gameId = gameId;
		this.publisherId = publisherId;
	}

	/**
	 * @return the gpLinkerId
	 */
	public int getGpLinkerId() {
		return gpLinkerId;
	}

	/**
	 * @param gpLinkerId the gpLinkerId to set
	 */
	public void setGpLinkerId(int gpLinkerId) {
		this.gpLinkerId = gpLinkerId;
	}

	/**
	 * @return the gameId
	 */
	public int getGameId() {
		return gameId;
	}

	/**
	 * @param gameId the gameId to set
	 */
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	/**
	 * @return the publisherId
	 */
	public int getPublisherId() {
		return publisherId;
	}

	/**
	 * @param publisherId the publisherId to set
	 */
	public void setPublisherId(int publisherId) {
		this.publisherId = publisherId;
	}
}