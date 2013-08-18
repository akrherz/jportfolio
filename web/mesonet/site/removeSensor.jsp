
<font class="bluet">Remove Sensor from site</font>

<p>This form allows you to remove a sensor from a site.  This 
change will be denoted in the sensor's history.</p>

<%
  mSite mySite = new mSite(thisUser.getPortfolio(), id);
  String o_id = (String)request.getParameter("o_id");
%>

<form method="POST" action="/jportfolio/mesonet/database.jsp">
<input type="hidden" name="mode" value="f">
<input type="hidden" name="o_id" value="<%= o_id %>">

<p>When was this sensor removed?
<br><input type="text" name="removed" value="now">
<br><i>yyyy-mm-dd HH:MM</i>

<p>Submit Form:
<br><input type="submit" value="Remove Sensor">

</form>

