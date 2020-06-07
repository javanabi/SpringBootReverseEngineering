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

public class ThymeleafNavigationHtmlPagesImpl extends AbstractDataAccessObject {

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
	public ThymeleafNavigationHtmlPagesImpl(String resourcePackage) {
		this.resourcePackage = resourcePackage;
	}


	public void createThymeLeafFirstNavigationPage() throws SQLException {
		try {
			File fragmentsDir = new File(resourcePackage + "\\templates\\fragments");
			if (!fragmentsDir.exists())
				fragmentsDir.mkdir();
			
			PrintWriter writer = new PrintWriter(fragmentsDir.getAbsolutePath() + "\\navbar.html");
			
			writer.println("<!-- Navbar -->");
			writer.println("<div class=\""+cssTitle+"-top\">");
			writer.println("  <div class=\""+cssTitle+"-bar "+cssTitle+"-theme "+cssTitle+"-top "+cssTitle+"-left-align "+cssTitle+"-large\">");
			writer.println("    <a class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-right "+cssTitle+"-hide-large "+cssTitle+"-hover-white "+cssTitle+"-large "+cssTitle+"-theme-l1\" href=\"javascript:void(0)\" onclick=\""+cssTitle+"_open()\"><i class=\"fa fa-bars\"></i></a>");
			writer.println("    <a href=\"#\" class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-theme-l1\">Logo</a>");
			writer.println("    <a href=\"#\" class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-hide-small "+cssTitle+"-hover-white\">About</a>");
			writer.println("    <a href=\"#\" class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-hide-small "+cssTitle+"-hover-white\">Values</a>");
			writer.println("    <a href=\"#\" class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-hide-small "+cssTitle+"-hover-white\">News</a>");
			writer.println("    <a href=\"#\" class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-hide-small "+cssTitle+"-hover-white\">Contact</a>");
			writer.println("    <a href=\"#\" class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-hide-small "+cssTitle+"-hide-medium "+cssTitle+"-hover-white\">Clients</a>");
			writer.println("    <a href=\"#\" class=\""+cssTitle+"-bar-item "+cssTitle+"-button "+cssTitle+"-hide-small "+cssTitle+"-hide-medium "+cssTitle+"-hover-white\">Partners</a>");
			writer.println("  </div>");
			writer.println("</div>");
			writer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createThymeLeafDefaultNavigationPage(PrintWriter writer) {
		try {
			 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}