package com.jdbc.main;

import com.jdbc.daoimpl.JdbcDAO;
import com.jdbc.fileread.JDBCFileRead;

public class CreateDAOLogic {
	public static void main(String[] args) {
		JDBCFileRead jdbcFileRead = new JDBCFileRead();
		jdbcFileRead.readFile();
		new JdbcDAO().readDataBaseDetails();
		System.out.println("Project created successfully");
	}
}
