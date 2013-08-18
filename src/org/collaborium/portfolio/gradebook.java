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
  * Container to modify a student's grades.
  *
  * @author Daryl Herzmann
  */

import java.io.*;
import java.util.*;
import java.lang.*;
import java.lang.String.*;
import java.sql.*;
import java.text.*;
import org.collaborium.portfolio.*;

public class gradebook{


  private portfolioUser thisUser = null;

 /**
  * Constructor to build the gradebook interface.
  * @param thisUser which is the current portfolio User
  *
  */
  public gradebook(portfolioUser thisUser) { 

  	this.thisUser = thisUser;
  }	

 /**
  * Method to total all the points for a particular user
  * 
  * @return sends back an int for the total score
  * @param user the users ID
  * @param classID the class ID
  */
  public String totalScores() {
	ResultSet rs = null;
	String myScore = "0";
	try{	
		rs = dbInterface.callDB("SELECT sum(score) as ssum from scores "
			+" WHERE userID = '"+ thisUser.getUserID() +"' "
			+" and portfolio = '"+ thisUser.getPortfolio() +"' ");
		if ( rs.next() ){
			myScore = rs.getString("ssum");
		}
		else {
		
		}
	} catch (Exception ex) {
		plogger.report("Error trying to total up the users scores.");
	}
	
	return myScore;
  }

 /**
  * Method to total Scores for a given app
  */
  public String appScores(String app) {
	ResultSet rs = null;
	String myScore = "0";
	try{	
		rs = dbInterface.callDB("SELECT sum(score) as ssum from scores "
			+" WHERE userID = '"+ thisUser.getUserID() +"' "
			+" and portfolio = '"+ thisUser.getPortfolio() +"' "
			+" and app = '"+app+"' ");
		if ( rs.next() ){
			myScore = rs.getString("ssum");
		}
		else {
		
		}
	} catch (Exception ex) {
		plogger.report("Error trying to total up the users scores.");
	}
	
	return myScore;
  } // End of appScores()
  
  
  public boolean addScoreElement(String app, String ID, String score) {
	dbInterface.updateDB("DELETE from scores WHERE "
		+" portfolio = '"+ thisUser.getPortfolio() +"' "
		+" and userID = '"+ thisUser.getUserID() +"' "
		+" and assign = '"+ID+"' and app = '"+app+"' ");
	dbInterface.updateDB("INSERT into scores"
		+" (portfolio, userID, assign, app, score) VALUES"
		+" ('"+ thisUser.getPortfolio()+"', "
		+" '"+ thisUser.getUserID() +"', '"+ID+"', '"+app+"', '"+score+"') ");
  
  	return true;
  } // End of addScoreElement()  
  
 /**
  * This method is for legacy support.  Back in the day, I did a stupid thing
  * and stored the quizName in the scores table instead of the qid.  The result
  * was bad.
  */
  public String getScoreElement(String app, String ID, String quizName) {
  	ResultSet rs = null;
  	String myScore = "-";
  	try{
  		rs = dbInterface.callDB("SELECT score from scores WHERE "
  			+" portfolio = '"+ thisUser.getPortfolio() +"' "
			+" and userID = '"+ thisUser.getUserID() +"' and "
			+" app = '"+app+"' and assign = '"+ID+"' ");
  		if ( rs.next() ) {
  			myScore = rs.getString("score");
  		} else {
			rs = dbInterface.callDB("SELECT score from scores WHERE "
  				+" portfolio = '"+ thisUser.getPortfolio() +"' "
				+" and userID = '"+ thisUser.getUserID() +"' and "
				+" app = '"+app+"' and assign = '"+quizName+"' ");
			if ( rs.next() ) {
				myScore = rs.getString("score");
			}
		}
  	} catch( Exception ex) {
  		plogger.report("Trouble in getScoreElement() ");
  	}
  
  	return myScore;
  } // End of getScoreElement()
  
  public String getScoreElement(String app, String ID) {
  	ResultSet rs = null;
  	String myScore = "-";
  	try{
  		rs = dbInterface.callDB("SELECT score from scores WHERE "
  			+" portfolio = '"+ thisUser.getPortfolio() +"' "
			+" and userID = '"+ thisUser.getUserID() +"' and "
			+" app = '"+app+"' and assign = '"+ID+"' ");
  		if (rs.next() ) {
  			myScore = rs.getString("score");
  		}
  	} catch( Exception ex) {
  		plogger.report("Trouble in getScoreElement() ");
  	}
  
  	return myScore;
  } // End of getScoreElement()
}  // End of gradebook()
