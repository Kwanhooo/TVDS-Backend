package org.csu.tvds.util;

import cn.hutool.crypto.digest.BCrypt;

/***
 * 加密工具类
 * 单向加密密码用
 *
 * @author Kwanho
 */
public class EncryptUtil {
    /**
     * 加密
     *
     * @param password 原密码
     * @return 加密后的密码
     * @author Kwanho
     */
    public static String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * 密码校对
     *
     * @param password       待鉴定的密码
     * @param encodePassword 加密后的密码
     * @return 是否相同
     * @author Kwanho
     */
    public static boolean match(String password, String encodePassword) {
        return BCrypt.checkpw(password, encodePassword);
    }
}
