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
		
		String srcPackage = ".\\"+title;
		File projectName = new File(srcPackage);
		if(!projectName.exists())
			projectName.mkdir();
		
		srcPackage = projectName.getAbsolutePath()+"\\src";
		projectName = new File(srcPackage);
		if(!projectName.exists())
			projectName.mkdir();
		
		srcPackage = projectName.getAbsolutePath()+"\\main";
		projectName = new File(srcPackage);
		if(!projectName.exists())
			projectName.mkdir();
		
		srcPackage = projectName.getAbsolutePath()+"\\java";
		projectName = new File(srcPackage);
		if(!projectName.exists())
			projectName.mkdir();
		
		
		
		String resourcePackage = ".\\"+title;
		File resourceName = new File(resourcePackage);
		if(!resourceName.exists())
			resourceName.mkdir();
		
		resourcePackage = resourceName.getAbsolutePath()+"\\src";
		resourceName = new File(resourcePackage);
		if(!resourceName.exists())
			resourceName.mkdir();
		
		resourcePackage = resourceName.getAbsolutePath()+"\\main";
		resourceName = new File(resourcePackage);
		if(!resourceName.exists())
			resourceName.mkdir();
		
		resourcePackage = resourceName.getAbsolutePath()+"\\resources";
		resourceName = new File(resourcePackage);
		if(!resourceName.exists())
			resourceName.mkdir();
		
		String testPackage = ".\\"+title+"";
		File testName = new File(testPackage);
		if(!testName.exists())
			testName.mkdir();
		
		testPackage = testName.getAbsolutePath()+"\\src";
		testName = new File(resourcePackage);
		if(!testName.exists())
			testName.mkdir();
		
		testPackage = testName.getAbsolutePath()+"\\test";
		testName = new File(resourcePackage);
		if(!testName.exists())
			testName.mkdir();
		
		testPackage = testName.getAbsolutePath()+"\\java";
		testName = new File(resourcePackage);
		if(!testName.exists())
			testName.mkdir();
		
		dBPropertiesFileRead.readFile();
		try {
			new JPAPersistance(projectName.getAbsolutePath(),resourceName.getAbsolutePath()).readDataBaseDetails();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Project created successfully");
	}
}
