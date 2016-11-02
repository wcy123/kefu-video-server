package com.easemob.kefu.rtcmedia.util;

import java.security.GeneralSecurityException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {
	private Encryptor() {
	}

	public static String hmacSha1(String text, String secretKey) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(text.getBytes());
            return new String(Base64.getEncoder().encode(rawHmac));
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
