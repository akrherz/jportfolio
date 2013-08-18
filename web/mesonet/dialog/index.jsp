
<%
	StringBuffer pageContent = new StringBuffer();
	String thisPageURL = "/jportfolio/mesonet/dialog/index.jsp";
%>

<%@include file='../setup.jsp' %>
<%@include file='../include/header.jsp' %>
<%@include file='variables.jsp' %>

<%
	if (callMethod == null) callMethod = "x";
	
%>


<TABLE>
<TR><TD width="150" valign="TOP" class="botBox"> 

 
	<%@include file='sideBar.jsp' %>

</TD><TD width="450" VALIGN="TOP">

<%@include file='actions.jsp' %>
	<%= pageContent.toString() %>

</TD></TR>
</TABLE>

<%= jlib.footer() %>
