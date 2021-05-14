package CDEye_PMAuto.backend.credentials;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * Used for editing the credentials (password) of an employee.
 */
@Named("editCredentialBean")
@RequestScoped
public class EditCredentials extends Credential implements Serializable {

    /**
     * Method that instructs the credentials list to persist an updated
     * set of credentials.
     * @return a string which will send the user to another screen
     */
    public String updateCreds() {
        return "somePage";
    }
}
