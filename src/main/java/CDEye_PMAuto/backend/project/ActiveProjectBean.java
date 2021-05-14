package CDEye_PMAuto.backend.project;

import java.io.Serializable;
import java.util.UUID;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import CDEye_PMAuto.backend.credentials.Credential;
import CDEye_PMAuto.backend.credentials.CredentialManager;
import CDEye_PMAuto.backend.employee.Employee;
import CDEye_PMAuto.backend.workpackage.WorkPackageList;

@Named("activeProjectBean")
@SessionScoped
public class ActiveProjectBean extends Project implements Serializable {

	@Inject ProjectManager projectManager;
	@Inject WorkPackageList wpl;
	
	public String setActiveProjectBean(EditableProject ep) {
		this.id = ep.getId();
		Project activeProject = projectManager.find(this.id);
		this.id = activeProject.id;
		this.projectName = activeProject.projectName;
		this.projectNumber = activeProject.projectNumber;
		this.projManager = activeProject.projManager;
		this.startDate = activeProject.startDate;
		this.endDate = activeProject.endDate;
		this.estimateBudget = activeProject.estimateBudget;
		this.markUpRate = activeProject.markUpRate;
		this.projectBudget = activeProject.projectBudget;
		wpl.refreshList();
		
		return "WPList?faces-redirect=true";
	}
	
	 public String setReportProjectBean(EditableProject ep) {
	        this.id = ep.getId();
	        Project activeProject = projectManager.find(this.id);
	        this.id = activeProject.id;
	        this.projectName = activeProject.projectName;
	        this.projectNumber = activeProject.projectNumber;
	        this.projManager = activeProject.projManager;
	        this.startDate = activeProject.startDate;
	        this.endDate = activeProject.endDate;
	        this.estimateBudget = activeProject.estimateBudget;
	        this.markUpRate = activeProject.markUpRate;
	        this.projectBudget = activeProject.projectBudget;
	        wpl.refreshList();
	        
	        return "ProjectReport?faces-redirect=true";
	 }
	
}
