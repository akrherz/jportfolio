<!-- index.jsp -->
<%@include file='../setup.jsp'%>
<%@ page import="org.collaborium.portfolio.jdot.*" %>
<%= jlib.genHeader(thisUser, "JPortfolio | ICT and Learning",  "Dialog") %>
<%
	StringBuffer pageContent = new StringBuffer();
	String thisPageURL = "/jportfolio/aalborg/dialog/index.jsp";
%>

<%@include file='variables.jsp' %>

<%
	if (thisUser == null) {
		response.setHeader("Refresh", "0; URL=/jportfolio/aalborg/index.jsp");
	}

	if (callMethod == null) callMethod = "x";
	
%>

<%@include file='actions.jsp' %>

<TABLE>
<TR><TD width="25%" valign="TOP"> 
<div id="portfolio-side">
 
	<%@include file='sideBar.jsp' %>

</div>
</TD><TD width="100%" VALIGN="TOP">

	<%= pageContent.toString() %>

</TD></TR>
</TABLE>

</TD></TR></TABLE>


</BODY>
</HTML>
