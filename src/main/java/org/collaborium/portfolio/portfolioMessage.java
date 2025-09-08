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
 * Container Class for Portfolio Messages.  It will dramatically clean up
 * jdot*
 *
 * @author Daryl Herzmann
 */

package org.collaborium.portfolio;
import java.math.*;
import java.sql.*;
import java.text.*;
import org.collaborium.util.*;

public class portfolioMessage {

  // Here are the values that methods must edit
  private String author = null;
  private String authorName = null;
  private String idnum = null;
  private String subject = null;
  private String body = null;
  private String threadID = null;
  private String replyAuthor = null;
  private String link = null;
  private String postedDate = null;
  private Timestamp postTS = null;
  private String gid = null;
  private String security = "public";
  private String classification = "other";
  private String portfolio = null;
  private String topicid = "";
  private String role = null;
  private portfolioUser thisUser = null;
  private int smiles = 0;
  private int frowns = 0;
  private int cat_smiles = 0;
  private int cat_frowns = 0;
  private int learn_smiles = 0;
  private int learn_frowns = 0;

  /**
   * Set up Message from a ResultSet
   */
  public portfolioMessage(ResultSet rs) throws SQLException {

    // Okay, now we begin assignments
    getMessageRS(rs);
  }
  /**
   * Prototype to portfolioMessage because we need thisUser container
   * @param rs ResultSet of the message to save
   * @param newUser which is the portfolioUser container
   * @return instance of portfolioMessage
   */
  public portfolioMessage(portfolioUser newUser, ResultSet rs)
      throws SQLException {
    if (newUser != null)
      this.thisUser = newUser;
    getMessageRS(rs);
  }

  private void getMessageRS(ResultSet rs) throws SQLException {

    setAuthor(rs.getString("username"));
    setBody(rs.getString("body"));
    setReplyAuthor(rs.getString("touser"));
    setPostedDate(rs.getString("date"));
    setThreadID(rs.getString("threadid"));
    setSecurity(rs.getString("security"));
    setGID(rs.getString("gid"));
    setLink(rs.getString("link"));
    setAuthorName(rs.getString("name"));
    setidnum(rs.getString("idnum"));
    setClassification(rs.getString("type"));
    setPortfolio(rs.getString("portfolio"));
    setSubject(rs.getString("subject"));
    setSmiles(rs.getInt("smile"));
    setFrowns(rs.getInt("frown"));
    setCatSmiles(rs.getInt("cat_smile"));
    setCatFrowns(rs.getInt("cat_frown"));
    setLearnSmiles(rs.getInt("learn_smile"));
    setLearnFrowns(rs.getInt("learn_frown"));
    postTS = rs.getTimestamp("date");

    /* Need to check to see if this RS has a role name */
    ResultSetMetaData rsmd = rs.getMetaData();
    int iColumns = rsmd.getColumnCount();
    for (int i = 1; i <= iColumns; i++) {
      if (rsmd.getColumnName(i).equalsIgnoreCase("role")) {
        setRole(rs.getString("role"));
        plogger.report("I am setting Role to:" + this.role);
      }
    }
  }

  private void getMessage() throws SQLException {
    ResultSet rs =
        dbInterface.callDB("SELECT * from dialog "
                           + " WHERE idnum = '" + idnum + "'::numeric ");

    if (rs.next()) {
      // Okay, now we begin assignments
      getMessageRS(rs);
    }
  }
  /**
   * Prototype to portfolioMessage because we need thisUser container
   * @param messageID which is the message you would like to retrieve
   * @param retreieveDB which should I actually get the message (Save DBtime)
   * @param newUser which is the portfolioUser container
   * @return instance of portfolioMessage
   */
  public portfolioMessage(portfolioUser newUser, String messageID,
                          boolean retrieveDB) throws SQLException {
    if (newUser != null)
      this.thisUser = newUser;
    setidnum(messageID);
    if (retrieveDB) {
      getMessage();
    }
  }

  /**
   * Prototype to portfolioMessage because we need thisUser container
   * @param messageID which is the message you would like to retrieve
   * @param newUser which is the portfolioUser container
   * @return instance of portfolioMessage
   */
  public portfolioMessage(portfolioUser newUser, String messageID)
      throws SQLException {
    if (newUser != null)
      this.thisUser = newUser;
    setidnum(messageID);
    getMessage();
  }

  /**
   * Create a quick reference to a portfolioMessage, just so that
   * we can do DB IO for it
   */
  public portfolioMessage(String messageID, boolean retrieveDB)
      throws SQLException {
    setidnum(messageID);
    if (retrieveDB) {
      getMessage();
    }

  } // End of portfolioMessage

  /**
   * If we are instanced with a messageID, then the message is
   * in the DB.  Horray.  We pull the message and set up vars
   */
  public portfolioMessage(String messageID) throws SQLException {
    setidnum(messageID);
    getMessage();
  } // End of portfolioMessage

  /**
   * Here we are instancing with no args, thus we have a new message
   *
   */
  public portfolioMessage() {} // End of portfolioMessage

  /**
   * Method to comit a Post to the Database
   *
   */
  public void commitMessage() {
    //    plogger.report(authorName +"::"+ subject +"::"+ body );
    dbInterface.updateDB(
        "insert into dialog (username, name, "
        + " subject, body, threadid, "
        + " idnum, portfolio, type, security, gID, link, topicid)"
        + " values('" + author + "', '" + stringUtils.cleanString(authorName) +
        "', "
        + " '" + stringUtils.cleanString(subject) + "',"
        + " '" + stringUtils.cleanString(body) + "', "
        + " '" + threadID + "',"
        + " '" + idnum + "', '" + portfolio + "', "
        + " '" + stringUtils.cleanString(classification) + "', "
        + " '" + security + "', " + gid + " , '" +
        stringUtils.cleanString(link) + "', "
        + " '" + topicid + "')");
  }

  /**
   * Method that prints out a standard box for post
   * @return HTML formatted String
   */
  public String printStandard() {
    StringBuffer sbuf = new StringBuffer();
    String newStr = " ";
    try {
      Timestamp tempstamp = thisUser.getLastLogin();

      if (postTS == null)
        postTS = tempstamp;

      if (tempstamp.before(postTS)) {
        newStr = "<img src=\"/jportfolio/images/new.gif\" alt=\"new\" "
                 + "align=\"left\">\n";
      }
    } catch (Exception ex) {
      plogger.report("Line 217 in portfolioMessage, argh");
      ex.printStackTrace();
    }

    sbuf.append("<div class=\"dialog-post-header\">\n" + newStr +
                "<h4 class=\"" + classification + "\">" + subject + " (" +
                classification + ")</h4>\n"
                + " Posted by <a href=\"/jportfolio/users/" + author + "\">" +
                authorName + "</a>");
    if (this.role != null) {
      sbuf.append(" (" + this.role + ") ");
    }

    if (postedDate != null) {
      sbuf.append(" on"
                  + " " + stringUtils.gmtDate(getDate()));
    }
    sbuf.append("</div>\n");
    sbuf.append("<p>" + stringUtils.toBR(body));

    if (link != null && link.length() > 0) {
      sbuf.append("<div class=\"dialog-post-link\">Link: <a target=\"_new\" "
                  + " href=\"" + link + "\">" + link + "</a></div>\n");
    }

    return sbuf.toString();
  }

  /**
   * Method to display more info about the post
   */
  public String printExtended(String thisPageURL) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append("<div class=\"dialog-post-footer\">\n");

    //	String tempCat_smiles = "--";
    //	String tempCat_frowns = "--";
    //	String tempLearn_smiles = "--";
    //	String tempLearn_frowns = "--";

    //	if (thisUser != null ) {
    //
    //	  if (thisUser.getUserID().equalsIgnoreCase(author) ||
    // thisUser.isAdmin() ){
    //
    //		tempCat_smiles = cat_smiles.toString();
    //		tempCat_frowns = cat_frowns.toString();
    //
    //		tempLearn_smiles = learn_smiles.toString();
    //		tempLearn_frowns = learn_frowns.toString();

    //	}  } // End of if

    //	sbuf.append("<BR><b>This post is correctly categorized:</b>\n"
    //		+ tempCat_smiles +"<a
    // href=\"javascript:new_window('/jportfolio/jsp/jdot/cat_smile.jsp?idnum="+
    // idnum +"');\">"
    //		+"<img src=\"/jportfolio/images/icon_smile.gif\"
    // border=\"0\"></a>\n"
    //		+ tempCat_frowns +"<a
    // href=\"javascript:new_window('/jportfolio/jsp/jdot/cat_frown.jsp?idnum="+
    // idnum +"');\">"
    //		+"<img src=\"/jportfolio/images/icon_frown.gif\"
    // border=\"0\"></a>\n");

    //	sbuf.append("<BR><b>This post contributed to my learning:</b>\n"
    //                + tempLearn_smiles +"<a
    //                href=\"javascript:new_window('/jportfolio/jsp/jdot/learn_smile.jsp?idnum="+
    //                idnum +"');\">"
    //                +"<img src=\"/jportfolio/images/icon_smile.gif\"
    //                border=\"0\"></a>\n"
    //                + tempLearn_frowns +"<a
    //                href=\"javascript:new_window('/jportfolio/jsp/jdot/learn_frown.jsp?idnum="+
    //                idnum +"');\">"
    //                +"<img src=\"/jportfolio/images/icon_frown.gif\"
    //                border=\"0\"></a><BR>\n");

    // Here is where we set a limit on how many sub-posts there can be,
    // currently we only allow a 6 deep thread, so if the idnum is 6x5 chars in
    // length, we had better not allow them to reply to this post
    if (idnum.length() < 30)
      sbuf.append("\n<a href='" + thisPageURL + "?mode=p&threadid=" + threadID +
                  "&idnum=" + idnum + "'>"
                  + "Post a follow up</a>\n");
    else
      sbuf.append("\n<i>Sorry, this post is too deep in the discussion to "
                  + "respond to.</i>\n");

    String numMessages = subPosts();

    if (!numMessages.equalsIgnoreCase("0")) {

      sbuf.append("\n(Follow-Ups: " + numMessages + ") "
                  + " <a href='" + thisPageURL + "?mode=r&idnum=" + idnum +
                  "'>Read More</a>"
                  + " &nbsp; | &nbsp; <a href='" + thisPageURL +
                  "?mode=e&threadid=" + threadID + "'>Overview</a>"
                  + "<br> ");
    }
    sbuf.append("</div>\n");

    return sbuf.toString();
  } // End of printExtended

  public String subPosts() {

    // We need to have a BigInt so to add 1, lots of digits!! :)
    BigInteger endID = new BigInteger(idnum);
    BigInteger secondOne = endID.add(BigInteger.ONE);
    String numMessages = "0";

    try {
      ResultSet rs2 = dbInterface.callDB(
          "select count(name) as result "
          + " from dialog where idnum > " + idnum + "0000::numeric "
          + " and idnum < " + secondOne + "0000::numeric ");
      rs2.next();
      numMessages = rs2.getString("result");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return numMessages;
  } // End of subPosts()

  /**
   *  Print a long version of the post
   *  @param thisPageURL  which is a reference to this current page
   *  @return HTML formatted string
   */
  public String printLong(String thisPageURL) {
    StringBuffer sbuf = new StringBuffer();
    String numMessages = subPosts();
    try {
      sbuf.append("<P><i>subject:</i><font size=+1 "
                  + "class=\"" + classification + "\"><b>" + subject + " (" +
                  classification + ")</b></font>\n");
    } catch (Exception ex) {
      sbuf.append("<P><i>subject:</i><font size=+1><b>" + subject + " (" +
                  classification + ")</b></font>\n");
    }

    // More info
    sbuf.append("\n<BR><i>author:</i>"
                + "<a class='commands' href='/jportfolio/users/" + author +
                "'>" + authorName + "</a> "
                + "\n<BR><i>date:</i>" + stringUtils.gmtDate(getDate()) +
                "\n<BR><i>posts:</i> Currently " + numMessages +
                " responses\n");
    sbuf.append("\n<BR><i>groupID:</i>" + gid);

    sbuf.append("<P>");
    sbuf.append(stringUtils.toBR(body));

    if (link != null && link != "")
      sbuf.append("<BR><B>Link Ref:</B> <a target=\"_new\" "
                  + " href=\"" + link + "\">" + link + "</a>\n");

    if (idnum.length() < 30)
      sbuf.append("\n<BR><BR>| <a href='" + thisPageURL +
                  "?mode=p&threadid=" + threadID + "&idnum=" + idnum + "'>"
                  + "Post a follow up</a> |\n");
    return sbuf.toString();
  }

  public String printShort(String thisPageURL) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append("<LI>");
    try {
      sbuf.append("<font size=+1 class=\"" + security + "\"><b>" + subject +
                  " (" + security + ")</b></font>\n");
    } catch (Exception ex) {
      sbuf.append("<font size=+1><b>" + subject + " (" + security +
                  ")</b></font>\n");
    }

    sbuf.append("\n<BR><i>author:</i> " + authorName + " ");

    if (idnum.length() < 30)
      sbuf.append("\n<BR>| <a href='" + thisPageURL +
                  "?mode=p&threadid=" + threadID + "&idnum=" + idnum + "'>"
                  + "Post a follow up</a>\n");

    sbuf.append("\n | <a href='" + thisPageURL + "?mode=r&idnum=" + idnum + "'>"
                + "Read More</a> |\n");

    sbuf.append("</LI>\n");

    return sbuf.toString();
  }

  /**
   * Method to set the Role for this Post
   * @param newRole which is the new Role
   */
  public void setRole(String newRole) { this.role = newRole; }

  /**
   * Method to return a nice formated Date String
   * @return String for GMT Date
   */
  public java.util.Date getDate() {
    java.util.Date dateO = null;
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
      dateO = sdf.parse(postedDate);
    } catch (Exception ex) {
      System.err.println("Exception caught.\n" + ex);
      ex.printStackTrace();
    }

    return dateO;
  }

  public void setUser(portfolioUser newUser) { this.thisUser = newUser; }

  /**
   * Set cat_smiles from DB
   */
  public void setCatSmiles(int thisSmiles) { this.cat_smiles = thisSmiles; }

  /**
   * Set learn_smiles from DB
   */
  public void setLearnSmiles(int thisSmiles) { this.learn_smiles = thisSmiles; }

  /**
   * Set smiles from DB
   */
  public void setSmiles(int thisSmiles) { this.smiles = thisSmiles; }

  /**
   * Set frowns from DB
   */
  public void setFrowns(int thisFrowns) { this.frowns = thisFrowns; }

  /**
   * Set cat_frowns from DB
   */
  public void setCatFrowns(int thisFrowns) { this.cat_frowns = thisFrowns; }

  /**
   * Set learn_frowns from DB
   */
  public void setLearnFrowns(int thisFrowns) { this.learn_frowns = thisFrowns; }

  public void addSmile() {
    try {
      dbInterface.updateDB("UPDATE dialog SET smile = smile + 1 WHERE "
                           + " idnum = " + idnum + " ");

    } catch (Exception ex) {
      plogger.report("Error in trying to add a smile.");
    }
    this.smiles = this.smiles + 1;
  } // End of addSmile()

  public void addFrown() {
    try {
      dbInterface.updateDB("UPDATE dialog SET frown = frown + 1 WHERE "
                           + " idnum = " + idnum + " ");

    } catch (Exception ex) {
      plogger.report("Error in trying to add a frown.");
    }
    this.frowns = this.frowns + 1;
  }

  public void addCatFrown() {
    try {
      dbInterface.updateDB("UPDATE dialog SET cat_frown = cat_frown + 1 WHERE "
                           + " idnum = " + idnum + " ");

    } catch (Exception ex) {
      plogger.report("Error in trying to add a frown.");
    }
    this.cat_frowns = this.cat_frowns + 1;
  }

  public void addLearnFrown() {
    try {
      dbInterface.updateDB(
          "UPDATE dialog SET learn_frown = learn_frown + 1 WHERE "
          + " idnum = " + idnum + " ");

    } catch (Exception ex) {
      plogger.report("Error in trying to add a frown.");
    }
    this.learn_frowns = this.learn_frowns + 1;
  }

  public void addCatSmile() {
    try {
      dbInterface.updateDB("UPDATE dialog SET cat_smile = cat_smile + 1 WHERE "
                           + " idnum = " + idnum + " ");

    } catch (Exception ex) {
      plogger.report("Error in trying to add a smile.");
    }
    this.cat_smiles = this.cat_smiles + 1;
  }

  public void addLearnSmile() {
    try {
      dbInterface.updateDB(
          "UPDATE dialog SET learn_smile = learn_smile + 1 WHERE "
          + " idnum = " + idnum + " ");

    } catch (Exception ex) {
      plogger.report("Error in trying to add a smile.");
    }
    this.learn_smiles = this.learn_smiles + 1;
  }

  /**
   * Set portfolio
   */
  public void setPortfolio(String newPortfolio) {
    if (newPortfolio != null)
      this.portfolio = newPortfolio;
  }
  public String getPortfolio() { return this.portfolio; }

  /**
   *  This method is needed to set classification if a user puts in
   *  their own!
   */
  public void setClassification(String newCfc, String myCfc) {
    if (myCfc != null && !myCfc.equalsIgnoreCase("")) {
      plogger.report("My Classification:" + myCfc + ":\n");
      this.classification = myCfc;
    } else {
      plogger.report("Default Classification:" + newCfc + ":\n");
      this.classification = newCfc;
    }

  } // End

  /**
   * Set classifciation
   */
  public void setClassification(String newClassification) {
    if (newClassification != null)
      this.classification = newClassification;
  }
  public String getClassification() { return this.classification; }

  /**
   * Set idnum
   */
  public void setidnum(String newidnum) {
    if (newidnum != null)
      this.idnum = newidnum;
  }
  public String getidnum() { return this.idnum; }
  /**
   * Set Author Name
   */
  public void setAuthorName(String newAuthorName) {
    if (newAuthorName != null)
      this.authorName = newAuthorName;
  }
  public String getAuthorName() { return this.authorName; }

  /**
   * Set topicid
   */
  public void setTopicid(String newTopicid) {
    if (newTopicid != null)
      this.topicid = newTopicid;
  }
  public String getTopicid() { return this.topicid; }

  /**
   * Set Link of this message
   */
  public void setLink(String newLink) {
    if (newLink != null)
      this.link = newLink;
  }
  public String getLink() { return this.link; }

  /**
   * Set GID of this message
   */
  public void setGID(String newGID) {
    if (newGID != null)
      this.gid = newGID;
  }
  public String getGID() { return this.gid; }

  /**
   * Set security of this message
   */
  public void setSecurity(String newSecurity) {
    if (newSecurity != null)
      this.security = newSecurity;
  }
  public String getSecurity() { return this.security; }

  /**
   * Set threadID of this message
   */
  public void setThreadID(String newThreadID) {
    if (newThreadID != null)
      this.threadID = newThreadID;
  }
  public String getThreadID() { return this.threadID; }

  /**
   * Set postedDate of the message
   */
  public void setPostedDate(String newPostedDate) {
    if (newPostedDate != null)
      this.postedDate = newPostedDate;
  }
  public String getPostedDate() { return this.postedDate; }

  /**
   * Set replyAuthor of the message
   */
  public void setReplyAuthor(String newReplyAuthor) {
    if (newReplyAuthor != null)
      this.replyAuthor = newReplyAuthor;
  }
  public String getReplyAuthor() { return this.replyAuthor; }

  /**
   * Set body of the message
   */
  public void setBody(String newBody) {
    if (newBody != null)
      this.body = newBody;
  }
  public String getBody() { return this.body; }

  /**
   * Set Subject of the message
   */
  public void setSubject(String newSubject) {
    if (newSubject != null)
      this.subject = newSubject;
  }
  public String getSubject() { return this.subject; }

  /**
   * Set Author Name of Post
   */
  public void setAuthor(String newAuthor) {
    if (newAuthor != null)
      this.author = newAuthor;
  }
  public String getAuthor() { return this.author; }

} // End of portfolioUser
