<html>
<HEAD>
  <TITLE>JPortfolio | IEM Meta Data</TITLE>
  <link rel="stylesheet" type="text/css" href="css/main.css">
</head>
<body>
<%@ page import="org.collaborium.portfolio.*" %>
<%@ page import="edu.iastate.agron.mesonet.*" %>
<%
  session.setAttribute("comebackURL", "/jportfolio/mesonet/");
  String register = (String)request.getParameter("register");
  String s_mid = null;
  String sname = null;
// Authentication component
  authBean auth = new authBean(request, session);
  portfolioUser thisUser = auth.thisUser;
  if (thisUser != null)
    session.setAttribute("User", thisUser);

%>
<%@include file='include/header.jsp'%>
<%
  if (thisUser == null){
%>

<p>Welcome to the IEM Meta Database Interface.  This system utilizes 
Portfolio for authentication and management.  Thus, if you have a Portfolio
account, you do not need to register again.</p>

<%
  String login = (String)request.getParameter("login");
  if (login != null && login.equalsIgnoreCase("yes"))
   {
%>
  <p>Account generation was successful, please try to log in.
<% } 

  if (auth.authError != null)
     { %>
     <p><b>Error:</b> <%= auth.authError %>
  <% } %>


<div id="login">
<h3>Please Log-In:</h3>
<form method="POST" action="index.jsp">

<br /><b>Username:</b><input type="text" name="username" size="20">
<br /><b>Password:</b><input type="password" name="password" size="20">
<br /><input type="SUBMIT" value="Log In"><input type="reset" value="Reset">

</form></div>

<P>Perhaps, you would like to <a href="createAccount.jsp">Create An Account</a></p>

<%
  } else if (register != null ){
%>

<div class="ptitle">Register for Network</div>

<p class="intro">Please select from the listing of networks below.  You will 
also need to enter an access password in order to register for a network.</p>

<form method="POST" action="/jportfolio/servlet/jportfolio">
<input type="hidden" name="mode" value="p">
<input type="hidden" name="forward" value="/jportfolio/mesonet/">

<P>Select Network to register for:</p>
<div style="padding-left: 20px">
  <%= jlib.selectPortfolios( thisUser.getUserID() ) %>
</div>

<p>Enter access password:</p>
<div style="padding-left: 20px">
  <input type="text" name="passwd" size="30">
</div>

<p><input type="submit" value="Register">
<input type="reset">

</form>

<%
  } else if (thisUser.getPortfolio() == null){
%>
  <p>You have successfully logged in!  You should now specify the 
  network of instruments you would like to work with.</p>
  <div id="login">
  <h3>Select Network:</h3>
  <form method="GET" action="index.jsp">
 
  <p>Select from the following:</p>
  
  <div style="padding-left: 20px">
  <select name='portfolio'>
  <%= jlib.userPortfolios( thisUser.getUserID() ) %>
  </select>
  </div>
  
  <br>
  <input type="SUBMIT" value="Sign In">
  
  </form>
  </div>  

  <p>Perhaps your network is not listed above.  If so, you will want to 
  <a href="index.jsp?register=yes">Register</a> for network access.</p>
  
<%
  } else {
  /** All systems are go... */
//  // Load up a hashtable of Station Conversions
//  ResultSet rs_st = dbInterface.callDB("SELECT * from iem_sites "
//  +" WHERE portfolio = '"+ thisUser.getPortfolio() +"' ");
//  Hashtable stns = new Hashtable(100);
//  while ( rs.next() ){
//    stns.put( rs_st.getString("s_mid"), rs_st.getString("name") );
//  }
//  session.setAttribute("stns", stns);
// Set up the Portfolio Environment, so they are included in Current Users
  jlib.addUser( thisUser.getUserID(), "IEM Tracker");

  response.setHeader("Refresh", "0; URL=main.jsp");

  }  
%>


<%= jlib.footer() %>
