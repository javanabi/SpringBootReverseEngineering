<%@ page isErrorPage="true" import="java.io.*"%>
<%
	String usertype;
%>
<%
	if (session.getAttribute("user") == null) {
		RequestDispatcher rd = request
				.getRequestDispatcher("./loginform.jsp");
		rd.forward(request, response);
	}
%>
<head>
	<title>Exception Even Occurred!</title>
	<style>
body,p {
	font-family: Tahoma;
	font-size: 10pt;
	padding-left: 30;
}

pre {
	font-size: 8pt;
}
</style>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<br />
	<center>
		<h1>
			Error Page
		</h1>
	</center>
</body>