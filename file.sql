/*
 Navicat Premium Data Transfer

 Source Server         : zz
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : disk

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 19/07/2021 08:27:25
*/


SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `pid` int(0) NULL DEFAULT NULL,
  `fileName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fileSize` bigint(0) NULL DEFAULT NULL,
  `fileType` int(0) NULL DEFAULT NULL,
  `filePath` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `isCollect` int(0) NULL DEFAULT NULL,
  `isRecovery` int(0) NULL DEFAULT NULL,
  `userId` int(0) NULL DEFAULT NULL,
  `createTime` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) 
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fromUser` int(0) NULL DEFAULT NULL,
  `toUser` int(0) NULL DEFAULT NULL,
  `file` int(0) NULL DEFAULT NULL,
  `createTime` datetime NULL DEFAULT NULL,
  `isRead` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) ,
  INDEX `fromUser`(`fromUser`) ,
  INDEX `toUser`(`toUser`) ,
  INDEX `file`(`file`) ,
  CONSTRAINT `message_ibfk_1` FOREIGN KEY (`fromUser`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `message_ibfk_2` FOREIGN KEY (`toUser`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `message_ibfk_3` FOREIGN KEY (`file`) REFERENCES `file` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) 
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` int(0) NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `birthday` date NULL DEFAULT NULL,
  `idCard` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role` int(0) NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `size` bigint(0) NULL DEFAULT NULL,
  `usedSize` bigint(0) NULL DEFAULT NULL,
  `photo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) ,
  INDEX `角色ID`(`role`) ,
  CONSTRAINT `角色ID` FOREIGN KEY (`role`) REFERENCES `role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
