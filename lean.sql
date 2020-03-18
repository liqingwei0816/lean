/*
 Navicat Premium Data Transfer

 Source Server         : 测试环境mdn
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : 10.3.1.16:3306
 Source Schema         : lean

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 18/03/2020 18:51:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for qrtz_dynamic_job
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_dynamic_job`;
CREATE TABLE `qrtz_dynamic_job`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `jobClass` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `jobClassContent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `jobClass`(`jobClass`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_dynamic_job
-- ----------------------------
INSERT INTO `qrtz_dynamic_job` VALUES (1, 'com.github.job.HelloWord', 'package com.github.job;\r\n\r\nimport org.quartz.JobExecutionContext;\r\nimport org.quartz.JobExecutionException;\r\nimport org.springframework.scheduling.quartz.QuartzJobBean;\r\n\r\npublic class HelloWord extends QuartzJobBean {\r\n    private Integer count = 0;\r\n\r\n    @Override\r\n    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {\r\n        System.out.println(count);\r\n        count++;\r\n    }\r\n}');
INSERT INTO `qrtz_dynamic_job` VALUES (2, 'com.github.job.SampleJob1', 'package com.github.job;\n\nimport com.github.bean.system.Auth;\nimport com.github.service.system.AuthService;\nimport javax.annotation.Resource;\nimport org.quartz.JobExecutionContext;\nimport org.quartz.JobExecutionException;\nimport org.slf4j.Logger;\nimport org.slf4j.LoggerFactory;\nimport org.springframework.scheduling.quartz.QuartzJobBean;\n\npublic class SampleJob1 extends QuartzJobBean {\n    private static final Logger log = LoggerFactory.getLogger(SampleJob1.class);\n    @Resource\n    private AuthService authService;\n    private String name;\n\n    public SampleJob1() {\n    }\n\n    public void setName(String var1) {\n    }\n\n    protected void executeInternal(JobExecutionContext var1) throws JobExecutionException {\n        Long var2 = this.authService.count(new Auth());\n        log.error(\"{} job33 {}\", var2, var1.getScheduledFireTime());\n    }\n}');
INSERT INTO `qrtz_dynamic_job` VALUES (3, 'com.github.job.SampleJob', 'package com.github.job;\r\n\r\nimport com.github.bean.system.Auth;\r\nimport com.github.service.system.AuthService;\r\nimport lombok.extern.slf4j.Slf4j;\r\nimport org.quartz.JobExecutionContext;\r\nimport org.quartz.JobExecutionException;\r\nimport org.springframework.scheduling.quartz.QuartzJobBean;\r\n\r\nimport javax.annotation.Resource;\r\n\r\n@Slf4j\r\n/*\r\n * 不允许并发执行注解\r\n * 存在，则允许一个QuartzJob并发执行，否则就是顺序执行\r\n * 例如QuartzJob A执行时间为15秒，配置为每10秒执行一次；\r\n * 如果此注解不存在，则0秒的时候启动一次A，10秒的时候再启动一次A，20秒的时候再启动一次A，不管前面启动的A有没有执行完；\r\n * 如果此注解存在，则0秒的时候启动一次A，15秒的时候A执行完毕，再第二次启动A 但对于总的执行次数两者是相同的，只是执行的时间不同。\r\n */\r\n//@DisallowConcurrentExecution\r\n@SuppressWarnings(\"unused\")\r\npublic class SampleJob extends QuartzJobBean {\r\n    /**\r\n     * 直接注入ioc中的bean\r\n     */\r\n    @Resource\r\n    private AuthService authService;\r\n\r\n\r\n    private String name;\r\n    public void setName(String name) {\r\n\r\n    }\r\n    @Override\r\n    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {\r\n        Long count = authService.count(new Auth());\r\n        log.error(\"{} job {}\",count,context.getScheduledFireTime());\r\n    }\r\n}\r\n');

-- ----------------------------
-- Table structure for t_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_auth`;
CREATE TABLE `t_auth`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `authCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限代码',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路径',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限名',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '注释',
  `parentNode` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '上级节点',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 93 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_auth
-- ----------------------------
INSERT INTO `t_auth` VALUES (1, '/auth/list', '/auth/list;/auth/dataList', '权限管理', NULL, 0);
INSERT INTO `t_auth` VALUES (2, '/auth/addOrUpdate', '/auth/addOrUpdate', '权限编辑', NULL, 1);
INSERT INTO `t_auth` VALUES (3, '/auth/delete', '/auth/delete', '权限删除', NULL, 1);
INSERT INTO `t_auth` VALUES (4, '/role/list', '/role/list;/role/dataList', '角色管理', NULL, 0);
INSERT INTO `t_auth` VALUES (5, '/role/delete', '/role/delete', '角色删除', NULL, 4);
INSERT INTO `t_auth` VALUES (6, '/role/addOrUpdate', '/role/addOrUpdate', '角色编辑', NULL, 4);
INSERT INTO `t_auth` VALUES (7, '/role/addAuth', '/role/addAuth', '角色权限添加', NULL, 4);
INSERT INTO `t_auth` VALUES (8, '/role/auth', '/role/auth/**', '获取角色权限', NULL, 4);
INSERT INTO `t_auth` VALUES (9, '/sysUser/list', '/sysUser/list;/sysUser/dataList', '管理员管理', NULL, 0);
INSERT INTO `t_auth` VALUES (10, '/sysUser/delete', '/sysUser/delete', '管理员删除', NULL, 9);
INSERT INTO `t_auth` VALUES (11, '/sysUser/addOrUpdate', '/sysUser/addOrUpdate', '管理员编辑', NULL, 9);
INSERT INTO `t_auth` VALUES (12, '/sysUser/addRole', '/sysUser/addRole', '管理员添加角色', NULL, 9);
INSERT INTO `t_auth` VALUES (13, '/sysUser/roles', '/sysUser/roles/**', '查看管理员角色', NULL, 9);
INSERT INTO `t_auth` VALUES (14, '/table/list', '/table/list;/table/dataList', '查看表单列表', NULL, 0);
INSERT INTO `t_auth` VALUES (15, '/table/createCode', '/table/createCode', '代码生成', NULL, 14);
INSERT INTO `t_auth` VALUES (86, '/sysUser/changePassword', '/sysUser/changePassword', '修改密码', '', 9);
INSERT INTO `t_auth` VALUES (92, NULL, ' ', '用户运营系统', NULL, NULL);

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `roleName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '注释',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (1, 'admin', 'admin');
INSERT INTO `t_role` VALUES (2, 'user1', 'user');

-- ----------------------------
-- Table structure for t_role_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_role_auth`;
CREATE TABLE `t_role_auth`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `roleId` int(10) UNSIGNED NOT NULL COMMENT '角色ID',
  `authId` int(10) UNSIGNED NOT NULL COMMENT '权限Id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role_auth
-- ----------------------------
INSERT INTO `t_role_auth` VALUES (1, 1, 1);
INSERT INTO `t_role_auth` VALUES (2, 1, 2);
INSERT INTO `t_role_auth` VALUES (3, 1, 3);
INSERT INTO `t_role_auth` VALUES (4, 1, 4);
INSERT INTO `t_role_auth` VALUES (5, 1, 5);
INSERT INTO `t_role_auth` VALUES (6, 1, 6);
INSERT INTO `t_role_auth` VALUES (7, 1, 7);
INSERT INTO `t_role_auth` VALUES (8, 1, 8);
INSERT INTO `t_role_auth` VALUES (9, 1, 9);
INSERT INTO `t_role_auth` VALUES (10, 1, 10);
INSERT INTO `t_role_auth` VALUES (11, 1, 11);
INSERT INTO `t_role_auth` VALUES (12, 1, 12);
INSERT INTO `t_role_auth` VALUES (13, 1, 13);
INSERT INTO `t_role_auth` VALUES (14, 1, 14);
INSERT INTO `t_role_auth` VALUES (15, 1, 15);
INSERT INTO `t_role_auth` VALUES (19, 2, 0);
INSERT INTO `t_role_auth` VALUES (20, 2, 14);
INSERT INTO `t_role_auth` VALUES (21, 2, 15);
INSERT INTO `t_role_auth` VALUES (22, 2, 1);
INSERT INTO `t_role_auth` VALUES (23, 2, 3);

-- ----------------------------
-- Table structure for t_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `userName` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `available` bit(1) NULL DEFAULT b'1' COMMENT '是否可用',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '注释',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_user
-- ----------------------------
INSERT INTO `t_sys_user` VALUES (1, 'user', '$2a$10$INKJ39xq.6/y0F1Nifbg7O3ixxVA73DTHeeu/CS49uY0.oOP.vD6G', b'1', NULL);
INSERT INTO `t_sys_user` VALUES (6, 'admin', '$2a$10$jomJPE1Eu3rwXlt78c7esuBLbB57YvvJFi2dY4jAjDqjPBntPi7yS', b'1', 'admin');
INSERT INTO `t_sys_user` VALUES (7, 'user1', '$2a$10$TqvfGZfK/vfCcInoIKT0mOf6ZrTlWbjV9y0azPb4To2qTycoKSS1O', b'1', '1111');
INSERT INTO `t_sys_user` VALUES (8, '111', '$2a$10$khdo9XJ31Wx15oSR3eg4ru0Snhq2owjq3Gz5o6bnex.KHTMRbJlAO', b'1', '111');
INSERT INTO `t_sys_user` VALUES (9, '222', '$2a$10$6C5U3TzrFPXIeYaiM/EEO.gRgr9zYcq7SDWDhKDnNMpxxKYzCgaiK', b'1', '222');

-- ----------------------------
-- Table structure for t_sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user_role`;
CREATE TABLE `t_sys_user_role`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `roleId` int(10) UNSIGNED NOT NULL COMMENT '角色ID',
  `sysUserId` int(11) NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_user_role
-- ----------------------------
INSERT INTO `t_sys_user_role` VALUES (1, 1, 1);
INSERT INTO `t_sys_user_role` VALUES (2, 2, 6);

-- ----------------------------
-- Table structure for u_coupon
-- ----------------------------
DROP TABLE IF EXISTS `u_coupon`;
CREATE TABLE `u_coupon`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '优惠卷编码',
  `memberCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '会员编码',
  `couponDictCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '卷字典code',
  `startTime` datetime(0) NULL DEFAULT NULL COMMENT '生效时间',
  `endTime` datetime(0) NULL DEFAULT NULL COMMENT '失效时间',
  `source` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '来源',
  `useTime` datetime(0) NULL DEFAULT NULL COMMENT '使用时间',
  `used` bit(1) NULL DEFAULT b'0' COMMENT '使用状态',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '类型',
  `monetaryLimits` decimal(8, 2) NULL DEFAULT NULL COMMENT '使用金额限制',
  `denomination` decimal(8, 2) NULL DEFAULT NULL COMMENT '优惠卷面额',
  `discount` float(3, 2) NULL DEFAULT NULL COMMENT '折扣率',
  `orderCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '订单编码',
  `orderInvalidTime` datetime(0) NULL DEFAULT NULL COMMENT '订单失效时间',
  `createTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `memberCode`(`memberCode`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '优惠卷表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of u_coupon
-- ----------------------------
INSERT INTO `u_coupon` VALUES (1, '000000001', '1111', '111', '2020-03-18 13:15:23', '2020-04-01 13:15:38', NULL, NULL, b'0', '满减卷', NULL, NULL, NULL, NULL, '2020-03-03 13:01:04', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for u_coupon_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `u_coupon_dictionary`;
CREATE TABLE `u_coupon_dictionary`  (
  `id` int(10) UNSIGNED NOT NULL COMMENT 'id',
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '编码',
  `type` enum('满减卷','折扣卷') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '满减卷' COMMENT '种类',
  `effectiveDuration` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '30d' COMMENT '有效时长',
  `monetaryLimits` decimal(8, 2) NULL DEFAULT NULL COMMENT '使用金额限制',
  `denomination` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠卷面额',
  `discount` float(3, 2) NULL DEFAULT NULL COMMENT '折扣率',
  `createTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateTime` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `updatePerson` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '优惠卷字典表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of u_coupon_dictionary
-- ----------------------------
INSERT INTO `u_coupon_dictionary` VALUES (1, '00000001', '满减卷', '30d', 0.00, 50.00, NULL, '2020-03-03 09:07:15', NULL, NULL, NULL, '暂定为生日卷');

-- ----------------------------
-- Table structure for u_member
-- ----------------------------
DROP TABLE IF EXISTS `u_member`;
CREATE TABLE `u_member`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '会员唯一code',
  `showName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `sex` tinyint(4) NULL DEFAULT NULL COMMENT '0-男，1-女',
  `showPicture` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `phoneNum` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '手机号',
  `source` int(11) NULL DEFAULT NULL COMMENT '用户来源，1-手机号，2-微信，3-支付宝，4-卡号',
  `externalId` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '微信或支付宝id',
  `birth` datetime(0) NULL DEFAULT NULL COMMENT '会员生日',
  `status` int(11) NULL DEFAULT NULL COMMENT '用户状态，0-正常，1-注销，2-暂停',
  `createTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市',
  `agentVendorCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道code',
  `serviceComboCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务组合code',
  `serviceGroupCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务分组code',
  `registArea` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '注册地区',
  `vipExpirationTime` datetime(0) NULL DEFAULT NULL COMMENT 'vip到期时间(bms产生订购时回调)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code`(`code`) USING BTREE,
  INDEX `phoneNum`(`phoneNum`) USING BTREE,
  INDEX `externalId`(`externalId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for u_rights_interests
-- ----------------------------
DROP TABLE IF EXISTS `u_rights_interests`;
CREATE TABLE `u_rights_interests`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '编码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `couponDictionarys` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '权益发放的优惠卷信息，可为json或（;）分割的卷及其数量集合',
  `createTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `updateTime` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `updatePerson` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权益内容描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '会员权益表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of u_rights_interests
-- ----------------------------
INSERT INTO `u_rights_interests` VALUES (1, '00001', '会员生日权益', '00000001|3', '2020-03-03 17:12:43', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for u_student_information
-- ----------------------------
DROP TABLE IF EXISTS `u_student_information`;
CREATE TABLE `u_student_information`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `memberCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '会员code 唯一编码',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `grade` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学员年纪',
  `phoneNumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系方式',
  `getBirthdayCouponTime` datetime(0) NULL DEFAULT NULL COMMENT '领取生日优惠卷时间',
  `createTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `memberCode`(`memberCode`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学员信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for u_system_dict
-- ----------------------------
DROP TABLE IF EXISTS `u_system_dict`;
CREATE TABLE `u_system_dict`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '属性分组',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '属性名',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '属性值',
  `order` int(11) NULL DEFAULT NULL COMMENT '同组内排序',
  `createTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateTime` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `updatePerson` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统字典表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of u_system_dict
-- ----------------------------
INSERT INTO `u_system_dict` VALUES (1, 'grade', '小班', '小班', 1, '2020-03-04 08:26:15', NULL, NULL, NULL, '小班');
INSERT INTO `u_system_dict` VALUES (2, 'grade', '中班', '中班', 2, '2020-03-04 08:26:15', NULL, NULL, NULL, '中班');
INSERT INTO `u_system_dict` VALUES (3, 'grade', '大班', '大班', 3, '2020-03-04 08:26:15', NULL, NULL, NULL, '大班');
INSERT INTO `u_system_dict` VALUES (4, 'grade', '四年级', '四年级', 7, '2020-03-04 08:26:15', NULL, NULL, NULL, '四年级');
INSERT INTO `u_system_dict` VALUES (5, 'grade', '五年级', '五年级', 8, '2020-03-04 08:26:15', NULL, NULL, NULL, '五年级');
INSERT INTO `u_system_dict` VALUES (6, 'grade', '六年级', '六年级', 9, '2020-03-04 08:26:15', NULL, NULL, NULL, '六年级');
INSERT INTO `u_system_dict` VALUES (7, 'grade', '初中一年级', '初中一年级', 10, '2020-03-04 08:26:15', NULL, NULL, NULL, '初中一年级');
INSERT INTO `u_system_dict` VALUES (8, 'grade', '初中二年级', '初中二年级', 11, '2020-03-04 08:26:15', NULL, NULL, NULL, '初中二年级');
INSERT INTO `u_system_dict` VALUES (9, 'grade', '初中三年级', '初中三年级', 12, '2020-03-04 08:26:15', NULL, NULL, NULL, '初中三年级');
INSERT INTO `u_system_dict` VALUES (10, 'grade', '高中一年级', '高中一年级', 13, '2020-03-04 08:26:15', NULL, NULL, NULL, '高中一年级');
INSERT INTO `u_system_dict` VALUES (11, 'grade', '高中二年级', '高中二年级', 14, '2020-03-04 08:26:15', NULL, NULL, NULL, '高中二年级');
INSERT INTO `u_system_dict` VALUES (12, 'grade', '高中三年级', '高中三年级', 15, '2020-03-04 08:26:15', NULL, NULL, NULL, '高中三年级');
INSERT INTO `u_system_dict` VALUES (13, 'grade', '一年级', '一年级', 4, '2020-03-04 08:26:15', NULL, NULL, NULL, '一年级');
INSERT INTO `u_system_dict` VALUES (14, 'grade', '二年级', '二年级', 5, '2020-03-04 08:26:15', NULL, NULL, NULL, '二年级');
INSERT INTO `u_system_dict` VALUES (15, 'grade', '三年级', '三年级', 6, '2020-03-04 08:26:15', NULL, NULL, NULL, '三年级');
INSERT INTO `u_system_dict` VALUES (16, 'vip_rights_display', 'vip权益展示页面地址', 'http://ip:port/src', 1, '2020-03-04 08:26:15', NULL, NULL, NULL, 'vip权益展示页面地址');

SET FOREIGN_KEY_CHECKS = 1;
