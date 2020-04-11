package com.servlet.impl;

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

import com.jdbc.dao.AbstractDataAccessObject;
import com.jdbc.main.CapitalCase;
import com.jdbc.main.SqlDatatypeTOJavaWrapperClass;

public class ServletImpl extends AbstractDataAccessObject{
	
	PreparedStatement pstmt;
	String pack;
	ResultSet resultSet, rs, rsf, tableTypes, contraintsRecords;
	String schemaName = getProperties().getProperty("duser");
	
	public ServletImpl(String pack) {
		super();
		this.pack = pack;
	}
	
	public void createServletImplClasses(String className,Connection con,String title) throws SQLException{
		try {
			pstmt = con.prepareStatement("select * from " + className);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();
			String packageNameString = ".\\com\\";
			File packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + pack;
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + "\\action";
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			PrintWriter writer = new PrintWriter(packageNameString + "\\Add"
					+ CapitalCase.toCapitalCase(className) + "Action.java");

			String packageImport = "package com." + pack + ".action;\n";
			packageImport += "import java.io.IOException; \n";
			packageImport += "import java.util.Map; \n";
			packageImport += "import java.util.List; \n";
			packageImport += "import com."+pack+".util.LoggerManager;\n";
			packageImport += "import javax.servlet.RequestDispatcher; \n";
			packageImport += "import javax.servlet.ServletException; \n";
			packageImport += "import javax.servlet.http.HttpServlet; \n";
			packageImport += "import javax.servlet.http.HttpServletRequest; \n";
			packageImport += "import javax.servlet.http.HttpServletResponse; \n";
			DatabaseMetaData databaseMetaData = con.getMetaData();
			String cataLog = con.getCatalog();
			rsf = databaseMetaData.getImportedKeys(cataLog, null, className);
			ArrayList<String> duplicateString = new ArrayList<String>();
			while (rsf.next()) {
				String ffkTableName = rsf.getString("PKTABLE_NAME");
				//String fkColumnName = rsf.getString("FKCOLUMN_NAME");
				String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
				if(fkSchemaName == null) fkSchemaName = "";
				if (!duplicateString.contains(ffkTableName) && fkSchemaName.equalsIgnoreCase(schemaName)) {
						packageImport += "import com."+pack+".bean."+CapitalCase.toBeanClass(ffkTableName)+"; \n";
						packageImport += "import com."+pack+".serviceimpl."+CapitalCase.toCapitalCase(ffkTableName) + "ServiceImpl; \n";
						
					}
				duplicateString.add(ffkTableName);
			}
			rsf.close();
			packageImport += "import com."+pack+".bean."+CapitalCase.toBeanClass(className)+"; \n";
			packageImport += "import com."+pack+".serviceimpl."+CapitalCase.toCapitalCase(className) + "ServiceImpl; \n";
			packageImport += "import com."+pack+".exception.ConnectionException; \n";
			packageImport += "import com."+pack+".util.UtilConstants; \n";
			packageImport += "import javax.servlet.annotation.WebServlet; \n";
			packageImport += "import com.sun.org.apache.commons.beanutils.BeanUtils; \n";

			writer.println(packageImport);
			writer.println("@WebServlet(\"/Add"
					+ CapitalCase.toCapitalCase(className) + "Action\")");
			writer.println("public class Add"
					+ CapitalCase.toCapitalCase(className) + "Action extends HttpServlet {\n");
			writer.println("	private static final long serialVersionUID = 1L;\n");
			writer.println("	public void doGet(HttpServletRequest request, HttpServletResponse response)");
			writer.println("			throws ServletException, IOException {");
			writer.println("		doPost(request, response);");
			writer.println("	}");
			writer.println("\n");	
			writer.println("	public void doPost(HttpServletRequest request, HttpServletResponse response)");
			writer.println("			throws ServletException, IOException {");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._START_ADD_"+ className.replace("_", "").toUpperCase() + "_LOG);");
			writer.println("		RequestDispatcher rd = null;");
			writer.println("		boolean flag = false;");
			writer.println("		String path = \"\";");
			writer.println("		"+CapitalCase.toBeanClass(className)+" "+CapitalCase.toBeanClass(className).toLowerCase()+" = new "+CapitalCase.toBeanClass(className)+"();");
			writer.println("		Map map = request.getParameterMap();");
			writer.println("		try {");
			writer.println("			BeanUtils.populate("+CapitalCase.toBeanClass(className).toLowerCase()+", map);");
			writer.println("			flag = new "+CapitalCase.toCapitalCase(className) + "ServiceImpl().add"+CapitalCase.toCapitalCase(className)+"("+CapitalCase.toBeanClass(className).toLowerCase()+");");
			writer.println("\n");	
			rsf = databaseMetaData.getImportedKeys(cataLog, null, className);
			duplicateString = new ArrayList<String>();
			while (rsf.next()) {
				String ffkTableName = rsf.getString("PKTABLE_NAME");
				//String fkColumnName = rsf.getString("FKCOLUMN_NAME");
				String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
				if(fkSchemaName == null) fkSchemaName = "";
				if (!duplicateString.contains(ffkTableName) && fkSchemaName.equalsIgnoreCase(schemaName)) {
					writer.println("		List<"+CapitalCase.toBeanClass(ffkTableName)+"> "+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+"list = null;");
					writer.println("			"+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+"list = new "+CapitalCase.toCapitalCase(ffkTableName) + "ServiceImpl().get"+CapitalCase.toCapitalCase(ffkTableName)+"();");
					writer.println("\n");
					writer.println("				request.setAttribute(\""+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+"list\", "+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+"list);");
					}
				duplicateString.add(ffkTableName);
			}
			
			writer.println("			if (flag) {");
			writer.println("				request.setAttribute(\"status\", UtilConstants._ADD_"+CapitalCase.toCapitalCase(className.toUpperCase()).toUpperCase()+");");
			writer.println("				path = UtilConstants._ADD_"+className.replace("_", "").toUpperCase()+"_JSP;");
			writer.println("			} else {");
			writer.println("				request.setAttribute(UtilConstants._STATUS,");
			writer.println("						UtilConstants._ADD_"+CapitalCase.toCapitalCase(className.toUpperCase()).toUpperCase()+"_FAILED);");
			writer.println("				path = UtilConstants._STATUS_PATH;");
			writer.println("			}");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._END_ADD_"+ className.replace("_", "").toUpperCase() + "_LOG);");
			
			writer.println("		}");
			writer.println("		catch (ConnectionException e) {");
			writer.println("			request.setAttribute(UtilConstants._STATUS,e.getMessage());");
			writer.println("			path=UtilConstants._STATUS_PATH;");
			writer.println("			LoggerManager.writeLogSevere(e);");
			writer.println("		}");
			writer.println("			catch (Exception e) {");
			writer.println("			e.printStackTrace();");
			writer.println("			LoggerManager.writeLogSevere(e);");
			writer.println("			request.setAttribute(UtilConstants._STATUS, UtilConstants._ADD_"+CapitalCase.toCapitalCase(className.toUpperCase()).toUpperCase()+"_FAILED);");
			writer.println("			path = UtilConstants._STATUS_PATH;");
			writer.println("		}finally{");
			writer.println("			rd = request.getRequestDispatcher(path);");
			writer.println("			rd.forward(request, response);");
			writer.println("		}\n" +
					"	}");
			writer.println("}");
			writer.close();
			rsf.close();
			
			
			//UPDATE
			 packageNameString = ".\\com\\";
			 packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + pack;
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + "\\action";
			packageDir = new File(packageNameString);
			packageDir.mkdir();

            writer = new PrintWriter(packageNameString + "\\Update"
					+ CapitalCase.toCapitalCase(className) + "Action.java");

			packageImport = "package com." + pack + ".action;\n";
			packageImport += "import java.io.IOException; \n";
			packageImport += "import java.util.Map; \n";
			packageImport += "import javax.servlet.RequestDispatcher; \n";
			packageImport += "import javax.servlet.ServletException; \n";
			packageImport += "import javax.servlet.http.HttpServlet; \n";
			packageImport += "import javax.servlet.http.HttpServletRequest; \n";
			packageImport += "import javax.servlet.http.HttpServletResponse; \n";
			packageImport += "import javax.servlet.annotation.WebServlet; \n";

			packageImport += "import com."+pack+".util.LoggerManager;\n";
			packageImport += "import com."+pack+".bean."+CapitalCase.toBeanClass(className)+"; \n";
			packageImport += "import com."+pack+".serviceimpl."+CapitalCase.toCapitalCase(className) + "ServiceImpl; \n";
			packageImport += "import com."+pack+".exception.ConnectionException; \n";
			packageImport += "import com."+pack+".util.UtilConstants; \n";
			packageImport += "import com.sun.org.apache.commons.beanutils.BeanUtils; \n";

			writer.println(packageImport);
			writer.println("@WebServlet(\"/Update"
								+ CapitalCase.toCapitalCase(className) + "Action\")");
			writer.println("public class Update"
					+ CapitalCase.toCapitalCase(className) + "Action extends HttpServlet {\n");
			writer.println("	private static final long serialVersionUID = 1L;\n");
			writer.println("	public void doGet(HttpServletRequest request, HttpServletResponse response)");
			writer.println("			throws ServletException, IOException {");
			writer.println("		doPost(request, response);");
			writer.println("	}");
			writer.println("\n");	
			writer.println("	public void doPost(HttpServletRequest request, HttpServletResponse response)");
			writer.println("			throws ServletException, IOException {");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._START_UPDATE_"+ className.replace("_", "").toUpperCase() + "_LOG);");
			writer.println("		RequestDispatcher rd = null;");
			writer.println("		boolean flag = false;");
			writer.println("		String path = \"\";");
			writer.println("		"+CapitalCase.toBeanClass(className)+" "+CapitalCase.toBeanClass(className).toLowerCase()+" = new "+CapitalCase.toBeanClass(className)+"();");
			writer.println("		Map map = request.getParameterMap();");
			writer.println("		try {");
			writer.println("			BeanUtils.populate("+CapitalCase.toBeanClass(className).toLowerCase()+", map);");
			writer.println("			flag = new "+CapitalCase.toCapitalCase(className) + "ServiceImpl().update"+CapitalCase.toCapitalCase(className)+"("+CapitalCase.toBeanClass(className).toLowerCase()+".get"+CapitalCase.toCapitalCase(columnMetaData.getColumnName(1).toLowerCase())+"(), "+CapitalCase.toBeanClass(className).toLowerCase()+");");
			writer.println("\n");	
			writer.println("			if (flag) {");
			writer.println("				request.setAttribute(\"status\", UtilConstants._UPDATE_"+CapitalCase.toCapitalCase(className.toUpperCase()).toUpperCase()+");");
			writer.println("				path = UtilConstants._VIEWALL_"+className.replace("_", "").toUpperCase()+"_ACTION;");
			writer.println("			} else {");
			writer.println("				request.setAttribute(UtilConstants._STATUS,");
			writer.println("						UtilConstants._UPDATE_"+CapitalCase.toCapitalCase(className.toUpperCase()).toUpperCase()+"_FAILED);");
			writer.println("				path = UtilConstants._STATUS_PATH;");
			writer.println("			}");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._START_UPDATE_"+ className.replace("_", "").toUpperCase() + "_LOG);");
			writer.println("		}");
			writer.println("		catch (ConnectionException e) {");
			writer.println("			request.setAttribute(UtilConstants._STATUS,e.getMessage());");
			writer.println("			path=UtilConstants._STATUS_PATH;");
			writer.println("			LoggerManager.writeLogSevere(e);");
			writer.println("		}");
			writer.println("			catch (Exception e) {");
			writer.println("			e.printStackTrace();");
			writer.println("			LoggerManager.writeLogSevere(e);");
			writer.println("			request.setAttribute(UtilConstants._STATUS, UtilConstants._UPDATE_"+CapitalCase.toCapitalCase(className.toUpperCase()).toUpperCase()+"_FAILED);");
			writer.println("			path = UtilConstants._STATUS_PATH;");
			writer.println("		}finally{");
			writer.println("			rd = request.getRequestDispatcher(path);");
			writer.println("			rd.forward(request, response);");
			writer.println("		}\n" +
					"	}");
			writer.println("}");
			writer.close();

			
			
			//DELETE
			 packageNameString = ".\\com\\";
			 packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + pack;
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + "\\action";
			packageDir = new File(packageNameString);
			packageDir.mkdir();

           writer = new PrintWriter(packageNameString + "\\Delete"
					+ CapitalCase.toCapitalCase(className) + "Action.java");

			packageImport = "package com." + pack + ".action;\n";
			packageImport += "import java.io.IOException; \n";
			packageImport += "import java.util.Map; \n";
			packageImport += "import javax.servlet.RequestDispatcher; \n";
			packageImport += "import javax.servlet.ServletException; \n";
			packageImport += "import javax.servlet.http.HttpServlet; \n";
			packageImport += "import javax.servlet.http.HttpServletRequest; \n";
			packageImport += "import javax.servlet.http.HttpServletResponse; \n";
			packageImport += "import javax.servlet.annotation.WebServlet; \n";

			packageImport += "import com."+pack+".util.LoggerManager;\n";
			packageImport += "import com."+pack+".bean."+CapitalCase.toBeanClass(className)+"; \n";
			packageImport += "import com."+pack+".bean."+CapitalCase.toBeanClass(className)+"; \n";
			packageImport += "import com."+pack+".serviceimpl."+CapitalCase.toCapitalCase(className) + "ServiceImpl; \n";
			packageImport += "import com."+pack+".exception.ConnectionException; \n";
			packageImport += "import com."+pack+".util.UtilConstants; \n";
			packageImport += "import com.sun.org.apache.commons.beanutils.BeanUtils; \n";

			writer.println(packageImport);
			writer.println("@WebServlet(\"/Delete"
								+ CapitalCase.toCapitalCase(className) + "Action\")");
			writer.println("public class Delete"
					+ CapitalCase.toCapitalCase(className) + "Action extends HttpServlet {\n");
			writer.println("	private static final long serialVersionUID = 1L;\n");
			writer.println("	public void doGet(HttpServletRequest request, HttpServletResponse response)");
			writer.println("			throws ServletException, IOException {");
			writer.println("		doPost(request, response);");
			writer.println("	}");
			writer.println("\n");	
			writer.println("	public void doPost(HttpServletRequest request, HttpServletResponse response)");
			writer.println("			throws ServletException, IOException {");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._START_DELETE_"+ className.replace("_", "").toUpperCase() + "_LOG);");
			
			writer.println("		RequestDispatcher rd = null;");
			writer.println("		boolean flag = false;");
			writer.println("		String path = \"\";");
			writer.println("		"+CapitalCase.toBeanClass(className)+" "+CapitalCase.toBeanClass(className).toLowerCase()+" = new "+CapitalCase.toBeanClass(className)+"();");
			writer.println("		Map map = request.getParameterMap();");
			writer.println("		try {");
			writer.println("			BeanUtils.populate("+CapitalCase.toBeanClass(className).toLowerCase()+", map);");

			writer.println("			flag = new "+CapitalCase.toCapitalCase(className) + "ServiceImpl().delete"+CapitalCase.toCapitalCase(className)+"("+CapitalCase.toBeanClass(className).toLowerCase()+".get"+CapitalCase.toCapitalCase(columnMetaData.getColumnName(1).toLowerCase())+"());");
			
			writer.println("\n");	
			writer.println("			if (flag) {");
			writer.println("				request.setAttribute(\"status\", UtilConstants._DELETE_"+CapitalCase.toCapitalCase(className.toUpperCase()).toUpperCase()+");");
			writer.println("				path = \"./ViewAll"+ CapitalCase.toCapitalCase(className) + "Action\";");
			writer.println("			} else {");
			writer.println("				request.setAttribute(UtilConstants._STATUS,");
			writer.println("						UtilConstants._DELETE_"+CapitalCase.toCapitalCase(className.toUpperCase()).toUpperCase()+"_FAILED);");
			writer.println("				path = UtilConstants._STATUS_PATH;");
			writer.println("			}");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._END_DELETE_"+ className.replace("_", "").toUpperCase() + "_LOG);");
			
			writer.println("		}");
			writer.println("		catch (ConnectionException e) {");
			writer.println("			request.setAttribute(UtilConstants._STATUS,e.getMessage());");
			writer.println("			path=UtilConstants._STATUS_PATH;");
			writer.println("			LoggerManager.writeLogSevere(e);");
			writer.println("		}");
			writer.println("			catch (Exception e) {");
			writer.println("			e.printStackTrace();");
			writer.println("			request.setAttribute(UtilConstants._STATUS, UtilConstants._DELETE_"+CapitalCase.toCapitalCase(className.toUpperCase()).toUpperCase()+"_FAILED);");
			writer.println("			path = UtilConstants._STATUS_PATH;");
			writer.println("			LoggerManager.writeLogSevere(e);");
			writer.println("		}finally{");
			writer.println("			rd = request.getRequestDispatcher(path);");
			writer.println("			rd.forward(request, response);");
			writer.println("		}\n" +
					"	}");
			writer.println("}");
			writer.close();

			  rsf = databaseMetaData.getImportedKeys(cataLog, null, className);
			duplicateString = new ArrayList<String>();
			boolean fkFlag = false;
			String fkTableName = "";
			while (rsf.next()) {
				fkTableName = rsf.getString("PKTABLE_NAME");
				//String fkColumnName = rsf.getString("FKCOLUMN_NAME");
				String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
				if(fkSchemaName == null) fkSchemaName = "";
				if (!duplicateString.contains(fkTableName) && fkSchemaName.equalsIgnoreCase(schemaName)) {
					fkFlag = true;
					break;
				}
				duplicateString.add(fkTableName);
			}
			rsf.close();
			if(fkFlag) {
				//Add View 
				 packageNameString = ".\\com\\";
				 packageDir = new File(packageNameString);
				packageDir.mkdir();

				packageNameString = packageNameString + pack;
				packageDir = new File(packageNameString);
				packageDir.mkdir();

				packageNameString = packageNameString + "\\action";
				packageDir = new File(packageNameString);
				packageDir.mkdir();

	          writer = new PrintWriter(packageNameString + "\\Get"+CapitalCase.toCapitalCase(fkTableName)+"Add" + CapitalCase.toCapitalCase(className) + "Action.java");

				packageImport = "package com." + pack + ".action;\n";
				packageImport += "import java.io.IOException; \n";
				packageImport += "import java.util.List; \n";
				packageImport += "import javax.servlet.RequestDispatcher; \n";
				packageImport += "import javax.servlet.ServletException; \n";
				packageImport += "import javax.servlet.http.HttpServlet; \n";
				packageImport += "import javax.servlet.http.HttpServletRequest; \n";
				packageImport += "import javax.servlet.http.HttpServletResponse; \n";
				packageImport += "import javax.servlet.annotation.WebServlet; \n";

				packageImport += "import com."+pack+".util.LoggerManager;\n";
				packageImport += "import com."+pack+".bean."+CapitalCase.toBeanClass(className)+"; \n";
				rsf = databaseMetaData.getImportedKeys(cataLog, null, className);
				duplicateString = new ArrayList<String>();
				while (rsf.next()) {
					String ffkTableName = rsf.getString("PKTABLE_NAME");
					//String fkColumnName = rsf.getString("FKCOLUMN_NAME");
					String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
					if(fkSchemaName == null) fkSchemaName = "";
					if (!duplicateString.contains(ffkTableName) && fkSchemaName.equalsIgnoreCase(schemaName)) {
							packageImport += "import com."+pack+".bean."+CapitalCase.toBeanClass(ffkTableName)+"; \n";
							packageImport += "import com."+pack+".serviceimpl."+CapitalCase.toCapitalCase(ffkTableName) + "ServiceImpl; \n";
							
						}
					duplicateString.add(ffkTableName);
				}
				rsf.close();
				packageImport += "import com."+pack+".serviceimpl."+CapitalCase.toCapitalCase(className) + "ServiceImpl; \n";
				packageImport += "import com."+pack+".exception.ConnectionException; \n";
				packageImport += "import com."+pack+".util.UtilConstants; \n";
				packageImport += "import com.sun.org.apache.commons.beanutils.BeanUtils; \n";

				writer.println(packageImport);
				writer.println("@WebServlet(\"/Get"+CapitalCase.toCapitalCase(fkTableName)+"Add"+ CapitalCase.toCapitalCase(className) + "Action\")");
				writer.println("public class Get"+CapitalCase.toCapitalCase(fkTableName)+"Add"
						+ CapitalCase.toCapitalCase(className) + "Action extends HttpServlet {\n");
				writer.println("	private static final long serialVersionUID = 1L;\n");
				writer.println("	public void doGet(HttpServletRequest request, HttpServletResponse response)");
				writer.println("			throws ServletException, IOException {");
				writer.println("		doPost(request, response);");
				writer.println("	}");
				writer.println("\n");	
				writer.println("	public void doPost(HttpServletRequest request, HttpServletResponse response)");
				writer.println("			throws ServletException, IOException {");
				writer.println("			LoggerManager.writeLogInfo(UtilConstants."+("_START_GET"+CapitalCase.toCapitalCase(fkTableName)+"Add_").toUpperCase()+ className.replace("_", "").toUpperCase() + "_LOG);");
				
				writer.println("		RequestDispatcher rd = null;");
				writer.println("		"+CapitalCase.toBeanClass(fkTableName)+" "+CapitalCase.toBeanClass(fkTableName).toLowerCase()+" = null;");
				writer.println("		String path = \"\";");
				writer.println("		try {");
				
				rsf = databaseMetaData.getImportedKeys(cataLog, null, className);
				duplicateString = new ArrayList<String>();
				while (rsf.next()) {
					String ffkTableName = rsf.getString("PKTABLE_NAME");
					//String fkColumnName = rsf.getString("FKCOLUMN_NAME");
					String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
					if(fkSchemaName == null) fkSchemaName = "";
					if (!duplicateString.contains(ffkTableName) && fkSchemaName.equalsIgnoreCase(schemaName)) {
						writer.println("		List<"+CapitalCase.toBeanClass(ffkTableName)+"> "+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+"list = null;");
						writer.println("			"+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+"list = new "+CapitalCase.toCapitalCase(ffkTableName) + "ServiceImpl().get"+CapitalCase.toCapitalCase(ffkTableName)+"();");
						writer.println("\n");
						writer.println("				request.setAttribute(\""+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+"list\", "+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+"list);");
						}
					duplicateString.add(ffkTableName);
				}
				rsf.close();
				writer.println("				path = UtilConstants._ADD_"+className.replace("_", "").toUpperCase()+"_JSP;");
				writer.println("			LoggerManager.writeLogInfo(UtilConstants."+("_END_GET"+CapitalCase.toCapitalCase(fkTableName)+"Add_").toUpperCase()+ className.replace("_", "").toUpperCase() + "_LOG);");
				
				writer.println("		}");
				writer.println("		catch (ConnectionException e) {");
				writer.println("			request.setAttribute(UtilConstants._STATUS,e.getMessage());");
				writer.println("			path=UtilConstants._STATUS_PATH;");
				writer.println("			LoggerManager.writeLogSevere(e);");
				writer.println("		}");
				writer.println("			catch (Exception e) {");
				writer.println("			e.printStackTrace();");
				writer.println("			request.setAttribute(UtilConstants._STATUS, UtilConstants._ADD_"+CapitalCase.toCapitalCase(className).toUpperCase()+"_FAILED);");
				writer.println("			path = UtilConstants._STATUS_PATH;");
				writer.println("			LoggerManager.writeLogSevere(e);");
				writer.println("		}finally{");
				writer.println("			rd = request.getRequestDispatcher(path);");
				writer.println("			rd.forward(request, response);");
				writer.println("		}\n" +
						"	}");
				writer.println("}");
				writer.close();
			}
			
			
			
			//View 
			 packageNameString = ".\\com\\";
			 packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + pack;
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + "\\action";
			packageDir = new File(packageNameString);
			packageDir.mkdir();

          writer = new PrintWriter(packageNameString + "\\UpdateView"
					+ CapitalCase.toCapitalCase(className) + "Action.java");

			packageImport = "package com." + pack + ".action;\n";
			packageImport += "import java.io.IOException; \n";
			packageImport += "import java.util.List; \n";
			packageImport += "import javax.servlet.RequestDispatcher; \n";
			packageImport += "import javax.servlet.ServletException; \n";
			packageImport += "import javax.servlet.http.HttpServlet; \n";
			packageImport += "import javax.servlet.http.HttpServletRequest; \n";
			packageImport += "import javax.servlet.http.HttpServletResponse; \n";
			packageImport += "import javax.servlet.annotation.WebServlet; \n";
			packageImport += "import com."+pack+".util.LoggerManager;\n";
			packageImport += "import com."+pack+".bean."+CapitalCase.toBeanClass(className)+"; \n";
			rsf = databaseMetaData.getImportedKeys(cataLog, null, className);
			duplicateString = new ArrayList<String>();
			while (rsf.next()) {
				String ffkTableName = rsf.getString("PKTABLE_NAME");
				//String fkColumnName = rsf.getString("FKCOLUMN_NAME");
				String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
				if(fkSchemaName == null) fkSchemaName = "";
				if (!duplicateString.contains(ffkTableName) && fkSchemaName.equalsIgnoreCase(schemaName)) {
						packageImport += "import com."+pack+".bean."+CapitalCase.toBeanClass(ffkTableName)+"; \n";
						packageImport += "import com."+pack+".serviceimpl."+CapitalCase.toCapitalCase(ffkTableName) + "ServiceImpl; \n";
						
					}
				duplicateString.add(ffkTableName);
			}
			rsf.close();
			packageImport += "import com."+pack+".serviceimpl."+CapitalCase.toCapitalCase(className) + "ServiceImpl; \n";
			packageImport += "import com."+pack+".exception.ConnectionException; \n";
			packageImport += "import com."+pack+".util.UtilConstants; \n";
			packageImport += "import com.sun.org.apache.commons.beanutils.BeanUtils; \n";

			writer.println(packageImport);
			writer.println("@WebServlet(\"/UpdateView"
								+ CapitalCase.toCapitalCase(className) + "Action\")");
			writer.println("public class UpdateView"
					+ CapitalCase.toCapitalCase(className) + "Action extends HttpServlet {\n");
			writer.println("	private static final long serialVersionUID = 1L;\n");
			writer.println("	public void doGet(HttpServletRequest request, HttpServletResponse response)");
			writer.println("			throws ServletException, IOException {");
			writer.println("		doPost(request, response);");
			writer.println("	}");
			writer.println("\n");	
			writer.println("	public void doPost(HttpServletRequest request, HttpServletResponse response)");
			writer.println("			throws ServletException, IOException {");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._START_UPDATE_VIEW_" + className.replace("_", "").toUpperCase() + "_LOG);");
			
			writer.println("		RequestDispatcher rd = null;");
			writer.println("		"+CapitalCase.toBeanClass(className)+" "+CapitalCase.toBeanClass(className).toLowerCase()+" = null;");
			writer.println("		String path = \"\";");
			writer.println("		try {");
			writer.println("			"+CapitalCase.toBeanClass(className).toLowerCase()+" = new "+CapitalCase.toCapitalCase(className) + "ServiceImpl().get"+CapitalCase.toCapitalCase(className)+"(Integer.parseInt(request.getParameter(\""+columnMetaData.getColumnName(1).toLowerCase()+"\")));");
			writer.println("\n");	
			rsf = databaseMetaData.getImportedKeys(cataLog, null, className);
		    duplicateString = new ArrayList<String>();
			while (rsf.next()) {
				String ffkTableName = rsf.getString("PKTABLE_NAME");
				String fkColumnName = rsf.getString("FKCOLUMN_NAME");
				String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
				if(fkSchemaName == null) fkSchemaName = "";
				if (!duplicateString.contains(ffkTableName) && fkSchemaName.equalsIgnoreCase(schemaName)) {
					writer.println("		"+CapitalCase.toBeanClass(ffkTableName)+" "+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+" = new "+CapitalCase.toCapitalCase(ffkTableName) + "ServiceImpl().get"+CapitalCase.toCapitalCase(ffkTableName)+"("+CapitalCase.toBeanClass(className).toLowerCase()+".get"+CapitalCase.toCapitalCase(fkColumnName)+"());");
					writer.println("			request.setAttribute(\""+fkColumnName.toLowerCase()+"\", "+CapitalCase.toBeanClass(className).toLowerCase()+".get"+CapitalCase.toCapitalCase(fkColumnName)+"());");
					String fkRequestColumnName = CapitalCase.toCapitalCase(getFKTableColumnName(con,ffkTableName,2));
					if(fkRequestColumnName!="")
						writer.println("			request.setAttribute(\""+fkRequestColumnName.toLowerCase()+"\", "+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+".get"+CapitalCase.toCapitalCase(getFKTableColumnName(con,ffkTableName,2))+"());");
					writer.println("		List<"+CapitalCase.toBeanClass(ffkTableName)+"> "+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+"list = null;");
					writer.println("			"+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+"list = new "+CapitalCase.toCapitalCase(ffkTableName) + "ServiceImpl().get"+CapitalCase.toCapitalCase(ffkTableName)+"();");
					writer.println("				request.setAttribute(\""+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+"list\", "+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+"list);");
					writer.println("				request.setAttribute(\""+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+"\", "+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+");");
				}
				duplicateString.add(ffkTableName);
			}
			rsf.close();
			
			/*rsf = databaseMetaData.getImportedKeys(cataLog, null, className);
			duplicateString = new ArrayList<String>();
			while (rsf.next()) {
				String ffkTableName = rsf.getString("PKTABLE_NAME");
				//String fkColumnName = rsf.getString("FKCOLUMN_NAME");
				String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
				if(fkSchemaName == null) fkSchemaName = "";
				if (!duplicateString.contains(ffkTableName) && fkSchemaName.equalsIgnoreCase(schemaName)) {
					writer.println("		List<"+CapitalCase.toBeanClass(ffkTableName)+"> "+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+"list = null;");
					writer.println("			"+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+"list = new "+CapitalCase.toCapitalCase(ffkTableName) + "ServiceImpl().get"+CapitalCase.toCapitalCase(ffkTableName)+"();");
					writer.println("\n");
					writer.println("				request.setAttribute(\""+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+"list\", "+CapitalCase.toBeanClass(ffkTableName).toLowerCase()+"list);");
					}
				duplicateString.add(ffkTableName);
			}
			rsf.close();*/
			
			
			
			writer.println("			if ("+CapitalCase.toBeanClass(className).toLowerCase()+" != null) {");
			writer.println("				request.setAttribute(\""+CapitalCase.toBeanClass(className).toLowerCase()+"\", "+CapitalCase.toBeanClass(className).toLowerCase()+");");
			writer.println("				path = UtilConstants._UPDATE_"+CapitalCase.toCapitalCase(className).toUpperCase()+"_JSP;");
			writer.println("			} else {");
			writer.println("				request.setAttribute(UtilConstants._STATUS,");
			writer.println("						UtilConstants._VIEW_"+CapitalCase.toCapitalCase(className).toUpperCase()+"_FAILED);");
			writer.println("				path = UtilConstants._UPDATE_"+className.replace("_", "").toUpperCase()+"_JSP;");
			writer.println("			}");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._END_UPDATE_VIEW_" + className.replace("_", "").toUpperCase() + "_LOG);");
			
			writer.println("		}");
			writer.println("		catch (ConnectionException e) {");
			writer.println("			request.setAttribute(UtilConstants._STATUS,e.getMessage());");
			writer.println("			path=UtilConstants._STATUS_PATH;");
			writer.println("			LoggerManager.writeLogSevere(e);");
			writer.println("		}");
			writer.println("			catch (Exception e) {");
			writer.println("			e.printStackTrace();");
			writer.println("			request.setAttribute(UtilConstants._STATUS, UtilConstants._UPDATE_"+CapitalCase.toCapitalCase(className).toUpperCase()+"_FAILED);");
			writer.println("			path = UtilConstants._STATUS_PATH;");
			writer.println("			LoggerManager.writeLogSevere(e);");
			writer.println("		}finally{");
			writer.println("			rd = request.getRequestDispatcher(path);");
			writer.println("			rd.forward(request, response);");
			writer.println("		}\n" +
					"	}");
			writer.println("}");
			writer.close();
			rsf.close();
			//View 
			 packageNameString = ".\\com\\";
			 packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + pack;
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + "\\action";
			packageDir = new File(packageNameString);
			packageDir.mkdir();

         writer = new PrintWriter(packageNameString + "\\ViewAll"
					+ CapitalCase.toCapitalCase(className) + "Action.java");

			packageImport = "package com." + pack + ".action;\n";
			packageImport += "import java.io.IOException; \n";
			packageImport += "import java.util.List; \n";
			packageImport += "import javax.servlet.RequestDispatcher; \n";
			packageImport += "import javax.servlet.ServletException; \n";
			packageImport += "import javax.servlet.http.HttpServlet; \n";
			packageImport += "import javax.servlet.http.HttpServletRequest; \n";
			packageImport += "import javax.servlet.http.HttpServletResponse; \n";
			packageImport += "import javax.servlet.annotation.WebServlet; \n";

			packageImport += "import com."+pack+".util.LoggerManager;\n";
			packageImport += "import com."+pack+".bean."+CapitalCase.toBeanClass(className)+"; \n";
			packageImport += "import com."+pack+".serviceimpl."+CapitalCase.toCapitalCase(className) + "ServiceImpl; \n";
			packageImport += "import com."+pack+".exception.ConnectionException; \n";
			packageImport += "import com."+pack+".util.UtilConstants; \n";
			packageImport += "import com."+pack+".util."+CapitalCase.toCapitalCase(title)+"Utils; \n";
			packageImport += "import com.sun.org.apache.commons.beanutils.BeanUtils; \n";

			writer.println(packageImport);
			writer.println("@WebServlet(\"/ViewAll"
								+ CapitalCase.toCapitalCase(className) + "Action\")");
			writer.println("public class ViewAll"
					+ CapitalCase.toCapitalCase(className) + "Action extends HttpServlet {\n");
			writer.println("	private static final long serialVersionUID = 1L;\n");
			writer.println("	public void doGet(HttpServletRequest request, HttpServletResponse response)");
			writer.println("			throws ServletException, IOException {");
			writer.println("		doPost(request, response);");
			writer.println("	}");
			writer.println("\n");	
			writer.println("	public void doPost(HttpServletRequest request, HttpServletResponse response)");
			writer.println("			throws ServletException, IOException {");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._START_VIEW_ALL_" + className.replace("_", "").toUpperCase() +"_LOG);");
			
			writer.println("		RequestDispatcher rd = null;");
			writer.println("		List<"+CapitalCase.toBeanClass(className)+"> "+CapitalCase.toBeanClass(className).toLowerCase()+"list = null;");
			writer.println("		String path = \"\";");
			writer.println("		try {");
			writer.println("				String cPage = request.getParameter(\"currentPage\");");
			writer.println("				int currentPage = cPage==null?1:Integer.valueOf(cPage);");
			writer.println("				int recordsPerPage = Integer.valueOf((String)request.getServletContext().getAttribute(\"recordsPerPage\"));");
			writer.println("			"+CapitalCase.toBeanClass(className).toLowerCase()+"list = new "+CapitalCase.toCapitalCase(className) + "ServiceImpl().get"+CapitalCase.toCapitalCase(className)+"();");
			writer.println("\n");	
			writer.println("			if ("+CapitalCase.toBeanClass(className).toLowerCase()+"list != null) {");
			writer.println("				int rows = "+CapitalCase.toBeanClass(className).toLowerCase()+"list.size();");
			writer.println("				int noOfPages = rows / recordsPerPage;");
			writer.println("				if (noOfPages % recordsPerPage > 0) {");
			writer.println("					noOfPages++;");
			writer.println("					}");
			writer.println("				int toIndex = currentPage*recordsPerPage;");
			writer.println("				int  fromIndex = (currentPage*recordsPerPage)-recordsPerPage;");
			writer.println("				request.setAttribute(\""+CapitalCase.toBeanClass(className).toLowerCase()+"list\", "+CapitalCase.toCapitalCase(title)+"Utils.getSubList("+CapitalCase.toBeanClass(className).toLowerCase()+"list, fromIndex ,toIndex));");
			writer.println("				request.setAttribute(\"currentPage\", currentPage);");
			writer.println("				request.setAttribute(\"recordsPerPage\", recordsPerPage);");
			writer.println("				request.setAttribute(\"noOfPages\", noOfPages);");
			//writer.println("				request.setAttribute(\""+CapitalCase.toBeanClass(className).toLowerCase()+"list\", "+CapitalCase.toBeanClass(className).toLowerCase()+"list);");
			writer.println("				path = UtilConstants._VIEW_"+CapitalCase.toCapitalCase(className).toUpperCase()+"_JSP;");
			writer.println("			} else {");
			writer.println("				request.setAttribute(UtilConstants._STATUS,");
			writer.println("						UtilConstants._VIEW_"+CapitalCase.toCapitalCase(className).toUpperCase()+"_FAILED);");
			writer.println("				path = UtilConstants._VIEW_"+CapitalCase.toCapitalCase(className).toUpperCase()+";");
			writer.println("			}");
			writer.println("			LoggerManager.writeLogInfo(UtilConstants._END_VIEW_ALL_" + className.replace("_", "").toUpperCase() + "_LOG);");
			
			writer.println("		}");
			writer.println("		catch (ConnectionException e) {");
			writer.println("			request.setAttribute(UtilConstants._STATUS,e.getMessage());");
			writer.println("			path=UtilConstants._STATUS_PATH;");
			writer.println("			LoggerManager.writeLogSevere(e);");
			writer.println("		}");
			writer.println("			catch (Exception e) {");
			writer.println("			e.printStackTrace();");
			writer.println("			request.setAttribute(UtilConstants._STATUS, UtilConstants._VIEW_"+CapitalCase.toCapitalCase(className).toUpperCase()+"_FAILED);");
			writer.println("			path = UtilConstants._STATUS_PATH;");
			writer.println("			LoggerManager.writeLogSevere(e);");
			writer.println("		}finally{");
			writer.println("			rd = request.getRequestDispatcher(path);");
			writer.println("			rd.forward(request, response);");
			writer.println("		}\n" +
					"	}");
			writer.println("}");
			writer.close();
			resultSet.close();
			rsf.close();
			pstmt.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private String getFKTableColumnName(Connection con, String className, int columnNumber) {
		try {
			pstmt = con.prepareStatement("select * from " + className);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();

			if (columnNumber == 1) {
				return columnMetaData.getColumnName(columnNumber);
			}
			if (columnNumber == 2 && !SqlDatatypeTOJavaWrapperClass
					.toPrimitiveClass(columnMetaData.getColumnTypeName(2)).equals("Int")) {
				if (columnMetaData.getColumnName(columnNumber).toLowerCase().endsWith("name"))
					return columnMetaData.getColumnName(columnNumber);
				else if (columnMetaData.getColumnName(3).endsWith("name")) {
					return columnMetaData.getColumnName(columnNumber);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
}
