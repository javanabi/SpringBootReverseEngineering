package com.springboot.controller;

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

public class SBThymeleafControllerClasses extends AbstractDataAccessObject{
	
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
	String loginTableName = ReadProjectPropertiesFile.projectProps.getProperty("database-login-table");
	List<String> basicTableList= JPAPersistance.basicTableListFromPRoperteisFile();
	List<String> tableList = JPAPersistance.readTablesFromPRoperteisFile();
	List<String> ignoreSelectUserTables= JPAPersistance.tablesListTODO("ignore-select-by-UserTables");
	
	public SBThymeleafControllerClasses() {
	}
	
	public void createControlerImplMethods(String className,Connection con,String title,String pack,String projectName) throws SQLException{
		try {
			pstmt = con.prepareStatement("select * from " + className);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();
			
			String packageNameString = projectName+"\\"+basePackage+"\\";
			File packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + pack;
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + "\\"+controllerPackage;
			packageDir = new File(packageNameString);
			packageDir.mkdir();
			
			PrintWriter writer = new PrintWriter(packageNameString + "\\"
					+ CapitalCase.toCapitalCase(className) + "Controller.java");

			String packageImport = "package "+basePackage+"." + pack + "."+controllerPackage+";\n";
			packageImport += "import java.util.ArrayList; \n";
			packageImport += "import java.util.List; \n";
			packageImport += "import java.util.Optional; \n";	
			packageImport += "import java.util.stream.Collectors;\n";	
			packageImport += "import java.util.stream.IntStream;\n";	
			packageImport += "import javax.validation.Valid;\n";
			packageImport += "import org.springframework.beans.factory.annotation.Autowired;\n";
			packageImport += "import org.springframework.data.domain.Page;\n";
			packageImport += "import org.springframework.stereotype.Controller;\n";
			packageImport += "import org.springframework.ui.Model;\n";
			packageImport += "import org.springframework.validation.BindingResult;\n";
			packageImport += "import org.springframework.web.bind.annotation.GetMapping;\n";
			packageImport += "import org.springframework.web.bind.annotation.ModelAttribute;\n";
			packageImport += "import org.springframework.web.bind.annotation.PatchMapping;\n";
			packageImport += "import org.springframework.web.bind.annotation.PathVariable;\n";
			packageImport += "import org.springframework.web.bind.annotation.PostMapping;\n";
			packageImport += "import org.springframework.web.bind.annotation.RequestMapping;\n";
			packageImport += "import org.springframework.web.bind.annotation.RequestParam;\n";

			packageImport = createImportPrimaryForeignKeyReferencesColumnNamesAnnotations(con,className,writer,packageImport);
			packageImport += "import "+basePackage+"."+pack+"."+beanPackage+"."+CapitalCase.toCapitalCase(className)+";\n";
			packageImport += "import "+basePackage+"."+pack+"."+servicePackage+"i."+CapitalCase.toCapitalCase(className)+"ServiceI;\n";
			packageImport += "import "+basePackage+"."+pack+".exception.DataNotFoundException;\n";
			packageImport += "import "+basePackage+"."+pack+".exception.DeleteException;\n";
			packageImport += "import "+basePackage+"."+pack+".exception.UpdateException;\n";
			packageImport += "import "+basePackage+"."+pack+".exception.CreateException;\n";
			packageImport += "import "+basePackage+"."+pack+".security.CustomUserDetailsService;\n";
			packageImport += "import "+basePackage+"."+pack+".util.ConstructFilePathUtil;\n";
			packageImport += "import java.io.File;\n";
			packageImport += "/**\n";
			packageImport += " *@auther "+author+"\n";
			packageImport += " */\n";
			writer.println(packageImport);
			writer.println("@Controller");
			writer.println("@RequestMapping(\"/"+CapitalCase.toCapitalCase(className).toLowerCase()+"\")");
			writer.println("public class "
					+ CapitalCase.toCapitalCase(className) + "Controller{\n");
			writer.println("	@Autowired");
			writer.println("	"+CapitalCase.toCapitalCase(className)+"ServiceI "+CapitalCase.replaceUnderScore(className).toLowerCase()+"ServiceI;\n");
			
			if(loginTableName.equalsIgnoreCase(CapitalCase.toCapitalCase(className).toLowerCase())) {
				writer.println("	@Autowired");
				writer.println("	CustomUserDetailsService customUserDetailsService;\n");
			}
			
			boolean fileFlag = false;
			for (int i = 1; i <=columnMetaData.getColumnCount(); i++) {
				String colType = columnMetaData.getColumnTypeName(i);
				if(colType.equalsIgnoreCase("LONGBLOB")) {
					fileFlag = true;
				}
			}
			
			createAutowirePrimaryForeignKeyReferences(con,className,writer);
			writer.println("	/**");
			writer.println("	 * Displays a single "+CapitalCase.toCapitalCase(className));
			writer.println("	 * ");
			writer.println("	 * @param id");
			writer.println("	 *            "+CapitalCase.replaceUnderScore(className).toLowerCase()+" Id");
			writer.println("	 * @param model");
			writer.println("	 *            "+CapitalCase.replaceUnderScore(className).toLowerCase()+" object");
			writer.println("	 * @return template for displaying a single "+CapitalCase.replaceUnderScore(className).toLowerCase());
			writer.println("	 */");
			writer.println("	@GetMapping(\"/view"+CapitalCase.toCapitalCase(className)+"/{id}\")");
			writer.println("	public String view"+CapitalCase.toCapitalCase(className)+"(@PathVariable(\"id\") long id, Model model)throws DataNotFoundException{");
			
			if(loginTableName.equalsIgnoreCase(CapitalCase.toCapitalCase(className).toLowerCase())) {
				writer.println("		"+CapitalCase.toCapitalCase(className)+"  "+CapitalCase.replaceUnderScore(className).toLowerCase()+" = null;");	
				writer.println("		if(id != 0)");
				writer.println("			"+CapitalCase.replaceUnderScore(className).toLowerCase()+" = "+CapitalCase.replaceUnderScore(className).toLowerCase()+"ServiceI.findById(id);");
				writer.println("		else");
				writer.println("			"+CapitalCase.replaceUnderScore(className).toLowerCase()+" = customUserDetailsService.getLoggedUserDetails().get();");
				

				String fileColumnName = ReadProjectPropertiesFile.projectProps.getProperty("userprofileimage-col-name");
				
				if(fileFlag)
						{
							writer.println("				    		File folder = new File(\".//src//main//resources//static//download//\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".getId());");
							writer.println("				    		if(!folder.exists())");
							writer.println("				    			folder.mkdir();");
							writer.println("				    		String path = folder.getAbsolutePath()+\"/profile-\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".get"+CapitalCase.toCapitalCase(fileColumnName)+"name();");
							writer.println("				    		ConstructFilePathUtil.writeByte("+CapitalCase.replaceUnderScore(className).toLowerCase()+".get"+CapitalCase.toCapitalCase(fileColumnName)+"data(), path);");
							writer.println("				    		"+CapitalCase.replaceUnderScore(className).toLowerCase()+".setFileDownloadPath(\"/download/\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".getUserid()+\"/profile-\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".get"+CapitalCase.toCapitalCase(fileColumnName)+"name());");
						}
			
			}else {
				writer.println("		"+CapitalCase.toCapitalCase(className)+"  "+CapitalCase.replaceUnderScore(className).toLowerCase()+" = "+CapitalCase.replaceUnderScore(className).toLowerCase()+"ServiceI.findById(id);");
			}
			writer.println("			if("+CapitalCase.replaceUnderScore(className).toLowerCase()+" == null)");
			writer.println("			     throw new DataNotFoundException(\""+CapitalCase.toCapitalCase(className)+" not available with id :\"+id);");
			writer.println("			model.addAttribute(\""+CapitalCase.replaceUnderScore(className).toLowerCase()+"\","+CapitalCase.replaceUnderScore(className).toLowerCase()+");");
     		writer.println("			return \""+CapitalCase.toCapitalCase(className).toLowerCase()+"/view"+CapitalCase.toCapitalCase(className)+"ById\";");
			writer.println("	}");
			writer.println("\n");
			
			writer.println("    /**");
			writer.println("	 * New "+CapitalCase.toCapitalCase(className)+" Form");
			writer.println("	 * ");
			writer.println("	 * @param model");
			writer.println("	 *            "+CapitalCase.replaceUnderScore(className).toLowerCase()+" object");
			writer.println("	 * @return template form for new "+CapitalCase.replaceUnderScore(className).toLowerCase());
			writer.println("	 */");
			writer.println("	@GetMapping(\"/add"+CapitalCase.toCapitalCase(className)+"\")");
			writer.println("	public String new"+CapitalCase.toCapitalCase(className)+"Form(Model model) throws DataNotFoundException{");
			writer.println("		model.addAttribute(\""+CapitalCase.replaceUnderScore(className).toLowerCase()+"\",new "+CapitalCase.toCapitalCase(className)+"());");
			
			//createLoadPrimaryForeignKeyReferencesData(con,className,writer);
			createLoadForeignKeyReferencesData(con,className,writer);
			
			writer.println("	     return \""+CapitalCase.toCapitalCase(className).toLowerCase()+"/add"+CapitalCase.toCapitalCase(className)+"\";");
			writer.println("	}");
			writer.println("\n");
			writer.println("    /**");
			writer.println("	 * saves the details of "+CapitalCase.toCapitalCase(className)+"/create to DB");
			writer.println("	 * ");
			writer.println("	 * @param "+CapitalCase.replaceUnderScore(className).toLowerCase());
			writer.println("	 *                   field values");
			writer.println("	 * @return redirection to list view of all "+CapitalCase.replaceUnderScore(className).toLowerCase());
			writer.println("	 * @throws CreateException");
			writer.println("	 */");
			writer.println("	@PostMapping(\"/add"+CapitalCase.toCapitalCase(className)+"\")");
			writer.println("	public String addNew"+CapitalCase.toCapitalCase(className)+"(@ModelAttribute @Valid "+CapitalCase.toCapitalCase(className)+" "+CapitalCase.replaceUnderScore(className).toLowerCase()+", BindingResult bindingResult, Model model)throws CreateException,DataNotFoundException{");
			writer.println("		if (bindingResult.hasErrors()) {");
			//createLoadPrimaryForeignKeyReferencesData(con,className,writer);
			createLoadForeignKeyReferencesData(con,className,writer);
			writer.println("	     return \""+CapitalCase.toCapitalCase(className).toLowerCase()+"/add"+CapitalCase.toCapitalCase(className)+"\";");
	    	writer.println("		}");
			writer.println("		"+CapitalCase.replaceUnderScore(className).toLowerCase()+"ServiceI.create("+CapitalCase.replaceUnderScore(className).toLowerCase()+");");
			writer.println("	     return \"redirect:/"+CapitalCase.toCapitalCase(className).toLowerCase()+"/viewAll"+CapitalCase.toCapitalCase(className)+"\";");
			writer.println("	}");
			writer.println("\n");
			writer.println("	/**");
			writer.println("	 * Edit Form");
			writer.println("	 * ");
			writer.println("	 * @param id");
			writer.println("	 *            "+CapitalCase.replaceUnderScore(className).toLowerCase()+" Id");
			writer.println("	 * @param model");
			writer.println("	 *            "+CapitalCase.replaceUnderScore(className).toLowerCase()+" object");
			writer.println("	 * @return template for editing a "+CapitalCase.replaceUnderScore(className).toLowerCase()+"");
			writer.println("	 * @throws DataNotFoundException");
			writer.println("	 */");
			writer.println("	@GetMapping(\"/edit"+CapitalCase.toCapitalCase(className)+"/{id}\")");
			writer.println("	public String edit"+CapitalCase.toCapitalCase(className)+"Form(@PathVariable(\"id\") long id, Model model)throws DataNotFoundException {");
			
			if(loginTableName.equalsIgnoreCase(CapitalCase.toCapitalCase(className).toLowerCase())) {
				writer.println("		"+CapitalCase.toCapitalCase(className)+"  "+CapitalCase.replaceUnderScore(className).toLowerCase()+" = null;");	
				writer.println("		if(id != 0)");
				writer.println("			"+CapitalCase.replaceUnderScore(className).toLowerCase()+" = "+CapitalCase.replaceUnderScore(className).toLowerCase()+"ServiceI.findById(id);");
				writer.println("		else");
				writer.println("			"+CapitalCase.replaceUnderScore(className).toLowerCase()+" = customUserDetailsService.getLoggedUserDetails().get();");
			
				String fileColumnName = ReadProjectPropertiesFile.projectProps.getProperty("userprofileimage-col-name");
				if(fileFlag)
						{
							writer.println("				    		File folder = new File(\".//src//main//resources//static//download//\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".getId());");
							writer.println("				    		if(!folder.exists())");
							writer.println("				    			folder.mkdir();");
							writer.println("				    		String path = folder.getAbsolutePath()+\"/profile-\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".get"+CapitalCase.toCapitalCase(fileColumnName)+"name();");
							writer.println("				    		ConstructFilePathUtil.writeByte("+CapitalCase.replaceUnderScore(className).toLowerCase()+".get"+CapitalCase.toCapitalCase(fileColumnName)+"data(), path);");
							writer.println("				    		"+CapitalCase.replaceUnderScore(className).toLowerCase()+".setFileDownloadPath(\"/download/\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".getUserid()+\"/profile-\"+"+CapitalCase.replaceUnderScore(className).toLowerCase()+".get"+CapitalCase.toCapitalCase(fileColumnName)+"name());");
						}
			}else {
				writer.println("		"+CapitalCase.toCapitalCase(className)+"  "+CapitalCase.replaceUnderScore(className).toLowerCase()+" = "+CapitalCase.replaceUnderScore(className).toLowerCase()+"ServiceI.findById(id);");
			}
				//createLoadPrimaryForeignKeyReferencesData(con,className,writer);
			createLoadForeignKeyReferencesData(con,className,writer);
			writer.println("		model.addAttribute(\""+CapitalCase.replaceUnderScore(className).toLowerCase()+"\", "+CapitalCase.replaceUnderScore(className).toLowerCase()+");");
			writer.println("		return \""+CapitalCase.toCapitalCase(className).toLowerCase()+"/edit"+CapitalCase.toCapitalCase(className)+"\";");
			writer.println("	}");
			writer.println("	/**");
			writer.println("	 * shows all existing "+CapitalCase.replaceUnderScore(className).toLowerCase()+" in  database as list");
			writer.println("	 * ");
			writer.println("	 * @param model");
			writer.println("	 *            "+CapitalCase.replaceUnderScore(className).toLowerCase()+" objects");
			writer.println("	 * @return template for list view");
			writer.println("	 * @throws DataNotFoundException");
			writer.println("	 */");
			writer.println("	");
			writer.println("	/*@GetMapping({\"/viewAll"+CapitalCase.toCapitalCase(className)+"\", \"/\" })");
			writer.println("	public String showAll"+CapitalCase.toCapitalCase(className)+"(Model model)throws DataNotFoundException {");
			writer.println("		model.addAttribute(\""+CapitalCase.replaceUnderScore(className).toLowerCase()+"\", "+CapitalCase.replaceUnderScore(className).toLowerCase()+"ServiceI.getAll());");
			createLoadPrimaryForeignKeyReferencesData(con,className,writer);
			writer.println("		return \"home\";");
			writer.println("	}*/");
			writer.println("");
			writer.println("	/**");
			writer.println("	 * shows all existing "+CapitalCase.replaceUnderScore(className).toLowerCase()+" in database as list");
			writer.println("	 * ");
			writer.println("	 * @param model");
			writer.println("	 *            "+CapitalCase.replaceUnderScore(className).toLowerCase()+" objects");
			writer.println("	 * @return template for list view");
			writer.println("	 * @throws DataNotFoundException");
			writer.println("	 */");
			writer.println("	@GetMapping(\"/viewAll"+CapitalCase.toCapitalCase(className)+"\")");
			writer.println("	public String showAll"+CapitalCase.toCapitalCase(className)+"(Model model, @RequestParam(defaultValue = \"0\") Integer pageNo,@RequestParam(defaultValue = \"5\") Integer pageSize, @RequestParam(defaultValue = \"id\") String sortBy)throws DataNotFoundException {");
			writer.println("		Page<"+CapitalCase.toCapitalCase(className)+"> pagedResult = "+CapitalCase.replaceUnderScore(className).toLowerCase()+"ServiceI.getAll(pageNo != 0 ? pageNo - 1 : 0, pageSize, sortBy);");
			writer.println("		List<"+CapitalCase.toCapitalCase(className)+"> "+CapitalCase.replaceUnderScore(className).toLowerCase()+" = new ArrayList<"+CapitalCase.toCapitalCase(className)+">();");
			writer.println("		"+CapitalCase.replaceUnderScore(className).toLowerCase()+".addAll(pagedResult.getContent());");
			writer.println("		model.addAttribute(\""+CapitalCase.replaceUnderScore(className).toLowerCase()+"\", "+CapitalCase.replaceUnderScore(className).toLowerCase()+");");
			writer.println("		model.addAttribute(\"pages\", pagedResult);");
			writer.println("		int totalPages = pagedResult.getTotalPages();");
			writer.println("		if (totalPages > 0) {");
			writer.println("			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());");
			writer.println("			model.addAttribute(\"pageNumbers\", pageNumbers);");
			writer.println("		}");
			createLoadPrimaryForeignKeyReferencesData(con,className,writer);
			writer.println("		return \""+CapitalCase.toCapitalCase(className).toLowerCase()+"/viewAll"+CapitalCase.toCapitalCase(className)+"\";");
			writer.println("	}");
			writer.println("");
			writer.println("	/**");
			writer.println("	 * shows all login user existing "+CapitalCase.replaceUnderScore(className).toLowerCase()+" in database as list");
			writer.println("	 * ");
			writer.println("	 * @param model");
			writer.println("	 *            "+CapitalCase.replaceUnderScore(className).toLowerCase()+" objects");
			writer.println("	 * @return template for list view");
			writer.println("	 * @throws DataNotFoundException");
			writer.println("	 */");
			if(!ignoreSelectUserTables.contains(CapitalCase.replaceUnderScore(className).toLowerCase())) {
			writer.println("	@GetMapping(\"/viewAll"+CapitalCase.toCapitalCase(className)+"ByUser/{username}\")");
			writer.println("	public String showAll"+CapitalCase.toCapitalCase(className)+"(@PathVariable(\"username\") String username, Model model, @RequestParam(defaultValue = \"0\") Integer pageNo,@RequestParam(defaultValue = \"5\") Integer pageSize, @RequestParam(defaultValue = \"id\") String sortBy)throws DataNotFoundException {");
			writer.println("		Page<"+CapitalCase.toCapitalCase(className)+"> pagedResult = "+CapitalCase.replaceUnderScore(className).toLowerCase()+"ServiceI.getAll(username,pageNo != 0 ? pageNo - 1 : 0, pageSize, sortBy);");
			writer.println("		List<"+CapitalCase.toCapitalCase(className)+"> "+CapitalCase.replaceUnderScore(className).toLowerCase()+" = new ArrayList<"+CapitalCase.toCapitalCase(className)+">();");
			writer.println("		"+CapitalCase.replaceUnderScore(className).toLowerCase()+".addAll(pagedResult.getContent());");
			writer.println("		model.addAttribute(\""+CapitalCase.replaceUnderScore(className).toLowerCase()+"\", "+CapitalCase.replaceUnderScore(className).toLowerCase()+");");
			writer.println("		model.addAttribute(\"pages\", pagedResult);");
			writer.println("		int totalPages = pagedResult.getTotalPages();");
			writer.println("		if (totalPages > 0) {");
			writer.println("			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());");
			writer.println("			model.addAttribute(\"pageNumbers\", pageNumbers);");
			writer.println("		}");
			createLoadPrimaryForeignKeyReferencesData(con,className,writer);
			writer.println("		return \""+CapitalCase.toCapitalCase(className).toLowerCase()+"/viewAll"+CapitalCase.toCapitalCase(className)+"\";");
			writer.println("	}");
			}
			writer.println("");
			writer.println("	/**");
			writer.println("	 * Saves "+CapitalCase.replaceUnderScore(className).toLowerCase()+" details from edit template for an existing "+CapitalCase.replaceUnderScore(className).toLowerCase()+" in database ");
			writer.println("	 * ");
			writer.println("	 * @param id");
			writer.println("	 *            "+CapitalCase.replaceUnderScore(className).toLowerCase()+" Id");
			writer.println("	 * @param "+CapitalCase.replaceUnderScore(className).toLowerCase()+"");
			writer.println("	 *            "+CapitalCase.replaceUnderScore(className).toLowerCase()+" details (of field values)");
			writer.println("	 * @return redirection to list view of all "+CapitalCase.replaceUnderScore(className).toLowerCase()+"");
			writer.println("	 */");
			writer.println("	/*@PutMapping(\"/update"+CapitalCase.toCapitalCase(className)+"/{id}\")");
			writer.println("	public String update"+CapitalCase.toCapitalCase(className)+"(@PathVariable(\"id\") long id, "+CapitalCase.toCapitalCase(className)+" "+CapitalCase.replaceUnderScore(className).toLowerCase()+") {");
			writer.println("		"+CapitalCase.replaceUnderScore(className).toLowerCase()+"ServiceI.update(id, "+CapitalCase.replaceUnderScore(className).toLowerCase()+");");
			writer.println("		return redirect://"+CapitalCase.replaceUnderScore(className).toLowerCase()+"/viewAll"+CapitalCase.toCapitalCase(className)+"\";");
			writer.println("	}*/");
			writer.println("	");
			writer.println("	@PostMapping(\"/update"+CapitalCase.toCapitalCase(className)+"/{id}\")");
			writer.println("	public String update"+CapitalCase.toCapitalCase(className)+"(@PathVariable(\"id\") long id, "+CapitalCase.toCapitalCase(className)+" "+CapitalCase.replaceUnderScore(className).toLowerCase()+")throws UpdateException, DataNotFoundException {");
			writer.println("		"+CapitalCase.replaceUnderScore(className).toLowerCase()+"ServiceI.update(id, "+CapitalCase.replaceUnderScore(className).toLowerCase()+");");
			writer.println("		return \"redirect:/"+CapitalCase.toCapitalCase(className).toLowerCase()+"/viewAll"+CapitalCase.toCapitalCase(className)+"\";");
			writer.println("	}");
			writer.println("");
			writer.println("	/**");
			writer.println("	 * deletes existing "+CapitalCase.replaceUnderScore(className).toLowerCase()+" from database");
			writer.println("	 * ");
			writer.println("	 * @param id");
			writer.println("	 *            "+CapitalCase.replaceUnderScore(className).toLowerCase()+" Id");
			writer.println("	 * @return redirection to list view of all "+CapitalCase.replaceUnderScore(className).toLowerCase()+"");
			writer.println("	 */");
			writer.println("	//@DeleteMapping(\"/delete"+CapitalCase.toCapitalCase(className)+"/{id}\") works only for form submit hiperlink submit will not work");
			writer.println("	@GetMapping(\"/delete"+CapitalCase.toCapitalCase(className)+"/{id}\")");
			writer.println("	public String delete"+CapitalCase.toCapitalCase(className)+"(@PathVariable(\"id\") long id)throws DeleteException {");
			writer.println("		"+CapitalCase.replaceUnderScore(className).toLowerCase()+"ServiceI.delete(id);");
			writer.println("		return \"redirect:/"+CapitalCase.toCapitalCase(className).toLowerCase()+"/viewAll"+CapitalCase.toCapitalCase(className)+"\";");
			writer.println("	}");
			writer.println("}");

			writer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createLoadPrimaryForeignKeyReferencesData(Connection con, String tableName, PrintWriter writer) throws Exception {

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
			if (!duplicateString.contains(fkTableName)   && !tableList.contains(fkTableName.toLowerCase())) {// && fkSchemaName.equalsIgnoreCase(schemaName))
				writer.println("	model.addAttribute(\""+CapitalCase.toCapitalCase(fkTableName).toLowerCase()+"\", "+CapitalCase.toCapitalCase(fkTableName).toLowerCase()+"ServiceI.getAll());");
			}
			duplicateString.add(fkTableName);
		}
		
/*			rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
			List<String> pkString = new ArrayList<String>();
			while (rsf.next()) {
				String pkTableName = rsf.getString("PKTABLE_NAME");
				String replacepk = CapitalCase.replaceUnderScore(rsf.getString("PKTABLE_NAME"));
				String pkSchemaName = rsf.getString("FKTABLE_SCHEM");
				 String fkColumnName = rsf.getString("FKCOLUMN_NAME");
				//if(pkSchemaName==null) pkSchemaName="";
				//if (!pkString.contains(pkTableName)) && pkSchemaName.equalsIgnoreCase(schemaName))
					writer.println("       @ManyToOne(fetch=FetchType.LAZY)");
					writer.println("       @JoinColumn(name=\""+fkColumnName.toLowerCase()+"\")");
					writer.println("	   "+CapitalCase.toCapitalCase(pkTableName) + " "
							+ replacepk.toLowerCase() + ";\n");
				pkString.add(pkTableName);
			}*/
		
	}
	
	private void createLoadForeignKeyReferencesData(Connection con, String tableName, PrintWriter writer)
			throws Exception {

		ResultSet rsf = con.getMetaData().getExportedKeys(con.getCatalog(), null, tableName);
		rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
		while (rsf.next()) {
			String pkTableName = CapitalCase.replaceUnderScore(rsf.getString("PKTABLE_NAME"));
			if (!pkTableName.equalsIgnoreCase(loginTableName) || basicTableList.contains(tableName)){
				writer.println("	model.addAttribute(\"" + CapitalCase.toCapitalCase(pkTableName).toLowerCase()
						+ "List\", " + CapitalCase.toCapitalCase(pkTableName).toLowerCase() + "ServiceI.getAll());");
			}
		}
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
				writer.println("	"+CapitalCase.toCapitalCase(fkTableName)+"ServiceI "+CapitalCase.toCapitalCase(fkTableName).toLowerCase()+"ServiceI;\n");
			}
			duplicateString.add(fkTableName);
		}
		
			rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
			while (rsf.next()) {
				String pkTableName = rsf.getString("PKTABLE_NAME");
					writer.println("	@Autowired");
					writer.println("	"+CapitalCase.toCapitalCase(pkTableName)+"ServiceI "+CapitalCase.toCapitalCase(pkTableName).toLowerCase()+"ServiceI;\n");
				
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
				packageImport += "import "+basePackage+"."+pack+"."+servicePackage+"i."+CapitalCase.toCapitalCase(fkTableName)+"ServiceI;\n";
			}
			duplicateString.add(fkTableName);
		}
		
		rsf = con.getMetaData().getImportedKeys(con.getCatalog(), null, tableName);
		while (rsf.next()) {
			String pkTableName = rsf.getString("PKTABLE_NAME");
			packageImport += "import "+basePackage+"."+pack+"."+beanPackage+"."+CapitalCase.toCapitalCase(pkTableName)+";\n";
			packageImport += "import "+basePackage+"."+pack+"."+servicePackage+"i."+CapitalCase.toCapitalCase(pkTableName)+"ServiceI;\n";
			
		}
			return packageImport;
		
	
	}
	
}
