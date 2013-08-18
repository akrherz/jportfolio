
<p><font class="bluet">Add Sensor to Site</font></p>

<p class="info">This form allows you to add a sensor to a site.
You should specify the install date of the sensor if possible.</p>

<%
  rs = dbInterface.callDB("SELECT * from iem_sensors WHERE "
   +" portfolio = '"+thisUser.getPortfolio() +"' ");
  sbuf = new StringBuffer();
  sbuf.append("<option value='null'>None\n");
  while ( rs.next() ){
   sbuf.append("<option value='"+ rs.getString("id") +"'>"
     + rs.getString("r_name") +"\n");
  } // End of while

%>

<form method="POST" action="/jportfolio/mesonet/database.jsp">
<input type="hidden" name="mode" value="e">

<table>
<tr>
  <th>Sensor Type:</th>
  <td>
  <select name="s1_type">
   <%= sbuf.toString() %>
  </select>
  </td>
</tr>

<tr>
  <th>UNIQUE ID:</th>
  <td><input type="text" name="s1_id" size="20"></td>
</tr>

<tr>
  <th>Installed:</th>
  <td><input type="text" name="s1_install" size="20"><br>
  yyyy-mm-dd HH:MM <i>(Leave blank for default of now)</i></td>
</tr>

<tr>
  <td colspan=2><input type="submit" value="Add Sensor"></td>
</tr>
</table>

</form>

<p><b>Current Sensors:</b>
<%
  rs = dbInterface.callDB("select h.installed, r.r_name, o.o_serial from "
   +" iem_sensor_history h, iem_sensor o, iem_sensors r, iem_sites s "
   +" WHERE h.portfolio = '"+ thisUser.getPortfolio() +"' and "
   +" h.o_id = o.id and o.r_id = r.id and h.s_id = s.id and "
   +" s.id = "+ id +" ");

  out.println("<p><table>\n"
   +"<tr><th>Serial</th><th>Name</th><th>Installed</th></tr>\n");

  while( rs.next() ){
    out.println("<tr><td>"+ rs.getString("o_serial") +"</td>\n"
     +"<td>"+ rs.getString("r_name") +"</td>\n"
     +"<td>"+ rs.getString("installed") +"</td>\n</tr>\n");
  }
  out.println("</table>\n");

  if (! rs.previous() ){
    out.println("<p>No sensors are defined for this site.\n");
  }
%>



