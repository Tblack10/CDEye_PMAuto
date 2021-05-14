package CDEye_PMAuto.backend.workpackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.enterprise.context.*;
import javax.inject.Inject;
import javax.inject.Named;

import CDEye_PMAuto.backend.employee.ActiveEmployeeBean;
import CDEye_PMAuto.backend.employee.Employee;
import CDEye_PMAuto.backend.paygrade.Paygrade;
import CDEye_PMAuto.backend.paygrade.PaygradeManager;
import CDEye_PMAuto.backend.project.ActiveProjectBean;
import CDEye_PMAuto.backend.project.Project;
import CDEye_PMAuto.backend.recepackage.RECEManager;
import CDEye_PMAuto.backend.recepackage.RespEngCostEstimate;
import CDEye_PMAuto.backend.wpallocation.WorkPackageAllocation;

@Named("workPackageList")
@RequestScoped
public class WorkPackageList implements Serializable {

    @Inject
    @Dependent 
    private WorkPackageManager workPackageManager;

    @Inject
    private PaygradeManager paygradeManager;
    
    @Inject
    private RECEManager receManager;
    
    @Inject
    private ActiveEmployeeBean activeEmp;
    
    @Inject ActiveProjectBean apb;
    
    private List<EditableWorkPackage> list; // WPs for a project
    private List<EditableWorkPackage> listAsRE; // WPs for a RE
    private List<EditableWorkPackage> listAsEmp; // WPs for an employee
    
    private String searchId; // = "123e4567-e89b-12d3-a456-599342400003";
    private String searchParentPackageId; // = "123e4567-e89b-12d3-a456-599342400001";
    private String searchPackageNumber; // = "111";

//    @Inject 
//    Conversation conversation;
    
    public List<EditableWorkPackage> getList() {
//        if(!conversation.isTransient()) {
//            conversation.end();
//        }
//        conversation.begin();
        if (list == null) {
            refreshList();
        }
        return list;
    }
    
    public List<EditableWorkPackage> getListAsRE() {
        List<WorkPackage> list = Arrays.asList(workPackageManager.getAll());
        listAsRE = new ArrayList<EditableWorkPackage>();
        
        for (WorkPackage wp : list) {
            Employee tempRespEnj = wp.getResponsibleEngineer();
            if (tempRespEnj != null && tempRespEnj.getUserName().equals(activeEmp.getUserName())) {
                listAsRE.add(new EditableWorkPackage(wp));
            }
        }
        return listAsRE;
    }
    
    public List<EditableWorkPackage> getListAsEmp() {
        RespEngCostEstimate[] costEstimates = receManager.getAll();
        listAsEmp = new ArrayList<EditableWorkPackage>();
        
        for (RespEngCostEstimate costEstimate : costEstimates) {
            Employee e = costEstimate.getEmployee();
            if (e != null && e.getUserName().equals(activeEmp.getUserName())) {
                listAsEmp.add(new EditableWorkPackage(costEstimate.getWorkPackage()));
            }
        }
        return listAsEmp;
    }
    
    public EditableWorkPackage getWpsBy(UUID id) {
        return new EditableWorkPackage(workPackageManager.getByUUID(id.toString()));     
    }

    public List<EditableWorkPackage> refreshList() {
    	System.out.println("starting to refresh wp list");
    	Project activeProj = new Project(apb.getId(), apb.getProjectName(), apb.getProjectNumber(), 
    			apb.getProjManager(), apb.getStartDate(), apb.getEndDate(), apb.getEstimateBudget(), 
    			apb.getMarkUpRate(), apb.getProjectBudget());
        WorkPackage[] workPackages = workPackageManager.findWpsByProject(activeProj);
        if (searchPackageNumber != null
                && searchPackageNumber.length() >= 1) {
            workPackages = workPackageManager.findWpsByPkgNumAndProj(searchPackageNumber, activeProj);
        	
        } 
        
        list = new ArrayList<EditableWorkPackage>();

        // Retrieve exist allocated budget breakdown and responsible enginner
        // If not exist, init new paygrade
        for (int i = 0; i < workPackages.length; i++) {
            EditableWorkPackage wp = new EditableWorkPackage(workPackages[i]);

            // Init hash table to store exist paygrades and person day
            HashMap<String, WorkPackageAllocation> mp = new HashMap<>();
            HashMap<String, RespEngCostEstimate> remap = new HashMap<>();
            for (WorkPackageAllocation w : workPackages[i].getWpAllocs()) {
                mp.put(w.getPaygrade().getName(), w);
            }
            for (RespEngCostEstimate r : workPackages[i].getRECEs()) {
                remap.put(r.getPaygrade().getName(), r);
            }

            // Init from P1 - P9 row and update person day if exist
            List<WorkPackageAllocation> wpa = new ArrayList<WorkPackageAllocation>();
            List<RespEngCostEstimate> receList = new ArrayList<RespEngCostEstimate>();
            for (Paygrade p : paygradeManager.getAll()) {
                if (mp.containsKey(p.getName())) {
                    wpa.add(mp.get(p.getName()));
                } else {
                    wpa.add(new WorkPackageAllocation(wp, p));
                }
                if (remap.containsKey(p.getName())) {
                    receList.add(remap.get(p.getName()));
                } else {
                    receList.add(new RespEngCostEstimate(wp, p));
                }
            }

            // Update allocated budget breakdown list and responsible enginner list
            wp.setWpAllocs(wpa);
            wp.setRECEs(receList);
            list.add(wp);
        }
        System.out.println("finishing wp list refresh");
        return list;
    }
    
    public void deleteWorkPackageIfLeaf(WorkPackage wp) {
        WorkPackage w = new WorkPackage();
        w.id = wp.id;
        w.workPackageNumber = wp.workPackageNumber;
        w.parentWp = wp.parentWp;
        w.completedBudget = wp.completedBudget;
        w.completedPersonDays = wp.completedPersonDays;
        w.startDate = wp.startDate;
        w.endDate = wp.endDate;
        w.isLeaf = wp.isLeaf;
        w.projectBudget = wp.projectBudget;
        w.project = wp.project;
        w.RECEs = wp.RECEs;
        w.wpAllocs = wp.wpAllocs;
        w.childPackages = wp.childPackages;
        w.responsibleEngineer = wp.responsibleEngineer;
        workPackageManager.deleteWorkPackageIfLeaf(w);
    }
    
    public void search() {
        refreshList();
    }
    
    public void clearSearch() {
        searchId = null;
        searchParentPackageId = null;
        searchPackageNumber = null;
        refreshList();
    }

    /**
     * @return the searchId
     */
    public String getSearchId() {
        return searchId;
    }

    /**
     * @param searchId the searchId to set
     */
    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    /**
     * @return the searchParentPackageId
     */
    public String getSearchParentPackageId() {
        return searchParentPackageId;
    }

    /**
     * @param searchParentPackageId the searchParentPackageId to set
     */
    public void setSearchParentPackageId(String searchParentPackageId) {
        this.searchParentPackageId = searchParentPackageId;
    }

    /**
     * @return the searchPackageNumber
     */
    public String getSearchPackageNumber() {
        return searchPackageNumber;
    }

    /**
     * @param searchPackageNumber the searchPackageNumber to set
     */
    public void setSearchPackageNumber(String searchPackageNumber) {
        this.searchPackageNumber = searchPackageNumber;
    }

}
