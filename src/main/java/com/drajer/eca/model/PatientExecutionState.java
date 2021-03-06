package com.drajer.eca.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.drajer.eca.model.EventTypes.JobStatus;

public class PatientExecutionState {
	
	String 							patientId;
	String 							encounterId;
	MatchTriggerStatus 				matchTriggerStatus;
	CreateEicrStatus				createEicrStatus;
	Set<PeriodicUpdateEicrStatus>   periodicUpdateStatus;
	EventTypes.JobStatus    		periodicUpdateJobStatus;
	CloseOutEicrStatus				closeOutEicrStatus;
	Set<ValidateEicrStatus> 		validateEicrStatus;
	Set<SubmitEicrStatus>			submitEicrStatus;
	Set<RRStatus>				 	rrStatus;
	
	public PatientExecutionState(String patId, String enId) {
		
		patientId = patId;
		encounterId = enId;
		
		matchTriggerStatus = new MatchTriggerStatus();
		
		createEicrStatus = new CreateEicrStatus();
		
		// Ignore Periodic Updates for now.
		periodicUpdateStatus = new HashSet<PeriodicUpdateEicrStatus>();
		
		closeOutEicrStatus = new CloseOutEicrStatus();
		
		validateEicrStatus = new HashSet<ValidateEicrStatus>();
		
		submitEicrStatus = new HashSet<SubmitEicrStatus>();
		
		rrStatus = new HashSet<RRStatus>();
		
		periodicUpdateJobStatus = JobStatus.NOT_STARTED;
		
	}
	
	public PatientExecutionState() {
		
		patientId = "";
		encounterId = "";
		
		matchTriggerStatus = new MatchTriggerStatus();
		
		createEicrStatus = new CreateEicrStatus();
		
		// Ignore Periodic Updates for now.
		periodicUpdateStatus = new HashSet<PeriodicUpdateEicrStatus>();
		
		closeOutEicrStatus = new CloseOutEicrStatus();
		
		validateEicrStatus = new HashSet<ValidateEicrStatus>();
		
		submitEicrStatus = new HashSet<SubmitEicrStatus>();
		
		periodicUpdateJobStatus = JobStatus.NOT_STARTED;
		
		rrStatus = new HashSet<RRStatus>();
		
	}

	public EventTypes.JobStatus getPeriodicUpdateJobStatus() {
		return periodicUpdateJobStatus;
	}

	public void setPeriodicUpdateJobStatus(EventTypes.JobStatus periodicUpdateJobStatus) {
		this.periodicUpdateJobStatus = periodicUpdateJobStatus;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(String encounterId) {
		this.encounterId = encounterId;
	}

	public MatchTriggerStatus getMatchTriggerStatus() {
		return matchTriggerStatus;
	}

	public void setMatchTriggerStatus(MatchTriggerStatus matchTriggerStatus) {
		this.matchTriggerStatus = matchTriggerStatus;
	}

	public CreateEicrStatus getCreateEicrStatus() {
		return createEicrStatus;
	}

	public void setCreateEicrStatus(CreateEicrStatus createEicrStatus) {
		this.createEicrStatus = createEicrStatus;
	}

	public Set<PeriodicUpdateEicrStatus> getPeriodicUpdateStatus() {
		return periodicUpdateStatus;
	}

	public void setPeriodicUpdateStatus(Set<PeriodicUpdateEicrStatus> periodicUpdateStatus) {
		this.periodicUpdateStatus = periodicUpdateStatus;
	}

	public CloseOutEicrStatus getCloseOutEicrStatus() {
		return closeOutEicrStatus;
	}

	public void setCloseOutEicrStatus(CloseOutEicrStatus closeOutEicrStatus) {
		this.closeOutEicrStatus = closeOutEicrStatus;
	}

	public Set<ValidateEicrStatus> getValidateEicrStatus() {
		return validateEicrStatus;
	}

	public void setValidateEicrStatus(Set<ValidateEicrStatus> validateEicrStatus) {
		this.validateEicrStatus = validateEicrStatus;
	}

	public Set<SubmitEicrStatus> getSubmitEicrStatus() {
		return submitEicrStatus;
	}

	public void setSubmitEicrStatus(Set<SubmitEicrStatus> submitEicrStatus) {
		this.submitEicrStatus = submitEicrStatus;
	}
	
	
	
	public Set<RRStatus> getRrStatus() {
		return rrStatus;
	}

	public void setRrStatus(Set<RRStatus> rrStatus) {
		this.rrStatus = rrStatus;
	}

	public Boolean hasActionCompleted(String actionId) {
		
		if(actionId.contentEquals(matchTriggerStatus.getActionId()) && 
		   matchTriggerStatus.getJobStatus() == JobStatus.COMPLETED) {
			
			// Add check to see if a trigger matched ...For testing because of lack of data the check is omitted.
			return true;
		}
		else if(actionId.contentEquals(createEicrStatus.getActionId()) && 
				   createEicrStatus.getJobStatus() == JobStatus.COMPLETED) {
			return true;
		}
		else if(actionId.contentEquals(closeOutEicrStatus.getActionId()) && 
				closeOutEicrStatus.getJobStatus() == JobStatus.COMPLETED) {
			return true;	
		}
		
		for(PeriodicUpdateEicrStatus pd : periodicUpdateStatus) {
			
			if(actionId.contentEquals(pd.getActionId()) && 
					pd.getJobStatus() == JobStatus.COMPLETED) {
				return true;
			}	
		}
		
		for(ValidateEicrStatus vs : validateEicrStatus) {
				
				if(actionId.contentEquals(vs.getActionId()) && 
						vs.getJobStatus() == JobStatus.COMPLETED) {
					return true;
				}
		}
		
		for(SubmitEicrStatus ss : submitEicrStatus) {
			
			if(actionId.contentEquals(ss.getActionId()) && 
					ss.getJobStatus() == JobStatus.COMPLETED) {
				return true;
			}	
		}
				
		return false;
	}
	
	public Set<Integer> getEicrIdForCompletedActions(String actionId) {
		
		Set<Integer> ids = new HashSet<Integer>();
		
		if(actionId.contentEquals(createEicrStatus.getActionId()) && 
				   createEicrStatus.getJobStatus() == JobStatus.COMPLETED) {
			
			ids.add(Integer.valueOf(createEicrStatus.geteICRId()));
		}
		
		if(actionId.contentEquals(closeOutEicrStatus.getActionId()) && 
				closeOutEicrStatus.getJobStatus() == JobStatus.COMPLETED) {
			ids.add(Integer.valueOf(closeOutEicrStatus.geteICRId()));	
		}
		
		for(PeriodicUpdateEicrStatus pd : periodicUpdateStatus) {
			
			if(actionId.contentEquals(pd.getActionId()) && 
					pd.getJobStatus() == JobStatus.COMPLETED) {
				ids.add(Integer.valueOf(pd.geteICRId()));
			}	
		}
				
		return ids;
	}
	
	public Set<Integer> getEicrsReadyForValidation() {
		
		Set<Integer> ids = new HashSet<Integer>();
		
		// Get the EICRs already validated. 
		Set<Integer> valIds = new HashSet<Integer>();
		Set<ValidateEicrStatus> vals = this.getValidateEicrStatus();
		for(ValidateEicrStatus val : vals) {
			
			// Collect the Ids.
			valIds.add(Integer.valueOf(val.geteICRId()));
		}
		
		if(this.getCreateEicrStatus().getJobStatus() == JobStatus.COMPLETED && 
		   !valIds.contains(Integer.valueOf(this.getCreateEicrStatus().geteICRId()))) {
			ids.add(Integer.valueOf(this.getCreateEicrStatus().geteICRId()));
		}
			
		if(this.getCloseOutEicrStatus().getJobStatus() == JobStatus.COMPLETED && 
				   !valIds.contains(Integer.valueOf(this.getCloseOutEicrStatus().geteICRId()))) {
			ids.add(Integer.valueOf(getCloseOutEicrStatus().geteICRId()));	
		}
		
		for(PeriodicUpdateEicrStatus pd : periodicUpdateStatus) {
			
			if(	pd.getJobStatus() == JobStatus.COMPLETED && 
					   !valIds.contains(Integer.valueOf(pd.geteICRId()))) {
				ids.add(Integer.valueOf(pd.geteICRId()));
			}	
		}
		
		return ids;
	}
	
	public Set<Integer> getEicrsReadyForSubmission() {
		
		Set<Integer> ids = new HashSet<Integer>();
		
		// Get the EICRs already validated. 
		Set<Integer> valIds = new HashSet<Integer>();
		Set<SubmitEicrStatus> vals = this.getSubmitEicrStatus();
		for(SubmitEicrStatus val : vals) {			
			// Collect the Ids.
			valIds.add(Integer.valueOf(val.geteICRId()));
		}
		
		for(ValidateEicrStatus pd : validateEicrStatus) {
			
			if(	pd.getJobStatus() == JobStatus.COMPLETED && 
					   !valIds.contains(Integer.valueOf(pd.geteICRId()))) {
				ids.add(Integer.valueOf(pd.geteICRId()));
			}	
		}
		
		return ids;
	}
	
	public Set<Integer> getEicrsForRRCheck() {
		
		Set<Integer> ids = new HashSet<Integer>();
		
		// Get the EICRs already validated. 
		Set<Integer> valIds = new HashSet<Integer>();
		Set<RRStatus> vals = this.getRrStatus();
		for(RRStatus val : vals) {			
			// Collect the Ids.
			valIds.add(Integer.valueOf(val.geteICRId()));
		}
		
		for(SubmitEicrStatus pd : submitEicrStatus) {
			
			if(	pd.getJobStatus() == JobStatus.COMPLETED && 
					   !valIds.contains(Integer.valueOf(pd.geteICRId()))) {
				ids.add(Integer.valueOf(pd.geteICRId()));
			}	
		}
		
		return ids;
	}
}
