-- auto-generated definition
create table user
(
    id            bigint auto_increment comment '用户id'
        primary key,
    user_account  varchar(256)                       null comment '账号',
    username      varchar(256)                       null comment '昵称',
    avatar_url    varchar(1024)                      null comment '头像',
    gender        tinyint                            null comment '性别，1-男，0-女',
    user_password varchar(512)                       not null comment '密码',
    salt          varchar(256)                       null comment '加密盐',
    phone         varchar(128)                       null comment '电话',
    email         varchar(512)                       null comment '邮箱',
    profile       varchar(1024)                      null comment '用户简介',
    tags          varchar(512)                       null comment '用户标签（json字符串：["Java", "男"]）',
    user_role     tinyint  default 0                 not null comment '角色 0-普通用户 1-管理员',
    user_status   int      default 0                 not null comment '状态，0正常',
    access_key    varchar(512)                       null comment '用户唯一标识',
    secret_key    varchar(512)                       null comment '用户密钥',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted       tinyint  default 0                 not null comment '是否删除 0 1（默认值0）'
);

