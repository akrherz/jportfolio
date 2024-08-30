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
 * Portfolio Application Suite
 *	Quiz Engine
 * Servlet that does the dirty work for the quiz engine
 *
 * @author Doug Fils
 * @author Daryl Herzmann
 */

import java.io.*;
// import java.lang.*;
import java.lang.String.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.collaborium.portfolio.*;
import org.collaborium.util.*;

public class jquiz extends HttpServlet {

  public static final String TITLE = "Portfolio Quiz";
  private int baseCred = portfolioCred.user; /* Must be a user to access */
  static String servletHttpBase = jlib.servletHttpBase;
  static String thisPageURL = servletHttpBase + "/jquiz";

  public void init(ServletConfig config) throws ServletException {
    plogger.report("Firing up jquiz engine.");
    super.init(config);

  } // End of init()

  /**
   * Method to destroy the servlet, called only once!
   *
   */
  public void destroy() {
    super.destroy();
    plogger.report("Method Destroy is called in jquiz.");
  } // End of destroy()

  /**
   * Method that handles servlet GET requests
   *
   * @param servlet request
   * @param servlet response
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // plogger.report("\n---Starting Jquiz---");
    /* Authentification */
    HttpSession session = request.getSession(true);
    authBean auth = new authBean(request, session);
    if (auth.requiredUserPort(response, session, "/jportfolio/servlet/jquiz")) {
      return;
    }

    /** Set up some containers to hold page content */
    StringBuffer sideContent = new StringBuffer();
    StringBuffer pageContent = new StringBuffer();

    /** Container to access grades for user */
    gradebook studentGrades = null;

    /** Set up servlet output */
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
    String callMethod = request.getParameter("mode");
    String qid = (String)request.getParameter("qid");

    jlib.addUser(thisUser.getUserID(), "Quiz");
    studentGrades = new gradebook(thisUser);

    if (callMethod == null)
      callMethod = "x";

    /**
     * Switch Statement Logic
     *
     * h | quiz help Box
     * p | view Quiz
     * r | review Quizes
     * t | take Quiz
     * default page
     */
    switch (callMethod.charAt(0)) {
    case 'h':
      pageContent.append(quizBoxHelp());
      break;
    case 'p':
      pageContent.append(viewQuiz(thisUser, studentGrades, qid));
      break;
    case 'r':
      pageContent.append(reviewQuizzes(thisUser));
      break;
    case 't':
      pageContent.append(takeQuiz(thisUser, qid, thisPageURL));
      break;
    default:
      pageContent.append(introduction());
      //	out.println("<P>"+ listQuizzes(user, portfolio) );
      pageContent.append(
          "<P>" + takableQuizzes(thisUser, studentGrades, false, thisPageURL));
      break;
    } // End of switch()

    sideContent.append(sideBar(thisUser, studentGrades));
    out.println(jlib.header(thisUser, TITLE, "Quiz"));
    out.println(jlib.makePage(sideContent.toString(), pageContent.toString()));

    out.println(jlib.footer());

    plogger.report("---Ending Jquiz---\n");

  } // End of doGet()

  /**
   * Method to handle method post
   *
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // plogger.report("--- Start Jquiz\n");
    /* Authentification */
    HttpSession session = request.getSession(true);
    authBean auth = new authBean(request, session);
    if (auth.requiredUserPort(response, session, "/jportfolio/servlet/jquiz")) {
      return;
    }

    /** containers to hold HTML for page */
    StringBuffer pageContent = new StringBuffer();
    StringBuffer sideContent = new StringBuffer();

    gradebook studentGrades = null;

    /** Set up output response */
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
    String callMethod = request.getParameter("mode");
    String qid = (String)request.getParameter("qid");

    jlib.addUser(thisUser.getUserID(), "jquiz");
    studentGrades = new gradebook(thisUser);

    if (callMethod == null)
      callMethod = "x";

    /**
     * Switching logic
     *
     * h | post a dialog response
     * u | input the Quiz allready
     * default | ask why are you here
     */
    switch (callMethod.charAt(0)) {
    case 'u':
      pageContent.append(inputQuiz(thisUser, request, studentGrades));
      break;
    case 'h':
      pageContent.append(inputQuizResponse(thisUser, request));
      break;
    default:
      pageContent.append(jlib.topBox("Error:"));
      pageContent.append("No Method Called!");
      pageContent.append(jlib.botBox());
      break;
    } // End of switch

    sideContent.append(sideBar(thisUser, studentGrades));
    out.println(jlib.header(thisUser, TITLE, "Quiz"));
    out.println(jlib.makePage(sideContent.toString(), pageContent.toString()));

    out.println(jlib.footer());

    plogger.report("---Ending Jquiz\n");

  } // End of doPost()

  public static String introduction() {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Information:"));

    sbuf.append("<font class=\"Instructions\"><blockquote>This page contains "
                + "quiz links and \n"
                + " information for your portfolio.  Any active quizzes will "
                + "be listed below.\n"
                + " </blockquote>\n</font>\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of introduction()

  public static String quizBoxHelp() {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Quiz Box Info:"));

    sbuf.append("<font color=\"green\"><blockquote>The box on the left hand "
                + "side lists out quizzes that are\n"
                + "currently available to take.  After the quiz has been "
                + "taken for the maximun number of allowed \n"
                + " attempts, the quiz appears listed, but unlinked.<BR>\n"
                + " The second column lists how many times you have taken "
                + "the quiz and the maximum number of alloted\n"
                + " times you have to take the quiz.  A value of \"0\" means "
                + "that you can take the quiz until you get\n"
                + " it right.<BR>\n"
                + " The third column shows your score for that quiz.  A "
                + "\"-\" means that you have yet to take the quiz once.\n"
                + "</blockquote>\n"
                + "</font>\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of introduction()

  /**
   * Method to print out a listing of all Quizzes taken
   *
   * @param userID value of the current user
   * @param portfolio value of the current portfolio value
   * @return HTML formatted string
   */
  public static String reviewQuizzes(portfolioUser thisUser) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Previous Quizzes:"));

    sbuf.append("<font color=\"green\"><blockquote>Note that quizzes "
                + " will not appear in this list\n"
                +
                " until it has expired.  Thus a previously taken quiz will not "
                + " appear here until it is\n"
                + " no longer active.</blockquote></font>\n");

    sbuf.append("<TABLE bgcolor=\"white\" width=\"100%\" cellpadding=\"2\" "
                + " cellspacing=\"0\">\n");

    sbuf.append("<TR><TH colspan=\"3\"><h3>All Taken Quizzes</H3></TH></TR>\n");
    sbuf.append("<TR><TH align=\"left\">Quiz Name:</TH>"
                + "<TH align=\"left\">Score:</TH></TR>\n");

    ResultSet rs = null;
    try {
      rs = jlib.callDB("SELECT a.qid, q.qname, s.score from quizattempts a, "
                       + " quizes q, scores s "
                       + " WHERE a.userID = '" + thisUser.getUserID() + "' "
                       + " and a.portfolio = '" + thisUser.getPortfolio() +
                       "' "
                       + " and a.qid = q.quiznum and s.assign = q.quiznum "
                       + " and s.userid = a.userID " +
                       (" and s.portfolio = a.portfolio and q.stopdate < "
                        + "CURRENT_TIMESTAMP "));
      while (rs.next()) {
        sbuf.append("<TR><TD> "
                    + "<a href=\"" + thisPageURL +
                    "?mode=p&qid=" + rs.getString("qid") + "\">\n" +
                    rs.getString("qname") + "</a></TD>"
                    + " <TD>" + rs.getString("score") + "</TD></TR>\n");
      }
    } catch (Exception ex) {
      plogger.report("Error caught trying to get all quizzes");
      ex.printStackTrace();
    }

    sbuf.append("</TR></TABLE>\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of reviewQuizzes()

  /**
   * MEthod to enter in a student's taken quiz
   *
   * @param thisUser which is the container for the current user
   * @param request Servlet request container
   * @param studentGrades a pointer to the student's grades
   */
  public static String inputQuiz(portfolioUser thisUser,
                                 HttpServletRequest request,
                                 gradebook studentGrades) {

    StringBuffer sbuf = new StringBuffer();

    try {

      // First we create the normal HTML box to frame this info
      sbuf.append(jlib.topBox("Input Quiz:"));

      // Fetch all of the necessary quiz postings
      String quizID = (String)request.getParameter("qid");
      String question1 = (String)request.getParameter("question1ans");
      String question2 = (String)request.getParameter("question2ans");
      String question3 = (String)request.getParameter("question3ans");
      String body = (String)request.getParameter("body");
      String thisAttempt = "1";

      // Avoid any null pointers
      if (question1 == null)
        question1 = "0";
      if (question2 == null)
        question2 = "0";
      if (question3 == null)
        question3 = "0";

      portfolioQuiz thisQuiz = new portfolioQuiz(quizID, "yes");

      try {
        /**
         * Lets figure out if this user has taken the quiz or not. If they have,
         * then the attempt variable is reset to this current attempt
         */
        ResultSet rs6 = jlib.callDB(
            "SELECT attempt +1 as att from quizattempts WHERE"
            + " userid = '" + thisUser.getUserID() + "' "
            + " and portfolio = '" + thisUser.getPortfolio() + "' "
            + " and qid = '" + quizID + "' ");
        if (rs6.next()) {
          thisAttempt = rs6.getString("att");
        }

        /**
         * Now query the DB to see how many attempts are allowed for this quiz
         */
        ResultSet rs7 = jlib.callDB("SELECT attempts FROM quizes "
                                    + " WHERE quiznum = " + quizID + " ");
        rs7.next();
        String allowedAttempts = rs7.getString("attempts");

        if (thisQuiz.goodAttempt(thisAttempt)) { // Allowed
          // Delete previous quiz attempts
          jlib.updateDB("DELETE from quizattempts WHERE"
                        + " userid = '" + thisUser.getUserID() + "' "
                        + " and portfolio = '" + thisUser.getPortfolio() +
                        "' "
                        + " and qid = '" + quizID + "' ");
          // Save this quiz attempt
          jlib.updateDB(
              "INSERT into quizattempts(qid, question1, question2, question3,"
              + " userid, portfolio, attempt) VALUES"
              + " (" + quizID + ", "
              + " '" + question1 + "', '" + question2 + "', '" + question3 +
              "', "
              + " '" + thisUser.getUserID() + "', '" +
              thisUser.getPortfolio() + "', " + thisAttempt + ")");
          sbuf.append("Your quiz attempt has successfully been saved.");

          sbuf.append("<H3>Quiz Grade:</H3>");

          String myRight =
              thisQuiz.numberRight(question1, question2, question3);
          String numQs = thisQuiz.numberQuestions();

          /**
           * Now if the quiz is allowed to be taken more than once, we need to
           * make sure that the student got all the Qs right, before we show the
           * score.
           */
          if (!allowedAttempts.equalsIgnoreCase("1")) {

            if (numQs.equalsIgnoreCase(myRight)) {
              sbuf.append("<BR>You got them all right!\n");
              jlib.updateDB("UPDATE quizattempts SET attempt = -1 "
                            + " WHERE userid = '" + thisUser.getUserID() + "' "
                            + " and portfolio = '" + thisUser.getPortfolio() +
                            "' ");
              sbuf.append("<BR>Since you answered the questions correctly, "
                          +
                          " you will not be allowed to take the quiz again.\n");
            }
            sbuf.append("<BR>You got " + myRight + " out of " + numQs +
                        " questions right!\n");

          } else {
            sbuf.append("Your quiz has been saved.  You can find out your "
                        + "grade after the quiz has \n"
                        + " expired.<BR>\n");
          }

          /**
           * Add the score to the database
           */
          studentGrades.addScoreElement("jquiz", quizID, myRight);

        } else {
          // Those bastards
          //	jlib.updateDB("INSERT into abuse values('"+
          // thisUser.getUserID()+"')");

          sbuf.append("<font class=\"warn\">Access Warning:</font> This quiz "
                      + "has been closed.  This quiz \n"
                      + " attempt is greater than the maximum allowed number "
                      + "of quiz attempts.\n");
        }

      } catch (Exception ex) {
        plogger.report("Error trying to input the quiz.");
        ex.printStackTrace();
      }

      /** Time to input the response, if there is one */
      if (body.length() > 4) {

        portfolioMessage myMessage = new portfolioMessage();
        String notifyBaseURL = "/jportfolio/servlet/jquiz";
        String notifyBaseURL2 = "/jportfolio/servlet/jdot3";
        String thisQuizName = thisQuiz.getQuizName();
        if (thisQuizName.length() > 5) {
          thisQuizName = (thisQuiz.getQuizName()).substring(0, 4);
        }
        /**
        ResultSet portHome = dbInterface.callDB("SELECT porthome from portfolios
        "
          +" WHERE portfolio = '"+ thisUser.getPortfolio() +"' and "
          +" porthome != '/jportfolio/servlet/' ");
        if ( portHome.next() ) {
          notifyBaseURL = portHome.getString("porthome") +"/quiz/index.jsp";
          notifyBaseURL2 = portHome.getString("porthome") +"/dialog/index.jsp";
        }
        */

        ResultSet maxth =
            jlib.callDB("select MAX (idnum)+1 as result from dialog"
                        + " WHERE idnum > 10000 and idnum < 20000");
        maxth.next();
        myMessage.setidnum(maxth.getString("result"));
        myMessage.setThreadID(maxth.getString("result"));

        myMessage.setSecurity("private");
        myMessage.setBody(body);
        myMessage.setSubject("Quiz " + thisQuizName + " Feedback");
        //	  myMessage.setReplyAuthor( ne);
        myMessage.setClassification("other");
        myMessage.setAuthor(thisUser.getUserID());
        myMessage.setAuthorName(thisUser.getRealName());
        myMessage.setPortfolio(thisUser.getPortfolio());
        myMessage.setGID(thisUser.getGroupID());
        myMessage.setLink(notifyBaseURL + "?qid=" + quizID + "&mode=p");

        myMessage.commitMessage();

        Vector myVect = thisUser.myPortfolio.getAdmins();
        for (int i = 0; i < myVect.size(); i++) {
          String thisUserID = (String)myVect.get(i);
          dbInterface.updateDB(
              "insert into notify(username, portfolio, program "
              + ", message) values('" + thisUserID + "', '" +
              thisUser.getPortfolio() + "' "
              + ", '" + notifyBaseURL2 + "?dialogType=private&mode=r&idnum=" +
              myMessage.getidnum() + "' "
              + ", 'Quiz " + thisQuizName +
              " Response: " + thisUser.getRealName() + "')");
        }
      } // End of Dialog insert

    } catch (Exception ex) {
      plogger.report("Problem in jquiz");
      ex.printStackTrace();
    }
    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of input Quiz()

  public static String inputQuizResponse(portfolioUser thisUser,
                                         HttpServletRequest request) {
    StringBuffer sbuf = new StringBuffer();
    sbuf.append(jlib.topBox("Input Quiz Response"));

    String body = (String)request.getParameter("body");
    String quizID = (String)request.getParameter("qid");
    portfolioQuiz thisQuiz = new portfolioQuiz(quizID, "yes");

    try { // Do the database insert here
      if (body.length() > 4) {

        portfolioMessage myMessage = new portfolioMessage();
        String notifyBaseURL = "/jportfolio/servlet/jquiz";
        String notifyBaseURL2 = "/jportfolio/servlet/jdot3";
        String thisQuizName = thisQuiz.getQuizName();
        if (thisQuizName.length() > 5) {
          thisQuizName = (thisQuiz.getQuizName()).substring(0, 4);
        }

        ResultSet portHome =
            dbInterface.callDB("SELECT porthome "
                               + " from portfolios WHERE portfolio = '" +
                               thisUser.getPortfolio() + "' "
                               + " and  porthome != '/jportfolio/servlet/' ");
        if (portHome.next()) {
          notifyBaseURL = portHome.getString("porthome") + "/quiz/index.jsp";
          notifyBaseURL2 = portHome.getString("porthome") + ("/dialog/"
                                                             + "index.jsp");
        }

        ResultSet maxth = jlib.callDB(
            "select MAX (idnum)+1 as result "
            + " from dialog  WHERE idnum > 10000 and idnum < 20000");
        maxth.next();
        myMessage.setidnum(maxth.getString("result"));
        myMessage.setThreadID(maxth.getString("result"));

        myMessage.setSecurity("private");
        myMessage.setBody(body);
        myMessage.setSubject("Quiz " + thisQuizName + " Feedback");
        //	  myMessage.setReplyAuthor( ne);
        myMessage.setClassification("other");
        myMessage.setAuthor(thisUser.getUserID());
        myMessage.setAuthorName(thisUser.getRealName());
        myMessage.setPortfolio(thisUser.getPortfolio());
        myMessage.setGID(thisUser.getGroupID());
        myMessage.setLink(notifyBaseURL + "?qid=" + quizID + "&mode=p");

        myMessage.commitMessage();

        Vector myVect = thisUser.myPortfolio.getAdmins();
        for (int i = 0; i < myVect.size(); i++) {
          String thisUserID = (String)myVect.get(i);
          dbInterface.updateDB(
              "insert into notify(username, portfolio, "
              + " program, message) values('" + thisUserID + "', "
              + " '" + thisUser.getPortfolio() + "' , '" + notifyBaseURL2 +
              "?dialogType=private&mode=r&idnum=" + myMessage.getidnum() +
              "' "
              + ",'Post Quiz " + thisQuizName +
              " Response: " + thisUser.getRealName() + "')");
        }
      } // End of Dialog insert

    } catch (Exception ex) {
      plogger.report("Problem inserting quiz response post!");
      ex.printStackTrace();
    }

    sbuf.append(
        "<p>Your dialog post has been inserted in the database.  You "
        +
        " will be alerted on your main login screen if action is taken on your "
        + " post.");

    sbuf.append(jlib.botBox());
    return sbuf.toString();
  } // End of inputQuizResponse

  /**
   * Method for students to take a quiz
   *
   * @param userID String value for the current userID
   * @param portfolio String portfolio name value
   * @param quizID String value for the quiz ID number
   * @return HTML formated string for the quiz
   */
  public static String takeQuiz(portfolioUser thisUser, String quizID,
                                String thisPageURL) {

    StringBuffer sbuf = new StringBuffer();
    ResultSet rs = null;
    ResultSet rs2 = null;

    sbuf.append(jlib.topBox("Take Quiz:"));

    portfolioQuiz thisQuiz = new portfolioQuiz(quizID, "no");

    try {
      if (thisQuiz.isValid() && thisQuiz.isActive()) { // If so

        sbuf.append("<FORM METHOD='POST' ACTION='" + thisPageURL + "'>\n"
                    + "<input type='HIDDEN' name='qid' value='" + quizID +
                    "'>\n"
                    + "<input type='HIDDEN' name='mode' value='u'>\n");

        sbuf.append(thisQuiz.printQ1());
        sbuf.append(thisQuiz.printQ2());
        sbuf.append(thisQuiz.printQ3());

        sbuf.append(
            "<p><b>Quiz Feedback:</b><br>\n"
            +
            " <blockquote>If you wish, you can post a comment about this quiz."
            +
            " The post is saved within the dialog as a private post, that only"
            + " the instructor and yourself can view.  The instructor will be "
            + " notified of your comment. </blockquote></p>\n"

            + " <p><TEXTAREA NAME='body' WRAP='Virtual' ROWS=\"8\" COLS=\"50\">"
            + " </TEXTAREA>\n"

            + " <p>\n<input type='SUBMIT' value='Submit Answers'>\n"
            + " <input type='RESET'>\n</FORM>\n");
      } else {
        sbuf.append("<P>This quiz can not be taken now.");
      }
    } catch (Exception ex) {
      plogger.report("Trouble getting the quiz from DB.\n");
      ex.printStackTrace();
    }

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of takeQuiz()

  /**
   * Method that returns the HTML side bar that appears
   *
   * @param user value of the current user
   * @param portfolio value of the current portfolio
   * @param studentGrades reference to the students gradebook
   * @return HTML string
   */
  public static String sideBar(portfolioUser thisUser,
                               gradebook studentGrades) {
    StringBuffer theBuffer = new StringBuffer();

    // Building our page parts with some glue html code

    theBuffer.append(jlib.libPortApps());
    theBuffer.append("<P>\n");
    theBuffer.append(
        jlib.currentUsers(thisUser.getPortfolio(), thisUser.getUserID()));
    theBuffer.append("<P>\n");
    theBuffer.append(commands(thisUser));
    theBuffer.append("<P>\n");
    theBuffer.append(scoreBox(studentGrades));
    theBuffer.append("<P>\n");
    theBuffer.append(
        takableQuizzes(thisUser, studentGrades, true, thisPageURL));
    theBuffer.append("<P>\n");
    if (thisUser.isAdmin())
      theBuffer.append(jlib.adminCommands());

    return theBuffer.toString();
  } // End of makeBody()

  /**
   * Method to list out the 10 most recent Quizzes
   *
   * @param String value for the current userID val
   * @param String value for the current portfolio
   * @return HTML listing of quizzes
   */
  public static String listQuizzes(portfolioUser thisUser) {
    StringBuffer myBuffer = new StringBuffer();

    myBuffer.append(jlib.topBox("Last 3 Quizzes Taken:"));

    ResultSet rs = null;
    try {
      rs = jlib.callDB("SELECT quizes.qname, quizes.quiznum, "
                       + "getQuestionText(question1id) as q1,"
                       + " getQuestionText(question2id) as q2, "
                       + " getQuestionText(question3id) as q3 , "
                       + "quizattempts.entered from quizattempts, quizes WHERE "
                       + " quizattempts.portfolio = '" +
                       thisUser.getPortfolio() + "' and "
                       + " quizattempts.userID = '" + thisUser.getUserID() +
                       "' and quizes.quiznum = quizattempts.qid "
                       + " ORDER by entered DESC LIMIT 3");
      while (rs.next()) {
        myBuffer.append(
            "<P><a href=\"" + thisPageURL +
            "?mode=p&qid=" + rs.getString("quiznum") + "\">" +
            rs.getString("qname") + "</a><BR>\n"
            + " <blockquote>\n" +
            printOption("1", rs.getString("q1"), " ", "") + "<BR>\n" +
            printOption("2", rs.getString("q2"), " ", "") + "<BR>\n" +
            printOption("3", rs.getString("q3"), " ", "") + "<BR>\n"
            + " </blockquote>\n");
      }

    } catch (Exception ex) {
      System.err.println("Error caught in listQuizzes!");
    }
    myBuffer.append("</td></tr></table>\n");

    return myBuffer.toString();

  } // End of listQuizzes()

  /**
   * Method to print out a nice display of the quiz and what the student
   * answered
   *
   * @param String value for the current user
   * @param String value for the current portfolio ID
   * @param String value for the desired quiz
   * @return HTML string consisting of the quiz
   */
  public static String viewQuiz(portfolioUser thisUser, gradebook myGrades,
                                String qid) {
    StringBuffer sbuf = new StringBuffer();

    ResultSet rs = null;
    ResultSet rs2 = null;

    sbuf.append(jlib.topBox("Quiz Review:"));

    sbuf.append(
        "<blockquote><font color=\"green\">Here is a review of a past"
        + " quiz.  The answer in red \n"
        + " is the correct answer and the blinking letter is what you answered."
        + "  If you notice any \n"
        + " difference in your score and your answers listed below, "
        + " please contact me \n"
        + " (akrherz@iastate.edu).</font></blockquote>\n");

    portfolioQuiz thisQuiz = new portfolioQuiz(qid, "yes");

    try {
      if (thisQuiz.isValid() && thisQuiz.hasExpired()) {
        String quizScore =
            myGrades.getScoreElement("jquiz", qid, thisQuiz.getQuizName());

        sbuf.append("<P><B>Quiz:</B> " + thisQuiz.getQuizName() + "<BR>");
        sbuf.append("<B>Score:</B> " + quizScore + "<BR>");

        // Lets go and get the quiz attempt for this quiz
        ResultSet myQuizAttempt = jlib.callDB(
            "SELECT * from quizattempts "
            + " WHERE userid = '" + thisUser.getUserID() + "' "
            + " and portfolio = '" + thisUser.getPortfolio() + "' "
            + " and qid = " + qid + " ");
        myQuizAttempt.next();

        String q1Ans = myQuizAttempt.getString("question1");
        String q2Ans = myQuizAttempt.getString("question2");
        String q3Ans = myQuizAttempt.getString("question3");

        sbuf.append(thisQuiz.printResults(q1Ans, q2Ans, q3Ans));
      }
    } catch (Exception ex) {
      plogger.report("Problems verifing the quiz.");
      ex.printStackTrace();
    }

    sbuf.append(
        "<p><b>Post response:</b>  Perhaps you would like to "
        + " post a response to the grading of this quiz?  This post will be "
        + " make in the 'private level' dialog and your instructor will be "
        + " alerted of your post.\n");

    sbuf.append("<FORM METHOD='POST' ACTION='" + thisPageURL + "'>\n"
                + "<input type='HIDDEN' name='qid' value='" + qid + "'>\n"
                + "<input type='HIDDEN' name='mode' value='h'>\n");

    sbuf.append("<p><TEXTAREA NAME='body' WRAP='Virtual' ROWS=\"8\" "
                + "COLS=\"50\"></TEXTAREA>\n");

    sbuf.append("<br><input type=\"submit\" value=\"Post Message\">\n"
                + "</form>\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of viewQuiz

  public static String printOption(String qNum, String textVal, String answer,
                                   String myAnswerID) {
    StringBuffer sbuf = new StringBuffer();
    System.err.println("Question num -> " + qNum + "  Answer -> " + answer +
                       "  myAnswer -> " + myAnswerID + " \n");
    if (textVal != null && !textVal.equalsIgnoreCase("")) {
      sbuf.append("<BR><font ");
      if (answer.equalsIgnoreCase(qNum))
        sbuf.append("color=\"red\" ");
      sbuf.append("> ");
      if (myAnswerID.equalsIgnoreCase(qNum))
        sbuf.append("<blink>" + qNum + "</blink>." + textVal + "</font>\n");
      // sbuf.append(""+qNum+"."+ textVal +"</font>\n");
      else
        sbuf.append(qNum + "." + textVal + "</font>\n");
    }
    return sbuf.toString();
  }

  /**
   * method to sum up scores and print them out or something
   *
   * @return sends back HTML of the score box
   */
  public static String scoreBox(gradebook studentGrades) {
    String theResults;
    StringBuffer myBuffer = new StringBuffer();

    myBuffer.append(jlib.topBox("My Grades:"));

    myBuffer.append("<TABLE BORDER='0'>\n"
                    + "<TR><TD>Cumulative Points:</TD> <TD>" +
                    studentGrades.totalScores() + "</TD></TR>\n"
                    + "<TR><TD>Score on Quizzes:</TD> <TD>" +
                    studentGrades.appScores("jquiz") + "</TD></TR>\n"
                    + "</TABLE>\n");

    myBuffer.append(jlib.botBox());

    theResults = myBuffer.toString();
    return theResults;
  } // End of scoreBox()

  /**
   * Method to make any links that are needed for available quizzes.
   *
   * @param string value of which userID
   * @param string value of which is the current portfolio
   */
  public static String takableQuizzes(portfolioUser thisUser,
                                      gradebook studentGrades, boolean isShort,
                                      String thisPageURL) {

    StringBuffer sbuf = new StringBuffer();

    ResultSet rs = null;
    ResultSet rs2 = null;

    sbuf.append(jlib.topBox("Available Quizzes:"));

    sbuf.append("<TABLE width=\"100%\" rowspacing=\"0\" cellpadding=\"0\">\n");
    sbuf.append("<TR><TH>Quiz:</TH><TH>Att:</TH><TH>Score:</TH></TR>\n");

    String qID = null;
    String allowedAttempts = null;
    String myAttempts = null;
    String qName = null;
    String scoreName = null;

    rs = dbInterface.callDB("SELECT quiznum, qname, attempts from quizes"
                            + " WHERE portfolio = '" + thisUser.getPortfolio() +
                            "' " +
                            (" and startdate < CURRENT_TIMESTAMP and "
                             + "stopdate > CURRENT_TIMESTAMP "));

    if (rs != null) {
      try {

        // Loop over any quizes found...
        while (rs.next()) {
          qID = rs.getString("quiznum");
          allowedAttempts = rs.getString("attempts");
          qName = rs.getString("qname");
          scoreName = qName;
          if (isShort && qName.length() > 11)
            qName = qName.substring(0, 11) + "...";

          // Figure out how many times this student has attempted the quiz
          rs2 = jlib.callDB("SELECT attempt from quizattempts WHERE "
                            + " userid = '" + thisUser.getUserID() + "' "
                            + " and portfolio = '" + thisUser.getPortfolio() +
                            "' "
                            + " and qid = " + qID + " ");
          if (rs2.next()) {
            myAttempts = rs2.getString("attempt");
            if (java.lang.Integer.parseInt(myAttempts) ==
                -1) // Student got them all right!
              sbuf.append("<TR><TH>" + qName + "</TH><TD>Done</TD>\n");
            else if (java.lang.Integer.parseInt(allowedAttempts) == 0 ||
                     java.lang.Integer.parseInt(myAttempts) <
                         java.lang.Integer.parseInt(allowedAttempts))
              sbuf.append("<TR><TH><a href='" + thisPageURL +
                          "?mode=t&qid=" + qID + "'> " + qName + "</a></TH>\n"
                          + "<TD>" + myAttempts + " - " + allowedAttempts +
                          "</TD>\n");
            else
              sbuf.append("<TR><TH>" + qName + "</TH><TD>Done</TD>\n");
          } else {
            sbuf.append("<TR><TH><a href='" + thisPageURL +
                        "?mode=t&qid=" + qID + "'> " + qName + "</a></TH>"
                        + "<TD>0 - " + allowedAttempts + "</TD>\n");
          }
          rs2.close();
          sbuf.append("<TH>" +
                      studentGrades.getScoreElement("jquiz", qID, scoreName) +
                      "</TH></TR>\n");

        } // End of while()

      } catch (Exception ex) {
        plogger.report("Error caught trying to check for quizzes");
        ex.printStackTrace();
      } // End of try
    } // End of if

    sbuf.append("</TABLE>\n");
    sbuf.append("<P align=\"right\"><a href=\"" + thisPageURL +
                "?mode=h\">Help</a>\n");
    sbuf.append(jlib.botBox());

    return sbuf.toString();

  } // End of takableQuizzes

  public static String commands(portfolioUser thisUser) {
    String theResults;
    StringBuffer myBuffer = new StringBuffer();

    myBuffer.append(jlib.topBox("Commands:"));
    myBuffer.append("<LI><a class=\"commands\" href='" + thisPageURL +
                    "?mode=r'>Review all my quizes</a></LI>\n");
    myBuffer.append(jlib.botBox());

    theResults = myBuffer.toString();
    return theResults;
  } // End of commands()

  /**
   * Simple HTML printer for jquiz intro
   *
   * @return HTML string
   */
  public static String intro() {
    StringBuffer myBuffer = new StringBuffer();

    myBuffer.append(jlib.topBox("Introduction:"));
    myBuffer.append("This is the Quiz servlet for the Portfolio pacakge.  It "
                    + "is designed to allow "
                    + " for any to access interface for students and "
                    + "teachers to take and create quizzes."
                    + " Details on what it is and how it works are coming, "
                    + "but for now you will need to "
                    + " log in and see what you can see.");
    myBuffer.append(jlib.botBox());

    return myBuffer.toString();
  } // End of intro()

} // End of jQuiz()
