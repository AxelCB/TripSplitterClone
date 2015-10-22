package org.kairos.tripSplitterClone.vo.account;

import org.kairos.tripSplitterClone.model.account.E_MovementStatus;
import org.kairos.tripSplitterClone.utils.exception.ValidationException;
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
	private List<MovementVo> outMovements = new ArrayList<>();

	/**
	 * List of all in movements.
	 */
	private List<MovementVo> inMovements = new ArrayList<>();

	/**
	 * User owner of the account (used for the view only)
	 */
	private UserVo user;

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

	@Override
	public String validate() {
		if(this.getBalance()==null){
			return "Balance cannot be null";
		}
		if(this.getCreation()==null){
			return "Account must have a creation date";
		}
		return null;
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
		return this.getInMovements().stream().filter(movementVo -> movementVo.getStatus().equals(E_MovementStatus.PAID)).map(MovementVo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add)
				.subtract(this.getOutMovements().stream().filter(movementVo -> movementVo.getStatus().equals(E_MovementStatus.PAID)).map(MovementVo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
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

    public MovementVo spend(BigDecimal amount) throws ValidationException {
        MovementVo movement = new MovementVo(null,this,Calendar.getInstance().getTime(),amount,E_MovementStatus.PAID);
        this.outMovements.add(movement);
	    this.setBalance(this.getBalance().subtract(movement.getAmount()));
	    return movement;
	}

    public MovementVo earn(BigDecimal amount) throws ValidationException {
        MovementVo movement = new MovementVo(this,null,Calendar.getInstance().getTime(),amount,E_MovementStatus.PAID);
        this.inMovements.add(movement);
	    this.setBalance(this.getBalance().add(movement.getAmount()));
	    return movement;
    }
    public MovementVo transfer(BigDecimal amount,AccountVo recipient) throws ValidationException {
        MovementVo movement = new MovementVo(recipient,this,Calendar.getInstance().getTime(),amount);
	    if(!recipient.equals(this)){
		    this.outMovements.add(movement);
	    }
        recipient.addInMovement(movement);
	    return movement;
    }

	public BigDecimal totalOwed(){
		return this.getOutMovements().stream().filter(movementVo ->
				movementVo.getStatus().equals(E_MovementStatus.PENDING)
						&& movementVo.getDeleted().equals(Boolean.FALSE)
						&& movementVo.getFrom().equals(this)
						&& movementVo.getTo() != null)
				.map(movementVo -> movementVo.getAmount())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public BigDecimal totalShouldHavePaid(){
		BigDecimal paidExpenses = this.getInMovements().stream()
				.filter(movementVo -> movementVo.getDeleted().equals(Boolean.FALSE)
						&& movementVo.getFrom() == null
						&& movementVo.getTo().equals(this)
						&& movementVo.getStatus().equals(E_MovementStatus.PAID))
				.map(movementVo -> movementVo.getAmount())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return this.getOutMovements().stream()
				.filter(movementVo -> movementVo.getDeleted().equals(Boolean.FALSE)
						&& ((movementVo.getFrom().equals(this) && movementVo.getTo() != null) && movementVo.getStatus().equals(E_MovementStatus.PENDING)
							|| (movementVo.getFrom() == null && movementVo.getTo().equals(this) && movementVo.getStatus().equals(E_MovementStatus.PAID))))
				.map(movementVo -> movementVo.getAmount())
				.reduce(paidExpenses, BigDecimal::add);
	}

	public BigDecimal totalPaid(){
		return this.getOutMovements().stream().filter(movementVo ->
				movementVo.getStatus().equals(E_MovementStatus.PAID)
						&& movementVo.getDeleted().equals(Boolean.FALSE)
						&& movementVo.getFrom().equals(this)
						&& movementVo.getTo()==null)
				.map(movementVo -> movementVo.getAmount())
				.reduce(BigDecimal.ZERO,BigDecimal::add);
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

	public UserVo getUser() {
		return user;
	}

	public void setUser(UserVo user) {
		this.user = user;
	}
}
