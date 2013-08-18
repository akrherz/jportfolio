<%@include file='setup.jsp'%>
<%= jlib.genHeader(thisUser, "ICT and Learning | Create An Account", "ba") %>

<%= jlib.blackBoxTop("") %>
	<H3>Create New User Account:</H3>
	
	<font class="instructions">It is necessary for you to create an account in 
	order for the Portfolio software to identify who you are. Once you have
	successfully completed this form, you will be able to use Portfolio.</font>
	
	<form method='POST' action='/jportfolio/login.jsp' name='user'>
<% session.setAttribute("comebackURL", "/jportfolio/aalborg/"); %>
	<table>
	 <tr>
	  <td><b>First name:</b></td>
	  <td><b>Last name:</b></td>
	 </tr>
         <tr>
	   <td><input type='text' name='fName' size='15'></td>
	   <td><input type='text' name='lName' size='30'></td>
	 </tr>
	</table>
	
	<P><b>Primary email address:</b><BR>
	  &nbsp; <i>(Portfolio may occasionally email information to this address.)</i>
	<BR><input type='text' name='email' size='30'>

	<P><b>Request a userID:</b><br>
	 &nbsp; <i>(preferably use your emailid and no spaces )</i> 
	<BR><input type='text' name='username' size='20'>

	<P><table>
	 <tr>
	  <td align="right"><b>Access password:</b></td> 
	  <td align="left"><input type='password' name='password1' size='15' MAXLENGTH='12'></td>
	 </tr>
	 <tr>
	  <td align="right"><b>Re-enter for verification:</b></td>
	  <td align="left"><input type='password' name='password2' size='15' MAXLENGTH='12'></td>
	 </tr>
	</table> 
	 
	<p><font class="instructions">This form attempts to automatically register you for the
	Global Change Portfolio and <b>the</b> ICT Portfolio.  In order to keep unwanted users from registering for
	this portfolio, a challenge password is needed.</font>
	 
	<P><b>Enter the ICT Portfolio challenge password</b><br>
	<i>(This password should have been supplied to you by Dr Sorensen.)</i> <BR>
	<input type='password' name='portpass' size='15'>

	<input type='hidden' name='portfolio' value='aal2004'>
	<input type='hidden' name='portfolio2' value='gcp2005'>
	 
	<P><input type='submit' value='Create Account'><input type='reset'>
	</form>

	
<%= jlib.blackBoxBot() %>
</div>

<%= jlib.footer() %>
