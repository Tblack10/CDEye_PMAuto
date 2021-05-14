package CDEye_PMAuto.backend.paygrade;

import CDEye_PMAuto.backend.employee.EditableEmployee;

import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class EditablePaygrade extends Paygrade implements Serializable {
	private boolean editable = false;
	private boolean deletable = false;
	
	public EditablePaygrade() {
		super();
	}
	
	public EditablePaygrade(Paygrade p) {
		super(p.id, p.name, p.salary);
	}
	
	public EditablePaygrade(UUID id, String name, BigDecimal salary) {
		super(id, name, salary);
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
	
}
