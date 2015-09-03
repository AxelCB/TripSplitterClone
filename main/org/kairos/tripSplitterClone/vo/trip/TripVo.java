package org.kairos.tripSplitterClone.vo.trip;

import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.kairos.tripSplitterClone.vo.destination.CityVo;
import org.kairos.tripSplitterClone.vo.destination.CountryVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.kairos.tripSplitterClone.web.I_MessageSolver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 */
public class TripVo extends AbstractVo implements Serializable {

	/**
	 * Creator and owner of the trip
	 */
	private UserVo owner;

	/**
	 * Trip's destination
	 */
	private CityVo destination;

	/**
	 * Trip's travelers
	 */
	private List<UserTripVo> travelers = new ArrayList<>();

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

	@Override
	public String validate(I_MessageSolver messageSolver) {
		if(this.getOwner()==null){
			return messageSolver.getMessage("trip.field.owner.notNull");
		}
		if(this.getDestination()==null){
			return messageSolver.getMessage("trip.field.destination.notNull");
		}
		return super.validate(messageSolver);
	}

	public List<UserTripVo> getTravelers() {
		return travelers;
	}

	public void setTravelers(List<UserTripVo> travelers) {
		this.travelers = travelers;
	}

	public UserVo getOwner() {
		return owner;
	}

	public void setOwner(UserVo owner) {
		this.owner = owner;
	}

	public CityVo getDestination() {
		return destination;
	}

	public void setDestination(CityVo destination) {
		this.destination = destination;
	}

	public CityVo getCity() {
		return destination;
	}

	public void setCity(CityVo city) {
		this.destination = city;
	}
}
