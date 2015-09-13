package org.kairos.tripSplitterClone.vo.trip;

import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.kairos.tripSplitterClone.vo.account.AccountVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 */
public class UserTripVo extends AbstractVo implements Serializable{

	/**
	 * User
	 */
	private UserVo user;

	/**
	 * Trip
	 */
	private TripVo trip;

	/**
	 * Account Vo
	 */
	private AccountVo account;

	/**
	 * Empty Constructor
	 */
	public UserTripVo() {
		account = new AccountVo();
	}

	public UserTripVo(UserVo user, TripVo trip) {
		this.user = user;
		this.trip = trip;
		account = new AccountVo(Calendar.getInstance().getTime());
	}

	public UserVo getUser() {
		return user;
	}

	public void setUser(UserVo user) {
		this.user = user;
	}

	public TripVo getTrip() {
		return trip;
	}

	public void setTrip(TripVo trip) {
		this.trip = trip;
	}

	public AccountVo getAccount() {
		return account;
	}

	public void setAccount(AccountVo account) {
		this.account = account;
	}
}
