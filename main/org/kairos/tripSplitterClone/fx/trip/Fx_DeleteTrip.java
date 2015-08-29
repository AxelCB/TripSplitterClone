package org.kairos.tripSplitterClone.fx.trip;

import org.kairos.tripSplitterClone.dao.trip.I_TripDao;
import org.kairos.tripSplitterClone.fx.AbstractFxImpl;
import org.kairos.tripSplitterClone.fx.FxValidationResponse;
import org.kairos.tripSplitterClone.fx.I_Fx;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.vo.trip.TripVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * FX for deleting a trip.
 * 
 * @author acollard
 * 
 */
public class Fx_DeleteTrip extends AbstractFxImpl implements I_Fx {

	/**
	 * Logger.
	 */
	private Logger logger = LoggerFactory.getLogger(Fx_DeleteTrip.class);

	/**
	 * Trip DAO.
	 */
	@Autowired
	private I_TripDao dao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.universe.core.fx.AbstractFxImpl#_execute()
	 */
	@Override
	protected JsonResponse _execute() {
		this.logger.debug("executing Fx_DeleteTrip._execute()");

		try {
			this.beginTransaction();

			// we persist the entity
			this.getDao().delete(this.getEm(), this.getVo());

			this.commitTransaction();

			return JsonResponse.ok(
					"",
					this.getRealMessageSolver().getMessage(
							"default.entity.deleted.ok",
							new String[] { this.getRealMessageSolver()
									.getMessage("entity.trip.name",
											null) }));
		} catch (Exception e) {
			this.logger.error("error executing Fx_DeleteTrip._execute()", e);
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
		this.logger.debug("executing Fx_DeleteTrip.validate()");

		if (this.getVo().getId() == null) {

			String errorCodeMessage = this.getRealMessageSolver().getMessage("default.error.code", new Object[] { 1L });
			String jsonResponseMessage = this.getRealMessageSolver().getMessage("default.entity.deleted.error", new String[] { this.getRealMessageSolver().getMessage("entity.trip.name", null), errorCodeMessage });

			return FxValidationResponse.error(jsonResponseMessage);
		} else {
			TripVo tripVo = this.getDao().getById(this.getEm(),this.getVo().getId());

			if (tripVo == null) {
				String jsonResponseMessage = this.getRealMessageSolver().getMessage("fx.trip.validation.entityNotExists", new String[] { this.getRealMessageSolver().getMessage("default.delete", null) });

				return FxValidationResponse.error(jsonResponseMessage);
			} else {
				return FxValidationResponse.ok();
			}
		}
	}

	/**
	 * Casted VO.
	 */
	@Override
	public TripVo getVo() {
		return (TripVo) super.getVo();
	}

	public I_TripDao getDao() {
		return dao;
	}

	public void setDao(I_TripDao dao) {
		this.dao = dao;
	}
}
