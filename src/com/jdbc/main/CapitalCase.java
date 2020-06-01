package com.jdbc.main;

import java.sql.ResultSetMetaData;

public class CapitalCase {

	public static String toCapitalCase(String name) {
		try {
			return name.toLowerCase().substring(0, 1).toUpperCase() + name.substring(1).toLowerCase().replace("_", "");
		} catch (Exception e) {
			return "";
		}
	}
	 

	public static String toMultiCapitalCase(String name) {
		return name.toLowerCase().substring(0, 1).toUpperCase() + name.substring(1).toLowerCase().replace("_", "");
	}

	public static String toBeanClass(String name) {
		return name.toLowerCase().substring(0, 1).toUpperCase() + name.substring(1).toLowerCase().replace("_", "")
				+ "TO";
	}

	public static String replaceUnderScore(String name) {
		return name.toLowerCase().substring(0, 1).toUpperCase() + name.substring(1).toLowerCase().replace("_", "");
	}
	
	public static String getFirstCharecter(String name) {
		return name.substring(0, 1).toLowerCase();
	}
	
	public static String inputType(String name) {
		return name.contains("password") ? "password" : "text";
	}
	
	public static String sortColumnName(ResultSetMetaData columnMetaData) {
		String nameCol = "";
		try {
			int columnCount = columnMetaData.getColumnCount();
			for (int i = 2; i <= columnCount; i++) {
				String colName = columnMetaData.getColumnName(i);
				if (colName.contains("name")) {
					nameCol = colName;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nameCol;
	}
	
	public static String displayColNameASTableHeadColumns(String colName,String tableName) {
		colName = colName.toLowerCase();
		tableName = tableName.toLowerCase();
		if(colName.contains(tableName)) {
			colName = colName.replace(tableName, "");
			return  colName.substring(0, 1).toUpperCase()+colName.substring(1).toLowerCase();
		}
		else if(tableName.endsWith("s")) {
			tableName = tableName.substring(0,tableName.length()-1);
			colName = colName.replace(tableName, "");
			return colName.substring(0, 1).toUpperCase()+colName.substring(1).toLowerCase();
		}
		else if(tableName.endsWith("es")) {
			tableName = tableName.substring(0,tableName.length()-2);
			colName = colName.replace(tableName, "");
			return colName.substring(0, 1).toUpperCase()+colName.substring(1).toLowerCase();
		}
		else
			return toCapitalCase(colName);
			
	}

}
