/**
 * Copyright 2001-2005 Iowa State University jportfolio@collaborium.org
 *
 * <p>This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * <p>This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * <p>You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307 USA
 */
/**
 * Class to handle the database connections. Since this interface is only called once, the servlet
 * engine should share one database connection. It was a problem that many database connections
 * where getting spawned.
 *
 * @author Daryl Herzmann
 */
package org.collaborium.portfolio;

import java.sql.*;

public class dbInterface {
  static Connection db = null;

  public static final String url = settings.DBurl;
  public static final String usr = settings.DBusr;
  public static final String pwd = settings.DBpwd;

  /** Method to set up the database connections */
  public static void dbInterfaceInit() {
    plogger.report("|||||||| I am initing dbInterface " + url);
    try {
      Class.forName("org.postgresql.Driver");
    } catch (Exception ex) {
      plogger.report("Exception caught Class.forName().\n" + ex);
      ex.printStackTrace();
    }
    try {
      // If password is omitted, let the JDBC driver use its password lookup flow
      // (for example passfile/.pgpass support where configured).
      if (pwd == null || pwd.trim().isEmpty() || "NULL".equalsIgnoreCase(pwd.trim())) {
        db = DriverManager.getConnection(url, usr, null);
      } else {
        db = DriverManager.getConnection(url, usr, pwd);
      }
    } catch (Exception ex) {
      plogger.report("Exception caught opening db and st.\n" + ex);
      ex.printStackTrace();
    }
  } // End of dbInterfaceInit

  /**
   * Run a SELECT using bind parameters. Values must never be concatenated into the query; pass them
   * as params and use ? placeholders.
   *
   * @param query SQL with ? placeholders
   * @param params values bound to the placeholders, in order
   * @return the result set, or null on error
   */
  public static ResultSet callDB(String query, Object... params) {
    if (db == null) dbInterfaceInit();

    ResultSet rs = null;
    try {
      plogger.report(query + ";");
      PreparedStatement pst =
          db.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      bind(pst, params);
      rs = pst.executeQuery();
    } catch (Exception ex) {
      plogger.report("Exception caught in callDB().\n" + ex);
      ex.printStackTrace();
      dbInterfaceInit();
    }
    return rs;
  } // End of callDB()

  /**
   * Run an INSERT/UPDATE/DELETE using bind parameters, logging and swallowing any error.
   *
   * @param query SQL with ? placeholders
   * @param params values bound to the placeholders, in order
   */
  public static void updateDB(String query, Object... params) {
    try {
      updateDBOrThrow(query, params);
    } catch (Exception ex) {
      plogger.report("Exception caught in updateDB().\n" + ex);
      ex.printStackTrace();
      dbInterfaceInit();
    }
  } // End of updateDB()

  /** Like updateDB(), but lets the caller handle the SQLException. */
  public static void updateDBOrThrow(String query, Object... params) throws SQLException {
    if (db == null) dbInterfaceInit();

    plogger.report(query + ";");
    try (PreparedStatement pst = db.prepareStatement(query)) {
      bind(pst, params);
      pst.executeUpdate();
    }
  } // End of updateDBOrThrow()

  private static void bind(PreparedStatement pst, Object[] params) throws SQLException {
    for (int i = 0; i < params.length; i++) {
      pst.setObject(i + 1, params[i]);
    }
  }
} // End of dbInterface
