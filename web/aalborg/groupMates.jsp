<%@include file='setup.jsp'%>
<%= jlib.genHeader(thisUser, "ICT Portfolio | Groupmates", "ba") %>


<h3>Listing of Group Members</h3>
<font class="instructions">Here is a listing of people in your group.</font>

<%
  ResultSet groupies = dbInterface.callDB("SELECT getUsername(u.username) "
   +" as name, u.username as username, u.email as email "
   +" from users u, students s WHERE "
   +" s.username = u.username and s.portfolio = '"+ thisUser.getPortfolio() +"'"
   +" and s.gid = '"+ thisUser.getGroupID() +"' ");
  ResultSet rs = null;
  String userName = null;
  while ( groupies.next() ){
   out.println("<p><font class=\"user\">"+ groupies.getString("name") 
    +"( "+ groupies.getString("username") +" )</font>");
   rs = dbInterface.callDB("SELECT getUserName(b.username) as name, "
     +" b.username, b.body from biosketch b, students s "
     +" WHERE s.username = b.username and "
     +" s.portfolio = '"+ thisUser.getPortfolio() +"' "
     +" and s.username = '"+ groupies.getString("username") +"' ");
   while (rs.next() ){
     userName = (String)rs.getString("username");
     out.println("\n\n<div class=\"user\"><img class=\"pict\" src=\"/jportfolio/FILES/"+userName+"/me.gif\">\n"
	+ stringUtils.toBR( rs.getString("body") ) +"</div>\n"
	+"<br clear=\"all\"><hr>");
  }
  }
%>

<%= jlib.footer() %>
