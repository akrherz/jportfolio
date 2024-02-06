<%@ page import="org.collaborium.portfolio.*" %>
<%
	portfolioUser thisUser = (portfolioUser)session.getAttribute("User");

%>

<%= jlib.genHeader(thisUser, "Edit Picture", "ba") %>

<b>Personal Information Editor:</b> &nbsp; &nbsp;
  <a class="commands" href="editBio.jsp">Bio Sketch</a>  &nbsp; <b>|</b> &nbsp;
  <font bgcolor="#EEEEEE">Picture</font>


<p><font class="title">Upload your picture:</font></p>

<P>With this dialog, you can upload a picture of yourself to be included on 
your <a href="/jportfolio/users/<%= thisUser.getUserID() %>">portfolio homepage</a>.

<P>Please note that only gifs are supported at this time.

<FORM METHOD="POST" ACTION="uploadPict.jsp" ENCTYPE="multipart/form-data">
<input type="hidden" name="mode" value="u">

<P>Upload File:<INPUT TYPE="FILE" NAME="myFile">

<P><INPUT TYPE="SUBMIT" value="Upload Photo">

</FORM>

<%= jlib.basicFooter() %>
