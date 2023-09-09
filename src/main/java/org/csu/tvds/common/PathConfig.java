package org.csu.tvds.common;

/**
 * @author Noted that Windows DIR must use '\\' instead of '/'
 */
public class PathConfig {
    // AI代码路径前缀
    public static String AI_CODE_BASE = System.getProperty("user.dir") + "/ai/";

    // BLOB资源路径前缀
    public static String BLOB_BASE = System.getProperty("user.dir") + "/blob/";
    public static String OTHER_ASSETS_BASE = System.getProperty("user.dir") + "/assets/";

    // 图片资源路径前缀
    public static String ORIGIN_BASE = BLOB_BASE + "origin/";
    public static String COMPOSITE_BASE = BLOB_BASE + "composite/";
    public static String ALIGNED_BASE = BLOB_BASE + "aligned/";
    public static String MARKED_BASE = BLOB_BASE + "marked/";
    public static String PARTS_BASE = BLOB_BASE + "parts/";
    public static String TEMP_BASE = BLOB_BASE + "temp/";


    // HTTP文件服务器的URL前缀
    String INET_URL_BASE = "https://tvds.0xcafebabe.cn/blob/";
}
