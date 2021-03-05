BEGIN;
-- ----------------------------
-- Records of message_config
-- ----------------------------
INSERT INTO `message_config` VALUES (2, b'1', b'1', b'1', '19', '管理员', '2020-12-06 16:32:15', '2021-01-10 16:57:49');
INSERT INTO `message_config` VALUES (15, b'0', b'0', b'1', '21', '测试用户', '2021-02-25 14:57:44', '2021-02-25 14:57:44');
-- ----------------------------
-- Records of monitor_data_threshold
-- ----------------------------
INSERT INTO `monitor_data_threshold` VALUES (1, 'gas', 'CO浓度', 'gas', '0.1', '2021-01-10 11:59:15', '2021-01-10 12:00:22');
INSERT INTO `monitor_data_threshold` VALUES (2, 'humidity', '大气压力', 'humidity', '30', '2021-01-10 11:59:15', '2021-01-10 12:00:22');
INSERT INTO `monitor_data_threshold` VALUES (3, 'temp', '温度', 'temp', '40', '2021-01-10 11:59:15', '2021-01-10 18:34:16');
INSERT INTO `monitor_data_threshold` VALUES (4, 'wind', '风力', 'wind', '40', '2021-01-10 11:59:15', '2021-01-10 12:00:24');
INSERT INTO `monitor_data_threshold` VALUES (5, 'wave', '震动', 'wave', '0.5', '2021-01-10 11:59:15', '2021-01-10 12:00:25');
INSERT INTO `monitor_data_threshold` VALUES (6, 'voicePress', '声压', 'voicePress', '0.5', '2021-01-10 11:59:15', '2021-01-10 12:00:26');
INSERT INTO `monitor_data_threshold` VALUES (7, 'electricalTemperature', '电器温度', 'electricalTemperature', '50', '2021-01-10 11:59:15', '2021-01-10 12:00:26');
INSERT INTO `monitor_data_threshold` VALUES (8, 'residualCurrent', '剩余电流', 'residualCurrent', '10', '2021-01-10 11:59:15', '2021-03-04 10:16:14');
-- ----------------------------
-- Records of pwd_info
-- ----------------------------
INSERT INTO `pwd_info` VALUES (5, 19, 'admin', '123456', '2020-12-02 23:17:39', '2020-12-06 10:10:53');
INSERT INTO `pwd_info` VALUES (7, 21, 'testUser', '123456', '2021-01-10 19:37:06', '2021-01-10 19:37:06');
-- ----------------------------
-- Records of room_info
-- ----------------------------
INSERT INTO `room_info` VALUES (1, 'room300', '300实验室', '覃奔', '2020-12-01 00:52:21', '2020-12-01 00:52:21');
INSERT INTO `room_info` VALUES (2, 'room309', '309实验室', '覃奔', '2020-12-01 00:58:05', '2020-12-01 00:58:05');
-- ----------------------------
-- Records of terminal_info
-- ----------------------------
INSERT INTO `terminal_info` VALUES (1, '5b8e5ffc4cd04967b3c8953a72b58ea9', '热成像A', '1', 'camera', 'hik', '1', '300', '2020-11-30 23:59:29', '2020-11-30 23:59:29');
INSERT INTO `terminal_info` VALUES (2, '2f6d23d6ee49470f9fbdb6b0fbd7ea40', 'CO传感器', '1', 'sensor', 'hik', '1', '300', '2020-11-30 23:59:29', '2020-11-30 23:59:29');
INSERT INTO `terminal_info` VALUES (3, '7c9aa22fde9e4f9ca71bf5926fd6bc59', '气压传感器', '1', 'sensor', 'hik', '1', '300', '2020-11-30 23:59:29', '2020-11-30 23:59:29');
INSERT INTO `terminal_info` VALUES (4, 'aad8a4639b8546869159abcfbf642e68', '温度传感器', '1', 'sensor', 'hik', '1', '300', '2020-11-30 23:59:29', '2020-11-30 23:59:29');
INSERT INTO `terminal_info` VALUES (5, '60a42fa0dc484a8d91be00fa862c1245', '风速传感器', '1', 'sensor', 'hik', '1', '300', '2020-11-30 23:59:29', '2020-11-30 23:59:29');
INSERT INTO `terminal_info` VALUES (7, '4ae8d4e3051a457dbc7929c938a097d5', '热成像B', '1', 'camera', 'hik', '1', '300', '2020-11-30 23:59:29', '2020-11-30 23:59:29');
INSERT INTO `terminal_info` VALUES (8, '677351f315f540f5922c36bd4eea7d9f', 'Camera 01', '1', 'camera', 'hik', '1', '300', '2020-11-30 23:59:29', '2020-11-30 23:59:29');
INSERT INTO `terminal_info` VALUES (9, 'de1274e80beb49838207ff2bc942cbd5', 'AI相机01', '1', 'camera', 'hik', '1', '300', '2020-11-30 23:59:29', '2020-11-30 23:59:29');
INSERT INTO `terminal_info` VALUES (10, 'eca9e1993abe4488bacb875fd68e5935', 'A300人脸抓拍', '1', 'camera', 'hik', '1', '300', '2020-11-30 23:59:29', '2020-11-30 23:59:29');
INSERT INTO `terminal_info` VALUES (11, '8351c0382e4c4778b982b253c7df70ec', 'A309人脸抓拍', '1', 'camera', 'hik', '2', '309', '2020-11-30 23:59:29', '2021-01-10 18:21:17');
INSERT INTO `terminal_info` VALUES (12, 'a8aa8127fbd747169d94f92ec8772ca7', 'A300智能警戒', '1', 'camera', 'hik', '1', '300', '2020-11-30 23:59:29', '2020-11-30 23:59:29');
INSERT INTO `terminal_info` VALUES (13, '8586bfc488fa45b49c003aa41373e7eb', 'A309智能警戒', '1', 'camera', 'hik', '2', '309', '2020-11-30 23:59:29', '2021-01-10 18:21:10');
INSERT INTO `terminal_info` VALUES (14, '908e98f1f0164bff9beb48d5cdfbc911', 'A300机房智能警戒', '1', 'camera', 'hik', '1', '300', '2020-11-30 23:59:29', '2020-11-30 23:59:29');
INSERT INTO `terminal_info` VALUES (15, '87bd8b5f78a54db7989b9feb70f57b3f', 'A309烟雾报警', '1', 'camera', 'hik', '2', '309', '2020-11-30 23:59:29', '2021-01-10 18:21:14');
INSERT INTO `terminal_info` VALUES (16, 'DH-PRESS', '声压传感器', '1', 'sensor', 'dh', '1', '300', '2020-11-30 23:59:29', '2020-11-30 23:59:29');
INSERT INTO `terminal_info` VALUES (17, 'DH-WAVE', '震动传感器', '1', 'sensor', 'dh', '1', '300', '2020-11-30 23:59:29', '2020-11-30 23:59:29');
-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (19, 'admin', '管理员', NULL, '男', '18678898329', '836623340@qq.com', '2020-12-02 23:17:39', '2021-01-10 16:04:03');
INSERT INTO `user_info` VALUES (21, 'testUser', '测试用户', '', '', '18678890002', '18678890002@qq.com', '2021-01-10 19:37:06', '2021-01-10 19:37:06');
-- ----------------------------
-- Records of warning_type
-- ----------------------------
INSERT INTO `warning_type` VALUES (1, 'intrusionWarning', '入侵预警', '2021-01-10 12:40:30', '2021-01-10 12:40:30');
INSERT INTO `warning_type` VALUES (2, 'sensorWarning', '传感器预警', '2021-01-10 12:40:30', '2021-01-10 12:40:44');
INSERT INTO `warning_type` VALUES (3, 'trendWarning', '趋势预警', '2021-01-10 12:40:30', '2021-01-10 12:40:45');
INSERT INTO `warning_type` VALUES (4, 'otherWarning', '其他预警', '2021-01-10 12:40:30', '2021-01-10 12:40:46');
COMMIT;
SET FOREIGN_KEY_CHECKS = 1;
