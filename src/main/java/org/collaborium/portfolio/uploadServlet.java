/**
 * Copyright 2001 Iowa State University jportfolio@collaborium.org
 *
 * <p>This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * <p>This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * <p>You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

/*
 * uploadServlet
 *	Application that handles file upload for the Portfolio Engine
 * @author Daryl Herzmann 06 Jul 2001
 */
package org.collaborium.portfolio;

import java.io.*;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

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
    if (!dir.isDirectory()) {
      throw new ServletException("Supplied uploadDir " + dirName +
                                 " is invalid");
    }
  }

  /** Handle Http Method Post to the Servlet */
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    /** Standard Servlet Stuff to get this going * */
    PrintWriter out = response.getWriter();
    response.setContentType("text/html");

    /** Go get info about user from session * */
    HttpSession session = request.getSession(true);
    portfolioUser thisUser = (portfolioUser)session.getAttribute("User");

    File userDir = new File(dir, thisUser.getUserID());
    if (!userDir.isDirectory()) {
      userDir.mkdir();
    }

    /** Allow this Upload * */
    if (thisUser != null && thisUser.isPortfolioUser()) {
      out.println(jlib.basicHeader(thisUser, "Portfolio Upload"));

      try {
        // Use Apache Commons FileUpload
        if (ServletFileUpload.isMultipartContent(request)) {
          DiskFileItemFactory factory = new DiskFileItemFactory();
          factory.setSizeThreshold(1024 * 1024);
          ServletFileUpload upload = new ServletFileUpload(factory);
          upload.setSizeMax(10 * 1024 * 1024); // 10MB

          List<FileItem> items = upload.parseRequest(request);
          for (FileItem item : items) {
            String name = item.getFieldName();
            if (item.isFormField()) {
              String value = item.getString();
              out.println("param; name=" + name + ", value=" + value);
            } else {
              String fileName = FilenameUtils.getName(item.getName());
              String fileType = item.getContentType();
              if (fileName != null && "image/gif".equals(fileType)) {
                File uploaded = new File(userDir, fileName);
                item.write(uploaded);
                out.println("file; name=" + name + "; filename=" + fileName +
                            ", content type=" + fileType +
                            " size=" + uploaded.length());
              } else {
                out.println("file; name=" + name + "; EMPTY");
              }
            }
          }
        }
      } catch (Exception lEx) {
        plogger.report(lEx + "error reading or saving file");
      }

      out.println(jlib.basicFooter());
    } else {
      out.println("ACCESS ERROR");
    }

    out.flush();
  } // End of doPost()
}
