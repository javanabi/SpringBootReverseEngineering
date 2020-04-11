<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
 <base href="<%=basePath%>">
    
    <title>My JSP 'articles.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->




	<script language="JavaScript" src="<%=request.getContextPath()+"/js/gen_validatorv31.js"%>" type="text/javascript"></script>
		
	</head>
	<body>
 	
<jsp:include page="header.jsp"></jsp:include>

                      <br>
		<FORM action="<%=request.getContextPath()+"/RecoverPasswordAction"%>" method="post" name="register">
			<TABLE border="0" align="center" bgcolor="white" width=80%>

				<TR>
					<TD>
						<TABLE border="0" align="center">
                      <center><h3><strong>ForgotPassword Form </strong></h3></center>
							<TR>
								<TD align="right"></TD>
								<TD>
									<TABLE border="0" align="center">
										<TR>
											<TD><span class=Title>
												Login Name</span>
											</TD>
											<TD>
												<INPUT type="text" name="username">
											</TD>
										</TR>
										<TR>
											<TD><span class=Title>
												Secret Question</span>
											</TD>
											<TD><span class=Title>
												<select name="squest">
      <option value="select" selected="true"><font size="3" face="Verdana">--Select One---</font></option>
      <option value="What is your favorite pastime?"><font size="3" face="Verdana">What is your favorite pastime?</font></option>
      <option value="Who your childhood hero?"><font size="3" face="Verdana">Who your childhood hero?</font></option>
      <option value="What is the name of your first school?"><font size="3" face="Verdana">What is the name of your first school?</font></option>
      <option value="Where did you meet your spouse?"><font size="3" face="Verdana">Where did you meet your spouse?</font></option>
      <option value="What is your favorite sports team?"><font size="3" face="Verdana">What is your favorite sports team?</font></option>
      <option value="What is your father middle name?"><font size="3" face="Verdana">What is your father middle name?</font></option>
      <option value="What was your high school mascot?"><font size="3" face="Verdana">What was your high school mascot?</font></option>
      <option value="What make was your first car or bike?"><font size="3" face="Verdana">What make was your first car or bike?</font></option>
      <option value="What is your pet name?"><font size="3" face="Verdana">What is your pet name?</font></option>
    </select>
										</span>	</TD>
										</TR>
										
										<TR>
											<TD><span class=Title>
												Secret Answer</span>
											</TD>
											<TD>
												<INPUT name="sanswer" type="text">
											</TD>
										</TR>
										<TR>
											<TD colspan="2"><span class=Title>
												<DIV align="center">
													<INPUT name="Input" type="submit" value="Recover">
													&nbsp;
													<INPUT name="Input" type="reset" value="Clear">
												</DIV></span>
											</TD>
										</TR>
									</TABLE>
								</TD>
								<TD>&nbsp;
									
							  </TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</FORM>
		
		
		<script language="JavaScript" type="text/javascript">
//You should create the validator only after the definition of the HTML form
  var frmvalidator  = new Validator("register");

  frmvalidator.addValidation("username","req","Loginname is required");
    frmvalidator.addValidation("username","alpha","Loginname is Only Characters");
    frmvalidator.addValidation("squest","req","Security Question is required");
    frmvalidator.addValidation("sanswer","req","SAnswer is required");

     </script>
      <br/><br/>
			
                  <jsp:include page="footer.jsp"></jsp:include>
	</body>
</html>