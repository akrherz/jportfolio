
<HTML>
<HEAD>
	<TITLE>Thanks for Voting</TITLE>
</HEAD>

<body bgcolor="white">

<%@ page import="org.collaborium.portfolio.portfolioMessage" %>
<%@ page import="org.collaborium.portfolio.*" %>

<%
	portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
	String idNum = (String)request.getParameter("idnum");
	if (idNum != null && thisUser != null && thisUser.checkDialogidnum_learn(idNum) ) {
		portfolioMessage thisMessage = new portfolioMessage( idNum, false );
		thisMessage.addLearnSmile();
%>
	<P>Your smile has been registered.
<%
	} else {
%>
	<P>This vote is not allowed.  Perhaps you allready voted on this message.
<%
	}
%>

</body>
</HTML>
