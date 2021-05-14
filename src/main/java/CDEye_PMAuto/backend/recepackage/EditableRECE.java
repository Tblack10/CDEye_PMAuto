package CDEye_PMAuto.backend.recepackage;

import java.math.BigDecimal;
import java.util.UUID;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import CDEye_PMAuto.backend.employee.Employee;
import CDEye_PMAuto.backend.employee.EmployeeManager;
import CDEye_PMAuto.backend.paygrade.Paygrade;
import CDEye_PMAuto.backend.workpackage.WorkPackage;

@RequestScoped
@Named("editableRECE")
public class EditableRECE extends RespEngCostEstimate {

    /** Determines if the RECE is editable, true = yes, false = no **/
    private boolean editable = false;
    
    /** Determines if the RECE is deletable, true = yes, false = no **/
    private boolean deletable = false;
    
    private String empUserName;
    
    @Inject
    private EmployeeManager employeeManager;
    @Inject
    private RECEManager receManager;
    
    /** No-Param Constructor */
    public EditableRECE() {
        super();
    }
    
    /**
     * Constructor
     * 
     * @param r, a RECE Package
     */
    public EditableRECE(RespEngCostEstimate r) {
        super(r.id, r.parentWp, r.paygrade, r.personDayEstimate, r.employee, r.workPackage);
    };
    
    /**
     * Constructor
     * 
     * @param id of the RECE as a UUID
     * @param parentwp of the RECE as a WorkPackage
     * @param paygrade of the RECE as a Paygrade
     * @param personDayEstimate, number of days anticipated at the current paygrade, as a BigDecimal
     * @param employee chosen to work on the RECE at this Paygrade
     */
    public EditableRECE(UUID id, WorkPackage parentwp, Paygrade paygrade, BigDecimal personDayEstimate, Employee employee, WorkPackage workPackage) {
        super(id, parentwp, paygrade, personDayEstimate, employee, workPackage);
    }
    
    public void assignEmployee() {
        System.out.println("employeeManager: " + employeeManager);
        System.out.println("empUserName: " + empUserName);
        Employee e = employeeManager.getEmployeeByUserName(empUserName);
        System.out.println("e: " + e);
        RespEngCostEstimate rece = receManager.find(this.getId());
        rece.setEmployee(e);
        
        System.out.println("rece: " + rece);
        System.out.println("emp name: " + rece.getEmployee());
        System.out.println("rece id: " + rece.getId());
        
        receManager.merge(rece);
    }
    
    // Getters and Setters ====================================================
    
    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    /**
     * @return the empUserName
     */
    public String getEmpUserName() {
        Employee emp = receManager.getByUUID(this.getId().toString()).getEmployee();
        if (emp != null) {
            empUserName = emp.getUserName();
        }
        return empUserName;
    }

    /**
     * @param empUserName the empUserName to set
     */
    public void setEmpUserName(String empUserName) {
        this.empUserName = empUserName;
    }

    /**
     * @return the employeeManager
     */
    public EmployeeManager getEmployeeManager() {
        return employeeManager;
    }

    /**
     * @param employeeManager the employeeManager to set
     */
    public void setEmployeeManager(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    /**
     * @return the receManager
     */
    public RECEManager getReceManager() {
        return receManager;
    }

    /**
     * @param receManager the receManager to set
     */
    public void setReceManager(RECEManager receManager) {
        this.receManager = receManager;
    }
    
}


