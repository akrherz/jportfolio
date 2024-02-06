<%@include file='./index.jsp'%>
<html>
<head>
<title>Jportfolio Admin | Student Dialog</title>

<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.collaborium.portfolio.*" %>

<% 
String selectedUser = request.getParameter("selectedUser");
String blockID = request.getParameter("blockID");
if (blockID == null)
	blockID = "1";

String subSelect = "threadid > 0";
String ethThread = null;
String notifyBaseURL = "/jportfolio/servlet/jdot3";

if ( blockID.equalsIgnoreCase("1") ){
	subSelect = " threadid >= 14106 and threadid < 14213 and threadid != 13892 ";
        ethThread = "14240";
}if ( blockID.equalsIgnoreCase("2") ){
	subSelect = " threadid >= 14270 and threadid <= 14408 and threadid != 14415";
        ethThread = "14415";
}if ( blockID.equalsIgnoreCase("3") ){
	subSelect = " threadid >= 14435 and threadid <= 14534 ";
        ethThread = "14545";
}
ResultSet myResultSet = dbInterface.callDB("Select * from dialog "
	+" where username ='"+selectedUser+"' and threadid != "+ethThread+" and"
	+" portfolio = '"+ thisUser.getPortfolio() +"' "
	+" and security = 'public' and "+subSelect+" ORDER by date");

ResultSet myResultSet2 = dbInterface.callDB("Select * from dialog where type = 'self"+blockID+"' and "
        +" username ='"+selectedUser+"' and portfolio = '"+ thisUser.getPortfolio() +"' and security = 'private' ");

ResultSet quizResponses = dbInterface.callDB("select * from dialog WHERE "
	+" security = 'private' and portfolio = '"+ thisUser.getPortfolio() +"' "
	+" and type != 'self1' and type != 'self2' and type != 'self3' and "
	+" subject ~* '"+blockID+"-' and username ='"+selectedUser+"' ORDER by date");

ResultSet ethical = dbInterface.callDB("select * from dialog WHERE"
  +" security = 'public' and threadid = '"+ethThread+"' "
  +" and portfolio = '"+ thisUser.getPortfolio() +"' "
  +" and username ='"+selectedUser+"' ORDER by date");


/* We need to get the porthome value for use in notifications */
ResultSet portHome = dbInterface.callDB("SELECT porthome from portfolios "
 +" WHERE portfolio = '"+ thisUser.getPortfolio() +"' and "
  +" porthome != '/jportfolio/servlet/' ");
if ( portHome.next() ) {
 notifyBaseURL = portHome.getString("porthome") +"/dialog/index.jsp";
}


%>

<link rel=stylesheet type=text/css href="/jportfolio/css/<%= thisUser.getStyle() %>.css">

</head>
<body bgcolor="white">

<H3>Dialog Entries:</H3>
<P>username: <%= selectedUser %><BR>
portfolio: <%= thisUser.getPortfolio() %><BR>
block: <%= blockID %>
<HR>

<TABLE bgcolor="#EEEEEE" width="100%"><TR><TD>
<%
  while (myResultSet2.next()) {
%>
	  <P><B><FONT color="blue"><%= myResultSet2.getString("subject") %> ( <%= myResultSet2.getString("type") %> )</FONT></B><BR>
          Date posted: <%= myResultSet2.getString("date") %><BR>
	<blockquote><%= jlib.toBR( myResultSet2.getString("body") ) %></blockquote><BR>

	<B>HTML Ref: </B> &#60;a href="<%= notifyBaseURL %>?mode=r&security=private&idnum=<%= jlib.toBR( myResultSet2.getString("idnum") ) %>"&#62; here&#60;/a&#62;
	   <CENTER><hr width="300"></CENTER>
<%
  }
%>

</TD></TR></TABLE>

<TABLE bgcolor="eecfa1" width="100%"><TR><TD>
<p><b>Ethical Posts:</b><br>
<%
  while (ethical.next()) {
%>
          <P><B><FONT color="blue"><%= ethical.getString("subject") %> ( <%= ethical.getString("type") %> )</FONT></B><BR>
          Date posted: <%= ethical.getString("date") %><BR>
        <blockquote><%= jlib.toBR( ethical.getString("body") ) %></blockquote><BR>

        <B>HTML Ref: </B> &#60;a href="<%= notifyBaseURL %>?mode=r&security=public&idnum=<%= jlib.toBR( ethical.getString("idnum") ) %>"&#62; here&#60;/a&#62;
           <CENTER><hr width="300"></CENTER>
<%
  }
%>

</TD></TR></TABLE>


<HR>

<%
  while (myResultSet.next()) { 
%>
	  <P><B><FONT color="blue"><%= myResultSet.getString("subject") %> ( <%= myResultSet.getString("type") %> )</FONT></B><BR>
          Date posted: <%= myResultSet.getString("date") %><BR>
	<blockquote><%= jlib.toBR( myResultSet.getString("body") ) %></blockquote><BR>

	<B>HTML Ref: </B> &#60;a href="<%= notifyBaseURL %>?mode=r&idnum=<%= myResultSet.getString("idnum") %>"&#62; here&#60;/a&#62;

	   <CENTER><hr width="300"></CENTER>
<% 
  } 

%>

<HR>

<%
  while (quizResponses.next()) { 
%>
	  <P><B><FONT color="blue"><%= quizResponses.getString("subject") %> ( <%= quizResponses.getString("type") %> )</FONT></B><BR>
          Date posted: <%= quizResponses.getString("date") %><BR>
	<blockquote><%= jlib.toBR( quizResponses.getString("body") ) %></blockquote><BR>

	<B>HTML Ref: </B> &#60;a href="<%= notifyBaseURL %>?security=private&mode=r&idnum=<%= quizResponses.getString("idnum") %>"&#62; here&#60;/a&#62;

	   <CENTER><hr width="300"></CENTER>
<% 
  } 

%>
</BODY>
</HTML>
