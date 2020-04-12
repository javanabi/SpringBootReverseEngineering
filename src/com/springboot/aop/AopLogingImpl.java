package com.springboot.aop;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.springboot.project.properties.ReadProjectPropertiesFile;

public class AopLogingImpl {

	public void createAopLoging(String projectName) {
		try {
			String controllerPackage = ReadProjectPropertiesFile.projectProps.getProperty("controller-Package");
			String basePackage = ReadProjectPropertiesFile.projectProps.getProperty("basePackage");
			String beanPackage = ReadProjectPropertiesFile.projectProps.getProperty("beanPackage");
			String repositoryPackage = ReadProjectPropertiesFile.projectProps.getProperty("repositoryPackage");
			String pack = ReadProjectPropertiesFile.projectProps.getProperty("pack");
			String packaop = ReadProjectPropertiesFile.projectProps.getProperty("pack-aop");
			String title = ReadProjectPropertiesFile.projectProps.getProperty("title");
			
			String packageNameString = projectName+"\\"+basePackage+"\\";
			File packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + pack;
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + "\\"+packaop;
			packageDir = new File(packageNameString);
			packageDir.mkdir();
			
			PrintWriter writer = new PrintWriter(packageNameString + "\\LoggingAspect.java");
			
			String packageImport = "package "+basePackage+"." + pack+"."+packaop+";\n";
			packageImport += "import org.apache.logging.log4j.LogManager; \n";
			packageImport += "import org.apache.logging.log4j.Logger; \n";
			packageImport += "import org.aspectj.lang.ProceedingJoinPoint; \n";
			packageImport += "import org.aspectj.lang.annotation.Around; \n";
			packageImport += "import org.aspectj.lang.annotation.Aspect; \n";
			packageImport += "import org.aspectj.lang.reflect.MethodSignature; \n";
			packageImport += "import org.springframework.stereotype.Component; \n";
			packageImport += "import org.springframework.util.StopWatch; \n";
			writer.println(packageImport);
			writer.println("");
			writer.println("@Aspect");
			writer.println("@Component");
			writer.println("public class LoggingAspect ");
			writer.println("{");
			writer.println("    private static final Logger LOGGER = LogManager.getLogger(LoggingAspect.class);");
			writer.println("     ");
			writer.println("    @Around(\"execution(* com."+pack+"..*(..)))\")");
			writer.println("    public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable"); 
			writer.println("    {");
			writer.println("        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();");
			writer.println("          ");
			writer.println("        //Get intercepted method details");
			writer.println("        String className = methodSignature.getDeclaringType().getSimpleName();");
			writer.println("        String methodName = methodSignature.getName();");
			writer.println("          ");
			writer.println("        final StopWatch stopWatch = new StopWatch();");
			writer.println("          ");
			writer.println("        //Measure method execution time");
			writer.println("        stopWatch.start();");
			writer.println("        Object result = proceedingJoinPoint.proceed();");
			writer.println("        stopWatch.stop();");
			writer.println("  ");
			writer.println("        //Log method execution time");
			writer.println("        LOGGER.info(\"Execution time of \" + className + \".\" + methodName + \" \"");
			writer.println("                            + \":: \" + stopWatch.getTotalTimeMillis() + \" ms\");");
			writer.println("  ");
			writer.println("        return result;");
			writer.println("    }");
			writer.println("}");
			writer.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
