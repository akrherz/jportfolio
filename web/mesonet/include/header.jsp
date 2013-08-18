
<div id="banner">

<a href="/jportfolio/mesonet/main.jsp">Home</a> 
  &nbsp; <b>|</b> &nbsp; 
<a href="/jportfolio/mesonet/dialog/">Dialog</a> 
  &nbsp; <b>|</b> &nbsp;
<a href="/jportfolio/mesonet/tt/">Trouble Tickets</a> 
  &nbsp; <b>|</b> &nbsp;
<a href="/jportfolio/login.jsp?logout=yes">Logout</a>

<h3>IEM Tracker</h3>

<table width="100%" cellpadding=1 cellspacing=0 border=0>  

<tr><th>Auth:</th>
 <td>
  <%
  if (thisUser != null){
    out.print( thisUser.getRealName() );
  } else{
    out.print("&lt;NA&gt;");
  } %>
</td>

<th>Network:</th>
 <td>
  <%
   if (thisUser != null && thisUser.getPortfolio() != null){
     out.println( thisUser.myPortfolio.getName() +" &nbsp; "
     +" <a href=\"/jportfolio/login.jsp?logoutPortfolio=yes\">Change</a>");
  } else {
     out.println("&lt;NA&gt;");
  }
  %>
</td>

<th>Station:</th>
  <td><%
   if (s_mid != null){
  %>
   <font style="font-weight: bold; font-size: 12pt">Station:</font>
    <%= sname %> &nbsp; 
   <a href="/jportfolio/mesonet/main.jsp?mode=b">Change</a>
  <%  } else if ( thisUser != null ) { %>

  <form name="stationSelect" method="post" action="/jportfolio/mesonet/main.jsp">
  <%=  mLib.stationSelect( thisUser.getPortfolio() ) %>
  <input type="submit" value="Set Station">
  </form>
  <% } else {
    out.println("&lt;NA&gt;");
  } %>
</td></tr>
</table>

</div>
