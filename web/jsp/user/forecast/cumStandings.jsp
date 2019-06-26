<HTML>
<HEAD>
	<TITLE>Forecast Standings</TITLE>
</HEAD>

<BODY bgcolor="WHITE">
<%@ page import="org.collaborium.portfolio.forecast.*" %>
<%@ page import="org.collaborium.portfolio.*" %>

<%
        String portfolio = (String)request.getParameter("portfolio");
        portfolio = fLib.cleanse(portfolio);
	String thisPageURL = "/jportfolio/jsp/user/forecast/cumStandings.jsp";
	String sort = (String)request.getParameter("sort");
        sort = fLib.cleanse(sort);
	portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
	if (thisUser != null && portfolio == null) {
                portfolio = thisUser.getPortfolio();
%>
                <a href="/jportfolio/servlet/forecast">Back to Portfolio</a>
<%
        } else {
%>
                <a href="http://www.meteor.iastate.edu/forecast/">Forecasting Homepage</a>
<%
        }

        out.println("<HR>");

        if (portfolio == null)
                portfolio = (String)session.getAttribute("portfolio");

        session.setAttribute("portfolio", portfolio);


	out.println( fLib.cumulativeResults( portfolio, sort, thisPageURL ) );
%>



</BODY>
</HTML>
