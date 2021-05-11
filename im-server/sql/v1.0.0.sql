
/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`om_sys` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

create table history_group_chat
(
    id bigint not null comment '主键id'
        primary key,
    msg_id varchar(36) not null comment '消息id',
    time_stamp bigint not null comment '回应着id',
    send_id bigint not null comment '发送者id',
    receive bigint not null comment '接收者id',
    message varchar(2248) not null comment '消息体',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null comment '修改时间',
    create_by bigint null comment '创建人',
    modify_by bigint null comment '修改人',
    status tinyint default 1 null comment '状态',
    is_delete tinyint default 0 null comment '0未删除 1已删除',
    tenant_id bigint null comment '租户id'
)
    comment '群主信息列表';

create table history_notify_chat
(
    id bigint not null comment '主键id'
        primary key,
    msg_id varchar(36) not null comment '消息id',
    time_stamp bigint not null comment '回应着id',
    send_id bigint not null comment '发送者id',
    receive bigint not null comment '接收者id',
    message varchar(2248) not null comment '消息体',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null comment '修改时间',
    create_by bigint null comment '创建人',
    modify_by bigint null comment '修改人',
    status tinyint default 1 null comment '状态',
    is_delete tinyint default 0 null comment '0未删除 1已删除',
    tenant_id bigint null comment '租户id'
)
    comment '通知信息列表';

create table history_private_chat
(
    id bigint not null comment '主键id'
        primary key,
    msg_id varchar(36) not null comment '消息id',
    time_stamp bigint not null comment '回应着id',
    send_id bigint not null comment '发送者id',
    receive bigint not null comment '接收者id',
    message varchar(2248) charset utf8mb4 not null comment '消息体',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null comment '修改时间',
    create_by bigint null comment '创建人',
    modify_by bigint null comment '修改人',
    status tinyint default 1 null comment '状态',
    is_delete tinyint default 0 null comment '0未删除 1已删除',
    tenant_id bigint null comment '租户id'
)
    comment '私聊信息列表';

create table im_group_card
(
    id bigint not null comment '主键id'
        primary key,
    name varchar(32) default '' not null comment '群聊名',
    group_master bigint not null comment '群主',
    type varchar(10) not null comment '群类型',
    label varchar(60) not null comment '群标签',
    notice varchar(512) not null comment '群公告',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null comment '修改时间',
    create_by bigint null comment '创建人',
    modify_by bigint null comment '修改人',
    status tinyint default 1 null comment '状态',
    is_delete tinyint default 0 null comment '0未删除 1已删除',
    tenant_id bigint null comment '租户id'
)
    comment '群组信息卡片';

create table im_user_friend
(
    id bigint not null comment '主键id'
        primary key,
    uid bigint not null comment '用户id',
    friend_id bigint not null comment '好友id',
    group_id bigint default 0 not null comment '好友分组id',
    nickname varchar(32) default '' not null comment '好友备注, 默认为添加好友时的名称',
    star_flag tinyint default 0 not null comment '普通好友0 星级好友1 特别关注2',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null comment '修改时间',
    create_by bigint null comment '创建人',
    modify_by bigint null comment '修改人',
    status tinyint default 1 null comment '状态',
    is_delete tinyint default 0 null comment '0未删除 1已删除',
    tenant_id bigint null comment '租户id',
    remark_name varchar(36) null comment '备注'
)
    comment '好友关系表';

create table im_user_group
(
    id bigint not null comment '主键id'
        primary key,
    uid bigint not null comment '用户id',
    group_id bigint default 0 not null comment '好友分组id',
    nickname varchar(32) default '' not null comment '好友备注, 默认为添加好友时的名称',
    star_flag tinyint default 0 not null comment '普通好友0 星级好友1 特别关注2',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null comment '修改时间',
    create_by bigint null comment '创建人',
    modify_by bigint null comment '修改人',
    status tinyint default 1 null comment '状态',
    is_delete tinyint default 0 null comment '0未删除 1已删除',
    tenant_id bigint null comment '租户id'
)
    comment '群聊好友人员关系';

create table mer_user
(
    id bigint unsigned not null comment 'id'
        primary key,
    username varchar(40) not null comment '用户名',
    password varchar(36) not null comment '密码',
    tel varchar(16) null comment '电话号码',
    nickname varchar(16) null comment '昵称',
    salt varchar(32) not null comment '密码盐',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null comment '修改时间',
    create_by bigint null comment '创建人',
    modify_by bigint null comment '修改人',
    status tinyint default 1 null comment '状态',
    is_delete tinyint default 0 null comment '0未删除 1已删除',
    tenant_id bigint null comment '租户id'
)
    charset=utf8mb4;

create table pt_user
(
    id bigint unsigned not null comment 'id'
        primary key,
    username varchar(40) null comment '用户名',
    password varchar(36) not null comment '密码',
    email varchar(320) null comment '邮箱',
    tel varchar(16) null comment '电话号码',
    nickname varchar(16) not null comment '昵称',
    salt varchar(32) not null comment '密码盐',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '修改时间',
    create_by bigint null comment '创建人',
    modify_by bigint null comment '修改人',
    status tinyint default 1 null comment '状态',
    is_delete tinyint default 0 null comment '0未删除 1已删除',
    tenant_id bigint null comment '租户id',
    constraint pt_username_uk
        unique (username)
)
    charset=utf8mb4;

create table pt_user_info
(
    id bigint not null comment '主键id'
        primary key,
    uid bigint not null comment '用户id',
    name varchar(32) default '' not null comment '用户名称',
    avatar varchar(512) null comment '头像',
    intro varchar(512) null comment '简介',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null comment '修改时间',
    create_by bigint null comment '创建人',
    modify_by bigint null comment '修改人',
    status tinyint default 1 null comment '状态',
    is_delete tinyint default 0 null comment '0未删除 1已删除',
    tenant_id bigint null comment '租户id',
    constraint uid
        unique (uid)
)
    comment '用户信息表';

create table role
(
    id bigint unsigned not null comment 'id'
        primary key,
    uid bigint not null comment '用户id',
    role_type varchar(10) not null comment '角色类型',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '修改时间',
    create_by bigint null comment '创建人',
    modify_by bigint null comment '修改人',
    status tinyint default 1 null comment '状态',
    is_delete tinyint default 0 null comment '0未删除 1已删除',
    tenant_id bigint null comment '租户id'
)
    charset=utf8mb4;

create table sys_user
(
    id bigint unsigned not null comment 'id'
        primary key,
    username varchar(32) not null comment '用户名',
    password varchar(40) not null comment '密码',
    tel varchar(16) null comment '电话号码',
    nickname varchar(16) null comment '昵称',
    salt varchar(32) not null comment '密码盐',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '修改时间',
    create_by bigint null comment '创建人',
    modify_by bigint null comment '修改人',
    status tinyint default 1 null comment '状态',
    is_delete tinyint default 0 not null comment '0未删除 1已删除',
    tenant_id bigint null comment '租户id'
)
    charset=utf8mb4;

create table user_apply
(
    id bigint not null comment '主键id'
        primary key,
    apply_id bigint not null comment '申请id',
    answer_id bigint not null comment '回应着id',
    leave_word varchar(13) not null comment '留言',
    answer_type tinyint null comment '回应操作',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null comment '修改时间',
    create_by bigint null comment '创建人',
    modify_by bigint null comment '修改人',
    status tinyint default 1 null comment '状态',
    is_delete tinyint default 0 null comment '0未删除 1已删除',
    tenant_id bigint null comment '租户id'
)
    comment '好友申请表';

create table user_config
(
    id bigint not null comment '主键id'
        primary key,
    uid bigint not null comment '用户id',
    receive_stranger tinyint default 0 not null comment '是否接收允许陌生人信息',
    receive_add_friend tinyint default 0 not null comment '是否允许添加好友',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null comment '修改时间',
    create_by bigint null comment '创建人',
    modify_by bigint null comment '修改人',
    status tinyint default 1 null comment '状态',
    is_delete tinyint default 0 null comment '0未删除 1已删除',
    tenant_id bigint null comment '租户id'
)
    comment '用户设置表';

