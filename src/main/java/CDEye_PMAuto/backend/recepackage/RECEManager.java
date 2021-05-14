package CDEye_PMAuto.backend.recepackage;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import CDEye_PMAuto.backend.workpackage.WorkPackage;

@Dependent
@Stateless
public class RECEManager implements Serializable {
    
    @PersistenceContext(unitName="inventory-jpa") 
    EntityManager em;
    
    /**
     * Gets all the RECEPackages in the DB
     * @return an array of RECEPackage, as an [RECEPackage]
     */
    public RespEngCostEstimate[] getAll() {
        TypedQuery<RespEngCostEstimate> query = em.createQuery("select p from RespEngCostEstimate p", RespEngCostEstimate.class); 
        List<RespEngCostEstimate> packages = query.getResultList();
        RespEngCostEstimate[] packageArr = new RespEngCostEstimate[packages.size()];
        for (int i = 0; i < packageArr.length; i++) {
            packageArr[i] = packages.get(i);
        }
        return packageArr;
    }
    
    public RespEngCostEstimate[] getByWP(UUID wpId) {
    	TypedQuery<RespEngCostEstimate> query = em.createQuery("select p from RespEngCostEstimate p", RespEngCostEstimate.class); 
        List<RespEngCostEstimate> packages = query.getResultList();
        RespEngCostEstimate[] packageArr = new RespEngCostEstimate[9];
        int j = 0;
        for (int i = 0; i < packages.size(); i++) {
        	if (packages.get(i).workPackage.getId().equals(wpId)) {
        		packageArr[j] = packages.get(i);
        		j++;
        	}
        }
        System.out.println("returned " + packageArr.length + " wps");
        return packageArr;
    }
    
//    public RespEngCostEstimate[] getByWP(UUID wpId) {
//    	System.out.println("querying for reces for wp " + wpId);
//        TypedQuery<RespEngCostEstimate> query = em.createQuery(
//                "SELECT r FROM RespEngCostEstimate r WHERE r.workPackage.id = :wpId", RespEngCostEstimate.class)
//                .setParameter("wpId", wpId);
//        List<RespEngCostEstimate> respEngCostEstimates = query.getResultList();
//        System.out.println("after query " + wpId + "");
//        RespEngCostEstimate[] respEngCostEstimateArr = new RespEngCostEstimate[respEngCostEstimates.size()];
//        for (int i = 0; i < respEngCostEstimateArr.length; i++) {
//            respEngCostEstimateArr[i] = respEngCostEstimates.get(i);
//        }
//        System.out.println("after query before return");
//        return respEngCostEstimateArr;
//    }
    
    public RespEngCostEstimate getByUUID(String uuid) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<RespEngCostEstimate> criteriaQuery = criteriaBuilder.createQuery(RespEngCostEstimate.class);
        Root<RespEngCostEstimate> itemRoot = criteriaQuery.from(RespEngCostEstimate.class);
        
        UUID uuidAsString = UUID.fromString(uuid);
        
        Predicate predicateForName = criteriaBuilder.equal(itemRoot.get("id"), uuidAsString);
        
        criteriaQuery.where(predicateForName);
        
        List<RespEngCostEstimate> respEngCostEstimate = em.createQuery(criteriaQuery).getResultList();

        return respEngCostEstimate.get(0);
    }
    
    public void persist(RespEngCostEstimate rece) {
    	em.persist(rece);
    }
    
    public void merge(RespEngCostEstimate rece) {
        em.merge(rece);
    }
    
    public RespEngCostEstimate find(UUID id) {
        return em.find(RespEngCostEstimate.class, id);
    }
    
    //TODO: Create Package
    //TODO: Edit Package
    //TODO: Remove Package
    
}
