package com.springboot.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.jdbc.main.CapitalCase;
import com.springboot.project.properties.ReadProjectPropertiesFile;

public class SBMainClass {

	public void createSpringBootMainClass(String projectName) {
		try {
			
			String controllerPackage = ReadProjectPropertiesFile.projectProps.getProperty("controller-Package");
			String basePackage = ReadProjectPropertiesFile.projectProps.getProperty("basePackage");
			String beanPackage = ReadProjectPropertiesFile.projectProps.getProperty("beanPackage");
			String repositoryPackage = ReadProjectPropertiesFile.projectProps.getProperty("repositoryPackage");
			String bootstrapClassName = ReadProjectPropertiesFile.projectProps.getProperty("bootstrapClassName");
			String pack = ReadProjectPropertiesFile.projectProps.getProperty("pack");

			String packageNameString = ".\\"+projectName+"\\"+basePackage+"\\" + pack + "\\";
			File packageDir = new File(packageNameString);
			packageDir.mkdir();

			PrintWriter writer = new PrintWriter(
					packageNameString + "\\" + CapitalCase.toCapitalCase(bootstrapClassName) + ".java");

			String packageImport = "package "+basePackage+"." + pack+";\n";
			packageImport += "import org.springframework.boot.SpringApplication; \n";
			packageImport += "import org.springframework.boot.autoconfigure.SpringBootApplication;\n\n";
			
			writer.println(packageImport);
			writer.println("@SpringBootApplication");
			writer.println("public class  "+ CapitalCase.toCapitalCase(bootstrapClassName) +"  {");
			writer.println("");
			writer.println("	public static void main(String[] args) {");
			writer.println("		SpringApplication.run("+ CapitalCase.toCapitalCase(bootstrapClassName) +".class, args);");
			writer.println("	}");
			writer.println("");
			writer.println("}");
			writer.close();
		} catch (IOException e) {
			// do something
		}
	}

}
