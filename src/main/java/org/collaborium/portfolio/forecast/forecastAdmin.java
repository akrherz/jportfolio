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
 * Servlet that handles administrative duties of the forecast
 *  activity in Portfolio.
 *
 * @author Daryl Herzmann 4 July 2001
 */
package org.collaborium.portfolio.forecast;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.collaborium.portfolio.*;

public class forecastAdmin extends HttpServlet {

  String thisPageURL = "/jportfolio/servlet/forecastAdmin";

  /**
   * Method to handle method GET to the servlet
   * @param HttpServletRequest
   * @param HttpServletResponse
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    plogger.report("---- ForecastAdmin\n");
    StringBuffer sideContent = new StringBuffer();
    StringBuffer pageContent = new StringBuffer();

    PrintWriter out = response.getWriter();
    response.setContentType("text/html");

    HttpSession session = request.getSession(true);
    portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
    String callMethod = (String)request.getParameter("mode");
    String sqlDate = (String)request.getParameter("sqlDate");
    String selectedUserID = (String)request.getParameter("selectedUserID");
    String thisPageURL = "/jportfolio/servlet/forecastAdmin";

    if (thisUser == null || !thisUser.isAdmin()) {
      response.setHeader("Refresh", "0; URL=./jportfolio");
      callMethod = "z"; // Abort
    } else {
      jlib.addUser(thisUser.getUserID(), "forecastAdmin");
    }

    out.println(jlib.header(thisUser, "Forecasting Activity", "forecast"));

    if (callMethod == null)
      callMethod = "z";

    switch (callMethod.charAt(0))
    // Current calling methods
    // y == Create Forecast Day Dialog
    // v == Grade Forecast Dialog
    // a == Which Admins Forecast Dialog
    // m == Modify A Certain Students Performance
    // n == I have stuid and sqlDate, lets modify forecast
    {
    case 'm':
      pageContent.append(alterStudent(thisUser, thisPageURL));
      break;
    case 'n':
      pageContent.append(
          makeForecast(thisUser, selectedUserID, sqlDate, thisPageURL));
      break;
    case 'a':
      pageContent.append(whoForecasts(thisUser, thisPageURL));
      pageContent.append(whatTime2Forecast(thisUser, thisPageURL));
      break;
    case 'b':
      String changeuser = (String)request.getParameter("changeuser");
      String changeto = (String)request.getParameter("changeto");
      jlib.studentUpdate(thisUser.getPortfolio(), changeuser, "nofx", changeto);
      pageContent.append("<P>Processing Results!\n");
      pageContent.append(whoForecasts(thisUser, thisPageURL));
      break;
    case 'y':
      pageContent.append(createForecastDay(thisPageURL));
      break;
    case 'u':
      try {
        pageContent.append(
            fLib.whichDay(thisUser.getPortfolio(), "v", thisPageURL));
      } catch (Exception ex) {
      }
      break;
    case 'v':
      pageContent.append(createValidation(thisUser, sqlDate));
      break;
    case 'w':
      try {
        pageContent.append(
            fLib.whichDay(thisUser.getPortfolio(), "x", thisPageURL));
      } catch (Exception ex) {
      }
      break;
    case 'x':
      pageContent.append(viewAllForecasts(thisUser, sqlDate));
      break;
    }

    sideContent.append(adminOptions(thisUser, thisPageURL));

    out.println(jlib.makePage(sideContent.toString(), pageContent.toString()));

    // Finish the page and close output
    out.write(jlib.footer());
    out.close();
  }

  /**
   * Method to handle servlet POST actions
   * @param HttpServletRequest
   * @param HttpServletResponse
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    plogger.report("---- ForecastAdmin\n");
    StringBuffer sideContent = new StringBuffer();
    StringBuffer pageContent = new StringBuffer();

    PrintWriter out = response.getWriter();
    response.setContentType("text/html");

    HttpSession session = request.getSession(true);
    portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
    String callMethod = (String)request.getParameter("mode");
    String sqlDate = (String)request.getParameter("sqlDate");

    String selectedUserID = (String)request.getParameter("selectedUserID");

    if (thisUser == null || !thisUser.isAdmin()) {
      response.setHeader("Refresh", "0; URL=./jportfolio");
      callMethod = "z"; // Abort
    } else {
      jlib.addUser(thisUser.getUserID(), "forecastAdmin");
    }

    out.println(jlib.header(thisUser, "Forecasting Activity", "forecast"));

    if (callMethod == null)
      callMethod = "x";
    switch (callMethod.charAt(0))
    // Current calling methods
    // y == Create New Forecast Date
    // v == Enter Validation
    // a == Change Settings on which admins forecast
    // f == Enter forecast
    // t == change the time in which forecasts are due...
    {
    case 'f':
      pageContent.append("<P>Entering Forecast values...<BR>\n");
      try {
        pageContent.append(enterForecast(thisUser, selectedUserID, request));
      } catch (Exception ex) {
        plogger.mail(ex.getMessage());
        plogger.report("Problem Entering Forecast.\n" + ex);
        pageContent.append("<P>An error occured processing your forecast"
                           + "<BR> " + ex +
                           ". <BR>Please try to forecast Again!");
      }

      pageContent.append("<P>Re-running Grading for this day...<BR>\n");
      try {
        pageContent.append(
            fLib.gradeForecasts(thisUser.getPortfolio(), sqlDate));
        pageContent.append(fLib.totalForecastErrors(thisUser.getPortfolio()));
      } catch (Exception ex) {
        plogger.report("Problem with Forecast Validation");
        ex.printStackTrace();
      }

      break;
    case 't':
      String fxtime = (String)request.getParameter("fxtime");
      if (fxtime != null) {
        jlib.portfolioUpdate(thisUser, "fxtime", fxtime);
      }

      pageContent.append(whoForecasts(thisUser, thisPageURL));
      pageContent.append(whatTime2Forecast(thisUser, thisPageURL));

      break;
    case 'y':
      pageContent.append(addForecastingDay(request, thisUser.getPortfolio()));
      break;
    case 'v':
      try {
        pageContent.append(enterForecastValidation(thisUser, request));
        pageContent.append(
            fLib.gradeForecasts(thisUser.getPortfolio(), sqlDate));
        pageContent.append(fLib.totalForecastErrors(thisUser.getPortfolio()));
      } catch (Exception ex) {
        plogger.report("Problem with Forecast Validation");
        ex.printStackTrace();
      }
      break;
    }

    sideContent.append(adminOptions(thisUser, thisPageURL));
    pageContent.append("Nothing");
    out.println(jlib.makePage(sideContent.toString(), pageContent.toString()));

    // Finish the page and close output
    out.write(jlib.footer());
    out.close();

    plogger.report("--- End ForecastAdmin");
  } // End of doPost

  public String viewAllForecasts(portfolioUser thisUser, String sqlDate) {
    StringBuffer sbuf = new StringBuffer();

    forecastDay thisDay = new forecastDay(thisUser, sqlDate);
    thisDay.getValidation();

    sbuf.append(thisDay.catAnswers());

    sbuf.append(
        "<HR><TABLE><TR>\n"
        + "<TH rowspan=\"2\">Forecaster:</TH><TH colspan=\"4\">" +
        thisDay.getLocalSite() + "</TH><TH colspan=\"4\">" +
        thisDay.getFloaterSite() + "</TH></TR>\n"
        + "<TR>\n"
        + "<TD>High:</TD><TD>Low:</TD><TD>Prec Cat:</TD><TD>Snow Cat:</TD>\n"
        + "<TD>High:</TD><TD>Low:</TD><TD>Prec Cat:</TD><TD>Snow Cat:</TD>\n"
        + "</TR>\n");
    try {
      ResultSet rs = dbInterface.callDB(
          "SELECT *, getUserName(userid) as rname from forecasts WHERE"
          + " portfolio = '" + thisUser.getPortfolio() + "' and day = '" +
          sqlDate + "' ");
      while (rs.next()) {
        sbuf.append("<TR><TD>" + rs.getString("rname") + "</TD>\n"
                    + "<TD>" + rs.getString("local_high") + "</TD>\n"
                    + "<TD>" + rs.getString("local_low") + "</TD>\n"
                    + "<TD>" + rs.getString("local_prec") + "</TD>\n"
                    + "<TD>" + rs.getString("local_snow") + "</TD>\n"
                    + "<TD>" + rs.getString("float_high") + "</TD>\n"
                    + "<TD>" + rs.getString("float_low") + "</TD>\n"
                    + "<TD>" + rs.getString("float_prec") + "</TD>\n"
                    + "<TD>" + rs.getString("float_snow") + "</TD>\n"
                    + "</TR>\n");
      }
    } catch (Exception ex) {
      plogger.report("Problem retrieving date in viewAllForecasts");
    }

    sbuf.append("</TABLE>\n");

    return sbuf.toString();
  }

  public String whatTime2Forecast(portfolioUser thisUser, String thisPageURL) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Forecast Deadline"));
    sbuf.append("<blockquote class=\"instructions\">You must specify a local "
                + "time at which the\n"
                + " forecasts are due by.  This time is in the format of "
                + "24HH, which means that 7 PM \n"
                + " is actually 19.</blockquote>\n");

    sbuf.append("<FORM METHOD=\"POST\" action=\"" + thisPageURL + "\">\n");
    sbuf.append("<input type=\"hidden\" name=\"mode\" value=\"t\">\n");
    sbuf.append("<P>Fx is due by: ");

    try {
      ResultSet rs =
          dbInterface.callDB("SELECT fxtime from portfolios WHERE "
                             + " portfolio = '" + thisUser.getPortfolio() +
                             "' and fxtime IS NOT NULL");
      if (rs.next())
        sbuf.append("<input type=\"text\" name=\"fxtime\" value "
                    + " =\"" + rs.getString("fxtime") +
                    "\" size=\"2\" maxlength=\"2\">\n");
      else
        sbuf.append("<input type=\"text\" name=\"fxtime\" "
                    + " size=\"2\" maxlength=\"2\">\n");

    } catch (Exception ex) {
      plogger.report("Booo, problem in whatTime2Forecat.");
    }

    sbuf.append("<INPUT TYPE=\"SUBMIT\">\n"
                + "</form>\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of whatTime2Forecast

  public String makeForecast(portfolioUser thisUser, String selectedUserID,
                             String sqlDate, String thisPageURL) {
    StringBuffer sbuf = new StringBuffer();

    String local_high = "0";
    String local_low = "0";
    String local_prec = "0";
    String local_snow = "0";
    String float_high = "0";
    String float_low = "0";
    String float_prec = "0";
    String float_snow = "0";

    if (sqlDate == null)
      sqlDate = "'TODAY'::date + '1 day'::interval";

    try {
      ResultSet rs = jlib.callDB("SELECT * from forecasts "
                                 + " WHERE day = '" + sqlDate +
                                 "' and userID = '" + selectedUserID + "' "
                                 + " and portfolio = '" +
                                 thisUser.getPortfolio() + "' ");
      if (rs.next()) {
        local_high = rs.getString("local_high");
        local_low = rs.getString("local_low");
        local_prec = rs.getString("local_prec");
        local_snow = rs.getString("local_snow");
        float_high = rs.getString("float_high");
        float_low = rs.getString("float_low");
        float_prec = rs.getString("float_prec");
        float_snow = rs.getString("float_snow");
      }
    } catch (Exception ex) {
      plogger.report("Problem Fetching Previous Forecast");
      ex.printStackTrace();
    }

    sbuf.append(
        "<form method='POST' action='" + thisPageURL + "'>\n"
        + " <input type='hidden' name='mode' value='f'>\n"
        + " <input type='hidden' name='sqlDate' value=\"" + sqlDate + "\">\n"
        + " <input type='hidden' name='selectedUserID' value=\"" +
        selectedUserID + "\">\n"
        + " <P><TABLE>\n" +
        (" <TR><TD></TD><TD>High Temp:</TD><TD>Low Temp:</TD><TD>Precip "
         + "Cat:</TD><TD>Snow Cat:</TD></TR>\n") +
        " <TR><TH>Local:</TH>\n" +
        ("	<TD><input type='text' size='4' MAXLENGTH='3' "
         + "name='local_high' value='") +
        local_high + "'></TD>\n" +
        ("	<TD><input type='text' size='4' MAXLENGTH='3' "
         + "name='local_low' value='") +
        local_low + "'></TD>\n"
        + "	<TD>" + fLib.rainSelect("local_prec", local_prec) + "</TD>\n"
        + "	<TD>" + fLib.snowSelect("local_snow", local_snow) + "</TD>\n"
        + "	</TR>\n"

        + " <TR><TH>Floater:</TH>\n" +
        ("	<TD><input type='text' size='4' MAXLENGTH='3' "
         + "name='float_high' value='") +
        float_high + "'></TD>\n" +
        ("	<TD><input type='text' size='4' MAXLENGTH='3' "
         + "name='float_low' value='") +
        float_low + "'></TD>\n"
        + "	<TD>" + fLib.rainSelect("float_prec", float_prec) + "</TD>\n"
        + "	<TD>" + fLib.snowSelect("float_snow", float_snow) + "</TD>\n"
        + "	</TR></TABLE>\n");

    sbuf.append("<P><input type='submit'>\n"
                + "<input type='reset'>\n"
                + "</form>\n");

    return sbuf.toString();
  } // End of makeForecast()

  public String enterForecast(portfolioUser thisUser, String selectedUserID,
                              HttpServletRequest req) throws myException {
    StringBuffer sbuf = new StringBuffer();

    /** I had better figure out something better than this **/
    String sqlDate = req.getParameter("sqlDate");

    String local_high = req.getParameter("local_high");
    String local_low = req.getParameter("local_low");
    String local_prec = req.getParameter("local_prec");
    String local_snow = req.getParameter("local_snow");

    String float_high = req.getParameter("float_high");
    String float_low = req.getParameter("float_low");
    String float_prec = req.getParameter("float_prec");
    String float_snow = req.getParameter("float_snow");

    String colNames = "(userID, portfolio, day, local_high, local_low, "
                      + "local_prec, local_snow, "
                      + " float_high, float_low, float_prec, float_snow )";
    String colVals = "('" + selectedUserID + "', '" + thisUser.getPortfolio() +
                     "', '" + sqlDate + "', " + local_high + ", " +
                     local_low + ", " + local_prec + ", " + local_snow + ", " +
                     float_high + ", " + float_low + ", " + float_prec + ", " +
                     float_snow + " )";

    try {
      jlib.updateDB("DELETE from forecasts WHERE userID = '" + selectedUserID +
                    "' "
                    + " and portfolio = '" + thisUser.getPortfolio() +
                    "' and day = '" + sqlDate + "' ");
      jlib.updateDB("INSERT into forecasts " + colNames + " VALUES " + colVals +
                    " ");
    } catch (Exception ex) {
      plogger.report("Problem Entering Forecast " + ex);
      throw new myException("Could Not Enter Forecast");
    }

    sbuf.append("<P>Forecast Entered Successfully!\n");
    sbuf.append("<P>Modified Forecast is:\n"
                + " <BR>Local Site High: " + local_high + "\n"
                + " <BR>Local Site Low: " + local_low + "\n"
                + " <BR>Local Precip Cat: " + local_prec + "\n"
                + " <BR>Local Snow Cat: " + local_snow + "\n"

                + "<P>Floater Site High: " + float_high + "\n"
                + "<BR>Floater Site Low: " + float_low + "\n"
                + "<BR>Floater Precip Cat: " + float_prec + "\n"
                + "<BR>Floater Snow Cat: " + float_snow + "\n");

    return sbuf.toString();
  } // End of enterForecast()

  /**
   * Method to Modify a forecast for a certain student
   */
  public String alterStudent(portfolioUser thisUser, String thisPageURL) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Alter User's Forecast:"));

    sbuf.append(
        "<P>Please select which user's forecast you want to meddle with.\n");

    sbuf.append("<FORM METHOD=\"GET\" ACTION=\"" + thisPageURL + "\">\n"
                + "<input type=\"hidden\" name=\"mode\" value=\"n\">\n");

    sbuf.append("<SELECT name=\"selectedUserID\" size=\"5\">\n");
    try {
      ResultSet rs = dbInterface.callDB(
          "SELECT getUserName(s.username) as name, s.username "
          + " from students s, users u"
          + " WHERE s.portfolio = '" + thisUser.getPortfolio() +
          "' and s.username = u.username "
          + " and s.nofx = 'n' ");
      while (rs.next()) {
        sbuf.append("<OPTION VALUE=\"" + rs.getString("username") + "\">" +
                    rs.getString("name") + " \n");
      }
    } catch (Exception ex) {
      plogger.report("Err, bad results when quering of students.");
    }
    sbuf.append("</SELECT>\n");

    sbuf.append("<P>Select Forecast Date:\n");

    sbuf.append("<SELECT NAME=\"sqlDate\">\n");
    try {
      ResultSet forecastDays = dbInterface.callDB(
          "SELECT * from forecast_days WHERE "
          + " portfolio = '" + thisUser.getPortfolio() + "' ORDER by day ASC ");
      while (forecastDays.next()) {
        String thisDate = forecastDays.getString("day");
        sbuf.append("<OPTION VALUE=\"" + thisDate + "\">" + thisDate + "\n");
      }
    } catch (Exception ex) {
      plogger.report("Err, bad results when looking for available dates.");
    }
    sbuf.append("</SELECT>\n");

    sbuf.append("<BR><input type=\"SUBMIT\" value=\"Alter Forecast\">\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of alterStudent

  /**
   * Method to switch if an admin can or cant forecast
   */
  public String whoForecasts(portfolioUser thisUser, String thisPageURL) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Who Forecasts?"));

    sbuf.append(
        "<blockquote><font class=\"instructions\">Listed below are \n"
        + " instructors for this portfolio and their status in the forecast \n"
        +
        " activity.  To change their status, simply click on their username. \n"
        + " </blockquote></font>\n");

    try {
      ResultSet rs = dbInterface.callDB(
          "SELECT getUsername(username) as uname, * from "
          + " students WHERE portfolio = '" + thisUser.getPortfolio() + "' ");
      while (rs.next()) {
        String thisUserName = rs.getString("uname");
        String thisUserID = rs.getString("username");
        String nofx = rs.getString("nofx");
        if (nofx != null && nofx.equalsIgnoreCase("t"))
          sbuf.append("<P><a href=\"" + thisPageURL + "?mode=b"
                      + "&changeuser=" + thisUserID + "&changeto=n\">" +
                      thisUserName + "</a> "
                      + " does not forecast.\n");
        else
          sbuf.append("<P><a href=\"" + thisPageURL + "?mode=b"
                      + "&changeuser=" + thisUserID + "&changeto=y\">" +
                      thisUserName + "</a> "
                      + " does forecast.\n");
      }
    } catch (Exception ex) {
      plogger.report("Problem querying.");
      ex.printStackTrace();
    }

    sbuf.append(jlib.botBox());
    return sbuf.toString();
  } // End of whoForecasts

  /**
   * Method to make a dialog to create a new forecasting Day
   */
  public String createForecastDay(String thisPageURL) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Create Forecast Day:"));

    sbuf.append("<form method='POST' action='" + thisPageURL + "'>");
    sbuf.append("<input type='hidden' name='mode' value='y'>");

    sbuf.append(
        "Select the day that the forecast will be made for, <B>NOT</B> the \n"
        + " day on which the forecast is made. \n");

    sbuf.append("<BR><TABLE>\n"
                + "<TR>\n<TH>Select Year:</TH>\n<TH>Select "
                + "Month:</TH>\n<TH>Select Day:</TH>\n</TR>\n"
                + "<TR>\n"
                + "<TD>\n" + fLib.yearSelect() + "</TD>\n"

                + "<TD>\n" + fLib.monthSelect() + "</TD>\n"

                + "<TD>\n" + fLib.daySelect() + "</TD>\n"
                + "</TR></TABLE>");

    sbuf.append("<P>Select Case Group:\n"
                + "<SELECT name=\"case_group\">\n"
                + "	<option value='0'>Section/Period 1\n"
                + "	<option value='1'>Section/Period 2\n"
                + "	<option value='2'>Section/Period 3\n"
                + "	<option value='3'>Section/Period 4\n"
                + "</SELECT>\n");

    sbuf.append(
        "<H3>Input the station code:</H3>\n"
        + "<input type=\"text\" size=\"20\" MAXLENGTH=\"50\" name=\"code\">\n"

        + "<H3>Input the station name:</H3>\n"
        + "<input type=\"text\" MAXLENGTH=\"50\" name=\"station\">\n"

        +
        "<BR><input type=\"submit\" value=\"Add this floater city\"></form>\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  }

  /**
   * Method to create validation for a forecast date
   * @param portfolio which is the String value of the current portfolio
   * @param sqlDate which is the date we are entering validation for
   * @return HTML formated String for the Validation
   */
  public String createValidation(portfolioUser thisUser, String sqlDate) {
    StringBuffer sbuf = new StringBuffer();

    forecastDay thisDay = new forecastDay(thisUser, sqlDate);

    if (thisDay.getValidation())
      sbuf.append("<P>Retrieving Previous Validation.\n");
    else
      sbuf.append("<P>Setting Validation to default values.\n");
    if (thisDay.getClimo())
      sbuf.append("<P>Retrieving Climo Information.\n");
    else
      sbuf.append("<P>Setting Climo to default values.\n");

    sbuf.append(
        "<form method='POST' action='" + thisPageURL + "'>\n"
        + " <input type='hidden' name='mode' value='v'>\n"
        + " <input type='hidden' name='sqlDate' value=\"" + sqlDate + "\">\n"
        + " <P><TABLE>\n"
        + " <TR><TD></TD>\n"
        + "	<TD>High T</TD><TD>Low T</TD>\n"
        + "	<TD>Prec Cat</TD><TD>Prec Text</TD>\n"
        + "	<TD>Snow Cat</TD><TD>Snow Text</TD></TR>\n"

        + " <TR><TH>Local:</TH>\n" +
        ("	<TD><input type='text' size='4' MAXLENGTH='3' "
         + "name='local_high' value='") +
        thisDay.getVLocalHighTemp() + "'></TD>\n" +
        ("	<TD><input type='text' size='4' MAXLENGTH='3' "
         + "name='local_low' value='") +
        thisDay.getVLocalLowTemp() + "'></TD>\n"
        + "	<TD>" +
        fLib.adminRainSelect("local_prec", thisDay.getVLocalPrecCat()) +
        "</TD>\n" +
        ("	<TD><input type=\"text\" size=\"10\" name=\"local_prec_txt\" "
         + "value=\"") +
        thisDay.getVLocalPrecNum() + "\"></TD>\n"
        + "	<TD>" +
        fLib.adminSnowSelect("local_snow", thisDay.getVLocalSnowCat()) +
        "</TD>\n" +
        ("	<TD><input type=\"text\" size=\"10\" name=\"local_snow_txt\" "
         + "value=\"") +
        thisDay.getVLocalSnowNum() + "\"></TD>\n"
        + "	</TR>\n"

        + " <TR><TH>" + thisDay.getFloaterSiteID() + ":</TH>\n" +
        ("	<TD><input type='text' size='4' MAXLENGTH='3' "
         + "name='float_high' value='") +
        thisDay.getVFloaterHighTemp() + "'></TD>\n" +
        ("	<TD><input type='text' size='4' MAXLENGTH='3' "
         + "name='float_low' value='") +
        thisDay.getVFloaterLowTemp() + "'></TD>\n"
        + "	<TD>" +
        fLib.adminRainSelect("float_prec", thisDay.getVFloaterPrecCat()) +
        "</TD>\n" +
        ("	<TD><input type=\"text\" size=\"10\" name=\"float_prec_txt\" "
         + "value=\"") +
        thisDay.getVFloaterPrecNum() + "\"></TD>\n"
        + "	<TD>" +
        fLib.adminSnowSelect("float_snow", thisDay.getVFloaterSnowCat()) +
        "</TD>\n" +
        ("	<TD><input type=\"text\" size=\"10\" name=\"float_snow_txt\" "
         + "value=\"") +
        thisDay.getVFloaterSnowNum() + "\"></TD>\n"
        + "	</TR>\n"

        + " <TR><TH>Climo Local:</TH>\n" +
        ("	<TD><input type='text' size='4' MAXLENGTH='3' "
         + "name='cl_local_high' value='") +
        thisDay.getCLocalHighTemp() + "'></TD>\n" +
        ("	<TD><input type='text' size='4' MAXLENGTH='3' "
         + "name='cl_local_low' value='") +
        thisDay.getCLocalLowTemp() + "'></TD>\n"
        + "	<TD>" +
        fLib.rainSelect("cl_local_prec", thisDay.getCLocalPrecCat()) +
        "</TD>\n" +
        ("	<TD><input type=\"text\" size=\"10\" "
         + "name=\"cl_local_prec_txt\" value=\"") +
        thisDay.getCLocalPrecNum() + "\"></TD>\n"
        + "	<TD>" +
        fLib.snowSelect("cl_local_snow", thisDay.getCLocalSnowCat()) +
        "</TD>\n" +
        ("	<TD><input type=\"text\" size=\"10\" "
         + "name=\"cl_local_snow_txt\" value=\"") +
        thisDay.getCLocalSnowNum() + "\"></TD>\n"
        + "	</TR>\n"

        + " <TR><TH>Climo " + thisDay.getFloaterSiteID() + ":</TH>\n" +
        ("	<TD><input type='text' size='4' MAXLENGTH='3' "
         + "name='cl_float_high' value='") +
        thisDay.getCFloaterHighTemp() + "'></TD>\n" +
        ("	<TD><input type='text' size='4' MAXLENGTH='3' "
         + "name='cl_float_low' value='") +
        thisDay.getCFloaterLowTemp() + "'></TD>\n"
        + "	<TD>" +
        fLib.rainSelect("cl_float_prec", thisDay.getCFloaterPrecCat()) +
        "</TD>\n" +
        ("	<TD><input type=\"text\" size=\"10\" "
         + "name=\"cl_float_prec_txt\" value=\"") +
        thisDay.getCFloaterPrecNum() + "\"></TD>\n"
        + "	<TD>" +
        fLib.snowSelect("cl_float_snow", thisDay.getCFloaterSnowCat()) +
        "</TD>\n" +
        ("	<TD><input type=\"text\" size=\"10\" "
         + "name=\"cl_float_snow_txt\" value=\"") +
        thisDay.getCFloaterSnowNum() + "\"></TD>\n"
        + "	</TR>\n"

        + "</TABLE>\n");

    sbuf.append("<P><input type='submit'></form>\n");

    return sbuf.toString();
  } // End of createValidation()

  public String enterForecastValidation(portfolioUser thisUser,
                                        HttpServletRequest req) {
    StringBuffer sbuf = new StringBuffer();

    /** I had better figure out something better than this **/
    String sqlDate = req.getParameter("sqlDate");

    String local_high = req.getParameter("local_high");
    String local_low = req.getParameter("local_low");
    String local_prec = req.getParameter("local_prec");
    String local_prec_txt = req.getParameter("local_prec_txt");
    String local_snow = req.getParameter("local_snow");
    String local_snow_txt = req.getParameter("local_snow_txt");

    String float_high = req.getParameter("float_high");
    String float_low = req.getParameter("float_low");
    String float_prec = req.getParameter("float_prec");
    String float_prec_txt = req.getParameter("float_prec_txt");
    String float_snow = req.getParameter("float_snow");
    String float_snow_txt = req.getParameter("float_snow_txt");

    String cl_local_high = req.getParameter("cl_local_high");
    String cl_local_low = req.getParameter("cl_local_low");
    String cl_local_prec = req.getParameter("cl_local_prec");
    String cl_local_prec_txt = req.getParameter("cl_local_prec_txt");
    String cl_local_snow = req.getParameter("cl_local_snow");
    String cl_local_snow_txt = req.getParameter("cl_local_snow_txt");

    String cl_float_high = req.getParameter("cl_float_high");
    String cl_float_low = req.getParameter("cl_float_low");
    String cl_float_prec = req.getParameter("cl_float_prec");
    String cl_float_prec_txt = req.getParameter("cl_float_prec_txt");
    String cl_float_snow = req.getParameter("cl_float_snow");
    String cl_float_snow_txt = req.getParameter("cl_float_snow_txt");

    String colNames =
        "(portfolio, local_high, local_low, local_prec, "
        + " local_snow, float_high, float_low, float_prec, float_snow, day, "
        + " local_prec_txt, local_snow_txt, float_prec_txt, float_snow_txt)";
    String colVals = "('" + thisUser.getPortfolio() + "' "
                     + ", " + local_high + ", " + local_low + ", " +
                     local_prec + ", " + local_snow + ", " + float_high + ", "
                     + " " + float_low + ", " + float_prec + ", " +
                     float_snow + ", '" + sqlDate + "', "
                     + " '" + local_prec_txt + "', '" + local_snow_txt +
                     "', '" + float_prec_txt + "', '" + float_snow_txt + "')";

    String colNames2 =
        "(portfolio, local_high, local_low, local_prec, "
        + " local_snow, float_high, float_low, float_prec, float_snow, day, "
        + " local_prec_txt, local_snow_txt, float_prec_txt, float_snow_txt)";
    String colVals2 =
        "('" + thisUser.getPortfolio() + "' "
        + ", " + cl_local_high + ", " + cl_local_low + ", " + cl_local_prec +
        ", " + cl_local_snow + ", " + cl_float_high + ", "
        + " " + cl_float_low + ", " + cl_float_prec + ", " + cl_float_snow +
        ", '" + sqlDate + "', "
        + " '" + cl_local_prec_txt + "', '" + cl_local_snow_txt + "', '" +
        cl_float_prec_txt + "', '" + cl_float_snow_txt + "')";

    try {
      dbInterface.updateDB("DELETE from forecast_answers WHERE "
                           + " portfolio = '" + thisUser.getPortfolio() +
                           "' and day = '" + sqlDate + "' ");
      dbInterface.updateDB("INSERT into forecast_answers " + colNames +
                           " VALUES " + colVals + " ");

      dbInterface.updateDB("DELETE from forecast_climo WHERE  "
                           + " portfolio = '" + thisUser.getPortfolio() +
                           "' and day = '" + sqlDate + "' ");
      dbInterface.updateDB("INSERT into forecast_climo " + colNames2 +
                           " VALUES " + colVals2 + " ");

    } catch (Exception ex) {
      plogger.report("PROBLEM in UPDATE DB");
      System.err.println(ex);
    }

    sbuf.append("DOne.");

    return sbuf.toString();
  } // End of enterForecast()

  /**
   * Method that creates a new forecasting day.
   *	Also puts a message in the calendar about the new forecast that is due
   * @param request which is the HttpServletRequest
   * @param portfolio which is the value of the portfolio
   */
  public String addForecastingDay(HttpServletRequest request,
                                  String portfolio) {
    StringBuffer sbuf = new StringBuffer();

    String year = (String)request.getParameter("year");
    String month = (String)request.getParameter("month");
    String day = (String)request.getParameter("day");
    String code = (String)request.getParameter("code");
    String station = (String)request.getParameter("station");
    String case_group = (String)request.getParameter("case_group");
    String dateStr = year + "-" + month + "-" + day;

    try {
      dbInterface.updateDB("DELETE from forecast_days WHERE day = '" + dateStr +
                           "' "
                           + " AND portfolio = '" + portfolio + "' ");
      dbInterface.updateDB("INSERT into forecast_days VALUES('" + portfolio +
                           "', "
                           + " '" + dateStr + "','" + station + "','" + code +
                           "', " + case_group + ") ");

      dbInterface.updateDB(
          "INSERT into calendar(portfolio, description, URL, valid) VALUES "
          + " ('" + portfolio + "', 'Forecast Due', "
          + " '" + jlib.servletHttpBase + "/forecast', "
          + " '" + dateStr + "' ) ");

    } catch (Exception ex) {
      plogger.report("Problem Creating Forecast Day!");
    }

    return sbuf.toString();
  }

  public String adminOptions(portfolioUser thisUser, String thisPageURL) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Fx Admin Options"));
    sbuf.append("<ul><li><a class=\"commands\" href='" + thisPageURL +
                "?mode=y'>Create a Forecast Day</a></li>\n");
    sbuf.append("<li><a class=\"commands\" href='" + thisPageURL +
                "?mode=u'>Enter Validation</a></li>\n");
    sbuf.append("<li><a class=\"commands\" href='" + thisPageURL +
                "?mode=a'>Admin Forecast Options</a></li>\n");
    sbuf.append("<li><a class=\"commands\" href='" + thisPageURL +
                "?mode=m'>Alter User's Forecast</a></li>\n");
    sbuf.append("<li><a class=\"commands\" href='" + thisPageURL +
                "?mode=w'>View All Forecasts</a></li></ul>\n");
    sbuf.append(jlib.botBox());

    sbuf.append("<P>\n");
    sbuf.append(jlib.currentUsers(thisUser));

    sbuf.append("<P>\n");
    sbuf.append(jlib.adminCommands());

    return sbuf.toString();
  } // End of classOptions

} // End of forecastAdmin
