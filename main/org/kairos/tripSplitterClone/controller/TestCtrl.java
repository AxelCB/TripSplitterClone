package org.kairos.tripSplitterClone.controller;

import com.google.gson.Gson;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.tests.DestinationTest;
import org.kairos.tripSplitterClone.tests.TripTest;
import org.kairos.tripSplitterClone.tests.UserTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping(value = "/test", produces = "text/json;charset=utf-8", method = RequestMethod.POST)
public class TestCtrl {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(TestCtrl.class);

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
		tripSplitterCloneTests.setVerbose(10);
		tripSplitterCloneTests.run();

		return this.getGson().toJson(JsonResponse.ok("","Everything was done correctly"));
	}

	private void exRun(){
		/*XmlSuite suite = new XmlSuite();
		suite.setName("CompleteTestSuite");

		XmlTest test = new XmlTest(suite);
		test.setName("CompleteTest");
		List<XmlClass> classList = new ArrayList<>();
		classList.add(new XmlClass("org.kairos.tripSplitterClone.tests.UserTest"));
//		classList.add(new XmlClass("org.kairos.tripSplitterClone.tests.UserTest"));
//		classList.add(new XmlClass("org.kairos.tripSplitterClone.tests.UserTest"));
		test.setXmlClasses(classList);

		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		suites.add(suite);

		tripSplitterCloneTests.setXmlSuites(suites);*/
	}

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}
}
