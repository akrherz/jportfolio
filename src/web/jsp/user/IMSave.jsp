<HTML>
<HEAD>
	<TITLE>Portfolio IM Save</TITLE>
	
</HEAD>
<BODY bgcolor="white">


<%
	String userID = request.getParameter("toUser");
	String content = request.getParameter("content");

	portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
%>



<%@ page import="org.collaborium.portfolio.*" %>

<%
	try{
		IMessage IMessage = new IMessage();
		IMessage.setAuthor( thisUser.getUserID() );
		IMessage.setContent( content );
		IMessage.setTo( userID );
	
	
		IMDatabase.postMessage( IMessage );
	} catch(Exception ex) {
		
		ex.printStackTrace();
	}
	
%>

<TABLE bgcolor="#EEEEEE"><TR><TD>

Message Posted to: <B><%= userID %></B>

<P><font color="green">It may take a little while from your friend to see your message. Please be patient.
You may close this window if you want.</font>

</TD></TR></TABLE>


<BR><a href="IMCat.jsp">Back to IM</a>


</BODY>
</HTML>
