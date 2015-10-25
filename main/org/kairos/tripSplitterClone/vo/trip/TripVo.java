package org.kairos.tripSplitterClone.vo.trip;

import org.kairos.tripSplitterClone.utils.exception.IncompleteProportionException;
import org.kairos.tripSplitterClone.utils.exception.ValidationException;
import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.kairos.tripSplitterClone.vo.account.AccountVo;
import org.kairos.tripSplitterClone.vo.destination.CityVo;
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
	 * Trip's title
	 */
	private String title;

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

	public TripVo(UserVo owner, CityVo destination, String title) throws ValidationException{
		this.owner = owner;
		this.destination = destination;
		this.title = title;
		this.addTraveler(owner);
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public List<DebtVo> calculateDebts() throws Exception {
		List<DebtVo> debts= new ArrayList<>();
//		List<AccountVo> accountVos = this.getTravelers().stream().map(userTripVo -> userTripVo.getAccount()).collect(Collectors.toList());
		for(UserTripVo debitorUserTrip : this.getTravelers()){
			BigDecimal remainingOwed = debitorUserTrip.getAccount().totalOwed().add(debitorUserTrip.getAccount().getBalance());
			if(remainingOwed.compareTo(BigDecimal.ZERO)>0){
				for(UserTripVo creditorUserTrip : this.getTravelers()){
					if(!creditorUserTrip.equals(debitorUserTrip)){
						if(remainingOwed.compareTo(creditorUserTrip.getAccount().getBalance())<1){
							//CREATE DEBT
							DebtVo debtVo = new DebtVo(debitorUserTrip.getUser(), creditorUserTrip.getUser(),this, creditorUserTrip.getAccount().getBalance());
							debts.add(debtVo);

							//Calculate remainders
							remainingOwed = remainingOwed.subtract(creditorUserTrip.getAccount().getBalance());
							creditorUserTrip.getAccount().setBalance(BigDecimal.ZERO);
						}else{
							//CREATE DEBT
							DebtVo debtVo = new DebtVo(debitorUserTrip.getUser(), creditorUserTrip.getUser(),this,remainingOwed);
							debts.add(debtVo);

							//Calculate remainders
							creditorUserTrip.getAccount().setBalance(creditorUserTrip.getAccount().getBalance().subtract(remainingOwed));
							remainingOwed = BigDecimal.ZERO;
							break;
						}
					}
				}
				if(!remainingOwed.equals(BigDecimal.ZERO)){
					throw new Exception("Shouldn't be any remaining debts");
				}
			}
		}
		return debts;
	}

	public List<TotalPerUserVo> totalOwedMap(){
		return this.getTravelers().stream().map(userTripVo -> {
			try {
				return new TotalPerUserVo(userTripVo.getUser(),userTripVo.getAccount().totalShouldHavePaid());
			} catch (ValidationException e) {
				System.out.println("Could not create TotalPerUserVo, cause: "+e.getMessage());

				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
	}

	public List<TotalPerUserVo> totalSpentMap(){
		return this.getTravelers().stream().map(userTripVo -> {
			try {
				return new TotalPerUserVo(userTripVo.getUser(), userTripVo.getAccount().totalPaid());
			} catch (ValidationException e) {
				System.out.println("Could not create TotalPerUserVo, cause: " + e.getMessage());

				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
	}
}
