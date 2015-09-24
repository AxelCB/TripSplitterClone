package org.kairos.tripSplitterClone.vo.expense;

/**
 * Created on 9/17/15 by
 *
 * @author AxelCollardBovy.
 */
public enum E_ExpenseSplittingForm {
	EQUAL_SPLITTING(ExpenseEqualSplittingStrategy.class),
	PROPORTIONAL_SPLITTING(ExpenseProportionalSplittingStrategy.class);

	/**
	 * Cost Sharing Splitting Strategy Class
	 */
	private Class<? extends ExpenseSplittingAbstractStrategy> expenseSplittingStrategyClass;

	/**
	 * Enum Constructor
	 * @param expenseSplittingStrategyClass
	 */
	private E_ExpenseSplittingForm(Class<? extends ExpenseSplittingAbstractStrategy> expenseSplittingStrategyClass){
		this.expenseSplittingStrategyClass = expenseSplittingStrategyClass;
	}

	public Class<? extends ExpenseSplittingAbstractStrategy> getExpenseSplittingStrategyClass(){
		return this.expenseSplittingStrategyClass;
	}
}
