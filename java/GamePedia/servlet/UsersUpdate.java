package GamePedia.servlet;

import GamePedia.dal.UsersDao;
import GamePedia.model.Users;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/userupdate")
public class UsersUpdate extends HttpServlet {
	protected UsersDao usersDao;
	
	@Override
	public void init() throws ServletException {
		usersDao = UsersDao.getInstance();
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        String userName = req.getParameter("username");
        if (userName == null || userName.trim().isEmpty()) {
            messages.put("success", "Please enter a valid UserName.");
        } else {
        	try {
        		Users users = usersDao.getUserFromUserName(userName);
        		if(users == null) {
        			messages.put("success", "UserName does not exist.");
        		}
        		req.setAttribute("user", users);
        	} catch (SQLException e) {
				e.printStackTrace();
				throw new IOException(e);
	        }
        }
        req.getRequestDispatcher("/UserUpdate.jsp").forward(req, resp);
	}
	
	@Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        
        Users user;
        String userName = req.getParameter("username");
        try {
        	user = usersDao.getUserFromUserName(userName);
        	if(user == null) {
    			messages.put("success", "UserName does not exist. No update to perform.");
        	}
        } catch (SQLException e) {
        	e.printStackTrace();
			throw new IOException(e);
        }
        
        String newEmail = req.getParameter("email");
        if (newEmail != null && !newEmail.equals("")) {
        	try {
        		user = usersDao.updateEmail(user, newEmail);
            	req.setAttribute("user", user);
        	} catch (SQLException e) {
				e.printStackTrace();
				throw new IOException(e);
	        }	
        }
        
        String newFirstName = req.getParameter("firstname");
        if (newFirstName != null && !newFirstName.equals("")) {
        	try {
        		user = usersDao.updateFirstName(user, newFirstName);
            	req.setAttribute("user", user);
        	} catch (SQLException e) {
				e.printStackTrace();
				throw new IOException(e);
	        }
        }
        
        
        String newPassword = req.getParameter("password");
        if (newPassword != null && !newPassword.equals("")) {
        	try {
        		user = usersDao.updatePassword(user, newPassword);
            	
        	} catch (SQLException e) {
				e.printStackTrace();
				throw new IOException(e);
	        }
        }     
        req.setAttribute("user", user);
        messages.put("success", "Successfully updated " + userName);
        req.getRequestDispatcher("/UserUpdate.jsp").forward(req, resp);
	}
}
