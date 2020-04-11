package com.springboot.project.properties;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.jdbc.dao.AbstractDataAccessObject;

public class ReadProjectPropertiesFile {
	
	public static Properties projectProps = readProjectPropertiesFile(); 
	AbstractDataAccessObject dobject;
	public void readFile() {

		Properties props = null;
		try {
			InputStream fis = new FileInputStream("./database/springboot-project.properties");
			props = new Properties();
			props.load(fis);
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
		dobject = new AbstractDataAccessObject();
		dobject.setProperties(props);
	}
	
	
	
	public static Properties readProjectPropertiesFile() {
		try {
			InputStream fis = new FileInputStream("./database/springboot-project.properties");
			projectProps = new Properties();
			projectProps.load(fis);
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
		return projectProps;
	}
}
