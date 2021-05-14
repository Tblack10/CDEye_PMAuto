package CDEye_PMAuto.backend.credentials;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Used for creating, accessing, and deleting credentials.
 */
@Named("credentialsListBean")
@ApplicationScoped
public class CredentialsList implements Serializable {

    /**
     * Persistent list of credentials to be used for accessing the system.
     */
    private static Map<String, String> credentialsList;

    /**
     * Constructor with hardcoded users.
     */
    public CredentialsList() {
        credentialsList = new HashMap<String, String>();
        credentialsList.put("user1", "password1");
        credentialsList.put("jdoe", "password2");
        credentialsList.put("jsmith", "password3");
    }

    /**
     * Getter for the persistent list of credentials.
     * 
     * @return a map of strings that represents valid credentials for accessing the
     *         system.
     */
    public Map<String, String> getCredentials() {
        return credentialsList;
    }

    /**
     * Method to find credentials given a username.
     * 
     * @param userName the username of an employee to validate or update the
     *                 credentials for.
     * @return the Ceredentials of the passed in employee.
     */
    public Credential findCredentials(final String userName) {
        if (credentialsList.containsKey(userName)) {
            Credential c = new Credential();
            c.setUserName(userName);
            c.setPassword(credentialsList.get(userName));
            return c;
        }
        return null;
    }

    /**
     * Method to validate that the passed in credentials match a member of the
     * credentials list.
     * 
     * @param credentials to validate, entered into the front end.
     * @return a boolean representing whether the credentials are valid.
     */
    public boolean validCredentials(Credential credentials) {
        // ensure username exists in credentials list
        if (!credentialsList.containsKey(credentials.getUserName())) {
            return false;
        }
        return (credentialsList.get(credentials.getUserName()).equals(credentials.getPassword()));
    }

    /**
     * Method to add credentials to the list.
     * 
     * @param credentials to add to the list of credentials.
     */
    public void addCredentials(final Credential credentials) {
        credentialsList.put(credentials.getUserName(), credentials.getPassword());
    }

    /**
     * Method to update the credentials (password) of a set of existing credentials.
     * 
     * @param credentials is a set of credentials with an existing username and a
     *                    new password.
     */
    public void updateCredentials(Credential credentials) {
        credentialsList.replace(credentials.getUserName(), credentials.getPassword());
    }

    /**
     * Method to remove some credentials from a list of credentials.
     * 
     * @param credentials a set of credentials to be removed from the list of
     *                    credentials.
     */
    public void deleteCredentials(Credential credentials) {
        credentialsList.remove(credentials.getUserName());
    }

}
