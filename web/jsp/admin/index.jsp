<%@ page import="org.collaborium.portfolio.*" %>

<% 

portfolioUser thisUser = (portfolioUser)session.getAttribute("User");

if (! thisUser.isAdmin() ) {
%>
	<jsp:forward page="<%= jlib.httpBase %>" />
<%
}
%>

