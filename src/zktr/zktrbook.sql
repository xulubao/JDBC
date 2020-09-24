/*
 Navicat Premium Data Transfer

 Source Server         : root
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : zktrbook

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 24/09/2020 23:11:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `Bookid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图书编号',
  `Bookname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图书名字',
  `total` int(0) NULL DEFAULT NULL COMMENT '总数量',
  `already` int(0) NULL DEFAULT NULL COMMENT '已借数量',
  `remain` int(0) NOT NULL,
  PRIMARY KEY (`Bookid`) USING BTREE,
  INDEX `Bookid`(`Bookid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES ('book00001', '天龙八部2', 10, 1, 9);
INSERT INTO `book` VALUES ('book00002', '盗墓笔记', 10, 0, 10);
INSERT INTO `book` VALUES ('book00003', 'xu1', 6, 0, 6);

-- ----------------------------
-- Table structure for computer
-- ----------------------------
DROP TABLE IF EXISTS `computer`;
CREATE TABLE `computer`  (
  `cptid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `state` int(0) NOT NULL,
  PRIMARY KEY (`cptid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of computer
-- ----------------------------
INSERT INTO `computer` VALUES ('cpt01', 1);
INSERT INTO `computer` VALUES ('cpt02', 1);
INSERT INTO `computer` VALUES ('cpt03', 1);
INSERT INTO `computer` VALUES ('cpt04', 0);
INSERT INTO `computer` VALUES ('cpt05', 1);
INSERT INTO `computer` VALUES ('cpt06', 1);
INSERT INTO `computer` VALUES ('cpt07', 0);
INSERT INTO `computer` VALUES ('cpt08', 0);

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `Empid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '员工编号',
  `empname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '员工姓名',
  `emppass` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '员工密码',
  `state` enum('2','1','3') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '员工状态',
  PRIMARY KEY (`Empid`) USING BTREE,
  INDEX `Empid`(`Empid`) USING BTREE,
  INDEX `Empid_2`(`Empid`) USING BTREE,
  INDEX `Empid_3`(`Empid`) USING BTREE,
  INDEX `Empid_4`(`Empid`) USING BTREE,
  INDEX `Empid_5`(`Empid`) USING BTREE,
  INDEX `Empid_6`(`Empid`) USING BTREE,
  INDEX `Empid_7`(`Empid`) USING BTREE,
  INDEX `Empid_8`(`Empid`) USING BTREE,
  INDEX `Empid_9`(`Empid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES ('emp2017001', '许仙', '123456', '2');
INSERT INTO `employee` VALUES ('emp2019003', '小秋', '123456', '2');
INSERT INTO `employee` VALUES ('emp2020001', '啊三', '123456', '2');
INSERT INTO `employee` VALUES ('emp2020002', 'xu', '123456', '2');

-- ----------------------------
-- Table structure for lease
-- ----------------------------
DROP TABLE IF EXISTS `lease`;
CREATE TABLE `lease`  (
  `leaseid` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '借阅编号',
  `Cardid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '借书证编号',
  `bookid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图书编号',
  `borrowdate` datetime(0) NOT NULL COMMENT '借阅时间',
  `returndate` datetime(0) NULL DEFAULT NULL COMMENT '归还时间',
  `Empid1` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '借书员工编号',
  `Empid2` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '归还员工编号',
  PRIMARY KEY (`leaseid`) USING BTREE,
  INDEX `fk_Cardid`(`Cardid`) USING BTREE,
  INDEX `fk_bookid`(`bookid`) USING BTREE,
  INDEX `fk_empid1`(`Empid1`) USING BTREE,
  INDEX `fk_empid2`(`Empid2`) USING BTREE,
  CONSTRAINT `fk_bookid` FOREIGN KEY (`bookid`) REFERENCES `book` (`Bookid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_Cardid` FOREIGN KEY (`Cardid`) REFERENCES `librarycard` (`Cardid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_empid1` FOREIGN KEY (`Empid1`) REFERENCES `employee` (`Empid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_empid2` FOREIGN KEY (`Empid2`) REFERENCES `employee` (`Empid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lease
-- ----------------------------
INSERT INTO `lease` VALUES ('lease202080001', 'card00003', 'book00001', '2020-08-16 17:02:39', '2020-08-16 17:16:53', 'emp2020001', 'emp2017001');
INSERT INTO `lease` VALUES ('lease202080002', 'card00001', 'book00001', '2020-08-16 17:22:46', NULL, 'emp2017001', NULL);

-- ----------------------------
-- Table structure for librarycard
-- ----------------------------
DROP TABLE IF EXISTS `librarycard`;
CREATE TABLE `librarycard`  (
  `Cardid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '借书证编号',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '读者姓名',
  `state` int(0) NULL DEFAULT NULL COMMENT '借书证状态',
  `number` int(0) NULL DEFAULT NULL COMMENT '已借书的数量',
  `totalNumber` int(0) NULL DEFAULT 3,
  PRIMARY KEY (`Cardid`) USING BTREE,
  INDEX `Cardid`(`Cardid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of librarycard
-- ----------------------------
INSERT INTO `librarycard` VALUES ('card00001', '许仙', 1, 1, 1);
INSERT INTO `librarycard` VALUES ('card00002', '小秋', 1, 0, 3);
INSERT INTO `librarycard` VALUES ('card00003', 'a', 2, 0, 3);
INSERT INTO `librarycard` VALUES ('card00004', 'a', 2, 0, 3);
INSERT INTO `librarycard` VALUES ('card00005', 'a', 1, 0, 3);

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `memid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `memname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `state` int(0) NOT NULL,
  `grade` int(0) NOT NULL,
  PRIMARY KEY (`memid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of member
-- ----------------------------
INSERT INTO `member` VALUES ('id00001', '许仙', 1, 1);
INSERT INTO `member` VALUES ('id00002', 'aa', 1, 3);
INSERT INTO `member` VALUES ('id00003', '0', 2, 1);
INSERT INTO `member` VALUES ('id00004', '00', 1, 1);
INSERT INTO `member` VALUES ('id00005', 'xu', 1, 1);
INSERT INTO `member` VALUES ('id00006', '小秋', 1, 1);
INSERT INTO `member` VALUES ('id00007', 'q', 1, 2);

-- ----------------------------
-- Table structure for question_info
-- ----------------------------
DROP TABLE IF EXISTS `question_info`;
CREATE TABLE `question_info`  (
  `questionId` int(0) NOT NULL COMMENT '主键，试题编号',
  `question` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '题干',
  `optionA` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '选项A',
  `optionB` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '选项B',
  `optionC` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '选项C',
  `optionD` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '选项D',
  `subject` int(0) NOT NULL COMMENT '科目（1代表java，2代表C#，3代表JSP）',
  `answer` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '正确答案',
  PRIMARY KEY (`questionId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question_info
-- ----------------------------
INSERT INTO `question_info` VALUES (1, '2', '1', '1', '1', '1', 0, 'a');
INSERT INTO `question_info` VALUES (2, 'java是什么', 'java', '咖啡', '语言', '掉头发', 1, 'D');
INSERT INTO `question_info` VALUES (3, 'jsp由什么构成', 'jsp是一个单词', 'jsp是由J、S、P组成的', 'jsp就是jsp', 'jsp是底层网页语言', 3, 'B');

-- ----------------------------
-- Table structure for upanddown
-- ----------------------------
DROP TABLE IF EXISTS `upanddown`;
CREATE TABLE `upanddown`  (
  `udid` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '上下机编号',
  `memid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会员编号',
  `cptid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '电脑编号',
  `update` datetime(0) NOT NULL COMMENT '上机时间',
  `downdate` datetime(0) NULL DEFAULT NULL COMMENT '下机时间',
  `money` decimal(6, 2) NULL DEFAULT NULL COMMENT '上网金额',
  `empid1` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ' 员工编号(办理上机ID)',
  `empid2` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '员工编号(办理下机ID)',
  PRIMARY KEY (`udid`) USING BTREE,
  INDEX `FK_memid`(`memid`) USING BTREE,
  INDEX `FK_cptid`(`cptid`) USING BTREE,
  INDEX `FK_empid11`(`empid1`) USING BTREE,
  INDEX `FK_empid12`(`empid2`) USING BTREE,
  CONSTRAINT `FK_cptid` FOREIGN KEY (`cptid`) REFERENCES `computer` (`cptid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_empid11` FOREIGN KEY (`empid1`) REFERENCES `employee` (`Empid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_empid12` FOREIGN KEY (`empid2`) REFERENCES `employee` (`Empid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_memid` FOREIGN KEY (`memid`) REFERENCES `member` (`memid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of upanddown
-- ----------------------------
INSERT INTO `upanddown` VALUES ('emp', 'id00001', 'cpt04', '2020-08-16 19:30:00', '2020-08-17 23:56:54', 50.00, 'emp2017001', 'emp2020001');
INSERT INTO `upanddown` VALUES ('ud2020081001', 'id00005', 'cpt03', '2020-08-17 22:19:52', '2020-08-18 00:00:29', 0.00, 'emp2020001', 'emp2020002');
INSERT INTO `upanddown` VALUES ('ud2020081002', 'id00001', 'cpt03', '2020-08-17 22:23:42', '2020-08-17 23:57:39', 50.00, 'emp2017001', 'emp2020001');
INSERT INTO `upanddown` VALUES ('ud2020082001', 'id00005', 'cpt03', '2020-08-18 00:02:26', '2020-08-18 15:10:52', 30.27, 'emp2020002', 'emp2020002');
INSERT INTO `upanddown` VALUES ('ud2020082002', 'id00005', 'cpt02', '2020-08-18 15:11:44', '2020-08-18 15:11:46', 0.00, 'emp2020002', 'emp2020002');
INSERT INTO `upanddown` VALUES ('ud2020082003', 'id00005', 'cpt01', '2020-08-18 15:40:00', '2020-08-18 15:51:27', 0.37, 'emp2020002', 'emp2020002');
INSERT INTO `upanddown` VALUES ('ud2020082004', 'id00001', 'cpt02', '2020-08-18 15:51:38', '2020-08-18 15:51:45', 0.00, 'emp2020002', 'emp2020002');
INSERT INTO `upanddown` VALUES ('ud2020082005', 'id00001', 'cpt03', '2020-08-18 15:52:13', '2020-08-18 17:02:57', 2.33, 'emp2017001', 'emp2020002');
INSERT INTO `upanddown` VALUES ('ud2020083001', 'id00005', 'cpt02', '2020-08-19 08:44:50', '2020-08-19 08:46:16', 0.03, 'emp2020002', 'emp2020002');
INSERT INTO `upanddown` VALUES ('ud2020083002', 'id00005', 'cpt02', '2020-08-19 08:46:54', NULL, NULL, 'emp2020002', NULL);
INSERT INTO `upanddown` VALUES ('ud2020083003', 'id00001', 'cpt05', '2020-08-19 08:47:45', NULL, NULL, 'emp2020002', NULL);
INSERT INTO `upanddown` VALUES ('ud2020083004', 'id00004', 'cpt01', '2020-08-19 08:50:50', NULL, NULL, 'emp2020002', NULL);
INSERT INTO `upanddown` VALUES ('ud2020083005', 'id00002', 'cpt06', '2020-08-19 08:51:17', NULL, NULL, 'emp2020002', NULL);
INSERT INTO `upanddown` VALUES ('ud2020083006', 'id00006', 'cpt03', '2020-08-19 11:11:56', NULL, NULL, 'emp2020002', NULL);

SET FOREIGN_KEY_CHECKS = 1;
