<%
 // Something to print out dialog stats for the student to look at.
%>
<html>
<head>
  <title>Portfolio Dialog Stats</title>
</head>
<%@ page import="org.collaborium.portfolio.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%
 portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
 String userID = thisUser.getUserID();
 if (thisUser.isAdmin() ){
   if (request.getParameter("userid") != null){
     userID = request.getParameter("userid");
   }
 } 
 out.println("<p>Dialog Stats for userID: "+ userID );
%>

<p>Go back to <a href="/jportfolio/gccourse/">GC Portfolio</a>

<%
 String sID;
 String lID;
 String eID;
 ResultSet rs;
 ResultSet rs2;
 int authorResponses;
 int otherResponses;
 String s = new String();
%>

<table width="100%">
<tr><th>IDNUM</th><th>Subject</th><th>Replies by Others</th><th>Post counts as a reply by you</th></tr>
<tr><th colspan="4" bgcolor="#EEEEEE">Block 1 Posts</th></tr>
<%
  sID = "11716";
  lID = "11767";
  eID = "11775";
// Grab me all my posts For block 1
  rs = dbInterface.callDB("SELECT "
  +" trim(to_char(idnum, '999999999999999999999999999999999999')) as idnum, subject "
  +" from dialog WHERE username = '"+ userID +"' "
  +" and portfolio = 'gcp2003' and security = 'public' "
  +" and threadid >= "+ sID +" and threadid <= "+ lID +" and "
  +" threadid != "+ eID +" "
  +" and date(date) < '2003-02-21' ");

  authorResponses = 0;
  otherResponses = 0;
  s = new String();
  while (rs.next()) {
    String aRes = "No";
    String idnum = rs.getString("idnum");
    if (idnum.length() > 9) {  
      authorResponses = authorResponses + 1;
      aRes = "Yes";
    }
    rs2 = dbInterface.callDB("SELECT count(username) as count "
   +" from dialog WHERE security = 'public' and portfolio = 'gcp2003' "
   +" and username != '"+ userID +"' "
   +" and idnum BETWEEN ("+ idnum +"::numeric * 10000)::numeric "
   +" AND ( ("+ idnum +"::numeric + 1)* 10000)::numeric ");
    rs2.next();
    int a = new java.lang.Integer(rs2.getString("count")).intValue();
    otherResponses = otherResponses + a;
    out.println("<tr><td><a href='/jportfolio/gccourse/dialog/index.jsp?mode=r&idnum="+ idnum +"'>"+ idnum +"</a></td><td>"+ rs.getString("subject") +"</td>"
      +"<td align='CENTER'>"+ rs2.getString("count") +"</td>"
      +"<td align='CENTER'>"+ aRes +"</td></tr>\n");
  }
  out.println("<tr><td></td><td></td><th align='CENTER'>"+ s.valueOf(otherResponses) +"</th><th align='CENTER'>"+ s.valueOf(authorResponses) +"</th></tr>\n");
%>

<tr><th colspan="4" bgcolor="#EEEEEE">Block 2 Posts</th></tr>
<%
  sID = "11773";
  lID = "12009";
  eID = "12025";
// Grab me all my posts For block 2
  rs = dbInterface.callDB("SELECT "
  +" trim(to_char(idnum, '999999999999999999999999999999999999')) as idnum, subject "
  +" from dialog WHERE username = '"+ userID +"' "
  +" and portfolio = 'gcp2003' and security = 'public' "
  +" and threadid >= "+ sID +" and threadid <= "+ lID +" and "
  +" threadid != "+ eID +" and threadid != 11775 "
  +" and date(date) < '2003-04-04' ");

  authorResponses = 0;
  otherResponses = 0;
  s = new String();
  while (rs.next()) {
    String aRes = "No";
    String idnum = rs.getString("idnum");
    if (idnum.length() > 9) {  
      authorResponses = authorResponses + 1;
      aRes = "Yes";
    }
    rs2 = dbInterface.callDB("SELECT count(username) as count "
   +" from dialog WHERE security = 'public' and portfolio = 'gcp2003' "
   +" and username != '"+ userID +"' "
   +" and idnum BETWEEN ("+ idnum +"::numeric * 10000)::numeric "
   +" AND ( ("+ idnum +"::numeric + 1)* 10000)::numeric ");
    rs2.next();
    int a = new java.lang.Integer(rs2.getString("count")).intValue();
    otherResponses = otherResponses + a;
    out.println("<tr><td><a href='/jportfolio/gccourse/dialog/index.jsp?mode=r&idnum="+ idnum +"'>"+ idnum +"</a></td><td>"+ rs.getString("subject") +"</td>"
      +"<td align='CENTER'>"+ rs2.getString("count") +"</td>"
      +"<td align='CENTER'>"+ aRes +"</td></tr>\n");
  }
  out.println("<tr><td></td><td></td><th align='CENTER'>"+ s.valueOf(otherResponses) +"</th><th align='CENTER'>"+ s.valueOf(authorResponses) +"</th></tr>\n");
%>

<tr><th colspan="4" bgcolor="#EEEEEE">Block 3 Posts</th></tr>
<%
  sID = "12037";
  lID = "12291";
  eID = "12100";
// Grab me all my posts For block 3
  rs = dbInterface.callDB("SELECT "
  +" trim(to_char(idnum, '999999999999999999999999999999999999')) as idnum, subject "
  +" from dialog WHERE username = '"+ userID +"' "
  +" and portfolio = 'gcp2003' and security = 'public' "
  +" and threadid >= "+ sID +" and threadid <= "+ lID +" and "
  +" threadid != "+ eID +" "
  +" and date(date) < '2003-05-04' ");

  authorResponses = 0;
  otherResponses = 0;
  s = new String();
  while (rs.next()) {
    String aRes = "No";
    String idnum = rs.getString("idnum");
    if (idnum.length() > 9) {  
      authorResponses = authorResponses + 1;
      aRes = "Yes";
    }
    rs2 = dbInterface.callDB("SELECT count(username) as count "
   +" from dialog WHERE security = 'public' and portfolio = 'gcp2003' "
   +" and username != '"+ userID +"' "
   +" and idnum BETWEEN ("+ idnum +"::numeric * 10000)::numeric "
   +" AND ( ("+ idnum +"::numeric + 1)* 10000)::numeric ");
    rs2.next();
    int a = new java.lang.Integer(rs2.getString("count")).intValue();
    otherResponses = otherResponses + a;
    out.println("<tr><td><a href='/jportfolio/gccourse/dialog/index.jsp?mode=r&idnum="+ idnum +"'>"+ idnum +"</a></td><td>"+ rs.getString("subject") +"</td>"
      +"<td align='CENTER'>"+ rs2.getString("count") +"</td>"
      +"<td align='CENTER'>"+ aRes +"</td></tr>\n");
  }
  out.println("<tr><td></td><td></td><th align='CENTER'>"+ s.valueOf(otherResponses) +"</th><th align='CENTER'>"+ s.valueOf(authorResponses) +"</th></tr>\n");
%>
</table>

