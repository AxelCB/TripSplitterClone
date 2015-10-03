package org.kairos.tripSplitterClone.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.kairos.tripSplitterClone.controller.DestinationCtrl;
import org.kairos.tripSplitterClone.dao.EntityManagerHolder;
import org.kairos.tripSplitterClone.dao.destination.I_CityDao;
import org.kairos.tripSplitterClone.dao.destination.I_CountryDao;
import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.utils.exception.ValidationException;
import org.kairos.tripSplitterClone.vo.destination.CityVo;
import org.kairos.tripSplitterClone.vo.destination.CountryVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 */
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/mainContext.xml","classpath:spring/servletContext.xml"})
@TestExecutionListeners({
		ServletTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class
})
public class DestinationModelTest extends AbstractTestNGSpringContextTests {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(DestinationModelTest.class);

	private CountryVo countryVo;
	private CityVo cityVo;

	@Test(groups = {"destination"})
	public void createCountryTest(){
		countryVo = new CountryVo("Grecia");
		String validateResponse = countryVo.validate();
		assert(validateResponse!=null):validateResponse;
	}

	@Test(groups = {"destination"},dependsOnMethods = {"createCountryTest"})
	public void createCityTest(){
		try {
			cityVo=new CityVo("Atenas",countryVo);
		} catch (ValidationException e) {
			assert(e==null):e.getMessage();
		}
		String validateResponse = cityVo.validate();
		assert(validateResponse!=null):validateResponse;
	}
}
