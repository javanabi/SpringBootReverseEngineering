package com.springboot.uipages;

import java.io.File;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.jdbc.dao.AbstractDataAccessObject;
import com.springboot.persistance.jpa.JPAPersistance;
import com.springboot.project.properties.ReadProjectPropertiesFile;
import com.springboot.util.InputNamesFileRead;

public class ThymeleafHeaderHtmlPagesImpl extends AbstractDataAccessObject {

	PreparedStatement pstmt;
	String pack, resourcePackage;
	String title;
	ResultSet resultSet, rs, rsf, tableTypes, contraintsRecords;
	String loginTableName = ReadProjectPropertiesFile.projectProps.getProperty("database-login-table");
	String cssTitle = ReadProjectPropertiesFile.projectProps.getProperty("css-title");
	String uitemplateNumber = ReadProjectPropertiesFile.projectProps.getProperty("uitemplate-number");
	List<String> colList= JPAPersistance.nottoDisplayColumnNamesFromPRoperteisFile();
	List<String> basicTableList= JPAPersistance.basicTableListFromPRoperteisFile();
	
	Set<String> inputNames = InputNamesFileRead.inputNames;
	public ThymeleafHeaderHtmlPagesImpl(String resourcePackage) {
		this.resourcePackage = resourcePackage;
	}


	public void createThymeLeafDefaultHeaderPage() throws SQLException {
		try {
			File fragmentsDir = new File(resourcePackage + "\\templates\\fragments");
			if (!fragmentsDir.exists())
				fragmentsDir.mkdir();
			
			PrintWriter writer = new PrintWriter(fragmentsDir.getAbsolutePath() + "\\head.html");
			writer.println("<head>");
			writer.println("    <meta charset=\"utf-8\" />");
			writer.println("    <link rel=\"icon\" type=\"image/png\" sizes=\"96x96\" href=\"/images/favicon.png\">");
			writer.println("    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />");
			writer.println("");
			writer.println("    <title>"+title+"</title>");
			writer.println("");
			writer.println("    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />");
			writer.println("    <meta name=\"viewport\" content=\"width=device-width\" />");
			writer.println("");
			writer.println("");
			writer.println("    <!-- Bootstrap core CSS     -->");
			writer.println("    <link th:href=\"@{/css/bootstrap.min.css}\" rel=\"stylesheet\" />");
			writer.println("");
			writer.println("    <!-- Animation library for notifications   -->");
			writer.println("    <link th:href=\"@{/css/animate.min.css}\" rel=\"stylesheet\"/>");
			writer.println("");
			writer.println("    <!--  Paper Dashboard core CSS    -->");
			writer.println("    <link th:href=\"@{/css/paper-dashboard.css}\" rel=\"stylesheet\"/>");
			writer.println("");
			writer.println("    <!--  CSS for Demo Purpose, don't include it in your project     -->");
			writer.println("    <link th:href=\"@{/css/demo.css}\" rel=\"stylesheet\" />");
			writer.println("");
			writer.println("    <!--  Fonts and icons     -->");
			writer.println("	<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">");
			writer.println("    <link href=\"https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css\" rel=\"stylesheet\">");
			writer.println("    <link href='https://fonts.googleapis.com/css?family=Muli:400,300' rel='stylesheet' type='text/css'>");
			writer.println("    <link th:href=\"@{/css/themify-icons.css}\" rel=\"stylesheet\">");
			writer.println("    <link th:rel=\"stylesheet\" th:href=\"@{/assets/bootstrap-datepicker/css/bootstrap-datepicker.css}\"/>");
			writer.println("    <link th:rel=\"stylesheet\"); th:href=\"@{/assets/bootstrap-datepicker/css/bootstrap-datepicker.standalone.css}\"/>");
			
			writer.println("<link rel=\"stylesheet\"	th:href=\"@{/assets/bootstrap/css/bootstrap.min.css}\" />");
			writer.println("<link rel=\"stylesheet\"	th:href=\"@{/assets/font-awesome-4.5.0/css/font-awesome.min.css}\" />");
			writer.println("<link rel=\"stylesheet\" th:href=\"@{/css/styles.css}\" />");
			
			writer.println("</head>");
			writer.println("");
			writer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createThymeLeafFirstHeaderPage() {
		try {
			
			File fragmentsDir = new File(resourcePackage + "\\templates\\fragments");
			if (!fragmentsDir.exists())
				fragmentsDir.mkdir();
			
			PrintWriter writer = new PrintWriter(fragmentsDir.getAbsolutePath() + "\\head.html");
			writer.println("<head>");
			writer.println("    <meta charset=\"utf-8\" />");
			writer.println("    <link rel=\"icon\" type=\"image/png\" sizes=\"96x96\" href=\"/images/favicon.png\">");
			writer.println("    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />");
			writer.println("    <title>Sweet</title>");
			writer.println("    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />");
			writer.println("	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
			writer.println("	<link rel=\"stylesheet\" th:href=\"@{/css/"+cssTitle+".css}\">");
			writer.println("	<link rel=\"stylesheet\" th:href=\"@{/css/"+cssTitle+"-theme-black.css}\">");
			writer.println("	<link rel=\"stylesheet\" th:href=\"@{/css/"+cssTitle+"-Roboto.css}\">");
			writer.println("	<link rel=\"stylesheet\" th:href=\"@{/css/font-awesome.min.css}\">");
			writer.println("");
			writer.println("<style>");
			writer.println("html, body, h1, h2, h3, h4, h5, h6 {");
			writer.println("	font-family: \"Roboto\", sans-serif;");
			writer.println("}");
			writer.println("");
			writer.println("."+cssTitle+"-sidebar {");
			writer.println("	z-index: 3;");
			writer.println("	width: 250px;");
			writer.println("	top: 43px;");
			writer.println("	bottom: 0;");
			writer.println("	height: inherit;");
			writer.println("}");
			writer.println("</style>");
			writer.println("    <!-- Bootstrap core CSS     -->");
			writer.println("    <link th:href=\"@{/css/bootstrap.min.css}\" rel=\"stylesheet\" />");
			writer.println("");
			writer.println("    <!-- Animation library for notifications   -->");
			writer.println("    <link th:href=\"@{/css/animate.min.css}\" rel=\"stylesheet\"/>");
			writer.println("");
			writer.println("    <!--  Paper Dashboard core CSS    -->");
			writer.println("    <link th:href=\"@{/css/paper-dashboard.css}\" rel=\"stylesheet\"/>");
			writer.println("");
			writer.println("    <!--  CSS for Demo Purpose, don't include it in your project     -->");
			writer.println("    <link th:href=\"@{/css/demo.css}\" rel=\"stylesheet\" />");
			writer.println("");
			writer.println("    <!--  Fonts and icons     -->");
			writer.println("	<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">");
			writer.println("    <link href=\"https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css\" rel=\"stylesheet\">");
			writer.println("    <link href='https://fonts.googleapis.com/css?family=Muli:400,300' rel='stylesheet' type='text/css'>");
			writer.println("    <link th:href=\"@{/css/themify-icons.css}\" rel=\"stylesheet\">");
			writer.println("    <link th:rel=\"stylesheet\" th:href=\"@{/assets/bootstrap-datepicker/css/bootstrap-datepicker.css}\"/>");
			writer.println("    <link th:rel=\"stylesheet\"); th:href=\"@{/assets/bootstrap-datepicker/css/bootstrap-datepicker.standalone.css}\"/>");
			writer.println("	<link rel=\"stylesheet\"	th:href=\"@{/assets/bootstrap/css/bootstrap.min.css}\" />");
			writer.println("	<link rel=\"stylesheet\"	th:href=\"@{/assets/font-awesome-4.5.0/css/font-awesome.min.css}\" />");
			writer.println("	<link rel=\"stylesheet\" th:href=\"@{/css/"+cssTitle+"-styles.css}\" />");
			writer.println("</head>");
			writer.println("");
			writer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void createThymeLeafSecondHeaderPage() {
		try {
			
			File fragmentsDir = new File(resourcePackage + "\\templates\\fragments");
			if (!fragmentsDir.exists())
				fragmentsDir.mkdir();
			
			PrintWriter writer = new PrintWriter(fragmentsDir.getAbsolutePath() + "\\head.html");
			writer.println("<head>");
			writer.println("    <meta charset=\"utf-8\" />");
			writer.println("    <link rel=\"icon\" type=\"image/png\" sizes=\"96x96\" href=\"/images/favicon.png\">");
			writer.println("    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />");
			writer.println("    <title>Sweet</title>");
			writer.println("    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />");
			writer.println("	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
			writer.println("	<link rel=\"stylesheet\" th:href=\"@{/css/"+cssTitle+".css}\">");
			writer.println("	<link rel=\"stylesheet\" th:href=\"@{/css/"+cssTitle+"-theme-black.css}\">");
			writer.println("	<link rel=\"stylesheet\" th:href=\"@{/css/"+cssTitle+"-Roboto.css}\">");
			writer.println("	<link rel=\"stylesheet\" th:href=\"@{/css/font-awesome.min.css}\">");
			writer.println("");
			writer.println("<style>");
			writer.println("html, body, h1, h2, h3, h4, h5, h6 {");
			writer.println("	font-family: \"Roboto\", sans-serif;");
			writer.println("}");
			writer.println("");
			writer.println("."+cssTitle+"-sidebar {");
			writer.println("	z-index: 3;");
			writer.println("	width: 250px;");
			writer.println("	top: 43px;");
			writer.println("	bottom: 0;");
			writer.println("	height: inherit;");
			writer.println("}");
			writer.println("</style>");
			writer.println("    <!-- Bootstrap core CSS     -->");
			writer.println("    <link th:href=\"@{/css/bootstrap.min.css}\" rel=\"stylesheet\" />");
			writer.println("");
			writer.println("    <!-- Animation library for notifications   -->");
			writer.println("    <link th:href=\"@{/css/animate.min.css}\" rel=\"stylesheet\"/>");
			writer.println("");
			writer.println("    <!--  Paper Dashboard core CSS    -->");
			writer.println("    <link th:href=\"@{/css/paper-dashboard.css}\" rel=\"stylesheet\"/>");
			writer.println("");
			writer.println("    <!--  CSS for Demo Purpose, don't include it in your project     -->");
			writer.println("    <link th:href=\"@{/css/demo.css}\" rel=\"stylesheet\" />");
			writer.println("");
			writer.println("    <!--  Fonts and icons     -->");
			writer.println("	<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">");
			writer.println("    <link href=\"https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css\" rel=\"stylesheet\">");
			writer.println("    <link href='https://fonts.googleapis.com/css?family=Muli:400,300' rel='stylesheet' type='text/css'>");
			writer.println("    <link th:href=\"@{/css/themify-icons.css}\" rel=\"stylesheet\">");
			writer.println("    <link th:rel=\"stylesheet\" th:href=\"@{/assets/bootstrap-datepicker/css/bootstrap-datepicker.css}\"/>");
			writer.println("    <link th:rel=\"stylesheet\"); th:href=\"@{/assets/bootstrap-datepicker/css/bootstrap-datepicker.standalone.css}\"/>");
			writer.println("	<link rel=\"stylesheet\"	th:href=\"@{/assets/bootstrap/css/bootstrap.min.css}\" />");
			writer.println("	<link rel=\"stylesheet\"	th:href=\"@{/assets/font-awesome-4.5.0/css/font-awesome.min.css}\" />");
			writer.println("	<link rel=\"stylesheet\" th:href=\"@{/css/"+cssTitle+"-styles.css}\" />");
			writer.println("</head>");
			writer.println(""); 
			writer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}