<HTML>
<HEAD>
	<TITLE>Front-End to JDOT</TITLE>
</HEAD>


<P>This is a test page for having people log-in here and then they can get into the jdot App.

<%@ page import="edu.iastate.iitap.portfolio.portfolioUser" %>


<%

	portfolioUser thisUser = (portfolioUser)session.getAttribute("User");


	if (thisUser ==  null) {
		thisUser = new portfolioUser();
		thisUser.setPortfolio("testP");
		thisUser.setUserID("Anonymous2");
		thisUser.setGroupID("-99");
		thisUser.setRealName("Anonymous2 Coward");
		session.setAttribute("User", thisUser);
	} else {
		thisUser.setPortfolio("testP");
		thisUser.setUserID("Anonymous2");
		thisUser.setGroupID("-99");
		thisUser.setRealName("Anonymous2 Coward");
	}

	session.setAttribute("isAdmin", Boolean.FALSE );
%>

<P>Let's try the link here:  <a href="jdot.jsp">Here</a>
