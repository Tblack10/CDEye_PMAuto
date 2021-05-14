package CDEye_PMAuto.backend.timesheet;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("activeTimesheet")
@SessionScoped
public class ActiveTimesheetBean extends Timesheet implements Serializable {

	@Inject TimesheetList editableTimesheetList;

	public String setActiveTimesheetBean(EditableTimesheet ets) {
		this.id = ets.id;
		this.employee = ets.employee;
		this.endDate = ets.endDate;
		this.details = ets.details;
		this.sick = ets.sick;
		this.flex = ets.flex;
		this.approved = ets.approved;
		this.vacation = ets.vacation;
		editableTimesheetList.refreshList();
		return "TimesheetDetails";
	}
	
	public String setActiveTimesheetBeanManagerView(EditableTimesheet ets) {
        this.id = ets.id;
        this.employee = ets.employee;
        this.endDate = ets.endDate;
        this.details = ets.details;
        this.sick = ets.sick;
        this.flex = ets.flex;
        this.approved = ets.approved;
        this.vacation = ets.vacation;
        editableTimesheetList.filterForManagerViewUnapproved();
        return "TimesheetDetails";
    }
}
