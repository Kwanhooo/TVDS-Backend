package org.csu.tvds.common;

/**
 * @author Noted that Windows DIR must use '\' instead of '/'
 */
public interface PathConfig {
    // AI代码路径前缀
    String AI_CODE_BASE = System.getProperty("user.dir") + "/ai/";
    // String AI_CODE_BASE = System.getProperty("user.dir") + "\ai\";

    // 图片资源路径前缀
    String BLOB_BASE = System.getProperty("user.dir") + "/blob/";
    // String BASE = System.getProperty("user.dir") + "\blob\";
    String ORIGIN_BASE = BLOB_BASE + "origin/";
    String COMPOSITE_BASE = BLOB_BASE + "composite/";
    String ALIGNED_BASE = BLOB_BASE + "aligned/";
    String MARKED_BASE = BLOB_BASE + "marked/";
    String PARTS_BASE = BLOB_BASE + "parts/";

    // HTTP文件服务器的URL前缀
    String INET_URL_BASE = "https://tvds.0xcafebabe.cn/blob/";
}
