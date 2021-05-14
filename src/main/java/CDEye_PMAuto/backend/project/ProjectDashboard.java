package CDEye_PMAuto.backend.project;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

import CDEye_PMAuto.backend.employee.ActiveEmployeeBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ConversationScoped
public class ProjectDashboard implements Serializable {
	@Inject @Dependent private ProjectManager projectManager;
	private List<EditableProject> list;
	@Inject Conversation conversation;
	@Inject ActiveProjectBean activeProjectBean;
	@Inject EditProjectWrapper editProjectWrapper;
	@Inject ProjectList projList;
	@Inject ActiveEmployeeBean aeb;
	private EditableProject deletedProject = new EditableProject();
	private DashboardModel model;

	@PostConstruct
	public void init() {
		System.out.println("ProjectDashboard initializing...");
		if (list == null) refreshList();
		model = new DefaultDashboardModel();
		DashboardColumn col1 = new DefaultDashboardColumn();
		DashboardColumn col2 = new DefaultDashboardColumn();
		DashboardColumn col3 = new DefaultDashboardColumn();

		int numberOfRows = list.size()/3;
		int remainder = list.size()%3;
		int count = 0;
		if (numberOfRows != 0) {
			for (int i=0; i<numberOfRows; i++, count++)
				col1.addWidget(list.get(count).projectName.replaceAll("\\s", "_"));
			for (int j=0; j<numberOfRows; j++, count++)
				col2.addWidget(list.get(count).projectName.replaceAll("\\s", "_"));
			for (int k=0; k<numberOfRows; k++, count++)
				col3.addWidget(list.get(count).projectName.replaceAll("\\s", "_"));
		}
		if (remainder == 1)
			col1.addWidget(list.get(count).projectName.replaceAll("\\s", "_"));
		else if (remainder == 2) {
			col1.addWidget(list.get(count).projectName.replaceAll("\\s", "_"));
			count++;
			col2.addWidget(list.get(count).projectName.replaceAll("\\s", "_"));
		}
		model.addColumn(col1);
		model.addColumn(col2);
		model.addColumn(col3);
		System.out.println("Dashboard generated.");
	}
	
	public List<EditableProject> getList() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
		conversation.begin();
		if (list == null) refreshList();
		return list;
	}

	public List<EditableProject> refreshList() {
		Project[] projects = projectManager.getAll();
		list = new ArrayList<>();
		for (Project project : projects) list.add(new EditableProject(project));
		return list;
	}

	public String viewProjectWPs(EditableProject p) {
		if (!conversation.isTransient()) {
			conversation.end();
		}
		return activeProjectBean.setActiveProjectBean(p);
	}

	public String editProject(EditableProject p) {
		if (!conversation.isTransient()) {
			conversation.end();
		}
		editProjectWrapper.setActiveProjectBean(p);
		return "EditProject?faces-redirect=true";
	}

    public String deleteProject(EditableProject p) {
    	projectManager.deleteProject(projectManager.findProject(p.projectName));
		projList.refreshList();
		if (!conversation.isTransient()) {
			conversation.end();
		}
    	return "ProjectDashboard?faces-redirect=true";
    }

	public String reportProject(EditableProject p) {
		if (!conversation.isTransient()) {
			conversation.end();
		}
		return activeProjectBean.setReportProjectBean(p);
	}

	public String back() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
		return "ProjectDashboard?faces-redirect=true";
	}

    public String home() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
		
		if (aeb.getHr()) {
			return "HRHome?faces-redirect=true";
		} else {
			return "Home";
		}
		
		
	}

	public DashboardModel getModel() {
		return model;
	}

	public void setModel(DashboardModel model) {
		this.model = model;
	}

	public EditableProject getDeletedProject() {
		return deletedProject;
	}

	public void setDeletedProject(EditableProject deletedProject) {
		this.deletedProject = deletedProject;
	}
}
