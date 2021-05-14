package CDEye_PMAuto.backend.employee;

import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named("usernameValidator")
public class UsernameValidator implements Validator<String> {

    @Inject
    EmployeeManager employeeManager;
    
    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
        String empName = (String)value;
        
        Employee one = employeeManager.getEmployeeByUserName(empName);
        if (one != null) {
            FacesMessage message = new FacesMessage( "Username is Already Taken.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);

            throw new ValidatorException(message);
        }        
    }
    
    

}
