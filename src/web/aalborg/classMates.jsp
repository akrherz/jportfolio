  <%@include file='setup.jsp'%>
<%= jlib.genHeader(thisUser, "ICT Portfolio | Classmates", "ba") %>


<h3>Listing of Classmates</h3>
<p>Here is a listing of students that have entered their Bio.</p>

<%
  ResultSet rs = dbInterface.callDB("SELECT getUserName(b.username) as name, b.username, b.body from biosketch b, students s WHERE s.username = b.username and s.portfolio = '"+ thisUser.getPortfolio() +"' ");
  String userName = null;
  while (rs.next() ){
	userName = (String)rs.getString("username");
	out.println("\n\n<div class=\"user\"><img class=\"pict\" src=\"/jportfolio/FILES/"+userName+"/me.gif\">\n"
	+"<font class=\"user\">"+ rs.getString("name") +" ( "+ userName +" )</font><br>\n"
	+ stringUtils.toBR( rs.getString("body") ) +"</div>\n"
	+"<br clear=\"all\"><hr>");
  }
%>

<%= jlib.footer() %>
