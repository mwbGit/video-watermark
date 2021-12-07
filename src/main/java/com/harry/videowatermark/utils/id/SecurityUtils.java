package com.harry.videowatermark.utils.id;

import cn.hutool.core.codec.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

/**
 * 描述:
 *
 * @author mengweibo@kanzhun.com
 * @create 2021/9/2
 */

public class SecurityUtils {
    private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String RC4_DEFAULT_PASSWORD = "3984aF333#@213";
    private static final String[] hexDigits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    private static final char[] c = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz".toCharArray();

    public SecurityUtils() {
    }

    private static String getSecretKey(String sourceSecretKey) {
        String md5secretKey = MD5Encode(sourceSecretKey);
        return md5secretKey.substring(md5secretKey.length() - 8);
    }

    public static String MD5Encode(String str) {
        if (str == null) {
            return null;
        } else {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                return byteArrayToHexString(md.digest(str.getBytes()));
            } catch (NoSuchAlgorithmException var3) {
                return null;
            }
        }
    }

    public static String MD5Encode(String str, String encoding) {
        if (str == null) {
            return null;
        } else {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                return byteArrayToHexString(md.digest(str.getBytes(encoding)));
            } catch (Exception var4) {
                return null;
            }
        }
    }

    public static String MD5Encode(InputStream input) {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            byte[] b = new byte[10240];
            boolean var3 = true;

            int length;
            while((length = input.read(b)) > -1) {
                messagedigest.update(b, 0, length);
            }

            String var4 = bufferToHex(messagedigest.digest());
            return var4;
        } catch (Exception var13) {
            var13.printStackTrace();
            throw new RuntimeException("InputStream md5 name error");
        } finally {
            if (null != input) {
                try {
                    input.close();
                } catch (IOException var12) {
                    var12.printStackTrace();
                    throw new RuntimeException("InputStream md5 name close error");
                }
            }

        }
    }

    public static String sha1Encrypt(String str) {
        String result = "";

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuffer hexString = new StringBuffer();

            for(int i = 0; i < messageDigest.length; ++i) {
                String shaHex = Integer.toHexString(messageDigest[i] & 255);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }

                hexString.append(shaHex);
            }

            result = hexString.toString();
            return result;
        } catch (NoSuchAlgorithmException var7) {
            return result;
        }
    }

    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;

        for(int l = m; l < k; ++l) {
            appendHexPair(bytes[l], stringbuffer);
        }

        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        String c0 = hexDigits[(bt & 240) >> 4];
        String c1 = hexDigits[bt & 15];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder(b.length * 2);

        for(int i = 0; i < b.length; ++i) {
            resultSb.append(byteToHexString(b[i]));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (b < 0) {
            n = 256 + b;
        }

        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String getRandomString(int len) {
        if (len < 1) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();
            Random random = new Random();

            for(int i = 0; i < len; ++i) {
                sb.append(c[random.nextInt(c.length)]);
            }

            return sb.toString();
        }
    }

    public static String UUID() {
        return UUID.randomUUID().toString();
    }

    /** @deprecated */
    public static String encryptRC4(Object source) {
        return String.valueOf(source);
    }

    public static String rc4Encrypt(Object source) {
        return rc4Encrypt(source, "3984aF333#@213");
    }

    /** @deprecated */
    @Deprecated
    public static String rc4EncryptOld(Object source, String rc4Password) {
        String encrypt = "";
        if (StringUtils.isBlank(rc4Password)) {
            return encrypt;
        } else {
            try {
                encrypt = Base64.encode(RC4.RC4encrypt(rc4Password.getBytes("UTF-8"), String.valueOf(source).getBytes("UTF-8")));
            } catch (UnsupportedEncodingException var4) {
                logger.error("SecurityUtils.rc4Encrypt(" + source + ", " + rc4Password + ")", var4);
            }

            return encrypt.replaceAll("/", "_").replaceAll("\\+", "-").replaceAll("=", "~");
        }
    }

    public static String rc4Encrypt(Object source, String rc4Password) {
        if (StringUtils.isBlank(rc4Password)) {
            return "";
        } else {
            try {
                byte[] key = rc4Password.getBytes("UTF-8");
                byte[] bytes = String.valueOf(source).getBytes("UTF-8");
                byte[] encryptBytes = RC4.RC4encrypt(key, bytes, bytes.length);
                return URLSafeBase64.encode(encryptBytes, encryptBytes.length);
            } catch (UnsupportedEncodingException var5) {
                logger.error("SecurityUtils.rc4Encrypt(" + source + ", " + rc4Password + ")", var5);
                return "";
            }
        }
    }

    public static String rc4Decrypt(String encryptStr) {
        return rc4Decrypt(encryptStr, "3984aF333#@213");
    }

    /** @deprecated */
    @Deprecated
    public static String rc4DecryptOld(String encryptStr, String rc4Password) {
        String decrypt = "";

        try {
            if (StringUtils.isBlank(rc4Password)) {
                return decrypt;
            }

            encryptStr = encryptStr.replaceAll("_", "/").replaceAll("-", "+").replaceAll("\\~", "=");
            byte[] keyBytes = rc4Password.getBytes("UTF-8");
            byte[] plainDataBytes = Base64.decode(encryptStr);
            if (plainDataBytes == null) {
                return decrypt;
            }

            byte[] bytes = RC4.RC4encrypt(keyBytes, plainDataBytes);
            decrypt = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException var6) {
            logger.error("SecurityUtils.rc4Decrypt(" + encryptStr + ", " + rc4Password + ")", var6);
        }

        return decrypt;
    }

    public static String rc4Decrypt(String encryptStr, String rc4Password) {
        try {
            if (StringUtils.isBlank(rc4Password)) {
                return "";
            } else {
                encryptStr = preprocess4Rc4Decrypt(encryptStr);
                byte[] keyBytes = rc4Password.getBytes("UTF-8");
                byte[] plainDataBytes = Base64.decode(encryptStr);
                if (plainDataBytes == null) {
                    return "";
                } else {
                    byte[] bytes = RC4.RC4encrypt(keyBytes, plainDataBytes, plainDataBytes.length);
                    return new String(bytes, "UTF-8");
                }
            }
        } catch (UnsupportedEncodingException var5) {
            logger.error("SecurityUtils.rc4Decrypt(" + encryptStr + ", " + rc4Password + ")", var5);
            return "";
        }
    }

    private static String preprocess4Rc4Decrypt(String encrypt) {
        char[] chars = new char[encrypt.length()];

        for(int i = 0; i < encrypt.length(); ++i) {
            char ch = encrypt.charAt(i);
            if (ch == '_') {
                chars[i] = '/';
            } else if (ch == '-') {
                chars[i] = '+';
            } else if (ch == '~') {
                chars[i] = '=';
            } else {
                chars[i] = ch;
            }
        }

        return new String(chars);
    }

    public static String rc4EncryptUseBase24(String source, String rc4Password) {
        return Base24.encode(rc4Encrypt(source, rc4Password));
    }

    public static String rc4DecryptUseBase24(String encryptStr, String rc4Password) {
        String decodeStr = Base24.decode(encryptStr);
        return StringUtils.isNotEmpty(decodeStr) ? rc4Decrypt(decodeStr, rc4Password) : "";
    }

    public static void main(String[] args) {
        String str = "1234";
        String pass = "1234";
        String strAfterEncode = rc4EncryptUseBase24(str, pass);
        System.out.println("strAfterEncode:" + strAfterEncode);
        String strAfterDecode = rc4DecryptUseBase24(strAfterEncode, pass);
        System.out.println("strAfterDecode:" + strAfterDecode);
        System.out.println(rc4Decrypt("-VfE6srIcsUtHxHvkHxTwgrPoD0siYqOFzLJYjHTpv49E3dbDcFHnZqzxZc2PBUTfDEfRObGe5ysbV0NVH69QGwLwgQcMcPGHSufXUlry0HbH3FXwgtxPSDfXddorCaOQcIVARlhkmPFnxpI8A~~", "1008"));
        System.out.println(rc4Decrypt("nw~~", "24274"));
        System.out.println(MD5Encode("articleId=5066133clientId=1000167df59e39e9b46419op106fa2583a7b9", "UTF-8"));
        String result = rc4Encrypt("pass", "pswp");
        System.out.println(result);
        System.out.println("rc4Decrypt->" + rc4Decrypt(result, "pswp"));
    }
}
