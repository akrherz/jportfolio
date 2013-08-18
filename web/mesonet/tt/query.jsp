<%
/**
 * query.jsp
 *   Interface to query for trouble tickets
 */
%>
<%@include file='../setup.jsp'%>
<%@include file='../include/header.jsp'%>
<%@include file='links.jsp'%>

<font class="bluet">Query Form</font>

<form method="POST" action="list.jsp">
<table width=100%>

<tr>
  <th align="left">Ticket Status:</th>
  <th align="left">Limit Sites:</th>
</tr>

<tr>
  <td>
<select name="status" size="5" MULTIPLE>
  <option value="OPEN">OPEN
  <option value="CLOSED">CLOSED
</select>
  </td>

  <td>
<%=  mLib.stationSelectMulti( thisUser.getPortfolio() ) %>
   </td>
</tr>

<tr>
  <th colspan=2>
   <input type="submit" value="Find Tickets">
   <input type="reset">
  </th>
</tr>
</table>
</form>

<%= jlib.footer() %>
