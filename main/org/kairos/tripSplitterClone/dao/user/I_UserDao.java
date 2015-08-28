package org.kairos.tripSplitterClone.dao.user;

import org.kairos.tripSplitterClone.dao.I_Dao;
import org.kairos.tripSplitterClone.vo.user.UserVo;

import javax.persistence.EntityManager;

/**
 * Interface for the User DAO.
 *
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 */
public interface I_UserDao extends I_Dao<UserVo> {

	/**
	 * Attempts to search a user with username.
	 *
	 * @param em
	 *            the entity manager
	 * @param username
	 *            the username to search for
	 *
	 * @return userVo or null
	 */
	public UserVo getByUsername(EntityManager em, String username);


	/**
	 * Checks that a user username is only used once.
	 *
	 * @param em
	 *            the entity manager
	 * @param username
	 *            the username to check
	 * @param excludeId
	 *            the id to exclude
	 *
	 * @return true if the code is unique
	 */
	public Boolean checkUsernameUniqueness(EntityManager em, String username,Long excludeId);
}
