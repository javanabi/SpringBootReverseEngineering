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
import com.springboot.persistance.jpa.JPAPersistance;
import com.springboot.project.properties.ReadProjectPropertiesFile;

public class SBEntities {
	
	List<String> tableList = JPAPersistance.readTablesFromPRoperteisFile();

	public void createEntityClasses(String tableName, Connection con, String pack, String schemaName,
			String projectName) throws Exception {
		try {
			PreparedStatement pstmt;
			ResultSet resultSet;
			pstmt = con.prepareStatement("select * from " + tableName);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();
			int columnCount = columnMetaData.getColumnCount();
			// The column count starts from 1

			String beanPackage = ReadProjectPropertiesFile.projectProps
					.getProperty("bean-jpa-modal-entity-pojo-Package");
			String basePackage = ReadProjectPropertiesFile.projectProps.getProperty("basePackage");
			String loginTableName = ReadProjectPropertiesFile.projectProps.getProperty("database-login-table");
			String fileColumnName = ReadProjectPropertiesFile.projectProps.getProperty("userprofileimage-col-name");
			
			String packageNameString = projectName + "\\" + basePackage + "\\";
			File packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + pack;
			packageDir = new File(packageNameString);
			packageDir.mkdir();
			packageNameString = packageNameString + "\\" + beanPackage;
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			PrintWriter writer = new PrintWriter(
					packageNameString + "\\" + CapitalCase.toCapitalCase(tableName) + ".java");

			String packageImport = "package " + basePackage + "." + pack + "." + beanPackage + ";\n";

			packageImport += "import java.io.Serializable; \n";
			packageImport += "import java.util.Date; \n";
			packageImport += "import javax.persistence.Column; \n";
			packageImport += "import javax.persistence.Entity; \n";
			packageImport += "import javax.persistence.GeneratedValue; \n";
			packageImport += "import javax.persistence.GenerationType; \n";
			packageImport += "import javax.persistence.Id; \n";
			packageImport += "import javax.persistence.Table; \n";
			packageImport += "import javax.persistence.JoinColumn;\n";
			packageImport += "import javax.persistence.Lob;\n";
			packageImport += "import javax.persistence.Transient;\n";
			packageImport += "import javax.persistence.JoinTable;\n";
			packageImport += "import javax.persistence.ManyToMany;\n";
			packageImport += "import javax.validation.constraints.NotEmpty;\n";
			packageImport += "import org.springframework.format.annotation.DateTimeFormat;\n";
			packageImport += "import org.springframework.web.multipart.MultipartFile;\n";
			packageImport += "import " + basePackage + "."+pack+".security.Role; \n";
			
			packageImport = createImportPrimaryForeignKeyReferencesColumnNamesAnnotations(con, tableName, writer,
					packageImport);
			String lazyLoadColumns = createPrimaryForeignKeyLazyLoadReferencesColumnNames(con, tableName, writer);

			writer.println(packageImport);
			writer.println("@Entity");
			writer.println("@Table(name = \"" + tableName + "\")");
			writer.print(lazyLoadColumns);
			writer.println("public class " + CapitalCase.toCapitalCase(tableName) + " implements Serializable {");
			writer.println("\n");
			writer.println("		private static final long serialVersionUID = 1L;\n");
			writer.println("		@Id");
			writer.println("		@Column(name = \"" + columnMetaData.getColumnName(1).toLowerCase() + "\")");
			writer.println("		@GeneratedValue(strategy = GenerationType.IDENTITY)");
			writer.println("		private "
					+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(1)) + " id;\n");
			boolean fileFlag = false;
			for (int i = 2; i <= columnCount; i++) {
				String name = columnMetaData.getColumnName(i);
				if (!isForeignKey(con, tableName, name)) {
					if(columnMetaData.getColumnTypeName(i).equalsIgnoreCase("Date") || columnMetaData.getColumnTypeName(i).equalsIgnoreCase("datetime")) {
						//writer.println("		@NotEmpty");
						//writer.println("		@Column(name = \"" + name.toLowerCase() + "\")");
						writer.println("		@DateTimeFormat(pattern = \"dd/MM/yyyy\")");
						writer.println("		private "
								+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(i)) + " "
								+ name.toLowerCase() + ";\n");
					
					} else if(columnMetaData.getColumnTypeName(i).equalsIgnoreCase("Timestamp")) {
						writer.println("		@Column(name = \"" + name.toLowerCase() + "\", columnDefinition=\"TIMESTAMP DEFAULT CURRENT_TIMESTAMP\")");
						writer.println("		private "
								+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(i)) + " "
								+ name.toLowerCase() + ";\n");
					
					}else if(columnMetaData.getColumnTypeName(i).equalsIgnoreCase("LONGBLOB")) {
						fileFlag = true;
						writer.println("		@Lob");
						writer.println("		@Column(name = \"" + name.toLowerCase() + "\")");
						writer.println("		private "
								+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(i)) + " "
								+ name.toLowerCase() + ";\n");
						
						writer.println("		@Transient");
						writer.println("		private MultipartFile "+ name.toLowerCase() +"file;");
						
						i=i+1;
						name = columnMetaData.getColumnName(i);
						writer.println("		@Column(name = \"" + name.toLowerCase() + "\")");
						writer.println("		private "
								+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(i)) + " "
								+ name.toLowerCase() + ";\n");	
						
						i=i+1;
						name = columnMetaData.getColumnName(i);
						writer.println("		@Column(name = \"" + name.toLowerCase() + "\")");
						writer.println("		private "
								+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(i)) + " "
								+ name.toLowerCase() + ";\n");	
						
						writer.println("		@Transient");
						writer.println("		private String fileDownloadPath;");
					
					}else {
						writer.println("		@NotEmpty");
						writer.println("		@Column(name = \"" + name.toLowerCase() + "\")");
						writer.println("		private "
								+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(i)) + " "
								+ name.toLowerCase() + ";\n");	
					}
				}
			}
			
			if(loginTableName.equalsIgnoreCase(CapitalCase.toCapitalCase(tableName).toLowerCase())) {
				
					    writer.println("		@ManyToMany(cascade = CascadeType.MERGE)");
					    writer.println("		@JoinTable(name = \"user_role\", joinColumns = {");
					    writer.println("        @JoinColumn(name = \"USER_ID\", referencedColumnName = \"" + columnMetaData.getColumnName(1).toLowerCase() + "\") }, inverseJoinColumns = {");
					    writer.println("		@JoinColumn(name = \"ROLE_ID\", referencedColumnName = \"ID\") })");
					    writer.println("		private List<Role> roles;");
					    writer.println("");
			}

			createPrimaryForeignKeyReferencesColumnNames(con, tableName, writer);

			// Contructors
			writer.println("		public " + CapitalCase.toCapitalCase(tableName) + "() {");
			writer.println("			");
			writer.println("		}");

			String constructorsArgs = "";
			boolean lastForeignKey = isForeignKey(con, tableName, columnMetaData.getColumnName(columnCount));
			for (int i = 2; i <= columnCount;) {
				String name = columnMetaData.getColumnName(i);
				boolean flag = isForeignKey(con, tableName, name);
				if (!flag)
					constructorsArgs = constructorsArgs + SqlDatatypeTOJavaWrapperClass.toWrapperClass(
							columnMetaData.getColumnTypeName(i).toLowerCase()) + " " + name.toLowerCase();
				i++;
				if (i <= columnCount && !flag)
					constructorsArgs = constructorsArgs + ", ";
			}

			try{if (lastForeignKey)
				constructorsArgs = constructorsArgs.substring(0, constructorsArgs.trim().length() - 1);
				writer.println("		public " + CapitalCase.toCapitalCase(tableName) + "(" + constructorsArgs + ") {");
				for (int i = 2; i <= columnCount; i++) {
					String name = columnMetaData.getColumnName(i);
					if (!isForeignKey(con, tableName, name))
						writer.println("		this." + name.toLowerCase() + " = " + name.toLowerCase() + ";");
				}
				writer.println("		}");
			}catch (Exception e) {
				e.printStackTrace();
			}

			

			// Setters and Getters
			for (int i = 1; i <= columnCount; i++) {
				String name = columnMetaData.getColumnName(i);
				if(i == 1 && name.equals("id"))
					continue;
				if (!isForeignKey(con, tableName, name)) {
					if (i == 1) {
						writer.println("\n");
						writer.println("	public void set" + CapitalCase.toCapitalCase(name) + "("
								+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(1))
								+ " " + name.toLowerCase() + "){");
						writer.println("			this.id = " + name.toLowerCase() + ";");
						writer.println("		}");
						writer.println("		    public "
								+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(1))
								+ " get" + CapitalCase.toCapitalCase(name) + "(){");
						writer.println("		return id;");
						writer.println("	}");
					} else if(columnMetaData.getColumnTypeName(i).equalsIgnoreCase("LONGBLOG")) {
						writer.println("\n");
						writer.println("	public void set" + CapitalCase.toCapitalCase(name) + "file(MultipartFile " + name.toLowerCase() + "file){");
						writer.println("			this." + name.toLowerCase() + "file = " + name.toLowerCase() + "file;");
						writer.println("		}");
						writer.println("		    public MultipartFile get" + CapitalCase.toCapitalCase(name) + "file(){");
						writer.println("		return " + name.toLowerCase() + "file;");
						writer.println("	}");
					
					}else {

						writer.println("\n");
						writer.println("	public void set" + CapitalCase.toCapitalCase(name) + "("
								+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(i))
								+ " " + name.toLowerCase() + "){");
						writer.println("			this." + name.toLowerCase() + " = " + name.toLowerCase() + ";");
						writer.println("		}");
						writer.println("		    public "
								+ SqlDatatypeTOJavaWrapperClass.toWrapperClass(columnMetaData.getColumnTypeName(i))
								+ " get" + CapitalCase.toCapitalCase(name) + "(){");
						writer.println("		return " + name.toLowerCase() + ";");
						writer.println("	}");
					}
				}
			}
			if(fileFlag) {
				writer.println("	public MultipartFile get"+CapitalCase.toCapitalCase(fileColumnName)+"datafile() {");
				writer.println("    	return "+fileColumnName+"datafile;");
				writer.println("	}");
				writer.println("");
				writer.println("	public void set"+CapitalCase.toCapitalCase(fileColumnName)+"datafile(MultipartFile "+fileColumnName+"datafile) {");
				writer.println("     	this."+fileColumnName+"datafile = "+fileColumnName+"datafile;");
				writer.println(" 	}");
				
				writer.println("				public String getFileDownloadPath() {");
				writer.println("					return fileDownloadPath;");
				writer.println("				}");
				writer.println("");
				writer.println("				public void setFileDownloadPath(String fileDownloadPath) {");
				writer.println("					this.fileDownloadPath = fileDownloadPath;");
				writer.println("				}");
				
			}
			writer.println("	public Long getId() {");
			writer.println("    	return id;");
			writer.println("	}");
			writer.println("");
			writer.println("	public void setId(Long id) {");
			writer.println("     	this.id = id;");
			writer.println(" 	}");

			createPrimaryForeignKeySetterMethods(con, tableName, writer);
			
			if(loginTableName.equalsIgnoreCase(CapitalCase.toCapitalCase(tableName).toLowerCase())) {
			    writer.println("");
			    writer.println("public List<Role> getRoles()");
			    writer.println("{");
			    writer.println("				return roles;");
			    writer.println("	}");
			    writer.println("public void setRoles(List<Role> roles)");
			    writer.println("{");
			    writer.println("	this.roles = roles;");
			    writer.println("}");
	}

			// toString Method
			writer.println("		@Override");
			writer.println("		public String toString() {");
			String toStringReturn = "	return \"" + CapitalCase.toCapitalCase(tableName) + " [";
			for (int i = 1; i <= columnCount;) {
				String name = columnMetaData.getColumnName(i);
				if (!isForeignKey(con, tableName, name))
					if (i == 1) 
						toStringReturn = toStringReturn + name.toLowerCase() + "=\"+ id + \"";
					else
						toStringReturn = toStringReturn + name.toLowerCase() + "=\"+" + name.toLowerCase() + "+ \"";
						
				i++;
				if (i <= columnCount)
					toStringReturn = toStringReturn + ", ";
			}
			writer.println(toStringReturn + "]\";");
			writer.println("		}");

			ResultSet rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
			// pkString = new ArrayList<String>();
			while (rsf.next()) {
				String pkTableName = rsf.getString("PKTABLE_NAME");
				String replacepk = CapitalCase.replaceUnderScore(rsf.getString("PKTABLE_NAME"));
				writer.println("\n");
				// pkString.add(pkTableName);
				String pkSchemaName = rsf.getString("FKTABLE_SCHEM");

			}

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

	private String createPrimaryForeignKeyLazyLoadReferencesColumnNames(Connection con, String tableName,
			PrintWriter writer) throws Exception {
		ResultSet rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
		String lazyString = "@JsonIgnoreProperties({\"hibernateLazyInitializer\",\"handler\"";
		boolean flag = false;
		while (rsf.next()) {
			String replacepk = CapitalCase.replaceUnderScore(rsf.getString("PKTABLE_NAME"));
			lazyString = lazyString + ",\"" + replacepk.toLowerCase() + "\"";
			flag = true;
		}
		if (flag)
			return lazyString + "})\n";
		else
			return "";
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

	private void createPrimaryForeignKeySetterMethods(Connection con, String tableName, PrintWriter writer)
			throws Exception {
		ResultSet rsf = con.getMetaData().getExportedKeys(con.getCatalog(), null, tableName);
		List<String> duplicateString = new ArrayList<String>();

		while (rsf.next()) {
			String fkTableName = rsf.getString("FKTABLE_NAME");
			String replacefk = CapitalCase.replaceUnderScore(rsf.getString("FKTABLE_NAME"));
			String fkColumnName = rsf.getString("FKCOLUMN_NAME");
			writer.println("\n");
			String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
			if (fkSchemaName == null)
				fkSchemaName = "";
			if (!duplicateString.contains(fkTableName)  && !tableList.contains(fkTableName.toLowerCase()) ) {// && fkSchemaName.equalsIgnoreCase(schemaName)) {
				if (!isForeignKey(con, tableName, replacefk.toLowerCase())) {
					writer.println("	public void set" + CapitalCase.toCapitalCase(replacefk.toLowerCase()) + "(List<"
							+ CapitalCase.toCapitalCase(fkTableName) + "> " + replacefk.toLowerCase() + "){");
					writer.println("		this." + replacefk.toLowerCase() + " = " + replacefk.toLowerCase() + ";");
					writer.println("	}");
					writer.println("	public List<" + CapitalCase.toCapitalCase(replacefk) + "> get"
							+ CapitalCase.toCapitalCase(replacefk.toLowerCase()) + "(){");
					writer.println("		return " + replacefk.toLowerCase() + ";");
					writer.println("	}");
				}
			}
			duplicateString.add(fkTableName);
		}

		rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
		List<String> pkString = new ArrayList<String>();
		while (rsf.next()) {
			String pkTableName = rsf.getString("PKTABLE_NAME");
			String replacepk = CapitalCase.replaceUnderScore(rsf.getString("PKTABLE_NAME"));
			String pkSchemaName = rsf.getString("FKTABLE_SCHEM");
			if (pkSchemaName == null)
				pkSchemaName = "";
			if (!pkString.contains(pkTableName)) {// && pkSchemaName.equalsIgnoreCase(schemaName)) {
				writer.println("	public void set" + CapitalCase.toCapitalCase(replacepk.toLowerCase()) + "("
						+ CapitalCase.toCapitalCase(pkTableName) + " " + replacepk.toLowerCase() + "){");
				writer.println("		this." + replacepk.toLowerCase() + " = " + replacepk.toLowerCase() + ";");
				writer.println("	}");
				writer.println("	public " + CapitalCase.toCapitalCase(replacepk) + " get"
						+ CapitalCase.toCapitalCase(replacepk.toLowerCase()) + "(){");
				writer.println("		return " + replacepk.toLowerCase() + ";");
				writer.println("	}");
			}
			pkString.add(pkTableName);
		}

	}

	private String createImportPrimaryForeignKeyReferencesColumnNamesAnnotations(Connection con, String tableName,
			PrintWriter writer, String packageImport) throws Exception {
		ResultSet rsf = con.getMetaData().getExportedKeys(con.getCatalog(), null, tableName);
		List<String> duplicateString = new ArrayList<String>();
		while (rsf.next()) {
			// System.out.println(" PK : "+rsf.getString("PKTABLE_NAME")+" FK :
			// "+rsf.getString("FKTABLE_NAME")+" SCHEM "+rsf.getString("FKTABLE_SCHEM"));
			String fkTableName = rsf.getString("FKTABLE_NAME");
			String replacefk = CapitalCase.replaceUnderScore(rsf.getString("FKTABLE_NAME"));
			String fkColumnName = rsf.getString("FKCOLUMN_NAME");
			String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
			if (fkSchemaName == null)
				fkSchemaName = "";
			if (!duplicateString.contains(fkTableName))// && fkSchemaName.equalsIgnoreCase(schemaName))
				// writer.println(" private List<" + CapitalCase.toBeanClass(fkTableName) + "> "
				// + replacefk.toLowerCase() + ";");
				packageImport += "import javax.persistence.OneToMany; \n";
			packageImport += "import javax.persistence.CascadeType; \n";
			packageImport += "import java.util.List; \n";
			duplicateString.add(fkTableName);
		}

		rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
		List<String> pkString = new ArrayList<String>();
		while (rsf.next()) {
			String pkTableName = rsf.getString("PKTABLE_NAME");
			String replacepk = CapitalCase.replaceUnderScore(rsf.getString("PKTABLE_NAME"));
			String pkSchemaName = rsf.getString("FKTABLE_SCHEM");
			packageImport += "import com.fasterxml.jackson.annotation.JsonIgnoreProperties; \n";
			packageImport += "import javax.persistence.ManyToOne; \n";
			packageImport += "import javax.persistence.JoinColumn; \n";
			packageImport += "import javax.persistence.FetchType; \n";
			pkString.add(pkTableName);
		}

		return packageImport;

	}

	private void createPrimaryForeignKeyReferencesColumnNames(Connection con, String tableName, PrintWriter writer)
			throws Exception {
		ResultSet rsf = con.getMetaData().getExportedKeys(con.getCatalog(), null, tableName);
		List<String> duplicateString = new ArrayList<String>();
		while (rsf.next()) {
			// System.out.println(" PK : "+rsf.getString("PKTABLE_NAME")+" FK :
			// "+rsf.getString("FKTABLE_NAME")+" SCHEM "+rsf.getString("FKTABLE_SCHEM"));
			String fkTableName = rsf.getString("FKTABLE_NAME");
			String replacefk = CapitalCase.replaceUnderScore(rsf.getString("FKTABLE_NAME"));
			String fkColumnName = rsf.getString("FKCOLUMN_NAME");
			String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
			if (fkSchemaName == null)
				fkSchemaName = "";
			if (!duplicateString.contains(fkTableName) && !tableList.contains(fkTableName.toLowerCase())) {// && fkSchemaName.equalsIgnoreCase(schemaName))
				writer.println("      	@OneToMany(mappedBy = \""
						+ CapitalCase.replaceUnderScore(tableName).toLowerCase() + "\", cascade = CascadeType.ALL)");
				writer.println("	 	private List<" + CapitalCase.toCapitalCase(fkTableName) + "> "
						+ replacefk.toLowerCase() + ";\n");
			}
			duplicateString.add(fkTableName);
		}

		rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
		List<String> pkString = new ArrayList<String>();
		while (rsf.next()) {
			String pkTableName = rsf.getString("PKTABLE_NAME");
			String replacepk = CapitalCase.replaceUnderScore(rsf.getString("PKTABLE_NAME"));
			String pkSchemaName = rsf.getString("FKTABLE_SCHEM");
			String fkColumnName = rsf.getString("FKCOLUMN_NAME");
			// if(pkSchemaName==null) pkSchemaName="";
			// if (!pkString.contains(pkTableName)) &&
			// pkSchemaName.equalsIgnoreCase(schemaName))
			writer.println("       @ManyToOne(fetch=FetchType.EAGER)");
			writer.println("       @JoinColumn(name=\"" + fkColumnName.toLowerCase() + "\")");
			writer.println("	   " + CapitalCase.toCapitalCase(pkTableName) + " " + replacepk.toLowerCase() + ";\n");
			pkString.add(pkTableName);
		}

	}

}
