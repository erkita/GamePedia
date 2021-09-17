package GamePedia.servlet;

import GamePedia.dal.GamesDao;
import GamePedia.model.Games;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/gameupdate")
public class GameUpdate extends HttpServlet {
	protected GamesDao gamesDao;
	
	@Override
	public void init() throws ServletException {
		gamesDao = GamesDao.getInstance();
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        String gameName = req.getParameter("gamename");
        if (gameName == null || gameName.trim().isEmpty()) {
            messages.put("success", "Please enter a valid name of game.");
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
        req.getRequestDispatcher("/GameUpdate.jsp").forward(req, resp);
	}
	
	@Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        
        Games game;
        String gameName = req.getParameter("gamename");
        try {
        	game = gamesDao.getGamesByName(gameName).get(0);
        	if(game == null) {
    			messages.put("success", "Game does not exist. No update to perform.");
        	}
        } catch (SQLException e) {
        	e.printStackTrace();
			throw new IOException(e);
        }
        
        String newDescription = req.getParameter("description");
        if (newDescription != null && !newDescription.equals("")) {
        	try {
        		game = gamesDao.updateDescription(game, newDescription);
            	req.setAttribute("game", game);
        	} catch (SQLException e) {
				e.printStackTrace();
				throw new IOException(e);
	        }	
        } 

        String stringReleaseDate = req.getParameter("releasedate");
        Date newReleaseDate = Date.valueOf(stringReleaseDate);
        if (stringReleaseDate != null && !stringReleaseDate.equals(null)) {
        	try {
        		game = gamesDao.updateReleaseDate(game, newReleaseDate);
        		req.setAttribute("game", game);
        	} catch (SQLException e) {
				e.printStackTrace();
				throw new IOException(e);
        	}
        }
        
        
        BigDecimal newPrice = new BigDecimal(req.getParameter("price"));
        if (newPrice != null && !newPrice.equals(null)) {
        	try {
        		game = gamesDao.updatePrice(game, newPrice);
        		req.setAttribute("game", game);
        	} catch (SQLException e) {
				e.printStackTrace();
				throw new IOException(e);
	        }
        }
        
        String newPicLink = req.getParameter("picLink");
        if (newPicLink != null && !newPicLink.equals("")) {
        	try {
        		game = gamesDao.updatePicLink(game, newPicLink);
            	req.setAttribute("game", game);
        	} catch (SQLException e) {
				e.printStackTrace();
				throw new IOException(e);
	        }	
        } 
        
        req.setAttribute("game", game);
        messages.put("success", "Successfully updated " + gameName);
        req.getRequestDispatcher("/GameUpdate.jsp").forward(req, resp);
	}
}
