package com.dobi.jiecon;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;


public class MyCrypto {
//	private static final String ALGORITHM = "AES/ECB/PKCS7Padding";
//	private static final byte[] keyValue = new byte[]{'A','b','c', 'D', 'e', 'f', 'G', 'h', 'I', 'L', 'm', 'n','o', 'P','Q','r'};

	public MyCrypto() {
		UtilLog.logD("MyCrypto construction");
	}
	/**
	 * Encodes a String in AES-256 with a given key
	 *
	 * @param context
	 * @param password
	 * @param text
	 * @return String Base64 and AES encoded String
	 */
	public String encode(String keyString, String stringToEncode) {
	    if (keyString.length() == 0 || keyString == null) {
	        throw new NullPointerException("Please give Password");
	    }
	    
	    if (stringToEncode.length() == 0 || stringToEncode == null) {
	        throw new NullPointerException("Please give text");
	    }
	    
	    try {
	        SecretKeySpec skeySpec = getKey(keyString);
	        byte[] clearText = stringToEncode.getBytes("UTF8");
	        
	        // IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
	        final byte[] iv = new byte[16];
	        Arrays.fill(iv, (byte) 0x00);
	        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
	        
	        // Cipher is not thread safe
	        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
	        
//	        String encrypedValue = Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);
	        String encrypedValue = Base64.encode(cipher.doFinal(clearText), Base64.DEFAULT).toString();
	        UtilLog.logD("Encrypted: " + stringToEncode + " -> " + encrypedValue);
	        return encrypedValue;
	        
	    } catch (InvalidKeyException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (BadPaddingException e) {
	        e.printStackTrace();
	    } catch (NoSuchPaddingException e) {
	        e.printStackTrace();
	    } catch (IllegalBlockSizeException e) {
	        e.printStackTrace();
	    } catch (InvalidAlgorithmParameterException e) {
	        e.printStackTrace();
	    }
	    return "";
	}

	/**
	 * Decodes a String using AES-256 and Base64
	 *
	 * @param context
	 * @param password
	 * @param text
	 * @return desoded String
	 */
	public String decode(String password, String text, int leng) {
	    UtilLog.logD("cry 0000");
	    if (password.length() == 0 || password == null) {
//	        throw new NullPointerException("Please give Password");
	    	return "11111";
	    }
	    
	    if (text.length() == 0 || text == null) {
//	        throw new NullPointerException("Please give text");
	    	return "22222";
	    }
	    
	    try {
	    	UtilLog.logD("cryxx 1111");
	        SecretKey key = getKey(password);
	        
	        UtilLog.logD("cryxx 222");
	        // IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
	        final byte[] iv = new byte[16];
	        Arrays.fill(iv, (byte) 0x00);
	        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
	        
	        UtilLog.logD("crxxy 3333");
//	        byte[] encrypedPwdBytes = Base64.decode(text);
	        byte[] encrypedPwdBytes = Base64.decode(text, Base64.DEFAULT);
	        // cipher is not thread safe
	        UtilLog.logD("cryxxx 4444 encrypedPwdBytes = " + encrypedPwdBytes.toString());
	        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
//	        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
	        cipher.init(Cipher.DECRYPT_MODE, key);
	        
	        UtilLog.logD("cryxxx 5555");
	        byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));
	        
	        UtilLog.logD("cryxxx 6666 " + decrypedValueBytes.toString());
	        
	        String decrypedValue = new String(decrypedValueBytes);
	        
	        UtilLog.logD("before Decrypted: " + text + " -> " + decrypedValue);
//	        decrypedValue = decrypedValue.substring(0, 20);
	        decrypedValue = decrypedValue.substring(0, leng);
	        
	        UtilLog.logD("Decrypted: " + text + " -> " + decrypedValue);
	        return decrypedValue;
	        
	    } catch (InvalidKeyException e) {
	        e.printStackTrace();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (BadPaddingException e) {
	        e.printStackTrace();
	    } catch (NoSuchPaddingException e) {
	        e.printStackTrace();
	    } catch (IllegalBlockSizeException e) {
	        e.printStackTrace();
//	    } catch (InvalidAlgorithmParameterException e) {
//	        e.printStackTrace();
	    } 
	    return "";
	}

	/**
	 * Generates a SecretKeySpec for given password
	 *
	 * @param password
	 * @return SecretKeySpec
	 * @throws UnsupportedEncodingException
	 */
	private SecretKeySpec getKey(String password) {
	    try {
		    // You can change it to 128 if you wish
		    int keyLength = 256;
		    byte[] keyBytes = new byte[keyLength / 8];
		    // explicitly fill with zeros
		    Arrays.fill(keyBytes, (byte) 0x0);
		    
		    // if password is shorter then key length, it will be zero-padded
		    // to key length
		    byte[] passwordBytes;

			passwordBytes = password.getBytes("UTF-8");
		    int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
		    System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
		    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		    return key;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
	    
	    return null;
	}
}
