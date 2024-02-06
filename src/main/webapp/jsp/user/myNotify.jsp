<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.collaborium.util.*" %>
<%@ page import="org.collaborium.portfolio.*" %>

<% 
portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
String mode = (String)request.getParameter("mode");
String idnum = (String)request.getParameter("idnum");

if (mode == null)  mode = "z";

switch ( mode.charAt(0) ){
  case 'o':
  case 'k':
    if ( idnum != null )
      jlib.removeNotify( idnum );
  break;
}


if (thisUser == null){
  response.setHeader("Refresh","0; URL=/jportfolio");
  return;
}

ResultSet myResultSet = dbInterface.callDB("Select message, program, idnum, "
  +" entered from notify WHERE username = '"+ thisUser.getUserID() +"' and "
  +" portfolio = '"+ thisUser.getPortfolio() +"' ORDER by entered DESC");

String portHome = thisUser.getHome();
if (portHome.equalsIgnoreCase("/jportfolio/servlet/") )
  portHome = "/jportfolio/servlet/jportfolio";
%>
<%= jlib.genHeader(thisUser, "Portfolio Messages", "Manager") %>
<link rel=stylesheet type=text/css href="/jportfolio/css/basic.css">
</head>
<body bgcolor="white">

<div align="center"><b>Portfolio Notifications</b></div>
<HR>
  Back to <a href="<%= portHome %>"><%= thisUser.getPortfolioName() %></a> portfolio.
<HR>

<p><font class="instructions">Certain actions within portfolio will generate
these 'notification' messages.  The target of these messages can be viewed
and then cleared on this page.</font></p>

<TABLE>
<%
  Timestamp ts = new Timestamp(1000000000);
  while ( myResultSet.next() ) { 
    try{
      ts = myResultSet.getTimestamp("entered");
    }catch(Exception ex){
    } 
    String sts = stringUtils.gmtDate(ts);
out.println("<tr>\n"
  +"<td>"+ myResultSet.getString("message") +" :</TD>\n"
  +"<TD><a href=\""+ myResultSet.getString("program") +"\">View</a></TD>\n"
	  +"<TD><B> / </B></TD>\n"
	  +"<TD><a href=\"/jportfolio/jsp/user/myNotify.jsp?mode=k&idnum="+ myResultSet.getString("idnum") +"\">Clear</a></TD>\n"
  +"<td><font style=\"font-size: 12pt\">"+ sts +"</font></td>\n"
	  +"</TR>\n");

  } 

  if (! myResultSet.previous() ){
	out.println("You have no notifications from the Portfolio system");
  }

%>
</TABLE>

<%= jlib.footer() %>
