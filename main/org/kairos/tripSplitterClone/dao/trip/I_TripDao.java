package org.kairos.tripSplitterClone.dao.trip;

import org.kairos.tripSplitterClone.dao.I_Dao;
import org.kairos.tripSplitterClone.vo.trip.TripVo;
import org.kairos.tripSplitterClone.vo.user.UserVo;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Interface for the Trip DAO.
 *
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 */
public interface I_TripDao extends I_Dao<TripVo> {

	/**
	 * Lists all trips from a particular user
	 */
	public List<TripVo> usersTrip(EntityManager em,UserVo userVo) throws Exception;

}
