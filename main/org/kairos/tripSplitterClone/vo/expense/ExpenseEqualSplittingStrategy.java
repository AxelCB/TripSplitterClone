package org.kairos.tripSplitterClone.vo.expense;

import org.kairos.tripSplitterClone.vo.account.AccountVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 9/17/15 by
 *
 * @author AxelCollardBovy.
 */
public class ExpenseEqualSplittingStrategy extends ExpenseSplittingAbstractStrategy {

	@Override
	public List<ExpenseMovementVo> splitExpense(ExpenseVo expense) {
		List<ExpenseMovementVo> expenseMovementVos = new ArrayList<>();
		AccountVo paymentAccount = expense.getPaymentMovement().getFrom();
		for(TravelerProportionVo travelerProportionVo : this.getTravelerProportionVos()){
			AccountVo travelerAccount = travelerProportionVo.getTraveler().getAccountInTrip(expense.getTrip());
			ExpenseMovementVo expenseMovementVo;
			if(travelerAccount.equals(paymentAccount)){
				expenseMovementVo = new ExpenseMovementVo(expense,
						travelerAccount.earn(expense.getAmount().divide(new BigDecimal(travelerProportionVos.size()))));
			}else{
				expenseMovementVo = new ExpenseMovementVo(expense,
						travelerAccount.transfer(expense.getAmount().divide(new BigDecimal(travelerProportionVos.size())),paymentAccount));
			}
			expenseMovementVos.add(expenseMovementVo);
		}
		return expenseMovementVos;
	}
}
