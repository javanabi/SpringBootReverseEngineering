package com.springboot.uipages;

import java.io.File;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.jdbc.dao.AbstractDataAccessObject;
import com.springboot.persistance.jpa.JPAPersistance;
import com.springboot.project.properties.ReadProjectPropertiesFile;
import com.springboot.util.InputNamesFileRead;

public class ThymeleafJSScriptslPagesImpl extends AbstractDataAccessObject {

	PreparedStatement pstmt;
	String pack, resourcePackage;
	String title;
	ResultSet resultSet, rs, rsf, tableTypes, contraintsRecords;
	String loginTableName = ReadProjectPropertiesFile.projectProps.getProperty("database-login-table");
	String cssTitle = ReadProjectPropertiesFile.projectProps.getProperty("css-title");
	String uitemplateNumber = ReadProjectPropertiesFile.projectProps.getProperty("uitemplate-number");
	List<String> colList= JPAPersistance.nottoDisplayColumnNamesFromPRoperteisFile();
	List<String> basicTableList= JPAPersistance.basicTableListFromPRoperteisFile();
	
	Set<String> inputNames = InputNamesFileRead.inputNames;
	public ThymeleafJSScriptslPagesImpl(String resourcePackage) {
		this.resourcePackage = resourcePackage;
	}

	public void createThymeLeafJSScriptFirstPage() throws SQLException {
		try {
			
			File fragmentsDir = new File(resourcePackage + "\\templates\\fragments");
			if (!fragmentsDir.exists())
				fragmentsDir.mkdir();
			
				PrintWriter writer = new PrintWriter(fragmentsDir.getAbsolutePath() + "\\scripts.html");
					writer.println("<!--   Core JS Files   -->");
					writer.println("");
					writer.println("<script th:src=\"@{/webjars/jquery/jquery.min.js}\"></script>");
					writer.println("<script th:src=\"@{/webjars/popper.js/umd/popper.min.js}\"></script>");
					writer.println("<script th:src=\"@{/webjars/bootstrap/js/bootstrap.min.js}\"></script>");
					writer.println("<script th:src=\"@{/assets/bootstrap-datepicker/js/bootstrap-datepicker.js}\"></script>");
					writer.println("");
					writer.println("<script>");
					writer.println("    $('.input-group.date').datepicker({");
					writer.println("        autoclose: true,");
					writer.println("        todayHighlight: true,");
					writer.println("        format: \"dd/mm/yyyy\"");
					writer.println("    });");
					writer.println("</script>");
					writer.println("");
					writer.println("<script>");
					writer.println("// Get the Sidebar");
					writer.println("var mySidebar = document.getElementById(\"mySidebar\");");
					writer.println("");
					writer.println("// Get the DIV with overlay effect");
					writer.println("var overlayBg = document.getElementById(\"myOverlay\");");
					writer.println("");
					writer.println("// Toggle between showing and hiding the sidebar, and add overlay effect");
					writer.println("function "+cssTitle+"_open() {");
					writer.println("  if (mySidebar.style.display === 'block') {");
					writer.println("    mySidebar.style.display = 'none';");
					writer.println("    overlayBg.style.display = \"none\";");
					writer.println("  } else {");
					writer.println("    mySidebar.style.display = 'block';");
					writer.println("    overlayBg.style.display = \"block\";");
					writer.println("  }");
					writer.println("}");
					writer.println("");
					writer.println("// Close the sidebar with the close button");
					writer.println("function "+cssTitle+"_close() {");
					writer.println("  mySidebar.style.display = \"none\";");
					writer.println("  overlayBg.style.display = \"none\";");
					writer.println("}");
					writer.println("</script>");
					writer.println("");
					writer.println("<script th:src=\"@{/js/jquery.min.js}\" type=\"text/javascript\"></script>");
					writer.println("<script th:src=\"@{/js/bootstrap.min.js}\" type=\"text/javascript\"></script>");
					writer.println("");
					writer.println("<!--  Checkbox, Radio & Switch Plugins -->");
					writer.println("<script th:src=\"@{/js/bootstrap-checkbox-radio.js}\"></script>");
					writer.println("");
					writer.println("<!--  Charts Plugin -->");
					writer.println("<script th:src=\"@{/js/chartist.min.js}\"></script>");
					writer.println("");
					writer.println("<!--  Notifications Plugin    -->");
					writer.println("<script th:src=\"@{/js/bootstrap-notify.js}\"></script>");
					writer.println("");
					writer.println("<!--  Google Maps Plugin    -->");
					writer.println("<script type=\"text/javascript\" src=\"https://maps.googleapis.com/maps/api/js\"></script>");
					writer.println("");
					writer.println("<!-- Paper Dashboard Core javascript and methods for Demo purpose -->");
					writer.println("<script th:src=\"@{/js/paper-dashboard.js}\"></script>");
					writer.println("");
					writer.println("<!-- Paper Dashboard DEMO methods, don't include it in your project! -->");
					writer.println("<script th:src=\"@{/js/demo.js}\"></script>");
					writer.println("");
					writer.println("");
			writer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createThymeLeafDefaultNavigationPage(PrintWriter writer) {
		try {
			 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}