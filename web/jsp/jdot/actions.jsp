<%
System.err.println("I am here now with "+callMethod );
switch ( callMethod.charAt(0) ) {
	case 'o':
		pageContent.append( myJdot.readOne(STRidnum, dialogType, thisPageURL) );
	break;
	case 'b':
		pageContent.append( myJdot.selfAccess(thisPageURL) );
	break;
	case 'e':
		pageContent.append( myJdot.treeMessages(portfolio, skipNum, userID, groupID, dialogType, threadID, isAdmin, thisPageURL) );
	break;	
	case 'i':
		pageContent.append( myJdot.servletInfo() );
	break;
	case 'p':
		pageContent.append( myJdot.postMessage(threadID, STRidnum, thisPageURL) );
	break;  
	case 'n':
		pageContent.append( myJdot.newThread(thisUser, thisPageURL) );
	break;
	
	case 'r': // We are reading more of a thread
		pageContent.append( myJdot.readMore(STRidnum, skipNum, dialogType, thisPageURL) );
	break;
	
	case 'y':
		if ( isAdmin.booleanValue() ) {
			pageContent.append("Deleting Message");
			myJdot.deleteMessage(threadID, STRidnum, oidid);
			pageContent.append( myJdot.adminOptions(thisPageURL) );
		
		} else {
			pageContent.append("Unsucessful Admin Login, sorry.<BR>\n");
			pageContent.append( myJdot.bodyItems(skipNum, portfolio, dialogType, userID, groupID, isAdmin,thisPageURL));
		}
	break;
	default:	
		System.err.println("skipNUm   " + skipNum );
		System.err.println("portfolio " + portfolio );
		System.err.println("dialogType" + dialogType );
		System.err.println("userID    " + userID );
		System.err.println("groupID   " + groupID );
		System.err.println("isAdmin" + isAdmin );
		System.err.println("thisPageURL" + thisPageURL );
		
		pageContent.append("<p><font class=\"bodyText\"><b>Discussion Topics...</b></font><br>");
		pageContent.append( myJdot.bodyItems(skipNum, portfolio, dialogType, userID, groupID, isAdmin, thisPageURL) );
		System.err.println("AhoyBack!");
	break; 
	case 's':
		if (searchStr != null) {
			pageContent.append("<H3>Search Results for "+searchStr+"<BR>\n");
			pageContent.append( myJdot.execSearch(searchStr, searchCol, skipNum, portfolio, dialogType, groupID, userID, isAdmin, thisPageURL) );
		} else 
			pageContent.append("You need to specific something to search on.<BR>\n");
	break;
	case 'z':
	  	pageContent.append( jlib.topBox("Results of Your Posting:") );
		if (body != null && subject != null){    
		try { 
			pageContent.append( myJdot.inputPost(userID, name, subject, body, threadID, 0, type, STRidnum, portfolio, postType, replyAuthor, dialogType, groupID, thisPageURL) );
				
		} catch(Exception ex) {
			System.err.println("inputPost Exception caught.\n"+ex);
			ex.printStackTrace();
			pageContent.append("<p>Error in posting to database!" + ex + "<p>\n");
			pageContent.append("<a href='"+thisPageURL+"?mode=d'>Access the main page</a>");
		}
		} else{
			pageContent.append("<P>Post did not work, sorry.\n");
		}
		
		pageContent.append("<H4><a href='"+thisPageURL+"?mode=d'>List Discussion Topics</a></H4></font>");
		pageContent.append( jlib.botBox() );
	break;
} // End of switch()


%>
