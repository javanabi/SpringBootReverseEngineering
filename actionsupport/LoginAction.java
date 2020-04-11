package com.collegemagazine.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.collegemagazine.delegate.SecurityMgrDelegate;
import com.collegemagazine.exception.ConnectionException;
import com.collegemagazine.formbeans.profileformbean;
import com.collegemagazine.util.UtilConstants;

/**
 * LoginActionClass implements the Login details 
 * using UserName and Password
 */
public class LoginAction extends HttpServlet {
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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String target = "";
		try {
			HttpSession session = request.getSession();
			profileformbean rb = new profileformbean();
			String username = request.getParameter(UtilConstants._USERNAME);
			rb.setLoginid(username);
			String pass = request.getParameter(UtilConstants._PASSWORD);
			rb.setPassword(pass);
			String role = new SecurityMgrDelegate().loginCheck(rb);
			if (role.equals(UtilConstants._ADMIN)) {
				request.setAttribute("status", "Welcome " + username);
				session.setAttribute(UtilConstants._LOGIN_USER, username);
				session.setAttribute(UtilConstants._LOGIN_ROLE, role);
				target = UtilConstants._ADMIN_HOME;
			} else if (role.equals(UtilConstants._STUDENT)) {
				request.setAttribute("status", "Welcome " + username);
				session.setAttribute(UtilConstants._LOGIN_USER, username);
				session.setAttribute(UtilConstants._LOGIN_ROLE, role);
				target = UtilConstants._STUDENT_PAGE;
			} else if (role.equals(UtilConstants._FACULTY)) {
				request.setAttribute("status", "Welcome " + username);
				session.setAttribute(UtilConstants._LOGIN_USER, username);
				session.setAttribute(UtilConstants._LOGIN_ROLE, role);
				target = UtilConstants._FACULTY_PAGE;
			} else if (role.equals(UtilConstants._MODERATOR)) {
				request.setAttribute("status", "Welcome " + username);
				session.setAttribute(UtilConstants._LOGIN_USER, username);
				session.setAttribute(UtilConstants._LOGIN_ROLE, role);
				target = UtilConstants._MODERATOR_HOME;
			} else {
				request.setAttribute("status", UtilConstants._INVALID_USER);
				target = UtilConstants._LOGIN_HOME;
			}
		} catch (ConnectionException e) {
			request.setAttribute("status",e.getMessage());
			target=UtilConstants._LOGIN_HOME;
				}
		catch (Exception e) {
		}finally {
		RequestDispatcher rd = request.getRequestDispatcher(target);
		rd.forward(request, response);
	}}
}
