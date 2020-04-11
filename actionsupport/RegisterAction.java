package com.collegemagazine.action;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.org.apache.commons.beanutils.BeanUtils;

import com.collegemagazine.delegate.ProfileMgrDelegate;
import com.collegemagazine.exception.ConnectionException;
import com.collegemagazine.formbeans.profileformbean;
import com.collegemagazine.util.UtilConstants;
/**
 * This RegistrationAction class implements the registration action for the users.
 * Administrator can register the user and give the designation depending on their qualification
 * Administrator can have authority for user registration
 */
public class RegisterAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = null;
		boolean flag = false;
		String path = "";
		profileformbean pf = new profileformbean();
		Map map = request.getParameterMap();
		try {
			BeanUtils.populate(pf, map);
		
			flag = new ProfileMgrDelegate().insertNewUser(pf);
			if (flag) {
				path = UtilConstants._STATUS;
				request.setAttribute("status",
						UtilConstants._REGISTERED_SUCCESS);
			} else {
				path = UtilConstants._STATUS;
				request.setAttribute("status", UtilConstants._REGISTERED_FAIL);
			}
		}
		
		catch (ConnectionException e) {
			request.setAttribute("status", e.getMessage());
			path=UtilConstants._STATUS;
		}
		catch (Exception e) {
			e.printStackTrace();
			path = UtilConstants._STATUS;
			request.setAttribute("status", UtilConstants._INVALID_ENTRIES);
		}finally {
		rd = request.getRequestDispatcher(path);
		rd.forward(request, response);
	}
	}
	public void init() throws ServletException {
		// Put your code here
	
}}
