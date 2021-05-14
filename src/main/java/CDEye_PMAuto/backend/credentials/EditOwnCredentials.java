package CDEye_PMAuto.backend.credentials;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Allows a user to edit their own credentials (password).
 */
@Named("editOwnCredentials")
@RequestScoped
public class EditOwnCredentials extends Credential implements Serializable {
    @Inject CredentialManager credentialManager;
    /**
     * @param userName is the userName of the user editing their own
     * credentials (password)
     * @return a string which will reload the current page.
     */
    public String updateOwnCredentials(String userName) {
        Credential cred = credentialManager.find(userName);
        cred.setPassword(this.getPassword());
        credentialManager.merge(cred);
        return "";
    }
}