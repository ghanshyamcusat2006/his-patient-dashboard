<%@ include file="/WEB-INF/template/include.jsp" %>
<openmrs:require privilege="View PatientDashboard" otherwise="/login.htm" redirect="index.htm" />
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="includes/js_css.jsp" %>
<input type="hidden" id="pageId" value="patientDashboard"/>

<b class="boxHeader">Dashboard</b>
<div  class="box">
<table  cellspacing="10" width="100%">
	<tr>
		<td width="40%"><b>Patient ID:</b> ${patient.patientIdentifier.identifier}</td>
		<td width="30%">
			<c:if test="${not empty admittedStatus }">
				<span style="background-color:red; color:white">Admitted patient</span>				
			</c:if>
		</td>
		<td width="30%"><b>Location:</b> ${opd.name } </td>
	</tr>
	<tr>
		<td width="40%"><b>Name:</b> ${patient.givenName}&nbsp;&nbsp;${patient.middleName}&nbsp;&nbsp; ${patient.familyName}</td>
		<td width="30%"><b>Age:</b> ${age }</td>
		<td width="30%"><b>Gender:</b> ${patient.gender }</td>
	</tr>
	<tr>
		<td width="40%"><b>Patient category:</b> ${patientCategory}</td>
		<td width="30%"><b>Age category:</b> ${ageCategory }</td>
		<td width="30%"><b>Referral:</b> ${referral.name }</td>
	</tr>
</table>
<div id="tabs">
     <ul>
         <li><a href="opdEntry.htm?patientId=${patient.patientId }&opdId=${opd.conceptId }&referralId=${referral.conceptId }&queueId=${queueId}"  title="OPD entry"><span > OPD entry</span></a></li>
         <li><a href="clinicalSummary.htm?patientId=${patient.patientId }"   title="Clinical summary"><span>Clinical summary</span></a></li>
         <li><a href="investigationReport.htm?patientId=${patient.patientId }"  title="Investigation report"><span >Investigation report</span></a></li>
         <li><a href="ipdRecord.htm?patientId=${patient.patientId }&opdLog=${opdLog}"  title="IPD record"><span >IPD record</span></a></li>
     </ul>
     
     <div id="OPD_entry"></div>
	 <div id="Clinical_summary"></div>
	 <div id="Investigation_report"></div>
	 <div id="IPD_record"></div>
</div>

</div>


<%@ include file="/WEB-INF/template/footer.jsp" %> 