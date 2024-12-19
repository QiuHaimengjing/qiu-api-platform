-- auto-generated definition
create table user_interface_info
(
    id           bigint auto_increment comment 'id'
        primary key,
    user_id      bigint                             not null comment '用户id',
    interface_id bigint                             not null comment '接口id',
    total_num    int      default 0                 not null comment '总调用次数',
    left_num     int      default 0                 not null comment '剩余调用次数',
    status       tinyint  default 0                 not null comment '调用状态 （0-正常，1-禁用）',
    create_time  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted      tinyint  default 0                 not null comment '是否删除 0 1（默认值0）'
)
    comment '用户接口调用关系';

