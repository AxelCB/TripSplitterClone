package org.kairos.tripSplitterClone.fx;

import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.utils.exception.CodedException;

/**
 * Coded Exception that holds a JsonResponse to the user.
 *
 * Created on 8/27/15 by
 *
 * @author AxelCollardBovy.
 *
 */
public class ResponseException extends CodedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2050148895656157798L;
	
	/**
	 * The response.
	 */
	private JsonResponse jsonResponse;

	/**
	 * @param message
	 * @param errorCode
	 * @param cause
	 */
	public ResponseException(String message, String errorCode, Throwable cause,JsonResponse jsonResponse) {
		super(message, errorCode, cause);
		this.jsonResponse = jsonResponse;
	}

	/**
	 * @return the jsonResponse
	 */
	public JsonResponse getJsonResponse() {
		return this.jsonResponse;
	}

	/**
	 * @param jsonResponse the jsonResponse to set
	 */
	public void setJsonResponse(JsonResponse jsonResponse) {
		this.jsonResponse = jsonResponse;
	}
	
}
