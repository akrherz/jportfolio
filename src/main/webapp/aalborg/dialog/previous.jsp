<!-- previous.jsp -->
<%@include file='../setup.jsp'%>
<%@ page import="org.collaborium.portfolio.jdot.*" %>
<%= jlib.genHeader(thisUser, "JPortfolio | ICT and Learning",  "Dialog") %>
<%
	StringBuffer pageContent = new StringBuffer();
 String title = "Previous Dialog Discussions";
	String thisPageURL = "/jportfolio/aalborg/dialog/index.jsp";
%>

<%@include file='variables.jsp' %>

<%
	if (thisUser == null) {
		response.setHeader("Refresh", "0; URL=/jportfolio/aalborg/index.jsp");
	}

	if (callMethod == null) callMethod = "x";
	
%>


<TABLE class="contentColor">
<TR><TD width="25%" valign="TOP" class="botBox"> 

 
	<%@include file='sideBar.jsp' %>

</TD><TD width="100%" VALIGN="TOP">

<%= jlib.topBox("Previous Discussions") %>
<font class="instructions">Select the thread that you would like to 
read from previous years' discussion</font>

<p><b>ICT 2002 Portfolio:<b>
<ul>
<%
  ResultSet rs2002 = dbInterface.callDB("SELECT threadid, subject from dialog "
   +" WHERE portfolio = 'aal2002' and security = 'public' and "
   +" idnum > 10000 and idnum < 100000 ORDER by threadid ASC");

  while(rs2002.next() ){
      out.println("<li><a href=\"/jportfolio/jsp/user/"
       +"threadCat.jsp?threadID="+ rs2002.getString("threadid")+"\">"
       + rs2002.getString("subject") +"</a>");
  }
%>
</ul>


<p><b>ICT 2001 Portfolio:</b>
<ul>
<%
  ResultSet rs = dbInterface.callDB("SELECT threadid, subject from dialog "
   +" WHERE portfolio = 'aal2001' and security = 'public' and "
   +" idnum > 10000 and idnum < 100000 ORDER by threadid ASC");
   
  while(rs.next() ){
//    out.println("<li><a href=\""/j?threadID="+ rs.getString("threadid") +""+ rs.getString("subject") +"</a><br>");
      out.println("<li><a href=\"/jportfolio/jsp/user/"
       +"threadCat.jsp?threadID="+ rs.getString("threadid")+"\">"
       + rs.getString("subject") +"</a>");
  }
%>
</ul>

<p><b>ICT 1999 Portfolio:</b>
<%   @include file='aal1999.html' %>

<%= jlib.botBox() %>

</TD></TR>
</TABLE>

</TD></TR></TABLE>


</BODY>
</HTML>
