<table width="100%">
<tr><td valign="top">

<div id="portfolio-side">

<% if ( thisUser.isAdmin() ){ %>
<%= jlib.adminCommands() %>
<% } %>


<%= jlib.topBox("Aalborg Links") %>
<ul>
  <li><a href="/jportfolio/gccourse/">ISU GC Portfolio</a></li>
  <li><a href="classMates.jsp">My Classmates</a></li>
  <li><a href="groupMates.jsp">My Group Members</a></li>
</ul>
<%= jlib.botBox() %>

<p><%= jlib.currentUsers( thisUser.getPortfolio(), thisUser.getUserID() ) %>


<p><%= jlib.topBox("Portfolio Contacts") %>

<p><strong>Elsebeth Sorensen</strong>
<br /><a HREF="mailto:eks@hum.aau.dk">eks@hum.aau.dk</a>

<p><strong>Eugene S. Takle</strong>
<br /><a HREF="mailto:gstakle@iastate.edu">gstakle@iastate.edu</a>

<p><strong>Daryl Herzmann</strong>
<br /><a HREF="mailto:akrherz@iastate.edu">akrherz@iastate.edu</a>

<%= jlib.botBox() %>

</div>

</td><td>

<p><b><a href="/jportfolio/jsp/user/printMotd.jsp?idnum=473">Details on Friday's Meeting</a></b><br />

<%= jlib.topBox("Message of the Day:") %>
<%@include file='motd.jsp'%>
<%= jlib.botBox() %>


<p><h4>Web Evaluations:</h4>
<ul>
 <li><a
 href="/jportfolio/jsp/user/customize/editBio.jsp">Edit your Bio-Sketch</a></li>
 <li><a href="syllabus.jsp">Course Syllabus</a></li>
 <li><a href="block/eval1.jsp">Evaluation 1</a></li>
 <li><a href="block/eval2.jsp">Evaluation 2</a></li>
 <li><a href="block/eval3.jsp">Evaluation 3</a></li>
</ul>

<%= jlib.printMessages( thisUser ) %>

</td></tr></table>
