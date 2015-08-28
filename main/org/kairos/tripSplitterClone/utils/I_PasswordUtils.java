package org.kairos.tripSplitterClone.utils;

import org.kairos.tripSplitterClone.vo.user.UserVo;

/**
 * Interface for Password utils
 * 
 * @author acollard
 * 
 */
public interface I_PasswordUtils {
	
	/**
	 * Checks potential password against parameter regExp
	 * 
	 * @param password
	 * @return
	 */
//	public Boolean formatCheck(String password);
	
	/**
	 * Check password of a user.
	 * 
	 * @param password
	 * @param userVo
	 * @param currentCost
	 * @return
	 */
	public Boolean checkPassword(String password, UserVo userVo,
	                             Long currentCost);

}
