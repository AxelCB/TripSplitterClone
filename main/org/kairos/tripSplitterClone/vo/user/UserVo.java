package org.kairos.tripSplitterClone.vo.user;

import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.validator.routines.EmailValidator;
import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.kairos.tripSplitterClone.vo.trip.UserTripVo;
import org.kairos.tripSplitterClone.web.I_MessageSolver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 */
public class UserVo extends AbstractVo implements Serializable {

	/**
	 * Email and also username
	 */
	private String email;

	/**
	 * Password for login (saved encrypted)
	 */
	private String password;

	/**
	 * User's first and lastname
	 */
	private String name;

	/**
	 * Hash cost for the BCrypt algorithm.
	 */
	public Long hashCost;

	/**
	 * User Authentication Token
	 */
	private String token;

	/**
	 * Total login Attempts
	 */
	private Integer loginAttempts;

	/**
	 * Collection of trips in which the user participates in.
	 */
	private List<UserTripVo> trips = new ArrayList<>();

	/**
	 * Empty Constructor
	 */
	public UserVo() {}

	@Override
	public String validate(I_MessageSolver messageSolver) {
//		EmailValidator emailValidator = EmailValidator.getInstance();
		if(StringUtils.isBlank(this.getEmail())){
			return messageSolver.getMessage("fx.user.field.email.notNull");
		}
//		if(!emailValidator.isValid(this.getEmail())){
//			return messageSolver.getMessage("fx.user.field.email.wrongFormat");
//		}
		if(StringUtils.isBlank(this.getName())){
			return messageSolver.getMessage("fx.user.field.name.notNull");
		}
		if(StringUtils.isBlank(this.getPassword())){
			return messageSolver.getMessage("fx.user.field.password.notNull");
		}
		return super.validate(messageSolver);
	}

	public String getUsername() {
		return email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<UserTripVo> getTrips() {
		return trips;
	}

	public void setTrips(List<UserTripVo> trips) {
		this.trips = trips;
	}

	public Long getHashCost() {
		return hashCost;
	}

	public void setHashCost(Long hashCost) {
		this.hashCost = hashCost;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getLoginAttempts() {
		return loginAttempts;
	}

	public void setLoginAttempts(Integer loginAttempts) {
		this.loginAttempts = loginAttempts;
	}
}
