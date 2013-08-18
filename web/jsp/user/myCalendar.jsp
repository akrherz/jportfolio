
<jsp:useBean id="calBean" scope="session" class="org.collaborium.portfolio.calBean"/>
<%@ page import="java.util.*" %>
<%@ page import="org.collaborium.portfolio.*" %>

<%
Calendar cal = Calendar.getInstance(TimeZone.getDefault());

int myMonth = cal.get(cal.MONTH);
int myYear = cal.get(cal.YEAR);

portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
String cgiMonth = (String)request.getParameter("month");
String cgiYear = (String)request.getParameter("year");

if (cgiMonth != null && cgiYear != null)
{
  try{
    myMonth = Integer.parseInt(request.getParameter("month"));
    myYear = Integer.parseInt(request.getParameter("year"));
  } catch(Exception ex) {
  }
}
cal.set(Calendar.YEAR, myYear);
cal.set(Calendar.MONTH, myMonth);
cal.set(Calendar.DAY_OF_MONTH, 1);
plogger.report( cal.toString() );

String[] months = new String [] { "January", "February", "March",
                                        "April", "May", "June",
                                        "July", "August", "September",
                                        "October", "November", "December" };
String[] days = new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", 
                                        "Thursday", "Friday", "Saturday"};

out.println( jlib.header( thisUser, "Portfolio Calendar", "Calendar") );
out.println("<center><font size=+2>" + months[cal.get(cal.MONTH)] +" "+ cal.get(cal.YEAR) +"</font></center><br>");

%>

<P><B>Available Calendars:</B> 
<form method="GET" action="myCalendar.jsp">
<table>
<tr><th>Select Month:</th><th>Select Year:</th></tr>
<tr><td>
<select name="month">
 <option value="0" <% if (myMonth == 0) out.print("SELECTED"); %>>January
 <option value="1" <% if (myMonth == 1) out.print("SELECTED"); %>>February
 <option value="2" <% if (myMonth == 2) out.print("SELECTED"); %>>March
 <option value="3" <% if (myMonth == 3) out.print("SELECTED"); %>>April
 <option value="4" <% if (myMonth == 4) out.print("SELECTED"); %>>May
 <option value="5" <% if (myMonth == 5) out.print("SELECTED"); %>>June
 <option value="6" <% if (myMonth == 6) out.print("SELECTED"); %>>July
 <option value="7" <% if (myMonth == 7) out.print("SELECTED"); %>>August
 <option value="8" <% if (myMonth == 8) out.print("SELECTED"); %>>September
 <option value="9" <% if (myMonth == 9) out.print("SELECTED"); %>>October
 <option value="10" <% if (myMonth == 10) out.print("SELECTED"); %>>November
 <option value="11" <% if (myMonth == 11) out.print("SELECTED"); %>>December
</select>
</td><td>
<select name="year">
 <option value="2000" <% if (myYear == 2000) out.print("SELECTED"); %>>2000
 <option value="2001" <% if (myYear == 2001) out.print("SELECTED"); %>>2001
 <option value="2002" <% if (myYear == 2002) out.print("SELECTED"); %>>2002
 <option value="2003" <% if (myYear == 2003) out.print("SELECTED"); %>>2003
 <option value="2004" <% if (myYear == 2004) out.print("SELECTED"); %>>2004
 <option value="2005" <% if (myYear == 2005) out.print("SELECTED"); %>>2005
 <option value="2006" <% if (myYear == 2006) out.print("SELECTED"); %>>2006
 <option value="2007" <% if (myYear == 2007) out.print("SELECTED"); %>>2007
</select>
</td></tr></table>
<input type="submit" value="Show Calendar">
</form>


<P>
<%= calBean.calTable(thisUser, cal)%>

<%= jlib.footer() %>
