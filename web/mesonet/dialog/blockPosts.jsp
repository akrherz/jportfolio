<% 
 /**
  * blockPosts.jsp
  *   prints out a listing of block postings for a user
  */
%>

<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.collaborium.portfolio.*" %>

<%
  portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
  if (thisUser == null) {
    response.setHeader("Refresh", "0; URL=/jportfolio/gccourse/index.jsp");
  }

  String blockID = request.getParameter("blockid");
  if (blockID == null){ blockID = "1"; }

  String threadSpec = "";
  if (blockID.equalsIgnoreCase("1") ){
	threadSpec = " threadid > 10753 and threadid < 10827 ";
  } else if (blockID.equalsIgnoreCase("2") ){
	threadSpec = " threadid > 11754 and threadid < 11827 ";
  } else {
	threadSpec = " threadid > 12754 and threadid < 12827 ";
  }


  out.println( jlib.basicHeader(thisUser, "Block Posts") );

  portfolioMessage myMessage = null;

  try{
  ResultSet rs = dbInterface.callDB("select * from dialog WHERE "
    + threadSpec 
    +" and portfolio = '"+ thisUser.getPortfolio() +"' "
    +" and security = 'public' and username = '"+ thisUser.getUserID() +"' "
    +" ORDER by date ");

  out.println("<table>\n");
  while ( rs.next() ){
    myMessage = new portfolioMessage(thisUser, rs);

    out.println( myMessage.printStandard() );

  } // End of while
  out.println("</table>\n");

  } catch(Exception ex){
    plogger.report("Problem in blockPosts.jsp");
    ex.printStackTrace();
  }

%>

</td></tr></table>
</body></html>
