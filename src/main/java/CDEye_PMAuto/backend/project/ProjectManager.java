package CDEye_PMAuto.backend.project;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import CDEye_PMAuto.backend.tsrow.TimesheetRowManager;
import CDEye_PMAuto.backend.workpackage.SortByNumber;
import CDEye_PMAuto.backend.workpackage.WorkPackage;
import CDEye_PMAuto.backend.workpackage.WorkPackageManager;

@Dependent
@Stateless
public class ProjectManager implements Serializable {

	@PersistenceContext(unitName = "inventory-jpa")
	EntityManager em;
	
	@Inject private WorkPackageManager workPackageManager;

	public Project[] getAll() {
		TypedQuery<Project> query = em.createQuery("select p from Project p", Project.class);
		List<Project> projects = query.getResultList();
		Project[] projectArr = new Project[projects.size()];
		for (int i = 0; i < projectArr.length; i++) {
			projectArr[i] = projects.get(i);
		}
		return projectArr;
	}

	public void updateProject(Project p) {
		em.merge(p);
	}

	public Project find(UUID projectId) {
		return em.find(Project.class, projectId);
	}

	public void persist(Project p) {
		em.persist(p);
	}

	public void deleteProject(Project p) {
	    deleteAssociatedInfo(p);
	    Project fp = em.find(Project.class, p.getId());
	    em.remove(fp);
		System.out.println("'project name: " + p.getProjectName() + "' was deleted");
	}
	
	/**
	 * Removes info associated with a given project.
	 */
	private void deleteAssociatedInfo(Project p) {
	    WorkPackage[] projectWPs = workPackageManager.findWpsByProject(p);
	    Arrays.sort(projectWPs, new SortByNumber());
	    for (WorkPackage wp : projectWPs) {
	        workPackageManager.deleteWorkPackageFully(wp);
	    }
	}

	public Project findProject(String projectName) {
		TypedQuery<Project> query = em
				.createQuery("SELECT p FROM Project p WHERE p.projectName LIKE :projectName", Project.class)
				.setParameter("projectName", "%" + projectName + "%");
		List<Project> projects = query.getResultList();
		return projects.get(0);
	}

	/**
	 * Return true if projectName already exist
	 * @param projectName
	 * @return
	 */
	public boolean isProjectNameExist(String projectName) {
		TypedQuery<Project> query = em.createQuery(
				"SELECT p FROM Project p WHERE p.projectName LIKE :projectName", Project.class)
				.setParameter("projectName", projectName);
		List<Project> projects = query.getResultList();

		return !projects.isEmpty();
	}

	/**
	 * Return true if projectNumber already exist
	 * @param projectNumber
	 * @return
	 */
	public boolean isProjectNumberExist(String projectNumber) {
		TypedQuery<Project> query = em.createQuery(
				"SELECT p FROM Project p WHERE p.projectNumber LIKE :projectNumber", Project.class)
				.setParameter("projectNumber", projectNumber);
		List<Project> projects = query.getResultList();

		return !projects.isEmpty();
	}

	public Project findProjectByNum(String projectNum) {
		TypedQuery<Project> query = em.createQuery(
				"SELECT p FROM Project p WHERE p.projectNumber LIKE :projectNumber", Project.class)
				.setParameter("projectNumber", "%" + projectNum + "%");
		List<Project> projects = query.getResultList();
		return projects.get(0);
	}
	//TODO add lookup method by project number
}
