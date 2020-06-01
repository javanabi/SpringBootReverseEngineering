package com.springboot.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jdbc.dao.AbstractDataAccessObject;
import com.jdbc.main.CapitalCase;

public class UtilImpl extends AbstractDataAccessObject {

	Connection con;
	PreparedStatement pstmt;
	String pack, title;
	ResultSet resultSet, rs, rsf, tableTypes, contraintsRecords;

	String schemaName = getProperties().getProperty("duser");
	public UtilImpl(String pack, String title) {
		super();
		this.pack = pack;
		this.title = title;
	}

	public void createUtilityClass() throws SQLException {
		String packageNameString = "";
		try {
			packageNameString = ".\\com\\";
			File packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + pack;
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + "\\util";
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			PrintWriter writer = new PrintWriter(packageNameString + "\\UtilConstants.java");

			String packageImport = "package com." + pack + ".util;\n";
			writer.println(packageImport);
			writer.println("public class UtilConstants" + " {");
			writer.println("\n");
			writer.println("	public static final String _STATUS = \"status\";\n");
			con = getConnection();
			DatabaseMetaData databaseMetaData = con.getMetaData();
			ResultSet tableTypes = databaseMetaData.getTableTypes();
			while (tableTypes.next()) {
				if ("TABLE".equalsIgnoreCase(tableTypes.getString("TABLE_TYPE"))) {
					ResultSet rs = databaseMetaData.getTables(null, databaseMetaData.getUserName(), "%",
							new String[] { "TABLE" });
					while (rs.next()) {
						String className = rs.getString("TABLE_NAME");
						writer.println("	public static final String _ADD_" + className.replace("_", "").toUpperCase()
								+ " = \"Successfully added " + CapitalCase.toCapitalCase(className.replace("_", ""))
								+ " data\";");
						writer.println("	public static final String _VIEW_"
								+ className.replace("_", "").toUpperCase() + " = \"List of "
								+ CapitalCase.toCapitalCase(className.replace("_", "")) + " \";");
						writer.println("	public static final String _UPDATE_"
								+ className.replace("_", "").toUpperCase() + " = \"Successfully updated "
								+ CapitalCase.toCapitalCase(className.replace("_", "")) + " data\";");
						writer.println("	public static final String _DELETE_"
								+ className.replace("_", "").toUpperCase() + " = \"Successfully deleted "
								+ CapitalCase.toCapitalCase(className.replace("_", "")) + " data\";");
						writer.println("	public static final String _ADD_" + className.replace("_", "").toUpperCase()
								+ "_FAILED = \"Failed to add " + CapitalCase.toCapitalCase(className.replace("_", ""))
								+ " data\";");
						writer.println("	public static final String _VIEW_"
								+ className.replace("_", "").toUpperCase() + "_FAILED = \"Failed to view "
								+ CapitalCase.toCapitalCase(className.replace("_", "")) + " \";");
						writer.println("	public static final String _UPDATE_"
								+ className.replace("_", "").toUpperCase() + "_FAILED = \"Failed to update "
								+ CapitalCase.toCapitalCase(className.replace("_", "")) + " data\";");
						writer.println("	public static final String _DELETE_"
								+ className.replace("_", "").toUpperCase() + "_FAILED = \"Failed to delete "
								+ CapitalCase.toCapitalCase(className.replace("_", "")) + " data\";");
						writer.println(
								"	public static final String _VIEWALL_" + className.replace("_", "").toUpperCase()
										+ "_ACTION = \"ViewAll" + CapitalCase.toCapitalCase(className) + "Action\";");
						writer.println("");

						writer.println("	public static final String _ADD_" + className.replace("_", "").toUpperCase()
								+ "_JSP = \"Add" + CapitalCase.toCapitalCase(className) + ".jsp\";");
						writer.println(
								"	public static final String _VIEW_" + className.replace("_", "").toUpperCase()
										+ "_JSP = \"View" + CapitalCase.toCapitalCase(className) + ".jsp\";");
						writer.println(
								"	public static final String _UPDATE_" + className.replace("_", "").toUpperCase()
										+ "_JSP = \"UpdateView" + CapitalCase.toCapitalCase(className) + ".jsp\";");
						
						
						writer.println("	public static final String _START_VIEW_"+ className.replace("_", "").toUpperCase() + "_LOG = \"start of doPost method of View"+ CapitalCase.toCapitalCase(className) +" class\";");
						writer.println("	public static final String _END_VIEW_"+ className.replace("_", "").toUpperCase() + "_LOG = \"end of doPost method of View"+ CapitalCase.toCapitalCase(className) +" class\";");
						
						writer.println("	public static final String _START_VIEW_ALL_"+ className.replace("_", "").toUpperCase() + "_LOG = \"start of doPost method of ViewAll"+ CapitalCase.toCapitalCase(className) +" class\";");
						writer.println("	public static final String _END_VIEW_ALL_" + className.replace("_", "").toUpperCase() + "_LOG = \"end of doPost method of ViewAll"+ CapitalCase.toCapitalCase(className) +" class\";");
						
						
						writer.println("	public static final String _START_ADD_"
								+ className.replace("_", "").toUpperCase() + "_LOG = \"start of doPost method of Add"+ CapitalCase.toCapitalCase(className) +" class\";");
						writer.println("	public static final String _END_ADD_"
								+ className.replace("_", "").toUpperCase() + "_LOG = \"end of doPost method of Add"+ CapitalCase.toCapitalCase(className) +" class\";");
						
						writer.println("	public static final String _START_UPDATE_"+ className.replace("_", "").toUpperCase() + "_LOG = \"start of doPost method of Update"+ CapitalCase.toCapitalCase(className) +" class\";");
						writer.println("	public static final String _END_UPDATE_"
								+ className.replace("_", "").toUpperCase() + "_LOG = \"end of doPost method of Update"+ CapitalCase.toCapitalCase(className) +" class\";");
						
						writer.println("	public static final String _START_DELETE_"
								+ className.replace("_", "").toUpperCase() + "_LOG = \"start of doPost method of Delete"+ CapitalCase.toCapitalCase(className) +" class\";");
						writer.println("	public static final String _END_DELETE_"
								+ className.replace("_", "").toUpperCase() + "_LOG = \"end of doPost method of Delete"+ CapitalCase.toCapitalCase(className) +" class\";");
						
						
						writer.println("	public static final String _START_UPDATE_VIEW_" + className.replace("_", "").toUpperCase() + "_LOG = \"start of doPost method of UpdateView"+ CapitalCase.toCapitalCase(className) +" class\";");
						writer.println("	public static final String _END_UPDATE_VIEW_"
								+ className.replace("_", "").toUpperCase() + "_LOG = \"end of doPost method of UpdateView"+ CapitalCase.toCapitalCase(className) +" class\";");
						
						
						writer.println("	public static final String _DAO_START_VIEW_"+ className.replace("_", "").toUpperCase() + "_LOG = \"start of view"+ CapitalCase.toCapitalCase(className) +"() method\";");
						writer.println("	public static final String _DAO_END_VIEW_"+ className.replace("_", "").toUpperCase() + "_LOG = \"end of view"+ CapitalCase.toCapitalCase(className) +"() class\";");
						
						writer.println("	public static final String _DAO_START_VIEW_ALL_"+ className.replace("_", "").toUpperCase() + "_LOG = \"start of viewAll"+ CapitalCase.toCapitalCase(className) +"() method\";");
						writer.println("	public static final String _DAO_END_VIEW_ALL_" + className.replace("_", "").toUpperCase() + "_LOG = \"end of viewAll"+ CapitalCase.toCapitalCase(className) +"() method\";");
						
						
						writer.println("	public static final String _DAO_START_ADD_"+ className.replace("_", "").toUpperCase() + "_LOG = \"start of add"+ CapitalCase.toCapitalCase(className) +"() method\";");
						writer.println("	public static final String _DAO_END_ADD_"+ className.replace("_", "").toUpperCase() + "_LOG = \"end of add"+ CapitalCase.toCapitalCase(className) +"() method\";");
						
						writer.println("	public static final String _DAO_START_UPDATE_"+ className.replace("_", "").toUpperCase() + "_LOG = \"start of update"+ CapitalCase.toCapitalCase(className) +"() method\";");
						writer.println("	public static final String _DAO_END_UPDATE_"+ className.replace("_", "").toUpperCase() + "_LOG = \"end of update"+ CapitalCase.toCapitalCase(className) +"() method\";");
						
						writer.println("	public static final String _DAO_START_DELETE_"+ className.replace("_", "").toUpperCase() + "_LOG = \"start of delete"+ CapitalCase.toCapitalCase(className) +"() method\";");
						writer.println("	public static final String _DAO_END_DELETE_"+ className.replace("_", "").toUpperCase() + "_LOG = \"end of delete"+ CapitalCase.toCapitalCase(className) +"() method\";");
						
						writer.println("	public static final String _DAO_START_UPDATE_VIEW_" + className.replace("_", "").toUpperCase() + "_LOG = \"start of updateView"+ CapitalCase.toCapitalCase(className) +"() method\";");
						writer.println("	public static final String _DAO_END_UPDATE_VIEW_"+ className.replace("_", "").toUpperCase() + "_LOG = \"end of updateView"+ CapitalCase.toCapitalCase(className) +"() method\";");
						
						String cataLog = con.getCatalog();
						ArrayList<String> duplicateString = new ArrayList<String>();
						  rsf = databaseMetaData.getImportedKeys(cataLog, null, className);
							duplicateString = new ArrayList<String>();
							boolean fkFlag = false;
							String fkTableName = "";
							while (rsf.next()) {
								fkTableName = rsf.getString("PKTABLE_NAME");
								//String fkColumnName = rsf.getString("FKCOLUMN_NAME");
								String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
								if (!duplicateString.contains(fkTableName) && fkSchemaName.equalsIgnoreCase(schemaName)) {
									fkFlag = true;
									break;
								}
								duplicateString.add(fkTableName);
							}
							rsf.close();
							if(fkFlag) {
								writer.println("	public static final String "+("_START_GET"+CapitalCase.toCapitalCase(fkTableName)+"Add_").toUpperCase()+ className.replace("_", "").toUpperCase() + "_LOG = \"start of doPost method of Get"+CapitalCase.toCapitalCase(className)+"Add"+ CapitalCase.toCapitalCase(className) +" class\";");
								writer.println("	public static final String "+("_END_GET"+CapitalCase.toCapitalCase(fkTableName)+"Add_").toUpperCase()
										+ className.replace("_", "").toUpperCase() + "_LOG = \"end of doPost method of Get"+CapitalCase.toCapitalCase(className)+"Add"+ CapitalCase.toCapitalCase(className) +" class\";");
								writer.println("	public static final String "+("_DAO_START_GET"+CapitalCase.toCapitalCase(fkTableName)+"BY").toUpperCase()+ className.replace("_", "").toUpperCase() + "_LOG = \"start of get"+CapitalCase.toCapitalCase(className)+"By"+ CapitalCase.toCapitalCase(className) +"() method\";");
								writer.println("	public static final String "+("_DAO_END_GET"+CapitalCase.toCapitalCase(fkTableName)+"BY").toUpperCase()
										+ className.replace("_", "").toUpperCase() + "_LOG = \"end of get"+CapitalCase.toCapitalCase(className)+"By"+ CapitalCase.toCapitalCase(className) +"() method\";");
								}
						
						writer.println("");
						writer.println("");

					}
					break;
				}
			}
			writer.println("	public static final String _STATUS_PATH = \"/status.jsp\";");
			writer.println(" //Security");	
			writer.println("  public static final String _USERNAME=\"username\";");
			writer.println("  public static final String _PASSWORD=\"password\";");
			writer.println("  public static final String _ADMIN=\"admin\" ;");
			writer.println("  public static final String _USER=\"user\" ;");
			writer.println("  public static final String _USER_HOME=\"./userhome.jsp\" ;");
			writer.println("  public static Object _INVALID_USER=\"Invalid UserName & Password\";");
			writer.println("  public static String _LOGIN_PAGE=\"./LoginForm.jsp\";");
			writer.println("  public static final String _ROLE=\"role\" ;");
			writer.println("  public static final String _SERVER_BUSY=\"Server Busy plz Try After Some time\";");
			writer.println("  public static final String  _LOGOUT_SUCCESS=\"Logout Successfully\";");
			writer.println("  public static final String  _LOGOUT_FAILED=\"Logout Failed\";   ");  
			writer.println("  public static final String _ADMIN_PASSWORD_CHANGE=\"./Changepassword.jsp\";");
			writer.println("  public static final String _PASSWORD_SUCCESS=\"Password Changed Successfully\";");
			writer.println("  public static final String _PASSWORD_FAILED=\"Password Changing Failed\";");
			writer.println("  public static final String _USER_PASSWORD_CHANGE=\"./Changepassword.jsp\";");
			writer.println("  public static final String _QUESTION_CHANGE=\"./Changequestion.jsp\";");
			writer.println("  public static final String _RECOVER_PASSWORD=\"./Recoverpassword.jsp\";");
			writer.println("  public static final String _RECOVER_PASSWORD_SUCCESS=\"Password Recovered Successfully\";");
			writer.println("  public static final String _RECOVER_PASSWORD_FAILED=\"Password Recovering is Failed\";");
			   
			writer.println("");
			writer.println("}");
			con.close();
			writer.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			PrintWriter writer = new PrintWriter(packageNameString + "\\InitServlet.java");

			String packageImport = "package com." + pack + ".util;\n";

			packageImport += "import java.io.IOException; \n";
			packageImport += "import java.io.InputStream; \n";
			packageImport += "import java.util.Properties; \n";

			packageImport += "import javax.servlet.ServletConfig; \n";
			packageImport += "import javax.servlet.ServletContext; \n";
			packageImport += "import javax.servlet.http.HttpServlet; \n";

			packageImport += "import com." + pack + ".dao.AbstractDataAccessObject; \n";
			packageImport += "import com." + pack + ".util.LoggerManager; \n";
			writer.println(packageImport);
			writer.println("public class InitServlet extends HttpServlet { ");
			writer.println("	/** ");
			writer.println("	 *  ");
			writer.println("	 */ ");
			writer.println("	private static final long serialVersionUID = 1L; ");
			writer.println("	AbstractDataAccessObject dobject; ");

			writer.println("	public void init(ServletConfig sc) ");
			writer.println("		{ ");
			writer.println("		ServletContext ctx = sc.getServletContext(); ");
			writer.println("		InputStream fis = ctx.getResourceAsStream(sc.getInitParameter(\"properties\")); ");
			writer.println("		Properties props = new Properties(); ");
			writer.println("		 ");
			writer.println("		try ");
			writer.println("		{ ");
			writer.println("			props.load(fis); ");
			writer.println("		} ");
			writer.println("		catch (IOException ioe) ");
			writer.println("	{ ");
			writer.println("			ioe.printStackTrace(); ");
			writer.println("		} ");
			writer.println("		ctx.setAttribute(\"recordsPerPage\", props.getProperty(\"recordsPerPage\"));");
			writer.println("		dobject = new AbstractDataAccessObject(); ");
			writer.println("		dobject.setProperties(props); ");

			writer.println(
					"		LoggerManager.logger = new LoggerManager().getLogger(props.getProperty(\"logfile\")); ");
			writer.println("		LoggerManager.writeLogInfo(\"Logger Instantiated\"); ");

			writer.println("		} ");

			writer.println("} ");

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			PrintWriter writer = new PrintWriter(packageNameString + "\\LoggerManager.java");
			String packageImport = "package com." + pack + ".util;\n";

			packageImport += "import java.io.File;\n";
			packageImport += "import java.util.logging.FileHandler;\n";
			packageImport += "import java.util.logging.Level;\n";
			packageImport += "import java.util.logging.LogManager;\n";
			packageImport += "import java.util.logging.Logger;\n";
			packageImport += "import java.util.logging.SimpleFormatter;\n";

			writer.println(packageImport);
			writer.println("");
			writer.println("");
			writer.println("public class LoggerManager { \n");
			writer.println("	public static Logger logger; \n");

			writer.println("	public LoggerManager()");
			writer.println("	{");
			writer.println("	}");

			writer.println("	public Logger getLogger(String aFilePath) ");
			writer.println("	{ ");
			writer.println("	String aLogDir = aFilePath.substring(0, aFilePath.lastIndexOf(\"/\")); ");
			writer.println("	logger = Logger.getLogger(\"Logger\"); ");
			writer.println("	try ");
			writer.println("	{ ");
			writer.println("	File aFile = new File( aLogDir ); ");
			writer.println("	boolean success = aFile.exists(); ");

			writer.println("		if (!success) ");
			writer.println("			success = aFile.mkdir(); ");

			writer.println("		LogManager lm = LogManager.getLogManager(); ");
			writer.println("		FileHandler fh = new FileHandler(aFilePath, true); ");
			writer.println("		logger = Logger.getLogger(\"LoggerManager\"); ");
			writer.println("		logger.setUseParentHandlers(false); ");
			writer.println("		lm.addLogger(logger); ");
			writer.println("		logger.setLevel(Level.INFO); ");
			writer.println("		fh.setFormatter(new SimpleFormatter()); ");
			writer.println("		logger.addHandler(fh); ");
			// fh.close();
			writer.println("	} ");
			writer.println("	catch (Exception e) ");
			writer.println("	{ ");

			writer.println("		logger.log(Level.INFO, e.toString(), e.fillInStackTrace()); ");
			writer.println("	} ");
			writer.println("	return logger; ");
			writer.println(" ");
			writer.println("}");
			writer.println("	 ");
			writer.println("	public static void writeLogInfo(Exception e) ");
			writer.println("	{ ");
			writer.println("		logger.log(Level.INFO, e.toString(), e.fillInStackTrace()); ");
			writer.println("	} ");
			writer.println("	 ");
			writer.println("	public static void writeLogSevere(Exception e) ");
			writer.println("	{ ");
			writer.println("		logger.log(Level.SEVERE, e.toString(), e.fillInStackTrace()); ");
			writer.println("	} ");
			writer.println("	 ");
			writer.println("	public static void writeLogWarning(Exception e) ");
			writer.println("	{ ");
			writer.println("		logger.log(Level.WARNING, e.toString(), e.fillInStackTrace()); ");
			writer.println("	} ");
			writer.println("	 ");
			writer.println("	public static void writeLogInfo(String info) ");
			writer.println("	{ ");
			writer.println("		logger.log(Level.INFO, info); ");
			writer.println("	} ");
			writer.println("	 ");
			writer.println("	");
			writer.println("	 ");
			writer.println("	public static void writeLogSevere(String severe)");
			writer.println("	 ");
			writer.println("	{");
			writer.println("	 ");
			writer.println("		logger.log(Level.SEVERE, severe);");
			writer.println("	} ");
			writer.println("	 ");
			writer.println("	public static void writeLogWarning(String warning) ");
			writer.println("	{ ");
			writer.println("		logger.log(Level.WARNING, warning); ");
			writer.println("	} ");
			writer.println("	 ");
			writer.println("	} ");

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			PrintWriter writer = new PrintWriter(
					packageNameString + "\\" + CapitalCase.toCapitalCase(title) + "Utils.java");
			String packageImport = "package com." + pack + ".util;\n";

			packageImport += "import java.util.List;\n";

			writer.println(packageImport);
			writer.println("");
			writer.println("");
			writer.println("public class " + CapitalCase.toCapitalCase(title) + "Utils { \n");
			writer.println("	public static <T> List<T> getSubList(List<T> list, int fromIndex, int toIndex) { ");
			writer.println(
					"			fromIndex = fromIndex < 0 ? 0 : fromIndex > list.size() ? list.size() : fromIndex;");
			writer.println("			toIndex = toIndex > list.size() ? list.size() : toIndex < 0 ? 0 : toIndex;");
			writer.println("	  	return list.subList(fromIndex, toIndex);");
			writer.println("		}");
			writer.println("	}");

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Tried for Pagination
		/*try {
			PrintWriter writer = new PrintWriter(".\\jsp" + "\\pagination.jsp");
			writer.println("<%@ page language=\"java\" import=\"java.util.*\" pageEncoding=\"ISO-8859-1\"%>");
			writer.println("<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>");
			writer.println("");
			writer.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
			writer.println("<html>");
			writer.println("<head>");
			writer.println("<link rel=\"stylesheet\"");
			writer.println(
					"	href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css\">");
			writer.println("</head>");
			writer.println("<style>");
			writer.println(".page-link {");
			writer.println("	position: relative;");
			writer.println("	display: block;");
			writer.println("	padding: .5rem .75rem;");
			writer.println("	margin-left: -1px;");
			writer.println("	line-height: 1.25;");
			writer.println("	color: #0275d8;");
			writer.println("	background-color: #fff;");
			writer.println("	 border: 0px solid #ddd; ");
			writer.println("}");
			writer.println("</style>");
			writer.println("<body>");
			writer.println("	<nav aria-label=\"\">");
			writer.println("	<ul class=\"pagination\">");
			writer.println("		<c:if test=\"${currentPage != 1}\">");
			writer.println("			<li class=\"page-item\"><a class=\"page-link\"");
			writer.println(
					"				href=\"ViewAll"+ CapitalCase.toCapitalCase(className)+"Action?recordsPerPage=${recordsPerPage}&currentPage=${currentPage-1}\">Previous</a>");
			writer.println("			</li>");
			writer.println("		</c:if>");
			writer.println("		<c:forEach begin=\"1\" end=\"${noOfPages}\" var=\"i\">");
			writer.println("			<c:choose>");
			writer.println("				<c:when test=\"${currentPage eq i}\">");
			writer.println("					<li class=\"page-item active\"><a class=\"page-link\"> ${i} <span");
			writer.println("							class=\"sr-only\">(current)</span></a></li>");
			writer.println("				</c:when>");
			writer.println("				<c:otherwise>");
			writer.println("					<li class=\"page-item\"><a class=\"page-link\"");
			writer.println(
					"						href=\"ViewAll"+ CapitalCase.toCapitalCase(title)+"Action?recordsPerPage=${recordsPerPage}&currentPage=${i}\">${i}</a>");
			writer.println("					</li>");
			writer.println("				</c:otherwise>");
			writer.println("			</c:choose>");
			writer.println("		</c:forEach>");
			writer.println("		<c:if test=\"${currentPage lt noOfPages}\">");
			writer.println("			<li class=\"page-item\"><a class=\"page-link\"");
			writer.println(
					"				href=\"ViewAll"+ CapitalCase.toCapitalCase(title)+"Action?recordsPerPage=${recordsPerPage}&currentPage=${currentPage+1}\">Next</a>");
			writer.println("			</li>");
			writer.println("		</c:if>");
			writer.println("	</ul>");
			writer.println("	</nav>");
			writer.println("</body>");
			writer.println("</html>");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
}
