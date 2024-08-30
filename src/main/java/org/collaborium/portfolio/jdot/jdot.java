/**
 * Copyright 2001,2005 Iowa State University
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
/**
 * Methods for use in the dialog engine.  Just a method container
 *
 * @author Doug Fils fils@collaborium.org
 * @author Daryl Herzmann akrherz@collaborium.org
 *
 */

import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import org.collaborium.portfolio.*;
import org.collaborium.util.*;

public class jdot {

  String servletHttpBase = jlib.servletHttpBase;

  public static String TITLE = "Portfolio Discussion:";
  public static int messageBlock = 10;

  /**
   * Method to create a new Discussion Thread.  Nothing special being done here.
   *
   * @param thisUser  portfolioUser container
   * @param thisPageURL the place to where this form should be posted...
   * @return HTML formatted String
   */
  public static String newThread(portfolioUser thisUser, String thisPageURL) {
    StringBuffer theBuffer = new StringBuffer();

    theBuffer.append(jlib.topBox("New discussion thread:"));
    theBuffer.append(
        "<FORM METHOD='POST' ACTION='" + thisPageURL + "'>\n "
        + "<P><B>Subject:</B><BR>\n"
        + "	<INPUT size=60 NAME='subject' Value=''>\n"
        + "<P><B>Classify Your Post:</B> <i>(Optional)</i> \n" +
        (" <a href=\"/jportfolio/info/dialog.html\" target=\"_new\">More "
         + "Information</a> about classifications.<BR>\n") +
        " <SELECT name='type'>\n"
        + "	<option value='other'>Unclassified\n"
        + "	<option value='ethical'>Ethical Discussion\n"
        + "	<option value='self'>Self Assessment\n"
        + "	<option value='unit'>Unit Topic\n"
        + " </SELECT>\n"

        + " <INPUT type='hidden' name='mode' value='q'>\n "
        + " <INPUT type='hidden' name='threadid' value='new'>\n "
        + "<P><B>Message:</B><br> " +
        (" <TEXTAREA NAME='body' WRAP='Virtual' ROWS=\"20\" "
         + "COLS=\"60\"></TEXTAREA>  ") +
        ("<BR>If you would like to include a link to another webpage, you "
         + "can enter it ") +
        " here:<BR>\n <INPUT TYPE='text' name='link' size='60'>\n"

        + ("<p>(<i>Optional</i>)  If this post is a topic starter, please "
           + "select the \n") +
        " topic to assign it to.\n" + jlib.topicsSelect(thisUser, "0") +
        " <p> <INPUT TYPE='submit' VALUE='Post message'>\n "
        + " <INPUT TYPE='reset' VALUE='Reset form'>\n "
        + " </FORM> ");

    theBuffer.append(jlib.botBox());

    return theBuffer.toString();
  } // End of newThread

  /**
   * bodyItems() -is the framework for how the page is displayed...
   *
   * @param thisUser which is the container for the portfolio User
   * @param skipNum the number of message Blocks to skip
   * @param thisPageURL needed to tell where forms should point
   * @return HTML formatted String for the body items
   */
  public static String bodyItems(portfolioUser thisUser, String skipNum,
                                 String thisPageURL)
      throws SQLException, ClassNotFoundException {
    StringBuffer sbuf = new StringBuffer();
    String threadType = thisUser.getThreadType();

    /** Set up the format of the top of the page.... */
    sbuf.append("<table width=\"100%\" border=0>\n"
                + " <tr><td><font class=\"bodyText\"><b>Restrict Thread "
                + "Types:</b></font>\n"
                + " </td><td>\n"
                + " <form method=\"GET\" action=\"" + thisPageURL + "\">\n"
                + " <select name=\"threadType\" " +
                ("   "
                 + "onChange=\"location=this.form.threadType.options[this."
                 + "form.threadType.selectedIndex].value\">\n") +
                "   <option value=\"" + thisPageURL + "?threadType=all\" ");
    if (threadType.equalsIgnoreCase("all"))
      sbuf.append(" SELECTED ");
    sbuf.append(">ALL \n"
                + "   <option value=\"" + thisPageURL +
                "?threadType=ethical\" ");
    if (threadType.equalsIgnoreCase("ethical"))
      sbuf.append(" SELECTED ");
    sbuf.append(">Ethical Discussion \n"
                + "   <option value=\"" + thisPageURL + "?threadType=unit\" ");
    if (threadType.equalsIgnoreCase("unit"))
      sbuf.append(" SELECTED ");
    sbuf.append(">Unit Topics \n");

    sbuf.append("</select>\n"
                + " </td></tr>");
    sbuf.append("</table>\n");

    if (skipNum == null)
      skipNum = "0";
    int skipMessenges = Integer.parseInt(skipNum) * messageBlock;

    String mySQL = null;
    String s1 = "";
    if (!threadType.equalsIgnoreCase("all")) {
      s1 = " and type = '" + threadType + "' ";
    }

    if (thisUser.getDialogSecurity().equalsIgnoreCase("group")) {
      if (thisUser.isAdmin())
        mySQL = "select count(name) as result from dialog where idnum > 10000 "
                + " and idnum < 200000 and portfolio = '" +
                thisUser.getPortfolio() + "' and security = 'group' " + s1;
      else
        mySQL = "select count(name) as result from dialog where idnum > 10000 "
                + " and idnum < 200000 and portfolio = '" +
                thisUser.getPortfolio() + "' and security = 'group' "
                + " and gid = '" + thisUser.getGroupID() + "' " + s1;
    } // End of group switch
    else if (thisUser.getDialogSecurity().equalsIgnoreCase("private")) {
      if (thisUser.isAdmin())
        mySQL = "select count(name) as result from dialog where idnum > 10000 "
                + " and idnum < 200000 and portfolio = '" +
                thisUser.getPortfolio() + "' and security = 'private' " + s1;
      else
        mySQL = "select count(name) as result from dialog where idnum > 10000 "
                + " and idnum < 200000 and portfolio = '" +
                thisUser.getPortfolio() + "' and security = 'private' "
                + " and (username = '" + thisUser.getUserID() +
                "' or touser = '" + thisUser.getUserID() + "' ) " + s1;
    } // End of private switch
    else {
      mySQL = "select count(name) as result from dialog where idnum > 10000 "
              + " and idnum < 200000 and portfolio = '" +
              thisUser.getPortfolio() + "' and security = '" +
              thisUser.getDialogSecurity() + "' " + s1;
    } // End of everything else

    // Query the db to get the number of messages in this thread
    ResultSet rs3 = dbInterface.callDB(mySQL);

    if (rs3.next()) {
      int numMessages2 = Integer.parseInt(rs3.getString("result"));
      plogger.report("numMessages2 equals " + rs3.getString("result"));
      if (numMessages2 > messageBlock) {
        sbuf.append("<P align='right'>\n  View Page: \n");
        for (int i = 0; i < ((numMessages2 - 1) / messageBlock + 1); i++) {
          if (!skipNum.equalsIgnoreCase(String.valueOf(i))) {
            sbuf.append("<a href='" + thisPageURL + "?skipNum=" + i + "'>" +
                        (i + 1) + "</a> &nbsp;\n");
          } else {
            sbuf.append("<b>" + (i + 1) + "</b> &nbsp;\n");
          }
        }
      } // End of if
    } // End of if
    rs3.close();

    String mySQL2 = null;
    if (thisUser.getDialogSecurity().equalsIgnoreCase("group"))
      if (thisUser.isAdmin())
        mySQL2 = "select *, getRoleName(username, portfolio) as role from "
                 + "dialog where idnum > 10000 "
                 + " and idnum < 200000 and portfolio = '" +
                 thisUser.getPortfolio() + "' and security = 'group' " + s1 +
                 " ORDER BY date DESC LIMIT " + messageBlock + " OFFSET " +
                 skipMessenges;
      else
        mySQL2 = "select *, getRoleName(username, portfolio) as role from "
                 + "dialog where idnum > 10000 "
                 + " and idnum < 200000 and portfolio = '" +
                 thisUser.getPortfolio() + "' and security = 'group' " + s1 +
                 " and gid = '" + thisUser.getGroupID() + "' "
                 + " ORDER BY date DESC LIMIT " + messageBlock + " OFFSET " +
                 skipMessenges;
    else if (thisUser.getDialogSecurity().equalsIgnoreCase("private"))
      if (thisUser.isAdmin())
        mySQL2 = "select *, getRoleName(username, portfolio) as role from "
                 + "dialog where idnum > 10000 "
                 + " and idnum < 200000 and portfolio = '" +
                 thisUser.getPortfolio() + "' and security = 'private' " + s1 +
                 " ORDER BY date DESC LIMIT " + messageBlock + " OFFSET " +
                 skipMessenges;
      else
        mySQL2 = "select *, getRoleName(username, portfolio) as role from "
                 + "dialog where idnum > 10000 "
                 + " and idnum < 200000 and portfolio = '" +
                 thisUser.getPortfolio() + "' and security = 'private' " + s1 +
                 " and (username = '" + thisUser.getUserID() + "' or "
                 + " touser = '" + thisUser.getUserID() + "') "
                 + " ORDER BY date DESC LIMIT " + messageBlock + " OFFSET " +
                 skipMessenges;
    else
      mySQL2 = "select *, getRoleName(username, portfolio) as role from "
               + "dialog where idnum > 10000 "
               + " and idnum < 200000 and portfolio = '" +
               thisUser.getPortfolio() + "' "
               + " and security = '" + thisUser.getDialogSecurity() + "' " +
               s1 + " ORDER BY date DESC LIMIT " + messageBlock + " OFFSET " +
               skipMessenges;

    // Query the db
    ResultSet qrs = dbInterface.callDB(mySQL2);
    if (qrs != null)
      sbuf.append(displayMessages(thisUser, qrs, thisPageURL));

    return sbuf.toString();
  } // End of bodyItems()

  /**
   * Method to display messages found for this query
   *
   * @param rs ResultSet containing the rows to be printed
   * @param thisPageURL String value of where the links should point
   * @return HTML formatted string
   */
  public static String displayMessages(portfolioUser thisUser, ResultSet rs,
                                       String thisPageURL) throws SQLException {

    StringBuffer sbuf = new StringBuffer();

    while (rs.next()) {

      portfolioMessage myMessage = new portfolioMessage(thisUser, rs);

      sbuf.append("<div class=\"dialog-post\">\n");

      sbuf.append(myMessage.printStandard() +
                  myMessage.printExtended(thisPageURL));

      sbuf.append("</div>\n");

    } // End of Loop

    return sbuf.toString();
  } // End of displayMessages()

  /**
   * Simple thing to print out a legend of dialog posts
   * @return HTML Formatted string
   */
  public static String dialogLegend() {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Legend"));
    sbuf.append("<ul>\n");
    sbuf.append("<li class=\"brainstorming\">Brainstorming</li>\n");
    sbuf.append("<li class=\"articulating\">Articulating</li>\n");
    sbuf.append("<li class=\"reacting\">Reacting</li>\n");
    sbuf.append("<li class=\"organization\">Organization</li>\n");
    sbuf.append("<li class=\"analysis\">Analysis</li>\n");
    sbuf.append("<li class=\"generalization\">Generalization</li>\n");
    sbuf.append("<li class=\"social\">Social</li>\n");
    sbuf.append("<li class=\"other\">Other</li>\n");
    sbuf.append("</ul>\n");
    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of dialogLegend()

  /**
   * Method to print help information for the dialog engine..
   *
   * @return HTML formatted text
   */
  public static String servletInfo() {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Dialog Servlet Information:"));

    sbuf.append("<H3>Dialog Help:</H3>\n"
                + "<P>In the left hand collumn under 'Dialog Commands', you "
                + "see various options.<BR>\n"
                + "<UL>\n"
                + "  <LI><i>List Discussion Topics</i> shows the discussion "
                + "topics.</LI>\n"
                + "  <LI><i>Create Discussion Topic</i> allows you to create "
                + "a top level thread.</LI>\n"
                + "  <LI><i>Discussion Quick-view</i> allows you to see all "
                + "of the messages \n"
                + " in a hierarchy of how they relate to each other.</LI>\n"
                + "</UL>\n");

    //	sbuf.append("<P>In order to maintain some sense of order within the
    // discussion, normal users are not allowed\n"
    //		+" to make top level posts within the <b>public</b> dialog
    // discussion.  If there is a topic you would\n"
    //		+" like covered, please contact the portfolio's
    // administrator.\n");

    sbuf.append(
        "<H3>Dialog info:</H3>\n"
        + "<P>Dialog is a java servlet program for collaborative web "
        + "discussions.  The program was\n"
        + "developed at the International Institute of Theoretical and "
        + "Applied Physics\n"
        + " (<a href='http://www.iitap.iastate.edu'>www.iitap.iastate.edu</a>) "
        + "in support of our\n"
        +
        " effort to provide collaborative tools.  For more information, see \n"
        + " <a href='http://www.collaborium.org'>www.collaborium.org</a>.\n"

        + "<P>More information about Dailog can be found <a "
        + "href=\"/jportfolio/info/dialog.html\">here</a>.\n");

    sbuf.append("<H3>Servlet Authors:</H3>\n"
                + " <ol>\n<li>Douglas Fils:</li> (<a "
                + "href='mailto:fils@iastate.edu'>fils@iastate.edu</a>) </li>\n"
                + " <li>Daryl Herzmann:</li> (<a "
                + "href='mailto:akrherz@iastate.edu'>akrherz@iastate.edu</"
                + "a>) </li>\n</ol>");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of servletInfo()

  /**
   * Method to print out the commands that are available
   * @param dialogType String value for the dialogType
   * @param isAdmin Boolean value for if this user is an administrator
   * @return HTML formatted string for this action
   */
  public static String actionBox(portfolioUser thisUser, String thisPageURL) {
    StringBuffer myBuffer = new StringBuffer();

    myBuffer.append(jlib.topBox("Dialog Commands:"));

    myBuffer.append("<ul><LI><a href='" + thisPageURL +
                    "?mode=d'>List Discussion Topics.</a></LI>\n");

    if (!thisUser.getDialogSecurity().equalsIgnoreCase("public") ||
        thisUser.isAdmin())
      myBuffer.append("  <LI><a href='" + thisPageURL +
                      "?mode=n'>Create Discussion Topic.</a></LI>\n");

    myBuffer.append("  <LI><a href=\"" + thisPageURL +
                    "?mode=e\">Discussion Quick-view.</a></LI>\n"
                    + "  <LI><a target=\"_new\" "
                    + "href=\"" + jlib.httpBase +
                    "/jsp/user/myDialog.jsp\">View All My Posts</a></LI>\n"
                    + "  <LI><a href=\"" + thisPageURL +
                    "?mode=i\">Help!</a></LI></ul>\n");

    myBuffer.append(jlib.botBox());

    return myBuffer.toString();
  } // End of actionBox()

  /**
   * Displays the cute little search box on the side of the screen...
   * @param thisPageURL which is the URL this form should link to
   * @return HTML formated String
   */
  public static String searchBox(String thisPageURL) {
    StringBuffer myBuffer = new StringBuffer();

    myBuffer.append("\n<!-- Begin Search Box -->\n");
    myBuffer.append(jlib.topBox("Search Dialog:"));

    myBuffer.append(
        "<FORM method='post' action='" + thisPageURL + "' name='search'>\n"
        + "<INPUT type='hidden' name='mode' value='s'>\n"
        + "Search For:<BR>\n"
        + "<INPUT type='text' name='searchStr' size='15'><BR>\n"
        + "Search On:<BR>\n"
        + "<SELECT name='searchCol'>\n<option value='body'>Posting Text\n"
        + "<option value='name'>Author\n"
        + "<option value='subject'>Subject\n"
        + "</SELECT>\n"
        + "<BR><INPUT type='submit' value='Search'>\n</form>\n");

    myBuffer.append(jlib.botBox());

    return myBuffer.toString();
  } // End of searchBox()

  /**
   * Method that prints out a standard display of a listing of messages
   *
   * @param rs which is the resultset holding our values
   * @param thisPageURL which is needed to print out listing
   */

  public static String listMessages(ResultSet rs, String thisPageURL)
      throws SQLException {
    StringBuffer sbuf = new StringBuffer();
    boolean tester = false;
    while (rs.next()) {
      tester = true;

      portfolioMessage myMessage = new portfolioMessage(rs);

      sbuf.append("<div class=\"dialog-post\">\n");
      sbuf.append(myMessage.printStandard());

      sbuf.append("</div>\n");
    }

    if (!tester)
      sbuf.append("No results for this query.<BR>");

    rs.close(); // again, you must close the result when done

    return sbuf.toString();

  } // End of listMessages()

  /**
   * Method that prints an option for a select menu
   *
   * @param indexVal the value of the select option
   * @param textVal what the user sees as the option value
   * @parm lookFor string of what I may be trying to match
   */
  public static String printOption(String indexVal, String textVal,
                                   String lookFor) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append("<OPTION value=\"" + indexVal + "\" ");
    if (lookFor == null || lookFor.equalsIgnoreCase(indexVal))
      sbuf.append("SELECTED");
    sbuf.append("> " + textVal + "\n");

    return sbuf.toString();
  }

  /**
   * Method to print out a message for anonymous viewing
   * @param thisUser PortfolioUser
   * @param message ResultSet
   * @param indentCols  number of columns to indent
   * @param dataCols number of data columns
   * @return HTML Formatted String
   */
  public static String anonPrint(portfolioUser thisUser, ResultSet message,
                                 String indentCols, String dataCols) {
    StringBuffer sbuf = new StringBuffer();
    try {
      String classification = message.getString("type");
      String portfolio = message.getString("portfolio");
      String link = message.getString("link");
      if (classification == null)
        classification = "generic";

      sbuf.append("<tr>\n"
                  + "  <td rowspan=2 colspan=\"" + indentCols +
                  "\">&nbsp;</td>\n"
                  + "  <td class=\"postTop\" colspan=\"" + dataCols + "\">"
                  + "<font size=+1 class=\"" + classification + "\">" +
                  message.getString("subject") + " (" + classification +
                  ") </font><br>\n");
      if (thisUser != null && thisUser.getPortfolio().equals(portfolio)) {
        sbuf.append("<b>posted by:</b> " + message.getString("name") + "\n");
      }
      sbuf.append("<b>posted on:</b> " +
                  stringUtils.gmtDate(message.getTimestamp("date")) + "</td>\n"
                  + "</tr>\n"
                  + "<tr>\n"
                  + "  <td class=\"postBottom\" colspan=\"" + dataCols +
                  "\" class=\"postBot\">"
                  + "<font class=\"" + classification + "\">" +
                  stringUtils.toBR(message.getString("body")) + "</font>");

      if (link != null && link.length() > 0)
        sbuf.append("<BR><B>Link Ref:</B> <a target=\"_new\" "
                    + " href=\"" + link + "\">" + link + "</a>\n");

      sbuf.append("  </td>\n"
                  + "</tr>\n");
    } catch (Exception ex) {
      ex.printStackTrace();
      plogger.report("Problem in anonPrint() ");
    }

    return sbuf.toString();
  } // End of anonPrint

  /**
   * method to create a tree view of all messages (up to 6 depth)
   *
   * @param sql Statement
   * @param String value of the current portfolio
   */
  public static String treeMessages(portfolioUser thisUser, String skipNum,
                                    String threadID, String thisPageURL)
      throws SQLException {

    StringBuffer sbuf = new StringBuffer();
    sbuf.append("\n<!-- Begin Tree View -->\n");

    sbuf.append(jlib.topBox("Discussion Tree View:"));

    String STRidnum;

    if (skipNum == null)
      skipNum = "0";

    int skipMessenges = Integer.parseInt(skipNum) * messageBlock;

    ResultSet threads = null;
    ResultSet threads2 = null;
    ResultSet threads3 = null;
    ResultSet threads4 = null;
    ResultSet threads5 = null;
    ResultSet threads6 = null;
    ResultSet threads7 = null;

    String SQL = null;
    String SQL2 = null;
    String threadCount = " idnum > 10000 and idnum < 20000 ";
    String threadSpec = "idnum = " + threadID;

    // We need to build the SQL strings, one is for the count and the other is
    // for the actual query.

    if (thisUser.getDialogSecurity().equalsIgnoreCase("group")) {
      if (thisUser.isAdmin()) {
        SQL = "SELECT * from dialog WHERE " + threadSpec + " and "
              + " portfolio = '" + thisUser.getPortfolio() + "' "
              + " and security = 'group' "
              + " order by idnum ASC";
        SQL2 = "SELECT threadid, subject from dialog WHERE " + threadCount + " "
               + " and portfolio = '" + thisUser.getPortfolio() +
               "' and security = 'group' ";
      } else {
        SQL = "SELECT * from dialog WHERE " + threadSpec + " "
              + " and portfolio = '" + thisUser.getPortfolio() + "' "
              + " and gID = '" + thisUser.getGroupID() + "' "
              + " and security = 'group' "
              + " order by idnum ASC";
        SQL2 = "SELECT threadid, subject from dialog WHERE " + threadCount + " "
               + " and portfolio = '" + thisUser.getPortfolio() + "' "
               + " and gID = '" + thisUser.getGroupID() +
               "' and security = 'group' ";
      }
    }

    else if (thisUser.getDialogSecurity().equalsIgnoreCase("private")) {
      if (thisUser.isAdmin()) {
        SQL = "SELECT * from dialog WHERE " + threadSpec + " and "
              + " portfolio = '" + thisUser.getPortfolio() + "' "
              + " and security = 'private' order by idnum "
              + " ASC";
        SQL2 = "SELECT threadid, subject from dialog WHERE "
               + " " + threadCount + " and portfolio = '" +
               thisUser.getPortfolio() + "' "
               + " and security = 'private' ";
      } else {
        SQL = "SELECT * from dialog WHERE " + threadSpec +
              " and portfolio = '" + thisUser.getPortfolio() + "' "
              + " and username = '" + thisUser.getUserID() + "' "
              + " and security = 'private' order by idnum ASC";
        SQL2 = "SELECT threadid, subject from dialog WHERE " + threadCount +
               " and portfolio = '" + thisUser.getPortfolio() + "' "
               + " and username = '" + thisUser.getUserID() + "' "
               + " and security = 'private' ";
      }
    } else {
      SQL = "SELECT * from dialog WHERE " + threadSpec + " and "
            + " portfolio = '" + thisUser.getPortfolio() + "' "
            + " and security = '" + thisUser.getDialogSecurity() +
            "' order by idnum ASC";
      SQL2 = "SELECT threadid, subject from dialog WHERE " + threadCount +
             " and portfolio = '" + thisUser.getPortfolio() + "' "
             + " and security = '" + thisUser.getDialogSecurity() + "' ";
    }

    // Execute the count query
    ResultSet rs3 = dbInterface.callDB(SQL2);

    sbuf.append("<FORM METHOD=\"GET\" ACTION=\"" + thisPageURL + "\">\n"
                + "<input type=\"hidden\" name=\"mode\" value=\"e\">\n"
                + "<SELECT name=\"threadid\" size=\"5\">\n");

    while (rs3.next()) {
      sbuf.append(printOption(rs3.getString("threadid"),
                              rs3.getString("subject"), threadID));
    }

    sbuf.append("</SELECT>\n"
                + "<BR><input type=\"SUBMIT\" value=\"View Thread\">\n"
                + "</FORM>\n");

    if (threadID != null) {

      // Lets make the query and figure out
      threads = dbInterface.callDB(SQL);

      String commonString = "portfolio = '" + thisUser.getPortfolio() + "' "
                            + " and security = '" +
                            thisUser.getDialogSecurity() + "' order by idnum";

      sbuf.append("<p><a href=\"/jportfolio/jsp/user/threadCat.jsp?threadID=" +
                  threadID + "\">Printable View of Thread</a>\n");

      //+"  background-repeat: repeat-y;\n"

      //    +"  background-color: #FFFFFF;\n"

      sbuf.append(
          "<table bgcolor=\"white\" style=\"background-repeat: repeat-y;\""
          + "background=\"/jportfolio/images/dbar.png\" "
          + "width=\"100%\"><tr><td>\n");
      sbuf.append("<ul class=\"dqv\">\n");
      while (threads.next()) {
        STRidnum = threads.getString("idnum");
        sbuf.append(
            printMessageLine(thisUser, STRidnum, 1, "6", threads, thisPageURL));

        String startNum = STRidnum + "0000";

        threads2 = dbInterface.callDB(
            "SELECT * from dialog WHERE idnum > " + startNum + " ::numeric"
            + " and idnum < (" + startNum + " +10000)::numeric and " +
            commonString);

        sbuf.append("<ul class=\"dqv\">\n");
        while (threads2.next()) {
          // if (threads2.isFirst() )
          // sbuf.append("<UL>\n");
          STRidnum = threads2.getString(
              "idnum"); // This shows how to get the value by name
          sbuf.append(printMessageLine(thisUser, STRidnum, 2, "5", threads2,
                                       thisPageURL));

          startNum = STRidnum + "0000";

          threads3 = dbInterface.callDB(
              "SELECT * from dialog WHERE idnum > " + startNum + "::numeric "
              + " and idnum < (" + startNum + "+10000)::numeric and " +
              commonString);

          sbuf.append("<ul class=\"dqv\">\n");
          while (threads3.next()) {
            STRidnum = threads3.getString(
                "idnum"); // This shows how to get the value by name
            sbuf.append(printMessageLine(thisUser, STRidnum, 3, "4", threads3,
                                         thisPageURL));

            startNum = STRidnum + "0000";

            threads4 = dbInterface.callDB(
                "SELECT * from dialog WHERE idnum > " + startNum + "::numeric "
                + " and idnum < (" + startNum + "+10000)::numeric and " +
                commonString);

            sbuf.append("<ul class=\"dqv\">\n");
            while (threads4.next()) {
              STRidnum = threads4.getString(
                  "idnum"); // This shows how to get the value by name
              sbuf.append(printMessageLine(thisUser, STRidnum, 4, "3", threads4,
                                           thisPageURL));

              startNum = STRidnum + "0000";

              threads5 =
                  dbInterface.callDB("SELECT * from dialog WHERE idnum > " +
                                     startNum + "::numeric "
                                     + " and idnum < (" + startNum +
                                     "+10000)::numeric and " + commonString);

              sbuf.append("<ul class=\"dqv\">\n");
              while (threads5.next()) {
                STRidnum = threads5.getString(
                    "idnum"); // This shows how to get the value by name
                sbuf.append(printMessageLine(thisUser, STRidnum, 5, "2",
                                             threads5, thisPageURL));

                startNum = STRidnum + "0000";

                threads6 = dbInterface.callDB(
                    "SELECT * from dialog WHERE idnum > " + startNum +
                    "::numeric "
                    + " and idnum < (" + startNum + "+10000)::numeric and " +
                    commonString);

                sbuf.append("<ul class=\"dqv\">\n");
                while (threads6.next()) {
                  STRidnum = threads6.getString(
                      "idnum"); // This shows how to get the value by name
                  sbuf.append(printMessageLine(thisUser, STRidnum, 6, "1",
                                               threads6, thisPageURL));

                  startNum = STRidnum + "0000";
                  threads7 = dbInterface.callDB(
                      "SELECT * from dialog WHERE idnum > " + startNum +
                      "::numeric "
                      + " and idnum < (" + startNum +
                      "+10000)::numeric and " + commonString);
                  sbuf.append("<ul class=\"dqv\">\n");
                  while (threads7.next()) {
                    STRidnum = threads7.getString("idnum");
                    sbuf.append(printMessageLine(thisUser, STRidnum, 6, "1",
                                                 threads7, thisPageURL));
                  } // while7
                  threads7.close();
                  sbuf.append("</UL>\n");
                } // while6
                threads6.close();
                sbuf.append("</UL>\n");
              } // while5
              threads5.close();
              sbuf.append("</UL>\n");
            } // while4
            threads4.close();
            sbuf.append("</UL>\n");
          } // End of while3()
          threads3.close();
          // if ( threads2.isLast() )
          sbuf.append("</UL>\n");
        } // End of while2()

        sbuf.append("</UL>\n");
        threads2.close();
      } // End of while1()
      threads.close();
      sbuf.append("</UL>\n");

      sbuf.append("</td></tr></table>\n");

    } else {
      sbuf.append("<P>Please select a thread listed above!\n");
    }

    sbuf.append(jlib.botBox());

    return sbuf.toString();

  } // End of listMessages()

  /**
   * printMessageLine() is a function to print out a line for treeView
   * @param thisUser
   * @param STRidnum which is the id of the thread
   * @param number of columns to indent
   * @param number of dataColumns to exist for this message
   * @param ResultSet rs which holds info
   * @param thisPageURL which is neeed for URLS
   * @return HTML formatted string
   */
  public static String printMessageLine(portfolioUser thisUser, String STRidnum,
                                        int indentCols, String dataCols,
                                        ResultSet rs, String thisPageURL)
      throws SQLException {
    StringBuffer sbuf = new StringBuffer();
    String newStr = " ";
    Timestamp tempstamp = thisUser.getLastLogin();

    String name = rs.getString("name");
    String subject = rs.getString("subject");
    String dateStr = rs.getString("date");
    Timestamp postTS = null;
    try {
      postTS = rs.getTimestamp("date");
    } catch (Exception ex) {
      postTS = new java.sql.Timestamp(1000000000);
    }
    String postType = rs.getString("type");
    java.util.Date date = stringUtils.dbDate2Date(dateStr);

    if (tempstamp.before(postTS)) {
      newStr = "<img src=\"/jportfolio/images/new.gif\" alt=\"new\">\n";
    }

    sbuf.append("<li><div style=\"background: #FFFFFF\">\n"
                + "<a class=\"boxlink\" "
                + " href=\"javascript: setLayerDisplay('post" + STRidnum +
                "');\">+/-</a>\n"

                + "<a "
                + "href=\"" + thisPageURL + "?mode=o&idnum=" + STRidnum +
                "\">" + subject + "</a>"
                + " <font class=\"" + postType + "\">"
                + " [" + stringUtils.date(date) + "] by " + name + "</font>" +
                newStr + "<div id=\"post" + STRidnum +
                "\" style=\"display: none;\">" +
                stringUtils.toBR(rs.getString("body")) + "\n"
                + "<br /><a "
                + " href=\"javascript: setLayerDisplay('post" + STRidnum +
                "');\">Hide Post</a></div>\n"
                + "</div></li>\n");

    return sbuf.toString();

  } // End of printMessageLine

  /**
   * Method to print back a view of the post BEFORE submitting it...
   *
   */
  public static String inputPost(portfolioMessage myMessage,
                                 String thisPageURL) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Posting Preview"));
    sbuf.append(
        "<div class=\"alertBox\"><h3>Alert!</h3>"
        + " Here is a preview of your post.  You can hit the 'back' button\n"
        + " on your browser to edit changes to your post.  Unless you click "
        + "the 'Finalize \n"
        + " Post' button, this post will <b>not</b> be saved.\n");

    sbuf.append("<p><form method=\"POST\" action=\"" + thisPageURL + "\">\n"
                + "<input type=\"hidden\" name=\"mode\" value=\"f\">\n"
                + "<input type=\"SUBMIT\" value=\"Finalize Post\">\n"
                + "</form></div>\n");

    sbuf.append("<div class=\"dialog-post\">\n" + myMessage.printStandard() +
                "\n</div>\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of inputPost

  /**
   * Method to post messages to the database.  Thank goodness for the new
   * message container class, since now my job is about 10 times easier
   * @param myMessage message Container
   * @param thisPageURL URl of calling page.
   * @return HTMLformated String
   *
   */
  public static String inputPostFinal(portfolioMessage myMessage,
                                      String thisPageURL)
      throws ClassNotFoundException, SQLException {

    StringBuffer sbuf = new StringBuffer();

    String newthreadid = null;
    String newSTRidnum = null;
    String startNum = null;
    String instructorID = null;
    String notifyBaseURL = "/jportfolio/servlet/jdot3";

    /* We need to get the porthome value for use in notifications */
    ResultSet portHome = dbInterface.callDB(
        "SELECT porthome from portfolios "
        + " WHERE portfolio = '" + myMessage.getPortfolio() + "' and "
        + " porthome != '/jportfolio/servlet/' ");
    if (portHome.next()) {
      notifyBaseURL = portHome.getString("porthome") + "/dialog/index.jsp";
    }

    if (myMessage.getThreadID().equalsIgnoreCase("new")) {
      ResultSet maxth =
          dbInterface.callDB("select MAX (idnum)+1 as result from dialog"
                             + " WHERE idnum > 10000 and idnum < 20000");
      maxth.next();
      newthreadid = maxth.getString("result");
      newSTRidnum = maxth.getString("result");
      if (newthreadid == null) {
        newthreadid = "10001";
        newSTRidnum = "10001";
      }

      myMessage.setidnum(newSTRidnum);
      myMessage.setThreadID(newthreadid);

      if (myMessage.getSecurity().equalsIgnoreCase("group")) {
        ResultSet students = dbInterface.callDB(
            "SELECT username from students WHERE portfolio = "
            + " '" + myMessage.getPortfolio() +
            "' and gid = " + myMessage.getGID() + " and "
            + " username != '" + myMessage.getAuthor() + "' ");
        while (students.next()) {
          String groupUserID = students.getString("username");
          jlib.updateDB(
              "INSERT into notify(username, portfolio, program, message) "
              + " VALUES( '" + groupUserID + "', '" + myMessage.getPortfolio() +
              "', "
              + " '" + notifyBaseURL +
              "?dialogType=group&mode=r&idnum=" + myMessage.getidnum() + "', "
              + " 'Group Discussion Topic Created: " +
              myMessage.getAuthorName() + " posted')");
        }
      }

      myMessage.commitMessage();

    } else { // We have a reply
      startNum = myMessage.getidnum() + "0000";
      newthreadid = myMessage.getThreadID();

      ResultSet maxm = dbInterface.callDB(
          "select MAX(idnum)+1 as result from dialog where idnum > "
          + " " + startNum + "::numeric and idnum < (" + startNum +
          "+10000)::numeric ");
      maxm.next();

      ResultSet test2 = dbInterface.callDB("SELECT security from dialog "
                                           + " WHERE idnum = '" +
                                           myMessage.getThreadID() + "' ");
      test2.next();

      newSTRidnum = maxm.getString("result");
      myMessage.setSecurity(test2.getString("security"));
      if (newSTRidnum == null)
        newSTRidnum = myMessage.getidnum() + "0001"; // first post in the
                                                       // thread

      myMessage.setidnum(newSTRidnum);
      myMessage.setThreadID(newthreadid);

      if (myMessage.getSecurity().equalsIgnoreCase("group")) {
        ResultSet students = dbInterface.callDB(
            "SELECT username from students WHERE portfolio = "
            + " '" + myMessage.getPortfolio() +
            "' and gid = " + myMessage.getGID() + " and "
            + " username != '" + myMessage.getAuthor() + "' "
            + " and username != '" + myMessage.getReplyAuthor() + "' ");
        while (students.next()) {
          String groupUserID = students.getString("username");
          jlib.updateDB(
              "INSERT into notify(username, portfolio, program, message) "
              + " VALUES( '" + groupUserID + "', '" + myMessage.getPortfolio() +
              "', "
              + " '" + notifyBaseURL +
              "?dialogType=group&mode=r&idnum=" + myMessage.getidnum() + "', "
              + " 'Group Discussion Updated: " + myMessage.getAuthorName() +
              " posted')");
        }
      }

      jlib.updateDB(
          "insert into notify values('" + myMessage.getReplyAuthor() + "', "
          + " '" + myMessage.getPortfolio() + "',"
          + " '" + notifyBaseURL + "?mode=r&idnum=" + myMessage.getidnum() +
          "&dialogType=" + myMessage.getSecurity() + "', "
          + " '" + myMessage.getSecurity() +
          " Dialog:  " + myMessage.getAuthorName() + " ')");

      myMessage.commitMessage();
    }

    sbuf.append("<p><div class=\"dialog-post\">\n" + myMessage.printStandard() +
                "\n</div>\n");

    sbuf.append("<P><a href=\"" + thisPageURL + "?mode=r&idnum=" +
                myMessage.getidnum() + "\">View Post</a>\n");

    return sbuf.toString();

  } // End of inputPostFinal

  /**
   * Prototype to postMessage, but without dialogType defined
   */
  public static String postMessage(String threadid, String STRidnum,
                                   String thisPageURL)
      throws SQLException, ClassNotFoundException {
    return postMessage(threadid, STRidnum, thisPageURL, "public");
  }

  /**
   * Method to add a response to somebodies posting.
   *
   * @param threadid which thread we are responding to
   * @param STRidnum value of the post we are responding to
   * @param thisPageURL String value for use in links
   * @param dialogType which is the dialog level
   * @return HTML formated string
   */
  public static String postMessage(String threadid, String STRidnum,
                                   String thisPageURL, String dialogType)
      throws SQLException, ClassNotFoundException {
    StringBuffer theBuffer = new StringBuffer();

    portfolioMessage myMessage = new portfolioMessage(STRidnum);

    theBuffer.append(jlib.topBox("Add posting to discussion topic:"));

    theBuffer.append(
        "<FORM METHOD='POST' ACTION='" + thisPageURL + "'>\n "
        + " <input type='hidden' name='threadid' "
        + " value='" + myMessage.getThreadID() + "'>\n"
        + " <input type='hidden' name='replyAuthor' "
        + " value='" + myMessage.getAuthor() + "'>\n"
        + "<input type='hidden' name='mode' value='q'>\n"

        + "<b>Subject:</b><br />\n"
        + "<INPUT size=60 NAME='subject' Value='" + myMessage.getSubject() +
        "'>\n"

        + "<br /><b>Classify Your Post:</b> &nbsp; <i>(Optional)</i> \n"
        + " <br><a href=\"/jportfolio/info/dialog.html\" "
        + "    target=\"_new\">More Information</a> about classifications.\n"

        + "<br />Classification: <SELECT name='type'>\n"
        + "	<option value='other'>Unclassified\n"
        + "	<option value='analysis'>Analysis\n"
        + "	<option value='articulating'>Articulating\n"
        + "	<option value='brainstorming'>Brainstorming\n"
        + "	<option value='generalization'>Generalization\n"
        + "	<option value='organization'>Organization\n"
        + "	<option value='reacting'>Reacting\n"
        + "	<option value='self'>Self Assessment\n"
        + "	<option value='social'>Social\n"
        + "	<option value='summary'>Discussion Summary\n"
        + " </SELECT>\n"
        + "  <b>-or-</b> Other: \n"
        + "  <input type='text' name='mytype' size='20' maxsize='30'>\n"

        + "<input type='hidden' name='idnum' value='" + myMessage.getidnum() +
        "'>\n"

        + " <P><B>Comments:</B><br> " +
        (" <TEXTAREA NAME='body' WRAP='Virtual' ROWS=\"20\" "
         + "COLS=\"70\"></TEXTAREA>  ") +
        ("<BR>If you would like to include a link to another webpage, you "
         + "can enter it ") +
        " here:<BR>\n <INPUT TYPE='text' name='link' size='60'>\n");

    theBuffer.append(
        "<P><b>Preview your post:</b>\n"
        +
        "<br />You must preview your message before it is added to the dialog."
        + "<br /><INPUT TYPE='submit' VALUE='Preview Message'> "
        + " </FORM> ");

    theBuffer.append(jlib.botBox());

    theBuffer.append("<BR><BR>\n");

    theBuffer.append(jlib.topBox("Reponse to this posting:"));
    theBuffer.append(myMessage.printLong(thisPageURL));
    theBuffer.append(jlib.botBox());

    return theBuffer.toString();
  } // End of postMessage()

  /**
   * method to delete messages from the databases
   *
   * @param String threadID value of the thredID
   */
  public static void deleteMessage(String threadID, String idnum, String oidid)
      throws SQLException {
    String queryStr = "DELETE from dialog WHERE threadid = '" + idnum + "' ";
    jlib.updateDB(queryStr);

  } // End of deleteMessage()

  /**
   *readMore()
   *	- given a threadID and skipNum it generates a resultSet to
   *       hand off to displayMessage() to view
   */
  public static String readMore(portfolioUser thisUser, String idNum,
                                String skipNum, String dialogType,
                                String thisPageURL) throws SQLException {
    if (idNum == null || idNum.length() == 0) {
      return "No dialog found for this identification number";
    }

    StringBuffer myBuffer = new StringBuffer();

    // Number of messages that should be skipped in any result we get
    if (skipNum == null)
      skipNum = "0";
    int skipMessenges = Integer.parseInt(skipNum) * messageBlock;

    BigInteger beginID = new BigInteger(idNum);
    BigInteger endID = new BigInteger(idNum);
    BigInteger secondOne = endID.add(BigInteger.ONE);

    // ResultSet rs2 = dbInterface.callDB("select count(name) as result from
    // dialog "
    //	+"where threadid = " + threadID +" and security = '"+dialogType+"' ");
    ResultSet rs2 = dbInterface.callDB(
        "select count(name) as result from dialog "
        + "where idnum > " + beginID + "0000::numeric and idnum < " +
        secondOne + "0000::numeric ");
    rs2.next();
    int numMessages = Integer.parseInt(rs2.getString("result"));

    if (numMessages > messageBlock) {
      myBuffer.append(
          "<P align='right'><font class=\"bodyText\">Pages:</font> \n");
      for (int i = 0; i < ((numMessages - 1) / messageBlock + 1); i++) {
        if (!skipNum.equalsIgnoreCase(String.valueOf(i))) {
          myBuffer.append("<a class=\"bodyText\" href='" + thisPageURL +
                          "?mode=r&idnum=" + idNum + "&skipNum=" + i + "'>" +
                          (i + 1) + "</a> &nbsp;\n");
        } else {
          myBuffer.append("<b><font class=\"bodyText\">" + (i + 1) +
                          "</font></b> &nbsp;\n");
        }
      }
    } // End of if

    int threadDepth = idNum.length();

    // if length is 9, we need to hit short once and then threadLeader
    // if length is 13, we need to hit short twice

    myBuffer.append(jlib.topBox("Thread Hierarchy:"));

    StringBuffer liS = new StringBuffer();

    //	int j = 1;
    for (int i = 4; i < (threadDepth - 1); i = i + 4) {
      String post = idNum.substring(0, i + 1);
      portfolioMessage qM = new portfolioMessage(post);

      liS.append("<UL>\n");
      myBuffer.append(qM.printShort(thisPageURL));
      liS.append("</UL>\n");
    }

    myBuffer.append(liS.toString());

    myBuffer.append(jlib.botBox());

    myBuffer.append("<P><TABLE width=\"100%\">\n");
    myBuffer.append(threadLeader(idNum, 1, thisPageURL));
    myBuffer.append("</TABLE>\n");

    ResultSet rs = dbInterface.callDB(
        "select * from dialog where idnum > " + beginID + "0000::numeric "
        + " and idnum < " + secondOne + "0000::numeric ORDER BY date DESC "
        + " LIMIT " + messageBlock + " OFFSET " + skipMessenges);

    myBuffer.append("<font class=\"bodyText\" size=\"+2\">Responses to "
                    + "Active Post:</font><BR>\n");
    if (rs != null) {
      myBuffer.append(displayMessages(thisUser, rs, thisPageURL));
    }

    rs.close(); // again, you must close the result when done

    return myBuffer.toString();
  } // End of readMore()

  /**
   * threadLeader()
   *	-method that prints out a special box with the current post that is
   *active
   *
   * @param idNum String value for the post we want to display
   * @param identCols int value for the number of columns that this post needs
   *to be shifted over
   * @return String html
   */

  public static String threadLeader(String idNum, int identCols,
                                    String thisPageURL) throws SQLException {
    StringBuffer sbuf = new StringBuffer();

    portfolioMessage myMessage = new portfolioMessage(idNum);

    sbuf.append("\n\t<!-- Lead Post -->\n<TR>");
    for (int i = identCols; i > 0;
         i--) { // Loop over the amount of indent we need.
      sbuf.append("<TD>-</TD>");
    }

    // Finally Place the active post, make sure to have the correct colspan
    sbuf.append("<TD colspan=\"" + (7 - identCols) + "\" bgcolor=\"white\">\n");

    sbuf.append(jlib.topBox("Active Post:")); // Create Box

    sbuf.append(myMessage.printLong(thisPageURL));

    sbuf.append(jlib.botBox());  // Finish Box Element
    sbuf.append("</TD></TR>\n"); // Finish Row

    return sbuf.toString(); // Return the String
  }

  /**
   * Method that organizes the search for a particular string
   *
   * @param thisUser for the Portfolio
   * @param searchStr string I am looking for
   * @param searchCol column to search for
   * @param skipNum number of results to skip, if we are pagnating
   * @param thisPageURL page we are linking with
   * @return HTML formatted string
   */
  public static String execSearch(portfolioUser thisUser, String searchStr,
                                  String searchCol, String skipNum,
                                  String thisPageURL)
      throws SQLException, ClassNotFoundException {

    StringBuffer sbuf = new StringBuffer();
    if (searchStr == null || searchStr.equals("")) {
      sbuf.append(jlib.topBox("Search Results"));
      sbuf.append("No Results Found...");
      sbuf.append(jlib.botBox());
      return sbuf.toString();
    }

    String queryStr = null;
    if (thisUser.isAdmin())
      queryStr = "SELECT * from dialog WHERE " + searchCol + " ~* '" +
                 stringUtils.cleanString(searchStr) + "' "
                 + " and security = '" + thisUser.getDialogSecurity() +
                 "' and portfolio = '" + thisUser.getPortfolio() +
                 "' ORDER BY date DESC";
    else
      queryStr = "SELECT * from dialog WHERE " + searchCol + " ~* "
                 + " '" + stringUtils.cleanString(searchStr) + "' "
                 + " and security = 'public' "
                 + " and portfolio = '" + thisUser.getPortfolio() +
                 "' and gid = " + thisUser.getGroupID() +
                 " ORDER BY date DESC";

    ResultSet rs;

    rs = dbInterface.callDB(queryStr);

    sbuf.append(displayMessages(thisUser, rs, thisPageURL));

    rs.close();

    return sbuf.toString();

  } // End of execSearch()

  /**
   * Method to create a box that switches the dialog that is currently active
   *
   * @param dialogType value of the current dialogType, so to make the right on
   *     selected
   * @param thisPageURL value of the page to return values to...
   * @return HTML formatted box for the switch dialog type
   */
  public static String switchDialogType(String dialogType, String thisPageURL) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append("\t<!-- Begin change dialog type box -->\n");
    sbuf.append("\n<form method=\"GET\" name=\"switchBox\" action=\"" +
                thisPageURL + "\">\n");
    sbuf.append(jlib.topBox("Dialog Level:"));
    sbuf.append("<SELECT name=\"dialogType\" "
                + "onChange=\"location=this.form.dialogType.options[this."
                + "form.dialogType.selectedIndex].value\">\n");

    sbuf.append("	<option value=\"" + thisPageURL +
                "?dialogType=public\" ");
    if (dialogType.equalsIgnoreCase("public"))
      sbuf.append("SELECTED");
    sbuf.append(">Public\n");

    sbuf.append("	<option value=\"" + thisPageURL +
                "?dialogType=group\" ");
    if (dialogType.equalsIgnoreCase("group"))
      sbuf.append("SELECTED");
    sbuf.append(">Group\n");

    sbuf.append("	<option value=\"" + thisPageURL +
                "?dialogType=private\" ");
    if (dialogType.equalsIgnoreCase("private"))
      sbuf.append("SELECTED");
    sbuf.append(">Private\n");

    sbuf.append("</SELECT>\n" + jlib.botBox());

    sbuf.append("</form>\n\t<!-- End of Change of dailog Type -->\n");

    return sbuf.toString();
  } // End of switchDialogType

} // End of jdotbase
