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
public class TripTest extends AbstractTestNGSpringContextTests {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(TripTest.class);

	@Autowired
	private TripCtrl tripCtrl;

	@Autowired
	private Gson gson;

	@Autowired
	private EntityManagerHolder entityManagerHolder;

	@Autowired
	private I_TripDao tripDao;

	@Autowired
	private I_UserDao userDao;

	@BeforeClass(dependsOnMethods={"springTestContextPrepareTestInstance"})
	private void initialize()throws Exception{
		EntityManager em = null;
		try{
			em = this.getEntityManagerHolder().getEntityManager();

			String [] userNames = new String[]{"axel@axel.com","martin@martin.com","nicolas@nicolas.com"};
			List<UserVo> users = new ArrayList<>();

			for(String username : userNames){
				UserVo user = this.getUserDao().getByUsername(em,username);
				if(user!=null){
					users.add(user);
				}
			}
			for(UserVo user : users){
				List<TripVo> tripVoList = this.getTripDao().usersTrip(em,user);

				for(TripVo tripVo : tripVoList){
					this.getTripDao().delete(em,tripVo);
				}
			}
			//TODO que hago acá?
		}catch(Exception ex){
			this.logger.debug("Trip test could not initalize",ex);
			throw ex;
		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
		}
	}

	@Test(groups = {"trip"},dependsOnGroups = {"user","destination"})
	public void createTripTest()throws Exception{
		EntityManager em=null,testEm = null;
		try{
			testEm = this.getEntityManagerHolder().getTestEntityManager();
			em = this.getEntityManagerHolder().getEntityManager();

			UserVo testUser = this.getUserDao().getByUsername(testEm, "martin@martin.com");
			UserVo user = this.getUserDao().getByUsername(em, "martin@martin.com");

			List<TripVo> tripVoList = this.getTripDao().usersTrip(testEm,testUser);

			Long amountOfTrips,lastAmountOfTrips = this.getTripDao().countAll(em);

			for(TripVo tripVo : tripVoList){
				tripVo.setId(null);
				tripVo.setOwner(user);
				tripVo.setTravelers(new ArrayList<>());
				tripCtrl.getWebContextHolder().setUserVo(user);
//				for(UserTripVo userTripVo : tripVo.getTravelers()){
//					UserVo userTripVoUser = this.getUserDao().getByUsername(em,userTripVo.getUser().getUsername());
//					if(userTripVoUser!=null){
//						userTripVo= new UserTripVo();
//						userTripVo.setUser(userTripVoUser);
//						userTripVo.setTrip(tripVo);
//						userTripVo.setId(null);
//					}else{
//						tripVo.getTravelers().remove(userTripVo);
//					}
//				}
				JsonResponse response = this.getGson().fromJson(this.getTripCtrl().create(this.getGson().toJson(tripVo)), JsonResponse.class);
				if(response.getOk()){
					amountOfTrips = this.getTripDao().countAll(em);
					TripVo persistedTripVo = this.getTripDao().listAll(em).get(amountOfTrips.intValue()-1);
                    assert lastAmountOfTrips.equals(amountOfTrips):"Trip not persisted correctly id:"+tripVo.getId();
                    if(persistedTripVo!=null){
                        assert persistedTripVo.getOwner().equals(tripVo.getOwner()):"Trip's owner wasn't persisted correctly id:"+tripVo.getId();
                        for(int i=0;i<Math.max(tripVo.getTravelers().size(),persistedTripVo.getTravelers().size());i++){
                            assert persistedTripVo.getTravelers().get(i).getUser().equals(tripVo.getTravelers().get(i).getUser()):
                                    "At least one of the trip's participants wasn't persisted correctly id:"+tripVo.getId()+" i:"+i;
                        }
                        lastAmountOfTrips = amountOfTrips;
                        assert persistedTripVo.getCity().getName().equals(tripVo.getCity().getName()):"Trip's owner wasn't persisted correctly, id"+tripVo.getId();
                    }
				}else{
					Boolean ok = tripVo.getOwner()!=null;
					//Trip doesn't need to have participants
					//ok= ok && !tripVo.getTravelers().isEmpty();
					ok = ok && tripVo.getCity()!=null && tripVo.getCity().getCountry()!=null;
					assert !ok:"Trip not persisted and none null fields";
				}
			}
		}catch(Exception ex){
			this.logger.debug("Trip test failed running create test",ex);
			throw ex;
		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
			this.getEntityManagerHolder().closeEntityManager(testEm);
		}
	}

	@Test(groups = {"trip"},dependsOnMethods = "createTripTest",dependsOnGroups = {"user","destination"})
	public void deleteTripTest()throws Exception{
		EntityManager em=null;
		try {
			em = this.getEntityManagerHolder().getEntityManager();
			List<TripVo> tripVoList = this.getTripDao().listAll(em);

			TripVo tripVo = new TripVo();
			tripVo.setId(tripVoList.get(tripVoList.size() - 1).getId());

			JsonResponse response = this.getGson().fromJson(this.getTripCtrl().delete(this.getGson().toJson(tripVo)), JsonResponse.class);
			TripVo persistedTripVo = this.getTripDao().getById(em, tripVo.getId());
			assert (persistedTripVo!=null && response.getOk()):"Couldn't delete trip correctly, id:"+tripVo.getId();

			tripVo = new TripVo();
			response = this.getGson().fromJson(this.getTripCtrl().delete(this.getGson().toJson(tripVo)),JsonResponse.class);
			assert (persistedTripVo==null || !response.getOk()):"Shouldn't have been able to delete and returned ok, id:"+tripVo.getId();
		}catch(Exception ex){
			this.logger.debug("Trip test failed running delete test",ex);
			throw ex;
		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
		}
	}

	@Test(groups = {"trip"},dependsOnMethods = "createTripTest",dependsOnGroups = {"user","destination"})
	public void addTravelerTest()throws Exception{
		EntityManager em=null;
		try {
			em = this.getEntityManagerHolder().getEntityManager();
			List<TripVo> tripVoList = this.getTripDao().listAll(em);

			UserVo userAriel = this.getUserDao().getByUsername(em, "ariel@ariel.com");

			TripVo tripVo = tripVoList.get(tripVoList.size() - 1);
			UserTripVo userTripVo = new UserTripVo();
			userTripVo.setUser(userAriel);
			tripVo.getTravelers().add(userTripVo);

			JsonResponse response = this.getGson().fromJson(this.getTripCtrl().addTraveler(this.getGson().toJson(tripVo)), JsonResponse.class);
			TripVo persistedTripVo = this.getTripDao().getById(em, tripVo.getId());
			assert (persistedTripVo.getTravelers().size()==tripVo.getTravelers().size()
					&&  persistedTripVo.getTravelers().get(persistedTripVo.getTravelers().size()-1).equals(userAriel) && response.getOk()):"Couldn't add traveler to trip, id:"+tripVo.getId();


			UserVo userTest = this.getUserDao().getByUsername(em, "test@test.com");
			userTripVo = new UserTripVo();
			userTripVo.setUser(userTest);
			tripVo.getTravelers().add(userTripVo);
			response = this.getGson().fromJson(this.getTripCtrl().addTraveler(this.getGson().toJson(tripVo)), JsonResponse.class);
		}catch(Exception ex){
			this.logger.debug("Trip test failed running delete test",ex);
			throw ex;
		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
		}
	}

	@Test(groups = {"trip"},dependsOnMethods = {"createTripTest","addTravelerTest"},dependsOnGroups = {"user","destination"})
	public void addExpense()throws Exception{
		EntityManager em=null;
		try {
			em = this.getEntityManagerHolder().getEntityManager();
//			List<TripVo> tripVoList = this.getTripDao().listAll(em);

			UserVo userAriel = this.getUserDao().getByUsername(em, "ariel@ariel.com");
			UserVo userTest = this.getUserDao().getByUsername(em, "test@test.com");

			TripVo tripVo = new TripVo();
			tripVo.addTraveler(userAriel);
			tripVo.addTraveler(userTest);

			BigDecimal amount = new BigDecimal(500);

			List<TravelerProportionVo> travelerProportionVos = new ArrayList<>();
			TravelerProportionVo travelerProportionVo = new TravelerProportionVo();
			travelerProportionVo.setTraveler(userAriel);
			travelerProportionVo.setProportion(new BigDecimal(18));
			travelerProportionVos.add(travelerProportionVo);

			travelerProportionVo = new TravelerProportionVo();
			travelerProportionVo.setTraveler(userTest);
			travelerProportionVo.setProportion(new BigDecimal(38));
			travelerProportionVos.add(travelerProportionVo);

			ExpenseVo expenseVo = tripVo.addExpense(amount,"Gasto de Prueba 1",userAriel, E_ExpenseSplittingForm.EQUAL_SPLITTING,travelerProportionVos);
			assert(expenseVo.getPaymentMovement().getFrom().equals(userAriel.getAccountInTrip(tripVo))):"Payment user wasn't correct";
			assert(expenseVo.getPaymentMovement().getAmount().equals(amount)):"Amount was incorrect";
			assert(expenseVo.getExpenseMovements().size()==travelerProportionVos.size()):"Proportions amount was incorrect";
			for(ExpenseMovementVo expenseMovementVo : expenseVo.getExpenseMovements()){
				assert(expenseMovementVo.getMovement().getAmount().equals(amount.divide(new BigDecimal(expenseVo.getExpenseMovements().size())))):"One of the proportions was incorrect";
			}
			assert(userAriel.getAccountInTrip(tripVo).getBalance().equals(new BigDecimal(250))):"User ariel balance is incorrect";
			assert(userTest.getAccountInTrip(tripVo).getBalance().equals(new BigDecimal(0))):"User test balance is incorrect";

//			UserTripVo userTripVo = new UserTripVo();
//			userTripVo.setUser(userAriel);
//			tripVo.getTravelers().add(userTripVo);
//
//			JsonResponse response = this.getGson().fromJson(this.getTripCtrl().addTraveler(this.getGson().toJson(tripVo)), JsonResponse.class);
//			TripVo persistedTripVo = this.getTripDao().getById(em,tripVo.getId());
//			assert (persistedTripVo.getTravelers().size()==tripVo.getTravelers().size()
//					&&  persistedTripVo.getTravelers().get(persistedTripVo.getTravelers().size()-1).equals(userAriel) && response.getOk()):"Couldn't add traveler to trip, id:"+tripVo.getId();
		}catch(Exception ex){
			this.logger.debug("Trip test failed running add expense test",ex);
			throw ex;
		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
		}
	}

	public I_UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(I_UserDao userDao) {
		this.userDao = userDao;
	}

	public TripCtrl getTripCtrl() {
		return tripCtrl;
	}

	public void setTripCtrl(TripCtrl tripCtrl) {
		this.tripCtrl = tripCtrl;
	}

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}

	public EntityManagerHolder getEntityManagerHolder() {
		return entityManagerHolder;
	}

	public void setEntityManagerHolder(EntityManagerHolder entityManagerHolder) {
		this.entityManagerHolder = entityManagerHolder;
	}

	public I_TripDao getTripDao() {
		return tripDao;
	}

	public void setTripDao(I_TripDao tripDao) {
		this.tripDao = tripDao;
	}
}
