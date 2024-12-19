-- auto-generated definition
create table interface_info
(
    id              bigint auto_increment comment '接口id'
        primary key,
    name            varchar(256)                       not null comment '名称',
    description     varchar(256)                       null comment '描述',
    host            varchar(512)                       not null comment '服务器地址',
    url             varchar(512)                       not null comment '接口地址',
    request_params  text                               null comment '请求参数',
    request_header  text                               null comment '请求头',
    response_header text                               null comment '响应头',
    status          tinyint  default 0                 not null comment '接口状态 （0-关闭，1-开启）',
    method          varchar(256)                       not null comment '请求类型',
    user_id         bigint                             not null comment '创建人',
    create_time     datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time     datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted         tinyint  default 0                 not null comment '是否删除 0 1（默认值0）'
)
    comment '接口信息';

