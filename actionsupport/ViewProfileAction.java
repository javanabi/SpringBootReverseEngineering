package com.collegemagazine.action;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.collegemagazine.delegate.ProfileMgrDelegate;
import com.collegemagazine.exception.ConnectionException;
import com.collegemagazine.formbeans.profileformbean;
import com.collegemagazine.util.UtilConstants;

/*
 * The ViewProfileAction Class represents the view the user own profile when
 * he/she enter into the system with their user name and password 
 */
public class ViewProfileAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	@SuppressWarnings({ "unused", "deprecation" })
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = "";
		HttpSession session = request.getSession();
		String requesttype = request.getParameter("requesttype");
		String user = request.getParameter("user");
		Vector<profileformbean> pfv = null;
		try {
			path = request.getRealPath("/images");
			pfv = new ProfileMgrDelegate().viewprofile(path, user);
			if (!pfv.isEmpty()) {
				request
						.setAttribute("status",
								UtilConstants._USER_PROFILE_INFO);
				request.setAttribute("userinfo", pfv);
				request.setAttribute("user", user);
				if (requesttype.equals("view"))
					path = UtilConstants._VIEW_USER_PROFILE;
				else
					path = UtilConstants._UPDATE_USER_PROFILE;
			} else {
				request.setAttribute("status", UtilConstants._NO_USER_PROFILE);
				request.setAttribute("user", user);
				path = UtilConstants._VIEW_USER_PROFILE;
			}
		} 
		
		catch (ConnectionException e) {
			request.setAttribute("status",e.getMessage());
			path=UtilConstants._VIEW_USER_PROFILE;
		}
		catch (Exception e) {
			request.setAttribute("status", UtilConstants._NO_USER_PROFILE);
			path = UtilConstants._VIEW_USER_PROFILE;
		}finally {
		RequestDispatcher rd = request.getRequestDispatcher(path);
		rd.forward(request, response);
	}
}}
