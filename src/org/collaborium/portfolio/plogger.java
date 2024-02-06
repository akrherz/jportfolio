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

import com.oreilly.servlet.*;
import java.io.*;

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

    try {
      MailMessage msg = new MailMessage("localhost");
      msg.from("nobody@iitappc1.iitap.iastate.edu");
      msg.to("akrherz@iastate.edu");
      msg.setSubject("Portfolio Error MSG");

      PrintStream out = msg.getPrintStream();
      out.println("\n"
                  + "# This email was generated from the Portfolio Website \n");

      out.println(errorMessage);
      msg.sendAndClose();

    } catch (Exception ex) {
      System.err.println("Problem sending email");
      ex.printStackTrace();
    }

  } // End of mail

} // End of plogger
