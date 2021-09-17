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

@WebServlet("/finduserreviews")
public class FindUserReviews extends HttpServlet {

    protected UserReviewsDao userReviewsDao;
    protected UsersDao usersDao;

    @Override
    public void init() throws ServletException {
        userReviewsDao = UserReviewsDao.getInstance();
        usersDao = UsersDao.getInstance();
    }

    @Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Map for storing messages.
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);
		
		// Retrieve and validate UserName.
        String userName = req.getParameter("username");
        if (userName == null || userName.trim().isEmpty()) {
            messages.put("title", "Invalid username.");
        } else {
        	messages.put("title", "User Reviews for " + userName);
        }
        
        List<UserReviews> userReviews = new ArrayList<UserReviews>();
        try {
        	Users user = new Users(userName);
        	userReviews = userReviewsDao.getAllUserReviewsOfUser(user);
        } catch (SQLException e) {
			e.printStackTrace();
			throw new IOException(e);
        }
        req.setAttribute("userReviews", userReviews);
        req.getRequestDispatcher("/FindUserReviews.jsp").forward(req, resp);
    }
    
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
		
		// Map for storing messages.
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        List<Users> user = new ArrayList<Users>();
        
        String username = req.getParameter("username");
        if (username == null || username.trim().isEmpty()) {
            messages.put("success", "Please enter a valid username.");
        } else {
        	try {
        		user = usersDao.getUserByName(username);
            } catch (SQLException e) {
    			e.printStackTrace();
    			throw new IOException(e);
            }
        	messages.put("success", "Displaying results for " + username);
        }
        System.out.printf("logging games size: %s", user.size());
        req.setAttribute("username", user);
        
        req.getRequestDispatcher("/FindUserReviews.jsp").forward(req, resp);
    }
}
