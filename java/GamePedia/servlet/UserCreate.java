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


@WebServlet("/usercreate")
public class UserCreate extends HttpServlet {
	
	protected UsersDao usersDao;
	
	@Override
	public void init() throws ServletException {
		usersDao = UsersDao.getInstance();
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Map for storing messages.
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);
        //Just render the JSP.   
        req.getRequestDispatcher("/UserCreate.jsp").forward(req, resp);
	}
	
	@Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
        // Map for storing messages.
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        int userId;
    	try{
    		userId = usersDao.getNextUserId();
        }catch(SQLException e){
			e.printStackTrace();
			throw new IOException(e);
        }
        
        String userName = req.getParameter("username");
        String email = req.getParameter("email");
        String firstname = req.getParameter("firstname");
        String password = req.getParameter("password");
       
    	try{
    		Users user = new Users(userId, userName, email, firstname, password);
    		user = UsersDao.create(user);
    		messages.put("success", "Successfully created " + userName);
        }catch(SQLException e){
			e.printStackTrace();
			throw new IOException(e);
        }
        
        req.getRequestDispatcher("/UserCreate.jsp").forward(req, resp);
    }
}
