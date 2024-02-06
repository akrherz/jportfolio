/**
 * Copyright 2001-2007 Iowa State University
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
//
// JquizAdmin, administrative access to the DB system

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

public class jquizAdmin extends HttpServlet {

  String servletHttpBase = jlib.servletHttpBase;
  String thisPageURL = servletHttpBase + "/jquizAdmin";
  StringBuffer errors = new StringBuffer();

  String TITLE = "Quiz Administrator";

  /**
   * Standard init method for a servlet
   *
   * @param ServletConfig provided by the servlet engine...
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);

  } // End of init()

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    plogger.report("--- jquizAdmin Start \n");

    /** Set up for containers for HTML output */
    StringBuffer pageContent = new StringBuffer();
    StringBuffer sideContent = new StringBuffer();

    /** Set up Servlet Response */
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    HttpSession session = request.getSession(true);
    portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
    String callMethod = request.getParameter("mode");
    String quizID = request.getParameter("quizNum");

    if (thisUser == null) {
      response.setHeader("Refresh", "0; URL=./jportfolio/");
      callMethod = "z"; // Abort
    } else if (!thisUser.isAdmin()) {
      response.setHeader("Refresh", "0; URL=./jportfolio/");
      callMethod = "z"; // Abort
    } else {
      jlib.addUser(thisUser.getUserID(), "jquiz");
    }

    if (callMethod == null)
      callMethod = "x";

    /**
     * What our swith does
     * l | quiz results for a specific quiz
     * q | Post a new Question
     * u | Post a new Quiz
     */

    switch (callMethod.charAt(0)) {
    case 'l':
      pageContent.append(quizResults(thisUser, quizID));
      break;
    case 'q':

      try {
        pageContent.append(jlib.topBox("Post Results:"));
        pageContent.append(inputQuestion(thisUser, request));
        pageContent.append(jlib.botBox());
      } catch (myException ex) {
        System.err.println("Error caught trying to input Question.");
      }

      break;
    case 't':
      pageContent.append(editQuestionDialog(thisUser, request));
      break;
    case 'u':
      try {
        pageContent.append(inputQuiz(thisUser, request));
      } catch (myException ex) {
        ex.printStackTrace();
      }
      break;
    default:

      pageContent.append("Invalid Post!\n");

      break;
    }
    sideContent.append(adminSideBar(thisUser));

    out.println(jlib.header(thisUser, TITLE, "Blah"));

    out.println(jlib.makePage(sideContent.toString(), pageContent.toString()));
    out.println(jlib.footer());

    plogger.report("--- jquizAdmin Done \n");

  } // End of doPost()

  /**
   * Standard servlet method
   *
   * @request  HttpServlet request
   * @response HttpServlet response
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    plogger.report("--- jquizAdmin Start \n");

    /** Set up for containers for HTML output */
    StringBuffer pageContent = new StringBuffer();
    StringBuffer sideContent = new StringBuffer();

    /** Set up Servlet Response */
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    HttpSession session = request.getSession(true);
    portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
    String callMethod = request.getParameter("mode");
    String quizID = request.getParameter("quizNum");

    if (thisUser == null) {
      response.setHeader("Refresh", "0; URL=./jportfolio");
      callMethod = "z"; // Abort
    } else if (!thisUser.isAdmin()) {
      response.setHeader("Refresh", "0; URL=./jportfolio");
      callMethod = "z"; // Abort
    } else {
      jlib.addUser(thisUser.getUserID(), "jquiz");
    }

    if (callMethod == null)
      callMethod = "x";

    switch (callMethod.charAt(0))
    // d -> Print out default Admin Options
    // q -> Create a new Question
    // u -> Create a new Quiz
    // z -> Tell them to authenticate
    // t -> List out questions for one to edit
    {
    case 'l':
      pageContent.append(whichQuiz(thisUser));
      break;
    case 't':
      pageContent.append(listQuestions(thisUser));
      break;
    case 'd':

      pageContent.append(mkIntroduction());
      break;
    case 'q':

      pageContent.append(newQuestion());
      break;
    case 'u':

      pageContent.append(newQuiz(thisUser));
      break;
    case 'v':
      pageContent.append(jlib.topBox("Previously Created Quizzes:"));
      pageContent.append(listQuizzes(thisUser));
      pageContent.append(jlib.botBox());
      break;
    case 'z':
      pageContent.append(
          "You must be authenticated as Admin in order to log in.");
      break;
    } // End of switch()

    sideContent.append(adminSideBar(thisUser));

    out.println(jlib.header(thisUser, TITLE, "Blah"));

    out.println(jlib.makePage(sideContent.toString(), pageContent.toString()));
    out.println(jlib.footer());

    plogger.report("--- jquiz end");

  } // End of doGet()

  /**
   * Method to prompt for which quiz
   *
   */
  public String whichQuiz(portfolioUser thisUser) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Which Quiz?"));

    sbuf.append("<blockquote><font color=\"green\">Please choose the quiz "
                + " that you would like the \n"
                + " results for.</font></blockquote>\n");

    sbuf.append("<FORM method=\"POST\" action=\"" + thisPageURL +
                "\" name=\"quizNum\">\n"
                + "<input type=\"hidden\" name=\"mode\" value=\"l\">\n"
                + "<SELECT name=\"quizNum\">\n");

    try {
      ResultSet rs =
          jlib.callDB("SELECT * from quizes WHERE "
                      + " portfolio = '" + thisUser.getPortfolio() + "' ");
      while (rs.next()) {
        String qid = rs.getString("quiznum");
        String qname = rs.getString("qname");
        sbuf.append("<OPTION VALUE=\"" + qid + "\">" + qname + "\n");
      }

    } catch (Exception ex) {
      System.err.println("Problem generating the list of quizzes!");
    }
    sbuf.append("</SELECT>\n");

    sbuf.append("<BR><input type=\"SUBMIT\" value=\"Select Quiz\"></form>\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of whichQuiz

  /**
   * Method to Print the Results of a Quiz
   *
   * @param portfolio String value for the current portfolio
   * @param quizName String value for the  quiz we are interested in...
   */
  public String quizResults(portfolioUser thisUser, String quizName) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Quiz Results"));

    sbuf.append(
        "<blockquote><font color=\"green\">Here are listed the quiz results.</font></blockquote>\n");

    sbuf.append("<H3>Quiz: " + quizName + "\n");

    try {

      ResultSet rs = jlib.callDB(
          "SELECT score, getUserName(userid) as name from scores s "
          + " WHERE s.portfolio = '" + thisUser.getPortfolio() + "' "
          + " and s.assign = '" + quizName + "' ");
      ResultSet rs2 = jlib.callDB(
          "SELECT AVG(score) as average_score from scores s "
          + " WHERE s.portfolio = '" + thisUser.getPortfolio() + "' "
          + " and s.assign = '" + quizName + "' ");
      rs2.next();
      sbuf.append("<P>Average Score: " + rs2.getString("average_score"));
      sbuf.append("<P><TABLE>\n");
      while (rs.next()) {
        sbuf.append("<TR><TD>" + rs.getString("name") + "</TD>"
                    + " <TD>" + rs.getString("score") + "</TD></TR>\n");
      }

    } catch (Exception ex) {
      System.err.println("Problem generating the list of quizzes!");
    }
    sbuf.append("</TABLE>\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of whichQuiz

  public String listQuestions(portfolioUser thisUser) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Which Question?"));

    sbuf.append(
        "<P><blockquote><font color=\"green\">With this dialog you are able to edit a question that\n"
        +
        " resides in the database.  Be careful to edit only questions that have yet to be asked for there is\n"
        +
        " no mechanism to change what users may have previously used.</font></blockquote>\n");

    sbuf.append("<FORM METHOD=\"POST\" ACTION=\"" + thisPageURL +
                "\" name=\"EDIT\">\n"
                + "<input type=\"hidden\" name=\"mode\" value=\"t\">\n");

    sbuf.append("<P>Select Question:<BR>\n" + listQuestions(thisUser, "1"));

    sbuf.append("<P><input type=\"SUBMIT\" value=\"Edit Question\">\n");
    sbuf.append("</FORM>\n");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  }

  public String editQuestionDialog(portfolioUser thisUser,
                                   HttpServletRequest req) {
    StringBuffer sbuf = new StringBuffer();

    String qid = (String)req.getParameter("question1");

    sbuf.append(jlib.topBox("Edit Question?"));

    try {
      ResultSet rs =
          jlib.callDB("SELECT * from questions "
                      + " WHERE qid = '" + qid + "' and portfolio = '" +
                      thisUser.getPortfolio() + "' ");

      if (rs.next()) {
        String question = jlib.cleanString(rs.getString("question"));
        String optiona = jlib.cleanString(rs.getString("optiona"));
        String optionb = jlib.cleanString(rs.getString("optionb"));
        String optionc = jlib.cleanString(rs.getString("optionc"));
        String optiond = jlib.cleanString(rs.getString("optiond"));
        String optione = jlib.cleanString(rs.getString("optione"));
        String optionf = jlib.cleanString(rs.getString("optionf"));
        String optiong = jlib.cleanString(rs.getString("optiong"));
        String optionh = jlib.cleanString(rs.getString("optionh"));
        String answer = rs.getString("answer");

        sbuf.append(
            "<form method='POST' action='" + thisPageURL + "'>\n"
            + " <input type='hidden' value='q' name='mode'>\n"
            + " <input type='hidden' value=\"" + qid + "\" name=\"qid\">\n"
            + " <TABLE BORDER=0>\n"
            + " <TR><TD></TD><TD>Is Answer?</TD></TR>"
            + " <TR><TH align='left'>Enter Question:<BR>\n"
            +
            "		<input type='text' size='60' name='question' value=\"" +
            question + "\"></TD>\n"
            + "<TD></TD>"
            + " </TR>\n\n" + mkAnswerOption("a", optiona, answer) +
            mkAnswerOption("b", optionb, answer) +
            mkAnswerOption("c", optionc, answer) +
            mkAnswerOption("d", optiond, answer) +
            mkAnswerOption("e", optione, answer) +
            mkAnswerOption("f", optionf, answer) +
            mkAnswerOption("g", optiong, answer) +
            mkAnswerOption("h", optionh, answer) + " </TABLE>\n"
            + " <input type='SUBMIT' VALUE='CHANGE QUESTION'>\n"
            + " <input type='RESET'>\n"
            + " </form>");

      } else {
        sbuf.append("Uh-oh");
      }
    } catch (Exception ex) {
      plogger.report("Errr");
      sbuf.append("<b>Error.</b> Could not access questionID: '" + qid + " "
                  + "<br>Perhaps this portfolio does not own the question!");
    }

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  }

  /**
   * Method to save the newly created question into the DB
   *
   * @param req servlet request which hopefully has all of the needed
   * @param String value for which is the current portfolio
   */
  public String inputQuestion(portfolioUser thisUser, HttpServletRequest req)
      throws myException {
    StringBuffer sbuf = new StringBuffer();

    String question =
        stringUtils.cleanString((String)req.getParameter("question"));
    String answer = (String)req.getParameter("answer");
    String qid = (String)req.getParameter("qid");
    String optiona =
        stringUtils.cleanString((String)req.getParameter("optiona"));
    String optionb =
        stringUtils.cleanString((String)req.getParameter("optionb"));
    String optionc =
        stringUtils.cleanString((String)req.getParameter("optionc"));
    String optiond =
        stringUtils.cleanString((String)req.getParameter("optiond"));
    String optione =
        stringUtils.cleanString((String)req.getParameter("optione"));
    String optionf =
        stringUtils.cleanString((String)req.getParameter("optionf"));
    String optiong =
        stringUtils.cleanString((String)req.getParameter("optiong"));
    String optionh =
        stringUtils.cleanString((String)req.getParameter("optionh"));
    if (optiona == null)
      optiona = "";
    if (optionb == null)
      optionb = "";
    if (optionc == null)
      optionc = "";
    if (optiond == null)
      optiond = "";
    if (optione == null)
      optione = "";
    if (optionf == null)
      optionf = "";
    if (optiong == null)
      optiong = "";
    if (optionh == null)
      optionh = "";

    if (qid != null) {
      try {
        jlib.updateDB("UPDATE questions SET question = '" + question + "', "
                      + " optiona = '" + optiona + "', "
                      + " optionb = '" + optionb + "', "
                      + " optionc = '" + optionc + "', "
                      + " optiond = '" + optiond + "', "
                      + " optione = '" + optione + "', "
                      + " optionf = '" + optionf + "', "
                      + " optiong = '" + optiong + "', "
                      + " optionh = '" + optionh + "', answer = '" + answer +
                      "' WHERE qid = " + qid + " ");

      } catch (Exception ex) {
      }
    } else {
      try {
        jlib.updateDB(
            "INSERT into questions(portfolio, question, optiona, optionb, optionc, optiond,"
            + " optione, optionf, optiong, optionh, answer)"
            + " VALUES ('" + thisUser.getPortfolio() + "', '" + question +
            "', '" + optiona + "' , '" + optionb + "', '" + optionc + "', "
            + " '" + optiond + "', '" + optione + "', '" + optionf + "', '" +
            optiong + "', '" + optionh + "', '" + answer + "') ");

      } catch (Exception ex) {
        System.err.println("Error caught trying to enter Q.");
        errors.append("\n** Problem entering the new Question");
      }
    }

    sbuf.append("Input Question was successful!");

    return sbuf.toString();
  } // End of inputQuestion()

  /**
   * Method to input the Quiz into the database
   * @param thisUser which is the portfolioUser container
   * @param req which is the HTTP request.
   * @return HTML formated string
   */
  public String inputQuiz(portfolioUser thisUser, HttpServletRequest req)
      throws myException {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Quiz Creation Results:"));

    String qName = stringUtils.cleanString((String)req.getParameter("qName"));
    String startDate = (String)req.getParameter("startDate");
    String endDate = (String)req.getParameter("endDate");
    String question1 = (String)req.getParameter("question1");
    String question2 = (String)req.getParameter("question2");
    String question3 = (String)req.getParameter("question3");
    String attempt = (String)req.getParameter("attempt");
    String topicid = (String)req.getParameter("topicid");
    if (qName.equalsIgnoreCase(""))
      qName = "Unnamed Quiz";
    if (question1.equalsIgnoreCase("null"))
      question1 = "0";
    if (question2.equalsIgnoreCase("null"))
      question2 = "0";
    if (question3.equalsIgnoreCase("null"))
      question3 = "0";
    if (attempt.equalsIgnoreCase(""))
      attempt = "0";
    if (topicid == null)
      topicid = "0";

    /** Know we need to figure out where calendar links should point */
    String notifyBaseURL = "/jportfolio/servlet/jquiz";

    /* We need to get the porthome value for use in notifications */
    try {
      ResultSet portHome = dbInterface.callDB(
          "SELECT porthome from portfolios "
          + " WHERE portfolio = '" + thisUser.getPortfolio() + "' and "
          + " porthome != '/jportfolio/servlet/' ");
      if (portHome.next()) {
        // notifyBaseURL = portHome.getString("porthome") +"/quiz/index.jsp";
      }
    } catch (Exception ex) {
      plogger.report("Error in jquizAdmin");
    }

    try {
      // We can't have duplicate quiz names yet, since I messed up on the scores
      // table.
      ResultSet tester =
          jlib.callDB("SELECT quiznum from quizes "
                      + " WHERE qname = '" + qName + "' and portfolio = '" +
                      thisUser.getPortfolio() + "'");
      if (!tester.next()) {

        // Create Quiz Entry
        jlib.updateDB(
            "INSERT into quizes(qname, portfolio, question1, question2, "
            + " question3, startDate, stopDate, attempts, topicid)"
            + " VALUES ('" + qName + "', '" + thisUser.getPortfolio() + "', " +
            question1 + ", '" + question2 + "' ,"
            + "  " + question3 + ", '" + startDate + "', '" + endDate + "', " +
            attempt + ", '" + topicid + "') ");
        // Figure out what quiznum this quiz was assigned
        ResultSet test2 =
            jlib.callDB("SELECT quiznum from quizes WHERE "
                        + " qname = '" + qName + "' and portfolio = '" +
                        thisUser.getPortfolio() + "'");
        test2.next();
        String thisQuizNum = test2.getString("quiznum");

        // Create Calendar Entries
        jlib.updateDB(
            "INSERT into calendar(valid, portfolio, description, url) VALUES "
            + " ('" + startDate + "', '" + thisUser.getPortfolio() +
            "', 'Quiz " + qName + " Available', "
            + " '" + notifyBaseURL + "?mode=t&qid=" + thisQuizNum + "')");
        jlib.updateDB(
            "INSERT into calendar(valid, portfolio, description) VALUES "
            + " ('" + endDate + "', '" + thisUser.getPortfolio() + "', 'Quiz " +
            qName + " Expires')");

        sbuf.append("Input Quiz was successful!");
      } else {
        sbuf.append(
            "<P>A quiz by this name allready exists, please use a different name.\n");
      }

    } catch (Exception ex) {
      System.err.println("Error caught trying to enter Q.");
    }

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of inputQuestion()

  public String mkIntroduction() {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Jquiz Admin Instructions:"));

    sbuf.append(
        "<P>The purpose of jquizAdmin is to allow instructor the ability to edit and manipulate quizzes for the portfolio "
        +
        " environment.\n The basic features allready work for this servlet, I just need to keep things up quite a bit and do some"
        + " more documentation.");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  }

  /**
   * Method that prints out dialog to create a New Quiz
   *
   * @param String value for the portfolio
   */
  public String newQuiz(portfolioUser thisUser) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Create a new Quiz:"));

    sbuf.append(
        "<blockquote><font color=\"green\">This dialog also you to combine previously created questions into\n"
        +
        " a takable quiz. Please make sure to fill in the indentifier section, so that it appears correctly.</font>\n"
        + " </blockquote><BR>\n");

    sbuf.append(
        "<form method='POST' action='" + thisPageURL + "'>\n"
        + " <input type='hidden' name='mode' value='u'>\n"

        + " <P><B>Assign this quiz an identifier:</B><blink> <- </blink><BR>\n"
        + " (<i>The quiz identifier appears on the calendar.</i>) "
        + " <input type='text' name='qName' size='30'>\n");

    sbuf.append("<P><b>Which Unit does this quiz belong to?</b><br>\n");

    sbuf.append(jlib.topicsSelect(thisUser, "0"));

    sbuf.append(
        " <P>Select Question 1:<BR>\n" + listQuestions(thisUser, "1")

        + " <P>Select Question 2:<BR>\n" + listQuestions(thisUser, "2")

        + " <P>Select Question 3:<BR>\n" + listQuestions(thisUser, "3")

        + " <P>Quiz Time Limits:<BR>\n"
        + " <blockquote>Format: YYYY-MM-DD HH:MM:SS </blockquote>\n"

        + " <P>Start Date:<BR>\n"
        + " 	<input type='text' name='startDate'>\n"

        + " <P>End Date:<BR>\n"
        + "	<input type='text' name='endDate'>\n"

        + " <P>Enter Allowable Quiz Attempts<BR> (Enter 0 for unlimited):<BR>\n"
        + " 	<input type='text' MAXLENGTH='1' size='2' name='attempt'>\n"

        + " <CENTER>\n"
        + " <input type='SUBMIT' value='Create Quiz'>\n"
        + " <input type='RESET'>\n"
        + " </CENTER>\n"
        + " </form>\n");

    sbuf.append("<P><B>Allready Created Quizzes:</B><BR>" +
                listQuizzes(thisUser));

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of newQuiz()

  public String listQuizzes(portfolioUser thisUser) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append("<TABLE width=\"100%\">\n");
    sbuf.append(
        "<TR><TH>Quiz Name:</TH><TH>Start Date:</TH><TH>End Date:</TH></TR>\n");
    ResultSet rs = null;
    try {
      rs = jlib.callDB("SELECT qname, startdate, stopdate from quizes "
                       + " WHERE portfolio = '" + thisUser.getPortfolio() +
                       "' ");
      while (rs.next()) {

        sbuf.append("<TR><TD>" + rs.getString("qname") + "</TD>\n"
                    + "<TD>" + rs.getString("startdate") + "</TD>\n"
                    + "<TD>" + rs.getString("stopdate") + "</TD>\n"
                    + "</TR>\n");
      }

    } catch (Exception ex) {
      System.err.println("Error generating Quizzes.");
      ex.printStackTrace();
    }

    sbuf.append("</TABLE>\n");

    return sbuf.toString();
  } // End of listQuestions()

  /**
   * Method to create select box for all available questions
   * @param thisUser which is the portfolioUser container
   * @param id which is the id of this particular question
   * @return HTML formated string of questions in a select box
   */
  public String listQuestions(portfolioUser thisUser, String id) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append("<SELECT name='question" + id + "' size=\"10\">\n");
    sbuf.append("<option value='null'>No question\n");
    ResultSet rs = null;
    try {
      rs = jlib.callDB("SELECT qid, question from questions "
                       + " WHERE portfolio IN " +
                       thisUser.myPortfolio.getPList() + " "
                       + " ORDER by question");
      while (rs.next()) {
        try {
          sbuf.append("<option value='" + rs.getString("qid") + "'>" +
                      rs.getString("question").substring(0, 70) + "\n");
        } catch (Exception ex) {
          sbuf.append("<option value='" + rs.getString("qid") + "'>" +
                      rs.getString("question") + "\n");
        }
      }

    } catch (Exception ex) {
      System.err.println("Error generating Questions.");
      ex.printStackTrace();
    }

    sbuf.append("</SELECT>\n");

    return sbuf.toString();
  } // End of listQuestions()

  public String adminSideBar(portfolioUser thisUser) {
    StringBuffer theBuffer = new StringBuffer();

    theBuffer.append(jlib.libPortApps());
    theBuffer.append("<P>");
    theBuffer.append(
        jlib.currentUsers(thisUser.getPortfolio(), thisUser.getUserID()));
    theBuffer.append("<P>");
    theBuffer.append(adminCommands());
    theBuffer.append("<P>");
    theBuffer.append(jlib.adminCommands());

    return theBuffer.toString();
  } // End of adminSideBar()

  /**
   * Method to create login Box
   *
   * @return HTML formated String
   */
  public String adminCommands() {
    StringBuffer myBuffer = new StringBuffer();

    myBuffer.append(jlib.topBox("Admin Commands:"));

    myBuffer.append("<ul><li><a class=\"commands\" href='" + thisPageURL +
                    "?mode=q'>New Question</a></li>\n"
                    + "   <li><a class=\"commands\" href='" + thisPageURL +
                    "?mode=t'>Edit Question</a></li>\n"
                    + "   <li><a class=\"commands\" href='" + thisPageURL +
                    "?mode=u'>Create A Quiz</a></li>\n"
                    + "   <li><a class=\"commands\" href='" + thisPageURL +
                    "?mode=v'>List Quizzes</a></li>\n"
                    + "   <li><a class=\"commands\" href='" + thisPageURL +
                    "?mode=l'>Quiz Results</a></li></ul>");

    myBuffer.append(jlib.botBox());

    return myBuffer.toString();
  } // End of adminCommands

  /**
   * Returns a form to input a new question
   *
   * @return String HTML form
   */
  public String newQuestion() {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(jlib.topBox("Create a new Question:"));

    sbuf.append(
        "<form method='POST' action='" + thisPageURL + "'>\n"
        + " <input type='hidden' value='q' name='mode'>\n"
        + " <TABLE BORDER=0>\n"
        + " <TR><TD></TD><TD>Is Answer?</TD></TR>"
        + " <TR><TH align='left'>Enter Question:<BR>\n"
        + "		<input type='text' size='60' name='question'></TD>\n"
        + "<TD></TD>"
        + " </TR>\n\n" + mkAnswerOption("a", "", "") +
        mkAnswerOption("b", "", "") + mkAnswerOption("c", "", "") +
        mkAnswerOption("d", "", "") + mkAnswerOption("e", "", "") +
        mkAnswerOption("f", "", "") + mkAnswerOption("g", "", "") +
        mkAnswerOption("h", "", "") + " </TABLE>\n"
        + " <input type='SUBMIT' VALUE='CREATE QUESTION'>\n"
        + " <input type='RESET'>\n"
        + " </form>");

    sbuf.append(jlib.botBox());

    return sbuf.toString();
  } // End of newQuestion()

  public String mkAnswerOption(String optionAlpha, String optionVal,
                               String answer) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append("\n<TR><TH align=\"left\">Option " + optionAlpha + ":<BR>\n"
                + "<input type=\"text\" size=\"60\" name=\"option" +
                optionAlpha + "\" value=\"" + optionVal + "\"></TH>\n");
    sbuf.append("<TD><input type='radio' name='answer' value='" + optionAlpha +
                "' ");
    if (optionAlpha.equalsIgnoreCase(answer)) {
      sbuf.append("CHECKED");
    }
    sbuf.append(">" + optionAlpha + "</TD>"
                + " </TR>\n\n");

    return sbuf.toString();
  }

} // End of jquizAdmin()
