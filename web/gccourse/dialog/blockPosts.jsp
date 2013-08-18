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
	threadSpec = " threadid > 14105 and threadid < 14213 ";
  } else if (blockID.equalsIgnoreCase("2") ){
	threadSpec = " threadid > 14212 and threadid < 20984 ";
  } else {
	threadSpec = " threadid > 10984 ";
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
