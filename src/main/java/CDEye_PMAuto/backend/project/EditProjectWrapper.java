package CDEye_PMAuto.backend.project;

import CDEye_PMAuto.backend.employee.Employee;
import CDEye_PMAuto.backend.employee.EmployeeManager;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;


@Named
@SessionScoped
public class EditProjectWrapper implements Serializable {
    @Inject private ProjectManager projectManager;
    @Inject private EmployeeManager employeeManager;
    private Project editProject = new Project();
    private Employee[] managers;
    private String selectedManager;

    public void setActiveProjectBean(EditableProject ep) {
        System.out.println("setActiveProjectBean id=" + ep.getId());
        Project project = projectManager.find(ep.getId());

        editProject.id = project.id;
        editProject.projectName = project.projectName;
        editProject.projectNumber = project.projectNumber;
        editProject.projManager = project.projManager;

        editProject.startDate = project.startDate;
        editProject.endDate = project.endDate;
        editProject.estimateBudget = project.estimateBudget;
        editProject.markUpRate = project.markUpRate;
        editProject.projectBudget = project.projectBudget;

        if (project.projManager != null) {
            this.selectedManager = project.projManager.getUserName();
        }

        this.managers = employeeManager.getAll();
        System.out.println("Ready to edit.");
    }

    public Project getEditProject() {
        return editProject;
    }

    public void setEditProject(Project editProject) {
        this.editProject = editProject;
    }

    public Employee[] getManagers() {
        return managers;
    }

    public void setManagers(Employee[] managers) {
        this.managers = managers;
    }

    public String updateProject() {
        System.out.println("update id:" + editProject.getId());
        System.out.println("projectName:" + editProject.getProjectName());
        System.out.println("selectedManager:" + selectedManager);
        editProject.projManager = employeeManager.getEmployeeByUserName(selectedManager);
        projectManager.updateProject(editProject);
        editProject = new Project();
        return "ProjectDashboard?faces-redirect=true";
    }

    public String back() {
        editProject = new Project();
        return "ProjectDashboard?faces-redirect=true";
    }

    public String getSelectedManager() {
        return selectedManager;
    }

    public void setSelectedManager(String selectedManager) {
        this.selectedManager = selectedManager;
    }
}
