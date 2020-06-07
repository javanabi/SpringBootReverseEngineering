package com.springboot.supportfiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;

import com.springboot.project.properties.ReadProjectPropertiesFile;

public class SpringBootSupportImpl {

	String pack;
	
	String basePackage = ReadProjectPropertiesFile.projectProps.getProperty("basePackage");
	String cssTitle = ReadProjectPropertiesFile.projectProps.getProperty("css-title");
	String uitemplateNumber = ReadProjectPropertiesFile.projectProps.getProperty("uitemplate-number");
	
	public SpringBootSupportImpl(String pack) {
		this.pack = pack;
	}

	public SpringBootSupportImpl() {
	}

	
	public void createSupportImplClasses(String resourcePackage,String srcPackage,String cssfileLocationPath) throws SQLException {
		try {
			
			File title = new File(ReadProjectPropertiesFile.projectProps.getProperty("title"));
			if(!title.exists())
				title.mkdir();
			
			File packageDir = new File(".\\sbsupport\\project");
			File destDir = new File(".\\"+title);
			FileUtils.copyDirectory(packageDir, destDir);
			
			File srcProperties = new File(".\\sbapplicationproperties\\");
			File destProperties = new File(resourcePackage);
			FileUtils.copyDirectory(srcProperties, destProperties);
			
			File srcStaticFiles = new File(".\\uisupport\\");
			File destStaticFiles = new File(resourcePackage);
			FileUtils.copyDirectory(srcStaticFiles, destStaticFiles);
			
			File securitySrcDir = new File(".\\sbsupport\\security");
			File securityDestDir = new File(srcPackage+"\\"+basePackage+"\\"+ReadProjectPropertiesFile.projectProps.getProperty("pack")+"\\security");
			FileUtils.copyDirectory(securitySrcDir, securityDestDir);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String fullyQualifiedName = ".\\renamedsupportfiles\\" + uitemplateNumber + "\\";
		File dir = new File(fullyQualifiedName);
		String[] files = dir.list();
		for (int i = 0; i < files.length; i++) {
			String filename = files[i];
			String srcPath = new File(fullyQualifiedName+"\\"+files[i]).getAbsolutePath();
			
			if(filename.startsWith("uitemplate")) {
				String newFilename = filename.replace("uitemplate", cssTitle);
				readTextFileWriteToAnother(srcPath,cssfileLocationPath+"\\"+newFilename);
				
			}
		}
		
	}
	
	public void readTextFileWriteToAnother(String oldFile, String newFile) {
		try {
			FileReader reader = new FileReader(oldFile);
			FileWriter writer = new FileWriter(newFile, true);
			BufferedReader bufferedReader = new BufferedReader(reader);

			String line;

			while ((line = bufferedReader.readLine()) != null) {
				writer.write(line.replace("w3-", cssTitle + "-"));
				writer.write("\r\n");
			}
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
