  <%@include file='setup.jsp'%>
<%= jlib.genHeader(thisUser, "Portfolio | ICT and Learning", "Manager") %>

<%   

if ( thisUser == null || thisUser.getPortfolio() == null ) {
 session.setAttribute("comebackURL", "/jportfolio/aalborg/index.jsp");
%>

<%= jlib.blackBoxTop("") %> 

	<H3>Login to Portfolio:</H3>
        <FORM METHOD="POST" action="/jportfolio/login.jsp">

        <input type="hidden" name="mode" value="b">

        <input type="hidden" name="forward" value="/jportfolio/aalborg">

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
     <option value="aal2005">Aalborg 2005 (Current)
     <option value="aal2004">Aalborg 2004
     <option value="aal2003">Aalborg 2003
     <option value="aal2002">Aalborg 2002
    </select></td></tr>
 </table>


        <BR>
        <input type="SUBMIT" value="Log IN">
        <input type="reset" value="Reset">
        </form>
	
	<B>First time users should:</b> <a class="commands"
          href="createAccount.jsp">Create Account</a>
	<br><b>HELP!</b>  <a class="commands"
          href="/jportfolio/jsp/user/mailPass.jsp?forward=/jportfolio/aalborg">I forgot my password</a>

<%= jlib.blackBoxBot() %>

<%  } else { %>

<% jlib.addUser( thisUser.getUserID(), "ICT Portfolio"); %>
<%@include file='mainPage.jsp'%>


<% } // End of if switch for deciding if a user is logged in
%>

<%= jlib.footer() %>
