package GamePedia.model;

public class Genres {
    protected int genreId;
    protected String genreName;

    public Genres(int genreId, String genreName) {
        this.genreId = genreId;
        this.genreName = genreName;
    }

    public Genres(int genreId) {
        this.genreId = genreId;
    }

    public Genres(String genreName) {
        this.genreName = genreName;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
