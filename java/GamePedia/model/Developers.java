package GamePedia.model;

public class Developers {
	private int developerId;
	private String developerName;
	
	/**
	 * @param developerId The unique Id of the developer
	 * @param developerName - The name of the developer
	 */
	public Developers(int developerId, String developerName) {
		this.developerId = developerId;
		this.developerName = developerName;
	}
	

	public Developers(String developerName) {
		this.developerName = developerName;
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

	/**
	 * @return the developerName
	 */
	public String getDeveloperName() {
		return developerName;
	}

	/**
	 * @param developerName the developerName to set
	 */
	public void setDeveloperName(String developerName) {
		this.developerName = developerName;
	}
	
}