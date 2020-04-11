package com.springboot.persistance.jpa;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.jdbc.main.CapitalCase;
import com.springboot.project.properties.ReadProjectPropertiesFile;

public class SBJPARepositories {

	public void createJPAPersistance(String tableName, Connection con, String pack, String schemaName,String projectName) {
		try {
			PreparedStatement pstmt;
			ResultSet resultSet,rsf;
			pstmt = con.prepareStatement("select * from " + tableName);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();
			int columnCount = columnMetaData.getColumnCount();
			
			String controllerPackage = ReadProjectPropertiesFile.projectProps.getProperty("controller-Package");
			String basePackage = ReadProjectPropertiesFile.projectProps.getProperty("basePackage");
			String beanPackage = ReadProjectPropertiesFile.projectProps.getProperty("beanPackage");
			String repositoryPackage = ReadProjectPropertiesFile.projectProps.getProperty("repositoryPackage");

			String packageNameString = projectName+"\\"+basePackage+"\\" + pack + "\\"+repositoryPackage;
			File packageDir = new File(packageNameString);
			packageDir.mkdir();

			PrintWriter writer = new PrintWriter(
					packageNameString + "\\" + CapitalCase.toCapitalCase(tableName) + "Repository.java");

			String packageImport = "package "+basePackage+"." + pack + "."+repositoryPackage+";\n";
			packageImport += "import java.util.List; \n";
			packageImport += "\n";
			packageImport += "import org.springframework.data.jpa.repository.JpaRepository;\n\n";
			packageImport += "import "+basePackage+"." + pack + "."+beanPackage+"."+CapitalCase.toCapitalCase(tableName)+";\n\n";
			DatabaseMetaData databaseMetaData = con.getMetaData();
			/*String cataLog = con.getCatalog();
			rsf = databaseMetaData.getExportedKeys(cataLog, null, tableName);
			List<String> duplicateString = new ArrayList<String>();
			while (rsf.next()) {
				String fkTableName = rsf.getString("FKTABLE_NAME");
				String fkColumnName = rsf.getString("FKCOLUMN_NAME");
				String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
				if (fkSchemaName == null)
					fkSchemaName = "";
				if (!duplicateString.contains(fkTableName) && fkSchemaName.equalsIgnoreCase(schemaName))
					packageImport += "import com." + pack + ".bean." + CapitalCase.toCapitalCase(fkTableName) + "TO;\n";
				duplicateString.add(fkTableName);
			}*/
			writer.println(packageImport);
			writer.println("public interface " + CapitalCase.toCapitalCase(tableName) + "Repository extends JpaRepository<"+CapitalCase.toCapitalCase(tableName)+", Long> {");
			writer.println("\n");
			writer.println("	}");
			/*prepareSelectQuery(tableName, writer);
			databaseMetaData = con.getMetaData();
			cataLog = con.getCatalog();
			rsf = databaseMetaData.getExportedKeys(cataLog, null, tableName);
			duplicateString = new ArrayList<String>();
			while (rsf.next()) {
				String fkTableName = rsf.getString("FKTABLE_NAME");
				String fkColumnName = rsf.getString("FKCOLUMN_NAME");
				String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
				if (fkSchemaName == null)
					fkSchemaName = "";
				if (!duplicateString.contains(fkTableName) && fkSchemaName.equalsIgnoreCase(schemaName))
					prepareSelectQueryWithForeignKeys(tableName, writer, fkColumnName, fkTableName);
				duplicateString.add(fkTableName);
			}
			prepareUpdateQuery(tableName, writer);
			prepareDeleteQuery(tableName, writer);
			prepareSelectQueryById(tableName, writer);*/
			writer.close();
			//pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// do something
		}
	}

}
