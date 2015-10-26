package org.kairos.tripSplitterClone.tests;

import com.google.gson.Gson;
import org.kairos.tripSplitterClone.controller.TripCtrl;
import org.kairos.tripSplitterClone.dao.EntityManagerHolder;
import org.kairos.tripSplitterClone.dao.trip.I_TripDao;
import org.kairos.tripSplitterClone.dao.user.I_UserDao;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.utils.exception.IncompleteProportionException;
import org.kairos.tripSplitterClone.vo.destination.CityVo;
import org.kairos.tripSplitterClone.vo.destination.CountryVo;
import org.kairos.tripSplitterClone.vo.expense.E_ExpenseSplittingForm;
import org.kairos.tripSplitterClone.vo.expense.ExpenseMovementVo;
import org.kairos.tripSplitterClone.vo.expense.ExpenseVo;
import org.kairos.tripSplitterClone.vo.expense.TravelerProportionVo;
import org.kairos.tripSplitterClone.vo.trip.DebtVo;
import org.kairos.tripSplitterClone.vo.trip.TotalPerUserVo;
import org.kairos.tripSplitterClone.vo.trip.TripVo;
import org.kairos.tripSplitterClone.vo.trip.UserTripVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 */
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/mainContext.xml","classpath:spring/servletContext.xml"})
@TestExecutionListeners({
		ServletTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class
})
public class TripModelTest extends AbstractTestNGSpringContextTests {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(TripModelTest.class);

	private TripVo tripVo;

	private UserVo traveler1 = new UserVo("NuevoViajero1@mail.com", "nuevoviajero1", "Nuevo Viajero 1");
	private UserVo traveler2 = new UserVo("NuevoViajero2@mail.com", "nuevoviajero2", "Nuevo Viajero 2");


	@Test(groups = {"trip"},dependsOnGroups = {"user","destination"})
	public void createTripTest()throws Exception{
		traveler1.setId(900L);
		traveler2.setId(60L);

		CityVo city = new CityVo("Praga",new CountryVo("Checoslovaquia"));

		tripVo = new TripVo(traveler1,city,"Mi primer viaje a Praga - Checoslovaquia");
		tripVo.setId(342L);

		assert(tripVo.getTravelers().get(0).getUser().equals(traveler1)):"El viajero dueño no se agregó como viajero";
		assert(tripVo.getCity().equals(city)):"El destino del viaje no se seteó correctamente";
		assert(tripVo.getTitle().equals("Mi primer viaje a Praga - Checoslovaquia")):"El titulo del viaje no fue correctamente seteado";
		assert(tripVo.getTravelers().size()==1):"Se agregó más de un viajero,o ninguno";
		assert(tripVo.getExpenses().size()==0):"Se agregó una o más expensas";
	}

	@Test(groups = {"trip"},dependsOnMethods = "createTripTest",dependsOnGroups = {"user","destination"})
	public void deleteTripTest()throws Exception{
		tripVo.delete();

		assert(tripVo.getDeleted()):"Trip was not deleted correctly";

		tripVo.setDeleted(Boolean.FALSE);
	}

	@Test(groups = {"trip"},dependsOnMethods = "createTripTest",dependsOnGroups = {"user","destination"})
	public void addTravelerTest()throws Exception {
		this.getTripVo().addTraveler(traveler2);

		this.getTripVo().getTravelers().get(0).setId(234L);
		this.getTripVo().getTravelers().get(0).getAccount().setId(234L);

		UserTripVo addedTraveler = null;

		for (UserTripVo userTripVo : this.getTripVo().getTravelers()) {
			if (userTripVo.getUser().equals(traveler2)) {
				addedTraveler = userTripVo;
			}
		}
		assert(addedTraveler!=null):"New Traveler was not added";
		assert(addedTraveler.getAccount()!=null):"New Traveler was added but account creation failed";
		assert(addedTraveler.getTrip().equals(this.getTripVo())):"New Traveler was added but trip was not setted correctly";
		assert(traveler2.getName().equals(addedTraveler.getUser().getName())):"New Traveler was added but name was incorrect";
		assert(traveler2.getEmail().equals(addedTraveler.getUser().getEmail())):"New Traveler was added but email was incorrect";
		assert(traveler2.getPassword().equals(addedTraveler.getUser().getPassword())):"New Traveler was added but password was incorrect";

		this.getTripVo().addTraveler(traveler1);
		this.getTripVo().getTravelers().get(1).setId(237L);
		this.getTripVo().getTravelers().get(1).getAccount().setId(237L);
	}

	@Test(groups = {"trip"},dependsOnMethods = {"createTripTest","addTravelerTest"},dependsOnGroups = {"user","destination"})
	public void addExpenseTest()throws Exception{
		BigDecimal amount = new BigDecimal(500);

		List<TravelerProportionVo> travelerProportionVos = new ArrayList<>();
		TravelerProportionVo travelerProportionVo1 = new TravelerProportionVo();
		travelerProportionVo1.setTraveler(traveler1);
		travelerProportionVo1.setProportion(new BigDecimal(18));
		travelerProportionVos.add(travelerProportionVo1);

		TravelerProportionVo travelerProportionVo2 = new TravelerProportionVo();
		travelerProportionVo2.setTraveler(traveler2);
		travelerProportionVo2.setProportion(new BigDecimal(38));
		travelerProportionVos.add(travelerProportionVo2);
		ExpenseVo expenseVo = null;
		try{
			expenseVo = tripVo.addExpense(amount,"Primer gasto de prueba",traveler1, E_ExpenseSplittingForm.EQUAL_SPLITTING,travelerProportionVos);
			assert(expenseVo==null):"Shouldn't have created the Expense";
		}catch(IncompleteProportionException ex){
			this.logger.debug("Didn't create expense, as expected");
		}
		travelerProportionVo1.setProportion(new BigDecimal(39));
		travelerProportionVo2.setProportion(new BigDecimal(61));
		expenseVo = tripVo.addExpense(amount,"Primer gasto de prueba - igual",traveler1, E_ExpenseSplittingForm.EQUAL_SPLITTING,travelerProportionVos);

		assert(expenseVo!=null && tripVo.getExpenses().size()==1):"Expense was not added";
		assert(expenseVo.getPaymentMovement().getFrom().equals(traveler1.getAccountInTrip(tripVo))):"Payment user wasn't correct";
		assert(expenseVo.getPaymentMovement().getAmount().equals(amount)):"Amount was incorrect";
		assert(expenseVo.getExpenseMovements().size()==travelerProportionVos.size()):"Proportions amount was incorrect";
		for(ExpenseMovementVo expenseMovementVo : expenseVo.getExpenseMovements()){
			assert(expenseMovementVo.getMovement().getAmount().equals(amount.divide(new BigDecimal(expenseVo.getExpenseMovements().size())))):"One of the proportions was incorrect";
		}
		assert(traveler1.getAccountInTrip(tripVo).getBalance().equals(new BigDecimal(250))):"User traveler1 balance is incorrect";
		assert(traveler2.getAccountInTrip(tripVo).getBalance().equals(new BigDecimal(0))):"User traveler2 balance is incorrect";

		expenseVo = tripVo.addExpense(amount,"Primer gasto de prueba - proporcional",traveler2, E_ExpenseSplittingForm.PROPORTIONAL_SPLITTING,travelerProportionVos);

		assert(expenseVo!=null && tripVo.getExpenses().size()==2):"Expense was not added";
		assert(expenseVo.getPaymentMovement().getFrom().equals(traveler2.getAccountInTrip(tripVo))):"Payment user wasn't correct";
		assert(expenseVo.getPaymentMovement().getAmount().equals(amount)):"Amount was incorrect";
		assert(expenseVo.getExpenseMovements().size()==travelerProportionVos.size()):"Proportions amount was incorrect";
		for(int i=0;i<expenseVo.getExpenseMovements().size();i++){
			ExpenseMovementVo expenseMovementVo = expenseVo.getExpenseMovements().get(i);
			assert(expenseMovementVo.getMovement().getAmount().equals(amount.multiply(travelerProportionVos.get(i).getProportion()).divide(new BigDecimal(100)))):"One of the proportions was incorrect";
		}
		assert(traveler2.getAccountInTrip(tripVo).getBalance().equals(new BigDecimal(310))):"User traveler2 balance is incorrect";
		assert(traveler1.getAccountInTrip(tripVo).getBalance().equals(new BigDecimal(250))):"User traveler1 balance is incorrect";

	}

	@Test(groups = {"trip"},dependsOnMethods = {"createTripTest","addExpenseTest"},dependsOnGroups = {"user","destination"})
	public void totalOwedTest()throws Exception{
		List<TotalPerUserVo> totalOwedMap = tripVo.totalOwedMap();

		assert(totalOwedMap.size()==2):"Amount of total Owed Map was incorrect. It was "+totalOwedMap.size()+" should have been 2";
		TotalPerUserVo totalPerUserVo = totalOwedMap.get(0);
		assert(totalPerUserVo.getUser().getId().equals(900L)):"Total owed map item user was incorrect, with id:"+totalPerUserVo.getUser().getId()+" should have been 900";
		assert(totalPerUserVo.getAmount().equals(new BigDecimal(695))):"Total owed map item amount was incorrect, with amount:"+totalPerUserVo.getAmount()+" should have been 695";
		totalPerUserVo = totalOwedMap.get(1);
		assert(totalPerUserVo.getUser().getId().equals(60L)):"Total owed map item user was incorrect, with id:"+totalPerUserVo.getUser().getId()+" should have been 60";
		assert(totalPerUserVo.getAmount().equals(new BigDecimal(805))):"Total owed map item amount was incorrect, with amount:"+totalPerUserVo.getAmount()+" should have been 805";
	}

	@Test(groups = {"trip"},dependsOnMethods = {"createTripTest","addExpenseTest"},dependsOnGroups = {"user","destination"})
	public void totalSpentTest()throws Exception{
		List<TotalPerUserVo> totalSpentMap = tripVo.totalSpentMap();

		assert(totalSpentMap.size()==2):"Amount of total Spent Map was incorrect. It was "+totalSpentMap.size()+" should have been 2";
		TotalPerUserVo totalPerUserVo = totalSpentMap.get(0);
		assert(totalPerUserVo.getUser().getId().equals(900L)):"Total spent map item user was incorrect, with id:"+totalPerUserVo.getUser().getId()+" should have been 900";
		assert(totalPerUserVo.getAmount().equals(new BigDecimal(1000))):"Total spent map item amount was incorrect, with amount:"+totalPerUserVo.getAmount()+" should have been 1000";
		totalPerUserVo = totalSpentMap.get(1);
		assert(totalPerUserVo.getUser().getId().equals(60L)):"Total spent map item user was incorrect, with id:"+totalPerUserVo.getUser().getId()+" should have been 60";
		assert(totalPerUserVo.getAmount().equals(new BigDecimal(500))):"Total spent map item amount was incorrect, with amount:"+totalPerUserVo.getAmount()+" should have been 500";
	}
	@Test(groups = {"trip"},dependsOnMethods = {"createTripTest","addExpenseTest"},dependsOnGroups = {"user","destination"})
	public void debtsTest()throws Exception{
		List<DebtVo> debts = tripVo.calculateDebts();

		assert(debts.size()==1):"Amount of debts was incorrect. Amount of debts was "+debts.size()+" should have been 1";
		DebtVo debtVo = debts.get(0);
		assert(debtVo.getDebtor().getId().equals(60L)):"Debt debtor was incorrect, with Id:"+debtVo.getDebtor().getId()+" should have been 60";
		assert(debtVo.getCreditor().getId().equals(900L)):"Debt creditor was incorrect, with Id:"+debtVo.getCreditor().getId()+" should have been 900";
		assert(debtVo.getTrip().getId().equals(342L)):"Debt trip was incorrect, with Id:"+debtVo.getTrip().getId()+" should have been 342";
		assert(debtVo.getAmount().equals(new BigDecimal(305))):"Debt amount was incorrect, with amount:"+debtVo.getAmount()+" should have been 305";
	}

	public TripVo getTripVo() {
		return tripVo;
	}

	public void setTripVo(TripVo tripVo) {
		this.tripVo = tripVo;
	}
}