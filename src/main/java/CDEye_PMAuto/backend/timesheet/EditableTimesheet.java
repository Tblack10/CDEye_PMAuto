package CDEye_PMAuto.backend.timesheet;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class EditableTimesheet extends Timesheet implements Serializable {
	private boolean editable;
	private boolean deletable;

	public EditableTimesheet() {}
	public EditableTimesheet(Timesheet t) {
		super(t);
	}

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

}
