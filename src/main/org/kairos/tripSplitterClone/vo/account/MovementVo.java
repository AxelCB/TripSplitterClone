package org.kairos.tripSplitterClone.vo.account;

import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.pojomatic.Pojomatic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 */
public class MovementVo extends AbstractVo implements Serializable{

	/**
	 * Moved amount of value
	 */
	private BigDecimal amount;

	/**
	 * Timestamp for the movement
	 */
	private Date timestamp;

	/**
	 * The account from where the amount came
	 */
	private AccountVo from;

	/**
	 * The account to where the amount went
	 */
	private AccountVo to;

	/**
	 * Empty Constructor
	 */
	public MovementVo() {}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return this.amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the from
	 */
	public AccountVo getFrom() {
		return this.from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(AccountVo from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public AccountVo getTo() {
		return this.to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(AccountVo to) {
		this.to = to;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return Pojomatic.equals(this, obj);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Pojomatic.hashCode(this);
	}

}
