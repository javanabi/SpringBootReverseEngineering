package com.springboot.service;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
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
import com.springboot.util.SBUtilImpl;

public class SBServiceClasses extends AbstractDataAccessObject{
	
	PreparedStatement pstmt;
	ResultSet resultSet, rs, rsf, tableTypes, contraintsRecords;
	String schemaName = getProperties().getProperty("duser");
	String controllerPackage = ReadProjectPropertiesFile.projectProps.getProperty("controller-Package");
	String pack = ReadProjectPropertiesFile.projectProps.getProperty("pack");
	String basePackage = ReadProjectPropertiesFile.projectProps.getProperty("basePackage");
	String beanPackage = ReadProjectPropertiesFile.projectProps.getProperty("beanPackage");
	String repositoryPackage = ReadProjectPropertiesFile.projectProps.getProperty("repositoryPackage");
	String servicePackage = ReadProjectPropertiesFile.projectProps.getProperty("servicePackage");
	String author = ReadProjectPropertiesFile.projectProps.getProperty("project-author");
	String fileColumnName = ReadProjectPropertiesFile.projectProps.getProperty("userprofileimage-col-name");
	String logintable = ReadProjectPropertiesFile.projectProps.getProperty("database-login-table");
	List<String> tableList = JPAPersistance.readTablesFromPRoperteisFile();
	
	public SBServiceClasses() {
	}
	
	public void createServiceImplMethods(Connection con,String className,String title,String pack,String projectName) throws SQLException{
		try {
			PreparedStatement pstmt;
			
			ResultSet resultSet;
			pstmt = con.prepareStatement("select * from " + className);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();
			int columnCount = columnMetaData.getColumnCount();
			
			String basePackage = ReadProjectPropertiesFile.projectProps.getProperty("basePackage");
			String beanPackage = ReadProjectPropertiesFile.projectProps.getProperty("beanPackage");
			String repositoryPackage = ReadProjectPropertiesFile.projectProps.getProperty("repositoryPackage");
			String servicePackage = ReadProjectPropertiesFile.projectProps.getProperty("servicePackage");
			String loginTableName = ReadProjectPropertiesFile.projectProps.getProperty("database-login-table");
			String fileColumnName = ReadProjectPropertiesFile.projectProps.getProperty("userprofileimage-col-name");
			List<String> basicTableList= JPAPersistance.basicTableListFromPRoperteisFile();
			List<String> ignoreSelectUserTables= JPAPersistance.tablesListTODO("ignore-select-by-UserTables");
			
			boolean fileFlag = false;
			for (int i = 1; i <=columnMetaData.getColumnCount(); i++) {
				String colType = columnMetaData.getColumnTypeName(i);
				if(colType.equalsIgnoreCase("LONGBLOB")) {
					fileFlag = true;
				}
			}
			
			boolean passwordFlag = false;
			for (int i = 1; i <=columnMetaData.getColumnCount(); i++) {
				String colType = columnMetaData.getColumnTypeName(i);
				if(colType.equalsIgnoreCase("password")) {
					passwordFlag = true;
				}
			}
			
			String packageNameString = projectName+"\\"+basePackage+"\\";
			File packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + pack;
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + "\\"+servicePackage;
			packageDir = new File(packageNameString);
			packageDir.mkdir();
			
			PrintWriter writer = new PrintWriter(packageNameString + "\\"
					+ CapitalCase.toCapitalCase(className) + "ServiceImpl.java");

			String packageImport = "package "+basePackage+"." + pack + "."+servicePackage+";\n";
			packageImport += "import java.util.ArrayList; \n";
			packageImport += "import java.util.List; \n";
			packageImport += "import java.util.Optional; \n\n";		 

			packageImport += "import org.springframework.stereotype.Service;\n";
			packageImport += "import org.springframework.web.multipart.MultipartFile;\n";
			packageImport += "import org.springframework.beans.factory.annotation.Autowired;\n";
			
			if (ReadProjectPropertiesFile.projectProps.getProperty("change-pass-feature").equals("1")
					&& ReadProjectPropertiesFile.projectProps.getProperty("database-login-table")
							.equalsIgnoreCase(CapitalCase.replaceUnderScore(className).toLowerCase())) {
				packageImport += "import com.springboot.security.PasswordResetToken;\n";
				packageImport += "import com.springboot.security.PasswordResetTokenRepository;\n";
			}
			 
			if(loginTableName.equalsIgnoreCase(CapitalCase.toCapitalCase(className).toLowerCase()))
				packageImport += "import org.springframework.security.crypto.password.PasswordEncoder;";
			packageImport += "import org.springframework.data.domain.Page;\n";
			packageImport += "import org.springframework.data.domain.PageRequest;\n";
			packageImport += "import org.springframework.data.domain.Pageable;\n";
			packageImport += "import org.springframework.data.domain.Sort;\n";
			packageImport += "import com.springboot.security.CustomUserDetailsService;\n";
			packageImport += "import "+basePackage+"."+pack+".util.ConstructFilePathUtil;\n";
			packageImport += "import "+basePackage+"."+pack+"."+beanPackage+"."+CapitalCase.toCapitalCase(className)+";\n";
			packageImport += "import "+basePackage+"."+pack+"."+repositoryPackage+"."+CapitalCase.toCapitalCase(className)+"Repository;\n";
			packageImport += "import "+basePackage+"."+pack+"."+servicePackage+"i."+CapitalCase.toCapitalCase(className)+"ServiceI;\n";
			packageImport = createImportPrimaryForeignKeyReferencesColumnNamesAnnotations(con, className, writer,
					packageImport);
			packageImport += "import "+basePackage+"."+pack+".exception.DataNotFoundException;\n";
			packageImport += "import "+basePackage+"."+pack+".exception.DeleteException;\n";
			packageImport += "import "+basePackage+"."+pack+".exception.UpdateException;\n";
			packageImport += "import "+basePackage+"."+pack+".exception.CreateException;\n";
			packageImport += "import "+basePackage+"."+pack+".exception.FileStorageException;\n";
			packageImport += "import "+basePackage+"."+pack+".exception.MyFileNotFoundException;\n";
			packageImport += "import org.springframework.util.StringUtils;\n";
			packageImport += "import java.io.File;\n";
			packageImport += "import java.io.IOException;\n";
			
			/*DatabaseMetaData databaseMetaData = con.getMetaData();
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
			rsf.close();*/
			
			writer.println(packageImport);
			writer.println("@Service");
			writer.println("public class "
					+ CapitalCase.toCapitalCase(className) + "ServiceImpl implements "+CapitalCase.toCapitalCase(className)+"ServiceI{\n");
			writer.println("	@Autowired");
			writer.println("	"+CapitalCase.toCapitalCase(className)+"Repository "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository;\n");
			
			createAutowirePrimaryForeignKeyReferences(con,className,writer);
			
			writer.println("	@Autowired");
			writer.println("	CustomUserDetailsService customUserDetailsService;");
			
			if(loginTableName.equalsIgnoreCase(CapitalCase.toCapitalCase(className).toLowerCase())) {
				writer.println("	@Autowired");
				writer.println("	private PasswordEncoder passwordEncoder;\n");
			}
			
			if (ReadProjectPropertiesFile.projectProps.getProperty("change-pass-feature").equals("1")
					&& ReadProjectPropertiesFile.projectProps.getProperty("database-login-table")
							.equalsIgnoreCase(CapitalCase.replaceUnderScore(className).toLowerCase())) {
				writer.println("	@Autowired");
				writer.println("	private PasswordResetTokenRepository passwordTokenRepository;\n");

			}
			
			writer.println("	 /**");
			writer.println("     * gets all "+CapitalCase.toCapitalCase(className)+" from Database");
			writer.println("     * @return  a List containing "+CapitalCase.toCapitalCase(className));
			writer.println("     */");
			writer.println("    @Override");
			writer.println("    public List<"+CapitalCase.toCapitalCase(className)+"> getAll()throws DataNotFoundException{");
			writer.println("    try {");
			
			if(!fileFlag) {
				writer.println("    	List<"+CapitalCase.toCapitalCase(className)+"> "+CapitalCase.replaceUnderScore(className).toLowerCase()+"List = new ArrayList<>();");
				writer.println("        "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository.findAll().iterator().forEachRemaining("+CapitalCase.replaceUnderScore(className).toLowerCase()+"List::add);");
			}else {
				writer.println("				    	List<"+CapitalCase.toCapitalCase(className)+"> "+CapitalCase.replaceUnderScore(className).toLowerCase()+"List = "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository.findAll();"); 
				writer.println("				    	for ("+CapitalCase.toCapitalCase(className)+" "+CapitalCase.replaceUnderScore(className).toLowerCase()+" : "+CapitalCase.replaceUnderScore(className).toLowerCase()+"List) {");
				writer.println("				    		File folder = new File(\".//src//main//resources//static//download//\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".getId());");
				writer.println("				    		if(!folder.exists())");
				writer.println("				    			folder.mkdir();");
				writer.println("				    		String path = folder.getAbsolutePath()+\"/profile-\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".get"+CapitalCase.toCapitalCase(fileColumnName)+"name();");
				writer.println("				    		ConstructFilePathUtil.writeByte("+CapitalCase.replaceUnderScore(className).toLowerCase()+".get"+CapitalCase.toCapitalCase(fileColumnName)+"data(), path);");
				writer.println("				    		"+CapitalCase.replaceUnderScore(className).toLowerCase()+".setFileDownloadPath(\"/download/\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".getUserid()+\"/profile-\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".get"+CapitalCase.toCapitalCase(fileColumnName)+"name());");
				writer.println("						}");
			}
			writer.println("        return "+CapitalCase.replaceUnderScore(className).toLowerCase()+"List;");
			writer.println("		} catch (Exception e) {");
			writer.println("			throw new DataNotFoundException(e.getMessage());");
			writer.println("		}");
			writer.println("    }");
			writer.println("    ");
			writer.println("    public Page<"+CapitalCase.toCapitalCase(className)+"> getAll(Integer pageNo, Integer pageSize, String sortBy)throws DataNotFoundException{");
			writer.println("    try {");
			writer.println("        	Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));");
			if(!fileFlag) {
				writer.println("       		 return "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository.findAll(paging);");
			}else {
				writer.println("				    	Page<"+CapitalCase.toCapitalCase(className)+"> "+CapitalCase.replaceUnderScore(className).toLowerCase()+"List = "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository.findAll(paging);"); 
				writer.println("				    	for ("+CapitalCase.toCapitalCase(className)+" "+CapitalCase.replaceUnderScore(className).toLowerCase()+" : "+CapitalCase.replaceUnderScore(className).toLowerCase()+"List) {");
				writer.println("				    		File folder = new File(\".//src//main//resources//static//download//\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".getId());");
				writer.println("				    		if(!folder.exists())");
				writer.println("				    			folder.mkdir();");
				writer.println("				    		String path = folder.getAbsolutePath()+\"/profile-\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".get"+CapitalCase.toCapitalCase(fileColumnName)+"name();");
				writer.println("				    		ConstructFilePathUtil.writeByte("+CapitalCase.replaceUnderScore(className).toLowerCase()+".get"+CapitalCase.toCapitalCase(fileColumnName)+"data(), path);");
				writer.println("				    		"+CapitalCase.replaceUnderScore(className).toLowerCase()+".setFileDownloadPath(\"/download/\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".getUserid()+\"/profile-\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".get"+CapitalCase.toCapitalCase(fileColumnName)+"name());");
				writer.println("						}");
				writer.println("       		 return "+CapitalCase.replaceUnderScore(className).toLowerCase()+"List;");
			}
			writer.println("		} catch (Exception e) {");
			writer.println("			throw new DataNotFoundException(e.getMessage());");
			writer.println("		}");
			writer.println("    }");
			
			writer.println("    ");
			if(!ignoreSelectUserTables.contains(CapitalCase.replaceUnderScore(className).toLowerCase())) {
				writer.println("    public Page<"+CapitalCase.toCapitalCase(className)+"> getAll(String username,Integer pageNo, Integer pageSize, String sortBy)throws DataNotFoundException{");
				writer.println("    try {");
				writer.println("        	Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));");
				
				String fkTableName = "";
				try {
					for (int i = 2; i <= columnCount; i++) {
						String name = columnMetaData.getColumnName(i).toLowerCase();
						if (isForeignKey(con, className, name)) {
							  fkTableName = SBUtilImpl.getForeignKeyTableName(con, className, name);
						}
					}
					} catch (Exception e) {
						// TODO: handle exception
					}
				
				if(!fileFlag) {
					writer.println("       		 return "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository.findAllBy"+CapitalCase.toCapitalCase(fkTableName)+"("+CapitalCase.replaceUnderScore(logintable).toLowerCase()+"Repository.findByLoginid(username).get().getUserid(),paging);");
				}else {
					writer.println("				    	Page<"+CapitalCase.toCapitalCase(className)+"> "+CapitalCase.replaceUnderScore(className).toLowerCase()+"List = "+CapitalCase.replaceUnderScore(logintable).toLowerCase()+"Repository.findAllByUser("+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository.findByLoginid(username).get().getUserid(),paging);"); 
					writer.println("				    	for ("+CapitalCase.toCapitalCase(className)+" "+CapitalCase.replaceUnderScore(className).toLowerCase()+" : "+CapitalCase.replaceUnderScore(className).toLowerCase()+"List) {");
					writer.println("				    		File folder = new File(\".//src//main//resources//static//download//\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".getId());");
					writer.println("				    		if(!folder.exists())");
					writer.println("				    			folder.mkdir();");
					writer.println("				    		String path = folder.getAbsolutePath()+\"/profile-\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".get"+CapitalCase.toCapitalCase(fileColumnName)+"name();");
					writer.println("				    		ConstructFilePathUtil.writeByte("+CapitalCase.replaceUnderScore(className).toLowerCase()+".get"+CapitalCase.toCapitalCase(fileColumnName)+"data(), path);");
					writer.println("				    		"+CapitalCase.replaceUnderScore(className).toLowerCase()+".setFileDownloadPath(\"/download/\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".getUserid()+\"/profile-\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".get"+CapitalCase.toCapitalCase(fileColumnName)+"name());");
					writer.println("						}");
					writer.println("       		 return "+CapitalCase.replaceUnderScore(className).toLowerCase()+"List;");
				}
				writer.println("		} catch (Exception e) {");
				writer.println("			throw new DataNotFoundException(e.getMessage());");
				writer.println("		}");
				writer.println("    }");
			}
			
			
			
			writer.println("");
			writer.println("    /**");
			writer.println("     * finds a "+CapitalCase.toCapitalCase(className)+" in DB by its ID");
			writer.println("     * @param "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Id    Database ID of "+CapitalCase.toCapitalCase(className));
			writer.println("     * @return          "+CapitalCase.toCapitalCase(className)+" with ID = "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Id");
			writer.println("     */");
			writer.println("    @Override");
			writer.println("    public "+CapitalCase.toCapitalCase(className)+" findById(Long "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Id)throws DataNotFoundException{");
			writer.println("		try {");
			writer.println("            Optional<"+CapitalCase.toCapitalCase(className)+"> "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Optional = "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository.findById("+CapitalCase.replaceUnderScore(className).toLowerCase()+"Id);");
			writer.println("");
			writer.println("       	    if (!"+CapitalCase.replaceUnderScore(className).toLowerCase()+"Optional.isPresent()) {");
			writer.println("       	    	  throw new RuntimeException(\""+CapitalCase.toCapitalCase(className)+" Not Found!\");");
			writer.println("        		}");
			if(!fileFlag) {
				writer.println("        	return "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Optional.get();");
			}else {
				writer.println("			"+CapitalCase.toCapitalCase(className)+" "+CapitalCase.replaceUnderScore(className).toLowerCase()+" = "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Optional.get();");
				writer.println("				    		File folder = new File(\".//src//main//resources//static//download//\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".getId());");
				writer.println("			if (!folder.exists())");
				writer.println("				folder.mkdir();");
				writer.println("			String path = folder.getAbsolutePath() + \"/profile-\" + "+CapitalCase.replaceUnderScore(className).toLowerCase()+".getImagename();");
				writer.println("			ConstructFilePathUtil.writeByte("+CapitalCase.replaceUnderScore(className).toLowerCase()+".getImagedata(), path);");
				writer.println("    		"+CapitalCase.replaceUnderScore(className).toLowerCase()+".setFileDownloadPath(\"/download/\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".getUserid()+\"/profile-\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".getImagename());");
				writer.println("        	return "+CapitalCase.replaceUnderScore(className).toLowerCase()+";");
			}
			
			writer.println("		} catch (Exception e) {");
			writer.println("			throw new DataNotFoundException(e.getMessage());");
			writer.println("		}");
			writer.println("    }");
			writer.println("");
			writer.println("    /**");
			writer.println("     * Updates a "+CapitalCase.toCapitalCase(className)+" with");
			writer.println("     * @param "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Id                ID of "+CapitalCase.toCapitalCase(className));
			writer.println("     * @param "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Details           "+CapitalCase.toCapitalCase(className)+" details from EDIT FORM");
			writer.println("     */");
			writer.println("    @Override");
			writer.println("    public void update(Long "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Id, "+CapitalCase.toCapitalCase(className)+" "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Details)throws UpdateException{");
			if(fileFlag) {
				writer.println("    	MultipartFile file = "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Details.get"+CapitalCase.toCapitalCase(fileColumnName)+"datafile();");
				writer.println("		String fileName = StringUtils.cleanPath(file.getOriginalFilename());");
			}
			writer.println("		try {");
			
			writer.println("			"+CapitalCase.replaceUnderScore(className)+" database"+CapitalCase.replaceUnderScore(className).toLowerCase()+" = findById("+CapitalCase.replaceUnderScore(className).toLowerCase()+"Id);");
			
			for (int i = 2; i <= columnCount; i++) {
				String name = columnMetaData.getColumnName(i);
				if(!isForeignKey(con, className,name)) 
					writer.println("     		if("+CapitalCase.replaceUnderScore(className).toLowerCase()+"Details.get"+CapitalCase.toCapitalCase(name)+"()!=null)	   database"+CapitalCase.replaceUnderScore(className).toLowerCase()+".set"+CapitalCase.toCapitalCase(name)+"("+CapitalCase.replaceUnderScore(className).toLowerCase()+"Details.get"+CapitalCase.toCapitalCase(name)+"());");
			}
			
			if(passwordFlag)
				writer.println("				"+CapitalCase.toCapitalCase(className)+"Details.setPassword(passwordEncoder.encode(userdetailsDetails.getPassword()));");

			if(fileFlag) {
				writer.println("				database"+CapitalCase.replaceUnderScore(className).toLowerCase()+".set"+CapitalCase.toCapitalCase(fileColumnName)+"name(fileName);");
				writer.println("				database"+CapitalCase.replaceUnderScore(className).toLowerCase()+".set"+CapitalCase.toCapitalCase(fileColumnName)+"type(file.getContentType());");
				writer.println("				database"+CapitalCase.replaceUnderScore(className).toLowerCase()+".set"+CapitalCase.toCapitalCase(fileColumnName)+"data(file.getBytes());");
			}
			writer.println("");
			writer.println("        "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository.save(database"+CapitalCase.replaceUnderScore(className).toLowerCase()+");");
			writer.println("		} catch (Exception e) {");
			writer.println("			throw new UpdateException(e.getMessage());");
			writer.println("		}");
			writer.println("    }");
			writer.println("");
			writer.println("    /**");
			writer.println("     * delete a "+CapitalCase.toCapitalCase(className)+" from DB");
			writer.println("     * @param "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Id     ID of "+CapitalCase.toCapitalCase(className)+"");
			writer.println("     */");
			writer.println("    @Override");
			writer.println("    public void delete(Long "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Id)throws DeleteException{");
			writer.println("		try {");
			writer.println("       		 "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository.deleteById("+CapitalCase.replaceUnderScore(className).toLowerCase()+"Id);");
			writer.println("		} catch (Exception e) {");
			writer.println("			throw new DeleteException(e.getMessage());");
			writer.println("		}");
			writer.println("    }");
			writer.println("");
			writer.println("    /**");
			writer.println("     * creates and inserts a new "+CapitalCase.toCapitalCase(className)+" in DB");
			writer.println("     * @param "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Details      "+CapitalCase.toCapitalCase(className)+" details from NEW "+CapitalCase.toCapitalCase(className)+" FROM");
			writer.println("     * @return                 id of new "+CapitalCase.toCapitalCase(className)+"");
			writer.println("     */");
			writer.println("    @Override");
			writer.println("    public Long create("+CapitalCase.toCapitalCase(className)+" "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Details)throws CreateException{");
			if(fileFlag) {
				writer.println("			MultipartFile file = "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Details.get"+CapitalCase.toCapitalCase(fileColumnName)+"datafile();");
				writer.println("			String fileName = StringUtils.cleanPath(file.getOriginalFilename());");
			}
			writer.println("	 try {");
			if(fileFlag) {
				writer.println("			// Check if the file's name contains invalid characters");
				writer.println("			if (fileName.contains(\"..\")) {");
				writer.println("				throw new FileStorageException(\"Sorry! Filename contains invalid path sequence \" + fileName);");
				writer.println("			}");
			}
			for (int i = 1; i <=columnMetaData.getColumnCount(); i++) {
				String name = columnMetaData.getColumnName(i);
				if(isForeignKey(con, className,name)) {
					String fkTableName = getForeignKeyTableName(con, className,name);
					if(fkTableName.equalsIgnoreCase(loginTableName) && !basicTableList.contains(className)) {	
					writer.println("	Optional<"+CapitalCase.toCapitalCase(fkTableName)+"> "+fkTableName.toLowerCase()+" = customUserDetailsService.getLoggedUserDetails();");
					writer.println("	"+CapitalCase.replaceUnderScore(className).toLowerCase()+"Details.set"+CapitalCase.toCapitalCase(fkTableName)+"("+fkTableName.toLowerCase()+".get());");
					}
				}
			}
			if(loginTableName.equalsIgnoreCase(CapitalCase.toCapitalCase(className).toLowerCase()))
				writer.println("		  "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Details.setPassword(passwordEncoder.encode("+CapitalCase.replaceUnderScore(className).toLowerCase()+"Details.getPassword()));");
			String name = columnMetaData.getColumnName(1);
			if(isForeignKey(con, className,name)) 
				name = columnMetaData.getColumnName(2);
			if(fileFlag) {
				writer.println(" 	"+CapitalCase.replaceUnderScore(className).toLowerCase()+"Details.set"+CapitalCase.toCapitalCase(fileColumnName)+"name(fileName);");
				writer.println(" 	"+CapitalCase.replaceUnderScore(className).toLowerCase()+"Details.set"+CapitalCase.toCapitalCase(fileColumnName)+"type(file.getContentType());");
				writer.println(" 	"+CapitalCase.replaceUnderScore(className).toLowerCase()+"Details.set"+CapitalCase.toCapitalCase(fileColumnName)+"data(file.getBytes());");
			}
			writer.println("          "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository.save("+CapitalCase.replaceUnderScore(className).toLowerCase()+"Details);");
		writer.println("            	return "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Details.get"+CapitalCase.toCapitalCase(name)+"();");
		writer.println("		}");
		if(fileFlag) {
			writer.println("		catch (IOException e) {");
			writer.println("			throw new FileStorageException(\"Could not store file \" + fileName + \". Please try again!\", e);");
			writer.println("		}");
		}
		writer.println("		catch (Exception e) {");
		writer.println("			throw new CreateException(e.getMessage());");
		writer.println("		}");
			writer.println("    }\n");
			
			writer.println("	public List<"+CapitalCase.toCapitalCase(className)+"> getAll"+CapitalCase.toCapitalCase(className)+"(String title){");
			writer.println("		List<"+CapitalCase.toCapitalCase(className)+"> "+CapitalCase.replaceUnderScore(className).toLowerCase()+" = new ArrayList<>();");
			writer.println("		try {");
			writer.println("			if (title == null)");
			writer.println("					"+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository.findAll().forEach("+CapitalCase.replaceUnderScore(className).toLowerCase()+"::add);");
			writer.println("			return "+CapitalCase.replaceUnderScore(className).toLowerCase()+";");
	    	writer.println("		} catch (Exception e) {");
	    	writer.println("			return "+CapitalCase.replaceUnderScore(className).toLowerCase()+";");
			writer.println("		}");
			writer.println("	}");
			writer.println("\n");
			
			writer.println("	public "+CapitalCase.toCapitalCase(className)+" get"+CapitalCase.toCapitalCase(className)+"(long id){");
			writer.println("		Optional<"+CapitalCase.toCapitalCase(className)+"> "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Data = "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository.findById(id);");
			writer.println("		return "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Data.get();");
			writer.println("	}");
			writer.println("\n");
			
			writer.println("	public "+CapitalCase.toCapitalCase(className)+" add"+CapitalCase.toCapitalCase(className)+"("+CapitalCase.toCapitalCase(className)+" "+CapitalCase.replaceUnderScore(className).toLowerCase()+") throws Exception{");
			writer.println("		try {");
			writer.println("			return "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository.save("+CapitalCase.replaceUnderScore(className).toLowerCase()+");");
	    	writer.println("		} catch (Exception e) {");
			writer.println("			throw new Exception();");
			writer.println("		}");
			writer.println("	}");
			writer.println("\n");
			
			writer.println("	public "+CapitalCase.toCapitalCase(className)+" update"+CapitalCase.toCapitalCase(className)+"(long id,"+CapitalCase.toCapitalCase(className)+" "+CapitalCase.replaceUnderScore(className).toLowerCase()+") throws UpdateException{");
			writer.println("		try {");
			writer.println("			return "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository.save("+CapitalCase.replaceUnderScore(className).toLowerCase()+");");
	    	writer.println("		} catch (Exception e) {");
			writer.println("			throw new UpdateException(e.getMessage());");
			writer.println("		}");
			writer.println("	}");
			writer.println("\n");
			
			writer.println("	public boolean delete"+CapitalCase.toCapitalCase(className)+"(long id)throws DeleteException{");
			writer.println("		try {");
			writer.println("			"+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository.deleteById(id);");
			writer.println("			return true;");
			writer.println("		} catch (Exception e) {");
			writer.println("			throw new DeleteException(e.getMessage());");
			writer.println("		}");
			writer.println("	}");
			writer.println("\n");
			
			writer.println("	public boolean deleteAll"+CapitalCase.toCapitalCase(className)+"(){");
			writer.println("         try {");
			writer.println("		     "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository.deleteAll();");
			writer.println("    	     return true;");
			writer.println("          } catch (Exception e) {");
			writer.println("			 return false;");
			writer.println("          }");
			writer.println("    }");
			writer.println("\n");
			
			if (ReadProjectPropertiesFile.projectProps.getProperty("change-pass-feature").equals("1")
					&& ReadProjectPropertiesFile.projectProps.getProperty("database-login-table")
							.equalsIgnoreCase(CapitalCase.replaceUnderScore(className).toLowerCase())) {
				String emailColName = CapitalCase
						.toCapitalCase(ReadProjectPropertiesFile.projectProps.getProperty("email-col-name"));
				String loginTable = CapitalCase.toCapitalCase(className);
				String loginTable_lowercase = CapitalCase.toCapitalCase(className).toLowerCase();

				writer.println("	public " + loginTable + " findBy" + emailColName + "(String emailid) {");
				writer.println("		return "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository.findBy" + emailColName + "(emailid);");
				writer.println("	}");
				writer.println("");
				writer.println("	@Override");
				writer.println("	public void createPasswordResetTokenForUser(final " + loginTable + " "
						+ loginTable_lowercase + ", final String token) {");
				writer.println("		final PasswordResetToken myToken = new PasswordResetToken(token, "
						+ loginTable_lowercase + ");");
				writer.println("		passwordTokenRepository.save(myToken);");
				writer.println("	}");
				writer.println("");
				writer.println("	@Override");
				writer.println(
						" 	public Optional<" + loginTable + "> getUserByPasswordResetToken(final String token) {");
				writer.println(
						"   	 return Optional.ofNullable(passwordTokenRepository.findByToken(token).getUserdetails());");
				writer.println("	}");
				writer.println("");
				writer.println(" 	@Override");
				writer.println(
						" 	public void changeUserPassword(final " + loginTable + " user, final String password) {");
				writer.println("	     user.setPassword(passwordEncoder.encode(password));");
				writer.println("    	"+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository.save(user);");
				writer.println("	}");
				writer.println("");

				String loginColName = ReadProjectPropertiesFile.projectProps.getProperty("login-col-name");
				String passColName = ReadProjectPropertiesFile.projectProps.getProperty("password-col-name");
				writer.println("	@Override");
				writer.println("	public Optional<"+CapitalCase.toCapitalCase(loginTable)+"> findByUser"+CapitalCase.toCapitalCase(loginColName)+"and"+CapitalCase.toCapitalCase(passColName)+"(final String "+loginColName+",");
				writer.println("			final String "+passColName+"){");
				writer.println("		return "+CapitalCase.replaceUnderScore(className).toLowerCase()+"Repository.findBy"+CapitalCase.toCapitalCase(loginColName)+"and"+CapitalCase.toCapitalCase(passColName)+"("+loginColName+","+passColName+");");
				writer.println("	}");
				
			}
			
			writer.println("}");
			writer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private String createImportPrimaryForeignKeyReferencesColumnNamesAnnotations(Connection con, String tableName,
			PrintWriter writer, String packageImport)throws Exception{

		ResultSet rsf = con.getMetaData().getExportedKeys(con.getCatalog(), null, tableName);
		List<String> duplicateString = new ArrayList<String>();
		while (rsf.next()) {
			// System.out.println(" PK : "+rsf.getString("PKTABLE_NAME")+" FK :
			// "+rsf.getString("FKTABLE_NAME")+" SCHEM "+rsf.getString("FKTABLE_SCHEM"));
			String fkTableName = rsf.getString("FKTABLE_NAME");
			String replacefk = CapitalCase.replaceUnderScore(rsf.getString("FKTABLE_NAME"));
			String fkColumnName = rsf.getString("FKCOLUMN_NAME");
			String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
			if(fkSchemaName == null) fkSchemaName = "";
			if (!duplicateString.contains(fkTableName) && !tableList.contains(fkTableName.toLowerCase())) {// && fkSchemaName.equalsIgnoreCase(schemaName))
				//writer.println("	private List<" + CapitalCase.toBeanClass(fkTableName) + "> "
						//+ replacefk.toLowerCase() + ";");
				packageImport += "import "+basePackage+"."+pack+"."+beanPackage+"."+CapitalCase.toCapitalCase(fkTableName)+";\n";
				packageImport += "import "+basePackage+"."+pack+"."+repositoryPackage+"."+CapitalCase.toCapitalCase(fkTableName)+"Repository;\n";
			}
			duplicateString.add(fkTableName);
		}
		
		rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
		while (rsf.next()) {
			String pkTableName = rsf.getString("PKTABLE_NAME");
			packageImport += "import "+basePackage+"."+pack+"."+beanPackage+"."+CapitalCase.toCapitalCase(pkTableName)+";\n";
			packageImport += "import "+basePackage+"."+pack+"."+repositoryPackage+"."+CapitalCase.toCapitalCase(pkTableName)+"Repository;\n";
			
		}
			return packageImport;
		
	
	}

	private boolean isForeignKey(Connection con, String tableName, String name)throws Exception {
		boolean flag = false;
		ResultSet rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
		while (rsf.next()) {
			String fkColumnName = rsf.getString("FKCOLUMN_NAME");
			//System.out.println("Table : "+tableName+" FKColumn : "+fkColumnName+" MatchColName : "+name);
			if (fkColumnName.equalsIgnoreCase(name)) { 
				flag =  true;
				break;
			}
		}
		return flag;
	}
	
	private String getForeignKeyTableName(Connection con, String tableName,String name)throws Exception{
		ResultSet rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
		String fkClassName = "";
		while (rsf.next()) {
			String fkTableName = CapitalCase.replaceUnderScore(rsf.getString("PKTABLE_NAME"));
			String fkColumnName = rsf.getString("FKCOLUMN_NAME");
			if (fkColumnName.equalsIgnoreCase(name)) {
				fkClassName = fkTableName;
				break;
			}
		}
			return fkClassName;
	}
	
	private void createAutowirePrimaryForeignKeyReferences(Connection con, String tableName, PrintWriter writer)throws Exception {

		ResultSet rsf = con.getMetaData().getExportedKeys(con.getCatalog(), null, tableName);
		List<String> duplicateString = new ArrayList<String>();
		while (rsf.next()) {
			// System.out.println(" PK : "+rsf.getString("PKTABLE_NAME")+" FK :
			// "+rsf.getString("FKTABLE_NAME")+" SCHEM "+rsf.getString("FKTABLE_SCHEM"));
			String fkTableName = rsf.getString("FKTABLE_NAME");
			String fkSchemaName = rsf.getString("FKTABLE_SCHEM");
			if(fkSchemaName == null) fkSchemaName = "";
			if (!duplicateString.contains(fkTableName) && !tableList.contains(fkTableName.toLowerCase())) {// && fkSchemaName.equalsIgnoreCase(schemaName))
				writer.println("	@Autowired");
				writer.println("	"+CapitalCase.toCapitalCase(fkTableName)+"Repository "+CapitalCase.toCapitalCase(fkTableName).toLowerCase()+"Repository;\n");
			}
			duplicateString.add(fkTableName);
		}
		
			rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
			while (rsf.next()) {
				String pkTableName = rsf.getString("PKTABLE_NAME");
					writer.println("	@Autowired");
					writer.println("	"+CapitalCase.toCapitalCase(pkTableName)+"Repository "+CapitalCase.toCapitalCase(pkTableName).toLowerCase()+"Repository;\n");
				
			}
		
	
	}
}
