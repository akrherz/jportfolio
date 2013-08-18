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
 * Class for storing IMs while people are on-line. Should work nicely.
 *
 */


import java.util.*;

public class IMessage {

    private Integer id;
    private String to;
    private String from;
    private String contents;
    private Date submitted;
   
/**
     * Constructor 
     * 
     * Gets the unique ID from the generator.
     */
    public IMessage()  {
        submitted = new Date();
    }

    /**
     * The index number
     */
    public void setId( Integer index )
    {
        this.id=index;
    }

    /**
     * Get the index number
     * @return the id
     */
    public String getId()
    {
        return id.toString();
    }

    /**
     * Get the value of submitted.
     * @return value of submitted.
     */
    public String getSubmitted() 
    {
        return submitted.toString();
    }
    
    
    /**
     * Get the value of contents.
     * @return value of contents.
     */
    public String getContent() {return contents;}
    
    /**
     * Set the value of contents.
     * @param v  Value to assign to contents.
     */
    public void setContent(String  v) {this.contents = v;}
    
        
    /**
     * Get the value of name.
     * @return value of name.
     */
    public String getAuthor() {return from;}
    
    
    public String getTo() {return to;}
    
    /**
     * Set the value of name.
     * @param v  Value to assign to name.
     */
    public void setAuthor(String  v) {this.from = v;}
    
    
    public void setTo(String  v) {
    	System.err.println("I am setting to: "+v );
	this.to = v;
    }
            
    /**
     * Get the value of subject.
     * @return value of subject.
     */
  

} // End of IMDatabase
