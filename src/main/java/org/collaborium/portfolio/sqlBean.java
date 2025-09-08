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
//
//  Simple bean to do my SQL calls for me
//
//

package org.collaborium.portfolio;

import java.sql.*;

public class sqlBean {

  public sqlBean() {}

  public ResultSet doSQL(String querry) {

    //    Connection db;

    //    Statement st       = null;
    //    String url         = "jdbc:postgresql:portfolio";
    //    String usr         = "nobody";
    //    String pwd         = "NULL";
    //    ResultSet rs       = null;

    //    try {
    //    	    Class.forName("postgresql.Driver");
    //    }
    //    catch(Exception ex) {
    //    	    System.err.println("Exception caught
    //    Class.forName().\n"+ex);
    //   	    ex.printStackTrace();
    //    }

    //    try {
    //    	    db = DriverManager.getConnection(url, usr, pwd );
    //    	    st = db.createStatement();
    //    }
    //    catch(Exception ex) {
    //    	    System.err.println("Exception caught opening db and
    //    st.\n"+ex); 	    ex.printStackTrace();
    //    }

    //    try {
    //    	rs = st.executeQuery(querry);
    //    }
    //    catch(Exception ex) {
    //    	    System.err.println("Exception caught talking to
    //    database.\n"+ex); 	    ex.printStackTrace();
    //    }

    ResultSet rs = dbInterface.callDB(querry);

    return rs;
  }
}
