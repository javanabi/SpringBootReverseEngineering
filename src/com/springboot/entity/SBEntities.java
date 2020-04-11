package com.springboot.entity;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jdbc.main.CapitalCase;
import com.jdbc.main.SqlDatatypeTOJavaWrapperClass;
import com.springboot.project.properties.ReadProjectPropertiesFile;

public class SBEntities {
	
	public void createEntityClasses(String tableName, Connection con,String pack,String schemaName,String projectName) {
		try {
			PreparedStatement pstmt;
			ResultSet resultSet;
			pstmt = con.prepareStatement("select * from " + tableName);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();
			int columnCount = columnMetaData.getColumnCount();
			// The column count starts from 1


			String beanPackage = ReadProjectPropertiesFile.projectProps.getProperty("bean-jpa-modal-entity-pojo-Package");
			String basePackage = ReadProjectPropertiesFile.projectProps.getProperty("basePackage");
			
			String packageNameString = projectName+"\\"+basePackage+"\\";
			File packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + pack;
			packageDir = new File(packageNameString);
			packageDir.mkdir();
			packageNameString = packageNameString + "\\"+beanPackage;
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			PrintWriter writer = new PrintWriter(
					packageNameString + "\\" + CapitalCase.toCapitalCase(tableName) + ".java");

			String packageImport = "package "+basePackage+"." + pack + "."+beanPackage+";\n";
			
			packageImport += "import java.io.Serializable; \n";
			packageImport += "import javax.persistence.Column; \n";
			packageImport += "import javax.persistence.Entity; \n";
			packageImport += "import javax.persistence.GeneratedValue; \n";
			packageImport += "import javax.persistence.GenerationType; \n";
			packageImport += "import javax.persistence.Id; \n";
			packageImport += "import javax.persistence.Table; \n";
			 
			
			writer.println(packageImport);
			writer.println("@Entity");
			writer.println("@Table(name = \""+tableName+"\")");
			writer.println("public class " + CapitalCase.toCapitalCase(tableName) + " implements Serializable {");
			writer.println("\n");
			writer.println("	private static final long serialVersionUID = 1L;\n");
			writer.println("\n");
			
			writer.println("		@Id");
			writer.println("		@GeneratedValue(strategy = GenerationType.AUTO)");
			writer.println("		private "
							+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(1))  + " "
							+ columnMetaData.getColumnName(1).toLowerCase() + ";");
			

			for (int i = 2; i <= columnCount; i++) {
				String name = columnMetaData.getColumnName(i);
				writer.println("	@Column(name = \""+name.toLowerCase()+"\")");
				writer.println("	private "
						+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(i))  + " "
						+ name.toLowerCase() + ";\n");
			}
			
			//Contructors
			writer.println("		public " + CapitalCase.toCapitalCase(tableName) + "() {");
			writer.println("			");
			writer.println("		}");

			String constructorsArgs = "";
			for (int i = 2; i <= columnCount;) {
				String name = columnMetaData.getColumnName(i);
				constructorsArgs  = constructorsArgs +  SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(i).toLowerCase())+" "+name.toLowerCase();
				i++;
				if(i <= columnCount)
					constructorsArgs = constructorsArgs +", ";
			}
			
			
			writer.println("		public " + CapitalCase.toCapitalCase(tableName) + "("+constructorsArgs+") {");
			for (int i = 2; i <= columnCount;i++) {
				String name = columnMetaData.getColumnName(i);
				writer.println("		this."+name.toLowerCase()+" = "+name.toLowerCase()+";");
			}
			writer.println("		}");

			/* rsf = con.getMetaData().getExportedKeys(con.getCatalog(), null, tableName);
			List<String> duplicateString = new ArrayList<String>();
			while (rsf.next()) {
				// System.out.println(" PK : "+rsf.getString("PKTABLE_NAME")+" FK :
				// "+rsf.getString("FKTABLE_NAME")+" SCHEM "+rsf.getString("FKTABLE_SCHEM"));
				String fkTableName = rsf.getString("FKTABLE_NAME");
				String replacefk = CapitalCase.replaceUnderScore(rsf.getString("FKTABLE_NAME"));
				String fkColumnName = rsf.getString("FKCOLUMN_NAME");
				String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
				if(fkSchemaName == null) fkSchemaName = "";
				if (!duplicateString.contains(fkTableName) && fkSchemaName.equalsIgnoreCase(schemaName))
					writer.println("	private List<" + CapitalCase.toBeanClass(fkTableName) + "> "
							+ replacefk.toLowerCase() + ";");
				duplicateString.add(fkTableName);
			}*/
			
			/*	rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
				List<String> pkString = new ArrayList<String>();
				while (rsf.next()) {
					String pkTableName = rsf.getString("PKTABLE_NAME");
					String replacepk = CapitalCase.replaceUnderScore(rsf.getString("PKTABLE_NAME"));
					String pkSchemaName = rsf.getString("FKTABLE_SCHEM");
					if(pkSchemaName==null) pkSchemaName="";
					if (!pkString.contains(pkTableName) && pkSchemaName.equalsIgnoreCase(schemaName))
						writer.println("	"+CapitalCase.toBeanClass(pkTableName) + " "
								+ replacepk.toLowerCase() + ";");
					pkString.add(pkTableName);
				}*/

			
			//Setters and Getters
			for (int i = 1; i <= columnCount; i++) {
				String name = columnMetaData.getColumnName(i);
				writer.println("\n");
				writer.println("	public void set" + CapitalCase.toCapitalCase(name) + "("
						+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(i)) + " "
						+ name.toLowerCase() + "){");
				writer.println("			this." + name.toLowerCase() + " = " + name.toLowerCase() + ";");
				writer.println("		}");
				writer.println(
						"		    public " + SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(i))
								+ " get" + CapitalCase.toCapitalCase(name) + "(){");
				writer.println("		return " + name.toLowerCase() + ";");
				writer.println("	}");
			}
			
			//toString Method
			writer.println("		@Override");
			writer.println("		public String toString() {");
			String toStringReturn = "	return \""+ CapitalCase.toCapitalCase(tableName)+" [";
			for (int i = 1; i <= columnCount;) {
				String name = columnMetaData.getColumnName(i);
				toStringReturn = toStringReturn + name.toLowerCase()+"=\"+"+name.toLowerCase()+"+ \"";
				i++;
				if(i <= columnCount)
					toStringReturn = toStringReturn +", ";
			}
			writer.println(toStringReturn+"]\";");
			writer.println("		}");

			/*rsf = con.getMetaData().getExportedKeys(con.getCatalog(), null, tableName);
			duplicateString = new ArrayList<String>();
			
			while (rsf.next()) {
				String fkTableName = rsf.getString("FKTABLE_NAME");
				String replacefk = CapitalCase.replaceUnderScore(rsf.getString("FKTABLE_NAME"));
				String fkColumnName = rsf.getString("FKCOLUMN_NAME");
				writer.println("\n");
				String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
				if(fkSchemaName == null) fkSchemaName = "";
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
			}*/
			
			
			/*rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
			pkString = new ArrayList<String>();
			while (rsf.next()) {
				String pkTableName = rsf.getString("PKTABLE_NAME");
				String replacepk = CapitalCase.replaceUnderScore(rsf.getString("PKTABLE_NAME"));
				String pkSchemaName = rsf.getString("FKTABLE_SCHEM");
				if(pkSchemaName==null) pkSchemaName = "";
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
			}*/
			
			
			/*rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
//			pkString = new ArrayList<String>();
			while (rsf.next()) {
				String pkTableName = rsf.getString("PKTABLE_NAME");
				String replacepk = CapitalCase.replaceUnderScore(rsf.getString("PKTABLE_NAME"));
				writer.println("\n");
//				pkString.add(pkTableName);
				String pkSchemaName = rsf.getString("FKTABLE_SCHEM");
				
			}*/

			writer.println(" }");
			writer.close();
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
