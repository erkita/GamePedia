package GamePedia.model;

public class GameNumPlayersLinkers {
	private int gnLinkerId;
	private int gameId;
	private int numPlayersId;
	
	public GameNumPlayersLinkers(int gnLinkerId, int gameId, int numPlayersId) {
		super();
		this.gnLinkerId = gnLinkerId;
		this.gameId = gameId;
		this.numPlayersId = numPlayersId;
	}

	public GameNumPlayersLinkers(int gnLinkerId) {
		super();
		this.gnLinkerId = gnLinkerId;
	}

	public GameNumPlayersLinkers(int gameId, int numPlayersId) {
		super();
		this.gameId = gameId;
		this.numPlayersId = numPlayersId;
	}

	public int getGnLinkerId() {
		return gnLinkerId;
	}

	public void setGnLinkerId(int gnLinkerId) {
		this.gnLinkerId = gnLinkerId;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getNumPlayersId() {
		return numPlayersId;
	}

	public void setNumPlayersId(int numPlayersId) {
		this.numPlayersId = numPlayersId;
	}
	
}
