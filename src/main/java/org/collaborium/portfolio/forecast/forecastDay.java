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
package org.collaborium.portfolio.forecast;
/**
 * Java class for forecast days, will clean up other java
 * programs
 * @author Daryl Herzmann 7 Sep 2001
 */

import java.sql.*;
import org.collaborium.portfolio.*;

public class forecastDay {

  private String userID = null;
  private String portfolio = null;
  private String sqlDate = null;

  private String floaterSite = "Undefined";
  private String floaterSiteID = "UND";
  private String localSite = "Des Moines, IA";
  private String localSiteID = "DSM";

  private String caseGroup = "1";

  private String confidence = "0";
  private String discussion = "None entered";

  /** forecasted values by User for float */
  private int floaterHighTemp = -99;
  private int floaterLowTemp = -99;
  private int floaterPrecCat = -99;
  private int floaterSnowCat = -99;

  /** forecasted values by User for local */
  private int localHighTemp = -99;
  private int localLowTemp = -99;
  private int localPrecCat = -99;
  private int localSnowCat = -99;

  /** Validation Values of this date for the floater site */
  private int VfloaterHighTemp = -99;
  private int VfloaterLowTemp = -99;
  private int VfloaterPrecCat = -99;
  private String VfloaterPrecNum = "-99";
  private int VfloaterSnowCat = -99;
  private String VfloaterSnowNum = "-99";

  /** Validation Values for the local site */
  private int VlocalHighTemp = -99;
  private int VlocalLowTemp = -99;
  private int VlocalPrecCat = -99;
  private String VlocalPrecNum = "-99";
  private int VlocalSnowCat = -99;
  private String VlocalSnowNum = "-99";

  /** Climate Values of this date for the floater site */
  private int CfloaterHighTemp = -99;
  private int CfloaterLowTemp = -99;
  private int CfloaterPrecCat = -99;
  private String CfloaterPrecNum = "-99";
  private int CfloaterSnowCat = -99;
  private String CfloaterSnowNum = "-99";

  /** Climate Values for the local site */
  private int ClocalHighTemp = -99;
  private int ClocalLowTemp = -99;
  private int ClocalPrecCat = -99;
  private String ClocalPrecNum = "-99";
  private int ClocalSnowCat = -99;
  private String ClocalSnowNum = "-99";

  public forecastDay(String newPortfolio, String newSqlDate) {
    this.portfolio = newPortfolio;
    this.sqlDate = newSqlDate;

    try {
      ResultSet rs =
          dbInterface.callDB("SELECT * from forecast_days "
                             + " WHERE portfolio = '" + portfolio + "' and "
                             + " day = '" + sqlDate + "' ");
      if (rs.next()) {
        this.floaterSite = rs.getString("floater_city");
        this.floaterSiteID = rs.getString("floater_abv");
        this.caseGroup = rs.getString("case_group");
      }

    } catch (Exception ex) {
      plogger.report("Problem getting the forecast day base parameters.\n");
    }
  }

  /**
   * Constructor for building forecast days
   * @param thisUser portfolioUser it must be
   * @param sqlDate which is the date of this forecastDay
   */
  public forecastDay(portfolioUser newThisUser, String newSqlDate) {

    this.sqlDate = newSqlDate;
    this.userID = newThisUser.getUserID();
    this.portfolio = newThisUser.getPortfolio();

    try {
      ResultSet rs =
          dbInterface.callDB("SELECT * from forecast_days "
                             + " WHERE portfolio = '" + portfolio + "' and "
                             + " day = '" + sqlDate + "' ");
      if (rs.next()) {
        this.floaterSite = rs.getString("floater_city");
        this.floaterSiteID = rs.getString("floater_abv");
        this.caseGroup = rs.getString("case_group");
      }

    } catch (Exception ex) {
      plogger.report("Problem getting the forecast day base parameters.\n");
    }

  } // End of forecastDay

  /**
   * Method to extract the answers for a date
   */
  public boolean getValidation() {
    if (portfolio == null || sqlDate == null)
      return false;

    try {

      ResultSet rs =
          dbInterface.callDB("SELECT * from forecast_answers "
                             + " WHERE portfolio = '" + portfolio + "' "
                             + " and day = '" + sqlDate + "' ");
      if (rs.next()) {
        this.VfloaterHighTemp = rs.getInt("float_high");
        this.VfloaterLowTemp = rs.getInt("float_low");
        this.VfloaterPrecCat = rs.getInt("float_prec");
        this.VfloaterPrecNum = rs.getString("float_prec_txt");
        this.VfloaterSnowCat = rs.getInt("float_snow");
        this.VfloaterSnowNum = rs.getString("float_snow_txt");

        // plogger.report("I am setting VlocalSnowCat to "+
        // rs.getString("local_snow") );

        this.VlocalHighTemp = rs.getInt("local_high");
        this.VlocalLowTemp = rs.getInt("local_low");
        this.VlocalPrecCat = rs.getInt("local_prec");
        this.VlocalPrecNum = rs.getString("local_prec_txt");
        this.VlocalSnowCat = rs.getInt("local_snow");
        this.VlocalSnowNum = rs.getString("local_snow_txt");

        return true;
      } // End of rs.next()

    } catch (Exception ex) {
      plogger.report("Problem getting Validation");
      ex.printStackTrace();
    }

    return false;
  }
  /**
   * Method to extract the answers for a date
   */
  public boolean getClimo() {
    if (portfolio == null || sqlDate == null)
      return false;

    try {

      ResultSet rs =
          dbInterface.callDB("SELECT * from forecast_climo "
                             + " WHERE portfolio = '" + portfolio + "' "
                             + " and day = '" + sqlDate + "' ");
      if (rs.next()) {
        this.CfloaterHighTemp = rs.getInt("float_high");
        this.CfloaterLowTemp = rs.getInt("float_low");
        this.CfloaterPrecCat = rs.getInt("float_prec");
        this.CfloaterPrecNum = rs.getString("float_prec_txt");
        this.CfloaterSnowCat = rs.getInt("float_snow");
        this.CfloaterSnowNum = rs.getString("float_snow_txt");

        this.ClocalHighTemp = rs.getInt("local_high");
        this.ClocalLowTemp = rs.getInt("local_low");
        this.ClocalPrecCat = rs.getInt("local_prec");
        this.ClocalPrecNum = rs.getString("local_prec_txt");
        this.ClocalSnowCat = rs.getInt("local_snow");
        this.ClocalSnowNum = rs.getString("local_snow_txt");

        return true;
      } // End of rs.next()

    } catch (Exception ex) {
      plogger.report("Problem getting Climo Forecast");
      ex.printStackTrace();
    }

    return false;
  }

  /**
   * Method to print out the answers for this forecast
   * @return HTML formatted String
   */
  public String catAnswers() {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(
        "<P><TABLE border=1>\n"
        + "<tr><th colspan=3>Forecast Verification for: " + sqlDate +
        " </th></tr> \n"
        + "<TR><TD></TD><TD>" + localSite + "( " + localSiteID + " ) </TD>\n"
        + " <TD>" + floaterSite + "( " + floaterSiteID + " ) </TD></TR>\n"

        + " <TR><TH>High Temp:</TH><TD>" + String.valueOf(localHighTemp) +
        "</TD>\n"
        + " <TD>" + String.valueOf(VfloaterHighTemp) + "</TD></TR>\n"

        + " <TR><TH>Low Temp:</TH><TD>" + String.valueOf(VlocalLowTemp) +
        "</TD>\n"
        + " <TD>" + String.valueOf(VfloaterLowTemp) + "</TD></TR>\n"

        + " <TR><TH>Precip:</TH><TD>CAT: " + String.valueOf(VlocalPrecCat) +
        " "
        + " ( " + VlocalPrecNum + ") </TD>\n"
        + " <TD>CAT: " + String.valueOf(VfloaterPrecCat) + " "
        + " ( " + VfloaterPrecNum + ")</TD></TR>\n"

        + " <TR><TH>Snowfall:</TH><TD>CAT: " + String.valueOf(VlocalSnowCat) +
        " "
        + " ( " + VlocalSnowNum + ") </TD>\n"
        + " <TD>CAT: " + String.valueOf(VfloaterSnowCat) + " "
        + " ( " + VfloaterSnowNum + ")</TD></TR>\n"
        + "</TABLE>\n");

    return sbuf.toString();
  }

  /**
   * Method to retrieve the users forecast from the DB
   */
  public boolean getForecast() {
    if (portfolio == null || sqlDate == null || userID == null)
      return false;

    try {
      ResultSet rs = dbInterface.callDB(
          "SELECT * from forecasts "
          + " WHERE userid = '" + userID + "' and portfolio = "
          + " '" + portfolio + "' and day = '" + sqlDate + "' ");
      if (rs.next()) {
        this.floaterHighTemp = rs.getInt("float_high");
        this.floaterLowTemp = rs.getInt("float_low");
        this.floaterPrecCat = rs.getInt("float_prec");
        this.floaterSnowCat = rs.getInt("float_snow");

        this.localHighTemp = rs.getInt("local_high");
        this.localLowTemp = rs.getInt("local_low");
        this.localPrecCat = rs.getInt("local_prec");
        this.localSnowCat = rs.getInt("local_snow");

        this.discussion = rs.getString("discussion");
        this.confidence = rs.getString("confidence");
        return true;
      } // End of rs.next()

    } catch (Exception ex) {
      plogger.report("Problem getting User's Forecast");
      ex.printStackTrace();
    }
    return false;
  } // End of getForecast()

  public String getFloaterHighTemp() { return String.valueOf(floaterHighTemp); }
  public String getFloaterLowTemp() { return String.valueOf(floaterLowTemp); }
  public String getFloaterPrecCat() { return String.valueOf(floaterPrecCat); }
  public String getFloaterSnowCat() { return String.valueOf(floaterSnowCat); }

  public String getLocalHighTemp() { return String.valueOf(localHighTemp); }
  public String getLocalLowTemp() { return String.valueOf(localLowTemp); }
  public String getLocalPrecCat() { return String.valueOf(localPrecCat); }
  public String getLocalSnowCat() { return String.valueOf(localSnowCat); }

  public String getVFloaterHighTemp() {
    return String.valueOf(VfloaterHighTemp);
  }
  public String getVFloaterLowTemp() { return String.valueOf(VfloaterLowTemp); }
  public String getVFloaterPrecCat() { return String.valueOf(VfloaterPrecCat); }
  public String getVFloaterPrecNum() { return String.valueOf(VfloaterPrecNum); }
  public String getVFloaterSnowCat() { return String.valueOf(VfloaterSnowCat); }
  public String getVFloaterSnowNum() { return String.valueOf(VfloaterSnowNum); }

  public String getVLocalHighTemp() { return String.valueOf(VlocalHighTemp); }
  public String getVLocalLowTemp() { return String.valueOf(VlocalLowTemp); }
  public String getVLocalPrecCat() { return String.valueOf(VlocalPrecCat); }
  public String getVLocalPrecNum() { return String.valueOf(VlocalPrecNum); }
  public String getVLocalSnowCat() { return String.valueOf(VlocalSnowCat); }
  public String getVLocalSnowNum() { return String.valueOf(VlocalSnowNum); }

  public String getCFloaterHighTemp() {
    return String.valueOf(CfloaterHighTemp);
  }
  public String getCFloaterLowTemp() { return String.valueOf(CfloaterLowTemp); }
  public String getCFloaterPrecCat() { return String.valueOf(CfloaterPrecCat); }
  public String getCFloaterPrecNum() { return String.valueOf(CfloaterPrecNum); }
  public String getCFloaterSnowCat() { return String.valueOf(CfloaterSnowCat); }
  public String getCFloaterSnowNum() { return String.valueOf(CfloaterSnowNum); }

  public String getCLocalHighTemp() { return String.valueOf(ClocalHighTemp); }
  public String getCLocalLowTemp() { return String.valueOf(ClocalLowTemp); }
  public String getCLocalPrecCat() { return String.valueOf(ClocalPrecCat); }
  public String getCLocalPrecNum() { return String.valueOf(ClocalPrecNum); }
  public String getCLocalSnowCat() { return String.valueOf(ClocalSnowCat); }
  public String getCLocalSnowNum() { return String.valueOf(ClocalSnowNum); }

  public String getFloaterSite() { return floaterSite; }
  public String getFloaterSiteID() { return floaterSiteID; }

  public String getLocalSite() { return localSite; }
  public String getLocalSiteID() { return localSiteID; }

  public String getCaseGroup() { return caseGroup; }

  public String getConfidence() { return confidence; }
  public String getDiscussion() { return discussion; }
} // End of class
