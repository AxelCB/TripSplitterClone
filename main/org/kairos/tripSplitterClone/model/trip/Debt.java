package org.kairos.tripSplitterClone.model.trip;

import org.kairos.tripSplitterClone.model.I_Model;
import org.kairos.tripSplitterClone.model.user.User;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created on 9/10/15 by
 *
 * @author AxelCollardBovy.
 */
@Entity
@AutoProperty(policy= DefaultPojomaticPolicy.TO_STRING)
public class Debt implements Serializable,I_Model {

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="debt_seq")
	@SequenceGenerator(name="debt_seq",sequenceName="debt_seq",allocationSize=1)
	@Property(policy= PojomaticPolicy.HASHCODE_EQUALS)
	private Long id;

	/**
	 * Logic deletion flag.
	 */
	private Boolean deleted = Boolean.FALSE;

	/**
	 * IF it is paid or still pending.
	 */
	@Enumerated(EnumType.STRING)
	private E_DebtStatus status;

	/**
	 * Debt's amount
	 */
	@Column(precision = 19, scale = 2)
	private BigDecimal amount;

	/**
	 * Debt's trip
	 */
	@ManyToOne
	@Property(policy = PojomaticPolicy.NONE)
	private Trip trip;

	/**
	 * Owner of the debt (the one who has to pay)
	 */
	@ManyToOne
	@Property(policy = PojomaticPolicy.NONE)
	private User debtor;

	/**
	 * The one who receives the debts money
	 */
	@ManyToOne
	@Property(policy = PojomaticPolicy.NONE)
	private User creditor;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Boolean getDeleted() {
		return deleted;
	}

	@Override
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public User getDebtor() {
		return debtor;
	}

	public void setDebtor(User debtor) {
		this.debtor = debtor;
	}

	public User getCreditor() {
		return creditor;
	}

	public void setCreditor(User creditor) {
		this.creditor = creditor;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	public E_DebtStatus getStatus() {
		return status;
	}

	public void setStatus(E_DebtStatus status) {
		this.status = status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
