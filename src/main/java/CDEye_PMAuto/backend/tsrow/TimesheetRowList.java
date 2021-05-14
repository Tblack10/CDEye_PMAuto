package CDEye_PMAuto.backend.tsrow;

import CDEye_PMAuto.backend.employee.Employee;
import CDEye_PMAuto.backend.employee.EmployeeManager;
import CDEye_PMAuto.backend.timesheet.ActiveTimesheetBean;
import CDEye_PMAuto.backend.timesheet.Timesheet;
import CDEye_PMAuto.backend.project.Project;
import CDEye_PMAuto.backend.project.ProjectManager;
import CDEye_PMAuto.backend.timesheet.TimesheetManager;
import CDEye_PMAuto.backend.workpackage.WorkPackage;
import CDEye_PMAuto.backend.workpackage.WorkPackageManager;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named
@ConversationScoped
public class TimesheetRowList implements Serializable {

    @Inject @Dependent TimesheetRowManager rowManager;
    @Inject TimesheetManager timesheetManager;
    @Inject ProjectManager projectManager;
    @Inject WorkPackageManager workPackageManager;
    @Inject EmployeeManager employeeManager;
    @Inject Conversation conversation;
    @Inject ActiveTimesheetBean activeTimesheet;

    private List<EditableTimesheetRow> rowList;
    private LocalDate endDate;
    private BigDecimal flex;
    private BigDecimal vacation;

    @PostConstruct
    public void init() {
        System.out.println("TimesheetDetails initializing...");
        if (rowList == null) refreshRowList();
        if (endDate == null) refreshTimesheetInfo();
        System.out.println("TimesheetDetails generated.");
    }

    public List<EditableTimesheetRow> getRowList() {
        if (!conversation.isTransient()) conversation.end();
        conversation.begin();
        if (rowList == null) refreshRowList();
        return rowList;
    }

    private void refreshTimesheetInfo() {
        Timesheet currentTimesheet = timesheetManager.find(activeTimesheet.getId());
        setEndDate(currentTimesheet.getEndDate());
        setVacation(currentTimesheet.getVacation());
        setFlex(currentTimesheet.getFlex());
    }

    public void refreshRowList() {
        TimesheetRow[] rows = rowManager.getRowsForTimesheet(timesheetManager.find(activeTimesheet.getId()));
        rowList = new ArrayList<>();
        for (TimesheetRow row : rows) {
            EditableTimesheetRow etr = new EditableTimesheetRow(row);
            etr.setProjects(projectManager.getAll());
            rowList.add(etr);
        }
    }

    public void addRow() {
        EditableTimesheetRow etr = new EditableTimesheetRow();
        etr.setEditable(true);
        etr.setTimesheet(timesheetManager.find(activeTimesheet.getId()));
        etr.setProjects(projectManager.getAll());
        rowList.add(etr);
    }

    public String save() {
        Timesheet currentSheet = timesheetManager.find(activeTimesheet.getId());
        currentSheet.setFlex(this.flex);
        currentSheet.setVacation(this.vacation);
        currentSheet.setEndDate(this.endDate);
        timesheetManager.updateTimesheet(currentSheet);
        for (EditableTimesheetRow edited : rowList) {
            if (edited.isDeletable()) {
                rowManager.deleteRow(new TimesheetRow(edited));
                continue;
            }
            Project p = projectManager.findProjectByNum(edited.getProjectNumber());
            WorkPackage[] wp = workPackageManager.findWpsByPkgNumAndProj(edited.getWorkPackageNumber(), p);
            if (wp.length > 0) {
                edited.setProject(p);
                edited.setWorkPackage(wp[0]);
                TimesheetRow row = new TimesheetRow(edited);
                rowManager.updateRow(row);
                edited.setEditable(false);
            } else System.out.println("Not found");
        }
        if (!conversation.isTransient()) {
            conversation.end();
        }
        return "";
    }

    private List<BigDecimal> getListOfHoursPerRow() {
        List<BigDecimal> listOfHoursPerRow = new ArrayList<>();
        for (EditableTimesheetRow row : rowList) {
            BigDecimal totalHours = BigDecimal.ZERO
                    .add(row.getFri())
                    .add(row.getMon())
                    .add(row.getSat())
                    .add(row.getSun())
                    .add(row.getThu())
                    .add(row.getTue())
                    .add(row.getWed());
            listOfHoursPerRow.add(totalHours);
        }
        return listOfHoursPerRow;
    }

    public String approve() {
        refreshRowList();
        List<BigDecimal> hoursList = getListOfHoursPerRow();
        BigDecimal totalHoursOfTimesheet = hoursList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalHoursOfTimesheet.add(flex).add(vacation).compareTo(BigDecimal.valueOf(40)) == 0) {
            Timesheet currentTimesheet = timesheetManager.find(activeTimesheet.getId());
            Employee owner = currentTimesheet.getEmployee();
            int count = 0;
            for (EditableTimesheetRow row : rowList) {
                BigDecimal hours = hoursList.get(count);
                WorkPackage workPackage = workPackageManager.find(row.getWorkPackage().getId());
                BigDecimal budgets = (workPackage.getCompletedBudget()!=null) ? workPackage.getCompletedBudget() : BigDecimal.ZERO;
                BigDecimal personDays = (workPackage.getCompletedPersonDays()!=null) ? workPackage.getCompletedPersonDays() : BigDecimal.ZERO;
                workPackage.setCompletedBudget(budgets.add(hours.multiply(owner.getPayGrade().getSalary())));
                workPackage.setCompletedPersonDays(personDays.add(hours));
                workPackageManager.updateWorkPackage(workPackage);
                count++;
            }
            currentTimesheet.setApproved(true);
            timesheetManager.updateTimesheet(currentTimesheet);
            Integer flex = owner.getFlextime();
            Integer vacation = owner.getVacationTime();
            owner.setFlextime(flex-this.flex.intValue());
            owner.setVacationTime(vacation-this.vacation.intValue());
            employeeManager.editEmployee(owner);
            if (!conversation.isTransient()) conversation.end();
            return "TimesheetList?faces-redirect=true";

        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", "The timesheet needs 40 hours total.");
            PrimeFaces.current().dialog().showMessageDynamic(message);
            return "";
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

    public String back() {
        if (!conversation.isTransient()) conversation.end();
        return "TimesheetList?faces-redirect=true";
    }

    public BigDecimal getFlex() {
        return flex;
    }

    public void setFlex(BigDecimal flex) {
        this.flex = flex;
    }

    public BigDecimal getVacation() {
        return vacation;
    }

    public void setVacation(BigDecimal vacation) {
        this.vacation = vacation;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
