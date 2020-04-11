package com.jdbc.main;

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

}
