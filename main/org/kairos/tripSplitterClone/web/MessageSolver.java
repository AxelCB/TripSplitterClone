package org.kairos.tripSplitterClone.web;

import org.kairos.tripSplitterClone.json.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * Created on 8/27/15 by
 *
 * @author AxelCollardBovy.
 * 
 */
public class MessageSolver implements I_MessageSolver {

	/**
	 * Message Bundle.
	 */
	@Autowired
	private ReloadableResourceBundleMessageSource messageSource;

	/**
	 * The default message locale.
	 */
	private String defaultMessageLocale;

	/**
	 * The locale.
	 */
	private Locale locale;

	/**
	 * Init method.
	 */
	public void init() {
		this.setLocale(new Locale(this.getDefaultMessageLocale()));
	}

	/**
	 * Gets a i18n message.
	 * 
	 * @param code
	 *            message code
	 * 
	 * @return string
	 */
	@Override
	public String getMessage(String code) {
		return this.getMessageSource().getMessage(code, new String[] {},
				this.getLocale());
	}

	/**
	 * Gets a i18n message.
	 * 
	 * @param code
	 *            message code
	 * @param args
	 *            message parameters
	 * 
	 * @return string
	 */
	@Override
	public String getMessage(String code, Object[] args) {
		return this.getMessageSource().getMessage(code, args, this.getLocale());
	}

	/**
	 * Gets a i18n message.
	 * 
	 * @param code
	 *            message code
	 * @param args
	 *            message parameters
	 * @param defaultMessage
	 *            default message to use if lookup fail
	 * 
	 * @return string
	 */
	@Override
	public String getMessage(String code, Object[] args, String defaultMessage) {
		return this.getMessageSource().getMessage(code, args, defaultMessage,
				this.getLocale());
	}

	/**
	 * Gets the default message for a required parameter that is missing.
	 * 
	 * @param parameter
	 *            the parameter code
	 * 
	 * @return String
	 */
	@Override
	public String getRequiredParameterMessage(String parameter) {
		String field = this.getMessage(parameter);

		return this.getMessage("default.fx.validation.parameter.required",
				new String[] { field });
	}

	/**
	 * Generates an error message with the specified code.
	 * 
	 * @param errorCode
	 *            the error code
	 * 
	 * @return string
	 */
	@Override
	public String errorMessage(String errorCode) {
		String errorCodeMessage = this.getMessage("default.error.code",
				new Object[] { errorCode });

		return this.getMessage("default.error.message",
				new String[] { errorCodeMessage });
	}

	/**
	 * Generates a standard error response with a generic message.
	 * 
	 * @return jsonResponse
	 */
	@Override
	public JsonResponse unexpectedErrorResponse() {
		return this.unexpectedErrorResponse("0");
	}//ErrorCodes.ERROR_UNEXPECTED

	/**
	 * 
	 * Generates a standard error response with a generic message.
	 * 
	 * @param errorCode
	 *            the error code to set
	 * 
	 * @return jsonResponse
	 */
	@Override
	public JsonResponse unexpectedErrorResponse(String errorCode) {
		return JsonResponse.error("", this.errorMessage(errorCode));
	}

	/**
	 * Gets a i18n message.
	 * 
	 * @param code
	 *            message code
	 * 
	 * @return string
	 */
	public String getMessage(String code, Locale locale) {
		return this.getMessageSource()
				.getMessage(code, new String[] {}, locale);
	}

	/**
	 * Gets a i18n message.
	 * 
	 * @param code
	 *            message code
	 * @param args
	 *            message parameters
	 * 
	 * @return string
	 */
	public String getMessage(String code, Object[] args, Locale locale) {
		return this.getMessageSource().getMessage(code, args, locale);
	}

	/**
	 * Gets a i18n message.
	 * 
	 * @param code
	 *            message code
	 * @param args
	 *            message parameters
	 * @param defaultMessage
	 *            default message to use if lookup fail
	 * 
	 * @return string
	 */
	public String getMessage(String code, Object[] args, String defaultMessage,
			Locale locale) {
		return this.getMessageSource().getMessage(code, args, defaultMessage,
				locale);
	}

	/**
	 * Gets the default message for a required parameter that is missing.
	 * 
	 * @param parameter
	 *            the parameter code
	 * 
	 * @return String
	 */
	public String getRequiredParameterMessage(String parameter, Locale locale) {
		String field = this.getMessage(parameter, locale);

		return this.getMessage("default.fx.validation.parameter.required",
				new String[] { field }, locale);
	}

	/**
	 * Generates an error message with the specified code.
	 * 
	 * @param errorCode
	 *            the error code
	 * 
	 * @return string
	 */
	public String errorMessage(String errorCode, Locale locale) {
		String errorCodeMessage = this.getMessage("default.error.code",
				new Object[] { errorCode }, locale);

		return this.getMessage("default.error.message",
				new String[] { errorCodeMessage }, locale);
	}

	/**
	 * Generates a standard error response with a generic message.
	 * 
	 * @return jsonResponse
	 */
	public JsonResponse unexpectedErrorResponse(Locale locale) {
		return this
				.unexpectedErrorResponse("0", locale);//ErrorCodes.ERROR_UNEXPECTED
	}

	/**
	 * 
	 * Generates a standard error response with a generic message.
	 * 
	 * @param errorCode
	 *            the error code to set
	 * 
	 * @return jsonResponse
	 */
	public JsonResponse unexpectedErrorResponse(String errorCode, Locale locale) {
		return JsonResponse.error("", this.errorMessage(errorCode, locale));
	}

	/**
	 * @return the messageSource
	 */
	public ReloadableResourceBundleMessageSource getMessageSource() {
		return this.messageSource;
	}

	/**
	 * @param messageSource
	 *            the messageSource to set
	 */
	public void setMessageSource(
			ReloadableResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @return the defaultMessageLocale
	 */
	public String getDefaultMessageLocale() {
		return this.defaultMessageLocale;
	}

	/**
	 * @param defaultMessageLocale
	 *            the defaultMessageLocale to set
	 */
	public void setDefaultMessageLocale(String defaultMessageLocale) {
		this.defaultMessageLocale = defaultMessageLocale;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return this.locale;
	}

	/**
	 * @param locale
	 *            the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
