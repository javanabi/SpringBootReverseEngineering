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
import com.collegemagazine.util.UtilConstants;

/*
 * LogOutAction Class implements the Abort from the System by click logout button
 */
public class LogOutAction extends HttpServlet {
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
		String path = "";
		HttpSession session = request.getSession();
		System.out.println("in logout user="
				+ session.getAttribute(UtilConstants._LOGIN_USER));
		try {
			new SecurityMgrDelegate().loginaudit((String) session
					.getAttribute(UtilConstants._LOGIN_USER));
			session.setAttribute(UtilConstants._LOGIN_USER, null);
			session.setAttribute(UtilConstants._LOGIN_ROLE, null);
			session.invalidate();
			request.setAttribute("status", UtilConstants._LOGOUT_SUCCESS);
			path = UtilConstants._LOGIN_HOME;
		} catch (ConnectionException e) {
			e.printStackTrace();
			request.setAttribute("status", e.getMessage());
			path = UtilConstants._LOGIN_HOME;
		}
		finally {
		RequestDispatcher rd = request.getRequestDispatcher(path);
		rd.forward(request, response);
	}
}}
