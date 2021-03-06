/*
 * SecurityDAO.java
 *
 * 
 */

package com.jdbc.daoimpl;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.css.impl.CSSImpl;
import com.jdbc.dao.AbstractDataAccessObject;
import com.jdbc.main.CapitalCase;
import com.jdbc.main.ListOfDateString;
import com.jdbc.main.SqlDatatypeTOJavaWrapperClass;
import com.service.impl.ServiceImpl;
import com.servlet.impl.ServletImpl;
import com.springboot.supportfiles.SpringBootSupportImpl;
import com.springboot.util.InitServletImpl;
import com.springboot.util.UtilImpl;
import com.xml.impl.WebXmlImpl;

/**
 * 
 * @author
 */
public class JdbcDAO extends AbstractDataAccessObject {
	Connection con;
	PreparedStatement pstmt;
	ResultSet resultSet,rs,rsf,tableTypes,contraintsRecords;
	private String desc;
	private boolean flag;
	String schemaName = getProperties().getProperty("duser");
	String title = getProperties().getProperty("title");
	String pack = getProperties().getProperty("pack");
	ServiceImpl serviceImpl = new ServiceImpl(pack, schemaName);
	ServletImpl servletImpl = new ServletImpl(pack);
	InitServletImpl initServletImpl = new InitServletImpl(pack);
	//ThymeleafHtmlPagesImpl jspImpl = new ThymeleafHtmlPagesImpl(pack, title);
	//ThymeLeafMenuImpl menuImpl = new ThymeLeafMenuImpl(pack, title);
	CSSImpl cssImpl = new CSSImpl(pack, title);
	SpringBootSupportImpl supportImpl = new SpringBootSupportImpl(pack);
	//SpringBootSecurityImpl securityImpl = new SpringBootSecurityImpl(pack);
	WebXmlImpl webXmlImpl = new WebXmlImpl(pack,title);
	Map<String, Map<String, String>> outerMap = new HashMap<String, Map<String, String>>();
	/** Creates a new instance of SecurityDAO */
	public JdbcDAO() {
		con = getConnection();
	}

	public boolean readDataBaseDetails() {
		try {

			DatabaseMetaData databaseMetaData = con.getMetaData();

			System.out.println("Database Major Version : " + databaseMetaData.getDatabaseMajorVersion());
			System.out.println("Database Minor Version : " + databaseMetaData.getDatabaseMinorVersion());
			System.out.println("Database Product Name : " + databaseMetaData.getDatabaseProductName());
			System.out.println("Database Prodcut Version : " + databaseMetaData.getDatabaseProductVersion());
			System.out.println("Schema Name : " + databaseMetaData.getUserName());

			  tableTypes = databaseMetaData.getTableTypes();
			new UtilImpl(pack,title).createUtilityClass();
			//supportImpl.createSupportImplClasses();
			String catalog = con.getCatalog();
			
			while (tableTypes.next()) {
				if ("TABLE".equalsIgnoreCase(tableTypes.getString("TABLE_TYPE"))) {
					  rs = databaseMetaData.getTables(null, databaseMetaData.getUserName(), "%",
							new String[] { "TABLE" });
					while (rs.next()) {
						// System.out.println(rs.getString("TABLE_NAME"));
						Map<String, String> innerMap = new HashMap<String, String>();
						try {
							contraintsRecords = databaseMetaData.getExportedKeys(catalog, null, rs.getString("TABLE_NAME"));
							while (contraintsRecords.next()) {
								String fkSchemaName = contraintsRecords.getString("FKTABLE_SCHEM");
								if (fkSchemaName.equalsIgnoreCase(schemaName))
									innerMap.put(contraintsRecords.getString("FKTABLE_NAME"),
										contraintsRecords.getString("FKCOLUMN_NAME"));
							}
							outerMap.put(rs.getString("TABLE_NAME"), innerMap);
							// System.out.println("Foreign Keys:"+list);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
			}
			
			 tableTypes = databaseMetaData.getTableTypes();
			while (tableTypes.next()) {
				if ("TABLE".equalsIgnoreCase(tableTypes.getString("TABLE_TYPE"))) {
					  rs = databaseMetaData.getTables(null, databaseMetaData.getUserName(), "%",
							new String[] { "TABLE" });
					while (rs.next()) {
						tableCRUDOperations(rs.getString("TABLE_NAME"),databaseMetaData);
					}
					break;

				}
			}

			webXmlImpl.createWebXmlFile(con);
			//securityImpl.createSecurityImplClasses(con, title);;
			//menuImpl.createMenuFiles(con);
			cssImpl.createCSSFile1(con);
		} catch (SQLException ex) {
			ex.printStackTrace();
			flag = false;
		}
		return flag;
	}

	public void tableCRUDOperations(String tableName,DatabaseMetaData databaseMetaData) throws SQLException {
		createBeanClass(tableName);
		prepareDAOLayer(tableName);
		prepareDatabaseConnectionClass();
		createPropertiesFile();
		createDateWraperClass();
		//new ExceptionImpl(pack).createExceptionClasses(tableName);
		serviceImpl.createServiceImplClasses(tableName, con);
		servletImpl.createServletImplClasses(tableName, con,title);
		//Map<String, Map<String, String>> listOfForeignKeys = jspImpl.getAllForiegnKeys(con);
		//jspImpl.createJSPImplClasses(tableName, con,outerMap);
	}

	private void createPropertiesFile() {

		try {
			String packageNameString = ".\\jsp\\WEB-INF\\properties\\";
			File packageDir = new File(packageNameString);
			packageDir.mkdir();
			
			PrintWriter writer = new PrintWriter(packageNameString+title+".properties");
			writer.println("# Database properties \n");
			writer.println("driver=oracle.jdbc.driver.OracleDriver");
			writer.println("url=jdbc:oracle:thin:@localhost:1521:xe");
			writer.println("duser=" + pack);
			writer.println("dpass=" + pack);
			writer.println("pack=test");
			writer.println("# Other properties \n");
			writer.println("logfile=C:/log/sample_log.txt");
			writer.println("recordsPerPage=10");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createDateWraperClass() {
		String packageNameString = ".\\com\\" + pack + "\\util";
		File packageDir = new File(packageNameString);
		packageDir.mkdir();

		try {
			PrintWriter writer = new PrintWriter(packageNameString + "\\DateWrapper.java");

			String packageImport = "package com." + pack + ".util;\n\n";
			packageImport += "import java.util.Date;\n";

			writer.println(packageImport);
			writer.println("public class DateWrapper {");
			writer.println(
					"	static String month[] = { \"Jan\", \"Feb\", \"Mar\", \"Apr\", \"May\", \"Jun\", \"Jul\",");
			writer.println("			\"Aug\", \"Sep\", \"Oct\", \"Nov\", \"Dec\" };\n ");
			writer.println("	public DateWrapper() {");
			writer.println("		}");
			writer.println("	public static String parseDate(Date date) {");
			writer.println("		int monthid = date.getMonth();");
			writer.println("		String newdate = date.getDate() + \"-\" + month[monthid] + \"-\"");
			writer.println("				+ (date.getYear() + 1900);");
			writer.println("		return newdate;");
			writer.println("	}");
			writer.println("	public static String parseDate(String date) {");
			writer.println("		int monthid = Integer.parseInt(date.substring(date.indexOf(\"-\") + 1,");
			writer.println("				date.lastIndexOf(\"-\")));");
			writer.println("		String newdate = date.substring(0, date.indexOf(\"-\")) + \"-\"");
			writer.println("				+ month[monthid - 1] + \"-\"");
			writer.println("				+ (date.substring(date.lastIndexOf(\"-\") + 1, date.length()));");
			writer.println("		return newdate;");
			writer.println("	}");
			writer.println("	public static String parseDate(java.sql.Date date) {");
			writer.println("		String olddate = date.toString();");
			writer.println("		String newdate = olddate.substring(olddate.lastIndexOf(\"-\") + 1,");
			writer.println("				olddate.length())");
			writer.println("				+ \"-\"");
			writer.println("				+ olddate.substring(olddate.indexOf(\"-\") + 1, olddate");
			writer.println("						.lastIndexOf(\"-\"))");
			writer.println("				+ \"-\"");
			writer.println("				+ olddate.substring(0, olddate.indexOf(\"-\"));");
			writer.println("		return newdate;");
			writer.println("	}");
			writer.println("	public static java.sql.Date convertFromJAVADateToSQLDate(Date javaDate) {");
			writer.println("	java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime());");
			writer.println("	return sqlDate;");
			writer.println("	}");
			writer.println("}");
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void prepareDatabaseConnectionClass() {
		String packageNameString = ".\\com\\" + pack + "\\dao";
		File packageDir = new File(packageNameString);
		packageDir.mkdir();

		try {
			PrintWriter writer = new PrintWriter(packageNameString + "\\AbstractDataAccessObject.java");
			String packageImport = "package com." + pack + ".dao;\n";
			packageImport += "import java.sql.*; \n";
			packageImport += "import java.sql.DriverManager;\n";
			packageImport += "import java.util.Properties;\n";

			writer.println(packageImport);
			writer.println("public class AbstractDataAccessObject {\n");
			writer.println("	private Connection connection;");
			writer.println("	private static Properties properties;");
			writer.println("	public Properties getProperties()\n");
			writer.println("	{");
			writer.println("		return properties;");
			writer.println("	}");
			writer.println("\n");
			writer.println("	public void setProperties(Properties properties)");
			writer.println("	{");
			writer.println("		this.properties = properties;");
			writer.println("	}");
			writer.println("\n");
			writer.println("	public Connection getConnection()");
			writer.println("    {");
			writer.println("			try");
			writer.println("			{");
			writer.println("				Properties properties = getProperties();");
			writer.println("				Class.forName(properties.getProperty(\"driver\"));");
			writer.println(
					"				connection = DriverManager.getConnection(properties.getProperty(\"url\"), properties.getProperty(\"duser\"), properties.getProperty(\"dpass\"));");
			writer.println("			}");
			writer.println("			catch (Exception e)");
			writer.println("			{");
			writer.println("				System.out.println(e);");
			writer.println("			}");
			writer.println("		return connection;");
			writer.println("	}");
			writer.println("  }");
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void prepareSelectQueryById(String tableName, PrintWriter writer) {
		try {

			pstmt = con.prepareStatement("select * from " + tableName);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();
			int columnCount = columnMetaData.getColumnCount();

			writer.append("\n");
			writer.append("\n");
			writer.append("			public " + CapitalCase.toCapitalCase(tableName) + "TO view"
					+ CapitalCase.toCapitalCase(tableName) + "("
					+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(1)) + " "
					+ columnMetaData.getColumnName(1).toLowerCase() + ") {\n");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._DAO_START_VIEW_"+ tableName.replace("_", "").toUpperCase() + "_LOG);");
			
			writer.append("				" + CapitalCase.toCapitalCase(tableName) + "TO " + tableName.toLowerCase()
					+ " = new " + CapitalCase.toCapitalCase(tableName) + "TO();\n");
			writer.append("				try {\n");
			writer.append("					preparedStatement = con.prepareStatement(\"select * from " + tableName
					+ " where " + columnMetaData.getColumnName(1) + " = \"+"
					+ columnMetaData.getColumnName(1).toLowerCase() + ");\n");
			writer.append("					resultSet = preparedStatement.executeQuery();\n");

			writer.append("					if(resultSet.next()) {\n");
			writer.append("						" + tableName.toLowerCase() + " = new "
					+ CapitalCase.toCapitalCase(tableName) + "TO();\n");
			
			String cataLog = con.getCatalog();
			DatabaseMetaData databaseMetaData = con.getMetaData();
			ResultSet rsf1 = databaseMetaData.getImportedKeys(cataLog, null, tableName);
			Set<String> listPK = new HashSet<String>();
			while (rsf1.next()) {
					listPK.add(CapitalCase.replaceUnderScore(rsf1.getString("PKTABLE_NAME")));
			}
			rsf1.close();
			
			for (int i = 1; i <= columnCount; i++) {
				if(listPK.size()!=0 && isForeignKey(tableName, columnMetaData.getColumnName(i), databaseMetaData, cataLog, outerMap)) {
					for (String string : listPK) {
					writer.println("							" + tableName.toLowerCase() + ".set"
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)) + "(" + "resultSet.get"
							+ SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i)) + "(" + i
							+ "));");
					writer.println("							" + tableName.toLowerCase() + ".set"
							+ CapitalCase.toCapitalCase(string) + "(new " + CapitalCase.toCapitalCase(string) + "DaoImpl(con).view"+CapitalCase.toCapitalCase(string)+ "(resultSet.get"
							+ SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i)) + "(" + i
							+ ")));");
					listPK.remove(string);
					break;
					}
				}
				else
				writer.println("							" + tableName.toLowerCase() + ".set"
						+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)) + "(" + "resultSet.get"
						+ SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i)) + "(" + i
						+ "));");
			}
			writer.append("					}\n");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._DAO_END_VIEW_"+ tableName.replace("_", "").toUpperCase() + "_LOG);");
			
			writer.append("				} catch (SQLException e) {\n");
			writer.println("			LoggerManager.writeLogSevere(e);");
			writer.append("					e.printStackTrace();\n");
			writer.append("				} finally {\n");
			writer.append("					/*try {\n");
			writer.append("						if (con != null)\n");
			writer.append("							con.close();\n");
			writer.append("					} catch (SQLException e) {\n");
			writer.append("						System.out.println(\"Exception raised\" + e);\n");
			writer.append("					}*/\n");
			writer.append("				}\n");
			writer.append("				return " + tableName.toLowerCase() + ";\n");
			writer.append("			}\n");
			writer.append("\n");
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// do something
		}
	}

	private void prepareSelectQuery(String tableName, PrintWriter writer) {
		try {

			pstmt = con.prepareStatement("select * from " + tableName);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();
			int columnCount = columnMetaData.getColumnCount();

			writer.append("\n");
			writer.append("\n");
			writer.append("			public List<" + CapitalCase.toCapitalCase(tableName) + "TO> view"
					+ CapitalCase.toCapitalCase(tableName) + "() {\n");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._DAO_START_VIEW_"+ tableName.replace("_", "").toUpperCase() + "_LOG);");
			writer.append("				List<" + CapitalCase.toCapitalCase(tableName) + "TO> list"
					+ CapitalCase.toCapitalCase(tableName) + " = new ArrayList<" + CapitalCase.toCapitalCase(tableName)
					+ "TO>();\n");
			writer.append("				" + CapitalCase.toCapitalCase(tableName) + "TO " + tableName.toLowerCase()
					+ " = null;\n				try {\n");
			writer.append("					preparedStatement = con.prepareStatement(\"select * from " + tableName
					+ " order by " + columnMetaData.getColumnName(1) + "\");\n");
			writer.append("					resultSet = preparedStatement.executeQuery();\n");
			writer.append("					while(resultSet.next()) {\n");
			writer.append("						" + tableName.toLowerCase() + " = new "
					+ CapitalCase.toCapitalCase(tableName) + "TO();\n");
			String cataLog = con.getCatalog();
			DatabaseMetaData databaseMetaData = con.getMetaData();
			ResultSet rsf1 = databaseMetaData.getImportedKeys(cataLog, null, tableName);
			Map<Integer,String> listPK = new HashMap<Integer,String>();
			Set<String> setPK = new HashSet<String>();
			int j = 1;
			while (rsf1.next()) {
					if(setPK.contains(rsf1.getString("PKTABLE_NAME"))) {
						listPK.put(j,CapitalCase.replaceUnderScore(rsf1.getString("PKTABLE_NAME")));
						j++;
					}
						setPK.add(rsf1.getString("PKTABLE_NAME"));
			}
			rsf1.close();
			for (int i = 1; i <= columnCount; i++) {
				if(listPK.size()!=0 && isForeignKey(tableName, columnMetaData.getColumnName(i), databaseMetaData, cataLog, outerMap)) {
					for (String string : setPK) {
					writer.println("							" + tableName.toLowerCase() + ".set"
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)) + "(" + "resultSet.get"
							+ SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i)) + "(" + i
							+ "));");
					writer.println("							" + tableName.toLowerCase() + ".set"
							+ CapitalCase.toCapitalCase(string) + "(new " + CapitalCase.toCapitalCase(string) + "DaoImpl(con).view"+CapitalCase.toCapitalCase(string)+ "(resultSet.get"
							+ SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i)) + "(" + i
							+ ")));");
					setPK.remove(string);
					break;
					}
				}
				else
				writer.append("						" + tableName.toLowerCase() + ".set"
						+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)) + "(resultSet.get"
						+ SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i)) + "(" + i
						+ "));\n");
			}
			writer.append("					list" + CapitalCase.toCapitalCase(tableName) + ".add("
					+ tableName.toLowerCase() + ");\n");
			writer.append("					}\n");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._DAO_END_VIEW_"+ tableName.replace("_", "").toUpperCase() + "_LOG);");
			
			writer.append("				} catch (SQLException e) {\n");
			writer.append("					e.printStackTrace();\n");
			writer.println("			LoggerManager.writeLogSevere(e);");
			writer.append("				} finally {\n");
			writer.append("					/*try {\n");
			writer.append("						if (con != null)\n");
			writer.append("							con.close();\n");
			writer.append("					} catch (SQLException e) {\n");
			writer.append("						System.out.println(\"Exception raised\" + e);\n");
			writer.append("					}*/\n");
			writer.append("				}\n");
			writer.append("				return list" + CapitalCase.toCapitalCase(tableName) + ";\n");
			writer.append("			}\n");
			writer.append("\n");
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// do something
		}
	}

	private void prepareSelectQueryWithForeignKeys(String tableName, PrintWriter writer, String foreignkey,
			String fkTableName) {
		try {

			pstmt = con.prepareStatement("select * from " + fkTableName);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();
			int columnCount = columnMetaData.getColumnCount();

			writer.append("\n");
			writer.append("\n");
			writer.append("			public List<" + CapitalCase.toCapitalCase(fkTableName) + "TO> view"	+ CapitalCase.toCapitalCase(fkTableName) + "By" + CapitalCase.toCapitalCase(tableName) + "(int "
					+ foreignkey.toLowerCase() + ") {\n");
			//writer.println("			LoggerManager.writeLogInfo(UtilConstants._DAO_START_VIEW_"	+ CapitalCase.toCapitalCase(fkTableName).toUpperCase() + "BY" + CapitalCase.toCapitalCase(tableName).toUpperCase() + "_LOG);");
			writer.append("				List<" + CapitalCase.toCapitalCase(fkTableName) + "TO> list"
					+ CapitalCase.toCapitalCase(fkTableName) + " = new ArrayList<" + CapitalCase.toCapitalCase(fkTableName)
					+ "TO>();\n");
			writer.append("				" + CapitalCase.toCapitalCase(fkTableName) + "TO " + fkTableName.toLowerCase()
					+ " = null;\n				try {\n");
			writer.append("					preparedStatement = con.prepareStatement(\"select * from "
					+ fkTableName.toLowerCase() + " where " + foreignkey.toLowerCase() + "=\"+"
					+ foreignkey.toLowerCase() + "+\" order by " + foreignkey.toLowerCase() + "\");\n");
			writer.append("					resultSet = preparedStatement.executeQuery();\n");
			writer.append("					while(resultSet.next()) {\n");
			writer.append("						" + fkTableName.toLowerCase() + " = new "
					+ CapitalCase.toCapitalCase(fkTableName) + "TO();\n");
			for (int i = 1; i <= columnCount; i++) {
				writer.append("						" + fkTableName.toLowerCase() + ".set"
						+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)) + "(resultSet.get"
						+ SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i)) + "(" + i
						+ "));\n");
			}
			writer.append("					list" + CapitalCase.toCapitalCase(fkTableName) + ".add("
					+ fkTableName.toLowerCase() + ");\n");
			writer.append("					}\n");
			//writer.println("			LoggerManager.writeLogInfo(UtilConstants._DAO_END_VIEW_"	+ CapitalCase.toCapitalCase(fkTableName).toUpperCase() + "BY" + CapitalCase.toCapitalCase(tableName) + "_LOG);");
			
			writer.append("				} catch (SQLException e) {\n");
			writer.append("					e.printStackTrace();\n");
			writer.println("			LoggerManager.writeLogSevere(e);");
			writer.append("				} finally {\n");
			writer.append("					/*try {\n");
			writer.append("						if (con != null)\n");
			writer.append("							con.close();\n");
			writer.append("					} catch (SQLException e) {\n");
			writer.append("						System.out.println(\"Exception raised\" + e);\n");
			writer.append("					}*/\n");
			writer.append("				}\n");
			writer.append("				return list" + CapitalCase.toCapitalCase(fkTableName) + ";\n");
			writer.append("			}\n");
			writer.append("\n");
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// do something
		}
	}

	private void prepareDAOLayer(String tableName) {
		try {

			pstmt = con.prepareStatement("select * from " + tableName);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();
			int columnCount = columnMetaData.getColumnCount();
			// The column count starts from 1

			String packageNameString = ".\\com\\" + pack + "\\daoimpl";
			File packageDir = new File(packageNameString);
			packageDir.mkdir();

			PrintWriter writer = new PrintWriter(
					packageNameString + "\\" + CapitalCase.toCapitalCase(tableName) + "DaoImpl.java");

			String packageImport = "package com." + pack + ".daoimpl;\n";
			packageImport += "import java.sql.*; \n";
			packageImport += "import java.util.List; \n";
			packageImport += "import java.util.ArrayList; \n";
			packageImport += "import java.util.Date; \n\n";
			packageImport += "import com."+pack+".util.LoggerManager;\n";
			packageImport += "import com." + pack + ".util.DateWrapper; \n";
			packageImport += "import com." + pack + ".util.UtilConstants; \n";
			packageImport += "import com." + pack + ".dao.AbstractDataAccessObject;\n";
			packageImport += "import com." + pack + ".bean." + CapitalCase.toCapitalCase(tableName) + "TO;\n";
			DatabaseMetaData databaseMetaData = con.getMetaData();
			String cataLog = con.getCatalog();
			  rsf = databaseMetaData.getExportedKeys(cataLog, null, tableName);
			List<String> duplicateString = new ArrayList<String>();
			while (rsf.next()) {
				String fkTableName = rsf.getString("FKTABLE_NAME");
				String fkColumnName = rsf.getString("FKCOLUMN_NAME");
				String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
				if (!duplicateString.contains(fkTableName) && fkSchemaName.equalsIgnoreCase(schemaName))
					packageImport += "import com." + pack + ".bean." + CapitalCase.toCapitalCase(fkTableName) + "TO;\n";
				duplicateString.add(fkTableName);
			}
			writer.println(packageImport);
			writer.println("public class " + CapitalCase.toCapitalCase(tableName) + "DaoImpl {");
			writer.println("\n");
			writer.println("	Connection con = null;");
			writer.println("	PreparedStatement preparedStatement = null;");
			writer.println("	Statement statement = null;");
			writer.println("	ResultSet resultSet = null;");
			writer.println("	boolean flag = false;\n");

			writer.println("	private static final long serialVersionUID = 1L;\n");
			writer.println("\n");

			writer.println("	public " + CapitalCase.toCapitalCase(tableName) + "DaoImpl(Connection con) {");
			writer.println("		this.con = (con != null)? con : new AbstractDataAccessObject().getConnection();");
			writer.println("	}");
			
			writer.println("	public " + CapitalCase.toCapitalCase(tableName) + "DaoImpl() {");
			writer.println("		this.con = new AbstractDataAccessObject().getConnection();");
			writer.println("	}");

			writer.println("	public boolean add" + CapitalCase.toCapitalCase(tableName) + "Details("
					+ CapitalCase.toBeanClass(tableName) + " " + tableName.toLowerCase() + ") {");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._DAO_START_ADD_"+ tableName.replace("_", "").toUpperCase() + "_LOG);");
			
			writer.println("		try {");
			String sqlQuery = "insert into " + tableName + " values((select nvl(max(" + columnMetaData.getColumnName(1)
					+ "),100)+1 from " + tableName + ")";

			for (int i = 2; i <= columnCount; i++) {
				if("DATE".equals(columnMetaData.getColumnTypeName(i)) && !ListOfDateString.checkDateString(columnMetaData.getColumnName(i),tableName))
					sqlQuery += ",sysdate";
				else
					sqlQuery += ",?";
			}
			sqlQuery += ")";

			writer.println("		preparedStatement = con.prepareStatement(\"" + sqlQuery + "\");");
			int pstmtCount = 0;
			for (int i = 2; i <= columnCount; i++) {

				String statement = "";
				if ("DATE".equals(columnMetaData.getColumnTypeName(i)) && !ListOfDateString.checkDateString(columnMetaData.getColumnName(i),tableName)) {
					/*statement = "		preparedStatement.set"
							+ SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i)) + "("
							+ ++pstmtCount + "," + "DateWrapper.convertFromJAVADateToSQLDate(" + tableName.toLowerCase() + ".get"
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)) + "()));";*/
					// ++pstmtCount;

				} else if ("DATE".equals(columnMetaData.getColumnTypeName(i)) && ListOfDateString.checkDateString(columnMetaData.getColumnName(i),tableName)) {
					statement = "		preparedStatement.set"
							+ SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i)) + "("
							+ ++pstmtCount + "," + "DateWrapper.convertFromJAVADateToSQLDate(" + tableName.toLowerCase() + ".get"
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)) + "()));";

				} else {
					statement = "		preparedStatement.set"
							+ SqlDatatypeTOJavaWrapperClass.toPrimitiveClass(columnMetaData.getColumnTypeName(i)) + "("
							+ ++pstmtCount + "," + tableName.toLowerCase() + ".get"
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)) + "());";
				}
				writer.println(statement);
			}
			writer.println("			int results = preparedStatement.executeUpdate();");
			writer.println("			if(results>0){");
			writer.println("				flag = true;");
			writer.println("				con.commit();");
			writer.println("				}");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._DAO_END_ADD_"+ tableName.replace("_", "").toUpperCase() + "_LOG);");
			writer.println("			} catch (SQLException e) {");
			writer.println("			LoggerManager.writeLogSevere(e);");
			writer.println("				e.printStackTrace();");
			writer.println("			} finally {");
			writer.println("			/*try {");
			writer.println("				if (con != null)");
			writer.println("				con.close();");
			writer.println("			} catch (SQLException e) {");
			writer.println("			}*/");
			writer.println("			return flag;");
			writer.println("		}");
			writer.println("  }");
			prepareSelectQuery(tableName, writer);
			databaseMetaData = con.getMetaData();
			cataLog = con.getCatalog();
			rsf = databaseMetaData.getExportedKeys(cataLog, null, tableName);
			duplicateString = new ArrayList<String>();
			while (rsf.next()) {
				String fkTableName = rsf.getString("FKTABLE_NAME");
				String fkColumnName = rsf.getString("FKCOLUMN_NAME");
				String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
				if (!duplicateString.contains(fkTableName) && fkSchemaName.equalsIgnoreCase(schemaName))
					prepareSelectQueryWithForeignKeys(tableName, writer, fkColumnName, fkTableName);
				duplicateString.add(fkTableName);
			}
			prepareUpdateQuery(tableName, writer);
			prepareDeleteQuery(tableName, writer);
			prepareSelectQueryById(tableName, writer);
			writer.println("  }");
			writer.close();
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// do something
		}

	}

	private void prepareDeleteQuery(String tableName, PrintWriter writer) {
		try {

			pstmt = con.prepareStatement("select * from " + tableName);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();
			writer.append("	public boolean delete" + CapitalCase.toCapitalCase(tableName) + "("
					+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(1)) + " "
					+ columnMetaData.getColumnName(1).toLowerCase() + ") {\n");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._DAO_START_DELETE_"+ tableName.replace("_", "").toUpperCase() + "_LOG);");
			
			writer.append("		try {\n");
			String sqlQuery = "delete " + tableName + " where " + columnMetaData.getColumnName(1) + " = \"+"
					+ columnMetaData.getColumnName(1).toLowerCase();

			writer.append("		preparedStatement = con.prepareStatement(\"" + sqlQuery + ");\n");
			writer.append("			int results = preparedStatement.executeUpdate();\n");
			writer.append("			if(results>0){\n");
			writer.append("				flag = true;\n");
			writer.append("				con.commit();\n");
			writer.append("				}\n");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._DAO_END_DELETE_"+ tableName.replace("_", "").toUpperCase() + "_LOG);");
			writer.append("			} catch (SQLException e) {\n");
			writer.println("			LoggerManager.writeLogSevere(e);");
			writer.append("				e.printStackTrace();\n");
			writer.append("			} finally {\n");
			writer.append("			/*try {\n");
			writer.append("				if (con != null)\n");
			writer.append("				con.close();\n");
			writer.append("			} catch (SQLException e) {\n");
			writer.append("			}*/\n");
			writer.append("			return flag;\n");
			writer.append("		}\n");
			writer.append("  }\n");
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// do something
		}
	}

	private void prepareUpdateQuery(String tableName, PrintWriter writer) {
		try {

			pstmt = con.prepareStatement("select * from " + tableName);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();
			int columnCount = columnMetaData.getColumnCount();
			writer.append("	public boolean update" + CapitalCase.toCapitalCase(tableName) + "("
					+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(1)) + " "
					+ columnMetaData.getColumnName(1).toLowerCase() + ", " + CapitalCase.toBeanClass(tableName) + " "
					+ tableName.toLowerCase() + ") {\n");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._DAO_START_UPDATE_"+ tableName.replace("_", "").toUpperCase() + "_LOG);");
			
			writer.append("		try {\n");
			String sqlQuery = "update " + tableName + " set ";
			for (int i = 2; i <= columnCount; i++) {
				if("DATE".equals(columnMetaData.getColumnTypeName(i)) && !ListOfDateString.checkDateString(columnMetaData.getColumnName(i),tableName))
					sqlQuery += columnMetaData.getColumnName(i) + " = sysdate ";
				else if("NUMBER".equals(columnMetaData.getColumnTypeName(i)))
					sqlQuery += columnMetaData.getColumnName(i) + " = \"+" + tableName.toLowerCase() + ".get"
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)) + "()+\"";
				else
					sqlQuery += columnMetaData.getColumnName(i) + " = '\"+" + tableName.toLowerCase() + ".get"
							+ CapitalCase.toCapitalCase(columnMetaData.getColumnName(i)) + "()+\"'";
				if (i < columnCount & columnCount > 2)
					sqlQuery += ",";
			}
			
			sqlQuery += " where " + columnMetaData.getColumnName(1) + " = \"+"
					+ columnMetaData.getColumnName(1).toLowerCase();

			writer.append("		preparedStatement = con.prepareStatement(\"" + sqlQuery + ");\n");
			writer.append("			int results = preparedStatement.executeUpdate();\n");
			writer.append("			if(results>0){\n");
			writer.append("				flag = true;\n");
			writer.append("				con.commit();\n");
			writer.append("				}\n");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._DAO_END_UPDATE_"+ tableName.replace("_", "").toUpperCase() + "_LOG);");
			
			writer.append("			} catch (SQLException e) {\n");
			writer.println("			LoggerManager.writeLogSevere(e);");
			writer.append("				e.printStackTrace();\n");
			writer.append("			} finally {\n");
			writer.append("			/*try {\n");
			writer.append("				if (con != null)\n");
			writer.append("				con.close();\n");
			writer.append("			} catch (SQLException e) {\n");
			writer.append("			}*/\n");
			writer.append("			return flag;\n");
			writer.append("		}\n");
			writer.append("  }\n");
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// do something
		}
	}

	private void createBeanClass(String tableName) {
		try {
			pstmt = con.prepareStatement("select * from " + tableName);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();
			int columnCount = columnMetaData.getColumnCount();
			// The column count starts from 1

			String packageNameString = ".\\com\\";
			File packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + pack;
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + "\\bean";
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			PrintWriter writer = new PrintWriter(
					packageNameString + "\\" + CapitalCase.toCapitalCase(tableName) + "TO.java");

			String packageImport = "package com." + pack + ".bean;\n";
			packageImport += "import java.io.Serializable; \n";
			packageImport += "import java.util.Date; \n";
			packageImport += "import java.sql.Blob; \n";
			packageImport += "import java.util.List; \n";
			writer.println(packageImport);
			writer.println("public class " + CapitalCase.toBeanClass(tableName) + " implements Serializable {");
			writer.println("\n");
			writer.println("	private static final long serialVersionUID = 1L;\n");
			writer.println("\n");

			for (int i = 1; i <= columnCount; i++) {
				String name = columnMetaData.getColumnName(i);
				writer.println("	private "
						+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(i)) + " "
						+ name.toLowerCase() + ";");
				// Do stuff with name
			}

			 rsf = con.getMetaData().getExportedKeys(con.getCatalog(), null, tableName);
			List<String> duplicateString = new ArrayList<String>();
			while (rsf.next()) {
				// System.out.println(" PK : "+rsf.getString("PKTABLE_NAME")+" FK :
				// "+rsf.getString("FKTABLE_NAME")+" SCHEM "+rsf.getString("FKTABLE_SCHEM"));
				String fkTableName = rsf.getString("FKTABLE_NAME");
				String replacefk = CapitalCase.replaceUnderScore(rsf.getString("FKTABLE_NAME"));
				String fkColumnName = rsf.getString("FKCOLUMN_NAME");
				String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
				if (!duplicateString.contains(fkTableName) && fkSchemaName.equalsIgnoreCase(schemaName))
					writer.println("	private List<" + CapitalCase.toBeanClass(fkTableName) + "> "
							+ replacefk.toLowerCase() + ";");
				duplicateString.add(fkTableName);
			}
			
				rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
				List<String> pkString = new ArrayList<String>();
				while (rsf.next()) {
					String pkTableName = rsf.getString("PKTABLE_NAME");
					String replacepk = CapitalCase.replaceUnderScore(rsf.getString("PKTABLE_NAME"));
					String pkSchemaName = rsf.getString("FKTABLE_SCHEM");
					if (!pkString.contains(pkTableName) && pkSchemaName.equalsIgnoreCase(schemaName))
						writer.println("	"+CapitalCase.toBeanClass(pkTableName) + " "
								+ replacepk.toLowerCase() + ";");
					pkString.add(pkTableName);
				}

			for (int i = 1; i <= columnCount; i++) {
				String name = columnMetaData.getColumnName(i);
				writer.println("\n");
				writer.println("	public void set" + CapitalCase.toCapitalCase(name) + "("
						+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(i)) + " "
						+ name.toLowerCase() + "){");
				writer.println("		this." + name.toLowerCase() + " = " + name.toLowerCase() + ";");
				writer.println("	}");
				writer.println(
						"	public " + SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(i))
								+ " get" + CapitalCase.toCapitalCase(name) + "(){");
				writer.println("		return " + name.toLowerCase() + ";");
				writer.println("	}");
			}

			rsf = con.getMetaData().getExportedKeys(con.getCatalog(), null, tableName);
			duplicateString = new ArrayList<String>();
			
			while (rsf.next()) {
				String fkTableName = rsf.getString("FKTABLE_NAME");
				String replacefk = CapitalCase.replaceUnderScore(rsf.getString("FKTABLE_NAME"));
				String fkColumnName = rsf.getString("FKCOLUMN_NAME");
				writer.println("\n");
				String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
				if (!duplicateString.contains(fkTableName) && fkSchemaName.equalsIgnoreCase(schemaName)) {
					writer.println("	public void set" + CapitalCase.toCapitalCase(replacefk.toLowerCase()) + "(List<"
							+ CapitalCase.toBeanClass(fkTableName) + "> " + replacefk.toLowerCase() + "){");
					writer.println("		this." + replacefk.toLowerCase() + " = " + replacefk.toLowerCase() + ";");
					writer.println("	}");
					writer.println("	public List<" + CapitalCase.toBeanClass(replacefk) + "> get"
							+ CapitalCase.toCapitalCase(replacefk.toLowerCase()) + "(){");
					writer.println("		return " + replacefk.toLowerCase() + ";");
					writer.println("	}");
				}
				duplicateString.add(fkTableName);
			}
			
			
			rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
			pkString = new ArrayList<String>();
			while (rsf.next()) {
				String pkTableName = rsf.getString("PKTABLE_NAME");
				String replacepk = CapitalCase.replaceUnderScore(rsf.getString("PKTABLE_NAME"));
				String pkSchemaName = rsf.getString("FKTABLE_SCHEM");
				if (!pkString.contains(pkTableName) && pkSchemaName.equalsIgnoreCase(schemaName)) {
					writer.println("	public void set" + CapitalCase.toCapitalCase(replacepk.toLowerCase()) + "("
							+ CapitalCase.toBeanClass(pkTableName) + " " + replacepk.toLowerCase() + "){");
					writer.println("		this." + replacepk.toLowerCase() + " = " + replacepk.toLowerCase() + ";");
					writer.println("	}");
					writer.println("	public " + CapitalCase.toBeanClass(replacepk) + " get"
							+ CapitalCase.toCapitalCase(replacepk.toLowerCase()) + "(){");
					writer.println("		return " + replacepk.toLowerCase() + ";");
					writer.println("	}");
				}
				pkString.add(pkTableName);
			}
			
			
			/*rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
//			pkString = new ArrayList<String>();
			while (rsf.next()) {
				String pkTableName = rsf.getString("PKTABLE_NAME");
				String replacepk = CapitalCase.replaceUnderScore(rsf.getString("PKTABLE_NAME"));
				writer.println("\n");
//				pkString.add(pkTableName);
				String pkSchemaName = rsf.getString("FKTABLE_SCHEM");
				
			}*/

			writer.println("}");
			writer.close();
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void printPrimearyKeys(DatabaseMetaData databaseMetaData, String schemaName, String tableName)
			throws SQLException {

		System.out.println(tableName);
		ResultSet rs = databaseMetaData.getPrimaryKeys(null, schemaName, tableName);
		while (rs.next()) {
			System.out.println("PK Name: " + rs.getString("COLUMN_NAME") + "   " + rs.getString("PK_NAME"));
		}
	}

	private static void printForeignKeys(DatabaseMetaData databaseMetaData, String schemaName, String tableName)
			throws SQLException {
		ResultSet rsf = databaseMetaData.getExportedKeys(null, schemaName, tableName);
		while (rsf.next()) {
			// System.out.println(rsf.getString(3)+" "+rsf.getString(4)+"
			// "+rsf.getString(5)+" "+rsf.getString(6)+" "+rsf.getString(7)+"
			// "+rsf.getString(8));
			System.out.println("FK Name: " + rsf.getString(7) + " " + rsf.getString(8));
		}
		System.out.println("----------------------------------------");
	}

	public boolean isForeignKey(String tableName, String columnName, DatabaseMetaData databaseMetaData, String cataLog,
			Map<String, Map<String, String>> listOfForeignKeys) {
		boolean flag = false;
		System.out.println(tableName);
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

}
