package GamePedia.model;

public class GameTagLinkers {
	private int gtId;
	private int gameId;
	private int tagId;
	
	public GameTagLinkers(int gtId, int gameId, int tagId) {
		super();
		this.gtId = gtId;
		this.gameId = gameId;
		this.tagId = tagId;
	}
	
	public GameTagLinkers(int gtId) {
		super();
		this.gtId = gtId;
	}
	
	public GameTagLinkers(int gameId, int tagId) {
		super();
		this.gameId = gameId;
		this.tagId = tagId;
	}

	public int getGtId() {
		return gtId;
	}

	public void setGtId(int gtId) {
		this.gtId = gtId;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}
	
}
