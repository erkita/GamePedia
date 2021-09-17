package GamePedia.servlet;

import GamePedia.dal.GamesDao;
import GamePedia.dal.GivenReviewsDao;
import GamePedia.dal.UserReviewsDao;
<<<<<<< HEAD

=======
import GamePedia.model.Games;
import GamePedia.model.GivenReviews;
>>>>>>> c5903f92363350bb17ef12e62322828165ea7725
import GamePedia.model.UserReviews;
import GamePedia.dal.UsersDao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/userreviewupdate")
public class UserReviewUpdate extends HttpServlet {
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
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);
        
        String username = req.getParameter("username");
        int userId;
		try {
			userId = usersDao.getUserIdFromUsername(username);
			if (userId == 0) {
	            messages.put("success", "Please enter a valid username.");
	        }
		} catch (SQLException e) {
			e.printStackTrace();
            throw new IOException(e);
		}
  
		String gameName = req.getParameter("gamename");
        if (gameName == null || gameName.trim().isEmpty()) {
        	messages.put("success", "Please enter a valid game name.");
        } else {
            try {
            	Games game = gamesDao.getGamesByName(gameName).get(0);
        		if(game == null) {
        			messages.put("success", "Game does not exist.");
        		}
                req.setAttribute("gamename", gameName);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
        }
        req.getRequestDispatcher("/UserReviewUpdate.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        // UserReviews userReview;
        String username = req.getParameter("username");
        String gameName = req.getParameter("gamename");
        int userId;
		try {
			userId = usersDao.getUserIdFromUsername(username);
			System.out.println("==== userId" + userId + "\n");
		} catch (SQLException e) {
            e.printStackTrace();
            throw new IOException(e);
		}
        
		int gameId;
        try {
			gameId = gamesDao.getGameIdByName(gameName);
			System.out.println("==== gameId" + gameId + "\n");
		} catch (SQLException e) {
            e.printStackTrace();
            throw new IOException(e);
		}
		
		GivenReviews givenReview;
		try {
			givenReview = givenReviewsDao.getReviewsByGameId(gameId);
			if (givenReview == null) {
	            messages.put("success", "Review does not exist.");
	        }
		} catch (SQLException e) {
            e.printStackTrace();
            throw new IOException(e);
		}
		
        UserReviews userReview;        
        try {
        	System.out.println("==== given rev id" + givenReview.getGivenReviewId() + "\n\n");
            userReview = userReviewsDao.getUserReviewByUserAndGivenReviewId(userId, givenReview.getGivenReviewId());
            if (userReview == null) {
            	System.out.println("==== userReview null\n");
                messages.put("success", "Review does not exist. No update to perform.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException(e);
        }

        // Updates
        req.setAttribute("userReview", userReview);
        messages.put("success", "Successfully updated " + gameName + " review.");
        req.getRequestDispatcher("/UserReviewUpdate.jsp").forward(req, resp);
    }
}
