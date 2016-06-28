package utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Ported from Magento Password Hash Functions
 * 
 * @author kmtong
 *
 */
public class CryptoUtils {

	public static String getRandomString(int len) {
		return RandomStringUtils.randomAlphanumeric(len);
	}

	public static String getHash(String password, int salt) {
		String saltStr = null;
		if (salt > 0) {
			saltStr = getRandomString(salt);
		}
		return getHashWithSalt(password, saltStr);
	}

	public static String getHashWithSalt(String password, String saltStr) {
		return (saltStr == null) ? md5(password) : md5(saltStr + password)
				+ ':' + saltStr;
	}

	public static boolean validateHash(String password, String hash) {
		String[] parts = hash.split(":");
		String salt = (parts.length == 2) ? parts[1] : null;
		return getHashWithSalt(password, salt).equals(hash);
	}

	public static String randomHash() {
		return md5(getRandomString(80));
	}

	public static String md5(String value) {
		try {
			String md5 = Hex.encodeHexString(MessageDigest.getInstance("MD5")
					.digest(value.getBytes("UTF-8")));
			return md5;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Should not happen", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Should not happen", e);
		}
	}
}
