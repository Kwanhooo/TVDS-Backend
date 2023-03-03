package org.csu.tvds.common;

/**
 * @author Noted that Windows DIR must use '\' instead of '/'
 */
public interface PathConfig {

    String BASE = System.getProperty("user.dir") + "/blob/";
    // String BASE = System.getProperty("user.dir") + "\blob\";
    String AI_BASE = System.getProperty("user.dir") + "/ai/";
    // String AI_BASE = System.getProperty("user.dir") + "\ai\";

    // 相对路径前缀
    String UPLOAD_BASE = BASE + "composite/";
    String ALIGNED_BASE = BASE + "aligned/";
    String ALIGNED_MARKED_BASE = BASE + "aligned_marked/";
    String PARTS_BASE = BASE + "parts/";

    // HTTP文件服务器的URL前缀
    String INET_URL_BASE = "https://tvds.0xcafebabe.cn/blob/";
}
