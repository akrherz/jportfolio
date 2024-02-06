<%
 /**
  * contacts.jsp
  *  - Manage Contacts for an IEM site
  */
%>
<%@include file='../setup.jsp'%>
<%@include file='../include/header.jsp'%>
<%@include file='links.jsp'%>

<%
  String name = (String)request.getParameter("name");
  String c_id = (String)request.getParameter("c_id");
  if (c_id != null){ // We must delete a contact
    dbInterface.updateDB("DELETE from iem_site_contacts WHERE "
     +" portfolio = '"+ thisUser.getPortfolio() +"' and "
     +" id = "+ c_id +" ");
  }
  if (name != null) { // We have a posting
    String email = (String)request.getParameter("email");
    if (email == null) email = "blank";
    String phone = (String)request.getParameter("phone");
    if (phone == null) phone = "blank";
    dbInterface.updateDB("INSERT into iem_site_contacts (portfolio, s_mid, "
     +" name, phone, email) VALUES('"+ thisUser.getPortfolio() +"', "
     +" '"+ s_mid +"', '"+ name +"', '"+ phone +"', '"+ email +"')");
    out.println("<p>Entered contact info for: "+ name );
  }
%>


<p><font class="bluet">Site Contacts</font>
<%= mLib.listContacts(s_mid, thisUser.getPortfolio() ) %>


<p><font class="bluet">Add Contact:</font>

<form method="POST" action="contacts.jsp">
<table>
<tr>
  <th>Name:</th>
  <td><input type="text" name="name"></td>
</tr>
<tr>
  <th>Phone Number:</th>
  <td><input type="text" name="phone"></td>
</tr>
<tr>
  <th>Email:</th>
  <td><input type="text" name="email"></td>
</tr>
</table>

<p><input type="submit" value="Create Contact">
<input type="reset" value="Reset Form">

</form>

<%= jlib.footer() %>
