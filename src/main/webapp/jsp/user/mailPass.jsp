<%@ page import="java.util.*" %>
<%@ page import="org.collaborium.portfolio.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.oreilly.servlet.*" %>
<% portfolioUser thisUser = null; %>
<%= jlib.genHeader(thisUser, "Jportfolio | Mail Password", "") %>

<h3>Forgotten your password?</h3>

<% 
String userID = (String)request.getParameter("userID");
String forward = (String)request.getParameter("forward");

/* Okay, if forward is in the request, we should save it */
if (forward != null){
  session.setAttribute("forward", forward);
} else {
  forward = (String)session.getAttribute("forward");
}

/* If forward is still null, then use default */
if (forward == null){
   forward = "/jportfolio";
}


if (userID == null) { %>

<%= jlib.blackBoxTop("") %>

<font class="instructions">Please Enter your userID 
  and then an email will be sent. If you are unsure of your
  userID, please email your instructor for help.</font>
<FORM method="GET" action="mailPass.jsp">

<p><b>Your userID:</b>
<br><input type="text" name="userID">

<p><INPUT type="SUBMIT" value="Email Password">

</form>

<%= jlib.blackBoxBot() %>

<%
} else {
ResultSet rs = dbInterface.callDB("Select email, passwd from users "
	+" where username ='"+ userID +"' ");
%>

<%
  if (rs.next()) { 

try {
	MailMessage msg = new MailMessage("localhost");
	msg.from("nobody@meteor.geol.iastate.edu");
	msg.to( rs.getString("email") );
	msg.setSubject("Portfolio Password");
		
  		
		
	PrintStream out2 = msg.getPrintStream();
	out2.println("\n"
		+"# This email was generated from the Portfolio Website \n");
		
	out2.println("# Here is your password: "+ rs.getString("passwd") );
	msg.sendAndClose();
  
	
} catch(Exception ex) {
	plogger.report("Problem sending email");
	ex.printStackTrace();
}
%>

<P>Wanna try <a href="<%= forward %>">logging in</a> now?

<%
  } else {
%>

<P>The username specified is invalid.  Please try again.
<P>Please Enter your userID and then an email will be sent.
<FORM method="GET" action="mailPass.jsp">
<BR><input type="text" name="userID">
<INPUT Type="SUBMIT" value="Email Password">
</form>

<%
  }

}
%>

<P>Back to <a href="<%= forward %>">Login</a> page.

</BODY>
</HTML>
