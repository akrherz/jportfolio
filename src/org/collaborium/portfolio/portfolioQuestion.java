/**
 * Copyright 2001 Iowa State University
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
 *		portfolioQuestion
 * Container for portfolio Quiz Questions.
 *
 * @author Daryl Herzmann
 */

import java.sql.*;
import java.util.*;

public class portfolioQuestion {

  private String questionID = null;
  private String optiona = null;
  private String optionb = null;
  private String optionc = null;
  private String optiond = null;
  private String optione = null;
  private String optionf = null;
  private String optiong = null;
  private String optionh = null;
  private String question = null;
  private String answer = null;

  /**
   * Contructor for a portfolio Question
   */
  public portfolioQuestion(String thisQuestionID) {

    this.questionID = thisQuestionID;
    ResultSet rs = dbInterface.callDB("SELECT * from questions "
                                      + " WHERE qid = " + thisQuestionID + " ");
    try {
      if (rs.next()) {

        this.optiona = rs.getString("optiona");
        this.optionb = rs.getString("optionb");
        this.optionc = rs.getString("optionc");
        this.optiond = rs.getString("optiond");
        this.optione = rs.getString("optione");
        this.optionf = rs.getString("optionf");
        this.optiong = rs.getString("optiong");
        this.optionh = rs.getString("optionh");

        this.question = rs.getString("question");
        this.answer = rs.getString("answer");
      }
    } catch (Exception ex) {
      plogger.report("Problem");
    }
  } // End of portfolioQuestion()

  /**
   * Method to return the question text
   */
  public String getQuestion() { return this.question; }
  public String getAnswer() { return this.answer; }
  /**
   * Method to print out the answer select Box
   */
  public String checkBoxen(String questionSeq) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append(mkOption("a", questionSeq, "a", optiona));
    sbuf.append(mkOption("b", questionSeq, "b", optionb));
    sbuf.append(mkOption("c", questionSeq, "c", optionc));
    sbuf.append(mkOption("d", questionSeq, "d", optiond));
    sbuf.append(mkOption("e", questionSeq, "e", optione));
    sbuf.append(mkOption("f", questionSeq, "f", optionf));
    sbuf.append(mkOption("g", questionSeq, "g", optiong));
    sbuf.append(mkOption("h", questionSeq, "h", optionh));

    return sbuf.toString();
  } // End of selectBox()

  /**
   * Method to make an option for the quiz creatation
   *
   */
  public String mkOption(String num, String formName, String formVal,
                         String formSeen) {
    StringBuffer sbuf = new StringBuffer();
    //	System.err.println(formSeen.length() );
    if (!formSeen.equalsIgnoreCase("")) {
      sbuf.append("<BR>" + num + ". <input type='radio' name='question" +
                  formName + "ans' value='" + formVal + "'> " + formSeen +
                  "\n");
    }

    return sbuf.toString();
  }

  public String printOption(String qNum, String textVal, String myAnswerID) {
    StringBuffer sbuf = new StringBuffer();
    //	System.err.println("Question num -> "+qNum+"  Answer -> "+answer+"
    //myAnswer -> "+myAnswerID+" \n");
    if (textVal != null && !textVal.equalsIgnoreCase("")) {
      sbuf.append("<BR><font ");
      if (answer.equalsIgnoreCase(qNum))
        sbuf.append("color=\"red\" ");
      sbuf.append("> ");
      if (myAnswerID.equalsIgnoreCase(qNum))
        sbuf.append("**<blink>" + qNum + "</blink>**." + textVal + "</font>\n");
      // sbuf.append(""+qNum+"."+ textVal +"</font>\n");
      else
        sbuf.append(qNum + "." + textVal + "</font>\n");
    }
    return sbuf.toString();
  }

  public String printAnswer(String theirAnswer) {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append("<P><B>Question:</B> " + question + "<BR>\n");

    sbuf.append(printOption("a", optiona, theirAnswer));
    sbuf.append(printOption("b", optionb, theirAnswer));
    sbuf.append(printOption("c", optionc, theirAnswer));
    sbuf.append(printOption("d", optiond, theirAnswer));
    sbuf.append(printOption("e", optione, theirAnswer));
    sbuf.append(printOption("f", optionf, theirAnswer));
    sbuf.append(printOption("g", optiong, theirAnswer));
    sbuf.append(printOption("h", optionh, theirAnswer));
    return sbuf.toString();
  }
}
