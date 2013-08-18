<%@include file='setup.jsp'%>
<%@include file='include/header.jsp'%>

<%@include file='include/post_vars.jsp'%>

<%
  String method = (String)request.getParameter("mode");
  String id = (String)request.getParameter("id");
  if (method == null)  method = "z";
  ResultSet rs = null;

  switch(method.charAt(0)){
   /**  Method summary
    * a -> add site to database
    * c -> update site information
    * d -> create sensor group
    * e -> add sensor to a site
    * f -> remove sensor from a site
    */
//--------------------------------------------------------------------------
    case 'a':
    try{
     dbInterface.updateDB("INSERT into iem_sites(portfolio, s_name, s_hid, "
      +" s_nid, s_mid, s_lat, s_lon, s_st, s_elev) VALUES "
      +" ('"+thisUser.getPortfolio()+"', '"+s_name+"', '"+s_hid+"', "
      +" '"+s_nid+"', '"+s_mid+"', "+s_lat+", "+s_lon+", '"+s_st+"', "
      +" "+s_elev+" )");
     out.println("<br>Site added to the database...\n");

     /** Time to retreive back the ID of this site */
     rs = dbInterface.callDB("SELECT last_value from iem_sites_id_seq");
     rs.next();
     String siteID = (String)rs.getString("last_value");
     rs.close();
     out.println("<br>DEBUG: Assigned database ID of "+ siteID +"\n");
     if (! s1_type.equalsIgnoreCase("null") ) {
       if (s1_id == null) s1_id = "0";
       if (s1_install == null) s1_install = "now";
       dbInterface.updateDB("INSERT into iem_sensor(portfolio, r_id, "
        +" o_serial) VALUES ('"+ thisUser.getPortfolio() +"', "+ s1_type +", "
        +" '"+ s1_id +"' )");

       /** Retreive back the ID of this insert */
       rs = dbInterface.callDB("SELECT last_value from iem_sensor_id_seq");
       rs.next();
       String sensorID = (String)rs.getString("last_value");  
       rs.close();
       out.println("<br>DEBUG: Created new sensor\n");

       dbInterface.updateDB("INSERT into iem_sensor_history(portfolio, "
        +" o_id, installed, s_id) VALUES ('"+ thisUser.getPortfolio() +"', "
        +" "+sensorID+", '"+ s1_install +"', "+siteID+")");
       out.println("<br>DEBUG: Added sensor to site\n");
     }

     if (! s2_type.equalsIgnoreCase("null") ) {
       if (s2_id == null) s2_id = "0";
       if (s2_install == null) s2_install = "now";
       dbInterface.updateDB("INSERT into iem_sensor(portfolio, r_id, "
        +" o_serial) VALUES ('"+ thisUser.getPortfolio() +"', "+ s2_type +", "
        +" '"+ s2_id +"' )");

       /** Retreive back the ID of this insert */
       rs = dbInterface.callDB("SELECT last_value from iem_sensor_id_seq");
       rs.next();
       String sensor2ID = (String)rs.getString("last_value");
       rs.close();
       out.println("<br>DEBUG: Created new sensor\n");

       dbInterface.updateDB("INSERT into iem_sensor_history(portfolio, "
        +" o_id, installed, s_id) VALUES ('"+ thisUser.getPortfolio() +"', "
        +" "+sensor2ID+", '"+ s2_install +"', "+siteID+")");
       out.println("<br>DEBUG: Added sensor to site\n");
     }

     if (! s3_type.equalsIgnoreCase("null") ) {
       if (s3_id == null) s3_id = "0";
       if (s3_install == null) s3_install = "now";
       dbInterface.updateDB("INSERT into iem_sensor(portfolio, r_id, "
        +" o_serial) VALUES ('"+ thisUser.getPortfolio() +"', "+ s3_type +", "
        +" '"+ s3_id +"' )");

       /** Retreive back the ID of this insert */
       rs = dbInterface.callDB("SELECT last_value from iem_sensor_id_seq");
       rs.next();
       String sensor3ID = (String)rs.getString("last_value");
       rs.close();
       out.println("<br>DEBUG: Created new sensor\n");

       dbInterface.updateDB("INSERT into iem_sensor_history(portfolio, "
        +" o_id, installed, s_id) VALUES ('"+ thisUser.getPortfolio() +"', "
        +" "+sensor3ID+", '"+ s3_install +"', "+siteID+")");
       out.println("<br>DEBUG: Added sensor to site\n");
     }

     ResultSet maxth = dbInterface.callDB("select MAX (idnum)+1 as result "
      +" from dialog WHERE idnum > 10000 and idnum < 20000");
     maxth.next();
     String newthreadid = maxth.getString("result");

     /** Now we add a dialog entry for this site */
     portfolioMessage myMessage = new portfolioMessage();
     myMessage.setSubject("Site Discussion: "+s_name);
     myMessage.setBody("Use the 'Post a follow up' link to make comments "
      +" about this station.");
     myMessage.setThreadID( newthreadid );
     myMessage.setidnum( newthreadid );
//     myMessage.setReplyAuthor( thisUser.getUserID() );
     myMessage.setSecurity("site");
     myMessage.setClassification("site");
     myMessage.setAuthor( thisUser.getUserID() );
     myMessage.setAuthorName( thisUser.getRealName() );
     myMessage.setPortfolio( thisUser.getPortfolio() );
//    myMessage.setGID( thisUser.getGroupID() );
//     myMessage.setLink( req.getParameter("link") );
     myMessage.setTopicid("site"+siteID);

     myMessage.commitMessage(); 
     out.println("<br>DEBUG: created discussion for site");

     out.println("<p>Input was successful.  What do you want to do now?\n"
      +"<br><a href='/jportfolio/mesonet/main.jsp'>Network Home</a>\n"
      +"<br><a href='/jportfolio/mesonet/main.jsp?mode=b'>Edit Site Info</a>\n"
      +"<br><a href='/jportfolio/mesonet/main.jsp?mode=b'>List Sites</a>\n");
   } catch(Exception ex){
     ex.printStackTrace();
     out.println("<p>There was an error processing this request.");

   }
    
    break;
//--------------------------------------------------------------------------
    case 'c':
    try{
     mSite mySite = new mSite();
     mySite.setName(s_name);
     mySite.setID(id);
     mySite.setHID(s_hid);
     mySite.setNID(s_nid);
     mySite.setMID(s_mid);
     mySite.setLat(s_lat);
     mySite.setLon(s_lon);
     mySite.setState(s_st);
     mySite.setElev(s_elev);
     mySite.update();
     out.println("<p>Updated Site information.\n"
      +"<br><a href='/jportfolio/mesonet/main.jsp'>Back to Home</a>\n");

    } catch(Exception ex){
     ex.printStackTrace();
     out.println("<p>There was an error processing this request.");
    }
    break;
//--------------------------------------------------------------------------
    case 'd':
    try{
      dbInterface.updateDB("INSERT into iem_sensors(portfolio, r_name, "
       +" r_hid, r_type, r_model, r_vendor) VALUES ('"+thisUser.getPortfolio()+"', "
       +" '"+r_name+"', '"+r_hid+"', '"+r_type+"', '"+r_model+"', "
       +" '"+r_vendor+"' )");
      out.println("<p>Input of Sensor Group was successful.");

    } catch(Exception ex){
     ex.printStackTrace();
     out.println("<p>There was an error processing this request.");
    }
    break;
//--------------------------------------------------------
    case 'e':
    try{
     if (s1_type != "null") {
       if (s1_id == null) s1_id = "0";
       if (s1_install == null) s1_install = "now";
       dbInterface.updateDB("INSERT into iem_sensor(portfolio, r_id, "
        +" o_serial) VALUES ('"+ thisUser.getPortfolio() +"', "+ s1_type +", "
        +" '"+ s1_id +"' )");
       /** Retreive back the ID of this insert */
       rs = dbInterface.callDB("SELECT last_value from iem_sensor_id_seq");
       rs.next();
       String sensorID = (String)rs.getString("last_value");
       rs.close();

       dbInterface.updateDB("INSERT into iem_sensor_history(portfolio, "
        +" o_id, installed, s_id) VALUES ('"+ thisUser.getPortfolio() +"', "
        +" "+sensorID+", '"+ s1_install +"', "+ id +")");
     }


    } catch(Exception ex){
     ex.printStackTrace();
     out.println("<p>There was an error processing this request.");
    }

    break;
//----------------------------------------------------------------------------
    case 'f':
      dbInterface.updateDB("UPDATE iem_sensor_history SET removed = "
       +" '"+ removed +"' WHERE portfolio = '"+ thisUser.getPortfolio()+"' "
       +" and s_id = "+id+" and o_id = "+o_id+" ");
      out.println("<p>Removed this sensor from the site.\n");
    break;
  } // End of switch
%>




<%= jlib.footer() %>
