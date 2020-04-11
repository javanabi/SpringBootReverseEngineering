package com.support.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jdbc.dao.AbstractDataAccessObject;
import com.jdbc.main.CapitalCase;
import com.jdbc.main.ListOfDateString;
import com.jdbc.main.SqlDatatypeTOJavaWrapperClass;
import com.util.impl.InputNamesFileRead;

public class SecurityImpl extends AbstractDataAccessObject {

	String pack, loginName="";;
	PreparedStatement pstmt;
	ResultSet resultSet, rs, rsf, tableTypes, contraintsRecords;
	String schemaName = getProperties().getProperty("duser");

	public SecurityImpl(String pack) {
		this.pack = pack;
	}

	public void createSecurityImplClasses(Connection con, String title) throws SQLException {
		try {

			String packageNameString = ".\\com\\";
			File packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + pack;
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + "\\action";
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			PrintWriter writer = new PrintWriter(packageNameString + "\\LogoutAction.java");

			String packageImport = "package com." + pack + ".action;\n";
			packageImport += "import java.io.IOException; \n";
			packageImport += "import javax.servlet.RequestDispatcher; \n";
			packageImport += "import javax.servlet.ServletException; \n";
			packageImport += "import javax.servlet.http.HttpServlet; \n";
			packageImport += "import javax.servlet.http.HttpServletRequest; \n";
			packageImport += "import javax.servlet.http.HttpServletResponse; \n";
			packageImport += "import javax.servlet.http.HttpSession; \n";
			packageImport += "import com." + pack + ".util.UtilConstants; \n";
			packageImport += "import javax.servlet.annotation.WebServlet; \n";

			writer.println("/**");
			writer.println(" * LogoutAction servlet Class for to LogoutAction from his account. This class");
			writer.println(" * Helps to get logout from his account.");
			writer.println(" * ");
			writer.println(" */");

			writer.println(packageImport);
			writer.println("@WebServlet(\"/LogoutAction\")");
			writer.println("public class LogoutAction extends HttpServlet {\n");
			writer.println("	private static final long serialVersionUID = 1L;\n");
			writer.println("");
			writer.println("public void doGet(HttpServletRequest request, HttpServletResponse response)");
			writer.println("		throws ServletException, IOException {");
			writer.println("	doPost(request, response);");
			writer.println("}");
			writer.println("public void doPost(HttpServletRequest request, HttpServletResponse response)");
			writer.println("		throws ServletException, IOException {");
			writer.println("			HttpSession session = request.getSession();");
			writer.println("			session.setAttribute(\"user\", null);");
			writer.println("			session.setAttribute(\"role\", null);");
			writer.println("			session.setAttribute(\"logintype\", null);");
			writer.println("			session.setAttribute(\"usertype\", null);");
			writer.println("			session.setAttribute(\"userid\", null);");
			writer.println("			session.setAttribute(\"status\", \"Logged out successfully\");");
			writer.println("			session.invalidate();");
			writer.println("			RequestDispatcher rd = request");
			writer.println("					.getRequestDispatcher(UtilConstants._STATUS_PATH);");
			writer.println("			rd.forward(request, response);");
			writer.println("		}");
			writer.println("");
			writer.println("	}");
			writer.close();

			pstmt = con.prepareStatement(
					"select distinct table_name from all_tab_columns where column_name in ('LOGINID', 'PASSWORD', 'LOGINTYPE', 'USERNAME', 'USERTYPE','USERNAME', 'LOGINNAME') and owner = '"
							+ schemaName.toUpperCase() + "'");
			resultSet = pstmt.executeQuery();

			if (resultSet.next()) {

				String className = resultSet.getString(1);

				packageNameString = ".\\com\\";
				packageDir = new File(packageNameString);
				packageDir.mkdir();

				packageNameString = packageNameString + pack;
				packageDir = new File(packageNameString);
				packageDir.mkdir();

				packageNameString = packageNameString + "\\action";
				packageDir = new File(packageNameString);
				packageDir.mkdir();

				writer = new PrintWriter(packageNameString + "\\LogInAction.java");

				packageImport = "package com." + pack + ".action;\n";
				packageImport += "import java.io.IOException; \n";
				packageImport += "import java.util.Map; \n";
				packageImport += "\n";

				packageImport += "import javax.servlet.RequestDispatcher; \n";
				packageImport += "import javax.servlet.ServletException; \n";
				packageImport += "import javax.servlet.http.HttpServlet; \n";
				packageImport += "import javax.servlet.http.HttpServletRequest; \n";
				packageImport += "import javax.servlet.http.HttpServletResponse; \n";
				packageImport += "import javax.servlet.http.HttpSession; \n";
				packageImport += "import javax.servlet.annotation.WebServlet; \n";
				packageImport += "import com." + pack + ".bean." + CapitalCase.toBeanClass(className) + "; \n";
				packageImport += "import com." + pack + ".serviceimpl.SecurityServiceImpl; \n";
				packageImport += "import com." + pack + ".exception.ConnectionException; \n";
				packageImport += "import com." + pack + ".util.UtilConstants; \n";
				packageImport += "import com.sun.org.apache.commons.beanutils.BeanUtils; \n";

				Set<String> setLoginNames = new HashSet<String>();
				setLoginNames.add("LOGINID".toLowerCase());
				setLoginNames.add("LOGINTYPE".toLowerCase());
				setLoginNames.add("USERNAME".toLowerCase());
				setLoginNames.add("USERTYPE".toLowerCase());
				setLoginNames.add("LOGINNAME".toLowerCase());

				writer.println(packageImport);
				writer.println("@WebServlet(\"/LogInAction\")");
				writer.println("public class LogInAction extends HttpServlet {\n");
				writer.println("	private static final long serialVersionUID = 1L;\n");
				writer.println("	public void doGet(HttpServletRequest request, HttpServletResponse response)");
				writer.println("			throws ServletException, IOException {");
				writer.println("		doPost(request, response);");
				writer.println("	}");
				writer.println("	public void doPost(HttpServletRequest request, HttpServletResponse response)");
				writer.println("			throws ServletException, IOException {");
				writer.println("		HttpSession session = request.getSession();");
				writer.println("		String path = \"\";");
				writer.println("		" + CapitalCase.toBeanClass(className) + " "
						+ CapitalCase.toBeanClass(className).toLowerCase() + " = new "
						+ CapitalCase.toBeanClass(className) + "();");
				writer.println("		Map map = request.getParameterMap();");
				writer.println("		try {");
				writer.println(
						"		BeanUtils.populate(" + CapitalCase.toBeanClass(className).toLowerCase() + ", map);");
				writer.println("		RequestDispatcher rd = null;");
				writer.println(
						"		" + CapitalCase.toBeanClass(className) + " " + className.toLowerCase() + " = null;");
				writer.println("			" + className.toLowerCase() + " = new SecurityServiceImpl().loginCheck("
						+ CapitalCase.toBeanClass(className).toLowerCase() + ");");
				writer.println("			if (" + className.toLowerCase() + " != null) {");

				pstmt = con.prepareStatement("select * from " + className);
				rs = pstmt.executeQuery();
				ResultSetMetaData columnMetaData = rs.getMetaData();
				int colCount = columnMetaData.getColumnCount();
				for (int i = 1; i <= colCount; i++) {
					String colName = columnMetaData.getColumnName(i);
					if (setLoginNames.contains(colName.toLowerCase())) {
						writer.println("				session.setAttribute(\"" + colName.toLowerCase() + "\", "
								+ className.toLowerCase() + ".get" + CapitalCase.toCapitalCase(colName.toLowerCase())
								+ "());");
					}
				}

				writer.println("				path = UtilConstants._USER_HOME;");
				writer.println("			} else {");
				writer.println("				request.setAttribute(UtilConstants._STATUS,");
				writer.println("						UtilConstants._INVALID_USER);");
				writer.println("				path = UtilConstants._USER_HOME;");
				writer.println("			}");
				writer.println("		}");
				writer.println("		catch (ConnectionException e) {");
				writer.println("			request.setAttribute(UtilConstants._STATUS,e.getMessage());");
				writer.println("			path=UtilConstants._STATUS_PATH;");
				writer.println("		}");
				writer.println("			catch (Exception e) {");
				writer.println("			e.printStackTrace();");
				writer.println("			request.setAttribute(UtilConstants._STATUS,e.getMessage());");
				writer.println("			path = UtilConstants._STATUS_PATH;");
				writer.println("		}finally{");
				writer.println("				RequestDispatcher rd = request.getRequestDispatcher(path);");
				writer.println("			rd.forward(request, response);");
				writer.println("		}\n" + "	}");
				writer.println("}");
				writer.close();

				try {
					DatabaseMetaData databaseMetaData = con.getMetaData();
					String cataLog = con.getCatalog();
					pstmt = con.prepareStatement("select * from " + className);
					resultSet = pstmt.executeQuery();
					packageNameString = ".\\com\\";
					packageDir = new File(packageNameString);
					packageDir.mkdir();

					packageNameString = packageNameString + pack;
					packageDir = new File(packageNameString);
					packageDir.mkdir();

					packageNameString = packageNameString + "\\serviceimpl";
					packageDir = new File(packageNameString);
					packageDir.mkdir();

					writer = new PrintWriter(packageNameString + "\\SecurityServiceImpl.java");

					packageImport = "package com." + pack + ".serviceimpl;\n";
					packageImport += "import java.util.List; \n";
					packageImport += "import java.util.Date; \n";
					packageImport += "import com." + pack + ".daoimpl.SecurityDaoImpl; \n";
					packageImport += "import com." + pack + ".bean." + CapitalCase.toCapitalCase(className) + "TO; \n";
					packageImport += "import com." + pack + ".exception.ConnectionException; \n";
					packageImport += "import com." + pack + ".exception.LoginException; \n";
					packageImport += "import com." + pack + ".exception.DataNotFoundException; \n";
					packageImport += "import com." + pack + ".bean." + CapitalCase.toBeanClass(className) + "; \n";

					writer.println(packageImport);
					writer.println("public class SecurityServiceImpl" + " {");
					writer.println("\n");
					writer.println("	private static final long serialVersionUID = 1L;\n");
					writer.println("	 SecurityDaoImpl securityDaoImpl = new SecurityDaoImpl();");
					writer.println("\n");
					writer.println("\n");

					writer.println("	public " + CapitalCase.toBeanClass(className) + " loginCheck("
							+ CapitalCase.toBeanClass(className) + " " + className.toLowerCase()
							+ ") throws ConnectionException {");
					writer.println("return " + className.toLowerCase() + " = securityDaoImpl.loginCheck("
							+ className.toLowerCase() + ");");
					writer.println("	}");
					writer.println("			}");
					writer.close();
					pstmt.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				try {
					
					Set<String> setLoginids = new HashSet<String>();
					setLoginids.add("LOGINID".toLowerCase());
					setLoginids.add("USERNAME".toLowerCase());
					setLoginids.add("LOGINNAME".toLowerCase());
					setLoginids.add("USERNAME".toLowerCase());

					int columnCount = columnMetaData.getColumnCount();
					for (int i = 1; i <= columnCount; i++) {
						if(setLoginids.contains(columnMetaData.getColumnName(i).toLowerCase())) {
							loginName = columnMetaData.getColumnName(i);
							break;
						}
					}
					
					
					 packageNameString = ".\\com\\" + pack + "\\daoimpl";
					 packageDir = new File(packageNameString);
					packageDir.mkdir();

					 writer = new PrintWriter(
							packageNameString + "\\SecurityDaoImpl.java");

					 packageImport = "package com." + pack + ".daoimpl;\n";
					packageImport += "import java.sql.*; \n";
					packageImport += "import java.util.List; \n";
					packageImport += "import java.util.ArrayList; \n";
					packageImport += "import java.util.Date; \n\n";
					packageImport += "import com." + pack + ".util.DateWrapper; \n";
					packageImport += "import com." + pack + ".dao.AbstractDataAccessObject;\n";
					packageImport += "import com." + pack + ".bean." + CapitalCase.toCapitalCase(className) + "TO;\n";
					writer.println(packageImport);
					writer.println("public class SecurityDaoImpl {");
					writer.println("\n");
					writer.println("	Connection con = null;");
					writer.println("	PreparedStatement preparedStatement = null;");
					writer.println("	Statement statement = null;");
					writer.println("	ResultSet resultSet = null;");
					writer.println("	boolean flag = false;\n");
					
					writer.println("	public SecurityDaoImpl(Connection con) {");
					writer.println("		this.con = (con != null)? con : new AbstractDataAccessObject().getConnection();");
					writer.println("	}");
					
					writer.println("	public SecurityDaoImpl() {");
					writer.println("		this.con = new AbstractDataAccessObject().getConnection();");
					writer.println("	}");
					
					writer.println("	private static final long serialVersionUID = 1L;\n");
					writer.println("\n");
					writer.append("\n");
					writer.append("			public " + CapitalCase.toCapitalCase(className) + "TO loginCheck("
							+ CapitalCase.toBeanClass(className) + " "
							+ className.toLowerCase() + "to) {\n");
					writer.append("				" + CapitalCase.toCapitalCase(className) + "TO " + className.toLowerCase()
							+ " = new " + CapitalCase.toCapitalCase(className) + "TO();\n");
					writer.append("				try {\n");
					writer.append("					preparedStatement = con.prepareStatement(\"select * from " + className
							+ " where "+loginName+" = '\"+"+ (className+"to").toLowerCase() +".get"+ CapitalCase.toCapitalCase(loginName)+"()+\"' and password = '\"+"
							+ (className+"to").toLowerCase() + ".getPassword()+\"'\");\n");
					writer.append("					resultSet = preparedStatement.executeQuery();\n");
					writer.append("					if(resultSet.next()) {\n");
					writer.append("						" + className.toLowerCase() + " = new "
							+ CapitalCase.toCapitalCase(className) + "TO();\n");
					
					for (int i = 1; i <= columnCount; i++) {
						writer.println("							" + className.toLowerCase() + ".set"
								+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)) + "(" + "resultSet.get"
								+ SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i)) + "(" + i
								+ "));");
					}
					writer.append("					}\n");
					writer.append("				} catch (SQLException e) {\n");
					writer.append("					e.printStackTrace();\n");
					writer.append("					System.out.println(\"Exception raised\" + e);\n");
					writer.append("				} finally {\n");
					writer.append("					/*try {\n");
					writer.append("						if (con != null)\n");
					writer.append("							con.close();\n");
					writer.append("					} catch (SQLException e) {\n");
					writer.append("						System.out.println(\"Exception raised\" + e);\n");
					writer.append("					}*/\n");
					writer.append("				}\n");
					writer.append("				return " + className.toLowerCase() + ";\n");
					writer.append("			}\n");		
					writer.append("	}\n");
					writer.append("\n");
					writer.close();
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// do something
				}

			}
			

				  writer = new PrintWriter(".\\jsp" + "\\loginform.jsp");

				writer.println("<%@ page language=\"java\" import=\"java.util.*\" pageEncoding=\"ISO-8859-1\"%>");
				writer.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
				writer.println("<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>");
				writer.println("<html>");
				writer.println("	<head>");

				writer.println("		<title>" + title + "</title>");
				writer.println("		<link rel=\"icon\" href=\"./images/"+ title +"_favicon.png\" />");
				writer.println("		<meta http-equiv=\"pragma\" content=\"no-cache\">");
				writer.println("		<meta http-equiv=\"cache-control\" content=\"no-cache\">");
				writer.println("		<meta http-equiv=\"expires\" content=\"0\">");
				writer.println("		<meta http-equiv=\"keywords\" content=\"keyword1,keyword2,keyword3\">");
				writer.println("		<meta http-equiv=\"description\" content=\"This is my page\">");
				writer.println(" 	");
				writer.println("");
				writer.println("		<script language=\"JavaScript\" src=\"<%=request.getContextPath() + \"/js/formvalidator.js\"%>\" type=\"text/javascript\"></script>");
				writer.println("		<script language=\"JavaScript\" src=\"<%=request.getContextPath() + \"/js/date_picker.js\"%>\" type=\"text/javascript\"></script>");
				writer.println("<link rel = \"stylesheet\"  type=\"text/css\"  href=\"./css/"+title+".css\" />");
				writer.println("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">");
				writer.println("	</head>");
				writer.println("");
				writer.println("	<body>");
				writer.println("");
				writer.println("");
				writer.println("					<jsp:include page=\"header.jsp\"></jsp:include>");
				writer.println("<form action=\"./LogInAction\" method=\"post\" name=\"loginform\"");
				writer.println("onsubmit='return validate()'>");
				writer.println("<center>");
				writer.println("	<h2>");
				writer.println("			<font color='#sharew'>Login Form</font>");
				writer.println("	</h2>");
				writer.println("	</center>");
				writer.println("	<pre>");
				writer.println("<table align='center'>");
						writer.println("        	<tr>");
						writer.println("                <td align='right'>");
						writer.println(
								"        		" + CapitalCase.toCapitalCase(loginName) + " :");
						writer.println("                </td>");
						writer.println("                <td>");
						writer.println("                        <input type='text' name=\'"
								+ loginName.toLowerCase()
								+ "' value=''></td><tr>");
						writer.println("        	<tr>");
						writer.println("                <td align='right'>");
						writer.println(
								"        		Password :");
						writer.println("                </td>");
						writer.println("                <td align='right'>");
						writer.println("                            <input type='password' value='' name='password'>");
							writer.println("                </td>");
							writer.println("                </tr>");
				writer.println("			        <tr align='center'>");
				writer.println("                          <td>");
				writer.println("                             <input type='submit' value='Submit' name='Sign In'>");
				writer.println("                             <input type='reset' value='Clear' name='clear'>");
				writer.println("                         </td>");
				writer.println("                    </tr>");
				writer.println("                </table>");
				writer.println("</pre>");
				writer.println("</form>");

				writer.println("<script language='JavaScript' type='text/javascript'>");
				writer.println("var frmvalidator = new Validator(\"loginform\");");
				writer.println("			frmvalidator.addValidation('"
						+ loginName.toLowerCase()+ "', 'req','Please enter "
						+ CapitalCase.toCapitalCase(loginName) + "');");
				writer.println("			frmvalidator.addValidation('password', 'req','Please enter password');");
				writer.println("</script>");
				writer.println("		<jsp:include page='footer.jsp'></jsp:include>");
				writer.println("	</body>");
				writer.println("</html>");
				writer.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
