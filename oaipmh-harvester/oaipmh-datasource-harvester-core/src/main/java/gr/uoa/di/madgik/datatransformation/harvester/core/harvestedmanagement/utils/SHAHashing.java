package gr.uoa.di.madgik.datatransformation.harvester.core.harvestedmanagement.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAHashing {

	public static String getHashedValue(String node) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(node.getBytes());
	        
	        byte byteData[] = md.digest();
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++)
	        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        return sb.toString();	
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        return null;
	}
	
}