/*
Navicat MySQL Data Transfer

Source Server         : gds
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : payment_gateway

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-05-03 08:30:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for pg_payment_order
-- ----------------------------
DROP TABLE IF EXISTS `pg_payment_order`;
CREATE TABLE `pg_payment_order` (
  `id` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `create_by` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `amount` double(11,2) DEFAULT NULL,
  `tittle` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `method` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `status` enum('CREATE','PAY','SUCCESS','COMPLETE') COLLATE utf8_unicode_ci DEFAULT NULL,
  `detail` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `out_trade_no` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `trade_no` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of pg_payment_order
-- ----------------------------
INSERT INTO `pg_payment_order` VALUES ('12e22304-2d8b-11e7-bf92-c85b7636065d', 'aaaa', null, null, null, '10.00', '測試支付', 'ALIPAY', 'CREATE', null, '20170430175454856', null);
INSERT INTO `pg_payment_order` VALUES ('2720aea9-2d94-11e7-bf92-c85b7636065d', 'aaaabbb', null, null, null, '10.00', '测试支付', 'ALIPAY', 'CREATE', null, '20170430185954709', null);
INSERT INTO `pg_payment_order` VALUES ('38b377fc-2d91-11e7-bf92-c85b7636065d', 'aaaa', null, null, null, '10.00', '測試支付', 'ALIPAY', 'CREATE', null, '20170430183855285', null);
INSERT INTO `pg_payment_order` VALUES ('47fa1dcd-2e4c-11e7-bf92-c85b7636065d', 'striassa', null, null, null, '10.00', 'a啊是大', 'ALIPAY', 'CREATE', null, '20170501165749607', null);
INSERT INTO `pg_payment_order` VALUES ('85d94ece-2d92-11e7-bf92-c85b7636065d', 'aaaa', null, null, null, '10.00', '測試支付', 'ALIPAY', 'CREATE', null, '20170430184814635', null);
INSERT INTO `pg_payment_order` VALUES ('92a0bdc0-2d91-11e7-bf92-c85b7636065d', 'aaaa', null, null, null, '10.00', '測試支付', 'ALIPAY', 'CREATE', null, '20170430184126575', null);
INSERT INTO `pg_payment_order` VALUES ('92c616c6-2d94-11e7-bf92-c85b7636065d', 'aaaabbb', null, null, null, '10.00', '测试支付', 'ALIPAY', 'CREATE', null, '20170430190254847', null);
INSERT INTO `pg_payment_order` VALUES ('a3d8e212-2e3e-11e7-bf92-c85b7636065d', 'striassa', null, null, null, '10.00', 'a啊是大', 'ALIPAY', 'CREATE', null, '20170501152010677', null);
INSERT INTO `pg_payment_order` VALUES ('a5e19aeb-2d8a-11e7-bf92-c85b7636065d', 'aaaa', null, null, null, '10.00', '測試支付', 'ALIPAY', 'CREATE', null, '20170430175152398', null);
INSERT INTO `pg_payment_order` VALUES ('b001054f-2d87-11e7-bf92-c85b7636065d', 'aaaa', null, null, null, '10.00', '測試支付', 'ALIPAY', 'CREATE', null, '20170430173040548', null);
INSERT INTO `pg_payment_order` VALUES ('ca720db5-2d93-11e7-bf92-c85b7636065d', 'aaaabbb', null, null, null, '10.00', '测试支付', 'ALIPAY', 'CREATE', null, '20170430185718828', null);
INSERT INTO `pg_payment_order` VALUES ('e60c0ad4-2d92-11e7-bf92-c85b7636065d', 'aaaabbb', null, null, null, '10.00', '测试支付', 'ALIPAY', 'CREATE', null, '20170430185055619', null);
INSERT INTO `pg_payment_order` VALUES ('ef39b611-2d90-11e7-bf92-c85b7636065d', 'aaaa', null, null, null, '10.00', '測試支付', 'ALIPAY', 'CREATE', null, '20170430183652429', null);

-- ----------------------------
-- Table structure for ws_card
-- ----------------------------
DROP TABLE IF EXISTS `ws_card`;
CREATE TABLE `ws_card` (
  `id` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '卡片名称',
  `number` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '卡片编号',
  `value` int(11) unsigned zerofill NOT NULL DEFAULT '00000000000' COMMENT '卡片值',
  `type` enum('CREDIT','DEBIT','VIP','CASH') COLLATE utf8_unicode_ci NOT NULL COMMENT '卡片类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of ws_card
-- ----------------------------
INSERT INTO `ws_card` VALUES ('27038f49b34e40b69b56862a187e6956', '会员卡aaa', 'a12342', '00000000100', 'VIP');
INSERT INTO `ws_card` VALUES ('34ea983fb3fd4fc696ce759a5839d91f', '会员卡aaa', 'a12342', '00000000029', 'VIP');
INSERT INTO `ws_card` VALUES ('5f84c58e2c3c48bda84a4af65c11d7af', '会员卡aaa', 'a12342', '00000000100', 'VIP');
INSERT INTO `ws_card` VALUES ('8bbd102e051d433983e3aa172ead629d', '会员卡aaa', 'a12342', '00000000100', 'VIP');
INSERT INTO `ws_card` VALUES ('ccc6c2057a1645c4b9c21023bd0a87af', '会员卡aaa', 'a12342', '00000000100', 'VIP');
INSERT INTO `ws_card` VALUES ('UUIDUUID', '现金卡', '1234567890', '00000000100', 'CASH');
