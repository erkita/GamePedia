package GamePedia.servlet;

import GamePedia.dal.*;
import GamePedia.model.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;

import javax.servlet.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/gamecreate")
public class GameCreate extends HttpServlet {
	
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
        //Just render the JSP.   
        req.getRequestDispatcher("/GameCreate.jsp").forward(req, resp);
	}
	
	@Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
        // Map for storing messages.
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        int gameId;
    	try{
    		gameId = gamesDao.getNextGameId();
        }catch(SQLException e){
			e.printStackTrace();
			throw new IOException(e);
        }
        
        String gameName = req.getParameter("gamename");
        String description = req.getParameter("description");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String stringReleaseDate = req.getParameter("releasedate");
        BigDecimal price = new BigDecimal(req.getParameter("price"));
        String picLink = req.getParameter("picLink");
        Date releaseDate = new Date();
    	try {
    		releaseDate = dateFormat.parse(stringReleaseDate);
    	} catch (ParseException e) {
    		e.printStackTrace();
			throw new IOException(e);
    	}
    	try{
    		Games game = new Games(gameId, gameName, description, releaseDate,price, picLink);
    		game = gamesDao.create(game);
    		messages.put("success", "Successfully created " + gameName);
        }catch(SQLException e){
			e.printStackTrace();
			throw new IOException(e);
        }
        
        req.getRequestDispatcher("/GameCreate.jsp").forward(req, resp);
    }
}
