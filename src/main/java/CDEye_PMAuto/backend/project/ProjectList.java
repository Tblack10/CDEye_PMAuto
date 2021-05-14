package CDEye_PMAuto.backend.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import CDEye_PMAuto.backend.employee.ActiveEmployeeBean;

@Named("projectList")
@RequestScoped
public class ProjectList implements Serializable {
	@Inject @Dependent private ProjectManager projectManager;
	private List<EditableProject> list;
	private List<EditableProject> listForPM;
//	@Inject Conversation conversation;
	@Inject ActiveProjectBean apb;
	@Inject ActiveEmployeeBean activeEmp;
	
	public List<EditableProject> getList() {
//		if (!conversation.isTransient()) {
//			conversation.end();
//		}
//		conversation.begin();
        if (list == null) {
            refreshList();
        }
        return list;
    }
	
	public List<EditableProject> getListForPM() {
//        if (!conversation.isTransient()) {
//            conversation.end();
//        }
//        conversation.begin();
        if (listForPM == null) {
            refreshListForPM();
        }
        return listForPM;
    }
	
	public List<EditableProject> refreshList() {
		Project[] projects = projectManager.getAll();
        list = new ArrayList<EditableProject>();
        for (int i = 0; i < projects.length; i++) {
            list.add(new EditableProject(projects[i]));
        }
        return list;
    }
	
	public List<EditableProject> refreshListForPM() {
        Project[] projects = projectManager.getAll();
        listForPM = new ArrayList<EditableProject>();
        for (int i = 0; i < projects.length; i++) {
            listForPM.add(new EditableProject(projects[i]));
        }
        filterProjects();
        return listForPM;
    }
	
	/**
     * Filters the project list based on the currently logged in employee.
     * If the employee is a regular employee, all projects are filtered out.
     * If the employee is a project manager, projects not related with the
     * manager are filtered out.
     */
    public void filterProjects() {
        ArrayList<EditableProject> resultingList = new ArrayList<EditableProject>();
        for (EditableProject project : listForPM) {
            if (project.projManager != null && project.projManager.getId().equals(activeEmp.getId())) {
                resultingList.add(project);
            }
        }
        listForPM = resultingList;
    }
	
	public String viewProjectWPs(EditableProject p) {
		//conversation.end();
		return apb.setActiveProjectBean(p);
	}
	
	public String save() {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).isEditable()) {
				Project p = new Project(list.get(i));
				projectManager.updateProject(p);
				list.get(i).setEditable(false);
			}
			if (list.get(i).isDeletable()) {
				Project p = new Project(list.get(i));
				projectManager.deleteProject(p);
			}
		}
		refreshList();
		return "";
	}
	
}
