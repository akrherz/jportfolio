<%
plogger.report("AALBORG dialog method:"+callMethod );
switch ( callMethod.charAt(0) ) {
	case 'o':
		plogger.report("::"+ STRidnum +"::");
		pageContent.append( jdot.readMore(thisUser, STRidnum, "0", thisUser.getDialogSecurity(), thisPageURL) );
	break;
	case 'e':
		pageContent.append( jdot.treeMessages(thisUser, skipNum, threadID, thisPageURL) );
	break;	
	case 'i':
		pageContent.append( jdot.servletInfo() );
	break;
	case 'p':
		pageContent.append( jdot.postMessage(threadID, STRidnum, thisPageURL) );
	break;  
	case 'n':
		pageContent.append( jdot.newThread(thisUser, thisPageURL) );
	break;
	
	case 'r': // We are reading more of a thread
		pageContent.append( jdot.readMore(thisUser, STRidnum, skipNum, thisUser.getDialogSecurity(), thisPageURL) );
	break;
	default:	
		pageContent.append( jdot.bodyItems(thisUser, skipNum, thisPageURL) );
//		System.err.println("AhoyBack!");
	break; 
	case 's':
		if (searchStr != null) {
			pageContent.append("<H3>Search Results for "+searchStr+"<BR>\n");
			pageContent.append( jdot.execSearch(thisUser, searchStr, searchCol, skipNum, thisPageURL) );
		} else 
			pageContent.append("You need to specific something to search on.<BR>\n");
	break;
        case 'q':
               /** Set the session var for this Message */
               session.setAttribute("sMessage", myMessage);
               myMessage.setUser( thisUser );
               pageContent.append( jdot.inputPost(myMessage, thisPageURL) );
        break;
        case 'f':
		myMessage = (portfolioMessage)session.getAttribute("sMessage");
	  	pageContent.append( jlib.topBox("Results of Your Posting:") );
	
		if (myMessage.getBody() != null && myMessage.getSubject() != null){    
		try { 
			pageContent.append( jdot.inputPostFinal(myMessage, thisPageURL) );
				
		} catch(Exception ex) {
			plogger.report("inputPost Exception caught.\n"+ex);
			ex.printStackTrace();
			pageContent.append("<p>Error in posting to database!" + ex + "<p>\n");
			pageContent.append("<a href='"+thisPageURL+"?mode=d'>Access the main page</a>");
		}
		} else{
			pageContent.append("<P>Post did not work, sorry.\n");
		}
		
		pageContent.append("<H4><a href='"+thisPageURL+"?mode=d'>List Discussion Topics</a></H4></font>");
		pageContent.append( jlib.botBox() );
		session.removeAttribute("sMessage");
	break;
} // End of switch()


%>
