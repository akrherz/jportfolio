/**
 * Copyright 2001-2005 Iowa State University jportfolio@collaborium.org
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
package org.collaborium.portfolio.jdot;
/**
 * jdot3.java is the dialog engine for the Portfolio Application Suite It is a
 * frontend to the jdot.java, which contains the methods The dialog engine is in
 * it's purest form here. The 3 in jdot is a version number :)
 *
 * @author Doug Fils fils@collaborium.org
 * @author Daryl Herzmann akrherz@collaborium.org
 */
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.collaborium.portfolio.*;

public class jdot3 extends HttpServlet {

  /**
   * Since the jdot methods can be called from JSP, we need to tell the internal
   * methods where to create links to. Maybe this can be handled differently
   * with an external class??
   */
  String servletHttpBase = jlib.servletHttpBase;

  String thisPageURL = servletHttpBase + "/jdot3";

  /** method to init the servlet */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  } // End of int()

  /** Method to supposedly destroy a servlet */
  public void destroy() {
    plogger.report("jdot3 was just destroyed!");
  } // End of destroy()

  /**
   * Method to handle method GET for the servlet...
   *
   * @param servlet request request
   * @param servlet request response
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    plogger.report("=============  JDOT3 GET() START ========");

    /** Two containers to hold the page text, then we write */
    StringBuffer sideContent = new StringBuffer();
    StringBuffer pageContent = new StringBuffer();

    /** Set up output HTML stream * */
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    // see if we can grab some variable here....
    String callMethod = request.getParameter("mode");
    String STRidnum = request.getParameter("idnum");
    String threadID = request.getParameter("threadid");
    String skipNum = request.getParameter("skipNum");
    String postedDialogType = request.getParameter("dialogType");
    String postedThreadType = request.getParameter("threadType");

    // start to put in some session tracking info...
    HttpSession session = request.getSession(true);
    portfolioUser thisUser = (portfolioUser)session.getAttribute("User");

    if (thisUser == null) {
      response.setHeader("Refresh", "0; URL=./jportfolio");
      callMethod = "z"; // Abort
      session.invalidate();
      return; // abort
    }

    thisUser.setThreadType(postedThreadType);

    if (thisUser.getPortfolio() != null) {
      jlib.addUser(thisUser.getUserID(), "Dialog");
    } else {
      response.setHeader("Refresh", "0; URL=./jportfolio");
      callMethod = "z";
      return; // Abort
    }

    /**
     * If the user posted a value for dialogSecurity, then we had better switch
     * the setting
     */
    if (postedDialogType != null) {
      thisUser.setDialogSecurity(postedDialogType);
    }

    sideContent.append(sideBarItems(thisUser, thisPageURL));

    /**
     * Switch Statement breakdown
     *
     * <p>o | Read more specifying a single message e | Tree Messages view of a
     * threadID i | Print out Servlet Information p | Post a reply to a certain
     * message n | new thread r | readMore of a specific thread z | do Nothing!!
     */
    if (callMethod == null)
      callMethod = "x";

    try {
      switch (callMethod.charAt(0)) {
      case 'o':
        pageContent.append(jdot.readMore(thisUser, STRidnum, "0",
                                         thisUser.getDialogSecurity(),
                                         thisPageURL));
        break;
        //	case 'b':
        //	   pageContent.append( jdot.selfAccess(thisPageURL) );
        //	break;
      case 'e':
        pageContent.append(
            jdot.treeMessages(thisUser, skipNum, threadID, thisPageURL));
        sideContent.append(jdot.dialogLegend());
        break;
      case 'i':
        pageContent.append(jdot.servletInfo());
        break;
      case 'p':
        pageContent.append(jdot.postMessage(threadID, STRidnum, thisPageURL,
                                            thisUser.getDialogSecurity()));
        break;
      case 'n':
        pageContent.append(jdot.newThread(thisUser, thisPageURL));
        break;
      case 'r':
        pageContent.append(jdot.readMore(thisUser, STRidnum, skipNum,
                                         thisUser.getDialogSecurity(),
                                         thisPageURL));
        break;
      case 'z':
        plogger.report("JDOT is aborting, kick user back to jportfolio\n");
        break;
      default:
        pageContent.append(jdot.bodyItems(thisUser, skipNum, thisPageURL));
        sideContent.append(jdot.dialogLegend());
        break;
      } // End of switch()
    } catch (Exception ex) {
      plogger.report("Error jdot3 doGET() " + ex);
      ex.printStackTrace();
    }

    /** Finally send the page back to the user */
    out.println(jlib.header(thisUser, jdot.TITLE, "Dialog"));
    out.println(jlib.makePage(sideContent.toString(), pageContent.toString()));
    out.println(jlib.footer());

    plogger.report("\n--- End of jdot3 ");
  } // End of doGet()

  /**
   * Standard method to handle method POST
   *
   * @param req servlet request
   * @param res servlet response
   */
  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    plogger.report("=============  JDOT3 POST() START ========");

    /** Set up containers to write HTML to */
    StringBuffer sideContent = new StringBuffer();
    StringBuffer pageContent = new StringBuffer();

    /** Setup output for this servlet */
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();

    HttpSession session = req.getSession(true);
    portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
    String searchStr = req.getParameter("searchStr");
    String searchCol = req.getParameter("searchCol");
    String skipNum = req.getParameter("skipNum");
    String callMethod = req.getParameter("mode");

    if (thisUser.getPortfolio() == null) {
      jlib.addUser(thisUser.getUserID(), "Dialog");
    } else {
      res.setHeader("Refresh", "0; URL=./jportfolio");
      callMethod = "z"; // Abort
    }

    portfolioMessage myMessage = new portfolioMessage();
    myMessage.setSubject(req.getParameter("subject"));
    myMessage.setBody(req.getParameter("body"));
    myMessage.setThreadID(req.getParameter("threadid"));
    myMessage.setidnum(req.getParameter("idnum"));
    myMessage.setReplyAuthor(req.getParameter("replyAuthor"));
    myMessage.setSecurity(thisUser.getDialogSecurity());
    myMessage.setClassification(req.getParameter("type"),
                                req.getParameter("mytype"));
    myMessage.setAuthor(thisUser.getUserID());
    myMessage.setAuthorName(thisUser.getRealName());
    myMessage.setPortfolio(thisUser.getPortfolio());
    myMessage.setGID(thisUser.getGroupID());
    myMessage.setLink(req.getParameter("link"));
    myMessage.setTopicid(req.getParameter("topicid"));

    if (skipNum == null)
      skipNum = "0";
    if (myMessage.getSubject() == null)
      myMessage.setSubject("No Subject");

    sideContent.append(sideBarItems(thisUser, thisPageURL));

    if (callMethod == null)
      callMethod = "x";

    plogger.report("Mode called in jdot3 -> " + callMethod);

    /**
     * Switch cases
     *
     * <p>s | search for string z | abort q | preview post f | finalize post
     */
    switch (callMethod.charAt(0)) {
    case 's':
      if (searchStr != null && !searchStr.equals("")) {
        pageContent.append("<H3>Search Results for " + searchStr + "<BR>\n");
        try {
          pageContent.append(jdot.execSearch(thisUser, searchStr, searchCol,
                                             skipNum, thisPageURL));
        } catch (Exception ex) {
          plogger.report("Error Searching dialog " + ex);
          pageContent.append("Problem with your query.  Try again.");
        }
      } else
        pageContent.append(
            "You need to specific something to search on.<BR>\n");
      break;
    case 'z':
      plogger.report("Aborting jdot3 doPost\n");
      break;
    case 'q':
      /** Set the session var for this Message */
      session.setAttribute("sMessage", myMessage);
      myMessage.setUser(thisUser);
      pageContent.append(jdot.inputPost(myMessage, thisPageURL));
      break;
      // -F-  Finalize Post
    case 'f':
      myMessage = (portfolioMessage)session.getAttribute("sMessage");
      pageContent.append(jlib.topBox("Results of Your Posting:"));
      if (myMessage.getBody() != null && myMessage.getSubject() != null) {
        try {
          pageContent.append(jdot.inputPostFinal(myMessage, thisPageURL));
        } catch (Exception ex) {
          plogger.report("inputPost Exception caught.\n" + ex);
          ex.printStackTrace();
          pageContent.append("<p>Error in posting to database!" + ex + "<p>\n");
          pageContent.append("<a href='" + thisPageURL +
                             "?mode=d'>Access the main page</a>");
        }
      } else {
        pageContent.append("<P>Post did not work, sorry.\n");
      }
      pageContent.append(jlib.botBox());
      session.removeAttribute("sMessage");
      break;
    } // End of case()

    out.println(jlib.header(thisUser, jdot.TITLE, "Dialog"));
    out.println(jlib.makePage(sideContent.toString(), pageContent.toString()));
    out.println(jlib.footer());

    plogger.report("\n--- End of jdot3 ");
  } // End of doPost()

  /**
   * Method that prints out a listing of side bar items, should be modified to
   * fit a certain application
   */
  public String sideBarItems(portfolioUser thisUser, String thisPageURL) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(
        jdot.switchDialogType(thisUser.getDialogSecurity(), thisPageURL));
    sbuf.append("<P>\n");
    sbuf.append(jlib.libPortApps());
    sbuf.append("<P>\n");
    sbuf.append(
        jlib.currentUsers(thisUser.getPortfolio(), thisUser.getUserID()));
    sbuf.append("<P>\n");
    sbuf.append(jdot.actionBox(thisUser, thisPageURL));
    sbuf.append("<P>\n");
    sbuf.append(jdot.searchBox(thisPageURL));
    if (thisUser.isAdmin()) {
      sbuf.append("<P>\n");
      sbuf.append(jlib.adminCommands());
    }

    return sbuf.toString();
  } // End of sideBarItems()
}
