package CDEye_PMAuto.backend.project;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import CDEye_PMAuto.backend.employee.Employee;

@Entity
@Table(name="projects")
@Named("project")
@DynamicUpdate
@SessionScoped
public class Project implements Serializable {

	@Transient
	@Inject @Dependent private ProjectManager projectManager;

	@Id
	@Column(name = "id")
	@Type(type = "uuid-char")
	protected UUID id;

	@Column(name = "projectname")
	protected String projectName;

	@Column(name = "projectnumber")
	protected String projectNumber;

	// CascadeType.ALL deletes projManager when deleting the project
	@ManyToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name = "projmanager")
	protected Employee projManager;

	@Column(name = "startdate")
	protected Date startDate;

	@Column(name = "enddate")
	protected Date endDate;

	@Column(name = "estimatebudget")
	protected BigDecimal estimateBudget;
	
	@Column(name = "markuprate")
	protected BigDecimal markUpRate;
	
	@Column(name = "projectbudget")
	protected BigDecimal projectBudget;

	public Project() {}
	
	public Project(Project p) {
		this.id = p.id;
		this.projectName = p.projectName;
		this.projectNumber = p.projectNumber;
		this.projManager = p.projManager;
		this.startDate = p.startDate;
		this.endDate = p.endDate;
		this.estimateBudget = p.estimateBudget;
		this.markUpRate = p.markUpRate;
		this.projectBudget = p.projectBudget;
	}

	public Project(UUID id, String projectName, String projectNumber, Employee projManager, Date startDate,
			Date endDate, BigDecimal estimateBudget, BigDecimal markUpRate, BigDecimal projectBudget) {
		super();
		this.id = id;
		this.projectName = projectName;
		this.projectNumber = projectNumber;
		this.projManager = projManager;
		this.startDate = startDate;
		this.endDate = endDate;
		this.estimateBudget = estimateBudget;
		this.markUpRate = markUpRate;
		this.projectBudget = projectBudget;
	}
	
	 public Project(CreateProjectBean cpb) {
	        super();
	        this.id = UUID.randomUUID();
	        this.projectNumber = cpb.projectNumber;
	        this.projectName = cpb.projectName;
	}
	 
	 public Project(ActiveProjectBean apb) {
	        super();
	        this.id = apb.id;
			this.projectName = apb.projectName;
			this.projectNumber = apb.projectNumber;
			this.projManager = apb.projManager;
			this.startDate = apb.startDate;
			this.endDate = apb.endDate;
			this.estimateBudget = apb.estimateBudget;
			this.markUpRate = apb.markUpRate;
			this.projectBudget = apb.projectBudget;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	public Employee getProjManager() {
		return projManager;
	}

	public void setProjManager(Employee projManager) {
		this.projManager = projManager;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getEstimateBudget() {
		return estimateBudget;
	}

	public void setEstimateBudget(BigDecimal estimateBudget) {
		this.estimateBudget = estimateBudget;
	}

	public BigDecimal getMarkUpRate() {
		return markUpRate;
	}

	public void setMarkUpRate(BigDecimal markUpRate) {
		this.markUpRate = markUpRate;
	}

	public BigDecimal getProjectBudget() {
		return projectBudget;
	}

	public void setProjectBudget(BigDecimal projectBudget) {
		this.projectBudget = projectBudget;
	}

	

}
