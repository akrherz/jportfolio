<html>
<head>
  <TITLE>JPortfolio | IEM Data</TITLE>
  <link rel=stylesheet type=text/css href=/jportfolio/mesonet/css/main.css>
  
  <%@ page import="org.collaborium.portfolio.*" %>
  <%@ page import="org.collaborium.util.*" %>
  <%@ page import="org.collaborium.portfolio.jdot.*" %>
  <%@ page import="java.sql.*" %>
  <%@ page import="edu.iastate.agron.mesonet.*" %>
<%
  portfolioUser thisUser = (portfolioUser)session.getAttribute("User");


  String s_mid = (String)request.getParameter("s_mid");
  String sname = (String)session.getAttribute("sname");
  String mode = (String)request.getParameter("mode");
  if (mode == null) mode = "z";

  if (sname == null){
    sname = "Undefined";
    session.setAttribute("sname", "Undefined");
  }
  if (s_mid == null){
    s_mid = (String)session.getAttribute("s_mid");
  } else {
    session.setAttribute("s_mid", s_mid);
    mSite newSite = new mSite(thisUser.getPortfolio(), s_mid);
    session.setAttribute("sname", newSite.getName() );
    sname = newSite.getName();
  }
  if (mode.charAt(0) == 'b'){ // Exit this station
    session.removeAttribute("s_mid");
    session.removeAttribute("sname");
    sname = null;
    s_mid = null;
  }

  /** Must also call footer.jsp from any page... */
  if (thisUser == null || thisUser.getPortfolio() == null ){
    response.setHeader("Refresh", "0; URL=/jportfolio/mesonet/index.jsp");
    return;
  }
%>  
</head>
<body>
