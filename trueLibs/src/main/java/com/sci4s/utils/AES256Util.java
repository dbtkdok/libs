package com.sci4s.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

/**
 * 양방향 암호화 알고리즘인 AES256 암호화를 지원하는 클래스
 */
public class AES256Util {
	private String iv;
	private Key keySpec;

	/**
	 * 16자리의 키값을 입력하여 객체를 생성한다.
	 * 
	 * @param key 암/복호화를 위한 키값
	 * @throws UnsupportedEncodingException 키값의 길이가 16이하일 경우 발생
	 */
	final static String key   = "AsdfghbvQwer1230";

	public AES256Util() throws UnsupportedEncodingException {
		this.iv = key.substring(0, 16);
		byte[] keyBytes = new byte[16];
		byte[] b = key.getBytes("UTF-8");
		int len = b.length;
		if (len > keyBytes.length) {
			len = keyBytes.length;
		}
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

		this.keySpec = keySpec;
	}	
	
	public static String encryptII(String args) {

		try {
			byte[] ivBytes = key.getBytes(StandardCharsets.UTF_8);
			
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			
			Key secretKey = new SecretKeySpec(ivBytes, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(ivBytes));
					
			byte[] src = args.getBytes(StandardCharsets.UTF_8);
			byte[] enc = cipher.doFinal(src);
			
			return Hex.encodeHexString(enc).toUpperCase();

		} catch (Exception e) {
			System.out.println("[encryptW Exception]:" + e.getMessage());
		}
		return null;
	}
	
	public String decryptII(String args) {
		try {
			byte[] ivBytes = key.getBytes(StandardCharsets.UTF_8);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

			Key secretKey = new SecretKeySpec(ivBytes, "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(ivBytes));
			
			byte[] src = Hex.decodeHex(args.toUpperCase().toCharArray());
			return new String(cipher.doFinal(src));

		} catch (Exception e) {
			System.out.println("[decryptW Exception]:" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * AES256 으로 암호화 한다.
	 * 
	 * @param str 암호화할 문자열
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 */
	public String encrypt(String str)
			throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
		byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
		String enStr = new String(Base64.encodeBase64(encrypted));
		return enStr;
	}

	/**
	 * AES256으로 암호화된 txt 를 복호화한다.
	 * 
	 * @param str 복호화할 문자열
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 */
	public String decrypt(String str)
			throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
		byte[] byteStr = Base64.decodeBase64(str.getBytes());
		return new String(c.doFinal(byteStr), "UTF-8");
	}	
	// FAB0E8B8E7AB645BA74D92FD75E4AAF5
	// fab0e8b8e7ab645ba74d92fd75e4aaf5
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StringBuilder sb  = new StringBuilder();
		try {
			AES256Util aes256 = new AES256Util();
			sb.append("!gkauddlf4010");	
			String outputOfEncrypt = aes256.encryptII(sb.toString());
			System.out.println("[CryptoSecurity.outputOfEncrypt]:" + outputOfEncrypt);
	
			//outputOfEncrypt = "F00B12105C281026960F1AB906835AB5BC60B1C6AFA9C61985AFCD8E4D507FA2B232316A367E9D3B9F7AB2881AA5A903265B295E2979C0B58A4BAC34FE08E100F7CC3F78C769BBC1781E59A0885681D3EF7A2CFB0229C51D0E01DCD3E4BC54B0";
			outputOfEncrypt = "118B41A571B59D01C5A43B54E5B47937C62DFEA8DBDE50AADCAE7D226656B2053B91498A9E28F44092D42B5F109C6650E4B7D07757E136F3EB61C3EBF3E5907D";
			String outputOfDecrypt = aes256.decryptII(outputOfEncrypt);
			System.out.println("[CryptoSecurity.outputOfDecrypt]:" + outputOfDecrypt);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}