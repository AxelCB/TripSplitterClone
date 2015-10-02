package org.kairos.tripSplitterClone.tests;

import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.Test;

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
public class UserModelTest extends AbstractTestNGSpringContextTests{

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(UserModelTest.class);

	private UserVo unregisteredUser;

	private UserVo registeredUser;


	@Test(groups = {"user"})
	public void registerUserTest(){
		unregisteredUser = new UserVo("juliangrigera@mail.com","123456","Julian Grigera");
		String validateResponse = unregisteredUser.validate();
		assert(validateResponse!=null):validateResponse;
	}

	@Test(groups = {"user"},dependsOnMethods = {"registerUserTest"})
	public void loginUser() {
		//Un user tiene que saber como loguearse al sistema? raro
	}

}
