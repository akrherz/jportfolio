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
package org.collaborium.portfolio;
/**
 * This is my first attempt at creating a container that could be a student
 * We shall see how this works, hopefully well :)  It is works, we can have
 * generic applications create a "portfolioUser" reference and then they are all
 * set
 *
 * @author Daryl Herzmann
 */

import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

public class portfolioUser implements HttpSessionBindingListener {

  // Here are the values that methods must edit
  private String realName = null;
  private String fName = null;
  private String lName = null;
  private String groupID = "-99";
  private String userID = "";
  private String emailAddress = "";
  private String style = "basic";
  private String threadType = "all";

  private Boolean isAdmin = false;
  private Boolean isValidUser = false;
  private Boolean doesForecast = true;

  // Stuff for the Application Registration
  public boolean usesCalendar = false;
  public boolean usesDialog = false;
  public boolean usesForecast = false;
  public boolean usesQuiz = false;

  // An Array to hold idnums for messages a user responded to
  private Hashtable notedPosts = new Hashtable();
  private Hashtable notedCatPosts = new Hashtable();
  private Hashtable notedLearnPosts = new Hashtable();

  // Reference to the portfolio they are currently in
  public portfolioPortfolio myPortfolio = new portfolioPortfolio();
  private portfolioCred myCred = new portfolioCred(portfolioCred.anonymous);

  // Something to hold when the user was last logged in
  private Timestamp lastLogin =
      Timestamp.valueOf("2001-01-01 01:00:00.000000000");

  /**
   * Method to override HttpSessionBindingListener.valueBound
   * it is called when the object is bound to the session
   */
  public void valueBound(HttpSessionBindingEvent event) {

    plogger.report("portfolioUser object created: " + realName);
    try {
      dbInterface.updateDB("INSERT into sessions (username) VALUES "
                           + " ( '" + userID + "' ) ");
    } catch (Exception ex) {
      plogger.report("Error in Creating Session DB Entry");
    }
  }

  /**
   * Method to override HttpSessionBindingListener.valueUnbound
   * it is called when the object is unbound from the session
   * When it is unbound, we then go into jlib and delete the user from
   * the current users array
   */
  public void valueUnbound(HttpSessionBindingEvent event) {

    plogger.report("portfolioUser object destroyed for: " + realName);

    jlib.deleteUser(userID);
    try {
      dbInterface.updateDB("UPDATE sessions SET logout = CURRENT_TIMESTAMP "
                           + " WHERE username = '" + userID +
                           "' and logout IS NULL ");

    } catch (Exception ex) {
      plogger.report("Error in Creating Session DB Entry");
    }
  }

  /**
   * Constructor for this class, this construct queries the users table
   * for more information on the user, if an entry is found, the appropiate
   * values are set
   * @param newUserID string value of the new user to init class with
   */
  public portfolioUser(String newUserID) {

    this.userID = newUserID;
    try {
      ResultSet rs = dbInterface.callDB(
          "SELECT "
          + " getUserName('" + userID + "') as longname, * from users "
          + " WHERE username = '" + newUserID + "' ");
      if (rs.next()) {
        this.emailAddress = rs.getString("email");
        this.realName = rs.getString("longname");
        this.fName = rs.getString("fName");
        this.lName = rs.getString("lName");
        String testStyle = rs.getString("color");

        if (testStyle != null && testStyle.length() > 2)
          this.style = testStyle;
        isValidUser = Boolean.TRUE;
      }

      rs = dbInterface.callDB("SELECT logout from sessions "
                              + " WHERE username = '" + newUserID +
                              "' and logout IS NOT NULL "
                              + " ORDER by logout DESC LIMIT 1");
      if (rs.next()) {
        this.lastLogin = rs.getTimestamp("logout");
      }

    } catch (Exception ex) {
      ex.printStackTrace();
      plogger.report("Problems getting username!");
    }
  }

  /**
   * Method to check an idnum to see if the user allready
   * submitted a smily/frowny face
   */
  public boolean checkDialogidnum(String idnum) {
    // notedPosts
    if (!notedPosts.isEmpty()) {
      if (notedPosts.containsKey(idnum))
        return false;
      else
        return true;
    }
    notedPosts.put(idnum, "Hello");
    return true;
  } // End of checkDialogidnum()

  /**
   * Method to check an idnum to see if the user allready
   * submitted a smily/frowny face
   */
  public boolean checkDialogidnum_cat(String idnum) {
    // notedPosts
    if (!notedCatPosts.isEmpty()) {
      if (notedCatPosts.containsKey(idnum))
        return false;
      else
        return true;
    }
    notedCatPosts.put(idnum, "Hello");
    return true;
  } // End of checkDialogidnum()

  /**
   * Method to check an idnum to see if the user allready
   * submitted a smily/frowny face
   */
  public boolean checkDialogidnum_learn(String idnum) {
    // notedPosts
    if (!notedLearnPosts.isEmpty()) {
      if (notedLearnPosts.containsKey(idnum))
        return false;
      else
        return true;
    }
    notedLearnPosts.put(idnum, "Hello");
    return true;
  } // End of checkDialogidnum()

  /**
   * Blank constructor just to init the class.
   *
   */
  public portfolioUser() {}

  /**
   * Does this user forecast?
   * @return Boolean value to if this user is allowed to forecast
   *
   */
  public boolean doForecast() { return doesForecast.booleanValue(); }

  /**
   * SOme times it is needed to just set that this user doesn't forecast
   */
  public void dontForecast() { this.doesForecast = Boolean.FALSE; }

  /**
   * getThreadType()
   *  returns the thread type set for this user
   */
  public String getThreadType() { return this.threadType; }

  /**
   * setThreadType()
   *  logic method to set threadType based on CGI post
   */
  public void setThreadType(String posted) {
    if (posted != null) {
      this.threadType = posted;
    }
  } // End of setThreadType()

  /**
   *
   */
  public Timestamp getLastLogin() { return this.lastLogin; }

  /**
   * Set isAdmin value to something
   * @param Boolean value for this user
   */
  public void setIsAdmin(Boolean newValue) {
    if (newValue != null)
      this.isAdmin = newValue;
  }

  /**
   * IS this user a authed portfolio User
   * @return boolean for this user
   */
  public boolean isPortfolioUser() { return isValidUser.booleanValue(); }

  /**
   * Return boolean to the admin status of this user
   * @return boolean for this user
   */
  public boolean isAdmin() { return isAdmin.booleanValue(); }

  /**
   * Return the value of the dialog Security
   * @return String value of dialogSecurity
   */
  public String getDialogSecurity() {
    //  	return this.dialogSecurity;
    return myPortfolio.getDialogSecurity();
  }

  /**
   * Method to set dialogSecurity
   * @param String value to set to
   */
  public void setDialogSecurity(String newDialogSecurity) {
    //  	if ( newDialogSecurity != null)
    //		this.dialogSecurity = newDialogSecurity;
    myPortfolio.setDialogSecurity(newDialogSecurity);
  }

  /**
   * Return the first Name of the user
   * @return fName of the user
   */
  public String getFirstName() { return fName; }

  /**
   * Return the last Name of the user
   * @return last of the user
   */
  public String getLastName() { return lName; }

  /**
   * Give me my credential setting
   */
  public int getCred() { return myCred.getCred(); }

  /**
   * Return the value of the current portfolio
   * @return portfolio string value
   */
  public String getPortfolio() {
    //  	return portfolio;
    return myPortfolio.getID();
  }
  public String getPortfolioName() {
    // 	return portfolioName;
    return myPortfolio.getName();
  }

  /**
   * Method to set the value of the current portfolio
   * we must also reset the groupID as well
   * @param thisPortfolio string value of the new portfolio to be set
   */
  public void setPortfolio(String thisPortfolio) {

    myCred = new portfolioCred(portfolioCred.user);
    setIsAdmin(Boolean.FALSE); // Reset Admin status
    groupID = "-99";           // Reset groupID
    if (thisPortfolio == null) {
      myPortfolio = new portfolioPortfolio();
      return;
    }

    myPortfolio = new portfolioPortfolio(thisPortfolio);

    doesForecast = Boolean.TRUE;
    usesCalendar = myPortfolio.usesCalendar;
    usesDialog = myPortfolio.usesDialog;
    usesForecast = myPortfolio.usesForecast;
    usesQuiz = myPortfolio.usesQuiz;

    try {
      ResultSet rs =
          dbInterface.callDB("SELECT * from students "
                             + " WHERE username = '" + userID + "' and "
                             + " portfolio = '" + thisPortfolio + "' ");
      if (rs != null && rs.next()) {
        myCred.setCred(rs.getInt("cred"));
        this.doesForecast = !rs.getBoolean("nofx");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  } // End of setPortfolio

  /**
   * Return the value of the current style
   * @return style string value of the current style
   */
  public String getStyle() { return style; }

  /**
   * Method to set the style for this user
   * @param newStyle string value of the new style
   */
  public void setStyle(String newStyle) { style = newStyle; }

  /**
   * Method to set the userID for this user
   * @param newUserID string value of the new user
   */
  public void setUserID(String newUserID) { userID = newUserID; }

  public void setEmailAddress(String newEmail) { emailAddress = newEmail; }

  /**
   * Method to get the current emailAddress
   * @return emailAddress for this user
   */
  public String getEmailAddress() { return emailAddress; }

  /**
   * Method to get the current userId for this user
   * @return userID of this user
   */
  public String getUserID() { return userID; }

  /**
   * Return the realName for this user
   * @return realName value of the realName for this user
   */
  public String getRealName() { return realName; }

  public void setFName(String newFName) { this.fName = newFName; }
  public void setLName(String newLName) { this.lName = newLName; }

  /**
   * Method to set the realName to a value
   * @param newRealName string value for the new Real Name
   */
  public void setRealName(String newRealName) { realName = newRealName; }

  /**
   * Return the value of the current groupID
   * @return groupID value of the current groupID
   */
  public String getGroupID() { return groupID; }

  /**
   * Method to set the groupID for this user
   * @param newGroupID string value of the new group ID
   */
  public void setGroupID(String newGroupID) { groupID = newGroupID; }

  /**
   * Method to return where this user should be right now
   * @return portHome
   */
  public String getHome() { return myPortfolio.getHome(); }

  /**
   *  Method to return the base for this user, which is desperated needed
   *  for the notification apps
   */
  public String getBase() { return myPortfolio.getBase(); }

} // End of portfolioUser
