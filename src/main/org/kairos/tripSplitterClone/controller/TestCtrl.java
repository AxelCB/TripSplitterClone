package org.kairos.tripSplitterClone.controller;

import com.google.gson.Gson;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.tests.DestinationTest;
import org.kairos.tripSplitterClone.tests.TripTest;
import org.kairos.tripSplitterClone.tests.UserTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

/**
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 */
@RequestMapping(value = "/test", produces = "text/json;charset=utf-8", method = RequestMethod.GET)
public class TestCtrl {

	@Autowired
	private Gson gson;

	/**
	 * Runs the tests.
	 *
	 * @param data
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/run.json")
	public String runTests(@RequestBody String data){
		TestListenerAdapter tla = new TestListenerAdapter();
		TestNG tripSplitterCloneTests = new TestNG();

		tripSplitterCloneTests.setTestClasses(new Class[]{UserTest.class, DestinationTest.class, TripTest.class});
		tripSplitterCloneTests.addListener(tla);
		tripSplitterCloneTests.run();

		return this.getGson().toJson(JsonResponse.ok("","Everything was done correctly"));
	}

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}
}
