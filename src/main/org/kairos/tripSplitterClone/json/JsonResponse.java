package org.kairos.tripSplitterClone.json;

import java.util.Arrays;
import java.util.List;

/**
 * General JSON Response Document.
 *
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 * 
 */
public class JsonResponse {

	/**
	 * Occurs when a token is no longer valid.
	 */
	public static final String ACTION_RELOGIN = "relogin";

	/**
	 * Occurs when a certain functionality is no longer available to the user.
	 */
	public static final String ACTION_REFRESH_USER = "refreshUser";

	/**
	 * OK flag.
	 */
	private Boolean ok;

	/**
	 * A list of messages
	 */
	private List<String> messages;

	/**
	 * Response Data
	 */
	private String data;

	/**
	 * Action Specification
	 */
	private String action;

	/**
	 * More data for this response that doesn't need to be strictly associated
	 * with the main data.
	 */
	private String payload;

	/**
	 * We make a private constructor.
	 */
	private JsonResponse() {
		super();
	}

	/**
	 * Returns a generic response.
	 * 
	 * @param data
	 * @param messages
	 * @return
	 */
	private static JsonResponse genericResponse(String data, Boolean ok,
			String... messages) {
		JsonResponse response = new JsonResponse();
		response.setOk(ok);
		if (messages != null && messages.length > 0) {
			response.setMessages(Arrays.asList(messages));
		}
		response.setData(data);

		return response;
	}

	/**
	 * Returns a new OK response.
	 * 
	 * @param data
	 * @param messages
	 *            string
	 * @return
	 */
	public static JsonResponse ok(String data, String... messages) {
		return genericResponse(data, Boolean.TRUE, messages);
	}

	/**
	 * Returns a new OK response.
	 * 
	 * @param data
	 *            the data
	 * @return JsonResponse
	 */
	public static JsonResponse ok(String data) {
		return genericResponse(data, Boolean.TRUE, new String[] {});
	}

	/**
	 * Returns a new OK response.
	 * 
	 * @param data
	 * @param messages
	 *            string
	 * @return
	 */
	public static JsonResponse ok(String data, List<String> messages) {
		return ok(data, messages.toArray(new String[] {}));
	}

	/**
	 * Returns a new OK response.
	 * 
	 * @param data
	 *            the data
	 * @param messages
	 *            the messages
	 * @return JsonResponse
	 */
	public static JsonResponse error(String data, String... messages) {
		return genericResponse(data, Boolean.FALSE, messages);
	}

	/**
	 * Returns a new error response.
	 * 
	 * @param data
	 * @param messages
	 *            string
	 * @return
	 */
	public static JsonResponse error(String data, List<String> messages) {
		return error(data, messages == null ? null : messages.toArray(new String[] {}));
	}

	/**
	 * @return the ok
	 */
	public Boolean getOk() {
		return this.ok;
	}

	/**
	 * @param ok
	 *            the ok to set
	 */
	public void setOk(Boolean ok) {
		this.ok = ok;
	}

	/**
	 * @return the messages
	 */
	public List<String> getMessages() {
		return this.messages;
	}

	/**
	 * @param messages
	 *            the messages to set
	 */
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return this.data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return this.action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the payload
	 */
	public String getPayload() {
		return this.payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(String payload) {
		this.payload = payload;
	}

}
