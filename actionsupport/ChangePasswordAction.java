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

/**
 * The ChangePasswordAction Class implements the changing password 
 * providing security for the particular user
 */
public class ChangePasswordAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

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
		HttpSession session = request.getSession();
		if ((String) session.getAttribute("user") == null)
			response
					.sendRedirect("./jsps/loginform.jsp?status=Session Expired");
		//String page = "";
		boolean flag = false;
		String target = "./jsps/changepassword.jsp?status=Password Changed Failed";
		profileformbean rb = new profileformbean();
		rb.setPassword(request.getParameter("oldpassword"));
		rb.setLoginid(request.getParameter("username"));
		rb.setNewpassword(request.getParameter("newpassword"));
		try {
			flag = new SecurityMgrDelegate().changePassword(rb);
		
		if (flag) {
			target = "./jsps/changepassword.jsp";
			request.setAttribute("status", "Password Changed Successfully");
		} else {
			target = "./jsps/changepassword.jsp";
			request.setAttribute("status", "Password Changing Failed");
		}} catch (ConnectionException e) {
			
			request.setAttribute("status",e.getMessage());
			target = "./jsps/changepassword.jsp";
			
			
		}
		finally {
		RequestDispatcher rd = request.getRequestDispatcher(target);
		rd.forward(request, response);
	}
	}
}
