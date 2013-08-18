<!-- index.jsp -->
<%@ page import="org.collaborium.portfolio.*" %>
<%
 authBean auth = new authBean(request, session);
 portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
%>
<%= jlib.genHeader(thisUser, "JPortfolio", "Portfolio") %>

<h3>Welcome to Portfolio!</h3>

<p>Portfolio is an online course management system in use since 1995.  It is
primarily used for the management of a course in Global Change taught
at Iowa State University.</p>

<p>
In order to use Portfolio you need to have an account, please proceed to the <a href="login.jsp">log in</a> page to either log in or create an account.
</p>

<h4>Special Portfolios:</h4>
<ul>
  <li><a href="/jportfolio/afc/">AMS Forecast Contest</a></li>
  <li><a href="/jportfolio/mesonet/">Iowa Mesonet Portfolio</a></li>
  <li><a href="/jportfolio/gccourse/">ISU GCP 2007</a></li>
</ul>


<%= jlib.footer() %>
