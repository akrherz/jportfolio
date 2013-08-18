<%@ page import="org.collaborium.portfolio.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.collaborium.util.*" %>
<%
portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
%>

<%
  /** Possible params that could be passed to this page */
  String topicid = (String)request.getParameter("topicid");
  String mode = (String)request.getParameter("mode");
  String s_topicid = (String)session.getAttribute("topicid");
  String s_mode = (String)session.getAttribute("mode");
  
  /** If there is nothing in the request and nothing in the
   * session, this request is probably invalid */
  if (topicid == null && s_topicid == null){
  	out.println("<b>How did you get here?</b>\n"
	  +"<br>Please email akrherz@iastate.edu your referring page\n");
  } 
  
  /** If the userid is null, then we should save the request and then
   * get them logged in!  */
  else if (thisUser == null){
  	session.setAttribute("mode", mode);
	session.setAttribute("topicid", topicid);
    session.setAttribute("comebackURL", "/jportfolio/wrap/quiz.jsp");
  	response.setHeader("Refresh", "0; URL=/jportfolio/login.jsp");
  } 
  
  /** Now that we have the user logged in, we had better log them 
   * into a Portfolio. */
  else if (thisUser.getPortfolio() == null) {
    session.setAttribute("comebackURL", "/jportfolio/wrap/quiz.jsp");
   	response.setHeader("Refresh", "0; URL=/jportfolio/login.jsp");
  } 
  
  /** Hey!  They are okay!  Yeah!  */
  else {
  	if (topicid == null) {
		topicid = s_topicid;
	}
	if (mode == null) {
		mode = s_mode;
	}
	if (jlib.isMember( thisUser.getUserID(), "gcp2005" ) ){
        thisUser.setPortfolio("gcp2005");
        if (jlib.isAdmin(thisUser.getUserID(), "gcp2005") )
           thisUser.setIsAdmin( Boolean.TRUE );
        String gID = jlib.getGID(thisUser.getUserID() , "gcp2005");
        thisUser.setGroupID( gID );
     }

  	String baseURL = thisUser.getHome();
	String portfolio = thisUser.getPortfolio();
	String qid = "";
	try{
	  ResultSet rs = dbInterface.callDB("SELECT quiznum from quizes "
	   +" WHERE topicid = '"+ topicid +"' and portfolio = '"+ portfolio +"' ");
	  if ( rs.next() ){
	    qid = rs.getString("quiznum");
	  }
	
	} catch(Exception ex){
	  plogger.report("Hi");
	}


	
	  response.setHeader("Refresh", "0; "
	  	+"URL=/jportfolio/servlet/jquiz?qid="+qid+"&mode="+mode);
  }

%>
