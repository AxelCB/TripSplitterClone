package org.kairos.tripSplitterClone.vo.expense;

import org.kairos.tripSplitterClone.utils.exception.IncompleteProportionException;
import org.kairos.tripSplitterClone.vo.account.AccountVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 9/17/15 by
 *
 * @author AxelCollardBovy.
 */
public class ExpenseProportionalSplittingStrategy extends ExpenseSplittingAbstractStrategy {

	@Override
	public List<ExpenseMovementVo> splitExpense(ExpenseVo expense)throws IncompleteProportionException {
		BigDecimal totalProportion = new BigDecimal(0);
		for(TravelerProportionVo travelerProportionVo : this.getTravelerProportionVos()){
			totalProportion = totalProportion.add(travelerProportionVo.getProportion());
		}
		if(totalProportion.intValue()==100){
			List<ExpenseMovementVo> expenseMovementVos = new ArrayList<>();
			AccountVo paymentAccount = expense.getPaymentMovement().getFrom();
			for(TravelerProportionVo travelerProportionVo : this.getTravelerProportionVos()){
				AccountVo travelerAccount = travelerProportionVo.getTraveler().getAccountInTrip(expense.getTrip());
				ExpenseMovementVo expenseMovementVo;
				if(travelerAccount.equals(paymentAccount)){
					expenseMovementVo = new ExpenseMovementVo(expense,
							travelerAccount.earn(expense.getAmount()
									.multiply(travelerProportionVo.getProportion())
									.divide(new BigDecimal(100))));
				}else{
					expenseMovementVo = new ExpenseMovementVo(expense,
							travelerAccount.transfer(expense.getAmount()
									.multiply(travelerProportionVo.getProportion())
									.divide(new BigDecimal(100)), paymentAccount));
				}
				expenseMovementVos.add(expenseMovementVo);
			}
			return expenseMovementVos;
		}else{
			throw new IncompleteProportionException("Total proportion was not equal to 100%");
		}
	}
}
