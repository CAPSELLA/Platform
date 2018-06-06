package gr.uoa.di.madgik.config;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;

public class Hash {
	
	public static String hashMD5Password(final String newPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	    MessageDigest digest = MessageDigest.getInstance("MD5");
	    digest.update(newPassword.getBytes("UTF8"));
	    String md5Password = new String(Base64.encodeBase64(digest.digest()));
	    return "{MD5}" + md5Password;
	  }

}
