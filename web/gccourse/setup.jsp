<%@ page import="org.collaborium.portfolio.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.collaborium.util.*" %>
<%
portfolioUser thisUser = (portfolioUser)session.getAttribute("User");

/* Now, we need to do some auto stuff for folks switching Portfolios */
if ( thisUser != null && thisUser.getPortfolio() != null ) {

    /** Okay, user may not be logged into this portfolio */

  if (! thisUser.getPortfolio().equals("gcp2003") &&
      ! thisUser.getPortfolio().equals("gcp2002") &&
      ! thisUser.getPortfolio().equals("gcp2004") &&
      ! thisUser.getPortfolio().equals("gcp2005") &&
      ! thisUser.getPortfolio().equals("gcp2006") &&
      ! thisUser.getPortfolio().equals("gcp2007") &&
      ! thisUser.getPortfolio().equals("gcde2002") &&
      ! thisUser.getPortfolio().equals("gcde2003") &&
      ! thisUser.getPortfolio().equals("gcp2001") ) {

     if (jlib.isMember( thisUser.getUserID(), "gcp2007" ) ){
        thisUser.setPortfolio("gcp2007");
        if (jlib.isAdmin(thisUser.getUserID(), "gcp2007") )
           thisUser.setIsAdmin( Boolean.TRUE );
        String gID = jlib.getGID(thisUser.getUserID() , "gcp2007");
        thisUser.setGroupID( gID );
     }
   }
}
%>
