package GamePedia.servlet;

import GamePedia.dal.*;
import GamePedia.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Note the logic for doGet() and doPost() are almost identical. However, there is a difference:
 * doGet() handles the http GET request. This method is called when you put in the /findusers
 * URL in the browser.
 * doPost() handles the http POST request. This method is called after you click the submit button.
 * 
 * To run:
 * 1. Run the SQL script to recreate your database schema: http://goo.gl/86a11H.
 * 2. Insert test data. You can do this by running blog.tools.Inserter (right click,
 *    Run As > JavaApplication.
 *    Notice that this is similar to Runner.java in our JDBC example.
 * 3. Run the Tomcat server at localhost.
 * 4. Point your browser to http://localhost:8080/BlogApplication/findusers.
 */

@WebServlet("/gamedetails")
public class GameDetails extends HttpServlet {
	
	protected GamesDao gamesDao;
	protected GameDeveloperLinkersDao gamesDevLinkersDao;
	protected GamePublisherLinkersDao gamesPubLinkersDao;
	protected GameTagLinkersDao gamesTagLinkersDao;
	protected GivenReviewsDao givenReviewsDao;
	protected GameGenreLinkersDao gamesGenresDao;
	protected GameNumPlayersLinkersDao numPlayersDao;
	protected GamePlatformLinkersDao gamePlatformLinkersDao;
	
	@Override
	public void init() throws ServletException {
		gamesDao = GamesDao.getInstance();
		gamesDevLinkersDao = GameDeveloperLinkersDao.getInstance();
		gamesPubLinkersDao = GamePublisherLinkersDao.getInstance();
		gamesTagLinkersDao = GameTagLinkersDao.getInstance();
		givenReviewsDao = GivenReviewsDao.getInstance();
		gamesGenresDao = GameGenreLinkersDao.getInstance();
		numPlayersDao = GameNumPlayersLinkersDao.getInstance();
		gamePlatformLinkersDao = GamePlatformLinkersDao.getInstance();
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Map for storing messages.
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        Games game = null;
        List<Developers> devs = new ArrayList<>();
        List<Publishers> pubs = new ArrayList<>();
        List<PopularTags> tags = new ArrayList<>();
        GivenReviews reviews = null;
        List<Genres> genres = new ArrayList<>();
        List<NumPlayers> numPlayers = new ArrayList<>();
        List<Platforms> platforms = new ArrayList<>();
        
        // Retrieve and validate name.
        // gamename is retrieved from the URL query string.
        String gameName = req.getParameter("gamename");
        if (gameName == null || gameName.trim().isEmpty()) {
            messages.put("success", "Please enter a valid name.");
        } else {
        	// Retrieve Games, and store as a message.
        	try {
            	game = gamesDao.getGamesByName(gameName).get(0);
            	devs = gamesDevLinkersDao.getDevelopersOfGame(game);
            	pubs = gamesPubLinkersDao.getPublishersOfGame(game);
            	tags = gamesTagLinkersDao.getTagsOfGame(game);
            	reviews = givenReviewsDao.getReviewsByGameId(game.getGameId());
            	genres = gamesGenresDao.getGenresOfGame(game);
            	numPlayers = numPlayersDao.getNumPlayersOfGame(game);
            	platforms = gamePlatformLinkersDao.getPlatformsOfGame(game);
            } catch (SQLException e) {
    			e.printStackTrace();
    			throw new IOException(e);
            }
        	messages.put("success", "Displaying results for " + gameName);
        	// Save the previous search term, so it can be used as the default
        	// in the input box when rendering FindGames.jsp.
        	messages.put("previousGameName", gameName);
        }
        
        // Calculate stats for use in the jsp
        double reviewTotal = reviews.getPositiveRatings() + reviews.getNegativeRatings();
        int percentApproval = (int)((double)reviews.getPositiveRatings() / reviewTotal * 100);
        int percentDisapproval = 100 - percentApproval;
        
        // Set the attributes to be used in the jsp
        req.setAttribute("game", game);
        req.setAttribute("devs", devs);
        req.setAttribute("pubs", pubs);
        req.setAttribute("tags", tags);
        req.setAttribute("aggregateReviews", reviews);
        req.setAttribute("percentApproval", percentApproval);
        req.setAttribute("percentDisapproval", percentDisapproval);
        req.setAttribute("genres", genres);
        req.setAttribute("numPlayers", numPlayers);
        req.setAttribute("platforms", platforms);
        
        req.getRequestDispatcher("/GameDetails.jsp").forward(req, resp);
	}
	
	@Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
		
		// Map for storing messages.
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        // Variables to add
        Games game = null;
        List<Developers> devs = new ArrayList<>();
        List<Publishers> pubs = new ArrayList<>();
        List<PopularTags> tags = new ArrayList<>();
        GivenReviews reviews = null;
        List<Genres> genres = new ArrayList<>();
        List<NumPlayers> numPlayers = new ArrayList<>();
        List<Platforms> platforms = new ArrayList<>();
        
        // Retrieve and validate name.
        // gamename is retrieved from the URL query string.
        String gameName = req.getParameter("gamename");
        if (gameName == null || gameName.trim().isEmpty()) {
            messages.put("success", "Please enter a valid name.");
        } else {
        	// Retrieve Games, and store as a message.
        	try {
            	game = gamesDao.getGamesByName(gameName).get(0);
            	devs = gamesDevLinkersDao.getDevelopersOfGame(game);
            	pubs = gamesPubLinkersDao.getPublishersOfGame(game);
            	tags = gamesTagLinkersDao.getTagsOfGame(game);
            	reviews = givenReviewsDao.getReviewsByGameId(game.getGameId());
            	genres = gamesGenresDao.getGenresOfGame(game);
            	numPlayers = numPlayersDao.getNumPlayersOfGame(game);
            	platforms = gamePlatformLinkersDao.getPlatformsOfGame(game);
            } catch (SQLException e) {
    			e.printStackTrace();
    			throw new IOException(e);
            }
        	messages.put("success", "Displaying results for " + gameName);
        	// Save the previous search term, so it can be used as the default
        	// in the input box when rendering FindGames.jsp.
        	messages.put("previousGameName", gameName);
        }
        
        // Calculate stats for use in the jsp
        double reviewTotal = reviews.getPositiveRatings() + reviews.getNegativeRatings();
        int percentApproval = (int)((double)reviews.getPositiveRatings() / reviewTotal * 100);
        int percentDisapproval = 100 - percentApproval;
        
       
        req.setAttribute("game", game);
        req.setAttribute("devs", devs);
        req.setAttribute("pubs", pubs);
        req.setAttribute("tags", tags);
        req.setAttribute("aggregateReviews", reviews);
        req.setAttribute("percentApproval", percentApproval);
        req.setAttribute("percentDisapproval", percentDisapproval);
        req.setAttribute("genres", genres);
        req.setAttribute("numPlayers", numPlayers);
        req.setAttribute("platforms", platforms);
        
        req.getRequestDispatcher("/GameDetails.jsp").forward(req, resp);
    }
}
