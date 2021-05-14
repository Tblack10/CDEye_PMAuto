package CDEye_PMAuto.backend.project;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import CDEye_PMAuto.backend.paygrade.Paygrade;
import CDEye_PMAuto.backend.paygrade.PaygradeManager;
import CDEye_PMAuto.backend.recepackage.RECEManager;
import CDEye_PMAuto.backend.recepackage.RespEngCostEstimate;
import CDEye_PMAuto.backend.workpackage.WorkPackage;
import CDEye_PMAuto.backend.workpackage.WorkPackageManager;
import CDEye_PMAuto.backend.wpallocation.WorkPackageAllocManager;
import CDEye_PMAuto.backend.wpallocation.WorkPackageAllocation;

@Named("createProject")
@RequestScoped
public class CreateProjectBean extends Project implements Serializable {
    @Inject private ProjectManager projectManager;
    @Inject private ProjectList projList;
    @Inject private WorkPackageManager wpManager;
    String errorMsg;
    boolean showError;
    @Inject
	PaygradeManager pgm;
	@Inject
	WorkPackageAllocManager wpam;
	@Inject
	RECEManager recem;
    public String add() {
        if (this.projectNumber.isEmpty()) {
            showError = true;
            errorMsg = "The project number cannot be empty";
        } else if (this.projectName.isEmpty()) {
            showError = true;
            errorMsg = "The project name cannot be empty";
        } else if (projectManager.isProjectNameExist(this.projectName)) {
            errorMsg = "The project name " + this.projectName + " already exists";
            showError = true;
        } else if (projectManager.isProjectNumberExist(this.projectNumber)) {
            errorMsg = "The project number " + this.projectNumber + " already exists";
            showError = true;
        } else {
            String id = this.projectNumber + "#";
            Project p = new Project(this);
            
            WorkPackage baseWp = new WorkPackage();
            baseWp.setId(UUID.randomUUID());
            baseWp.setWorkPackageNumber("00000");
            baseWp.setProject(p);
            baseWp.setProjectBudget(new BigDecimal(0));
            baseWp.setCompletedBudget(new BigDecimal(0));
            baseWp.setCompletedPersonDays(new BigDecimal(0));
            baseWp.setStartDate(new Date());
            baseWp.setEndDate(new Date());
            projectManager.persist(p);
            wpManager.addWorkPackage(baseWp);
            
            WorkPackage addedWp = wpManager.getByUUID(baseWp.getId().toString());
    		createWpAllocs(addedWp);
    		createRECEs(addedWp);
            
            showError = false;
            errorMsg = "";
           projList.refreshList();
        return "ProjectDashboard";
        }
        projList.refreshList();
        return "ProjectDashboard";
    }
    
 // create a wpalloc for each paygrade
 	public void createWpAllocs(WorkPackage addedWp) {
 		Paygrade[] paygradeArr = pgm.getAll();
 		for (Paygrade p : paygradeArr) {
 			WorkPackageAllocation wpa = new WorkPackageAllocation();
 			wpa.setId(UUID.randomUUID());
 			wpa.setWorkPackage(addedWp);
 			wpa.setPaygrade(p);
 			wpa.setPersonDaysEstimate(new BigDecimal(0));
 			wpam.addWorkPackageAlloc(wpa);
 		}
 	}

 	// create a RECE for each paygrade
 	public void createRECEs(WorkPackage addedWp) {
 		Paygrade[] paygradeArr = pgm.getAll();
 		for (Paygrade p : paygradeArr) {
 			RespEngCostEstimate rece = new RespEngCostEstimate();
 			rece.setId(UUID.randomUUID());
 			rece.setWorkPackage(addedWp);
 			rece.setPaygrade(p);
 			rece.setPersonDayEstimate(new BigDecimal(0));
 			recem.persist(rece);
 		}
 	}

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isShowError() {
        return showError;
    }

    public void setShowError(boolean showError) {
        this.showError = showError;
    }
}
