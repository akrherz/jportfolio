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
package org.collaborium.portfolio;

import java.io.*;
import java.lang.*;
import java.lang.String.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.collaborium.portfolio.*;
import org.collaborium.util.*;

public class jportfolioAdmin extends HttpServlet {

  public static final String TITLE = "Portfolio Administrator";

  String servletHttpBase = jlib.servletHttpBase;
  String thisPageURL = servletHttpBase + "/jportfolioAdmin";

  public void init(ServletConfig config) throws ServletException {

    super.init(config);

  } // End of init()

  public void destroy() { super.destroy(); }

  /**
   * Method to handle server GET requests
   *
   * @param servlet request
   * @param servlet response
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    System.err.println("-------- jportfolio Admin\n");
    StringBuffer sideContent = new StringBuffer();
    StringBuffer pageContent = new StringBuffer();

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    HttpSession session = request.getSession(true);

    // get the mode in which we are called
    String callMethod = request.getParameter("mode");
    if (callMethod == null)
      callMethod = "d";
    String selectedUserID = (String)request.getParameter("selectedUserID");

    portfolioUser thisUser = (portfolioUser)session.getAttribute("User");

    if (thisUser == null || thisUser.getPortfolio() == null) {
      response.setHeader("Refresh", "0; URL=./jportfolio");
      callMethod = "z"; // Abort
    } else if (!thisUser.isAdmin()) {
      response.setHeader("Refresh", "0; URL=./jportfolio");
      callMethod = "z"; // Abort
    } else {
      jlib.addUser(thisUser.getUserID(), "portfolioAdmin");
    }

    switch (callMethod.charAt(0)) {
      /**
       * u -- create Unit Topic
       */
    default:
      pageContent.append(classPortfolio(thisUser.getPortfolio()));
      break;
    case 'r':
      pageContent.append(assignRolesForm(thisUser));
      break;
    case 'a':
      pageContent.append(changeApps(thisUser, request));
      pageContent.append(selectApps(thisUser, thisPageURL));
      break;
    case 's':
      pageContent.append(selectApps(thisUser, thisPageURL));
      break;
    case 'g':
      pageContent.append(editGroupIDs(thisUser.getPortfolio()));
      break;
    case 'l':
      pageContent.append(listStudents(thisUser.getPortfolio()));
      break;
    case 'm':
      pageContent.append(editMOTD());
      break;
    case 'b':
      pageContent.append(addAdminDialog(thisUser.getPortfolio()));
      break;
    case 'c':
      pageContent.append(addCalendarElement());
      break;
    case 'h':
      pageContent.append(listStudentGrades(thisUser.getPortfolio()));
      break;
    case 'i':
      if (selectedUserID == null) {
        pageContent.append(jlib.whichStudent(thisUser, "i", thisPageURL));
      } else {
        pageContent.append(listStudentGrades(thisUser, selectedUserID));
      }
      break;
    case 'n':
      pageContent.append(newEmailMessage());
      break;
    case 'q':
      pageContent.append(createUnit());
      break;
    } // End of this switch()

    sideContent.append(adminSideBar(thisUser));

    out.println(jlib.header(thisUser, TITLE, "Blah"));

    out.println(jlib.makePage(sideContent.toString(), pageContent.toString()));

    out.println(jlib.footer());
    plogger.report("------ End jportfolio Admin\n");
  } // End of doGet()

  public String createUnit() {
    StringBuffer sbuf = new StringBuffer();

    return sbuf.toString();
  }

  /**
   * Method to allow admin to select what apps are used for this portfolio
   */
  public String selectApps(portfolioUser thisUser, String thisPageURL) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Select Portfolio Elements"));

    sbuf.append(
        "<font class=\"instructions\">You can restrict which elements are usable by \n"
        +
        " your portfolio.  Simply select or deselect elements listed below.   Portfolio data\n"
        + " is not lost from this change.</font>\n");

    sbuf.append("<FORM METHOD=\"GET\" ACTION=\"" + thisPageURL + "\">\n"
                + "<input type=\"hidden\" value=\"a\" name=\"mode\">\n");

    sbuf.append("<TABLE>\n");

    sbuf.append("<TR><TD>Calendar:</TD>\n");
    if (thisUser.usesCalendar) {
      sbuf.append(
          "<TD><input type=\"radio\" value=\"yes\" name=\"usesCalendar\" CHECKED>yes\n"
          +
          "</TD><TD><input type=\"radio\" value=\"no\" name=\"usesCalendar\">no</TD></TR>\n");
    } else {
      sbuf.append(
          "<TD><input type=\"radio\" value=\"yes\" name=\"usesCalendar\">yes\n"
          +
          "</TD><TD><input type=\"radio\" value=\"no\" name=\"usesCalendar\" CHECKED>no</TD></TR>\n");
    }

    sbuf.append("<TR><TD>Dialog:</TD>\n");
    if (thisUser.usesDialog) {
      sbuf.append(
          "<TD><input type=\"radio\" value=\"yes\" name=\"usesDialog\" CHECKED>yes\n"
          +
          "</TD><TD><input type=\"radio\" value=\"no\" name=\"usesDialog\">no</TD></TR>\n");
    } else {
      sbuf.append(
          "<TD><input type=\"radio\" value=\"yes\" name=\"usesDialog\">yes\n"
          +
          "</TD><TD><input type=\"radio\" value=\"no\" name=\"usesDialog\" CHECKED>no</TD></TR>\n");
    }

    sbuf.append("<TR><TD>Forecast:</TD>\n");
    if (thisUser.usesForecast) {
      sbuf.append(
          "<TD><input type=\"radio\" value=\"yes\" name=\"usesForecast\" CHECKED>yes\n"
          +
          "</TD><TD><input type=\"radio\" value=\"no\" name=\"usesForecast\">no</TD></TR>\n");
    } else {
      sbuf.append(
          "<TD><input type=\"radio\" value=\"yes\" name=\"usesForecast\">yes\n"
          +
          "</TD><TD><input type=\"radio\" value=\"no\" name=\"usesForecast\" CHECKED>no</TD></TR>\n");
    }

    sbuf.append("<TR><TD>Quiz:</TD>\n");
    if (thisUser.usesQuiz) {
      sbuf.append(
          "<TD><input type=\"radio\" value=\"yes\" name=\"usesQuiz\" CHECKED>yes\n"
          +
          "</TD><TD><input type=\"radio\" value=\"no\" name=\"usesQuiz\">no</TD></TR>\n");
    } else {
      sbuf.append(
          "<TD><input type=\"radio\" value=\"yes\" name=\"usesQuiz\">yes\n"
          +
          "</TD><TD><input type=\"radio\" value=\"no\" name=\"usesQuiz\" CHECKED>no</TD></TR>\n");
    }

    sbuf.append("<TR><TD>Chat:</TD>\n");
    if (thisUser.usesChat) {
      sbuf.append(
          "<TD><input type=\"radio\" value=\"yes\" name=\"usesChat\" CHECKED>yes\n"
          +
          "</TD><TD><input type=\"radio\" value=\"no\" name=\"usesChat\">no</TD></TR>\n");
    } else {
      sbuf.append(
          "<TD><input type=\"radio\" value=\"yes\" name=\"usesChat\">yes\n"
          +
          "</TD><TD><input type=\"radio\" value=\"no\" name=\"usesChat\" CHECKED>no</TD></TR>\n");
    }
    sbuf.append("</TABLE>\n");

    sbuf.append("<INPUT TYPE=\"submit\" value=\"Make Changes\"></form>\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of selectApps()

  /**
   * Method that enacts the changes in the portfolio
   */
  public String changeApps(portfolioUser thisUser, HttpServletRequest request) {
    StringBuffer sbuf = new StringBuffer();
    String usesCalendar = (String)request.getParameter("usesCalendar");
    String usesDialog = (String)request.getParameter("usesDialog");
    String usesForecast = (String)request.getParameter("usesForecast");
    String usesQuiz = (String)request.getParameter("usesQuiz");
    String usesChat = (String)request.getParameter("usesChat");
    try {
      dbInterface.updateDB("INSERT into appregistry VALUES('" +
                           thisUser.getPortfolio() + "') ");
      dbInterface.updateDB(
          "UPDATE appregistry SET use_calendar = '" + usesCalendar + "' "
          + " , use_dialog = '" + usesDialog + "' "
          + " , use_forecast = '" + usesForecast + "' "
          + " , use_quiz = '" + usesQuiz + "' "
          + " , use_chat = '" + usesChat + "' "
          + " WHERE portfolio = '" + thisUser.getPortfolio() + "' ");
    } catch (Exception ex) {
      plogger.report("Problem setting vals in appregistry");
    }

    thisUser.setPortfolio(thisUser.getPortfolio());
    thisUser.setIsAdmin(Boolean.TRUE);

    return sbuf.toString();
  }

  public String newEmailMessage() {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Email Portfolio:"));
    sbuf.append(
        "<FORM METHOD=\"POST\" ACTION=\"" + thisPageURL + "\">\n"
        + "<input type=\"hidden\" value=\"n\" name=\"mode\">\n"

        + "<P>Enter Subject:<BR>\n"
        + "<input type=\"text\" name=\"subject\">\n"

        + "<P>Enter Message:<BR>\n"
        +
        "<TEXTAREA WRAP=\"Virtual\" name=\"message\" ROWS=\"5\" COLS=\"40\"></TEXTAREA>\n"

        + "<input type=\"SUBMIT\">\n"
        + "</FORM>\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  }

  public String listStudentGrades(String portfolio) {
    StringBuffer sbuf = new StringBuffer();
    sbuf.append(jlib.topBox("Student Grades:"));
    sbuf.append("<TABLE>\n");
    try {
      ResultSet rs = dbInterface.callDB(
          "SELECT sum(score) as tot_score, getUserName(userid) as name from scores s "
          + " WHERE s.portfolio = '" + portfolio + "' GROUP by s.userid");
      while (rs.next()) {
        sbuf.append("<TR><TD>" + rs.getString("name") + "</TD><TD>" +
                    rs.getString("tot_score") + "</TD></TR>\n");
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }
    sbuf.append("</TABLE>\n");
    sbuf.append(jlib.botBox());
    return sbuf.toString();
  } // End of listStudentGrades()

  /**
   * Method to list out grades for one selected user
   * @param portfolio String value
   * @param selectedUserID which is the string value of selected user
   * @return HTML formatted string
   */
  public String listStudentGrades(portfolioUser thisUser,
                                  String selectedUserID) {
    StringBuffer sbuf2 = new StringBuffer();
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Grades for " + selectedUserID + ":"));
    sbuf2.append("<TABLE>\n");
    try {
      ResultSet rs = dbInterface.callDB(
          "SELECT app, score, "
          + " getUserName(userid) as name, assign from scores s "
          + " WHERE s.portfolio = '" + thisUser.getPortfolio() + "' "
          + " and s.userid = '" + selectedUserID + "' ");
      while (rs.next()) {
        sbuf2.append("<TR>\n"
                     + " <td>" + rs.getString("app") + " " +
                     rs.getString("assign") + "</td>\n"
                     + " <td>" + rs.getString("score") + "</td>\n"
                     + " </tr>\n");
      }
      sbuf.append("<p>Name:" + rs.getString("name") + "\n");

    } catch (Exception ex) {
      ex.printStackTrace();
    }
    sbuf2.append("</TABLE>\n");
    sbuf2.append(jlib.botBox());
    return sbuf.toString() + sbuf2.toString();

  } // End of listStudentGrades()

  public String addAdminDialog(String portfolio) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Add Administrator:"));

    sbuf.append(
        "<P><blockquote><font color=\"green\">With this form you can knight somebody else to share\n"
        +
        " administrative duties with you.  Just add their username and you are all set.</font></blockquote>\n");

    sbuf.append(
        "<FORM METHOD=\"POST\" ACTION=\"" + thisPageURL + "\">\n"
        + " <input type=\"hidden\" name=\"mode\" value=\"b\">\n"
        + " <P><B>Enter UserID:</B> <input type=\"text\" name=\"newAdmin\">\n"
        + " <P><input type=\"SUBMIT\" value=\"Add Administrator\">\n"
        + " </FORM>\n");

    sbuf.append("<H3>Current Admins:</H3>\n");

    try {
      ResultSet rs = dbInterface.callDB(
          "SELECT * from admins WHERE portfolio = '" + portfolio + "' ");
      while (rs.next()) {
        sbuf.append("\n<P> " + rs.getString("admin"));
      }

    } catch (Exception ex) {
    }
    sbuf.append(jlib.botBox());

    return sbuf.toString();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    StringBuffer pageContent = new StringBuffer();
    StringBuffer sideContent = new StringBuffer();

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    String type = null; // hold my image type is any
    // start to put in some session tracking info...
    HttpSession session = request.getSession(true);

    // get the mode in which we are called
    String callMethod = request.getParameter("mode");
    if (callMethod == null)
      callMethod = "x";

    portfolioUser thisUser = (portfolioUser)session.getAttribute("User");

    if (thisUser == null || thisUser.getPortfolio() == null) {
      response.setHeader("Refresh", "0; URL=./jportfolio");
      return;
    } else if (!thisUser.isAdmin()) {
      response.setHeader("Refresh", "0; URL=./jportfolio");
      return;
    } else {
      jlib.addUser(thisUser.getUserID(), "portfolioAdmin");
    }

    switch (callMethod.charAt(0)) {
    default:
      pageContent.append(classPortfolio(thisUser.getPortfolio()));
      break;
    case 'r':
      pageContent.append(postRoles(request, thisUser));
      break;
    case 'c':
      pageContent.append(
          enterCalendarElement(request, thisUser.getPortfolio()));
      break;
    case 'g':
      pageContent.append(postGroupIDs(request, thisUser.getPortfolio()));
      break;
    case 'm': // Post MOTD
      pageContent.append("<H3>Saving MOTD into the Database:</H3>");
      pageContent.append(postMOTD(request, thisUser));
      pageContent.append(classPortfolio(thisUser.getPortfolio()));
      break;
    case 'n': // Send email
      String message = request.getParameter("message");
      String subject = request.getParameter("subject");
      pageContent.append(
          portfolioUtils.emailPortfolio(message, subject, thisUser));

      break;
    case 'b': // Post new Admin
      pageContent.append(postNewAdmin(request, thisUser));
      break;
    } // End of this switch()

    sideContent.append(adminSideBar(thisUser));

    out.println(jlib.header(thisUser, TITLE, "Blah"));

    out.println(jlib.makePage(sideContent.toString(), pageContent.toString()));

    out.println(jlib.footer());

    destroy();

  } // End of doPost()

  /**
   * Method that adds a new user as an admin for the course.
   *
   * @param req which is just the servlet request
   * @param thisUser which is the user container
   * @return HTML formated string for the successfullness
   */
  public String postNewAdmin(HttpServletRequest req, portfolioUser thisUser) {

    String newAdmin = (String)req.getParameter("newAdmin");
    String portfolio = thisUser.getPortfolio();
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Administrator Addition:"));
    sbuf.append("You are adding user <i>" + newAdmin +
                "</i> as an administrator"
                + " to portfolio <i>" + portfolio + "</i>\n");
    try {
      jlib.updateDB("DELETE from admins WHERE admin = '" + newAdmin +
                    "' and portfolio = '" + portfolio + "' ");
      jlib.updateDB("INSERT into admins(portfolio, admin) VALUES ('" +
                    portfolio + "', '" + newAdmin + "') ");
    } catch (Exception ex) {
      System.err.println("Problem updating the admins database.");
      ex.printStackTrace();
    }
    sbuf.append(jlib.botBox());

    return sbuf.toString();
  }

  /**
   * Method that posts the newly created MOTD
   *
   * @param servlet request
   * @param thisUser current container for the user
   * @return gives back a string of how successful the post was
   */
  public String postMOTD(HttpServletRequest req, portfolioUser thisUser) {
    StringBuffer sbuf = new StringBuffer();

    String motd = jlib.toBR(req.getParameter("motd"));

    String portfolio = thisUser.getPortfolio();

    System.err.println(motd);
    if (motd == null)
      motd = "Cowardly MOTD";

    String sendAsEmail = req.getParameter("sendEmail");

    try {

      SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
      java.util.Date currentTime_1 = new java.util.Date();
      String dateString = formatter.format(currentTime_1);

      ResultSet newMotd =
          dbInterface.callDB("SELECT last_value+1 as newmotd from motd_id_seq");
      newMotd.next();
      String newValue = newMotd.getString("newmotd");

      jlib.updateDB("INSERT into motd(portfolio, body) "
                    + " values('" + portfolio + "', '" +
                    jlib.cleanString(motd) + "')");

      // Lets notify everybody in the portfolio that they have a MOTD to look
      // at.
      ResultSet students =
          dbInterface.callDB("SELECT username from students WHERE portfolio = "
                             + " '" + portfolio + "' ");
      while (students.next()) {
        String studentUserID = students.getString("username");
        jlib.updateDB(
            "INSERT into notify(username, portfolio, program, message) "
            + " VALUES( '" + studentUserID + "', '" + portfolio + "', "
            + " '" + jlib.httpBase +
            "/jsp/user/printMotd.jsp?idnum=" + newValue + "', "
            + " 'MOTD Posted: " + dateString + " ')");
      }

      // Now lets create the calendar posts needed
      jlib.updateDB("INSERT into calendar(portfolio, description, url) VALUES "
                    + " ('" + portfolio + "', 'MOTD Post', '" + jlib.httpBase +
                    "/jsp/user/printMotd.jsp?idnum=" + newValue + "')");

      if (sendAsEmail != null) {
        System.err.println("I am sending the Portfolio An Email");
        sbuf.append(
            portfolioUtils.emailPortfolio(motd, "MOTD Posted", thisUser));
      }

    } catch (Exception ex) {
      System.err.println("Boo");
    }

    return sbuf.toString();
  } // End of postMOTD()

  /**
   * Method to add a calendar element to the DB and thus the calendar
   *
   */
  public String addCalendarElement() {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Add Calendar Entry:"));

    sbuf.append(
        "<font class=\"instructions\">With this dialog, "
        + " you can add entries to the calendar.  These entries will \n"
        + "appear on the calendar for the date that you enter.  "
        + " You can optionally enter a URL and then the text will \n"
        + "be presented as a link to the user.</font>\n"

        + "<FORM METHOD=\"POST\" ACTION=\"" + thisPageURL + "\">\n"
        + "<input type=\"hidden\" value=\"c\" name=\"mode\">\n"

        + "<P>Enter the Calendar Date: (ex 2001-02-18)<BR>\n"
        + "<input type=\"text\" name=\"validDate\">\n"

        + "<P>Enter SHORT text to appear:<BR>\n"
        +
        "<input type=\"text\" name=\"description\" size=\"20\" MAXLENGTH=\"20\">\n"

        + "<P>Enter a URL for text: (Optional)<BR>\n"
        + "<input type=\"text\" name=\"URL\" size=\"60\">\n"

        + "<input type=\"SUBMIT\" value=\"Create Entry\">\n"

        + "</FORM>\n");

    sbuf.append(jlib.botBox());
    return sbuf.toString();
  } // End of listStudents

  /**
   * Method to enter in the calendar Element.
   * @param req HttpServletRequest object
   * @param portfolio String value of the current portfolio
   * @return HTML formated String...
   */
  public String enterCalendarElement(HttpServletRequest req, String portfolio) {
    StringBuffer sbuf = new StringBuffer();

    String description =
        stringUtils.cleanString(req.getParameter("description"));
    String validDate = (String)req.getParameter("validDate");
    String thisURL = (String)req.getParameter("URL");
    String sqlString = "";
    if (thisURL == null)
      sqlString = "INSERT into calendar(valid, description, portfolio) VALUES "
                  + " ('" + validDate + "', '" + description + "', '" +
                  portfolio + "') ";
    else
      sqlString =
          "INSERT into calendar(valid, description, portfolio, url) VALUES "
          + " ('" + validDate + "', '" + description + "', '" + portfolio +
          "', '" + thisURL + "') ";

    sbuf.append(jlib.topBox("Update Calendar:"));
    try {
      jlib.updateDB(sqlString);
      sbuf.append("WORKED!");
    } catch (Exception ex) {
      System.err.println("Problem here, Charly.");
      ex.printStackTrace();
      sbuf.append("You entry did not fly, sorry.");
    }

    sbuf.append(jlib.botBox());
    return sbuf.toString();
  }

  public String listStudents(String portfolio) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Current Portfolio Roster:"));

    sbuf.append("<P>Current students signed up for this portfolio.<BR>\n");
    ResultSet rs = null;
    try {
      rs = dbInterface.callDB("SELECT u.fname, u.lname from students s, users u"
                              + " WHERE s.portfolio = '" + portfolio +
                              "' and s.username = u.username");
      while (rs.next()) {
        sbuf.append("<P>" + rs.getString("fname") + " " +
                    rs.getString("lname") + " ");
      }
    } catch (Exception ex) {
      System.err.println("Err, bad results when quering of students.");
    }

    sbuf.append(jlib.botBox());
    return sbuf.toString();
  } // End of listStudents

  /**
   * Method to generate a form to create a new MOTD
   *
   */
  public String editMOTD() {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Post Message of the Day:"));

    sbuf.append("<form method='POST' action='" + thisPageURL +
                "' name='motd'>\n");
    sbuf.append("<input type='hidden' name='mode' value='m'>\n");

    sbuf.append(
        "<blockquote><font color=\"green\">The message of the day appears when the portfolio\n"
        +
        " is initially opened.  Good messages would include reminders of assignments and other announcements.<BR>\n"
        +
        " The previous MOTD is saved and easily viewable by a link on the homepage.\n"
        + " </font></blockquote>\n");

    sbuf.append(
        "<P>Enter Message:<BR>"
        + "<textarea COLS='60' ROWS='10' name='motd' wrap='Virtual'></textarea>"

        +
        "<P>Send copy as email to all? <input type=\"checkbox\" value=\"Yes\" name=\"sendEmail\">\n"

        + "<P>Submit Message:<BR>"
        + "<input type='submit' value='Enter MOTD'>");

    sbuf.append(jlib.botBox());
    sbuf.append("</form>\n");

    return sbuf.toString();
  } // End of editMOTD()

  public String adminCommands() {
    StringBuffer myBuffer = new StringBuffer();

    myBuffer.append(jlib.topBox("Admin Commands:"));

    myBuffer.append(
        "<ul><li><a href='" + thisPageURL + "?mode=m'>Post MOTD</a></li>\n"
        + "<li><a href='" + thisPageURL +
        "?mode=r'>Assign User Roles</a></li>\n"
        + "   <li><a href='" + thisPageURL +
        "?mode=s'>Select Portfolio Elements</a></li>\n"
        + "   <li><a href='" + thisPageURL +
        "?mode=g'>Edit Students Group IDs</a></li>\n"
        + "   <li><a href='" + thisPageURL + "?mode=l'>List Students</a></li>\n"
        + "   <li><a href='" + thisPageURL +
        "?mode=b'>Add Administrator</a></li>\n"
        + "   <li><a href='" + thisPageURL +
        "?mode=c'>Make a Calendar Entry</a></li>\n"
        + "   <li><a href='" + thisPageURL +
        "?mode=h'>List Sum Grades</a></li>\n"
        + "   <li><a href='" + thisPageURL +
        "?mode=i'>List Student Grades</a></li>\n"
        + "   <li><a href='" + thisPageURL +
        "?mode=n'>Send Portfolio Email</a></li></ul>\n");

    myBuffer.append(jlib.botBox());

    return myBuffer.toString();
  } // End of adminCommands

  public String editGroupIDs(String portfolio) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Assign group ID numbers:"));

    sbuf.append("<FORM METHOD='POST' ACTION='" + thisPageURL + "'>\n");
    sbuf.append("<input type='hidden' name='mode' value='g'>\n");

    ResultSet students = null;
    String gID = null;
    String userID = null;
    String realname = null;

    // Students will allready assigned GID
    try {
      students = dbInterface.callDB(
          "SELECT *, getUserName(username) as realname from students "
          + " WHERE portfolio = '" + portfolio + "' and gid = -99 ");
      while (students.next()) {
        userID = students.getString("username");
        realname = students.getString("realname");
        sbuf.append("<P>" + realname + "<input type='text' name='" + userID +
                    "' size='6' MAXLENGTH='4'>");
      }
    } catch (Exception ex) {
      System.err.println("Error caught!\n");
    }

    sbuf.append("<P><input type='SUBMIT' value=\"Assign GIDS\">\n");

    sbuf.append("</form>\n");

    // Loop over the rest of the students to make a printout of allready
    // assigned values
    sbuf.append("<h3>Allready assigned GIDs.</h3>\n");
    sbuf.append("<TABLE>\n");
    try {
      students = dbInterface.callDB(
          "SELECT *, getUserName(username) as realname from students "
          + " WHERE portfolio = '" + portfolio + "' and gid > -99 ");
      while (students.next()) {
        userID = students.getString("username");
        realname = students.getString("realname");
        gID = students.getString("gid");
        sbuf.append("<TR><TD>" + realname + " (" + userID + ")</TD>\n"
                    + "<TD>" + gID + "</TD></TR>\n");
      }
    } catch (Exception ex) {
      System.err.println("Error caught!\n");
    }
    sbuf.append("</TABLE>\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  }

  /**
   * A method to print out users, so that we can assign roles
   * @param thisUser  portfolioUser
   * @return HTML formatted string
   */
  public String assignRolesForm(portfolioUser thisUser) {
    StringBuffer sbuf = new StringBuffer();
    sbuf.append(jlib.topBox("Assign Portfolio Roles"));

    /* Print out a listing of roles */
    ResultSet rs = dbInterface.callDB("SELECT * from roles "
                                      + " ORDER by id ASC");
    sbuf.append("<ul>\n");
    try {
      while (rs.next()) {
        sbuf.append("<li><b>" + rs.getString("id") + ":</b> " +
                    rs.getString("name") + "</li>\n");
      }
    } catch (Exception ex) {
      System.err.println(ex);
    }
    sbuf.append("</ul>\n");

    /* We get a listing of users and roles */
    rs = dbInterface.callDB(
        "SELECT r.id as id, r.name as name, "
        +
        " u.username as username, u.fname ||' '|| u.lname as realname from roles r, users u, "
        + " students s WHERE r.id = s.role and "
        + " s.portfolio = '" + thisUser.getPortfolio() + "' and "
        + " u.username = s.username");

    sbuf.append(
        "<form method=\"POST\" name=\"assign\" action=\"/jportfolio/servlet/jportfolioAdmin\"><input type=\"hidden\" value=\"r\" name=\"mode\">\n");
    sbuf.append("<table border=\"1\"><tr><th>UserName</th><th>Old Role</th>"
                + " <th>New Role</th></tr>\n");
    try {
      while (rs.next()) {
        sbuf.append("<tr><td>" + rs.getString("realname") + "</td>"
                    + "<td>" + rs.getString("name") + "</td>"
                    + "<td><input type=\"text\" name=\"" +
                    rs.getString("username") + "\" "
                    + "size=\"4\" value=\"" + rs.getString("id") +
                    "\"></td></tr>\n");
      }
    } catch (Exception ex) {
      System.err.println(ex);
    }
    sbuf.append("</table>\n");
    sbuf.append("<br /><input type=\"submit\"></form>\n");
    sbuf.append(jlib.botBox());
    return sbuf.toString();
  }

  public String postRoles(HttpServletRequest req, portfolioUser thisUser) {
    StringBuffer sbuf = new StringBuffer();
    Enumeration myE = req.getParameterNames();
    while (myE.hasMoreElements()) {
      String userID = (String)myE.nextElement();
      String roleID = (String)req.getParameter(userID);
      try {
        if (!userID.equalsIgnoreCase("mode")) {
          jlib.updateDB("UPDATE students SET role = '" + roleID + "' "
                        + "WHERE username = '" + userID + "' and "
                        + "portfolio = '" + thisUser.getPortfolio() + "'");
          plogger.report("Updating user: " + userID + " role: " + roleID);
        }
      } catch (Exception ex) {
        System.err.println(ex);
      }
    }
    sbuf.append("No Errors Encountered");
    return sbuf.toString();
  } // End of postRoles

  public String postGroupIDs(HttpServletRequest req, String portfolio) {
    StringBuffer sbuf = new StringBuffer();

    Enumeration myE = req.getParameterNames();
    while (myE.hasMoreElements()) {
      String userID = (String)myE.nextElement();
      String gID = (String)req.getParameter(userID);

      try {
        if (!userID.equalsIgnoreCase("mode") && !gID.equals("")) {
          jlib.updateDB("UPDATE students SET gid = '" + gID + "' "
                        + "WHERE username = '" + userID +
                        "' and portfolio = '" + portfolio + "'");
        }
      } catch (Exception ex) {
        System.err.println(ex);
      }
    }

    return sbuf.toString();
  } // End of postGroupIDs()

  public String intro() {
    StringBuffer myBuffer = new StringBuffer();

    myBuffer.append(jlib.topBox("Introduction"));

    myBuffer.append(
        "<font size=+1>This is the portfolio manager."
        +
        " It is used to access the various classess and projects being managed by the "
        +
        " Portfolio system. When you authenitcate as a user you will be shown the current"
        +
        " portfolio's that you have and be able to navigate your data in them."
        +
        " <p>This version of portfolio is a test environment. If you don't know why you are "
        +
        " seeing this, most likley you don't want to be here (unless you are just naturally curious"
        + " about things on the net). :) ");

    myBuffer.append(jlib.botBox());

    return myBuffer.toString();
  } // End of intro()

  public String adminSideBar(portfolioUser thisUser) {
    StringBuffer theBuffer = new StringBuffer();

    theBuffer.append(jlib.libPortApps());
    theBuffer.append("<P>");
    theBuffer.append(
        jlib.currentUsers(thisUser.getPortfolio(), thisUser.getUserID()));
    theBuffer.append("<P>");
    theBuffer.append(adminCommands());
    theBuffer.append("<P>");
    theBuffer.append(jlib.adminCommands());

    return theBuffer.toString();
  } // End of adminSideBar()

  /**
   * Method to print out the basic information for a class
   *
   * @param String value for the current portfolio
   */
  public String classPortfolio(String portfolio) {
    StringBuffer sbuf = new StringBuffer();
    ResultSet rs = null;
    String motd = null;
    try {
      rs = dbInterface.callDB("SELECT * from motd "
                              + " WHERE portfolio = '" + portfolio +
                              "' ORDER by issue DESC LIMIT 1");
      if (rs.next())
        motd = rs.getString("body");
    } catch (Exception ex) {
      System.err.println(ex);
    }

    if (motd == null)
      motd = "Message not found";

    sbuf.append(jlib.topBox("Portfolio Administration:"));

    sbuf.append("<H3>" + portfolio + " Options</H3>\n"
                + " <P><B>MOTD:</B>\n"
                + " " + motd);

    sbuf.append(jlib.botBox());

    return sbuf.toString();

  } // End of classPort()
} // End of jportfolioAdmin()
