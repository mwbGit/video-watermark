package com.harry.videowatermark.utils;

import org.springframework.util.DigestUtils;

public class MD5Util {

    public static String md5(String str, String safe) {
        return DigestUtils.md5DigestAsHex((str + safe).getBytes());
    }

    public static String md5(String str) {
        return DigestUtils.md5DigestAsHex((str).getBytes());
    }

    public static void main(String[] args) {
        //1f18d6d3718c5ed821986fa1dbd50f21
        System.out.println(md5("Some thing"));
    }

}
