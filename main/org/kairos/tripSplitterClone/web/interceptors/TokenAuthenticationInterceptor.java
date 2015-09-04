package org.kairos.tripSplitterClone.web.interceptors;

import com.google.gson.Gson;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.kairos.tripSplitterClone.web.WebContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Intercepts all requests and cheks the token existence (if necesary)
 * 
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 * 
 */
public class TokenAuthenticationInterceptor extends HandlerInterceptorAdapter {

	/**
	 * Logger.
	 */
	private Logger logger = LoggerFactory
			.getLogger(TokenAuthenticationInterceptor.class);

	/**
	 * Web Context Holder.
	 */
	@Autowired
	private WebContextHolder webContextHolder;

	/**
	 * GSON transformer.
	 */
	@Autowired
	private Gson gson;

	/**
	 * URL's that don't need token authentication.
	 */
	private String[] letPass = { "/footer", // just the
																	// footer of
																	// the
																	// page
			"/core/user/login.json", // in the login phase, we still don't have a
									// generated token
			"/core/test/run.json", // the user registration enabled
												// flag
			"/core/user/register.json", // anything under the registration controller
	};

	/**
	 * Check if we have to let it pass the request
	 * 
	 * @param uri
	 *            to check to pass
	 * @return
	 */
	private Boolean letItPass(String uri) {
		for (String itemToPass : this.letPass) {
			if (uri.replace("/tripSplitterClone-core", "").startsWith(itemToPass)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
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
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		this.logger.debug("calling TokenAuthenticationInterceptor.preHandle()");

		if (this.letItPass(request.getRequestURI())) {
			// the reKest doesn't requires a token
			this.logger.debug("trying to login, auth not required");
			return true;
		} else {
			if (request.getHeader("Authorization") == null
					|| request.getHeader("Authorization").trim().equals("")) {

				this.logger.error("no authorization header was found,"
						+ "from ip {}, trying to access to {}",
						request.getRemoteAddr(), request.getRequestURI());

				response.sendError(401);

				return false;
			} else {
				String token = request.getHeader("Authorization");

				UserVo userVo = (UserVo)this.getWebContextHolder().getSession().getAttribute(token);

				if (userVo != null) {
					this.getWebContextHolder().setUserVo(userVo);
					this.logger.debug("user {} retreived by token",
							userVo.getEmail());

						String uri = request.getRequestURI()
								.replace(".json", "")
								.replace("/tripSplitterClone-core", "");

						// if the handler doesn't implements the I_URIValidator
						// interface
						// or if the handler says that the URI must be
						// validated,
						// validates it with the UserVo
//						if (!(handler instanceof I_URIValidator)
//								|| ((I_URIValidator) handler).validate(uri)) {
//							if (userVo.canAcces(uri)) {
//								this.logger
//										.debug("the user can access this URI");
//							} else {
//								this.logger
//										.debug("the user cannot access this URI");
//
//								JsonResponse jsonResponse = JsonResponse
//										.error(this.getGson().toJson(userVo),
//												this.getWebContextHolder()
//														.getMessage(
//																"security.validation.forbiddenAction",
//																null));
//								jsonResponse
//										.setAction(JsonResponse.ACTION_REFRESH_USER);
//
//								response.getOutputStream().write(
//										this.getGson().toJson(jsonResponse)
//												.getBytes());
//
//								return false;
//							}
//						} else {
//							this.logger
//									.debug("the handler doesn't requires to validate the URI");
//						}

						// everything's OK
						return true;
//					}
				} else {
					this.logger
							.debug("the token was not associated with any logged user");

					JsonResponse jsonResponse = JsonResponse.error(
							"",
							this.getWebContextHolder().getMessage(
									"security.validation.userByTokenNotFound",
									null));
					jsonResponse.setAction(JsonResponse.ACTION_RELOGIN);

					response.getOutputStream().write(
							this.getGson().toJson(jsonResponse).getBytes());

					return false;
				}
			}
		}
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
	 * @return the gson
	 */
	public Gson getGson() {
		return this.gson;
	}

	/**
	 * @param gson
	 *            the gson to set
	 */
	public void setGson(Gson gson) {
		this.gson = gson;
	}

}
