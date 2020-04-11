package com.jsp.impl;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jdbc.dao.AbstractDataAccessObject;
import com.jdbc.main.CapitalCase;
import com.jdbc.main.ListOfDateString;
import com.jdbc.main.SqlDatatypeTOJavaWrapperClass;
import com.util.impl.InputNamesFileRead;

public class JspImpl extends AbstractDataAccessObject {

	PreparedStatement pstmt;
	String pack;
	String title;
	ResultSet resultSet, rs, rsf, tableTypes, contraintsRecords;
	Set<String> inputNames = InputNamesFileRead.inputNames;
	public JspImpl(String pack, String title) {
		super();
		this.pack = pack;
		this.title = title;
	}

	public boolean isForeignKey(String tableName, String columnName, DatabaseMetaData databaseMetaData, String cataLog,
			Map<String, Map<String, String>> listOfForeignKeys) {
		boolean flag = false;
		try {

			for (String tName : listOfForeignKeys.keySet()) {
				Map<String, String> foriegnKeyMap = listOfForeignKeys.get(tName);
				for (String fKeys : foriegnKeyMap.keySet()) {
					if (foriegnKeyMap.get(fKeys).equalsIgnoreCase(columnName) && tableName.equalsIgnoreCase(fKeys)) {
						flag = true;
						break;
					}
				}
				if (flag)
					break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;

	}

	public boolean isPrimaryKey(String tableName, String columnName, DatabaseMetaData databaseMetaData,
			String cataLog) {
		boolean flag = false;
		try {
			contraintsRecords = databaseMetaData.getExportedKeys(cataLog, null, tableName);

			while (contraintsRecords.next()) {
				if (columnName.equalsIgnoreCase(contraintsRecords.getString("PKCOLUMN_NAME"))) {
					flag = true;
					break;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;

	}

	public List<String> getForeignKey(String tableName, Connection con) {
		List<String> list = new ArrayList<String>();
		try {
			contraintsRecords = con.getMetaData().getExportedKeys(con.getCatalog(), null, tableName);
			while (contraintsRecords.next()) {
				list.add(contraintsRecords.getString("FKCOLUMN_NAME"));
				/*
				 * String fkTableName = contraintsRecords.getString("FKTABLE_NAME"); String
				 * fkColumnName = contraintsRecords.getString("FKCOLUMN_NAME"); int fkSequence =
				 * contraintsRecords.getInt("KEY_SEQ");
				 * System.out.println("getExportedKeys(): fkTableName=" + fkTableName);
				 * System.out.println("getExportedKeys(): fkColumnName=" + fkColumnName);
				 * System.out.println("getExportedKeys(): fkSequence=" + fkSequence);
				 * 
				 * String pkTableName = contraintsRecords.getString("PKTABLE_NAME"); String
				 * pkColumnName = contraintsRecords.getString("PKCOLUMN_NAME"); int pkSequence =
				 * contraintsRecords.getInt("KEY_SEQ");
				 * System.out.println("getExportedKeys(): pkTableName=" + pkTableName);
				 * System.out.println("getExportedKeys(): pkColumnName=" + pkColumnName);
				 * System.out.println("getExportedKeys(): pkSequence=" + pkSequence);
				 */
			}
			// System.out.println("Foreign Keys:"+list);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public Map<String, Map<String, String>> getAllForiegnKeys(Connection con, DatabaseMetaData databaseMetaData,
			Map<String, Map<String, String>> outerMap, String tableName) {
		try {
			String catalog = con.getCatalog();
			Map<String, String> innerMap = new HashMap<String, String>();
			try {
				contraintsRecords = databaseMetaData.getExportedKeys(catalog, null, tableName);
				while (contraintsRecords.next()) {
					innerMap.put(contraintsRecords.getString("FKTABLE_NAME"),
							contraintsRecords.getString("FKCOLUMN_NAME"));
				}
				outerMap.put(tableName, innerMap);
				// System.out.println("Foreign Keys:"+list);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outerMap;
	}

	public void createJSPImplClasses(String className, Connection con,
			Map<String, Map<String, String>> listOfForeignKeys) throws SQLException {
		try {
			pstmt = con.prepareStatement("select * from " + className);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();
			File packageDir = new File(".\\jsp");
			packageDir.mkdir();

			PrintWriter writer = new PrintWriter(".\\jsp" + "\\Add" + CapitalCase.toCapitalCase(className) + ".jsp");

			writer.println("<%@ page language=\"java\" import=\"java.util.*\" pageEncoding=\"ISO-8859-1\"%>");
			writer.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
			writer.println("<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>");
			writer.println("<html>");
			writer.println("	<head>");

			writer.println("		<title>" + title + "</title>");
			writer.println("		<link rel=\"icon\" href=\"./images/"+ title +"_favicon.png\" />");
			writer.println(" 	");
			writer.println("");
			writer.println("		<script language=\"JavaScript\" src=\"<%=request.getContextPath() + \"/js/formvalidator.js\"%>\" type=\"text/javascript\"></script>");
			writer.println("		<script language=\"JavaScript\" src=\"<%=request.getContextPath() + \"/js/date_picker.js\"%>\" type=\"text/javascript\"></script>");
			writer.println("<link rel = \"stylesheet\"  type=\"text/css\"  href=\"./css/"+title+".css\" />");
			writer.println("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">");
			writer.println("	</head>");
			writer.println("");
			
			writer.println("	<body>");
			writer.println("<div class=\"container-fluid\">");
			writer.println("		<div class=\"row\">");
			writer.println("			<div class=\"col-xs-6 col-md-2\"><jsp:include page=\"TopLeft.jsp\"/></div>");
			writer.println("			<div class=\"col-xs-6 col-md-8\"><jsp:include page=\"TopMiddle.jsp\"/></div>");
			writer.println("			<div class=\"col-xs-6 col-md-2\"><jsp:include page=\"TopRight.jsp\"/></div>");
			writer.println("		</div>");
			writer.println("		<div class=\"row\">");
			writer.println("			<div class=\"col-xs-6 col-md-2\"><jsp:include page=\"MidLeft.jsp\"/></div>");
			writer.println("			<div class=\"col-xs-6 col-md-8\">");
			writer.println("");
			writer.println("");
			writer.println("					<jsp:include page=\"header.jsp\"></jsp:include>");
			writer.println("<form action=\"./Add" + CapitalCase.toCapitalCase(className)
					+ "Action\" method=\"post\" name=\"" + CapitalCase.toCapitalCase(className).toLowerCase() + "\"");
			writer.println("onsubmit='return validate()'>");
			writer.println("<center>");
			writer.println("	<h2>");
			writer.println("			<font color='#sharew'>Add " + InputNamesFileRead.getProperString(className,inputNames) + "</font>");
			writer.println("	</h2>");
			writer.println("	</center>");
			writer.println("	<pre>");
			writer.println("<table align='center'>");
			// for loop
			int columnCount = columnMetaData.getColumnCount();
			List<String> fkConstraints = getForeignKey(className, con);
			DatabaseMetaData databaseMetaData = con.getMetaData();
			String cataLog = con.getCatalog();
			for (int i = 1; i <= columnCount; i++) {
				if ((i == 1) && !isForeignKey(className, columnMetaData.getColumnName(i), databaseMetaData, cataLog,
						listOfForeignKeys)) {
					continue;
				} else if (SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i))
						.equals("Int")
						&& isForeignKey(className, columnMetaData.getColumnName(i), databaseMetaData, cataLog,
								listOfForeignKeys)) {
					String fkTableName = getFKTableName(listOfForeignKeys, className, columnMetaData.getColumnName(i));
					writer.println("        	<tr>");
					writer.println("                <td align='right'>");
					writer.println(
							"        		" + InputNamesFileRead.getProperString(getFKTableColumnName(con,fkTableName,2).toLowerCase(),inputNames) + " :");
					writer.println("                </td>");
					writer.println("                <td>");
					writer.println("                        <select name=\'"
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
							+ "' onChange='cleartext()'>");
					writer.println("                               <option value='select' selected='true'>");
					writer.println("                               Select ");
					writer.println("                               </option>");
					writer.println("<c:forEach var=\"" + CapitalCase.replaceUnderScore(fkTableName).toLowerCase() + "\" items=\"${"
							+ CapitalCase.toBeanClass(fkTableName).toLowerCase() + "list}\">");
					writer.println(" <option value='${" + CapitalCase.replaceUnderScore(fkTableName).toLowerCase() + "."+getFKTableColumnName(con,fkTableName,1).toLowerCase()+ "}'>");
					writer.println(" ${"	+ CapitalCase.toCapitalCase(fkTableName).toLowerCase() +"."+getFKTableColumnName(con,fkTableName,2).toLowerCase()+ "}");
					writer.println("                               </option>");
					writer.println("</c:forEach>");
					/*
					 * writer.println("                               <option value='student'>");
					 * writer.
					 * println("                               <font size='3' face='Verdana'>Student</font>"
					 * ); writer.println("                               </option>");
					 * writer.println("                               </option>");
					 * writer.println("                               <option value='moderator'>");
					 * writer.
					 * println("                               <font size='3' face='Verdana'>Moderator</font>"
					 * ); writer.println("                               </option>");
					 */
					writer.println("                         </select>");
				} else if (SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i)).equals(
						"Int") && isPrimaryKey(className, columnMetaData.getColumnName(i), databaseMetaData, cataLog))
					continue;
				else if ((i==1 || i==2) && SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i)).equals(
						"Int") && !isForeignKey(className, columnMetaData.getColumnName(i), databaseMetaData, cataLog,
								listOfForeignKeys))
					continue;
				else if (CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase().equals("password")) {
					writer.println("        	<tr>");
					writer.println("                <td align='right'>");
					writer.println(
							"        		" + CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)) + " :");
					writer.println("                </td>");
					writer.println("                <td align='right'>");
					writer.println("                            <input type='password' value='' name="
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase() + ">");
				} else if (SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i))
						.equals("Date")) {
					if (ListOfDateString.checkDateString(columnMetaData.getColumnName(i).toLowerCase(), className)) {
						writer.println("        	<tr>");
						writer.println("                <td align='right'>");
						writer.println(
								"        		" + InputNamesFileRead.getProperString(columnMetaData.getColumnName(i),inputNames) + " :");
						writer.println("                </td>");
						writer.println("                <td align='right'>");
						writer.println("        	<input type=\"text\" name=\""
								+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
								+ "\" size=\"20\" value=\"\"");
						writer.println("        			readonly=\"readonly\" /><a");
						writer.println("        						href=\"javascript:show_calendar('document."
								+ CapitalCase.toCapitalCase(className).toLowerCase() + ".dob', document."
								+ CapitalCase.toCapitalCase(className).toLowerCase() + ".dob.value);\">");
						writer.println("        	<img src=\"<%=request.getContextPath() + \"/images/cal.gif\"%>\"");
						writer.println("        	alt=\"cal\" width=\"18\" height=\"18\" border=\"0\" />");
						writer.println("        	</a>");

					} else {

					}
				} else if("VARCHAR2".equalsIgnoreCase(columnMetaData.getColumnTypeName(i)) && columnMetaData.getPrecision(i)>150){

					writer.println("        	<tr>");

					writer.println("                <td align='right'>");
					writer.println("        		" + InputNamesFileRead.getProperString(columnMetaData.getColumnName(i),inputNames) + " :");
					writer.println("                </td>");
					writer.println("                <td align='right'>");
					writer.println("                            <textarea value='' name='"
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase() + "'></textarea>");
				}else {
					writer.println("        	<tr>");

					writer.println("                <td align='right'>");
					writer.println(
							"        		" + InputNamesFileRead.getProperString(columnMetaData.getColumnName(i),inputNames) + " :");
					writer.println("                </td>");
					writer.println("                <td align='right'>");
					writer.println("                            <input type='text' value='' name='"
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase() + "'>");
				}
				writer.println("                </td>");
				writer.println("            </tr>");

			}
			writer.println("			        <tr align='center'>");
			writer.println("                          <td>");
			writer.println("                             <input type='submit' value='Submit' name='submit'>");
			writer.println("                             <input type='reset' value='Clear' name='clear'>");
			writer.println("                         </td>");
			writer.println("                    </tr>");
			writer.println("                </table>");
			writer.println("<input type='hidden' name='username' value='<%=session.getAttribute(\"user\")%>'>");
			writer.println("</pre>");
			writer.println("</form>");

			writer.println("<script language='JavaScript' type='text/javascript'>");
			writer.println("var frmvalidator = new Validator(\"" + CapitalCase.toCapitalCase(className).toLowerCase()
					+ "\");");
			columnCount = columnMetaData.getColumnCount();
			for (int i = 2; i <= columnCount; i++) {
				if("DATE".equals(columnMetaData.getColumnTypeName(i)) && !ListOfDateString.checkDateString(columnMetaData.getColumnName(i),className)) {
					/*writer.println("			frmvalidator.addValidation('"
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
							+ "', 'req','Please enter "
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase() + "');");*/
				}else if("NUMBER".equals(columnMetaData.getColumnTypeName(i))) {
					if (SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i))
							.equals("Int")
							&& isForeignKey(className, columnMetaData.getColumnName(i), databaseMetaData, cataLog,
									listOfForeignKeys)) {
					writer.println("			frmvalidator.addValidation('"
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
							+ "', 'dontselect=0');");
					}else {
						writer.println("			frmvalidator.addValidation('"
								+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
								+ "', 'req','Please enter "
								+ InputNamesFileRead.getProperString(columnMetaData.getColumnName(i),inputNames) + "');");
						writer.println("			frmvalidator.addValidation('"
								+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
								+ "', 'numeric');");
					}
				}else if("email".equalsIgnoreCase(columnMetaData.getColumnName(i))) {
					writer.println("			frmvalidator.addValidation('"+CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()+"', 'maxlen=50');");
					writer.println("			frmvalidator.addValidation('"+CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()+"', 'req');");
					writer.println("			frmvalidator.addValidation('"+CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()+"', 'email');");
				}else if("phoneNo".equalsIgnoreCase(columnMetaData.getColumnName(i))) {
					writer.println("			frmvalidator.addValidation('"+CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()+"', 'maxlen=50');");
					writer.println("			frmvalidator.addValidation('"+CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()+"', 'numeric');");
					writer.println("			frmvalidator.addValidation('"+CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()+"', 'Phone');");
				}else {
				writer.println("			frmvalidator.addValidation('"
						+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
						+ "', 'req','Please enter "
						+ InputNamesFileRead.getProperString(columnMetaData.getColumnName(i),inputNames) + "');");
				writer.println("			frmvalidator.addValidation('"
						+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
						+ "', 'maxlen="+columnMetaData.getPrecision(i)+"','Max length for "
						+ InputNamesFileRead.getProperString(columnMetaData.getColumnName(i),inputNames) + " is "+columnMetaData.getPrecision(i)+"');");
				if(columnMetaData.getPrecision(i)<150)
				writer.println("			frmvalidator.addValidation('"
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
							+ "', 'alpha','"+ InputNamesFileRead.getProperString(columnMetaData.getColumnName(i),inputNames) + " Alphabetic chars only');");
					
				}
			}
			writer.println("</script>");
			writer.println("		<jsp:include page='footer.jsp'></jsp:include>");
			writer.println("		</div>");
			writer.println("			<div class=\"col-xs-6 col-md-2\"><jsp:include page=\"MidRight.jsp\"/></div>");
			writer.println("		</div>");
			writer.println("		<div class=\"row\">");
			writer.println("			<div class=\"col-xs-6 col-md-2\"><jsp:include page=\"FooterLeft.jsp\"/></div>");
			writer.println("			<div class=\"col-xs-6 col-md-8\"><jsp:include page=\"FooterMiddle.jsp\"/></div>");
			writer.println("			<div class=\"col-xs-6 col-md-2\"><jsp:include page=\"FooterRight.jsp\"/></div>");
			writer.println("		</div>");
			writer.println("	</div>");
			writer.println("	</body>");
			writer.println("</html>");
			writer.close();

			writer = new PrintWriter(".\\jsp" + "\\View" + CapitalCase.toCapitalCase(className) + ".jsp");

			writer.println("<%@ page language=\"java\" import=\"java.util.*\" pageEncoding=\"ISO-8859-1\"%>");
			writer.println("<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>");
			writer.println("<%");
			writer.println("String path = request.getContextPath();");
			writer.println(
					"String basePath = request.getScheme()+\"://\"+request.getServerName()+\":\"+request.getServerPort()+path+\"//\";");
			writer.println("%>");

			writer.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
			writer.println("	<html>");
			writer.println("<head>");
			writer.println("		<title>" + title + "</title>");
			writer.println("		<link rel=\"icon\" href=\"./images/"+ title +"_favicon.png\" />");
			writer.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css\">");
			writer.println("<link rel = \"stylesheet\"  type=\"text/css\"  href=\"./css/"+title+".css\" />");
			writer.println("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">");
			/*
			 * writer.println("<%");
			 * writer.println("	if (session.getAttribute(\"user\") == null) {");
			 * writer.println("       RequestDispatcher rd = request");
			 * writer.println("            .getRequestDispatcher(\"/loginform.jsp\");");
			 * writer.println("        rd.forward(request, response);");
			 * writer.println("	}"); writer.println("%>");
			 */
			writer.println("</head>");
			writer.println("  ");
			writer.println("  <body>");
			writer.println("<div class=\"container-fluid\">");
			writer.println("		<div class=\"row\">");
			writer.println("			<div class=\"col-xs-6 col-md-2\"><jsp:include page=\"TopLeft.jsp\"/></div>");
			writer.println("			<div class=\"col-xs-6 col-md-8\"><jsp:include page=\"TopMiddle.jsp\"/></div>");
			writer.println("			<div class=\"col-xs-6 col-md-2\"><jsp:include page=\"TopRight.jsp\"/></div>");
			writer.println("		</div>");
			writer.println("		<div class=\"row\">");
			writer.println("			<div class=\"col-xs-6 col-md-2\"><jsp:include page=\"MidLeft.jsp\"/></div>");
			writer.println("			<div class=\"col-xs-6 col-md-8\">");
			writer.println("    ");
			writer.println("  	  <jsp:include page=\"header.jsp\"></jsp:include>");

			writer.println("<form action='.\\Delete" + CapitalCase.toCapitalCase(className)
					+ "Action' method='post'>\n");
			writer.println("	<center>");
			writer.println("	<h2>");
			writer.println("			<font color='#sharew'>View " + InputNamesFileRead.getProperString(className,inputNames) + "</font>");
			writer.println("	</h2></center>");
			writer.println("<table align=\"center\" border=\"1\">");
			writer.println("   <tr align=\"center\">");

			writer.println("              <td align=\"center\"><b></b></td>");
			columnCount = columnMetaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				writer.println();
				//writer.println("		+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)) + 
				//writer.println("              <td align=\"center\"><b>"
					//	+ InputNamesFileRead.getProperString(getFKTableColumnName(con,className,i).toLowerCase(),inputNames) + "</b></td>");
				String fkTableName = getFKTableName(listOfForeignKeys, className, columnMetaData.getColumnName(i));
				if (i==1 && isForeignKey(className, columnMetaData.getColumnName(i), databaseMetaData, cataLog,
						listOfForeignKeys)) {
					writer.println("              <td align=\"center\"><b>"+InputNamesFileRead.getProperString(getFKTableColumnName(con,fkTableName,2).toLowerCase(),inputNames)+"</b></td>");
				} 
				else if (isForeignKey(className, columnMetaData.getColumnName(i), databaseMetaData, cataLog,
								listOfForeignKeys)) {
					writer.println("              <td align=\"center\"><b>"+InputNamesFileRead.getProperString(getFKTableColumnName(con,fkTableName,2).toLowerCase(),inputNames)+"</b></td>");
				} 
				else {
					if(i!=1)
						writer.println("              <td align=\"center\"><b>"+InputNamesFileRead.getProperString(columnMetaData.getColumnName(i),inputNames)+"</b></td>");
						
				}
			}

			String ref = CapitalCase.toCapitalCase(className).toLowerCase() + "TO";

			writer.println("           <c:forEach   var=\"" + ref + "\"  items=\"${"
					+ CapitalCase.toBeanClass(className).toLowerCase() + "list}\" >");
			writer.println("        <tr> ");
			writer.println("           <td><input type='checkbox' name='"
					+ columnMetaData.getColumnName(1).toLowerCase() + "' value='${" + ref + "."
					+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(1)).toLowerCase() + "}'><a href='.\\UpdateView"+ CapitalCase.toCapitalCase(className)
					+ "Action?" + columnMetaData.getColumnName(1).toLowerCase() + "=${" + ref + "."
					+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(1)).toLowerCase() + "}'><i class=\"fa fa-edit\"></i></a><a href='.\\Delete"+ CapitalCase.toCapitalCase(className)
					+ "Action?" + columnMetaData.getColumnName(1).toLowerCase() + "=${" + ref + "."
					+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(1)).toLowerCase() + "}'><i class=\"fa fa-trash\"></i></a></td>");
			boolean flag = true;
			
			ResultSet rsf1 = databaseMetaData.getImportedKeys(cataLog, null, className);
			Map<Integer,String> listPK = new HashMap<Integer,String>();
			Set<String> setPK = new HashSet<String>();
			int j = 1;
			while (rsf1.next()) {
					if(setPK.contains(rsf1.getString("PKTABLE_NAME"))) {
						listPK.put(j,rsf1.getString("PKTABLE_NAME"));
						j++;
					}
						setPK.add(rsf1.getString("PKTABLE_NAME"));
			}
			rsf1.close();
			for (int i = 1; i <= columnCount; i++) {
				/*if (SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i)).equals("String")
						&& flag) {
					flag = false;
					writer.println("           <td><a href='.\\UpdateView" + CapitalCase.toCapitalCase(className)
							+ "Action?" + columnMetaData.getColumnName(1).toLowerCase() + "=${" + ref + "."
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(1)).toLowerCase() + "}'>${" + ref
							+ "." + CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
							+ "}</a></td>");*/
					//writer.println("           <td>${" + ref
				//	+ "." + CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
				//	+ "}</td>");
				if(listPK.size()!=0 && (i==1 || isForeignKey(className, columnMetaData.getColumnName(i), databaseMetaData, cataLog, listOfForeignKeys))) {
					for (String string : setPK) {
					writer.println("           <td>${" + ref + "."
							+ CapitalCase.toCapitalCase(string).toLowerCase() + "."+getFKTableColumnName(con,string,2).toLowerCase()+"}</td>");
					setPK.remove(string);
					break;
					}
				} else {
					if(i!=1)
					writer.println("           <td>${" + ref + "."
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase() + "}</td>");
					}
				}
			writer.println("         </tr> ");
			writer.println("       </c:forEach>");
			writer.println("        <tr> <td colspan='2'>");
			writer.println("<nav aria-label=\"\">");
			writer.println(" <ul class=\"pagination\">");
		    writer.println("    <c:if test=\"${currentPage != 1}\">");
		    writer.println("       <li class=\"page-item\"><a class=\"page-link\" ");
		    writer.println("         href=\"ViewAll"+ CapitalCase.toCapitalCase(className)+"Action?recordsPerPage=${recordsPerPage}&currentPage=${currentPage-1}\">Previous</a>");
		    writer.println("    </li>");
			writer.println(" </c:if>");
			writer.println("<c:forEach begin=\"1\" end=\"${noOfPages}\" var=\"i\">");
		    writer.println(" <c:choose>");
		    writer.println(" <c:when test=\"${currentPage eq i}\">");
		    writer.println("    <li class=\"page-item active\"><a class=\"page-link\">");
		    writer.println("           ${i} <span class=\"sr-only\">(current)</span></a>");
		    writer.println("     </li>");
		    writer.println("</c:when>");
		    writer.println("<c:otherwise>");
		    writer.println("              <li class=\"page-item\"><a class=\"page-link\" ");
		    writer.println("                      href=\"ViewAll"+ CapitalCase.toCapitalCase(className)+"Action?recordsPerPage=${recordsPerPage}&currentPage=${i}\">${i}</a>");
		    writer.println("                   </li>");
		    writer.println("                </c:otherwise>");
		    writer.println("            </c:choose>");
		    writer.println("        </c:forEach>");
		    writer.println("	        <c:if test=\"${currentPage lt noOfPages}\">");
		    writer.println("	            <li class=\"page-item\"><a class=\"page-link\" ");
		    writer.println("		                href=\"ViewAll"+ CapitalCase.toCapitalCase(className)+"Action?recordsPerPage=${recordsPerPage}&currentPage=${currentPage+1}\">Next</a>");
		    writer.println("		            </li>");
		    writer.println("		        </c:if>");              
		    writer.println("</ul>");
		    writer.println("</nav>");
		    writer.println("        </td></tr> ");
			/*
			 * writer.println("			        <tr align='center'>");
			 * writer.println("                          <td>"); writer.
			 * println("                             <input type='submit' value='Delete' name='submit'>"
			 * ); writer.println("                         </td>");
			 * writer.println("                    </tr>");
			 */
			writer.println(" </table>");
			writer.println("<br><center> <input type='submit' value='Delete' name='submit'></center>");
			
			writer.println("</form>");
			writer.println("<br><br><br><br><br><br>");
			writer.println("    <jsp:include page=\"footer.jsp\"></jsp:include>");
			writer.println("		</div>");
			writer.println("			<div class=\"col-xs-6 col-md-2\"><jsp:include page=\"MidRight.jsp\"/></div>");
			writer.println("		</div>");
			writer.println("		<div class=\"row\">");
			writer.println("			<div class=\"col-xs-6 col-md-2\"><jsp:include page=\"FooterLeft.jsp\"/></div>");
			writer.println("			<div class=\"col-xs-6 col-md-8\"><jsp:include page=\"FooterMiddle.jsp\"/></div>");
			writer.println("			<div class=\"col-xs-6 col-md-2\"><jsp:include page=\"FooterRight.jsp\"/></div>");
			writer.println("		</div>");
			writer.println("	</div>");
			writer.println("   </body>");
			writer.println("  </html>");
			writer.close();

			writer = new PrintWriter(".\\jsp" + "\\UpdateView" + CapitalCase.toCapitalCase(className) + ".jsp");

			writer.println("<%@ page language=\"java\" import=\"java.util.*\" pageEncoding=\"ISO-8859-1\"%>");
			writer.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
			writer.println("<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>");
			writer.println("<html>");
			writer.println("	<head>");

			writer.println("		<title>" + title + "</title>");
			writer.println("		<link rel=\"icon\" href=\"./images/"+ title +"_favicon.png\" />");
			writer.println(" 	");
			writer.println("");
			writer.println(
					"		<script language=\"JavaScript\" src=\"<%=request.getContextPath() + \"/js/formvalidator.js\"%>\" type=\"text/javascript\"></script>");
			writer.println("		<script language=\"JavaScript\" src=\"<%=request.getContextPath() + \"/js/date_picker.js\"%>\" type=\"text/javascript\"></script>");
			writer.println("<link rel = \"stylesheet\"  type=\"text/css\"  href=\"./css/"+title+".css\" />");
			writer.println("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">");
			writer.println("	</head>");
			writer.println("");
			writer.println("	<body>");
			writer.println("<div class=\"container-fluid\">");
			writer.println("		<div class=\"row\">");
			writer.println("			<div class=\"col-xs-6 col-md-2\"><jsp:include page=\"TopLeft.jsp\"/></div>");
			writer.println("			<div class=\"col-xs-6 col-md-8\"><jsp:include page=\"TopMiddle.jsp\"/></div>");
			writer.println("			<div class=\"col-xs-6 col-md-2\"><jsp:include page=\"TopRight.jsp\"/></div>");
			writer.println("		</div>");
			writer.println("		<div class=\"row\">");
			writer.println("			<div class=\"col-xs-6 col-md-2\"><jsp:include page=\"MidLeft.jsp\"/></div>");
			writer.println("			<div class=\"col-xs-6 col-md-8\">");
			writer.println("");
			writer.println("");
			writer.println("					<jsp:include page=\"header.jsp\"></jsp:include>");
			writer.println("<form action=\"./Update" + CapitalCase.toCapitalCase(className)
					+ "Action\" method=\"post\" name=\"" + CapitalCase.toCapitalCase(className).toLowerCase() + "\"");
			writer.println("onsubmit='return validate()'>");
			writer.println("<center>");
			writer.println("	<h2>");
			writer.println("			<font color='#sharew'>Update " + InputNamesFileRead.getProperString(className,inputNames) + "</font>");
			writer.println("	</h2>");
			writer.println("	</center>");
			writer.println("	<pre>");
			writer.println("	<c:if test='${not empty " + CapitalCase.toBeanClass(className).toLowerCase() + "}'>");
			writer.println("<table align='center'>");
			// for loop
			columnCount = columnMetaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				if (i == 1) {
					continue;
				} else if (SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i))
						.equals("Int")
						&& isForeignKey(className, columnMetaData.getColumnName(i), databaseMetaData, cataLog,
								listOfForeignKeys)) {
					writer.println("        	<tr>");
					writer.println("                <td align='right'>");
					writer.println(
							"        		" + CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)) + " :");
					writer.println("                </td>");
					writer.println("                <td>");
					writer.println("                        <select name=\'"
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
							+ "' onChange='cleartext()'>");
					String fkTableName = getFKTableName(listOfForeignKeys, className, columnMetaData.getColumnName(i));
					writer.println("                               <option value='${requestScope."+getFKTableColumnName(con,fkTableName,1).toLowerCase()+"}' selected='true'>");
					writer.println("                               ${requestScope."+getFKTableColumnName(con,fkTableName,2).toLowerCase()+"}");
					writer.println("                               </option>");
					fkTableName = getFKTableName(listOfForeignKeys, className, columnMetaData.getColumnName(i));
					writer.println("<c:forEach var=\"" + CapitalCase.replaceUnderScore(fkTableName).toLowerCase() + "\" items=\"${"
							+ CapitalCase.toBeanClass(fkTableName).toLowerCase() + "list}\">");
					writer.println(" <option value='${" + CapitalCase.replaceUnderScore(fkTableName).toLowerCase() + "."+getFKTableColumnName(con,fkTableName,1).toLowerCase()+ "}'>");
					writer.println(" ${"	+ CapitalCase.toCapitalCase(fkTableName).toLowerCase() +"."+getFKTableColumnName(con,fkTableName,2).toLowerCase()+ "}");
					writer.println("                               </option>");
					writer.println("</c:forEach>");
				} else if (SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i)).equals(
						"Int") && isPrimaryKey(className, columnMetaData.getColumnName(i), databaseMetaData, cataLog))
					continue;
				else if (CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
						.equals("password")) {
					// writer.println(" <tr>");
					// writer.println(" <td align='right'>");
					// writer.println("
					// "+CapitalCase.toCapitalCase(columnMetaData.getColumnName(i))+" :");
					// writer.println(" </td>");
					// writer.println(" <td align='right'>");
					// writer.println(" <input type='password' value=''
					// name="+CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()+">");
				} else if (SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i))
						.equals("Date")) {
					if (ListOfDateString.checkDateString(columnMetaData.getColumnName(i).toLowerCase(), className)) {
						writer.println("        	<tr>");
						writer.println("                <td align='right'>");
						writer.println(
								"        		" + CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)) + " :");
						writer.println("                </td>");
						writer.println("                <td align='right'>");
						writer.println("        	<input type=\"text\" name=\""
								+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
								+ "\" size=\"20\" value='${"+CapitalCase.toBeanClass(className).toLowerCase()+"."+CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()+"}'");
						writer.println("        			readonly=\"readonly\" /><a");
						writer.println("        						href=\"javascript:show_calendar('document."
								+ CapitalCase.toCapitalCase(className).toLowerCase() + ".dob', document."
								+ CapitalCase.toCapitalCase(className).toLowerCase() + ".dob.value);\">");
						writer.println("        	<img src=\"<%=request.getContextPath() + \"/images/cal.gif\"%>\"");
						writer.println("        	alt=\"cal\" width=\"18\" height=\"18\" border=\"0\" />");
						writer.println("        	</a>");

					} else {

					}
				} else if("VARCHAR2".equalsIgnoreCase(columnMetaData.getColumnTypeName(i)) && columnMetaData.getPrecision(i)>150){

					writer.println("        	<tr>");

					writer.println("                <td align='right'>");
					writer.println("        		" + CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)) + " :");
					writer.println("                </td>");
					writer.println("                <td align='right'>");
					writer.println("                            <textarea value='' name='"
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase() + "'>${"+CapitalCase.toBeanClass(className).toLowerCase()+"."+CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()+"}</textarea>");
				}else {
					writer.println("        	<tr>");

					writer.println("                <td align='right'>");
					writer.println(
							"        		" + CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)) + " :");
					writer.println("                </td>");
					writer.println("                <td align='right'>");
					writer.println("                            <input type='text' value='${"+CapitalCase.toBeanClass(className).toLowerCase()+"."+CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()+"}' name='"
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase() + "'>");
				}
					writer.println("                </td>");
					writer.println("            </tr>");
			}
			writer.println("<input type='hidden' name='" + columnMetaData.getColumnName(1).toLowerCase() + "' value='${"
					+ CapitalCase.toBeanClass(className).toLowerCase() + "."
					+ columnMetaData.getColumnName(1).toLowerCase() + "}'>");
			writer.println("			        <tr align='center'>");
			writer.println("                          <td>");
			writer.println("                             <input type='submit' value='Update' name='submit'>");
			writer.println("                         </td>");
			writer.println("                    </tr>");
			writer.println("                </table>");
			writer.println("</c:if>");
			writer.println("<input type='hidden' name='name' value='<%=session.getAttribute(\"user\")%>'>");
			writer.println("</pre>");
			writer.println("</form>");

			writer.println("<script language='JavaScript' type='text/javascript'>");
			writer.println("var frmvalidator = new Validator(\"" + CapitalCase.toCapitalCase(className).toLowerCase()
					+ "\");");
			columnCount = columnMetaData.getColumnCount();
			for (int i = 2; i <= columnCount; i++) {
				if("DATE".equals(columnMetaData.getColumnTypeName(i)) && !ListOfDateString.checkDateString(columnMetaData.getColumnName(i),className)) {
					/*writer.println("			frmvalidator.addValidation('"
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
							+ "', 'req','Please enter "
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase() + "');");*/
				}else if("NUMBER".equals(columnMetaData.getColumnTypeName(i))) {
					if (SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i))
							.equals("Int")
							&& isForeignKey(className, columnMetaData.getColumnName(i), databaseMetaData, cataLog,
									listOfForeignKeys)) {
					/*writer.println("			frmvalidator.addValidation('"
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
							+ "', 'dontselect=0');")*/;
					}else {
						writer.println("			frmvalidator.addValidation('"
								+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
								+ "', 'req','Please enter "
								+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase() + "');");
						writer.println("			frmvalidator.addValidation('"
								+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
								+ "', 'numeric');");
					}
				}else if("email".equalsIgnoreCase(columnMetaData.getColumnName(i))) {
					writer.println("			frmvalidator.addValidation('"+CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()+"', 'maxlen=50');");
					writer.println("			frmvalidator.addValidation('"+CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()+"', 'req');");
					writer.println("			frmvalidator.addValidation('"+CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()+"', 'email');");
				}else if("phoneNo".equalsIgnoreCase(columnMetaData.getColumnName(i))) {
					writer.println("			frmvalidator.addValidation('"+CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()+"', 'maxlen=50');");
					writer.println("			frmvalidator.addValidation('"+CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()+"', 'numeric');");
					writer.println("			frmvalidator.addValidation('"+CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()+"', 'Phone');");
				}else {
				writer.println("			frmvalidator.addValidation('"
						+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
						+ "', 'req','Please enter "
						+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase() + "');");
				writer.println("			frmvalidator.addValidation('"
						+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
						+ "', 'maxlen="+columnMetaData.getPrecision(i)+"','Max length for "
						+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase() + " is "+columnMetaData.getPrecision(i)+"');");
				writer.println("			frmvalidator.addValidation('"
						+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase()
						+ "', 'alpha','"+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)).toLowerCase() + " Alphabetic chars only');");
				}
			}
			writer.println("</script>");
			writer.println("		<jsp:include page='footer.jsp'></jsp:include>");
			writer.println("		</div>");
			writer.println("			<div class=\"col-xs-6 col-md-2\"><jsp:include page=\"MidRight.jsp\"/></div>");
			writer.println("		</div>");
			writer.println("		<div class=\"row\">");
			writer.println("			<div class=\"col-xs-6 col-md-2\"><jsp:include page=\"FooterLeft.jsp\"/></div>");
			writer.println("			<div class=\"col-xs-6 col-md-8\"><jsp:include page=\"FooterMiddle.jsp\"/></div>");
			writer.println("			<div class=\"col-xs-6 col-md-2\"><jsp:include page=\"FooterRight.jsp\"/></div>");
			writer.println("		</div>");
			writer.println("	</div>");
			writer.println("	</body>");
			writer.println("</html>");
			writer.close();
			resultSet.close();
			pstmt.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getFKTableName(Map<String, Map<String, String>> listOfForeignKeys, String className,
			String columnName) {
		String tableName = "";
		boolean flag = false;
		try {

			for (String tName : listOfForeignKeys.keySet()) {
				Map<String, String> foriegnKeyMap = listOfForeignKeys.get(tName);
				for (String fKeys : foriegnKeyMap.keySet()) {
					if (foriegnKeyMap.get(fKeys).equalsIgnoreCase(columnName) && className.equalsIgnoreCase(fKeys)) {
						tableName = tName;
						flag = true;
						break;
					}
				}
				if (flag)
					break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tableName;
	}

	private String getFKTableColumnName(Connection con, String className, int columnNumber) {
		String fkColumnName = "";
		try {
			pstmt = con.prepareStatement("select * from " + className);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();

			if (columnNumber == 1) {
				fkColumnName =  columnMetaData.getColumnName(columnNumber);
			}
			if (columnNumber == 2 && !SqlDatatypeTOJavaWrapperClass
					.toPrimitiveClass(columnMetaData.getColumnTypeName(2)).equals("Int")) {
				if (columnMetaData.getColumnName(columnNumber).toLowerCase().endsWith("name"))
					fkColumnName =  columnMetaData.getColumnName(columnNumber);
				else if (columnMetaData.getColumnName(3).endsWith("name")) {
					fkColumnName =  columnMetaData.getColumnName(columnNumber);
				}
			} 
			if (columnNumber == 2 && SqlDatatypeTOJavaWrapperClass
					.toPrimitiveClass(columnMetaData.getColumnTypeName(2)).equals("Int")) {
				boolean flag = true;
				int i = columnNumber;
				while (flag) {
					i++;
					String colName = "";
					try {
					  colName = columnMetaData.getColumnName(i).toLowerCase();
					  if (colName.endsWith("name")) {
							fkColumnName = colName;
							flag = false;
							break;
						}
					  /*else if(i>3 && !SqlDatatypeTOJavaWrapperClass
								.toPrimitiveClass(columnMetaData.getColumnTypeName(i)).equals("Int")) {
						  SqlDatatypeTOJavaWrapperClass
							.toPrimitiveClass(columnMetaData.getColumnTypeName(2)).equals("Int"); 
					  }*/
					}catch(Exception e) {
						for (int j = columnNumber; j < columnMetaData.getColumnCount(); j++) {
							if(!SqlDatatypeTOJavaWrapperClass
									.toPrimitiveClass(columnMetaData.getColumnTypeName(j)).equals("String")) {
								fkColumnName = columnMetaData.getColumnName(j).toLowerCase();
								flag = false;
								break;
							}
						}
					}
				}
			} else if(columnNumber == 2 && SqlDatatypeTOJavaWrapperClass
					.toPrimitiveClass(columnMetaData.getColumnTypeName(2)).equals("String")) {
				fkColumnName = columnMetaData.getColumnName(2);
			}

		} catch (Exception e) {
			System.out.println("ERR "+className);
			e.printStackTrace();
		}
		try {
			resultSet.close();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("ERR "+className);
			e.printStackTrace();
		}
		
		return fkColumnName;
	}
}