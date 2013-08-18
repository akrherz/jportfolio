<%
/**
 * list.jsp
 *  Java server page that lists out a listing of trouble tickets
 */

%>

<%@include file='../setup.jsp'%>
<%@include file='../include/header.jsp'%>
<%@include file='links.jsp'%>

<%
// ----------------- Query out a listing of current TT
String offset = request.getParameter("offset");
String sessionOffset = (String)session.getAttribute("tt_offset");
if (offset == null){  
 if (sessionOffset != null){
  offset = sessionOffset;
 } else{
  offset = "0"; 
 }
} else{
 session.setAttribute("tt_offset", offset);
}

// ---------------------------------------------------------------------
String filter = request.getParameter("filter");
String sessionFilter = (String)session.getAttribute("tt_filter");
if (filter == null){  
 if (sessionFilter != null){
  filter = sessionFilter;
 } else{
  filter = "0"; 
 }
} else{
 session.setAttribute("tt_filter", filter);
}


String filterTxt = "Status == OPEN";
String sqlFilter = " and status != 'CLOSED' ";
switch (filter.charAt(0) ){
  case '1':
   sqlFilter = " and last > 'TODAY'::date ";
   filterTxt = "Ticket changed today";
  break;
  case '2':
   sqlFilter = " and last > ('TODAY'::date - '7 days'::interval) ";
   filterTxt = "Ticket changed this week";
  break;
  case '3':
   sqlFilter = " and s_mid = '"+ s_mid +"' ";
   filterTxt = "Use Site Filter from above";
  break;
  case '4': /* Filter by Site, but no offline */
   sqlFilter = " and s_mid = '"+ s_mid +"' and subject != 'Site Offline' ";
   filterTxt = "Use Site Filter / No Offline Tickets";
  break;
  case '5': /*  no offline */
   sqlFilter = " and subject != 'Site Offline' ";
   filterTxt = "List All / No Offline Tickets";
  break;
  case '6':
   sqlFilter = " ";
   filterTxt = "All TT Listed";
  break;
}
%>

<form method="GET" action="list.jsp" name="filter">
<b>Set Filter:</b>
<select name="filter">
 <option value="0" <% if (filter.equals("0")) out.print("SELECTED"); %>>Status is OPEN
 <option value="1" <% if (filter.equals("1")) out.print("SELECTED"); %>>Ticket changed today
 <option value="2" <% if (filter.equals("2")) out.print("SELECTED"); %>>Ticket changed this week
 <option value="3" <% if (filter.equals("3")) out.print("SELECTED"); %>>Use site filter from header
 <option value="4" <% if (filter.equals("4")) out.print("SELECTED"); %>>Use site filter / No Offline Tickets</option>
 <option value="5" <% if (filter.equals("5")) out.print("SELECTED"); %>>List All / No Offline Tickets
 <option value="6" <% if (filter.equals("6")) out.print("SELECTED"); %>>List All
</select>
<input type="submit" value="Set Filter"></form>
<p>
<%
ResultSet tt = dbInterface.callDB("SELECT *, getUserName(author) as rname"
  +" ,getSiteName(s_mid) as s_name from tt_base WHERE "
  +" portfolio = '"+ thisUser.getPortfolio() +"' "+ sqlFilter  
  +" ORDER by entered DESC "
  +" LIMIT 20 OFFSET "+ offset );
StringBuffer tableContent = new StringBuffer();

int tt_cnt = 0;
if (tt != null) {
  while ( tt.next() ){
    tt_cnt = tt_cnt + 1;
    mTT thisTT = new mTT(tt);
    tableContent.append( thisTT.printTR() );
  }
} else {
  out.println("No results found!");
}// End of if
%>

<p align="right">
<font size="-1">Paging: 
<%
 // Time for some logic to handle paging...
 Integer thisOffset = new Integer(offset);
 int thisoff = thisOffset.intValue();
 int prevoff = thisoff - 20;
 int nextoff = thisoff + 20;
 if (thisoff > 0){ // Print Previous link
   out.println("<a href=\"list.jsp?offset="+ prevoff +"\">Prev 20</a>\n");
 }
 out.println("<b>"+ (thisoff +1) +" - "+ (thisoff + 20) +"</b>\n");
 if (tt_cnt == 20){
   out.println("<a href=\"list.jsp?offset="+ nextoff+"\">Next 20</a>\n");
 }
%></font>


<table width="100%" cellspacing=0 cellpadding=2 
  border=0 style="font-size: 10pt; font-family: arial;">
<tr bgcolor="#eeeeee">
  <th align="left">ID</th>
  <th align="left">Date</th>
  <th align="left">By</th>
  <th align="left">Status</th>
  <th align="left">Station ID</th>
  <th align="left">Summary</th>
</tr>
<%= tableContent.toString() %>
</table>

<%= jlib.footer() %>
