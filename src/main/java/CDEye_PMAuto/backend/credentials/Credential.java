package CDEye_PMAuto.backend.credentials;

import java.io.Serializable;
import java.util.UUID;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * Login credentials.
 */
@Entity
@Table(name="credentials")
@Named("credentials")
@SessionScoped
public class Credential implements Serializable {
    
	@Id
	@Type(type = "uuid-char")
	@Column(name="id")
	protected UUID id;
    
	/** The employee's name. */
	@Column(name="userName")
    protected String userName;
	
    /** The password. */
    @Column(name="password")
    protected String password;

    public Credential() {}
    
    public Credential(String userName, String password) {
    	id = UUID.randomUUID();
    	this.userName = userName;
    	this.password = password;
    }
    
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
   

}
