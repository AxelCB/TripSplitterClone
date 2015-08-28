package org.kairos.tripSplitterClone.web.interceptors;

import org.kairos.tripSplitterClone.utils.HttpUtils;
import org.kairos.tripSplitterClone.web.WebContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Intercepts the requests and sets the locale to the WebContextHolder
 * 
 * @author fgonzalez
 * 
 */
public class WebContextHolderInterceptor extends HandlerInterceptorAdapter {

	private static int ic = 0;

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory
			.getLogger(WebContextHolderInterceptor.class);

	/**
	 * Web Context Holder
	 */
	@Autowired
	private WebContextHolder webContextHolder;

	/**
	 * Accepted locales.
	 */
	private List<String> acceptedLocales = Arrays.asList(new String[] { "es",
			"pt" });

	/**
	 * Default constructor.
	 */
	public WebContextHolderInterceptor() {
		ic++;
		this.logger.debug("ic count: {}", ic);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle
	 * (javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		this.logger.debug("calling TokenAuthenticationInterceptor.preHandle()");

		String locale = HttpUtils.extractLanguageTag(request, this
				.getWebContextHolder().getMessageSolver()
				.getDefaultMessageLocale(), Boolean.FALSE);
		if (this.getAcceptedLocales().contains(locale)) {
			this.getWebContextHolder().setLocale(Locale.forLanguageTag(locale));
		} else {
			this.getWebContextHolder().setLocale(
					Locale.forLanguageTag(this.getWebContextHolder()
							.getMessageSolver().getDefaultMessageLocale()));
		}

		this.logger.debug("locale: "
				+ this.getWebContextHolder().getLocale().toLanguageTag());


		this.logger.debug("Setting WebContextHolder session");

		this.getWebContextHolder().setSession(request.getSession());

		return true;
	}

	/**
	 * @return the webContextHolder
	 */
	public WebContextHolder getWebContextHolder() {
		return this.webContextHolder;
	}

	/**
	 * @param webContextHolder
	 *            the webContextHolder to set
	 */
	public void setWebContextHolder(WebContextHolder webContextHolder) {
		this.webContextHolder = webContextHolder;
	}

	/**
	 * @param acceptedLocales
	 *            the acceptedLocales to set
	 */
	public void setAcceptedLocales(List<String> acceptedLocales) {
		this.acceptedLocales = acceptedLocales;
	}

	/**
	 * @return the acceptedLocales
	 */
	public List<String> getAcceptedLocales() {
		return this.acceptedLocales;
	}
}
