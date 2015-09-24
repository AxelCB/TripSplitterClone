package org.kairos.tripSplitterClone.vo.expense;

import org.kairos.tripSplitterClone.utils.exception.IncompleteProportionException;
import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.kairos.tripSplitterClone.vo.account.MovementVo;
import org.kairos.tripSplitterClone.vo.trip.TripVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 9/10/15 by
 *
 * @author AxelCollardBovy.
 */
public class ExpenseVo extends AbstractVo{

	/**
	 * Movement that represents the real payment
	 */
	private MovementVo paymentMovement;

	/**
	 * Short Description
	 */
	private String description;

	/**
	 * Trip
	 */
	private TripVo trip;

	private List<ExpenseMovementVo> expenseMovements = new ArrayList<>();

	private ExpenseSplittingAbstractStrategy expenseSplittingStrategy;

	public ExpenseVo() {}

	public ExpenseVo(TripVo trip, BigDecimal amount,UserVo payingUser) {
		this.trip = trip;
		this.paymentMovement = payingUser.getAccountInTrip(trip).spend(amount);
	}

	public void splitExpense(E_ExpenseSplittingForm expenseSplittingForm,List<TravelerProportionVo> travelerProportionVos) throws IncompleteProportionException{
		try {
			this.setExpenseSplittingStrategy((expenseSplittingForm.getExpenseSplittingStrategyClass()).newInstance());
			this.getExpenseSplittingStrategy().setTravelerProportionVos(travelerProportionVos);
			this.setExpenseMovements(this.getExpenseSplittingStrategy().splitExpense(this));
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public ExpenseSplittingAbstractStrategy getExpenseSplittingStrategy() {
		return expenseSplittingStrategy;
	}

	public void setExpenseSplittingStrategy(ExpenseSplittingAbstractStrategy expenseSplittingStrategy) {
		this.expenseSplittingStrategy = expenseSplittingStrategy;
	}

	public BigDecimal getAmount() {
		return paymentMovement.getAmount();
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

	public List<ExpenseMovementVo> getExpenseMovements() {
		return expenseMovements;
	}

	public void setExpenseMovements(List<ExpenseMovementVo> expenseMovements) {
		this.expenseMovements = expenseMovements;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}