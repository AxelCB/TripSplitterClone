package org.kairos.tripSplitterClone.fx.destination;

import org.kairos.tripSplitterClone.dao.destination.I_CityDao;
import org.kairos.tripSplitterClone.fx.AbstractFxImpl;
import org.kairos.tripSplitterClone.fx.FxValidationResponse;
import org.kairos.tripSplitterClone.fx.I_Fx;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.vo.destination.CityVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * FX for creating a city.
 * 
 * Created on 8/29/15 by
 *
 * @author AxelCollardBovy.
 */
public class Fx_CreateCity extends AbstractFxImpl implements I_Fx {
	
	/**
	 * Logger.
	 */
	private Logger logger = LoggerFactory.getLogger(Fx_CreateCity.class);

	/**
	 * City DAO.
	 */
	@Autowired
	private I_CityDao dao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.universe.core.fx.AbstractFxImpl#_execute()
	 */
	@Override
	protected JsonResponse _execute() {
		this.logger.debug("executing Fx_CreateCity._execute()");

		try {
			this.beginTransaction();

			// we persist the entity
			CityVo cityVo = this.getDao().persist(this.getEm(), this.getVo());
			this.setVo(cityVo);

			this.commitTransaction();

			return JsonResponse.ok(
					this.getGson().toJson(cityVo),
					this.getRealMessageSolver().getMessage(
							"default.entity.created.ok",
							new String[] { this.getRealMessageSolver()
									.getMessage("entity.city.name", null) }));
		} catch (Exception e) {
			this.logger.error(
					"error executing Fx_CreateCity.execute()", e);
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
		this.logger.debug("executing Fx_CreateCity._validate()");

		String result =null;
		//= this.getVo().validate(this.getWebContextHolder());
		if (result != null) {
			return FxValidationResponse.error(result);
		}

		if (!this.getDao().checkNameUniqueness(this.getEm(),
				this.getVo().getName(), null)) {
			String jsonResponseMessage = this.getRealMessageSolver()
					.getMessage("fx.city.validation.nonUniqueName",
							new String[] { this.getVo().getName(),this.getVo().getCountry().getName() });

			return FxValidationResponse.error(jsonResponseMessage);
		} else {
			return FxValidationResponse.ok();
		}
	}

	/**
	 * Casted VO.
	 */
	@Override
	public CityVo getVo() {
		return (CityVo) super.getVo();
	}

	/**
	 * @return the dao
	 */
	public I_CityDao getDao() {
		return this.dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(I_CityDao dao) {
		this.dao = dao;
	}

}
