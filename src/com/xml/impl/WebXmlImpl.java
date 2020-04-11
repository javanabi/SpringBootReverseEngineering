package com.xml.impl;

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
import java.util.List;
import java.util.Vector;

import com.jdbc.dao.AbstractDataAccessObject;
import com.jdbc.main.CapitalCase;
import com.jdbc.main.SqlDatatypeTOJavaWrapperClass;
import com.util.impl.UtilImpl;

public class WebXmlImpl extends AbstractDataAccessObject{
	
	PreparedStatement pstmt;
	String pack,title;
	ResultSet resultSet, rs, rsf, tableTypes, contraintsRecords;
	String schemaName = getProperties().getProperty("duser");
	public WebXmlImpl(String pack,String title) {
		super();
		this.pack = pack;
		this.title = title;
	}
	
	public void createWebXmlFile(Connection con) throws SQLException{
		try {
			String className="";
			String packageNameString = ".\\jsp\\WEB-INF";

			PrintWriter writer = new PrintWriter(packageNameString + "\\web.xml");
			writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			writer.println("<web-app version=\"2.4\" xmlns=\"http://java.sun.com/xml/ns/j2ee\"");
			writer.println("	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
			writer.println("	xsi:schzemaLocation=\"http://java.sun.com/xml/ns/j2ee");
			writer.println("	http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd\">");
			writer.println("	<servlet>");
			writer.println("		<description>This is the description of my J2EE component</description>");
			writer.println("		<display-name>This is the display name of my J2EE component</display-name>");
			writer.println("		<servlet-name>InitServlet</servlet-name>");
			writer.println("		<servlet-class>com."+pack+".util.InitServlet</servlet-class>");
    		writer.println("		<init-param>");
			writer.println("			<param-name>properties</param-name>");
			writer.println("			<param-value>/WEB-INF/properties/"+title+".properties</param-value>");
			writer.println("		</init-param>");
			writer.println("		<load-on-startup>0</load-on-startup>");
			writer.println("	</servlet>");
			writer.println("		<servlet-mapping>");
			writer.println("		<servlet-name>InitServlet</servlet-name>");
			writer.println("		<url-pattern>/InitServlet</url-pattern>");
			writer.println("	</servlet-mapping>");
			writer.println("  <servlet>");
			writer.println("       <servlet-name>ExportPDF</servlet-name>");
     		writer.println("       <servlet-class>com."+pack+".exports.ExportPDF</servlet-class>");
			writer.println("  </servlet>");
			writer.println("  <servlet>");
			writer.println("       <servlet-name>ExportTXT</servlet-name>");
			writer.println("       <servlet-class>com."+pack+".exports.ExportTXT</servlet-class>");
			writer.println("  </servlet>");
			writer.println("  <servlet>");
	    	writer.println("       <servlet-name>ExportXLS</servlet-name>");
			writer.println("       <servlet-class>com."+pack+".exports.ExportXLS</servlet-class>");
			writer.println("  </servlet>");

			writer.println("  <servlet-mapping>");
			writer.println("       <servlet-name>ExportPDF</servlet-name>");
			writer.println("       <url-pattern>/ExportPDF</url-pattern>");
			writer.println("  </servlet-mapping>");
			writer.println("  <servlet-mapping>");
			writer.println("       <servlet-name>ExportTXT</servlet-name>");
			writer.println("       <url-pattern>/ExportTXT</url-pattern>");
			writer.println("  </servlet-mapping>");
     		writer.println("    <servlet-mapping>");
	    	writer.println("       <servlet-name>ExportXLS</servlet-name>");
		 	writer.println("       <url-pattern>/ExportXLS</url-pattern>");
		 	writer.println("  </servlet-mapping>");
		 	
//		 	writer.println("    <servlet-mapping>");
//	    	writer.println("       <servlet-name>RegisterAction</servlet-name>");
//		 	writer.println("       <url-pattern>/RegisterAction</url-pattern>");
//		 	writer.println("  </servlet-mapping>");
//		 	
//		    writer.println("  <servlet>");
//    		writer.println("  		<description>This is the description of my J2EE component</description>");
//			writer.println("  		<display-name>This is the display name of my J2EE component</display-name>");
//			writer.println("  		<servlet-name>RegisterAction</servlet-name>");
//			writer.println("  		<servlet-class>com."+pack+".action.RegisterAction</servlet-class>");
//			writer.println("  </servlet>");
		    
		    writer.println("  <servlet>");
    		writer.println("  		<description>This is the description of my J2EE component</description>");
			writer.println("  		<display-name>This is the display name of my J2EE component</display-name>");
			writer.println("  		<servlet-name>LoginAction</servlet-name>");
			writer.println("  		<servlet-class>com."+pack+".action.LoginAction</servlet-class>");
			writer.println("  </servlet>");
			writer.println("  <servlet>");
			writer.println("  		<description>This is the description of my J2EE component</description>");
			writer.println("  		<display-name>This is the display name of my J2EE component</display-name>");
			writer.println("  		<servlet-name>ChangePasswordAction</servlet-name>");
			writer.println("  		<servlet-class>com."+pack+".action.ChangePasswordAction</servlet-class>");
			writer.println("  </servlet>");
			writer.println("  <servlet>");
			writer.println("  		<description>This is the description of my J2EE component</description>");
			writer.println("  		<display-name>This is the display name of my J2EE component</display-name>");
			writer.println("  		<servlet-name>RecoverPasswordAction</servlet-name>");
			writer.println("  		<servlet-class>com."+pack+".action.RecoverPasswordAction</servlet-class>");
			writer.println("  </servlet>");
		    
			writer.println("  <servlet-mapping>");
    		writer.println("	   <servlet-name>LoginAction</servlet-name>");
			writer.println("	   <url-pattern>/LoginAction</url-pattern>");
			writer.println("  </servlet-mapping>");
			writer.println("  <servlet-mapping>");
			writer.println("	   <servlet-name>ChangePasswordAction</servlet-name>");
			writer.println("	   <url-pattern>/ChangePasswordAction</url-pattern>");
			writer.println("  </servlet-mapping>");
     		writer.println("  <servlet-mapping>");
			writer.println("	   <servlet-name>RecoverPasswordAction</servlet-name>");
			writer.println("	   <url-pattern>/RecoverPasswordAction</url-pattern>");
	    	writer.println("  </servlet-mapping>");
	    	
	    	/*DatabaseMetaData databaseMetaData = con.getMetaData();
			ResultSet tableTypes = databaseMetaData.getTableTypes();
			while (tableTypes.next()) {
				if ("TABLE"
						.equalsIgnoreCase(tableTypes.getString("TABLE_TYPE"))) {
					ResultSet rs = databaseMetaData.getTables(null,
							databaseMetaData.getUserName(), "%",
							new String[] { "TABLE" });
					while (rs.next()) {
						className = rs.getString("TABLE_NAME");
	    	
			writer.println("  <servlet>");
			writer.println("  		<description>This is the description of my J2EE component</description>");
			writer.println("  		<display-name>This is the display name of my J2EE component</display-name>");
			writer.println("  		<servlet-name>Add"+ CapitalCase.toCapitalCase(className) + "Action</servlet-name>");
			writer.println("  		<servlet-class>com."+pack+".action.Add"+ CapitalCase.toCapitalCase(className) + "Action</servlet-class>");
			writer.println("  </servlet>");
	    	writer.println("  <servlet-mapping>");
			writer.println("	   <servlet-name>Add"+ CapitalCase.toCapitalCase(className) + "Action</servlet-name>");
			writer.println("	   <url-pattern>/Add"+ CapitalCase.toCapitalCase(className) + "Action</url-pattern>");
	    	writer.println("  </servlet-mapping>");
	    	
			writer.println("  <servlet>");
			writer.println("  		<description>This is the description of my J2EE component</description>");
			writer.println("  		<display-name>This is the display name of my J2EE component</display-name>");
			writer.println("  		<servlet-name>ViewAll"+ CapitalCase.toCapitalCase(className) + "Action</servlet-name>");
			writer.println("  		<servlet-class>com."+pack+".action.ViewAll"+ CapitalCase.toCapitalCase(className) + "Action</servlet-class>");
			writer.println("  </servlet>");
	    	writer.println("  <servlet-mapping>");
			writer.println("	   <servlet-name>ViewAll"+ CapitalCase.toCapitalCase(className) + "Action</servlet-name>");
			writer.println("	   <url-pattern>/ViewAll"+ CapitalCase.toCapitalCase(className) + "Action</url-pattern>");
	    	writer.println("  </servlet-mapping>");

	    	

			writer.println("  <servlet>");
			writer.println("  		<description>This is the description of my J2EE component</description>");
			writer.println("  		<display-name>This is the display name of my J2EE component</display-name>");
			writer.println("  		<servlet-name>Delete"+ CapitalCase.toCapitalCase(className) + "Action</servlet-name>");
			writer.println("  		<servlet-class>com."+pack+".action.Delete"+ CapitalCase.toCapitalCase(className) + "Action</servlet-class>");
			writer.println("  </servlet>");
	    	writer.println("  <servlet-mapping>");
			writer.println("	   <servlet-name>Delete"+ CapitalCase.toCapitalCase(className) + "Action</servlet-name>");
			writer.println("	   <url-pattern>/Delete"+ CapitalCase.toCapitalCase(className) + "Action</url-pattern>");
	    	writer.println("  </servlet-mapping>");
	    	
			writer.println("  <servlet>");
			writer.println("  		<description>This is the description of my J2EE component</description>");
			writer.println("  		<display-name>This is the display name of my J2EE component</display-name>");
			writer.println("  		<servlet-name>UpdateView"+ CapitalCase.toCapitalCase(className) + "Action</servlet-name>");
			writer.println("  		<servlet-class>com."+pack+".action.UpdateView"+ CapitalCase.toCapitalCase(className) + "Action</servlet-class>");
			writer.println("  </servlet>");
	    	writer.println("  <servlet-mapping>");
			writer.println("	   <servlet-name>UpdateView"+ CapitalCase.toCapitalCase(className) + "Action</servlet-name>");
			writer.println("	   <url-pattern>/UpdateView"+ CapitalCase.toCapitalCase(className) + "Action</url-pattern>");
	    	writer.println("  </servlet-mapping>");


			writer.println("  <servlet>");
			writer.println("  		<description>This is the description of my J2EE component</description>");
			writer.println("  		<display-name>This is the display name of my J2EE component</display-name>");
			writer.println("  		<servlet-name>Update"+ CapitalCase.toCapitalCase(className) + "Action</servlet-name>");
			writer.println("  		<servlet-class>com."+pack+".action.Update"+ CapitalCase.toCapitalCase(className) + "Action</servlet-class>");
			writer.println("  </servlet>");
	    	writer.println("  <servlet-mapping>");
			writer.println("	   <servlet-name>Update"+ CapitalCase.toCapitalCase(className) + "Action</servlet-name>");
			writer.println("	   <url-pattern>/Update"+ CapitalCase.toCapitalCase(className) + "Action</url-pattern>");
	    	writer.println("  </servlet-mapping>");
	    	
	    	String cataLog = con.getCatalog();
			  rsf = databaseMetaData.getImportedKeys(cataLog, null, className);
			List<String> duplicateString = new ArrayList<String>();
			boolean fkFlag = false;
			String fkTableName = "";
			while (rsf.next()) {
				fkTableName = rsf.getString("PKTABLE_NAME");
				String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
				if (fkSchemaName.equalsIgnoreCase(schemaName)) {
					if (!duplicateString.contains(fkTableName) && fkSchemaName.equalsIgnoreCase(schemaName)) {
						fkFlag = true;
						break;
					}
					duplicateString.add(fkTableName);
					
				  System.out.println("getExportedKeys(): fkColumnName=" + rsf.getString("PKTABLE_CAT"));
				  System.out.println("getExportedKeys(): fkColumnName=" + rsf.getString("PKTABLE_SCHEM"));
				  System.out.println("getExportedKeys(): fkColumnName=" + rsf.getString("PKTABLE_NAME"));
				  System.out.println("getExportedKeys(): fkColumnName=" + rsf.getString("PKCOLUMN_NAME"));
				  System.out.println("getExportedKeys(): fkColumnName=" + rsf.getString("FKTABLE_CAT"));
				  System.out.println("getExportedKeys(): fkColumnName=" + rsf.getString("FKTABLE_SCHEM"));
				  System.out.println("getExportedKeys(): fkColumnName=" + rsf.getString("FKCOLUMN_NAME"));
				  System.out.println("getExportedKeys(): fkColumnName=" + rsf.getString("FKTABLE_NAME"));
				  System.out.println("getExportedKeys(): fkColumnName=" + rsf.getString("FKCOLUMN_NAME"));
				//String fkColumnName = rsf.getString("FKCOLUMN_NAME");
				}
			}
			if(fkFlag) {
				writer.println("  <servlet>");
				writer.println("  		<description>This is the description of my J2EE component</description>");
				writer.println("  		<display-name>This is the display name of my J2EE component</display-name>");
				writer.println("  		<servlet-name>Get"+CapitalCase.toCapitalCase(fkTableName)+"Add"+ CapitalCase.toCapitalCase(className) + "Action</servlet-name>");
				writer.println("  		<servlet-class>com."+pack+".action.Get"+CapitalCase.toCapitalCase(fkTableName)+"Add"+ CapitalCase.toCapitalCase(className) + "Action</servlet-class>");
				writer.println("  </servlet>");
		    	writer.println("  <servlet-mapping>");
				writer.println("	   <servlet-name>Get"+CapitalCase.toCapitalCase(fkTableName)+"Add"+ CapitalCase.toCapitalCase(className) + "Action</servlet-name>");
				writer.println("	   <url-pattern>/Get"+CapitalCase.toCapitalCase(fkTableName)+"Add"+ CapitalCase.toCapitalCase(className) + "Action</url-pattern>");
		    	writer.println("  </servlet-mapping>");
				fkFlag = false;
			}
	    	
					}break;
				}	
				
			}*/
	    	writer.println("  <welcome-file-list>");
	    	writer.println("  	<welcome-file>./home.jsp</welcome-file>");
	    	writer.println("  </welcome-file-list>");
	    	writer.println("</web-app>");
	    	writer.close();
	    	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
