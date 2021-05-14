package CDEye_PMAuto.backend.recepackage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import CDEye_PMAuto.backend.workpackage.EditableWorkPackage;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import CDEye_PMAuto.backend.employee.Employee;
import CDEye_PMAuto.backend.employee.EmployeeManager;
import CDEye_PMAuto.backend.paygrade.Paygrade;
import CDEye_PMAuto.backend.workpackage.WorkPackage;
import CDEye_PMAuto.backend.workpackage.WorkPackageManager;

@Entity
@Table(name="recepackage")
@Named("recepackage")
@RequestScoped
public class RespEngCostEstimate implements Serializable {

    
    /** The ID of the RECE **/
    @Id
    @Column(name="id")
    @Type(type = "uuid-char")
    protected UUID id;
    
    /**  The parent workpackage **/
    @JoinColumn(name="parentwp")
    @ManyToOne
    protected WorkPackage parentWp;
  
    /** The paygrade that is associated with this RECE **/
    @JoinColumn(name="paygrade")
    @ManyToOne
    protected Paygrade paygrade;
    
    /** Number of days estimated at this paygrade for the package **/
    @Column(name="persondayestimate")
    protected BigDecimal personDayEstimate;
    
    /** Employee assigned to work on this project **/
    @JoinColumn(name="employeeId")
    @ManyToOne
    protected Employee employee;
    
  
    
    @JoinColumn(name="wp")
    @ManyToOne(fetch = FetchType.EAGER)
    protected WorkPackage workPackage;

    
    /**
     * Empty Default Constructor
     */
    public RespEngCostEstimate() {
//    	System.out.println("I exist as a bean " + employeeManager);
    };

    public RespEngCostEstimate(EditableWorkPackage wp, Paygrade paygrade) {
        this.id = UUID.randomUUID();
        this.workPackage = wp;
        this.paygrade = paygrade;
        this.personDayEstimate = new BigDecimal(0);
    }

    
    /**
     * RECEPackage Constructor
     * 
     * @param id of the package, as a UUID
     * @param parentWp, the parent of the RECE, as a WorkPackage
     * @param paygrade, the paygrade associated with the RECE as a Paygrade
     * @param personDayEstimate, the number of days estimated to be spent on the project at this paygrade, as a BigDecimal
     * @param employee, the employee assigned to the package at the chosen paygrade
     */
    public RespEngCostEstimate(UUID id, WorkPackage parentWp, Paygrade paygrade, BigDecimal personDayEstimate, Employee employee, WorkPackage workPackage) {
        this.id = id;
        this.parentWp = parentWp;
        this.paygrade = paygrade;
        this.personDayEstimate = personDayEstimate;
        this.employee = employee;
        this.workPackage = workPackage;
    }
    
    public RespEngCostEstimate(EditableRECE erece) {
        this.id = erece.id;
        this.parentWp = erece.parentWp;
        this.paygrade = erece.paygrade;
        this.personDayEstimate = erece.personDayEstimate;
        this.employee = erece.employee;
        this.workPackage = erece.workPackage;
    }
    
    /**Ensures that associated package cost estimates sum to 
     * estimated budget - cannot save if false*/
    public boolean valid() { return true; }
    
    
    // Getters and Setters ====================================================
    
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
     
    public WorkPackage getParentWp() {
        return parentWp;
    }
    
    public void setParentWp(WorkPackage parentWp) {
        this.parentWp = parentWp;
    }
    
    public Paygrade getPaygrade() {
        return paygrade;
    }
    
    public void setPaygrade(Paygrade paygrade) {
        this.paygrade = paygrade;
    }
 
    public BigDecimal getPersonDayEstimate() {
        return personDayEstimate;
    }
    
    public void setPersonDayEstimate(BigDecimal personDayEstimate) {
        this.personDayEstimate = personDayEstimate;
    }
   
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * @return the workPackage
     */
    public WorkPackage getWorkPackage() {
        return workPackage;
    }

    /**
     * @param workPackage the workPackage to set
     */
    public void setWorkPackage(WorkPackage workPackage) {
        this.workPackage = workPackage;
    }


	

   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}

