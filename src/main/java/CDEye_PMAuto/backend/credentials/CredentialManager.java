package CDEye_PMAuto.backend.credentials;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import CDEye_PMAuto.backend.employee.Employee;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;

/**
 * Handle CRUD actions for Credentials.
 */
@Dependent
@Stateless
public class CredentialManager implements Serializable {
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = "inventory-jpa")
    EntityManager em;

    /**
     * Find a Credentials record from database.
     * 
     * @param username primary key for record.
     * @return the Credential record with key = username, null if not found.
     */
    public Credential find(String username) {
        return em.find(Credential.class, username);
    }

    /**
     * Persist Credential record into database. id must be unique.
     * 
     * @param credential the record to be persisted.
     */
    public void persist(Credential credential) {
        em.persist(credential);
    }

    /**
     * merge Credential record fields into existing database record.
     * 
     * @param credential the record to be merged.
     */
    public void merge(Credential credential) {
        em.merge(credential);
    }

    /**
     * Remove credential from database.
     * 
     * @param credential record to be removed from database
     */
    public void remove(Credential credential) {
        // attach credential
        credential = find(credential.getUserName());
        em.remove(credential);
    }

    /**
     * Return Categories table as array of Credential.
     * 
     * @return Credential[] of all records in Categories table
     */
    public Credential[] getAll() {
        // need to make sure the table name matches the one in the database
        TypedQuery<Credential> query = em.createQuery("select c from Credentials c", Credential.class);
        java.util.List<Credential> credentialsList = query.getResultList();
        Credential[] credentials = new Credential[credentialsList.size()];
        for (int i = 0; i < credentials.length; i++) {
            credentials[i] = credentialsList.get(i);
        }
        return credentials;
    }

    /**
     * Validates credentials by ensuring that the password of the credentials
     * passed in matches the password of the credential looked up by username.
     * @param credentials credentials entered by the user.
     * @return boolean for whether the credentials are valid.
     */
    public boolean validCredentials(Credential c) {
    	System.out.println("username: " + c.userName);
    	System.out.println("password: " + c.password);
    	TypedQuery<Credential> query = em.createQuery(
    			"SELECT c FROM Credential c WHERE c.userName LIKE :userName", Credential.class)
    			.setParameter("userName", "%" + c.getUserName() + "%");
    	List<Credential> credentials = query.getResultList();
    	System.out.println("credentials size:"+ credentials.size());
        
        // if credentials not in database
        if (credentials.size() == 0) {
            return false;
        } else {
        	Credential cred = credentials.get(0);
            System.out.println("credentials size:"+ cred);
            return c.getPassword().equals(cred.getPassword());
        }
    }
}
