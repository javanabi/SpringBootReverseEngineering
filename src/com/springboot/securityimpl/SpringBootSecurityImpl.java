package com.springboot.securityimpl;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.jdbc.dao.AbstractDataAccessObject;
import com.jdbc.main.CapitalCase;
import com.springboot.project.properties.ReadProjectPropertiesFile;

public class SpringBootSecurityImpl extends AbstractDataAccessObject {

	String pack, loginName="";;
	PreparedStatement pstmt;
	ResultSet resultSet, rs, rsf, tableTypes, contraintsRecords;
	String schemaName = getProperties().getProperty("duser");

	public SpringBootSecurityImpl(String pack) {
		this.pack = pack;
	}

	public void createSecurityImplClasses(Connection con,String projectName, String tableName) throws SQLException {
		try {
  			String basePackage = ReadProjectPropertiesFile.projectProps.getProperty("basePackage");
  			String pack = ReadProjectPropertiesFile.projectProps.getProperty("pack");
  			String entityPack = ReadProjectPropertiesFile.projectProps.getProperty("bean-jpa-modal-entity-pojo-Package");
  			String repoPack = ReadProjectPropertiesFile.projectProps.getProperty("repositoryPackage");

			String packageNameString = projectName+"\\"+basePackage+"\\" + pack;
			File packageDir = new File(packageNameString);
			packageDir.mkdir();

			packageNameString = packageNameString + "\\security";
			packageDir = new File(packageNameString);
			packageDir.mkdir();

			PrintWriter writer = new PrintWriter(packageNameString + "\\HomeController.java");
			
			String packageImport  = "package "+basePackage+"."+pack+".security;\n"; 
			packageImport += "import java.util.List;\n";
			packageImport += "import java.io.File;\n";
			packageImport += "import javax.servlet.http.HttpSession;\n";
			packageImport += "import org.springframework.beans.factory.annotation.Autowired;\n";
			packageImport += "import org.springframework.security.crypto.password.PasswordEncoder;\n";
			packageImport += "import org.springframework.stereotype.Controller;\n";
			packageImport += "import org.springframework.ui.Model;\n";
			packageImport += "import org.springframework.web.bind.annotation.GetMapping;\n";
			packageImport += "import org.springframework.web.bind.annotation.PostMapping;\n";
			packageImport += "import "+basePackage+"."+pack+"."+entityPack+"." + CapitalCase.toCapitalCase(tableName) + ";\n";
			packageImport += "import "+basePackage+"."+pack+"."+repoPack+"." + CapitalCase.toCapitalCase(tableName) + "Repository;\n";
			packageImport += "import "+basePackage+"."+pack+".util.ConstructFilePathUtil;\n";
			
			writer.println(packageImport);
			writer.println("");
			writer.println("@Controller");
			writer.println("public class HomeController {");
			writer.println("	@Autowired");
			writer.println("	private " + CapitalCase.toCapitalCase(tableName) + "Repository userService;");
			writer.println("");
			writer.println("	@Autowired");
			writer.println("	private RoleRepository roleService;\n");
			
			writer.println("	@Autowired");
			writer.println("	private CustomUserDetailsService customUserDetailsService;");
			writer.println("");
			writer.println("	@Autowired");
			writer.println("	public PasswordEncoder bCryptPasswordEncoder;");
			writer.println("");
			writer.println("	@PostMapping(\"/register\")");
			writer.println("	public String registration(" + CapitalCase.toCapitalCase(tableName) + " userForm) {");
			writer.println("		userForm.setPassword(bCryptPasswordEncoder.encode(userForm.getPassword()));");
			writer.println("		userService.save(userForm);");
			writer.println("		return \"redirect:/register\";");
			writer.println("	}");
			writer.println("");
			writer.println("	@GetMapping(\"/register\")");
			writer.println("	public String getRegistration(Model model) {");
			writer.println("		model.addAttribute(\"user\", new " + CapitalCase.toCapitalCase(tableName) + "());");
			writer.println("		List<Role> roles = roleService.findAll();");
			writer.println("		model.addAttribute(\"roles\", roles);");
			writer.println("		return \"register\";");
			writer.println("	}");
			writer.println("");
			writer.println("");
			writer.println("	@GetMapping({\"/home\",\"/\"})");
			writer.println("	public String getHome(HttpSession session) {");
			
			
			String logintable = ReadProjectPropertiesFile.projectProps.getProperty("database-login-table");
			String fileColumnName = ReadProjectPropertiesFile.projectProps.getProperty("userprofileimage-col-name");
			PreparedStatement pstmt;
			ResultSet resultSet;
			pstmt = con.prepareStatement("select * from " + logintable);
			resultSet = pstmt.executeQuery();
			ResultSetMetaData columnMetaData = resultSet.getMetaData();
			int columnCount = columnMetaData.getColumnCount();
			boolean fileFlag = false;
			for (int i = 1; i <=columnMetaData.getColumnCount(); i++) {
				String colType = columnMetaData.getColumnTypeName(i);
				if(colType.equalsIgnoreCase("LONGBLOB")) {
					fileFlag = true;
				}
			}
			
			writer.println("           "+CapitalCase.toCapitalCase(logintable)+" "+CapitalCase.toCapitalCase(logintable).toLowerCase()+" = customUserDetailsService.getLoggedUserDetails().get();");
			writer.println("				    		File folder = new File(\".//src//main//resources//static//download//\"+"+CapitalCase.replaceUnderScore(logintable).toLowerCase()+".getId());");
			writer.println("				    		if(!folder.exists())");
			writer.println("				    			folder.mkdir();");
			writer.println("				    		String path = folder.getAbsolutePath()+\"/profile-\"+"+CapitalCase.replaceUnderScore(logintable).toLowerCase()+".get"+CapitalCase.toCapitalCase(fileColumnName)+"name();");
			writer.println("				    		ConstructFilePathUtil.writeByte("+CapitalCase.replaceUnderScore(logintable).toLowerCase()+".get"+CapitalCase.toCapitalCase(fileColumnName)+"data(), path);");
			writer.println("				    		"+CapitalCase.replaceUnderScore(logintable).toLowerCase()+".setFileDownloadPath(\"/download/\"+"+CapitalCase.replaceUnderScore(logintable).toLowerCase()+".getUserid()+\"/profile-\"+"+CapitalCase.replaceUnderScore(logintable).toLowerCase()+".get"+CapitalCase.toCapitalCase(fileColumnName)+"name());");
			writer.println("							session.setAttribute(\"userProfileImage\","+logintable.toLowerCase()+".getFileDownloadPath());");
			writer.println("							return \"home\";");
			writer.println("	}");
			writer.println("");
			writer.println("}"); 
			writer.close();
			
			writer = new PrintWriter(packageNameString + "\\CustomUserDetailsService.java");
			
			packageImport  = "package "+basePackage+"."+pack+".security;\n"; 

			packageImport += "import java.util.Collection;\n";
			packageImport += "import java.util.Optional;\n\n";

			packageImport += "import org.springframework.beans.factory.annotation.Autowired;\n";
			packageImport += "import org.springframework.security.core.GrantedAuthority;\n";
			packageImport += "import org.springframework.security.core.authority.AuthorityUtils;\n";
			packageImport += "import org.springframework.security.core.userdetails.UserDetails;\n";
			packageImport += "import org.springframework.security.core.context.SecurityContextHolder;\n";
			packageImport += "import org.springframework.security.core.userdetails.UserDetailsService;\n";
			packageImport += "import org.springframework.security.core.userdetails.UsernameNotFoundException;\n";
			packageImport += "import org.springframework.stereotype.Service;\n";
			packageImport += "import org.springframework.security.crypto.password.PasswordEncoder;\n";
			packageImport += "import org.springframework.transaction.annotation.Transactional;\n\n";

			packageImport += "import com.springboot.entity." + CapitalCase.toCapitalCase(tableName) + ";\n";
			packageImport += "import com.springboot.persistance." + CapitalCase.toCapitalCase(tableName) + "Repository;\n";
			writer.println(packageImport);
			writer.println("");
			writer.println("@Service");
			writer.println("@Transactional");
			writer.println("public class CustomUserDetailsService implements UserDetailsService {");
			writer.println("");
			writer.println("	@Autowired");
			writer.println("	private " + CapitalCase.toCapitalCase(tableName) + "Repository userRepository;");
			writer.println("");
			writer.println("	@Autowired");
			writer.println("	private PasswordEncoder passwordEncoder;");
			writer.println("");
			writer.println("	@Override");
			writer.println("	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {");
					String loginColName = ReadProjectPropertiesFile.projectProps.getProperty("login-col-name");
			writer.println("		" + CapitalCase.toCapitalCase(tableName) + " user = userRepository.findBy"+CapitalCase.toCapitalCase(loginColName)+"(userName)");
			writer.println("				.orElseThrow(() -> new UsernameNotFoundException(\""+loginColName+" \" + userName +\"  not found\"));");
			writer.println("		return new org.springframework.security.core.userdetails.User(user.get"+CapitalCase.toCapitalCase(loginColName)+"(), user.getPassword(),");
			writer.println("				getAuthorities(user));");
			writer.println("	}");
			writer.println("");
			writer.println("	private static Collection<? extends GrantedAuthority> getAuthorities(" + CapitalCase.toCapitalCase(tableName) + " user) {");
			writer.println("		String[] userRoles = user.getRoles().stream().map((role) -> role.getName()).toArray(String[]::new);");
			writer.println("		Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);");
			writer.println("		return authorities;");
			writer.println("	}");
			writer.println("	");
			writer.println("			public Optional<"+ CapitalCase.toCapitalCase(tableName) +"> getLoggedUserDetails() {");
			writer.println("				Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();");
			writer.println("				String username=\"\";");
			writer.println("				if (principal instanceof UserDetails) ");
			writer.println("				    username = ((UserDetails)principal).getUsername();");
			writer.println("				return userRepository.findBy"+CapitalCase.toCapitalCase(loginColName)+"(username);");
			writer.println("	}");
			writer.println("");
			writer.println("	public boolean checkIfValidOldPassword(final String oldPassword,final Userdetails user) {");
			writer.println("    	return passwordEncoder.matches(oldPassword, user.getPassword());");
			writer.println("	}");
			writer.println("}");
			writer.close();
			
			writer = new PrintWriter(packageNameString + "\\AuthenticatedUser.java");
			
			packageImport  = "package "+basePackage+"."+pack+".security;\n"; 

			packageImport+="import java.util.Collection;\n";
			packageImport+="import java.util.HashSet;\n";
			packageImport+="import java.util.List;\n";
			packageImport+="import java.util.Set;\n\n";

			packageImport+="import org.springframework.security.core.GrantedAuthority;\n";
			packageImport+="import org.springframework.security.core.authority.AuthorityUtils;\n\n";

			packageImport+="import com.springboot.entity." + CapitalCase.toCapitalCase(tableName) + ";\n";
		    writer.println(packageImport);
			writer.println("public class AuthenticatedUser extends org.springframework.security.core.userdetails.User");
			writer.println("{");
			writer.println("");
			writer.println("	private static final long serialVersionUID = 1L;");
			writer.println("	private " + CapitalCase.toCapitalCase(tableName) + " user;");
			writer.println("	");
			writer.println("	public AuthenticatedUser(" + CapitalCase.toCapitalCase(tableName) + " user)");
			writer.println("	{");
			writer.println("		super(user.get"+CapitalCase.toCapitalCase(loginColName)+"(), user.getPassword(), getAuthorities(user));");
			writer.println("		this.user = user;");
			writer.println("	}");
			writer.println("	");
			writer.println("	public " + CapitalCase.toCapitalCase(tableName) + " getUser()");
			writer.println("	{");
			writer.println("		return user;");
			writer.println("	}");
			writer.println("	");
			writer.println("	private static Collection<? extends GrantedAuthority> getAuthorities(" + CapitalCase.toCapitalCase(tableName) + " user)");
			writer.println("	{");
			writer.println("		Set<String> roleAndPermissions = new HashSet<>();");
			writer.println("		List<Role> roles = user.getRoles();");
			writer.println("		");
			writer.println("		for (Role role : roles)");
			writer.println("		{");
			writer.println("			roleAndPermissions.add(role.getName());");
			writer.println("		}");
			writer.println("		String[] roleNames = new String[roleAndPermissions.size()];");
			writer.println("		Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roleAndPermissions.toArray(roleNames));");
			writer.println("		return authorities;");
			writer.println("	}");
			writer.println("}");
			
			writer.close();
			writer = new PrintWriter(packageNameString + "\\PasswordController.java");
			String loginTable = CapitalCase.toCapitalCase(tableName);

			writer.println("package com.springboot.security;");
			writer.println("");
			writer.println("import java.util.Locale;");
			writer.println("import java.util.Optional;");
			writer.println("import java.util.UUID;");
			writer.println("");
			writer.println("import javax.servlet.http.HttpServletRequest;");
			writer.println("import javax.validation.Valid;");
			writer.println("");
			writer.println("import org.springframework.beans.factory.annotation.Autowired;");
			writer.println("import org.springframework.context.MessageSource;");
			writer.println("import org.springframework.core.env.Environment;");
			writer.println("import org.springframework.mail.SimpleMailMessage;");
			writer.println("import org.springframework.mail.javamail.JavaMailSender;");
			writer.println("import org.springframework.security.crypto.password.PasswordEncoder;");
			writer.println("import org.springframework.stereotype.Controller;");
			writer.println("import org.springframework.ui.Model;");
			writer.println("import org.springframework.web.bind.annotation.GetMapping;");
			writer.println("import org.springframework.web.bind.annotation.PostMapping;");
			writer.println("import org.springframework.web.bind.annotation.RequestParam;");
			writer.println("import org.springframework.web.bind.annotation.ResponseBody;");
			writer.println("");
			writer.println("import com.springboot.entity."+loginTable+";");
			writer.println("import com.springboot.servicei."+loginTable+"ServiceI;");
			writer.println("");
			writer.println("@Controller");
			writer.println("public class PasswordController {");
			writer.println("");
			writer.println("	@Autowired");
			writer.println("	"+loginTable+"ServiceI "+loginTable.toLowerCase()+"Service;");
			writer.println("	");
			writer.println("    @Autowired");
			writer.println("    private JavaMailSender mailSender;");
			writer.println("    ");
			writer.println("	@Autowired");
			writer.println("	private CustomUserDetailsService customUserDetailsService;");
			writer.println("    ");
			writer.println("    @Autowired");
			writer.println("    private PasswordEncoder passwordEncoder;");
			writer.println("    ");
			writer.println("    @Autowired");
			writer.println("    private MessageSource messages;");
			writer.println("    ");
			writer.println("    @Autowired");
			writer.println("    private Environment env;");
			writer.println("    ");
			writer.println("    @Autowired");
			writer.println("    private ISecurityUserService securityUserService;");
			writer.println("    ");
			writer.println("    ");
			writer.println("    @GetMapping(\"/user/changePassword\")");
			writer.println("    public String showChangePasswordPage(");
			writer.println("            final Locale locale,");
			writer.println("            final Model model,");
			writer.println("            @RequestParam(\"token\") final String token");
			writer.println("    ) {");
			writer.println("        final String result = securityUserService.validatePasswordResetToken(token);");
			writer.println("");
			writer.println("        if(result != null) {");
			writer.println("            String message = messages.getMessage(\"auth.message.\" + result, null, locale);");
			writer.println("            return \"redirect:/login.html?lang=\" + locale.getLanguage() + \"&message=\" + message;");
			writer.println("        } else {");
			writer.println("            model.addAttribute(\"token\", token);");
			writer.println("            return \"updatePassword\";");
			writer.println("        }");
			writer.println("    }");
			writer.println("    ");
			
			String passColName = ReadProjectPropertiesFile.projectProps.getProperty("password-col-name");
			
			writer.println("    @GetMapping(\"/user/passwordChange\")");
			writer.println("    @ResponseBody");
			writer.println("    public GenericResponse passwordChange(final Locale locale, @Valid PasswordDto passwordDto) {");
			writer.println("        Optional<"+loginTable+"> user = customUserDetailsService.getLoggedUserDetails();");
			writer.println("        if(customUserDetailsService.checkIfValidOldPassword(passwordDto.getOldPassword(),user.get())) {");
			writer.println("        	userdetailsService.changeUserPassword(user.get(), passwordDto.getNewPassword());");
			writer.println("            return new GenericResponse(messages.getMessage(\"message.changePasswordSuc\", null, locale));");
			writer.println("        } else {");
			writer.println("            return new GenericResponse(messages.getMessage(\"auth.message.invalidPassword\", null, locale));");
			writer.println("        }");
			writer.println("    }");
			
			writer.println("    @GetMapping(\"/user/savePassword\")");
			writer.println("    @ResponseBody");
			writer.println("    public GenericResponse savePassword(final Locale locale, @Valid PasswordDto passwordDto) {");
			writer.println("");
			writer.println("        final String result = securityUserService.validatePasswordResetToken(passwordDto.getToken());");
			writer.println("");
			writer.println("        if(result != null) {");
			writer.println("            return new GenericResponse(messages.getMessage(\"auth.message.\" + result, null, locale));");
			writer.println("        }");
			writer.println("");
			writer.println("        Optional<"+loginTable+"> user = "+loginTable.toLowerCase()+"Service.getUserByPasswordResetToken(passwordDto.getToken());");
			writer.println("        if(user.isPresent()) {");
			writer.println("        	"+loginTable.toLowerCase()+"Service.changeUserPassword(user.get(), passwordDto.getNewPassword());");
			writer.println("            return new GenericResponse(messages.getMessage(\"message.resetPasswordSuc\", null, locale));");
			writer.println("        } else {");
			writer.println("            return new GenericResponse(messages.getMessage(\"auth.message.invalid\", null, locale));");
			writer.println("        }");
			writer.println("    }");
			writer.println("");
			writer.println("	// Reset password");
			writer.println("	@GetMapping(\"/user/resetPassword\")");
			writer.println("	public String resetPassword(final HttpServletRequest request,");
			writer.println("			@RequestParam(\"email\") final String userEmail,Model model) {");
			writer.println("		final "+loginTable+" "+loginTable.toLowerCase()+" = "+loginTable.toLowerCase()+"Service.findByEmailid(userEmail);");
			writer.println("		if ("+loginTable.toLowerCase()+" != null) {");
			writer.println("			final String token = UUID.randomUUID().toString();");
			writer.println("			"+loginTable.toLowerCase()+"Service.createPasswordResetTokenForUser("+loginTable.toLowerCase()+", token);");
			writer.println("			mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, "+loginTable.toLowerCase()+"));");
			writer.println("			model.addAttribute(\"msg\",messages.getMessage(\"message.resetPasswordEmail\", null, request.getLocale()));");
			writer.println("		}");
			writer.println("		return \"redirect:/login\";");
			writer.println("	}");
			writer.println("	");
			writer.println("	// Reset password");
			writer.println("	@GetMapping(\"/forgetPassword\")");
			writer.println("	public String resetPassword() {");
			writer.println("		 return \"forgetPassword\";");
			writer.println("	}");
			writer.println("	// password direct change");
			writer.println("	@GetMapping(\"/passwordChange\")");
			writer.println("	public String passwordChange() {");
			writer.println("		 return \"passwordChange\";");
			writer.println("	}");
			
			
			
			writer.println("	");
			writer.println("    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final "+loginTable+" "+loginTable.toLowerCase()+") {");
			writer.println("        final String url = contextPath + \"/user/changePassword?token=\" + token;");
			writer.println("        final String message = messages.getMessage(\"message.resetPassword\", null, locale);");
			writer.println("        return constructEmail(\"Reset Password\", message + \" \" + url, "+loginTable.toLowerCase()+");");
			writer.println("    }");
			writer.println("    ");
			writer.println("    private SimpleMailMessage constructEmail(String subject, String body, "+loginTable+" user) {");
			writer.println("        final SimpleMailMessage email = new SimpleMailMessage();");
			writer.println("        email.setSubject(subject);");
			writer.println("        email.setText(body);");
			writer.println("        email.setTo(user.getEmailid());");
			writer.println("        email.setFrom(env.getProperty(\"support.email\"));");
			writer.println("        return email;");
			writer.println("    }");
			writer.println("");
			writer.println("    private String getAppUrl(HttpServletRequest request) {");
			writer.println("        return \"http://\" + request.getServerName() + \":\" + request.getServerPort() + request.getContextPath();");
			writer.println("    }");
			writer.println("     ");
			writer.println("	");
			writer.println("}");
			writer.println("");
			

			writer.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
