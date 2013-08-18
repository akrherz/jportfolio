<%@include file='setup.jsp'%>
<%@include file='include/header.jsp'%>

<table width=100%> 
  <tr><td width=150 valign="top">

<table>

<tr><th>Network Options</th></tr>
<tr>
  <td>
   <a href="network/listSites.jsp">List Sites</a>
   <br><a href="network/addSite.jsp">Add Site</a>
   <br><a href="network/listSensors.jsp">List Sensors</a>
  </td>
</tr>


  <tr><th>Site Options</th></tr>
  <tr>
    <td>

<% if (s_mid != null) { %>

     <a href="site/editSite.jsp">Edit Site</a>
     <br><a href="site/contacts.jsp">Edit Contacts</a>

<% } else { %>

   <i>No Site Defined</i>

<% } %>

  </td></tr>

<tr><th>Calibrations</th></tr>
<tr><td>
 <a href="addcal.jsp">Add Event</a>
</td></tr>

<!--
<tr><th>Sensor Options</th></tr>
<tr>
  <td>
  <a href="sensor/addSensor.jsp">Add Sensor Group</a>
  <br><a href="sensor/editSensor.jsp">Edit Sensor</a>
  </td>
</tr>
-->

<tr><th>Trouble Tickets</th></tr>
<tr>
  <td>
  <a href="tt/create.jsp">Create a TT</a>
  <br><a href="tt/list.jsp">List TT</a>
  </td>
</tr>

<tr><td>
<%= jlib.currentUsers( thisUser.getPortfolio(), thisUser.getUserID() ) %>
</td></tr>


</table>



  </td><td width=500 valign="top">

<div class="ptitle"><%= thisUser.myPortfolio.getName() %> Network</div>

<p class="story">Welcome!  You have successfully logged in and are ready to 
begin work on your network.  Listed on the side are options available.  Listed
at the top are links to various sections in this application.</p>


  </td></tr></table>
<%= jlib.footer() %>
