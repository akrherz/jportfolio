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
/**
 * This file is just the settings for the portfolio engine, this probably should
 * be a XML file, but I am not experienced enough yet
 * or better yet, in the webxml file
 *
 * @author Daryl Herzmann
 */
 
package org.collaborium.portfolio;


public class settings {

	public static final String servletHttpBase = "/jportfolio/servlet";
	
	public static final String DBurl = "jdbc:postgresql://iemdb:5432/portfolio";
  	public static final String DBusr = "nobody";
  	public static final String DBpwd = "NULL";

	public static String httpBase = "/jportfolio/";
	
	public static String systemPassword = "iitap";

} // End of settings
