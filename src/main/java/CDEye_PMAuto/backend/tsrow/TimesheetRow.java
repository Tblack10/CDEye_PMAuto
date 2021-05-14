package CDEye_PMAuto.backend.tsrow;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.*;

import CDEye_PMAuto.backend.project.Project;
import org.hibernate.annotations.Type;

import CDEye_PMAuto.backend.timesheet.Timesheet;
import CDEye_PMAuto.backend.workpackage.WorkPackage;

@Entity
@Table(name="timesheetrow")
public class TimesheetRow {
	@Id
	@Column(name="id")
	@Type(type = "uuid-char")
	protected UUID id;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="parent_sheet")
    protected Timesheet timesheet;

	@Column(name="comments")
	protected String comments;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="project")
	protected Project project;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="workpackage")
	protected WorkPackage workPackage;

	@Column(name="mon")
	protected BigDecimal mon;
	@Column(name="tue")
	protected BigDecimal tue;
	@Column(name="wed")
	protected BigDecimal wed;
	@Column(name="thu")
	protected BigDecimal thu;
	@Column(name="fri")
	protected BigDecimal fri;
	@Column(name="sat")
	protected BigDecimal sat;
	@Column(name="sun")
	protected BigDecimal sun;

	public TimesheetRow() {
		this.id = UUID.randomUUID();
		setFri(new BigDecimal(0));
		setSat(new BigDecimal(0));
		setSun(new BigDecimal(0));
		setMon(new BigDecimal(0));
		setTue(new BigDecimal(0));
		setWed(new BigDecimal(0));
		setThu(new BigDecimal(0));
	}

	public TimesheetRow(TimesheetRow row) {
		this.id = row.getId();
		this.timesheet = row.getTimesheet();
		this.comments = row.getComments();
		this.project = row.getProject();
		this.workPackage = row.getWorkPackage();
		this.fri = row.getFri();
		this.sat = row.getSat();
		this.sun = row.getSun();
		this.mon = row.getMon();
		this.tue = row.getTue();
		this.wed = row.getWed();
		this.thu = row.getThu();
	}

	public TimesheetRow(EditableTimesheetRow edited) {
		this.id = edited.getId();
		this.timesheet = edited.getTimesheet();
		this.comments = edited.getComments();
		this.project = edited.getProject();
		this.workPackage = edited.getWorkPackage();
		this.fri = edited.getFri();
		this.sat = edited.getSat();
		this.sun = edited.getSun();
		this.mon = edited.getMon();
		this.tue = edited.getTue();
		this.wed = edited.getWed();
		this.thu = edited.getThu();
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public WorkPackage getWorkPackage() {
		return workPackage;
	}

	public void setWorkPackage(WorkPackage workPackage) {
		this.workPackage = workPackage;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Timesheet getTimesheet() {
		return timesheet;
	}

	public void setTimesheet(Timesheet timesheet) {
		this.timesheet = timesheet;
	}

	public BigDecimal getMon() {
		return mon;
	}

	public void setMon(BigDecimal mon) {
		this.mon = mon;
	}

	public BigDecimal getTue() {
		return tue;
	}

	public void setTue(BigDecimal tue) {
		this.tue = tue;
	}

	public BigDecimal getWed() {
		return wed;
	}

	public void setWed(BigDecimal wed) {
		this.wed = wed;
	}

	public BigDecimal getThu() {
		return thu;
	}

	public void setThu(BigDecimal thu) {
		this.thu = thu;
	}

	public BigDecimal getFri() {
		return fri;
	}

	public void setFri(BigDecimal fri) {
		this.fri = fri;
	}

	public BigDecimal getSat() {
		return sat;
	}

	public void setSat(BigDecimal sat) {
		this.sat = sat;
	}

	public BigDecimal getSun() {
		return sun;
	}

	public void setSun(BigDecimal sun) {
		this.sun = sun;
	}
}
