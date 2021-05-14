package CDEye_PMAuto.backend.employee;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import CDEye_PMAuto.backend.credentials.Credential;
import CDEye_PMAuto.backend.paygrade.Paygrade;
import CDEye_PMAuto.backend.paygrade.PaygradeManager;
import CDEye_PMAuto.backend.workpackage.WorkPackage;

import org.hibernate.sql.Select;

import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

@Named("editEmployee")
@RequestScoped
public class EditableEmployee extends Employee implements Serializable {
	private boolean editable;
	private boolean deletable;

	@Inject
	private PaygradeManager paygradeManager;

	private String paygradeName;

	public String getPaygradeName() {
		return paygradeName;
	}
	public void setPaygradeName(String paygradeName) {
		this.payGrade = paygradeManager.findPaygrade(paygradeName);
		this.paygradeName = paygradeName;
	}
	public enum PAYGRADE {
		P1,
		P2,
		P3,
		P4,
		P5,
		P6,
		DS,
		JS,
		XS,
	}

	/**
	 * HR dropdown list
	 */
	private static Collection<SelectItem> hrList;
	static {
		hrList = new ArrayList<SelectItem>();
		hrList.add(new SelectItem(true));
		hrList.add(new SelectItem(false));
	}

	/**
	 * Active dropdown list
	 */
	private static Collection<SelectItem> activeList;
	static {
		activeList = new ArrayList<SelectItem>();
		activeList.add(new SelectItem(true));
		activeList.add(new SelectItem(false));
	}

	/**
	 * Paygrade dropdown list
	 */
	private static Collection<SelectItem> paygradeList;
	static {
		paygradeList = new ArrayList<SelectItem>();

		for (PAYGRADE p : PAYGRADE.values()) {
			paygradeList.add(new SelectItem((p)));
		}
	}
	public Collection<SelectItem> getPaygradeItems() {
		return paygradeList;
	}
	public Collection<SelectItem> getHRItems() {
		return hrList;
	}
	public Collection<SelectItem> getActiveItems() {
		return activeList;
	}

	public EditableEmployee() {
		super();
	}

	public EditableEmployee(Employee e) {
		super(e.id, e.empNum, e.firstName, e.lastName, e.userName, e.active, e.hr, e.payGrade, e.manager, e.peons, e.flexTime, e.vacationTime,
		        e.packagesAssignedToRE);
	}

	public EditableEmployee(UUID id, Integer empNum, String firstName, String lastName, 
	        String userName, Boolean active, Boolean hr, Paygrade payGrade, Employee manager, 
	        Collection<Employee> peons, Integer flexTime, Integer vacationTime, Collection<WorkPackage> packagesAssignedToRE) {
		super(id, empNum, firstName, lastName, userName, active, hr, payGrade, manager, peons, flexTime, vacationTime,
		        packagesAssignedToRE);
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
