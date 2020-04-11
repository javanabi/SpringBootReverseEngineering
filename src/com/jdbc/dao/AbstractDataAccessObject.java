package com.jdbc.dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class AbstractDataAccessObject
{
	private Connection mCon;
	public static Properties mProps;

	/**
	 * @return the props
	 */
	public Properties getProperties()
	{
		return mProps;
	}

	/**
	 * @param props
	 *            application properties object
	 */
	public void setProperties(Properties aProps)
	{
		mProps = aProps;
	}

	public Connection getConnection()
	{
		try
		{
			Properties aProps = getProperties();
			Class.forName(aProps.getProperty("driver"));
			mCon = DriverManager.getConnection(aProps.getProperty("url"), aProps.getProperty("duser"), aProps.getProperty("dpass"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mCon;
	}

 
}
