package org.kairos.tripSplitterClone.utils.exception;

/**
 * Represents an exception that has an error code so it can be easily tracked
 * down.
 *
 * Created on 8/27/15 by
 *
 * @author AxelCollardBovy.
 * 
 */
public class CodedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5021606347094575182L;

	/**
	 * The error code of this exception.
	 */
	private String errorCode;
	
	/**
	 * Declares the only constructor of this base exception.
	 * 
	 * @param message message of the exception
	 * @param errorCode error code
	 * @param cause cause (could be null)
	 */
	public CodedException(String message, String errorCode, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return this.errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
