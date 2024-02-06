package edu.iastate.agron.mesonet;
/**
 * Mesonet Trouble Ticket container
 * I love java :)
 * Daryl Herzmann 18 May 2002
 */

import java.sql.*;
import java.text.*;
import org.collaborium.portfolio.*;
import org.collaborium.util.*;

public class mTT {
  private String id = null;
  private String portfolio = null;
  private String s_mid = null;
  private String s_name = null;
  private Timestamp entered = null;
  private Timestamp last = null;
  private Timestamp closed = null;
  private String subject = null;
  private String status = null;
  private String author = null;
  private String authorName = null;
  private String sensor = null;

  /** Blank Constructor */
  public mTT() {}

  public mTT(String portfolio, String tt_id) throws SQLException {
    ResultSet rs = dbInterface.callDB(
        "SELECT *, getUsername(author) as rname "
        + " ,getSiteName(s_mid) as s_name from tt_base WHERE "
        + " portfolio = '" + portfolio + "' and id = " + tt_id);
    rs.next();
    this.doSQL(rs);
  }

  public mTT(ResultSet rs) { this.doSQL(rs); } // End of mTT constructor

  public void doSQL(ResultSet rs) {
    try {
      this.id = rs.getString("id");
      this.portfolio = rs.getString("portfolio");
      this.s_mid = rs.getString("s_mid");
      this.s_name = rs.getString("s_name");
      this.sensor = rs.getString("sensor");
      try {
        this.entered = rs.getTimestamp("entered");
        this.last = rs.getTimestamp("last");
        this.closed = rs.getTimestamp("closed");
      } catch (Exception ex) {
        plogger.report("Error loading TT timestamps");
        this.entered = new Timestamp(1000000000);
        this.last = new Timestamp(1000000000);
        this.closed = new Timestamp(1000000000);
      }
      this.subject = rs.getString("subject");
      this.status = rs.getString("status");
      this.author = rs.getString("author");
      this.authorName = rs.getString("rname");
    } catch (Exception ex) {
      plogger.report("Error Building mTT object");
      ex.printStackTrace();
    }
  } // End of doSQL
  /**
   *  printTR()
   *   print table row for this TT.
   *  @return HTML formated string
   */
  public String printTR() {
    StringBuffer sbuf = new StringBuffer();
    SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy");
    String postDate = sdf.format(this.entered);

    sbuf.append("<tr id=\"" + this.status + "\">\n"
                + " <td><a href=\"details.jsp?tt_id=" + this.id + "\">" +
                this.id + "</a></td>\n"
                + " <td>" + postDate + "</td>\n"
                + " <td>" + this.author + "</td>\n");
    // sbuf.append(" <td><font color=\"");
    // if (this.status.equalsIgnoreCase("OPEN") )
    //    sbuf.append("red");
    // else
    //    sbuf.append("green");
    sbuf.append("<td>" + this.status + "</font></td>\n");
    sbuf.append(" <td>" + this.s_name + "</td>\n"
                + " <td>" + this.subject + "</td>\n"
                + "</tr>\n");

    return sbuf.toString();
  }

  public String printLongView() {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append("<p><table width='100%' cellspacing='0' cellpadding='3'>\n"
                + " <tr BGCOLOR=\"#ababab\">\n"
                + "  <th align=\"left\">ID</th>\n"
                + "  <th align=\"left\">Status</th>\n"
                + "  <th align=\"left\">ID</th>\n"
                + "  <th align=\"left\">Name</th>\n"
                + "  <th align=\"left\">Owner</th>\n"
                + "  <th align=\"left\">Short Description</th>\n"
                + " </tr>\n"
                + " <tr bgcolor=\"#dedede\">\n"
                + "  <td align=\"left\">" + this.id + "</th>\n"
                + "  <td align=\"left\">" + this.status + "</th>\n"
                + "  <td align=\"left\">" + this.s_mid + "</th>\n"
                + "  <td align=\"left\">" + this.s_name + "</th>\n"
                + "  <td align=\"left\">" + this.authorName + "</th>\n"
                + "  <td align=\"left\">" + this.subject + "</th>\n"
                + " </tr></table>\n");

    return sbuf.toString();
  }

  /**
   * printHistory()
   *  - prints out the history of a station with all the comments
   * @return HTML formated string for the comments
   */
  public String printHistory() throws SQLException {
    StringBuffer sbuf = new StringBuffer();
    ResultSet rs = dbInterface.callDB(
        "SELECT *, getUserName(author) as rname"
        + " from tt_log WHERE tt_id = " + this.id + " ORDER by entered DESC");
    sbuf.append("<p><table width=\"100%\" cellspacing=0 "
                + " colspacing=0 cellpadding=2>\n");
    while (rs.next()) {
      sbuf.append(
          "<tr bgcolor=\"#ababab\">\n"
          + "  <th align=\"left\">By: " + rs.getString("rname") + "</th>\n"
          + "  <th align=\"right\"> " + rs.getString("entered") + "</th>\n"
          + "</tr>\n"
          + "<tr>\n"
          + "  <td colspan=2>\n" + stringUtils.toBR(rs.getString("comments")) +
          "\n");
      if (rs.getString("status_c") == null ||
          !rs.getString("status_c").equalsIgnoreCase("OKAY")) {
        sbuf.append("<br><div align=\"right\"><b>Status changed to:</b> "
                    + " " + rs.getString("status_c") + "</div>\n");
      }
      sbuf.append("</td></tr>\n");
    } // There should always be 1 entry in tt_log

    sbuf.append("</table>\n");
    return sbuf.toString();
  }

  public String getS_mid() { return this.s_mid; }
  public String getSubject() { return this.subject; }
  public String getSensor() { return this.sensor; }
  public void setStatus(String newStatus) { this.status = newStatus; }
} // End of class definition
