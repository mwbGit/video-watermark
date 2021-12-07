package com.harry.videowatermark.utils;

import com.harry.videowatermark.utils.id.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述:
 *
 * @author mengweibo@kanzhun.com
 * @create 2021/9/2
 */
public class IdSecurityUtils {
    private static final Logger logger = LoggerFactory.getLogger(IdSecurityUtils.class);

    public IdSecurityUtils() {
    }

    public static String encryptString(String plainText) {
        try {
            String encryptString = SecurityUtils.rc4Encrypt(plainText);
            encryptString = StringUtils.substring(MD5Util.md5(encryptString), 8, 24) + encryptString;
            return encryptString;
        } catch (Throwable var2) {
            logger.error("encryptString[" + plainText + "]", var2);
            return null;
        }
    }

    public static String encryptString(String plainText, String password) {
        try {
            String encryptString = SecurityUtils.rc4Encrypt(plainText, password);
            encryptString = StringUtils.substring(MD5Util.md5(encryptString), 8, 24) + encryptString;
            return encryptString;
        } catch (Throwable var3) {
            logger.error("encryptString[" + plainText + "]", var3);
            return null;
        }
    }

    public static String decryptString(String encryptString) {
        if (StringUtils.isBlank(encryptString)) {
            return encryptString;
        } else {
            try {
                String sum = StringUtils.substring(encryptString, 0, 16);
                encryptString = StringUtils.substring(encryptString, 16);
                String plainText = SecurityUtils.rc4Decrypt(encryptString);
                if (StringUtils.isNotBlank(plainText) && StringUtils.equalsIgnoreCase(StringUtils.substring(MD5Util.md5(encryptString), 8, 24), sum)) {
                    return plainText;
                }
            } catch (Throwable var3) {
                logger.warn("decryptString[" + encryptString + "]", var3);
            }

            return null;
        }
    }

    public static String decryptString(String encryptString, String password) {
        if (StringUtils.isBlank(encryptString)) {
            return encryptString;
        } else {
            try {
                String sum = StringUtils.substring(encryptString, 0, 16);
                encryptString = StringUtils.substring(encryptString, 16);
                String plainText = SecurityUtils.rc4Decrypt(encryptString, password);
                if (StringUtils.isNotBlank(plainText) && StringUtils.equalsIgnoreCase(StringUtils.substring(MD5Util.md5(encryptString), 8, 24), sum)) {
                    return plainText;
                }
            } catch (Throwable var4) {
                logger.warn("decryptString[" + encryptString + "]", var4);
            }

            return null;
        }
    }

    public static String encryptId(long id) {
        return encryptString(String.valueOf(id));
    }

    public static long decryptId(String encryptId) {
        return NumberUtils.toLong(decryptString(encryptId));
    }

    public static String encryptId(long id, String password) {
        return encryptString(String.valueOf(id), password);
    }

    public static long decryptId(String encryptId, String password) {
        return NumberUtils.toLong(decryptString(encryptId, password));
    }

    public static void main(String[] args) {
        System.out.println(encryptId(3L));
        System.out.println(decryptString("f2b441dc736f8db41A~~"));
        System.out.println(decryptId("b53fedc1816271a4TwCfcwp4rA~~", "11931545"));
    }
}
