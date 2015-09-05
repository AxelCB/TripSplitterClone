package org.kairos.tripSplitterClone.fx.user;

import org.kairos.tripSplitterClone.dao.user.I_UserDao;
import org.kairos.tripSplitterClone.fx.AbstractFxImpl;
import org.kairos.tripSplitterClone.fx.FxValidationResponse;
import org.kairos.tripSplitterClone.fx.I_Fx;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.services.caching.api.I_UserCacheManager;
import org.kairos.tripSplitterClone.utils.HashUtils;
import org.kairos.tripSplitterClone.utils.I_PasswordUtils;
import org.kairos.tripSplitterClone.utils.Parameters;
import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Login Function.
 *
 * Created on 8/27/15 by
 *
 * @author AxelCollardBovy.
 *
 */
public class Fx_Register extends AbstractFxImpl implements I_Fx {

	/**
	 * Logger.
	 */
	private Logger logger = LoggerFactory.getLogger(Fx_Register.class);

	/**
	 * User Dao.
	 */
	@Autowired
	private I_UserDao userDao;

	/**
	 * User Cache Manager.
	 */
	@Autowired
	private I_UserCacheManager userCacheManager;
	
	/**
	 * Password utils
	 */
	@Autowired
	private I_PasswordUtils passwordUtils;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.universe.core.fx.AbstractFxImpl#validate()
	 */
	@Override
	protected FxValidationResponse validate() {
		String result = this.getVo().validate(this.getWebContextHolder());
		if (result != null) {
			return FxValidationResponse.error(result);
		}
		if (!this.getUserDao().checkUsernameUniqueness(this.getEm(),
				this.getVo().getEmail(), null)) {
			return FxValidationResponse.error(this.getWebContextHolder()
					.getMessage("fx.register.nonUniqueEmail"));
		}
		return FxValidationResponse.ok();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.universe.core.fx.AbstractFxImpl#_execute()
	 */
	@Override
	protected JsonResponse _execute() {
		this.logger.debug("executing Fx_Register._execute() (username: {})", this
				.getVo().getUsername());
		try {
			this.getEm().getTransaction().begin();

			this.getVo().setLoginAttempts(0);
			this.getVo().setHashCost(Parameters.HASH_COST);
			this.getVo().setPassword(HashUtils.hashPassword(this.getVo().getPassword(),Parameters.HASH_COST));

			this.setVo(this.getUserDao().persist(this.getEm(), this.getVo()));

			this.commitTransaction();

			return JsonResponse.ok(
					this.getGson().toJson(this.getVo()),
					this.getRealMessageSolver().getMessage(
							"default.entity.created.ok",
							new String[] { this.getRealMessageSolver()
									.getMessage("entity.user.name", null) }));
		} catch (Exception e) {
			this.logger.error(
					"error executing Fx_Register.execute()", e);
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
	 * @see org.universe.core.fx.AbstractFxImpl#getVo()
	 */
	@Override
	public UserVo getVo() {
		return (UserVo) super.getVo();
	}

	/**
	 * @return the userDao
	 */
	public I_UserDao getUserDao() {
		return this.userDao;
	}

	/**
	 * @param userDao
	 *            the userDao to set
	 */
	public void setUserDao(I_UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * @return the userCacheManager
	 */
	public I_UserCacheManager getUserCacheManager() {
		return this.userCacheManager;
	}

	/**
	 * @param userCacheManager
	 *            the userCacheManager to set
	 */
	public void setUserCacheManager(I_UserCacheManager userCacheManager) {
		this.userCacheManager = userCacheManager;
	}

	public I_PasswordUtils getPasswordUtils() {
		return passwordUtils;
	}

	public void setPasswordUtils(I_PasswordUtils passwordUtils) {
		this.passwordUtils = passwordUtils;
	}
}
