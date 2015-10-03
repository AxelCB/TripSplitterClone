package org.kairos.tripSplitterClone.vo.expense;

import org.kairos.tripSplitterClone.utils.exception.IncompleteProportionException;
import org.kairos.tripSplitterClone.utils.exception.ValidationException;
import org.kairos.tripSplitterClone.vo.user.UserVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 9/17/15 by
 *
 * @author AxelCollardBovy.
 */
public abstract class ExpenseSplittingAbstractStrategy {

	protected List<TravelerProportionVo> travelerProportionVos = new ArrayList<>();

	/**
	 * Default empty constructor
	 */
	public ExpenseSplittingAbstractStrategy() {}

	/**
	 * Constructor with traveler proportion vos
	 * @param travelerProportionVos
	 */
	public ExpenseSplittingAbstractStrategy(List<TravelerProportionVo> travelerProportionVos) {
		this.travelerProportionVos = travelerProportionVos;
	}

	public abstract List<ExpenseMovementVo> splitExpense (ExpenseVo expense) throws IncompleteProportionException, ValidationException;

	public List<TravelerProportionVo> getTravelerProportionVos() {
		return travelerProportionVos;
	}

	public void setTravelerProportionVos(List<TravelerProportionVo> travelerProportionVos) {
		this.travelerProportionVos = travelerProportionVos;
	}
}
