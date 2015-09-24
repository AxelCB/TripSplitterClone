package org.kairos.tripSplitterClone.model.expense;

import org.eclipse.persistence.jpa.config.Cascade;
import org.kairos.tripSplitterClone.model.I_Model;
import org.kairos.tripSplitterClone.model.account.Movement;
import org.kairos.tripSplitterClone.model.trip.Trip;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 9/10/15 by
 *
 * @author AxelCollardBovy.
 */
@Entity
@AutoProperty(policy= DefaultPojomaticPolicy.TO_STRING)
public class Expense implements I_Model {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "expense_seq")
	@SequenceGenerator(name = "expense_seq", sequenceName = "expense_seq", allocationSize = 1)
	@Property(policy = PojomaticPolicy.HASHCODE_EQUALS)
	private Long id;

	/**
	 * Logic deletion flag.
	 */
	private Boolean deleted = Boolean.FALSE;

	/**
	 * Short Description
	 */
	private String description;

	/**
	 * Movement that represents the real payment
	 */
	@OneToOne(cascade = CascadeType.ALL)
	private Movement paymentMovement;

	@OneToMany(mappedBy="expense",cascade = CascadeType.ALL)
	private List<ExpenseMovement> expenseMovements = new ArrayList<>();

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
		return paymentMovement.getAmount();
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

	public List<ExpenseMovement> getExpenseMovements() {
		return expenseMovements;
	}

	public void setExpenseMovements(List<ExpenseMovement> expenseMovements) {
		this.expenseMovements = expenseMovements;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return Pojomatic.equals(this, obj);
	}
}