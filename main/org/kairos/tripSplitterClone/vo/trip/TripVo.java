package org.kairos.tripSplitterClone.vo.trip;

import org.kairos.tripSplitterClone.utils.exception.IncompleteProportionException;
import org.kairos.tripSplitterClone.utils.exception.ValidationException;
import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.kairos.tripSplitterClone.vo.destination.CityVo;
import org.kairos.tripSplitterClone.vo.destination.CountryVo;
import org.kairos.tripSplitterClone.vo.expense.E_ExpenseSplittingForm;
import org.kairos.tripSplitterClone.vo.expense.ExpenseVo;
import org.kairos.tripSplitterClone.vo.expense.TravelerProportionVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.kairos.tripSplitterClone.web.I_MessageSolver;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	 * Expenses
	 */
	private List<ExpenseVo> expenses = new ArrayList<>();

	/**
	 * Empty Constructor
	 */
	public TripVo() {}

	/**
	 * Constructor with Owner
	 */
	public TripVo(UserVo owner) {
		this.owner = owner;
	}

	public Boolean isOwner(UserVo user){
		if(user.equals(owner)){
			return Boolean.TRUE;
		}else{
			return Boolean.FALSE;
		}
	}

	@Override
	public String validate(I_MessageSolver messageSolver) {
//		if(this.getOwner()==null){
//			return messageSolver.getMessage("fx.trip.field.owner.notNull");
//		}
		if(this.getDestination()==null){
			return messageSolver.getMessage("fx.trip.field.destination.notNull");
		}
		return super.validate(messageSolver);
	}

	@Override
	public String validate() {
		if(this.getOwner()==null || this.getOwner().validate()!=null){
			return "Invalid trip owner";
		}
		if(this.getDestination()==null || this.getDestination().validate()!=null){
			return "Invalid trip destination";
		}
		return null;
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

	public List<ExpenseVo> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<ExpenseVo> expenses) {
		this.expenses = expenses;
	}

	public void addTraveler(UserVo traveler) throws ValidationException {
		Boolean found = Boolean.FALSE;
		for(UserTripVo userTripVo : this.getTravelers()){
			if(userTripVo.getUser().equals(traveler)){
				found = Boolean.TRUE;
				break;
			}
		}
		if(!found){
			UserTripVo userTripVo = new UserTripVo(traveler,this);
			this.getTravelers().add(userTripVo);
			traveler.getTrips().add(userTripVo);
		}
	}

	public UserVo removeTraveler(UserVo traveler){
//		Boolean found = Boolean.FALSE;
		for(UserTripVo userTripVo : this.getTravelers()){
			if(userTripVo.getUser().equals(traveler)){
//				found = Boolean.TRUE;
				this.getTravelers().remove(userTripVo);
				traveler.getTrips().remove(userTripVo);
				return userTripVo.getUser();
			}
		}
		return null;
	}

	public ExpenseVo addExpense(BigDecimal amount,String description,UserVo payingUser,E_ExpenseSplittingForm expenseSplittingForm,
									List<TravelerProportionVo> travelerProportionVos) throws IncompleteProportionException, ValidationException {
		ExpenseVo expenseVo = new ExpenseVo(this,amount,payingUser,description);
		expenseVo.splitExpense(expenseSplittingForm,travelerProportionVos);
		this.getExpenses().add(expenseVo);
		return expenseVo;
	}

	public List<ExpenseVo> listExpenses(){
		return this.getExpenses().stream().
				filter(expenseVo -> expenseVo.getDeleted().equals(Boolean.FALSE))
				.collect(Collectors.<ExpenseVo>toList());
	}
}
