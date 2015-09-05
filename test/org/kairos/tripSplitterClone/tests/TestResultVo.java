package org.kairos.tripSplitterClone.tests;

import java.io.Serializable;

/**
 * Created on 9/5/15 by
 *
 * @author AxelCollardBovy.
 */
public class TestResultVo implements Serializable {

	public String clazz;

	public String method;

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}
