<%
 /**
  * editSite.jsp
  *   - Edit site information
  */
%>
<%@include file='../setup.jsp'%>
<%@include file='../include/header.jsp'%>
<%@include file='links.jsp'%>

<p><font class="bluet">Edit Site Information</font>

<%
  if (s_mid == null || s_mid == "null") {
%>
<form method="GET" action="editSite.jsp">
<p><font color="red">Could not find a default site in your session.</font>
<br>Which site would you like to edit?<br>
<div style="padding-left: 20px;">
  <%= mLib.stationSelect(thisUser.getPortfolio() ) %>
</div>

<p><input type="submit" value="Edit Site">
</form>
<%
  } else {

  mSite site = new mSite(thisUser.getPortfolio(), s_mid);
%>

<div style="background: blue">
 <font color="white"><%= site.getName() %></font>
</div>


  <form method="POST" action="/jportfolio/mesonet/database.jsp">
  <input type="hidden" name="mode" value="c">
  <input type="hidden" name="id" value="<%= site.getID() %>">

<table width="100%">
<tr>
 <th align="left">IEM ID:</th>
 <td><%= site.getMID() %></td>
 <th align="left">Site Name:</th>
 <td><input type="text" name="s_name" value="<%= site.getName() %>"></td>
</tr>
<tr>
 <th align="left">In-House ID:</th>
 <td><input type="text" name="s_hid" value="<%= site.getHID() %>"></td>
 <th align="left">NWS ID:</th>
 <td><input type="text" name="s_nid" value="<%= site.getNID() %>"></td>
</tr>
<tr>
 <th align="left">Latitude:</th>
 <td><input size="8" type="text" name="s_lat" value="<%= site.getLat() %>"></td>
 <th align="left">Longitude:</th>
 <td><input size="8" type="text" name="s_lon" value="<%= site.getLon() %>"></td>
</tr>
<tr>
 <th align="left">State:</th>
 <td><input size="3" type="text" name="s_st" value="<%= site.getState() %>"></td>
 <th align="left">Elevation: [<i>meters</i>]</th>
 <td><input size="5" type="text" name="s_elev" value="<%= site.getElev() %>"></td>
</tr>
</table>
<p><input type="submit" value="Update Site">
 <input type="reset" value="Reset">
</form>

<p><font class="bluet">Site Contacts:</font>
<br> &nbsp; &nbsp; <a href="/jportfolio/mesonet/site/contacts.jsp">Add/Remove Contacts</a>
<br>
<%= mLib.listContacts(s_mid, thisUser.getPortfolio() ) %>

<p><font class="bluet">Sensor Inventory</font>
<br> &nbsp; &nbsp; <a href="/jportfolio/mesonet/sensor/addSensor.jsp">Add Sensor</a>

<%
  ResultSet rs = dbInterface.callDB("select h.installed, r.r_name, o.o_serial, o.id from "
   +" iem_sensor_history h, iem_sensor o, iem_sensors r, iem_sites s "
   +" WHERE h.portfolio = '"+ thisUser.getPortfolio() +"' and "
   +" h.o_id = o.id and o.r_id = r.id and h.s_id = s.id and "
   +" s.s_mid = '"+ s_mid +"' and h.removed IS null");

  out.println("<p><table width=\"100%\">\n"
   +"<tr><th>Serial</th><th>Name</th><th>Installed</th><td></td></tr>\n");

  if (rs != null) {
  while( rs.next() ){
    out.println("<tr><td>"+ rs.getString("o_serial") +"</td>\n"
     +"<td>"+ rs.getString("r_name") +"</td>\n"
     +"<td>"+ rs.getString("installed") +"</td>\n"
     +"<td><a href='/jportfolio/mesonet/main.jsp?mode=f&o_id="
     + rs.getString("id")+"'>Remove</a></tr>\n");
  } } // End of if
  out.println("</table>\n");
%>

<% } %>

<%= jlib.footer() %>
