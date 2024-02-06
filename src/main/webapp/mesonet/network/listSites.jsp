<%
 /**
  * listSites.jsp
  *  - List out sites in the  Network
  */
%>

<%@include file='../setup.jsp'%>
<%@include file='../include/header.jsp'%>


<font class="bluet">List Sites</font>

<p class="intro">Here is a listing of sites in your network.  By clicking
on the name you can get a more detailed and editable listing of station
attributes.</p>

<%
  ResultSet lsites = dbInterface.callDB("SELECT * from iem_sites WHERE "
   +" portfolio = '"+thisUser.getPortfolio()+"' ");
  if ( lsites == null ){
    out.println("<p>Could not find any sites for this network.  Perhaps you "
     +" would like to <a href='/jportfolio/mesonet/network/addSite.jsp'>Add "
     +" A Site</a>\n");

  } else{
    out.println("<table cellpadding=2 rowspacing=1>\n");
    out.println("<tr><th align=\"left\">Station Name:</th>"
     +"<th>Latitude</th><th>Longitude</th></tr>\n");
    while( lsites.next() ){
      out.println("<tr>\n"
       +"<th><a href='/jportfolio/mesonet/site/editSite.jsp?s_mid="
       +lsites.getString("s_mid")+"'>"+lsites.getString("s_name")+"</a></th>\n"
       +"<td>"+lsites.getString("s_lat")+"</td>\n"
       +"<td>"+lsites.getString("s_lon")+"</td>\n"
       +"</tr>\n");

    }
    out.println("</table>\n");
  }

%>

<%= jlib.footer() %>
