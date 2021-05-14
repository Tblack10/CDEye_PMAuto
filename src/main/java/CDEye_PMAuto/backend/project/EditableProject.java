package CDEye_PMAuto.backend.project;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import CDEye_PMAuto.backend.employee.Employee;

@Named("editProject")
@RequestScoped
public class EditableProject extends Project implements Serializable {
	private boolean editable = false;
	private boolean deletable = false;
	private String panelId;

	public EditableProject() {
		super();
	}

	public EditableProject(Project p) {
		super(p.id, p.projectName, p.projectNumber, p.projManager, p.startDate, p.endDate, p.estimateBudget,
				p.markUpRate, p.projectBudget);
		panelId = p.projectName.replaceAll("\\s", "_");
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

	public String getPanelId() {
		return panelId;
	}

	public void setPanelId(String panelId) {
		this.panelId = panelId;
	}
}
