package com.code.common;
import cn.hutool.crypto.digest.MD5;
public class MD5Util {
    public static String encode(String s) {
        return MD5.create().digestHex(s);
    }
}