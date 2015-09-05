package org.kairos.tripSplitterClone.utils;

import java.math.BigDecimal;

/**
 * Ugly fix to provide a way to decide between to types of representation
 * for the BigDecimal on a JSON Object.
 *
 * Created on 8/27/15 by
 *
 * @author AxelCollardBovy.
 * 
 */
public class BigDecimalWithoutTypeAdapting extends BigDecimal {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2865078744495824508L;

	/**
	 * Constructor with original big decimal.
	 * 
	 * @param value
	 */
	public BigDecimalWithoutTypeAdapting(BigDecimal value) {
		super(value.toString());
	}
}
