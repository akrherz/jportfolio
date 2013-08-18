<%@ page import="org.collaborium.portfolio.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.collaborium.util.*" %>
<%
portfolioUser thisUser = (portfolioUser)session.getAttribute("User");

/* Now, we need to do some auto stuff for folks switching Portfolios */
if ( thisUser != null && thisUser.getPortfolio() != null ) {

    /** Okay, user may not be logged into this portfolio */

  if (! thisUser.getPortfolio().equals("aal2003") &&
      ! thisUser.getPortfolio().equals("aal2002") &&
      ! thisUser.getPortfolio().equals("aal2004") &&
      ! thisUser.getPortfolio().equals("aal2005") &&
      ! thisUser.getPortfolio().equals("aal2001") ) {

     if (jlib.isMember( thisUser.getUserID(), "aal2005" ) ){
        thisUser.setPortfolio("aal2005");
        if (jlib.isAdmin(thisUser.getUserID(), "aal2005") )
           thisUser.setIsAdmin( Boolean.TRUE );
        String gID = jlib.getGID(thisUser.getUserID() , "aal2005");
        thisUser.setGroupID( gID );
     }
   }
}
%>
