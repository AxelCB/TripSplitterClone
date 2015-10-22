package org.kairos.tripSplitterClone.vo.trip;

import org.kairos.tripSplitterClone.utils.exception.ValidationException;
import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.kairos.tripSplitterClone.vo.account.AccountVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created on 10/17/15 by
 *
 * @author AxelCollardBovy.
 */
public class DebtVo extends AbstractVo implements Serializable{

	/**
	 * User who owns the debt
	 */
	private UserVo debtor;

	/**
	 * User who should receive the payment
	 */
	private UserVo creditor;

	/**
	 * Trip
	 */
	private TripVo trip;

	/**
	 * Amount of the debt
	 */
	private BigDecimal amount;

	/**
	 * Empty Constructor
	 */
	public DebtVo() {}

	public DebtVo(UserVo debtor,UserVo creditor, TripVo trip,BigDecimal amount) throws ValidationException {
		String validationResponse = debtor.validate();
		if(validationResponse != null){
			throw new ValidationException(validationResponse);
		}
		validationResponse = creditor.validate();
		if(validationResponse != null){
			throw new ValidationException(validationResponse);
		}
		validationResponse=trip.validate();
		if(validationResponse != null){
			throw new ValidationException(validationResponse);
		}
		if(amount==null && amount.compareTo(BigDecimal.ZERO)<=0){
			throw new ValidationException("Amount must be greater than zero");
		}
		this.debtor = debtor;
		this.creditor = creditor;
		this.trip = trip;
		this.amount=amount;
	}

	public UserVo getDebtor() {
		return debtor;
	}

	public void setDebtor(UserVo debtor) {
		this.debtor = debtor;
	}

	public UserVo getCreditor() {
		return creditor;
	}

	public void setCreditor(UserVo creditor) {
		this.creditor = creditor;
	}

	public TripVo getTrip() {
		return trip;
	}

	public void setTrip(TripVo trip) {
		this.trip = trip;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String validate() {
		if(this.getDebtor()==null || this.getDebtor().validate()!=null){
			return "Needs a valid debtor";
		}
		if(this.getCreditor()==null || this.getCreditor().validate()!=null){
			return "Needs a valid creditor";
		}
		if(this.getAmount()==null || this.getAmount().compareTo(BigDecimal.ZERO)<=0){
			return "Needs a valid amount";
		}
		if(this.getTrip()==null || this.getTrip().validate()!=null){
			return "Needs a valid trip";
		}
		return null;
	}
}
