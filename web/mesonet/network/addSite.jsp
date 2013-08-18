<%
 /**
  * addSite.jsp
  *  - Add a site to the mesonet
  */
%>
<%@include file='../setup.jsp'%>
<%@include file='../include/header.jsp'%>


<font class="bluet">Add Site</font>

<p class="intro">You have selected to add a site to this network.  Please
fill out the following form and submit the values when finished.</p>

<form method="POST" action="/jportfolio/mesonet/database.jsp">
<input type="hidden" name="mode" value="a">

<table>
<tr>
 <th colspan=3>Site Name:</th>
 <td colspan=3><input type="text" name="s_name" size="40">
 <br> <font style="text-color: brown;">ex: Ames (I-35/13th St)</font>
 </td>
</tr>

<tr>
 <th bgcolor="#ffe4e1" colspan="6">Identifiers - Leave blank if you are unsure.</th>
</tr>

<tr>
  <th>In-house ID:</th>
  <td><input type="text" name="s_hid" size="10">
  <br> <font class="ex">ex: KCCI42</font>
  </td>
  
  <th>NWS ID:</th>
  <td><input type="text" name="s_nid" size=10">
  <br> <font class="ex">ex: WMTI4</font>
  </td>
  
  <th>Mesonet ID:</th>
  <td><input type="text" name="s_mid" size="10">
  <br> <font class="ex">ex: RWIN</font>
  </td>

</tr>

<tr>
  <th bgcolor="#ffe4e1" colspan="6">Location</th>
</tr>

<tr>
  <th>Latitude:</th>
  <td><input type="text" name="s_lat" size="10">
  <br><font class="ex">[DD] ex: 42.01</font></td>
  <th>Longitude:</th>
  <td><input type="text" name="s_lon" size="10"></td>
  <th>State:</th>
  <td><select name="s_st">
    <option value="IA">Iowa
    <option value="MN">Minnesota
    <option value="WI">Wisconsin
    <option value="IL">Illinios
    <option value="MO">Missouri
    <option value="NE">Nebraska
    <option value="SD">South Dakota
    <option value="ND">North Dakota
  </select></td>
</tr>

<tr>
  <td></td><td></td>
  <th>Elevation:</th>
  <td><input type="text" name="s_elev" size="10">
      <br>[meters] ex: 342
   </td>
  <td></td><td></td>
</tr>

<tr>
  <th bgcolor="#ffe4e1" colspan="6">Sensors</th>
</tr>

<%
  ResultSet rs = dbInterface.callDB("SELECT * from iem_sensors WHERE "
   +" portfolio = '"+thisUser.getPortfolio() +"' ");
  StringBuffer sbuf = new StringBuffer();
  sbuf.append("<option value='null'>None\n");
  while ( rs.next() ){
   sbuf.append("<option value='"+ rs.getString("id") +"'>"
     + rs.getString("r_name") +"\n");
  } // End of while

%>



<tr>
  <td colspan="6">
  Sensor 1:<br>
  <select name="s1_type">
   <%= sbuf.toString() %>
  </select>
  <br>UNIQUE ID:<input type="text" name="s1_id" size="20">
  <br>Installed:<input type="text" name="s1_install" size="20">
  yyyy-mm-dd HH:MM <i>(Leave blank for default of now)</i>
  </td>
</tr>

<tr bgcolor="#eeeeee">
  <td colspan="6">
  Sensor 2:<br>
  <select name="s2_type">
   <%= sbuf.toString() %>
  </select>
  <br>UNIQUE ID:<input type="text" name="s2_id" size="20">
  <br>Installed:<input type="text" name="s2_install" size="20">
  yyyy-mm-dd HH:MM <i>(Leave blank for default of now)</i>
  </td>
</tr>

<tr>
  <td colspan="6">
  Sensor 3:<br>
  <select name="s3_type">
   <%= sbuf.toString() %>
  </select>
  <br>UNIQUE ID:<input type="text" name="s3_id" size="20">
  <br>Installed:<input type="text" name="s3_install" size="20">
  yyyy-mm-dd HH:MM <i>(Leave blank for default of now)</i>
  </td>
</tr>


<tr>
  <th colspan=6><input type="submit" value="Create Site">
  <input type="reset"></th>
</tr>

</table>

</form>

<p><b>Note:</b>  You can add/edit more information at a later time.  This 
form is meant to get something in the database to manipulate.

<%= jlib.footer() %>
