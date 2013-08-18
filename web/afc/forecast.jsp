<%@include file='setup.jsp' %>
<%@include file='auth.jsp' %>
<html>
<head>
  <title>Enter Your AFC Forecast</title>
</head>
<body bgcolor="white">

<h3>AMS Forecasting Contest</h3>
<a href="index.jsp">AFC Home</a><br>
<%
  ResultSet lastf = dbInterface.callDB("SELECT * from afc_forecasts WHERE "
   +" username = '"+ thisUser.getUserID() +"' and "
   +" portfolio = '"+ thisUser.getPortfolio() +"' and "
   +" day = 'TOMORROW'::date ");
  String lo_t = "";
  String hi_t = "";
  String pop00 = "0";
  String pop12 = "0";
  String qpf00 = "0";
  String qpf12 = "0";
  String qsf00 = "0";
  String qsf12 = "0";
  String wind14 = "0";
  String ceil02 = "0";
  String vis02 = "0";
  while (lastf.next() ) {
    lo_t = lastf.getString("low");
    hi_t = lastf.getString("high");
    pop00 = lastf.getString("pop00");
    pop12 = lastf.getString("pop12");
    qpf00 = lastf.getString("qpf00");
    qpf12 = lastf.getString("qpf12");
    qsf00 = lastf.getString("qsf00");
    qsf12 = lastf.getString("qsf12");
    wind14 = lastf.getString("wind14");
    ceil02 = lastf.getString("ceil02");
    vis02 = lastf.getString("vis02");
  } 
  ResultSet rs = dbInterface.callDB("SELECT * from afc_days WHERE "
   +" day = 'TOMORROW'::date and portfolio = '"+ thisUser.getPortfolio() +"' ");
  if (rs.next()){
%>
<form method="POST" action="post_forecast.jsp">

<p>You are forecasting for:
<br><table>
 <tr><td>Date:</td><td><%= rs.getString("day") %></td></tr>
 <tr><td>Site:</td><td><%= rs.getString("fxsitename") %></td></tr>
</table>

<!--
<input type="hidden" name="lo_t" value="0">
<input type="hidden" name="hi_t" value="0">
-->
<p><b>Enter 01Z-14Z minimum temperature:</b>
 <input type="text" maxlength=3 size=5 name="lo_t" value="<%= lo_t %>">

<p><b>Enter 13Z-01Z maximum temperature:</b>
 <input type="text" maxlength=3 size=5 name="hi_t" value="<%= hi_t %>">

<!--
<input type="hidden" name="qpf00" value="0">
<input type="hidden" name="qpf12" value="0">
-->
<input type="hidden" name="qsf00" value="0">
<input type="hidden" name="qsf12" value="0">

<p><b>00-12Z QPF:</b>
<br><select name="qpf00">
 <option value="0" <% if (qpf00.equals("0")) out.print("SELECTED"); %>>CAT 0: 0-Trace
 <option value="1" <% if (qpf00.equals("1")) out.print("SELECTED"); %>>CAT 1: 0.01-0.09"
 <option value="2" <% if (qpf00.equals("2")) out.print("SELECTED"); %>>CAT 2: 0.10-0.24"
 <option value="3" <% if (qpf00.equals("3")) out.print("SELECTED"); %>>CAT 3: 0.25-0.49"
 <option value="4" <% if (qpf00.equals("4")) out.print("SELECTED"); %>>CAT 4: 0.50-0.99"
 <option value="5" <% if (qpf00.equals("5")) out.print("SELECTED"); %>>CAT 5: 1.00-1.99"
 <option value="6" <% if (qpf00.equals("6")) out.print("SELECTED"); %>>CAT 6: 2.00"+
</select>

<!--
<p><b>00-12Z QSF:</b>
<br><select name="qsf00">
 <option value="0" <% //if (qsf00.equals("0")) out.print("SELECTED"); %>>CAT 0: 0-Trace
 <option value="1" <% //if (qsf00.equals("1")) out.print("SELECTED"); %>>CAT 1: 0.1-1.9"
 <option value="2" <% //if (qsf00.equals("2")) out.print("SELECTED"); %>>CAT 2: 2.0-3.9"
 <option value="4" <% //if (qsf00.equals("4")) out.print("SELECTED"); %>>CAT 4: 4.0-5.9"
 <option value="5" <% //if (qsf00.equals("5")) out.print("SELECTED"); %>>CAT 6: 6.0-7.9"
 <option value="6" <% //if (qsf00.equals("6")) out.print("SELECTED"); %>>CAT 8: 8.0"+
</select>
-->

<p><b>12-00Z QPF:</b>
<br><select name="qpf12">
 <option value="0" <% if (qpf12.equals("0")) out.print("SELECTED"); %>>CAT 0: 0-Trace
 <option value="1" <% if (qpf12.equals("1")) out.print("SELECTED"); %>>CAT 1: 0.01-0.09"
 <option value="2" <% if (qpf12.equals("2")) out.print("SELECTED"); %>>CAT 2: 0.10-0.24"
 <option value="3" <% if (qpf12.equals("3")) out.print("SELECTED"); %>>CAT 3: 0.25-0.49"
 <option value="4" <% if (qpf12.equals("4")) out.print("SELECTED"); %>>CAT 4: 0.50-0.99"
 <option value="5" <% if (qpf12.equals("5")) out.print("SELECTED"); %>>CAT 5: 1.00-1.99"
 <option value="6" <% if (qpf12.equals("6")) out.print("SELECTED"); %>>CAT 6: 2.00"+
</select>

<!--
<p><b>12-00Z QSF:</b>
<br><select name="qsf12">
 <option value="0" <% //if (qsf12.equals("0")) out.print("SELECTED"); %>>CAT 0: 0-Trace
 <option value="1" <% //if (qsf12.equals("1")) out.print("SELECTED"); %>>CAT 1: 0.1-1.9"
 <option value="2" <% //if (qsf12.equals("2")) out.print("SELECTED"); %>>CAT 2: 2.0-3.9"
 <option value="4" <% //if (qsf12.equals("4")) out.print("SELECTED"); %>>CAT 4: 4.0-5.9"
 <option value="5" <% //if (qsf12.equals("5")) out.print("SELECTED"); %>>CAT 6: 6.0-7.9"
 <option value="6" <% //if (qsf12.equals("6")) out.print("SELECTED"); %>>CAT 8: 8.0"+
</select>
-->

<!--
<input type="hidden" name="pop00" value="0">
<input type="hidden" name="pop12" value="0">
-->

<p><b>POP: (00-12Z)</b>
<br><select name="pop00">
 <option value="0" <% if (pop00.equals("0")) out.print("SELECTED"); %>>CAT 0: 0-4%
 <option value="1" <% if (pop00.equals("1")) out.print("SELECTED"); %>>CAT 1: 5-14%
 <option value="2" <% if (pop00.equals("2")) out.print("SELECTED"); %>>CAT 2: 15-24%
 <option value="3" <% if (pop00.equals("3")) out.print("SELECTED"); %>>CAT 3: 25-34%
 <option value="4" <% if (pop00.equals("4")) out.print("SELECTED"); %>>CAT 4: 35-44%
 <option value="5" <% if (pop00.equals("5")) out.print("SELECTED"); %>>CAT 5: 45-54%
 <option value="6" <% if (pop00.equals("6")) out.print("SELECTED"); %>>CAT 6: 55-64%
 <option value="7" <% if (pop00.equals("7")) out.print("SELECTED"); %>>CAT 7: 65-74%
 <option value="8" <% if (pop00.equals("8")) out.print("SELECTED"); %>>CAT 8: 75-84%
 <option value="9" <% if (pop00.equals("9")) out.print("SELECTED"); %>>CAT 9: 85-94%
 <option value="10" <% if (pop00.equals("10")) out.print("SELECTED"); %>>CAT 10: 95-100%
</select>

<p><b>POP: (12-00Z)</b>
<br><select name="pop12">
 <option value="0" <% if (pop12.equals("0")) out.print("SELECTED"); %>>CAT 0: 0-4%
 <option value="1" <% if (pop12.equals("1")) out.print("SELECTED"); %>>CAT 1: 5-14%
 <option value="2" <% if (pop12.equals("2")) out.print("SELECTED"); %>>CAT 2: 15-24%
 <option value="3" <% if (pop12.equals("3")) out.print("SELECTED"); %>>CAT 3: 25-34%
 <option value="4" <% if (pop12.equals("4")) out.print("SELECTED"); %>>CAT 4: 35-44%
 <option value="5" <% if (pop12.equals("5")) out.print("SELECTED"); %>>CAT 5: 45-54%
 <option value="6" <% if (pop12.equals("6")) out.print("SELECTED"); %>>CAT 6: 55-64%
 <option value="7" <% if (pop12.equals("7")) out.print("SELECTED"); %>>CAT 7: 65-74%
 <option value="8" <% if (pop12.equals("8")) out.print("SELECTED"); %>>CAT 8: 75-84%
 <option value="9" <% if (pop12.equals("9")) out.print("SELECTED"); %>>CAT 9: 85-94%
 <option value="10" <% if (pop12.equals("10")) out.print("SELECTED"); %>>CAT 10: 95-100%
</select>

<input type="hidden" name="wind14" value="0">
<input type="hidden" name="ceil02" value="0">
<input type="hidden" name="vis02" value="0">

<!--
<p><b>Average Wind Speed: (14-17z)</b>
<br /><select name="wind14">
 <option value="1" <% if (wind14.equals("1")) out.print("SELECTED"); %>>CAT 1: 0.0 - 3.4 kts
 <option value="2" <% if (wind14.equals("2")) out.print("SELECTED"); %>>CAT 2: 3.5 - 7.7 kts
 <option value="3" <% if (wind14.equals("3")) out.print("SELECTED"); %>>CAT 3: 7.5 - 11.4 kts
 <option value="4" <% if (wind14.equals("4")) out.print("SELECTED"); %>>CAT 4: 11.5 - 15.4 kts
 <option value="5" <% if (wind14.equals("5")) out.print("SELECTED"); %>>CAT 5: 15.5 - 18.9 kts
 <option value="6" <% if (wind14.equals("6")) out.print("SELECTED"); %>>CAT 6: 19.0 - 22.4 kts
 <option value="7" <% if (wind14.equals("7")) out.print("SELECTED"); %>>CAT 7: 22.5 - 25.9 kts
 <option value="8" <% if (wind14.equals("8")) out.print("SELECTED"); %>>CAT 8: >= 26.0 kts
</select>

<p><b>Lowest Reporting Ceiling (02-17z)</b>
<br /><select name="ceil02">
 <option value="1" <% if (ceil02.equals("1")) out.print("SELECTED"); %>>CAT 1: < 200 feet
 <option value="2" <% if (ceil02.equals("2")) out.print("SELECTED"); %>>CAT 2: 200 to 400 feet
 <option value="3" <% if (ceil02.equals("3")) out.print("SELECTED"); %>>CAT 3: 500 to 900 feet
 <option value="4" <% if (ceil02.equals("4")) out.print("SELECTED"); %>>CAT 4: 1000 to 1900 feet
 <option value="5" <% if (ceil02.equals("5")) out.print("SELECTED"); %>>CAT 5: 2000 to 3000 feet
 <option value="6" <% if (ceil02.equals("6")) out.print("SELECTED"); %>>CAT 6: 3100 to 6500 feet
 <option value="7" <% if (ceil02.equals("7")) out.print("SELECTED"); %>>CAT 7: 6600 to 12000 feet
 <option value="8" <% if (ceil02.equals("8")) out.print("SELECTED"); %>>CAT 8: >= 12000 feet or no ceiling
</select>

<p><b>Lowest Reported Visibility (02-17z)</b>
<br /><select name="vis02">
 <option value="1" <% if (vis02.equals("1")) out.print("SELECTED"); %>>CAT 1: < 0.5 miles 
 <option value="2" <% if (vis02.equals("2")) out.print("SELECTED"); %>>CAT 2: 0.5 < 0.9 miles 
 <option value="3" <% if (vis02.equals("3")) out.print("SELECTED"); %>>CAT 3: 1.0 < 1.9 miles 
 <option value="4" <% if (vis02.equals("4")) out.print("SELECTED"); %>>CAT 4: 2.0 < 2.9 miles 
 <option value="5" <% if (vis02.equals("5")) out.print("SELECTED"); %>>CAT 5: 3.0 < 5.4 miles 
 <option value="6" <% if (vis02.equals("6")) out.print("SELECTED"); %>>CAT 6: 5.5 < 6.4 miles 
 <option value="7" <% if (vis02.equals("7")) out.print("SELECTED"); %>>CAT 7: >= 6.5 miles 
</select>
-->

<p><input type="submit" value="Submit Forecast">

</form>
<% } else { %>
 <p>Currently, there aren't any forecasts available
<% } %> 
</body>
</html>
