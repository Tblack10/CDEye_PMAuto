package CDEye_PMAuto.backend.timesheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import CDEye_PMAuto.backend.employee.ActiveEmployeeBean;
import CDEye_PMAuto.backend.employee.Employee;

@Named
@ConversationScoped
public class TimesheetList implements Serializable {

	@Inject 
	@Dependent 
	private TimesheetManager timesheetManager;
	
	@Inject 
    ActiveEmployeeBean activeEmployee;
	
	@Inject 
	Conversation conversation;
	
	private List<EditableTimesheet> list;
	private List<EditableTimesheet> managerViewList;
	
	public List<EditableTimesheet> getList() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
		conversation.begin();
		if (list == null) refreshList();
        return list;
    }
	
	public List<EditableTimesheet> getManagerViewList() {
        if (!conversation.isTransient()) {
            conversation.end();
        }
        conversation.begin();
        if (managerViewList == null) {
            filterForManagerViewUnapproved();
        }
        return managerViewList;
    }

	public List<EditableTimesheet> refreshList() {
		Timesheet[] timesheets = timesheetManager.getAllForCurrentEmployee();
        list = new ArrayList<EditableTimesheet>();
		for (Timesheet timesheet : timesheets) {
			list.add(new EditableTimesheet(timesheet));
		}
        return list;
    }
	
	public List<EditableTimesheet> filterForManagerViewUnapproved() {
	    List<EditableTimesheet> filtered = new ArrayList<EditableTimesheet>();
	    Timesheet[] allSheets = timesheetManager.getAll();
	    for (Timesheet sheet : allSheets) {
	        Employee manager = sheet.getEmployee().getManager();
	        if (manager != null && manager.getUserName().equals(activeEmployee.getUserName()) && !sheet.isApproved())
	            filtered.add(new EditableTimesheet(sheet));
	    }
	    managerViewList = filtered;
	    return filtered;
	}

	private void updateTimesheetDB(List<EditableTimesheet> list) {
		for (EditableTimesheet editableTimesheet : list) {
			if (editableTimesheet.isEditable()) {
				Timesheet t = new Timesheet(editableTimesheet);
				timesheetManager.updateTimesheet(t);
				editableTimesheet.setEditable(false);
			}
			if (editableTimesheet.isDeletable()) {
				Timesheet t = new Timesheet(editableTimesheet);
				timesheetManager.deleteTimesheet(t);
			}
		}
	}

	public String save() {
		updateTimesheetDB(list);
		refreshList();
		if (!conversation.isTransient()) {
			conversation.end();
		}
		return "TimesheetList";
	}

	public String saveForManagerView() {
		updateTimesheetDB(managerViewList);
		filterForManagerViewUnapproved();
        if (!conversation.isTransient()) {
            conversation.end();
        }
        return "TimesheetListManagerView";
    }

	public String back() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
		return activeEmployee.getHr() ? "HRHome" : "Home";
	}
}
