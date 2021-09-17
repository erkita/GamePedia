package GamePedia.tools;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import GamePedia.dal.*;
import GamePedia.model.*;

public class Inserter {
	public static void main(String[] args) throws SQLException {
		// DAO Instances
		GamesDao gamesDao = GamesDao.getInstance();
		
		// Insert
		Games newGame = new Games(gamesDao.getNextGameId(), "A new game", "Description of a new game", new Date(), new BigDecimal(59.99), "www.google.com/piclink");
		newGame = gamesDao.create(newGame);
		
		// Read
		Games newGameRead = gamesDao.getGameById(newGame.getGameId());
		System.out.format("New game inserted is: id = %d, name = %s, description = %s, releasedate = %s, price = %.2f, piclink = %s \n", 
				newGameRead.getGameId(), newGameRead.getGameName(), newGameRead.getDescription(),
				newGameRead.getReleaseDate(), newGameRead.getPrice(), newGameRead.getPicLink());
		List<Games> matchingGames = gamesDao.getGamesByName("A new game");
		System.out.println("Games that match pattern of name \"A new game\":");
		for (Games game : matchingGames) {
			System.out.format("id = %d, name = %s, description = %s, releasedate = %s, price = %.2f, piclink = %s \n",
					game.getGameId(), game.getGameName(), game.getDescription(), game.getReleaseDate(),
					game.getPrice(), game.getPicLink());
		}
		
		// Update
		BigDecimal newPrice = new BigDecimal(13.99);
		newGame = gamesDao.updatePrice(newGame, newPrice);
		System.out.format("New price of game \"%s\" is %.2f \n", 
				newGame.getGameName(), newGame.getPrice());
		
		// Delete
		newGame = gamesDao.delete(newGame);
		System.out.format("newGame is now %s \n", newGame);
	}
}