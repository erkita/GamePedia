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


@WebServlet("/gamedelete")
public class GameDelete extends HttpServlet {
	
	protected GamesDao gamesDao;
	
	@Override
	public void init() throws ServletException {
		gamesDao = GamesDao.getInstance();
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Map for storing messages.
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);
        // Provide a title and render the JSP.
        messages.put("title", "Delete Game");        
        req.getRequestDispatcher("/GameDelete.jsp").forward(req, resp);
	}
	
	@Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
        // Map for storing messages.
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        // Retrieve and validate name.
        String gameName = req.getParameter("gamename");
        if (gameName == null || gameName.trim().isEmpty()) {
            messages.put("title", "Invalid UserName");
            messages.put("disableSubmit", "true");
        } else {
        	// Delete the BlogUser.
        	
	        Games gameToDelete = null;
	        try {
	        	gameToDelete = gamesDao.getGamesByName(gameName).get(0);
	        } catch (NumberFormatException | SQLException e1) {
	        	e1.printStackTrace();
	        }
	        try {
	        	
	        gameToDelete = gamesDao.delete(gameToDelete);
	        	// Update the message.
		        if (gameToDelete == null) {
		            messages.put("title", "Successfully deleted " + gameName);
		            messages.put("disableSubmit", "true");
		        } else {
		        	messages.put("title", "Failed to delete " + gameName);
		        	messages.put("disableSubmit", "false");
		        }
	        } catch (SQLException e) {
				e.printStackTrace();
				throw new IOException(e);
	        }
        }
        
        req.getRequestDispatcher("/GameDelete.jsp").forward(req, resp);
    }
}
