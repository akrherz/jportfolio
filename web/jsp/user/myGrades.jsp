<html>
<head>
<title>Jportfolio | My Grades</title>

<jsp:useBean id="pgsql" scope="session" class="org.collaborium.portfolio.sqlBean"/>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.collaborium.portfolio.*" %>


<% 
portfolioUser thisUser = (portfolioUser)session.getAttribute("User");

ResultSet myResultSet = pgsql.doSQL("Select * from scores "
	+" where userid ='"+ thisUser.getUserID() +"' and portfolio = '"+ thisUser.getPortfolio() +"' ");
%>

<link rel=stylesheet type=text/css href="<%= jlib.httpBase%>/css/<%= thisUser.getStyle() %>.css">

</head>
<body bgcolor="white">

<H3>Your Grades:</H3>
<P>username: <%= thisUser.getUserID() %><BR>
portfolio: <%= thisUser.getPortfolio() %>

<HR>

<TABLE>
<TR>
	<TH>Assignment:</TH>
	<TH>Application:</TH>
	<TH>Grade:</TH>
</TR>

<%
  while (myResultSet.next()) { 
%>
	<TR>
	  <TD><%= myResultSet.getString("assign") %></TD>
	  <TD><%= myResultSet.getString("app") %></TD>
	  <TD><%= myResultSet.getString("score") %></TD>
	</TR>
<% 
  } 
%>

</TABLE>

</BODY>
</HTML>
