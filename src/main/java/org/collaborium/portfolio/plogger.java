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
package org.collaborium.portfolio;

/**
 * Logger class for handle error messages from the portfolio.
 * Variables for this class are set in the init servlet files
 * Options include logging to standard out or to a text file.
 */

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class plogger {

  /**
   * Method that reports an message from the portfolio.
   */
  public static void report(String errorMessage) {
    if (errorMessage != null)
      System.err.println(errorMessage);
  }

  /**
   * Method to mail me messages that I may want to see
   */
  public static void mail(String errorMessage) {
    Properties props = new Properties();
    props.put("mail.smtp.host", "localhost");

    Session session = Session.getInstance(props, null);

    try {
      MimeMessage msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress("nobody@iitappc1.iitap.iastate.edu"));
      msg.setRecipients(Message.RecipientType.TO, "akrherz@iastate.edu");
      msg.setSubject("Portfolio Error MSG");

      String content =
          "\n"
          + "# This email was generated from the Portfolio Website \n" +
          errorMessage;

      msg.setText(content);

      Transport.send(msg);
    } catch (MessagingException e) {
      e.printStackTrace();
    }

  } // End of mail

} // End of plogger
