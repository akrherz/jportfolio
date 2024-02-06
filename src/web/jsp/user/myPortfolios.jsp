<html>
<head>
<title>Jportfolio | My Portfolios</title>

<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.collaborium.portfolio.*" %>


<% 
portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
String forward = (String)request.getParameter("forward");

%>

<link rel=stylesheet type=text/css href="<%= jlib.httpBase%>/css/<%= thisUser.getStyle() %>.css">

</head>
<body bgcolor="white">

<%
	StringBuffer myBuffer = new StringBuffer();
	
	myBuffer.append( jlib.topBox("Select A Portfolio:") );
	myBuffer.append("<font class=\"instructions\"><blockquote>Listed below are the portfolios \n"
		+" that you have signed up for.\n"
		+" If you want to register for a portfolio, use the \"Sign Up for a Portfolio\" \n"
		+" option on the side.\n</blockquote></font>\n"
		+"<form method='GET' action='/jportfolio/servlet/jportfolio'>\n"
		+"<input type=\"hidden\" value=\""+forward+"\" name=\"forward\">\n");
	
	myBuffer.append("<H3>Registered Portfolios</H3>\n");
	
	myBuffer.append("<CENTER><SELECT name='portfolio' size='10'>\n");
	
	myBuffer.append( jlib.userPortfolios( thisUser.getUserID() ) );
	
	myBuffer.append("</SELECT>\n");
	
	myBuffer.append("<P><input type='SUBMIT' value='Open Portfolio'>\n"
		+" </form>\n");
	myBuffer.append("</CENTER>\n");
	myBuffer.append( jlib.botBox() );
	
	out.println( myBuffer.toString() );
%>


</BODY>
</HTML>
