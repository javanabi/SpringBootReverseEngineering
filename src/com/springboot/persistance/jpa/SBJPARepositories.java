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
import com.jdbc.main.SqlDatatypeTOJavaWrapperClass;
import com.springboot.project.properties.ReadProjectPropertiesFile;
import com.springboot.util.SBUtilImpl;

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
			packageImport += "import java.util.Optional;\n\n";
			packageImport += "import org.springframework.data.domain.Page; \n";
			packageImport += "import org.springframework.data.domain.Pageable;\n";
			packageImport += "import org.springframework.data.jpa.repository.Query; \n";
			packageImport += "import org.springframework.data.repository.query.Param; \n";
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
			String loginTableName = ReadProjectPropertiesFile.projectProps.getProperty("database-login-table");
			String loginColName = ReadProjectPropertiesFile.projectProps.getProperty("login-col-name");
			if(loginTableName.equalsIgnoreCase(CapitalCase.toCapitalCase(tableName).toLowerCase())) {
				writer.println("Optional<"+CapitalCase.toCapitalCase(tableName)+"> findBy"+CapitalCase.toCapitalCase(loginColName)+"(String "+loginColName+");");
			}
			
			if(ReadProjectPropertiesFile.projectProps.getProperty("change-pass-feature").equals("1") && ReadProjectPropertiesFile.projectProps.getProperty("database-login-table").equalsIgnoreCase(CapitalCase.replaceUnderScore(tableName).toLowerCase())) {
				String emailColName = CapitalCase.toCapitalCase(ReadProjectPropertiesFile.projectProps.getProperty("email-col-name"));
				writer.println("");
				writer.println("		"+CapitalCase.toCapitalCase(tableName)+" findBy"+emailColName+"(String emailid);");
			}
			
			
			 
			try {
			for (int i = 2; i <= columnCount; i++) {
				String name = columnMetaData.getColumnName(i).toLowerCase();
				if (isForeignKey(con, tableName, name)) {
					String fkTableName = SBUtilImpl.getForeignKeyTableName(con, tableName, name);
					writer.println("		@Query(\"from "+CapitalCase.toCapitalCase(tableName)+" "+CapitalCase.getFirstCharecter(tableName)+" where "+CapitalCase.getFirstCharecter(tableName)+"."+CapitalCase.replaceUnderScore(fkTableName).toLowerCase()+".id = :"+name+"\")");
						writer.println("		public Page<"+CapitalCase.toCapitalCase(tableName)+"> findAllBy"+CapitalCase.toCapitalCase(fkTableName)+"(@Param(\""+name.toLowerCase()+"\") "+SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(i))+" "+name+",Pageable pageable);\n");
				}
			}
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(loginTableName.equalsIgnoreCase(CapitalCase.toCapitalCase(tableName).toLowerCase())) {
				String passColName = ReadProjectPropertiesFile.projectProps.getProperty("password-col-name");
				writer.println("	@Query(\"from "+CapitalCase.toCapitalCase(tableName)+" "+CapitalCase.getFirstCharecter(tableName)+" where "+CapitalCase.getFirstCharecter(tableName)+"."+loginColName+" = :"+loginColName+" and "+CapitalCase.getFirstCharecter(tableName)+"."+passColName+" = :"+passColName+"\")");
				writer.println("	public Optional<"+CapitalCase.toCapitalCase(tableName)+"> findBy"+CapitalCase.toCapitalCase(loginColName)+"and"+CapitalCase.toCapitalCase(passColName)+"(@Param(\""+loginColName+"\") String "+loginColName+",");
				writer.println("			@Param(\""+passColName+"\") String "+passColName+");");
			}
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
	
	private boolean isForeignKey(Connection con, String tableName, String name) throws Exception {
		boolean flag = false;
		ResultSet rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
		while (rsf.next()) {
			String fkColumnName = rsf.getString("FKCOLUMN_NAME");
			// System.out.println("Table : "+tableName+" FKColumn : "+fkColumnName+"
			// MatchColName : "+name);
			if (fkColumnName.equalsIgnoreCase(name)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

}
