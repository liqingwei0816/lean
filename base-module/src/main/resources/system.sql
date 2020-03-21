/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : system

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 16/02/2020 21:05:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_auth`;
CREATE TABLE `t_auth`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `authCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限代码',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路径',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限名',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '注释',
  `parentNode` int(0) UNSIGNED NULL DEFAULT NULL COMMENT '上级节点',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 78 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_auth
-- ----------------------------
INSERT INTO `t_auth` VALUES (0, NULL, ' ', '用户运营系统', NULL, NULL);
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


-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
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
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `roleId` int(0) UNSIGNED NOT NULL COMMENT '角色ID',
  `authId` int(0) UNSIGNED NOT NULL COMMENT '权限Id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

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

-- ----------------------------
-- Table structure for t_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `userName` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `available` int(0) NULL DEFAULT 1 COMMENT '是否可用',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '注释',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_user
-- ----------------------------
INSERT INTO `t_sys_user` VALUES (1, 'user', '$2a$10$k7HHkQUs48wcE7IQL5ynH..WesY3m0XAgOhq8eOCVevMGfq8ejYXm', 1, NULL);
INSERT INTO `t_sys_user` VALUES (6, 'admin', '$2a$10$jomJPE1Eu3rwXlt78c7esuBLbB57YvvJFi2dY4jAjDqjPBntPi7yS', 1, 'admin');

-- ----------------------------
-- Table structure for t_sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user_role`;
CREATE TABLE `t_sys_user_role`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `roleId` int(0) UNSIGNED NOT NULL COMMENT '角色ID',
  `sysUserId` int(0) NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_user_role
-- ----------------------------
INSERT INTO `t_sys_user_role` VALUES (1, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
