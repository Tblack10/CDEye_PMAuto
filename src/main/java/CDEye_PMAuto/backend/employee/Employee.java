package CDEye_PMAuto.backend.employee;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import CDEye_PMAuto.backend.paygrade.Paygrade;
import CDEye_PMAuto.backend.paygrade.PaygradeManager;
import CDEye_PMAuto.backend.workpackage.WorkPackage;

/**
 * Contains employee information.
 * 
 * @author travisblack and wilsonzhu
 */
@Entity
@Table(name="employees")
@Named("employee")
@DynamicUpdate
@SessionScoped
public class Employee implements Serializable {

	@Transient
	@Inject private EmployeeManager employeeManager;
	
	@Id
	@Type(type = "uuid-char")
	@Column(name="id")
	protected UUID id;
	
    /** The employee's employee number. Assigned by HR, indicates num and department */
	@Column(name="empnumber")
    protected int empNum;
    
    /** The employee's name. */
	@Column(name="firstname")
    protected String firstName;
    
    /** The employee's name. */
	@Column(name="lastname")
    protected String lastName;
	
	/** Indicates whether the employee is active or not within the company**/
	@Column(name="active")
	protected Boolean active;
	
	/** Indicates whether the employee is HR **/
    @Column(name="hr")
    protected Boolean hr;
	
	/** Indicates whether the employee is active or not within the company**/
	@Column(name="username")
	protected String userName;
	
	/** Employees Paygrade (Position/Salary) **/
	@ManyToOne
	@JoinColumn(name="paygrades")
	protected Paygrade payGrade;
	
	/** An employee Manager **/
	@ManyToOne
	protected Employee manager;

	/** The employees working under the manager **/
    @OneToMany(mappedBy="manager")
	protected Collection<Employee> peons;
    
    /** The employees flextime, can be no greater than 10 and no less than -10 **/
    @Column(name="flexTime", columnDefinition = "TINYINT(4)")
    protected Integer flexTime;
    
    /** The employees vaction time, in days remaining **/
    @Column(name="vacationTime", columnDefinition = "TINYINT(4)")
    protected Integer vacationTime;
    
    /** If this Employee is not a responsible engineer then collection should be empty. **/
    @OneToMany(mappedBy="responsibleEngineer", fetch = FetchType.EAGER)
    protected Collection<WorkPackage> packagesAssignedToRE;

    public Employee() {}

	public Employee(UUID id, Integer empNum, String firstName, String lastName, String userName,
	       Boolean active,  Boolean hr, Paygrade payGrade, Employee manager, Collection<Employee> peons, Integer flexTime, Integer vacationTime,
	       Collection<WorkPackage> packagesAssignedToRE) {
		super();
		this.id = id;
		this.empNum = empNum;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.active = active;
		this.hr = hr;
		this.payGrade = payGrade;
		this.manager = manager;
		this.peons = peons;
		this.flexTime = flexTime;
		this.vacationTime = vacationTime;
		this.packagesAssignedToRE = packagesAssignedToRE;
	}
	
	public Employee(CreateEmployeeBean ceb) {
		super();
		this.id = UUID.randomUUID();
		this.empNum = ceb.empNum;
		this.firstName = ceb.firstName;
		this.lastName = ceb.lastName;
		this.userName = ceb.userName;
		this.active = ceb.active;
		this.hr = ceb.hr;
		this.payGrade = ceb.payGrade;
		this.manager = ceb.manager;
		this.peons = ceb.peons;
		this.flexTime = ceb.flexTime;
		this.vacationTime = ceb.vacationTime;
		this.packagesAssignedToRE = ceb.packagesAssignedToRE;
	}

	// GETTERS AND SETTERS =============================================================================

    public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	public Integer getEmpNum() {
	    return empNum;
	}
	
	public void setEmpNum(Integer empNum) {
	    this.empNum = empNum;
	}
	
	public String getFirstName() {
	    return firstName;
	}
	
	public void setFirstName(String firstName) {
	    this.firstName = firstName;
	}
	
	public String getLastName() {
	    return lastName;
	}
	
	public void setLastName(String lastName) {
	    this.lastName = lastName;
	}
	
	public Boolean getActive() {
	    return active;
	}
	
	public void setActive(Boolean active) {
	    this.active = active;
	}
	
	public Boolean getHr() {
        return hr;
	}
	    
	public void setHr(Boolean hr) {
        this.hr = hr;
    }

	public Paygrade getPayGrade() {
	    return payGrade;
	}
	
	public void setPayGrade(Paygrade paygrade) {
	    this.payGrade = paygrade;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public Employee getManager() {
	    return manager;
	}

	public void setManager(Employee manager) {
	    this.manager = manager;
	}

	public Collection<Employee> getPeons() {
	    return peons;
	}

	public void setPeons(Collection<Employee> peons) {
	    this.peons = peons;
	}

	public Integer getFlextime() {
        return flexTime;
    }

	public void setFlextime(Integer flextime) {
	    if (flextime > 10) {
	        this.flexTime = 10;
	    } else if (flextime < -10) {
	        this.flexTime = -10;
	    } else {
	        this.flexTime = flextime;
	    }
	}
	
    public Integer getVacationTime() {
        return vacationTime;
    }

    public void setVacationTime(Integer vacationTime) {
        this.vacationTime = vacationTime;
    }

    /**
     * @return the packagesAssignedToRE
     */
    public Collection<WorkPackage> getPackagesAssignedToRE() {
        return packagesAssignedToRE;
    }

    /**
     * @param packagesAssignedToRE the packagesAssignedToRE to set
     */
    public void setPackagesAssignedToRE(Collection<WorkPackage> packagesAssignedToRE) {
        this.packagesAssignedToRE = packagesAssignedToRE;
    }
    
}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	