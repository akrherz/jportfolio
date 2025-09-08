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

// Import these for good use.
import java.sql.*; // obviously for the database connections
import java.util.*;
import javax.servlet.http.*;   // for the auth section
import org.collaborium.util.*; // Import IITAP Utilities

/**
 *  Jlib is the base functions for use in the portfolio system,
 *
 *  @author Douglas Fils  (fils@iastate.edu)
 *  @author Daryl Herzmann (akrherz@iastate.edu)
 */

public class jlib {

  public static String servletHttpBase = settings.servletHttpBase;
  public static String httpBase = settings.httpBase;
  private static Hashtable allUsers = new Hashtable();

  /**
   * Method allowing students to sign up for a portfolio.
   *
   * @param thisUser value of the current PortfolioUser
   * @param String value of the HTTP servlet request
   * @return HTML formatted string
   */
  public static String signUpForPortfolio(portfolioUser thisUser,
                                          HttpServletRequest req) {
    String portfolio = req.getParameter("portfolio");
    String portfolio2 = req.getParameter("portfolio2");
    String passwd1 = req.getParameter("portpass");
    StringBuffer sbuf = new StringBuffer();

    ResultSet rs = null;
    String passwd2 = null;
    try {
      rs = dbInterface.callDB(
          "SELECT passwd from portfolios WHERE portfolio = '" + portfolio +
          "' ");
      rs.next();
      passwd2 = rs.getString("passwd");
      if (!passwd2.equals(passwd1)) {
        return "";
      }
    } catch (Exception ex) {
      plogger.report("\nError finding Class Password!");
      return "";
    }

    try {
      updateDB("INSERT into students(username, portfolio) VALUES('" +
               thisUser.getUserID() + "', '" + portfolio + "')");
      if (portfolio2 != null) {
        updateDB("INSERT into students(username, portfolio) VALUES('" +
                 thisUser.getUserID() + "', '" + portfolio2 + "')");
      }
    } catch (Exception ex) {
    }

    sbuf.append(topBox("Sign In:"));
    sbuf.append("Signed Up Successfully! Now you can return to the portfolio "
                + "manager and log into your newly created"
                + " portfolio");
    sbuf.append(botBox());

    return sbuf.toString();
  } // End of createUser()

  /**
   * Method to sign up for a portfolio
   *
   * @param thisUser which is the container for this current user
   * @return HTML formated string to sign up for a portfolio
   */
  public static String registerPortfolio(portfolioUser thisUser) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(topBox("Portfolio Registration:"));

    sbuf.append("<p>"
                + "Listed below are the portfolios that you are <b>not</b> "
                + " signed up for. </p>\n");
    sbuf.append("<form method='POST' action='/jportfolio/login.jsp'>\n");
    sbuf.append(selectPortfolios(thisUser.getUserID()));
    sbuf.append(
        "<P><B>Registration Password:</B> <BR> &nbsp;\n"
        + " <br />\n"
        + " If your portfolio requires a password, please enter it here. \n"
        + " <BR><input type='text' name='portpass' size='20'>\n"
        + " <BR><input type=\"submit\" value=\"Register for Portfolio\">\n");

    sbuf.append(botBox());

    return sbuf.toString();
  } // End of postPortfolio()

  /**
   * Method to print out a month selection widget
   * @param name which is the name of this select form
   * @param selected month which is selected
   * @return HTML formated form
   */
  public static String monthSelect(String name, int selected) {
    StringBuffer sbuf = new StringBuffer();
    String months[] = {"",        "January",  "February", "March",  "April",
                       "May",     "June",     "July",     "August", "September",
                       "October", "November", "December"};
    sbuf.append("<select name=\"" + name + "\">\n");
    for (int i = 1; i < 13; i++) {
    }
    sbuf.append("</select>\n");
    return sbuf.toString();
  }

  /**
   * whichStudent, method that selects which student to work with
   *
   */
  public static String whichStudent(portfolioUser thisUser, String getID,
                                    String thisPageURL) {
    StringBuffer sbuf = new StringBuffer();
    ResultSet rs = null;

    sbuf.append(jlib.topBox("Which Student?"));

    sbuf.append("<P>Current students signed up for this portfolio.<BR>\n");

    sbuf.append("<FORM METHOD=\"GET\" ACTION=\"" + thisPageURL + "\">\n"
                + "<input type=\"hidden\" name=\"mode\" value=\"" + getID +
                "\">\n");
    sbuf.append("<SELECT name=\"selectedUserID\" size=\"5\">\n");
    try {
      rs = dbInterface.callDB("SELECT getUserName(s.username) as name, "
                              + " s.username from students s, users u"
                              + " WHERE s.portfolio = '" +
                              thisUser.getPortfolio() + "' "
                              + " and s.username = u.username ORDER by name");
      while (rs.next()) {
        sbuf.append("<OPTION VALUE=\"" + rs.getString("username") + "\">" +
                    rs.getString("name") + " \n");
      }
    } catch (Exception ex) {
      plogger.report("Err, bad results when quering of students.");
    }
    sbuf.append("</SELECT>\n");

    sbuf.append("<BR><input type=\"SUBMIT\" value=\"View Student\">\n");
    sbuf.append("</FORM>\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();

  } // End of whichStudent

  /**
   * This method creates the layout for the page
   * @param sideContent which is the string for the HTML to appear in the side
   *     bar
   * @param pageContent which is the string for the HTML to appear in the main
   *     page
   * @return HTML formatted string
   */
  public static String makePage(String sideContent, String pageContent) {
    StringBuffer sbuf = new StringBuffer();

    if (sideContent.length() > 0) {
      sbuf.append("<div id=\"portfolio-side\">" + sideContent + "</div>\n");
    }
    sbuf.append("<div id=\"portfolio-content\">" + pageContent + "</div>\n");

    return sbuf.toString();
  }

  /**
   * Method to make a cute little tab box at the top of the page
   *
   * @param insideTxt which is the text in the box
   * @param optional URL link
   * @return HTML formated Box
   */
  public static String tabBox(String liclass, String insideTxt, String urlLink,
                              String selected) {
    StringBuffer sbuf = new StringBuffer();
    String linker = null;

    linker = "<a href=\"" + urlLink + "\">" + insideTxt + "</a>";
    if (selected.equalsIgnoreCase(insideTxt)) {
      liclass = liclass + "-active";
    }
    sbuf.append("<li class=\"" + liclass + "\">" + linker + "</li>\n");

    return sbuf.toString();
  }

  public static String basicHeader(portfolioUser thisUser, String title) {
    StringBuffer sbuf = new StringBuffer();

    String portfolio = null;
    String realname = null;
    String style = null;

    if (thisUser == null) {
      portfolio = "None open";
      realname = "Anonymous";
      style = "basic";
    } else if (thisUser.getPortfolio() == null) {
      portfolio = "None open";
      realname = "<a href=\"/jportfolio/users/" + thisUser.getUserID() + "\">" +
                 thisUser.getRealName() + "</a>";
      style = thisUser.getStyle();
    } else {
      realname = "<a href=\"/jportfolio/users/" + thisUser.getUserID() + "\">" +
                 thisUser.getRealName() + "</a>";
      portfolio = thisUser.getPortfolio();
      style = thisUser.getStyle();
    }

    sbuf.append(
        "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">\n"
        + "<HTML>\n"
        + "<HEAD>\n"
        + " \t<TITLE>" + title + "</TITLE>\n"
        + " \t<link rel=stylesheet type=text/css href=" + httpBase +
        "/css/screen.css>\n"

        + "<script language=\"JavaScript\" type=\"text/javascript\">\n"
        + "<!--//BEGIN Script\n"
        + "function new_window(url) {\n"
        + "	link = \n" +
        ("	window.open(url,\"Link\",\"toolbar=0,location=0,directories="
         + "0,status=0") +
        ",menubar=no,scrollbars=yes,resizable=yes,width=522,height=282\");\n"
        + "} \n"
        + "//END Script-->\n"
        + "</script>\n"

        + "</HEAD>\n"
        + "<body>\n");

    sbuf.append("<div id=\"header-auth\"><B>User:</B></I> " + realname + " ( " +
                portfolio + " ) </div>\n");

    sbuf.append("\n\t<!-- End of jlib initial HTML gen -->\n\n");

    return sbuf.toString();
  } // End of basicHeader()

  /**
   * This method just prints out the generic header for all of portfolio
   * @param title which is the title of the page
   * @param style which is the CSS to use
   * @param selected app that is currently calling
   * @return a HTML formated String
   */
  public static String genHeader(portfolioUser thisUser, String title,
                                 String selected) {

    StringBuffer sbuf = new StringBuffer();

    String portfolio = null;
    String realname = null;
    String style = null;

    if (thisUser == null) {
      portfolio = "None open";
      realname = "Anonymous";
      style = "basic";
    } else if (thisUser.getPortfolio() == null) {
      portfolio = "None open";
      realname = "<a href=\"/jportfolio/users/" + thisUser.getUserID() + "\">" +
                 thisUser.getRealName() + "</a>";
      style = thisUser.getStyle();
    } else {
      realname = "<a href=\"/jportfolio/users/" + thisUser.getUserID() + "\">" +
                 thisUser.getRealName() + "</a>";
      portfolio = thisUser.getPortfolio();
      style = thisUser.getStyle();
    }

    String portfolioName = "";
    if (thisUser != null)
      try {
        portfolioName = thisUser.getPortfolioName();
      } catch (Exception ex) {
        plogger.report("Caught Exception in genHeader");
      }

    if (title == null)
      title = "Portfolio";

    sbuf.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">\n"
                + "<html>\n"
                + "<head>\n"
                + " \t<title>" + title + "</title>\n"
                + " \t<link rel=\"stylesheet\" type=\"text/css\" href=\"" +
                httpBase + "/css/screen.css\" media=\"all\">\n"
                + " \t<link rel=\"stylesheet\" type=\"text/css\" href=\"" +
                httpBase + "/css/print.css\" media=\"print\">\n"
                + "<script src=\"" + httpBase +
                "/js/portfolio.js\" type=\"text/javascript\"></script>\n"
                + "</head>\n"
                + "<body>\n"

                + "<div id=\"portfolio-header\">\n"
                + "<h1>" + portfolioName + "</h1>\n");

    sbuf.append("<div id=\"portfolio-auth\">" + realname + " (" + portfolio +
                ") </div>\n</div>\n");

    sbuf.append("\n<div id=\"mainNavOuter\">\n<div id=\"mainNav\">\n<div "
                + "id=\"mainNavInner\">\n<ul>\n");

    String uri = "";
    /* Okay, if we are logged in */
    if (thisUser != null && thisUser.getPortfolio() != null) {
      /* Manager Page */
      uri = "/jportfolio/servlet/jportfolio";
      if (!thisUser.getBase().equalsIgnoreCase("/jportfolio/servlet/")) {
        uri = thisUser.getBase() + "/index.jsp";
      }
      if (thisUser.getPortfolio().equals("gcp2005")) {
        sbuf.append(tabBox("mainFirst", "GC Homepage", "/gccourse/", selected));
        sbuf.append(tabBox("main", "Manager", uri, selected));
      } else {
        sbuf.append(tabBox("mainFirst", "Manager", uri, selected));
      }

      /* Calendar, nobody overrides at this point :) */
      if (thisUser.usesCalendar) {
        uri = "/jportfolio/jsp/user/myCalendar.jsp";
        sbuf.append(tabBox("main", "Calendar", uri, selected));
      }

      /* Chat, again nobody overrides */
      if (thisUser.usesChat) {
        uri = "/jportfolio/servlet/ChatDispatch";
        sbuf.append(tabBox("main", "Chat", uri, selected));
      }

      /* Dialog */
      if (thisUser.usesDialog) {
        uri = "/jportfolio/servlet/jdot3";
        if (!thisUser.getBase().equalsIgnoreCase("/jportfolio/servlet/")) {
          uri = thisUser.getBase() + "/dialog/index.jsp";
        }
        sbuf.append(tabBox("main", "Dialog", uri, selected));
      }

      /* Forecast */
      if (thisUser.usesForecast) {
        uri = "/jportfolio/servlet/forecast";
        sbuf.append(tabBox("main", "Forecast", uri, selected));
      }

      /* Quiz */
      if (thisUser.usesQuiz) {
        uri = "/jportfolio/servlet/jquiz";
        sbuf.append(tabBox("main", "Quiz", uri, selected));
      }

      if (thisUser.getPortfolio().equals("gcp2005")) {
        sbuf.append(
            tabBox("main", "Syllabus", "/gccourse/courseinfo.html", selected));
        sbuf.append(
            tabBox("main", "Topics", "/gccourse/oncampus.html", selected));
      }
      sbuf.append(tabBox("mainLast", "Logout",
                         "/jportfolio/login.jsp?logout=yes", selected));
    }
    /* Only give option to login, otherwise */
    else {
      sbuf.append(tabBox("mainFirst", "Portfolio", "/jportfolio/", selected));

      /* Login Tab */
      if (thisUser != null &&
          !thisUser.getBase().equalsIgnoreCase("/jportfolio/servlet/")) {
        sbuf.append(tabBox("mainLast", "Login",
                           thisUser.getBase() + "/login.jsp", selected));
      } else {
        sbuf.append(
            tabBox("mainLast", "Login", "/jportfolio/login.jsp", selected));
      }
    }

    sbuf.append("</ul>\n</div>\n</div>\n</div>\n<br clear=\"all\"/>\n");

    return sbuf.toString();
  } // End of genHeader()

  /**
   * Method that prints the Universal HTML that appears at the bottom
   *
   * @param errors string value for the current amount of errors
   * @return HTML formated text
   */
  public static String basicFooter() { return footer(); }

  /**
   * The actual code for the header for all pages, should this be a JSP?
   *
   * @param thisUser which is the current portfolio User
   * @param title which is the current title of this page
   * @param selected which is the string of the selected page
   * @return String HTML header
   */
  public static String header(portfolioUser thisUser, String title,
                              String selected) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(genHeader(thisUser, title, selected));

    return sbuf.toString();

  } // End of header()

  /**
   * Method to find the Instructor ID for a certain portfolio
   *
   * @param String value of the portfolio in question
   * @return String value of the portfolio Admin
   */
  public static String getInstructor(String portfolio) {
    try {
      ResultSet rs = dbInterface.callDB(
          "SELECT admin from admins WHERE portfolio = '" + portfolio + "' ");
      rs.next();
      return rs.getString("admin");
    } catch (Exception ex) {
      System.err.println("Problem getting the instructor for this class.\n");
      ex.printStackTrace();
    }
    return "nuller";
  }

  /**
   * Method to check to see if a user is in a portfolio
   * @param username current username
   * @param portfolio current portfolio value
   * @return true or false
   */
  public static boolean isMember(String username, String portfolio) {
    try {
      ResultSet rs =
          dbInterface.callDB("SELECT * from students "
                             + " WHERE username = '" + username +
                             "' and portfolio = '" + portfolio + "' ");

      if (rs.next())
        return true;
      else
        return false;

    } catch (Exception ex) {
      System.err.println("Problem Baby!");
      ex.printStackTrace();
    }

    return false;
  }

  /**
   * Method to check to see if a user is a user at all
   * @param username current username
   * @param portfolio current portfolio value
   * @return true or false
   */
  public static boolean isUser(String username) {
    if (username == null || username == "")
      return false;
    try {
      ResultSet rs = dbInterface.callDB(
          "SELECT * from users WHERE username = '" + username + "' ");

      if (rs.next())
        return true;
      else
        return false;

    } catch (Exception ex) {
      plogger.report("Problem Baby!");
      ex.printStackTrace();
    }

    return false;
  }

  /**
   * Method that prints the Universal HTML that appears at the bottom
   *
   * @return HTML formated text
   */
  public static String footer() {
    StringBuffer theBuffer = new StringBuffer();

    TimeZone tz = TimeZone.getTimeZone("America/Chicago");
    java.util.Calendar cal = java.util.Calendar.getInstance(tz);
    String timeString = cal.getTime().toString();

    theBuffer.append(
        "<br clear=\"all\" /><div class=\"portfolio-footer\">Portfolio "
        + "Application Suite"
        + "<BR>&copy; 1995-2005 Iowa State University\n"
        + "<BR>[ Local System time is " + timeString + " ]\n"
        + "<br>Your session expires in 3 hours from page load.</div>\n");
    theBuffer.append("</body>\n</html>\n");

    return theBuffer.toString();
  }

  /**
   * Method to print an alert Box
   * @param title string
   * @param body HTML formated body string
   * @return HTML formated string
   */
  public static String alertBox(String title, String body) {
    StringBuffer sbuf = new StringBuffer();
    sbuf.append("<div class=\"alertBox\">\n"
                + "<h3>" + title + "</h3>\n" + body + "</div>");
    return sbuf.toString();
  }

  /**
   * Method to print out listing of links to administrative programs
   *
   * @return HTML string for the box that appears...
   */
  public static String adminCommands() {
    StringBuffer myBuffer = new StringBuffer();

    myBuffer.append(topBox("Admin Access:"));

    myBuffer.append(
        "<ul><li><a "
        + " href='" + servletHttpBase +
        "/jportfolioAdmin'>Portfolio Manager</a></li>\n"
        + " <li><a "
        + " href='" + servletHttpBase + "/jquizAdmin'>Quiz</a></li>\n"
        + " <li><a "
        + " href='" + servletHttpBase + "/forecastAdmin'>Forecast</a></li>\n"
        + " <li><a "
        + " href='" + servletHttpBase + "/jdotAdmin'>Dialog</a></li></ul>");

    myBuffer.append(botBox());

    return myBuffer.toString();
  } // End of adminCommands

  /**
   * Method to return a Date when a user last logged in
   * @param username
   * @return Date of last login
   */
  public static java.sql.Timestamp lastLogin(String username) {
    java.sql.Timestamp lastTime = null;
    try {
      ResultSet rs = dbInterface.callDB(
          "SELECT login from sessions WHERE username "
          + " = '" + username + "' ORDER by login DESC limit 1");
      if (rs.next()) {
        lastTime = rs.getTimestamp("login");
      }
    } catch (Exception ex) {
      plogger.report("Trouble in lastLogin");
    } // End of try

    return lastTime;
  }

  /**
   * Method that adds a user to a userTable for use in currentUsers
   * @param username which is the String of the username
   * @param eventName which is what you want to appear beside name
   */
  public static void addUser(String username, String eventName) {
    String testVal = null;

    if (allUsers.size() > 15)
      allUsers.clear();

    // If the dictionary is not empty
    if (!allUsers.isEmpty()) {
      testVal = (String)allUsers.get((Object)username);
      if (testVal != null) { // IF a value exists for the key
        if (!testVal.equalsIgnoreCase(eventName)) { // If the new value is diff
          allUsers.put((Object)username, eventName);
        }
      } else {
        allUsers.put((Object)username, eventName);
      }
    } else if (username != null) {
      allUsers.put((Object)username, eventName);
    }
  } // End of addUser()

  public static void deleteUser(String username) {
    deleteUser(username, "Blah");
  }

  public static void deleteUser(String username, String eventName) {
    // 	System.err.println("In deleteUser:"+ allUsers.toString() );
    if (!allUsers.isEmpty() && (username != null)) {
      if (allUsers.containsKey((Object)username)) {
        allUsers.remove((Object)username);
      }
    }
    //	System.err.println("Out deleteUser:"+ allUsers.toString() );
  }

  public static String currentUsers(portfolioUser thisUser) {

    return currentUsers(thisUser.getPortfolio(), thisUser.getUserID());
  }

  /**
   * Method currentUsers, prototype
   */
  public static String currentUsers(String userID) {

    return currentUsers("BoboTheClown", userID);
  }

  // return table of the current users
  public static String currentUsers(String portfolio, String thisUser) {
    StringBuffer myBuffer = new StringBuffer();

    myBuffer.append(topBox("Current Users:"));

    //	myBuffer.append( allUsers.toString() + "Hello");

    Enumeration myKeys = allUsers.keys();

    if (!allUsers.isEmpty()) {
      do {
        Object tmp = myKeys.nextElement();
        String nowUser = tmp.toString();

        // plogger.report("ThisUser is :"+thisUser +": NowUser is
        // :"+nowUser+":\n");

        if (nowUser.equalsIgnoreCase(thisUser)) {
          myBuffer.append("<B>" + nowUser + "</B>: ");
        } else {
          myBuffer.append("<a "
                          + "href=\"javascript:new_window('/jportfolio/jsp/"
                          + "user/IMPost.jsp?toUser=" + tmp.toString() +
                          "');\">" + nowUser + "</a>: ");
        }
        myBuffer.append(allUsers.get(tmp).toString() + "<BR>");
      } while (myKeys.hasMoreElements());
    }

    if (IMDatabase.hasMessage(thisUser).booleanValue())
      myBuffer.append("<BR><a "
                      + "href=\"javascript:new_window('/jportfolio/jsp/user/"
                      + "IMCat.jsp');\">You have an IM</a>");

    myBuffer.append(botBox());

    return myBuffer.toString();
  }

  /**
   * Method that makes the HTML box on the side of the web page...
   *
   * @return HTML string for the box
   */
  static public String libPortApps() {

    StringBuffer myBuffer = new StringBuffer();

    //	myBuffer.append( topBoxNoBR("Portfolio Apps:") );

    // +" <a href='/portfolio/servlet/jpinball'>Pinball Simulation<a/><br>\n"
    // +" <a href='/portfolio/servlet/jsib'>Sib Simultation<a/><br>\n"

    //	myBuffer.append("   <a class=\"commands\"
    // href='"+servletHttpBase+"/jportfolio'>Portfolio Manager<a/><br>\n"
    //		+"   <a class=\"commands\"
    // href='"+httpBase+"/jsp/user/myCalendar.jsp'>Calendar</a><BR>\n"
    //		+"   <a class=\"commands\"
    // href='"+servletHttpBase+"/ChatDispatch'>Chat</a><BR>\n"
    //		+"   <a class=\"commands\"
    // href='"+servletHttpBase+"/jdot3'>Dialog</a><br>\n"
    //		+"   <a class=\"commands\"
    // href='"+servletHttpBase+"/forecast'>Forecast</a><br>\n"
    //		+"   <a class=\"commands\"
    // href='"+servletHttpBase+"/jquiz'>Quiz<a/><br>\n"
    //		+"   <a class=\"commands\"
    // href='"+servletHttpBase+"/jportfolio?mode=l'>Log Out</a>\n");

    myBuffer.append("");
    // 	myBuffer.append( botBox() );

    return myBuffer.toString();

  } // End of libPortApps()

  /**
   * Method to return the name of a certain user
   *
   * @param String userID value
   */
  public static String getName(String userID) {
    String name = null;
    ResultSet rs = null;

    try {
      // rs = callDB("select fname, lname from users where username =
      // '"+userID+"' ");
      rs = dbInterface.callDB("SELECT getUserName('" + userID + "') as name");
      rs.next();
      name = rs.getString("name");
    } catch (Exception ex) {
      System.err.println("Could Not get Users real name.\n");
      ex.printStackTrace();
    }
    return name;
  } // End of getName()

  /**
   * Method that returns what group the user is in
   *
   * @param userID String user ID
   * @param portfolio String portfolio value
   * @return returns String gID value
   */
  public static String getGID(String username, String portfolio) {
    String gID = "-99";
    ResultSet rs = null;
    try {
      rs = dbInterface.callDB("SELECT getUserGID('" + portfolio + "', '" +
                              username + "') as gid");
      if (rs.next()) {
        gID = rs.getString("gid");
      }
      if (gID == null || gID.equalsIgnoreCase("null"))
        gID = "-99";
    } catch (Exception ex) {
      System.err.println("Exception caught in getGID");
      ex.printStackTrace();
    }
    System.err.println("Leaving getGID with " + gID);
    return gID;
  } // End of getGID()

  public static String basicSideBar() {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(topBox("General Help"));

    sbuf.append("If you have any questions about this system, please send an "
                + "email to \n"
                + "<a "
                + "href=\"mailto:systems@iitap.iastate.edu\">systems@iitap."
                + "iastate.edu</a>\n");

    sbuf.append(botBox());

    return sbuf.toString();
  }

  /**
   * Returns a boolean of wether the user is an admin of
   * the specified portfolio
   *
   * @param user The user's ID.
   * @param portfolio The portfolio to check
   * @return boolean true if is admin, false if not.
   */
  public static boolean isAdmin(String user, String portfolio) {

    if (portfolio == null || user == null)
      return false;

    try {
      ResultSet rs = dbInterface.callDB("SELECT admin as admin1 from admins "
                                        + " WHERE portfolio = '" + portfolio +
                                        "' and admin = '" + user + "' ");
      if (rs.next())
        return true;
      else
        return false;
    } catch (Exception ex) {
      System.err.println(ex);
    }

    return false;
  } // End of isAdmin()

  public static boolean hasQuizToTake(portfolioUser thisUser) {

    ResultSet rs = null;
    boolean returnVal = false;
    try {
      rs = dbInterface.callDB("SELECT quiznum, qname, attempts from quizes"
                              + " WHERE portfolio = '" +
                              thisUser.getPortfolio() + "' " +
                              (" and startdate < CURRENT_TIMESTAMP and "
                               + "stopdate > CURRENT_TIMESTAMP "));
      if (rs.next()) {
        returnVal = true;
      }

    } catch (Exception ex) {
      System.err.println("Problem trying to get quizes");
    }
    return returnVal;

  } // End of haveQuizToTake

  public static void updateDB(String querry) {

    dbInterface.updateDB(querry);

  } // End of callDB()

  public static String selectPortfolios() {
    return selectPortfolios("HI EverYone");
  }

  /**
   * Method to generate a listing of available portfolios that a user could work
   * through
   *
   * @param value for the current userID
   * @return HTML formated form for a select box of portfolios
   */
  public static String selectPortfolios(String userID) {
    StringBuffer myBuffer = new StringBuffer();

    myBuffer.append("<select name='portfolio' size='10'>\n");
    myBuffer.append("<optgroup label=\"Active Portfolios\">\n");
    try {
      ResultSet rs =
          dbInterface.callDB("SELECT * from portfolios "
                             + " WHERE portfolio NOT IN (SELECT portfolio "
                             + "from students WHERE username = "
                             + " '" + userID + "') and active");

      while (rs.next()) {
        myBuffer.append("<option value='" + rs.getString("portfolio") + "'>" +
                        rs.getString("name"));
      }
    } catch (Exception ex) {
      System.err.println(ex);
    }
    myBuffer.append("</optgroup>\n");

    myBuffer.append("<optgroup label=\"Inactive Portfolios\">\n");
    try {
      ResultSet rs =
          dbInterface.callDB("SELECT * from portfolios "
                             + " WHERE portfolio NOT IN (SELECT portfolio "
                             + "from students WHERE username = "
                             + " '" + userID + "') and not active");

      while (rs.next()) {
        myBuffer.append("<option value='" + rs.getString("portfolio") + "'>" +
                        rs.getString("name"));
      }
    } catch (Exception ex) {
      System.err.println(ex);
    }
    myBuffer.append("</optgroup>\n");
    myBuffer.append("</select>\n");

    return myBuffer.toString();
  } // End of myFolios()

  /**
   * Method that prints out members of a group
   * @param thisUser which is the current user
   * @return HTML formatted string with everybody in it.
   */
  public static String printGroupMembers(portfolioUser thisUser) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(topBox("Group Members"));
    sbuf.append("<blockquote><font color=\"green\">Following is a listing of "
                + "members in your group.</font></blockquote>\n");
    sbuf.append("<P>Group Number: " + thisUser.getGroupID() + " \n<BR>\n");

    sbuf.append(groupMemberListing(thisUser));

    sbuf.append(botBox());

    return sbuf.toString();
  }

  public static String groupMemberListing(portfolioUser thisUser) {
    StringBuffer sbuf = new StringBuffer();
    sbuf.append("<TABLE>\n");
    try {
      ResultSet rs = dbInterface.callDB(
          "SELECT getUserName(username) as realname, "
          + " getEmail(username) as email, username from students "
          + " WHERE portfolio = '" + thisUser.getPortfolio() + "' and "
          + " gid = '" + thisUser.getGroupID() + "' ");
      while (rs.next()) {
        sbuf.append("<TR><TD><a href=\"/jportfolio/users/" +
                    rs.getString("username") + "\"> " +
                    rs.getString("realname") + "</a></TD>\n"
                    + " <TD>( " + rs.getString("email") + " )</TD></TR>\n");
      }

    } catch (Exception ex) {
      plogger.report("Problem getting group members.");
    }

    sbuf.append("</TABLE>");
    return sbuf.toString();
  } // End of groupMemberListing

  /**
   * printPmates()
   *  - print out a listing of users in the Portfolio
   */
  public static String printPmates(portfolioUser thisUser) {
    StringBuffer sbuf = new StringBuffer();
    sbuf.append(topBox("Group Members"));
    sbuf.append("<blockquote><font color=\"green\">Following is a listing of "
                + "members in your portfolio.</font></blockquote>\n");
    sbuf.append("<TABLE>\n");
    try {
      ResultSet rs = dbInterface.callDB(
          "SELECT getUserName(username) as realname, "
          + " getEmail(username) as email, username from students "
          + " WHERE portfolio = '" + thisUser.getPortfolio() + "' ");
      while (rs.next()) {
        sbuf.append("<TR><TD><a "
                    + "href=\"/jportfolio/users/" + rs.getString("username") +
                    "\"> " + rs.getString("realname") + "</a></TD>\n"
                    + " <TD>( " + rs.getString("email") + " )</TD></TR>\n");
      }

    } catch (Exception ex) {
      plogger.report("Problem getting group members.");
    }

    sbuf.append("</TABLE>");
    sbuf.append(jlib.botBox());
    return sbuf.toString();
  } // End of printClassmates

  /**
   * Method to clean a String from bad characters
   *
   * @param String to clean
   * @return String that has been cleaned
   */
  public static String cleanString(String source) {
    return stringUtils.cleanString(source);
  } // End of cleanString()

  /**
   * Method to clean a String from bad spaces
   *
   * @param String to clean
   * @return String that has been cleaned
   */
  public static String cleanGetString(String source) {
    char[] myCharArray = source.toCharArray();
    StringBuffer myBuffer = new StringBuffer();

    for (int i = 0; i < source.length(); i++) {
      String temp = java.lang.String.valueOf(myCharArray[i]);
      if (temp.equals(" ")) {
        myBuffer.append("%20");
      } else {
        myBuffer.append(temp);
      }
    }
    System.err.println(myBuffer.toString());
    return myBuffer.toString();
  } // End of cleanString()

  /**
   * Method to update the user table for a certain value
   *
   * @param userID value of the user to update
   * @param column to be updated
   * @param value for the column
   */
  public static void userUpdate(portfolioUser thisUser, String column,
                                String value) {
    try {
      updateDB("UPDATE users SET " + column + " = '" + value + "' "
               + " WHERE username = '" + thisUser.getUserID() + "' ");

    } catch (Exception ex) {
      System.err.println("Problem updating user Database");
      ex.printStackTrace();
    }

  } // End of userUpdate

  /**
   * Method to update the portfolio table for a certain value
   *
   * @param thisUser which is used to distinguish the wanted portfolio
   * @param column to be updated
   * @param value for the column
   */
  public static void portfolioUpdate(portfolioUser thisUser, String column,
                                     String value) {
    try {
      updateDB("UPDATE portfolios SET " + column + " = '" + value + "' "
               + " WHERE portfolio = '" + thisUser.getPortfolio() + "' ");

    } catch (Exception ex) {
      System.err.println("Problem updating portfolio Database");
      ex.printStackTrace();
    }

  } // End of portfolioUpdate

  /**
   * Method to update the students table for a certain value
   *
   * @param userID value of the user to update
   * @param column to be updated
   * @param value for the column
   */
  public static void studentUpdate(String portfolio, String userID,
                                   String column, String value) {
    try {
      updateDB("UPDATE students SET " + column + " = '" + value + "' "
               + " WHERE username = '" + userID + "' and portfolio = '" +
               portfolio + "' ");

    } catch (Exception ex) {
      System.err.println("Problem updating user Database");
      ex.printStackTrace();
    }

  } // End of userUpdate

  /**
   * Method to print messages
   *
   * @param idnum String value for the id we want removed
   */
  public static void removeNotify(String idnum) {
    try {
      updateDB("DELETE from notify WHERE idnum = " + idnum + " ");

    } catch (Exception ex) {
      System.err.println("Hello, can't delete notify from DB\n");
      ex.printStackTrace();
    }

  } // End of removeNotify

  /**
   * Proxy to the dbInterface.callDB method
   */
  public static ResultSet callDB(String query) {
    return dbInterface.callDB(query);
  }

  /**
   * Method to print out all messages in the notify table
   *
   * @param thisUser which is the current portfolio User
   * @return HTML formatted String
   */
  public static String printAllMessages(portfolioUser thisUser) {
    StringBuffer sbuf = new StringBuffer();

    ResultSet rs = null;
    String message = null;
    String program = null;
    String idnum = null;
    String notifyBaseURL = "/jportfolio/servlet/jportfolio";

    sbuf.append("<P>Messages for You (5 most recent):\n");

    sbuf.append("<TABLE><tr>"
                + "<th>Date:</th>"
                + "<th>Message:</th>"
                + "<td></td></tr>\n");

    try {
      /* We need to get the porthome value for use in notifications */
      ResultSet portHome = dbInterface.callDB(
          "SELECT porthome from portfolios "
          + " WHERE portfolio = '" + thisUser.getPortfolio() + "' and "
          + " porthome != '/jportfolio/servlet/' ");
      if (portHome.next()) {
        notifyBaseURL = "/jportfolio/jsp/user/myNotify.jsp";
      }
      rs = dbInterface.callDB(
          "select to_char(entered, 'Mon dd') as d, "
          + " message, program, idnum from notify "
          + " WHERE username = '" + thisUser.getUserID() + "' and "
          + " portfolio = '" + thisUser.getPortfolio() + "' "
          + " ORDER by entered DESC");

      while (rs.next()) {
        message = rs.getString("message");
        program = rs.getString("program");
        idnum = rs.getString("idnum");
        sbuf.append("<TR>\n"
                    + "<th>" + rs.getString("d") + "</th>\n"
                    + "<TD>" + message + " :</TD>\n"
                    + "<TD><a href=\"" + program +
                    "\">View</a>\n &nbsp; <b>/</b> &nbsp;"
                    + "<a href=\"" + notifyBaseURL +
                    "?mode=o&idnum=" + idnum + "\">Clear</a></TD>\n"
                    + "</TR>\n");
      }

      if (!rs.previous()) {
        sbuf.append(
            "<tr>\n"
            + "<TD colspan=5><b>No messages found for your portfolio</b></td>\n"
            + "</tr>\n");
      }

    } catch (Exception ex) {
      plogger.report(
          "An error was encountered trying to pull messages from DB");
      ex.printStackTrace();
    }

    sbuf.append("</TABLE>\n");

    return sbuf.toString();
  }

  /**
   * Method to print system messages for the notify table
   *
   * @param thisUser which is the current Portfolio User
   * @return HTML formatted string to display
   */
  public static String printMessages(portfolioUser thisUser) {
    StringBuffer sbuf = new StringBuffer();

    ResultSet rs = null;
    String message = null;
    String program = null;
    String idnum = null;

    String notifyBaseURL = "/jportfolio/servlet/jportfolio";
    sbuf.append("<h4>Portfolio Notifications:</h4>");

    try {
      /* We need to get the porthome value for use in notifications */
      ResultSet portHome = dbInterface.callDB(
          "SELECT porthome from portfolios "
          + " WHERE portfolio = '" + thisUser.getPortfolio() + "' and "
          + " porthome != '/jportfolio/servlet/' ");
      if (portHome.next()) {
        notifyBaseURL = "/jportfolio/jsp/user/myNotify.jsp";
      }

      rs = dbInterface.callDB(
          "select to_char(entered, 'Mon dd') as d, "
          + " message, program, idnum from notify "
          + " WHERE username = '" + thisUser.getUserID() + "' and "
          + " portfolio = '" + thisUser.getPortfolio() + "' "
          + " ORDER by entered DESC LIMIT 5");

      if (rs.next()) {
        sbuf.append(
            "<TABLE><tr><th>Date:</th><th>Message:</th><td></td></tr>\n");
        rs.previous();
      }
      while (rs.next()) {
        message = rs.getString("message");
        program = rs.getString("program");
        idnum = rs.getString("idnum");
        sbuf.append("<TR>\n"
                    + "<th>" + rs.getString("d") + "</th>\n"
                    + "<TD>" + message + " :</TD>\n"
                    + "<TD><a href=\"" + program +
                    "\">View</a>\n &nbsp; <b>/</b> &nbsp;"
                    + "<a href=\"" + notifyBaseURL +
                    "?mode=o&idnum=" + idnum + "\">Clear</a></TD>\n"
                    + "</TR>\n");
      }

      if (rs.previous())
        sbuf.append("<TR><TD colspan=\"5\"><a "
                    + "href=\"/jportfolio/jsp/user/myNotify.jsp\">View "
                    + "All</a></TD></TR>\n");
      else {
        sbuf.append("<b>No messages found for your portfolio</b>\n");
      }

    } catch (Exception ex) {
      System.err.println(
          "An error was encountered trying to pull messages from DB");
      ex.printStackTrace();
    }

    sbuf.append("</TABLE>\n");

    return sbuf.toString();
  }

  /**
   * Method to print out a topics select box
   * @param blockid which is a value for a certain block.
   * @return HTML String with topic in
   */
  public static String topicsSelect(portfolioUser thisUser, String blockid) {
    StringBuffer sbuf = new StringBuffer();
    sbuf.append("<SELECT name=\"topicid\">\n");
    try {
      ResultSet rs = dbInterface.callDB(
          "select name, blockid, unitid, "
          + " blockid || '.' || unitid || ' ' || title as uname from units "
          + " WHERE portfolio = '" + thisUser.getPortfolio() +
          "' ORDER by blockid,unitid ASC");

      while (rs.next()) {
        sbuf.append("<option value=\"" + rs.getString("name") + "\">" +
                    rs.getString("uname") + "\n");
      }

    } catch (Exception ex) {
      plogger.report("Troubles getting topics from DB");
    }
    sbuf.append("</select>\n");
    return sbuf.toString();
  } // End of topicsSelect

  /**
   * Print out the current MOTD..
   * @param portfolio Value of the current portfolio
   * @return HTML formated string
   */
  public static String getMotd(String portfolio) {
    StringBuffer sbuf = new StringBuffer();
    try {
      ResultSet rs =
          dbInterface.callDB("SELECT id, body, to_char(issue, 'DD Month YYYY "
                             + "HH12:MI AM') as issued, issue "
                             + " from motd WHERE portfolio = '" + portfolio +
                             "'  ORDER by issue DESC LIMIT 1");
      rs.next();
      sbuf.append("<P>Posted on: " + rs.getString("issued") + "<BR />\n");
      String body = rs.getString("body");
      if (body.length() > 1000) {
        sbuf.append(body.substring(0, 990) + "<p><i>Message truncated...</i><a "
                    + " href=\"/jportfolio/jsp/user/printMotd.jsp?idnum=" +
                    rs.getString("id") + "\">View Entire MOTD</a><br />");
      } else {
        sbuf.append(body);
      }

    } catch (Exception ex) {
      sbuf.append("Can't find MOTD.\n");
    }

    return sbuf.toString();
  }

  public static String blackBoxTop(String title) { return topBox(title); }
  public static String blackBoxBot() { return botBox(); }

  public static String topBoxNoBR(String title) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append("\n\n\t<!-- Begin box element for " + title + " -->\n");
    sbuf.append("<table width='100%' border='0' rowspacing='0' "
                + "cellpadding='3' cellspacing='0'>\n");
    sbuf.append("<tr><td class=\"topBox\" NOWRAP>"
                //	+ "<font size=+1
                // color=\""+boxTitleTextColor+"\"><i>"+title+"</i></font>"
                + title + "</td></tr>\n");
    sbuf.append("<tr><td class=\"botBox\" NOWRAP>\n");
    return sbuf.toString();
  }

  public static String topBox(String title) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append("<div class=\"portfolioBox\">"
                + "<h3>" + title + "</h3>");
    return sbuf.toString();
  }

  /**
   * Method to check to make sure that an userID is currently not being used.
   * @param String userID to test
   * @return Boolean for if this userID exists
   */
  public static Boolean userIDExists(String userID) {
    if (userID == null)
      return Boolean.TRUE;
    try {
      ResultSet rs = dbInterface.callDB(
          "SELECT username from users WHERE username = '" + userID + "' ");
      if (rs.next())
        return Boolean.TRUE;
      else
        return Boolean.FALSE;
    } catch (Exception ex) {
      System.err.println("Problem looking for username in DB.");
      ex.printStackTrace();
    }

    return Boolean.TRUE;
  }

  public static String botBox() {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append("</div>");

    return sbuf.toString();
  }

  public static String toBR(String source) {

    return stringUtils.toBR(source);

  } // End of toBR()

  /**
   * Method to print out a listing of portfolios that the user is signed up
   * for, but has not logged in yet.
   * @param Sting userID
   * @return HTML formated select options
   */
  public static String userPortfolios(String userID) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append("<optgroup label=\"Active Portfolios\">\n");
    try {
      ResultSet rs = dbInterface.callDB(
          "select s.portfolio, p.name as pname"
          + " from students s, portfolios p "
          + " WHERE s.username = '" + userID + "' "
          + " and p.portfolio = s.portfolio and p.active ORDER by pname ASC");
      while (rs.next()) {
        sbuf.append("<option value='" + rs.getString("portfolio") + "'>" +
                    rs.getString("pname") + "</option>\n");
      }
    } catch (Exception ex) {
      System.err.println(ex);
    }
    sbuf.append("</optgroup>\n");
    sbuf.append("<optgroup label=\"Inactive Portfolios\">\n");
    try {
      ResultSet rs =
          dbInterface.callDB("select s.portfolio, p.name as pname"
                             + " from students s, portfolios p "
                             + " WHERE s.username = '" + userID + "' " +
                             (" and p.portfolio = s.portfolio and not "
                              + "p.active ORDER by pname ASC"));
      while (rs.next()) {
        sbuf.append("<option value='" + rs.getString("portfolio") + "'>" +
                    rs.getString("pname") + "</option>\n");
      }
    } catch (Exception ex) {
      System.err.println(ex);
    }
    sbuf.append("</optgroup>\n");
    return sbuf.toString();
  } // End of userPortfolios()
} // End of jlib()
