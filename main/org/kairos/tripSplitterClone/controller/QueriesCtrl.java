package org.kairos.tripSplitterClone.controller;

import com.google.gson.Gson;
import org.kairos.tripSplitterClone.dao.EntityManagerHolder;
import org.kairos.tripSplitterClone.dao.trip.I_TripDao;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.tests.*;
import org.kairos.tripSplitterClone.vo.trip.DebtVo;
import org.kairos.tripSplitterClone.vo.trip.TotalPerUserVo;
import org.kairos.tripSplitterClone.vo.trip.TripVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.kairos.tripSplitterClone.web.WebContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created on 10/18/15 by
 *
 * @author AxelCollardBovy.
 */
@RequestMapping(value = "/queries", produces = "text/json;charset=utf-8", method = RequestMethod.POST)
public class QueriesCtrl {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(QueriesCtrl.class);

	/**
	 * Gson
	 */
	@Autowired
	private Gson gson;

	/**
	 * Trip dao
	 */
	@Autowired
	private I_TripDao tripDao;

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
	 * Lists all debts.
	 *
	 * @param data
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/debts.json")
	public String debts(@RequestBody String data){
		EntityManager em = null;
		JsonResponse jsonResponse;
		try{
			em = this.getEntityManagerHolder().getEntityManager();

			TripVo tripVo = this.getGson().fromJson(data,TripVo.class);
			tripVo = this.getTripDao().getById(em,tripVo.getId());
			List<DebtVo> debts = tripVo.calculateDebts();

			jsonResponse = JsonResponse.ok(this.getGson().toJson(debts));
		}catch(Exception e){
			this.logger.debug("unexpected error", e);

			jsonResponse = this.getWebContextHolder().unexpectedErrorResponse();
		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
		}
		return this.getGson().toJson(jsonResponse);
	}

	/**
	 * Lists all total owed.
	 *
	 * @param data
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/totalOwed.json")
	public String totalOwed(@RequestBody String data){
		EntityManager em = null;
		JsonResponse jsonResponse;
		try{
			em = this.getEntityManagerHolder().getEntityManager();

			TripVo tripVo = this.getGson().fromJson(data,TripVo.class);
			tripVo = this.getTripDao().getById(em,tripVo.getId());
			List<TotalPerUserVo> totalOwedMap = tripVo.totalOwedMap();

			jsonResponse = JsonResponse.ok(this.getGson().toJson(totalOwedMap));
		}catch(Exception e){
			this.logger.debug("unexpected error", e);

			jsonResponse = this.getWebContextHolder().unexpectedErrorResponse();
		}finally{
			this.getEntityManagerHolder().closeEntityManager(em);
		}
		return this.getGson().toJson(jsonResponse);
	}

	/**
	 * Lists all total spent.
	 *
	 * @param data
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/totalSpent.json")
	public String totalSpent(@RequestBody String data){
		EntityManager em = null;
		JsonResponse jsonResponse;
		try{
			em = this.getEntityManagerHolder().getEntityManager();

			TripVo tripVo = this.getGson().fromJson(data,TripVo.class);
			tripVo = this.getTripDao().getById(em,tripVo.getId());
			List<TotalPerUserVo> totalSpentMap = tripVo.totalSpentMap();

			jsonResponse = JsonResponse.ok(this.getGson().toJson(totalSpentMap));
		}catch(Exception e){
			this.logger.debug("unexpected error", e);

			jsonResponse = this.getWebContextHolder().unexpectedErrorResponse();
		}finally{
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

	public I_TripDao getTripDao() {
		return tripDao;
	}

	public void setTripDao(I_TripDao tripDao) {
		this.tripDao = tripDao;
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
