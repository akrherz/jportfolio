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

import java.sql.*;
import java.util.*;
import java.util.Date;

public class calBean {

  private static calBean instance = new calBean();

  public static calBean getInstance(){
	return instance;
  }

  public calBean() {
  }

  static final int FEBRUARY = 1;   /* special month during leap years */
  
  // Date object to get current month and year.
  Date now = new Date();

  static String days[] = {"Sunday", "Monday", "Tuesday", "Wednesday",
                   "Thursday", "Friday", "Saturday"};

  static String months[] = {"January", "February", "March", "April",
                     "May", "June", "July", "August", "September",
                     "October", "November", "December"};

  static int DaysInMonth[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

  // Month and year entered by user.
  int userMonth = 1;
  int userYear = 2000;

  // simple access for number of days...
  public int numDays(int month) {
     return DaysInMonth[month];
  }

  public int CalcFirstOfMonth(int year, int month)
    {
    int firstDay;       /* day of week for Jan 1, then first day of month */
    int i;              /* to traverse months before given month */
  
    /* Start at 1582, when modern calendar starts. */
    if (year < 1582) return (-1);
  
    /* Catch month out of range. */
    if ((month < 0) || (month > 11)) return (-1);
  
    /* Get day of week for Jan 1 of given year. */
    firstDay = CalcJanuaryFirst(year);
  
    /* Increase firstDay by days in year before given month to get first day
     * of month.
     */
    for (i = 0; i < month; i++)
      firstDay += DaysInMonth[i];
  
    /* Increase by one if month after February and leap year. */
    if ((month > FEBRUARY) && IsLeapYear(year)) firstDay++;
  
    /* Convert to day of the week and return. */
    return (firstDay % 7);
    } // CalcFirstOfMonth
  
  public boolean IsLeapYear(int year)
    {
  
    /* If multiple of 100, leap year if multiple of 400. */
    if ((year % 100) == 0) return((year % 400) == 0);
  
    /* Otherwise leap year if multiple of 4. */
    return ((year % 4) == 0);
    } // IsLeapYear
  
  int CalcJanuaryFirst(int year)
    {
    /* Start at 1582, when modern calendar starts. */
    if (year < 1582) return (-1);
  
    /* Start Fri 01-01-1582; advance a day for each year, 2 for leap yrs. */
    return ((5 + (year - 1582) + CalcLeapYears(year)) % 7);
    } 
  
  int CalcLeapYears(int year)
    {
    int leapYears;      /* number of leap years to return */
    int hundreds;       /* number of years multiple of a hundred */
    int fourHundreds;   /* number of years multiple of four hundred */
  
    /* Start at 1582, when modern calendar starts. */
    if (year < 1582) return (-1);
  
    /* Calculate number of years in interval that are a multiple of 4. */
    leapYears = (year - 1581) / 4;
  
    /* Calculate number of years in interval that are a multiple of 100;
     * subtract, since they are not leap years.
     */
    hundreds = (year - 1501) / 100;
    leapYears -= hundreds;
  
    /* Calculate number of years in interval that are a multiple of 400;
     * add back in, since they are still leap years.
     */
    fourHundreds = (year - 1201) / 400;
    leapYears += fourHundreds;
  
    return (leapYears);
    } 
    
    

  public String calTable(portfolioUser thisUser, Calendar cal) {
    if (thisUser == null) return "";

    StringBuffer myBuffer = new StringBuffer();
    
    int numBox = 0;
    
    String portfolio = thisUser.getPortfolio();
    int offSet = CalcFirstOfMonth(cal.get(cal.YEAR), cal.get(cal.MONTH));
    int maxDays = cal.getActualMaximum(cal.DAY_OF_MONTH);
    
    int currentMonth = cal.get(cal.MONTH);
    int currentYear = cal.get(cal.YEAR);
    
    myBuffer.append("<table width=\"100%\" border=\"1\">");
    
    // lable days..
    myBuffer.append("<tr><td>Sunday</td><td>Monday</td><td>Tuesday</td><td>Wednesday</td>");
    myBuffer.append("<td>Thursday</td><td>Friday</td><td>Saturday</td></tr>");
    
    for (int i=0; i < 6; i++) {
      myBuffer.append("<tr>");
      for (int j=0; j < 7; j++) {
        myBuffer.append("<td valign=\"top\" align=\"left\" height=\"100\" width=\"125\" >");
        
	//myBuffer.append("hello");
	numBox++;
	if (numBox > offSet && numBox < (maxDays + offSet + 1)) { 
	  myBuffer.append((numBox - offSet));  // put in the day of the week
	  
	  ResultSet myRS = dbInterface.callDB("select * from calendar where portfolio = '"+portfolio+"' and date(valid) = '"+currentYear+" "+(currentMonth+1)+" "+(numBox - offSet)+"'");
	  
	  try {
		while( myRS.next() ) {
			String url = myRS.getString("url");
			System.err.println( url );
			if ( url == null || url == "" || url.length() < 2 )
				myBuffer.append("<br>"+ myRS.getString("description")+"\n");	
			else
				myBuffer.append("<br><a href='"+myRS.getString("url")+"'>"+ myRS.getString("description")+"</a>");
		
		}
		myRS.close();
	  }
	  catch(Exception ex) {
            System.err.println("Exception caught opening db and st.\n"+ex);
            ex.printStackTrace();
          }
	}
	else {
	  myBuffer.append("&nbsp;");
	}
	myBuffer.append("</td>\n");
      }	
      myBuffer.append("</tr>");
    }     
    myBuffer.append("</table>");
      
    return myBuffer.toString();
    
  }  
 
  
} 
