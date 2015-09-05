package org.kairos.tripSplitterClone.utils;

import javax.persistence.EntityManager;

import org.kairos.tripSplitterClone.dao.EntityManagerHolder;
import org.kairos.tripSplitterClone.vo.user.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Password utils.
 * 
 * This currently uses BCrypt to store hashed passwords.
 * 
 * The cost (log rounds) is retrieved from a parameter (that MUST exist on the
 * database).
 * 
 * BCrypt hash help to improve security on brute force attacks (in the case that
 * the server is penetrated and the passwords hashes retrieved from the
 * database).
 *
 * Created on 8/27/15 by
 *
 * @author AxelCollardBovy.
 * 
 */
public class PasswordUtils implements I_PasswordUtils {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(PasswordUtils.class);

	/**
	 * Entity Manager Holder
	 */
	@Autowired
	private EntityManagerHolder emh;

	/**
	 * Format check of a potential password. Retrieves the regExp from the
	 * parameter to check the password received
	 * 
	 * @return
	 */
//	@Override
//	public Boolean formatCheck(String password) {
//		EntityManager em = this.getEmh().getEntityManager();
//
//		try {
//			ParameterVo passwordRegexpParameter = this.getParameterDao()
//					.getByName(em, ParameterVo.PASSWORD_REGEXP);
//			String[] regExpList = passwordRegexpParameter.getValue().split(",");
//
//			for (String regexp : regExpList) {org.kairos.tripSplitterClone.utils
//				if (!password.matches(regexp)) {org.kairos.tripSplitterClone.utils
//					return Boolean.TRUE;
//				}
//			}
//			return Boolean.FALSE;
//		} catch (Exception e) {
//			this.logger.debug("error getting {} parameter", e,
//					ParameterVo.PASSWORD_REGEXP);
//
//			return Boolean.FALSE;
//		} finally {
//			this.getEmh().closeEntityManager(em);
//		}
//	}

	/**
	 * Checks password of a user.
	 * 
	 * @param password
	 *            password to check
	 * @param userVo
	 *            userVo
	 * 
	 * @return true iif
	 */
	@Override
	public Boolean checkPassword(String password, UserVo userVo,
			Long currentCost) {
		Boolean result = Boolean.FALSE;
		Boolean update = Boolean.FALSE;

		if (userVo.getHashCost() == null) {
			// old password (hashed with SHA-512), marks it to update
			result = HashUtils.hashPasswordSHA512(password).equals(
					userVo.getPassword());
			update = Boolean.TRUE;
		} else {
			// the password check doesn't need the cost (is stored in the hash
			// itself)
			result = HashUtils.checkPassword(password, userVo.getPassword());
			// if the costs are different the password needs to be updated
			update = !userVo.getHashCost().equals(currentCost);
		}

		if (result && update) {
			// only updates if is needed AND the check was successful
			userVo.setHashCost(currentCost);
			userVo.setPassword(HashUtils.hashPassword(password, currentCost));
			// note: this updates occurs only in the VO (i.e., in memory)
			// the actual update on the database depends on the use given
			// to the VO after this update is made (e.g., in Fx_Login, the VO
			// is persisted after a successful login, thus, this changes are
			// made on the database as well)
		}

		return result;
	}

	/**
	 * @return the emh
	 */
	public EntityManagerHolder getEmh() {
		return this.emh;
	}

	/**
	 * @param emh
	 *            the emh to set
	 */
	public void setEmh(EntityManagerHolder emh) {
		this.emh = emh;
	}

}
