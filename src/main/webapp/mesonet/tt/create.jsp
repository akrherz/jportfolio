<%
 /**
  * Create a mesonet Trouble Ticket!
  *  Daryl Herzmann 18 May 2002
  */
%>

<%@include file='../setup.jsp'%>
<%@include file='../include/header.jsp'%>
<%@include file='links.jsp'%>

<font class="bluet">Create a Trouble Ticket</font>

<form method="POST" action="create_post.jsp">

<p>Select a Site:<br>
<%= mLib.stationSelect( thisUser.getPortfolio() ) %>

<p>Flag sensors:<br />
<input type="text" name="sensor">

<p>Enter subject of this trouble ticket<br>
<input type="text" name="subject">

<p>Enter a more verbose description of the problem.<br>
<textarea name="comments" wrap="Virtual" rows=5 cols=60></textarea>

<p>Email Site Contacts with your change?<br>
<input type="radio" name="email" value="yes">Yes
<br><input type="radio" name="email" value="no" CHECKED>No

<p>Submit Ticket:<br>
<input type="submit" value="Create Ticket">

</form>

<%= jlib.footer() %>
