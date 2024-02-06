<%

String callMethod = request.getParameter("mode");
String STRidnum = request.getParameter("idnum");   		// used for the reply et al.
String threadID = request.getParameter("threadid");		// used for the reply et al.
String skipNum = request.getParameter("skipNum");     		// number of messages to skip
String searchStr = request.getParameter("searchStr");		// if the user happens to want to search
String oidid = request.getParameter("oidid");    		// used for the reply et al.
String postedDialogType = request.getParameter("dialogType");

String subject = request.getParameter("subject");
String body = request.getParameter("body");
String searchCol = request.getParameter("searchCol");
String postType = request.getParameter("type");
String replyAuthor = request.getParameter("replyAuthor");
String type = null;

%>
