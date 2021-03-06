package com.springboot.pom;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.springboot.project.properties.ReadProjectPropertiesFile;

public class POMImpl {
	
	String pack;

	public POMImpl(String pack) {
		this.pack = pack;
	}
 

	public void createPomXml(String resourcePackage) {
		try {
			String title = ReadProjectPropertiesFile.projectProps.getProperty("title");
			String pack = ReadProjectPropertiesFile.projectProps.getProperty("pack");
			
			String packageNameString = ".\\"+title;
			File packageDir = new File(packageNameString);
			packageDir.mkdir();

			PrintWriter writer = new PrintWriter(
					packageNameString + "\\pom.xml");
			
			writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			writer.println("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
			writer.println("	xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">");
			writer.println("	<modelVersion>4.0.0</modelVersion>");
			writer.println("	<parent>");
			writer.println("		<groupId>org.springframework.boot</groupId>");
			writer.println("		<artifactId>spring-boot-starter-parent</artifactId>");
			
			if(ReadProjectPropertiesFile.projectProps.getProperty("swagger2").equals("1")) 
				writer.println("		<version>2.2.1.RELEASE</version>");
			else 
				writer.println("		<version>2.2.1.RELEASE</version>");
			
			writer.println("		<relativePath/> <!-- lookup parent from repository -->");
			writer.println("	</parent>");
			writer.println("	<groupId>com."+pack+"</groupId>");
			writer.println("	<artifactId>"+title+"</artifactId>");
			writer.println("	<version>0.0.1-SNAPSHOT</version>");
			writer.println("	<name>"+title+"</name>");
			writer.println("	<description>Demo project for Spring Boot Apis CRUD using Spring Data JPA</description>");
			writer.println("");
			writer.println("	<properties>");
			writer.println("		<java.version>1.8</java.version>");
			if(ReadProjectPropertiesFile.projectProps.getProperty("date-picker").equals("1")) {
				writer.println("<bootstrap.version>4.0.0-2</bootstrap.version>");
				writer.println("<webjars-locator.version>0.30</webjars-locator.version>");
				writer.println("<font-awesome.version>5.11.2</font-awesome.version>");
			}
			writer.println("	</properties>");
			writer.println("");
			writer.println("	<dependencies>");
			
			if(ReadProjectPropertiesFile.projectProps.getProperty("data-rest-api-starter-template").equals("1")) {
				writer.println("		<dependency>");
				writer.println("			<groupId>org.springframework.boot</groupId>");
				writer.println("			<artifactId>spring-boot-starter-data-rest</artifactId>");
				writer.println("		</dependency>");
			}
			
			writer.println("		<dependency>");
			writer.println("			<groupId>org.springframework.boot</groupId>");
			writer.println("			<artifactId>spring-boot-starter-data-jpa</artifactId>");
			writer.println("		</dependency>");
			writer.println("		");
			writer.println("		<dependency>");
			writer.println("			<groupId>org.springframework.boot</groupId>");
			writer.println("			<artifactId>spring-boot-starter-web</artifactId>");
			writer.println("		</dependency>");
			writer.println("		");
			if(ReadProjectPropertiesFile.projectProps.getProperty("default-security").equals("1")) {
				writer.println("		<dependency>");
				writer.println("			<groupId>org.springframework.boot</groupId>");
				writer.println("			<artifactId>spring-boot-starter-security</artifactId>");
				writer.println("		</dependency>");
			}
			if(ReadProjectPropertiesFile.projectProps.getProperty("thymeleaf-controllers").equals("1")) {
				writer.println("		<dependency>");
				writer.println("			<groupId>org.springframework.boot</groupId>");
				writer.println("			<artifactId>spring-boot-starter-thymeleaf</artifactId>");
				writer.println("		</dependency>");
				if(ReadProjectPropertiesFile.projectProps.getProperty("default-security").equals("1")) 
					{	
						writer.println("		<dependency>");
						writer.println("			<groupId>org.thymeleaf.extras</groupId>");
						writer.println("			<artifactId>thymeleaf-extras-springsecurity5</artifactId>");
						writer.println("		</dependency>");
					}
				writer.println("		<dependency>");
				writer.println("			<groupId>nz.net.ultraq.thymeleaf</groupId>");
				writer.println("			<artifactId>thymeleaf-layout-dialect</artifactId>");
				writer.println("		</dependency>");
			}
			if(ReadProjectPropertiesFile.projectProps.getProperty("date-picker").equals("1")) {
				writer.println("	<dependency>");
				writer.println("		<groupId>org.webjars</groupId>");
				writer.println("		<artifactId>bootstrap</artifactId>");
				writer.println("		<version>${bootstrap.version}</version>");
				writer.println("	</dependency>");
				writer.println("");
				writer.println("	<dependency>");
				writer.println("		<groupId>org.webjars</groupId>");
				writer.println("		<artifactId>webjars-locator</artifactId>");
				writer.println("		<version>${webjars-locator.version}</version>");
				writer.println("	</dependency>");
				writer.println("");
				writer.println("	<dependency>");
				writer.println("		<groupId>org.webjars</groupId>");
				writer.println("		<artifactId>font-awesome</artifactId>");
				writer.println("		<version>${font-awesome.version}</version>");
				writer.println("	</dependency>");
			}
			if(ReadProjectPropertiesFile.projectProps.getProperty("change-pass-feature").equals("1")) {
				writer.println("		<dependency>");
				writer.println("			<groupId>org.springframework.boot</groupId>");
				writer.println("			<artifactId>spring-boot-starter-mail</artifactId>");
				writer.println("		</dependency>");
				writer.println("		<!-- https://mvnrepository.com/artifact/org.passay/passay -->");
				writer.println("		<dependency>");
				writer.println("		    <groupId>org.passay</groupId>");
				writer.println("		    <artifactId>passay</artifactId>");
				writer.println("		    <version>1.0</version>");
				writer.println("		</dependency>");
				writer.println("		<dependency>");
				writer.println("			<groupId>com.google.guava</groupId>");
				writer.println("			<artifactId>guava</artifactId>");
				writer.println("			<version>r05</version>");
				writer.println("		</dependency>");
			}
			
			if(ReadProjectPropertiesFile.projectProps.getProperty("sb-aop").equals("1")) {
				writer.println("		<dependency>");
				writer.println("			<groupId>org.springframework.boot</groupId>");
				writer.println("			<artifactId>spring-boot-starter-aop</artifactId>");
				writer.println("		</dependency>");
			}
			if(ReadProjectPropertiesFile.projectProps.getProperty("actuator").equals("1")) {
				writer.println("		<dependency>");
				writer.println("			<groupId>org.springframework.boot</groupId>");
				writer.println("			<artifactId>spring-boot-starter-actuator</artifactId>");
				writer.println("		</dependency>");
			}
			
			if(ReadProjectPropertiesFile.projectProps.getProperty("swagger2").equals("1")) {
				writer.println("		<dependency>");
				writer.println("			<groupId>io.springfox</groupId>");
				writer.println("			<artifactId>springfox-swagger2</artifactId>");
				writer.println("			<version>2.7.0</version>");
				writer.println("		</dependency>");
				writer.println("		<dependency>");
				writer.println("			<groupId>io.springfox</groupId>");
				writer.println("			<artifactId>springfox-swagger-ui</artifactId>");
				writer.println("			<version>2.7.0</version>");
				writer.println("		</dependency>");
			}
			writer.println("");
			writer.println("		<dependency>");
			writer.println("			<groupId>mysql</groupId>");
			writer.println("			<artifactId>mysql-connector-java</artifactId>");
			writer.println("			<scope>runtime</scope>");
			writer.println("		</dependency>");
			writer.println("		");
			writer.println("		<dependency>");
			writer.println("			<groupId>org.springframework.boot</groupId>");
			writer.println("			<artifactId>spring-boot-starter-test</artifactId>");
			writer.println("			<scope>test</scope>");
			writer.println("			<exclusions>");
			writer.println("				<exclusion>");
			writer.println("					<groupId>org.junit.vintage</groupId>");
			writer.println("					<artifactId>junit-vintage-engine</artifactId>");
			writer.println("				</exclusion>");
			writer.println("			</exclusions>");
			writer.println("		</dependency>");
			writer.println("	</dependencies>");
			writer.println("");
			writer.println("	<build>");
			writer.println("		<plugins>");
			writer.println("			<plugin>");
			writer.println("				<groupId>org.springframework.boot</groupId>");
			writer.println("				<artifactId>spring-boot-maven-plugin</artifactId>");
			writer.println("			</plugin>");
			writer.println("		</plugins>");
			writer.println("	</build>");
			writer.println("");
			writer.println("</project>");
			
			writer.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
