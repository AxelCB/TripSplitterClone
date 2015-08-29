package org.kairos.tripSplitterClone.dao.destination;

import org.kairos.tripSplitterClone.dao.I_Dao;
import org.kairos.tripSplitterClone.vo.destination.CityVo;
import org.kairos.tripSplitterClone.vo.destination.CountryVo;

import javax.persistence.EntityManager;

/**
 * Interface for the Country DAO.
 *
 * Created on 8/23/15 by
 *
 * @author AxelCollardBovy.
 */
public interface I_CityDao extends I_Dao<CityVo> {

	/**
	 * Finds a country by its name.
	 *
	 * @param em
	 *            the entity manager
	 * @param name
	 *            the name to search for
	 *
	 * @return ZoneVo or null
	 *
	 */
	public CountryVo findByName(EntityManager em, String name);

	/**
	 * Checks that a country name is only used once.
	 *
	 * @param em
	 *            the entity manager
	 * @param name
	 *            the name to check
	 * @param excludeId
	 *            the id to exclude
	 *
	 * @return true if the code is unique
	 */
	public Boolean checkNameUniqueness(EntityManager em, String name,
	                                   Long excludeId);
}
