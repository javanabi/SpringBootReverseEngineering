package com.util.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import com.jdbc.main.CapitalCase;

public class InputNamesFileRead {

	public static Set<String> inputNames = readFields();

	public static Set<String> readFields() {
		inputNames = new HashSet();
		try {
			File file = new File("./database/inputnamefields.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String st;
			while ((st = br.readLine()) != null)
				inputNames.add(st.toLowerCase());
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
		return inputNames;
	}

	public static String getProperString(String columnName, Set<String> inputNames) {
		columnName = columnName.toLowerCase();
		if (inputNames.contains(columnName))
			return CapitalCase.toCapitalCase(columnName);
		else if (columnName.contains("name"))
			return getReplacedString(columnName, "name", " name ");
		if (columnName.contains("date"))
			return getReplacedString(columnName, "date", " date ");
		if (columnName.contains("days"))
			return getReplacedString(columnName, "days", " days ");
		if (columnName.contains("from"))
			return getReplacedString(columnName, "from", " days ");
		if (columnName.contains("to"))
			return getReplacedString(columnName, "to", " to ");
		if (columnName.contains("join"))
			return getReplacedString(columnName, "join", " join ");
		if (columnName.contains("present"))
			return getReplacedString(columnName, "present", " present ");
		if (columnName.contains("working"))
			return getReplacedString(columnName, "working", " working ");
		if (columnName.contains("number"))
			return getReplacedString(columnName, "number", " number ");
		if (columnName.contains("desc"))
			return getReplacedString(columnName, "desc", " desc ");
		if (columnName.contains("description"))
			return getReplacedString(columnName, "description", " description ");
		if (columnName.contains("type"))
			return getReplacedString(columnName, "type", " type ");
		if (columnName.contains("duration"))
			return getReplacedString(columnName, "duration", " duration ");
		if (columnName.contains("message"))
			return getReplacedString(columnName, "message", " message ");
		if (columnName.contains("semisters"))
			return getReplacedString(columnName, "semisters", " semisters ");
		if (columnName.contains("semister"))
			return getReplacedString(columnName, "semister", " semister ");
		if (columnName.contains("subjects"))
			return getReplacedString(columnName, "subjects", " subjects ");
		if (columnName.contains("subject"))
			return getReplacedString(columnName, "subject", " subject ");
		if (columnName.contains("details"))
			return getReplacedString(columnName, "details", " details ");
		if (columnName.contains("marks"))
			return getReplacedString(columnName, "marks", " marks ");
		if (columnName.contains("internal"))
			return getReplacedString(columnName, "internal", " internal ");
		if (columnName.contains("address"))
			return getReplacedString(columnName, "address", " address ");
		if (columnName.contains("grades"))
			return getReplacedString(columnName, "grades", " grades ");
		if (columnName.contains("leaves"))
			return getReplacedString(columnName, "leaves", " leaves ");
		if (columnName.contains("join"))
			return getReplacedString(columnName, "join", " join ");
		return CapitalCase.toCapitalCase(columnName);

	}

	public static String getReplacedString(String columnName, String actuvalString, String replacedString) {
		columnName = columnName.toLowerCase();
		columnName = columnName.replace(actuvalString, replacedString);
		String splitted[] = columnName.split("\\s+");
		String finalName = "";
		for (int i = 0; i < splitted.length; i++) {
			finalName = finalName + " " + CapitalCase.toCapitalCase(splitted[i].replace("_", "").trim());
		}
		return finalName.trim();
	}

	public static void main(String[] args) {

		String name = "branchsemisters";
		System.out.println(getProperString(name, inputNames));
	}

}
