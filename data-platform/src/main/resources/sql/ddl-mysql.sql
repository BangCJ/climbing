

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for important_people_info
-- ----------------------------
DROP TABLE IF EXISTS `important_people_info`;
CREATE TABLE `important_people_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `bkg_url` varchar(255) DEFAULT NULL,
  `snap_url` varchar(255) DEFAULT NULL,
  `bkg_url_bak` varchar(255) DEFAULT NULL,
  `snap_url_bak` varchar(255) DEFAULT NULL,
  `bkg_url_picture_name_bak` varchar(255) DEFAULT NULL,
  `snap_url_picture_name_bak` varchar(255) DEFAULT NULL,
  `total_similar` bigint(255) DEFAULT NULL,
  `camera_index_code` varchar(255) DEFAULT NULL,
  `camera_name` varchar(255) DEFAULT NULL,
  `device_index_code` varchar(255) DEFAULT NULL,
  `device_name` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `age` varchar(255) DEFAULT NULL,
  `glass` varchar(255) DEFAULT NULL,
  `event_time` varchar(255) DEFAULT NULL,
  `data_time` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `name` (`name`) USING BTREE,
  KEY `date_time` (`data_time`) USING BTREE,
  KEY `url` (`bkg_url`) USING BTREE,
  KEY `url2` (`snap_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=208 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for login_log
-- ----------------------------
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_userid` int(222) DEFAULT NULL,
  `login_username` varchar(255) DEFAULT NULL,
  `login_ip` varchar(255) DEFAULT NULL,
  `login_time` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for message_config
-- ----------------------------
DROP TABLE IF EXISTS `message_config`;
CREATE TABLE `message_config` (
  `id` int(111) NOT NULL AUTO_INCREMENT,
  `email_enable` bit(1) DEFAULT NULL,
  `sms_enable` bit(1) DEFAULT NULL,
  `voice_enable` bit(1) DEFAULT NULL,
  `receiver_id` varchar(255) DEFAULT NULL,
  `receiver_name` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for message_info
-- ----------------------------
DROP TABLE IF EXISTS `message_info`;
CREATE TABLE `message_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `content` text,
  `msg_type` varchar(255) DEFAULT NULL,
  `msg_channel` varchar(255) DEFAULT NULL,
  `status` int(255) DEFAULT NULL,
  `sender` varchar(255) DEFAULT NULL,
  `receiver` varchar(255) DEFAULT NULL,
  `send_time` varchar(0) DEFAULT NULL,
  `ts` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for message_limit
-- ----------------------------
DROP TABLE IF EXISTS `message_limit`;
CREATE TABLE `message_limit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for monitor_data
-- ----------------------------
DROP TABLE IF EXISTS `monitor_data`;
CREATE TABLE `monitor_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(255) DEFAULT NULL COMMENT '编码',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `monitor_type` varchar(255) DEFAULT NULL COMMENT '监控类型',
  `unit` varchar(255) DEFAULT NULL COMMENT '计量单位',
  `value` varchar(255) DEFAULT NULL COMMENT '数据',
  `origin_data_time` varchar(255) DEFAULT NULL COMMENT '数据采集时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `type` (`monitor_type`) USING BTREE,
  KEY `name` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=92493 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for monitor_data_threshold
-- ----------------------------
DROP TABLE IF EXISTS `monitor_data_threshold`;
CREATE TABLE `monitor_data_threshold` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pwd_info
-- ----------------------------
DROP TABLE IF EXISTS `pwd_info`;
CREATE TABLE `pwd_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `user_code` varchar(255) DEFAULT NULL,
  `pwd` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for room_frequence
-- ----------------------------
DROP TABLE IF EXISTS `room_frequence`;
CREATE TABLE `room_frequence` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `room_id` int(11) DEFAULT NULL,
  `check_date` varchar(255) DEFAULT NULL,
  `times` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for room_info
-- ----------------------------
DROP TABLE IF EXISTS `room_info`;
CREATE TABLE `room_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `owner` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for room_used_time
-- ----------------------------
DROP TABLE IF EXISTS `room_used_time`;
CREATE TABLE `room_used_time` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room_id` int(11) DEFAULT NULL,
  `time_length` bigint(20) DEFAULT NULL,
  `earlier_time` varchar(255) DEFAULT NULL,
  `later_time` varchar(255) DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for stranger_info
-- ----------------------------
DROP TABLE IF EXISTS `stranger_info`;
CREATE TABLE `stranger_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `times` varchar(255) DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `data_time` varchar(255) DEFAULT NULL,
  `bkg_url` varchar(255) DEFAULT NULL,
  `snap_url` varchar(255) DEFAULT NULL,
  `bkg_url_bak` varchar(255) DEFAULT NULL,
  `snap_url_bak` varchar(255) DEFAULT NULL,
  `bkg_url_picture_name_bak` varchar(255) DEFAULT NULL,
  `snap_url_picture_name_bak` varchar(255) DEFAULT NULL,
  `bkg_data` longtext,
  `snap_data` longtext,
  `total_similar` bigint(20) DEFAULT NULL,
  `camera_indexcode` varchar(255) DEFAULT NULL,
  `camera_name` varchar(255) DEFAULT NULL,
  `device_indexcode` varchar(255) DEFAULT NULL,
  `device_name` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `age` varchar(255) DEFAULT NULL,
  `glass` varchar(255) DEFAULT NULL,
  `event_time` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `data_time` (`data_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=717 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for terminal_info
-- ----------------------------
DROP TABLE IF EXISTS `terminal_info`;
CREATE TABLE `terminal_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `groupInfo` varchar(255) DEFAULT NULL,
  `room_id` varchar(255) DEFAULT NULL,
  `room_name` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `type` (`type`) USING BTREE,
  KEY `group` (`groupInfo`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `id_card` varchar(255) DEFAULT NULL,
  `sex` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for warning_info
-- ----------------------------
DROP TABLE IF EXISTS `warning_info`;
CREATE TABLE `warning_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `warning_content` text,
  `warning_type` varchar(255) DEFAULT NULL,
  `warning_time` varchar(255) DEFAULT NULL,
  `warning_area` varchar(255) DEFAULT NULL,
  `warning_attach` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `ts` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `warning_time` (`warning_time`)
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for warning_type
-- ----------------------------
DROP TABLE IF EXISTS `warning_type`;
CREATE TABLE `warning_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
