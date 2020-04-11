package com.jdbc.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * class for Date Convertions
 * 
 */
public class DateWrapper {
	static String month[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
			"Aug", "Sep", "Oct", "Nov", "Dec" };

	/** Creates a new instance of DateWrapper */
	public DateWrapper() {
	}

	@SuppressWarnings("deprecation")
	public static String parseDate(Date date) {
		int monthid = date.getMonth();
		String newdate = date.getDate() + "-" + month[monthid] + "-"
				+ (date.getYear() + 1900);
		System.out.println("new date==" + newdate);
		return newdate;
	}

	public static String parseDate(String date) {
		int monthid = Integer.parseInt(date.substring(date.indexOf("-") + 1,
				date.lastIndexOf("-")));
		String newdate = date.substring(0, date.indexOf("-")) + "-"
				+ month[monthid - 1] + "-"
				+ (date.substring(date.lastIndexOf("-") + 1, date.length()));
		return newdate;
	}

	public static String parseDate(java.sql.Date date) {
		String olddate = date.toString();
		String newdate = olddate.substring(olddate.lastIndexOf("-") + 1,
				olddate.length())
				+ "-"
				+ olddate.substring(olddate.indexOf("-") + 1, olddate
						.lastIndexOf("-"))
				+ "-"
				+ olddate.substring(0, olddate.indexOf("-"));
		return newdate;
	}
	
	

	public static java.util.Date convertFromSQLDateToJAVADate(
            java.sql.Date sqlDate) {
        java.util.Date javaDate = null;
        if (sqlDate != null) {
            javaDate = new Date(sqlDate.getTime());
        }
        return javaDate;
    }
	
	
	public static java.sql.Date convertFromJAVADateToSQLDate(Date javaDate) {
		java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime());
        return sqlDate;
    }

	
	
}
