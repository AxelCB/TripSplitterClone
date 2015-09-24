package org.kairos.tripSplitterClone.model.trip;

import org.kairos.tripSplitterClone.model.I_Model;
import org.kairos.tripSplitterClone.model.destination.City;
import org.kairos.tripSplitterClone.model.expense.Expense;
import org.kairos.tripSplitterClone.model.user.User;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
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
	 * Entity ID.	public CityVo getCity() {
		return city;
	}

	public void setCity(CityVo city) {
		this.city = city;
	}
	 */
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="trip_seq")
	@SequenceGenerator(name="trip_seq",sequenceName="trip_seq",allocationSize=1)
	@Property(policy= PojomaticPolicy.HASHCODE_EQUALS)
	private Long id;

	/**
	 * Logic deletion flag.
	 */
	private Boolean deleted = Boolean.FALSE;

	@OneToMany(mappedBy="trip",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@Property(policy = PojomaticPolicy.NONE)
	private List<UserTrip> travelers = new ArrayList<>();

	/**
	 * Creator and owner of the trip
	 */
	@ManyToOne
	@Property(policy = PojomaticPolicy.NONE)
	private User owner;

	/**
	 * Destination
	 */
	@ManyToOne
	@Property(policy = PojomaticPolicy.NONE)
	private City destination;

	/**
	 * Expenses
	 */
	@OneToMany(mappedBy="trip",cascade = CascadeType.ALL)
	@Property(policy = PojomaticPolicy.NONE)
	private List<Expense> expenses = new ArrayList<>();

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

    /**
     * Constructor with owner and destination fields
     *
     * @param owner
     * @param destination
     */
    public Trip(User owner, City destination) {
        this.owner = owner;
        this.destination = destination;
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

	public List<UserTrip> getTravelers() {
		return travelers;
	}

	public void setTravelers(List<UserTrip> participants) {
		this.travelers = participants;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public City getDestination() {
		return destination;
	}

	public void setDestination(City destination) {
		this.destination = destination;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return Pojomatic.equals(this, obj);
	}

	public List<Expense> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<Expense> expenses) {
		this.expenses = expenses;
	}

	public void addTraveler(UserTrip userTrip){
		Boolean found = Boolean.FALSE;
		for(UserTrip auxUserTrip : this.getTravelers()){
			if(userTrip.equals(auxUserTrip)){
				found = Boolean.TRUE;
				break;
			}
		}
		if(!found){
			this.getTravelers().add(userTrip);
		}
	}

	public void addExpense(Expense expense){
		Boolean found = Boolean.FALSE;
		for(Expense auxExpense : this.getExpenses()){
			if(expense.equals(auxExpense)){
				found = Boolean.TRUE;
				break;
			}
		}
		if(!found){
			this.getExpenses().add(expense);
		}
	}
}
