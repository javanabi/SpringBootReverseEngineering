package com.springboot.controller;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.jdbc.dao.AbstractDataAccessObject;
import com.jdbc.main.CapitalCase;
import com.springboot.project.properties.ReadProjectPropertiesFile;

public class SBControllerClasses extends AbstractDataAccessObject{
	
	PreparedStatement pstmt;
	String pack;
	ResultSet resultSet, rs, rsf, tableTypes, contraintsRecords;
	String schemaName = getProperties().getProperty("duser");
	
	public SBControllerClasses() {
	}
	
	public void createControlerImplMethods(String className,Connection con,String title,String pack,String projectName) throws SQLException{
		try {
			pstmt = con.prepareStatement("select * from " + className);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();
			
			String controllerPackage = ReadProjectPropertiesFile.projectProps.getProperty("controller-Package");
			String basePackage = ReadProjectPropertiesFile.projectProps.getProperty("basePackage");
			String beanPackage = ReadProjectPropertiesFile.projectProps.getProperty("beanPackage");
			String repositoryPackage = ReadProjectPropertiesFile.projectProps.getProperty("repositoryPackage");
			
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
			packageImport += "import java.util.Optional; \n\n";		 

			packageImport += "import org.springframework.beans.factory.annotation.Autowired;\n";
			packageImport += "import org.springframework.http.HttpStatus;\n";
			packageImport += "import org.springframework.http.ResponseEntity;\n";
			packageImport += "import org.springframework.web.bind.annotation.DeleteMapping;\n";
			packageImport += "import org.springframework.web.bind.annotation.GetMapping;\n";
			packageImport += "import org.springframework.web.bind.annotation.PathVariable;\n";
			packageImport += "import org.springframework.web.bind.annotation.PostMapping;\n";
			packageImport += "import org.springframework.web.bind.annotation.PutMapping;\n";
			packageImport += "import org.springframework.web.bind.annotation.RequestBody;\n";
			packageImport += "import org.springframework.web.bind.annotation.RequestMapping;\n";
			packageImport += "import org.springframework.web.bind.annotation.RequestParam;\n";
			packageImport += "import org.springframework.web.bind.annotation.RestController;\n";

			packageImport += "import "+basePackage+"."+pack+"."+beanPackage+"."+CapitalCase.toCapitalCase(className)+";\n";
			packageImport += "import "+basePackage+"."+pack+"."+repositoryPackage+"."+CapitalCase.toCapitalCase(className)+"Repository;\n";
			
			
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
			writer.println("@RestController");
			writer.println("@RequestMapping(\"/api\")");
			writer.println("public class "
					+ CapitalCase.toCapitalCase(className) + "Controller{\n\n\n");
			writer.println("     @Autowired");
			writer.println("     "+CapitalCase.toCapitalCase(className)+"Repository "+className.toLowerCase()+"Repository;\n\n");
			
			writer.println("	@GetMapping(\"/"+className.toLowerCase()+"\")");
			writer.println("	public ResponseEntity<List<"+CapitalCase.toCapitalCase(className)+">> getAll"+CapitalCase.toCapitalCase(className)+"(@RequestParam(required = false) String title){");
			writer.println("		try {");
			writer.println("		List<"+CapitalCase.toCapitalCase(className)+"> "+className.toLowerCase()+" = new ArrayList<>();");
			writer.println("			if (title == null)");
			writer.println("					"+className.toLowerCase()+"Repository.findAll().forEach("+className.toLowerCase()+"::add);");
			//writer.println("			else");
			//writer.println("					"+className.toLowerCase()+"Repository.findByTitleContaining(title).forEach("+className.toLowerCase()+"::add);");
			writer.println("\n");
			writer.println("			if ("+className.toLowerCase()+".isEmpty())");
			writer.println("				return new ResponseEntity<>(HttpStatus.NO_CONTENT);");
			writer.println("\n");
     		writer.println("			return new ResponseEntity<>("+className.toLowerCase()+", HttpStatus.OK);");
	    	writer.println("		} catch (Exception e) {");
			writer.println("			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);");
			writer.println("		}");
			writer.println("	}");
			writer.println("\n");
			writer.println("\n");
			
			writer.println("	@GetMapping(\"/"+className.toLowerCase()+"/{id}\")");
			writer.println("	public ResponseEntity<"+CapitalCase.toCapitalCase(className)+"> get"+CapitalCase.toCapitalCase(className)+"ById(@PathVariable(\"id\")  long id){");
			writer.println("		"+className.toLowerCase()+"Repository.deleteById(id);");
			writer.println("			return new ResponseEntity<>(HttpStatus.NO_CONTENT);");
			writer.println("	}");
			writer.println("\n");
			writer.println("\n");
			
			writer.println("	@PostMapping(\"/"+className.toLowerCase()+"\")");
			writer.println("	public ResponseEntity<"+CapitalCase.toCapitalCase(className)+"> add"+CapitalCase.toCapitalCase(className)+"(@RequestBody "+CapitalCase.toCapitalCase(className)+" "+className.toLowerCase()+"){");
			writer.println("		try {");
			writer.println("			"+CapitalCase.toCapitalCase(className)+" "+className.toLowerCase()+"Res = "+className.toLowerCase()+"Repository.save("+className.toLowerCase()+");");
     		writer.println("				return new ResponseEntity<>("+className.toLowerCase()+"Res, HttpStatus.OK);");
	    	writer.println("			} catch (Exception e) {");
			writer.println("				return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);");
			writer.println("			}");
			writer.println("	}");
			writer.println("\n");
			writer.println("\n");
			
			writer.println("	@PutMapping(\"/"+className.toLowerCase()+"/{id}\")");
			writer.println("	public ResponseEntity<"+CapitalCase.toCapitalCase(className)+"> update"+CapitalCase.toCapitalCase(className)+"(@PathVariable(\"id\") long id,@RequestBody "+CapitalCase.toCapitalCase(className)+" "+className.toLowerCase()+"){");
			writer.println("		try {");
			writer.println("		"+CapitalCase.toCapitalCase(className)+" "+className.toLowerCase()+"Res = "+className.toLowerCase()+"Repository.save("+className.toLowerCase()+");");
     		writer.println("				return new ResponseEntity<>("+className.toLowerCase()+"Res, HttpStatus.OK);");
	    	writer.println("			} catch (Exception e) {");
			writer.println("				return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);");
			writer.println("			}");
			writer.println("	}");
			writer.println("\n");
			writer.println("\n");
			
			writer.println("	@DeleteMapping(\"/"+className.toLowerCase()+"/{id}\")");
			writer.println("	public ResponseEntity<"+CapitalCase.toCapitalCase(className)+"> delete"+CapitalCase.toCapitalCase(className)+"ById(@PathVariable(\"id\")  long id){");
			writer.println("		Optional<"+CapitalCase.toCapitalCase(className)+"> "+className.toLowerCase()+" = "+className.toLowerCase()+"Repository.findById(id);");
			writer.println("			if ("+className.toLowerCase()+".isPresent()) {");
			writer.println("				return new ResponseEntity<>("+className.toLowerCase()+".get(), HttpStatus.OK);");
			writer.println("			}else{");
			writer.println("				return new ResponseEntity<>(HttpStatus.NOT_FOUND); ");
			writer.println("			}");
			writer.println("}");
			writer.println("\n");
			writer.println("\n");
			
			writer.println("	@DeleteMapping(\"/"+className.toLowerCase()+"\")");
			writer.println("	public ResponseEntity<"+CapitalCase.toCapitalCase(className)+"> deleteAll"+CapitalCase.toCapitalCase(className)+"(){");
			writer.println("    try {");
			writer.println("		"+className.toLowerCase()+"Repository.deleteAll();");
			writer.println("			return new ResponseEntity<>(HttpStatus.NO_CONTENT);");
			writer.println("     } catch (Exception e) {");
			writer.println("		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);");
			writer.println("    }");
			writer.println(" }");
			writer.println("\n");
			writer.println("\n");
			
			writer.println("}");
			writer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
