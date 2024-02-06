<%
 /**
  * addSensor.jsp
  *  - Add sensor to a site
  */
%>
<%@include file='../setup.jsp'%>
<%@include file='../include/header.jsp'%>


<font class="bluet">Add Sensor Group</font>

<p class="intro">This page adds a sensor group.  Think of this group 
as a sensor brand that is used in the network.  You will then create 
individual implementations of this sensor on your sites.  This entry will
not contain hardware IDs.</p>

<form method="POST" action="/jportfolio/mesonet/database.jsp">
<input type="hidden" value="d" name="mode">

<p>Name of Sensor Group:
<br><input type="text" name="r_name" size="40">

<p>In House ID of Sensor Group:
<br><input type="text" name="r_hid" size="40">

<p>Model of Sensor:
<br><input type="text" name="r_model" size="40">

<p>Vendor of Sensor:
<br><input type="text" name="r_vendor" size="40">

<p>Type of Sensor:
<br><select name="r_type">
 <option value="Temperature">Temperature
 <option value="Data Logger / RPU">Data Logger / RPU
</select>

<p><input type="submit" value="Create Sensor Group">

</form>

<%

ResultSet sensors = dbInterface.callDB("SELECT * from iem_sensors WHERE "
 +" portfolio = '"+ thisUser.getPortfolio() +"' ");

out.println("<p><font class='bluet'>Current Sensor Groups</font></p>");

out.println("<table>\n"
  +"<tr><th>Name:</th>\n"
  +"<th>Sensor Type:</th>\n"
  +"<th>Sensor Model:</th>\n"
  +"<th>Sensor Vendor:</th></tr>\n");

while ( sensors.next() ){
  out.println("<tr><td>"+ sensors.getString("r_name") +"</td>\n"
   +"<td>"+ sensors.getString("r_type") +"</td>\n"
   +"<td>"+ sensors.getString("r_model") +"</td>\n"
   +"<td>"+ sensors.getString("r_vendor") +"</td>\n"
   +"</tr>\n");

} // End of while

out.println("</table>\n");

%>

<%= jlib.footer() %>
