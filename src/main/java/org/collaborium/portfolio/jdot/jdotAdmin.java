/**
 * Copyright 2001-2005 Iowa State University
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
package org.collaborium.portfolio.jdot;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.collaborium.portfolio.*;

// our rewrite of the Orielly Multipart Request to
// interface with the SQL server and not the file system
// import com.oreilly.servlet.MultipartRequestSQL;

public class jdotAdmin extends HttpServlet {

  String servletHttpBase = jlib.servletHttpBase;
  String thisPageURL = servletHttpBase + "/jdotAdmin";
  StringBuffer errors = new StringBuffer();
  String TITLE = "JDOT Admin";

  /**
   * Standard init method for a servlet
   *
   * @param ServletConfig provided by the servlet engine...
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);

  } // End of init()

  /**
   * Method to handle servlet post
   *
   * @param req HttpServletRequest
   * @param res HttpServletResponse
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    StringBuffer pageContent = new StringBuffer();
    StringBuffer sideContent = new StringBuffer();

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    HttpSession session = request.getSession(true);
    // session ID

    portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
    String callMethod = (String)request.getParameter("mode");

    if (thisUser == null || thisUser.getPortfolio() == null) {
      response.setHeader("Refresh", "0; URL=./jportfolio");
      callMethod = "z"; // Abort
    } else if (!thisUser.isAdmin()) {
      response.setHeader("Refresh", "0; URL=./jportfolio");
      callMethod = "z"; // Abort
    } else {
      jlib.addUser(thisUser.getUserID(), "DialogAdmin");
    }

    switch (callMethod.charAt(0)) {
      //
      // e -> Post Group Level Assignment
      // f -> Post Portfolio Assignment
      // p -> Input User evaluation
    case 'e':
      pageContent.append(jlib.topBox("Post Results:"));
      pageContent.append(postAssignedThread(thisUser, request));
      pageContent.append(jlib.botBox());
      break;
    case 'p':
      pageContent.append(jlib.topBox("Post Results:"));
      pageContent.append(postEvaluation(thisUser, request));
      pageContent.append(jlib.botBox());
      break;
    default:
      pageContent.append("Invalid Post!\n");

      break;
    }
    sideContent.append(adminSideBar(thisUser));

    out.println(jlib.header(thisUser, TITLE, "Blah"));

    out.println(jlib.makePage(sideContent.toString(), pageContent.toString()));

    out.println(jlib.footer());

  } // End of doPost()

  /**
   * Standard servlet method
   *
   * @param request  HttpServlet request
   * @param response HttpServlet response
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    StringBuffer sideContent = new StringBuffer();
    StringBuffer pageContent = new StringBuffer();

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    System.err.println("--------- Begin jdotAdmin\n");

    HttpSession session = request.getSession(true);
    // session ID
    String callMethod = request.getParameter("mode");
    portfolioUser thisUser = (portfolioUser)session.getAttribute("User");

    if (thisUser == null || thisUser.getPortfolio() == null) {
      response.setHeader("Refresh", "0; URL=./jportfolio");
      callMethod = "z"; // Abort
    } else if (!thisUser.isAdmin()) {
      response.setHeader("Refresh", "0; URL=./jportfolio");
      callMethod = "z"; // Abort
    } else {
      jlib.addUser(thisUser.getUserID(), "DialogAdmin");
    }

    if (callMethod == null)
      callMethod = "d";

    String selectedUserID = request.getParameter("selectedUserID");

    switch (callMethod.charAt(0))
    // s -> View Statistics Options
    // g -> Select Which Kiddie to grade
    // w -> Grade student dialog
    // e -> Create Group Level Assignment
    // h -> Grade Ethical Question
    {
    case 'e':
      pageContent.append(createGroupDialogAssign(thisUser));
      break;
    case 'f':
      pageContent.append(createPortfolioAssign(thisUser));
      break;
    case 'w':
      pageContent.append(gradeDialog(selectedUserID, thisUser.getPortfolio()));
      break;
    case 'g':
      if (selectedUserID == null)
        pageContent.append(jlib.whichStudent(thisUser, "g", thisPageURL));
      else
        pageContent.append(
            userOptions(thisUser.getPortfolio(), selectedUserID));
      break;
    case 't':
      pageContent.append(statsOptions());
      pageContent.append(dailyPosts(thisUser.getPortfolio()));
      break;
    case 's':
      pageContent.append(statsOptions());
      break;
    case 'z':
      pageContent.append(
          "You must be authenticated as Admin in order to log in.");
      break;
    case 'd':
      pageContent.append(printIntro());
      break;
    } // End of switch()

    sideContent.append(adminSideBar(thisUser));
    System.err.println("--------- End jdotAdmin\n");

    out.println(jlib.header(thisUser, TITLE, "Blah"));

    out.println(jlib.makePage(sideContent.toString(), pageContent.toString()));

    out.println(jlib.footer());

  } // End of doGet()

  /**
   * Simple Method to print a small introduction for a default page
   *
   * @return HTML box
   */
  public String printIntro() {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Dialog Administrator:"));

    sbuf.append(
        "<P>This page allows you to administer various components of the Dialog.  You can view stats for the dialog \n"
        +
        ", assign a dialog to a specific group or to the entire portfolio.  You can also grade a users dialog.\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of printIntro()

  /**
   * Method to input a post by the administrator
   *
   * @param req HttpServletRequest for getting the posted variables
   * @param portfolio String value for the current portfolio
   * @param userID String value for the current user
   * @param realname String value for the realname of the admin
   * @return a String for the successfullness of the post.
   */
  public String postAssignedThread(portfolioUser thisUser,
                                   HttpServletRequest req) {

    StringBuffer sbuf = new StringBuffer();

    String bodyText = (String)req.getParameter("bodyText");
    String groupID = (String)req.getParameter("groupID");
    //  String dateDue   = (String)req.getParameter("duedate");
    String subject = (String)req.getParameter("subject");
    ResultSet students = null;
    String security = "group";
    String dateDue = "2001-01-01 12:00:00";
    String portbase = "/jportfolio/servlet/jdot3";
    if (!thisUser.getBase().equalsIgnoreCase("/jportfolio/servlet/"))
      portbase = thisUser.getBase() + "/dialog/index.jsp";

    try {
      // Find me the new ThreadID to use
      ResultSet maxth = dbInterface.callDB(
          "select MAX (idnum)+1 as result "
          + "from dialog WHERE idnum > 10000 and idnum < 20000");
      maxth.next();
      String newSTRidnum = maxth.getString("result");

      if (newSTRidnum == null) // In case this is the first post
        newSTRidnum = "10001";

      // If this is a post that should go to all
      if (groupID.equalsIgnoreCase("all")) {
        sbuf.append("<P>This is a post was assigned to all users in "
                    + " the portfolio.\n");
        students = dbInterface.callDB("SELECT username from students "
                                      + " WHERE portfolio = '" +
                                      thisUser.getPortfolio() + "' ");
        // Default security for a portfolio assignment
        security = "public";
        // Something bogus for a gid, it is needed so that all is not used
        groupID = "-99";
        jlib.updateDB(
            "INSERT into calendar(portfolio, description, URL) VALUES "
            + " ('" + thisUser.getPortfolio() + "', 'Dialog Assigned', "
            + " '" + portbase +
            "?dialogType=public&mode=r&idnum=" + newSTRidnum + "' ) ");
      } else // Otherwise just get a listing of students in the group
        students = dbInterface.callDB(
            "SELECT username from students WHERE portfolio = "
            + " '" + thisUser.getPortfolio() + "' and gid = " + groupID + " ");

      // Put in the DB
      jlib.updateDB(
          "INSERT into dialog(username, name, subject, threadid, idnum, "
          + " portfolio, security, gid, body, type, expires) values ('" +
          thisUser.getUserID() + "', "
          + " '" + thisUser.getRealName() + "', '" + subject + "' "
          + " , '" + newSTRidnum + "', '" + newSTRidnum + "', '" +
          thisUser.getPortfolio() + "', '" + security + "', '" + groupID + "' "
          + " , '" + bodyText + "', 'Response Required', '" + dateDue + "')");

      // For each student create a notification
      while (students.next()) {
        String thisUserID = students.getString("username");
        jlib.updateDB(
            "INSERT into notify(username, portfolio, program, message) "
            + " VALUES( '" + thisUserID + "', '" + thisUser.getPortfolio() +
            "',"
            + "'" + portbase + "?dialogType=" + security +
            "&mode=r&idnum=" + newSTRidnum + "', "
            + " 'Dialog Assigned:')");
      }

    } catch (Exception ex) {
      System.err.println("<P>Trouble with postGroupThread");
      ex.printStackTrace();
    }

    sbuf.append("\nYour Post was successfull!");
    return sbuf.toString();
  } // End of postGroupIDs

  /**
   * Method to input the administrators evaluation
   *
   * @param request is the Servlet Request container
   * @param portfolio which is the string value for the current portfolio
   * @return HTML string for how this method went
   */
  public String postEvaluation(portfolioUser thisUser,
                               HttpServletRequest request) {
    StringBuffer sbuf = new StringBuffer();

    String selectedUserID = (String)request.getParameter("selectedUser");
    String score1 = (String)request.getParameter("score1");
    String blockID = (String)request.getParameter("blockID");
    String body1 = jlib.cleanString((String)request.getParameter("body1"));
    String score2 = (String)request.getParameter("score2");
    String body2 = jlib.cleanString((String)request.getParameter("body2"));
    String notifyBaseURL = "/jportfolio/servlet/jdot3";

    try {
      /* We need to get the porthome value for use in notifications */
      ResultSet portHome = dbInterface.callDB(
          "SELECT porthome from portfolios "
          + " WHERE portfolio = '" + thisUser.getPortfolio() + "' and "
          + " porthome != '/jportfolio/servlet/' ");
      if (portHome.next()) {
        notifyBaseURL = portHome.getString("porthome") + "/dialog/index.jsp";
      }

      ResultSet maxth =
          dbInterface.callDB("select MAX (idnum)+1 as result1, "
                             + " MAX (idnum)+2 as result2 from dialog "
                             + " WHERE idnum > 10000 and idnum < 20000");
      maxth.next();
      String newSTRidnum1 = maxth.getString("result1");
      String newSTRidnum2 = maxth.getString("result2");

      if (score1 != null && score1.length() != 0) {
        jlib.updateDB(
            "INSERT into dialog(username, name, subject, threadid, idnum, "
            + " portfolio, security, gid, body, type, touser) values "
            + " ('" + thisUser.getUserID() + "', "
            + " '" + thisUser.getRealName() + "', 'Dialog Evaluation' "
            + " , '" + newSTRidnum1 + "', '" + newSTRidnum1 + "', "
            + " '" + thisUser.getPortfolio() + "', 'private', '-99' "
            + " , '" + body1 + "', 'evaluation', '" + selectedUserID + "')");

        jlib.updateDB(
            "INSERT into notify(username, portfolio, program, message) "
            + " VALUES( '" + selectedUserID + "', '" + thisUser.getPortfolio() +
            "',"
            + " '" + notifyBaseURL +
            "?dialogType=private&mode=r&idnum=" + newSTRidnum1 + "', "
            + " 'Evaluation Made:')");
      }

      if (score2 != null && score2.length() != 0) {
        jlib.updateDB(
            "INSERT into dialog(username, name, subject, threadid, idnum, "
            + " portfolio, security, gid, body, type, touser) values "
            + " ('" + thisUser.getUserID() + "', "
            + " '" + thisUser.getRealName() + "', 'Ethical Q Evaluation' "
            + " , '" + newSTRidnum2 + "', '" + newSTRidnum2 + "', "
            + " '" + thisUser.getPortfolio() + "', 'private', '-99' "
            + " , '" + body2 + "', 'evaluation', '" + selectedUserID + "')");

        jlib.updateDB(
            "INSERT into notify(username, portfolio, program, message) "
            + " VALUES( '" + selectedUserID + "', '" + thisUser.getPortfolio() +
            "',"
            + " '" + notifyBaseURL +
            "?dialogType=private&mode=r&idnum=" + newSTRidnum2 + "', "
            + " 'Ethical Q Eval:')");
      }

      portfolioUser selectedUser = new portfolioUser(selectedUserID);
      selectedUser.setPortfolio(thisUser.getPortfolio());
      gradebook studentGrades = new gradebook(selectedUser);
      if (score1 != null && score1.length() != 0 && !score1.equals("0")) {
        studentGrades.addScoreElement("jdot", blockID, score1);
      }
      if (score2 != null && score2.length() != 0 && !score2.equals("0")) {
        studentGrades.addScoreElement("jdot", "Ethical Q" + blockID, score2);
      }

    } catch (Exception ex) {
      plogger.report("Problem with the postEvaluation.");
      ex.printStackTrace();
    }

    sbuf.append("<P>Got the entry in the DB\n");

    return sbuf.toString();

  } // End of postEvaluation()

  /**
   * Method to create a new dialog assignment for a particular <b>group</B>
   *
   * @param portfolio string value for the current portfolio
   * @return HTML formated string
   */
  public String createGroupDialogAssign(portfolioUser thisUser) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Create Group Dialog Assignment:"));

    sbuf.append("<form method='POST' action='" + thisPageURL +
                "' name='motd'>\n");
    sbuf.append("<input type='hidden' name='mode' value='e'>\n");
    sbuf.append(
        "<P><blockquote><font class=\"instructions\">\n"
        +
        " With this dialog you can create a group level dialog assignment.  Notification is then sent to all group\n"
        +
        " members that a group level assignment has been posted.  Please be sure to fill in all the blanks.\n"
        + " </font></blockquote>\n"
        + "<P>Subject of this post:\n"
        + "<BR><input type='text' name='subject' size='40'>\n"

        + "<P>Enter the Initial Post for this thread:<BR>\n"
        +
        "<textarea COLS='60' ROWS='10' name='bodyText' wrap='Virtual'></textarea>\n"

        + "<P>Assign This Thread to a Group:<BR>\n"
        + "<input type=\"text\" name=\"groupID\" size=\"4\">\n"

        //	+ "<P>When should this thread by answered by:\n"
        //	+ "<BR>Ex) 2000-09-05 12:00\n"
        //	+ "<BR><input type=\"text\" name=\"duedate\">\n"

        + "<P>Submit Message:<BR>\n"
        + "<input type='submit' value='Assign This Dialog'>\n"
        + "<input type='reset'>\n"
        + "</FORM>\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of createDialogAssign()

  /**
   * Method to create a new dialog assignment for an entire Class
   *
   * @param portfolio string value for the current portfolio
   * @return HTML String value for
   */
  public String createPortfolioAssign(portfolioUser thisUser) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Create a Dialog Assignment:"));
    sbuf.append("<form method='POST' action='" + thisPageURL +
                "' name='motd'>\n");
    sbuf.append("<input type='hidden' name='mode' value='e'>\n");
    sbuf.append(
        "<font class=\"instructions\">\n"
        + " This form allows you to make a special top level thread in the "
        + " public dialog.  This thread is denoted with 'Response Required', "
        + " a portfolio message is sent to each student alerting them of this "
        + " post, and a calendar entry is made.  Perhaps you would rather just "
        + " make a top level post from the normal 'Dialog' interface?\n"
        + " </font>\n"

        + "<P>Subject of this post:\n"
        + "<BR><input type='text' name='subject' size='40'>\n"

        + "<P>Enter the Initial Post for this thread:<BR>\n"
        +
        "<textarea COLS='60' ROWS='10' name='bodyText' wrap='Virtual'></textarea>\n"

        + "<input type=\"hidden\" name=\"groupID\" value=\"all\">\n"

        //	+ "<P>When should this thread by answered by:\n"
        //	+ "<BR>Ex) 2000-09-05 12:00\n"
        //	+ "<BR><input type=\"text\" name=\"duedate\">\n"

        + "<P>Submit Message:<BR>\n"
        + "<input type='submit' value='Assign This Dialog'>\n"
        + "<input type='reset'>\n"
        + "</FORM>\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of createDialogAssign()

  /**
   * Method to print out dialog statistics for a particular user
   *
   * @param userID value of the user in question
   * @param portfolio value for the current portfolio
   * @return HTML string with the user's stats
   */
  public String gradeDialog(String userID, String portfolio) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("User Statistics:"));

    sbuf.append("<P>Block 2 Dialog Stats for " + userID + "<BR>\n");

    String totalCount = "0";
    String responses = "0";
    String ETHICAL = "14415";
    String P01 = "14270";
    String P15 = "14408";
    String EDATE = "2007-03-26";
    try {
      // Stats on user posts
      ResultSet rs = dbInterface.callDB(
          "SELECT count(username) as count, "
          + " round(avg(length(body)),1) as length "
          + " from dialog WHERE username = '" + userID + "' "
          + " and security = 'public'"
          + " and portfolio = '" + portfolio + "' and threadid >= " + P01 +
          " and threadid != " + ETHICAL + " and threadid <= " + P15 +
          " and date(date) < '" + EDATE + "' ");

      // Go get me a listing of all their posts.
      ResultSet rs2 = dbInterface.callDB(
          "SELECT idnum from dialog "
          + " WHERE username = '" + userID + "' "
          + " and security = 'public'"
          + " and portfolio = '" + portfolio + "' and threadid >= " + P01 +
          " and threadid != " + ETHICAL + " and threadid <= " + P15 +
          " and date(date) < '" + EDATE + "' ");

      // Figure out how many responses they had
      ResultSet rs4 = dbInterface.callDB(
          "SELECT count(username) as count "
          + " from dialog WHERE idnum > 1000000000000::numeric "
          + " and username = '" + userID + "' "
          + " and security = 'public'"
          + " and portfolio = '" + portfolio + "' and threadid >= " + P01 +
          " and threadid != " + ETHICAL + " and threadid <= " + P15 +
          " and date(date) < '" + EDATE + "' ");
      if (rs4.next())
        responses = rs4.getString("count");

      // Loop through the user's posts
      while (rs2.next()) {
        String thisIDnum = rs2.getString("idnum");

        BigInteger beginID = new BigInteger(thisIDnum);
        BigInteger endID = new BigInteger(thisIDnum);
        BigInteger secondOne = endID.add(BigInteger.ONE);

        ResultSet rs3 = dbInterface.callDB(
            "SELECT " + totalCount + " + count(username) as count "
            + "  from dialog WHERE username != '" + userID + "' "
            + " and idnum > " + beginID.toString() + "0000::numeric "
            + " and idnum < " + secondOne.toString() + "0000::numeric");
        if (rs3.next())
          totalCount = rs3.getString("count");
      }

      if (rs.next()) {
        sbuf.append("<table>\n"
                    + "<tr><th>Total Posts</th> <td>" + rs.getString("count") +
                    "</td></tr>\n"
                    + "<tr><th>Average Post in Characters</th>"
                    + "<td>" + rs.getString("length") + "</td></tr>\n");
      }

      sbuf.append("<tr><th>Total Responses by others</th>"
                  + "<td>" + totalCount + "</td></tr>\n");
      sbuf.append("<tr><th>Number of followups by student</th>"
                  + "<td>" + responses + "</td></tr></table>\n");

    } catch (Exception ex) {
      System.err.println("Hello");
      ex.printStackTrace();
    }

    sbuf.append(
        "<P><B>Post Your Evaluations:</B><BR>\n"

        +
        "<blockquote>If you do not enter a score for either the dialog section\n "
        +
        " or the ethical Q section, the evaluation for that section will not be\n"
        +
        " posted.  This means that you can just do one or the other or both.\n"
        + "</blockquote>\n"

        + "<FORM action=\"" + thisPageURL + "\" method=\"POST\">\n"
        + "<input type=\"hidden\" value=\"p\" name=\"mode\">\n"
        + "<input type=\"hidden\" value=\"" + userID +
        "\" name=\"selectedUser\">\n"

        + "<p>Select Block ID:<br>\n"
        + "<select name='blockID'>\n"
        + "  <option value=\"1\" SELECTED>Block 1\n"
        + "  <option value=\"2\">Block 2\n"
        + "  <option value=\"3\">Block 3\n"
        + "</select>\n"

        +
        "<P>Assign a dialog score: (<i>Input 0 to not submit grade, only evaluation</i>)<BR>\n"
        + "<input type=\"text\" name=\"score1\" size=\"4\">\n"

        + "<P>Enter your dialog evaluation:<BR>\n"
        +
        "<TEXTAREA name=\"body1\" cols=\"60\" ROWS=\"10\" WRAP=\"Virtual\"></TEXTAREA>\n"

        +
        "<p>Assign a Ethical Q score: (<i>Input 0 to not submit grade, only evaluation</i>)<br>\n"
        + "<input type=\"text\" name=\"score2\" size=\"4\">\n"

        + "<p>Enter your Ethical Q evaluation:<br>\n"
        +
        "<TEXTAREA name=\"body2\" cols=\"60\" ROWS=\"10\" WRAP=\"Virtual\"></TEXTAREA>\n"

        + "<P><input type=\"SUBMIT\" value=\"Grade\">\n"
        + "</FORM>\n");

    sbuf.append(jlib.botBox());
    return sbuf.toString();

  } // End of gradeDialog()

  /**
   * Method for printing options for grading a students work...
   *
   * @param String value for the current user
   * @param String value for the current portfolio
   * @return HTML string for printing
   */
  public String userOptions(String portfolio, String selectedUserID) {
    StringBuffer sbuf = new StringBuffer();

    // No NO
    // userID = jlib.cleanGetString(userID);

    sbuf.append(jlib.topBox("Dialog Grader:"));

    sbuf.append("<P>Evaluate a different <a href=\"" + thisPageURL +
                "?mode=g\">user</a>.<BR>\n");

    sbuf.append(
        "<font class=\"instructions\"><blockquote>This page allows you to view a certain users dialog\n"
        + " and assign some sort of grade to the user.</blockquote></font>\n");

    sbuf.append("<P>View All User Dialog:<BR>");
    sbuf.append("<a href=\"" + jlib.httpBase +
                "/jsp/admin/printDialog.jsp?blockID=1&selectedUser=" +
                jlib.cleanGetString(selectedUserID) + "\" "
                + " target=\"_new\">Block 1</a>\n"
                + "<BR><a href=\"" + jlib.httpBase +
                "/jsp/admin/printDialog.jsp?blockID=2&selectedUser=" +
                jlib.cleanGetString(selectedUserID) + "\" "
                + " target=\"_new\">Block 2</a>\n"
                + "<BR><a href=\"" + jlib.httpBase +
                "/jsp/admin/printDialog.jsp?blockID=3&selectedUser=" +
                jlib.cleanGetString(selectedUserID) + "\" "
                + " target=\"_new\">Block 3</a>\n");
    sbuf.append(jlib.botBox());

    sbuf.append("<P>\n");

    sbuf.append(gradeDialog(selectedUserID, portfolio));

    return sbuf.toString();
  }

  public String dailyPosts(String portfolio) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Daily Posts:"));

    sbuf.append(
        "<blockquote><font color=\"green\">Here is a daily summary of total \n"
        +
        " posts and average length of each post for that day.  The length is in terms of \n"
        + " characters.</font></blockquote>\n");

    sbuf.append("<TABLE width=\"100%\">\n");
    sbuf.append(
        "<TR><TH>Date:</TH><TH>Posts:</TH><TH>Ave(Len) per post:</TH></TR>\n");

    try {
      ResultSet rs = dbInterface.callDB(
          "SELECT count(username) as posts, date(date) , avg(length(body)) as len from dialog "
          + " WHERE portfolio = '" + portfolio + "' GROUP by date(date)");
      while (rs.next()) {
        String posts = rs.getString("posts");
        String strDate = rs.getString("date");
        String len = rs.getString("len");
        sbuf.append("  <TR><TD>" + strDate + "</TD><TD>" + posts + "</TD><TD>" +
                    len + "</TD></TR>\n");
      }

    } catch (Exception ex) {
      System.err.println("Problem generating stats!");
      ex.printStackTrace();
    }

    sbuf.append("</TABLE>\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of whichQuiz

  /**
   * Method to print a simple introduction
   *
   * @return HTML for the introduction
   */
  public String statsOptions() {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Available Statistics:"));

    sbuf.append("<UL>\n"
                + "  <LI><a href=\"" + thisPageURL +
                "?mode=t\">Daily Posts</a></LI>\n"
                + "</UL>\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  }

  public String adminSideBar(portfolioUser thisUser) {
    StringBuffer theBuffer = new StringBuffer();

    theBuffer.append(jlib.libPortApps());
    theBuffer.append("<P>");
    theBuffer.append(jlib.currentUsers(thisUser));
    theBuffer.append("<P>");
    theBuffer.append(adminCommands());
    theBuffer.append("<P>");
    theBuffer.append(jlib.adminCommands());

    return theBuffer.toString();
  } // End of adminSideBar()

  public String adminCommands() {
    StringBuffer myBuffer = new StringBuffer();

    myBuffer.append(jlib.topBox("Admin Commands:"));

    myBuffer.append("<ul><li><a class=\"commands\" href='" + thisPageURL +
                    "?mode=s'>Stats</a></li>\n"
                    + " <li><a class=\"commands\" href='" + thisPageURL +
                    "?mode=e'>Assign A Group Dialog</a></li>\n"
                    + " <li><a class=\"commands\" href='" + thisPageURL +
                    "?mode=f'>Assign A Portfolio Dialog</a></li>\n"
                    + " <li><a class=\"commands\" href='" + thisPageURL +
                    "?mode=g'>Grade Dialog</a></li></ul>\n");

    myBuffer.append(jlib.botBox());

    return myBuffer.toString();
  } // End of adminCommands

} // End of jquizAdmin()
