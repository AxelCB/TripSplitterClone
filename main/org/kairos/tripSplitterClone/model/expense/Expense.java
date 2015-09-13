package org.kairos.tripSplitterClone.model.expense;

import org.kairos.tripSplitterClone.model.I_Model;
import org.kairos.tripSplitterClone.model.account.Movement;
import org.kairos.tripSplitterClone.model.trip.Trip;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created on 9/10/15 by
 *
 * @author AxelCollardBovy.
 */
@Entity
@AutoProperty(policy= DefaultPojomaticPolicy.TO_STRING)
public class Expense implements I_Model{

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="expense_seq")
	@SequenceGenerator(name="expense_seq",sequenceName="expense_seq",allocationSize=1)
	@Property(policy= PojomaticPolicy.HASHCODE_EQUALS)
	private Long id;

	/**
	 * Logic deletion flag.
	 */
	private Boolean deleted = Boolean.FALSE;

	/**
	 * Expense amount (not divided yet)
	 */
	@Column(precision = 19, scale = 2)
	private BigDecimal amount;

	/**
	 * Movement that represents the real payment
	 */
	@OneToOne
	private Movement paymentMovement;

	@ManyToOne
	private Trip trip;

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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Movement getPaymentMovement() {
		return paymentMovement;
	}

	public void setPaymentMovement(Movement paymentMovement) {
		this.paymentMovement = paymentMovement;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}
}