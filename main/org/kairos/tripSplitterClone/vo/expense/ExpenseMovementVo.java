package org.kairos.tripSplitterClone.vo.expense;

import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.kairos.tripSplitterClone.vo.account.MovementVo;

/**
 * Created on 9/10/15 by
 *
 * @author AxelCollardBovy.
 */
public class ExpenseMovementVo extends AbstractVo {

    /**
     * Expense
     */
    private ExpenseVo expense;

    /**
     * Movement
     */
    private MovementVo movement;

    /**
     * Default empty constructor
     */
    public ExpenseMovementVo() {}

    /**
     * Constructor using fields
     *
     * @param expense
     * @param movement
     */
    public ExpenseMovementVo(ExpenseVo expense, MovementVo movement) {
        this.expense = expense;
        this.movement = movement;
    }

    public ExpenseVo getExpense() {
        return expense;
    }

    public void setExpense(ExpenseVo expense) {
        this.expense = expense;
    }

    public MovementVo getMovement() {
        return movement;
    }

    public void setMovement(MovementVo movement) {
        this.movement = movement;
    }

    @Override
    public String validate() {
        if(this.getExpense()== null || this.getExpense().validate()!=null){
            return "Needs to be owned by a valid expense";
        }
        if(this.getMovement()== null || this.getMovement().validate()!=null){
            return "Invalid movement";
        }
        return null;
    }
}
