<%
/**
 * create_post.jsp
 *  - Input a new trouble ticket
 */
%>

<%@include file='../setup.jsp'%>
<%@include file='../include/header.jsp'%>
<%@include file='links.jsp'%>
<%@ page import="com.oreilly.servlet.*" %>
<%@ page import="java.io.*" %>


<%
 String comments = (String)request.getParameter("comments");
 String subject = (String)request.getParameter("subject");
 String sensor = (String)request.getParameter("sensor");

 /** Insert TT */
 dbInterface.updateDB("INSERT into tt_base(portfolio, s_mid, subject, "
  +" status, author, sensor)"
  +" VALUES ('"+ thisUser.getPortfolio() +"', '"+ s_mid +"', '"
  + stringUtils.cleanString(subject)+"', 'OPEN', '"+thisUser.getUserID()+"'"
  +",'"+ sensor +"' ) ");

 /** Call back the id of this TT */
 ResultSet ttt = dbInterface.callDB("SELECT last_value from tt_base_id_seq");
 ttt.next();
 String tt_id = ttt.getString("last_value");

 /** Insert the first comment */
 dbInterface.updateDB("INSERT into tt_log(portfolio, s_mid, author, status_c "
  +", comments, tt_id) VALUES ('"+ thisUser.getPortfolio() +"', "
  +" '"+ s_mid +"', '"+ thisUser.getUserID() +"', 'OPEN', "
  +" '"+ stringUtils.cleanString( comments ) +"', "+ tt_id +" )");

    String emailCheck = (String)request.getParameter("email");
   if (emailCheck.equalsIgnoreCase("yes") ){
      ResultSet smeta = dbInterface.callDB("SELECT * from iem_sites WHERE "
       +" portfolio = '"+ thisUser.getPortfolio() +"' and s_mid = '"+ s_mid +"' ");
      smeta.next();
      String siteName = smeta.getString("s_name");
      ResultSet contacts = dbInterface.callDB("SELECT * from iem_site_contacts "
       +" WHERE s_mid = '"+ s_mid +"' and email IS NOT NULL");
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
              msg.setSubject("[TT:"+ tt_id +":"+ s_mid +"] - "+ stringUtils.cleanString(subject) );
              PrintStream mout = msg.getPrintStream();
              mout.println( message );
              msg.sendAndClose();
              plogger.report("Email sent to:" + emailAddr );
          } catch(Exception ex){
              plogger.report("Could Not send email!");
              ex.printStackTrace();
          }
        } // End of while()
    } // End of email Change check



%>


<% response.setHeader("Refresh", "0; URL=details.jsp?tt_id="+tt_id); %>

<%= jlib.footer() %>
