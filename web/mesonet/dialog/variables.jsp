<%

String callMethod = request.getParameter("mode");
String STRidnum = request.getParameter("idnum");   		// used for the reply et al.
String threadID = request.getParameter("threadid");		// used for the reply et al.
String skipNum = request.getParameter("skipNum");     		// number of messages to skip
String searchStr = request.getParameter("searchStr");		// if the user happens to want to search
String oidid = request.getParameter("oidid");    		// used for the reply et al.
String link = request.getParameter("link");

String subject = request.getParameter("subject");
String body = request.getParameter("body");
String searchCol = request.getParameter("searchCol");
String postType = request.getParameter("type");
String replyAuthor = request.getParameter("replyAuthor");
String type = request.getParameter("type");
String postedDialogType = request.getParameter("dialogType");

if (postedDialogType != null ) {
  thisUser.setDialogSecurity( postedDialogType );
}

portfolioMessage myMessage = new portfolioMessage();
myMessage.setSubject( request.getParameter("subject") );
myMessage.setBody( request.getParameter("body") );
myMessage.setThreadID( request.getParameter("threadid") );
myMessage.setidnum( request.getParameter("idnum") );
myMessage.setReplyAuthor( request.getParameter("replyAuthor") );
myMessage.setSecurity( request.getParameter("dialogType") );
myMessage.setClassification( request.getParameter("type") );
myMessage.setAuthor( thisUser.getUserID() );
myMessage.setAuthorName( thisUser.getRealName() );
myMessage.setPortfolio( thisUser.getPortfolio() );
myMessage.setGID( thisUser.getGroupID() );
myMessage.setLink( request.getParameter("link") );
myMessage.setTopicid( request.getParameter("topicid") );

%>
