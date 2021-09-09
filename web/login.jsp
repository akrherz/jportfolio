<!-- login.jsp  -->
<%@ page import="org.collaborium.portfolio.*" %>
<%
 String comebackURL = (String)session.getAttribute("comebackURL");
 if (comebackURL == null) comebackURL = "/jportfolio/servlet/jportfolio";

 authBean auth = new authBean(request, session);
 portfolioUser thisUser = (portfolioUser)session.getAttribute("User");

 if (thisUser != null && thisUser.getPortfolio() != null) 
 {
   response.setHeader("Refresh", "0; URL="+comebackURL);
   return;
 }
%>
<%= jlib.genHeader(thisUser, "Portfolio Login", "Login") %>

<%
if (auth.authError != null)
{
%>
<div class="portfolioError">
<strong>Portfolio Error:</strong><br />
<%= auth.authError %>
</div>
<%
}
%>

<form method="POST" action="login.jsp" name="p">

<% if (thisUser == null) { %>
<div id="loginBox">
<%= jlib.topBox("Log In") %>

<p>
If you don't already have a Portfolio account, you will need to <a href="javascript: setLayerDisplay('loginBox'); setLayerDisplay('createAccount'); ">create an account</a>.
</p>

<p><strong>Username:</strong><input type="text" name="username">
<p><strong>Password:</strong><input type="password" name="password">

<p><input type="submit" value="Log In!">

<%= jlib.botBox() %>

</div>
</form>

<% } else { 
// session.removeAttribute("comebackURL");
%>
<form method="POST" action="login.jsp" name="q">
<div id="loginPortfolio">
<h3>Log into your Portfolio</h3>
<p>
Below are the Portfolios you have previously registered for.  If you 
are accessing a new Portfolio for the first time, you will need to <a href="javascript: setLayerDisplay('registerPortfolio');">register</a> for it.
</p>
<p><strong>Select Portfolio:</strong>
<br /><select name="portfolio">
<%= jlib.userPortfolios(thisUser.getUserID()) %>
</select>
<p><input type="submit" value="Log In!">
</div>
</form>


<form method="POST" action="login.jsp" name="r">
<div id="registerPortfolio" style="display: none;">
<br /><%= jlib.registerPortfolio( thisUser ) %>
</div>
</form>
<% } %>

<form method="POST" action="login.jsp" name="s">
<div id="createAccount" style="display: none;">
<%= jlib.topBox("Create a new Account:") %>

<blockquote>This form registers you for the portfolio system.  Before you can sign up for a portfolio, you need to be registered. If you already have an account, please <a href="javascript: setLayerDisplay('loginBox'); setLayerDisplay('createAccount');">log in</a>.</blockquote>

		<TABLE><TR>
		<TD>Enter your first name:</TD>
		<TD>and last name:</TD>
		</TR><TR>
		<TD><input type='text' name='fName' size='20'></TD>
		<TD><input type='text' name='lName' size='40'></TD>
		</TR></TABLE>
		
		<P>Enter your primary email address:<BR>
		<input type='text' name='email' size='30'>
		
		<P>Request an userID:<BR>
		<input type='text' name='username' size='20'>
		
		<TABLE><TR>
		<TD>Choose a password:</TD>
		<TD>Verify password:</TD>
		</TR><TR>
		<TD><input type='password' name='password1' size='15'></TD>
		<TD><input type='password' name='password2' size='15'></TD>
		</TR></TABLE>
		
<p><input type="submit" value="Create Account!">
	
<%= jlib.botBox() %>
</div>
</form>

<%= jlib.footer() %>
