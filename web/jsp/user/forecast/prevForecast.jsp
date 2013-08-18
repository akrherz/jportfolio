<%@ page import="org.collaborium.portfolio.forecast.*" %>
<%@ page import="org.collaborium.portfolio.*" %>
<html>
<HEAD>
	<TITLE>Previous Forecast Results</TITLE>
</HEAD>

<BODY bgcolor="WHITE">
<%
  String portfolio = (String)request.getParameter("portfolio");
  String sqlDate = (String)request.getParameter("sqlDate");
  String thisPageURL = "/jportfolio/jsp/user/forecast/prevForecast.jsp";
  String sort = (String)request.getParameter("sort");

  portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
  if (thisUser != null && portfolio == null) {
    portfolio = thisUser.getPortfolio(); 

    if (portfolio == null)
      portfolio = (String)session.getAttribute("portfolio");

    session.setAttribute("portfolio", portfolio);
%>
<a href="/jportfolio/servlet/forecast">Back to Portfolio</a>
<%	} else {  %>
<a href="http://www.meteor.iastate.edu/forecast/">Forecasting Homepage</a>
<%	}

  out.println("<HR>");
  try{
	out.println( fLib.forecastResults( portfolio, sqlDate, sort, thisPageURL ) );
	} catch( Exception ex) {
	out.println("There are no forecast dates available.");
	}
%>
</BODY>
</HTML>
