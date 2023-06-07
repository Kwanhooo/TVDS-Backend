create table composite_aligned_image
(
    dbId          bigint                     not null
        primary key,
    id            varchar(200)               not null,
    inspectionSeq int                        not null,
    cameraNumber  int                        null,
    carriageId    int                        null,
    carriageNo    int                        not null,
    model         varchar(50) default '未知' not null,
    status        tinyint                    not null,
    hasDefect     tinyint(1)  default 0      not null comment '只要子部件有一个异常，这个状态位就为异常',
    comment       varchar(2000)              null,
    compositeUrl  varchar(200)               null,
    compositeTime datetime                   null,
    alignedUrl    varchar(200)               null,
    alignTime     datetime                   null,
    createTime    datetime                   null,
    updateTime    datetime                   null,
    isDeleted     int         default 0      not null,
    constraint dbId
        unique (dbId)
);

create table defect_info
(
    dbId          bigint            not null
        primary key,
    id            varchar(200)      not null,
    partName      varchar(50)       null,
    inspectionSeq int               null,
    carriageNo    int               null,
    model         varchar(50)       null,
    compositeId   varchar(200)      null,
    imageUrl      varchar(200)      null,
    checkTime     datetime          null,
    createTime    datetime          null,
    updateTime    datetime          null,
    isDeleted     tinyint default 0 not null,
    constraint dbId
        unique (dbId)
);

create index tb_defect_info_tb_composite_aligned_img_id_fk
    on defect_info (compositeId);

create table job_assign
(
    dbId           bigint            not null,
    personnelSeq   varchar(255)      not null,
    assignee       bigint            not null,
    deadline       datetime          null,
    targetCarriage bigint            not null,
    comment        varchar(2000)     null,
    status         tinyint default 0 not null,
    createTime     datetime          null,
    updateTime     datetime          null,
    isDeleted      int     default 0 not null
);

create table origin_image
(
    dbId             bigint            not null
        primary key,
    id               varchar(100)      not null,
    filename         varchar(200)      null,
    inspectionSeqDay int               null,
    localUrl         varchar(200)      null,
    cameraNumber     int               null,
    carriageNumber   int               null,
    contentType      varchar(100)      null,
    relatedId        varchar(100)      null,
    createTime       datetime          null,
    updateTime       datetime          null,
    isDeleted        tinyint default 0 not null,
    constraint dbId
        unique (dbId)
);

create table part_info
(
    dbId          bigint            not null
        primary key,
    id            varchar(200)      not null,
    partName      varchar(50)       null,
    inspectionSeq int               null,
    carriageNo    int               null,
    model         varchar(50)       null,
    compositeId   varchar(200)      null,
    imageUrl      varchar(200)      null,
    status        tinyint           not null,
    verifyStatusA tinyint default 0 not null,
    verifyStatusB tinyint default 0 not null,
    checkTime     datetime          null,
    createTime    datetime          null,
    updateTime    datetime          null,
    isDeleted     tinyint default 0 not null,
    constraint dbId
        unique (dbId)
);

create index tb_part_info_tb_composite_aligned_img_id_fk
    on part_info (compositeId);

create table templates_lib
(
    dbId         bigint            not null
        primary key,
    id           varchar(200)      not null,
    model        varchar(100)      not null,
    cameraNumber int               null,
    templateUrl  varchar(200)      null,
    createYear   int               null,
    version      varchar(100)      null,
    createTime   datetime          null,
    updateTime   datetime          null,
    isDeleted    tinyint default 0 not null,
    constraint dbId
        unique (dbId)
);

create table user
(
    user_id  varchar(255)                           not null comment 'id'
        primary key,
    username varchar(255)                           null comment '用户名',
    password varchar(255)                           null comment '密码',
    nickname varchar(255)                           null comment '昵称',
    reg_time datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '注册时间',
    role     varchar(255) default 'user'            not null,
    is_valid tinyint                                null comment '是否激活（1激活、0封号）',
    constraint user_id_unique
        unique (user_id),
    constraint username_unique
        unique (username)
)
    charset = utf8mb4;

