<%
/**
 * oldDialogCat.jsp
 *   - print out dialog from previous Aalborg
 *   - see jdot.treeMessages for how this is done.
 */
%>

<html>
<head>
  <title>Jportfolio | Print Thread</title>
  <link rel=stylesheet type=text/css href=/jportfolio//css/basic.css>
</head>

<body bgcolor="white">

<%@ page import="org.collaborium.portfolio.*" %>
<%@ page import="org.collaborium.util.*" %>
<%@ page import="org.collaborium.portfolio.jdot.*" %>
<%@ page import="java.sql.*" %>

<div align="center">
<h3>Portfolio Dialog</h3>
</div> 

<%
portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
String block_id = (String)request.getParameter("block_id");
String year = (String)request.getParameter("year");

StringBuffer sbuf = new StringBuffer();
	

ResultSet threads  = null;

String SQL =  "SELECT * from dialog_"+year+" WHERE block_id = '"+block_id+"'" 
	+" and portfolio = 'aal"+year+"' and category = 'public' order by thedate ASC";

// Lets make the query and figure out 
threads = dbInterface.callDB(SQL);


sbuf.append("<table width=600>");
sbuf.append("<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>"
	+"<td>&nbsp;</td><td>&nbsp;</td><td></td></tr>\n");

while( threads.next() ) {

  sbuf.append("<tr><td colspan=\"7\" class=\"postTop\">"
    + threads.getString("subject") +"\n"
    + "<br><b>posted on:</b> "+ threads.getString("thedate") +"</td></tr>\n"
    + "<tr><td colspan=\"7\" class=\"postBottom\">\n"
    + stringUtils.toBR( threads.getString("body") ) +"</td></tr>\n");
	


} //End of while1()
threads.close();
	    
sbuf.append("</table>");
out.println( sbuf.toString() );
    
%>

