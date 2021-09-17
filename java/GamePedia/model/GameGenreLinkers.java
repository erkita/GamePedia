package GamePedia.model;

public class GameGenreLinkers {
    protected int gGLinkerId;
    int gameId;
    int genreId;

    public GameGenreLinkers(int GGLinkerId, int gameId, int genreId) {
        this.gGLinkerId = GGLinkerId;
        this.gameId = gameId;
        this.genreId = genreId;
    }

    public GameGenreLinkers(int gameId, int genreId) {
        this.gameId = gameId;
        this.genreId = genreId;
    }

    public GameGenreLinkers(int gGLinkerId) {
        this.gGLinkerId = gGLinkerId;
    }

    public int getGGLinkerId() {
        return gGLinkerId;
    }

    public void setGGLinkerId(int gGLinkerId) {
        this.gGLinkerId = gGLinkerId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }
}
