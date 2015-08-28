package org.kairos.tripSplitterClone.utils;

import java.util.Calendar;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.kairos.tripSplitterClone.vo.user.UserVo;

/**
 * Utils Class for Managing Password Hashes
 * 
 * @author fgonzalez
 * 
 */
public class HashUtils {

	/**
	 * Hashes the password. Old and discouraged method (using SHA-512).
	 * 
	 * @param password
	 *            to hash
	 * 
	 * @return hashed password as a hex-string
	 */
	public static String hashPasswordSHA512(String password) {
		return DigestUtils.sha512Hex(password);
	}

	/**
	 * Hashes the password. Default and preferred method (using BCrypt).
	 * 
	 * @param password
	 *            to hash
	 * @param cost
	 *            of the hash
	 * 
	 * @return hashed password as a hex-string
	 */
	public static String hashPassword(String password, Long cost) {
		// we use the BCrypt algorithm
		return BCrypt.hashpw(password, BCrypt.gensalt(cost.intValue()));
	}

	/**
	 * Checks a password. Default and preferred method (using BCrypt).
	 * 
	 * @param password
	 *            plain text password
	 * @param hashed
	 *            hashed password
	 * 
	 * @return
	 */
	public static Boolean checkPassword(String password, String hashed) {
		return BCrypt.checkpw(password, hashed);
	}

	/**
	 * Generates a new token for the logged user.
	 * 
	 * @param userVo
	 *            the user to generate the token to
	 * 
	 * @return the token as a hex-string hash
	 */
	public static String generateUserToken(UserVo userVo) {
		return DigestUtils.sha512Hex(((Long) Calendar.getInstance()
				.getTimeInMillis()).toString());
	}

	/**
	 * Generates a new random alphanumeric password of length 'length'.
	 * 
	 * @param length
	 *            the length of the generated password
	 * @param onlyNumeric
	 *            if the password must be only numeric
	 * @Param allLowerCase if the password must be all in lowercase
	 * 
	 * @return alphanumeric string
	 */
	public static String generateRandomPassword(Integer length,
			Boolean onlyNumeric, Boolean allLowerCase) {
		String newPassword = "";
		if (onlyNumeric) {
			newPassword = RandomStringUtils.randomNumeric(length.intValue());
		} else {
			newPassword = RandomStringUtils.randomAlphanumeric(length.intValue());
		}
		
		if (allLowerCase) {
			newPassword = newPassword.toLowerCase();
		}
		
		return newPassword;
	}

	/**
	 * Only used for generating password to set on the DB.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// last argument interpreted as the hash cost
		Long cost = Long.parseLong(args[args.length - 1]);

		for (int i = 0; i < (args.length - 1); i++) {
			System.out.println("Hashing... - " + args[i] + " - Cost: " + cost
					+ " - " + hashPassword(args[i], cost));
		}
	}

}
