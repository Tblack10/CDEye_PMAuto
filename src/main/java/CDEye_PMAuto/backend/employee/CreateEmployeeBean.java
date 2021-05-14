package CDEye_PMAuto.backend.employee;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.annotations.DynamicUpdate;

import CDEye_PMAuto.backend.credentials.Credential;
import CDEye_PMAuto.backend.credentials.CredentialManager;
import CDEye_PMAuto.backend.paygrade.PaygradeManager;

@Named("createEmployee")
@RequestScoped
public class CreateEmployeeBean extends Employee implements Serializable
{
	@Inject private PaygradeManager paygradeManager;
	@Inject private EmployeeManager employeeManager;
	@Inject private CredentialManager credentialManager;

	protected String paygradeName;
	
	protected String password;

    /** Get selected paygrade from dropdown list. */
	public String getPaygradeName() {
		return paygradeName;
	}

	public void setPaygradeName(String paygradeName) {
		System.out.println("running: " + paygradeName);
		this.payGrade = paygradeManager.findPaygrade(paygradeName);
		this.paygradeName = paygradeName;
	}

	/** Paygrade Enum with 9 paygrdes
	 * Unsure for the last one, so just randomly give a name
	 */
	public enum PAYGRADE {
		P1,
		P2,
		P3,
		P4,
		P5,
		P6,
		DS,
		JS,
		XS,
	}

	/** Paygrade list to populate each select item. */
	private static final Collection<SelectItem> paygradeList;
	static {
		paygradeList = new ArrayList<SelectItem>();

		for (PAYGRADE p : PAYGRADE.values()) {
			paygradeList.add(new SelectItem((p)));
		}
	}

	/** Return paygrade list. */
	public Collection<SelectItem> getPaygradeItems() {
		return paygradeList;
	}

	public void add() {	  
//	    if (!employeeInSystem()){
//	        return false
//	    }
	    flexTime = 0;
	    vacationTime = 0;
		Employee e = new Employee(this);
		Credential c = new Credential(this.userName, this.password);
		employeeManager.addEmployee(e);
		credentialManager.persist(c);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	//Validates that the employee username and number are not already in the system
//	private boolean employeeInSystem() {
//	     Employee one = employeeManager.getEmployeeByUserName(this.userName);
//      Employee two = employeeManager.getEmployeeByEmpNum(this.empNum);
//      
//      System.out.println(one);
//      System.out.println(two);
//      
//      if (one != null || two != null) {
//          return true;
//      }
//	    
//	    return false;
//	}
	
}
