package com.springboot.util;

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
import com.springboot.persistance.jpa.JPAPersistance;
import com.springboot.project.properties.ReadProjectPropertiesFile;

public class SBUtilImpl extends AbstractDataAccessObject {

	Connection con;
	PreparedStatement pstmt;
	String title;
	ResultSet resultSet, rs, rsf, tableTypes, contraintsRecords;

	String schemaName = getProperties().getProperty("duser");
	String pack = ReadProjectPropertiesFile.projectProps.getProperty("pack");
	String basePackage = ReadProjectPropertiesFile.projectProps.getProperty("basePackage");
	
	List<String> tableList = JPAPersistance.readTablesFromPRoperteisFile();
	
	public SBUtilImpl(String pack, String title) {
		super();
		this.pack = pack;
		this.title = title;
	}

	public void createUtilityClass(String projectName) throws SQLException {
		try {
			
			String  packageNameString = projectName+"\\"+basePackage+"\\";
			File packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + pack;
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + "\\util";
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			PrintWriter writer = new PrintWriter(packageNameString + "\\ConstructFilePathUtil.java");
			String packageImport = "package "+basePackage+"." + pack + ".util;\n";		
			packageImport += "import java.io.FileOutputStream; \n";
			packageImport += "import java.io.OutputStream; \n";
			writer.println(packageImport);
			writer.println("public class ConstructFilePathUtil {");
			writer.println("");
			writer.println("	public static String writeByte(byte[] bytes, String path) {");
			writer.println("		try {");
			writer.println("			// Initialize a pointer");
			writer.println("			// in file using OutputStream");
			writer.println("			OutputStream os = new FileOutputStream(path);");
			writer.println("			// Starts writing the bytes in it");
			writer.println("			os.write(bytes);");
			writer.println("			// Close the file");
			writer.println("			os.close();");
			writer.println("		} catch (Exception e) {");
			writer.println("			System.out.println(\"Exception: \" + e);");
			writer.println("		}");
			writer.println("		return path;");
			writer.println("	}");
			writer.println("}");
			writer.println("");
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String getForeignKeyTableName(Connection con, String tableName,String name)throws Exception{
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
}
