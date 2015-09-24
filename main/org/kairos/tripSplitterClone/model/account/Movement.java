package org.kairos.tripSplitterClone.model.account;

import org.kairos.tripSplitterClone.model.I_Model;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Created on 8/21/15 by
 *
 * @author AxelCollardBovy.
 */
@Entity
@AutoProperty(policy = DefaultPojomaticPolicy.TO_STRING)
public class Movement implements Serializable, I_Model {

	/**
	 *
	 */
	private static final long serialVersionUID = -2814144092146445792L;

	/**
	 * Entity ID.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movement_seq")
	@SequenceGenerator( name = "movement_seq", sequenceName = "movement_seq", allocationSize = 1)
	@Property(policy = PojomaticPolicy.HASHCODE_EQUALS)
	private Long id;

	/**
	 * Logic deletion flag.
	 */
	private Boolean deleted;

	/**
	 * Transaction ID that identifies the transaction for a group of movements.
	 */
//	private Long transactionId;

	/**
	 * Moved amount of value
	 */
	@Column(precision = 19, scale = 2)
	private BigDecimal amount;

	/**
	 * Timestamp for the movement
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	/**
	 * The account from where the amount came
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	@Property(policy = PojomaticPolicy.NONE)
	private Account from;

	/**
	 * The account to where the amount went
	 */
	@ManyToOne
	@Property(policy = PojomaticPolicy.NONE)
	private Account to;

    /**
     * Movement status
     */
    @Enumerated(EnumType.STRING)
    private E_MovementStatus status;

	/**
	 * Empty Constructor
	 */
	public Movement() {
		this.timestamp = Calendar.getInstance().getTime();
	}

	public Movement(BigDecimal amount, Date timestamp) {
		this.amount = amount;
		this.timestamp = timestamp;
	}

	/**
	 * Fixes the accounts relations.
	 *
	 * @param em
	 *            the entity manager
	 */
//	private void fixAccounts(EntityManager em) {
//		if (this.getFrom() != null && this.getFrom().getId() == null) {
//			em.detach(this.getFrom());
//			this.setFrom(null);
//		}
//		if (this.getTo() != null && this.getTo().getId() == null) {
//			em.detach(this.getTo());
//			this.setTo(null);
//		}
//	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.tripSplitterClone.core.model.I_Model#getId()
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.tripSplitterClone.core.model.I_Model#setId(java.lang.Long)
	 */
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.tripSplitterClone.core.model.I_Model#getDeleted()
	 */
	@Override
	public Boolean getDeleted() {
		return this.deleted;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.tripSplitterClone.core.model.I_Model#setDeleted(java.lang.Boolean)
	 */
	@Override
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

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
	public Account getFrom() {
		return this.from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(Account from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public Account getTo() {
		return this.to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(Account to) {
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

    public E_MovementStatus getStatus() {
        return status;
    }

    public void setStatus(E_MovementStatus status) {
        this.status = status;
    }
}
