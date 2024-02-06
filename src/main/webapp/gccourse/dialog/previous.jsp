<!-- previous.jsp -->
<%@include file='../setup.jsp'%>
<%@ page import="org.collaborium.portfolio.jdot.*" %>
<%= jlib.genHeader(thisUser, "JPortfolio | Global Change Course",  "Dialog") %>
<%
	StringBuffer pageContent = new StringBuffer();
	String thisPageURL = "/jportfolio/gccourse/dialog/index.jsp";
	
%>

<%@include file='variables.jsp' %>

<%
	if (thisUser == null) {
		response.setHeader("Refresh", "0; URL=/jportfolio/gccourse/index.jsp");
	}

	if (callMethod == null) callMethod = "x";
	
%>


<TABLE>
<TR><TD width="25%" valign="TOP" class="botBox"> 

 
	<%@include file='sideBar.jsp' %>

</TD><TD width="100%" VALIGN="TOP">

<%= jlib.topBox("Previous Discussions") %>
<font class="instructions">Select the thread that you would like to 
read from previous years' discussion</font>

<p><b>GCP 2006 Portfolio:</b>
<ul>
<%
  ResultSet rs2006 = dbInterface.callDB("SELECT threadid, subject from dialog "
   +" WHERE portfolio = 'gcp2006' and security = 'public' and "
   +" idnum > 10000 and idnum < 100000 ORDER by threadid ASC");

  while(rs2006.next() ){
      out.println("<li><a href=\"/jportfolio/jsp/user/"
       +"threadCat.jsp?threadID="+ rs2006.getString("threadid")+"\">"
       + rs2006.getString("subject") +"</a>");
  }
%>
</ul>


<p><b>GCP 2005 Portfolio:</b>
<ul>
<%
  ResultSet rs2005 = dbInterface.callDB("SELECT threadid, subject from dialog "
   +" WHERE portfolio = 'gcp2005' and security = 'public' and "
   +" idnum > 10000 and idnum < 100000 ORDER by threadid ASC");

  while(rs2005.next() ){
      out.println("<li><a href=\"/jportfolio/jsp/user/"
       +"threadCat.jsp?threadID="+ rs2005.getString("threadid")+"\">"
       + rs2005.getString("subject") +"</a>");
  }
%>
</ul>


<p><b>GCP 2004 Portfolio:</b>
<ul>
<%
  ResultSet rs2004 = dbInterface.callDB("SELECT threadid, subject from dialog "
   +" WHERE portfolio = 'gcp2004' and security = 'public' and "
   +" idnum > 10000 and idnum < 100000 ORDER by threadid ASC");

  while(rs2004.next() ){
      out.println("<li><a href=\"/jportfolio/jsp/user/"
       +"threadCat.jsp?threadID="+ rs2004.getString("threadid")+"\">"
       + rs2004.getString("subject") +"</a>");
  }
%>
</ul>


<p><b>GCP 2003 Portfolio:</b>
<ul>
<%
  ResultSet rs2003 = dbInterface.callDB("SELECT threadid, subject from dialog "
   +" WHERE portfolio = 'gcp2003' and security = 'public' and "
   +" idnum > 10000 and idnum < 100000 ORDER by threadid ASC");

  while(rs2003.next() ){
//    out.println("<li><a href=\""/j?threadID="+ rs.getString("threadid") +""+ rs.getString("subject") +"</a><br>");
      out.println("<li><a href=\"/jportfolio/jsp/user/"
       +"threadCat.jsp?threadID="+ rs2003.getString("threadid")+"\">"
       + rs2003.getString("subject") +"</a>");
  }
%>
</ul>


<p><b>GCP 2002 Portfolio:</b>
<ul>
<%
  ResultSet rs2002 = dbInterface.callDB("SELECT threadid, subject from dialog "
   +" WHERE portfolio = 'gcp2002' and security = 'public' and "
   +" idnum > 10000 and idnum < 100000 ORDER by threadid ASC");

  while(rs2002.next() ){
//    out.println("<li><a href=\""/j?threadID="+ rs.getString("threadid") +""+ rs.getString("subject") +"</a><br>");
      out.println("<li><a href=\"/jportfolio/jsp/user/"
       +"threadCat.jsp?threadID="+ rs2002.getString("threadid")+"\">"
       + rs2002.getString("subject") +"</a>");
  }
%>
</ul>


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
