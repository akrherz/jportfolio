<%@include file='../setup.jsp'%>

<%= jlib.genHeader(thisUser, "GCP Blocks",  "a") %>
<%
	String thisBlock = (String)request.getParameter("blockid");
	if ( thisBlock == null ){
		response.setHeader("Refresh", "0; URL=../index.jsp");
	} else { 
%>


<div align='center'> 
  <a href="/jportfolio/gccourse/">GCP Portfolio Home</a><br>
  <a href="index.jsp?blockid=1">Block 1</a> &nbsp; <b>|</b> &nbsp;
  <a href="index.jsp?blockid=2">Block 2</a> &nbsp; <b>|</b> &nbsp;
  <a href="index.jsp?blockid=3">Block 3</a> 
</div>

<h3>Block <%= thisBlock %></h3>

<% if ( thisBlock.equals("1") ) { %>
	<%@include file='block1.jsp'%>
<% } else if ( thisBlock.equals("2") ) { %>
	<%@include file='block2.jsp'%>
<% } else { %>
	<%@include file='block3.jsp'%>
<% } %>

<% } // End of else %>

<%= jlib.footer() %>
