package clabs.tools;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class StringUtils {

	private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH");
	
	public static String toCamelCase(String s){
		   String[] parts = s.split("_");
		   String camelCaseString = "";
		   if(parts.length > 1) {
			   camelCaseString = parts[0].toLowerCase();
			   for(int i=1; i<parts.length; i++) camelCaseString = camelCaseString + toProperCase(parts[i]);
			   return camelCaseString;
		   }
		   return s.toLowerCase();
		}
	
	public static synchronized String makeDeviceID() {
		String[] dt = simpleDateFormat.format(new Date()).split("-");
		String[] tmp = UUID.randomUUID().toString().split("-");
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<4; i++) sb.append(String.format("%s%s", tmp[i], dt[i]));
		sb.append(tmp[4]);
		return sb.toString();
	}
	
	public static String aes256Enc(String key, String str) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		if(key.length() < 16) key +="qnwhrgksqnqnsdmscodnjsjgrhgody";
		String iv = key.substring(0, 16);
		SecretKeySpec keySpec = aes256GetKeySpec(key);
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
		byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
		String enStr = new String(Base64.encodeBase64(encrypted));
		return enStr;
	}
	
	public static String aes256Dec(String key, String str) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		if(key.length() < 16) key +="qnwhrgksqnqnsdmscodnjsjgrhgody";
		String iv = key.substring(0, 16);
		SecretKeySpec keySpec = aes256GetKeySpec(key);
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
		byte[] byteStr = Base64.decodeBase64(str.getBytes());
		return new String(c.doFinal(byteStr), "UTF-8");
	}
	
	static SecretKeySpec aes256GetKeySpec(String key) throws UnsupportedEncodingException {
		byte[] keyBytes = new byte[16];
		byte[] b = key.getBytes("UTF-8");
		int len = b.length;
		if (len > keyBytes.length) len = keyBytes.length;
	
		System.arraycopy(b, 0, keyBytes, 0, len);
		return new SecretKeySpec(keyBytes, "AES");
	}

	static String toProperCase(String s) {
	    return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}	
	
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "")+'-'+UUID.randomUUID().toString().replace("-", "");
	}
}
