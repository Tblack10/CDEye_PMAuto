package CDEye_PMAuto.backend.recepackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import CDEye_PMAuto.backend.employee.Employee;
import CDEye_PMAuto.backend.employee.EmployeeManager;
import CDEye_PMAuto.backend.workpackage.ActiveWPBean;
import CDEye_PMAuto.backend.workpackage.UserNameBean;
import CDEye_PMAuto.backend.workpackage.WorkPackage;

@Named("receList")
@RequestScoped
public class RECEList implements Serializable {
    
    /** The RECEManage used to access the DB **/
    @Inject 
    @Dependent 
    private RECEManager receManager;
    
    @Inject
    private EmployeeManager employeeManager;
    
    @Inject
    private ActiveWPBean awp;
   
    /** A list of editable rece packages **/
    private List<EditableRECE> list;
    
//    @Inject 
//    Conversation conversation;
    
    /**
     * Gets a list of RECEPackages. Will call refreshList() if the list is currently null.
     * @return a list of EditableRecePackage, List<EditableRECEPackage>
     */
    public List<EditableRECE> getList() {
//        if(!conversation.isTransient()) {
//            conversation.end();
//        }
//        conversation.begin();
        if (list == null) {
            refreshList();
        }
        return list;
    }
    
    public List<RespEngCostEstimate> refreshRespEngCostEstimate() {
        WorkPackage wp = new WorkPackage();
        wp.setId(awp.getId());
        RespEngCostEstimate[] packages = receManager.getByWP(wp.getId());
        
        
        
        return Arrays.asList(packages);
    }

    /**
     * Accesses the RECEManager Class to get all EditableRECEPackages. This function is used by 
     * getList().
     * 
     * @return a list of EditableREcePackage, List<EditableRECEPackage>
     */
    public List<EditableRECE> refreshList() {
//    	System.out.println("starting rece  ");
//        WorkPackage wp = new WorkPackage();
//        wp.setId(awp.getId());
////        wp.wo(awp.getId());
////        wp.parentWp = awp.parentWp;
////        wp.completedBudget = awp.completedBudget;
////        wp.completedPersonDays = awp.completedPersonDays;
////        wp.startDate = awp.startDate;
////        wp.endDate = awp.endDate;
////        wp.isLeaf = awp.isLeaf;
////        wp.projectBudget = awp.projectBudget;
////        wp.project = awp.project;
////        wp.RECEs = awp.RECEs;
////        wp.wpAllocs = awp.wpAllocs;
////        wp.childPackages = awp.childPackages;
//        RespEngCostEstimate[] packages = receManager.getByWP(wp);
//        list = new ArrayList<EditableRECE>();
//        for (int i = 0; i < packages.length; i++) {
//            EditableRECE editableRECE = new EditableRECE(packages[i]);
//            editableRECE.setEmployeeManager(employeeManager);
//            editableRECE.setReceManager(receManager);
//            list.add(editableRECE);
//        }
//        System.out.println("ending rece");
    	System.out.println("before");
    	System.out.println("awp id: " + awp.getId());
    	UUID wpId = awp.getId();
    	RespEngCostEstimate[] reces = receManager.getByWP(wpId);
    	list = new ArrayList<EditableRECE>();
    	for (int i = 0; i < reces.length; i++) {
          EditableRECE editableRECE = new EditableRECE(reces[i]);
          editableRECE.setEmployeeManager(employeeManager);
          editableRECE.setReceManager(receManager);
          list.add(editableRECE);
      }
    	System.out.println("before returning recelist");
//    	RespEngCostEstimate[] reces = receManager.getByWP(awp.getId());
//    	System.out.println(reces.length);
        return list;
    }

    public void assignEmployees() {
        for (EditableRECE erece : list) {
        	System.out.println("empname: " + erece.getEmpUserName());
        	Employee e = employeeManager.getEmployeeByUserName(erece.getEmpUserName());
        	RespEngCostEstimate rece = receManager.find(erece.getId());
            rece.setEmployee(e);
            receManager.merge(rece);
        }
        
        //receManager.merge(rece);
    }

}
