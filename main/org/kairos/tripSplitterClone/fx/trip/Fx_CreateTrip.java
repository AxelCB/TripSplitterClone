package org.kairos.tripSplitterClone.fx.trip;

import org.kairos.tripSplitterClone.dao.trip.I_TripDao;
import org.kairos.tripSplitterClone.fx.AbstractFxImpl;
import org.kairos.tripSplitterClone.fx.FxValidationResponse;
import org.kairos.tripSplitterClone.fx.I_Fx;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.vo.trip.TripVo;
import org.kairos.tripSplitterClone.vo.trip.UserTripVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.kairos.tripSplitterClone.web.WebContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * FX for creating a trip.
 * 
 * Created on 8/29/15 by
 *
 * @author AxelCollardBovy.
 */
public class Fx_CreateTrip extends AbstractFxImpl implements I_Fx {
	
	/**
	 * Logger.
	 */
	private Logger logger = LoggerFactory.getLogger(Fx_CreateTrip.class);

	/**
	 * Trip DAO.
	 */
	@Autowired
	private I_TripDao dao;

	/**
	 * Owner (user)
	 */
	private UserVo owner;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.universe.core.fx.AbstractFxImpl#_execute()
	 */
	@Override
	protected JsonResponse _execute() {
		this.logger.debug("executing Fx_CreateTrip._execute()");

		try {
			this.beginTransaction();

			this.getVo().setOwner(this.getOwner());
			this.getVo().getTravelers().add(new UserTripVo(this.getOwner(),this.getVo()));

			// we persist the entity
			TripVo tripVo = this.getDao().persist(this.getEm(), this.getVo());

			this.setVo(tripVo);

			this.commitTransaction();

			return JsonResponse.ok(
					this.getGson().toJson(tripVo),
					this.getRealMessageSolver().getMessage(
							"default.entity.created.ok",
							new String[] { this.getRealMessageSolver()
									.getMessage("entity.trip.name", null) }));
		} catch (Exception e) {
			this.logger.error(
					"error executing Fx_CreateTrip.execute()", e);
			try {
				this.rollbackTransaction();
			} catch (Exception e1) {
				this.logger.error("error rollbacking transaction", e);
			}

			return this.unexpectedErrorResponse();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.universe.core.fx.AbstractFxImpl#validate()
	 */
	@Override
	protected FxValidationResponse validate() {
		this.logger.debug("executing Fx_CreateTrip._validate()");

		String result =null;
		//= this.getVo().validate(this.getWebContextHolder());
		if (result != null) {
			return FxValidationResponse.error(result);
		}

//		if (!this.getDao().checkNameUniqueness(this.getEm(),
//				this.getVo().getName(), null)) {
//			String jsonResponseMessage = this.getRealMessageSolver()
//					.getMessage("fx.Trip.validation.nonUniqueName",
//							new String[] { this.getVo().getName() });
//
//			return FxValidationResponse.error(jsonResponseMessage);
//		} else {
			return FxValidationResponse.ok();
//		}
	}

	/**
	 * Casted VO.
	 */
	@Override
	public TripVo getVo() {
		return (TripVo) super.getVo();
	}

	/**
	 * @return the dao
	 */
	public I_TripDao getDao() {
		return this.dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(I_TripDao dao) {
		this.dao = dao;
	}

	public UserVo getOwner() {
		return owner;
	}

	public void setOwner(UserVo owner) {
		this.owner = owner;
	}
}
