package GamePedia.model;

public class PopularTags {
    protected int tagId;
    protected String tagName;

    public PopularTags(int tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public PopularTags(int tagId) {
        this.tagId = tagId;
    }

    public PopularTags(String tagName) {
        this.tagName = tagName;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
