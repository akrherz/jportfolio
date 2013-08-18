<HTML>
<HEAD>
  <TITLE>JPortfolio | IEM Meta Data</TITLE>
  <link rel=stylesheet type=text/css href=css/main.css>
</head>
<body>

<%@ page import="org.collaborium.portfolio.*" %>
<%@ page import="edu.iastate.agron.mesonet.*" %>
<%
  portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
  String message = (String)request.getParameter("errorMessage");
  String sname = ""; 
  String s_mid = null;
  if (thisUser == null){ 
  
%>

<%@ include file='include/header.jsp' %>



<p><font class="bluet">Create Account:</font></p>

<p class="intro">In order to use this system, you must create an account with Portfolio.
Please fill in the appropriate blanks and select which network you wish to access.</p>

<form method="POST" action="/jportfolio/servlet/jportfolio">
<input type="hidden" value="i" name="mode">
<input type="hidden" value="/jportfolio/mesonet/" name="baseForward">

<%
if (message != null){
  out.write("ERROR: <font class=\"warn\">"+message+"</font>\n");

}
%>
<table bgcolor="#EEEEEE">
<tr>
  <th>Request Username:</th>
  <td><input type="text" name="userID" size="20"></td>
</tr>

<tr>
  <th>Request Password:</th>
  <td><input type="password" name="passwd1" size="20">
  <br>Verify: <input type="password" name="passwd2" size="20"></td>
</tr>

<tr>
  <th>First Name:</th>
  <td><input type="text" name="fName" size="30"></td>
</tr>

<tr>
  <th>Last Name:</th>
  <td><input type="text" name="lName" size="30"></td>
</tr>

<tr>
  <th>Email Address:</th>
  <td><input type="text" name="email" size="30"></td>
</tr>

<tr>
  <td colspan="2">Portfolio may occasionally email you with updates or some
  other event notification.  Please use an address that you routinely check.</td>
</tr>


<tr>
  <th colspan="2">
  <input type="submit" value="Create Account">
  <input type="reset">
  </th>
</tr>
</table>



<%
  } else {
  /** All systems are go... */
  response.setHeader("Refresh", "0; URL=main.jsp");

%>

<%= jlib.footer() %>
