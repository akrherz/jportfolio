/**
 * Copyright 2001-2005 Iowa State University jportfolio@collaborium.org
 *
 * <p>This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * <p>This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * <p>You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.collaborium.portfolio;

import java.sql.*;
import java.util.*;

public class portfolioPortfolio {

  private String name = "";
  private String id = null;
  private String dialogSecurity = "public";
  private String home = "/jportfolio/servlet/jportfolio";
  private String base = "/jportfolio/servlet/";
  private String pList = "('null')";
  private String groupp = null;
  private List<String> admins = new ArrayList<>();

  public boolean usesCalendar = false;
  public boolean usesDialog = false;
  public boolean usesForecast = false;
  public boolean usesQuiz = false;

  /** Generic container */
  public portfolioPortfolio() {}

  public portfolioPortfolio(String newID) {
    this.id = newID;
    this.name = "";
    this.home = "/jportfolio/servlet/jportfolio";
    this.base = "/jportfolio/servlet/";
    this.usesCalendar = false;
    this.usesDialog = false;
    this.usesForecast = false;
    this.usesQuiz = false;
    this.pList = "('" + newID + "')";
    this.admins = new ArrayList<>();

    try {
      ResultSet rs = dbInterface.callDB("SELECT * from appregistry "
                                        + " WHERE portfolio = '" + id + "'  ");
      if (rs.next()) {
        this.usesCalendar = rs.getBoolean("use_calendar");
        this.usesDialog = rs.getBoolean("use_dialog");
        this.usesForecast = rs.getBoolean("use_forecast");
        this.usesQuiz = rs.getBoolean("use_quiz");
      } else {
        this.usesCalendar = true;
        this.usesDialog = true;
        this.usesForecast = true;
        this.usesQuiz = true;
      }

      rs = dbInterface.callDB("SELECT porthome, name, groupp from portfolios "
                              + " WHERE portfolio = '" + id + "'  ");
      if (rs.next()) {
        this.home = (String)rs.getString("porthome");
        this.base = this.home;
        this.name = (String)rs.getString("name");
        if (rs.getString("groupp") != null) {
          this.groupp = (String)rs.getString("groupp");
          this.pList = "('" + newID + "','" + groupp + "')";
        } else
          this.groupp = "null";
      }

      rs = dbInterface.callDB("SELECT admin from admins WHERE "
                              + " portfolio = '" + id + "' ");
      while (rs.next()) {
        admins.add(rs.getString("admin"));
      }

    } catch (Exception ex) {
      plogger.report("Problem setting new Portfolio");
    }
  } // End of portfolioPortfolio

  public String getPList() { return this.pList; }

  public String getID() { return this.id; }

  public String getName() { return this.name; }

  public String getHome() { return this.home; }

  public String getBase() { return this.base; }

  public List<String> getAdmins() { return this.admins; }

  /**
   * Return the value of the dialog Security
   *
   * @return String value of dialogSecurity
   */
  public String getDialogSecurity() { return this.dialogSecurity; }

  /**
   * Method to set dialogSecurity
   *
   * @param String value to set to
   */
  public void setDialogSecurity(String newDialogSecurity) {
    plogger.report("-----Setting DIALOG security" + newDialogSecurity);
    if (newDialogSecurity != null)
      this.dialogSecurity = newDialogSecurity;
  }

  /**
   * Method to print a select box with each student ID in it!!!
   *
   * @return HTML formatted String
   */
  public String printUserSelect() {
    try {

    } catch (Exception ex) {
      plogger.report("Error in generate printUserSelect()");
      ex.printStackTrace();
    }
    return "Not Implemented";
  } // End of printUserSelect()
} // End of portfolioPortfolio
