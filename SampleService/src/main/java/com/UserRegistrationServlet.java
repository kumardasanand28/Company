package main.java.com;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.mysql.jdbc.StringUtils;

import main.java.com.bean.User;
import main.java.com.constants.UserConstants;
import main.java.com.db.UserService;

@WebServlet("/UserRegistrationServlet")
public class UserRegistrationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	UserService userService = new UserService();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("Called");
		ServletContext ctx=getServletContext();
		
		System.out.println("Servlet Context TEST!!!!: "+ctx.getAttribute("name"));
		
		response.setHeader("Access-Control-Allow-Origin", "*");
	    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	    response.setHeader("Access-Control-Max-Age", "3600");
	    response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
	    
		String action = request.getParameter("action");
		String name = request.getParameter("name");

		if (action != null && action.equalsIgnoreCase("register")) {

			User user = new User();
			response.setContentType("text/html;charset=UTF-8");
			boolean isValid = validateUserDetails(request);
			if (isValid) {
				populateUserDetails(request, user);

				try {
					userService.insertIntoDataBase(user);
					response.getWriter().write("SuccessFully Registered");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					response.getWriter().write("Registration Failed");
				}
			}else{
				response.getWriter().write("Validation Failure");
			}
		}else if(action != null && action.equalsIgnoreCase("createDatabase")){
			try {
				userService.createUserTable();
				response.getWriter().write("SuccessFully Created");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.getWriter().write("Creation Failed");
			}
		}else if(action != null && action.equalsIgnoreCase("verifyConnection")){
			try {
				Connection con = userService.getMySqlConnection();
				if(con != null){
					response.getWriter().write("SuccessFully Connected");
				}else{
					response.getWriter().write("Connection Failed");
				}
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.getWriter().write(e.getMessage());
			}
			
		}
		else {
			User user = new User();
			response.setContentType("text/html;charset=UTF-8");
			boolean isValid = validateUserDetails(request);
			if (isValid) {
				populateUserDetails(request, user);
				user.setId(Integer.parseInt(name));
				try {
					userService.updateUser(user);
					response.getWriter().write("SuccessFully Updated");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					response.getWriter().write("Updation Failed");
				}
			}else{
				response.getWriter().write("Validation Failure");
			}
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader("Access-Control-Allow-Origin", "*");
	    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	    response.setHeader("Access-Control-Max-Age", "3600");
	    response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
	    
		String param = request.getParameter("getAllValues");

		if (param != null && param.equalsIgnoreCase("true")) {
			try {
				List<User> userList = userService.fetchUsers();

				String json = new Gson().toJson(userList);

				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(json);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			String name = request.getParameter("name");
			if (!StringUtils.isNullOrEmpty(name)) {
				int id = Integer.parseInt(request.getParameter("name"));
				try {
					User user = userService.fetchUser(id);
					String json = new Gson().toJson(user);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(json);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	private boolean validateUserDetails(HttpServletRequest request) {

		String fullName = request.getParameter(UserConstants.FULL_NAME);
		if (StringUtils.isNullOrEmpty(fullName)) {
			return false;
		}

		String age = request.getParameter(UserConstants.AGE);
		if (StringUtils.isNullOrEmpty(age) || Pattern.matches("[a-zA-Z]+", age)) {
			return false;
		}

		String qualification = request.getParameter(UserConstants.QUALIFICATION);
		if (StringUtils.isNullOrEmpty(qualification)) {
			return false;
		}

		String yearPassed = request.getParameter(UserConstants.YEAR_PASSED);
		if (StringUtils.isNullOrEmpty(yearPassed) || Pattern.matches("[a-zA-Z]+", yearPassed)) {
			return false;
		}

		String gender = request.getParameter(UserConstants.GENDER);
		if (StringUtils.isNullOrEmpty(gender)) {
			return false;
		}

		String[] interests = request.getParameterValues(UserConstants.INTERESTS);
		if (interests == null) {
			return false;
		}
		if (interests.length == 0) {
			return false;
		}

		return true;

	}

	private void populateUserDetails(HttpServletRequest request, User user) {
		user.setFullName(request.getParameter(UserConstants.FULL_NAME));
		user.setAge(Integer.parseInt(request.getParameter(UserConstants.AGE)));
		user.setQualification(request.getParameter(UserConstants.QUALIFICATION));
		user.setYearPassed(Integer.parseInt(request.getParameter(UserConstants.YEAR_PASSED)));
		user.setGender(request.getParameter(UserConstants.GENDER));
		user.setInterests(request.getParameterValues(UserConstants.INTERESTS));
	}

}
