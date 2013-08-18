<form method="GET" name="switchBox" action="/jportfolio/servlet/jdot3">


<!-- Begin box element for Switch Dialog Type: -->
<table width='100%' border='0' rowspacing='0' cellpadding='3' cellspacing='0'>
<tr><td class="topBox"><font class="topBox">Switch Dialog Type:</font></td></tr>
<tr><td class="botBox">
<select name="dialogType" onChange="location=this.form.dialogType.options[this.form.dialogType.selectedIndex].value">

<% 
   if (dialogType == null) dialogType = "public";
   if (dialogType.equalsIgnoreCase("public") ) {%>
	<option value="jdot.jsp?dialogType=public" SELECTED>Public
<% } else { %>
	<option value="jdot.jsp?dialogType=public">Public
<% } %>

<% if (dialogType.equalsIgnoreCase("group") ) {%>
	<option value="jdot.jsp?dialogType=group" SELECTED>Group
<% } else { %>
	<option value="jdot.jsp?dialogType=group">Group
<% } %>

<% if (dialogType.equalsIgnoreCase("private") ) {%>
	<option value="jdot.jsp?dialogType=private" SELECTED>Private
<% } else { %>
	<option value="jdot.jsp?dialogType=private">Private
<% } %>

</select>
</td></tr></table>
</form>
<!-- End of Change of dailog Type -->
<p>


<!-- Begin box element for Portfolio Apps: -->
<table width='100%' border='0' rowspacing='0' cellpadding='3' cellspacing='0'>
<tr><td class="topBox" NOWRAP><font class="topBox">Portfolio Apps:</font></td></tr>
<tr><td class="botBox" NOWRAP>
<a class="commands" href='main.jsp'>Home<a/><br>
<a class="commands" href='/jportfolio/servlet/jportfolio?mode=l'>Log Out</a>
</td></tr></table>
<p>

<!-- Begin box element for JDOT Commands: -->
<table width='100%' border='0' rowspacing='0' cellpadding='3' cellspacing='0'>
<tr><td class="topBox"><font class="topBox">JDOT Commands:</font></td></tr>
<tr><td class="botBox">
<a class="commands" href='jdot.jsp?mode=d'>List Discussion Topics.</a><br>
<a class="commands" href='jdot.jsp?mode=n'>Create Discussion Topic.</a><br>
<a class="commands" href="jdot.jsp?mode=e">Discussion Quick-view.</a><br>
<a class="commands" href="jdot.jsp?mode=i">Help!</a>
</td></tr></table>
<p>

<!-- Begin Search Box -->


<!-- Begin box element for Search Dialog: -->
<table width='100%' border='0' rowspacing='0' cellpadding='3' cellspacing='0'>
<tr><td class="topBox"><font class="topBox">Search Dialog:</font></td></tr>
<tr><td class="botBox">
<form method='post' action='jdot.jsp' name='search'>
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

