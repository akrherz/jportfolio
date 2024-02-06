<%
  if (thisUser == null) {
%>
 <%@ include file='login.html'%>
 <%= jlib.footer() %>
<%
    return;
  }
%>
