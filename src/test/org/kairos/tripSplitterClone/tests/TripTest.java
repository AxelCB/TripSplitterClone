package org.kairos.tripSplitterClone.tests;

import com.google.gson.Gson;
import org.kairos.tripSplitterClone.controller.TripCtrl;
import org.kairos.tripSplitterClone.dao.EntityManagerHolder;
import org.kairos.tripSplitterClone.dao.trip.I_TripDao;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.vo.trip.TripVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TripTest {

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
				JsonResponse response = this.getGson().fromJson(tripCtrl.create(this.getGson().toJson(tripVo)), JsonResponse.class);
				if(response.getOk()){
					amountOfTrips = this.getTripDao().countAll(em);
					TripVo persistedTripVo = this.getTripDao().listAll(em).get(amountOfTrips.intValue()-1);
					assert lastAmountOfTrips.equals(amountOfTrips):"Trip not persisted correctly id:"+tripVo.getId();
					assert persistedTripVo.getOwner().equals(tripVo.getOwner()):"Trip's owner was persisted correctly id:"+tripVo.getId();
					for(int i=0;i<Math.max(tripVo.getParticipants().size(),persistedTripVo.getParticipants().size());i++){
						assert persistedTripVo.getParticipants().get(i).getUser().equals(tripVo.getParticipants().get(i).getUser()):
								"At least one of the trip's participants wasn't persisted correctly id:"+tripVo.getId()+" i:"+i;
					}
					//TODO asserts check destination
				}else{
				//TODO asserts if not persisted
				}
			}

		}catch(Exception ex){

		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
		}
	}

	@Test(groups = {"trip"})
	public void deleteTripTest(){

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
