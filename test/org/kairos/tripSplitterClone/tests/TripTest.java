package org.kairos.tripSplitterClone.tests;

import com.google.gson.Gson;
import org.kairos.tripSplitterClone.controller.TripCtrl;
import org.kairos.tripSplitterClone.dao.EntityManagerHolder;
import org.kairos.tripSplitterClone.dao.trip.I_TripDao;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.vo.trip.TripVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 */
//@ContextConfiguration(locations = {"classpath:spring/mainContext.xml"})
public class TripTest {//extends AbstractTestNGSpringContextTests {

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

	private HttpSession session;

	@BeforeTest
	private void initialize() {
		EntityManager em = null;
		try{
			em = this.getEntityManagerHolder().getEntityManager();

			UserVo sessionUser = (UserVo)this.getSession().getAttribute("loggedUser");

			List<TripVo> tripVoList = this.getTripDao().usersTrip(em,sessionUser);

			for(TripVo tripVo : tripVoList){
				this.getTripDao().delete(em,tripVo);
			}
			//TODO que hago acá?

		}catch(Exception ex){
			this.logger.debug("Trip test could not initalize",ex);
		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
		}
	}

	@Test(groups = {"trip"})
	public void createTripTest(){
		EntityManager em=null,testEm = null;
		try{
			testEm = this.getEntityManagerHolder().getTestEntityManager();
			em = this.getEntityManagerHolder().getEntityManager();

			UserVo sessionUser = (UserVo)this.getSession().getAttribute("loggedUser");

			List<TripVo> tripVoList = this.getTripDao().usersTrip(testEm,sessionUser);

			Long amountOfTrips,lastAmountOfTrips = this.getTripDao().countAll(em);

			for(TripVo tripVo : tripVoList){
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
					ok = ok && tripVo.getCity()!=null && tripVo.getCountry()!=null;
					assert ok:"Trip not persisted and none null fields";
				}
			}
		}catch(Exception ex){
			this.logger.debug("Trip test failed running create test",ex);
		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
			this.getEntityManagerHolder().closeEntityManager(testEm);
		}
	}

	@Test(groups = {"trip"})
	public void deleteTripTest(){
		EntityManager em=null;
		try {
			em = this.getEntityManagerHolder().getEntityManager();
			List<TripVo> tripVoList = this.getTripDao().listAll(em);

			TripVo tripVo = new TripVo();
			tripVo.setId(tripVoList.get(tripVoList.size()).getId());

			JsonResponse response = this.getGson().fromJson(this.getTripCtrl().delete(this.getGson().toJson(tripVo)), JsonResponse.class);
			TripVo persistedTripVo = this.getTripDao().getById(em,tripVo.getId());
			assert (persistedTripVo!=null && response.getOk()):"Couldn't delete trip correctly, id:"+tripVo.getId();

			tripVo = new TripVo();
			response = this.getGson().fromJson(this.getTripCtrl().delete(this.getGson().toJson(tripVo)),JsonResponse.class);
			assert (persistedTripVo==null || !response.getOk()):"Shouldn't have been able to delete and returned ok, id:"+tripVo.getId();
		}catch(Exception ex){
			this.logger.debug("User test failed running delete test",ex);
		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
		}
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
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
