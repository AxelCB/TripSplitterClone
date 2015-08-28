package org.kairos.tripSplitterClone.model.trip;

import org.kairos.tripSplitterClone.model.I_Model;
import org.kairos.tripSplitterClone.model.user.User;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created on 8/21/15 by
 *
 * @author AxelCollardBovy.
 */
@Entity
@AutoProperty(policy= DefaultPojomaticPolicy.TO_STRING)
public class UserTrip implements I_Model,Serializable{

	/**
	 * Entity ID.
	 */
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="usertrip_seq")
	@SequenceGenerator(name="usertrip_seq",sequenceName="usertrip_seq",allocationSize=1)
	@Property(policy= PojomaticPolicy.HASHCODE_EQUALS)
	private Long id;

	/**
	 * Logic deletion flag.
	 */
	private Boolean deleted;

	/**
	 * User
	 */
	@ManyToOne
	@Property(policy = PojomaticPolicy.NONE)
	private User user;

	/**
	 * Trip
	 */
	@ManyToOne
	@Property(policy = PojomaticPolicy.NONE)
	private Trip trip;

	/**
	 * Empty Constructor
	 */
	public UserTrip() {}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Boolean getDeleted() {
		return deleted;
	}

	@Override
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}
}
