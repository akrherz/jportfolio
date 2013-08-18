<HTML>
<HEAD>
	<TITLE>Portfolio IM Cat</TITLE>
	<meta http-equiv="refresh" content="60; URL=IMCat.jsp">
</HEAD>
<BODY bgcolor="white">

<%@ page import="java.util.*" %>
<%
	// Make sure we do this first
	String tester = request.getParameter("remove");
	if (tester != null) {
		IMDatabase.removeMessage( tester );
	}
	portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
	String userID ;
	Vector messages = null;
	if (thisUser == null) {
		userID = "Anonymous Coward";
	
	} else {
		userID = thisUser.getUserID();
		messages = IMDatabase.getIDs( userID );
	}
	
	
%>

<H3>Portfolio Messenger:</H3>

<TABLE bgcolor="#EEEEEE"><TR><TD>
Current Messages for: <B><%= userID %></B>
</TD></TR></TABLE>

<BR><a href="IMCat.jsp">Refresh Page</a> (automatically refreshes in 5 minutes)

<P>Messages as of: <font color="blue"><%= new java.util.Date() %></font>
<%@ page import="org.collaborium.portfolio.*" %>

<%
	if ( messages != null ) {
		for (int i = 0; i < messages.size() ; i++ ) {
			IMessage IMessage = IMDatabase.getMessage( messages.elementAt( i ).toString() );
			
			String author  	= IMessage.getAuthor();
			String message 	= IMessage.getContent();
			String idNum	= IMessage.getId();
			
			out.println("<P><TABLE bgcolor=\"#EEEEEE\" width=\"100%\"><TR><TD>\n");
			out.println("<B>Posted By:</B> "+ author +"<BR>");
			out.println( message );
			out.println("<P><a href=\"IMPost.jsp?toUser="+author+"\">Reply</a> &nbsp; <B>|</B> &nbsp; <a href=\"IMCat.jsp?remove="+idNum+"\">Clear</a> ");
			out.println("</TD></TR></TABLE>\n");
		}
	
	} else if (userID.equalsIgnoreCase("Anonymous Coward") ){
		out.println("<BR><BR>You must not be logged in.  Sorry.");
	
	} else {
		out.println("You currently have no instant messages!");
	}
%>

</BODY>
</HTML>
