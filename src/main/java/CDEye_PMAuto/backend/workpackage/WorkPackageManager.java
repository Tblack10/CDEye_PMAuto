package CDEye_PMAuto.backend.workpackage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import CDEye_PMAuto.backend.project.Project;
import CDEye_PMAuto.backend.recepackage.RECEManager;
import CDEye_PMAuto.backend.recepackage.RespEngCostEstimate;
import CDEye_PMAuto.backend.tsrow.TimesheetRowManager;
import CDEye_PMAuto.backend.wpallocation.WorkPackageAllocManager;
import CDEye_PMAuto.backend.wpallocation.WorkPackageAllocation;

@Dependent
@Stateless
public class WorkPackageManager implements Serializable {
    
    @PersistenceContext(unitName="inventory-jpa") 
    EntityManager em;
    
    @Inject 
    @Dependent 
    private WorkPackageAllocManager workPackageAllocManager;
    
    @Inject 
    @Dependent 
    private RECEManager receManager;
    
    @Inject 
    @Dependent 
    private TimesheetRowManager timesheetRowManager;
    
    /**
     * Gets all the work packages in the DB.
     * @return an array of packages
     */
    public WorkPackage[] getAll() {
        TypedQuery<WorkPackage> query = em.createQuery("select wp from WorkPackage wp",
                WorkPackage.class);
        return getWorkPackages(query);
    }
    
    /**
     * Gets a workPackage by UUID number.
     * 
     * @param uuid string id
     * @return workPackage workPackage
     */
    public WorkPackage getByUUID(String uuid) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<WorkPackage> criteriaQuery = criteriaBuilder.createQuery(WorkPackage.class);
        Root<WorkPackage> itemRoot = criteriaQuery.from(WorkPackage.class);
        
        UUID uuidAsString = UUID.fromString(uuid);
        
        Predicate predicateForName = criteriaBuilder.equal(itemRoot.get("id"), uuidAsString);
        
        criteriaQuery.where(predicateForName);
        
        List<WorkPackage> workPackage = em.createQuery(criteriaQuery).getResultList();

        return workPackage.get(0);
    }
    
    public WorkPackage[] getByPackageNumber(String packageNumber) {
        TypedQuery<WorkPackage> query = em.createQuery(
                "SELECT wp FROM WorkPackage wp WHERE workpackagenumber LIKE :packageNumber", WorkPackage.class)
                .setParameter("packageNumber", "%" + packageNumber + "%");
        return getWorkPackages(query);
    }
    
    public WorkPackage[] getByParentId(String parentId) {
        UUID parentUUID = UUID.fromString(parentId);
        TypedQuery<WorkPackage> query = em.createQuery(
                "SELECT wp FROM WorkPackage wp WHERE wp.parentWp.id = :parentUUID", WorkPackage.class)
                .setParameter("parentUUID", parentUUID);
        return getWorkPackages(query);
    }
    
    public WorkPackage[] findWpsByProject(Project p) {
        TypedQuery<WorkPackage> query = em.createQuery(
                "SELECT wp FROM WorkPackage wp WHERE wp.project.id = :projectId ORDER BY wp.workPackageNumber ASC", WorkPackage.class)
                .setParameter("projectId", p.getId());
        return getWorkPackages(query);
    }
    
    public WorkPackage[] findWpsByPkgNumAndProj(String workPackageNumber, Project p) {
    	System.out.println("the wp num being looked up is " + workPackageNumber);
    	TypedQuery<WorkPackage> query = em.createQuery(
                "SELECT wp FROM WorkPackage wp WHERE wp.workPackageNumber = :workPackageNumber AND wp.project.id = :projectId", WorkPackage.class)
    			.setParameter("workPackageNumber", workPackageNumber)
    			.setParameter("projectId", p.getId());
        return getWorkPackages(query);
    }

    private WorkPackage[] getWorkPackages(TypedQuery<WorkPackage> query) {
        List<WorkPackage> workPackages = query.getResultList();
        WorkPackage[] packageArr = new WorkPackage[workPackages.size()];
        for (int i = 0; i < packageArr.length; i++) {
            packageArr[i] = workPackages.get(i);
        }
        
        return packageArr;
    }
    
    public WorkPackage find(UUID id) {
    	return em.find(WorkPackage.class, id);
    }

    /**
     * Uses a modified workPackage to update a workPackage in the database.
     * 
     * @param modifiedWP the work package with modifications
     * @return the modified work package
     */
    public WorkPackage updateWorkPackage(WorkPackage modifiedWP) {
        return em.merge(modifiedWP);
    }
    
    /**
     * Adds a work package to the database.
     * @param wp the work package to add
     */
    public void addWorkPackage(WorkPackage wp) {
        em.persist(wp);
    }
    
    /**
     * Deletes a work package from the database.
     * @param wp the work package to delete
     */
    public void deleteWorkPackage(WorkPackage wp) {
        WorkPackage w = em.find(WorkPackage.class, wp.getId());
        em.remove(w);
    }
    
    /**
     * Deletes a work package from the database along with its data
     * @param wp the work package to delete
     */
    @Transactional
    public void deleteWorkPackageFully(WorkPackage wp) {
        timesheetRowManager.deleteRowsForWP(wp);
        workPackageAllocManager.deleteAllForWP(wp);
        deleteWorkPackage(wp);
    }
    
    public void deleteWorkPackageIfLeaf(WorkPackage wp) {
        if (isLeaf(wp)) {
            deleteWorkPackageFully(wp);
        } else {
            System.out.println("couldnt delete workpackage because it is not a leaf");
        }
    }
    
    /**
     * Looks to see if a work package is a leaf.
     * @param wp work package to determine leaf status of
     * @return boolean for whether the work package is a leaf or not
     */
    public boolean isLeaf(WorkPackage wp) {
        WorkPackage[] childrenWPs = getByParentId(wp.getId().toString());
        return childrenWPs.length < 1;
    }
    
    /**
     * Sums all person day estimates from work package allocations for a specific
     * work package.
     * @param wp work package for which person day estimates are summed
     * @return calculated allocated person days
     */
    public BigDecimal calculateAllocatedPersonDays2(WorkPackage wp) {
        WorkPackageAllocation[] allocationsWP = workPackageAllocManager.getByWP(wp);
        BigDecimal personDays = new BigDecimal(0);
        
        for (WorkPackageAllocation allocation : allocationsWP) {
            personDays.add(allocation.getPersonDaysEstimate());
        }
        
        return personDays;
    }
    
    /**
     * If it is a branch, recursively calculates and sets the allocated budget for
     * a work package and its children by looking at its children. Otherwise, if it is
     * a leaf, the paygrade salary and person days are multiplied to get the allocated
     * budget.
     * @param wp work package for which to calculate allocated budget
     * @return allocated budget
     */
    public BigDecimal calculateAllocatedBudget(WorkPackage wp) {
        BigDecimal budget = new BigDecimal(0);
        
        if (isLeaf(wp)) {
            WorkPackageAllocation[] allocationsWP = workPackageAllocManager.getByWP(wp);
            
            for (WorkPackageAllocation allocation : allocationsWP) {
                BigDecimal calculatedBudget = allocation.getPaygrade().getSalary()
                        .multiply(allocation.getPersonDaysEstimate());
                budget.add(calculatedBudget);
            }
        } else { // for a branch work package
            WorkPackage[] childrenWPs = getByParentId(wp.id.toString());
            
            // sums all the children's allocated person days
            for (WorkPackage child : childrenWPs) {
                budget.add(calculateAllocatedBudget(child));
            }
        }
        
        return budget;
    }
    
    /**
     * Sums all responsible engineer person day estimates for a specific
     * work package. It is assumed this will be used only on leaf work
     * packages.
     * @param wp work package for which person day estimates are summed
     * @return calculated responsible engineer person day estimate
     */
    public BigDecimal calculateREPDEstimate(WorkPackage wp) {
        RespEngCostEstimate[] respEngPDEstimates = receManager.getByWP(wp.id);
        BigDecimal personDays = new BigDecimal(0);
        
        for (RespEngCostEstimate estimatePD : respEngPDEstimates) {
            personDays.add(estimatePD.getPersonDayEstimate());
        }
        
        return personDays;
    }
    
    /**
     * Sums all responsible engineer budget estimates for a specific
     * work package. It is assumed this will be used only on leaf work
     * packages.
     * @param wp work package for which budget estimates are summed
     * @return calculated responsible engineer budget estimate
     */
    public BigDecimal calculateREBudgetEstimate(WorkPackage wp) {
        RespEngCostEstimate[] respEngCostEstimates = receManager.getByWP(wp.id);
        BigDecimal budget = new BigDecimal(0);
        
        for (RespEngCostEstimate estimateCost : respEngCostEstimates) {
            BigDecimal calculatedCost = estimateCost.getPaygrade().getSalary()
                    .multiply(estimateCost.getPersonDayEstimate());
            budget.add(calculatedCost);
        }
        
        return budget;
    }
    
    /**
     * Calculate unallocated budget of the WorkPackage
     * 
     * @param wp WorkPackage
     * @return calculated unallocated budget
     */
    public BigDecimal calculateUnallocatedBudget(WorkPackage wp) {
		return wp.projectBudget.subtract(calculateAllocatedBudget(wp));
	}

	/**
	 * Get WorkPackage(s) by StartDate
	 * 
	 * @param startDate Start Date
	 * @return WorkPackage(s) by StartDate
	 */
	public WorkPackage[] getByStartDate(LocalDate startDate) {
		TypedQuery<WorkPackage> query = em
				.createQuery("SELECT wp FROM WorkPackage wp WHERE startdate LIKE :startDate", WorkPackage.class)
				.setParameter("startDate", "%" + startDate + "%");
        return getWorkPackages(query);
    }

	/**
	 * Get WorkPackage(s) by EndDate
	 * 
	 * @param endDate End Date
	 * @return WorkPackage(s) by EndDate
	 */
	public WorkPackage[] getByEndDate(LocalDate endDate) {
		TypedQuery<WorkPackage> query = em
				.createQuery("SELECT wp FROM WorkPackage wp WHERE enddate LIKE :endDate", WorkPackage.class)
				.setParameter("endDate", "%" + endDate + "%");
        return getWorkPackages(query);
    }

	/**
	 * Get all leaves WorkPackage(s)
	 * 
	 * @param isLeaf isLeaf
	 * @return All leaves WorkPackage(s)
	 */
	public WorkPackage[] getAllLeaves(boolean isLeaf) {
		TypedQuery<WorkPackage> query = em
				.createQuery("SELECT wp FROM WorkPackage wp WHERE isleaf LIKE :isLeaf", WorkPackage.class)
				.setParameter("isLeaf", "%" + isLeaf + "%");
        return getWorkPackages(query);
    }

    /**
     * Updated selected Work Package Leaf details
     * @param w
     */
	public void updateWorkPackageLeaf(EditableWorkPackageLeaf w) {
        WorkPackage wp = getByUUID(String.valueOf(w.getId()));
        wp.workPackageNumber = w.workPackageNumber;
        wp.startDate = w.startDate;
        wp.endDate = w.endDate;
        wp.projectBudget = w.projectBudget;
        wp.completedBudget = w.completedBudget;
        wp.RECEs = w.RECEs;
        wp.wpAllocs = w.wpAllocs;
        wp.childPackages = w.childPackages;
        wp.responsibleEngineer = w.responsibleEngineer;
        em.merge(wp);
        System.out.println("Update work package leaf" + wp.workPackageNumber);
    }
	
	// HELPER FUNCTIONS
    
    //TODO: Need to account for edge cases
    /**
     * Returns the parent Workpackage of any given workpackage
     * ex) 13100 return 13000, 14200 return 14000
     * 
     * @param wp, the child Workpackage as a WorkPackage
     * @return the parent work package wpnum, as a string
     */
    public String determineParentWPNum(String wpNumber) {
        //Gets the WP Number(10000) -> 11000, 12000, 13000, 14000, 1500 -> 11100 , 11200
        String wpNum = wpNumber;
        System.out.println("running determinewpnum");
        //Concatenates any non 0 digit to the blank string (This assumes a wp must have all trailing 0s at the end)
        
      
        String blankString = "";
        for (int i = 0; i < wpNum.length(); i++) {
            if (wpNum.charAt(i) != '0') {
                blankString += wpNum.charAt(i);
            }
        }
      //LOGIC ERROR HERE
        //Parse the wpNum into an int
        
        if (blankString.length() == 0) {
        	return "-1";
        }
        
        int wpNumAsInt = Integer.parseInt(blankString);
        //Divide the integer by 10 in order to get the parent WP (Truncates and decimals)
        int parentWpNumAsInt = wpNumAsInt / 10; 
        //Convert parentWPNumAsInt back to string
        String parentWpNumAsString = Integer.toString(parentWpNumAsInt);
        
        //Adds the trailing 0s back on (This assumes all wp have 5 digits, can add count variable into first four loop if not
        while(parentWpNumAsString.length() < 5) {
            parentWpNumAsString += "0";
        }
        
        return parentWpNumAsString;
    }

    //TODO: Need to account for edge cases
    /**
     * Determines the children WP of any given WP
     * ex) 13100 return 13110 13120 13130 etc
     * 
     * @param wp, the parent WorkPackage, as a WorkPackage
     * @return an array of child work package numbers, as strings 
     */
    public ArrayList<String> determineWPNumFromParent(String wpNum) {
        //ArrayList containing strings of all ChildWPNums
        ArrayList<String> potentialWorkPackages = new ArrayList<String>();
        //The wp num of the ParentWP
        String parentWpNum = wpNum;
        
        //Maintains a count of what char we are at
        int count = 0;
        //Counts each non 0 number in the string, in order to get to where the Child WP should be
        for (int i = 0; i < parentWpNum.length(); i++) {
            if (parentWpNum.charAt(i) != '0') {
                count++;
            } 
        }
        
        //Adds one to the count in order to get to the child work package digit
        count++;
        
        //Replaces each var at count with a num from 1-9 (10000) -> 11000, 12000, 13000
        for (int i = 1; i < 10; i++) {
            StringBuilder stringBuilderWP = new StringBuilder(parentWpNum);
            stringBuilderWP.setCharAt(count, (char)(i + 48));
            potentialWorkPackages.add(new String(stringBuilderWP.toString()));
        }
        
        return potentialWorkPackages;
    }
    
    /** Gets rid of the 0s on a WorkPackage number **/
    public String determineChildWPWithoutZeroes(String wpNum) {
        //The wp num of the ParentWP
        String parentWpNum = wpNum;
        
        String childWP = "";
        
        //For each number in the WpNum appends it to childWp, if it's not a 0.
        //Once a 0 is encountered, breaks from the loop
        for (int i = 0; i < parentWpNum.length(); i++) {
            if (parentWpNum.charAt(i) != '0') {
                childWP += parentWpNum.charAt(i);
            } else {
                break;
            }
        }
        
        return childWP;
    }
}
