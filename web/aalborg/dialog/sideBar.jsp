<!-- Switch the levels of Portfolio Access -->

<% if ( thisUser.isAdmin() ){ %>
<%= jlib.adminCommands() %>
<% } %>


<%= jdot.switchDialogType(thisUser.getDialogSecurity() , thisPageURL) %>


<%= jlib.topBox("Dialog Commands") %>
<ul>
<% if (! thisUser.getDialogSecurity().equalsIgnoreCase("public") || thisUser.isAdmin()) { %>
<li><a href='<%= thisPageURL %>?mode=n'>Create Discussion Topic</a></li>
<% } %>
<li><a href='<%= thisPageURL %>?mode=d'>List Discussion Topics</a></li>
<li><a href="<%= thisPageURL %>?mode=e">Discussion Quick-view</a></li>
<li><a href="<%= thisPageURL %>?mode=i">Help!</a></li>
<li><a target="_new" href="/jportfolio/jsp/user/myDialog.jsp">View All My Posts</a></li>
<%= jlib.botBox() %> 


<%= jlib.currentUsers( thisUser.getPortfolio(), thisUser.getUserID() ) %>

<!-- Begin Search Box -->

<br>

<%= jdot.searchBox(thisPageURL) %>


<%= jdot.dialogLegend() %>
