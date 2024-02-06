<%
 /**
  * listSensors.jsp
  *  - List out sensors in the Mesonetwork
  */
%>
<%@include file='../setup.jsp'%>
<%@include file='../include/header.jsp'%>


<font class="bluet">List Sensors</font>

<%
  rs = dbInterface.callDB("SELECT o.o_serial, r.r_name "
   +" from iem_sensor o, iem_sensors r WHERE "
   +" o.portfolio = '"+ thisUser.getPortfolio() +"' and o.r_id = r.id ");

  if (rs != null) {
    while( rs.next() ){
      out.println("<p>"+ rs.getString("r_name") 
        +" || "+ rs.getString("o_serial") +"\n");
    } // End of while
  } // End of if

%>

<%= jlib.footer() %>
