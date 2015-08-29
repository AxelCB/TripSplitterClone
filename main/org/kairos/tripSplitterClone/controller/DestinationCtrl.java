package org.kairos.tripSplitterClone.controller;

import com.google.gson.Gson;
import org.kairos.tripSplitterClone.dao.EntityManagerHolder;
import org.kairos.tripSplitterClone.fx.I_FxFactory;
import org.kairos.tripSplitterClone.fx.destination.Fx_CreateCity;
import org.kairos.tripSplitterClone.fx.destination.Fx_CreateCountry;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.vo.destination.CityVo;
import org.kairos.tripSplitterClone.vo.destination.CountryVo;
import org.kairos.tripSplitterClone.web.WebContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;

/**
 * Created on 8/23/15 by
 *
 * @author AxelCollardBovy.
 */
@RequestMapping(value = "/destination", produces = "text/json;charset=utf-8", method = RequestMethod.POST)
public class DestinationCtrl {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(DestinationCtrl.class);

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
	 * Creates a city.
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/createCity.json")
	public String createCity(@RequestBody String data){
		this.logger.debug("calling DestinationCtrl.createCity()");
		EntityManager em = this.getEntityManagerHolder().getEntityManager();
		JsonResponse jsonResponse = null;

		try {
			CityVo userVo = this.getGson().fromJson(data,CityVo.class);

			Fx_CreateCity fx = this.getFxFactory().getNewFxInstance(Fx_CreateCity.class);
			fx.setVo(userVo);
			fx.setEm(em);
			this.logger.debug("executing Fx_CreateCity");
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
	 * Creates a country.
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/createCountry.json")
	public String createCountry(@RequestBody String data){
		this.logger.debug("calling DestinationCtrl.createCountry()");
		EntityManager em = this.getEntityManagerHolder().getEntityManager();
		JsonResponse jsonResponse = null;

		try {
			CountryVo userVo = this.getGson().fromJson(data,CountryVo.class);

			Fx_CreateCountry fx = this.getFxFactory().getNewFxInstance(Fx_CreateCountry.class);
			fx.setVo(userVo);
			fx.setEm(em);
			this.logger.debug("executing Fx_CreateCountry");
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
	 * Lists all destinations.
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listDestinations.json")
	public String listDestinations(@RequestBody String data){
		//TODO: implement this
		return null;
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
}
