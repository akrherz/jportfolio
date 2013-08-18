<%@include file='setup.jsp'%>
<%= jlib.genHeader(thisUser, "GC Portfolio | Classmates", "ba") %>


<h3>Listing of Classmates</h3>
<font class="instructions">Here is a listing of students that have entered their Bio.</font><br>

<%
  ResultSet rs = dbInterface.callDB("SELECT getUserName(b.username) as name, "
   +" b.username, b.body from biosketch b, students s WHERE "
   +" s.username = b.username and "
   +" s.username NOT IN "
       +" (select username from students WHERE portfolio = 'aal2005' and "
       +" username != 'akrherz' and username != 'gstakle' )"
   +" and s.portfolio = '"+ thisUser.getPortfolio() +"' ");

  while (rs.next() ){
     String userName = (String)rs.getString("username");
     out.println("\n\n<div class=\"user\"><img width=150 class=\"pict\" src=\"/jportfolio/FILES/"+userName+"/me.gif\">\n"
      +"<font class=\"user\">"+ rs.getString("name") +" ( "+ userName +" )</font><br>\n"
      + stringUtils.toBR( rs.getString("body") ) +"</div>\n"
      +"<br clear=\"all\"><hr>");
  }

%>

<%= jlib.footer() %>
