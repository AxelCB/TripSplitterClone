package org.kairos.tripSplitterClone.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.kairos.tripSplitterClone.dao.EntityManagerHolder;
import org.kairos.tripSplitterClone.dao.trip.I_TripDao;
import org.kairos.tripSplitterClone.dao.user.I_UserDao;
import org.kairos.tripSplitterClone.fx.I_FxFactory;
import org.kairos.tripSplitterClone.fx.trip.Fx_CreateTrip;
import org.kairos.tripSplitterClone.fx.trip.Fx_DeleteTrip;
import org.kairos.tripSplitterClone.fx.trip.Fx_ModifyTrip;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.utils.exception.IncompleteProportionException;
import org.kairos.tripSplitterClone.vo.account.AccountVo;
import org.kairos.tripSplitterClone.vo.expense.E_ExpenseSplittingForm;
import org.kairos.tripSplitterClone.vo.expense.TravelerProportionVo;
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
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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
	 * Trip Dao.
	 */
	@Autowired
	private I_UserDao userDao;

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
			fx.setVo(new TripVo(tripVo.getOwner(),tripVo.getCity(),tripVo.getTitle()));
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
			JsonObject jsonObject = this.getGson().fromJson(data, JsonObject.class);

			UserVo newTraveler = this.getGson().fromJson(jsonObject.get("newTraveler"),UserVo.class);

			newTraveler = this.getUserDao().getByUsername(em,newTraveler.getUsername());

			TripVo tripVo = this.getGson().fromJson(jsonObject.get("trip"), TripVo.class);
			tripVo = this.getTripDao().getById(em,tripVo.getId());

			tripVo.addTraveler(newTraveler);

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

			userVo = this.getUserDao().getByUsername(em,userVo.getUsername());

			List<TripVo> tripsVoList = userVo.listTrips();

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
	 * Search trip by id
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/search.json")
	public String search(@RequestBody String data){
		this.logger.debug("calling TripCtrl.search()");
		EntityManager em = this.getEntityManagerHolder().getEntityManager();
		JsonResponse jsonResponse = null;

		try {
			TripVo tripVo = this.getGson().fromJson(data, TripVo.class);

			tripVo = this.getTripDao().getById(em,tripVo.getId());

			jsonResponse = JsonResponse.ok(this.getGson().toJson(tripVo));
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
			tripVo.delete();

			Fx_ModifyTrip fx = this.getFxFactory().getNewFxInstance(Fx_ModifyTrip.class);
			fx.setVo(tripVo);
			fx.setEm(em);
			this.logger.debug("executing Fx_ModifyTrip");
			jsonResponse = fx.execute();
		} catch (Exception e) {
			this.logger.debug("unexpected error", e);

			jsonResponse = this.getWebContextHolder().unexpectedErrorResponse("0");
		} finally {
			this.getEntityManagerHolder().closeEntityManager(em);
		}

		return this.getGson().toJson(jsonResponse);
	}

	/**
	 * Adds an expense to a trip.
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addExpense.json")
	public String addExpense(@RequestBody String data){
		this.logger.debug("calling TripCtrl.addExpense()");
		EntityManager em = this.getEntityManagerHolder().getEntityManager();
		JsonResponse jsonResponse = null;

		try {
			JsonObject jsonObject = this.getGson().fromJson(data, JsonObject.class);

			//Gets and refreshes user who paid the expense from the request
			UserVo payingUser = this.getGson().fromJson(jsonObject.get("payingUser"),UserVo.class);
			payingUser = this.getUserDao().getByUsername(em, payingUser.getUsername());

			//Gets and refreshes the trip from the request
			TripVo tripVo = this.getGson().fromJson(jsonObject.get("trip"), TripVo.class);
			tripVo = this.getTripDao().getById(em, tripVo.getId());

			//Gets the amount of the expense and the splitting form from the request
			BigDecimal amount = jsonObject.get("amount").getAsBigDecimal();
			String description = jsonObject.get("description").getAsString();
			E_ExpenseSplittingForm expenseSplittingForm = E_ExpenseSplittingForm.valueOf(jsonObject.get("splittingForm").getAsString());

			//Gets the list of travelers and proportions from the request (proportion's values only used for non-equal splitting)
			Type type = new TypeToken<List<TravelerProportionVo>>() {}.getType();
			List<TravelerProportionVo> travelerProportionVos = this.getGson().fromJson(jsonObject.get("travelerProportions"), type);
			//Refresh users
			for(TravelerProportionVo travelerProportionVo : travelerProportionVos){
				travelerProportionVo.setTraveler(this.getUserDao().getById(em,travelerProportionVo.getTraveler().getId()));
			}

			//Adds the new expense with all the previous values
			tripVo.addExpense(amount,description,payingUser,expenseSplittingForm,travelerProportionVos);

			//Persists all changes
			Fx_ModifyTrip fx = this.getFxFactory().getNewFxInstance(Fx_ModifyTrip.class);
			fx.setVo(tripVo);
			fx.setEm(em);
			this.logger.debug("executing Fx_CreateTrip");
			jsonResponse = fx.execute();
			if(jsonResponse.getOk()){
				jsonResponse.setMessages(new ArrayList<>());
				jsonResponse.getMessages().add(this.getWebContextHolder().getMessage("trip.addExpense.ok"));
			}
		} catch (IncompleteProportionException ipe) {
			this.logger.debug(ipe.getMessage());

			jsonResponse = JsonResponse.error("",this.getWebContextHolder().getMessage("trip.addExpense.incompletTotalProportion"));
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
	@RequestMapping(value = "/listTripExpenses.json")
	public String listTripExpenses(@RequestBody String data){
		this.logger.debug("calling TripCtrl.listTripExpenses()");
		EntityManager em = this.getEntityManagerHolder().getEntityManager();
		JsonResponse jsonResponse = null;

		try {
			Long tripId = Long.parseLong(data);
			TripVo tripVo = this.getTripDao().getById(em,tripId);

			jsonResponse = JsonResponse.ok(this.getGson().toJson(tripVo.getExpenses()));
		} catch (Exception e) {
			this.logger.debug("unexpected error", e);

			jsonResponse = this.getWebContextHolder().unexpectedErrorResponse();
		}finally {
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
	@RequestMapping(value = "/listSplittingForms.json")
	public String listSplittingForms(){
		this.logger.debug("calling TripCtrl.listSplittingForms()");
		JsonResponse jsonResponse = null;

		try {
			jsonResponse = JsonResponse.ok(this.getGson().toJson(E_ExpenseSplittingForm.values()));
		} catch (Exception e) {
			this.logger.debug("unexpected error", e);

			jsonResponse = this.getWebContextHolder().unexpectedErrorResponse();
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

	public I_UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(I_UserDao userDao) {
		this.userDao = userDao;
	}
}
