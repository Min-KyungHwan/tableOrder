package com.mkh.tableOrder.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {
    private final static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    /**
     * SHA256 암호화
     * @param value
     * @return
     */
    public static String encryptSHA256(String value) {
        String encryptData = "";

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(value.getBytes());

            byte[] digest = sha.digest();
            for (int i=0; i<digest.length; i++) {
                encryptData += Integer.toHexString(digest[i] & 0xFF).toUpperCase();
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error("encryptSHA256() exception.....", e);
        }

        return encryptData;
    }

    /**
     * MD5 암호화
     * @param strData
     * @return
     */
    public static String MD5(String strData) {
        String strENCData = "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] bytData = strData.trim().getBytes();
            md.update(bytData);
            byte[] digest = md.digest();

            for (int i = 0; i < digest.length; i++) {
                strENCData = strENCData + Integer.toHexString(digest[i] & 0xFF).toUpperCase();
            }
        } catch (Exception e) {
            logger.error("MD5() exception.....", e);
        }
        return strENCData;
    }
    
    /**
     * AES256 암호화
     * @param strData
     * @return
     */
	public static byte[] encyptAES256( String content, String key ) throws Exception {
		if( content == null || content.length() <= 0 || key == null || key.length() <= 0 ) return null;

		SecretKeySpec skeySpec = new SecretKeySpec( key.getBytes(), "AES" );
		Cipher cipher = Cipher.getInstance( "AES" );
		cipher.init( Cipher.ENCRYPT_MODE, skeySpec );
		
        return cipher.doFinal( content.getBytes() );
	}

	public static String decyptAES256( byte[] content, String key ) throws Exception {
		if( content == null || content.length <= 0 || key == null || key.length() <= 0 ) return null;

		SecretKeySpec skeySpec = new SecretKeySpec( key.getBytes(), "AES" );
        Cipher cipher = Cipher.getInstance( "AES" );
        cipher.init( Cipher.DECRYPT_MODE, skeySpec );

        return new String( cipher.doFinal(content) );
	}
}
