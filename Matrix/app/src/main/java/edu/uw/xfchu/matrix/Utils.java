package edu.uw.xfchu.matrix;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.Charset;
import java.security.MessageDigest;

public class Utils {
    /**
     * Md5 encryption, encode string
     * @param input the string to be encoded
     * @return encoded string
     */
    public static String md5Encryption(final String input) {
        String result = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(input.getBytes(Charset.forName("UTF8")));
            byte[] resultByte = messageDigest.digest();
            result = new String(Hex.encodeHex(resultByte));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
