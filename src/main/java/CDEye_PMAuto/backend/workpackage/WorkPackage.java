package CDEye_PMAuto.backend.workpackage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import CDEye_PMAuto.backend.employee.Employee;
import CDEye_PMAuto.backend.project.Project;
import CDEye_PMAuto.backend.recepackage.EditableRECE;
import CDEye_PMAuto.backend.recepackage.RespEngCostEstimate;
import CDEye_PMAuto.backend.wpallocation.WorkPackageAllocation;

@Entity
@Table(name="workpackages")
@Named("workPackage")
@SessionScoped
public class WorkPackage implements Serializable {

	@Id
	@Column(name="id")
	@Type(type = "uuid-char")
	protected UUID id;
	
	/** Number of the package ex: 12100. */
	@Column(name="workpackagenumber")
	protected String workPackageNumber;
	
	/** Parent WorkPackage. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="parentworkpackage")
	protected WorkPackage parentWp;
	
	@Column(name="projectbudget")
	protected BigDecimal projectBudget;
	
    /**
     * Budget sum from this and all children WP that has been used up / is payable to the
     * employees on the WP for their effort based on timesheets they submitted.
     */
	@Column(name="completedbudget")
    protected BigDecimal completedBudget; // sum
	
	/**
     * Person day sum from this and all children WP that has been used up / accumulated by the
     * employees on the WP for their effort based on timesheets they submitted.
     */
	@Column(name="completedpersondays")
	protected BigDecimal completedPersonDays; //sum
	
    /** Start date of the package. */
	@Column(name="startdate")
    protected Date startDate;
    
	/** End date of the package. */
    @Column(name="enddate")
    protected Date endDate;
    
    /** Boolean for identifying if the package has children or not. */
	@Column(name="isleaf")
	protected boolean isLeaf;

	@ManyToOne
	@JoinColumn(name="project")
	protected Project project;
	
	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(mappedBy="workPackage", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	protected List<RespEngCostEstimate> RECEs;
	
	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(mappedBy="workPackage", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    protected List<WorkPackageAllocation> wpAllocs;
	
	@Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(mappedBy="parentWp", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	protected List<WorkPackage> childPackages;
	
	@ManyToOne
    @JoinColumn(name="responsibleengineer")
    protected Employee responsibleEngineer;
	
	/**
	 * Default no parameter constructor.
	 */
    public WorkPackage() {
        super();
    }

    /**
     * Constructor with all parameters.
     */
    public WorkPackage(UUID id, String workPackageNumber, WorkPackage parentWp, BigDecimal completedBudget, BigDecimal completedPersonDays,
            Date startDate,
            Date endDate, boolean isLeaf, BigDecimal projectBudget, Project project, List<RespEngCostEstimate> RECEs,
            List<WorkPackageAllocation> wpAllocs, List<WorkPackage> childPackages, Employee responsibleEngineer) {
        super();
        this.id = id;
        this.workPackageNumber = workPackageNumber;
        this.parentWp = parentWp;
        this.completedBudget = completedBudget;
        this.completedPersonDays = completedPersonDays;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isLeaf = isLeaf;
        this.projectBudget = projectBudget;
        this.project = project;
        this.RECEs = RECEs;
        this.wpAllocs = wpAllocs;
        this.childPackages = childPackages;
        this.responsibleEngineer = responsibleEngineer;
    }

    /**
     * Constructor without the id parameter.
     */
    public WorkPackage(String workPackageNumber, WorkPackage parentWp, BigDecimal completedBudget, BigDecimal completedPersonDays,
            Date startDate,
            Date endDate, boolean isLeaf, BigDecimal projectBudget, Project project, List<RespEngCostEstimate> RECEs,
            List<WorkPackageAllocation> wpAllocs, List<WorkPackage> childPackages, Employee responsibleEngineer) {
        super();
        this.id = UUID.randomUUID();
        this.workPackageNumber = workPackageNumber;
        this.parentWp = parentWp;
        this.completedBudget = completedBudget;
        this.completedPersonDays = completedPersonDays;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isLeaf = isLeaf;
        this.projectBudget = projectBudget;
        this.project = project;
        this.RECEs = RECEs;
        this.wpAllocs = wpAllocs;
        this.childPackages = childPackages;
        this.responsibleEngineer = responsibleEngineer;
    }
    
    /**
     * Constructor that accepts a new work package.
     */
    public WorkPackage(NewWorkPackage wp) {
        super();
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
    }
    
    /**
     * Constructor that accepts another work package.
     */
    public WorkPackage(WorkPackage wp) {
        super();
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
    }
    
    /**
     * Constructor that accepts an edited work package.
     */
    public WorkPackage(EditableWorkPackage wp) {
        super();
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
    }
    
    public WorkPackage(ActiveWPBean wp) {
        super();
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
    }
    
    /**
     * Sets all variables to the ones in the passed in work package.
     */
    public void usePackage(WorkPackage wp) {
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
    }
    
    public BigDecimal calcAllocatedBudget() {
    	if (this.isLeaf) {
    		BigDecimal personDays = new BigDecimal(0);
            BigDecimal budgetEstimate = new BigDecimal(0);
            for (WorkPackageAllocation w : this.getWpAllocs()) {
                BigDecimal salary = w.getPaygrade().getSalary();
                BigDecimal days = w.getPersonDaysEstimate();
                BigDecimal res = salary.multiply(days);

                budgetEstimate = budgetEstimate.add(res);
                personDays = personDays.add(w.getPersonDaysEstimate());
            }
            return budgetEstimate;
    	} else {
    		BigDecimal budgetEstimate = new BigDecimal(0); 
    		for (WorkPackage w : this.childPackages) {
                budgetEstimate = budgetEstimate.add(w.projectBudget);
            }
            return budgetEstimate;
    	}
    }
    
    public BigDecimal calcCompletedBudget() {
    	if (this.isLeaf) {
    		if(completedBudget == null) {
                completedBudget = new BigDecimal(0);
            }
    		return completedBudget;
    	} else {
    		
    		BigDecimal budgetEstimate = new BigDecimal(0); 
    		for (WorkPackage w : this.childPackages) {
    			if (w.completedBudget == null) {
                    w.completedBudget = new BigDecimal(0);
                }
                budgetEstimate = budgetEstimate.add(w.calcCompletedBudget());
            }
            return budgetEstimate;
    	}	
    }
    
    public BigDecimal calcUnallocatedBudget() {
    	return this.projectBudget.subtract(calcAllocatedBudget());
    }

    public BigDecimal calcAllocatedPersonDays() {
    	BigDecimal personDays = new BigDecimal(0);
        BigDecimal budgetEstimate = new BigDecimal(0);
        for (WorkPackageAllocation w : this.getWpAllocs()) {
            BigDecimal salary = w.getPaygrade().getSalary();
            BigDecimal days = w.getPersonDaysEstimate();
            BigDecimal res = salary.multiply(days);

            budgetEstimate = budgetEstimate.add(res);
            personDays = personDays.add(w.getPersonDaysEstimate());
        }
        return personDays;
    }
    
    public BigDecimal calcRespEngBudget() {
    	//If lowest level
    	if (this.isLeaf()) {
    		//sum RECEs
    		BigDecimal personDays = new BigDecimal(0);
            BigDecimal budgetEstimate = new BigDecimal(0);
            for (RespEngCostEstimate r : this.RECEs) {
                BigDecimal salary = r.getPaygrade().getSalary();
                BigDecimal days = r.getPersonDayEstimate();
                BigDecimal res = salary.multiply(days);

                budgetEstimate = budgetEstimate.add(res);
                personDays = personDays.add(r.getPersonDayEstimate());
            }
            return budgetEstimate;
            //If not lowest level
    	} else {
    		//Sum respEngBudgetOfChildren
    		BigDecimal budgetEstimate = new BigDecimal(0);
    		for (WorkPackage w : this.childPackages) {
                budgetEstimate = budgetEstimate.add(w.calcRespEngBudget());
            }
            return budgetEstimate;
    	}
    }
    
    public BigDecimal calcCompletion() {
//        if(completedBudget == null) {
//            completedBudget = new BigDecimal(0);
//        }
    	completedBudget = calcCompletedBudget();
        if(completedBudget.compareTo(BigDecimal.ZERO) == 0 || calcRespEngBudget().compareTo(BigDecimal.ZERO) == 0) {
            return new BigDecimal(0);
        }
        completedBudget = calcCompletedBudget();
    	return calcRespEngBudget().divide(completedBudget, 2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal calcVariance() {
    	BigDecimal difference = calcRespEngBudget().subtract(projectBudget);
    	
    	if(difference.compareTo(BigDecimal.ZERO) == 0 || projectBudget.compareTo(BigDecimal.ZERO) == 0) {
    	    return new BigDecimal(0);
    	}
    	return difference.divide(projectBudget, 2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal calcRespEngPersonDays() {
    	BigDecimal personDays = new BigDecimal(0);
        BigDecimal budgetEstimate = new BigDecimal(0);
        for (RespEngCostEstimate r : this.RECEs) {
            BigDecimal salary = r.getPaygrade().getSalary();
            BigDecimal days = r.getPersonDayEstimate();
            BigDecimal res = salary.multiply(days);

            budgetEstimate = budgetEstimate.add(res);
            personDays = personDays.add(r.getPersonDayEstimate());
        }
        return personDays;
    }
    
    public ArrayList<EditableRECE> getEditableRECEs() {
    	ArrayList<EditableRECE> editableRECEs = new ArrayList<EditableRECE>();
    	for (RespEngCostEstimate rece : this.RECEs) {
    		editableRECEs.add(new EditableRECE(rece));
    	}
    	return editableRECEs;
    }
    
    
    /**
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * @return the workPackageNumber
     */
    public String getWorkPackageNumber() {
        return workPackageNumber;
    }

    /**
     * @param workPackageNumber the workPackageNumber to set
     */
    public void setWorkPackageNumber(String workPackageNumber) {
        this.workPackageNumber = workPackageNumber;
    }

    /**
     * @return the parentWp
     */
    public WorkPackage getParentWp() {
        return parentWp;
    }

    /**
     * @param parentWp the parentWp to set
     */
    public void setParentWp(WorkPackage parentWp) {
        this.parentWp = parentWp;
    }

    /**
     * @return the completedBudget
     */
    public BigDecimal getCompletedBudget() {
        return completedBudget;
    }

    /**
     * @param completedBudget the completedBudget to set
     */
    public void setCompletedBudget(BigDecimal completedBudget) {
        this.completedBudget = completedBudget;
    }

    /**
     * @return the completedPersonDays
     */
    public BigDecimal getCompletedPersonDays() {
        return completedPersonDays;
    }

    /**
     * @param completedPersonDays the completedPersonDays to set
     */
    public void setCompletedPersonDays(BigDecimal completedPersonDays) {
        this.completedPersonDays = completedPersonDays;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the isLeaf
     */
    public boolean isLeaf() {
        return isLeaf;
    }

    /**
     * @param isLeaf the isLeaf to set
     */
    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

	public BigDecimal isProjectBudget() {
		return projectBudget;
	}

	public void setProjectBudget(BigDecimal projectBudget) {
		this.projectBudget = projectBudget;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public BigDecimal getProjectBudget() {
		return projectBudget;
	}

    /**
     * @return the rECEs
     */
    public List<RespEngCostEstimate> getRECEs() {
        return RECEs;
    }

    /**
     * @param rECEs the rECEs to set
     */
    public void setRECEs(List<RespEngCostEstimate> rECEs) {
        RECEs = rECEs;
    }

    /**
     * @return the wpAllocs
     */
    public List<WorkPackageAllocation> getWpAllocs() {
        return wpAllocs;
    }

    /**
     * @param wpAllocs the wpAllocs to set
     */
    public void setWpAllocs(List<WorkPackageAllocation> wpAllocs) {
        this.wpAllocs = wpAllocs;
    }

    /**
     * @return the responsibleEngineer
     */
    public Employee getResponsibleEngineer() {
        return responsibleEngineer;
    }

    /**
     * @param responsibleEngineer the responsibleEngineer to set
     */
    public void setResponsibleEngineer(Employee responsibleEngineer) {
        this.responsibleEngineer = responsibleEngineer;
    }

    /**
     * @return the childPackages
     */
    public List<WorkPackage> getChildPackages() {
        return childPackages;
    }

    /**
     * @param childPackages the childPackages to set
     */
    public void setChildPackages(List<WorkPackage> childPackages) {
        this.childPackages = childPackages;
    }
	
	
    
}
