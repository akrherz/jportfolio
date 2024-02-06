<!-- index.jsp -->
<%@include file='../setup.jsp'%>
<%@ page import="org.collaborium.portfolio.jdot.*" %>
<%= jlib.genHeader(thisUser, "JPortfolio | Global Change Course",  "Dialog") %>
<%
	StringBuffer pageContent = new StringBuffer();
	String thisPageURL = "/jportfolio/gccourse/dialog/index.jsp";
%>

<%@include file='variables.jsp' %>

<%
	if (thisUser == null) {
		response.setHeader("Refresh", "0; URL=/jportfolio/gccourse/index.jsp");
	}

	if (callMethod == null) callMethod = "x";
	
%>

<%@include file='actions.jsp' %>

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
