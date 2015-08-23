package org.kairos.tripSplitterClone.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 */
@RequestMapping(value = "/user", produces = "text/json;charset=utf-8", method = RequestMethod.POST)
public class UserCtrl {

	/**
	 * Registers a user.
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/register.json")
	public String register(@RequestBody String data){
		//TODO: implement this
		return null;
	}

	/**
	 * Login a user.
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/login.json")
	public String login(@RequestBody String data){
		//TODO: implement this
		return null;
	}

}
