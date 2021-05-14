package CDEye_PMAuto.backend.workpackage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import CDEye_PMAuto.backend.paygrade.Paygrade;
import CDEye_PMAuto.backend.paygrade.PaygradeManager;
import CDEye_PMAuto.backend.project.ActiveProjectBean;
import CDEye_PMAuto.backend.project.Project;
import CDEye_PMAuto.backend.project.ProjectManager;
import CDEye_PMAuto.backend.recepackage.RECEManager;
import CDEye_PMAuto.backend.recepackage.RespEngCostEstimate;
import CDEye_PMAuto.backend.wpallocation.WorkPackageAllocManager;
import CDEye_PMAuto.backend.wpallocation.WorkPackageAllocation;

/**
 * For creating new work packages and temporarily storing their information.
 */
@Named("newWorkPackage")
@RequestScoped
public class NewWorkPackage extends WorkPackage implements Serializable {

	@Inject
	private WorkPackageManager workPackageManager;
	@Inject
	ActiveProjectBean apb;
	@Inject
	PaygradeManager pgm;
	@Inject
	WorkPackageAllocManager wpam;
	@Inject
	RECEManager recem;
	@Inject
	ProjectManager pm;
	@Inject
	WorkPackageList wpl;

	String parentWpNumber = "";

	public Boolean validateWorkPackageNumber() {
		if (workPackageManager.getByPackageNumber(workPackageNumber).length != 0) {
			System.out.println("already existing workPackage.");
			return false;
		}
		return true;
	}

	public void validateWPNum(FacesContext context, UIComponent comp, Object value) {
		String workPackageNumber = (String) value;

		if (workPackageManager.getByPackageNumber(workPackageNumber).length != 0) {
			((UIInput) comp).setValid(false);
			FacesMessage message = new FacesMessage("already existing workPackage");
			context.addMessage(comp.getClientId(context), message);
		}
	}

	public ArrayList<String> getListOfMissingParents(String localParentWpNumber, Project activeProj) {
		ArrayList<String> result = new ArrayList<String>();
		WorkPackage[] parentWp = workPackageManager.findWpsByPkgNumAndProj(localParentWpNumber, activeProj);
		while (parentWp.length == 0) {
			result.add(localParentWpNumber);
			localParentWpNumber = workPackageManager.determineParentWPNum(localParentWpNumber);
			parentWp = workPackageManager.findWpsByPkgNumAndProj(localParentWpNumber, activeProj);
			if (parentWp.length > 0) {
				break;
			}
			System.out.println(" --------------------------- ");
		}
		Collections.reverse(result);
		return result;
	}
	
	public void createWps(ArrayList<String> listOfNeededParents) {
		Project activeProj = new Project();
		activeProj = pm.find(apb.getId());
		//find the first parent wp num
		String parentWpNum = workPackageManager.determineParentWPNum(listOfNeededParents.get(0));
		//Leave the last wp to be added as "this"
		for (int i = 0; i < listOfNeededParents.size() - 1; i++) {
			WorkPackage[] parentWp = workPackageManager.findWpsByPkgNumAndProj(parentWpNum, activeProj);
			
			WorkPackage newWorkPackage = new WorkPackage(listOfNeededParents.get(i), parentWp[0], BigDecimal.valueOf(0),
					BigDecimal.valueOf(0), new Date(), new Date(), false, BigDecimal.valueOf(0), activeProj, null, null,
					null, null);
			newWorkPackage.setId(UUID.randomUUID());
			workPackageManager.addWorkPackage(newWorkPackage);
			System.out.println("added " + listOfNeededParents.get(i));
			WorkPackage addedWp = workPackageManager.getByUUID(newWorkPackage.getId().toString());
			createWpAllocs(addedWp);
			createRECEs(addedWp);
			parentWpNum = listOfNeededParents.get(i);
		}
		//FOR THE LAST WP
		WorkPackage[] parentWp = workPackageManager.findWpsByPkgNumAndProj(parentWpNum, activeProj);
		WorkPackage wp = new WorkPackage(this);
		wp.setProject(activeProj);
		wp.setId(UUID.randomUUID());
		wp.setLeaf(wp.isLeaf);
		wp.setParentWp(parentWp[0]);
		workPackageManager.addWorkPackage(wp);
		System.out.println("added " + listOfNeededParents.get(listOfNeededParents.size() - 1));
		WorkPackage addedWp = workPackageManager.getByUUID(wp.getId().toString());
		createWpAllocs(addedWp);
		createRECEs(addedWp);
	}
	
	/**
	 * Used to persist the new work package.
	 */
	public String add() {
		if (this.endDate.before(this.startDate)) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage("Start Date is before End Date");
			facesContext.addMessage("beginDate", facesMessage);
			return null;
		}

		Project activeProj = new Project();

		activeProj = pm.find(apb.getId());

		ArrayList<String> listOfNeededParents = getListOfMissingParents(workPackageNumber, activeProj);
		
		createWps(listOfNeededParents);
		
//		WorkPackage[] parentWp = workPackageManager.findWpsByPkgNumAndProj(parentWpNumber, activeProj);
//		if (parentWp.length == 0) {
//			System.out.println("running conditional add");
//			String testParentWPNum = workPackageManager.determineParentWPNum(workPackageNumber);
//			
//			checkAndCreateWP(testParentWPNum);
//			
//			// Parent should now be created, check for a parent again
//			parentWp = workPackageManager.findWpsByPkgNumAndProj(testParentWPNum, activeProj);
//		}
//
//		WorkPackage wp = new WorkPackage(this);
//		wp.setProject(activeProj);
//		wp.setId(UUID.randomUUID());
//		wp.setLeaf(wp.isLeaf);
//		System.out.println("before setting parent committed work package " + wp.getId().toString() + " wpnum "
//				+ wp.getWorkPackageNumber());
//		wp.setParentWp(parentWp[0]);
//
//		workPackageManager.addWorkPackage(wp);
//
//		WorkPackage addedWp = workPackageManager.getByUUID(wp.getId().toString());
//		System.out.println("committed work package " + wp.getId().toString() + " wpnum " + wp.getWorkPackageNumber());
//		createWpAllocs(addedWp);
//		createRECEs(addedWp);
		System.out.println("about to refreshing wplist");
		wpl.refreshList();
		System.out.println("after refreshing wplist");
		return "WPList";
	}

	/**
	 * A RECURSIVE function that checks to see if a WorkPackage has a parent, if
	 * not, check parent to see if the parent has a parent. Once the function finds
	 * a WorkPackage with a parent, creates the desired WorkPackage
	 *
	 * @param wpToCreate, the WorkPackage number, as a String, of the WorkPackage to
	 *                    create
	 */
	public void checkAndCreateWP(String wpToCreate) {

		// check that wp does not exist in project

		Project activeProj = new Project();
		activeProj = pm.find(apb.getId());
		// Determines the wpNum of the WorkPackage that is SUPPOSED to exist
		String schrodingersWp = workPackageManager.determineParentWPNum(wpToCreate);
		System.out.println("shrodingers package num " + schrodingersWp);

		// Checks to see if the supposed to exist WP really does exist
		WorkPackage[] arrayOfParentWPs = workPackageManager.findWpsByPkgNumAndProj(schrodingersWp, activeProj);
		System.out.println("after getting parent wps");
		// Checks to see if parentless WorkPackages parent has a WP
		if (arrayOfParentWPs.length == 0) {
			System.out.println("running conditional cac");
			checkAndCreateWP(schrodingersWp);
			
			// Parent should now be created, check for a parent again
			arrayOfParentWPs = workPackageManager.findWpsByPkgNumAndProj(schrodingersWp, activeProj);
		}
		System.out.println("after conditional" + wpToCreate);
		WorkPackage newWorkPackage = new WorkPackage(wpToCreate, arrayOfParentWPs[0], BigDecimal.valueOf(0),
				BigDecimal.valueOf(0), new Date(), new Date(), false, BigDecimal.valueOf(0), activeProj, null, null,
				null, null);

		newWorkPackage.setId(UUID.randomUUID());
		newWorkPackage.setProject(activeProj);
		workPackageManager.addWorkPackage(newWorkPackage);
		System.out.println("after addition" + newWorkPackage.workPackageNumber);
		WorkPackage addedWp = workPackageManager.getByUUID(newWorkPackage.getId().toString());
		createWpAllocs(addedWp);
		createRECEs(addedWp);
	}

	// create a wpalloc for each paygrade
	public void createWpAllocs(WorkPackage addedWp) {
		Paygrade[] paygradeArr = pgm.getAll();
		for (Paygrade p : paygradeArr) {
			WorkPackageAllocation wpa = new WorkPackageAllocation();
			wpa.setId(UUID.randomUUID());
			wpa.setWorkPackage(addedWp);
			wpa.setPaygrade(p);
			wpa.setPersonDaysEstimate(new BigDecimal(0));
			wpam.addWorkPackageAlloc(wpa);
		}
	}

	// create a RECE for each paygrade
	public void createRECEs(WorkPackage addedWp) {
		Paygrade[] paygradeArr = pgm.getAll();
		for (Paygrade p : paygradeArr) {
			RespEngCostEstimate rece = new RespEngCostEstimate();
			rece.setId(UUID.randomUUID());
			rece.setWorkPackage(addedWp);
			rece.setPaygrade(p);
			rece.setPersonDayEstimate(new BigDecimal(0));
			recem.persist(rece);
		}
	}

	/**
	 * Gets the child WP number from a parent WP on keyup event from
	 * testCreateWP.xhtml
	 */
	public void ajaxEvent() {
		System.out.println("Called: " + parentWpNumber);
		workPackageNumber = workPackageManager.determineChildWPWithoutZeroes(parentWpNumber);
	}

	public String getParentWpNumber() {
		return parentWpNumber;
	}

	public void setParentWpNumber(String parentWpNumber) {
		this.parentWpNumber = parentWpNumber;
	}

}
