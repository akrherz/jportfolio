<%@include file='setup.jsp'%>
<%= jlib.genHeader(thisUser, "Portfolio | Create Account", "ba") %>


<%= jlib.topBox("Create New User Account") %>
	
	It is necessary for you to create an account in 
	order for the Portfolio software to identify who you are. Once you have
	successfully completed this form, you will be able to use Portfolio.
	
	<form method='POST' action='/jportfolio/login.jsp' name='user'>
	<% session.setAttribute("comebackURL", "/jportfolio/gccourse/"); %>
	
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
	 
	<p>This form attempts to automatically register you for the
	Global Change Portfolio.  In order to keep unwanted users from registering for
	this portfolio, a challenge password is needed.
	 
	<P><b>Enter the GCP 2006 Portfolio challenge password</b><br>
	<i>(This password should have been supplied to you by Dr Takle.)</i> <BR>
	<input type='password' name='portpass' size='15'>
	<input type='hidden' name='portfolio' value='gcp2007'>
	 
	<P><input type='submit' value='Create Account'><input type='reset'>
	</form>

	
<%= jlib.botBox() %>

<%= jlib.footer() %>
