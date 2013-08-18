<%
/**
 * selfAssess.jsp  
 *  Program to allow students to edit their self assessments.
 */
%>

<%@ page import="org.collaborium.portfolio.*" %> 
<%@ page import="org.collaborium.portfolio.jdot.*" %>
<%@ page import="java.sql.*" %>

<%
  portfolioUser thisUser = null;

  try{
    thisUser = (portfolioUser)session.getAttribute("User");
  } catch(Exception ex) {
    plogger.report("Problem in Self Assessment");
    ex.printStackTrace();
  }

  if (thisUser == null) {
    response.setHeader("Refresh", "0; URL=/jportfolio/gccourse/index.jsp");
  }

  String thisBlock = (String)request.getParameter("blockid");
  if (thisBlock == null){ thisBlock = "1"; }

  String mode = (String)request.getParameter("mode");
  String newThreadID = null;
  boolean closed = false;
  ResultSet tester = null;

  portfolioMessage myMessage = null;
  portfolioMessage myMessage2 = null;

  /** We are making a post eh? */
   try{
      ResultSet maxth = dbInterface.callDB("select MAX (idnum)+1 as result "
	+" from dialog WHERE idnum > 10000 and idnum < 20000");
      maxth.next();
      newThreadID = maxth.getString("result");

      tester = dbInterface.callDB("select * from dialog WHERE "
       +" username = '"+ thisUser.getUserID() +"' and "
       +" portfolio = '"+ thisUser.getPortfolio() +"' and security = 'private' and "
       +" topicid = 'self"+ thisBlock+"' ");
      if (tester.next() ){
        closed = true;
      }

   } catch(Exception ex){
      plogger.report("Problem in Self Assess");
      ex.printStackTrace();
   } 
   myMessage = new portfolioMessage();
   myMessage.setSubject("Self Assessment: "+ thisUser.getRealName() );
   myMessage.setBody( request.getParameter("body") );
   myMessage.setThreadID( newThreadID );
   myMessage.setidnum( newThreadID );
   myMessage.setReplyAuthor("gstakle");
   myMessage.setSecurity("private");
   myMessage.setClassification("self"+ thisBlock );
   myMessage.setAuthor( thisUser.getUserID() );
   myMessage.setAuthorName( thisUser.getRealName() );
   myMessage.setPortfolio( thisUser.getPortfolio() );
   myMessage.setGID( thisUser.getGroupID() );
   myMessage.setTopicid("self"+ thisBlock );
   myMessage.setUser( thisUser );

   if (closed){
     myMessage2 = new portfolioMessage(tester);
     myMessage2.setUser( thisUser );

   } else if (mode != null) {
     myMessage.commitMessage();
     myMessage2 = myMessage;
     closed = true;
   }

%>

<%= jlib.basicHeader(thisUser, "Self Assessment") %>

<div align='center'> 
  <a href="/jportfolio/gccourse/">GCP Portfolio Home</a><br>
  <a href="/jportfolio/gccourse/block/index.jsp?blockid=1">Block 1</a> &nbsp; <b>|</b> &nbsp;
  <a href="/jportfolio/gccourse/block/index.jsp?blockid=2">Block 2</a> &nbsp; <b>|</b> &nbsp;
  <a href="/jportfolio/gccourse/block/index.jsp?blockid=3">Block 3</a> 
</div>



<h3>Self Assessment for Block <%= thisBlock %></h3>

<p>Perhaps you would like to <a target="new" href="blockPosts.jsp?blockid=<%= thisBlock %>">view your postings</a> for this block.
<br>or perhaps, you would like to review the <a target="new"
 href="http://www.meteor.iastate.edu/gccourse/SAssess.html">Self Assessment</a>
 guidlines.</p>

<% if (closed) { %>

<p>Here is your Posting.</p>

<p><table><tr><td>
<%= myMessage2.printStandard() %>
</td></tr></table>

<% } else { %>

<p><form method="POST" action="selfAssess.jsp">
<input type="hidden" name="mode" value="a">
<input type="hidden" name="blockid" value="<%= thisBlock %>">

<p>Self Assessment:<br>
<textarea NAME='body' WRAP='Virtual' ROWS="25" COLS="60"></textarea>

<p><input type="submit" value="Post Assessment">

</form>

<% } %>

</td></tr></table>
</body></html>
