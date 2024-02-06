<%@include file='setup.jsp'%>
<%
 /* Handle posting! */
String sid = (String)request.getParameter("s_mid");
if (sid != null){
  String para = (String)request.getParameter("parameter");
  String valid = (String)request.getParameter("valid");
  String adjustment = (String)request.getParameter("adjustment");
  String val = (String)request.getParameter("final");
  String comments = (String)request.getParameter("comments");
  if (comments == null) comments = "";

  dbInterface.updateDB("INSERT into iem_calibration(station, portfolio, valid, parameter, adjustment, final, comments) values ('"+ sid +"', '"+ thisUser.getPortfolio() +"', '"+ valid +"','"+ para +"', "+ adjustment +", "+ val +", '"+ stringUtils.cleanString(comments) +"')");
  
}

%>
<%@include file='include/header.jsp'%>

<%
if (s_mid != null)
{
  ResultSet r = dbInterface.callDB("SELECT * from iem_calibration WHERE portfolio = '"+ thisUser.getPortfolio() +"' and station = '"+ s_mid +"' ORDER by valid DESC");
  out.println("<table>");
  while (r.next()){
    out.println("<tr><th>"+ r.getString("id") +"</th><td>"+ r.getString("station") +"</td><td>"+ r.getString("valid") +"</td><td>"+ r.getString("parameter") +"</td><td>"+ r.getString("adjustment") +"</td><td>"+ r.getString("final") +"</td></tr>");
     if (! r.getString("comments").equalsIgnoreCase("") ){ out.println("<tr><td colspan=6>"+ r.getString("comments") +"</td></tr>"); }

  } 
  out.println("</table>");
}

%>

<h3>Add Calibration Event!</h3>

<form method="POST">

<p>Select a Site:<br>
<%= mLib.stationSelect( thisUser.getPortfolio() ) %>

<p>Select Parameter:<br />
<select name="parameter">
  <option value="dwpf">Air Dew Point
  <option value="tmpf">Air Temperature
  <option value="pres">Pressure
</select>

<p>Enter Date: YYYY-mm-dd HH:MM<br />
<input type="text" name="valid">

<p>Enter adjustment:<br />
<input type="text" name="adjustment" size="5">

<p>Enter final value:<br />
<input type="text" name="final" size="5">

<p>Enter comments:<br />
<textarea name="comments" cols="60" rows="5"></textarea>

<input type="submit">

</form>

<%= jlib.footer() %>
