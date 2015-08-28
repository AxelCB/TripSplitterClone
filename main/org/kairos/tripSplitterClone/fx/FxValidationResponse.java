package org.kairos.tripSplitterClone.fx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Validation response of an Fx.validate() call
 * 
 * @author fgonzalez
 *
 */
public class FxValidationResponse {
	
	/**
	 * Static OK response.
	 */
	private static FxValidationResponse _ok = _ok();

	/**
	 * Validation flag.
	 */
	private Boolean ok;
	
	/**
	 * Data. (only used internally)
	 */
	private String data;
	
	/**
	 * Validation messages 
	 */
	private List<String> messages =  new ArrayList<>();
	
	/**
	 * Private constructor.
	 */
	private FxValidationResponse() {
		super();
	}
	
	/**
	 * Returns an OK validation response.
	 * 
	 * @return
	 */
	public static FxValidationResponse ok() {
		return _ok;
	}
	
	/**
	 * Returns an OK FxValidationResponse
	 * 
	 * @return OK FxValidationResponse
	 */
	private static FxValidationResponse _ok() {
		FxValidationResponse fxValidationResponse =  new FxValidationResponse();
		fxValidationResponse.setOk(Boolean.TRUE);

		return fxValidationResponse;
	}
	
	/**
	 * Returns an error FxValidationResponse
	 * 
	 * @param messages error messages
	 * 
	 * @return error FxValidationResponse
	 */
	public static FxValidationResponse error(String... messages) {
		FxValidationResponse fxValidationResponse =  new FxValidationResponse();
		fxValidationResponse.setOk(Boolean.FALSE);
		fxValidationResponse.setMessages(Arrays.asList(messages));
		
		return fxValidationResponse;
	}
	
	/**
	 * Returns an error FxValidationResponse
	 * 
	 * @param messages error messages
	 * 
	 * @return error FxValidationResponse
	 */
	public static FxValidationResponse error(List<String> messages) {
		FxValidationResponse fxValidationResponse =  new FxValidationResponse();
		fxValidationResponse.setOk(Boolean.FALSE);
		fxValidationResponse.setMessages(messages);
		
		return fxValidationResponse;
	}
	
	/**
	 * Sets the data and returns itself.
	 * 
	 * @param data
	 * @return
	 */
	public FxValidationResponse withData(String data) {
		this.setData(data);
		return this;
	}
	
	
	/**
	 * @return the ok
	 */
	public Boolean getOk() {
		return this.ok;
	}

	/**
	 * @param ok the ok to set
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
	 * @param messages the messages to set
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
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	
}
