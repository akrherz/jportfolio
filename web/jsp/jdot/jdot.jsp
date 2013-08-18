<%@ page import="jlib" %>
<%@ page import="edu.iastate.iitap.portfolio.portfolioUser" %>
<%@ page import="edu.iastate.iitap.portfolio.jdot.*" %>
<%
	
	StringBuffer pageContent = new StringBuffer();
	jdot myJdot = jdot.getInstance();
	String thisPageURL = "/jportfolio/jsp/jdot/jdot.jsp";
%>

<%@include file='variables.jsp' %>

<%	
	// Change the current dialog type if something was posted
	String dialogType = (String)session.getAttribute("dialogType");;
	if (postedDialogType != null ) {
		dialogType = postedDialogType;
		session.setAttribute("dialogType", dialogType);
	}
	portfolioUser thisUser = null;
	
	try{
		thisUser = (portfolioUser)session.getAttribute("User");
	} catch(Exception ex) {
		System.err.println("I am here");
		ex.printStackTrace();
		thisUser = new portfolioUser();
	}
	String portfolio = thisUser.getPortfolio();
	String groupID = thisUser.getGroupID();
	String userID = thisUser.getUserID();
	String name = thisUser.getRealName();

	
	//String portfolio = (String)session.getValue("portfolio");
	//String groupID = (String)session.getValue("groupID");
	//String userID = (String)session.getValue("user");
	//String name = (String)session.getValue("name");

	Boolean isAdmin = (Boolean)session.getAttribute("isAdmin");
	if (isAdmin == null) isAdmin = Boolean.FALSE;
	if (callMethod == null) callMethod = "x";
	if (name == null) name = "Anonymous Coward";
	if (portfolio == null) portfolio = "No Open Portfolio";
%>

<%@include file='actions.jsp' %>

<%= jlib.header(myJdot.TITLE, name, portfolio, (String)session.getValue("style")) %>

<TABLE>
<TR><TD width="25%" valign="TOP" class="botBox"> 

 
	<%@include file='sideBar.jsp' %>

</TD><TD width="100%" VALIGN="TOP">

	<%= pageContent.toString() %>

</TD></TR>
</TABLE>

</TD></TR></TABLE>


</BODY>
</HTML>
