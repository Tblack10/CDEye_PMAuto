package CDEye_PMAuto.backend.timesheet;

import CDEye_PMAuto.backend.tsrow.EditableTimesheetRow;
import CDEye_PMAuto.backend.tsrow.TimesheetRow;
import CDEye_PMAuto.backend.tsrow.TimesheetRowManager;
import CDEye_PMAuto.backend.employee.Employee;
import CDEye_PMAuto.backend.employee.EmployeeManager;
import CDEye_PMAuto.backend.project.Project;
import CDEye_PMAuto.backend.project.ProjectManager;
import CDEye_PMAuto.backend.workpackage.WorkPackage;
import CDEye_PMAuto.backend.workpackage.WorkPackageManager;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Named("createTimesheet")
@ConversationScoped
public class CreateTimesheetBean extends Timesheet implements Serializable {

	@Inject TimesheetManager timesheetManager;
	@Inject ProjectManager projectManager;
	@Inject WorkPackageManager workPackageManager;
	@Inject EmployeeManager employeeManager;
	@Inject Conversation conversation;
	@Inject
	TimesheetRowManager rowManager;

	private LocalDate sheetDate;
	private List<EditableTimesheetRow> editableRows;

	public CreateTimesheetBean(){ }

	public String initialize(String activeUserName) {
		if (!conversation.isTransient()) conversation.end();
		conversation.begin();

		Employee activeEmployee = employeeManager.getEmployeeByUserName(activeUserName);
		this.setEmployee(activeEmployee);
		return "CreateTimesheet";
	}

	public String add() {
		timesheetManager.addTimesheet(new Timesheet(this));
		saveTimesheetRow();
		conversation.end();
//		if (!conversation.isTransient()) {
//			conversation.end();
//		}
		return "TimesheetList";
	}

	private void saveTimesheetRow() {
		for (EditableTimesheetRow er : getEditableRows()) {
			Project p = projectManager.findProjectByNum(er.getProjectNumber());
			WorkPackage[] wp = workPackageManager.findWpsByPkgNumAndProj(er.getWorkPackageNumber(), p);
			if (wp.length > 0) {
				er.setProject(p);
				er.setWorkPackage(wp[0]);
				er.setTimesheet(this);
				TimesheetRow tsr = new TimesheetRow(er);
				this.getDetails().add(tsr);
				rowManager.addRow(tsr);
			} else System.out.println("findWpsByPkgNumAndProj returns none.");
		}
	}

	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String pNum = value.toString();
		UIInput uiInputWorkPackageNumber = (UIInput) component.getAttributes().get("workPackageNumber");
		String wNum = uiInputWorkPackageNumber.getSubmittedValue().toString();

		Project p = projectManager.findProjectByNum(pNum);
		WorkPackage[] wp = workPackageManager.findWpsByPkgNumAndProj(wNum, p);
		if (wp.length == 0) {
			FacesMessage msg = new FacesMessage();
			msg.setDetail("The work package doesn't exists within given project");
			msg.setSummary("Work package not found");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}

	public void addRow() {
		if (editableRows == null) editableRows = new ArrayList<>();
		EditableTimesheetRow etr = new EditableTimesheetRow();
		etr.setProjects(projectManager.getAll());
		this.editableRows.add(etr);
	}

	public String back() {
		if (!conversation.isTransient()) conversation.end();
		return "HRHome";
	}

	public LocalDate getSheetDate() {
		return sheetDate;
	}

	public void setSheetDate(LocalDate sheetDate) {
		this.sheetDate = sheetDate;
	}

	public List<EditableTimesheetRow> getEditableRows() {
		return editableRows;
	}

	public void setEditableRows(List<EditableTimesheetRow> editableRows) {
		this.editableRows = editableRows;
	}
}
