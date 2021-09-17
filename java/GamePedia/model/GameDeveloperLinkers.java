package GamePedia.model;

public class GameDeveloperLinkers {
	private int gdLinkerId;
	private int gameId;
	private int developerId;
	
	public GameDeveloperLinkers(int gdId, int gameId, int developerId) {
		this.gdLinkerId = gdId;
		this.gameId = gameId;
		this.developerId = developerId;
	}

	// Constructor to use if you are inserting into the database, so the gdId can be auto-generated.
	public GameDeveloperLinkers(int gameId, int developerId) {
		this.gameId = gameId;
		this.developerId = developerId;
	}

	/**
	 * @return the gdId
	 */
	public int getGdLinkerId() {
		return gdLinkerId;
	}

	/**
	 * @param gdId the gdId to set
	 */
	public void setGdLinkerId(int gdId) {
		this.gdLinkerId = gdId;
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
	 * @return the developerId
	 */
	public int getDeveloperId() {
		return developerId;
	}

	/**
	 * @param developerId the developerId to set
	 */
	public void setDeveloperId(int developerId) {
		this.developerId = developerId;
	}
}