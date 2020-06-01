package com.springboot.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class InitServletImpl {


	String pack;
	
	public InitServletImpl(String pack) {
		this.pack = pack;
	}
	
	public void createInitServletImpl() throws SQLException{
		try {
			String packageNameString = ".\\com."+pack+".util";

			PrintWriter writer = new PrintWriter(packageNameString + "\\InitServlet.java");
			
			String packageImport = "package com." + pack + ".util;\n";

			packageImport += "import java.io.IOException; \n";
			packageImport += "import java.io.InputStream; \n";
			packageImport += "import java.util.Properties; \n";

			packageImport += "import javax.servlet.ServletConfig; \n";
			packageImport += "import javax.servlet.ServletContext; \n";
			packageImport += "import javax.servlet.http.HttpServlet; \n";

			packageImport += "import com."+pack+".dao.AbstractDataAccessObject; \n";
			packageImport += "import com."+pack+".util.LoggerManager; \n";
			writer.println(packageImport);
			writer.println("public class InitServlet extends HttpServlet { \n");
			writer.println("	/** \n");
			writer.println("	 *  \n");
			writer.println("	 */ \n");
			writer.println("	private static final long serialVersionUID = 1L; \n");
			writer.println("	DBConnectionFactory dobject; \n");

			writer.println("	public void init(ServletConfig sc) \n");
			writer.println("		{ \n");
			writer.println("		ServletContext ctx = sc.getServletContext(); \n");
			writer.println("		InputStream fis = ctx.getResourceAsStream(sc.getInitParameter(\"config\")); \n");
			writer.println("		Properties props = new Properties(); \n");
			writer.println("		 \n");
			writer.println("		try \n");
			writer.println("		{ \n");
			writer.println("			props.load(fis); \n");
			writer.println("		} \n");
			writer.println("		catch (IOException ioe) \n");
			writer.println("	{ \n");
			writer.println("			ioe.printStackTrace(); \n");
			writer.println("		} \n");
			writer.println("		dobject = new DBConnectionFactory(); \n");
			writer.println("		dobject.setProperties(props); \n");

			writer.println("		LoggerManager.logger = new LoggerManager().getLogger(props.getProperty(\"logfile\")); \n");
			writer.println("		LoggerManager.writeLogInfo(\"Logger Instantiated\"); \n");

			writer.println("		} \n");

			writer.println("} \n");

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
