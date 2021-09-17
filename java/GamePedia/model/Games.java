package GamePedia.model;

import java.math.BigDecimal;
import java.util.Date;

public class Games {
	private int gameId;
	private String gameName;
	private String description;
	private Date releaseDate;
	private BigDecimal price;
	private String picLink;
	
	public Games(int gameId, String gameName, String description, Date releaseDate, BigDecimal price, String picLink) {
		this.gameId = gameId;
		this.gameName = gameName;
		this.description = description;
		this.releaseDate = releaseDate;
		this.price = price;
		this.picLink = picLink;
	}

	
	public Games(int gameId) {
		this.gameId = gameId;
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
	 * @return the gameName
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * @param gameName the gameName to set
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the releaseDate
	 */
	public Date getReleaseDate() {
		return releaseDate;
	}

	/**
	 * @param releaseDate the releaseDate to set
	 */
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the picLink
	 */
	public String getPicLink() {
		return picLink;
	}

	/**
	 * @param picLink the picLink to set
	 */
	public void setPicLink(String picLink) {
		this.picLink = picLink;
	}
		
}
