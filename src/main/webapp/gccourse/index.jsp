<!-- index.jsp -->
<%@include file='setup.jsp'%>
<%= jlib.genHeader(thisUser, "JPortfolio | Global Change Course",  "Manager") %>

<%
if ( thisUser == null || thisUser.getPortfolio() == null ) {

 session.setAttribute("comebackURL", "/jportfolio/gccourse/index.jsp");
%>

<%= jlib.topBox("Login to Portfolio") %>

        <FORM METHOD="POST" action="/jportfolio/login.jsp">

  <table>
  <tr>
   <th>Username:</th>
   <td><input type="text" size="20" name="username"></td>
  </tr>
  <tr>
   <th>Password:</th>
   <td><input type="password" size="20" name="password"></td>
  </tr>
  <tr>
   <th>Select Year:</th>
   <td><select name="portfolio">
     <option value="gcp2007">GCP Spring 2007 (Current)
     <option value="gcp2006">GCP Spring 2006
     <option value="gcp2005">GCP Spring 2005
     <option value="gcp2004">GCP Spring 2004
     <option value="gcde2003">GCP DE 2003
     <option value="gcp2003">GCP Spring 2003
     <option value="gcde2002">GCP DE 2002
     <option value="gcp2002">GCP Spring 2002
     <option value="gcp2001">GCP Spring 2001
    </select></td></tr>
 </table>

        <input type="SUBMIT" value="Log IN">
        <input type="reset" value="Reset">
        </form>
	

	<p><B>First time users should:</b> <a href="createAccount.jsp">Create Account</a>
	<br /><b>HELP!</b>  I <a href="/jportfolio/jsp/user/mailPass.jsp?forward=/jportfolio/gccourse">forgot</a> my
	password.

<%= jlib.botBox() %>

<%  } else { 

  jlib.addUser( thisUser.getUserID(), "GC Portfolio"); 
  session.setMaxInactiveInterval( 10000 );
%>
<%@include file='mainPage.jsp'%>


<% } // End of if switch for deciding if a user is logged in
%>

<%= jlib.footer() %>
