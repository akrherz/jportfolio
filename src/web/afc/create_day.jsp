<%@ include file='setup.jsp'%>
<%= jlib.genHeader(thisUser, "JPortfolio | AFC" ,  "Manager") %>
<%@ include file='auth.jsp'%>
<%
  String day = (String)request.getParameter("day");
  String fxsiteid = (String)request.getParameter("fxsiteid");
  String fxsitename = (String)request.getParameter("fxsitename");
  String period = (String)request.getParameter("period");
  if (day != null && fxsiteid != null & fxsitename != null){
    dbInterface.updateDB("INSERT into afc_days( portfolio, day, fxsiteid, "
     +" fxsitename, period) VALUES ('"+ thisUser.getPortfolio() +"', "
     +" '"+ day +"', '"+ fxsiteid +"', '"+ fxsitename +"', '"+ period +"')");
  out.println("<p>Forecast Date Created: "+ day );
  }
%>

<p><b>Previous Forecast Dates:</b><br>
<table>
<%
 ResultSet days = dbInterface.callDB("SELECT * from afc_days "
  +"WHERE portfolio = '"+ thisUser.getPortfolio() +"' ORDER by day DESC");
 if (days != null) {
  while (days.next()){
   out.println("<tr><td>"+ days.getString("day") +"</td><td>"+ days.getString("fxsitename") +"("+ days.getString("fxsiteid") +")</td></tr>");
  }
 }
%>
</table>

<p>You are creating a forecast day.  You should enter the day which this
forecast is for.  Typically, this is tomorrow.

<form method="POST" action="create_day.jsp">
<p>Enter Date: (Format: YYYY-MM-DD) <input type="text" name="day" size="10">
<p>Enter Site ID: <input type="text" name="fxsiteid" size="5">
<p>Enter Site Name: <input type="text" name="fxsitename" size="20">
<p>Enter Period: (Format: <i>Number</i>) <input type="text" name="period" size="2" maxsize="2">
<p><input type="submit" value="Create Forecast Day">
</form>

</body>
</html>
