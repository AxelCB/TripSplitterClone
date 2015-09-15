package org.kairos.tripSplitterClone.vo.expense;

import org.kairos.tripSplitterClone.model.I_Model;
import org.kairos.tripSplitterClone.model.account.Movement;
import org.kairos.tripSplitterClone.model.trip.Trip;
import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.kairos.tripSplitterClone.vo.account.MovementVo;
import org.kairos.tripSplitterClone.vo.trip.TripVo;
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
public class ExpenseVo extends AbstractVo{

	/**
	 * Expense amount (not divided yet)
	 */
	private BigDecimal amount;

	/**
	 * Movement that represents the real payment
	 */
	private MovementVo paymentMovement;

	private TripVo trip;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public MovementVo getPaymentMovement() {
		return paymentMovement;
	}

	public void setPaymentMovement(MovementVo paymentMovement) {
		this.paymentMovement = paymentMovement;
	}

	public TripVo getTrip() {
		return trip;
	}

	public void setTrip(TripVo trip) {
		this.trip = trip;
	}
}