package CDEye_PMAuto.backend.workpackage;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.Entity;
import javax.persistence.Table;

@Named("userNameBean")
@RequestScoped
public class UserNameBean implements Serializable {

	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
}
