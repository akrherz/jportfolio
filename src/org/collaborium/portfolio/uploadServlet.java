/**
 * Copyright 2001 Iowa State University
 * jportfolio@collaborium.org
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

/*
 * uploadServlet
 *	Application that handles file upload for the Portfolio Engine
 * @author Daryl Herzmann 06 Jul 2001
 */
package org.collaborium.portfolio;
 
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import org.collaborium.portfolio.*;

import com.oreilly.servlet.multipart.*;

public class uploadServlet extends HttpServlet {
  private File dir;
  
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    // Read the uploadDir from the servlet parameters
    String dirName = config.getInitParameter("uploadDir");
    if (dirName == null) {
      throw new ServletException("Please supply uploadDir parameter");
    }
    dir = new File(dirName);
    if (! dir.isDirectory()) { 
      throw new ServletException("Supplied uploadDir " + dirName +
                                 " is invalid");
    }
  }
  
 /**
  * Handle Http Method Post to the Servlet
  */
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    /** Standard Servlet Stuff to get this going **/
    PrintWriter out = response.getWriter();
    response.setContentType("text/html");
    
    /** Go get info about user from session **/
    HttpSession session = request.getSession(true);
    portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
   
    File userDir = new File(dir, thisUser.getUserID() );
    if ( ! userDir.isDirectory() ){
    	userDir.mkdir();
    }
    
    
    /** Allow this Upload **/
    if (thisUser != null && thisUser.isPortfolioUser() ) {
	out.println( jlib.basicHeader(thisUser, "Portfolio Upload" ) );
  
	
	try {
	   MultipartParser mp = new MultipartParser(request, 10*1024*1024); // 10MB
      	   Part part;
           while ((part = mp.readNextPart()) != null) {
        	String name = part.getName();

        	if (part.isParam()) {
         	 	// it's a parameter part
          	   ParamPart paramPart = (ParamPart) part;
          	   String value = paramPart.getStringValue();
          	   out.println("param; name=" + name + ", value=" + value);
        	}
		else if (part.isFile()) {
          	   // it's a file part
             	   FilePart filePart = (FilePart) part;
          	   String fileName = filePart.getFileName();
		   String fileType = filePart.getContentType();
          	   if (fileName != null && fileType.equals("image/gif") ) {
            		// the part actually contained a file
            		long size = filePart.writeTo(userDir);
            		out.println("file; name=" + name + "; filename=" + fileName +
              		", content type=" + filePart.getContentType() + " size=" + size);
          	   } else { 
            		// the field did not contain a file
            		out.println("file; name=" + name + "; EMPTY");
          	   }
	  
	  	} 
	  }
	  
        } catch (IOException lEx) {
      		plogger.report(lEx +"error reading or saving file");
	}
	
	
    	out.println( jlib.basicFooter() );
    } else {
    	out.println("ACCESS ERROR");
    }
    
    out.flush();

  } // End of doPost()
}


