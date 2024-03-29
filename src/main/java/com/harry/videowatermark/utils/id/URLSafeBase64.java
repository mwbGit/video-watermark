package com.harry.videowatermark.utils.id;

/**
 * 描述:
 *
 * @author mengweibo@kanzhun.com
 * @create 2021/9/2
 */

public final class URLSafeBase64 {

    private static final int BASE_LENGTH = 128;
    private static final int LOOKUP_LENGTH = 64;
    private static final int TWENTY_FOUR_BIT_GROUP = 24;
    private static final int FOUR_BYTE = 4;
    private static final int EIGHT_BIT = 8;
    private static final int SIXTEEN_BIT = 16;
    private static final int SIGN = -128;
    static private final char PAD = '~';//性能优化，直接使用 '~'； 而不是 '=' 然后再替换为 '~'
    private static final byte[] base64Alphabet = new byte[BASE_LENGTH];
    private static final char[] lookUpBase64Alphabet = new char[LOOKUP_LENGTH];

    static {
        for (int i = 0; i < BASE_LENGTH; ++i) {
            base64Alphabet[i] = -1;
        }

        for (int i = 'Z'; i >= 'A'; i--) {
            base64Alphabet[i] = (byte) (i - 'A');
        }

        for (int i = 'z'; i >= 'a'; i--) {
            base64Alphabet[i] = (byte) (i - 'a' + 26);
        }

        for (int i = '9'; i >= '0'; i--) {
            base64Alphabet[i] = (byte) (i - '0' + 52);
        }

        base64Alphabet['-'] = 62;
        base64Alphabet['_'] = 63;

        for (int i = 0; i <= 25; i++) {
            lookUpBase64Alphabet[i] = (char) ('A' + i);
        }

        for (int i = 26, j = 0; i <= 51; i++, j++) {
            lookUpBase64Alphabet[i] = (char) ('a' + j);
        }

        for (int i = 52, j = 0; i <= 61; i++, j++) {
            lookUpBase64Alphabet[i] = (char) ('0' + j);
        }
        lookUpBase64Alphabet[62] = '+';
        lookUpBase64Alphabet[63] = '/';

        //直接修改 lookUpBase64Alphabet 中的内容，减少 Base64 encode 后一次循环！！！
        for (int i = 0; i < lookUpBase64Alphabet.length; ++i) {
            char ch = lookUpBase64Alphabet[i];
            if (ch == '/') {
                lookUpBase64Alphabet[i] = '_';
            } else if (ch == '+') {
                lookUpBase64Alphabet[i] = '-';
            } else if (ch == '=') {
                lookUpBase64Alphabet[i] = '~';
            }
        }
    }

    /**
     * Encodes hex octects into URL safe Base64
     *
     * @param binaryData Array containing binaryData
     * @return Encoded Base64 array
     */
    public static String encode(byte[] binaryData, int length) {
        if (binaryData == null) {
            return null;
        }

        int lengthDataBits = length * EIGHT_BIT;
        if (lengthDataBits == 0) {
            return "";
        }

        int fewerThan24bits = lengthDataBits % TWENTY_FOUR_BIT_GROUP;
        int numberTriplets = lengthDataBits / TWENTY_FOUR_BIT_GROUP;
        int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1 : numberTriplets;
        char[] encodedData = new char[numberQuartet * 4];

        byte k, l, b1, b2, b3;
        int encodedIndex = 0;
        int dataIndex = 0;
        for (int i = 0; i < numberTriplets; i++) {
            b1 = binaryData[dataIndex++];
            b2 = binaryData[dataIndex++];
            b3 = binaryData[dataIndex++];

            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);
            byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6) : (byte) ((b3) >> 6 ^ 0xfc);

            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[(l << 2) | val3];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[b3 & 0x3f];
        }

        // form integral number of 6-bit groups
        if (fewerThan24bits == EIGHT_BIT) {
            b1 = binaryData[dataIndex];
            k = (byte) (b1 & 0x03);
            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);

            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[k << 4];
            encodedData[encodedIndex++] = PAD;
            encodedData[encodedIndex] = PAD;
        } else if (fewerThan24bits == SIXTEEN_BIT) {
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);

            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[l << 2];
            encodedData[encodedIndex] = PAD;
        }
        return new String(encodedData);
    }

    /**
     * 注意：该方法还未进行过性能优化
     */
    public static byte[] decode(final String encodedStr) {
        if (encodedStr == null || encodedStr.length() <= 0) {
            return null;
        }

        return decode(encodedStr.toCharArray());
    }

    public static byte[] decode(char[] encoded) {
        if (encoded == null || encoded.length <= 0) {
            return null;
        }

        final int len = removeWhiteSpace(encoded);
        if (len % FOUR_BYTE != 0) {
            return null;//should be divisible by four
        }

        final int numberQuadruple = (len / FOUR_BYTE);
        if (numberQuadruple == 0) {
            return new byte[0];
        }

        byte b1, b2, b3, b4;
        char d1, d2, d3, d4;

        int i = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        final byte[] decodedData = new byte[(numberQuadruple) * 3];
        for (; i < numberQuadruple - 1; i++) {

            if (!isData((d1 = encoded[dataIndex++]))
                    || !isData((d2 = encoded[dataIndex++]))
                    || !isData((d3 = encoded[dataIndex++]))
                    || !isData((d4 = encoded[dataIndex++]))) {
                return null; // if found "no data" just return null
            }

            b1 = base64Alphabet[d1];
            b2 = base64Alphabet[d2];
            b3 = base64Alphabet[d3];
            b4 = base64Alphabet[d4];

            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
        }

        if (!isData((d1 = encoded[dataIndex++])) || !isData((d2 = encoded[dataIndex++]))) {
            return null;//if found "no data" just return null
        }

        b1 = base64Alphabet[d1];
        b2 = base64Alphabet[d2];

        d3 = encoded[dataIndex++];
        d4 = encoded[dataIndex++];
        if (!isData((d3)) || !isData((d4))) {//Check if they are PAD characters
            if (isPad(d3) && isPad(d4)) {
                if ((b2 & 0xf) != 0)//last 4 bits should be zero
                {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 1];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                return tmp;
            } else if (!isPad(d3) && isPad(d4)) {
                b3 = base64Alphabet[d3];
                if ((b3 & 0x3) != 0) {//last 2 bits should be zero
                    return null;
                }

                byte[] tmp = new byte[i * 3 + 2];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
                tmp[encodedIndex] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
                return tmp;
            } else {
                return null;
            }
        } else { //No PAD e.g 3cQl
            b3 = base64Alphabet[d3];
            b4 = base64Alphabet[d4];
            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
        }
        return decodedData;
    }

    /**
     * remove WhiteSpace from MIME containing encoded Base64 data.
     *
     * @param data the byte array of base64 data (with WS)
     * @return the new length
     */
    private static int removeWhiteSpace(char[] data) {
        if (data == null) {
            return 0;
        }

        // count characters that's not whitespace
        int newSize = 0;
        int len = data.length;
        for (int i = 0; i < len; i++) {
            if (!isWhiteSpace(data[i])) {
                data[newSize++] = data[i];
            }
        }
        return newSize;
    }

    private static boolean isWhiteSpace(char octect) {
        return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
    }

    private static boolean isPad(char octect) {
        return (octect == PAD);
    }

    private static boolean isData(char octect) {
        return (octect < BASE_LENGTH && base64Alphabet[octect] != -1);
    }
}

