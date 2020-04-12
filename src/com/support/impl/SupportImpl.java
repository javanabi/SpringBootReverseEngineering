package com.support.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import com.jdbc.dao.AbstractDataAccessObject;
import com.jdbc.main.CapitalCase;
import com.jdbc.main.SqlDatatypeTOJavaWrapperClass;
import com.springboot.project.properties.ReadProjectPropertiesFile;

public class SupportImpl {

	String pack;
	
	public SupportImpl(String pack) {
		this.pack = pack;
	}

	public void createSupportImplClasses(String resourcePackage) throws SQLException {
		try {
			
			File title = new File(ReadProjectPropertiesFile.projectProps.getProperty("title"));
			if(!title.exists())
				title.mkdir();
			
			File packageDir = new File(".\\sbsupport\\project");
			File destDir = new File(".\\"+title);
			FileUtils.copyDirectory(packageDir, destDir);
			
			File srcProperties = new File(".\\sbapplicationproperties\\");
			File destProperties = new File(resourcePackage);
			FileUtils.copyDirectory(srcProperties, destProperties);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		
//		try {
//			File packageDir = new File(".\\actionsupport");
//			File destDir = new File(".\\com\\"+pack+"\\action");
//			FileUtils.copyDirectory(packageDir, destDir);
//
//		} catch (Exception e) {
//			System.out.println(e);
//		}
		
	}
}
