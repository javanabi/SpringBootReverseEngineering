package com.menu.impl;

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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.jdbc.dao.AbstractDataAccessObject;
import com.jdbc.main.CapitalCase;
import com.jdbc.main.SqlDatatypeTOJavaWrapperClass;

public class MenuImpl extends AbstractDataAccessObject{
	
	PreparedStatement pstmt;
	String pack;
	String title;
	ResultSet resultSet, rs, rsf, tableTypes, contraintsRecords;
	String schemaName = getProperties().getProperty("duser");
	public MenuImpl(String pack,String title) {
		super();
		this.pack = pack;
		this.title = title;
	}
	
	public void createMenuFiles(Connection con) throws SQLException{
		try {

	DatabaseMetaData databaseMetaData = con.getMetaData();
	ResultSet tableTypes = databaseMetaData.getTableTypes();
	
	
	PrintWriter writer = new PrintWriter(".\\jsp\\adminmenu.jsp");
	writer.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml2/DTD/xhtml1-strict.dtd\">");
	writer.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
	writer.println("<head>");
	writer.println("		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
	writer.println("		<title>"+title+"</title>");
	writer.println("		<meta name=\"Author\" content=\"Stu Nicholls\" />");
	writer.println("		<link rel=\"stylesheet\" type=\"text/css\" href=\"./css/pro_drop_1.css\" />");
	writer.println("		<script src=\"css/stuHover.js\" type=\"text/javascript\">");
	writer.println("</script>");
	writer.println("	</head>");
	writer.println("	<body>");
	writer.println("	<span class=\"preload1\"></span>");
	writer.println("	<span class=\"preload2\"></span>");
	writer.println("		<ul id=\"nav\">");
	writer.println("			<li class=\"top\">");
	writer.println("				<a href=\"./adminhome.jsp\" class=\"top_link\"><span>Home</span>");
	writer.println("				</a>");
	writer.println("			</li>");
	writer.println("<li class=\"top\">"); 
	writer.println("<a href=\"#nogo22\" id=\"services\" class=\"top_link\"><span"); 
	writer.println("	class=\"down\">Registration</span> </a>"); 
	writer.println("<ul class=\"sub\">"); 
	writer.println("	<li>"); 
	writer.println("		<a href=\"./registration.jsp\">Registering</a>"); 
	writer.println("	</li>"); 
	writer.println("</ul>"); 
	writer.println("</li>");
	
	while (tableTypes.next()) {
		if ("TABLE"
				.equalsIgnoreCase(tableTypes.getString("TABLE_TYPE"))) {
			ResultSet rs = databaseMetaData.getTables(null,
					databaseMetaData.getUserName(), "%",
					new String[] { "TABLE" });
			while (rs.next()) {
				String actualTableName = rs.getString("TABLE_NAME");
				String tableName = CapitalCase.toCapitalCase(rs.getString("TABLE_NAME"));
				writer.println("			<li class=\"top\">");
				writer.println("				<a href=\"#nogo22\" id=\"services\" class=\"top_link\"><span");
				writer.println("					class=\"down\">"+tableName+"</span> </a>");
				writer.println("				<ul class=\"sub\">");
				writer.println("					<li>");
				
				String cataLog = con.getCatalog();
				boolean fkFlag = false;
				DatabaseMetaData databaseMetaDataForFks = con.getMetaData();
				ResultSet rsf = databaseMetaDataForFks.getImportedKeys(cataLog, null, actualTableName.toUpperCase());
				List<String> duplicateString = new ArrayList<String>();
				String ffkTableName="";
				while (rsf.next()) {
					ffkTableName = rsf.getString("PKTABLE_NAME");
					//String fkColumnName = rsf.getString("FKCOLUMN_NAME");
					String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
					if(fkSchemaName == null) fkSchemaName = "";
					if (!duplicateString.contains(ffkTableName) && fkSchemaName.equalsIgnoreCase(schemaName)) {
						fkFlag = true;
						break;
						}
					duplicateString.add(ffkTableName);
				}
				if(fkFlag) {
					writer.println("						<a href=\"./Get"+CapitalCase.toCapitalCase(ffkTableName)+"Add"
							+ CapitalCase.toCapitalCase(tableName) + "Action\">Add"+CapitalCase.toCapitalCase(tableName)+"</a>");
					fkFlag = false;
				}
				else {
				writer.println("						<a href=\"./Add"
				+ CapitalCase.toCapitalCase(tableName) + ".jsp\">Add"+tableName+"</a>");
				writer.println("					</li>");
				}
				writer.println("					<li>");
				writer.println("						<a href=\"./ViewAll"
					+ CapitalCase.toCapitalCase(tableName) + "Action\">View"+tableName+"</a>");
				writer.println("					</li>");
				writer.println("				</ul>");
				writer.println("			</li>");
			}
			break;
		}
	}
	writer.println("			<li class=\"top\">");
	writer.println("						<a href=\"#nogo22\" id=\"services\" class=\"top_link\"><span");
	writer.println("				class=\"down\">security</span> </a>");
	writer.println("					<ul class=\"sub\">");
	writer.println("			<li>");
	writer.println("				<a");
	writer.println("				href=\"./changepassword.jsp?userid=<%=session.getAttribute(\"user\")%>\">changepassword</a>");
	writer.println("			</li>");
	writer.println("		</ul>");
	writer.println("			</li>");
	writer.println("			<li class=\"top\">");
	writer.println("				<a href=\"./LogoutAction\" class=\"top_link\"><span>Logout</span>");
	writer.println("				</a>");
	writer.println("			 </li>");
	writer.println("		</ul>");
	writer.println("	</body>");
	writer.println("</html>");
	writer.close();
	
	
	writer = new PrintWriter(".\\jsp\\adminmenu1.jsp");
	writer.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml2/DTD/xhtml1-strict.dtd\">");
	writer.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
	writer.println("<head>");
	writer.println("		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
	writer.println("		<title>"+title+"</title>");
	writer.println("		<meta name=\"Author\" content=\"Stu Nicholls\" />");
	writer.println("		<link rel=\"stylesheet\" type=\"text/css\" href=\"./css/pro_drop_1.css\" />");
	writer.println("		<script src=\"css/stuHover.js\" type=\"text/javascript\">");
	writer.println("</script>");
	writer.println("	</head>");
	writer.println("	<body>");
	writer.println("	<span class=\"preload1\"></span>");
	writer.println("	<span class=\"preload2\"></span>");
	writer.println("		<ul id=\"nav\">");
	writer.println("			<li class=\"top\">");
	writer.println("				<a href=\"./adminhome.jsp\" class=\"top_link\"><span>Home</span>");
	writer.println("				</a>");
	writer.println("			</li>");
	writer.println("<li class=\"top\">"); 
	writer.println("<a href=\"#nogo22\" id=\"services\" class=\"top_link\"><span"); 
	writer.println("	class=\"down\">Registration</span> </a>"); 
	writer.println("<ul class=\"sub\">"); 
	writer.println("	<li>"); 
	writer.println("		<a href=\"./registration.jsp\">Registering</a>"); 
	writer.println("	</li>"); 
	writer.println("</ul>"); 
	writer.println("</li>");
	
	while (tableTypes.next()) {
		if ("TABLE"
				.equalsIgnoreCase(tableTypes.getString("TABLE_TYPE"))) {
			ResultSet rs = databaseMetaData.getTables(null,
					databaseMetaData.getUserName(), "%",
					new String[] { "TABLE" });
			while (rs.next()) {
				String tableName = CapitalCase.toCapitalCase(rs.getString("TABLE_NAME"));
				writer.println("			<li class=\"top\">");
				writer.println("				<a href=\"#nogo22\" id=\"services\" class=\"top_link\"><span");
				writer.println("					class=\"down\">"+tableName+"</span> </a>");
				writer.println("				<ul class=\"sub\">");
				writer.println("					<li>");
				writer.println("						<a href=\"./Add"
				+ CapitalCase.toCapitalCase(tableName) + ".jsp\">Add"+tableName+"</a>");
				writer.println("					</li>");
				writer.println("					<li>");
				writer.println("						<a href=\"./ViewAll"
					+ CapitalCase.toCapitalCase(tableName) + "Action\">View"+tableName+"</a>");
				writer.println("					</li>");
				writer.println("				</ul>");
				writer.println("			</li>");
			}
			break;
		}
	}
	writer.println("			<li class=\"top\">");
	writer.println("						<a href=\"#nogo22\" id=\"services\" class=\"top_link\"><span");
	writer.println("				class=\"down\">security</span> </a>");
	writer.println("					<ul class=\"sub\">");
	writer.println("			<li>");
	writer.println("				<a");
	writer.println("				href=\"./changepassword.jsp?userid=<%=session.getAttribute(\"user\")%>\">changepassword</a>");
	writer.println("			</li>");
	writer.println("		</ul>");
	writer.println("			</li>");
	writer.println("			<li class=\"top\">");
	writer.println("				<a href=\"./LogoutAction\" class=\"top_link\"><span>Logout</span>");
	writer.println("				</a>");
	writer.println("			 </li>");
	writer.println("		</ul>");
	writer.println("	</body>");
	writer.println("</html>");
	writer.close();
	
	writer = new PrintWriter(".\\jsp\\adminmenu2.jsp");
	writer.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml2/DTD/xhtml1-strict.dtd\">");
	writer.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
	writer.println("<head>");
	writer.println("		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
	writer.println("		<title>"+title+"</title>");
	writer.println("		<meta name=\"Author\" content=\"Stu Nicholls\" />");
	writer.println("		<link rel=\"stylesheet\" type=\"text/css\" href=\"./css/pro_drop_1.css\" />");
	writer.println("		<script src=\"css/stuHover.js\" type=\"text/javascript\">");
	writer.println("</script>");
	writer.println("	</head>");
	writer.println("	<body>");
	writer.println("	<span class=\"preload1\"></span>");
	writer.println("	<span class=\"preload2\"></span>");
	writer.println("		<ul id=\"nav\">");
	writer.println("			<li class=\"top\">");
	writer.println("				<a href=\"./adminhome.jsp\" class=\"top_link\"><span>Home</span>");
	writer.println("				</a>");
	writer.println("			</li>");
	writer.println("<li class=\"top\">"); 
	writer.println("<a href=\"#nogo22\" id=\"services\" class=\"top_link\"><span"); 
	writer.println("	class=\"down\">Registration</span> </a>"); 
	writer.println("<ul class=\"sub\">"); 
	writer.println("	<li>"); 
	writer.println("		<a href=\"./registration.jsp\">Registering</a>"); 
	writer.println("	</li>"); 
	writer.println("</ul>"); 
	writer.println("</li>");
	
	while (tableTypes.next()) {
		if ("TABLE"
				.equalsIgnoreCase(tableTypes.getString("TABLE_TYPE"))) {
			ResultSet rs = databaseMetaData.getTables(null,
					databaseMetaData.getUserName(), "%",
					new String[] { "TABLE" });
			while (rs.next()) {
				String tableName = CapitalCase.toCapitalCase(rs.getString("TABLE_NAME"));
				writer.println("			<li class=\"top\">");
				writer.println("				<a href=\"#nogo22\" id=\"services\" class=\"top_link\"><span");
				writer.println("					class=\"down\">"+tableName+"</span> </a>");
				writer.println("				<ul class=\"sub\">");
				writer.println("					<li>");
				writer.println("						<a href=\"./Add"
				+ CapitalCase.toCapitalCase(tableName) + ".jsp\">Add"+tableName+"</a>");
				writer.println("					</li>");
				writer.println("					<li>");
				writer.println("						<a href=\"./ViewAll"
					+ CapitalCase.toCapitalCase(tableName) + "Action\">View"+tableName+"</a>");
				writer.println("					</li>");
				writer.println("				</ul>");
				writer.println("			</li>");
			}
			break;
		}
	}
	writer.println("			<li class=\"top\">");
	writer.println("						<a href=\"#nogo22\" id=\"services\" class=\"top_link\"><span");
	writer.println("				class=\"down\">security</span> </a>");
	writer.println("					<ul class=\"sub\">");
	writer.println("			<li>");
	writer.println("				<a");
	writer.println("				href=\"./changepassword.jsp?userid=<%=session.getAttribute(\"user\")%>\">changepassword</a>");
	writer.println("			</li>");
	writer.println("		</ul>");
	writer.println("			</li>");
	writer.println("			<li class=\"top\">");
	writer.println("				<a href=\"./LogoutAction\" class=\"top_link\"><span>Logout</span>");
	writer.println("				</a>");
	writer.println("			 </li>");
	writer.println("		</ul>");
	writer.println("	</body>");
	writer.println("</html>");
	writer.close();
	
	
	writer = new PrintWriter(".\\jsp\\adminmenu3.jsp");
	writer.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml2/DTD/xhtml1-strict.dtd\">");
	writer.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
	writer.println("<head>");
	writer.println("		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
	writer.println("		<title>"+title+"</title>");
	writer.println("		<meta name=\"Author\" content=\"Stu Nicholls\" />");
	writer.println("		<link rel=\"stylesheet\" type=\"text/css\" href=\"./css/pro_drop_1.css\" />");
	writer.println("		<script src=\"css/stuHover.js\" type=\"text/javascript\">");
	writer.println("</script>");
	writer.println("	</head>");
	writer.println("	<body>");
	writer.println("	<span class=\"preload1\"></span>");
	writer.println("	<span class=\"preload2\"></span>");
	writer.println("		<ul id=\"nav\">");
	writer.println("			<li class=\"top\">");
	writer.println("				<a href=\"./adminhome.jsp\" class=\"top_link\"><span>Home</span>");
	writer.println("				</a>");
	writer.println("			</li>");
	writer.println("<li class=\"top\">"); 
	writer.println("<a href=\"#nogo22\" id=\"services\" class=\"top_link\"><span"); 
	writer.println("	class=\"down\">Registration</span> </a>"); 
	writer.println("<ul class=\"sub\">"); 
	writer.println("	<li>"); 
	writer.println("		<a href=\"./registration.jsp\">Registering</a>"); 
	writer.println("	</li>"); 
	writer.println("</ul>"); 
	writer.println("</li>");
	
	while (tableTypes.next()) {
		if ("TABLE"
				.equalsIgnoreCase(tableTypes.getString("TABLE_TYPE"))) {
			ResultSet rs = databaseMetaData.getTables(null,
					databaseMetaData.getUserName(), "%",
					new String[] { "TABLE" });
			while (rs.next()) {
				String tableName = CapitalCase.toCapitalCase(rs.getString("TABLE_NAME"));
				writer.println("			<li class=\"top\">");
				writer.println("				<a href=\"#nogo22\" id=\"services\" class=\"top_link\"><span");
				writer.println("					class=\"down\">"+tableName+"</span> </a>");
				writer.println("				<ul class=\"sub\">");
				writer.println("					<li>");
				writer.println("						<a href=\"./Add"
				+ CapitalCase.toCapitalCase(tableName) + ".jsp\">Add"+tableName+"</a>");
				writer.println("					</li>");
				writer.println("					<li>");
				writer.println("						<a href=\"./ViewAll"
					+ CapitalCase.toCapitalCase(tableName) + "Action\">View"+tableName+"</a>");
				writer.println("					</li>");
				writer.println("				</ul>");
				writer.println("			</li>");
			}
			break;
		}
	}
	writer.println("			<li class=\"top\">");
	writer.println("						<a href=\"#nogo22\" id=\"services\" class=\"top_link\"><span");
	writer.println("				class=\"down\">security</span> </a>");
	writer.println("					<ul class=\"sub\">");
	writer.println("			<li>");
	writer.println("				<a");
	writer.println("				href=\"./changepassword.jsp?userid=<%=session.getAttribute(\"user\")%>\">changepassword</a>");
	writer.println("			</li>");
	writer.println("		</ul>");
	writer.println("			</li>");
	writer.println("			<li class=\"top\">");
	writer.println("				<a href=\"./LogoutAction\" class=\"top_link\"><span>Logout</span>");
	writer.println("				</a>");
	writer.println("			 </li>");
	writer.println("		</ul>");
	writer.println("	</body>");
	writer.println("</html>");
	writer.close();
	
	writer = new PrintWriter(".\\jsp\\homemenu.jsp");
	writer.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml2/DTD/xhtml1-strict.dtd\">");
	writer.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
	writer.println("<head>");
	writer.println("		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
	writer.println("		<title>"+title+"</title>");
	writer.println("		<meta name=\"Author\" content=\"Stu Nicholls\" />");
	writer.println("		<link rel=\"stylesheet\" type=\"text/css\" href=\"./css/pro_drop_1.css\" />");
	writer.println("		<script src=\"css/stuHover.js\" type=\"text/javascript\">");
	writer.println("</script>");
	writer.println("	</head>");
	writer.println("	<body>");
	writer.println("	<span class=\"preload1\"></span>");
	writer.println("	<span class=\"preload2\"></span>");
	writer.println("		<ul id=\"nav\">");
	writer.println("			<li class=\"top\">");
	writer.println("				<a href=\"./home.jsp\" class=\"top_link\"><span>Home</span>");
	writer.println("				</a>");
	writer.println("			</li>");
	writer.println("<li class=\"top\">"); 
	writer.println("		<a href=\"./registration.jsp\" class=\"top_link\"><span>Registering</span></a>"); 
	writer.println("	</li>"); 
	writer.println("	<li class=\"top\">"); 
	writer.println("		<a href=\"./aboutus.jsp\" class=\"top_link\"><span>Aboutus</span></a>"); 
	writer.println("	</li>"); 
	writer.println("	<li class=\"top\">"); 
	writer.println("		<a href=\"./contactus.jsp\" class=\"top_link\"><span>contact</span></a>"); 
	writer.println("	</li>"); 
	writer.println("	<li>"); 
	writer.println("				<a");
	writer.println("				href=\"./loginform.jsp\" class=\"top_link\"><span>login</span></a>");
	writer.println("			</li>");
	writer.println("		</ul>");
	writer.println("	</body>");
	writer.println("</html>");
	writer.close();
	
	 
	}catch(Exception e){
		e.printStackTrace();
	}
}
	
}