package com.springboot.service;

import java.io.File;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.jdbc.dao.AbstractDataAccessObject;
import com.jdbc.main.CapitalCase;
import com.springboot.persistance.jpa.JPAPersistance;
import com.springboot.project.properties.ReadProjectPropertiesFile;

public class SBServiceInterface extends AbstractDataAccessObject{
	
	PreparedStatement pstmt;
	String pack;
	ResultSet resultSet, rs, rsf, tableTypes, contraintsRecords;
	String schemaName = getProperties().getProperty("duser");
	
	String author = ReadProjectPropertiesFile.projectProps.getProperty("project-author");
	String basePackage = ReadProjectPropertiesFile.projectProps.getProperty("basePackage");
	String beanPackage = ReadProjectPropertiesFile.projectProps.getProperty("beanPackage");
	String repositoryPackage = ReadProjectPropertiesFile.projectProps.getProperty("repositoryPackage");
	String servicePackage = ReadProjectPropertiesFile.projectProps.getProperty("servicePackage");
	List<String> ignoreSelectUserTables= JPAPersistance.tablesListTODO("ignore-select-by-UserTables");
	
	public SBServiceInterface() {
	}
	
	public void createServiceInterface(String title,String pack,String projectName) throws SQLException{
		try {
			String packageNameString = projectName+"\\"+basePackage+"\\";
			File packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + pack;
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + "\\"+servicePackage+"i";
			packageDir = new File(packageNameString);
			packageDir.mkdir();
			
			PrintWriter writer = new PrintWriter(packageNameString + "\\CrudService.java");

			writer.println("package "+basePackage+"."+pack+"."+servicePackage+"i;\n");
			writer.println("import org.springframework.data.domain.Page;\n");
					writer.println("import "+basePackage+"."+pack+".exception.DataNotFoundException;\n");
			writer.println("import java.util.List;\n");
			writer.println("/**");
			writer.println(" * @author "+author);
			writer.println(" */");
			writer.println("public interface CrudService<T, ID> {");
			writer.println("    /**");
			writer.println("     * GET all Objects from DB");
			writer.println("     * @return all Objects from Database");
			writer.println("     */");
			writer.println("    List<T> getAll()throws Exception;");
			writer.println("");
			writer.println("    /**");
			writer.println("     * GET all Objects from DB");
			writer.println("     * @return all Objects from Database");
			writer.println("     */");
			writer.println("    public Page<T> getAll(Integer pageNo, Integer pageSize, String sortBy)throws DataNotFoundException;");
			writer.println("");
			//writer.println("");
			//writer.println("    public Page<T> getAll(String username,Integer pageNo, Integer pageSize, String sortBy)throws DataNotFoundException;");
			//writer.println("");
			writer.println("    /**");
			writer.println("     * finds an Object by its ID");
			writer.println("     * @param id    Database ID of Object");
			writer.println("     * @return      Object");
			writer.println("     */");
			writer.println("    T findById(ID id)throws Exception;");
			writer.println("");
			writer.println("    /**");
			writer.println("     * creates new Object and saves it in Database");
			writer.println("     * @param tDetails   field values");
			writer.println("     * @return           new Object");
			writer.println("     */");
			writer.println("    Long create(T tDetails)throws Exception;");
			writer.println("");
			writer.println("    /**");
			writer.println("     * updates Object from Database with field values in taskDetails");
			writer.println("     * @param id        Database ID of Object");
			writer.println("     * @param tDetails  field values");
			writer.println("     * @return          updated Object");
			writer.println("     */");
			writer.println("    void update(ID id, T tDetails)throws Exception;");
			writer.println("");
			writer.println("    /**");
			writer.println("     * deletes Object from Database");
			writer.println("     * @param id    Database ID of Object");
			writer.println("     */");
			writer.println("    void delete(ID id)throws Exception;");
			writer.println("}");
			writer.println("");
			writer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createAllServiceInterfaces(String className,String title,String pack,String projectName) throws SQLException{
		try {
			String packageNameString = projectName+"\\"+basePackage+"\\";
			File packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + pack;
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + "\\"+servicePackage+"i";
			packageDir = new File(packageNameString);
			packageDir.mkdir();
			
			PrintWriter writer = new PrintWriter(packageNameString + "\\"+CapitalCase.toCapitalCase(className)+"ServiceI.java");

			writer.println("package "+basePackage+"."+pack+"."+servicePackage+"i;\n");
			writer.println("import java.util.List;\n");
			writer.println("import java.util.Optional;\n");
			writer.println("import org.springframework.data.domain.Page;\n");
			writer.println("import "+basePackage+"."+pack+"."+beanPackage+"."+CapitalCase.toCapitalCase(className)+";\n");
			String packageImport = "import "+basePackage+"."+pack+".exception.DataNotFoundException;\n";
			packageImport += "import "+basePackage+"."+pack+".exception.DeleteException;\n";
			packageImport += "import "+basePackage+"."+pack+".exception.UpdateException;\n";
			packageImport += "import "+basePackage+"."+pack+".exception.CreateException;\n";
			writer.println(packageImport);
			writer.println("");
			writer.println("/**");
			writer.println(" * @author "+author);
			writer.println(" */");
			writer.println("public interface "+CapitalCase.toCapitalCase(className)+"ServiceI extends CrudService<"+CapitalCase.toCapitalCase(className)+", Long> {");
			writer.println("    /**");
			writer.println("     * GET all Objects from Database");
			writer.println("     * @return all Objects from Database");
			writer.println("     */");
			writer.println("    List<"+CapitalCase.toCapitalCase(className)+"> getAll()throws DataNotFoundException;");
			writer.println("");
			if(!ignoreSelectUserTables.contains(CapitalCase.replaceUnderScore(className).toLowerCase())) {
			writer.println("    /**");
			writer.println("     * GET all User Objects from Database");
			writer.println("     * @return all Objects from Database");
			writer.println("     */");
			writer.println("  public Page<"+CapitalCase.toCapitalCase(className)+"> getAll(String username,Integer pageNo, Integer pageSize, String sortBy)throws DataNotFoundException;");
			}
			writer.println("");
			writer.println("    /**");
			writer.println("     * finds an Object by its id");
			writer.println("     * @param id    Database id of Object");
			writer.println("     * @return      Object");
			writer.println("     */");
			writer.println("    "+CapitalCase.toCapitalCase(className)+" findById(Long id)throws DataNotFoundException;");
			writer.println("");
			writer.println("    /**");
			writer.println("     * creates new Object and saves it in Database");
			writer.println("     * @param tDetails   field values");
			writer.println("     * @return           new Object");
			writer.println("     */");
			writer.println("    Long create("+CapitalCase.toCapitalCase(className)+" "+className.toLowerCase()+"Details)throws CreateException;");
			writer.println("");
			writer.println("    /**");
			writer.println("     * updates Object from Database with field values in taskDetails");
			writer.println("     * @param id        Database id of Object");
			writer.println("     * @param tDetails  field values");
			writer.println("     * @return          updated Object");
			writer.println("     */");
			writer.println("    void update(Long id, "+CapitalCase.toCapitalCase(className)+" "+className.toLowerCase()+"Details)throws UpdateException, DataNotFoundException;");
			writer.println("");
			writer.println("    /**");
			writer.println("     * deletes Object from Database");
			writer.println("     * @param id    Database id of Object");
			writer.println("     */");
			writer.println("    void delete(Long id)throws DeleteException;");
			
			
			if(ReadProjectPropertiesFile.projectProps.getProperty("change-pass-feature").equals("1") && ReadProjectPropertiesFile.projectProps.getProperty("database-login-table").equalsIgnoreCase(CapitalCase.replaceUnderScore(className).toLowerCase())) {
				String emailColName = CapitalCase.toCapitalCase(ReadProjectPropertiesFile.projectProps.getProperty("email-col-name"));
				String loginTable = CapitalCase.toCapitalCase(className);
				String loginTable_lowercase = CapitalCase.toCapitalCase(className).toLowerCase();
			writer.println("	"+loginTable+" findBy"+emailColName+"(String emailId);");
			writer.println("");
			writer.println("	void createPasswordResetTokenForUser("+loginTable+" "+loginTable_lowercase+", String token);");
			writer.println("");
			writer.println("	public Optional<"+loginTable+"> getUserByPasswordResetToken(final String token);");
			writer.println("");
			writer.println("	public void changeUserPassword(final "+loginTable+" user, final String password);");

			String loginColName = ReadProjectPropertiesFile.projectProps.getProperty("login-col-name");
			String passColName = ReadProjectPropertiesFile.projectProps.getProperty("password-col-name");
			writer.println("	public Optional<"+CapitalCase.toCapitalCase(loginTable)+"> findByUser"+CapitalCase.toCapitalCase(loginColName)+"and"+CapitalCase.toCapitalCase(passColName)+"(final String "+loginColName+",");
			writer.println("			final String "+passColName+");");			}
			
			writer.println("}");
			writer.println("");
			writer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
