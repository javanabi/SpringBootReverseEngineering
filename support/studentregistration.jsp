

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
<script language="JavaScript" type="text/javascript" src="<%=request.getContextPath()+"/js/ts_picker.js"%>"></script>
<script language="JavaScript1.1" src="js/pass.js">


</script> <script type="text/javascript" src="<%=request.getContextPath()+"/js/image.js"%>"> </script>
 <script type="text/javascript" src="<%=request.getContextPath()+"/js/general.js"%>"> </script>
 <script type="text/javascript" src="<%=request.getContextPath()+"/js/adi.js"%>"> </script>
 <script type="text/javascript" src="<%=request.getContextPath()+"/js/form_validation.js"%>"> </script>

		<script language="JavaScript" src="images/javascripts.js"></script>
		<script language="JavaScript" src="images/pop-closeup.js"></script>
<script>



function goOn() {
var port=document.register.port.value;
var host=document.register.host.value;
var userName=document.register.userName.value;
window.location.href='http://'+host+':'+port+'/CollegeMagazine/CheckUserAction?userName='+userName+"&path=./registration.jsp";
;

} 
</script>
	</head>	
		
	<body>

		<jsp:include page="header.jsp"></jsp:include>
		<br />
		<center>
			<h3>

				<span class=subHead><br />Registration Form </span>
			</h3>
		</center>

		<form action="./RegisterAction" method="post" name="register"
			onSubmit="return validate()">
			<!--<table border="1"><tr><td></td></tr></table>-->
			<input type="hidden" name="port" value="<%=request.getLocalPort()%>" />
			<input type="hidden" name="host" value="<%=request.getServerName()%>" />
			<br />

			<table border='0' align="center" width=70%>
				<th colspan="6" bgcolor="#999933">
					Account Details
				</th>
				<tr></tr>
				<tr></tr>
				<tr>
					<td align='right'>
						User Name :
					</td>
					<td width="303">
						<input type="text" name="userName"
							value="<% if(request.getParameter("userName")!=null)
    out.print(request.getParameter("userName")); %>"
							size="20" onblur="goOn()" />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;



					
					
						<font color="black"> ${requestScope.status } </font>
				

					<td align='left'>

						<b><font color="green"> <c:if
									test="${'requestScope.status1'!='null'}">

									<c:out value="${param.status1}"></c:out>
								</c:if> </font>
						</b>


					</td>
					<td></td><td></td>
					<td></td>
				</tr>
				<tr>
					<td align='right'>
						PassWord :
					</td>
					<td>
						<input type="password" name="password"
							onkeyup="testPassword(document.forms.register.password.value);"
							onchange="Encrypt(document.forms.register.password.value);" />
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
				<td></td>
					<td align='right'>
						<a id="Words"> Strength :</a>
					</td>
					<td>
						<center> <table border='0' cellpadding=0 cellspacing=0 colspan='2'>
							<tr>
								<td height=15 bgcolor=red></td>
							</tr>
						</table></center>
					</td>
					
					<td></td>
				</tr>
				<tr>
					<td align='right'>
						Confirm :
					</td>
					<td>
						<input type="password" name="conformpassword" value="" size="20"
							onBlur="checkconformpassword()" />
					</td>
					<td border="0" align="left" rowspan="5" colspan='2'>


						<img alt="See Photo Here" id="previewField" src="images/flag.gif"
							height="150" width="120" />

					</td>
				</tr>
				<tr>
					<td align='right'>
						User Type :
					</td>
					<td width="276">
						<select name="loginType" onChange="cleartext()">
							<option value="select" selected="true">
								<font size="3" face="Verdana">Select </font>
							</option>
							<option value="student">
								<font size="3" face="Verdana">Student</font>
							</option>
							<option value="faculty">
								<font size="3" face="Verdana">Faculty</font>
							</option>
							<option value="moderator">
								<font size="3" face="Verdana">Moderator</font>
							</option>
						</select>
					</td>
				<tr>
					<td align='right'>
						SecurityQuestion :
					</td>
					<td>
						<select name="squest">
							<option value="select" selected="true">
								<font size="3" face="Verdana">--Select One---</font>
							</option>
							<option value="What is your favorite pastime?">
								<font size="3" face="Verdana">What is your favorite
									pastime?</font>
							</option>
							<option value="Who your childhood hero?">
								<font size="3" face="Verdana">Who your childhood hero?</font>
							</option>
							<option value="What is the name of your first school?">
								<font size="3" face="Verdana">What is the name of your
									first school?</font>
							</option>
							<option value="Where did you meet your spouse?">
								<font size="3" face="Verdana">Where did you meet your
									spouse?</font>
							</option>
							<option value="What is your favorite sports team?">
								<font size="3" face="Verdana">What is your favorite
									sports team?</font>
							</option>
							<option value="What is your father middle name?">
								<font size="3" face="Verdana">What is your father middle
									name?</font>
							</option>
							<option value="What was your high school mascot?">
								<font size="3" face="Verdana">What was your high school
									mascot?</font>
							</option>
							<option value="What make was your first car or bike?">
								<font size="3" face="Verdana">What make was your first
									car or bike?</font>
							</option>
							<option value="What is your pet name?">
								<font size="3" face="Verdana">What is your pet name?</font>
							</option>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="2" align='center'>
						<input type="checkbox" name="ch" value="1"
							onClick="check(register)" />
						Own Question :
					</td>
				</tr>
				<tr>
					<td>
						<span class="style3"><font size="3" face="verdana"></font>
					</td>
					<td>
						<input type="text" name="ownquest" disabled="disabled" size="37" />
					</td>
				</tr>
				<tr>
					<td align='right'>
						Security Answer :
					</td>
					<td>
						<input type="text" name="secrete" value="" size="20" />
					</td>
				</tr>

				<tr></tr>
				<tr></tr>
				<th colspan="6" bgcolor="#999933">
					Personal Details
				</th>
				<tr></tr>
				<tr></tr>
				<tr>
					<td align='right'>
						First Name :
					</td>
					<td width="276">
						<input type="text" name="firstName" value="" />
					</td>
					<td align='right'>
						Gender :
					</td>
					<td>
						<select name="gender">
							<option value="select" selected="true">
								<font size="3" face="Verdana">--Select--</font>
							</option>
							<option value="Male">
								<font size="3" face="Verdana">Male</font>
							</option>
							<option value="Female">
								<font size="3" face="Verdana">Female</font>
							</option>
						</select>
					</td>
				</tr>
				<tr>
					<td align='right'>
						Last Name :
					</td>
					<td width="276">
						<input type="text" name="lastName" value="" size="20" />
					</td>
					<td align='right'>
						Email :
					</td>
					<td>
						<input type="text" name="email" value="" size="20" />
					</td>
				</tr>
				<tr>
					<td align='right'>
						Birth Date :
					</td>
					<td>
						<input type="text" name="birthdate" value="" size="20"
							readonly="readonly" />
						<a href="javascript:show_calendar('document.register.birthdate', document.register.birthdate.value);"> <img src="<%=request.getContextPath()+"/images/cal.gif"%>" alt="a" width="18" height="18" border="0"/></a>
					</td>
					<td align='right'>
						Fax No :
					</td>
					<td>
						<input type="text" name="fax" value="" size="20" />
					</td>
				</tr>
				<tr>
					<td align='right'>
						Browse Photo :
					</td>
					<td>
						<input type="file" name="photo" class="textfield"
							onChange="preview(this)" />
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
				<tr>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
				</tr>
				<tr></tr>
				<tr></tr>
				<th colspan="6" bgcolor="#999933">
					<center>
						Contact Details
					</center>
				</th>
				<tr></tr>
				<tr></tr>
				<tr>
					<td align='right'>
						Address Type :
					</td>
					<td width="276">
						<select name="addressType" onChange="cleartext()">
							<option value="select" selected="true">
								<font size="3" face="Verdana">Select </font>
							</option>
							<option value="home">
								<font size="3" face="Verdana">Home</font>
							</option>
							<option value="office">
								<font size="3" face="Verdana">Office</font>
							</option>
							<option value="personal">
								<font size="3" face="Verdana">Personal</font>
							</option>
						</select>
					</td>
					<td width="" align='right'>
						City :
					</td>
					<td width="273">
						<input type="text" name="city" value="" size="20" />
					</td>

				</tr>
				<tr>
					<td align='right'>
						House No :
					</td>
					<td>
						<input type="text" name="houseNo" value="" size="20" />
					</td>

					
					
				</tr>
				<tr>
					<td align='right'>
						Street :
					</td>
					<td>
						<input type="text" name="street" value="" size="20" />
					</td>
					<td align='right'>
						State :
					</td>
					<td>
						<input type="text" name="state" value="" size="20" />
					</td>
				</tr>
				<tr>
					
					<td align='right'>
						Country :
					</td>
					<td>
						<input type="text" name="country" value="" size="20" />
					</td>
				</tr>
				<tr>
					<td align='right'>
						Phone No :
					</td>
					<td>
						<input type="text" name="phoneNo" value="" size="20"
							onBlur="ValidateForm()" />
					</td>
					<td align='right'>
						Pin :
					</td>
					<td>
						<input type="text" name="pin" value="" size="20"
							onChange="showStatus()" />
					</td>
				</tr>
				<th colspan="5">
					&nbsp;
				</th>
				<tr></tr>
				<tr></tr>

				<tr>
					<td></td>
					<td align="right">
						<font size="3" face="Verdana"> <input type="submit"
								name="register" value="Register" />&nbsp; </font>
					</td>
					<td align="left">
						<font size="3" face="Verdana"> <input type="reset"
								name="cancel" value="Cancel" /> </font>
					</td>
					<td></td>
				</tr>
			</table>
			<p>
				<br />
				<br />
				<br />
			</p>
			<p>
				&nbsp;
			</p>
			<p>
				&nbsp;
			</p>
			<p>
				&nbsp;
			</p>
			<p>
				<br />
			</p>

		</form>

		<script language="JavaScript" type="text/javascript">
//You should create the validator only after the definition of the HTML form
  var frmvalidator  = new Validator("register");
  
  frmvalidator.addValidation("userName","req","Please enter your Username");
   frmvalidator.addValidation("password","req","Please enter your Password");
   frmvalidator.addValidation("conformpassword","req","Please enter your Confirm Password");
   
   frmvalidator.addValidation("squest","dontselect=0");
   frmvalidator.addValidation("secrete","req","Please enter your Answer");
    
  frmvalidator.addValidation("firstName","req","Please enter your First Name");
  frmvalidator.addValidation("firstName","maxlen=20",	"Max length for FirstName is 20");
  frmvalidator.addValidation("firstName","alpha"," First Name Alphabetic chars only");
  
  frmvalidator.addValidation("lastName","req","Please enter your Last Name");
  frmvalidator.addValidation("lastName","maxlen=20","Max length is 20");
  frmvalidator.addValidation("lastName","alpha"," Last Name Alphabetic chars only");
  
   frmvalidator.addValidation("gender","dontselect=0");
   frmvalidator.addValidation("birthdate","req","Please enter your birthdate"); 
  
   frmvalidator.addValidation("photo","req","Please Load Your Photo"); 
  
  frmvalidator.addValidation("email","maxlen=50");
  frmvalidator.addValidation("email","req");
  frmvalidator.addValidation("email","email");
   
  
   frmvalidator.addValidation("addressType","dontselect=0");
   
   frmvalidator.addValidation("houseNo","req","Please enter your House Number");
   
   frmvalidator.addValidation("street","req","Please enter your Street Number");
     frmvalidator.addValidation("phoneType","dontselect=0");
   frmvalidator.addValidation("phoneNo","req");
  
  frmvalidator.addValidation("phoneNo","maxlen=50");
  frmvalidator.addValidation("phoneNo","numeric");
 frmvalidator.addValidation("phoneNo","Phone");
   
   frmvalidator.addValidation("city","req","Please enter your city Name");
   frmvalidator.addValidation("state","req","Please enter your State Name");
   frmvalidator.addValidation("country","req","Please enter your Country Name");
   frmvalidator.addValidation("pin","req","Please enter your pin Number");
   
  
   frmvalidator.addValidation("fax","req","Please enter Fax Number");
  frmvalidator.addValidation("fax","maxlen=6","Max length is 6 only");
  frmvalidator.addValidation("fax","numeric","  Fax numerics  only");
    
 </script>

		<br />
		<br />

		<jsp:include page="footer.jsp"></jsp:include>


	</body>
</html>