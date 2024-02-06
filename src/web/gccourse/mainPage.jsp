
<TABLE width="100%">
<tr><td valign="top" style="width: 25%;">
<% if ( thisUser.isAdmin() ){ %>
<%= jlib.adminCommands() %>
<% } %>

<p><%= jlib.topBox("Portfolio Links:") %>
<ul>
 <li><a href="classMates.jsp">My Classmates</a></li>
</ul>

<%= jlib.botBox() %>


<p><%= jlib.currentUsers( thisUser.getPortfolio(), thisUser.getUserID() ) %>

<p><%= jlib.topBox("Portfolio Contacts:") %>

<table cellpadding="1" cellspacing="0" style="width: 100%">
<tr bgcolor="#EEEEEE">
  <td colspan=2><b>Instructor:</b></td> </tr>

<tr>
   <td colspan=2><a
     href="http://www.meteor.iastate.edu/faculty/takle/">Dr. Eugene S. Takle</a></td></tr>

<tr>
  <td>Email:</td>
  <td><a
   HREF="mailto:gstakle@iastate.edu">gstakle@iastate.edu</A></td>
</tr>
<tr>
  <td>Office:</td>
  <td>3013 Agronomy</td>
</tr>
<tr>
  <td>Hours:</td>
  <td>By appointment</td>
</tr>

<tr bgcolor="#EEEEEE">
  <td colspan=2><b>Technical Assistant:</b></td></tr>

<tr><td colspan=2>Daryl Herzmann</td>
</tr>
<tr>
  <td>Email:</td> 
  <td><A class="commands" 
    HREF="mailto:akrherz@iastate.edu">akrherz@iastate.edu</A></td>
</tr>
<tr>
  <td>Office:</td> 
  <td>3015 Agronomy</td>
</tr>
<tr>
  <td>Hours:</td> 
  <td>Just stop by.</td>
</tr>
</table>


<%= jlib.botBox() %>

</td><td valign="top" style="width: 75%;">

<%@include file='motd.jsp'%>


<p><font class="title">Required Reading:</font><br>
<ul>
  <li><a href="/gccourse/oncampus.html#block1">Block 1:  Climate and Agents of Global Change</a></li>
  <li><a href="/gccourse/oncampus.html#block2">Block 2:  Models and Measurements of Global Change</a></li>
  <li><a href="/gccourse/oncampus.html#block3">Block 3:  Biosphere and Human Component of Global Change</a></li>
</ul>

<p><font class="title">My Assignment Book:</font><br>
<ul>
<!--
 <li><a class="commands" href="/jportfolio/jsp/user/dialogMatrix.jsp">Dialog Requirements Statistics</a></li>
-->
  <li><a class="commands" href="/jportfolio/jsp/user/customize/editBio.jsp">Enter Bio-Sketch</a></li>
  <li><a class="commands" href="block/index.jsp?blockid=1">Block 1</a></li>
  <li><a class="commands" href="block/index.jsp?blockid=2">Block 2</a></li>
  <li><a class="commands" href="block/index.jsp?blockid=3">Block 3</a></li>
</ul>


<%= jlib.printMessages( thisUser ) %>

</td></tr></table>
