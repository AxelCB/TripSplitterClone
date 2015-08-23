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
@RequestMapping(value = "/trip", produces = "text/json;charset=utf-8", method = RequestMethod.POST)
public class TripCtrl {

	/**
	 * Creates a trip.
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/create.json")
	public String create(@RequestBody String data){

		//TODO: implement this
		return null;
	}

	/**
	 * Adds a participant in a trip.
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addParticipant.json")
	public String addParticipant(){
		//TODO: implement this
		return null;
	}

	/**
	 * Delets a trip.
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete.json")
	public String delete(@RequestBody String data){
		//TODO: implement this
		return null;
	}

}
