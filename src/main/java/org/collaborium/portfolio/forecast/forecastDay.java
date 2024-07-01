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

import java.io.*;
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
  private Integer floaterHighTemp = new Integer(-99);
  private Integer floaterLowTemp = new Integer(-99);
  private Integer floaterPrecCat = new Integer(-99);
  private Integer floaterSnowCat = new Integer(-99);

  /** forecasted values by User for local */
  private Integer localHighTemp = new Integer(-99);
  private Integer localLowTemp = new Integer(-99);
  private Integer localPrecCat = new Integer(-99);
  private Integer localSnowCat = new Integer(-99);

  /** Validation Values of this date for the floater site */
  private Integer VfloaterHighTemp = new Integer(-99);
  private Integer VfloaterLowTemp = new Integer(-99);
  private Integer VfloaterPrecCat = new Integer(-99);
  private String VfloaterPrecNum = "-99";
  private Integer VfloaterSnowCat = new Integer(-99);
  private String VfloaterSnowNum = "-99";

  /** Validation Values for the local site */
  private Integer VlocalHighTemp = new Integer(-99);
  private Integer VlocalLowTemp = new Integer(-99);
  private Integer VlocalPrecCat = new Integer(-99);
  private String VlocalPrecNum = "-99";
  private Integer VlocalSnowCat = new Integer(-99);
  private String VlocalSnowNum = "-99";

  /** Climate Values of this date for the floater site */
  private Integer CfloaterHighTemp = new Integer(-99);
  private Integer CfloaterLowTemp = new Integer(-99);
  private Integer CfloaterPrecCat = new Integer(-99);
  private String CfloaterPrecNum = "-99";
  private Integer CfloaterSnowCat = new Integer(-99);
  private String CfloaterSnowNum = "-99";

  /** Climate Values for the local site */
  private Integer ClocalHighTemp = new Integer(-99);
  private Integer ClocalLowTemp = new Integer(-99);
  private Integer ClocalPrecCat = new Integer(-99);
  private String ClocalPrecNum = "-99";
  private Integer ClocalSnowCat = new Integer(-99);
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
        this.VfloaterHighTemp = new Integer(rs.getInt("float_high"));
        this.VfloaterLowTemp = new Integer(rs.getInt("float_low"));
        this.VfloaterPrecCat = new Integer(rs.getInt("float_prec"));
        this.VfloaterPrecNum = rs.getString("float_prec_txt");
        this.VfloaterSnowCat = new Integer(rs.getInt("float_snow"));
        this.VfloaterSnowNum = rs.getString("float_snow_txt");

        // plogger.report("I am setting VlocalSnowCat to "+
        // rs.getString("local_snow") );

        this.VlocalHighTemp = new Integer(rs.getInt("local_high"));
        this.VlocalLowTemp = new Integer(rs.getInt("local_low"));
        this.VlocalPrecCat = new Integer(rs.getInt("local_prec"));
        this.VlocalPrecNum = rs.getString("local_prec_txt");
        this.VlocalSnowCat = new Integer(rs.getInt("local_snow"));
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
        this.CfloaterHighTemp = new Integer(rs.getInt("float_high"));
        this.CfloaterLowTemp = new Integer(rs.getInt("float_low"));
        this.CfloaterPrecCat = new Integer(rs.getInt("float_prec"));
        this.CfloaterPrecNum = rs.getString("float_prec_txt");
        this.CfloaterSnowCat = new Integer(rs.getInt("float_snow"));
        this.CfloaterSnowNum = rs.getString("float_snow_txt");

        this.ClocalHighTemp = new Integer(rs.getInt("local_high"));
        this.ClocalLowTemp = new Integer(rs.getInt("local_low"));
        this.ClocalPrecCat = new Integer(rs.getInt("local_prec"));
        this.ClocalPrecNum = rs.getString("local_prec_txt");
        this.ClocalSnowCat = new Integer(rs.getInt("local_snow"));
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

        + " <TR><TH>High Temp:</TH><TD>" + VlocalHighTemp.toString() +
        "</TD>\n"
        + " <TD>" + VfloaterHighTemp.toString() + "</TD></TR>\n"

        + " <TR><TH>Low Temp:</TH><TD>" + VlocalLowTemp.toString() + "</TD>\n"
        + " <TD>" + VfloaterLowTemp.toString() + "</TD></TR>\n"

        + " <TR><TH>Precip:</TH><TD>CAT: " + VlocalPrecCat.toString() + " "
        + " ( " + VlocalPrecNum + ") </TD>\n"
        + " <TD>CAT: " + VfloaterPrecCat.toString() + " "
        + " ( " + VfloaterPrecNum + ")</TD></TR>\n"

        + " <TR><TH>Snowfall:</TH><TD>CAT: " + VlocalSnowCat.toString() + " "
        + " ( " + VlocalSnowNum + ") </TD>\n"
        + " <TD>CAT: " + VfloaterSnowCat.toString() + " "
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
        this.floaterHighTemp = new Integer(rs.getInt("float_high"));
        this.floaterLowTemp = new Integer(rs.getInt("float_low"));
        this.floaterPrecCat = new Integer(rs.getInt("float_prec"));
        this.floaterSnowCat = new Integer(rs.getInt("float_snow"));

        this.localHighTemp = new Integer(rs.getInt("local_high"));
        this.localLowTemp = new Integer(rs.getInt("local_low"));
        this.localPrecCat = new Integer(rs.getInt("local_prec"));
        this.localSnowCat = new Integer(rs.getInt("local_snow"));

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

  public String getFloaterHighTemp() { return floaterHighTemp.toString(); }
  public String getFloaterLowTemp() { return floaterLowTemp.toString(); }
  public String getFloaterPrecCat() { return floaterPrecCat.toString(); }
  public String getFloaterSnowCat() { return floaterSnowCat.toString(); }

  public String getLocalHighTemp() { return localHighTemp.toString(); }
  public String getLocalLowTemp() { return localLowTemp.toString(); }
  public String getLocalPrecCat() { return localPrecCat.toString(); }
  public String getLocalSnowCat() { return localSnowCat.toString(); }

  public String getVFloaterHighTemp() { return VfloaterHighTemp.toString(); }
  public String getVFloaterLowTemp() { return VfloaterLowTemp.toString(); }
  public String getVFloaterPrecCat() { return VfloaterPrecCat.toString(); }
  public String getVFloaterPrecNum() { return VfloaterPrecNum.toString(); }
  public String getVFloaterSnowCat() { return VfloaterSnowCat.toString(); }
  public String getVFloaterSnowNum() { return VfloaterSnowNum.toString(); }

  public String getVLocalHighTemp() { return VlocalHighTemp.toString(); }
  public String getVLocalLowTemp() { return VlocalLowTemp.toString(); }
  public String getVLocalPrecCat() { return VlocalPrecCat.toString(); }
  public String getVLocalPrecNum() { return VlocalPrecNum.toString(); }
  public String getVLocalSnowCat() { return VlocalSnowCat.toString(); }
  public String getVLocalSnowNum() { return VlocalSnowNum.toString(); }

  public String getCFloaterHighTemp() { return CfloaterHighTemp.toString(); }
  public String getCFloaterLowTemp() { return CfloaterLowTemp.toString(); }
  public String getCFloaterPrecCat() { return CfloaterPrecCat.toString(); }
  public String getCFloaterPrecNum() { return CfloaterPrecNum.toString(); }
  public String getCFloaterSnowCat() { return CfloaterSnowCat.toString(); }
  public String getCFloaterSnowNum() { return CfloaterSnowNum.toString(); }

  public String getCLocalHighTemp() { return ClocalHighTemp.toString(); }
  public String getCLocalLowTemp() { return ClocalLowTemp.toString(); }
  public String getCLocalPrecCat() { return ClocalPrecCat.toString(); }
  public String getCLocalPrecNum() { return ClocalPrecNum.toString(); }
  public String getCLocalSnowCat() { return ClocalSnowCat.toString(); }
  public String getCLocalSnowNum() { return ClocalSnowNum.toString(); }

  public String getFloaterSite() { return floaterSite; }
  public String getFloaterSiteID() { return floaterSiteID; }

  public String getLocalSite() { return localSite; }
  public String getLocalSiteID() { return localSiteID; }

  public String getCaseGroup() { return caseGroup; }

  public String getConfidence() { return confidence; }
  public String getDiscussion() { return discussion; }
} // End of class
