package org.kairos.tripSplitterClone.model.account;

import org.kairos.tripSplitterClone.model.I_Model;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User's account
 *
 * Created by
 * @author AxelCollardBovy on 8/21/15.
 */
@Entity
//@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@AutoProperty(policy= DefaultPojomaticPolicy.TO_STRING)
public class Account implements Serializable, I_Model {

	/**
	 *
	 */
	private static final long serialVersionUID = 1014576108216469171L;

	/**
	 * Entity ID.
	 */
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="account_seq")
	@SequenceGenerator(name="account_seq",sequenceName="account_seq",allocationSize=1)
	@Property(policy= PojomaticPolicy.HASHCODE_EQUALS)
	private Long id;

	/**
	 * Logic deletion flag.
	 */
	private Boolean deleted = Boolean.FALSE;

	/**
	 * Timestamp for the creation of the account.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date creation;

	/**
	 * Current balance of the account.
	 */
	@Column(precision=19, scale=2)
	private BigDecimal balance = new BigDecimal(0);

//	/**
//	 * Account floor.
//	 */
//	@Column(precision=19, scale=2)
//	private BigDecimal minimum;

	/**
	 * List of all out movements.
	 */
	@OneToMany(mappedBy="from",cascade = CascadeType.ALL)
	private List<Movement> outMovements = new ArrayList<>();

	/**
	 * List of all in movements.
	 */
	@OneToMany(mappedBy="to",cascade = CascadeType.ALL)
	private List<Movement> inMovements = new ArrayList<>();

	/**
	 * Empty Constructor
	 */
	public Account() {}

	/**
	 * Constructor with creation Date
	 */
	public Account(Date date) {
		this.setCreation(date);
	}

	/*
	 * (non-Javadoc)
	 * @see org.tripSplitterClone.core.model.I_Model#getId()
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tripSplitterClone.core.model.I_Model#setId(java.lang.Long)
	 */
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tripSplitterClone.core.model.I_Model#getDeleted()
	 */
	@Override
	public Boolean getDeleted() {
		return this.deleted;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tripSplitterClone.core.model.I_Model#setDeleted(java.lang.Boolean)
	 */
	@Override
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the outMovements
	 */
	public List<Movement> getOutMovements() {
		return this.outMovements;
	}

	/**
	 * @param outMovements the outMovements to set
	 */
	public void setOutMovements(List<Movement> outMovements) {
		this.outMovements = outMovements;
	}

	/**
	 * @return the inMovements
	 */
	public List<Movement> getInMovements() {
		return this.inMovements;
	}

	/**
	 * @param inMovements the inMovements to set
	 */
	public void setInMovements(List<Movement> inMovements) {
		this.inMovements = inMovements;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
//	public boolean equals(Object obj) {
//		return id.equals(((Account)obj).getId());
//	}
	public boolean equals(Object obj) {
		return Pojomatic.equals(this, obj);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Pojomatic.hashCode(this);
	}

	/**
	 * @return the creation
	 */
	public Date getCreation() {
		return this.creation;
	}

	/**
	 * @param creation the creation to set
	 */
	public void setCreation(Date creation) {
		this.creation = creation;
	}

	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return this.balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

//	/**
//	 * @return the minimum
//	 */
//	public BigDecimal getMinimum() {
//		return this.minimum;
//	}
//
//	/**
//	 * @param minimum the minimum to set
//	 */
//	public void setMinimum(BigDecimal minimum) {
//		this.minimum = minimum;
//	}

}
