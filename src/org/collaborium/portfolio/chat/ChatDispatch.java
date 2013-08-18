package org.collaborium.portfolio.chat;
//
// Portfolio Chat client, 
//

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.lang.*;
import java.lang.String.*;
import java.sql.*;
import java.text.*;
import org.collaborium.portfolio.*;

public class ChatDispatch extends HttpServlet {

  
    public static final String TITLE = "Chat Environment";
    
    
  public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		  doGet(request, response); //  :)
	}  

  public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
	
	StringBuffer sideContent = new StringBuffer();
	StringBuffer pageContent = new StringBuffer();	
	
	response.setContentType("text/html");
	PrintWriter out = response.getWriter();
		
	// start to put in some session tracking info...
	HttpSession session = request.getSession(true);
	
	// session ID
	portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
	if (thisUser == null) response.setHeader("Refresh", "0; URL=/jportfolio");
	
	String callMethod = request.getParameter("mode"); // mode we are called in
	String method = request.getParameter("method"); // request chat method of interaction

	if (callMethod == null) callMethod = "x";  // let us drop to the the default

	
	// set up our inital case switching 
	// d == default
	// a == authenticate(d)
	switch ( callMethod.charAt(0) ) 
            {
		//default:
		//	out.println(intro());
		//	out.println(commands(user));
		//break;
		default:
			jlib.addUser(thisUser.getUserID() , "chat");
			pageContent.append( body(thisUser, method) );
		break;
		case 'a':
			response.setHeader("Refresh", "0; URL=./jportfolio");
		break;
	} // End of switch()
	
	sideContent.append(makeSide(thisUser, method) );
	
	out.println( jlib.header(thisUser, TITLE, "chat") );
	
	out.println( jlib.makePage( sideContent.toString(), pageContent.toString() ) );
	
	
	out.println( jlib.footer());
  } // End of doGet()

    
  public String makeSide(portfolioUser thisUser, String method)
  {
	StringBuffer theBuffer = new StringBuffer();
        
	theBuffer.append("<P>\n");
	theBuffer.append( jlib.currentUsers(thisUser));
	theBuffer.append("<P>\n");
	
	
	return theBuffer.toString();
	
  } // End of makeBody()
  
  // One of the main test of jsib will be a test of the applet to server communication.  Though
  // note erequired for this the testing of subapps to Portfolio issues, it does increase the 
  // range of issue involved.
  public String body( portfolioUser thisUser, String method)
  {
	
	StringBuffer myBuffer = new StringBuffer();
        
	myBuffer.append( jlib.topBox("Instructions:") );
	
	myBuffer.append("<font color=\"green\">This is a simple chat applet. You join the discussion in progress when"
		+" you initially load the page.  This means that the prior discussion is not visible to you.<BR>"
		+" Remember that <b>anyone</b> can be watching!!");
	
	myBuffer.append( jlib.botBox() );
	myBuffer.append("<P>\n");
	myBuffer.append( jlib.topBox("Applet Chat Interface:") );
		
	String applet = "HttpChatApplet";

	myBuffer.append("<APPLET CODE=" + applet + " CODEBASE=\"/jportfolio/chat\" " +
		"WIDTH=500 HEIGHT=370>");
	if (thisUser != null)
		myBuffer.append("<PARAM NAME=user VALUE=\"" + thisUser.getUserID() + "\">");
		
	myBuffer.append("</APPLET>");
		
	myBuffer.append( jlib.botBox() );
        
	return myBuffer.toString();
	
  } // End of body()      
  
  
    
} // End of chatDispatchv2 classs()
