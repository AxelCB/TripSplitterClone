package org.kairos.tripSplitterClone.vo.expense;

import org.kairos.tripSplitterClone.model.I_Model;
import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.kairos.tripSplitterClone.vo.account.MovementVo;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

import javax.persistence.*;
import java.io.Serializable;

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
}
