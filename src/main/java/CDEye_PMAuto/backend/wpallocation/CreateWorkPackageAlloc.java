package CDEye_PMAuto.backend.wpallocation;

import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import CDEye_PMAuto.backend.workpackage.WorkPackage;
import CDEye_PMAuto.backend.workpackage.WorkPackageManager;

@Named("createWorkPackageAlloc")
@RequestScoped
public class CreateWorkPackageAlloc extends WorkPackageAllocation {

	@Inject private WorkPackageAllocManager workPackageAllocManager;
	
	public String add() {
		WorkPackageAllocation wpa = new WorkPackageAllocation(this);
		workPackageAllocManager.addWorkPackageAlloc(wpa);
        return "";
    }
	
}
