<%
/**
 * threadCat.jsp
 *   - anonymously print out a dialog thread.
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
String threadID = (String)request.getParameter("threadID");

StringBuffer sbuf = new StringBuffer();
	

ResultSet threads  = null;
ResultSet threads2 = null;
ResultSet threads3 = null;
ResultSet threads4 = null;
ResultSet threads5 = null;
ResultSet threads6 = null;
ResultSet threads7 = null;

String SQL  = null;
String SQL2 = null;
String threadSpec = "threadid = "+threadID+" and idnum < 100000 ";
String STRidnum = "";

// We need to build the SQL strings, one is for the count and the other is
// for the actual query.
SQL =  "SELECT * from dialog WHERE "+ threadSpec 
	+" and security = '"+ thisUser.getDialogSecurity() +"' order by idnum ASC";

// Lets make the query and figure out 
threads = jlib.callDB(SQL);

String commonString = "security = '"+ thisUser.getDialogSecurity() +"' order by idnum";

sbuf.append("<table width=600>");
sbuf.append("<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>"
	+"<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td></td></tr>\n");

while( threads.next() ) {
  STRidnum = threads.getString("idnum"); // This shows how to get the value by name
  sbuf.append( jdot.anonPrint(thisUser, threads, "1", "7") );
	
  String startNum = STRidnum +"0000";

  threads2 = jlib.callDB("SELECT * from dialog WHERE idnum > "+startNum+" ::numeric"
	+" and idnum < ("+startNum+" +10000)::numeric and "+commonString );

  while(threads2.next()) {
    STRidnum = threads2.getString("idnum");
    sbuf.append( jdot.anonPrint(thisUser, threads2, "2", "6") );
			
    startNum = STRidnum +"0000";
			
    threads3 = jlib.callDB("SELECT * from dialog WHERE idnum > "+startNum+"::numeric "
	+" and idnum < ("+startNum+"+10000)::numeric and "+commonString);
		

    while( threads3.next() ) {
      STRidnum = threads3.getString("idnum"); // This shows how to get the value by name
      sbuf.append( jdot.anonPrint(thisUser, threads3, "3", "5") );
				
      startNum = STRidnum +"0000";
			
      threads4 = jlib.callDB("SELECT * from dialog WHERE idnum > "+startNum+"::numeric "
	+" and idnum < ("+startNum+"+10000)::numeric and "+commonString );

      while( threads4.next() ) {
	STRidnum = threads4.getString("idnum"); // This shows how to get the value by name
	sbuf.append( jdot.anonPrint(thisUser, threads4, "4", "4") );
					
	startNum = STRidnum +"0000";
			
	threads5 = jlib.callDB("SELECT * from dialog WHERE idnum > "+startNum+"::numeric "
		+" and idnum < ("+startNum+"+10000)::numeric and "+commonString );

	while( threads5.next() ) {
	  STRidnum = threads5.getString("idnum"); // This shows how to get the value by name
	  sbuf.append( jdot.anonPrint(thisUser, threads5, "5", "3") );
						
	  startNum = STRidnum +"0000";
	
	  threads6 = jlib.callDB("SELECT * from dialog WHERE idnum > "+startNum+"::numeric "
		+" and idnum < ("+startNum+"+10000)::numeric and "+commonString );

	  while( threads6.next() ) {
	    STRidnum = threads6.getString("idnum"); // This shows how to get the value by name
	    sbuf.append( jdot.anonPrint(thisUser, threads6, "6", "2") );
		 
	    startNum = STRidnum +"0000";
            threads7 = jlib.callDB("SELECT * from dialog WHERE idnum > "+startNum+"::numeric "
              +" and idnum < ("+startNum+"+10000)::numeric and "+commonString );
            while( threads7.next() ){
              STRidnum = threads7.getString("idnum");
              sbuf.append( jdot.anonPrint(thisUser, threads7, "7", "1") );
            } // while7
            threads7.close();
	  } // while6
	  threads6.close();
	} // while5
	threads5.close();
      }  // while4
      threads4.close();
    } // End of while3()
    threads3.close();
  } // End of while2()
  threads2.close();
} //End of while1()
threads.close();
	    
sbuf.append("</table>");
out.println( sbuf.toString() );
    


%>

