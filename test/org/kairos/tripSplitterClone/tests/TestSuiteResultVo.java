package org.kairos.tripSplitterClone.tests;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 9/5/15 by
 *
 * @author AxelCollardBovy.
 */
public class TestSuiteResultVo implements Serializable {

	/**
	 * Successful tests
	 */
	public List<TestResultVo> configurationFailures = new ArrayList<>();

	/**
	 * Configuration
	 */
	public List<TestResultVo> passedTests = new ArrayList<>();

	/**
	 * Skipped tests
	 */
	public List<TestResultVo> skippedTests = new ArrayList<>();

	/**
	 * Failed tests
	 */
	public List<TestResultVo> failedTests = new ArrayList<>();

	public List<TestResultVo> getConfigurationFailures() {
		return configurationFailures;
	}

	public void setConfigurationFailures(List<TestResultVo> configurationFailures) {
		this.configurationFailures = configurationFailures;
	}

	public List<TestResultVo> getPassedTests() {
		return passedTests;
	}

	public void setPassedTests(List<TestResultVo> passedTests) {
		this.passedTests = passedTests;
	}

	public List<TestResultVo> getSkippedTests() {
		return skippedTests;
	}

	public void setSkippedTests(List<TestResultVo> skippedTests) {
		this.skippedTests = skippedTests;
	}

	public List<TestResultVo> getFailedTests() {
		return failedTests;
	}

	public void setFailedTests(List<TestResultVo> failedTests) {
		this.failedTests = failedTests;
	}
}
