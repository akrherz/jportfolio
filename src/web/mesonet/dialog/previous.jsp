<%@ page import="org.collaborium.portfolio.*" %>
 <%@ page import="java.sql.*" %>
<%@ page import="org.collaborium.portfolio.jdot.*" %>
<%
	StringBuffer pageContent = new StringBuffer();
	String thisPageURL = "/jportfolio/gccourse/dialog/index.jsp";
%>


<%	
	
	portfolioUser thisUser = null;
	
	try{
		thisUser = (portfolioUser)session.getAttribute("User");
	} catch(Exception ex) {
		System.err.println("I am here");
		ex.printStackTrace();
	}
%>

<%@include file='variables.jsp' %>

<%
	if (thisUser == null) {
		response.setHeader("Refresh", "0; URL=/jportfolio/gccourse/index.jsp");
	}

	if (callMethod == null) callMethod = "x";
	
%>

<%= jlib.basicHeader(thisUser, jdot.TITLE) %>

<TABLE>
<TR><TD width="25%" valign="TOP" class="botBox"> 

 
	<%@include file='sideBar.jsp' %>

</TD><TD width="100%" VALIGN="TOP">

<%= jlib.topBox("Previous Discussions") %>
<font class="instructions">Select the thread that you would like to 
read from previous years' discussion</font>

<p><b>GCP 2001 Portfolio:</b>
<ul>
<%
  ResultSet rs = dbInterface.callDB("SELECT threadid, subject from dialog "
   +" WHERE portfolio = 'gcp2001' and security = 'public' and "
   +" idnum > 10000 and idnum < 100000 ORDER by threadid ASC");
   
  while(rs.next() ){
//    out.println("<li><a href=\""/j?threadID="+ rs.getString("threadid") +""+ rs.getString("subject") +"</a><br>");
      out.println("<li><a href=\"/jportfolio/jsp/user/"
       +"threadCat.jsp?threadID="+ rs.getString("threadid")+"\">"
       + rs.getString("subject") +"</a>");
  } 
%>
</ul>

<p><b>GCP 2000 Portfolio:</b>
<%@include file='gcp2000.html' %>

<p><b>GCP 1999 Portfolio:</b>
<%@include file='gcp1999.html' %>

<p><b>GCP 1998 Portfolio:</b>
<%@include file='gcp1998.html' %>

<p><b>GCP 1997 Portfolio:</b>
<%@include file='gcp1997.html' %>

<p><b>GCP 1996 Portfolio:</b>
<%@include file='gcp1996.html' %>


<%= jlib.botBox() %>

</TD></TR>
</TABLE>

</TD></TR></TABLE>


</BODY>
</HTML>
