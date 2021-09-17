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


@WebServlet("/userreviewdelete")
public class UserReviewDelete extends HttpServlet {
	
	protected UserReviewsDao userReviewsDao;
	
	@Override
	public void init() throws ServletException {
		userReviewsDao = UserReviewsDao.getInstance();
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Map for storing messages.
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);
        // Provide a title and render the JSP.
        messages.put("title", "Delete User Review");        
        req.getRequestDispatcher("/UserReviewDelete.jsp").forward(req, resp);
	}
	
	@Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
        // Map for storing messages.
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        // Retrieve and validate user review id.
        String userReviewId = req.getParameter("userreviewid");
        System.out.println("user review id " + userReviewId);
        if (userReviewId == null || userReviewId.trim().isEmpty()) {
            messages.put("title", "Invalid user review ID");
            messages.put("disableSubmit", "true");
        } else {
        	int id = Integer.parseInt(userReviewId);
	        UserReviews userReviewToDelete = null;
	        try {
	        	userReviewToDelete = userReviewsDao.getUserReviewById(id);
	        } catch (NumberFormatException | SQLException e1) {
	        	e1.printStackTrace();
	        }
	        try {
	        	
	        userReviewToDelete = userReviewsDao.delete(userReviewToDelete);
	        	// Update the message.
		        if (userReviewToDelete == null) {
		            messages.put("title", "Successfully deleted review ID: " + userReviewId);
		            messages.put("disableSubmit", "true");
		        } else {
		        	messages.put("title", "Failed to delete review ID: " + userReviewId);
		        	messages.put("disableSubmit", "false");
		        }
	        } catch (SQLException e) {
				e.printStackTrace();
				throw new IOException(e);
	        }
        }
        
        req.getRequestDispatcher("/UserReviewDelete.jsp").forward(req, resp);
    }
}
