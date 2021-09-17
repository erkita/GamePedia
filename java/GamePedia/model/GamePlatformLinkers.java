package GamePedia.model;

public class GamePlatformLinkers {
    protected int GPId;
    protected int gameId;
    protected int platformId;

    public GamePlatformLinkers(int GPId, int gameId, int platformId) {
        this.GPId = GPId;
        this.gameId = gameId;
        this.platformId = platformId;
    }

    public GamePlatformLinkers(int gameId, int platformId) {
        this.gameId = gameId;
        this.platformId = platformId;
    }

    public GamePlatformLinkers(int GPId) {
        this.GPId = GPId;
    }

    public int getGPId() {
        return GPId;
    }

    public void setGPId(int GPId) {
        this.GPId = GPId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }
}
