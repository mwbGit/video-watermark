package com.harry.videowatermark.utils.id;

import org.apache.commons.lang3.StringUtils;

/**
 * 描述:
 *
 * @author mengweibo@kanzhun.com
 * @create 2021/9/2
 */

public final class Base24 {
    private static String table = "BCDFGHJKMPQRTVWXY2346789";

    public Base24() {
    }

    public static String encode(String text) {
        if (StringUtils.isEmpty(text)) {
            return "";
        } else {
            int position = 0;

            char[] buffer;
            for(buffer = new char[text.length() << 1]; position < text.length(); ++position) {
                buffer[position << 1] = table.charAt(text.charAt(position) >> 4);
                buffer[(position << 1) + 1] = table.charAt(23 - (text.charAt(position) & 15));
            }

            return new String(buffer);
        }
    }

    public static String decode(String text) {
        if (!StringUtils.isEmpty(text) && text.length() % 2 == 0) {
            int[] position = new int[2];
            char[] buffer = new char[text.length() >> 1];

            for(int i = 0; i < text.length() >> 1; ++i) {
                position[0] = table.indexOf(text.charAt(i << 1));
                position[1] = 23 - table.indexOf(text.charAt((i << 1) + 1));
                if (position[0] < 0 || position[1] < 0) {
                    return "";
                }

                buffer[i] = (char)(position[0] << 4 | position[1]);
            }

            return new String(buffer);
        } else {
            return "";
        }
    }

    public static void main(String[] args) {
        String str = "adsf++--//>>123";
        String strAfterEncode = encode(str);
        String strAfterDecode = decode(strAfterEncode);
        System.out.println(strAfterDecode);
    }
}
