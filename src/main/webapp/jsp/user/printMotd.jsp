<%@ page import="org.collaborium.portfolio.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<% 
portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
if (thisUser == null || thisUser.getPortfolio() == null)
{
  return;
}
String idnum = (String)request.getParameter("idnum");
ResultSet myResultSet = null;
%>
<%= jlib.genHeader(thisUser, "Past Message of the Day", "ba") %>
<%
if (idnum == null) {
  myResultSet = dbInterface.callDB("Select issue at time zone 'America/Chicago' as issue, body from motd "
	+" where portfolio = '"+ thisUser.getPortfolio() +"' ");
} else {
  myResultSet = dbInterface.callDB("Select issue at time zone 'America/Chicago' as issue, body from motd "
	+" where id ='"+idnum+"' and portfolio = '"+ thisUser.getPortfolio() +"' ");
}
%>

<link rel=stylesheet type=text/css href="<%= jlib.httpBase%>/css/<%= thisUser.getStyle() %>.css">

</head>
<body bgcolor="white">

<p>Prior Messages of the Day (MOTD)

<HR>
  Back to <a href="<%= thisUser.getHome() %>"><%= thisUser.getPortfolioName() %></a> portfolio.
<HR>

<%
  while (myResultSet.next()) { 
%>
	  <P><B>Posted At: <FONT color="blue"><%= myResultSet.getString("issue") %></FONT></B><BR>
	<blockquote><%= jlib.toBR( myResultSet.getString("body") ) %></blockquote><BR>

	   <CENTER><hr width="300"></CENTER>
<% 
  } 

%>



</BODY>
</HTML>
