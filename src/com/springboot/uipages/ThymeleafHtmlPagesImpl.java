package com.springboot.uipages;

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
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jdbc.dao.AbstractDataAccessObject;
import com.jdbc.main.CapitalCase;
import com.jdbc.main.SqlDatatypeTOJavaWrapperClass;
import com.springboot.persistance.jpa.JPAPersistance;
import com.springboot.project.properties.ReadProjectPropertiesFile;
import com.springboot.util.InputNamesFileRead;

public class ThymeleafHtmlPagesImpl extends AbstractDataAccessObject {

	PreparedStatement pstmt;
	String pack, resourcePackage;
	String title;
	ResultSet resultSet, rs, rsf, tableTypes, contraintsRecords;
	String loginTableName = ReadProjectPropertiesFile.projectProps.getProperty("database-login-table");
	List<String> colList= JPAPersistance.nottoDisplayColumnNamesFromPRoperteisFile();
	List<String> basicTableList= JPAPersistance.basicTableListFromPRoperteisFile();
	
	Set<String> inputNames = InputNamesFileRead.inputNames;
	public ThymeleafHtmlPagesImpl(String pack, String title,String resourcePackage) {
		super();
		this.pack = pack;
		this.title = title;
		this.resourcePackage = resourcePackage;
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
	
	private boolean isForeignKey(Connection con, String tableName, String name)throws Exception {
		boolean flag = false;
		ResultSet rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
		while (rsf.next()) {
			String fkColumnName = rsf.getString("FKCOLUMN_NAME");
			if (fkColumnName.equalsIgnoreCase(name)) { 
				flag =  true;
				break;
			}
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

	public void createThymeLeafImpPages(String className, Connection con,
			Map<String, Map<String, String>> listOfForeignKeys) throws SQLException {
		try {
			pstmt = con.prepareStatement("select * from " + className);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();
			
			List<String> listOfForeignKeysofaTable = getListOfForeignKeysTable(con, className);
			
			File packageDir = new File(resourcePackage + "\\templates\\" + CapitalCase.toCapitalCase(className).toLowerCase());
			if (!packageDir.exists())
				packageDir.mkdir();
			
			boolean fileFlag = false;
			for (int i = 2; i <= columnMetaData.getColumnCount(); i++) {
				if(columnMetaData.getColumnTypeName(i).equalsIgnoreCase("LONGBLOB"))
					fileFlag = true;
			}	

			PrintWriter writer = new PrintWriter(packageDir.getAbsolutePath() + "\\add" + CapitalCase.toCapitalCase(className) + ".html");
			writer.println("<!DOCTYPE html>");
			writer.println("<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">");
			writer.println("<th:block th:include=\"fragments/head\"></th:block>");
			writer.println("<body>");
			writer.println("	<!-- Page Wrapper -->");
			writer.println("	<div id=\"wrapper\">");
			writer.println("		<th:block th:include=\"fragments/sidebar\"></th:block>");
			writer.println("		<!-- Main Panel -->");
			writer.println("		<div class=\"main-panel\">");
			writer.println("			<th:block th:include=\"fragments/navbar\"></th:block>");
			writer.println("			<!-- Begin Page Content -->");
			writer.println("			<div class=\"content\">");
			writer.println("				<div class=\"container-fluid\">");
			writer.println("					<div class=\"row\">");
			writer.println("						<div class=\"col-md-12\">");
			writer.println("							<div class=\"card\">");
			writer.println("								<div class=\"header\">");
			writer.println("									<h4 class=\"title\">New " + CapitalCase.toCapitalCase(className)+"</h4>");
			writer.println("								</div>");
			writer.println("");
			writer.println("								<div class=\"content\">");
			writer.println("									<form th:object=\"${" +CapitalCase.toCapitalCase(className).toLowerCase()+"}\" th:action=\"@{/" +CapitalCase.toCapitalCase(className).toLowerCase()+"/add" + CapitalCase.toCapitalCase(className)+"}\"");
			writer.println("										th:method=\"post\" enctype=\"multipart/form-data\">");
			writer.println("										<div class=\"form-group\">");
			String hiddenColName = columnMetaData.getColumnName(1).toLowerCase();
			if(isForeignKey(con, className,hiddenColName))
				hiddenColName = columnMetaData.getColumnName(2).toLowerCase();
			writer.println("   										 <input type=\"hidden\" class=\"form-control\" th:field=\"*{"+hiddenColName+"}\" />");
			writer.println("									    </div>");
			int columnCount = columnMetaData.getColumnCount();
			for (int i = 2; i <= columnCount; i++) {
				String colName = columnMetaData.getColumnName(i);
				if(!isForeignKey(con, className,colName)) {
					int coldataTypeSize = columnMetaData.getColumnDisplaySize(i);
					if(columnMetaData.getColumnTypeName(i).equalsIgnoreCase("LONGBLOB"))
					 {
						writer.println("                                        <div class=\"form-group\">");
						writer.println("	                                        <label for=\"title\">"+CapitalCase.displayColNameASTableHeadColumns(colName.toLowerCase(), className)+"</label> <input type=\"file\"");
						writer.println("	                                        	class=\"form-control\" id=\""+colName.toLowerCase()+"file\" aria-describedby=\""+colName.toLowerCase()+"file\"");
						writer.println("	                                        	th:field=\"*{"+colName.toLowerCase()+"file}\"");
						writer.println("	                                        	playeholder=\""+CapitalCase.toCapitalCase(colName)+"file\">");
						writer.println("                                        <span id=\"errorMessage\" th:if=\"${#fields.hasErrors('"+colName.toLowerCase()+"file')}\" th:errors=\"*{"+colName.toLowerCase()+"file}\">"+CapitalCase.toCapitalCase(colName)+"file error</span>");
						writer.println("                                        </div>");
						i = i+2;
					} else if(columnMetaData.getColumnTypeName(i).equalsIgnoreCase("Date") || columnMetaData.getColumnTypeName(i).equalsIgnoreCase("datetime")) { 
						writer.println("                                        <div class=\"form-group\">");
						writer.println("	                                        <label for=\"title\">"+CapitalCase.displayColNameASTableHeadColumns(colName.toLowerCase(), className)+"</label>");
						writer.println("											<div class=\"input-group date\">");
						writer.println("											<input type=\""+CapitalCase.inputType(colName)+"\"");
						writer.println("	                                        	class=\"form-control\" id=\""+colName.toLowerCase()+"\" aria-describedby=\""+colName.toLowerCase()+"\"");
						writer.println("	                                        	th:field=\"*{"+colName.toLowerCase()+"}\"");
						writer.println("	                                        	playeholder=\""+CapitalCase.toCapitalCase(colName)+"\">");
						writer.println("												<div class=\"input-group-append\">");
						writer.println("													<span class=\"input-group-text\"><i");
						writer.println("														class=\"far fa-calendar-alt\"></i></span>");
						writer.println("												</div>");
						writer.println("                                        <span id=\"errorMessage\" th:if=\"${#fields.hasErrors('"+colName.toLowerCase()+"')}\" th:errors=\"*{"+colName.toLowerCase()+"}\">"+CapitalCase.toCapitalCase(colName)+" error</span>");
						writer.println("                                        	</div>");
						writer.println("                                        </div>");
					} else if (columnMetaData.getColumnTypeName(i).equalsIgnoreCase("Timestamp")) {
					} else if(coldataTypeSize>=256) {
						writer.println("										<div class=\"form-group\">");
						writer.println("											<label for=\"description\">"+CapitalCase.displayColNameASTableHeadColumns(colName.toLowerCase(), className)+"</label>");
						writer.println("												<textarea rows=\"4\" type=\"textarea\" class=\"form-control\"");
						writer.println("												id=\""+colName.toLowerCase()+"\" th:field=\"*{"+colName.toLowerCase()+"}\"");
						writer.println("												playeholder=\"type here ...\" /></textarea>");
						writer.println("										<span id=\"errorMessage\" th:if=\"${#fields.hasErrors('"+colName.toLowerCase()+"')}\" th:errors=\"*{"+colName.toLowerCase()+"}\">"+colName.toLowerCase()+" error</span>");
						writer.println("										</div>");
					} else {
						writer.println("                                        <div class=\"form-group\">");
						writer.println("	                                        <label for=\"title\">"+CapitalCase.displayColNameASTableHeadColumns(colName.toLowerCase(), className)+"</label> <input type=\""+CapitalCase.inputType(colName)+"\"");
						writer.println("	                                        	class=\"form-control\" id=\""+colName.toLowerCase()+"\" aria-describedby=\""+colName.toLowerCase()+"\"");
						writer.println("	                                        	th:field=\"*{"+colName.toLowerCase()+"}\"");
						writer.println("	                                        	playeholder=\""+CapitalCase.toCapitalCase(colName)+"\">");
						writer.println("                                        <span id=\"errorMessage\" th:if=\"${#fields.hasErrors('"+colName.toLowerCase()+"')}\" th:errors=\"*{"+colName.toLowerCase()+"}\">"+CapitalCase.toCapitalCase(colName)+" error</span>");
						writer.println("                                        </div>");
					}
				}
				else if(isForeignKey(con, className,colName)) {
					String fkTableName = getForeignKeyTableName(con, className, columnMetaData.getColumnName(i));
					if(!fkTableName.toLowerCase().equalsIgnoreCase(loginTableName) || basicTableList.contains(className)){
							String idCol = getFKTableIdColName(con,fkTableName);
							String nameCol = getFKTableNameColName(con,fkTableName);
							fkTableName = CapitalCase.replaceUnderScore(fkTableName).toLowerCase();
							writer.println("										<div class=\"form-group\">");
							writer.println("											<label for=\""+fkTableName.toLowerCase()+"\">"+CapitalCase.toCapitalCase(fkTableName)+"</label> <select id=\""+fkTableName.toLowerCase()+"List\"");
							writer.println("												class=\"form-control\" th:field=\"*{"+fkTableName.toLowerCase()+"}\"");
							writer.println("												multiple=\"multiple\">");
							writer.println("												<option th:each=\""+fkTableName.toLowerCase()+" : ${"+fkTableName.toLowerCase()+"List}\"");
							writer.println("													th:text=\"${"+fkTableName.toLowerCase()+"."+nameCol.toLowerCase()+"}\" th:value=\"${"+fkTableName.toLowerCase()+"."+idCol.toLowerCase()+"}\"></option>");
							writer.println("													<span  id=\"errorMessage\" th:if=\"${#fields.hasErrors('"+fkTableName.toLowerCase()+"')}\" th:errors=\"*{"+fkTableName.toLowerCase()+"}\">"+CapitalCase.toCapitalCase(fkTableName)+" error</span>");
							writer.println("											</select>");
							writer.println("										</div>");
					}
				}
			}
			writer.println("										<button type=\"submit\" class=\"btn btn-primary\">Save</button>");
			writer.println("									</form>");
			writer.println("");
			writer.println("								</div>");
			writer.println("							</div>");
			writer.println("						</div>");
			writer.println("					</div>");
			writer.println("				</div>");
			writer.println("			</div>			");
			writer.println("		</div>");
			writer.println("		<!-- End of Main Panel -->");
			writer.println("		<th:block th:include=\"fragments/footer\"></th:block>");
			writer.println("	</div>");
			writer.println("	<th:block th:include=\"fragments/scripts\"></th:block>");
			writer.println("</body>");
			writer.println("</html>");
			writer.close();
			
			writer = new PrintWriter(packageDir.getAbsolutePath() + "\\edit" + CapitalCase.toCapitalCase(className) + ".html");
			
			writer.println("<!DOCTYPE html>");
			writer.println("<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">");
			writer.println("<th:block th:include=\"fragments/head\"></th:block>");
			writer.println("<body>");
			writer.println("	<!-- Page Wrapper -->");
			writer.println("	<div id=\"wrapper\">");
			writer.println("		<th:block th:include=\"fragments/sidebar\"></th:block>");
			writer.println("		<!-- Main Panel -->");
			writer.println("		<div class=\"main-panel\">");
			writer.println("			<th:block th:include=\"fragments/navbar\"></th:block>");
			writer.println("			<!-- Begin Page Content -->");
			writer.println("			<div class=\"content\">");
			writer.println("				<div class=\"container-fluid\">");
			writer.println("					<div class=\"row\">");
			writer.println("						<div class=\"col-md-12\">");
			writer.println("							<div class=\"card\">");
			writer.println("								<div class=\"header\">");
			writer.println("									<h4 class=\"title\">Edit " + CapitalCase.toCapitalCase(className) + "</h4>");
			writer.println("									<p class=\"category\"></p>");
			writer.println("								</div>");
			writer.println("								<div class=\"content\">");
			writer.println("									<form th:object=\"${" +CapitalCase.toCapitalCase(className).toLowerCase()+ "}\"");
			writer.println("										th:action=\"@{/"+CapitalCase.toCapitalCase(className).toLowerCase()+"/update" + CapitalCase.toCapitalCase(className) + "/{id}(id=${"+CapitalCase.toCapitalCase(className).toLowerCase()+".id})}\"");
			writer.print("										th:method=\"patch\"");
			if(fileFlag) 
				writer.println("	enctype=\"multipart/form-data\">");
			else		
				writer.println(">");
			writer.println("										<div class=\"form-group\">");
			  hiddenColName = columnMetaData.getColumnName(1).toLowerCase();
						if(isForeignKey(con, className,hiddenColName))
							hiddenColName = columnMetaData.getColumnName(2).toLowerCase();
						writer.println("   										 <input type=\"hidden\" class=\"form-control\" th:field=\"*{"+hiddenColName+"}\" />");
						writer.println("									    </div>");
						  columnCount = columnMetaData.getColumnCount();
						for (int i = 2; i <= columnCount; i++) {
							String colName = columnMetaData.getColumnName(i);
						if(!colList.contains(colName.toLowerCase())) {
							if(!isForeignKey(con, className,colName)) {
								int coldataTypeSize = columnMetaData.getColumnDisplaySize(i);
								if(columnMetaData.getColumnTypeName(i).equalsIgnoreCase("LONGBLOB")) 
								 {
									writer.println("                                        <div class=\"form-group\">");
									writer.println("	                                        <label for=\"title\">"+CapitalCase.displayColNameASTableHeadColumns(colName.toLowerCase(), className)+"</label>");
									writer.println("											<p class=\"profile-pic\"> ");
									writer.println("				<img th:src=\"@{${"+CapitalCase.replaceUnderScore(className).toLowerCase()+".fileDownloadPath}}\" class=\"profile-pic\">");
									writer.println("											</p>");	
									writer.println("											<input type=\"file\"");
									writer.println("	                                        	class=\"form-control\" id=\""+colName.toLowerCase()+"file\" aria-describedby=\""+colName.toLowerCase()+"file\"");
									writer.println("	                                        	th:field=\"*{"+colName.toLowerCase()+"file}\"");
									writer.println("	                                        	playeholder=\""+CapitalCase.toCapitalCase(colName)+"file\">");
									writer.println("                                        <span id=\"errorMessage\" th:if=\"${#fields.hasErrors('"+colName.toLowerCase()+"file')}\" th:errors=\"*{"+colName.toLowerCase()+"file}\">"+CapitalCase.toCapitalCase(colName)+"file error</span>");
									writer.println("                                        </div>");
									i = i+2;
								} else if(columnMetaData.getColumnTypeName(i).equalsIgnoreCase("Date") || columnMetaData.getColumnTypeName(i).equalsIgnoreCase("datetime")) { 
										writer.println("                                        <div class=\"form-group\">");
										writer.println("	                                        <label for=\"title\">"+CapitalCase.displayColNameASTableHeadColumns(colName, className)+"</label>");
										writer.println("											<div class=\"input-group date\">");
										writer.println("										 <input type=\""+CapitalCase.inputType(colName)+"\"");
										writer.println("	                                        	class=\"form-control\" id=\""+colName.toLowerCase()+"\" aria-describedby=\""+colName.toLowerCase()+"\"");
										writer.println("	                                        	th:field=\"*{"+colName.toLowerCase()+"}\"");
										writer.println("	                                        	playeholder=\""+CapitalCase.toCapitalCase(colName)+"\">");
										writer.println("												<div class=\"input-group-append\">");
										writer.println("													<span class=\"input-group-text\"><i");
										writer.println("														class=\"far fa-calendar-alt\"></i></span>");
										writer.println("													</div>");
										writer.println("												</div>");
										writer.println("                                        <span id=\"errorMessage\" th:if=\"${#fields.hasErrors('"+colName.toLowerCase()+"')}\" th:errors=\"*{"+colName.toLowerCase()+"}\">"+CapitalCase.displayColNameASTableHeadColumns(colName, className)+" error</span>");
										writer.println("                                        </div>");
									} else if (columnMetaData.getColumnTypeName(i).equalsIgnoreCase("Timestamp")) {
									}/*else if (columnMetaData.getColumnTypeName(i).equalsIgnoreCase("LONGBLOB")) {
										writer.println("                                        <div class=\"form-group\">");
										writer.println("	                                        <label for=\"title\">"+CapitalCase.displayColNameASTableHeadColumns(colName, className)+"</label> <input type=\""+CapitalCase.inputType(colName)+"file\"");
										writer.println("	                                        	class=\"form-control\" id=\""+colName.toLowerCase()+"file\" aria-describedby=\""+colName.toLowerCase()+"datafile\"");
										writer.println("	                                        	th:field=\"*{"+colName.toLowerCase()+"file}\">");
										writer.println("                                        </div>");
									} */
									else if(coldataTypeSize>=256) {
										writer.println("										<div class=\"form-group\">");
										writer.println("											<label for=\"description\">"+CapitalCase.displayColNameASTableHeadColumns(colName.toLowerCase(), className)+"</label>");
										writer.println("												<textarea rows=\"4\" type=\"textarea\" class=\"form-control\"");
										writer.println("												id=\""+colName.toLowerCase()+"\" th:field=\"*{"+colName.toLowerCase()+"}\"");
										writer.println("												playeholder=\"type here ...\" /></textarea>");
										writer.println("										<span id=\"errorMessage\" th:if=\"${#fields.hasErrors('"+colName.toLowerCase()+"')}\" th:errors=\"*{"+colName.toLowerCase()+"}\">"+colName.toLowerCase()+" error</span>");
										writer.println("										</div>");
									}else {
										writer.println("                                        <div class=\"form-group\">");
										writer.println("	                                        <label for=\"title\">"+CapitalCase.displayColNameASTableHeadColumns(colName, className)+"</label> <input type=\""+CapitalCase.inputType(colName)+"\"");
										writer.println("	                                        	class=\"form-control\" id=\""+colName.toLowerCase()+"\" aria-describedby=\""+colName.toLowerCase()+"\"");
										writer.println("	                                        	th:field=\"*{"+colName.toLowerCase()+"}\"");
										writer.println("	                                        	playeholder=\""+CapitalCase.toCapitalCase(colName)+"\">");
										writer.println("                                        <span id=\"errorMessage\" th:if=\"${#fields.hasErrors('"+colName.toLowerCase()+"')}\" th:errors=\"*{"+colName.toLowerCase()+"}\">"+CapitalCase.displayColNameASTableHeadColumns(colName, className)+" error</span>");
										writer.println("                                        </div>");
									}
								}
							else if(isForeignKey(con, className,colName)) {
								String fkTableName = getForeignKeyTableName(con, className, columnMetaData.getColumnName(i));
								if(!fkTableName.toLowerCase().equalsIgnoreCase(loginTableName) || basicTableList.contains(className)){
										String idCol = getFKTableIdColName(con,fkTableName);
										String nameCol = getFKTableNameColName(con,fkTableName);
										fkTableName = CapitalCase.replaceUnderScore(fkTableName).toLowerCase();
										writer.println("										<div class=\"form-group\">");
										writer.println("											<label for=\""+fkTableName.toLowerCase()+"\">"+CapitalCase.toCapitalCase(fkTableName)+"</label> <select id=\""+fkTableName.toLowerCase()+"List\"");
										writer.println("												class=\"form-control\" th:field=\"*{"+fkTableName.toLowerCase()+"}\"");
										writer.println("												multiple=\"multiple\">");
										writer.println("												<option th:each=\""+fkTableName.toLowerCase()+" : ${"+fkTableName.toLowerCase()+"List}\"");
										writer.println("													th:text=\"${"+fkTableName.toLowerCase()+"."+nameCol.toLowerCase()+"}\" th:value=\"${"+fkTableName.toLowerCase()+"."+idCol.toLowerCase()+"}\"></option>");
										writer.println("													<span  id=\"errorMessage\" th:if=\"${#fields.hasErrors('"+fkTableName.toLowerCase()+"')}\" th:errors=\"*{"+fkTableName.toLowerCase()+"}\">"+CapitalCase.toCapitalCase(fkTableName)+" error</span>");
										writer.println("											</select>");
										writer.println("										</div>");
								}
							
							}
						  }
						}
			writer.println("										<button type=\"submit\" class=\"btn btn-primary\">Save</button>");
			writer.println("										<a th:href=\"@{'/"+CapitalCase.toCapitalCase(className).toLowerCase()+"/viewAll" + CapitalCase.toCapitalCase(className) + "'}\" class=\"btn btn-default\">Cancel</a>");
			writer.println("									</form>");
			writer.println("								</div>");
			writer.println("							</div>");
			writer.println("						</div>");
			writer.println("					</div>");
			writer.println("				</div>");
			writer.println("				<!-- /.container-fluid -->");
			writer.println("			</div>");
			writer.println("			<!-- /.content -->");
			writer.println("		</div>");
			writer.println("		<!-- End of Main Panel -->");
			writer.println("		<th:block th:include=\"fragments/footer\"></th:block>");
			writer.println("	</div>");
			writer.println("	<th:block th:include=\"fragments/scripts\"></th:block>");
			writer.println("</body>");
			writer.println("</html>");
			writer.close();

			writer = new PrintWriter(packageDir.getAbsolutePath() + "\\view" + CapitalCase.toCapitalCase(className) + "ById.html");
			writer.println("<!DOCTYPE html>");
			writer.println("<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">");
			writer.println("<th:block th:include=\"fragments/head\"></th:block>");
			writer.println("<body>");
			writer.println("	<!-- Page Wrapper -->");
			writer.println("	<div id=\"wrapper\">");
			writer.println("		<th:block th:include=\"fragments/sidebar\"></th:block>");
			writer.println("		<!-- Main Panel -->");
			writer.println("		<div class=\"main-panel\">");
			writer.println("			<th:block th:include=\"fragments/navbar\"></th:block>");
			writer.println("			<!-- Begin Page Content -->");
			writer.println("			<div class=\"content\">");
			writer.println("				<div class=\"container-fluid\">");
			writer.println("					<div class=\"row\">");
			writer.println("						<div class=\"col-md-12\">");
			writer.println("							<div class=\"card\">");
			writer.println("								<div class=\"header\">");
			//writer.println("									<h4 th:text=\"" + CapitalCase.toCapitalCase(className) + " Details\" class=\"title\">Edit " + CapitalCase.toCapitalCase(className) + "</h4>");
			writer.println("									<h4 class=\"title\">View " + CapitalCase.toCapitalCase(className) + "</h4>");
			writer.println("									<p class=\"category\"></p>");
			writer.println("								</div>");
			String sortColName = CapitalCase.sortColumnName(columnMetaData);
			hiddenColName = columnMetaData.getColumnName(1).toLowerCase();
									if(isForeignKey(con, className,hiddenColName))
										hiddenColName = columnMetaData.getColumnName(2).toLowerCase();
									  columnCount = columnMetaData.getColumnCount();
									writer.println("								<div class=\"content\">");
									for (int i = 1; i <= columnCount; i++) {
										String colName = columnMetaData.getColumnName(i);
										if(!colList.contains(colName.toLowerCase())) {
											if(!isForeignKey(con, className,colName)) {
												if(columnMetaData.getColumnTypeName(i).equalsIgnoreCase("Date") || columnMetaData.getColumnTypeName(i).equalsIgnoreCase("datetime"))
													writer.println("									<p th:text=\"${#dates.format("+CapitalCase.toCapitalCase(className).toLowerCase()+"."+colName.toLowerCase()+", 'yyyy-MM-dd')}\"></p>");
												else if(columnMetaData.getColumnTypeName(i).equalsIgnoreCase("LONGBLOB"))
												{
													writer.println("			<p class=\"profile-pic\">");
													writer.println("				<img th:src=\"@{${"+CapitalCase.replaceUnderScore(className).toLowerCase()+".fileDownloadPath}}\" class=\"profile-pic\">");
													writer.println("			</p>");
													i = i+2;
												}
												else
													writer.println("									<p th:text=\"${"+CapitalCase.toCapitalCase(className).toLowerCase()+"."+colName.toLowerCase()+"}\"></p>");
											}
											else if(isForeignKey(con, className,colName)) {
	/*											String fkTableName = getFKTableName(listOfForeignKeys, className, columnMetaData.getColumnName(i));
												writer.println("									<th:block th:each=\""+fkTableName.toLowerCase()+" : ${"+CapitalCase.toCapitalCase(className).toLowerCase()+"."+fkTableName.toLowerCase()+"}\">");
												writer.println("										<p th:text=\"${"+fkTableName.toLowerCase()+"."+colName+"}\"></p>");
												writer.println("									</th:block>");*/
											}
										}
									} 
			writer.println("								</div>");
			writer.println("							</div>");
			writer.println("						</div>");
			writer.println("					</div>");
			writer.println("				</div> ");
			writer.println("			</div> ");
			writer.println("		</div>");
			writer.println("		<!-- End of Main Panel -->");
			writer.println("		<th:block th:include=\"fragments/footer\"></th:block>");
			writer.println("	</div>");
			writer.println("");
			writer.println("	<th:block th:include=\"fragments/scripts\"></th:block>");
			writer.println("</body>");
			writer.println("</html>");
			writer.close();
			
			writer = new PrintWriter(packageDir.getAbsolutePath() + "\\viewAll" + CapitalCase.toCapitalCase(className) + ".html");
			writer.println("<!DOCTYPE html>");
			writer.println("<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">");
			writer.println("<th:block th:include=\"fragments/head\"></th:block>");
			writer.println("<body>");
			writer.println("	<!-- Page Wrapper -->");
			writer.println("	<div id=\"wrapper\">");
			writer.println("		<th:block th:include=\"fragments/sidebar\"></th:block>");
			writer.println("		<!-- Main Panel -->");
			writer.println("		<div class=\"main-panel\">");
			writer.println("			<th:block th:include=\"fragments/navbar\"></th:block>");
			writer.println("			<!-- Begin Page Content -->");
			writer.println("			<div class=\"content\">");
			writer.println("				<div class=\"container-fluid\">");
			writer.println("					<div class=\"row\">");
			writer.println("						<div class=\"col-md-12\">");
			writer.println("							<div class=\"card\">");
			writer.println("								<div class=\"header\">");
			writer.println("									<h4 class=\"title\">" + CapitalCase.toCapitalCase(className) + "</h4>");
			writer.println("									<p class=\"category\">shows all available " +CapitalCase.toCapitalCase(className).toLowerCase()+ "</p>");
			writer.println("								</div>");
			writer.println("");
			writer.println("								<div class=\"content table-responsive table-full-width\">");
			writer.println("									<table class=\"table table-striped\">");
						 writer.println("									    </div>");
									  columnCount = columnMetaData.getColumnCount();
									  	writer.println("										<thead>");
									for (int i = 2; i <= columnCount; i++) {
										String colName = columnMetaData.getColumnName(i);
										if(!colList.contains(colName.toLowerCase())) {
											if(!isForeignKey(con, className,colName)) {
												if(columnMetaData.getColumnTypeName(i).equalsIgnoreCase("LONGBLOB")) {
													writer.println("											<th>"+CapitalCase.displayColNameASTableHeadColumns(colName.toLowerCase(), className)+"</th>");
													i = i+2;
												} else	
												writer.println("											<th>"+CapitalCase.displayColNameASTableHeadColumns(colName.toLowerCase(), className)+"</th>"); 
											}
											else if(isForeignKey(con, className,colName)) {
												/*String fkTableName = getFKTableName(listOfForeignKeys, className, columnMetaData.getColumnName(i));
												writer.println("											<th>"+fkTableName.toLowerCase()+"."+colName.toLowerCase()+"</th>");*/ 
											}
										}
									}
									writer.println("											<th></th>");
									writer.println("											<th></th>");
									writer.println("											<th></th>");
									writer.println("										</thead>");
									
									
			writer.println("										<tbody>");
			writer.println("											<tr th:each=\"" +CapitalCase.toCapitalCase(className).toLowerCase()+ " : ${" +CapitalCase.toCapitalCase(className).toLowerCase()+ "}\">");
							for (int i = 2; i <= columnCount; i++) {
								String colName = columnMetaData.getColumnName(i);
								if(!colList.contains(colName.toLowerCase())) {
									if(!isForeignKey(con, className,colName)) {
										if(columnMetaData.getColumnTypeName(i).equalsIgnoreCase("Date") || columnMetaData.getColumnTypeName(i).equalsIgnoreCase("datetime") || columnMetaData.getColumnTypeName(i).equalsIgnoreCase("timestamp"))
											writer.println("									<td th:text=\"${#dates.format("+CapitalCase.toCapitalCase(className).toLowerCase()+"."+colName.toLowerCase()+", 'yyyy-MM-dd')}\"></td>");
										else if(columnMetaData.getColumnTypeName(i).equalsIgnoreCase("LONGBLOB"))
										{
											writer.println("			<td class=\"profile-pic\">");
											writer.println("				<img th:src=\"@{${"+CapitalCase.replaceUnderScore(className).toLowerCase()+".fileDownloadPath}}\" class=\"profile-pic\">");
											writer.println("			</td>");
											i=i+2;
										}
										else
											writer.println("												<td th:text=\"${" +CapitalCase.toCapitalCase(className).toLowerCase()+ "."+colName.toLowerCase()+"}\"></td>");
									}
									else if(isForeignKey(con, className,colName)) {
	/*									String fkTableName = getFKTableName(listOfForeignKeys, className, columnMetaData.getColumnName(i));
										writer.println("												<td><th:block th:each=\""+fkTableName.toLowerCase()+" : ${" +CapitalCase.toCapitalCase(className).toLowerCase()+ "."+fkTableName.toLowerCase()+"}\">");
										writer.println("														<span th:text=\"${"+fkTableName.toLowerCase()+"."+colName+"} + ' '\">Item");
										writer.println("															description here...</span>");
										writer.println("													</th:block></td>");*/
									}
								}
							}
							writer.println("												<td><a th:href=\"@{'/" +CapitalCase.toCapitalCase(className).toLowerCase()+ "/view" + CapitalCase.toCapitalCase(className) + "/' + ${" +CapitalCase.toCapitalCase(className).toLowerCase()+ ".id}}\">");
							writer.println("													<i class=\"fa fa-eye\"></i></a></td>");
							//writer.println("												<td sec:authorize=\"hasRole('ADMIN')\">");
							writer.println("												<td>	<a th:href=\"@{'/" +CapitalCase.toCapitalCase(className).toLowerCase()+ "/edit" + CapitalCase.toCapitalCase(className) + "/' + ${" +CapitalCase.toCapitalCase(className).toLowerCase()+ ".id}}\">");
							writer.println("													<i class=\"fa fa-pencil-square-o\"></i></a></td>");
							//writer.println("												<td sec:authorize=\"hasRole('ADMIN')\">");
							writer.println("												<td>	<a th:href=\"@{'/" +CapitalCase.toCapitalCase(className).toLowerCase()+ "/delete" + CapitalCase.toCapitalCase(className) + "/' + ${" +CapitalCase.toCapitalCase(className).toLowerCase()+ ".id}}\"");
							writer.println("													th:data-method=\"delete\"><i class=\"fa fa-trash\"></a></td>");
							writer.println("											</tr>");
			 
			writer.println("										</tbody>");
			writer.println("									</table>");
			writer.println("									<div th:if=\"${pages.totalPages > 0}\" class=\"pagination\"");
			writer.println("										th:each=\"pageNumber : ${pageNumbers}\">");
			writer.println("										<a");
			writer.println("											th:href=\"@{/" +CapitalCase.toCapitalCase(className).toLowerCase()+ "/viewAll" + CapitalCase.toCapitalCase(className) + "(pageNo=${pageNumber}, pageSize=${pages.size}, sortBy="+sortColName+")}\"");
			writer.println("											th:text=${pageNumber}");
			writer.println("											th:class=\"${pageNumber==pages.number + 1} ? active\"></a>");
			writer.println("									</div>");
			writer.println("								</div>");
			writer.println("");
			writer.println("							</div>");
			writer.println("						</div>");
			writer.println("					</div>");
			writer.println("				</div>");
			writer.println("				<!-- /.container-fluid -->");
			writer.println("			</div>");
			writer.println("			<!-- /.content -->");
			writer.println("		</div>");
			writer.println("		<!-- End of Main Panel -->");
			writer.println("		<th:block th:include=\"fragments/footer\"></th:block>");
			writer.println("	</div>");
			writer.println("");
			writer.println("	<th:block th:include=\"fragments/scripts\"></th:block>");
			writer.println("</body>");
			writer.println("</html>");
			writer.close();
			
			String title = ReadProjectPropertiesFile.projectProps.getProperty("title");
			
			File fragmentsDir = new File(resourcePackage + "\\templates\\fragments");
			if (!fragmentsDir.exists())
				fragmentsDir.mkdir();
			
			writer = new PrintWriter(fragmentsDir.getAbsolutePath() + "\\head.html");
			
			writer.println("<head>");
			writer.println("    <meta charset=\"utf-8\" />");
			writer.println("    <link rel=\"icon\" type=\"image/png\" sizes=\"96x96\" href=\"/images/favicon.png\">");
			writer.println("    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />");
			writer.println("");
			writer.println("    <title>"+title+"</title>");
			writer.println("");
			writer.println("    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />");
			writer.println("    <meta name=\"viewport\" content=\"width=device-width\" />");
			writer.println("");
			writer.println("");
			writer.println("    <!-- Bootstrap core CSS     -->");
			writer.println("    <link th:href=\"@{/css/bootstrap.min.css}\" rel=\"stylesheet\" />");
			writer.println("");
			writer.println("    <!-- Animation library for notifications   -->");
			writer.println("    <link th:href=\"@{/css/animate.min.css}\" rel=\"stylesheet\"/>");
			writer.println("");
			writer.println("    <!--  Paper Dashboard core CSS    -->");
			writer.println("    <link th:href=\"@{/css/paper-dashboard.css}\" rel=\"stylesheet\"/>");
			writer.println("");
			writer.println("    <!--  CSS for Demo Purpose, don't include it in your project     -->");
			writer.println("    <link th:href=\"@{/css/demo.css}\" rel=\"stylesheet\" />");
			writer.println("");
			writer.println("    <!--  Fonts and icons     -->");
			writer.println("	<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">");
			writer.println("    <link href=\"https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css\" rel=\"stylesheet\">");
			writer.println("    <link href='https://fonts.googleapis.com/css?family=Muli:400,300' rel='stylesheet' type='text/css'>");
			writer.println("    <link th:href=\"@{/css/themify-icons.css}\" rel=\"stylesheet\">");
			writer.println("    <link th:rel=\"stylesheet\" th:href=\"@{/assets/bootstrap-datepicker/css/bootstrap-datepicker.css}\"/>");
			writer.println("    <link th:rel=\"stylesheet\"); th:href=\"@{/assets/bootstrap-datepicker/css/bootstrap-datepicker.standalone.css}\"/>");
			
			writer.println("<link rel=\"stylesheet\"	th:href=\"@{/assets/bootstrap/css/bootstrap.min.css}\" />");
			writer.println("<link rel=\"stylesheet\"	th:href=\"@{/assets/font-awesome-4.5.0/css/font-awesome.min.css}\" />");
			writer.println("<link rel=\"stylesheet\" th:href=\"@{/assets/css/styles.css}\" />");
			
			writer.println("</head>");
			writer.println("");
			writer.close();
			resultSet.close();
			pstmt.close();
			
			new ThymeleafHtmlHomeLayout(resourcePackage).createThymeLeafHomeLayouts();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getFKTableIdColName(Connection con, String fkTableName) {
		String pkIdCol="";
		try {
		PreparedStatement pstmt = con.prepareStatement("select * from " + fkTableName);
		ResultSet resultSet = pstmt.executeQuery();
		ResultSetMetaData columnMetaData = resultSet.getMetaData();
		DatabaseMetaData databaseMetaData = con.getMetaData();
		for (int i = 1; i <= columnMetaData.getColumnCount(); i++) {
			String colName = columnMetaData.getColumnName(i);
			if(isPrimaryKey(fkTableName,colName, databaseMetaData,con.getCatalog())) {
				pkIdCol = colName;
				break;
			}
		}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				resultSet.close();
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return pkIdCol;
	}
	
	private String getFKTableNameColName(Connection con, String fkTableName) {
		String pkIdCol="";
		try {
		PreparedStatement pstmt = con.prepareStatement("select * from " + fkTableName);
		ResultSet resultSet = pstmt.executeQuery();
		ResultSetMetaData columnMetaData = resultSet.getMetaData();
		DatabaseMetaData databaseMetaData = con.getMetaData();
		for (int i = 1; i <= columnMetaData.getColumnCount(); i++) {
			String colName = columnMetaData.getColumnName(i).toLowerCase();
			if(!isPrimaryKey(fkTableName,colName, databaseMetaData,con.getCatalog()) && !isForeignKey(con, fkTableName, colName) && colName.contains("name")) {
				pkIdCol = colName;
				break;
			}
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				resultSet.close();
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return pkIdCol;
	}

	private String getForeignKeyTableName(Connection con, String tableName,String name)throws Exception{
		ResultSet rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
		String fkClassName = "";
		while (rsf.next()) {
			String fkTableName = rsf.getString("PKTABLE_NAME");
			String fkColumnName = rsf.getString("FKCOLUMN_NAME");
			if (fkColumnName.equalsIgnoreCase(name)) {
				fkClassName = fkTableName;
				break;
			}
		}
			return fkClassName;
	}
	
	private List<String> getListOfForeignKeysTable(Connection con, String tableName)throws Exception{
		ResultSet rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
		List<String> keys = new ArrayList<>();
		while (rsf.next()) {
				keys.add(rsf.getString("PKTABLE_NAME").toLowerCase());
		}
			return keys;
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