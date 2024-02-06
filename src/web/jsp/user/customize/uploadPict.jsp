<%@ page import="org.collaborium.portfolio.*" %>
<%@ page import="com.oreilly.servlet.multipart.*" %>
<%@ page import="java.io.File" %>

<%
	portfolioUser thisUser = (portfolioUser)session.getAttribute("User");

	File dir = new File("webapps/jportfolio/FILES/"+ thisUser.getUserID() );
	if (! dir.isDirectory() ){
		dir.mkdir();
	}

	File finalFile = new File(dir, "me.gif");
%>

<%= jlib.basicHeader(thisUser, "Upload Picture") %>

<%

try {
	MultipartParser mp = new MultipartParser(request, 10*1024*1024); // 10MB
	Part part;
	while ((part = mp.readNextPart()) != null) {
        	String name = part.getName();

		if (part.isFile()) {
          	   // it's a file part
             	   FilePart filePart = (FilePart) part;
          	   String fileName = filePart.getFileName();
		   String fileType = filePart.getContentType();
          	   if (fileName != null && fileType.equals("image/gif") ) {
            		// the part actually contained a file
            		long size = filePart.writeTo( finalFile );
            		out.println("DEBUG:  file; name=" + name + "; filename=" + fileName +
              		", content type=" + filePart.getContentType() + " size=" + size);
          	   } else {
			out.println("<B>Error:</B> Sorry, only GIFs are supported at this time.");
		  }
	  	} 
	  }
	  
        } catch (Exception ex) {
      		plogger.report(ex +"error reading or saving file");
	}



%>

<b>Personal Information Editor:</b> &nbsp; &nbsp;
  <a class="commands" href="editBio.jsp">Bio Sketch</a>  &nbsp; <b>|</b> &nbsp;
  <a class="commands" href="editPict.jsp">Picture</a>

<P>Upload Finished:

<BR>Here is what you supposedly look like.

<BR><img src="/jportfolio/FILES/<%= thisUser.getUserID() %>/me.gif">

<P>Return to your portfolio <a href="/jportfolio/users/<%= thisUser.getUserID() %>">homepage</a>


<%= jlib.basicFooter() %>
