package GamePedia.model;

public class Platforms {
    protected int platformId;
    protected PlatformName platformName;
    
    public enum PlatformName {
        Mac, Windows, Linux
    }

    public Platforms(int platformId) {
        this.platformId = platformId;
    }

    public Platforms(int platformId, PlatformName platformName) {
        this.platformId = platformId;
        this.platformName = platformName;
    }

    public Platforms(PlatformName platformName) {
        this.platformName = platformName;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public PlatformName getPlatformName() {
        return platformName;
    }

    public void setPlatformName(PlatformName platformName) {
        this.platformName = platformName;
    }
}
