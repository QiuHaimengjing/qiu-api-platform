-- MySQL dump 10.13  Distrib 8.0.39, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: qiu
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `interface_info`
--

DROP TABLE IF EXISTS `interface_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `interface_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '接口id',
  `name` varchar(256) NOT NULL COMMENT '名称',
  `description` varchar(256) DEFAULT NULL COMMENT '描述',
  `host` varchar(512) NOT NULL COMMENT '服务器地址',
  `url` varchar(512) NOT NULL COMMENT '接口地址',
  `request_params` text COMMENT '请求参数',
  `request_header` text COMMENT '请求头',
  `response_header` text COMMENT '响应头',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '接口状态 （0-关闭，1-开启）',
  `method` varchar(256) NOT NULL COMMENT '请求类型',
  `user_id` bigint NOT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除 0 1（默认值0）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接口信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interface_info`
--

LOCK TABLES `interface_info` WRITE;
/*!40000 ALTER TABLE `interface_info` DISABLE KEYS */;
INSERT INTO `interface_info` VALUES (1,'判断闰年','输入一个年份判断是否为闰年','http://localhost:8083','/first/leap-year','{\"year\":2024}','Content-Type: application/x-www-form-urlencoded','content-type: text/plain;charset=UTF-8',1,'GET',1,'2024-12-18 13:26:29','2024-12-19 18:17:56',0),(2,'计算间隔天数','输入具体日期计算两个日期之间的间隔天数','http://localhost:8083','/first/date-interval','{\"startDate\":\"2001-10-19\",\"endDate\":\"2024-12-19\"}','Content-Type: application/x-www-form-urlencoded','Content-Type: text/plain;charset=UTF-8',1,'GET',1,'2024-12-19 18:25:20','2024-12-19 18:26:51',0),(3,'随机点名','从名单中抽取指定数量的结果','http://localhost:8083','/first/roll-call','{\"number\":3,\"participants\":[\"A\",\"B\",\"C\",\"D\",\"E\"]}','Content-Type: application/json','Content-Type: application/json',1,'POST',1,'2024-12-19 18:33:35','2024-12-19 18:33:55',0);
/*!40000 ALTER TABLE `interface_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_account` varchar(256) DEFAULT NULL COMMENT '账号',
  `username` varchar(256) DEFAULT NULL COMMENT '昵称',
  `avatar_url` varchar(1024) DEFAULT NULL COMMENT '头像',
  `gender` tinyint DEFAULT NULL COMMENT '性别，1-男，0-女',
  `user_password` varchar(512) NOT NULL COMMENT '密码',
  `salt` varchar(256) DEFAULT NULL COMMENT '加密盐',
  `phone` varchar(128) DEFAULT NULL COMMENT '电话',
  `email` varchar(512) DEFAULT NULL COMMENT '邮箱',
  `profile` varchar(1024) DEFAULT NULL COMMENT '用户简介',
  `tags` varchar(512) DEFAULT NULL COMMENT '用户标签（json字符串：["Java", "男"]）',
  `user_role` tinyint NOT NULL DEFAULT '0' COMMENT '角色 0-普通用户 1-管理员',
  `user_status` int NOT NULL DEFAULT '0' COMMENT '状态，0正常',
  `access_key` varchar(512) DEFAULT NULL COMMENT '用户唯一标识',
  `secret_key` varchar(512) DEFAULT NULL COMMENT '用户密钥',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除 0 1（默认值0）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'qiuqiu','邱海梦旌',NULL,NULL,'ccac018cf5c2166e5cda1b3f2bdb17d499bb6ad625051b84cfb0e9d0f4ada5ea','jxcn7gd3NmkIlOvXopurIQ==','12345678952','xxx@mail.com','嗨你好呀',NULL,1,0,'e96523faddecccf94ab40738dfc08f7ac708b37ebd03615226f1e3c156a53f87','8fb532cb5980f6d4da0876aaa1e5aef482d9d08739b3e95719d81d0bd5c82e1d','2024-12-18 11:12:09','2024-12-19 18:39:17',0),(2,'zzzz',NULL,NULL,NULL,'6e78c05101889a459b1480e7aa188678c83044a71e4b79ef8b0c5bbe7fae90a0','iel5TT1TiR7nDXcXNnRBug==',NULL,NULL,NULL,NULL,0,0,NULL,NULL,'2024-12-18 11:12:36','2024-12-18 11:12:36',0),(3,'qqqq',NULL,NULL,NULL,'40dcd1d124c8200efb4cea056ec6ed3c76bf5786cba977f37f674462daf74b20','UFY9akBfs1FR9SGb8zewuw==',NULL,NULL,NULL,NULL,0,0,NULL,NULL,'2024-12-18 11:12:39','2024-12-18 11:12:39',0),(4,'yyyy',NULL,NULL,NULL,'6b2fc9660d01ee6225db9f8bbd556944b8ac733d016d9351665d860026281172','Q0O6/NBNzq6JLHUdZOZSOQ==',NULL,NULL,NULL,NULL,0,0,NULL,NULL,'2024-12-18 11:12:43','2024-12-18 11:12:43',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_interface_info`
--

DROP TABLE IF EXISTS `user_interface_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_interface_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `interface_id` bigint NOT NULL COMMENT '接口id',
  `total_num` int NOT NULL DEFAULT '0' COMMENT '总调用次数',
  `left_num` int NOT NULL DEFAULT '0' COMMENT '剩余调用次数',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '调用状态 （0-正常，1-禁用）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除 0 1（默认值0）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户接口调用关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_interface_info`
--

LOCK TABLES `user_interface_info` WRITE;
/*!40000 ALTER TABLE `user_interface_info` DISABLE KEYS */;
INSERT INTO `user_interface_info` VALUES (1,1,1,11,39,0,'2024-12-18 13:49:30','2024-12-19 18:37:14',0),(2,1,2,1,49,0,'2024-12-19 18:27:13','2024-12-19 18:27:17',0),(3,1,3,5,45,0,'2024-12-19 18:34:15','2024-12-19 18:37:03',0);
/*!40000 ALTER TABLE `user_interface_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-19 20:48:27
