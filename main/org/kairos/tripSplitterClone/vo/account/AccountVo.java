package org.kairos.tripSplitterClone.vo.account;

import org.kairos.tripSplitterClone.model.account.E_MovementStatus;
import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.kairos.tripSplitterClone.vo.trip.UserTripVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.pojomatic.Pojomatic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * User's account
 *
 * Created by
 * @author AxelCollardBovy on 8/22/15.
 */
public class AccountVo extends AbstractVo implements Serializable {

	/**
	 * Timestamp for the creation of the account.
	 */
	private Date creation;

	/**
	 * Current balance of the account.
	 */
	private BigDecimal balance = new BigDecimal(0);

	/**
	 * List of all out movements.
	 */
	private List<MovementVo> outMovements = new ArrayList<>();;

	/**
	 * List of all in movements.
	 */
	private List<MovementVo> inMovements = new ArrayList<>();;

	/**
	 * Empty Constructor
	 */
	public AccountVo() {
	}

	/**
	 * Constructor with creation date
	 */
	public AccountVo(Date date) {
		this.setCreation(date);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return Pojomatic.equals(this, obj);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Pojomatic.hashCode(this);
	}

	/**
	 * @return the creation
	 */
	public Date getCreation() {
		return this.creation;
	}

	/**
	 * @param creation the creation to set
	 */
	public void setCreation(Date creation) {
		this.creation = creation;
	}

	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return this.balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

    public void addInMovement(MovementVo movementVo){
        this.inMovements.add(movementVo);
    }

    public void addOutMovement(MovementVo movementVo){
        this.outMovements.add(movementVo);
    }

    public MovementVo spend(BigDecimal amount){
        MovementVo movement = new MovementVo(null,this,Calendar.getInstance().getTime(),amount,E_MovementStatus.PAID);
        this.outMovements.add(movement);
	    this.setBalance(this.getBalance().subtract(movement.getAmount()));
	    return movement;
	}

    public MovementVo earn(BigDecimal amount){
        MovementVo movement = new MovementVo(this,null,Calendar.getInstance().getTime(),amount,E_MovementStatus.PAID);
        this.inMovements.add(movement);
	    this.setBalance(this.getBalance().add(movement.getAmount()));
	    return movement;
    }
    public MovementVo transfer(BigDecimal amount,AccountVo recipient){
        MovementVo movement = new MovementVo(recipient,this,Calendar.getInstance().getTime(),amount);
	    if(!recipient.equals(this)){
		    this.outMovements.add(movement);
	    }
        recipient.addInMovement(movement);
	    return movement;
    }

	public List<MovementVo> getOutMovements() {
		return outMovements;
	}

	public void setOutMovements(List<MovementVo> outMovements) {
		this.outMovements = outMovements;
	}

	public List<MovementVo> getInMovements() {
		return inMovements;
	}

	public void setInMovements(List<MovementVo> inMovements) {
		this.inMovements = inMovements;
	}
}
