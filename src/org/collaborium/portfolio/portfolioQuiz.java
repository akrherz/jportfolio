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
 *		portfolioQuiz
 * Container for portfolio Quizes.
 *
 * @author Daryl Herzmann
 */
 
import java.util.*;
import java.sql.*;
import org.collaborium.portfolio.*;
import org.collaborium.util.*;


public class portfolioQuiz {

  private portfolioQuestion question1 = null;
  private String question1_id = null;
  private portfolioQuestion question2 = null;
  private String question2_id = null;
  private portfolioQuestion question3 = null;
  private String question3_id = null;
  
  private String quizName = null;
  private String quizID = null;
  private String attempts = null;
  private java.util.Date startDate = null;
  private java.util.Date endDate = null;

 /**
  * Constructor for a quiz.
  * @param thisQuizID, needed to find this quiz
  */
  public portfolioQuiz(String thisQuizID, String initQuestions ) {
  
  	this.quizID = thisQuizID ;
    if (thisQuizID == null || thisQuizID.equals("")) return;

  	ResultSet rs = dbInterface.callDB("SELECT * from quizes "
		+" WHERE quiznum = "+ thisQuizID +" ");
	try{
		if ( rs.next() ) {
		
			this.question1_id = rs.getString("question1");
			this.question2_id = rs.getString("question2");
			this.question3_id = rs.getString("question3");
			this.quizName = rs.getString("qname");
			this.attempts = rs.getString("attempts");
			this.startDate = stringUtils.dbDate2Date( rs.getString("startdate") );
			this.endDate = stringUtils.dbDate2Date( rs.getString("stopdate") );
			if ( initQuestions.equalsIgnoreCase("yes") ){
				question1 = new portfolioQuestion( question1_id );
				question2 = new portfolioQuestion( question2_id );
				question3 = new portfolioQuestion( question3_id );
			}
		
		} 
	} catch(Exception ex) {
		plogger.report("Problem");
		ex.printStackTrace();	
	}
  }

 /**
  * Method to print out a select box with the possible answers in it
  * @return HTML formated String
  */
  public String printQ1(){
  	StringBuffer sbuf = new StringBuffer();
  	if (question1 == null)
		question1 = new portfolioQuestion( question1_id );
	sbuf.append("<P><b>Question 1:</b> "+ question1.getQuestion() );
	sbuf.append( question1.checkBoxen("1") );
  
  	return sbuf.toString();
  }

/**
  * Method to print out a select box with the possible answers in it
  * @return HTML formated String
  */
  public String printQ2(){
  	StringBuffer sbuf = new StringBuffer();
  	if (question2 == null)
		question2 = new portfolioQuestion( question2_id );
	
	sbuf.append("<P><b>Question 2:</b> "+ question2.getQuestion() );
	sbuf.append( question2.checkBoxen("2") );
  
  	return sbuf.toString();
  }
 
 /**
  * Method to print out a select box with the possible answers in it
  * @return HTML formated String
  */
  public String printQ3(){
  	StringBuffer sbuf = new StringBuffer();
  	if (question3 == null)
		question3 = new portfolioQuestion( question3_id );
	sbuf.append("<P><b>Question 3:</b> "+ question3.getQuestion() );
	sbuf.append( question3.checkBoxen("3") );
  
  	return sbuf.toString();
  }

 /**
  * A method to tell if this quiz is a valid one or not
  */
  public boolean isValid() {
        if (quizID == null || quizID.equals("") ) return false;
  	if (quizName == null) return false;
	else return true;
  }
  
 /**
  * Method that returns if this quiz is currently available to take
  */
  public boolean isActive() {
  	java.util.Date now = new java.util.Date();
	if ( now.after( startDate ) && now.before( endDate ) ) return true;
	else return false;
  }

  public boolean hasExpired() {
	java.util.Date now = new java.util.Date();
	if ( now.after( endDate )) return true;
	else return false;
  }
  
 /**
  * Method to get the string value of the current quizName
  */
  public String getQuizName() {
  	return this.quizName ;
  }
  
  public String numberQuestions() {
  	if (question1 == null) return "0";
	if (question2 == null) return "1";
	if (question3 == null) return "2";
	return "3";
  
  }
  
  
 public boolean goodAttempt(String thisAttempt) {
 	if (thisAttempt == null) return true;
	
 	int thisA = new java.lang.Integer( thisAttempt ).intValue();
	int allowA = new java.lang.Integer( attempts ).intValue();
	
	if (thisA <= allowA) return true;
	else return false;
 
 }
  
 public String numberRight(String q1Ans, String q2Ans, String q3Ans) {
 	int numRight = 0;
	if (q1Ans.equals( question1.getAnswer() ) ) numRight = numRight + 1;
 	if (q2Ans.equals( question2.getAnswer() ) ) numRight = numRight + 1;
 	if (q3Ans.equals( question3.getAnswer() ) ) numRight = numRight + 1;

	String numberRight = new java.lang.Integer( numRight ).toString();
	return numberRight;
 }
  
 /**
  * Method that prints out how this student did on the quiz
  *
  */
  public String printResults(String theirQ1Ans, String theirQ2Ans, String theirQ3Ans) {
  	StringBuffer sbuf = new StringBuffer();
  
  	sbuf.append( question1.printAnswer( theirQ1Ans ) );
	sbuf.append( question2.printAnswer( theirQ2Ans ) );
	sbuf.append( question3.printAnswer( theirQ3Ans ) );
  
  	return sbuf.toString();
  } // End of printResults()
  
  
  
  

} // End of portfolioQuiz
