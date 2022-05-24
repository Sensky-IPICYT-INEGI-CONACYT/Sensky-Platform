package com.youilab.senskywebapp.util;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Some Utils for managing hashing and formats of Strings.
 */
public class Encoders {

    /**
     * Converts an input string into a hashed string using the algorithm SHA256.
     * @param word The input String.
     * @return The hashed String.
     */
    public static String toSHA256(String word) {
        return Hashing.sha256()
                .hashString(word, StandardCharsets.UTF_8)
                .toString();
    }

    /**
     * Converts an input string encoded in Base64 to a human-readable string.
     * @param raw The string in Base64.
     * @return The Human-readable representation.
     */
    public static String fromBase64(String raw) {
        byte[] decodedBytes = Base64.getDecoder().decode(raw.getBytes());
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    /**
     * Converts an input string to a Base64 format.
     * @param string The input string.
     * @return The Base64 representation of the input.
     */
    public static String toBase64(String string){
        byte[] encodedBytes = Base64.getEncoder().encode(string.getBytes());
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }
}
