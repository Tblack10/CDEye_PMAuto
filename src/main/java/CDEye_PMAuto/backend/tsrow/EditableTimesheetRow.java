package CDEye_PMAuto.backend.tsrow;

import CDEye_PMAuto.backend.project.Project;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class EditableTimesheetRow extends TimesheetRow implements Serializable {

    private boolean editable;
    private boolean deletable;
    private String projectNumber;
    private String workPackageNumber;
    private Project[] projects;
    private availProject[] projectNameList;

    public EditableTimesheetRow() {
    }

    public EditableTimesheetRow(TimesheetRow target) {
        super(target);
        setProjectNumber(target.project.getProjectNumber());
        setWorkPackageNumber(target.workPackage.getWorkPackageNumber());
    }

    public static class availProject {
        public String availProjectName;
        public String availProjectNumber;

        public availProject(String name, String num) {
            this.availProjectName = name;
            this.availProjectNumber = num;
        }

        public String getAvailProjectNumber() {
            return availProjectNumber;
        }

        public String getAvailProjectName() {
            return availProjectName;
        }
    }

    public availProject[] getAvailProjects() {
        projectNameList = new availProject[projects.length];
        for (int i = 0; i < projectNameList.length; i++) {
            projectNameList[i] = new availProject(
                    projects[i].getProjectName(),
                    projects[i].getProjectNumber()
            );
        }
        return projectNameList;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getWorkPackageNumber() {
        return workPackageNumber;
    }

    public void setWorkPackageNumber(String workPackageNumber) {
        this.workPackageNumber = workPackageNumber;
    }

    public Project[] getProjects() {
        return projects;
    }

    public void setProjects(Project[] projects) {
        this.projects = projects;
    }
}
