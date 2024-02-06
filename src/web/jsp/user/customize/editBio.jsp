<%@ page import="org.collaborium.portfolio.*" %>
<%@ page import="java.sql.*" %>
<%
  portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
%>

<%= jlib.genHeader(thisUser, "Jportfolio | Edit Bio Sketch", "Ba") %>

<b>Personal Information Editor:</b> &nbsp; &nbsp;
  <font bgcolor="#EEEEEE">Bio Sketch</font>  &nbsp; <b>|</b> &nbsp;
  <a class="commands" href="editPict.jsp">Picture</a>

<h3>Biographical Sketch:</h3>

<p>Your biographical sketch is an important way for your classmates to 
get to know you better in the virtual environment.  

<p>Possible items to include:
<li>Where you are from</li>
<li>Your academic major</li>
<li>Reason for taking the course</li>
<li>Personal interests</li>
<li>Photo</li>
<li>Personal website</li>

<p>Please notice that when you post an entry in the dialog, your name will be highlighted, which allows classmates to connect to your bio.   Also notice that you can change your bio during the semester if you choose.

<P>With this dialog, you can edit the Bio-Sketch that appears on your
 <a href="/jportfolio/users/<%= thisUser.getUserID() %>">portfolio homepage</a>.

<%
  String prevBio = "";
  try{
    ResultSet rs = dbInterface.callDB("SELECT body from biosketch WHERE "
	+" username = '"+ thisUser.getUserID() +"' ");
    if (rs.next() )
      prevBio = rs.getString("body");
  } catch(Exception ex){
    plogger.report("Problem Getting Bio");
  }
%>

<FORM METHOD="POST" ACTION="updateBio.jsp">

<CENTER>
<b>Enter your bio-sketch.  (HTML is okay)</b>
<br /><TEXTAREA NAME="biosketch" wrap="Virtual" cols="60" rows="20"><%= prevBio %></TEXTAREA>

<P><INPUT TYPE="SUBMIT" value="Submit Bio Sketch">
</CENTER>

</FORM>

<%= jlib.basicFooter() %>
