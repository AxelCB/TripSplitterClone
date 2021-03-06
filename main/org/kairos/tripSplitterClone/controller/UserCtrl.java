package org.kairos.tripSplitterClone.controller;

import com.google.gson.Gson;
import org.kairos.tripSplitterClone.dao.EntityManagerHolder;
import org.kairos.tripSplitterClone.dao.trip.I_TripDao;
import org.kairos.tripSplitterClone.dao.user.I_UserDao;
import org.kairos.tripSplitterClone.fx.I_FxFactory;
import org.kairos.tripSplitterClone.fx.user.Fx_Login;
import org.kairos.tripSplitterClone.fx.user.Fx_Logout;
import org.kairos.tripSplitterClone.fx.user.Fx_Register;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.vo.destination.CityVo;
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
@RequestMapping(value = "/user", produces = "text/json;charset=utf-8", method = RequestMethod.POST)
public class UserCtrl {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(UserCtrl.class);

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
	 * User Dao.
	 */
	@Autowired
	private I_UserDao userDao;

	/**
	 * Registers a user.
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/register.json")
	public String register(@RequestBody String data){
		this.logger.debug("calling UserCtrl.register()");
		EntityManager em = this.getEntityManagerHolder().getEntityManager();
		JsonResponse jsonResponse = null;

		try {
			UserVo userVo = this.getGson().fromJson(data,UserVo.class);

			Fx_Register fx = this.getFxFactory().getNewFxInstance(Fx_Register.class);
			fx.setVo(userVo);
			fx.setEm(em);
			this.logger.debug("executing Fx_Register");
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
	 * Login a user.
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/login.json")
	public String login(@RequestBody String data){
		this.logger.debug("calling UserCtrl.login()");
		EntityManager em = this.getEntityManagerHolder().getEntityManager();
		JsonResponse jsonResponse = null;

		try {
			UserVo userVo = this.getGson().fromJson(data,UserVo.class);

			Fx_Login fx = this.getFxFactory().getNewFxInstance(Fx_Login.class);
			fx.setVo(userVo);
			fx.setEm(em);
			this.logger.debug("executing Fx_Login");
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
	 * Logout a user.
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/logout.json")
	public String logout(@RequestBody String data){
		this.logger.debug("calling UserCtrl.logout()");
		EntityManager em = this.getEntityManagerHolder().getEntityManager();
		JsonResponse jsonResponse = null;

		try {
			UserVo userVo = this.getGson().fromJson(data,UserVo.class);

			Fx_Logout fx = this.getFxFactory().getNewFxInstance(Fx_Logout.class);
			fx.setVo(userVo);
			fx.setEm(em);
			this.logger.debug("executing Fx_Logout");
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
	 * Lists all users.
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listAll.json")
	public String listCities(@RequestBody String data){
		this.logger.debug("calling UserCtrl.listAll()");
		EntityManager em = this.getEntityManagerHolder().getEntityManager();
		JsonResponse jsonResponse = null;
		try {
			List<UserVo> userVoList = this.getUserDao().listAll(em);

			jsonResponse = JsonResponse.ok(this.getGson().toJson(userVoList));
		} catch (Exception e) {
			this.logger.debug("unexpected error", e);

			jsonResponse = this.getWebContextHolder().unexpectedErrorResponse();
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

	public I_UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(I_UserDao userDao) {
		this.userDao = userDao;
	}
}
