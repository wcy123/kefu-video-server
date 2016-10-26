package com.easemob.kefu.video.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class Encryptor {
	public static String HMACSHA1(String text, String secretKey) {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
			
			//Mac mac = Mac.getInstance("HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(signingKey);
			
			byte[] rawHmac = mac.doFinal(text.getBytes());
			//return new String(Base64.getEncoder().encodeToString(rawHmac));
			return new String(Base64.getEncoder().encode(rawHmac));
		} catch (GeneralSecurityException e) {
			throw new IllegalArgumentException();
		}
	}

	public static String MD5(String value) {
		try {
			return Encryptor.encrypt(value, "MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String encrypt(String value, String method) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(method);
		md.update(value.getBytes());
		byte[] digest = md.digest();

		return decodeHexStr(digest, 0, digest.length);
	}

	public static String decodeHexStr(byte[] buf, int pos, int len) {
		StringBuilder hex = new StringBuilder();
		while (len-- > 0) {
			byte ch = buf[(pos++)];
			int d = ch >> 4 & 0xF;
			hex.append((char) (d >= 10 ? 87 + d : 48 + d));
			d = ch & 0xF;
			hex.append((char) (d >= 10 ? 87 + d : 48 + d));
		}
		return hex.toString();
	}
}