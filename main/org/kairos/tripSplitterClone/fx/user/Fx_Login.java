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
public class Fx_Login extends AbstractFxImpl implements I_Fx {

	/**
	 * Logger.
	 */
	private Logger logger = LoggerFactory.getLogger(Fx_Login.class);

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
		return FxValidationResponse.ok();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.universe.core.fx.AbstractFxImpl#_execute()
	 */
	@Override
	protected JsonResponse _execute() {
		this.logger.debug("executing Fx_Login._execute() (username: {})", this
				.getVo().getUsername());

		UserVo userVo = null;

		String token = null;

		try {
			// response to send
			JsonResponse response = null;

			this.getEm().getTransaction().begin();

			Long maxAttempts = Parameters.LOGIN_MAX_ATTEMPTS;
			Long hashCost = Parameters.HASH_COST;
			if (userVo == null) {
				userVo = this.getUserDao().getByUsername(this.getEm(),
						this.getVo().getUsername());
			}

			if (userVo == null) {
				this.logger.debug("failed to find user with username {}", this
						.getVo().getUsername());

				response = JsonResponse.error("", this.getWebContextHolder().getMessageSolver().getMessage("fx.login.userNotFound"));
			} else if (userVo.getLoginAttempts() >= maxAttempts) {
				// block the user
				this.logger.debug("not allowing the user to log in, max failed attempts reached");

				response = JsonResponse.error("", this.getWebContextHolder().getMessageSolver().getMessage("fx.login.disabledCause.maxLoginAttemps"));
			} else if (!this.passwordUtils.checkPassword(this.getVo().getPassword(),
					userVo, hashCost)) {
				// adds the failed attempt
				userVo.setLoginAttempts(userVo.getLoginAttempts() + 1);

				this.logger.debug(
						"failed to login user {}, total failed attempts: {}",
						this.getVo().getUsername(), userVo.getLoginAttempts());

				if (userVo.getLoginAttempts() < maxAttempts) {
					// we return the remaining attempts messages
					response = JsonResponse.error("",this.getWebContextHolder().getMessageSolver().getMessage("fx.login.attemptFailed",new Object[] { maxAttempts - userVo.getLoginAttempts() }));

					this.getUserDao().persist(this.getEm(), userVo);
				} else {
					// block the user
					this.logger.debug("not allowing the user to log in, max failed attempts reached");



					response = JsonResponse.error("", this.getWebContextHolder().getMessageSolver().getMessage("fx.login.disabledCause.maxLoginAttemps"));

					this.getUserDao().persist(this.getEm(), userVo);
				}
			} else {
				this.logger.debug("user properly logged");
				// properly logged, reset the attempts
				userVo.setLoginAttempts(0);

				UserVo newUserVo = this.getUserDao().persist(this.getEm(),
						userVo);

				// try to get the existing user from the cache
				UserVo cacheUserVo = this.getUserCacheManager().getUser(this.getVo().getUsername());
				if (cacheUserVo == null) {
					// there was none, we generate a new token and put it on the
					// cache
					token = HashUtils.generateUserToken(newUserVo);
					newUserVo.setToken(token);

					this.logger.debug("storing the user to the cache");
					this.getUserCacheManager().putUser(token, newUserVo);
					this.getUserCacheManager().putUser(newUserVo.getUsername(),newUserVo);
				} else {
					// we return the same user
					newUserVo = cacheUserVo;
				}

				response = JsonResponse.ok(this.getGson().toJson(newUserVo));
			}

			this.getEm().getTransaction().commit();

			return response;

		} catch (Exception e) {
			this.logger.error("error executing Fx_Login._execute()", e);
			try {
				this.getEm().getTransaction().rollback();
			} catch (Exception e1) {
				this.logger.error("error rollbacking transaction", e);
			}

			if (token != null) {
				this.getUserCacheManager().putUser(token, null);
			}
			if (userVo != null) {
				this.getUserCacheManager().putUser(userVo.getUsername(), null);
			}

			return this.getWebContextHolder().getMessageSolver().unexpectedErrorResponse();
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

}
