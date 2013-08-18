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
import java.lang.*;
import java.io.*;
// import java.beans.*;
 
public class IMDatabase {

  private static Hashtable messages = new Hashtable();
  
  // Contains to and Message ID.
  private static Hashtable pickupMessages = new Hashtable();
  
  private static int nextId = 1;

// Not sure why I had this...
//  private static IMDatabase me = null;

 public static void storeMessages(){
 
 	
 /**	
	try{
	Class.forName("org.collaborium.portfolio.IMessage");
	
	XMLEncoder e1 = new XMLEncoder( new BufferedOutputStream(
				new FileOutputStream("IMpickup.xml")));
	XMLEncoder e2 = new XMLEncoder( new BufferedOutputStream(
				new FileOutputStream("IMmessages.xml")));
	XMLEncoder e3 = new XMLEncoder( new BufferedOutputStream(
				new FileOutputStream("IMnextID.xml")));
	e1.writeObject( pickupMessages );
	e1.close();
	e2.writeObject( messages );
	e2.close();
	e3.writeObject( new Integer(nextId) );
	e3.close();
	} catch( Exception ex){
		plogger.report("Problem with XMLEncoder");
		ex.printStackTrace();
	}
 */
 }

 public static void loadMessages(){

 /**
 	try{
	XMLDecoder d1 = new XMLDecoder( new BufferedInputStream(
                    new FileInputStream("IMpickup.xml")));
	XMLDecoder d2 = new XMLDecoder( new BufferedInputStream(
                    new FileInputStream("IMmessages.xml")));
	XMLDecoder d3 = new XMLDecoder( new BufferedInputStream(
                    new FileInputStream("IMnextID.xml")));
		    
	pickupMessages = (Hashtable)d1.readObject();
	d1.close();
	messages = (Hashtable)d2.readObject();
	d2.close();
	Integer temp = (Integer)d3.readObject();
	nextId = temp.intValue();
	d3.close();
	} catch( Exception ex){
		plogger.report("Problem with XMLDecoder");
		ex.printStackTrace();
	}
 */
 
 }


  /** Constructor */
  private IMDatabase()
    {}

 public static synchronized void removeMessage( String idNum )
 {
 	System.err.println("I am removing "+idNum );
 	Integer oIdNum = new java.lang.Integer(idNum);
	messages.remove( oIdNum );
	pickupMessages.remove( oIdNum );

 }


/**
 * Post a new message.
 */
 public static synchronized void postMessage( IMessage message )
 {
	Integer nextNumber = new Integer( nextId++ );
	message.setId( nextNumber );	
	messages.put( nextNumber,  message );
	
	String toUser = message.getTo();
	pickupMessages.put(nextNumber, toUser );
	
	storeMessages();
	
 }
    
    public static String ezCat() {
    
    	return messages.toString() + pickupMessages.toString();
    
    }
    
    /**
     * List all messages in the store
     */
 //   public static Object[] listAll()
  //  {
 //       return messages.values().toArray();
 //   }
    public static Boolean hasMessage (String user) {
    
	//System.err.println(" For User: "+user);
    	
	// Go and get one ID from the table, if it exists?
    //	Enumeration testIDs = (Enumeration)pickupMessages.get( user );
//	if (testIDs != null) {
//		do {
//			String thisID = testIDs.nextElement().toString();	
//			IMessage thisMessage = IMDatabase.getMessage( thisID );
//			String messageAuthor = thisMessage.getAuthor();
//			if ( messageAuthor.equalsIgnoreCase( author ) )
//				return thisID;
//		
//		} while( testIDs.hasMoreElements() );
//	}

	Enumeration myKeys = pickupMessages.keys();

	if (! pickupMessages.isEmpty() ) {
		
		do {
			String thisValue = (String)pickupMessages.get( myKeys.nextElement() );
			if ( thisValue.equalsIgnoreCase( user ) )
				return Boolean.TRUE;			
		} while( myKeys.hasMoreElements() );
	}

    	return Boolean.FALSE;
    
    }
    
    public static Vector getIDs(String userID) {
    
    	Vector returnVector = new Vector();
	
	Enumeration myKeys = pickupMessages.keys();

	if (! pickupMessages.isEmpty() ) {
		
		do {
			Object thisElement = myKeys.nextElement();
			String thisValue = (String)pickupMessages.get( thisElement );
			if ( thisValue.equalsIgnoreCase( userID ) ){
				returnVector.add( thisElement.toString() );			
			
			}
		} while( myKeys.hasMoreElements() );
	}

	return returnVector;
    
    }
    
    /**
     * Get a specific message
     */
    public static synchronized IMessage getMessage( String index )
    {
        return (IMessage)messages.get( new Integer( index ) );
    }
    
    /**
     * Post a reply to a message
     */
 //  public static synchronized void postReply( Message reply, String parent )
 //   {
 //       Message thread = getMessage( parent );
 //       thread.addReply( reply );
 //   }


} // End of IMDatabase
