package org.kairos.tripSplitterClone.vo.trip;

import org.kairos.tripSplitterClone.utils.exception.ValidationException;
import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created on 10/17/15 by
 *
 * @author AxelCollardBovy.
 */
public class TotalPerUserVo extends AbstractVo implements Serializable{

	/**
	 * User
	 */
	private UserVo user;

	/**
	 * Amount of the debt
	 */
	private BigDecimal amount;

	/**
	 * Empty Constructor
	 */
	public TotalPerUserVo() {}

	public TotalPerUserVo(Map.Entry<UserVo,BigDecimal> totalPerUserMap) throws ValidationException{
		this(totalPerUserMap.getKey(),totalPerUserMap.getValue());
	}

	public TotalPerUserVo(UserVo user, BigDecimal amount) throws ValidationException {
		String validationResponse = user.validate();
		if(validationResponse != null){
			throw new ValidationException(validationResponse);
		}
		if(amount==null && amount.compareTo(BigDecimal.ZERO)<=0){
			throw new ValidationException("Amount must be greater than zero");
		}
		this.user = user;
		this.amount=amount;
	}

	public UserVo getUser() {
		return user;
	}

	public void setUser(UserVo user) {
		this.user = user;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String validate() {
		if(this.getUser()==null || this.getUser().validate()!=null){
			return "Needs a valid user";
		}
		if(this.getAmount()==null || this.getAmount().compareTo(BigDecimal.ZERO)<=0){
			return "Needs a valid amount";
		}
		return null;
	}
}
