package org.kairos.tripSplitterClone.vo.trip;

import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;

import java.io.Serializable;

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
	 * Empty Constructor
	 */
	public UserTripVo() {}

	public UserTripVo(UserVo user, TripVo trip) {
		this.user = user;
		this.trip = trip;
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
}
