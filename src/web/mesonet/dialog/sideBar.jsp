
<!-- Switch the levels of Portfolio Access -->
<form name="switch">
<%= jlib.topBox("Dialog Levels:") %>

<select name="dlevel" onChange="location=this.form.dlevel.options[this.form.dlevel.selectedIndex].value">

<% 
   String dlevel = (String)request.getParameter("dlevel");
   if (dlevel != null)
     thisUser.myPortfolio.setDialogSecurity(dlevel);
   else 
     dlevel = thisUser.myPortfolio.getDialogSecurity();
   if (dlevel == null) {
     dlevel = "public";
     thisUser.myPortfolio.setDialogSecurity(dlevel);
   }

   if (dlevel.equalsIgnoreCase("public") ) {%>
        <option value="<%= thisPageURL %>?dlevel=public" SELECTED>Public
<% } else { %>
        <option value="<%= thisPageURL %>?dlevel=public">Public
<% } %>

<% if (dlevel.equalsIgnoreCase("site") ) {%>
        <option value="<%= thisPageURL %>?dlevel=site" SELECTED>Site
<% } else { %>
        <option value="<%= thisPageURL %>?dlevel=site">Site
<% } %>

<% if (dlevel.equalsIgnoreCase("sensor") ) {%>
        <option value="<%= thisPageURL %>?dlevel=sensor" SELECTED>Sensor
<% } else { %>
        <option value="<%= thisPageURL %>?dlevel=sensor">Sensor
<% } %>

</select>
</td></tr></table>
</form>

<%= jlib.topBox("Links:") %>
  <a class="commands" href='/jportfolio/mesonet/main.jsp'>IEM Portfolio Home</a><br>
</td></tr></table>
<p>

<%= jlib.topBox("Dialog Commands:") %>
<a class="commands" href='<%= thisPageURL %>?mode=n'>Create Discussion Topic.</a><br>
<a class="commands" href='<%= thisPageURL %>?mode=d'>List Discussion Topics.</a><br>
<a class="commands" href="<%= thisPageURL %>?mode=e">Discussion Quick-view.</a><br>
<a class="commands" href="<%= thisPageURL %>?mode=i">Help!</a><br>
</td></tr></table>
<p>

<!-- Begin Search Box -->


<%= jlib.topBox("Search Dialog:") %>
<form method='post' action='<%= thisPageURL %>' name='search'>
<input type='hidden' name='mode' value='s'>
Search For:<br>
<input type='text' name='searchStr' size='15'><br>
Search On:<br>
<select name='searchCol'>
<option value='body'>Posting Text
<option value='name'>Author
<option value='subject'>Subject
</select>
<br><input type='submit' value='Search'>
</form>
</td></tr></table>

