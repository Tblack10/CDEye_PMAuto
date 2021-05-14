package CDEye_PMAuto.backend.wpallocation;

import java.math.BigDecimal;
import java.util.UUID;

import CDEye_PMAuto.backend.paygrade.Paygrade;
import CDEye_PMAuto.backend.workpackage.WorkPackage;

public class EditableWorkPackageAlloc extends WorkPackageAllocation {
	private boolean editable = false;
	private boolean deletable = false;
	
	public EditableWorkPackageAlloc() {}
	
	public EditableWorkPackageAlloc(UUID id, WorkPackage workPackage, Paygrade paygrade, BigDecimal personDaysEstimate) {
		super();
		this.id = id;
		this.workPackage = workPackage;
		this.paygrade = paygrade;
		this.personDaysEstimate = personDaysEstimate;
	}
	
	public EditableWorkPackageAlloc(WorkPackageAllocation wpa) {
		super();
		this.id = wpa.id;
		this.workPackage = wpa.workPackage;
		this.paygrade = wpa.paygrade;
		this.personDaysEstimate = wpa.personDaysEstimate;
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
