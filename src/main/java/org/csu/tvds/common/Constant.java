package org.csu.tvds.common;

import java.time.LocalDateTime;

/**
 * @author Kwanho
 * @date 2022-10-30 11:45
 * 常量类
 */
public class Constant {

    /**
     * 用户身份常量
     */
    public interface Role {
        String ADMIN = "admin";
        String USER = "user";
    }

    /**
     * 任务常量
     */
    public interface JobStatus {
        Integer INACTIVE = 0;
        Integer UNFINISHED = 1;
        Integer FINISHED = 2;
    }

    /**
     * 激活常量，1为激活，0为封号
     */
    public interface IS_VALID {
        Integer VALID = 1;
        Integer NOT_VALID = 0;
    }

    /**
     * file各种标签的开放status，1为使用，0为关闭
     */
    public interface IS_OPEN {
        Integer OPEN = 1;
        Integer CLOSE = 0;
    }

    /**
     * file文档状态，未审核(0)，已通过(1)，已拒绝(2)，已下架(3)
     */
    public interface FILE_STATUS {
        Integer Not_REVIEW = 0;
        Integer PASS = 1;
        Integer REFUSE = 2;
        Integer TAKE_OFF = 3;
    }

    /**
     * notice通知状态，未读(0)，已读(1)
     **/
    public interface NOTICE_STATUS {
        Integer UNREAD = 0;
        Integer READ = 1;
    }

    /**
     * notice通知访问时间的redis键名(Hash)
     **/
    public static final String NOTICE_ACCESS_HASH = "NOTICE_ACCESS_HASH";

    /*非业务常量*/

    /**
     * 数据库查表排序依据
     */
    public interface ORDER_BY {
        String TIME = "time";
    }

    /**
     * MySQL DATETIME的最小值
     **/
    public static final LocalDateTime MIN_DATETIME = LocalDateTime.of(1000, 1, 1, 0, 0, 0);
}
