<%@ include file='setup.jsp' %>
<%
 String hi_t = (String)request.getParameter("hi_t");
 String lo_t = (String)request.getParameter("lo_t");
 String qpf00 = (String)request.getParameter("qpf00");
 String qpf12 = (String)request.getParameter("qpf12");
 String qsf00 = (String)request.getParameter("qsf00");
 String qsf12 = (String)request.getParameter("qsf12");
 String pop00 = (String)request.getParameter("pop00");
 String pop12 = (String)request.getParameter("pop12");
 String wind14 = (String)request.getParameter("wind14");
 String ceil02 = (String)request.getParameter("ceil02");
 String vis02 = (String)request.getParameter("vis02");

 java.util.Date now = new java.util.Date();
 if ( now.getHours() >= 18 ) {
  out.println("Oops, the deadline of 2310Z (6:10pm) has passed!");
  return;
 }
 if ( now.getHours() == 18 && now.getMinutes() > 10 ) {
  out.println("Oops, the deadline of 2310Z (6:10pm) has passed!");
  return;
 }

 if (hi_t != null && lo_t != null && qpf00 != null
   && qpf12 != null && pop00 != null && pop12 != null){
   out.println("<p>Entering Your Forecast:");
   dbInterface.updateDB("DELETE from afc_forecasts WHERE "
    +" username = '"+ thisUser.getUserID() +"'  and "
    +" portfolio = '"+ thisUser.getPortfolio() +"' and "
    +" day = 'TOMORROW'::date ");
   dbInterface.updateDB("INSERT into afc_forecasts(username, portfolio, "
    +" day, high, low, qpf00, qpf12, pop00, pop12, wind14, ceil02, vis02, qsf00, qsf12) VALUES "
    +" ('"+ thisUser.getUserID()+"'"
    +", '"+ thisUser.getPortfolio() +"', 'TOMORROW'::date,"+ hi_t +", "
    +" "+ lo_t +", "
    +" "+ qpf00 +", "+ qpf12 +", "+ pop00 +", "+ pop12 +", "+ wind14 +", "+ ceil02 +", "+ vis02 +", "+ qsf00 +", "+ qsf12 +" ) ");
   out.println("<p>Done!");
 }
%>
<p>Go back and <a href="forecast.jsp">Revise your forecast</a>, if you like
