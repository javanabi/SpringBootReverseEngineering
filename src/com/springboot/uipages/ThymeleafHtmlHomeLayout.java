package com.springboot.uipages;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.springboot.persistance.jpa.JPAPersistance;
import com.springboot.project.properties.ReadProjectPropertiesFile;
import com.springboot.util.InputNamesFileRead;

public class ThymeleafHtmlHomeLayout {

	String resourcePackage;
	String title = ReadProjectPropertiesFile.projectProps.getProperty("title");
	String footer = ReadProjectPropertiesFile.projectProps.getProperty("footer");
	String loginTableName = ReadProjectPropertiesFile.projectProps.getProperty("database-login-table");
	String cssTitle = ReadProjectPropertiesFile.projectProps.getProperty("css-title");
	String uitemplateNumber = ReadProjectPropertiesFile.projectProps.getProperty("uitemplate-number");
	String addstemplateNumber = ReadProjectPropertiesFile.projectProps.getProperty("addstemplate-number");
	List<String> colList= JPAPersistance.nottoDisplayColumnNamesFromPRoperteisFile();
	List<String> basicTableList= JPAPersistance.basicTableListFromPRoperteisFile();
	
	Set<String> inputNames = InputNamesFileRead.inputNames;
	public ThymeleafHtmlHomeLayout(String resourcePackage) {
		super();
		this.resourcePackage = resourcePackage;
	}

	

	public boolean isForeignKey(String tableName, String columnName, DatabaseMetaData databaseMetaData, String cataLog,
			Map<String, Map<String, String>> listOfForeignKeys) {
		boolean flag = false;
		try {
			for (String tName : listOfForeignKeys.keySet()) {
				Map<String, String> foriegnKeyMap = listOfForeignKeys.get(tName);
				for (String fKeys : foriegnKeyMap.keySet()) {
					if (foriegnKeyMap.get(fKeys).equalsIgnoreCase(columnName) && tableName.equalsIgnoreCase(fKeys)) {
						flag = true;
						break;
					}
				}
				if (flag)
					break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;

	}
	
	private boolean isForeignKey(Connection con, String tableName, String name)throws Exception {
		boolean flag = false;
		ResultSet rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
		while (rsf.next()) {
			String fkColumnName = rsf.getString("FKCOLUMN_NAME");
			if (fkColumnName.equalsIgnoreCase(name)) { 
				flag =  true;
				break;
			}
		}
		return flag;
	}

	 

	 

	 
	public void createThymeLeafHomeLayouts() throws SQLException {
		try {
			
			File packageDir = new File(resourcePackage + "\\templates\\");
			if (!packageDir.exists())
				packageDir.mkdir();
			 
			PrintWriter writer = new PrintWriter(packageDir.getAbsolutePath() + "\\layout.html");
			writer.println("<!DOCTYPE html>");
			writer.println("<html xmlns=\"http://www.w3.org/1999/xhtml\"");
			writer.println("	xmlns:th=\"http://www.thymeleaf.org\"");
			writer.println("	xmlns:layout=\"http://www.ultraq.net.nz/thymeleaf/layout\"");
			writer.println("	xmlns:sec=\"http://www.thymeleaf.org/thymeleaf-extras-springsecurity3\">");
			writer.println("<head>");
			writer.println("<meta charset=\"utf-8\" />");
			writer.println("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />");
			writer.println("<link rel=\"icon\" type=\"image/png\" sizes=\"96x96\" href=\"/images/favicon.png\">");
			writer.println("");
			writer.println("<title layout:title-pattern=\"$DECORATOR_TITLE - $CONTENT_TITLE\">"+title+"</title>");
			writer.println("<meta");
			writer.println("	content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\"");
			writer.println("	name=\"viewport\" />");
			writer.println("<link rel=\"stylesheet\"");
			writer.println("	th:href=\"@{/assets/bootstrap/css/bootstrap.min.css}\" />");
			writer.println("<link rel=\"stylesheet\"");
			writer.println("	th:href=\"@{/assets/font-awesome-4.5.0/css/font-awesome.min.css}\" />");
			writer.println("<link rel=\"stylesheet\" th:href=\"@{/css/"+cssTitle+"-styles.css}\" />");
			writer.println("");
			writer.println("<style>");
			writer.println(".footer {");
			writer.println("	position: fixed;");
			writer.println("	left: 0;");
			writer.println("	bottom: 0;");
			writer.println("	width: 100%;");
			writer.println("	background-color: black;");
			writer.println("	color: white;");
			writer.println("	height: 100px;");
			writer.println("	text-align: center;");
			writer.println("}");
			writer.println("</style>");
			writer.println("");
			writer.println("</head>");
			writer.println("");
			writer.println("<body class=\""+cssTitle+"-black\">");
			writer.println("	<nav class=\"navbar navbar-inverse navbar-fixed-top\">");
			writer.println("		<div class=\"container\">");
			writer.println("			<div class=\"navbar-header\">");
			writer.println("				<button type=\"button\" class=\"navbar-toggle collapsed\"");
			writer.println("					data-toggle=\"collapse\" data-target=\"#navbar\" aria-expanded=\"false\"");
			writer.println("					aria-controls=\"navbar\">");
			writer.println("					<span class=\"sr-only\">Toggle navigation</span> <span");
			writer.println("						class=\"icon-bar\"></span> <span class=\"icon-bar\"></span> <span");
			writer.println("						class=\"icon-bar\"></span>");
			writer.println("				</button>");
			writer.println("				<a class=\"navbar-brand\" href=\"#\" th:href=\"@{/}\">"+title+"</a>");
			writer.println("				<a class=\"navbar-brand\" href=\"#\" th:href=\"@{/}\">About</a>");
			writer.println("				<a class=\"navbar-brand\" href=\"#\" th:href=\"@{/}\">Values</a>");
			writer.println("				<a class=\"navbar-brand\" href=\"#\" th:href=\"@{/}\">News</a>");
			writer.println("				<a class=\"navbar-brand\" href=\"#\" th:href=\"@{/}\">Contact</a>");
			writer.println("				<a class=\"navbar-brand\" href=\"#\" th:href=\"@{/}\">Clients</a>");
			writer.println("				<a class=\"navbar-brand\" href=\"#\" th:href=\"@{/}\">Partners</a>");
			writer.println("			</div>");
			writer.println("			<div id=\"navbar\" class=\"collapse navbar-collapse\">");
			writer.println("				<ul class=\"nav navbar-nav\">");
			writer.println("					<li sec:authorize=\"isAuthenticated()\"><a th:href=\"@{/logout}\">Logout</a></li>");
			writer.println("				</ul>");
			writer.println("			</div>");
			writer.println("		</div>");
			writer.println("	</nav>");
			writer.println("");
			writer.println("	<div class=\"container\">");
			writer.println("");
			writer.println("		<div layout:fragment=\"content\">");
			writer.println("			<!-- Your Page Content Here -->");
			writer.println("		</div>");
			writer.println("");
			writer.println("	</div>");
			writer.println("");
			writer.println("	<script th:src=\"@{'/assets/js/jquery-2.1.4.min.js'}\"></script>");
			writer.println("	<script th:src=\"@{'/assets/bootstrap/js/bootstrap.min.js'}\"></script>");
			writer.println("");
			writer.println("<footer class=\""+cssTitle+"-container "+cssTitle+"-padding-64 "+cssTitle+"-center "+cssTitle+"-opacity "+cssTitle+"-light-grey "+cssTitle+"-xlarge\">");
			writer.println("  <i class=\"fa fa-facebook-official "+cssTitle+"-hover-opacity\"></i>");
			writer.println("  <i class=\"fa fa-instagram "+cssTitle+"-hover-opacity\"></i>");
			writer.println("  <i class=\"fa fa-snapchat "+cssTitle+"-hover-opacity\"></i>");
			writer.println("  <i class=\"fa fa-pinterest-p "+cssTitle+"-hover-opacity\"></i>");
			writer.println("  <i class=\"fa fa-twitter "+cssTitle+"-hover-opacity\"></i>");
			writer.println("  <i class=\"fa fa-linkedin "+cssTitle+"-hover-opacity\"></i>");
			writer.println("  <p class=\""+cssTitle+"-medium\">Powered by <a href=\"#\" target=\"_blank\">Sweet</a></p>");
			writer.println("</footer>");
			writer.println("	<div class=\"footer\">");
			writer.println("		<h1>");
			writer.println("			<a href=\"#\">"+footer+"</a>");
			writer.println("		</h1>");
			writer.println("	</div>");
			writer.println("");
			writer.println("</body>");
			writer.println("</html>");
			writer.close();
			
			createCommonFiles(packageDir);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void createThymeLeafSecondLayout() throws SQLException {
		try {
			
			File packageDir = new File(resourcePackage + "\\templates\\");
			if (!packageDir.exists())
				packageDir.mkdir();
			 
			PrintWriter writer = new PrintWriter(packageDir.getAbsolutePath() + "\\layout.html");
			writer.println("<!DOCTYPE html>");
			writer.println("<html xmlns=\"http://www."+cssTitle+".org/1999/xhtml\"");
			writer.println("	xmlns:th=\"http://www.thymeleaf.org\"");
			writer.println("	xmlns:layout=\"http://www.ultraq.net.nz/thymeleaf/layout\"");
			writer.println("	xmlns:sec=\"http://www.thymeleaf.org/thymeleaf-extras-springsecurity3\">");
			writer.println("<head>");
			writer.println("<meta charset=\"utf-8\" />");
			writer.println("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />");
			writer.println("<link rel=\"icon\" type=\"image/png\" sizes=\"96x96\" href=\"/images/favicon.png\">");
			writer.println("");
			writer.println("<title layout:title-pattern=\"$DECORATOR_TITLE - $CONTENT_TITLE\">Sweet</title>");
			writer.println("<meta");
			writer.println("	content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\"");
			writer.println("	name=\"viewport\" />");
			writer.println("<link rel=\"stylesheet\"");
			writer.println("	th:href=\"@{/assets/bootstrap/css/bootstrap.min.css}\" />");
			writer.println("<link rel=\"stylesheet\"");
			writer.println("	th:href=\"@{/assets/font-awesome-4.5.0/css/font-awesome.min.css}\" />");
			writer.println("<link rel=\"stylesheet\" th:href=\"@{/css/"+cssTitle+"-styles.css}\" />");
			writer.println("	<link rel=\"stylesheet\" th:href=\"@{/css/"+cssTitle+".css}\">");
			writer.println("	<link rel=\"stylesheet\" th:href=\"@{/css/"+cssTitle+"-theme-black.css}\">");
			writer.println("	<link rel=\"stylesheet\" th:href=\"@{/css/"+cssTitle+"-Roboto.css}\">");
			writer.println("	<link rel=\"stylesheet\" th:href=\"@{/css/font-awesome.min.css}\">");
			writer.println("");
			writer.println("<style>");
			writer.println(".footer {");
			writer.println("	position: fixed;");
			writer.println("	left: 0;");
			writer.println("	bottom: 0;");
			writer.println("	width: 100%;");
			writer.println("	background-color: black;");
			writer.println("	color: white;");
			writer.println("	height: 35px;");
			writer.println("	text-align: center;");
			writer.println("}");
			writer.println("</style>");
			writer.println("");
			writer.println("</head>");
			writer.println("");
			writer.println("<body class=\""+cssTitle+"-black\">");
			writer.println("");
			writer.println("	<!-- Icon Bar (Sidebar - hidden on small screens) -->");
			writer.println("	<nav class=\""+cssTitle+"-sidebar "+cssTitle+"-bar-block "+cssTitle+"-small "+cssTitle+"-hide-small "+cssTitle+"-center\">");
			writer.println("		 <a href=\"#\" class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-padding-large "+cssTitle+"-hover-black\">");
			writer.println("			<i class=\"fa fa-home "+cssTitle+"-xxlarge\"></i>");
			writer.println("			<p>HOME</p></a>");
			writer.println("		 <a href=\"#about\"");
			writer.println("			class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-padding-large "+cssTitle+"-hover-black\"> <i");
			writer.println("			class=\"fa fa-user "+cssTitle+"-xxlarge\"></i>");
			writer.println("			<p>ABOUT</p></a>");
			writer.println("		 <a href=\"#photos\"");
			writer.println("			class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-padding-large "+cssTitle+"-hover-black\"> <i");
			writer.println("			class=\"fa fa-eye "+cssTitle+"-xxlarge\"></i>");
			writer.println("			<p>PHOTOS</p> </a>");
			writer.println("		 <a href=\"#contact\"");
			writer.println("			class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-padding-large "+cssTitle+"-hover-black\"> <i");
			writer.println("			class=\"fa fa-envelope "+cssTitle+"-xxlarge\"></i>");
			writer.println("			<p>CONTACT</p> </a>");
			writer.println("	</nav>");
			writer.println("	");
			writer.println("	<!-- Navbar on small screens (Hidden on medium and large screens) -->");
			writer.println("<div class=\""+cssTitle+"-top "+cssTitle+"-hide-large "+cssTitle+"-hide-medium\" id=\"myNavbar\">");
			writer.println("  <div class=\""+cssTitle+"-bar "+cssTitle+"-black "+cssTitle+"-opacity "+cssTitle+"-hover-opacity-off "+cssTitle+"-center "+cssTitle+"-small\">");
			writer.println("    <a href=\"#\" class=\""+cssTitle+"-bar-item "+cssTitle+"-button\" style=\"width:25% !important\">HOME</a>");
			writer.println("    <a href=\"#about\" class=\""+cssTitle+"-bar-item "+cssTitle+"-button\" style=\"width:25% !important\">ABOUT</a>");
			writer.println("    <a href=\"#photos\" class=\""+cssTitle+"-bar-item "+cssTitle+"-button\" style=\"width:25% !important\">PHOTOS</a>");
			writer.println("    <a href=\"#contact\" class=\""+cssTitle+"-bar-item "+cssTitle+"-button\" style=\"width:25% !important\">CONTACT</a>");
			writer.println("  </div>");
			writer.println("</div>");
			writer.println("");
			writer.println("	<div class=\"container\">");
			writer.println("");
			writer.println("		<div layout:fragment=\"content\">");
			writer.println("			<!-- Your Page Content Here -->");
			writer.println("		</div>");
			writer.println("");
			writer.println("	</div>");
			writer.println("");
			writer.println("	<script th:src=\"@{'/assets/js/jquery-2.1.4.min.js'}\"></script>");
			writer.println("	<script th:src=\"@{'/assets/bootstrap/js/bootstrap.min.js'}\"></script>");
			writer.println("");
			writer.println("	<footer");
			writer.println("		class=\""+cssTitle+"-container "+cssTitle+"-padding-64 "+cssTitle+"-center "+cssTitle+"-opacity "+cssTitle+"-light-grey "+cssTitle+"-xlarge\">");
			writer.println("		<i class=\"fa fa-facebook-official "+cssTitle+"-hover-opacity\"></i> <i");
			writer.println("			class=\"fa fa-instagram "+cssTitle+"-hover-opacity\"></i> <i");
			writer.println("			class=\"fa fa-snapchat "+cssTitle+"-hover-opacity\"></i> <i");
			writer.println("			class=\"fa fa-pinterest-p "+cssTitle+"-hover-opacity\"></i> <i");
			writer.println("			class=\"fa fa-twitter "+cssTitle+"-hover-opacity\"></i> <i");
			writer.println("			class=\"fa fa-linkedin "+cssTitle+"-hover-opacity\"></i>");
			writer.println("		<p class=\""+cssTitle+"-medium\">");
			writer.println("			Powered by <a href=\"#\" target=\"_blank\">"+footer+"</a>");
			writer.println("		</p>");
			writer.println("	</footer>");
			writer.println("	<div class=\"footer\">");
			writer.println("		<h1>");
			writer.println("			<a href=\"#\">"+footer+"</a>");
			writer.println("		</h1>");
			writer.println("	</div>");
			writer.println("");
			writer.println("</body>");
			writer.println("</html>");
			writer.close();
			
			createCommonFiles(packageDir);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createCommonFiles(File packageDir) {
		try {
		PrintWriter writer = new PrintWriter(packageDir.getAbsolutePath() + "\\forgetPassword.html");
		writer.println("<!DOCTYPE html>");
		writer.println("<html>");
		writer.println("<head>");
		writer.println("<link rel=\"stylesheet\"");
		writer.println("	href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css\" />");
		writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=US-ASCII\" />");
		writer.println("<link rel=\"icon\" type=\"image/png\" sizes=\"96x96\" href=\"/images/favicon.png\">");
		writer.println("");
		writer.println("<title th:utext=\"#{message.resetPassword}\">reset</title>");
		writer.println("<style>");
		writer.println(".footer {");
		writer.println("	position: fixed;");
		writer.println("	left: 0;");
		writer.println("	bottom: 0;");
		writer.println("	width: 100%;");
		writer.println("	background-color: black;");
		writer.println("	color: white;");
		writer.println("	height: 100px;");
		writer.println("	text-align: center;");
		writer.println("}.panel {");
		writer.println("		margin: 50px 0 0 69px;");
		writer.println("		background-color: #fff;");
		writer.println("		border: 1px solid transparent; ");
		writer.println("	}");
		writer.println("</style>");
		writer.println("</head>");
		writer.println("<body class=\""+cssTitle+"-black\">");
		writer.println("	<nav class=\"navbar navbar-inverse navbar-fixed-top\">");
		writer.println("		<div class=\"container\">");
		writer.println("			<div class=\"navbar-header\">");
		writer.println("				<button type=\"button\" class=\"navbar-toggle collapsed\"");
		writer.println("					data-toggle=\"collapse\" data-target=\"#navbar\" aria-expanded=\"false\"");
		writer.println("					aria-controls=\"navbar\">");
		writer.println("					<span class=\"sr-only\">Toggle navigation</span> <span");
		writer.println("						class=\"icon-bar\"></span> <span class=\"icon-bar\"></span> <span");
		writer.println("						class=\"icon-bar\"></span>");
		writer.println("				</button>");
		writer.println("				<a class=\"navbar-brand\" href=\"/\">"+title+"</a>");
		writer.println("			</div>");
		writer.println("			<div id=\"navbar\" class=\"collapse navbar-collapse\">");
		writer.println("				<ul class=\"nav navbar-nav\">");
		writer.println("");
		writer.println("				</ul>");
		writer.println("			</div>");
		writer.println("		</div>");
		writer.println("	</nav>");
		writer.println("");
		writer.println("	<div layout:fragment=\"content\">");
		writer.println("		<div class=\"panel col-md-5\">");
		writer.println("			<div class=\"panel panel-primary\">");
		writer.println("				<div class=\"panel-heading\">");
		writer.println("					<span th:utext=\"#{message.resetPassword}\"></span>");
		writer.println("				</div>");
		writer.println("				<div class=\"panel-body\">");
		writer.println("					<br />");
		writer.println("					<div class=\"row\">");
		writer.println("						<form action=\"#\">");
		writer.println("							<label class=\"col-sm-1\" th:utext=\"#{label.user.email}\">email</label>");
		writer.println("							<span class=\"col-sm-5\"><input class=\"form-control\"");
		writer.println("								id=\"email\" name=\"email\" type=\"email\" value=\"\"");
		writer.println("								required=\"required\" /></span>");
		writer.println("							<button class=\"btn btn-primary\" type=\"submit\"");
		writer.println("								th:utext=\"#{message.resetPassword}\">reset</button>");
		writer.println("						</form>");
		writer.println("					</div>");			
		writer.println("					</br>");
		writer.println("					<a class=\"btn btn-default\" th:href=\"@{/login}\"");
		writer.println("						th:utext=\"#{label.form.loginLink}\">login</a>");
		writer.println("				</div>");
		writer.println("			</div>");
		writer.println("		</div>");
		writer.println("	</div>");
		writer.println("	<script");
		writer.println("		src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js\"></script>");
		writer.println("	<script th:inline=\"javascript\">");
		writer.println("var serverContext = [[@{/}]];");
		writer.println("");
		writer.println("$(document).ready(function () {");
		writer.println("	$('form').submit(function(event) {");
		writer.println("		resetPass(event);");
		writer.println("    });");
		writer.println("});");
		writer.println("");
		writer.println("function resetPass(event){");
		writer.println("    event.preventDefault();	");
		writer.println("    var email = $(\"#email\").val();");
		writer.println("    $.get(serverContext + \"user/resetPassword\",{email: email} ,function(data){");
		writer.println("            window.location.href = serverContext + \"login?message=\" + data.message;");
		writer.println("    })");
		writer.println("    .fail(function(data) {");
		writer.println("    	if(data.responseJSON.error.indexOf(\"MailError\") > -1)");
		writer.println("        {");
		writer.println("            window.location.href = serverContext + \"emailError.html\";");
		writer.println("        }");
		writer.println("        else{");
		writer.println("            window.location.href = serverContext + \"login?message=\" + data.responseJSON.message;");
		writer.println("        }");
		writer.println("    });");
		writer.println("}");
		writer.println("");
		writer.println("$(document).ajaxStart(function() {");
		writer.println("    $(\"title\").html(\"LOADING ...\");");
		writer.println("});");
		writer.println("</script>");
		writer.println("	<div class=\"footer\">");
		writer.println("		<h1>");
		writer.println("			<a href=\"#\">"+footer+"</a>");
		writer.println("		</h1>");
		writer.println("	</div>");
		writer.println("</body>");
		writer.println("");
		writer.println("</html>");
		writer.close();
		
		
		if(uitemplateNumber.equals("1")) {
			writer = new PrintWriter(packageDir.getAbsolutePath() + "\\fragments\\footer.html");
			writer.println(" <footer id=\"myFooter\">");
			  writer.println("    <div class=\""+cssTitle+"-container "+cssTitle+"-theme-l1\">");
			  writer.println("      <p align='left'>Powered by <a href=\"#\" target=\"_blank\">"+footer+"</a></p>");
			  writer.println("    </div>");
			  writer.println("  </footer>");
			  writer.close();
		}
		else if(uitemplateNumber.equals("2")) {
			writer = new PrintWriter(packageDir.getAbsolutePath() + "\\home.html");
			writer.println("<!DOCTYPE html>");
			writer.println("<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">");
			writer.println("<th:block th:include=\"fragments/head\"></th:block>");
			writer.println("<body class=\""+cssTitle+"-black\">");
			writer.println("	<!-- Page Wrapper -->");
			writer.println("		<th:block th:include=\"fragments/sidebar\"></th:block>");
			writer.println("		<!-- Main Panel -->");
			writer.println("		<div class=\""+cssTitle+"-main\" style=\"margin-left:250px\">");
			writer.println("  			<div class=\""+cssTitle+"-row "+cssTitle+"-padding-64\">");
			writer.println("    				<div class=\""+cssTitle+"-twothird "+cssTitle+"-container\">");
			writer.println("						<div class=\"row\">");
			writer.println("						<div class=\"col-md-12\">");
			writer.println("							<div class=\"card\">");
			writer.println("								<div class=\"header\">");
			writer.println("									<h4 class=\"title\">Welcome</h4>");
			writer.println("							</div>");
			writer.println("							</div>");
			writer.println("						</div>");
			writer.println("					</div>");
			writer.println("				</div>");
			writer.println("				<div class=\"swt-third swt-container\">");
			writer.println("						<p class=\"swt-border swt-padding-large swt-padding-32 swt-center\">AD</p>");
			writer.println("						<p class=\"swt-border swt-padding-large swt-padding-64 swt-center\">AD</p>");
			writer.println("						<p class=\"swt-border swt-padding-large swt-padding-32 swt-center\">AD</p>");
			writer.println("				</div>");
			writer.println("			</div>			");
			writer.println("		</div>");
			writer.println("		<!-- End of Main Panel -->");
			writer.println("		<th:block th:include=\"fragments/footer\"></th:block>");
			writer.println("	<th:block th:include=\"fragments/scripts\"></th:block>");
			writer.println("</body>");
			writer.println("</html>");
			  writer.close();
		}else {
			
		}
		
		
		if(addstemplateNumber.equals("1")) {
			writer = new PrintWriter(packageDir.getAbsolutePath() + "\\fragments\\adds.html");
			writer.println("						<p class=\""+cssTitle+"-border "+cssTitle+"-padding-large "+cssTitle+"-padding-32 "+cssTitle+"-center\">AD</p>");
			writer.println("						<p class=\""+cssTitle+"-border "+cssTitle+"-padding-large "+cssTitle+"-padding-64 "+cssTitle+"-center\">AD</p>");
			writer.println("						<p class=\""+cssTitle+"-border "+cssTitle+"-padding-large "+cssTitle+"-padding-32 "+cssTitle+"-center\">AD</p>");
			    writer.close();
		}else {
			
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	 

	private String getForeignKeyTableName(Connection con, String tableName,String name)throws Exception{
		ResultSet rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
		String fkClassName = "";
		while (rsf.next()) {
			String fkTableName = rsf.getString("PKTABLE_NAME");
			String fkColumnName = rsf.getString("FKCOLUMN_NAME");
			if (fkColumnName.equalsIgnoreCase(name)) {
				fkClassName = fkTableName;
				break;
			}
		}
			return fkClassName;
	}
	
	private List<String> getListOfForeignKeysTable(Connection con, String tableName)throws Exception{
		ResultSet rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
		List<String> keys = new ArrayList<>();
		while (rsf.next()) {
				keys.add(rsf.getString("PKTABLE_NAME").toLowerCase());
		}
			return keys;
	}

	 
}