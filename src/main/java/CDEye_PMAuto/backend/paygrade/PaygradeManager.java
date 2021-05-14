package CDEye_PMAuto.backend.paygrade;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import CDEye_PMAuto.backend.credentials.Credential;
import CDEye_PMAuto.backend.employee.Employee;

@Dependent
@Stateless
public class PaygradeManager implements Serializable {

	@PersistenceContext(unitName="inventory-jpa") EntityManager em;
	
	public Paygrade[] getAll() {
		TypedQuery<Paygrade> query = em.createQuery("select p from Paygrade p",
        		Paygrade.class);
		List<Paygrade> paygrades = query.getResultList();
		Paygrade[] paygradeArr = new Paygrade[paygrades.size()];
        for (int i = 0; i < paygradeArr.length; i++) {
        	paygradeArr[i] = paygrades.get(i);
        }
        return paygradeArr;
	}

	public void persist(Paygrade p) {
        em.persist(p);
    }
	
	public Paygrade findPaygrade(String paygradeName) {
		TypedQuery<Paygrade> query = em.createQuery(
				"SELECT p FROM Paygrade p WHERE p.name LIKE :name", Paygrade.class)
				.setParameter("name", "%" + paygradeName + "%");
		List<Paygrade> paygrades = query.getResultList();
		return paygrades.get(0);
	}

	/**
	 * Delete paygrade in db
	 * @param paygradeToDelete
	 */
	public void deletePaygrade(Paygrade paygradeToDelete) {
		Paygrade p = em.find(Paygrade.class, paygradeToDelete.getId());
		em.remove(p);
	}

	/**
	 * Update paygrade in db
	 * @param paygradeToUpdate
	 */
	public void updatePaygrade(Paygrade paygradeToUpdate) {
		Paygrade p = em.find(Paygrade.class, paygradeToUpdate.getId());
		p.name = paygradeToUpdate.name;
		p.salary = paygradeToUpdate.salary;
		em.merge(p);
	}


}
