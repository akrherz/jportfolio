<html>
<head>
<title>Jportfolio | My Dialog</title>

<jsp:useBean id="pgsql" scope="session" class="org.collaborium.portfolio.sqlBean"/>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.collaborium.util.*" %>
<%@ page import="org.collaborium.portfolio.*" %>


<% 
portfolioUser thisUser = (portfolioUser)session.getAttribute("User");

ResultSet myResultSet = pgsql.doSQL("Select * from dialog "
	+" where username ='"+ thisUser.getUserID() +"' and portfolio = '"+ thisUser.getPortfolio() +"' ORDER by date");
%>

	
<link rel=stylesheet type=text/css href="/jportfolio/css/basic.css">

</head>
<body bgcolor="white">

<H3>Your Dialog Entries:</H3>
<P>username: <%= thisUser.getUserID() %><BR>
portfolio: <%= thisUser.getPortfolio() %>

<HR>

<%
  while (myResultSet.next()) { 
%>
	  <P><B><FONT color="blue"><%= myResultSet.getString("subject") %> (<%= myResultSet.getString("type") %>)</FONT></B><BR>
          Date posted: <%= myResultSet.getString("date") %><BR>
	<blockquote><%= stringUtils.toBR( myResultSet.getString("body") ) %></blockquote><BR>

	   <CENTER><hr width="300"></CENTER>
<% 
  } 

%>

</BODY>
</HTML>



</body>
</html>
