<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">
<html>
<head>
  <TITLE><%= title %></TITLE>
  <link rel="stylesheet" type="text/css" href="/jportfolio/css/<%= thisUser.getStyle() %>.css">
  <script language="JavaScript" type="text/javascript">
  <!--//BEGIN Script
  function new_window(url) {
    link = window.open(url,"Link","toolbar=0,location=0,directories=0,status=0,menubar=no,scrollbars=yes,resizable=yes,width=522,height=282");
  } // End of new_window()
  //END Script-->
 </script>


 </HEAD>
 <BODY class="main">
		
 <!-- Begin Table to space entire document -->
 <TABLE width='100%' cellpadding=0 rowspacing=0 border=0 cellspacing=0>
   <TR class="headerBar">
    <TD class="contentColor">
        <td><%@include file='menubar.jsp' %></td>
        <TD align="right">
<%
  if (thisUser != null){
    out.println("<b>Welcome:</b> "+ thisUser.getRealName() 
     +"<br><b>Portfolio:</b> "+ thisUser.getPortfolio() );

  } else {
    out.println("You must be logged in!");
  }
%>
	</TD>
   </TR>
 </table>

<!-- End of header.jsp -->
