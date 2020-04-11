package com.css.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.jdbc.dao.AbstractDataAccessObject;
import com.jdbc.main.CapitalCase;
import com.jdbc.main.SqlDatatypeTOJavaWrapperClass;

public class CSSImpl extends AbstractDataAccessObject {

	PreparedStatement pstmt;
	String pack;
	String title;
	ResultSet resultSet, rs, rsf, tableTypes, contraintsRecords;
	String schemaName = getProperties().getProperty("duser");

	public CSSImpl(String pack, String title) {
		super();
		this.pack = pack;
		this.title = title;
	}

	public void createCSSFile1(Connection con) throws SQLException {
		try {

			String packageNameString = ".\\jsp\\css\\";
			File packageDir = new File(packageNameString);
			packageDir.mkdir();
			PrintWriter writer = new PrintWriter(packageNameString + title + ".css");
			writer.println(".Title {");
			writer.println("    font-family: Optima, sans-serif;");
			writer.println("    font-weight: bold;");
			writer.println("    font-size: 12pt;");
			writer.println("    color: green;");
			writer.println("}");
			writer.println("");
			writer.println(".Title1 {");
			writer.println("	font-family: Verdana;");
			writer.println("	font-weight: bold;");
			writer.println("	font-size: 8pt");
			writer.println("}");
			writer.println("");
			writer.println("input[type=text], input[type=password] {");
			writer.println("	height: 25px;");
			writer.println("	padding: 12px 20px;");
			writer.println("	margin: 8px 0;");
			writer.println("	display: inline-block;");
			writer.println("	border: 1px solid #ccc;");
			writer.println("	box-sizing: border-box;");
			writer.println("	border-radius: 15px;");
			writer.println("}");
			writer.println("");
			writer.println("textarea {");
			writer.println("	height: 100px;");
			writer.println("	width: 300px;");
			writer.println("	padding: 12px 20px;");
			writer.println("	margin: 8px 0;");
			writer.println("	display: inline-block;");
			writer.println("	border: 1px solid #ccc;");
			writer.println("	box-sizing: border-box;");
			writer.println("	border-radius: 15px;");
			writer.println("}");
			writer.println("");
			writer.println("select {");
			writer.println("	height: 25px;");
			writer.println("	width: 75%;");
			writer.println("	margin: 8px 0;");
			writer.println("	display: inline-block;");
			writer.println("	border: 1px solid #ccc;");
			writer.println("	box-sizing: border-box;");
			writer.println("	border-radius: 15px;");
			writer.println("	outline: none !important;");
			writer.println("	border: 1px solid #4CAF50;");
			writer.println("	box-shadow: 0 0 10px #719ECE;");
			writer.println("	margin: 8px 0;");
			writer.println("}");
			writer.println("");
			writer.println("input[type=file] {");
			writer.println("	height: 25px;");
			writer.println("	width: 75%;");
			writer.println("	margin: 8px 0;");
			writer.println("	display: inline-block;");
			writer.println("	border: 1px solid #ccc;");
			writer.println("	box-sizing: border-box;");
			writer.println("	border-radius: 15px;");
			writer.println("	margin: 8px 0;");
			writer.println("}");
			writer.println("");
			writer.println("input:focus {");
			writer.println("	outline: none !important;");
			writer.println("	border: 1px solid #4CAF50;");
			writer.println("	box-shadow: 0 0 10px #719ECE;");
			writer.println("}");
			writer.println("");
			writer.println("input[type=submit] {");
			writer.println("	background-color: #4CAF50;");
			writer.println("	color: white;");
			writer.println("	padding: 14px 20px;");
			writer.println("	margin: 8px 0;");
			writer.println("	border: none;");
			writer.println("	cursor: pointer;");
			writer.println("	border-radius: 15px;");
			writer.println("}");
			writer.println("");
			writer.println("input[type=reset] {");
			writer.println("	color: white;");
			writer.println("	padding: 14px 20px;");
			writer.println("	margin: 8px 0;");
			writer.println("	border: none;");
			writer.println("	background-color: #453858;");
			writer.println("	border-radius: 14px;");
			writer.println("}");
			writer.println("");
			writer.println("#register {");
			writer.println("	color: #453858;");
			writer.println("	font-weight: bold;");
			writer.println("}");
			writer.println("");
			writer.println(".container {");
			writer.println("	padding: 0 500px 0 550px;");
			writer.println("	color: 433E4A;");
			writer.println("	font-family: courier;");
			writer.println("}");
			writer.println("");
			writer.println("span.psw {");
			writer.println("	float: right;");
			writer.println("	padding-top: 16px;");
			writer.println("}");
			writer.println("");
			writer.println("button {");
			writer.println("	background-color: #4CAF50;");
			writer.println("	color: white;");
			writer.println("	padding: 14px 20px;");
			writer.println("	margin: 8px 0;");
			writer.println("	border: none;");
			writer.println("	cursor: pointer;");
			writer.println("	width: 100%;");
			writer.println("}");
			writer.println("");
			writer.println("button:hover {");
			writer.println("	opacity: 0.8;");
			writer.println("}");
			writer.println("");
			writer.println(" table {");
			writer.println("	border-collapse: collapse;");
			writer.println("}");
			writer.println("");
			writer.println("th, td {");
			writer.println("	text-align: left;");
			writer.println("	padding: 8px;");
			writer.println("}");
			writer.println("");
			writer.println("tr:nth-child(even) {");
			writer.println("	background-color: #f2f2f2;");
			writer.println("}");
			writer.println("");
			writer.println("th {");
			writer.println("	background-color: #4CAF50;");
			writer.println("	color: white;");
			writer.println("}");
			writer.println(".page-link {");
			writer.println("	position: relative;");
			writer.println("	display: block;");
			writer.println("	padding: .5rem .75rem;");
			writer.println("	margin-left: -1px;");
			writer.println("	line-height: 1.25;");
			writer.println("	color: #0275d8;");
			writer.println("	background-color: #fff;");
			writer.println("	border: 0px solid #ddd; ");
			writer.println("}");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}