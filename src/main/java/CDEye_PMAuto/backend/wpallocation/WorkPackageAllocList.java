package CDEye_PMAuto.backend.wpallocation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import CDEye_PMAuto.backend.recepackage.EditableRECE;
import CDEye_PMAuto.backend.recepackage.RECEManager;
import CDEye_PMAuto.backend.recepackage.RespEngCostEstimate;

@Named("wpAllocList")
@RequestScoped
public class WorkPackageAllocList implements Serializable {

	@Inject 
    @Dependent 
    private WorkPackageAllocManager wpaManager;

    private List<EditableWorkPackageAlloc> list;
    
//    @Inject 
//    Conversation conversation;
    
    public List<EditableWorkPackageAlloc> getList() {
//        if(!conversation.isTransient()) {
//            conversation.end();
//        }
//        conversation.begin();
        if (list == null) {
            refreshList();
        }
        return list;
    }
    
    public List<EditableWorkPackageAlloc> refreshList() {
//    	System.out.println("before returning recelist");
        List <WorkPackageAllocation> packages = wpaManager.getAll();
        list = new ArrayList<EditableWorkPackageAlloc>();
        for (int i = 0; i < packages.size(); i++) {
            list.add(new EditableWorkPackageAlloc(packages.get(i)));
        }
        return list;
    }


}
