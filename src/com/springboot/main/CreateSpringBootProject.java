package com.springboot.main;

import java.io.File;

import com.springboot.persistance.jpa.JPAPersistance;
import com.springboot.project.properties.ReadProjectPropertiesFile;

public class CreateSpringBootProject {
	public static void main(String[] args) {
		ReadProjectPropertiesFile dBPropertiesFileRead = new ReadProjectPropertiesFile();
		
		String title = ReadProjectPropertiesFile.projectProps.getProperty("title");
		String controllerPackage = ReadProjectPropertiesFile.projectProps.getProperty("controller-Package");
		String basePackage = ReadProjectPropertiesFile.projectProps.getProperty("basePackage");
		String beanPackage = ReadProjectPropertiesFile.projectProps.getProperty("beanPackage");
		String repositoryPackage = ReadProjectPropertiesFile.projectProps.getProperty("repositoryPackage");
		String pack = ReadProjectPropertiesFile.projectProps.getProperty("pack");
		
		String srcPackage = ".\\"+title+"\\src\\main\\java\\";
		File projectName = new File(srcPackage);
		if(!projectName.exists())
			projectName.mkdir();
		
		String resourcePackage = ".\\"+title+"\\src\\main\\resources\\";
		File resourceName = new File(resourcePackage);
		if(!resourceName.exists())
			resourceName.mkdir();
		
		String testPackage = ".\\"+title+"\\src\\test\\java\\";
		File testName = new File(testPackage);
		if(!testName.exists())
			testName.mkdir();
		
		dBPropertiesFileRead.readFile();
		new JPAPersistance(srcPackage,resourcePackage).readDataBaseDetails();
		System.out.println("Project created successfully");
	}
}
