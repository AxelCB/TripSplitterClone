package org.kairos.tripSplitterClone.model.trip;

import org.kairos.tripSplitterClone.model.I_Model;
import org.kairos.tripSplitterClone.model.user.User;
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
public class Trip implements Serializable,I_Model {

	/**
	 * Entity ID.
	 */
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="account_seq")
	@SequenceGenerator(name="account_seq",sequenceName="account_seq",allocationSize=1)
	@Property(policy= PojomaticPolicy.HASHCODE_EQUALS)
	private Long id;

	/**
	 * Logic deletion flag.
	 */
	private Boolean deleted;

	@OneToMany(mappedBy="trip")
	@Property(policy = PojomaticPolicy.NONE)
	private List<UserTrip> participants;

	/**
	 * Creator and owner of the trip
	 */
	@ManyToOne
	@Property(policy = PojomaticPolicy.NONE)
	private User owner;

	/**
	 * Empty Constructor
	 */
	public Trip() {}

	public Boolean isOwner(User user){
		if(user.equals(owner)){
			return Boolean.TRUE;
		}else{
			return Boolean.FALSE;
		}
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

	public List<UserTrip> getParticipants() {
		return participants;
	}

	public void setParticipants(List<UserTrip> participants) {
		this.participants = participants;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
}
