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
import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import org.collaborium.portfolio.*;

public class portfolioCred {

  public final static int admin = -1;
  public final static int portfolioAdmin = 1;
  public final static int user = 2;
  public final static int guest = 3;
  public final static int anonymous = 4;
  private int myCred = 3;

 /**
  * Give me a portfolioCred instance with some initial credential
  * @param initialCred my initial credential setting
  */
  public portfolioCred(int initialCred)
  {
    this.myCred = initialCred;
  }

 /**
  * Method to set credentials
  * @param newCred new credential setting
  */
  public void setCred(int newCred)
  {
    if (newCred == 0) this.myCred = this.user;
    else this.myCred = newCred;
    plogger.report("Setting portfolioCred to :"+ this.myCred );
  }

 /**
  * Simple method to return our current credential
  */
  public int getCred()
  {
    return this.myCred;
  }

} // End of portfolioCred
