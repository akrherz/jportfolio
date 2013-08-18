<%@ include file='setup.jsp'%>
<%
 /** index.jsp
  *   Daryl Herzmann 15 Sep 2003 */

  String utest = (String)request.getParameter("username");
  if (utest != null) {
    authBean auth = new authBean(request, session);
    thisUser = auth.thisUser;
    if (thisUser != null)
      jlib.addUser( thisUser.getUserID(), "AFC");
      session.setAttribute("User", thisUser);
      session.setMaxInactiveInterval(18600);
  }
%>
<%= jlib.genHeader(thisUser, "JPortfolio | AFC" ,  "Manager") %>

<h4>AMS Forecasting Contest</h4>

<%@ include file='auth.jsp'%>

<% if ( thisUser.isAdmin() ){ %>
<%= jlib.adminCommands() %>
<% } %>

<p><b>Current Functionality:</b>
<ul>
 <li><a href="forecast.jsp">Enter your forecast</a></li>
<% if (thisUser.isAdmin()){ %>
 <li><a href="create_day.jsp">Create Forecast Day</a></li>
<% } %>
</ul>

<%= jlib.footer() %>
