package com.service.impl;

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
import java.util.List;

import com.jdbc.dao.AbstractDataAccessObject;
import com.jdbc.main.CapitalCase;
import com.jdbc.main.SqlDatatypeTOJavaWrapperClass;

public class ServiceImpl extends AbstractDataAccessObject {

	PreparedStatement pstmt;
	String pack, schemaName;
	ResultSet resultSet,rs,rsf,tableTypes,contraintsRecords;
	public ServiceImpl(String pack, String schemaName) {
		super();
		this.pack = pack;
		this.schemaName = schemaName;
	}

	public void createServiceImplClasses(String className, Connection con) throws SQLException {
		try {
			DatabaseMetaData databaseMetaData = con.getMetaData();
			String cataLog = con.getCatalog();
			pstmt = con.prepareStatement("select * from " + className);
			  resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();
			String packageNameString = ".\\com\\";
			File packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + pack;
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + "\\serviceimpl";
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			PrintWriter writer = new PrintWriter(
					packageNameString + "\\" + CapitalCase.toCapitalCase(className) + "ServiceImpl.java");

			String packageImport = "package com." + pack + ".serviceimpl;\n";
			packageImport += "import java.util.List; \n";
			packageImport += "import java.util.Date; \n";
			packageImport += "import com." + pack + ".daoimpl." + CapitalCase.toCapitalCase(className) + "DaoImpl; \n";
			/*ResultSet rsf = databaseMetaData.getExportedKeys(cataLog, null, className);
			List<String> duplicateString = new ArrayList<String>();
			while (rsf.next()) {
				String fkTableName = rsf.getString("FKTABLE_NAME");
				String fkColumnName = rsf.getString("FKCOLUMN_NAME");
				String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
				if(fkSchemaName == null) fkSchemaName = "";
				if (!duplicateString.contains(fkTableName) && fkSchemaName.equalsIgnoreCase(schemaName))
					packageImport += "import com." + pack + ".daoimpl." + CapitalCase.toCapitalCase(fkTableName) + "DaoImpl;\n";
				 duplicateString.add(fkTableName);
			}*/
			packageImport += "import com." + pack + ".bean." + CapitalCase.toCapitalCase(className) + "TO; \n";
			packageImport += "import com." + pack + ".exception.ConnectionException; \n";
			packageImport += "import com." + pack + ".exception.LoginException; \n";
			packageImport += "import com." + pack + ".exception.DataNotFoundException; \n";
			packageImport += "import com." + pack + ".bean." + CapitalCase.toBeanClass(className) + "; \n";

			writer.println(packageImport);
			writer.println("public class " + CapitalCase.toCapitalCase(className) + "ServiceImpl" + " {");
			writer.println("\n");
			writer.println("	private static final long serialVersionUID = 1L;\n");
			writer.println("	 " + CapitalCase.toCapitalCase(className) + "DaoImpl "
					+ CapitalCase.toCapitalCase(className).toLowerCase() + "DaoImpl = new "
					+ CapitalCase.toCapitalCase(className) + "DaoImpl();");
			/*ResultSet rsff = databaseMetaData.getExportedKeys(cataLog, null, className);
			List<String> duplicateStringg = new ArrayList<String>();
			while (rsff.next()) {
				String fkTableName = rsff.getString("FKTABLE_NAME");
				String fkColumnName = rsff.getString("FKCOLUMN_NAME");
				String fkSchemaName = rsff.getString("FKTABLE_SCHEM");
				if (!duplicateStringg.contains(fkTableName) && fkSchemaName.equalsIgnoreCase(schemaName))
					writer.println("	 " + CapitalCase.toCapitalCase(fkTableName) + "DaoImpl "
							+ CapitalCase.toCapitalCase(fkTableName).toLowerCase() + "DaoImpl = new "
							+ CapitalCase.toCapitalCase(fkTableName) + "DaoImpl();");
				 duplicateStringg.add(fkTableName);
			}*/
			writer.println("	 boolean flag = false;");
			writer.println("\n");
			writer.println("\n");
			writer.println("	public List<" + CapitalCase.toBeanClass(className) + "> get"
					+ CapitalCase.toBeanClass(className).replace("TO", "") + "() throws ConnectionException {");
			writer.println("		return " + CapitalCase.toCapitalCase(className).toLowerCase() + "DaoImpl.view"
					+ CapitalCase.toCapitalCase(className) + "();");
			writer.println("	}");
			writer.println("	public " + CapitalCase.toBeanClass(className) + " get"
					+ CapitalCase.toBeanClass(className).replace("TO", "") + "("
					+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(1)) + " "
					+ columnMetaData.getColumnName(1).toLowerCase() + ") throws ConnectionException {");
			writer.println("               " + CapitalCase.toBeanClass(className) + " " + className.toLowerCase()
					+ " = " + CapitalCase.toCapitalCase(className).toLowerCase() + "DaoImpl.view"
					+ CapitalCase.toCapitalCase(className) + "(" + columnMetaData.getColumnName(1).toLowerCase()
					+ ");");

			  rsf = databaseMetaData.getExportedKeys(cataLog, null, className);
			List<String> duplicateString = new ArrayList<String>();
			while (rsf.next()) {
				String fkTableName = rsf.getString("FKTABLE_NAME");
				String fkColumnName = rsf.getString("FKCOLUMN_NAME");
				String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
				if(fkSchemaName == null) fkSchemaName = "";
				if (!duplicateString.contains(fkTableName) && fkSchemaName.equalsIgnoreCase(schemaName))
					writer.println(
							"               " + className.toLowerCase() + ".set" + CapitalCase.toCapitalCase(fkTableName)
									+ "(" + CapitalCase.toCapitalCase(className).toLowerCase() + "DaoImpl.view"+ 
											CapitalCase.toCapitalCase(fkTableName) + "By" + CapitalCase.toCapitalCase(className) + "("
									+ columnMetaData.getColumnName(1).toLowerCase() + "));");
				 duplicateString.add(fkTableName);
			}
			writer.println("		return " + className.toLowerCase() + ";");
			// writer.println(" return "+CapitalCase.toCapitalCase(className).toLowerCase()
			// + "DaoImpl.view"+ CapitalCase.toCapitalCase(className) +
			// "("+columnMetaData.getColumnName(1).toLowerCase()
			// + ");");
			writer.println("	}");

			writer.println("			public boolean add" + CapitalCase.toCapitalCase(className) + "("
					+ CapitalCase.toBeanClass(className) + " " + className.toLowerCase()
					+ ") throws ConnectionException {");
			writer.println(
					"				return " + CapitalCase.toCapitalCase(className).toLowerCase() + "DaoImpl.add"
							+ CapitalCase.toCapitalCase(className) + "Details(" + className.toLowerCase() + ");");
			writer.println("			}");
			writer.println("			public boolean update" + CapitalCase.toCapitalCase(className) + "("
					+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(1)) + " "
					+ columnMetaData.getColumnName(1).toLowerCase() + ", " + CapitalCase.toBeanClass(className) + " "
					+ className.toLowerCase() + ") throws ConnectionException {");
			writer.println("		return " + CapitalCase.toCapitalCase(className).toLowerCase() + "DaoImpl.update"
					+ CapitalCase.toCapitalCase(className) + "(" + columnMetaData.getColumnName(1).toLowerCase() + ", "
					+ className.toLowerCase() + ");");
			writer.println("	}");

			writer.println("			public boolean delete" + CapitalCase.toCapitalCase(className) + "("
					+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(1)) + " "
					+ columnMetaData.getColumnName(1).toLowerCase() + ") throws ConnectionException {");
			writer.println("				return " + CapitalCase.toCapitalCase(className).toLowerCase()
					+ "DaoImpl.delete" + CapitalCase.toCapitalCase(className) + "("
					+ columnMetaData.getColumnName(1).toLowerCase() + ");");
			writer.println("			}");
			writer.println("			}");
			writer.close();
			pstmt.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
