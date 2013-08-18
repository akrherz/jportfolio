<!-- -------------------------- Begin Big Layout -->

<TABLE bgcolor="#eeeee" border=1>

<!-- Begin First Header -->
<TR>
        <TH>Assignment:</TH>
        <TH>Due Date:</TH>
        <TH>Available / Link:</TH>
	<th>Points Available</th>
</TR>

<!-- Biographical Sketch -->
<TR>
   <TH>Biographical Sketch:</TH>
   <TD>TBA</TD>
   <TD><a href="/jportfolio/jsp/user/customize/editBio.jsp">Edit</a></TD>
</TR>


<!-- Quizzes Section -->
<TR>
        <TH>Quizes:</TH>
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
	+" (SELECT name from units WHERE blockid = 1) ");
      
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
	<form name="quizdate" action="/jportfolio/gccourse/quiz/index.jsp">
	<input type="hidden" value="t" name="mode">
	Opens: <input type="text" size="25" name="start" value="1">
	<br>Closes: <input type="text" size="25" name="end" value="2">
	</TD>
        <TD> 
	<SELECT name="qid" 
	  onChange="changeD(this.form.quizid.options[this.form.quizid.selectedIndex].value)">
	 <%= quizListing %>
	
	</SELECT>
	
	<br><input type="SUBMIT" value="View/Take Quiz">
	
	</form>
	</TD>
</TR>

<!-- Dialog -->
<TR>
   <TH>Dialog:</TH>
   <TD>Ongoing...</TD>
   <TD>
   <form name="dialog">
   <SELECT name="entry" onChange="location=this.form.entry.options[this.form.entry.selectedIndex].value">
<%
   try{
     ResultSet rs = dbInterface.callDB("SELECT subject,idnum,date from dialog WHERE"
       +"  portfolio = '"+ thisUser.getPortfolio() +"' and topicid IN (SELECT "
       +" name from units WHERE blockid = 1) ORDER by date");
       
     while (rs.next() ){
       out.println("  <option value=\"/jportfolio/gccourse/dialog/index.jsp?mode=o&idnum="
         +rs.getString("idnum") +"\">"+ rs.getString("subject") +"<br>\n");
     }
   
   } catch(Exception ex){
     plogger.report("Problem in JSPland");
   }
%> 
   </select>
   <br><input type="SUBMIT" value="Enter Dialog">
   </form>
   </TD>
</TR>



<!-- Ethical Discussion -->
<TR>
   <TH>Ethical Discussion:</TH>
   <TD>TBA</TD>
   <TD><i>(Insert link here)</i></TD>
</TR>



<!-- Self Assessment -->
<TR>
   <TH>Self Assessment:</TH>
   <TD>TBA</TD>
   <TD><i>(Insert link here)</i></TD>
</TR>


</TABLE>
<!-- End layout -->

