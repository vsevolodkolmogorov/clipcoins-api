package com.clipcoins.api.utils;

import java.security.SecureRandom;

public class TokenGenerator {
    private static final int DEFAULT_LENGTH = 12;
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String CHAR_DIGITS = "0123456789";
    private static final String CHAR_SPECIAL = "!@#$%^&*()-_+=<>?";
    private static final String CODE_ALLOW = CHAR_LOWER + CHAR_UPPER + CHAR_DIGITS + CHAR_SPECIAL;

    private static final SecureRandom random = new SecureRandom();

    public static String generateToken() {
        StringBuilder token = new StringBuilder(DEFAULT_LENGTH);

        token.append(CHAR_LOWER.charAt(random.nextInt(CHAR_LOWER.length())));
        token.append(CHAR_UPPER.charAt(random.nextInt(CHAR_UPPER.length())));
        token.append(CHAR_DIGITS.charAt(random.nextInt(CHAR_DIGITS.length())));
        token.append(CHAR_SPECIAL.charAt(random.nextInt(CHAR_SPECIAL.length())));

        for (int i = 4; i < DEFAULT_LENGTH; i++) {
            token.append(CODE_ALLOW.charAt(random.nextInt(CODE_ALLOW.length())));
        }

        return shuffleString(token.toString());
    }

    private static String  shuffleString(String input) {
        char[] chars = input.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            int index = random.nextInt(chars.length);
            char temp = chars[index];
            chars[index] = chars[i];
            chars[i] = temp;
        }

        return new String(chars);
    }
}
