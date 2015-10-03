package org.kairos.tripSplitterClone.vo.expense;

import org.kairos.tripSplitterClone.utils.exception.ValidationException;
import org.kairos.tripSplitterClone.vo.user.UserVo;

import java.math.BigDecimal;

/**
 * Created on 9/17/15 by
 *
 * @author AxelCollardBovy.
 */
public class TravelerProportionVo {

	/**
	 * Traveler
	 */
	private UserVo traveler;

	/**
	 * Proportion
	 */
	private BigDecimal proportion;

	/**
	 * Default empty constructor
	 */
	public TravelerProportionVo() {}

	/**
	 * Constructor using fields
	 *
	 * @param traveler
	 * @param proportion
	 */
	public TravelerProportionVo(UserVo traveler, BigDecimal proportion) throws ValidationException {
		String validationResponse = traveler.validate();
		if(validationResponse!=null){
			throw new ValidationException(validationResponse);
		}
		this.traveler = traveler;
		this.proportion = proportion;
	}

	public UserVo getTraveler() {
		return traveler;
	}

	public void setTraveler(UserVo traveler) {
		this.traveler = traveler;
	}

	public BigDecimal getProportion() {
		return proportion;
	}

	public void setProportion(BigDecimal proportion) {
		this.proportion = proportion;
	}
}
