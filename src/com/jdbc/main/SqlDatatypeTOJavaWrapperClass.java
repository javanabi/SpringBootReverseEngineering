package com.jdbc.main;

import com.springboot.project.properties.ReadProjectPropertiesFile;

public class SqlDatatypeTOJavaWrapperClass {

	public static String toWrapperClass(String sqlDatatype) {
		String database = ReadProjectPropertiesFile.projectProps.getProperty("database-name");
		if ("Oracle".equalsIgnoreCase(database)) {
			if ("NUMBER".equals(sqlDatatype)) {
				return "Integer";
			} else if ("VARCHAR2".equalsIgnoreCase(sqlDatatype)) {
				return "String";
			} else if ("FLOAT".equalsIgnoreCase(sqlDatatype)) {
				return "Float";
			} else if ("CHAR".equalsIgnoreCase(sqlDatatype)) {
				return "Character";
			} else if ("LONG".equalsIgnoreCase(sqlDatatype)) {
				return "Long";
			} else if ("BLOB".equalsIgnoreCase(sqlDatatype)) {
				return "Blob";
			} else if ("INTEGER".equalsIgnoreCase(sqlDatatype)) {
				return "Integer";
			} else if ("DATE".equalsIgnoreCase(sqlDatatype)) {
				return "Date";
			} else if ("BIGINT".equalsIgnoreCase(sqlDatatype)) {
				return "Long";
			} else if ("BIT".equalsIgnoreCase(sqlDatatype)) {
				return "boolean";
			} else if ("Int".equalsIgnoreCase(sqlDatatype)) {
				return "Long";
			} else {
				return "String";
			}
		}

		if ("mysql".equalsIgnoreCase(database)) {
			if ("NUMBER".equals(sqlDatatype)) {
				return "Integer";
			} else if ("VARCHAR2".equalsIgnoreCase(sqlDatatype)) {
				return "String";
			} else if ("FLOAT".equalsIgnoreCase(sqlDatatype)) {
				return "Float";
			} else if ("CHAR".equalsIgnoreCase(sqlDatatype)) {
				return "Character";
			} else if ("LONG".equalsIgnoreCase(sqlDatatype)) {
				return "Long";
			} else if ("BLOB".equalsIgnoreCase(sqlDatatype)) {
				return "Blob";
			} else if ("LONGBLOB".equalsIgnoreCase(sqlDatatype)) {
				return "byte[]";
			} else if ("INTEGER".equalsIgnoreCase(sqlDatatype)) {
				return "Integer";
			} else if ("DATE".equalsIgnoreCase(sqlDatatype) || "TIMESTAMP".equalsIgnoreCase(sqlDatatype)) {
				return "Date";
			} else if ("BIGINT".equalsIgnoreCase(sqlDatatype)) {
				return "Long";
			} else if ("BIT".equalsIgnoreCase(sqlDatatype)) {
				return "boolean";
			} else if ("Int".equalsIgnoreCase(sqlDatatype)) {
				return "Long";
			} else {
				return "String";
			}
		}
		return "String";
	}

	public static String toPrimitiveClass(String sqlDatatype) {
		if ("NUMBER".equals(sqlDatatype)) {
			return "Int";
		} else if ("VARCHAR2".equalsIgnoreCase(sqlDatatype)) {
			return "String";
		} else if ("FLOAT".equalsIgnoreCase(sqlDatatype)) {
			return "Float";
		} else if ("CHAR".equalsIgnoreCase(sqlDatatype)) {
			return "Character";
		} else if ("LONG".equalsIgnoreCase(sqlDatatype)) {
			return "Long";
		} else if ("BLOB".equalsIgnoreCase(sqlDatatype)) {
			return "Blob";
		} else if ("INTEGER".equalsIgnoreCase(sqlDatatype)) {
			return "Int";
		} else if ("DATE".equalsIgnoreCase(sqlDatatype)) {
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
