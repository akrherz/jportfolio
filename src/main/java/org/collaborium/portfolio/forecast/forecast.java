/**
 * Copyright 2001-2008 Iowa State University
 * akrherz@iastate.edu
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
 * I am back and will incorperate the forecast activity into
 * the portfolio system.  The transition should be excellent
 *
 * @author Daryl Herzmann 4 July 2001
 */
package org.collaborium.portfolio.forecast;

import java.io.*;
import java.lang.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.collaborium.portfolio.*;
import org.collaborium.portfolio.forecast.*;
import org.collaborium.util.*;

public class forecast extends HttpServlet {

  public void init(ServletConfig config) throws ServletException {
    super.init(config);

  } // End of init()

  public void destroy() {} // End of destroy()

  /**
   * Method to handle servlet POST actions
   *
   * @param HttpServletRequest
   * @param HttpServletResponse
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    plogger.report("---- Forecast\n");

    /** Set up containers to hold HTML, before sending it out. **/
    StringBuffer sideContent = new StringBuffer();
    StringBuffer pageContent = new StringBuffer();

    PrintWriter out = response.getWriter();
    response.setContentType("text/html");

    HttpSession session = request.getSession(true);
    portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
    String callMethod = (String)request.getParameter("mode");
    String thisPageURL = "/jportfolio/servlet/forecast";

    if (thisUser == null) {
      response.setHeader("Refresh", "0; URL=./jportfolio");
      callMethod = "z"; // Abort

    } else {
      jlib.addUser(thisUser.getUserID(), "forecast");
    }

    out.println(jlib.header(thisUser, "Forecasting Activity", "forecast"));

    switch (callMethod.charAt(0))
    // Current calling methods
    // f == Enter Forecast
    {
    case 'f':
      pageContent.append("Entering Forecast values...<BR>");
      try {
        pageContent.append(enterForecast(thisUser, request));
      } catch (Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String stackTrace = sw.toString();
        plogger.mail(stackTrace);
        plogger.report("Problem Entering Forecast.\n" + ex);
        pageContent.append("<P>An error occured processing your forecast"
                           + "<BR> " + ex +
                           ". <BR>Please try to forecast Again!");
      }
      break;
    default:
      pageContent.append(mkIntro());
      break;
    }

    sideContent.append(classOptions(thisUser, thisPageURL));

    out.println(jlib.makePage(sideContent.toString(), pageContent.toString()));

    // Finish the page and close output
    out.write(jlib.footer());
    out.close();
    plogger.report("--- End of forecast");

  } // End of doPost()

  /**
   * Method to handle method GET to the server
   *
   * @param HttpServletRequest  request
   * @param HttpServletResponse response
   *
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    plogger.report("---- Forecast");

    PrintWriter out = response.getWriter();
    response.setContentType("text/html");
    StringBuffer sideContent = new StringBuffer();
    StringBuffer pageContent = new StringBuffer();

    HttpSession session = request.getSession(true);
    String callMethod = (String)request.getParameter("mode");
    portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
    String thisPageURL = "/jportfolio/servlet/forecast";
    String sort = (String)request.getParameter("sort");
    String sqlDate = (String)request.getParameter("sqlDate");

    if (thisUser == null || thisUser.getPortfolio() == null) {
      response.setHeader("Refresh", "0; URL=./jportfolio");
      callMethod = "z"; // Abort

    } else {
      jlib.addUser(thisUser.getUserID(), "forecast");
    }

    // Setup the Page
    out.println(jlib.header(thisUser, "Forecasting Activity", "forecast"));

    if (callMethod == null)
      callMethod = "d";

    switch (callMethod.charAt(0))
    // Current calling methods
    // d == Log out
    // y == Admin create Forecast day
    // l == Last Forecast Results
    // c == cumulative results
    // m == select a certain forecast to view results
    // n == select a certain day to view a users forecast for
    {
    case 'c':
      pageContent.append("<P align=\"right\">"
                         + "<a "
                         + "href=\"/jportfolio/jsp/user/forecast/"
                         + "cumStandings.jsp\">Printable Version</a>\n");
      try {
        pageContent.append(
            fLib.cumulativeResults(thisUser.getPortfolio(), sort, thisPageURL));
      } catch (Exception ex) {
        plogger.report("Problem in cumulativeResults");
        pageContent.append(ex);
      }
      break;
    case 'l':
      if (sqlDate != null)
        pageContent.append(
            "<P align=\"right\">"
            + "<a "
            + "href=\"/jportfolio/jsp/user/forecast/prevForecast.jsp?sqlDate=" +
            sqlDate + "\">Printable Version</a>\n");
      else
        pageContent.append("<P align=\"right\">"
                           + "<a "
                           + "href=\"/jportfolio/jsp/user/forecast/"
                           + "prevForecast.jsp\">Printable Version</a>\n");

      try {
        pageContent.append(fLib.forecastResults(thisUser.getPortfolio(),
                                                sqlDate, sort, thisPageURL));
      } catch (Exception ex) {
        plogger.report("Problem in forecastResults");
        pageContent.append(ex);
      }
      break;
    case 'm':
      try {
        pageContent.append(
            fLib.whichDay(thisUser.getPortfolio(), "l", thisPageURL));
      } catch (Exception ex) {
        plogger.report("Problem in whichDay!");
        ex.printStackTrace();
      }
      break;
    case 'n':
      try {
        pageContent.append(
            fLib.whichDay(thisUser.getPortfolio(), "o", thisPageURL));
      } catch (Exception ex) {
        plogger.report("Problem with 'n' in doGet");
        ex.printStackTrace();
      }
      break;
    case 'o':
      try {
        pageContent.append(userForecastResults(thisUser, sqlDate));
      } catch (Exception ex) {
        plogger.report("Problem with 'o' in doGet");
        ex.printStackTrace();
      }
      break;
    case 'z':
      pageContent.append("nothing");
      break;
    case 'f':
      pageContent.append("Make your Forecast!");
      pageContent.append(makeForecast(thisUser, null, thisPageURL));
      break;
    default:
      pageContent.append(mkIntro());
      break;
    }

    if (thisUser != null) {
      sideContent.append(classOptions(thisUser, thisPageURL));
    }

    out.println(jlib.makePage(sideContent.toString(), pageContent.toString()));
    // Finish the page and close output
    out.println(jlib.footer());
    out.close();

    plogger.report("--- End of forecast");
  }

  /**
   * Method to print out what a user forecasted
   *
   */
  public String userForecastResults(portfolioUser thisUser, String sqlDate) {
    StringBuffer sbuf = new StringBuffer();

    forecastDay thisDay = new forecastDay(thisUser, sqlDate);
    thisDay.getForecast();
    thisDay.getValidation();
    thisDay.getClimo();

    if (thisDay == null)
      return "Unable to retrieve information for this day!";

    sbuf.append("<P>Forecast Numbers for " + sqlDate + "\n"

                + "  <P><TABLE border=1>\n"
                + "  <caption><B>You forecasted:</B></caption>\n"
                + "  <TR><TH>Forecast Site:</TH><TH>High:</TH>\n"
                + "	<TH>Low:</TH><TH>Prec Cat:</TH>\n"
                + "	<TH>Snow Cat:</TH></TR>\n"

                + "  <TR><TD>" + thisDay.getLocalSite() + "</TD>\n"
                + "	<TD>" + thisDay.getLocalHighTemp() + "</TD>\n"
                + "	<TD>" + thisDay.getLocalLowTemp() + "</TD>\n"
                + "	<TD>CAT " + thisDay.getLocalPrecCat() + "</TD>\n"
                + "	<TD>CAT " + thisDay.getLocalSnowCat() + "</TD></TR>\n"

                + "  <TR><TD>" + thisDay.getFloaterSite() + "</TD>\n"
                + "	<TD>" + thisDay.getFloaterHighTemp() + "</TD>\n"
                + "	<TD>" + thisDay.getFloaterLowTemp() + "</TD>\n"
                + "	<TD>CAT " + thisDay.getFloaterPrecCat() + "</TD>\n"
                + "	<TD>CAT " + thisDay.getFloaterSnowCat() +
                "</TD></TR>\n"

                + "</TABLE>\n");
    sbuf.append("<br><b>Confidence:</b> " + thisDay.getConfidence() +
                "<br><b>Discussion:</b> " +
                stringUtils.toBR(thisDay.getDiscussion()));

    sbuf.append("<P><TABLE>\n"
                + "  <caption><B>Validation:</B></caption>\n"
                + "  <TR><TH>Forecast Site:</TH><TH>High:</TH>\n"
                + "	<TH>Low:</TH><TH>Prec Cat:</TH>\n"
                + "	<TH>Snow Cat:</TH></TR>\n"

                + "  <TR><TD>" + thisDay.getLocalSite() + "</TD>\n"
                + "	<TD>" + thisDay.getVLocalHighTemp() + "</TD>\n"
                + "	<TD>" + thisDay.getVLocalLowTemp() + "</TD>\n"
                + "	<TD>CAT " + thisDay.getVLocalPrecCat() + " "
                + "	( " + thisDay.getVLocalPrecNum() + " ) </TD>\n"
                + "	<TD>CAT " + thisDay.getVLocalSnowCat() + " "
                + "	( " + thisDay.getVLocalSnowNum() + " ) </TD></TR>\n"

                + "  <TR><TD>" + thisDay.getFloaterSite() + "</TD>\n"
                + "	<TD>" + thisDay.getVFloaterHighTemp() + "</TD>\n"
                + "	<TD>" + thisDay.getVFloaterLowTemp() + "</TD>\n"
                + "	<TD>CAT " + thisDay.getVFloaterPrecCat() + " "
                + "	( " + thisDay.getVFloaterPrecNum() + " ) </TD>\n"
                + "	<TD>CAT " + thisDay.getVFloaterSnowCat() + " "
                + "	( " + thisDay.getVFloaterSnowNum() + " )</TD></TR>\n"

                + "</TABLE>\n");

    sbuf.append(
        "<P><TABLE>\n"
        + " <caption><B>Your Classmates Forecasted:</B></caption>\n"
        + "<TR><TH rowspan=2>Forecaster</TH><TD colspan=4>" +
        thisDay.getLocalSite() + "</TD>\n"
        + "  <TD colspan=4>" + thisDay.getFloaterSite() + "</TD></TR>\n"
        + "<TR><TH>High:</TH><TH>Low:</TH><TH>Prec:</TH><TH>Snow:</TH>\n"
        + "<TH>High:</TH><TH>Low:</TH><TH>Prec:</TH><TH>Snow:</TH></TR>\n");
    try {
      ResultSet rs = dbInterface.callDB(
          "SELECT getUserName(userid) as rname, "
          + " * from forecasts WHERE portfolio = '" + thisUser.getPortfolio() +
          "' "
          + " and day = '" + sqlDate + "' and "
          + " CURRENT_TIMESTAMP::date >= '" + sqlDate + "' ");
      while (rs.next()) {
        sbuf.append("<TR>\n"
                    + "   <TD>" + rs.getString("rname") + "</TD>\n"
                    + "   <TD>" + rs.getString("local_high") + "</TD>\n"
                    + "   <TD>" + rs.getString("local_low") + "</TD>\n"
                    + "   <TD>" + rs.getString("local_prec") + "</TD>\n"
                    + "   <TD>" + rs.getString("local_snow") + "</TD>\n"
                    + "   <TD>" + rs.getString("float_high") + "</TD>\n"
                    + "   <TD>" + rs.getString("float_low") + "</TD>\n"
                    + "   <TD>" + rs.getString("float_prec") + "</TD>\n"
                    + "   <TD>" + rs.getString("float_snow") +
                    "</TD></TR>\n");
        if (thisUser.isAdmin()) {
          sbuf.append(
              "<tr><td></td><td><i>Coinfidence:</i>  " +
              rs.getString("confidence") + "</td><td colspan=7></td></tr>\n"
              + "<tr><td></td><td colspan=8>" +
              stringUtils.toBR(rs.getString("discussion")) + "</td></tr>\n");
        }
      }
    } catch (Exception ex) {
      plogger.report("Problem retrieving everybodies forecast");
      ex.printStackTrace();
    }

    sbuf.append("</TABLE>\n");

    return sbuf.toString();
  } // End of userForecastResults

  /**
   * Method to print out a simple welcome to the forecasting exercise
   */
  public String mkIntro() {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append("<h3>Welcome!!</h3>\n");
    sbuf.append("<P>This is the Forecasting Activity Application inside of "
                + "Portfolio.  Your \n"
                + " portfolio may not be participating in the Activity, so "
                + "this page may be ignored. \n");

    sbuf.append("<P>In the column to the left, you will find options "
                + "available for this portfolio \n"
                + " and valid at this particular time.  The \"Make "
                + "Forecast\" link only works when a forecast \n"
                + " needs to be made.\n");

    return sbuf.toString();
  }

  public String classOptions(portfolioUser thisUser, String thisPageURL) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Forecast Options:"));
    sbuf.append("<ul>\n");
    if (isForecastDay(thisUser))
      sbuf.append("<LI><a href='" + thisPageURL +
                  "?mode=f'>Make Forecast</a></LI>\n");
    else
      sbuf.append("No Forecast for Today!");

    sbuf.append("<LI><a class=\"commands\" href='" + thisPageURL +
                "?mode=m'>Previous Forecast Results</a></li>\n");
    sbuf.append("<li><a class=\"commands\" href='" + thisPageURL +
                "?mode=l'>Last Forecast Results</a></li>\n");
    sbuf.append("<li><a class=\"commands\" href='" + thisPageURL +
                "?mode=c'>Cumulative Standings</a></li>\n");
    sbuf.append("<li><a class=\"commands\" href='" + thisPageURL +
                "?mode=n'>View Forecasts</a></li>\n");
    sbuf.append("</ul>\n");
    sbuf.append(jlib.botBox());

    sbuf.append("<P>\n");
    sbuf.append(jlib.currentUsers(thisUser));
    sbuf.append("<P>\n");
    if (thisUser.isAdmin())
      sbuf.append(jlib.adminCommands());

    return sbuf.toString();
  } // End of classOptions

  /**
   *
   *
   */
  public String makeForecast(portfolioUser thisUser, String sqlDate,
                             String thisPageURL) {
    StringBuffer sbuf = new StringBuffer();

    if (sqlDate == null)
      sqlDate = "tomorrow";

    forecastDay thisDay = new forecastDay(thisUser, sqlDate);
    if (thisDay.getForecast())
      sbuf.append("<P>It appears that you are ammending your forecast.\n");
    else
      sbuf.append(
          "<P>This is your first attempt at this forecast. Good luck.\n");

    sbuf.append("<P>The floater site is " + thisDay.getFloaterSite() + " "
                + " ( " + thisDay.getFloaterSiteID() + " ) \n");

    sbuf.append(
        "<form method='POST' action='" + thisPageURL + "'>\n"
        + " <input type='hidden' name='mode' value='f'>\n"
        + " <P><TABLE>\n" +
        ("<tr><td colspan=5 bgcolor=\"#0000f0\"><font "
         + "color=\"#f0f0f0\"><b>Enter your numeric "
         + "forecast:</b></font></td></tr>\n") +
        (" <TR><TD></TD><TD>High Temp:</TD><TD>Low Temp:</TD><TD>Precip "
         + "Cat:</TD><TD>Snow Cat:</TD></TR>\n") +
        " <TR><TH>Local:</TH>\n" +
        (" <TD><input type='text' size='4' MAXLENGTH='3' name='local_high' "
         + "value='") +
        thisDay.getLocalHighTemp() + "'></TD>\n" +
        (" <TD><input type='text' size='4' MAXLENGTH='3' name='local_low' "
         + "value='") +
        thisDay.getLocalLowTemp() + "'></TD>\n"
        + "	<TD>" +
        fLib.rainSelect("local_prec", thisDay.getLocalPrecCat()) + "</TD>\n"
        + "	<TD>" +
        fLib.snowSelect("local_snow", thisDay.getLocalSnowCat()) + "</TD>\n"
        + "	</TR>\n"

        + " <TR><TH>" + thisDay.getFloaterSite() + ":</TH>\n" +
        ("	<TD><input type='text' size='4' MAXLENGTH='3' "
         + "name='float_high' value='") +
        thisDay.getFloaterHighTemp() + "'></TD>\n" +
        ("	<TD><input type='text' size='4' MAXLENGTH='3' "
         + "name='float_low' value='") +
        thisDay.getFloaterLowTemp() + "'></TD>\n"
        + "	<TD>" +
        fLib.rainSelect("float_prec", thisDay.getFloaterPrecCat()) + "</TD>\n"
        + "	<TD>" +
        fLib.snowSelect("float_snow", thisDay.getFloaterSnowCat()) + "</TD>\n"
        + "	</TR>" +
        ("<tr><td colspan=5 bgcolor=\"#0000f0\"><font "
         + "color=\"#f0f0f0\"><b>Justification:</b> "
         + "(<i>Optional</i>)</font></td></tr>\n") +
        "<tr><td colspan=5><b>Rate your confidence in this forecast?</b>"
        + " <br><i>1 (not confident) - 10 (confident)</i>"
        + " <input type=\"text\" name=\"confidence\" value=\"" +
        thisDay.getConfidence() + "\" size=\"2\" maxlength=\"2\">"

        + "<p><b>Discuss your thoughts regarding this forecast.</b>"
        + "<br>You will <b>not</b> be graded for this discussion."
        + "<br><textarea name=\"discussion\" cols=50 rows=4>" +
        thisDay.getDiscussion() + "</textarea></td></tr>\n"
        + "</TABLE>\n");

    sbuf.append("<P><input type='submit' value='Make Forecast'>\n"
                + "<input type='reset'>\n"
                + "</form>\n");

    return sbuf.toString();
  } // End of makeForecast()

  /**
   * Method that returns if today is a forecast day or not.
   *
   * @param thisUser which is the user
   * @return boolean for it is is or not.
   */
  public boolean isForecastDay(portfolioUser thisUser) {
    if (thisUser == null)
      return false;

    if (!thisUser.doForecast())
      return false;

    try {
      ResultSet rs = dbInterface.callDB(
          "SELECT day from forecast_days "
          + " WHERE portfolio = '" + thisUser.getPortfolio() + "' "
          + " and day = 'TODAY'::date + '1 day'::interval ");
      if (rs.next()) {
        ResultSet rs2 = dbInterface.callDB(
            "SELECT * from portfolios "
            + " WHERE portfolio = '" + thisUser.getPortfolio() + "' and "
            +
            " fxtime > to_char(CURRENT_TIMESTAMP::timestamp, 'HH24')::int ");
        if (rs2.next())
          return true;
      }
    } catch (Exception ex) {
      plogger.report("Problem Getting Active Days " + ex);
      ex.printStackTrace();
    }

    return false;

  } // End of isForecastDay

  public String enterForecast(portfolioUser thisUser, HttpServletRequest req)
      throws myException {
    StringBuffer sbuf = new StringBuffer();

    /** I had better figure out something better than this **/
    // String sqlDate = req.getParameter("sqlDate");
    String sqlDate = "tomorrow";

    String local_high = req.getParameter("local_high");
    Integer Int_local_high = new Integer(local_high.trim());
    String local_low = req.getParameter("local_low");
    Integer Int_local_low = new Integer(local_low.trim());
    String local_prec = req.getParameter("local_prec");
    String local_snow = req.getParameter("local_snow");

    String float_high = req.getParameter("float_high");
    Integer Int_float_high = new Integer(float_high.trim());
    String float_low = req.getParameter("float_low");
    Integer Int_float_low = new Integer(float_low.trim());
    String float_prec = req.getParameter("float_prec");
    String float_snow = req.getParameter("float_snow");

    String confidence = (String)req.getParameter("confidence");
    String discussion = stringUtils.cleanString(req.getParameter("discussion"));
    if (confidence.equals("")) {
      confidence = "0";
    } else {
      try {
        Integer.parseInt(confidence);
      } catch (NumberFormatException e) {
        confidence = "0";
      }
    }

    if (!isForecastDay(thisUser)) {
      return "Forecast Period Expired.  Sorry!";
    }

    if (Int_local_high.intValue() < Int_local_low.intValue()) {
      local_high = Int_local_low.toString();
      local_low = Int_local_high.toString();
      sbuf.append("<BR>....Values for Local High & Low have been switched!");
    }

    if (Int_float_high.intValue() < Int_float_low.intValue()) {
      float_high = Int_float_low.toString();
      float_low = Int_float_high.toString();
      sbuf.append("<BR>....Values for Floater High & Low have been switched!");
    }

    String colNames = "(userID, portfolio, day, local_high, "
                      + " local_low, local_prec, local_snow, "
                      + " float_high, float_low, float_prec, "
                      + " float_snow, confidence, discussion )";
    String colVals = "('" + thisUser.getUserID() + "', "
                     + " '" + thisUser.getPortfolio() + "', '" + sqlDate +
                     "', " + local_high + ", " + local_low + ", " +
                     local_prec + ", " + local_snow + ", " + float_high + ", " +
                     float_low + ", " + float_prec + ", " + float_snow + " "
                     + ", '" + confidence + "'::float::int, '" + discussion +
                     "' )";

    try {
      jlib.updateDB("DELETE from forecasts WHERE userID = '" +
                    thisUser.getUserID() + "' "
                    + " and portfolio = '" + thisUser.getPortfolio() +
                    "' and day = '" + sqlDate + "' ");
      jlib.updateDB("INSERT into forecasts " + colNames + " VALUES " + colVals +
                    " ");
    } catch (Exception ex) {
      plogger.report("Problem Entering Forecast " + ex);
      throw new myException("Could Not Enter Forecast");
    }

    sbuf.append("<P>Forecast Entered Successfully!\n");
    sbuf.append("<P>You Forecasted:\n"
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

  public String selectForecastDays(portfolioUser thisUser, String selected) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append("<SELECT name='dayStr'>\n");

    ResultSet rs = null;
    String thisVal = null;
    try {
      rs = jlib.callDB("SELECT * from forecast_days WHERE "
                       + " portfolio = '" + thisUser.getPortfolio() + "' ");
      while (rs.next()) {
        thisVal = rs.getString("day");
        sbuf.append("<option value='" + thisVal + "' ");
        if (selected.equalsIgnoreCase(thisVal))
          sbuf.append("SELECTED");
        sbuf.append(">" + thisVal + "\n");
      }
    } catch (Exception ex) {
    }

    return sbuf.toString();
  } // End of selectForecastDays()

} // End of Forecast()
