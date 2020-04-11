package com.exception.impl;

import java.io.File;
import java.io.PrintWriter;


public class ExceptionImpl {

		String pack;

		public ExceptionImpl(String pack) {
			this.pack = pack;
		}
		
		public void createExceptionClasses(String className){
			try {

				String packageNameString = ".\\com\\";
				File packageDir = new File(packageNameString);
				packageDir.mkdir();

				packageNameString = packageNameString + pack;
				packageDir = new File(packageNameString);
				packageDir.mkdir();

				packageNameString = packageNameString + "\\exception";
				packageDir = new File(packageNameString);
				packageDir.mkdir();

				PrintWriter writer = new PrintWriter(packageNameString + "\\ConnectionException.java");

				String packageImport = "package com." + pack + ".exception;\n";
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
				packageImport = "package com." + pack + ".exception;\n";
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
				writer = new PrintWriter(packageNameString + "\\LoginException.java");
				packageImport = "package com." + pack + ".exception;\n";
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
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}

