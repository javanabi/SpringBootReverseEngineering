<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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




		<script type="text/javascript">
function validator() {

	document.getElementById("pass").innerHTML = "";
	document.getElementById("change").innerHTML = "";

	if (document.getElementById("newpass").value == "") {

		document.getElementById("pass").innerHTML = "Plz Enter Password";
		return false;
	} else if (document.getElementById("new").value == "") {

		document.getElementById("pass").innerHTML = "Plz Enter New Password";
		return false;
	}

}
</script>

		<style type="text/css"></style>
	</head>
	<body>
		<span class="preload1"></span>
		<span class="preload2"></span>


		<jsp:include page="header.jsp"></jsp:include>

		<br />
		<br />
		<br />

		<center>
			<span class=Title><h3>
					<font color="ertyashblue">Change Password Form</font>
				</h3>
			</span>
		</center>



		<form id="form3" name="newedesignation" method="post"
			action="./ChangePasswordAction" onsubmit="return validator();">
			<table border="0" align="center">
				<tr>
					<td>
						<span class=Title> Login Name: </span>
					</td>
					<td>
						<input type="text" name="username"
							value="<%=session.getAttribute("user")%>" readonly />
					</td>
				</tr>
				<tr>
					<td>
						<span class=Title> Old Password: </span>
					</td>
					<td>
						<input type="password" name="oldpassword" id="newpass" />
						<div id="pass"></div>
					</td>
				</tr>
				<tr>
					<td>
						<span class=Title> New Password: </span>
					</td>
					<td>
						<input type="password" name="newpassword" id="new" />
						<div id="change"></div>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<div align="center">
							<strong> <input type="submit" name="Submit"
									value="Change" /> </strong>
						</div>
					</td>
				</tr>
			</table>
		</form>
		<jsp:include page="footer.jsp"></jsp:include>
	</body>
</html>
