package com.example.ai_practice_bot.util;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtils {
    public static String sha256(String content) {
        return DigestUtils.sha256Hex(content);
    }
}
