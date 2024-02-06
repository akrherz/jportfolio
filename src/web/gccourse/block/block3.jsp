<!-- -------------------------- Begin Big Layout -->

<TABLE bgcolor="#eeeee" border=1>
<caption><font class="title">Biosphere and Human Component of Global Change</font></caption>

<!-- Begin First Header -->
<TR>
        <TH>Assignment:</TH>
        <TH>Due Date:</TH>
        <TH>Available / Link:</TH>
</TR>

<!-- Quizzes Section -->
<TR>
        <TH>Quizzes:</TH>
        <TD>
	<script>
	
	function changeD(qid){
	
		//
		startDates = new Array();
		endDates = new Array();
<%
  StringBuffer quizListing = new StringBuffer();

  try{
    ResultSet rs = dbInterface.callDB("SELECT quiznum, qname, "
	+" to_char(startdate,'Dy Mon DD HH12:MI AM') as startdate, "
	+" to_char(stopdate,'Dy Mon DD HH12:MI AM') as stopdate from quizes "
	+" where portfolio = '"+ thisUser.getPortfolio() +"' and topicid IN "
	+" (SELECT name from units WHERE blockid = 3) ");
      
   while (rs.next() ){
  	out.println("startDates["+ rs.getString("quiznum") +"] = \""+ rs.getString("startdate") +"\";\n");
	out.println("endDates["+ rs.getString("quiznum") +"] = \""+ rs.getString("stopdate") +"\";\n");
   	quizListing.append("<option value=\""+ rs.getString("quiznum") +"\">"+ rs.getString("qname") +"\n");
   }
  } catch(Exception ex){
    plogger.report("Hello");
    ex.printStackTrace();
  }
%>
	
		document.quizdate.start.value = startDates[qid];
		document.quizdate.end.value = endDates[qid];
		return true;
	}
	</script>
	<form name="quizdate" action="/jportfolio/servlet/jquiz">
	<input type="hidden" value="t" name="mode">
	Opens: <input type="text" size="25" name="start" value="Select Quiz">
	<br>Closes: <input type="text" size="25" name="end" value="Select Quiz">
	</TD>
        <TD> 
	<SELECT name="qid" 
	  onChange="changeD(this.form.qid.options[this.form.qid.selectedIndex].value)">
	 <%= quizListing %>
	
	</SELECT>
	
	<br><input type="SUBMIT" value="View/Take Quiz">
	
	</form>
	</TD>
</TR>

<!-- Dialog -->
<TR>
   <TH>Dialog:</TH>
   <TD><a href="http://www.meteor.iastate.edu/gccourse/DialogIntegrity.html">See Requirements</a></TD>
   <TD>
   <form name="dialog">
   <SELECT name="entry" onChange="location=this.form.entry.options[this.form.entry.selectedIndex].value">
   <option value="">Select Discussion Topic
<%
   try{
     ResultSet rs = dbInterface.callDB("SELECT subject,idnum,date from dialog WHERE"
       +"  portfolio = '"+ thisUser.getPortfolio() +"' and topicid IN (SELECT "
       +" name from units WHERE blockid = 3) ORDER by date");
       
     while (rs.next() ){
       out.println("  <option value=\"/jportfolio/gccourse/dialog/index.jsp?mode=o&idnum="
         +rs.getString("idnum") +"\">"+ rs.getString("subject") +"<br>\n");
     }
   
   } catch(Exception ex){
     plogger.report("Problem in JSPland");
   }
%> 
   </select>
   </form>
   </TD>
</TR>



<!-- Ethical Discussion -->
<TR>
   <TH><a href="http://www.meteor.iastate.edu/gccourse/EthicalQuestion.html">Ethical Discussion</a></TH>
   <TD>25 Apr @Midnight</TD>
   <TD>
<a href="/jportfolio/gccourse/dialog/index.jsp?mode=r&idnum=11001">Complete</a>
<!--
<i>Link to be posted</i>
-->
</TD>
</TR>



<!-- Self Assessment -->
<TR>
   <TH><a href="http://www.meteor.iastate.edu/gccourse/SAssess.html">Self Assessment:</a></TH>
   <TD>30 Apr @Midnight</TD>
   <TD><a href="/jportfolio/gccourse/dialog/selfAssess.jsp?blockid=3">Complete</a></TD>
</TR>


</TABLE>
<!-- End layout -->

