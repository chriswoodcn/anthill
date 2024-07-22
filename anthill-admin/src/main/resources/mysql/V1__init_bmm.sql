/*
 Navicat Premium Dump SQL

 Source Server         : dell_mysql8
 Source Server Type    : MySQL
 Source Server Version : 80037 (8.0.37)
 Source Host           : 192.168.1.188:3307
 Source Schema         : bmm

 Target Server Type    : MySQL
 Target Server Version : 80037 (8.0.37)
 File Encoding         : 65001

 Date: 22/07/2024 16:28:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for flyway_schema_history
-- ----------------------------
DROP TABLE IF EXISTS `flyway_schema_history`;
CREATE TABLE `flyway_schema_history`  (
  `installed_rank` int NOT NULL,
  `version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `script` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `checksum` int NULL DEFAULT NULL,
  `installed_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`) USING BTREE,
  INDEX `flyway_schema_history_s_idx`(`success` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flyway_schema_history
-- ----------------------------
INSERT INTO `flyway_schema_history` VALUES (1, '1', '<< Flyway Baseline >>', 'BASELINE', '<< Flyway Baseline >>', NULL, 'root', '2024-07-10 00:56:21', 0, 1);

-- ----------------------------
-- Table structure for sys_company
-- ----------------------------
DROP TABLE IF EXISTS `sys_company`;
CREATE TABLE `sys_company`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户ID',
  `company_name_json` json NULL COMMENT '客户名称JSON',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态 0-正常 1-停用 2-删除',
  `template_id` int NULL DEFAULT NULL COMMENT '权限模板ID',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `active_time` bigint NULL DEFAULT NULL COMMENT '有效截止时间',
  `version` int NULL DEFAULT 0 COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统厂商' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_company
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `config_name_json` json NULL COMMENT '参数名称JSON',
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数键值',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态 0-正常 1-停用 2-删除',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `remark_json` json NULL COMMENT '备注JSON',
  `version` int NULL DEFAULT 0 COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统参数' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '{\"en\": \"Backend User Default Password\", \"zh\": \"后管用户默认密码\"}', 'sys.backend.user.defaultPwd', '123456.', '0', 1720513869131, 1720513869131, NULL, 0);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label_json` json NULL COMMENT '字典标签JSON',
  `dict_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态 0-正常 1-停用 2-删除',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `remark_json` json NULL COMMENT '备注JSON',
  `version` int NULL DEFAULT 0 COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 110 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 0, '{\"en\": \"Male\", \"zh\": \"男\"}', '0', 'sys_sex', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2, 1, '{\"en\": \"Female\", \"zh\": \"女\"}', '1', 'sys_sex', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (3, 2, '{\"en\": \"Unknow\", \"zh\": \"未知\"}', '2', 'sys_sex', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (4, 0, '{\"en\": \"Normal\", \"zh\": \"正常\"}', '0', 'sys_status', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (5, 1, '{\"en\": \"Disabled\", \"zh\": \"停用\"}', '1', 'sys_status', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (6, 2, '{\"en\": \"Deleted\", \"zh\": \"删除\"}', '2', 'sys_status', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (7, 3, '{\"en\": \"Buildin\", \"zh\": \"内置数据\"}', '3', 'sys_status', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (8, 0, '{\"en\": \"No\", \"zh\": \"否\"}', '0', 'sys_yes_no', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (9, 1, '{\"en\": \"Yes\", \"zh\": \"是\"}', '1', 'sys_yes_no', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (101, 0, '{\"en\": \"Chinese\", \"zh\": \"中文\"}', 'zh', 'sys_lang', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (102, 1, '{\"en\": \"English\", \"zh\": \"英文\"}', 'en', 'sys_lang', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (103, 2, '{\"en\": \"Spanish\", \"zh\": \"西班牙语\"}', 'es', 'sys_lang', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (104, 3, '{\"en\": \"French\", \"zh\": \"法语\"}', 'fr', 'sys_lang', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (105, 4, '{\"en\": \"German\", \"zh\": \"德语\"}', 'de', 'sys_lang', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (106, 5, '{\"en\": \"Italian\", \"zh\": \"意大利语\"}', 'it', 'sys_lang', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (107, 6, '{\"en\": \"Czech\", \"zh\": \"捷克语\"}', 'cs', 'sys_lang', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (108, 7, '{\"en\": \"Polish\", \"zh\": \"波兰语\"}', 'pl', 'sys_lang', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (109, 8, '{\"en\": \"Protueguess\", \"zh\": \"葡萄牙语\"}', 'pt', 'sys_lang', '0', 1720513869131, 1720513869131, NULL, 0);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dict_name_json` json NULL COMMENT '字典名称JSON',
  `dict_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态 0-正常 1-停用 2-删除',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `remark_json` json NULL COMMENT '备注JSON',
  `version` int NULL DEFAULT 0 COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '{\"en\": \"Gender\", \"zh\": \"性别\"}', 'sys_sex', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (2, '{\"en\": \"Status\", \"zh\": \"状态\"}', 'sys_status', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (3, '{\"en\": \"IsOrNot\", \"zh\": \"是否\"}', 'sys_yes_no', '0', 1720513869131, 1720513869131, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (4, '{\"en\": \"Language\", \"zh\": \"语言\"}', 'sys_lang', '0', 1720513869131, 1720513869131, NULL, 0);

-- ----------------------------
-- Table structure for sys_login_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_info`;
CREATE TABLE `sys_login_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '登录状态 0-成功 1-失败',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` bigint NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统访问日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_login_info
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name_json` json NULL COMMENT '菜单名称JSON',
  `parent_id` int NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '菜单类型 M目录 C菜单 F按钮',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态0正常 1停用 2删除 3系统数据',
  `perms` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `remark_json` json NULL COMMENT '备注JSON',
  `menu_version` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '菜单版本',
  `hidden_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态 0-显示 1-隐藏',
  `frame_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否为外链 0-否 1-是',
  `affiliate_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '菜单归属 0-超管 1-系统用户 2-客户',
  `version` int NULL DEFAULT 0 COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 222 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '{\"en\": \"SysManage\", \"zh\": \"系统管理\"}', 0, 0, 'system', NULL, 'M', '3', NULL, '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (2, '{\"en\": \"SysUser\", \"zh\": \"用户管理\"}', 0, 1, 'user', NULL, 'M', '3', NULL, '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (3, '{\"en\": \"SysMonitor\", \"zh\": \"系统监控\"}', 0, 2, 'monitor', NULL, 'M', '3', NULL, '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (4, '{\"en\": \"SysTool\", \"zh\": \"系统工具\"}', 0, 3, 'tool', NULL, 'M', '3', NULL, '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (51, '{\"en\": \"UserList\", \"zh\": \"用户列表\"}', 2, 0, 'list', NULL, 'C', '3', 'sys:user:list', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (52, '{\"en\": \"UserRole\", \"zh\": \"用户角色\"}', 2, 0, 'role', NULL, 'C', '3', 'sys:role:list', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (53, '{\"en\": \"Menu\", \"zh\": \"系统菜单\"}', 1, 0, 'menu', NULL, 'C', '3', 'sys:menu:list', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (54, '{\"en\": \"Dict\", \"zh\": \"系统字典\"}', 1, 0, 'dict', NULL, 'C', '3', 'sys:dict:list', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (55, '{\"en\": \"Param\", \"zh\": \"系统参数\"}', 1, 0, 'param', NULL, 'C', '3', 'sys:param:list', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (56, '{\"en\": \"Config\", \"zh\": \"系统配置\"}', 1, 0, 'config', NULL, 'C', '2', 'sys:config:list', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (57, '{\"en\": \"OnlineUser\", \"zh\": \"在线用户\"}', 3, 0, 'online', NULL, 'C', '3', 'sys:online:list', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (58, '{\"en\": \"LoginLog\", \"zh\": \"登录日志\"}', 3, 0, 'loginLog', NULL, 'C', '3', 'sys:login:list', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (59, '{\"en\": \"OperLog\", \"zh\": \"操作日志\"}', 3, 0, 'operLog', NULL, 'C', '3', 'sys:oper:list', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (60, '{\"en\": \"Job\", \"zh\": \"任务中心\"}', 4, 0, 'https://prod.taotaozn.com/fitness-xxl-job-admin', NULL, 'C', '3', 'sys:job:list', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '1', '0', 0);
INSERT INTO `sys_menu` VALUES (61, '{\"en\": \"GenForm\", \"zh\": \"表单构建\"}', 4, 0, 'genForm', NULL, 'C', '2', 'sys:genForm:list', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (62, '{\"en\": \"GenCode\", \"zh\": \"代码生成\"}', 4, 0, 'genCode', NULL, 'C', '2', 'sys:genCode:list', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (201, '{\"en\": \"Query\", \"zh\": \"查询\"}', 51, 0, NULL, NULL, 'F', '0', 'sys:user:query', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (202, '{\"en\": \"Add\", \"zh\": \"新增\"}', 51, 0, NULL, NULL, 'F', '0', 'sys:user:add', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (203, '{\"en\": \"Update\", \"zh\": \"修改\"}', 51, 0, NULL, NULL, 'F', '0', 'sys:user:update', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (204, '{\"en\": \"Delete\", \"zh\": \"删除\"}', 51, 0, NULL, NULL, 'F', '0', 'sys:user:delete', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (205, '{\"en\": \"Export\", \"zh\": \"导出\"}', 51, 0, NULL, NULL, 'F', '0', 'sys:user:export', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (206, '{\"en\": \"Query\", \"zh\": \"查询\"}', 52, 0, NULL, NULL, 'F', '0', 'sys:role:query', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (207, '{\"en\": \"Add\", \"zh\": \"新增\"}', 52, 0, NULL, NULL, 'F', '0', 'sys:role:add', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (208, '{\"en\": \"Update\", \"zh\": \"修改\"}', 52, 0, NULL, NULL, 'F', '0', 'sys:role:update', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (209, '{\"en\": \"Delete\", \"zh\": \"删除\"}', 52, 0, NULL, NULL, 'F', '0', 'sys:role:delete', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (210, '{\"en\": \"Export\", \"zh\": \"导出\"}', 52, 0, NULL, NULL, 'F', '0', 'sys:role:export', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (211, '{\"en\": \"Query\", \"zh\": \"查询\"}', 53, 0, NULL, NULL, 'F', '0', 'sys:menu:query', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (212, '{\"en\": \"Add\", \"zh\": \"新增\"}', 53, 0, NULL, NULL, 'F', '0', 'sys:menu:add', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (213, '{\"en\": \"Update\", \"zh\": \"修改\"}', 53, 0, NULL, NULL, 'F', '0', 'sys:menu:update', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (214, '{\"en\": \"Delete\", \"zh\": \"删除\"}', 53, 0, NULL, NULL, 'F', '0', 'sys:menu:delete', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (215, '{\"en\": \"Export\", \"zh\": \"导出\"}', 53, 0, NULL, NULL, 'F', '0', 'sys:menu:export', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (216, '{\"en\": \"Query\", \"zh\": \"查询\"}', 54, 0, NULL, NULL, 'F', '0', 'sys:dict:query', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (217, '{\"en\": \"Add\", \"zh\": \"新增\"}', 54, 0, NULL, NULL, 'F', '0', 'sys:dict:add', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (218, '{\"en\": \"Update\", \"zh\": \"修改\"}', 54, 0, NULL, NULL, 'F', '0', 'sys:dict:update', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (219, '{\"en\": \"Delete\", \"zh\": \"删除\"}', 54, 0, NULL, NULL, 'F', '0', 'sys:dict:delete', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (220, '{\"en\": \"Export\", \"zh\": \"导出\"}', 54, 0, NULL, NULL, 'F', '0', 'sys:dict:export', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);
INSERT INTO `sys_menu` VALUES (221, '{\"en\": \"Refresh\", \"zh\": \"刷新缓存\"}', 54, 0, NULL, NULL, 'F', '0', 'sys:dict:refresh', '#', 1720513869131, 1720513869131, NULL, '1.0.0', '0', '0', '0', 0);

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title_json` json NULL COMMENT '公告标题JSON',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告类型 1通知 2公告',
  `notice_content` json NULL COMMENT '公告内容JSON',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '公告状态 0正常 1关闭 2删除',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `remark_json` json NULL COMMENT '备注JSON',
  `version` int NULL DEFAULT 0 COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------

-- ----------------------------
-- Table structure for sys_notice_unread
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice_unread`;
CREATE TABLE `sys_notice_unread`  (
  `user_id` int NOT NULL COMMENT '主键 用户ID',
  `unread_notice_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '累计未读通知ID，逗号分割',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态 0-正常 1-停用 2-删除',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice_unread
-- ----------------------------

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '模块',
  `business_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '业务类型 0其它 1新增 2修改 3删除 ',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_user_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作类别 0其它 1-系统用户 2-app用户',
  `oper_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作人员',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '操作状态 0-成功 1-失败',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` bigint NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统操作日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name_json` json NULL COMMENT '角色名称JSON',
  `role_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '角色状态 0正常 1停用 2删除 3-系统数据',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `com_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司ID',
  `remark_json` json NULL COMMENT '备注JSON',
  `affiliate_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '角色归属 0-超管 1-系统用户 2-商户 3-商户模板',
  `version` int NULL DEFAULT 0 COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '{\"en\": \"SuperAdministrator\", \"zh\": \"超级管理员\"}', 'super-admin', 0, '3', 1720513869131, 1720513869131, NULL, NULL, '0', 0);
INSERT INTO `sys_role` VALUES (2, '{\"en\": \"SysAdministrator\", \"zh\": \"系统管理员\"}', 'sys-admin', 1, '3', 1720513869131, 1720513869131, NULL, NULL, '1', 0);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` int NOT NULL COMMENT '角色ID',
  `menu_id` int NOT NULL COMMENT '菜单ID'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1);
INSERT INTO `sys_role_menu` VALUES (1, 2);
INSERT INTO `sys_role_menu` VALUES (1, 3);
INSERT INTO `sys_role_menu` VALUES (1, 4);
INSERT INTO `sys_role_menu` VALUES (1, 51);
INSERT INTO `sys_role_menu` VALUES (1, 52);
INSERT INTO `sys_role_menu` VALUES (1, 53);
INSERT INTO `sys_role_menu` VALUES (1, 54);
INSERT INTO `sys_role_menu` VALUES (1, 55);
INSERT INTO `sys_role_menu` VALUES (1, 56);
INSERT INTO `sys_role_menu` VALUES (1, 57);
INSERT INTO `sys_role_menu` VALUES (1, 58);
INSERT INTO `sys_role_menu` VALUES (1, 59);
INSERT INTO `sys_role_menu` VALUES (1, 60);
INSERT INTO `sys_role_menu` VALUES (1, 61);
INSERT INTO `sys_role_menu` VALUES (1, 62);
INSERT INTO `sys_role_menu` VALUES (1, 201);
INSERT INTO `sys_role_menu` VALUES (1, 202);
INSERT INTO `sys_role_menu` VALUES (1, 203);
INSERT INTO `sys_role_menu` VALUES (1, 204);
INSERT INTO `sys_role_menu` VALUES (1, 205);
INSERT INTO `sys_role_menu` VALUES (1, 206);
INSERT INTO `sys_role_menu` VALUES (1, 207);
INSERT INTO `sys_role_menu` VALUES (1, 208);
INSERT INTO `sys_role_menu` VALUES (1, 209);
INSERT INTO `sys_role_menu` VALUES (1, 210);
INSERT INTO `sys_role_menu` VALUES (1, 211);
INSERT INTO `sys_role_menu` VALUES (1, 212);
INSERT INTO `sys_role_menu` VALUES (1, 213);
INSERT INTO `sys_role_menu` VALUES (1, 214);
INSERT INTO `sys_role_menu` VALUES (1, 215);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `nickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `user_type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '用户类型 0-超管 1-系统用户 2-商户',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `mobile` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '2' COMMENT '用户性别 0男 1女 2未知',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '帐号状态 0-正常 1-停用 2-删除 3-系统数据',
  `login_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后登陆IP',
  `login_time` bigint NULL DEFAULT NULL COMMENT '最后登陆时间',
  `create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `com_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司ID',
  `admin_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否管理员 0-否 1-是',
  `version` int NULL DEFAULT 0 COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '超级管理员', '0', 'project-dev@taotaozn.com', '15861880096', '2', '', '$2a$10$R5mqCs3/tJPrX3p49Eh.cOfA2TGSNa.boutZx3ps5Om4B.jReUUte', '0', '', NULL, 1720513869131, 1720513869131, NULL, '1', 0);
INSERT INTO `sys_user` VALUES (2, 'taotao', '系统管理员', '1', 'project-dev@taotaozn.com', '15861880096', '2', '', '$2a$10$R5mqCs3/tJPrX3p49Eh.cOfA2TGSNa.boutZx3ps5Om4B.jReUUte', '0', '', NULL, 1720513869131, 1720513869131, NULL, '1', 0);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` int NOT NULL COMMENT '用户ID',
  `role_id` int NOT NULL COMMENT '角色ID'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);

SET FOREIGN_KEY_CHECKS = 1;
