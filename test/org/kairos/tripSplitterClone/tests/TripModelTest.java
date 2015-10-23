package org.kairos.tripSplitterClone.tests;

import com.google.gson.Gson;
import org.kairos.tripSplitterClone.controller.TripCtrl;
import org.kairos.tripSplitterClone.dao.EntityManagerHolder;
import org.kairos.tripSplitterClone.dao.trip.I_TripDao;
import org.kairos.tripSplitterClone.dao.user.I_UserDao;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.vo.expense.E_ExpenseSplittingForm;
import org.kairos.tripSplitterClone.vo.expense.ExpenseMovementVo;
import org.kairos.tripSplitterClone.vo.expense.ExpenseVo;
import org.kairos.tripSplitterClone.vo.expense.TravelerProportionVo;
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


	@Test(groups = {"trip"},dependsOnGroups = {"user","destination"})
	public void createTripTest()throws Exception{

	}

	@Test(groups = {"trip"},dependsOnMethods = "createTripTest",dependsOnGroups = {"user","destination"})
	public void deleteTripTest()throws Exception{

	}

	@Test(groups = {"trip"},dependsOnMethods = "createTripTest",dependsOnGroups = {"user","destination"})
	public void addTravelerTest()throws Exception {
		UserVo userVo = new UserVo("NuevoViajero1@mail.com", "nuevoviajero1", "Nuevo Viajero 1");
		userVo.setId(900L);
		this.getTripVo().addTraveler(userVo);

		UserTripVo addedTraveler = null;

		for (UserTripVo userTripVo : this.getTripVo().getTravelers()) {
			if (userTripVo.getUser().equals(userVo)) {
				addedTraveler = userTripVo;
			}
		}
		assert(addedTraveler!=null):"New Traveler was not added";
		assert(addedTraveler.getAccount()!=null):"New Traveler was added but account creation failed";
		assert(addedTraveler.getTrip().equals(this.getTripVo())):"New Traveler was added but trip was not setted correctly";
		assert(userVo.getName().equals(addedTraveler.getUser().getName())):"New Traveler was added but name was incorrect";
		assert(userVo.getEmail().equals(addedTraveler.getUser().getEmail())):"New Traveler was added but email was incorrect";
		assert(userVo.getPassword().equals(addedTraveler.getUser().getPassword())):"New Traveler was added but password was incorrect";
	}

	@Test(groups = {"trip"},dependsOnMethods = {"createTripTest","addTravelerTest"},dependsOnGroups = {"user","destination"})
	public void addExpense()throws Exception{
//		EntityManager em=null;
//		try {
////			em = this.getEntityManagerHolder().getEntityManager();
////			List<TripVo> tripVoList = this.getTripDao().listAll(em);
//
//			UserVo userAriel = this.getUserDao().getByUsername(em, "ariel@ariel.com");
//			UserVo userTest = this.getUserDao().getByUsername(em, "test@test.com");
//
//			TripVo tripVo = new TripVo();
//			tripVo.addTraveler(userAriel);
//			tripVo.addTraveler(userTest);
//
//			BigDecimal amount = new BigDecimal(500);
//
//			List<TravelerProportionVo> travelerProportionVos = new ArrayList<>();
//			TravelerProportionVo travelerProportionVo = new TravelerProportionVo();
//			travelerProportionVo.setTraveler(userAriel);
//			travelerProportionVo.setProportion(new BigDecimal(18));
//			travelerProportionVos.add(travelerProportionVo);
//
//			travelerProportionVo = new TravelerProportionVo();
//			travelerProportionVo.setTraveler(userTest);
//			travelerProportionVo.setProportion(new BigDecimal(38));
//			travelerProportionVos.add(travelerProportionVo);
//
//			ExpenseVo expenseVo = tripVo.addExpense(amount,userAriel, E_ExpenseSplittingForm.EQUAL_SPLITTING,travelerProportionVos);
//			assert(expenseVo.getPaymentMovement().getFrom().equals(userAriel.getAccountInTrip(tripVo))):"Payment user wasn't correct";
//			assert(expenseVo.getPaymentMovement().getAmount().equals(amount)):"Amount was incorrect";
//			assert(expenseVo.getExpenseMovements().size()==travelerProportionVos.size()):"Proportions amount was incorrect";
//			for(ExpenseMovementVo expenseMovementVo : expenseVo.getExpenseMovements()){
//				assert(expenseMovementVo.getMovement().getAmount().equals(amount.divide(new BigDecimal(expenseVo.getExpenseMovements().size())))):"One of the proportions was incorrect";
//			}
//			assert(userAriel.getAccountInTrip(tripVo).getBalance().equals(new BigDecimal(250))):"User ariel balance is incorrect";
//			assert(userTest.getAccountInTrip(tripVo).getBalance().equals(new BigDecimal(0))):"User test balance is incorrect";
//
////			UserTripVo userTripVo = new UserTripVo();
////			userTripVo.setUser(userAriel);
////			tripVo.getTravelers().add(userTripVo);
////
////			JsonResponse response = this.getGson().fromJson(this.getTripCtrl().addTraveler(this.getGson().toJson(tripVo)), JsonResponse.class);
////			TripVo persistedTripVo = this.getTripDao().getById(em,tripVo.getId());
////			assert (persistedTripVo.getTravelers().size()==tripVo.getTravelers().size()
////					&&  persistedTripVo.getTravelers().get(persistedTripVo.getTravelers().size()-1).equals(userAriel) && response.getOk()):"Couldn't add traveler to trip, id:"+tripVo.getId();
//		}catch(Exception ex){
//			this.logger.debug("Trip test failed running add expense test",ex);
//			throw ex;
//		}finally{
//			this.getEntityManagerHolder().closeEntityManager(em);
//		}
	}

	public TripVo getTripVo() {
		return tripVo;
	}

	public void setTripVo(TripVo tripVo) {
		this.tripVo = tripVo;
	}
}
