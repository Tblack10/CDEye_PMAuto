package CDEye_PMAuto.backend.paygrade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Entity
@Table(name="paygrades")
@Named("paygrade")
@RequestScoped
public class Paygrade implements Serializable {
	@Transient
	@Inject @Dependent private PaygradeManager paygradeManager;
	
	@Id
	@Column(name="id")
	@Type(type = "uuid-char")
	protected UUID id;
	
	@Column(name="name")
	protected String name;
	
	@Column(name="salary")
	protected BigDecimal salary;

	public Paygrade() {
		this.id = UUID.randomUUID();
		this.name = "";
		this.salary = new BigDecimal(1000);
	}
	
	public Paygrade(Paygrade p) {
		this.id = p.id;
		this.name = p.name;
		this.salary = p.salary;
	}
	
	public Paygrade(UUID id, String name, BigDecimal salary) {
		super();
		this.id = id;
		this.name = name;
		this.salary = salary;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	/**
	 * Add new paygrade to db
	 * @return refresh page
	 */
	public String add() {
		paygradeManager.persist(this);
		return "";
	}
	
}
