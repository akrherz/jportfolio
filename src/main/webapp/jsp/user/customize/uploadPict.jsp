<%@ page import="org.collaborium.portfolio.*" %>
<%@ page import="java.io.File" %>

<%
		portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
%>

<%= jlib.basicHeader(thisUser, "Upload Picture") %>

<b>Personal Information Editor:</b> &nbsp; &nbsp;
	<a class="commands" href="editBio.jsp">Bio Sketch</a>  &nbsp; <b>|</b> &nbsp;
	<a class="commands" href="editPict.jsp">Picture</a>

<form method="post" action="/jportfolio/uploadServlet" enctype="multipart/form-data">
	<input type="file" name="file" accept="image/gif" />
	<button type="submit">Upload</button>
</form>

<P>Upload Finished:

<BR>Here is what you supposedly look like.

<BR><img src="/jportfolio/FILES/<%= thisUser.getUserID() %>/me.gif">

<P>Return to your portfolio <a href="/jportfolio/users/<%= thisUser.getUserID() %>">homepage</a>


<%= jlib.basicFooter() %>
