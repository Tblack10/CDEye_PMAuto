package CDEye_PMAuto.backend.employee;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named("employeeNumberValidator")
public class EmployeeNumberValidator implements Validator<Integer>{

    @Inject
    EmployeeManager employeeManager;
    
    @Override
    public void validate(FacesContext context, UIComponent component, Integer value) throws ValidatorException {
        int empName = value;
        
        Employee one = employeeManager.getEmployeeByEmpNum(empName);
        if (one != null) {
            FacesMessage message = new FacesMessage( "Employee Number is Already Taken.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);

            throw new ValidatorException(message);
        }        
    }
}
