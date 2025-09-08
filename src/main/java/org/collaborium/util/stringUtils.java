/**
 * Copyright 2001,2003 Iowa State University
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
 * This is a java code to handle miscellaneous String operations
 * that the portfolio code needs to do.
 *
 * @author Daryl Herzmann
 */

package org.collaborium.util;
import java.text.*;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class stringUtils {

  /**
   * Method to correct formating problems when an entry comes from the database
   *
   * @param source String that needs to be fixed
   * @return fixed String marked up with BR tags....
   */
  public static String toBR(String source) {
    if (source == null)
      return "";
    StringTokenizer myTokens = new StringTokenizer(source, "\r", true);
    StringBuffer myBuffer = new StringBuffer();

    if (myTokens.countTokens() > 0 && source != null) {
      do {
        String temp = myTokens.nextToken();
        if (!temp.equals("\r")) {
          myBuffer.append(temp);
        } else {
          myBuffer.append("<br>");
        }
      } while (myTokens.hasMoreTokens());
    } else {
      myBuffer.append(
          "Error in getting Buffer.  Could not split or source was null");
    }
    return myBuffer.toString();
  } // End of toBR()

  /**
   * Method to clean up the apostrophies
   *
   * @param source which is the string we need to parse
   * @return the same string with the necessary changes
   */
  public static String cleanString(String source) {
    if (source == null)
      return "";
    char[] myCharArray = source.toCharArray();
    StringBuffer myBuffer = new StringBuffer();

    for (int i = 0; i < source.length(); i++) {
      String temp = java.lang.String.valueOf(myCharArray[i]);
      if (temp.equals("'")) {
        myBuffer.append("&#180;");
      } else {
        myBuffer.append(temp);
      }
    }
    return myBuffer.toString();
  } // End of cleanString()

  /**
   * Method that parses what the database spits out as a date
   * and converts it into a date format
   * @param String dbDate
   * @return Date
   */
  public static Date dbDate2Date(String dbDate) {
    Date newDate = null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    try {
      newDate = sdf.parse(dbDate);
    } catch (Exception ex) {
      System.err.println("Problem converting SQL date for some reason");
      ex.printStackTrace();
    }

    return newDate;
  }

  /**
   * Method that converts a date into the timestamp String
   * @param myDate which is the date to convert
   * @return String formated like we like
   */
  public static String gmtDate(Date myDate) {
    SimpleDateFormat sdf = new SimpleDateFormat();
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    sdf.applyPattern("dd MMM yyyy HH:mm z");
    return sdf.format(myDate);
  } // End of gmtDate

  public static String date(Date myDate) {
    SimpleDateFormat sdf = new SimpleDateFormat();
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    sdf.applyPattern("dd MMM yyyy");
    return sdf.format(myDate);
  } // End of gmtDate

} // End of stringUtils
