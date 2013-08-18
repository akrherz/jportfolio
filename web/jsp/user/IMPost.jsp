<HTML>
<HEAD>
	<TITLE>Portfolio IM Post</TITLE>
	
</HEAD>
<BODY bgcolor="white">


<%
	String userID = request.getParameter("toUser");

%>

<H3>Portfolio Messenger:</H3>

<B>|</B> &nbsp; <a href="IMCat.jsp">Messenges</a> &nbsp; <B>|</B> &nbsp;

<BR><TABLE bgcolor="#EEEEEE"><TR><TD>

You are posting a message to: <B><%= userID %></B>


</TD></TR></TABLE>


<FORM METHOD="POST" action="IMSave.jsp">
<input type="hidden" name="toUser" value="<%= userID %>">

<TEXTAREA name="content" COLS="40" ROWS="3" WRAP></TEXTAREA>

<BR>
<input type="SUBMIT" value="Post IM">
<input type="RESET" value="Clear Message">

</FORM>


<BR><blockquote><font color="green">With this page, you can send another user an almost instant message. 
If your friend has their message window open, or are actively using the system, they will see the message.
Your friend may not instantly recieve this message, so it may take a bit to respond.
</font></blockquote>



</BODY>
</HTML>
