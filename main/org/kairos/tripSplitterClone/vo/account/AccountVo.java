package org.kairos.tripSplitterClone.vo.account;

import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.pojomatic.Pojomatic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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

}