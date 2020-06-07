package com.springboot.menuimpl;

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
import com.springboot.project.properties.ReadProjectPropertiesFile;

public class ThymeLeafMenuImpl extends AbstractDataAccessObject{
	
	PreparedStatement pstmt;
	String pack,resourcePackage;
	String title;
	ResultSet resultSet, rs, rsf, tableTypes, contraintsRecords;
	String schemaName = getProperties().getProperty("duser");
	String cssTitle = ReadProjectPropertiesFile.projectProps.getProperty("css-title");
	String uitemplateNumber = ReadProjectPropertiesFile.projectProps.getProperty("uitemplate-number");
	
	
	public ThymeLeafMenuImpl(String pack,String title,String resourcePackage) {
		super();
		this.pack = pack;
		this.title = title;
		this.resourcePackage = resourcePackage;
	}
	
	public void createDefaultMenuFiles(Connection con) throws SQLException{
		try {

	DatabaseMetaData databaseMetaData = con.getMetaData();
	ResultSet tableTypes = databaseMetaData.getTableTypes();
	File packageDir = new File(resourcePackage + "\\templates\\fragments\\");
	if (!packageDir.exists())
		packageDir.mkdir();
	PrintWriter writer = new PrintWriter(packageDir.getAbsolutePath() + "\\sidebar.html");
	
	writer.println("<div class=\"sidebar\" data-background-color=\"white\" data-active-color=\"danger\">");
	writer.println("");
	writer.println("    <div class=\"sidebar-wrapper\">");
	String loginTableName = ReadProjectPropertiesFile.projectProps.getProperty("database-login-table");
	 
	 writer.println("		<div>");
	 writer.println("			<img th:src=\"@{${session.userProfileImage}}\" style=\"width: 100%\">");
	 writer.println("			<h3 align='center' ><span th:text=\"${#authentication.getPrincipal().getUsername()}\"></span> </h3>");
	 writer.println("				<div align='center'>");
	 writer.println("					<a th:href=\"@{/"+loginTableName.toLowerCase()+"/view"+CapitalCase.toCapitalCase(loginTableName)+"/0}\"> <i class=\"fa fa-eye\"></i> </a> ");
	 writer.println("					<a th:href=\"@{/"+loginTableName.toLowerCase()+"/edit"+CapitalCase.toCapitalCase(loginTableName)+"/0}\"> <i class=\"fa fa-pencil-square-o\"></i> </a>");
	 writer.println("				</div>");
	 writer.println("			<p align='center'><a th:href=\"@{/passwordChange}\"> Change Password </a> </p>");
	 //writer.println("			<p align='center'><a th:href=\"@{/viewUserProfile}\"> View Profile </a> </p>");
	 writer.println("			<p align='center'><a th:href=\"@{/logout}\"> Logout</a> </p>");
	 writer.println("		</div>");
	 writer.println("<hr/>");
	writer.println("");
	writer.println("        <ul class=\"nav\">");
	writer.println("            <li>");
	writer.println("                <a th:href=\"@{/}\">");
	writer.println("                    <p><i class=\"ti-plus\"></i>  Home</p>");
	writer.println("                </a>");
	writer.println("            </li>");
	
	while (tableTypes.next()) {
		if ("TABLE"
				.equalsIgnoreCase(tableTypes.getString("TABLE_TYPE"))) {
			ResultSet rs = databaseMetaData.getTables(null,
					databaseMetaData.getUserName(), "%",
					new String[] { "TABLE" });
			while (rs.next()) {
				String actualTableName = rs.getString("TABLE_NAME");
				String tableName = CapitalCase.toCapitalCase(rs.getString("TABLE_NAME"));
				writer.println("            <li>");
				writer.println("                <a th:href=\"@{/"+tableName.toLowerCase()+"/viewAll"+CapitalCase.toCapitalCase(tableName)+"}\">");
				writer.println("                    <p><i class=\"ti-book\"></i>     "+CapitalCase.toCapitalCase(tableName)+"</p>");
				writer.println("                </a>");
				writer.println("            </li>");
				writer.println("            <li>");
				writer.println("                <a th:href=\"@{'/"+tableName.toLowerCase()+"/viewAll"+CapitalCase.toCapitalCase(tableName)+"ByUser/'+${#authentication.getPrincipal().getUsername()}}\">");
				writer.println("                    <p><i class=\ti-plus\"></i>     "+CapitalCase.toCapitalCase(tableName)+"</p>");
				writer.println("                </a>");
				writer.println("            </li>");
				writer.println("            </li>");
				writer.println("            <li sec:authorize=\"hasRole('ADMIN')\">");
				writer.println("                <a th:href=\"@{/"+tableName.toLowerCase()+"/add"+CapitalCase.toCapitalCase(tableName)+"}\">");
				writer.println("                    <p><i class=\"ti-plus\"></i>     Add "+CapitalCase.toCapitalCase(tableName)+"</p>");
				writer.println("                </a>");
				writer.println("            </li>");
			}
		}
	}
	writer.println("            </li>");
	writer.println("            <li>");
	writer.println("                <a th:href=\"@{/logout}\">");
	writer.println("                    <i class=\"ti-plus\"></i>");
	writer.println("                    <p>Logout</p>");
	writer.println("                </a>");
	writer.println("            </li>");
	writer.println("        </ul>");
	writer.println("    </div>");
	writer.println("</div>");
	writer.println("");
	writer.println("");
	writer.close();
	
	 
	}catch(Exception e){
		e.printStackTrace();
	}
}
	
	public void createFirstMenuFiles(Connection con) throws SQLException{
		try {

		DatabaseMetaData databaseMetaData = con.getMetaData();
		ResultSet tableTypes = databaseMetaData.getTableTypes();
		File packageDir = new File(resourcePackage + "\\templates\\fragments\\");
		if (!packageDir.exists())
			packageDir.mkdir();
		PrintWriter writer = new PrintWriter(packageDir.getAbsolutePath() + "\\sidebar.html");
		
		writer.println("<div class=\""+cssTitle+"-sidebar "+cssTitle+"-bar-block "+cssTitle+"-collapse "+cssTitle+"-large "+cssTitle+"-theme-l5 "+cssTitle+"-animate-left\" id=\"mySidebar\">");
		writer.println("  <a href=\"javascript:void(0)\" onclick=\""+cssTitle+"_close()\" class=\""+cssTitle+"-right "+cssTitle+"-xlarge "+cssTitle+"-padding-large "+cssTitle+"-hover-black "+cssTitle+"-hide-large\" title=\"Close Menu\">");
		writer.println("    <i class=\"fa fa-remove\"></i>");
		writer.println("  </a>");
		writer.println("");
		writer.println("    <div class=\"sidebar-wrapper\">");
		String loginTableName = ReadProjectPropertiesFile.projectProps.getProperty("database-login-table");
		 
		 writer.println("		<div>");
		 writer.println("			<img th:src=\"@{${session.userProfileImage}}\" style=\"width: 100%\">");
		 writer.println("			<h3 align='center' ><span th:text=\"${#authentication.getPrincipal().getUsername()}\"></span> </h3>");
		 writer.println("				<div align='center'>");
		 writer.println("					<a th:href=\"@{/"+loginTableName.toLowerCase()+"/view"+CapitalCase.toCapitalCase(loginTableName)+"/0}\"> <i class=\"fa fa-eye\"></i> </a> ");
		 writer.println("					<a th:href=\"@{/"+loginTableName.toLowerCase()+"/edit"+CapitalCase.toCapitalCase(loginTableName)+"/0}\"> <i class=\"fa fa-pencil-square-o\"></i> </a>");
		 writer.println("				</div>");
		 writer.println("			<p align='center'><a class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-hover-black\" th:href=\"@{/passwordChange}\"> Change Password </a> </p>");
		 //writer.println("			<p align='center'><a th:href=\"@{/viewUserProfile}\"> View Profile </a> </p>");
		 writer.println("			<p align='center'><a class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-hover-black\" th:href=\"@{/logout}\"> Logout</a> </p>");
		 writer.println("		</div>");
		 writer.println("<hr/>");
		writer.println("");
		writer.println("        <ul class=\"nav\">");
		writer.println("            <li>");
		writer.println("                <a th:href=\"@{/}\">");
		writer.println("                    <p><i class=\"ti-plus\"></i>Home</p>");
		writer.println("                </a>");
		writer.println("            </li>");
		
		while (tableTypes.next()) {
			if ("TABLE"
					.equalsIgnoreCase(tableTypes.getString("TABLE_TYPE"))) {
				ResultSet rs = databaseMetaData.getTables(null,
						databaseMetaData.getUserName(), "%",
						new String[] { "TABLE" });
				while (rs.next()) {
					String actualTableName = rs.getString("TABLE_NAME");
					String tableName = CapitalCase.toCapitalCase(rs.getString("TABLE_NAME"));
					writer.println("            <li>");
					writer.println("                <a class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-hover-black\"  th:href=\"@{/"+tableName.toLowerCase()+"/viewAll"+CapitalCase.toCapitalCase(tableName)+"}\">");
					writer.println("                    <p><i class=\"ti-plus\"></i>     "+CapitalCase.toCapitalCase(tableName)+"</p>");
					writer.println("                </a>");
					writer.println("            </li>");
					writer.println("            <li>");
					writer.println("                <a class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-hover-black\"  th:href=\"@{'/"+tableName.toLowerCase()+"/viewAll"+CapitalCase.toCapitalCase(tableName)+"ByUser/'+${#authentication.getPrincipal().getUsername()}}\">");
					writer.println("                    <p><i class=\"ti-plus\"></i>     "+CapitalCase.toCapitalCase(tableName)+"</p>");
					writer.println("                </a>");
					writer.println("            </li>");
					writer.println("            </li>");
					writer.println("            <li sec:authorize=\"hasRole('ADMIN')\">");
					writer.println("                <a class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-hover-black\"  th:href=\"@{/"+tableName.toLowerCase()+"/add"+CapitalCase.toCapitalCase(tableName)+"}\">");
					writer.println("                    <p><i class=\"ti-plus\"></i>     Add "+CapitalCase.toCapitalCase(tableName)+"</p>");
					writer.println("                </a>");
					writer.println("            </li>");
				}
			}
		}
		writer.println("            </li>");
		writer.println("        </ul>");
		writer.println("    </div>");
		writer.println("</div>");
		writer.println("<!-- Overlay effect when opening sidebar on small screens -->");
		writer.println("<div class=\""+cssTitle+"-overlay "+cssTitle+"-hide-large\" onclick=\""+cssTitle+"_close()\" style=\"cursor:pointer\" title=\"close side menu\" id=\"myOverlay\"></div>");

		writer.println("");
		writer.println("");
		writer.close();
	 
		
	}catch(Exception e){
		e.printStackTrace();
	}
}
	
	public void createSecondMenuFiles(Connection con) throws SQLException{
		try {

		DatabaseMetaData databaseMetaData = con.getMetaData();
		ResultSet tableTypes = databaseMetaData.getTableTypes();
		File packageDir = new File(resourcePackage + "\\templates\\fragments\\");
		if (!packageDir.exists())
			packageDir.mkdir();
		PrintWriter writer = new PrintWriter(packageDir.getAbsolutePath() + "\\sidebar.html");
		
		writer.println("		<nav");
		writer.println("			class=\""+cssTitle+"-sidebar "+cssTitle+"-bar-block "+cssTitle+"-small "+cssTitle+"-hide-small "+cssTitle+"-center\">");
		writer.println("				<img th:src=\"@{${session.userProfileImage}}\" style=\"width: 100%\">");
		writer.println("				<h3 align='center'>");
		writer.println("					<span th:text=\"${#authentication.getPrincipal().getUsername()}\"></span>");
		writer.println("				</h3>  ");
		writer.println("			<a th:href=\"@{/userdetails/viewUserdetails/0}\"> <i");
		writer.println("					class=\"fa fa-eye\"></i>");
		writer.println("			</a> ");
		writer.println("			<a th:href=\"@{/userdetails/editUserdetails/0}\"> <i class=\"fa fa-pencil-square-o\"></i></a>");
		writer.println("			<a class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-hover-black\" th:href=\"@{/passwordChange}\"> Change Password </a> ");
		writer.println("			<a class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-padding-large\" th:href=\"@{/logout}\">Logout</a> ");
		writer.println("			<a href=\"#\" class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-padding-large\"><p>HOME</p></a>");
		
		while (tableTypes.next()) {
			if ("TABLE"
					.equalsIgnoreCase(tableTypes.getString("TABLE_TYPE"))) {
				ResultSet rs = databaseMetaData.getTables(null,
						databaseMetaData.getUserName(), "%",
						new String[] { "TABLE" });
				while (rs.next()) {
					String actualTableName = rs.getString("TABLE_NAME");
					String tableName = CapitalCase.toCapitalCase(rs.getString("TABLE_NAME"));
					writer.println("                <p class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-padding-large "+cssTitle+"-hover-black\">");
					writer.println("                    <i class=\"fa fa-user "+cssTitle+"-xxlarge\"></i><br>");
					writer.println("                <a th:href=\"@{/"+tableName.toLowerCase()+"/viewAll"+CapitalCase.toCapitalCase(tableName)+"}\">"+CapitalCase.toCapitalCase(tableName)+"</a>|");
					writer.println("                <a th:href=\"@{'/"+tableName.toLowerCase()+"/viewAll"+CapitalCase.toCapitalCase(tableName)+"ByUser/'+${#authentication.getPrincipal().getUsername()}}\">"+CapitalCase.toCapitalCase(tableName)+"</a>|");
					writer.println("                <a th:href=\"@{/"+tableName.toLowerCase()+"/add"+CapitalCase.toCapitalCase(tableName)+"}\">Add "+CapitalCase.toCapitalCase(tableName)+"</a>");
					//writer.println("            </li>");
				}
			}
		}
		writer.println("		</nav>");
		writer.println("");
		writer.println("");
		writer.close();
	 
		
	}catch(Exception e){
		e.printStackTrace();
	}
}
}