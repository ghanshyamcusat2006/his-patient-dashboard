/**
 *  Copyright 2010 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of Patient-dashboard module.
 *
 *  Patient-dashboard module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  Patient-dashboard module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Patient-dashboard module.  If not, see <http://www.gnu.org/licenses/>.
 *
 **/


package org.openmrs.module.patientdashboard.web.controller.autocomplete;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.User;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.HospitalCoreService;
import org.openmrs.module.hospitalcore.InventoryCommonService;
import org.openmrs.module.hospitalcore.IpdService;
import org.openmrs.module.hospitalcore.PatientDashboardService;
import org.openmrs.module.hospitalcore.model.Answer;
import org.openmrs.module.hospitalcore.model.Examination;
import org.openmrs.module.hospitalcore.model.InventoryDrug;
import org.openmrs.module.hospitalcore.model.InventoryDrugFormulation;
import org.openmrs.module.hospitalcore.model.IpdPatientAdmissionLog;
import org.openmrs.module.hospitalcore.model.OpdDrugOrder;
import org.openmrs.module.hospitalcore.model.OpdPatientQueueLog;
import org.openmrs.module.hospitalcore.model.Question;
import org.openmrs.module.hospitalcore.model.Symptom;
import org.openmrs.module.hospitalcore.model.TriagePatientData;
import org.openmrs.module.hospitalcore.util.PatientDashboardConstants;
import org.openmrs.module.hospitalcore.util.PatientUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p> Class: AutoCompleteController </p>
 * <p> Package: org.openmrs.module.patientdashboard.web.controller.autocomplete </p> 
 * <p> Author: Nguyen manh chuyen </p>
 * <p> Update by: Nguyen manh chuyen </p>
 * <p> Version: $1.0 </p>
 * <p> Create date: Jan 26, 2011 5:15:41 PM </p>
 * <p> Update date: Jan 26, 2011 5:15:41 PM </p>
 **/
@Controller("AutoCompleteControllerPatientDashboard")
public class AutoCompleteController {
	
	@RequestMapping(value="/module/patientdashboard/autoCompleteDrugCoreList.htm", method=RequestMethod.GET)
	public String drugCoreGet(@RequestParam(value="term",required=false) String name, Model model) {
		List<Drug> drugs = new ArrayList<Drug>();
		if(!StringUtils.isBlank(name)){
			drugs = Context.getConceptService().getDrugs(name);
		}else{
			drugs = Context.getConceptService().getAllDrugs();
		}
			model.addAttribute("drugs",drugs);
		return "module/patientdashboard/autocomplete/autoCompleteDrugCoreList";
	}
	
	@RequestMapping(value="/module/patientdashboard/autoCompleteDrugCoreList.htm", method=RequestMethod.POST)
	public String drugCorePost(@RequestParam(value="term",required=false) String name, Model model) {
		List<Drug> drugs = new ArrayList<Drug>();
		if(!StringUtils.isBlank(name)){
			drugs = Context.getConceptService().getDrugs(name);
		}else{
			drugs = Context.getConceptService().getAllDrugs();
		}
			model.addAttribute("drugs",drugs);
		return "module/patientdashboard/autocomplete/autoCompleteDrugCoreList";
	}
	@RequestMapping("/module/patientdashboard/checkSession.htm")
	public String checkSession(HttpServletRequest request,Model model) {
		 if( Context.getAuthenticatedUser() != null &&  Context.getAuthenticatedUser().getId() != null){
			 model.addAttribute("session","1");
		 }else{
			 model.addAttribute("session","0");
		 }
		
		return "module/patientdashboard/session/checkSession";
	}
	
	@RequestMapping(value="/module/patientdashboard/autoCompleteProvisionalDianosis.htm", method=RequestMethod.GET)
	public String provisionalDianosis(@RequestParam(value="term",required=false) String name, Model model) {
		List<Concept> diagnosis = new ArrayList<Concept>();
		PatientDashboardService dashboardService = Context.getService(PatientDashboardService.class);
		diagnosis = dashboardService.searchDiagnosis(name);
		
		model.addAttribute("diagnosis", diagnosis);
		return "/module/patientdashboard/autocomplete/autoCompleteProvisionalDianosis";
	}
	
	@RequestMapping(value="/module/patientdashboard/comboboxProcedure.htm", method=RequestMethod.GET)
	public String comboboxProcedure(@RequestParam(value="text",required=false) String text, Model model) {
		List<Concept> procedures = new ArrayList<Concept>();
		PatientDashboardService dashboardService = Context.getService(PatientDashboardService.class);
		procedures = dashboardService.searchProcedure(text);
		model.addAttribute("procedures", procedures);
		return "module/patientdashboard/autocomplete/comboboxProcedure";
	}
	
	@RequestMapping(value="/module/patientdashboard/comboboxSymptom.htm", method=RequestMethod.GET)
	public String comboboxSymptom(@RequestParam(value="text",required=false) String text, Model model) {
		List<Concept> symptom = new ArrayList<Concept>();
		PatientDashboardService dashboardService = Context.getService(PatientDashboardService.class);
		symptom = dashboardService.searchSymptom(text);
		model.addAttribute("symptom", symptom);
		return "/module/patientdashboard/autocomplete/comboboxSymptom";
	}
	//Examination
	@RequestMapping(value="/module/patientdashboard/comboboxExamination.htm", method=RequestMethod.GET)
	public String comboboxExamination(@RequestParam(value="text",required=false) String text, Model model) {
		List<Concept> examination = new ArrayList<Concept>();
		PatientDashboardService dashboardService = Context.getService(PatientDashboardService.class);
		 examination = dashboardService.searchExamination(text);
		model.addAttribute(" examination",  examination);
		return "/module/patientdashboard/autocomplete/comboboxExamination";
	}
	
	@RequestMapping(value="/module/patientdashboard/comboboxDianosis.htm", method=RequestMethod.GET)
	public String comboboxDianosis(@RequestParam(value="text",required=false) String text, Model model) {
		List<Concept> diagnosis = new ArrayList<Concept>();
		PatientDashboardService dashboardService = Context.getService(PatientDashboardService.class);
		diagnosis = dashboardService.searchDiagnosis(text);
		model.addAttribute("diagnosis", diagnosis);
		return "/module/patientdashboard/autocomplete/comboboxDianosis";
	}
	
	//ghanshyam 1-june-2013 New Requirement #1633 User must be able to send investigation orders from dashboard to billing
	@RequestMapping(value="/module/patientdashboard/comboboxInvestigation.htm", method=RequestMethod.GET)
	public String comboboxInvestigation(@RequestParam(value="text",required=false) String text, Model model) {
		List<Concept> investigation = new ArrayList<Concept>();
		PatientDashboardService dashboardService = Context.getService(PatientDashboardService.class);
		investigation = dashboardService.searchInvestigation(text);
		model.addAttribute("investigation", investigation);
		return "/module/patientdashboard/autocomplete/comboboxInvestigation";
	}
	
	//ghanshyam 12-june-2013 New Requirement #1635 User should be able to send pharmacy orders to issue drugs to a patient from dashboard
	@RequestMapping(value="/module/patientdashboard/comboboxDrug.htm", method=RequestMethod.GET)
	public String comboboxDrug(@RequestParam(value="text",required=false) String text, Model model) {
		List<InventoryDrug> drugs = new ArrayList<InventoryDrug>();
		PatientDashboardService dashboardService = Context.getService(PatientDashboardService.class);
		drugs = dashboardService.findDrug(text);
		model.addAttribute("drugs", drugs);
		return "/module/patientdashboard/autocomplete/comboboxDrug";
	}
	
	@RequestMapping(value="/module/patientdashboard/detailClinical.htm", method=RequestMethod.GET)
	public String detailClinical(@RequestParam(value="id",required=false) Integer id, Model model) {
		if(id != null){
			
			 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			 ConceptService conceptService = Context.getConceptService();
			
			EncounterService encounterService = Context.getEncounterService();
			AdministrationService administrationService = Context.getAdministrationService();
			PatientDashboardService patientDashboardService = Context
				.getService(PatientDashboardService.class);
			String gpVisiteOutCome = administrationService.getGlobalProperty(PatientDashboardConstants.PROPERTY_VISIT_OUTCOME);
			Encounter encounter =encounterService.getEncounter(id);
			String internal = "";
			String external = "";
			String visitOutCome = "";
			String otherValueOfVisit = "";
			//ghanshyam 8-july-2013 New Requirement #1963 Redesign patient dashboard
			String otherInstructions = "";
			String illnessHistory = "";
			
			Concept conInternal = Context.getConceptService().getConceptByName(Context.getAdministrationService().getGlobalProperty(PatientDashboardConstants.PROPERTY_INTERNAL_REFERRAL));
			Concept conExternal = Context.getConceptService().getConceptByName(Context.getAdministrationService().getGlobalProperty(PatientDashboardConstants.PROPERTY_EXTERNAL_REFERRAL));
			Concept conVisiteOutCome  = conceptService.getConcept(gpVisiteOutCome);
			//ghanshyam 8-july-2013 New Requirement #1963 Redesign patient dashboard
			Concept conOtherInstructions = conceptService.getConceptByName("OTHER INSTRUCTIONS");
			
			Concept conIllnessHistory = conceptService.getConceptByName("History of Present Illness");
			List<Concept> symptoms = new ArrayList<Concept>();
			List<Concept> examinations = new ArrayList<Concept>();
			List<Concept> diagnosiss = new ArrayList<Concept>();
			List<Concept> procedures = new ArrayList<Concept>();
			List<Concept> investigations = new ArrayList<Concept>();
			try {
				if(encounter != null){
					for( Obs obs : encounter.getAllObs()){
						if( obs.getConcept().getConceptId().equals(conInternal.getConceptId()) ){
							internal = obs.getValueCoded().getName()+"";
						}
						if( obs.getConcept().getConceptId().equals(conExternal.getConceptId()) ){
							external = obs.getValueCoded().getName()+"";
						}
						if( obs.getConcept().getConceptId().equals(conVisiteOutCome.getConceptId()) ){
							visitOutCome = obs.getValueText();
							if("Follow-up".equalsIgnoreCase(visitOutCome)){
								try {
									otherValueOfVisit = formatter.format(obs.getValueDatetime());
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								
							}else if("admit".equalsIgnoreCase(visitOutCome)){
								if(obs.getValueCoded() != null){
									
									try {
										otherValueOfVisit = obs.getValueCoded().getName()+"";
									} catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
									}
								}
							}
						}
						//ghanshyam 8-july-2013 New Requirement #1963 Redesign patient dashboard
						if( obs.getConcept().getConceptId().equals(conOtherInstructions.getConceptId()) ){
							otherInstructions = obs.getValueText();
						}

						if( obs.getConcept().getConceptId().equals(conIllnessHistory.getConceptId()) ){
							illnessHistory = obs.getValueText();
						}
						
						if (obs.getValueCoded() != null) {
							if (obs.getValueCoded().getConceptClass().getName().equals("Symptom")) {
								symptoms.add(obs.getValueCoded());
							}
							if (obs.getValueCoded().getConceptClass().getName().equals("Examination")) {
								examinations.add(obs.getValueCoded());
							}
							if (obs.getValueCoded().getConceptClass().getName().equals("Diagnosis")) {
								diagnosiss.add(obs.getValueCoded());
							}
							if (obs.getValueCoded().getConceptClass().getName().equals("Procedure")) {
								procedures.add(obs.getValueCoded());
							}
							if (obs.getValueCoded().getConceptClass().getName().equals("Test")) {
								investigations.add(obs.getValueCoded());
							}
							if (obs.getValueCoded().getConceptClass().getName().equals("LabSet")) {
								investigations.add(obs.getValueCoded());
							}
						}

					}
					
					
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			OpdPatientQueueLog opql=patientDashboardService.getOpdPatientQueueLog(encounter);
			Patient patient = opql.getPatient();
			String patientName;
			if (patient.getMiddleName() != null) {
				patientName = patient.getGivenName() + " "
						+ patient.getFamilyName() + " " + patient.getMiddleName();
			} else {
				patientName = patient.getGivenName() + " "
						+ patient.getFamilyName();
			}
			model.addAttribute("patient", patient);
			model.addAttribute("patientName", patientName);
			
			Date birthday = patient.getBirthdate();
			model.addAttribute("age", PatientUtils.estimateAge(birthday));
			
			User user=opql.getUser();
			Person person=user.getPerson();
			String givenName=person.getGivenName();
			String middleName=person.getMiddleName();
			String familyName=person.getFamilyName();
			
			if(givenName==null){
				givenName="";
			}
			if(middleName==null){
				middleName="";
			}
			if(familyName==null){
				familyName="";
			}
		
			String treatingDoctor=givenName+" "+middleName+" "+familyName;
			
			HospitalCoreService hcs = Context.getService(HospitalCoreService.class);
			List<PersonAttribute> pas = hcs.getPersonAttributes(patient.getPatientId());
			for (PersonAttribute pa : pas) {
				PersonAttributeType attributeType = pa.getAttributeType();
				if (attributeType.getPersonAttributeTypeId() == 14) {
					model.addAttribute("selectedCategory", pa.getValue());
				}
				if (attributeType.getPersonAttributeTypeId() == 36) {
					model.addAttribute("exemptionNumber", pa.getValue());
				}
				if (attributeType.getPersonAttributeTypeId() == 33) {
					model.addAttribute("nhifCardNumber", pa.getValue());
				}
				if (attributeType.getPersonAttributeTypeId() == 32) {
					model.addAttribute("waiverNumber", pa.getValue());
				}
			}
			
			List<OpdDrugOrder> opdDrugOrders=patientDashboardService.getOpdDrugOrder(encounter);
			
			model.addAttribute("treatingDoctor", treatingDoctor);
			model.addAttribute("internal", internal);
			model.addAttribute("external", external);
			model.addAttribute("visitOutCome", visitOutCome);
			model.addAttribute("otherValueOfVisit", otherValueOfVisit);
			//ghanshyam 8-july-2013 New Requirement #1963 Redesign patient dashboard
			model.addAttribute("otherInstructions", otherInstructions);
			model.addAttribute("illnessHistory", illnessHistory);
			model.addAttribute("symptoms", symptoms);
			model.addAttribute("examinations", examinations);
			model.addAttribute("diagnosiss", diagnosiss);
			model.addAttribute("procedures", procedures);
			model.addAttribute("investigations", investigations);
			model.addAttribute("opdDrugOrders", opdDrugOrders);
			
		}
		return "module/patientdashboard/detailClinical";
	}
	
	@RequestMapping(value="/module/patientdashboard/vitalStatistic.htm", method=RequestMethod.GET)
	public String vitalStatistic(@RequestParam(value="id",required=false) Integer id, Model model) {
		if(id != null){
			PatientDashboardService dashboardService =  Context.getService(PatientDashboardService.class);
			EncounterService encounterService = Context.getEncounterService();
			Encounter encounter =encounterService.getEncounter(id);
			OpdPatientQueueLog opdPatientQueueLog=dashboardService.getOpdPatientQueueLog(encounter);
			if (opdPatientQueueLog != null) {
				if(null  != opdPatientQueueLog.getTriageDataId())
				{
				model.addAttribute("triagePatientData", opdPatientQueueLog.getTriageDataId());
				}
				else
				{
					TriagePatientData triagePatientData = new TriagePatientData();
					triagePatientData = dashboardService.getTriagePatientDataFromEncounter(id);
					model.addAttribute("triagePatientData", triagePatientData);
				}
			}
		}
		return "module/patientdashboard/vitalStatistic";
	}
	
	@RequestMapping(value="/module/patientdashboard/symptomDetails.htm", method=RequestMethod.GET)
	public String symptomDetails(@RequestParam(value="id",required=false) Integer id, Model model) {
		if(id != null){
		    PatientDashboardService dashboardService =  Context.getService(PatientDashboardService.class);
			EncounterService encounterService = Context.getEncounterService();
			Encounter encounter =encounterService.getEncounter(id);
			List<Symptom> symptoms=dashboardService.getSymptom(encounter);
			List<String> al=new ArrayList<String>();
			Map<String,Collection<Question>> syptomquestionanswer=new LinkedHashMap<String,Collection<Question>>();
			Map<Question,String> questionanswer=new LinkedHashMap<Question,String>();
			for(Symptom symptom:symptoms){
				al.add(symptom.getSymptomConcept().getName().toString());
				Collection<Question> questions=dashboardService.getQuestion(symptom);
				syptomquestionanswer.put( symptom.getSymptomConcept().getName().toString(), (Collection<Question>) questions);
				for(Question question:questions){
					Answer answer=dashboardService.getAnswer(question);
					if(answer.getAnswerConcept()!=null){
					questionanswer.put(question, answer.getAnswerConcept().getName().toString());
					}
					else{
						questionanswer.put(question, answer.getFreeText());
					}
				}
			}
			model.addAttribute("al", al);
			model.addAttribute("syptomquestionanswer", syptomquestionanswer);
			model.addAttribute("questionanswer", questionanswer);
		}
		return "module/patientdashboard/symptomDetail";
	}
	//Examination
	@RequestMapping(value="/module/patientdashboard/examinationDetails.htm", method=RequestMethod.GET)
	public String examinationDetails(@RequestParam(value="id",required=false) Integer id, Model model) {
		if(id != null){
		    PatientDashboardService dashboardService =  Context.getService(PatientDashboardService.class);
			EncounterService encounterService = Context.getEncounterService();
			Encounter encounters =encounterService.getEncounter(id);
			List<Examination> examinations=dashboardService.getExamination(encounters);
			List<String> al=new ArrayList<String>();
			Map<String,Collection<Question>> exminationquestionanswer=new LinkedHashMap<String,Collection<Question>>();
			Map<Question,String> questionanswer=new LinkedHashMap<Question,String>();
			for(Examination examination:examinations){
				al.add(examination.getExaminationConcept().getName().toString());
				Collection<Question> questions=dashboardService.getQuestion(examination);
				exminationquestionanswer.put( examination.getExaminationConcept().getName().toString(), (Collection<Question>) questions);
				for(Question question:questions){
					Answer answer=dashboardService.getAnswer(question);
					if(answer.getAnswerConcept()!=null){
					questionanswer.put(question, answer.getAnswerConcept().getName().toString());
					}
					else{
						questionanswer.put(question, answer.getFreeText());
					}
				}
			}
			model.addAttribute("al", al);
			model.addAttribute("exminationquestionanswer", exminationquestionanswer);
			model.addAttribute("questionanswer", questionanswer);
		}
		return "module/patientdashboard/examinationDetail";
	}
	@RequestMapping(value="/module/patientdashboard/currentVitalStatistic.htm", method=RequestMethod.GET)
	public String currentVitalStatistic(@RequestParam(value="id",required=false) Integer id, Model model) {
		if(id != null){
		    PatientDashboardService dashboardService =  Context.getService(PatientDashboardService.class);
		    TriagePatientData triagePatientData = dashboardService.getTriagePatientData(id);
		    model.addAttribute("triagePatientData",triagePatientData);
		}
		return "module/patientdashboard/currentVitalStatistic";
	}
	
	@RequestMapping(value="/module/patientdashboard/autoCompleteSymptom.htm", method=RequestMethod.GET)
	public String autoCompleteSymptom(@RequestParam(value="q",required=false) String name, Model model) {
		List<Concept> symptom = Context.getService(PatientDashboardService.class).searchSymptom(name);
		model.addAttribute("symptom",symptom);
		return "module/patientdashboard/autocomplete/autoCompleteSymptom";
	}
	//Examination
	@RequestMapping(value="/module/patientdashboard/autoCompleteExamination.htm", method=RequestMethod.GET)
	public String autoCompleteExamination(@RequestParam(value="q",required=false) String name, Model model) {
		List<Concept> examination = Context.getService(PatientDashboardService.class).searchExamination(name);
		model.addAttribute("examination",examination);
		return "module/patientdashboard/autocomplete/autoCompleteExamination";
	}
	@RequestMapping(value="/module/patientdashboard/autoCompleteDiagnosis.htm", method=RequestMethod.GET)
	public String autoCompleteDiagnosis(@RequestParam(value="q",required=false) String name, Model model) {
		List<Concept> diagnosis = Context.getService(PatientDashboardService.class).searchDiagnosis(name);
		model.addAttribute("diagnosis",diagnosis);
		return "module/patientdashboard/autocomplete/autoCompleteDiagnosis";
	}
	
	@RequestMapping(value="/module/patientdashboard/autoCompleteProcedure.htm", method=RequestMethod.GET)
	public String autoCompleteProcedure(@RequestParam(value="q",required=false) String name, Model model) {
		List<Concept> procedures = Context.getService(PatientDashboardService.class).searchProcedure(name);
		
		model.addAttribute("procedures",procedures);
		return "module/patientdashboard/autocomplete/autoCompleteProcedure";
	}
	
	//ghanshyam 1-june-2013 New Requirement #1633 User must be able to send investigation orders from dashboard to billing
	@RequestMapping(value="/module/patientdashboard/autoCompleteInvestigation.htm", method=RequestMethod.GET)
	public String autoCompleteInvestigation(@RequestParam(value="q",required=false) String name, Model model) {
		List<Concept> investigations = Context.getService(PatientDashboardService.class).searchInvestigation(name);
		model.addAttribute("investigations",investigations);
		return "module/patientdashboard/autocomplete/autoCompleteInvestigation";
	}
	
	//ghanshyam 12-june-2013 New Requirement #1635 User should be able to send pharmacy orders to issue drugs to a patient from dashboard
	@RequestMapping(value="/module/patientdashboard/autoCompleteDrug.htm", method=RequestMethod.GET)
	public String autoCompleteDrug(@RequestParam(value="q",required=false) String name, Model model) {
		List<InventoryDrug> drugs = Context.getService(PatientDashboardService.class).findDrug(name);
		model.addAttribute("drugs",drugs);
		return "module/patientdashboard/autocomplete/autoCompleteDrug";
	}
	
	//ghanshyam 12-june-2013 New Requirement #1635 User should be able to send pharmacy orders to issue drugs to a patient from dashboard
	@RequestMapping(value="/module/patientdashboard/formulationByDrugNameForIssue.form",method=RequestMethod.GET)
	public String formulationByDrugNameForIssueDrug(@RequestParam(value="drugName",required=false)String drugName, Model model) {
		InventoryCommonService inventoryCommonService = (InventoryCommonService) Context.getService(InventoryCommonService.class);
		InventoryDrug drug = inventoryCommonService.getDrugByName(drugName);
		if(drug != null){
			List<InventoryDrugFormulation> formulations = new ArrayList<InventoryDrugFormulation>(drug.getFormulations());
			model.addAttribute("formulations", formulations);
			model.addAttribute("drugId", drug.getId());
		}
		return "/module/patientdashboard/autocomplete/formulationByDrugForIssue";
	}
	
	@RequestMapping(value="/module/patientdashboard/detailProcedure.htm", method=RequestMethod.GET)
	public String detailProcedure(@RequestParam(value="id",required=false) Integer id, Model model) {
	if (id != null){
	ConceptService conceptService = Context.getConceptService();

	EncounterService encounterService = Context.getEncounterService();
	AdministrationService administrationService = Context.getAdministrationService();
	String gpVisiteOutCome = administrationService.getGlobalProperty(PatientDashboardConstants.PROPERTY_VISIT_OUTCOME);
	Encounter encounter = encounterService.getEncounter(id);

	Concept conProcedure = Context.getConceptService().getConceptByName(Context.getAdministrationService().getGlobalProperty(PatientDashboardConstants.PROPERTY_POST_FOR_PROCEDURE));
	Concept conVisiteOutCome = conceptService.getConcept(gpVisiteOutCome);

	String procedure = "";
	String observations = "";
	String visitOutCome = "";

	try {
	if (encounter != null){
	for (Obs obs : encounter.getAllObs()){
	if (obs.getConcept().getConceptId().equals(conProcedure.getConceptId())) {
	procedure = obs.getValueCoded().getName()+"";
	String observ = obs.getComment()+"";
	if (!observ.equals("null"))
	observations = observ;
	}
	if( obs.getConcept().getConceptId().equals(conVisiteOutCome.getConceptId()) ){
	visitOutCome = obs.getValueText();
	}
	}
	}
	} catch (Exception e) {
	e.printStackTrace();
	}

	model.addAttribute("procedure", procedure);
	model.addAttribute("observations", observations);
	model.addAttribute("visitOutCome", visitOutCome);
	}
	return "module/patientdashboard/detailProcedure";
	}
	
	@RequestMapping(value="/module/patientdashboard/detailIPDClinical.htm", method=RequestMethod.GET)
	public String detailIPDClinical(@RequestParam(value="id",required=false) Integer encounterId, Model model) {
		if(encounterId != null){
			
			 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			 ConceptService conceptService = Context.getConceptService();
			
			EncounterService encounterService = Context.getEncounterService();
			AdministrationService administrationService = Context.getAdministrationService();
			PatientDashboardService patientDashboardService = Context
				.getService(PatientDashboardService.class);
			IpdService ipdService = Context.getService(IpdService.class);
			String gpVisiteOutCome = administrationService.getGlobalProperty(PatientDashboardConstants.PROPERTY_VISIT_OUTCOME);
			Encounter encounter =encounterService.getEncounter(encounterId);
			String internal = "";
			String external = "";
			String visitOutCome = "";
			String otherValueOfVisit = "";
			//ghanshyam 8-july-2013 New Requirement #1963 Redesign patient dashboard
			String otherInstructions = "";
			String illnessHistory = "";
			
			Concept conInternal = Context.getConceptService().getConceptByName(Context.getAdministrationService().getGlobalProperty(PatientDashboardConstants.PROPERTY_INTERNAL_REFERRAL));
			Concept conExternal = Context.getConceptService().getConceptByName(Context.getAdministrationService().getGlobalProperty(PatientDashboardConstants.PROPERTY_EXTERNAL_REFERRAL));
			Concept conVisiteOutCome  = conceptService.getConcept(gpVisiteOutCome);
			//ghanshyam 8-july-2013 New Requirement #1963 Redesign patient dashboard
			Concept conOtherInstructions = conceptService.getConceptByName("OTHER INSTRUCTIONS");
			
			Concept conIllnessHistory = conceptService.getConceptByName("History of Present Illness");
			List<Obs> symptoms = new ArrayList<Obs>();
			List<Obs> examinations = new ArrayList<Obs>();
			List<Obs> diagnosiss = new ArrayList<Obs>();
			List<Obs> procedures = new ArrayList<Obs>();
			List<Obs> investigations = new ArrayList<Obs>();
			try {
				if(encounter != null){
					for( Obs obs : encounter.getAllObs()){
						if( obs.getConcept().getConceptId().equals(conInternal.getConceptId()) ){
							internal = obs.getValueCoded().getName()+"";
						}
						if( obs.getConcept().getConceptId().equals(conExternal.getConceptId()) ){
							external = obs.getValueCoded().getName()+"";
						}
						if( obs.getConcept().getConceptId().equals(conVisiteOutCome.getConceptId()) ){
							visitOutCome = obs.getValueText();
							if("Follow-up".equalsIgnoreCase(visitOutCome)){
								try {
									otherValueOfVisit = formatter.format(obs.getValueDatetime());
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								
							}else if("admit".equalsIgnoreCase(visitOutCome)){
								if(obs.getValueCoded() != null){
									
									try {
										otherValueOfVisit = obs.getValueCoded().getName()+"";
									} catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
									}
								}
							}
						}
						//ghanshyam 8-july-2013 New Requirement #1963 Redesign patient dashboard
						if( obs.getConcept().getConceptId().equals(conOtherInstructions.getConceptId()) ){
							otherInstructions = obs.getValueText();
						}

						if( obs.getConcept().getConceptId().equals(conIllnessHistory.getConceptId()) ){
							illnessHistory = obs.getValueText();
						}
						
						if (obs.getValueCoded() != null) {
							if (obs.getValueCoded().getConceptClass().getName().equals("Symptom")) {
								symptoms.add(obs);
							}
							if (obs.getValueCoded().getConceptClass().getName().equals("Examination")) {
								examinations.add(obs);
							}
							if (obs.getValueCoded().getConceptClass().getName().equals("Diagnosis")) {
								diagnosiss.add(obs);
							}
							if (obs.getValueCoded().getConceptClass().getName().equals("Procedure")) {
								procedures.add(obs);
							}
							if (obs.getValueCoded().getConceptClass().getName().equals("Test")) {
								investigations.add(obs);
							}
							if (obs.getValueCoded().getConceptClass().getName().equals("LabSet")) {
								investigations.add(obs);
							}
						}

					}
					
					
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			IpdPatientAdmissionLog ipdPatientAdmissionLog=ipdService.getIpdPatientAdmissionLog(encounter);
			OpdPatientQueueLog opql=patientDashboardService.getOpdPatientQueueLog(ipdPatientAdmissionLog.getOpdLog().getEncounter());
			Patient patient = opql.getPatient();
			String patientName;
			if (patient.getMiddleName() != null) {
				patientName = patient.getGivenName() + " "
						+ patient.getFamilyName() + " " + patient.getMiddleName();
			} else {
				patientName = patient.getGivenName() + " "
						+ patient.getFamilyName();
			}
			model.addAttribute("patient", patient);
			model.addAttribute("patientName", patientName);
			
			Date birthday = patient.getBirthdate();
			model.addAttribute("age", PatientUtils.estimateAge(birthday));
			
			User user=opql.getUser();
			Person person=user.getPerson();
			String givenName=person.getGivenName();
			String middleName=person.getMiddleName();
			String familyName=person.getFamilyName();
			
			if(givenName==null){
				givenName="";
			}
			if(middleName==null){
				middleName="";
			}
			if(familyName==null){
				familyName="";
			}
		
			String treatingDoctor=givenName+" "+middleName+" "+familyName;
			
			HospitalCoreService hcs = Context.getService(HospitalCoreService.class);
			List<PersonAttribute> pas = hcs.getPersonAttributes(patient.getPatientId());
			for (PersonAttribute pa : pas) {
				PersonAttributeType attributeType = pa.getAttributeType();
				if (attributeType.getPersonAttributeTypeId() == 14) {
					model.addAttribute("selectedCategory", pa.getValue());
				}
				if (attributeType.getPersonAttributeTypeId() == 36) {
					model.addAttribute("exemptionNumber", pa.getValue());
				}
				if (attributeType.getPersonAttributeTypeId() == 33) {
					model.addAttribute("nhifCardNumber", pa.getValue());
				}
				if (attributeType.getPersonAttributeTypeId() == 32) {
					model.addAttribute("waiverNumber", pa.getValue());
				}
			}
			
			List<OpdDrugOrder> opdDrugOrders=patientDashboardService.getOpdDrugOrder(encounter);
			
			model.addAttribute("treatingDoctor", treatingDoctor);
			model.addAttribute("internal", internal);
			model.addAttribute("external", external);
			model.addAttribute("visitOutCome", visitOutCome);
			model.addAttribute("otherValueOfVisit", otherValueOfVisit);
			//ghanshyam 8-july-2013 New Requirement #1963 Redesign patient dashboard
			model.addAttribute("otherInstructions", otherInstructions);
			model.addAttribute("illnessHistory", illnessHistory);
			model.addAttribute("symptoms", symptoms);
			model.addAttribute("examinations", examinations);
			model.addAttribute("diagnosiss", diagnosiss);
			model.addAttribute("procedures", procedures);
			model.addAttribute("investigations", investigations);
			model.addAttribute("opdDrugOrders", opdDrugOrders);
			model.addAttribute("admissionWard", ipdPatientAdmissionLog.getAdmissionWard().getName());
			
		}
		return "module/patientdashboard/detailIPDClinical";
	}
	
}
