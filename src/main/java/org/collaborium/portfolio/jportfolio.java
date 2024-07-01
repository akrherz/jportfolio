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

/**
 * This servlet is a template for the generic portfolio.  Any portfolio
 * can take advantage of this servlet, but some portfolios will be automatically
 * forwarded out.
 * @author Daryl Herzmann 4 July 2001
 */

import java.io.*;
import java.lang.*;
import java.lang.String.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.collaborium.portfolio.*;

public class jportfolio extends HttpServlet {

  String servletHttpBase = jlib.servletHttpBase;
  String thisPageURL = servletHttpBase + "/jportfolio";

  public static final String TITLE = "Portfolio Manager";

  int timeout = 10800;

  /**
   * Method for when the servlet loads.
   * @param config is the servlet configuration
   */
  public void init(ServletConfig config) throws ServletException {

    IMDatabase.loadMessages();

    super.init(config);
  }

  public void destroy() { IMDatabase.storeMessages(); }

  /**
   * Standard method for servlet GET method
   *
   * @param request
   * @param response
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(true);
    authBean auth = new authBean(request, session);
    if (auth.requiredUserPort(response, session,
                              "/jportfolio/servlet/jportfolio")) {
      return;
    }

    response.setContentType("text/html");

    PrintWriter out = response.getWriter();
    String forward = request.getParameter("forward");

    // Container for page content
    StringBuffer sideContent = new StringBuffer();
    StringBuffer pageContent = new StringBuffer();

    portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
    String callMethod = request.getParameter("mode");

    jlib.addUser(thisUser.getUserID(), "Manager");

    if (callMethod == null)
      callMethod = "d";

    switch (callMethod.charAt(0))
    // v -> list out user preferences
    // k -> remove a notify message from database
    // b -> prints out default login screen, but with failed login attempt noted
    // p -> Sign up for portfolio dialog
    // o -> Print out all messsages
    {
    case 'o':
      /**
       * If idnum is specified, they must be removing the message
       * from the page which prints out all of the messages
       */
      String testIDnum = (String)request.getParameter("idnum");
      if (testIDnum != null)
        jlib.removeNotify(testIDnum);
      pageContent.append(jlib.topBox("Portfolio Messages"));
      pageContent.append(jlib.printAllMessages(thisUser));
      pageContent.append(jlib.botBox());
      break;
    case 'v':
      pageContent.append(preferencesDialog(thisUser, thisPageURL));
      if (thisUser.isAdmin())
        sideContent.append(adminSideBar(thisUser));
      else
        sideContent.append(sideBar(thisUser));
      break;
    case 'k':
      jlib.removeNotify(request.getParameter("idnum"));

      pageContent.append(portfolioMain(thisUser));

      if (thisUser.isAdmin())
        sideContent.append(adminSideBar(thisUser));
      else
        sideContent.append(sideBar(thisUser));

      break;
    case 'b':
      pageContent.append(badIntro());

      break;
    case 'u':
      pageContent.append(createPortfolio());
      if (thisUser.isAdmin())
        sideContent.append(adminSideBar(thisUser));
      else
        sideContent.append(sideBar(thisUser));
      break;
    case 'n':
      pageContent.append(listAllMotd(thisUser));

      if (thisUser.isAdmin())
        sideContent.append(adminSideBar(thisUser));
      else
        sideContent.append(sideBar(thisUser));

      break;
    case 'g': // List out group members
      pageContent.append(jlib.printGroupMembers(thisUser));
      if (thisUser.isAdmin())
        sideContent.append(adminSideBar(thisUser));
      else
        sideContent.append(sideBar(thisUser));
      break;
    case 'h': // List out portfolio mates!
      pageContent.append(jlib.printPmates(thisUser));
      if (thisUser.isAdmin())
        sideContent.append(adminSideBar(thisUser));
      else
        sideContent.append(sideBar(thisUser));
      break;
    default:
      if (thisUser.getPortfolio() == null) {
        sideContent.append(sideBar(thisUser));
      } else if (!(thisUser.getPortfolio() == null)) {
        pageContent.append(portfolioMain(thisUser));

        if (thisUser.isAdmin()) {

          sideContent.append(adminSideBar(thisUser));
        } else {

          sideContent.append(sideBar(thisUser));
        }

      } else {
        sideContent.append(sideBar(thisUser));
      }

      break;
    } // End of Case

    out.println(jlib.header(thisUser, TITLE, "Manager"));

    out.println(jlib.makePage(sideContent.toString(), pageContent.toString()));

    out.println(jlib.footer());
    plogger.report("------ End Jportfolio\n");

  } // End of doGet()

  /**
   * Method to handle method POST from the WEB server
   *
   * @param request http servlet request container
   * @param response http servlet response container
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    /* Authentification */
    HttpSession session = request.getSession(true);
    authBean auth = new authBean(request, session);
    if (auth.requiredUserPort(response, session,
                              "/jportfolio/servlet/jportfolio")) {
      return;
    }

    StringBuffer pageContent = new StringBuffer();
    StringBuffer sideContent = new StringBuffer();

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    portfolioUser thisUser = (portfolioUser)session.getAttribute("User");

    jlib.addUser(thisUser.getUserID(), "jportfolio");

    // get the mode in which we are called
    String callMethod = request.getParameter("mode");
    if (callMethod == null)
      callMethod = "x";

    plogger.report(callMethod + "\n");

    String forward = request.getParameter("forward");
    String baseForward = request.getParameter("baseForward");
    String username = null;
    String password = null;
    String f = null;

    switch (callMethod.charAt(0)) {
      // u == create portfolio
      // v == switch the style of the page
    case 'v':
      String style = request.getParameter("style");
      String postedEmail = request.getParameter("email");
      String lName = request.getParameter("lName");
      String fName = request.getParameter("fName");

      jlib.userUpdate(thisUser, "fname", fName);
      jlib.userUpdate(thisUser, "lname", lName);
      jlib.userUpdate(thisUser, "color", style); // Update DB
      jlib.userUpdate(thisUser, "email", postedEmail);

      thisUser.setStyle(style); // Update portfolioUser Object
      thisUser.setEmailAddress(postedEmail);
      thisUser.setRealName(fName + " " + lName);
      thisUser.setFName(fName);
      thisUser.setLName(lName);
      f = jlib.servletHttpBase + "/jportfolio?mode=v";
      break;
    case 'u':
      pageContent.append(postPortfolio(thisUser, request));
      break;
    } // End of switch

    if (f != null) {
      response.setHeader("Refresh", "0; URL=" + f);
      return;
    }
    // Lets construct the HTML output

    out.println(jlib.header(thisUser, TITLE, "Manager"));

    out.println(jlib.makePage(sideContent.toString(), pageContent.toString()));

    out.println(jlib.footer());

    System.err.println("------- End Jportfolio\n");
  } // End of doPost()

  /**
   * Method that allows the user to switch the default Theme
   *
   * @return HTML string for the user select box.
   */
  public String preferencesDialog(portfolioUser thisUser, String thisPageURL) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("User Preferences:"));

    sbuf.append("<font color=\"green\"><blockquote>This page modifies your " +
                "user preferences.</blockquote></font>\n");

    sbuf.append("<P><B>Current Settings:</B><BR>\n"
                + "<TABLE>\n"
                + "<TR><TD bgcolor=\"#EEEEEE\">Style:</TD><TD>" +
                thisUser.getStyle() + "</TD></TR>\n"
                + "<TR><TD bgcolor=\"#EEEEEE\">Email Address:</TD><TD>" +
                thisUser.getEmailAddress() + "</TD></TR>\n"
                + "<TR><TD bgcolor=\"#EEEEEE\">First Name:</TD><TD>" +
                thisUser.getFirstName() + "</TD></TR>\n"
                + "<TR><TD bgcolor=\"#EEEEEE\">Last Name:</TD><TD>" +
                thisUser.getLastName() + "</TD></TR>\n"
                + "</TABLE>\n");

    sbuf.append("<P><B>Change Settings:</B><BR>\n"
                + " <FORM ACTION=\"" + thisPageURL + "\" METHOD=\"POST\">\n"
                + "	<input type=\"hidden\" name=\"mode\" value=\"v\">\n"

                + "<TABLE>\n"

                + "<TR><TH>Email Address:</TH>\n"
                + "<TD><input type=\"text\" name=\"email\" value=\"" +
                thisUser.getEmailAddress() + "\"></TD></TR>\n"

                + "<TR><TH>First Name:</TH>\n"
                + "<TD><input type=\"text\" name=\"fName\" value=\"" +
                thisUser.getFirstName() + "\"></TD></TR>\n"

                + "<TR><TH>Last Name:</TH>\n"
                + "<TD><input type=\"text\" name=\"lName\" value=\"" +
                thisUser.getLastName() + "\"></TD></TR>\n"

                + "<TR><TH>Style:</TH>\n"
                + "<TD><SELECT name=\"style\">\n"
                + "	<option value=\"basic\">Default Theme (basic)\n"
                + "	<option value=\"plain\">Plain Theme (plain)\n"
                + "	<option value=\"slashdot\">Slashdot (slashdot)\n"
                + "</SELECT></TD></TR></TABLE>\n"
                + "<BR><input type=\"SUBMIT\" value=\"Submit Changes\">\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  }

  /**
   * Method to enter new portfolio values into the DB
   *
   * @param req HTTP servlet request
   * @param user current User ID
   * @return returns a HTML string
   */
  public String postPortfolio(portfolioUser thisUser, HttpServletRequest req) {
    StringBuffer sbuf = new StringBuffer();

    String portfolioName = jlib.cleanString(req.getParameter("portfolioName"));
    String portfolioID = req.getParameter("portfolioID");
    String about = jlib.cleanString(req.getParameter("about"));
    String instructor = req.getParameter("instructor");
    String passwd = req.getParameter("passwd");
    String sysPass = req.getParameter("sysPass");

    sbuf.append(jlib.topBox("Create Portfolio Results:"));

    if (sysPass.equalsIgnoreCase(settings.systemPassword)) {

      // Create Entry in portfolios DB
      jlib.updateDB(
          "INSERT into portfolios(portfolio, name, about, instructor, passwd) "
          + " VALUES( '" + portfolioID + "', '" + portfolioName + "', '" +
          about + "', '" + instructor + "', '" + passwd + "') ");

      // Create entry in motds, just as a place holder till it gets replaced
      jlib.updateDB("INSERT into motd(portfolio, body) VALUES('" + portfolioID +
                    "', 'Welcome Users.') ");

      // Create an administrative entry for this user
      jlib.updateDB("INSERT into admins(portfolio, admin) VALUES ('" +
                    portfolioID + "', '" + thisUser.getUserID() + "')");

      // Create a student user account
      jlib.updateDB("INSERT into students(username, portfolio) VALUES('" +
                    thisUser.getUserID() + "', '" + portfolioID + "') ");

      sbuf.append("Your Portfolio has been created sucessfully!\n"
                  + " You can now <a href=\"/jportfolio/login.jsp?portfolio=" +
                  portfolioID + "\">log in</a> \n"
                  + " to your portfolio and configure it.\n");
    } else {
      sbuf.append("Incorrect system password, email " +
                  "support@iitap.iastate.edu if you need assistance");
    }

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of postPortfolio()

  /**
   * Method to print out a form to create a Portfolio
   *
   * @return String HTML Form Portfolio
   */
  public String createPortfolio() {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append("<form method='POST' action='" + thisPageURL +
                "' name='motd'>\n");
    sbuf.append("<input type='hidden' name='mode' value='u'>\n");
    sbuf.append(jlib.topBox("Create a new Portfolio:"));

    sbuf.append(
        "<font class=\"instructions\"><blockquote>This page is intended for " +
        "instructors to create class portfolios.  In order\n"
        + " to register a portfolio, a system administration password needs " +
          "to be known.\n</blockquote></font>");

    sbuf.append(
        "<BR><FORM method='POST' name='create' action='" + thisPageURL + "'>\n"
        + " <input type='hidden' name='mode' value='u'>\n"

        +
        " <P>System access password: (Contact akrherz@iastate.edu for)<BR>\n"
        + "  <input type='text' name='sysPass' size='10'>\n"

        + " <P>Enter a Portfolio Name:<BR>\n"
        + " <input type='text' name='portfolioName' size='40'>\n"

        + (" <P>Enter a Portfolio Abreviation (no spaces) (Include year) Ex) " +
           "gcp2001:<BR>\n")
        + " <input type='text' name='portfolioID' size='20'>\n"

        + (" <P>Enter Portfolio Description: (appears on Portfolio " +
           "homepage)<BR>\n")
        + " <textarea name='about' cols='50' rows='10'></textarea>\n"

        + " <P>Portfolio challenge password:<BR>\n"
        + " <input name='passwd' type='text' size='40'>\n"

        + " <P>Enter your formal name: (appears on Portfolio homepage)<BR>\n"
        + " <input type='text' name='instructor' size='40'>\n"

        + " <P><input type=\"submit\" value=\"Create Portfolio\"></form>\n");

    sbuf.append("</TD></TR></TABLE>\n");

    return sbuf.toString();
  } // End of createPortfolio()

  /**
   * Produces the available Commands box on the right.
   * @param portfolio - value of the current portfolio
   * @return HTML formatted string
   */
  public String commands(String portfolio) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Commands:"));
    sbuf.append("<ul>\n"
                + " <li><a class=\"commands\" " +
                  "href='/jportfolio/login.jsp?logoutPortfolio=yes'>Switch " +
                  "Portfolios</a></li>\n"
                + "    <LI><a class=\"commands\" href='" + thisPageURL +
                "?mode=u'>Create Portfolio</a></LI>\n"
                +
                ("    <LI><a class=\"commands\" " +
                 "href='/jportfolio/login.jsp?logoutPortfolio=yes'>Register " +
                 "for Portfolio</a></LI>\n"));

    if (portfolio != null) {
      sbuf.append("    <LI><a class=\"commands\" "
                  + " href='" + thisPageURL +
                  "?mode=g'>List group members</a></LI>\n");
      sbuf.append("    <LI><a class=\"commands\" "
                  + " href='" + thisPageURL +
                  "?mode=h'>List portfolio colleagues</a></LI>\n");
      sbuf.append(" <LI><a class=\"commands\" "
                  + " href='" + jlib.httpBase + "/jsp/user/myGrades.jsp' "
                  + " target=\"_new\">Show my grades</a></LI>\n");
    } // End of if

    sbuf.append(" <LI><a class=\"commands\" href='" + thisPageURL +
                "?mode=v'>Set preferences</a></LI>\n");
    sbuf.append("</ul>\n");
    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of commands

  /**
   * Method that prints out introduction to jportfolio
   *
   * @return HTML String for the introduction
   */
  public String badIntro() {
    StringBuffer myBuffer = new StringBuffer();

    myBuffer.append(jlib.topBox("Welcome!"));

    myBuffer.append(
        "<FONT class=\"warn\">Incorrect Login, please try again.</FONT>\n");

    myBuffer.append(
        "<H3 align=\"CENTER\">Welcome to the Portfolio Manager.</H3>\n"
        + "<blockquote>In order to use this system, you must have an " +
          "account.  Once you have an account, you can use the log-in\n"
        + "box in the upper-right hand corner.</blockquote>\n"
        + "<HR width=\"450\"><BR><BR><H3><font color=\"red\">Attention New " +
          "Users:</font></H3>\n"
        +
        "<blockquote>If you are new to the system, you will probably want to \n"
        + " <a href='" + thisPageURL +
        "?mode=i'>Create a New User Account.</blockquote><br>\n"

        + "<H3><font color=\"red\">Help:</font></H3>\n"
        + ("<blockquote>If you have any questions about this system, please " +
           "send\n")
        + ("an email to <a " +
           "href=\"mailto:systems@iitap.iastate.edu\">systems@iitap.iastate." +
           "edu</a> </blockquote><br>\n"));

    myBuffer.append(jlib.botBox());

    return myBuffer.toString();
  } // End of intro()

  /**
   * Method to print out portfolio information that appears right off the bat
   *
   * @param user String value for the current user
   * @param portfolio String value for the current portfolio
   * @return String formated HTML
   */
  public String portfolioMain(portfolioUser thisUser) {
    StringBuffer sbuf = new StringBuffer();
    ResultSet rs = null;
    ResultSet rs2 = null;
    String motd = null;
    String issue = null;
    String about = null;
    String instructor = null;
    String className = null;
    String homepage = null;
    String porthome = null;
    try {
      rs = dbInterface.callDB(
          "SELECT m.body, "
          + " to_char(m.issue, 'DD Month YYYY HH12:MI AM') as issued ,"
          + " m.issue as issue , p.instructor,"
          + " p.about, p.name, p.homepage, p.porthome from portfolios p, "
          + " motd m WHERE p.portfolio = m.portfolio AND"
          + " p.portfolio = '" + thisUser.getPortfolio() + "' "
          + " ORDER by issue DESC LIMIT 1");

      if (rs.next()) {
        motd = rs.getString("body");
        issue = rs.getString("issued");
        about = rs.getString("about");
        instructor = rs.getString("instructor");
        className = rs.getString("name");
        homepage = rs.getString("homepage");
        porthome = rs.getString("porthome");
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }

    if (motd == null) {
      issue = "";
      motd = "Warning!  This should not happen! Email Daryl....";
    }
    sbuf.append("<h3>Welcome to " + className + "</h3>");
    sbuf.append(about + "\n");

    if (porthome != null && !porthome.equalsIgnoreCase("/jportfolio/servlet/"))
      sbuf.append(jlib.alertBox(
          "Portfolio Message",
          "You are currently accessing the <i>generic portfolio interface.</i>"
              + " The " + className +
              " portfolio has a custom interface which can be "
              + " <a href=\"" + porthome + "\">found here.</a>\n"
              + ("<br /><strong>Note:</strong>  You can still use this page, " +
                 "but specialized")
              + " content will be missing.\n"));

    sbuf.append("<P><B><I>Message of the Day (MOTD):</I></b>\n"
                + "<blockquote>\n"
                + "[ <font color=\"red\">" + issue + "</font> ] &nbsp; &nbsp; "
                + "| <a href=\"" + thisPageURL +
                "?mode=n\">List all MOTDs</a> |<BR>\n"
                + "<BR>" + motd + "<BR>"
                + "</blockquote>\n");

    sbuf.append("<P><B><I>Information:</I></b>\n"
                + "<blockquote>\n"
                + "<B>Portfolio Administrator:</B> &nbsp; &nbsp; " +
                instructor + "\n");

    if (homepage != null)
      sbuf.append("<BR><B>Course Website:</B> &nbsp; &nbsp; "
                  + " <a href=\"" + homepage + "\">Here</a>\n");

    sbuf.append("</blockquote>\n");

    sbuf.append("<P><B><I>Available Assignments:</I></B>\n");
    sbuf.append("<blockquote>\n");
    if (jlib.hasQuizToTake(thisUser)) {
      sbuf.append("<a href=\"" + jlib.servletHttpBase +
                  "/jquiz\">Available Quiz!</a>\n");
    } else {
      sbuf.append("None\n");
    }
    sbuf.append("</blockquote>\n");

    sbuf.append(jlib.printMessages(thisUser));

    return sbuf.toString();

  } // End of classPort()

  /**
   * Method to make a HTML side bar for those normal users
   *
   * @param portfolio string value for the current portfolio
   * @return HTML formated string
   */

  public String sideBar(portfolioUser thisUser) {
    StringBuffer theBuffer = new StringBuffer();

    theBuffer.append(jlib.libPortApps());
    theBuffer.append("<P>");
    theBuffer.append(jlib.currentUsers(thisUser.getUserID()));
    theBuffer.append("<P>");
    theBuffer.append(commands(thisUser.getPortfolio()));

    return theBuffer.toString();
  } // End of sideBar()

  /**
   * Method to make a HTML side bar for those admin users
   *
   * @param portfolio string value for the current portfolio
   * @return HTML formated string
   */

  public String adminSideBar(portfolioUser thisUser) {
    StringBuffer theBuffer = new StringBuffer();

    theBuffer.append(jlib.libPortApps());
    theBuffer.append("<P>");
    theBuffer.append(jlib.currentUsers(thisUser.getUserID()));
    theBuffer.append("<P>");
    theBuffer.append(commands(thisUser.getPortfolio()));
    theBuffer.append("<P>");
    theBuffer.append(jlib.adminCommands());

    return theBuffer.toString();
  } // End of adminSideBar()

  /**
   * Method to print out all MOTDs
   *
   * @param thisUser which is the portfolioUser container
   * @return HTML string of all MOTDs
   */
  public String listAllMotd(portfolioUser thisUser) {
    StringBuffer sbuf = new StringBuffer();
    ResultSet rs = null;

    sbuf.append(jlib.topBox("Listing of all MOTDs:"));

    try {
      rs = dbInterface.callDB(
          "SELECT body, to_char(issue, 'DD Month YYYY HH12:MI AM') "
          + " as issued, issue "
          + " from motd WHERE portfolio = '" + thisUser.getPortfolio() + "' "
          + " ORDER by issue DESC");

      while (rs.next()) {
        sbuf.append("<BR><B>Issued On:</B> " + rs.getString("issued") + " "
                    + "<BR><blockquote>" + rs.getString("body") +
                    "</blockquote><HR>");
      }
    } catch (Exception ex) {
      plogger.report("Problem Listing MOTDs\n");
    }

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of listAllMotd()

} // End of jportfolio()
