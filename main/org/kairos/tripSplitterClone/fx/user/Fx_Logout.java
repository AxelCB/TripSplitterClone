package org.kairos.tripSplitterClone.fx.user;

import org.kairos.tripSplitterClone.fx.AbstractFxImpl;
import org.kairos.tripSplitterClone.fx.FxValidationResponse;
import org.kairos.tripSplitterClone.fx.I_Fx;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.services.caching.api.I_UserCacheManager;
import org.kairos.tripSplitterClone.web.WebContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Logout function.
 * 
 * @author fgonzalez
 * 
 */
public class Fx_Logout extends AbstractFxImpl implements I_Fx {

	/**
	 * Logger.
	 */
	private Logger logger = LoggerFactory.getLogger(Fx_Logout.class);

	/**
	 * User Cache manager.
	 */
	@Autowired
	private I_UserCacheManager userCacheManager;

	/**
	 * Web Context Holder.
	 */
	@Autowired
	private WebContextHolder webContextHolder;

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
		this.logger.debug("logging out user {}", this.getWebContextHolder().getUserVo().getUsername());

		this.getUserCacheManager().removeUser(this.getWebContextHolder().getUserVo().getToken());
		this.getUserCacheManager().removeUser(this.getWebContextHolder().getUserVo().getUsername());

		return JsonResponse.ok("");
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

	/**
	 * @return the webContextHolder
	 */
	@Override
	public WebContextHolder getRealMessageSolver() {
		return this.webContextHolder;
	}

	/**
	 * @param webContextHolder
	 *            the webContextHolder to set
	 */
	@Override
	public void setWebContextHolder(WebContextHolder webContextHolder) {
		this.webContextHolder = webContextHolder;
	}

}
