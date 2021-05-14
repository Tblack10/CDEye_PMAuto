package CDEye_PMAuto.backend.workpackage;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import CDEye_PMAuto.backend.project.EditableProject;
import CDEye_PMAuto.backend.project.Project;
import CDEye_PMAuto.backend.project.ProjectManager;

@Named("activeWpBean")
@SessionScoped
public class ActiveWPBean extends WorkPackage implements Serializable {

	@Inject ProjectManager projectManager;
	@Inject WorkPackageList wpl;
	@Inject WorkPackageManager wpm;
	@Inject EditableWorkPackageLeaf ewpl;
	
//	private String newparentwpnum;
	
	
    public String setActiveWorkPackage(EditableWorkPackage wp) {
        this.id = wp.id;
        this.workPackageNumber = wp.workPackageNumber;
        this.parentWp = wp.parentWp;
        this.completedBudget = wp.completedBudget;
        this.completedPersonDays = wp.completedPersonDays;
        this.startDate = wp.startDate;
        this.endDate = wp.endDate;
        this.isLeaf = wp.isLeaf;
        this.projectBudget = wp.projectBudget;
        this.project = wp.project;
        this.RECEs = wp.RECEs;
        this.wpAllocs = wp.wpAllocs;
        this.childPackages = wp.childPackages;
        
		if (wp.isLeaf) {
			System.out.println("setting active wp: ");
			return ewpl.editSelectedWP(this);
			//return "EditWorkPackageLeaf";
		} else {
			return "EditWorkPackageBranch";
		}
	}
	
	public String mergeActive() {
		//TODO
		//check that wp does not exist in project
		if (this.endDate.before(this.startDate)) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            FacesMessage facesMessage = new FacesMessage("Start Date is before End Date");
            facesContext.addMessage("beginDate", facesMessage);
            return null;
        }
		
	    WorkPackage newWp = new WorkPackage();
	    newWp.id = this.id;
	    newWp.workPackageNumber = this.workPackageNumber;
	    newWp.parentWp = this.parentWp;
	    newWp.completedBudget = this.completedBudget;
	    newWp.completedPersonDays = this.completedPersonDays;
	    newWp.startDate = this.startDate;
	    newWp.endDate = this.endDate;
	    newWp.isLeaf = this.isLeaf;
	    newWp.projectBudget = this.projectBudget;
	    newWp.project = this.project;
	    newWp.RECEs = this.RECEs;
	    newWp.wpAllocs = this.wpAllocs;
	    newWp.childPackages = this.childPackages;
	    wpm.updateWorkPackage(newWp);
	        return "WPList";
	    
	}



	
	
}
