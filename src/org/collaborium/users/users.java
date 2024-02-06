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

/**
 * Servlet that will handle the user side of portfolio activities
 * Some of the neat features planned for this application is
 *	o  Upload Pictures of themselves
 *	o  Display information about the person
 *	o  Able to link their homepage in
 *
 * @author Daryl Herzmann
 */

package org.collaborium.portfolio.users;

import java.io.*;
import java.lang.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.collaborium.portfolio.*;
import org.collaborium.util.*;

public class users extends HttpServlet {

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    plogger.report("---- Begin users");

    /** Standard stuff to set up the servlet **/
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    StringBuffer sideContent = new StringBuffer();
    StringBuffer pageContent = new StringBuffer();

    /** Get information from the Request **/
    String location = request.getRequestURI();
    HttpSession session = request.getSession(true);
    Boolean writePerm = Boolean.FALSE;
    portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
    String userName = getUserFromURI(location);

    if (thisUser == null)
      writePerm = Boolean.FALSE;
    else if (thisUser.getUserID().equals(userName))
      writePerm = Boolean.TRUE;

    out.println(jlib.basicHeader(thisUser, "User Homepage"));

    if (jlib.isUser(userName) && userName.length() > 0) {
      out.println(
          "<P align=\"center\"><B>Welcome to my portfolio homepage!</B><HR>\n");
      out.println(printInfo(userName, writePerm));
    } else {
      out.println("<FONT class=\"error\">This username '" + userName + "' "
                  + " does not exist. Sorry!</FONT>\n");
    }

    out.println(jlib.basicFooter());

    plogger.report("---- End users");
  } // End of doGet()

  /**
   * This makes the Picture available
   *
   */
  public String printInfo(String requestedUser, Boolean writePerm) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append("<TABLE><TR><TD valign=\"top\">\n");

    sbuf.append("<!-- My Picture -->\n");
    sbuf.append(
        "	<table cellpadding=0 cellspacing=0 border=1 width=200>\n"
        + "	<caption><B>My Picture:</B></caption>\n"
        + "	<TR bgcolor=\"ffefd5\"><TD>\n"
        + "	<BR>\n"
        + "	<CENTER><img src=\"/jportfolio/FILES/" + requestedUser +
        "/me.gif\"></CENTER>\n"
        + "	<BR>\n");

    if (writePerm.booleanValue())
      sbuf.append(
          "	<a href=\"/jportfolio/jsp/user/customize/editPict.jsp\">Edit</a>\n");

    sbuf.append("	</TD></TR></TABLE>\n");
    sbuf.append("<!-- End of My Picture -->\n");

    sbuf.append("<br /><ul>");

    sbuf.append(jlib.topBox("My Portfolios"));
    try {
      ResultSet rs = dbInterface.callDB(
          "select s.portfolio, p.name, "
          + " p.porthome from students s, portfolios p WHERE "
          + " s.username = '" + requestedUser + "' and "
          + " p.portfolio = s.portfolio");

      while (rs.next()) {
        sbuf.append("<li><a "
                    + " href=\"/jportfolio/servlet/jportfolio?portfolio=" +
                    rs.getString("portfolio") + "\">" + rs.getString("name") +
                    "</a></li>\n");
      }

    } catch (Exception ex) {
      plogger.report("Problem in printInfo()");
    }
    sbuf.append("</ul>" + jlib.botBox());

    sbuf.append("</TD><TD valign=\"top\">\n");

    sbuf.append("<p align=\"center\"><B>My Information:</B>");

    String bioSketch = null;
    try {
      ResultSet rs = dbInterface.callDB(
          "SELECT getUserName(username) as realn, * from users"
          + " WHERE username = '" + requestedUser + "' ");

      if (rs.next()) {
        sbuf.append("<TABLE>\n");
        sbuf.append("<TR><TD>Real Name:</TD><TD> " + rs.getString("realn") +
                    "</TD></TR>\n"
                    + "<TR><TD>Email Address:</TD><TD> " +
                    rs.getString("email") + "</TD></TR>\n");
        sbuf.append("</TABLE>\n");
        //	  if ( writePerm.booleanValue() )
        //	  	sbuf.append("	<a
        // href=\"/jportfolio/jsp/user/customize/editInfo.jsp\">Edit</a>\n");
      }

      ResultSet rs2 =
          dbInterface.callDB("SELECT body from biosketch WHERE "
                             + " username = '" + requestedUser + "' ");
      if (rs2.next())
        bioSketch = rs2.getString("body");

    } catch (Exception ex) {
      plogger.report("Problem in printInfo()");
    }

    java.sql.Timestamp lastLogin = jlib.lastLogin(requestedUser);
    SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
    sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
    if (lastLogin != null)
      sbuf.append("<P><B>Last Login at:</B>" + sdf.format(lastLogin));

    sbuf.append("<P><B>Bio Sketch:</B><BR>\n");
    if (bioSketch != null)
      sbuf.append(stringUtils.toBR(bioSketch) + "\n");
    if (writePerm.booleanValue())
      sbuf.append(
          " <a href=\"/jportfolio/jsp/user/customize/editBio.jsp\">Edit</a>\n");

    sbuf.append("</TD></TR></TABLE>\n");

    return sbuf.toString();
  }

  /**
   * Method to get the user specified from the given location
   * @param location String value of the current location
   * @return String value of the user requested
   */
  public String getUserFromURI(String location) {

    // Okay, /jportfolio/users/ is 26 chars
    String userName = location.substring(18);
    plogger.report("User parsed name is :" + userName + ":");

    return userName;

  } // End of getUserFromURI

} // End of users
