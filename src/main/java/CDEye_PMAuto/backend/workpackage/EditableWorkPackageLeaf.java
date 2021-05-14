package CDEye_PMAuto.backend.workpackage;

import CDEye_PMAuto.backend.employee.Employee;
import CDEye_PMAuto.backend.employee.EmployeeManager;
import CDEye_PMAuto.backend.paygrade.Paygrade;
import CDEye_PMAuto.backend.paygrade.PaygradeManager;
import CDEye_PMAuto.backend.recepackage.EditableRECE;
import CDEye_PMAuto.backend.recepackage.RECEList;
import CDEye_PMAuto.backend.recepackage.RECEManager;
import CDEye_PMAuto.backend.recepackage.RespEngCostEstimate;
import CDEye_PMAuto.backend.wpallocation.WorkPackageAllocation;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Handles the input forms of selected editable work package leaf
 */
@Named("editableWpLeaf")
@SessionScoped
public class EditableWorkPackageLeaf extends WorkPackage implements Serializable {
    @Inject
    private WorkPackageManager workPackageManager;
    
    @Inject
    private RECEManager receManager;
    
    @Inject
    private RECEList receList;

    @Inject
    private PaygradeManager paygradeManager;
    
    @Inject
    private EmployeeManager employeeManager;
    
    @Inject
    private ActiveWPBean activeWP;
    
    private String respEngUsername;

    EditableWorkPackageLeaf() {}

    /**
     * Set the current member fields to the selectedWp
     * @param selectedWp selected EditableWorkPackage from WPList View
     * @return Edit selected workpackage leaf
     */
    public String editSelectedWP(WorkPackage selectedWp) {
    	System.out.println("starting in edit bean ");
    	//receList.refreshList();
        this.id = selectedWp.id;
        this.workPackageNumber = selectedWp.workPackageNumber;
        this.parentWp = selectedWp.parentWp;
        this.completedBudget = selectedWp.completedBudget;
        this.completedPersonDays = selectedWp.completedPersonDays;
        this.startDate = selectedWp.startDate;
        this.endDate = selectedWp.endDate;
        this.isLeaf = selectedWp.isLeaf;
        this.projectBudget = selectedWp.projectBudget;
        this.project = selectedWp.project;
        this.RECEs = selectedWp.RECEs;
//        System.out.println("WORK PACKAGE ALLOCS");
//        for (WorkPackageAllocation w : this.getWpAllocs()) {
//            System.out.println(w.getPaygrade().getName() + ": " + w.getPersonDaysEstimate());
//        }
        this.wpAllocs = selectedWp.wpAllocs;
        this.responsibleEngineer = selectedWp.responsibleEngineer;
        
        if (this.isLeaf) {
        	System.out.println("returning in edit bean ");
        	return "EditWorkPackageLeaf?faces-redirect=true";
        } else {
        	return "EditWorkPackageBranch";
        }
    }

    /**
     * Save the edited input and update workpackage, workalloc, RECE tables
     * @return Return to WPList view
     */
    public String saveEditedWorkPackageLeaf() {
        calculateAllocation();
        calculateResponsibleEngineer();

        // WorkPackage detail DEBUG
        System.out.println("Unallocated Budget: "
            + "\nWork package Number: " + this.workPackageNumber
            + "\nBegin Date: " + this.startDate
            + "\nEnd Date: " + this.endDate
            + "\nProject Budget: " + this.projectBudget
            + "\nCompleted Project Budget: " + this.completedBudget);
        // Workpackage Allocation DEBUG
        System.out.println("WORK PACKAGE ALLOCS");
        for (WorkPackageAllocation w : this.getWpAllocs()) {
            System.out.println(w.getPaygrade().getName() + ": " + w.getPersonDaysEstimate());
        }
        // Responsible Engineer DEBUG
//        for (RespEngCostEstimate r : this.getRECEs()) {
//            System.out.println(r.getPaygrade().getName() + ": " + r.getPersonDayEstimate());
//        }

        // Update this selected workpackage leaf data into database
        System.out.println("the new resp eng is is: "+ this.responsibleEngineer);
        workPackageManager.updateWorkPackageLeaf(this);
        
        //save the list of work packages
        for (EditableRECE r : receList.getList()) {
        	RespEngCostEstimate rece = new RespEngCostEstimate(r);
        	receManager.merge(rece);
          System.out.println(r.getPaygrade().getName() + ": " + r.getPersonDayEstimate());
      }
        
        return "RETURN_WP_LIST";
    }

    /**
     * Refresh allocated budget and person day
     * @return
     */
    public String refreshAllocatedBudgetCalculation() {
        calculateAllocation();
        return "";
    }

    /**
     * Refresh responsible engineer budget and person day
     * @return
     */
    public String refreshResponsibleEngineerCalculation() {
        calculateResponsibleEngineer();
        return "";
    }

    /**
     * Allocated Budget calculation
     * Person Day is calculated as accumulative sum of person days assigned for each paygrade
     * Budget estimate is calculated by each paygrade person days multiply respected paygrade salary
     */
    public void calculateAllocation() {
        BigDecimal personDays = new BigDecimal(0);
        BigDecimal budgetEstimate = new BigDecimal(0);
        for (WorkPackageAllocation w : this.getWpAllocs()) {
            BigDecimal salary = w.getPaygrade().getSalary();
            BigDecimal days = w.getPersonDaysEstimate();
            BigDecimal res = salary.multiply(days);

            budgetEstimate = budgetEstimate.add(res);
            personDays = personDays.add(w.getPersonDaysEstimate());
        }
    }

    /**
     * Receponsible Engineer Budget and Person Day calculation
     * Same as allocated budget
     */
    public void calculateResponsibleEngineer() {
        BigDecimal personDays = new BigDecimal(0);
        BigDecimal budgetEstimate = new BigDecimal(0);
        for (RespEngCostEstimate r : this.RECEs) {
            BigDecimal salary = r.getPaygrade().getSalary();
            BigDecimal days = r.getPersonDayEstimate();
            BigDecimal res = salary.multiply(days);

            budgetEstimate = budgetEstimate.add(res);
            personDays = personDays.add(r.getPersonDayEstimate());
        }
    }

    /**
     * Cancel edit return the navigation link to WPList View
     * @return Return to WPList view
     */
    public String cancelEditedWorkPackageLeaf() {
        return "RETURN_WP_LIST";
    }

    /**
     * @return the receList
     */
    public RECEList getReceList() {
        return receList;
    }

    /**
     * @param receList the receList to set
     */
    public void setReceList(RECEList receList) {
        this.receList = receList;
    }

    /**
     * @return the respEngUsername
     */
    public String getRespEngUsername() {
        System.out.println("WP number: " + activeWP.getWorkPackageNumber());
        System.out.println("WP proj: " + activeWP.getProject().toString());
        System.out.println("before getrespenglookup");
        Employee respEng = workPackageManager.findWpsByPkgNumAndProj(
                activeWP.getWorkPackageNumber(), activeWP.getProject())
                [0].getResponsibleEngineer();
        System.out.println("after getrespenglookup");
        return respEng != null ? respEng.getUserName() : "No One";
//        System.out.println("after getrespenglookup");
//        return "No one";
    }

    /**
     * @param respEngUsername the respEngUsername to set
     */
    public void setRespEngUsername(String respEngUsername) {
        this.respEngUsername = respEngUsername;
        Employee respEng = employeeManager.getEmployeeByUserName(respEngUsername);
        System.out.println("the resp eng found: " + respEng + " with username: " + respEngUsername);
        if (respEng != null) {
            this.setResponsibleEngineer(respEng);
            workPackageManager.updateWorkPackageLeaf(this);
            Collection<WorkPackage> aaa = respEng.getPackagesAssignedToRE();
            aaa.add(this);
            respEng.setPackagesAssignedToRE(aaa);
            employeeManager.editEmployee(respEng);
        }
    }
    
}
