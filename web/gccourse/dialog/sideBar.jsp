
<!-- Switch the levels of Portfolio Access -->

<%= jdot.switchDialogType(thisUser.getDialogSecurity() , thisPageURL) %>

<% if ( thisUser.isAdmin() ){ %>
<%= jlib.adminCommands() %>
<% } %>

<%= jlib.topBox("Learning Blocks") %>
<ul>
<li><a href='/jportfolio/gccourse/block/index.jsp?blockid=1'>Block 1</a></li>
<li><a href='/jportfolio/gccourse/block/index.jsp?blockid=2'>Block 2</a></li>
<li><a href='/jportfolio/gccourse/block/index.jsp?blockid=3'>Block 3</a></li>
</ul>


<%= jlib.topBox("Dialog Commands") %>
<ul>
<% if (! thisUser.getDialogSecurity().equalsIgnoreCase("public") || thisUser.isAdmin()) { %>
<li><a href='<%= thisPageURL %>?mode=n'>Create Discussion Topic</a></li>
<% } %>
<li><a href='<%= thisPageURL %>?mode=d'>List Discussion Topics.</a></li>
<li><a href="<%= thisPageURL %>?mode=e">Discussion Quick-view.</a></li>
<li><a href="<%= thisPageURL %>?mode=i">Help!</a></li>
<li><a href="previous.jsp">View Previous Discussions</a></li>
</ul>

<%= jdot.searchBox(thisPageURL) %>
<%= jdot.dialogLegend() %>
