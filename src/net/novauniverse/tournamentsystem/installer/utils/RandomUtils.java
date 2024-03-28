package net.novauniverse.tournamentsystem.installer.utils;

import java.security.SecureRandom;

public class RandomUtils {
	public static String generateHexString(int length) {
        if (length % 2 != 0) {
            throw new IllegalArgumentException("Hex string length must be even.");
        }
        
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[length / 2];
        secureRandom.nextBytes(bytes);

        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            // Convert each byte to a 2-digit hexadecimal representation
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }
}
