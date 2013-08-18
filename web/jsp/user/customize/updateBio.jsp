<%@ page import="org.collaborium.portfolio.*" %>
<%@ page import="org.collaborium.util.*" %>
<%@ page import="java.sql.*" %>
<%
	portfolioUser thisUser = (portfolioUser)session.getAttribute("User");

%>

<%= jlib.basicHeader(thisUser, "Update Bio Sketch") %>

<b>Personal Information Editor:</b> &nbsp; &nbsp;
  <a class="commands" href="editBio.jsp">Bio Sketch</a>  &nbsp; <b>|</b> &nbsp;
  <a class="commands" href="editPict.jsp">Picture</a>


<p><font class="title">Biographical Sketch</font></p>

<%
  String newBioSketch = (String)request.getParameter("biosketch");
  if (newBioSketch != null) {
	newBioSketch = stringUtils.cleanString( newBioSketch );
  	try{
	  dbInterface.updateDB("DELETE from biosketch WHERE username = '"+ thisUser.getUserID() +"' ");
	  dbInterface.updateDB("INSERT into biosketch(username, body) VALUES ('"+ thisUser.getUserID() +"' "
		+", '"+ newBioSketch +"' )");

  	} catch(Exception ex){
	  plogger.report("Problem in updateBio.jsp");

  	}

  }
%>

<P>Your new bio-sketch is now updated on your 
 <a href="/jportfolio/users/<%= thisUser.getUserID() %>">portfolio homepage</a>.

<p>Perhaps, you would like to <a class="commands" href="editPict.jsp">upload</a>
your photo now...

<P>Your updated bio-sketch:<br><hr>
<%= stringUtils.toBR(newBioSketch) %>

<%= jlib.basicFooter() %>
