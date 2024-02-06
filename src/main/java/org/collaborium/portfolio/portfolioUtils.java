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
/**
 * A set of methods that are needed to various portfolio apps
 * They are not necessarily unique to a portfolio App
 * @author Daryl Herzmann
 */

package org.collaborium.portfolio;

import com.oreilly.servlet.*;
import java.io.*;
import java.sql.*;
import org.collaborium.portfolio.*;

public class portfolioUtils {

  /**
   * This method allows an instructor to email an entire portfolio
   *
   * @param portfolio value of the current portfolio that needs to be modified
   * @param message which is the string value of the message to be sent out
   * @return string for the status of this method...
   */
  public static String emailPortfolio(String message, String subject,
                                      portfolioUser thisUser) {
    StringBuffer sbuf = new StringBuffer();
    String mailhost = "localhost"; // or another mail host
    String from = thisUser.getEmailAddress();
    String portfolio = thisUser.getPortfolio();
    String to = "akrherz@iastate.edu";

    sbuf.append(jlib.topBox("Results of Email Posting"));
    sbuf.append("<P>Sent message to:<BR><blockquote>\n");

    if (subject == null || message == null) {
      sbuf.append("You did not complete the form.  Try again.");

    } else {
      try {
        MailMessage msg = new MailMessage(mailhost);
        msg.from(from);
        msg.to(to);
        msg.setSubject("[" + portfolio + "] " + subject);
        msg.cc(from);

        ResultSet rs = dbInterface.callDB(
            "SELECT s.username, u.email from students s, users u "
            + " WHERE s.portfolio = '" + portfolio +
            "' and s.username = u.username ");
        while (rs.next()) {
          String email = rs.getString("email");
          if (email != null) {
            msg.bcc(email);
            sbuf.append(rs.getString("username") + " , ");
          }
        }
        sbuf.append("</blockquote>\n");

        PrintStream out = msg.getPrintStream();
        out.println("\n"
                    + "# This email was generated from the Portfolio Website \n"
                    + "# You have received this email \n"
                    + "#  because you are a member of the " + portfolio +
                    " portfolio \n");

        out.println(message);
        msg.sendAndClose();

      } catch (Exception ex) {
        System.err.println("Problem sending email");
        ex.printStackTrace();
      }
      sbuf.append("<P>Posted Message:\n<blockquote>\n" + message +
                  "</blockquote>\n");

      sbuf.append("<P>Message processing completed.");
    }

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  }

} // End of portfolioUtils
