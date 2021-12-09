package org.itxtech.pams.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
    public static String bytesToHex(byte[] hash) {
        var hexString = new StringBuilder(2 * hash.length);
        for (var b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String checksum(String payload) throws NoSuchAlgorithmException {
        var digest = MessageDigest.getInstance("SHA3-256");
        var bytes = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(bytes);
    }
}
