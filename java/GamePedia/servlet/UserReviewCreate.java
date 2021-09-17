package GamePedia.servlet;

import GamePedia.dal.*;
import GamePedia.model.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/userreviewcreate")
public class UserReviewCreate extends HttpServlet {
	
	protected UserReviewsDao userReviewsDao;
	protected UsersDao usersDao;
	protected GamesDao gamesDao;
	protected GivenReviewsDao givenReviewsDao;
	
	@Override
	public void init() throws ServletException {
		userReviewsDao = UserReviewsDao.getInstance();
		usersDao = UsersDao.getInstance();
		gamesDao = GamesDao.getInstance();
		givenReviewsDao = GivenReviewsDao.getInstance();
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Map for storing messages.
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);
        
        // Megan test - prepopulate the gameName and get the givenReviewId to get the game
        Games game = null;
        int givenReviewId = 0;
        
        String gameName = req.getParameter("gamename");
        if (gameName == null || gameName.trim().isEmpty()) {
            messages.put("success", "Please enter a valid game name.");
        } else {
        	// Retrieve Games, and store as a message.
        	try {
            	game = gamesDao.getGamesByName(gameName).get(0);
            	givenReviewId = givenReviewsDao.getReviewsByGameId(game.getGameId()).getGivenReviewId();
            } catch (SQLException e) {
    			e.printStackTrace();
    			throw new IOException(e);
            }
        	messages.put("success", "Displaying results for " + gameName);
        	// Save the previous search term, so it can be used as the default
        	// in the input box when rendering FindGames.jsp.
        	messages.put("previousGameName", gameName);
        }
        
        req.setAttribute("game", game);
        req.setAttribute("givenreviewid", givenReviewId);
        
        
        //Just render the JSP.   
        req.getRequestDispatcher("/UserReviewCreate.jsp").forward(req, resp);
	}
	
	@Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
        // Map for storing messages.
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        int userReviewId;
    	try{
    		userReviewId = userReviewsDao.getNextUserReviewId();
        }catch(SQLException e){
			e.printStackTrace();
			throw new IOException(e);
        }
        
    	String gameName = req.getParameter("gamename");
    	int gameId = Integer.parseInt(req.getParameter("gameid"));
        String username = req.getParameter("username");
        int userId = 0;
		try {
			userId = usersDao.getUserIdFromUsername(username);
		} catch (SQLException e1) {
			req.setAttribute("succeed", false);
            req.setAttribute("msg", "username not found");
			e1.printStackTrace();
		}
        boolean isPositive = Boolean.parseBoolean(req.getParameter("review"));
        int givenReviewId = 0;
		try {
			givenReviewId = givenReviewsDao.getReviewsByGameId(gameId).getGivenReviewId();
		} catch (SQLException e1) {
			req.setAttribute("succeed", false);
            req.setAttribute("msg", "given review not found");
			e1.printStackTrace();
		}
    	try{
    		UserReviews userReview = new UserReviews(userReviewId, userId, givenReviewId, isPositive);
    		userReview = userReviewsDao.create(userReview);
    		messages.put("success", "Successfully created " + gameName + " review");
        }catch(SQLException e){
			e.printStackTrace();
			throw new IOException(e);
        }
        
        req.getRequestDispatcher("/UserReviewCreate.jsp").forward(req, resp);
    }
}
