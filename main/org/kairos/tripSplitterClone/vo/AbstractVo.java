package org.kairos.tripSplitterClone.vo;

import org.kairos.tripSplitterClone.web.I_MessageSolver;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

import java.io.Serializable;

/**
 * Abstract Value Object.
 *
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 */
public abstract class AbstractVo implements Serializable  {
	
	/**
	 * VO id.
	 */
	@Property(policy=PojomaticPolicy.ALL)
	private Long id;
	
	/**
	 * Validates this VO.
	 * 
//	 * @param wch the web context holder.
	 * @param messageSolver the message solver
	 * 
	 * @return string iif is not valid
	 */
	public String validate(I_MessageSolver messageSolver) {
		return null;
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
	
}
