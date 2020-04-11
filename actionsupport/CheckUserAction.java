package com.collegemagazine.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.collegemagazine.daoImpl.SecurityDAOImpl;

/*
 * CheckUserAction Class implements the check the user authentication
 * depending upon the UserName and password
 */
public class CheckUserAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("userName");
		String target = "./jsps/registration.jsp?status1=Invalid username and password";
		try {
			boolean flag = new SecurityDAOImpl().checkAvailability(username);
			if (flag == false) {
				target = "./jsps/registration.jsp?status1=Available";
			} else
				target = "./jsps/registration.jsp?status1=Alreadyexist&userName="
						+ username;
		} catch (Exception e) {
			target = "./jsps/registration.jsp?status1=Available&userName="
					+ username;
		}
		RequestDispatcher rd = request.getRequestDispatcher(target);
		rd.forward(request, response);
	}
}
