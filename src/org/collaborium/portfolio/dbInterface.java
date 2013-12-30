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
 * Class to handle the database connections.  Since this interface is only called once, the
 * servlet engine should share one database connection.  It was a problem that many database
 * connections where getting spawned.
 * @author Daryl Herzmann
 */

package org.collaborium.portfolio;

import java.sql.*;

public class dbInterface {
  static Connection db = null;
 
  public static final String url = settings.DBurl;
  public static final String usr = settings.DBusr;
  public static final String pwd = settings.DBpwd;

 /**
  * Method to set up the database connections
  */
  public static void dbInterfaceInit() {
     plogger.report("|||||||| I am initing dbInterface "+ url);
     try {
       Class.forName("org.postgresql.Driver");
     } catch(Exception ex) {
       plogger.report("Exception caught Class.forName().\n"+ex);
       ex.printStackTrace();
     }   
     try {
       db = DriverManager.getConnection(url, usr, pwd);
     } catch(Exception ex) {
       plogger.report("Exception caught opening db and st.\n"+ex);
       ex.printStackTrace();
     } 
  } // End of dbInterfaceInit

 /**
  * Method to return a result set for a query string
  *
  * @param String value for the query wanted.
  */
  public static ResultSet callDB(String querry) {
    if (db == null)
      dbInterfaceInit();

    ResultSet rs = null;
    try {
      plogger.report(querry +";");
      Statement st = db.createStatement(); 
      rs = st.executeQuery(querry);
    } catch(Exception ex) {
      plogger.report("Exception caught in callDB().\n"+ex);
      ex.printStackTrace();
    } 
    return rs;
  } // End of callDB()

 /**
  * Method to return a result set for a query string
  *
  * @param String value for the query wanted.
  */
  public static void updateDB(String querry) {
        
    try {
      plogger.report(querry +";");
      Statement st = db.createStatement();
      st.executeUpdate(querry);
    } catch(Exception ex) {
      plogger.report("Exception caught in callDB().\n"+ex);
      ex.printStackTrace();
    } 
  } // End of updateDB()

} // End of dbInterface
