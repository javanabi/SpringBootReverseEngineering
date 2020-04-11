package com.jdbc.main;

public class SqlDatatypeTOJavaWrapperClass {

	public static String toWrapperClass(String sqlDatatype) {
		if ("NUMBER".equals(sqlDatatype)) {
			return "Integer";
		} else if ("VARCHAR2".equals(sqlDatatype)) {
			return "String";
		} else if ("FLOAT".equals(sqlDatatype)) {
			return "Float";
		} else if ("CHAR".equals(sqlDatatype)) {
			return "Character";
		} else if ("LONG".equals(sqlDatatype)) {
			return "Long";
		} else if ("BLOB".equals(sqlDatatype)) {
			return "Blob";
		} else if ("INTEGER".equals(sqlDatatype)) {
			return "Integer";
		} else if ("DATE".equals(sqlDatatype)) {
			return "Date";
		} else {
			return "String";
		}
		
	}

	public static String toPrimitiveClass(String sqlDatatype) {
		if ("NUMBER".equals(sqlDatatype)) {
			return "Int";
		} else if ("VARCHAR2".equals(sqlDatatype)) {
			return "String";
		}  else if ("FLOAT".equals(sqlDatatype)) {
			return "Float";
		} else if ("CHAR".equals(sqlDatatype)) {
			return "Character";
		} else if ("LONG".equals(sqlDatatype)) {
			return "Long";
		} else if ("BLOB".equals(sqlDatatype)) {
			return "Blob";
		} else if ("INTEGER".equals(sqlDatatype)) {
			return "Intr";
		} else if ("DATE".equals(sqlDatatype)) {
			return "Date";
		} else {
			return "String";
		}
	}
	
	
	public static String toDateConversion(String sqlDatatype) {
		if ("DATE".equals(sqlDatatype)) {
			return "Date";
		} else {
			return "String";
		}
	}
	
	

}
