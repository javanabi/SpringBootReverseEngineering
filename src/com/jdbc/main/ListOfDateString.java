package com.jdbc.main;

import java.util.ArrayList;
import java.util.List;

public class ListOfDateString {

	public static boolean checkDateString(String columnName, String className) {
		boolean flag = false;
		List<String> list = new ArrayList<String>();
		list.add("from");
		list.add("to");
		list.add("dob");
		for (String string : list) {
			flag = columnName.contains(string);
			if(flag)
				break;
		}

		return flag;
	}

}
