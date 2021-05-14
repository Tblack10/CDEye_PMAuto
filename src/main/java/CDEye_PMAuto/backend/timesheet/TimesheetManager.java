package CDEye_PMAuto.backend.timesheet;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import CDEye_PMAuto.backend.employee.ActiveEmployeeBean;
import CDEye_PMAuto.backend.employee.Employee;

@Dependent
@Stateless
public class TimesheetManager implements Serializable {

	@PersistenceContext(unitName="inventory-jpa") 
	EntityManager em;
	@Inject ActiveEmployeeBean aeb;
	
	public Timesheet[] getAll() {
        TypedQuery<Timesheet> query = em.createQuery("select t from Timesheet t",
        		Timesheet.class);
		return getTimesheets(query);
	}

	private Timesheet[] getTimesheets(TypedQuery<Timesheet> query) {
		List<Timesheet> timesheet = query.getResultList();
		Timesheet[] timesheetArr = new Timesheet[timesheet.size()];
		for (int i = 0; i < timesheetArr.length; i++) {
			timesheetArr[i] = timesheet.get(i);
		}
		return timesheetArr;
	}

	public void updateTimesheet(Timesheet t) {
		em.merge(t);
	}
	
	public void deleteTimesheet(Timesheet t) {
		em.remove(em.contains(t) ? t : em.merge(t));
	}
	
	public Timesheet[] getAllForCurrentEmployee() {
		Employee currentEmp = new Employee();
		currentEmp.setId(aeb.getId());
		TypedQuery<Timesheet> query = em.createQuery("select t from Timesheet t where t.employee = :emp",
        		Timesheet.class).setParameter("emp", currentEmp);
		return getTimesheets(query);
	}

	public void addTimesheet(Timesheet t) {
		em.persist(t);
	}

	public Timesheet find(UUID id) {
		return em.find(Timesheet.class, id);
	}
}
