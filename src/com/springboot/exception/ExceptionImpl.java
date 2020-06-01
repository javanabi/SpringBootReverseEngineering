package com.springboot.exception;

import java.io.File;
import java.io.PrintWriter;

import com.springboot.project.properties.ReadProjectPropertiesFile;


public class ExceptionImpl {

	String controllerPackage = ReadProjectPropertiesFile.projectProps.getProperty("controller-Package");
	String pack = ReadProjectPropertiesFile.projectProps.getProperty("pack");
	String basePackage = ReadProjectPropertiesFile.projectProps.getProperty("basePackage");
	String beanPackage = ReadProjectPropertiesFile.projectProps.getProperty("beanPackage");
	String repositoryPackage = ReadProjectPropertiesFile.projectProps.getProperty("repositoryPackage");
	String servicePackage = ReadProjectPropertiesFile.projectProps.getProperty("servicePackage");
	String author = ReadProjectPropertiesFile.projectProps.getProperty("project-author");
		
		public void createExceptionClasses(String className,String projectName){
			try {

				String packageNameString = projectName+"\\"+basePackage+"\\";
				File packageDir = new File(packageNameString);
				packageDir.mkdir();

				packageNameString = packageNameString + pack;
				packageDir = new File(packageNameString);
				packageDir.mkdir();

				packageNameString = packageNameString + "\\exception";
				packageDir = new File(packageNameString);
				packageDir.mkdir();

				PrintWriter writer = new PrintWriter(packageNameString + "\\ConnectionException.java");

				String packageImport = "package "+basePackage+"." + pack + ".exception;\n";
				writer.println(packageImport);
				writer.println("	public class ConnectionException extends Exception {");
				writer.println("	private static final long serialVersionUID = 1L;");
				writer.println("\n");
				writer.println("	public ConnectionException() {");
				writer.println("\n");
				writer.println("	}");
				writer.println("\n");
				writer.println("	public ConnectionException(String arg0) {");
				writer.println("		super(arg0);");
				writer.println("	}");
				writer.println("\n");
				writer.println("}");
				writer.close();
				
				writer = new PrintWriter(packageNameString + "\\DataNotFoundException.java");
				writer.println(packageImport);
				packageImport = "package "+basePackage+"." + pack + ".exception;\n";
				writer.println("	public class DataNotFoundException extends Exception {");
				writer.println("	private static final long serialVersionUID = 1L;");
				writer.println("\n");
				writer.println("	public DataNotFoundException() {");
				writer.println("\n");
				writer.println("	}");
				writer.println("	public DataNotFoundException(String arg0) {");
				writer.println("		super(arg0);");
				writer.println("	}");
				writer.println("}");
				writer.close();
				
				writer = new PrintWriter(packageNameString + "\\DeleteException.java");
				writer.println(packageImport);
				packageImport = "package "+basePackage+"." + pack + ".exception;\n";
				writer.println("	public class DeleteException extends Exception {");
				writer.println("	private static final long serialVersionUID = 1L;");
				writer.println("\n");
				writer.println("	public DeleteException() {");
				writer.println("\n");
				writer.println("	}");
				writer.println("	public DeleteException(String arg0) {");
				writer.println("		super(arg0);");
				writer.println("	}");
				writer.println("}");
				writer.close();
				
				writer = new PrintWriter(packageNameString + "\\UpdateException.java");
				writer.println(packageImport);
				packageImport = "package "+basePackage+"." + pack + ".exception;\n";
				writer.println("	public class UpdateException extends Exception {");
				writer.println("	private static final long serialVersionUID = 1L;");
				writer.println("\n");
				writer.println("	public UpdateException() {");
				writer.println("\n");
				writer.println("	}");
				writer.println("	public UpdateException(String arg0) {");
				writer.println("		super(arg0);");
				writer.println("	}");
				writer.println("}");
				writer.close();
				
				writer = new PrintWriter(packageNameString + "\\CreateException.java");
				writer.println(packageImport);
				packageImport = "package "+basePackage+"." + pack + ".exception;\n";
				writer.println("	public class CreateException extends RuntimeException {");
				writer.println("	private static final long serialVersionUID = 1L;");
				writer.println("\n");
				writer.println("	public CreateException() {");
				writer.println("\n");
				writer.println("	}");
				writer.println("	public CreateException(String arg0) {");
				writer.println("		super(arg0);");
				writer.println("	}");
				writer.println("}");
				writer.close();
				
				writer = new PrintWriter(packageNameString + "\\LoginException.java");
				packageImport = "package "+basePackage+"." + pack + ".exception;\n";
				writer.println(packageImport);
				writer.println("	public class LoginException extends Exception {");
				writer.println("	private static final long serialVersionUID = 1L;");
				writer.println("\n");
				writer.println("	public LoginException() {");
				writer.println("\n");
				writer.println("	}");
				writer.println("\n");
				writer.println("	public LoginException(String msg) {");
				writer.println("		super(msg);");
				writer.println("	}");
				writer.println("\n");
				writer.println("}");
				writer.close();
				
				writer = new PrintWriter(packageNameString + "\\FileStorageException.java");
				packageImport = "package "+basePackage+"." + pack + ".exception;\n";
				writer.println(packageImport);
				writer.println("	public class FileStorageException extends RuntimeException {");
				writer.println("	private static final long serialVersionUID = 1L;");
				writer.println("\n");
				writer.println("	public FileStorageException() {");
				writer.println("\n");
				writer.println("	}");
				writer.println("\n");
				writer.println("	public FileStorageException(String msg) {");
				writer.println("		super(msg);");
				writer.println("	}");
				writer.println("	public FileStorageException(String msg,Throwable cause) {");
				writer.println("		super(msg,cause);");
				writer.println("	}");
				writer.println("\n");
				writer.println("}");
				writer.close();
				
				writer = new PrintWriter(packageNameString + "\\MyFileNotFoundException.java");
				packageImport = "package "+basePackage+"." + pack + ".exception;\n";
				writer.println(packageImport);
				writer.println("	public class MyFileNotFoundException extends RuntimeException {");
				writer.println("	private static final long serialVersionUID = 1L;");
				writer.println("\n");
				writer.println("	public MyFileNotFoundException() {");
				writer.println("\n");
				writer.println("	}");
				writer.println("\n");
				writer.println("	public MyFileNotFoundException(String msg) {");
				writer.println("		super(msg);");
				writer.println("	}");
				writer.println("	public MyFileNotFoundException(String msg,Throwable cause) {");
				writer.println("		super(msg,cause);");
				writer.println("	}");
				writer.println("\n");
				writer.println("}");
				writer.close();
				
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}

