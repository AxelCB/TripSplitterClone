package org.kairos.tripSplitterClone.services.caching.session;

import org.kairos.tripSplitterClone.services.caching.api.I_UserCacheManager;
import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.kairos.tripSplitterClone.web.WebContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
 * Created on 8/28/15 by
 *
 * @author AxelCollardBovy.
 */
public class UserCacheSessionImpl implements I_UserCacheManager {

	/**
	 * Logger.
	 */
	private Logger logger = LoggerFactory.getLogger(UserCacheSessionImpl.class);

	/**
	 * Web Context Holder
	 */
	@Autowired
	private WebContextHolder webContextHolder;

	/**
	 * Get's the typed remote User cache
	 *
	 * @return remote User cache
	 */
	private HttpSession getSession() {
		return this.getWebContextHolder().getSession();
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.universe.core.services.caching.client.api.I_CacheProfiler#clear(java
	 * .lang.String)
	 */
//	@Override
//	public void clear(String cacheName) {
//		if (cacheName.equals(this.getUserCacheName())) {
//			this.getUserCache().clear();
//
//			this.logger.debug("cleared cache {}", cacheName);
//		} else if (cacheName.equals(this.getParameterCacheName())) {
//			this.getParameterCache().clear();
//
//			this.logger.debug("cleared cache {}", cacheName);
//		} else {
//			this.logger.error("cache {} not managed by this profiler",
//					cacheName);
//		}
//	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.universe.core.services.caching.client.api.I_CacheProfiler#getEntries
	 * (java.lang.String, java.util.List)
	 */
//	@Override
//	public Map<String, Object> getEntries(String cacheName,
//	                                      List<String> entryList) {
//		if (cacheName.equals(this.getUserCacheName())) {
//			Map<String, Object> entryMap = new HashMap<>();
//
//			for (String entry : entryList) {
//				entryMap.put(entry, this.getUserCache().getWithMetadata(entry));
//			}
//
//			this.logger.debug("entries getted for cache {}", cacheName);
//
//			return entryMap;
//		} else if (cacheName.equals(this.getParameterCacheName())) {
//			Map<String, Object> entryMap = new HashMap<>();
//
//			for (String entry : entryList) {
//				entryMap.put(entry,
//						this.getParameterCache().getWithMetadata(entry));
//			}
//
//			this.logger.debug("entries getted for cache {}", cacheName);
//
//			return entryMap;
//		} else {
//			this.logger.error("cache {} not managed by this profiler",
//					cacheName);
//		}
//
//		return new HashMap<>();
//	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.universe.core.services.caching.client.api.I_CacheProfiler#removeEntries
	 * (java.lang.String, java.util.List)
	 */
//	@Override
//	public void removeEntries(String cacheName, List<String> entryList) {
//		if (cacheName.equals(this.getUserCacheName())) {
//			for (String entry : entryList) {
//				this.getUserCache().remove(entry);
//			}
//
//			this.logger.debug("entries getted for cache {}", cacheName);
//		} else if (cacheName.equals(this.getParameterCacheName())) {
//			for (String entry : entryList) {
//				this.getParameterCache().remove(entry);
//			}
//
//			this.logger.debug("entries getted for cache {}", cacheName);
//		} else {
//			this.logger.error("cache {} not managed by this profiler",
//					cacheName);
//		}
//	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.universe.core.caching.client.I_UserCacheManager#getUser(java.lang
	 * .String)
	 */
	@Override
	public UserVo getUser(String key) {
		UserVo user = null;
		try {
			user = (UserVo)this.getSession().getAttribute(key);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}

	@Override
	public void putUser(String key, UserVo userInCache) {
		try {
			this.getSession().setAttribute(key,userInCache);

			this.logger.info("Insertado Sync {}", key);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.universe.core.services.caching.client.api.I_UserCacheManager#removeUser
	 * (java.lang.String)
	 */
	@Override
	public UserVo removeUser(String key) {
		UserVo user = null;
		try {
			user = (UserVo) this.getSession().getAttribute(key);
			this.getSession().setAttribute(key,null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}

	public WebContextHolder getWebContextHolder() {
		return webContextHolder;
	}

	public void setWebContextHolder(WebContextHolder webContextHolder) {
		this.webContextHolder = webContextHolder;
	}

}
