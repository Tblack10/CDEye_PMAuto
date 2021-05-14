package CDEye_PMAuto.backend.wpallocation;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import CDEye_PMAuto.backend.workpackage.WorkPackage;

@Dependent
@Stateless
public class WorkPackageAllocManager implements Serializable {

	@PersistenceContext(unitName="inventory-jpa") 
    EntityManager em;
	
	public List<WorkPackageAllocation> getAll() {
        TypedQuery<WorkPackageAllocation> query = em.createQuery("select wpa from WorkPackageAllocation wpa",
        		WorkPackageAllocation.class); 
        List<WorkPackageAllocation> workPackages = query.getResultList();
//        WorkPackageAllocation[] wpaArr = new WorkPackageAllocation[workPackages.size()];
//        for (int i = 0; i < wpaArr.length; i++) {
//            wpaArr[i] = workPackages.get(i);
//        }
        return workPackages;
    }
	
	public WorkPackageAllocation[] getByWP(WorkPackage wp) {
	    TypedQuery<WorkPackageAllocation> query = em.createQuery(
                "SELECT wpa FROM WorkPackageAllocation wpa WHERE wpa.workPackage.id = :wpId", WorkPackageAllocation.class)
                .setParameter("wpId", wp.getId());
	    List<WorkPackageAllocation> workPackageAllocs = query.getResultList();
	    WorkPackageAllocation[] workPackageAllocsArr = new WorkPackageAllocation[workPackageAllocs.size()];
        for (int i = 0; i < workPackageAllocsArr.length; i++) {
            workPackageAllocsArr[i] = workPackageAllocs.get(i);
        }
        return workPackageAllocsArr;
	}
	
	public void addWorkPackageAlloc(WorkPackageAllocation wpa) {
		em.persist(wpa);
	}
	
	public void deleteWorkPackageAlloc(WorkPackageAllocation wpa) {
	    em.remove(wpa);
	}
	
	public void deleteAllForWP(WorkPackage wp) {
	    WorkPackageAllocation[] wpAllocs = getByWP(wp);
        for (WorkPackageAllocation allocation : wpAllocs) {
            deleteWorkPackageAlloc(allocation);
        }
	}
	
}
