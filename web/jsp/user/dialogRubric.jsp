<%
// dialogRubric.jsp -> Something to print a rubric of Dialog Posts
// Daryl Herzmann 30 Oct 2002
%>
<html>
<head>
  <title>Jportfolio | Dialog Rubric</title>
  <link rel=stylesheet type=text/css href="/jportfolio/css/basic.css">
</head>
<body bgcolor="white">

<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.collaborium.util.*" %>
<%@ page import="org.collaborium.portfolio.*" %>


<% 
portfolioUser thisUser = (portfolioUser)session.getAttribute("User");
String notifyBaseURL = "/jportfolio/servlet/jdot3";

//ResultSet rs = dbInterface.callDB("SELECT *, "
//  +" case WHEN threadid >= 10754 and threadid <= 10825 THEN 1 "
//  +"   WHEN  threadid >= 10844 and threadid <= 10980 THEN 2 "
//  +"   WHEN threadid >= 10990 and threadid <= 11073 THEN 3 "
//+"   ELSE 0 END as block from dialog "
//+" where username = '"+ thisUser.getUserID() +"' and "
//+" security = 'public' and "
//+" portfolio = '"+ thisUser.getPortfolio() +"' ORDER by date");

ResultSet rs = dbInterface.callDB("SELECT *, "
  +" case WHEN threadid >= 10754 and threadid <= 10825 THEN 1 "
  +"   WHEN  threadid >= 10844 and threadid <= 10980 THEN 2 "
  +"   WHEN threadid >= 10990 and threadid <= 11073 THEN 3 "
  +"   ELSE 0 END as block from dialog "
  +" where username = 'stbauer' and "
  +" security = 'public' and "
  +" portfolio = 'gcp2002' ORDER by date");

Hashtable cat1 = new Hashtable();
cat1.put("analysis", "");		cat1.put("articulating", "");
cat1.put("brainstorming", "");		cat1.put("generalization", "");
cat1.put("organization", "");		cat1.put("reacting", "");	
cat1.put("social", "");			cat1.put("summary", "");
Hashtable cat2 = new Hashtable();
cat2.put("analysis", "");		cat2.put("articulating", "");
cat2.put("brainstorming", "");		cat2.put("generalization", "");
cat2.put("organization", "");		cat2.put("reacting", "");	
cat2.put("social", "");			cat2.put("summary", "");
Hashtable cat3 = new Hashtable();
cat3.put("analysis", "");		cat3.put("articulating", "");
cat3.put("brainstorming", "");		cat3.put("generalization", "");
cat3.put("organization", "");		cat3.put("reacting", "");	
cat3.put("social", "");			cat3.put("summary", "");


while ( rs.next() ){
  String postCat = (String)rs.getString("type");
  String next = "<li><a href=\""+ notifyBaseURL +"?mode=r&idnum="+ rs.getString("idnum") +"\">"+ rs.getString("subject") +"</a></li>" ;
 
 if (rs.getString("block").equalsIgnoreCase("1") ) {
    String prev = (String)cat1.get(postCat);
    if (prev == null) prev = "";
    cat1.put(postCat, prev + next +"<br>\n");
 } // End of if
 if (rs.getString("block").equalsIgnoreCase("2") ) {
    String prev = (String)cat2.get(postCat);
    if (prev == null) prev = "";
    cat2.put(postCat, prev + next +"<br>\n");
 } // End of if
 if (rs.getString("block").equalsIgnoreCase("3") ) {
    String prev = (String)cat3.get(postCat);
    if (prev == null) prev = "";
    cat3.put(postCat, prev + next +"<br>\n");
 } // End of if
}
%>

<table border=1 width="100%">
 <tr><th>KB Type:</th><th>Block 1:</th><th>Block 2:</th><th>Block 3:</th></tr>
 <tr><th>Analysis:</th>
   <td><%= cat1.get("analysis") %></td>
   <td><%= cat2.get("analysis") %></td>
   <td><%= cat3.get("analysis") %></td></tr>
 <tr><th>Articulating:</th>
   <td><%= cat1.get("articulating") %></td>
   <td><%= cat2.get("articulating") %></td>
   <td><%= cat3.get("articulating") %></td></tr>
 <tr><th>Brainstorming:</th>
   <td><%= cat1.get("brainstorming") %></td>
   <td><%= cat2.get("brainstorming") %></td>
   <td><%= cat3.get("brainstorming") %></td></tr>
 <tr><th>Generalization:</th>
   <td><%= cat1.get("generalization") %></td>
   <td><%= cat2.get("generalization") %></td>
   <td><%= cat3.get("generalization") %></td></tr>
 <tr><th>Organization:</th>
   <td><%= cat1.get("organization") %></td>
   <td><%= cat2.get("organization") %></td>
   <td><%= cat3.get("organization") %></td></tr>
 <tr><th>Reacting:</th>
   <td><%= cat1.get("reacting") %></td>
   <td><%= cat2.get("reacting") %></td>
   <td><%= cat3.get("reacting") %></td></tr>
 <tr><th>Social:</th>
   <td><%= cat1.get("social") %></td>
   <td><%= cat2.get("social") %></td>
   <td><%= cat3.get("social") %></td></tr>
 <tr><th>Summary:</th>
   <td><%= cat1.get("summary") %></td>
   <td><%= cat2.get("summary") %></td>
   <td><%= cat3.get("summary") %></td></tr>
</table>
