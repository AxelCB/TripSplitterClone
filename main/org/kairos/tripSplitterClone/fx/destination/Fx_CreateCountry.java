package org.kairos.tripSplitterClone.fx.destination;

import org.kairos.tripSplitterClone.dao.destination.I_CountryDao;
import org.kairos.tripSplitterClone.fx.AbstractFxImpl;
import org.kairos.tripSplitterClone.fx.FxValidationResponse;
import org.kairos.tripSplitterClone.fx.I_Fx;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.vo.destination.CountryVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * FX for creating a country.
 * 
 * Created on 8/29/15 by
 *
 * @author AxelCollardBovy.
 */
public class Fx_CreateCountry extends AbstractFxImpl implements I_Fx {
	
	/**
	 * Logger.
	 */
	private Logger logger = LoggerFactory.getLogger(Fx_CreateCountry.class);

	/**
	 * Country DAO.
	 */
	@Autowired
	private I_CountryDao dao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.universe.core.fx.AbstractFxImpl#_execute()
	 */
	@Override
	protected JsonResponse _execute() {
		this.logger.debug("executing Fx_CreateCountry._execute()");

		try {
			this.beginTransaction();

			// we persist the entity
			CountryVo countryVo = this.getDao().persist(this.getEm(), this.getVo());
			this.setVo(countryVo);

			this.commitTransaction();

			return JsonResponse.ok(
					this.getGson().toJson(countryVo),
					this.getRealMessageSolver().getMessage(
							"default.entity.created.ok",
							new String[] { this.getRealMessageSolver()
									.getMessage("entity.country.name", null) }));
		} catch (Exception e) {
			this.logger.error(
					"error executing Fx_CreateCountry.execute()", e);
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
		this.logger.debug("executing Fx_CreateCountry._validate()");

		String result =null;
		//= this.getVo().validate(this.getWebContextHolder());
		if (result != null) {
			return FxValidationResponse.error(result);
		}

		if (!this.getDao().checkNameUniqueness(this.getEm(),
				this.getVo().getName(), null)) {
			String jsonResponseMessage = this.getRealMessageSolver()
					.getMessage("fx.country.validation.nonUniqueName",
							new String[] { this.getVo().getName() });

			return FxValidationResponse.error(jsonResponseMessage);
		} else {
			return FxValidationResponse.ok();
		}
	}

	/**
	 * Casted VO.
	 */
	@Override
	public CountryVo getVo() {
		return (CountryVo) super.getVo();
	}

	/**
	 * @return the dao
	 */
	public I_CountryDao getDao() {
		return this.dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(I_CountryDao dao) {
		this.dao = dao;
	}

}
