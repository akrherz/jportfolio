<%@include file='setup.jsp'%>

<%
/* logout.jsp
 *	Does what the name implies
 *	Daryl Herzmann 3 Sept 2001
 */

if ( thisUser != null ) {
	session.invalidate();
	jlib.deleteUser( thisUser.getUserID() );
}
%>

<h3>________</h3>

<% response.setHeader("Refresh", "0; URL=index.jsp" ); %>
