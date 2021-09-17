package GamePedia.model;

public class Publishers {
	private int publisherId;
	private String publisherName;
	
	public Publishers(int publisherId, String publisherName) {
		this.publisherId = publisherId;
		this.publisherName = publisherName;
	}

	// Constructor to use if object hasn't yet been inserted into the database
	public Publishers(String publisherName) {
		this.publisherName = publisherName;
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

	/**
	 * @return the publisherName
	 */
	public String getPublisherName() {
		return publisherName;
	}

	/**
	 * @param publisherName the publisherName to set
	 */
	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}
}