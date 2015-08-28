package org.kairos.tripSplitterClone.vo.trip;

import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.kairos.tripSplitterClone.vo.destination.CityVo;
import org.kairos.tripSplitterClone.vo.destination.CountryVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 */
public class TripVo extends AbstractVo implements Serializable {

	private List<UserTripVo> participants;

	/**
	 * Creator and owner of the trip
	 */
	private UserVo owner;

	private CountryVo country;

	private CityVo city;

	/**
	 * Empty Constructor
	 */
	public TripVo() {}

	public Boolean isOwner(UserVo user){
		if(user.equals(owner)){
			return Boolean.TRUE;
		}else{
			return Boolean.FALSE;
		}
	}

	public List<UserTripVo> getParticipants() {
		return participants;
	}

	public void setParticipants(List<UserTripVo> participants) {
		this.participants = participants;
	}

	public UserVo getOwner() {
		return owner;
	}

	public void setOwner(UserVo owner) {
		this.owner = owner;
	}

	public CountryVo getCountry() {
		return country;
	}

	public void setCountry(CountryVo country) {
		this.country = country;
	}

	public CityVo getCity() {
		return city;
	}

	public void setCity(CityVo city) {
		this.city = city;
	}
}
