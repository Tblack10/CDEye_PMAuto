package CDEye_PMAuto.backend.employee;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Used to convert a managers username to an Employee object.
 * 
 * @author travisblack & wilsonzhu
 */
@Named("employeeConverter")
@RequestScoped
public class EmployeeConverter implements Converter<Employee> {

    @Inject
    @Dependent
    private EmployeeManager employeeManager;

    /**
     * Gets an Employee Object from the DB by using a employee username.
     */
    @Override
    public Employee getAsObject(FacesContext context, UIComponent component, String value) {
        if(value == null || value.equals("")) {
            return null;
        }
        
        Employee employee = employeeManager.getEmployeeByUserName(value);
        
        if(employee == null) {
            throw new ConverterException(new FacesMessage("Employee with number: " + value + " not found."));
        }
        return employee;
    }

    /**
     * Gets the UserName of an employee as a string by using an Employee value.
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Employee value) {
        if (!(value instanceof Employee) || (value == null)) {
            return null;
        }
        return value.getUserName().toString();
    }


}
