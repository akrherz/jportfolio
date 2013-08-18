<%
/**
 * details.jsp
 *  - Print out details about this TT
 * 12 Jul 2002 - Add ability to email site contacts when a change is made
 */
%>
<%@include file='../setup.jsp'%>
<%@include file='../include/header.jsp'%>
<%@include file='links.jsp'%>
<%@ page import="com.oreilly.servlet.*" %>
<%@ page import="java.io.*" %>

<p><font class="bluet">Trouble Ticket Details</font>

<%
  /** Lets see if anything new has been posted */
  String status = (String)request.getParameter("status");
  String tt_id = (String)request.getParameter("tt_id");
  String tt_sensor = (String)request.getParameter("tt_sensor");
  mTT thisTT = new mTT(thisUser.getPortfolio(), tt_id);
  s_mid = thisTT.getS_mid();
  if (tt_sensor == null){ tt_sensor = thisTT.getSensor(); }

  if (status != null) { // We have a post to do...
    String comments = (String)request.getParameter("comments");
    dbInterface.updateDB("INSERT into tt_log(portfolio, s_mid, author, "
     +" status_c, comments, tt_id) VALUES ('"+ thisUser.getPortfolio() +"', "
     +" '"+ s_mid +"', '"+ thisUser.getUserID() +"', '"+ status +"', "
     +" '"+ stringUtils.cleanString( comments ) +"', "+ tt_id +")"); 
    /** Change status of tt and update timestamp */
    dbInterface.updateDB("UPDATE tt_base SET last = 'NOW'::timestamp "
     +", sensor = '"+ tt_sensor +"' WHERE id = "+ tt_id );
    if (! status.equalsIgnoreCase("OKAY") ){ // Update status
      dbInterface.updateDB("UPDATE tt_base SET status = '"+ status +"' "
      +" WHERE id = "+ tt_id );
      thisTT.setStatus( status );
    }
    if (status.equalsIgnoreCase("CLOSED") ){  // Close TT
      dbInterface.updateDB("UPDATE tt_base SET closed = 'NOW'::timestamp "
       +" WHERE id = "+ tt_id );
      thisTT.setStatus( status );
    }
    String emailCheck = (String)request.getParameter("email");
    if (emailCheck.equalsIgnoreCase("yes") ){
      ResultSet smeta = dbInterface.callDB("SELECT * from iem_sites WHERE "
       +" portfolio = '"+ thisUser.getPortfolio() +"' and s_mid = '"+ s_mid +"' ");
      smeta.next();
      String siteName = smeta.getString("s_name");

      ResultSet contacts = dbInterface.callDB("SELECT * from iem_site_contacts "
       +" WHERE s_mid = '"+ s_mid +"' and email IS NOT NULL");
      if ( contacts.next() ){
        contacts.previous();
        while ( contacts.next() ) {
          String emailAddr = contacts.getString("email");
          String message = "\n"
           +"! Iowa Environmental Mesonet - IEM_Tracker Report \n"
           +"!   Updated Trouble Ticket ID: "+ tt_id +"\n"
           +"!   Ticket for station: ["+ s_mid +"] "+ siteName +"\n"
           +"!   Comments made by: "+ thisUser.getRealName() +" "
             +"("+ thisUser.getEmailAddress() +") \n"
           +"!_____________________________________________________\n"
           +" "+ comments +" \n\n"
           +"!____________________________________________________\n";
          try{
              MailMessage msg = new MailMessage("localhost");
              msg.from("akrherz@iastate.edu");
              msg.to(emailAddr);
              msg.setSubject("[TT:"+ tt_id +":"+ s_mid +"] - "+ thisTT.getSubject() );
              PrintStream mout = msg.getPrintStream();
              mout.println( message );
              msg.sendAndClose();
              plogger.report("Email sent to:" + emailAddr );
          } catch(Exception ex){
              plogger.report("Could Not send email!");
          }
        } // End of while()
      } // End of if for DB results
    } // End of email Change check
  }  // End of if for the POST

%>

<%= thisTT.printLongView() %>


<div id="login">

<h3>Make Additonal Comment</h3>

<form method="post" action="details.jsp">
<input type="hidden" name="tt_id" value="<%= tt_id %>">
<p><b>Sensors Flagged:</b><br />
 <input type="text" name="tt_sensor" value="<%= tt_sensor %>">
<p><b>Change Status:</b><br>
<select name="status">
  <option value="OKAY">Leave As Is
  <option value="OPEN">OPEN
  <option value="CLOSED">CLOSED
  <option value="SENSOR_SENT">Sensor sent to site
  <option value="SITE_VISIT">Requires Site Visit
  <option value="NO_SENSOR">Need replacement to ship
  <option value="WAIT_THEM">Waiting for action at site
  <option value="WAIT_SENSOR_BACK">Waiting for old sensor to come back
  <option value="WAIT_TXWX">Sent to TWI-Awaiting Return
  <option value="NEEDINFO">NEEDINFO
  <option value="WORKS4ME">WORKS4ME
  <option value="NOTABUG">NOTABUG
</select>

<p><b>Enter your comments:</b><br>
<textarea name="comments" cols=70 rows=10 wrap="virtual"></textarea>

<p><b>Email Site Contacts with your change?</b><br>
<input type="radio" name="email" value="yes">Yes
<br><input type="radio" name="email" value="no" CHECKED>No

<p><input type="submit" value="Enter Comments">
<input type="reset" value="Reset Form">
</form>

</div>

<p><font class="bluet">Comments:</font>
<%= thisTT.printHistory() %>

<%= jlib.footer() %>
