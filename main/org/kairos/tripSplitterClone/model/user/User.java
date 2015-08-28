package org.kairos.tripSplitterClone.model.user;

import org.kairos.tripSplitterClone.model.I_Model;
import org.kairos.tripSplitterClone.model.trip.UserTrip;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created on 8/21/15 by
 *
 * @author AxelCollardBovy.
 */
@Entity
@AutoProperty(policy= DefaultPojomaticPolicy.TO_STRING)
public class User implements Serializable,I_Model {

	/**
	 * Entity ID.
	 */
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="user_seq")
	@SequenceGenerator(name="user_seq",sequenceName="user_seq",allocationSize=1)
	@Property(policy= PojomaticPolicy.HASHCODE_EQUALS)
	private Long id;

	/**
	 * Logic deletion flag.
	 */
	private Boolean deleted;

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
	 * Total login Attempts
	 */
	private Integer loginAttempts;

	/**
	 * Collection of trips in which the user participates in.
	 */
	@OneToMany(mappedBy="user",cascade = CascadeType.ALL)
	@Property(policy = PojomaticPolicy.NONE)
	private List<UserTrip> trips;

	/**
	 * Empty Constructor
	 */
	public User() {}

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

	public List<UserTrip> getTrips() {
		return trips;
	}

	public void setTrips(List<UserTrip> trips) {
		this.trips = trips;
	}

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

	public Long getHashCost() {
		return hashCost;
	}

	public void setHashCost(Long hashCost) {
		this.hashCost = hashCost;
	}

	public Integer getLoginAttempts() {
		return loginAttempts;
	}

	public void setLoginAttempts(Integer loginAttempts) {
		this.loginAttempts = loginAttempts;
	}
}
