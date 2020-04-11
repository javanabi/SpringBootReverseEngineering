package com.jdbc.fileread;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.jdbc.dao.AbstractDataAccessObject;

public class JDBCFileRead {
	AbstractDataAccessObject dobject;
	public void readFile() {

		Properties props = null;
		try {
			InputStream fis = new FileInputStream("./database/database.properties");
			props = new Properties();
			props.load(fis);
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
		dobject = new AbstractDataAccessObject();
		dobject.setProperties(props);
	}
}
