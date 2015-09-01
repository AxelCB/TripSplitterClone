package org.kairos.tripSplitterClone.controller;

import com.google.gson.Gson;
import org.kairos.tripSplitterClone.dao.EntityManagerHolder;
import org.kairos.tripSplitterClone.dao.trip.I_TripDao;
import org.kairos.tripSplitterClone.fx.I_FxFactory;
import org.kairos.tripSplitterClone.fx.trip.Fx_CreateTrip;
import org.kairos.tripSplitterClone.fx.trip.Fx_DeleteTrip;
import org.kairos.tripSplitterClone.fx.trip.Fx_ModifyTrip;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.vo.trip.TripVo;
import org.kairos.tripSplitterClone.vo.trip.UserTripVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.kairos.tripSplitterClone.web.WebContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 */
@RequestMapping(value = "/trip", produces = "text/json;charset=utf-8", method = RequestMethod.POST)
public class TripCtrl {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(TripCtrl.class);

	/**
	 * Gson Holder
	 */
	@Autowired
	private Gson gson;

	/**
	 * FX Factory.
	 */
	@Autowired
	private I_FxFactory fxFactory;

	/**
	 * Entity Manager Holder
	 */
	@Autowired
	private EntityManagerHolder entityManagerHolder;

	/**
	 * Web Context Holder.
	 */
	@Autowired
	private WebContextHolder webContextHolder;

	/**
	 * Trip Dao.
	 */
	@Autowired
	private I_TripDao tripDao;

	/**
	 * Creates a trip.
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/create.json")
	public String create(@RequestBody String data){
		this.logger.debug("calling TripCtrl.create()");
		EntityManager em = this.getEntityManagerHolder().getEntityManager();
		JsonResponse jsonResponse = null;

		try {
			TripVo tripVo = this.getGson().fromJson(data,TripVo.class);

			Fx_CreateTrip fx = this.getFxFactory().getNewFxInstance(Fx_CreateTrip.class);
			fx.setOwner(this.getWebContextHolder().getUserVo());
			fx.setVo(tripVo);
			fx.setEm(em);
			this.logger.debug("executing Fx_CreateTrip");
			jsonResponse = fx.execute();
		} catch (Exception e) {
			this.logger.debug("unexpected error", e);

			jsonResponse = this.getWebContextHolder().unexpectedErrorResponse();
		} finally {
			this.getEntityManagerHolder().closeEntityManager(em);
		}

		return this.getGson().toJson(jsonResponse);
	}

	/**
	 * Adds a participant in a trip.
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addTraveler.json")
	public String addTraveler(@RequestBody String data){
		this.logger.debug("calling TripCtrl.addTraveler()");
		EntityManager em = this.getEntityManagerHolder().getEntityManager();
		JsonResponse jsonResponse = null;

		try {
			TripVo tripVo = this.getGson().fromJson(data, TripVo.class);
			for(UserTripVo userTripVo : tripVo.getTravelers()){
				userTripVo.setTrip(tripVo);
			}

			Fx_ModifyTrip fx = this.getFxFactory().getNewFxInstance(Fx_ModifyTrip.class);
			fx.setVo(tripVo);
			fx.setEm(em);
			this.logger.debug("executing Fx_CreateTrip");
			jsonResponse = fx.execute();
		} catch (Exception e) {
			this.logger.debug("unexpected error", e);

			jsonResponse = this.getWebContextHolder().unexpectedErrorResponse();
		} finally {
			this.getEntityManagerHolder().closeEntityManager(em);
		}

		return this.getGson().toJson(jsonResponse);
	}

	/**
	 * Lists user's trips
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list.json")
	public String listUsersTrips(){
		this.logger.debug("calling TripCtrl.listUsersTrips()");
		EntityManager em = this.getEntityManagerHolder().getEntityManager();
		JsonResponse jsonResponse = null;

		try {
			UserVo userVo = (UserVo)this.getWebContextHolder().getSession().getAttribute(this.getWebContextHolder().getToken());

			List<TripVo> tripsVoList = this.getTripDao().usersTrip(em, userVo);

			jsonResponse = JsonResponse.ok(this.getGson().toJson(tripsVoList));
		} catch (Exception e) {
			this.logger.debug("unexpected error", e);

			jsonResponse = this.getWebContextHolder().unexpectedErrorResponse();
		} finally {
			this.getEntityManagerHolder().closeEntityManager(em);
		}

		return this.getGson().toJson(jsonResponse);
	}

	/**
	 * Delets a trip.
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete.json")
	public String delete(@RequestBody String data){
		this.logger.debug("calling TripCtrl.delete()");
		EntityManager em = this.getEntityManagerHolder().getEntityManager();
		JsonResponse jsonResponse = null;

		try {
			TripVo tripVo = this.getGson().fromJson(data, TripVo.class);

			Fx_DeleteTrip fx = this.getFxFactory().getNewFxInstance(Fx_DeleteTrip.class);

			fx.setVo(tripVo);
			fx.setEm(em);
			this.logger.debug("executing Fx_DeleteTrip");
			jsonResponse = fx.execute();
		} catch (Exception e) {
			this.logger.debug("unexpected error", e);

			jsonResponse = this.getWebContextHolder().unexpectedErrorResponse("0");
		} finally {
			this.getEntityManagerHolder().closeEntityManager(em);
		}

		return this.getGson().toJson(jsonResponse);
	}

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}

	public I_FxFactory getFxFactory() {
		return fxFactory;
	}

	public void setFxFactory(I_FxFactory fxFactory) {
		this.fxFactory = fxFactory;
	}

	public EntityManagerHolder getEntityManagerHolder() {
		return entityManagerHolder;
	}

	public void setEntityManagerHolder(EntityManagerHolder entityManagerHolder) {
		this.entityManagerHolder = entityManagerHolder;
	}

	public WebContextHolder getWebContextHolder() {
		return webContextHolder;
	}

	public void setWebContextHolder(WebContextHolder webContextHolder) {
		this.webContextHolder = webContextHolder;
	}

	public I_TripDao getTripDao() {
		return tripDao;
	}

	public void setTripDao(I_TripDao tripDao) {
		this.tripDao = tripDao;
	}
}
