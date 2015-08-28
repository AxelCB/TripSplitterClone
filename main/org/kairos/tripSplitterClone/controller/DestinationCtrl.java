package org.kairos.tripSplitterClone.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created on 8/23/15 by
 *
 * @author AxelCollardBovy.
 */
@RequestMapping(value = "/destination", produces = "text/json;charset=utf-8", method = RequestMethod.POST)
public class DestinationCtrl {

	/**
	 * Creates a city.
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/createCity.json")
	public String createCity(@RequestBody String data){

		//TODO: implement this
		return null;
	}

	/**
	 * Creates a country.
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/createCountry.json")
	public String createCountry(@RequestBody String data){

		//TODO: implement this
		return null;
	}

	/**
	 * Lists all destinations.
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listDestinations.json")
	public String listDestinations(@RequestBody String data){
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
