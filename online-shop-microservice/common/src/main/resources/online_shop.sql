/*
 Navicat Premium Data Transfer

 Source Server         : Online Shop
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : online_shop

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 30/05/2025 23:58:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for online_shop_address_book
-- ----------------------------
DROP TABLE IF EXISTS `online_shop_address_book`;
CREATE TABLE `online_shop_address_book`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '所属用户id',
  `consignee` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `sex` tinyint(4) NOT NULL DEFAULT 1 COMMENT '性别：1-先生；2-女士',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货手机号',
  `address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收获详细地址',
  `label` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址标签',
  `defaulted` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否是默认地址：0-否；1-是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of online_shop_address_book
-- ----------------------------
INSERT INTO `online_shop_address_book` VALUES (1, 1, '11大灰灰', 1, '15885166185', '11test地址', '学1123校', 1);
INSERT INTO `online_shop_address_book` VALUES (2, 1, '大灰灰', 1, '15885166185', 'test地址', '学校', 0);
INSERT INTO `online_shop_address_book` VALUES (4, 5, 'addd223', 1, '15888866185', 'addd223est地址', 'addd223', 1);
INSERT INTO `online_shop_address_book` VALUES (5, 8, 'test11', 1, '15885166185', '常州市金坛区吾悦广场', '家', 0);
INSERT INTO `online_shop_address_book` VALUES (6, 6, '231623', 2, '19851937737', '长荡湖校区1915', '学校', 1);
INSERT INTO `online_shop_address_book` VALUES (7, 9, 'sss', 1, '15885166185', 'ssss佛山电翰是', '公司', 1);
INSERT INTO `online_shop_address_book` VALUES (8, 8, 'test地址2', 1, '15885166185', '常州校区13号楼', '宿舍', 1);

-- ----------------------------
-- Table structure for online_shop_cart
-- ----------------------------
DROP TABLE IF EXISTS `online_shop_cart`;
CREATE TABLE `online_shop_cart`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '所属用户id',
  `product_id` int(11) NOT NULL COMMENT '商品id',
  `quantity` int(11) NOT NULL COMMENT '商品数量',
  `selected` tinyint(4) NOT NULL COMMENT '商品勾选状态：0-未选中；1-选中',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of online_shop_cart
-- ----------------------------

-- ----------------------------
-- Table structure for online_shop_category
-- ----------------------------
DROP TABLE IF EXISTS `online_shop_category`;
CREATE TABLE `online_shop_category`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类目录名称',
  `type` int(11) NOT NULL COMMENT '分类目录级别，1：一级目录，2：二级目录，3：三级目录',
  `parent_id` int(11) NOT NULL COMMENT '父目录id，即上级目录id，若为一级目录则为0',
  `order_num` int(11) NOT NULL COMMENT '目录展示时的顺序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of online_shop_category
-- ----------------------------
INSERT INTO `online_shop_category` VALUES (3, '新鲜水果', 1, 0, 1, '2025-05-04 20:20:26', '2025-05-04 20:20:26');
INSERT INTO `online_shop_category` VALUES (4, '橘子橙子', 2, 3, 1, '2025-05-04 20:20:26', '2025-05-04 20:20:26');
INSERT INTO `online_shop_category` VALUES (5, '海鲜水产', 1, 0, 2, '2025-05-04 20:20:26', '2025-05-04 20:20:26');
INSERT INTO `online_shop_category` VALUES (6, '精选肉类', 1, 0, 3, '2025-05-04 20:20:26', '2025-05-04 20:20:26');
INSERT INTO `online_shop_category` VALUES (7, '螃蟹', 2, 5, 1, '2025-05-04 20:20:26', '2025-05-04 20:20:26');
INSERT INTO `online_shop_category` VALUES (8, '鱼类', 2, 5, 2, '2025-05-04 20:20:26', '2025-05-04 20:20:26');
INSERT INTO `online_shop_category` VALUES (9, '冷饮冻食', 1, 0, 4, '2025-05-04 20:20:26', '2025-05-04 20:20:26');
INSERT INTO `online_shop_category` VALUES (10, '蔬菜蛋品', 1, 0, 5, '2025-05-04 20:20:26', '2025-05-04 20:20:26');
INSERT INTO `online_shop_category` VALUES (11, '草莓', 2, 3, 2, '2025-05-04 20:20:26', '2025-05-04 20:20:26');
INSERT INTO `online_shop_category` VALUES (12, '奇异果', 2, 3, 3, '2025-05-04 20:20:26', '2025-05-04 20:20:26');
INSERT INTO `online_shop_category` VALUES (13, '海参', 2, 5, 3, '2025-05-04 20:20:26', '2025-05-04 20:20:26');
INSERT INTO `online_shop_category` VALUES (14, '车厘子', 2, 3, 4, '2025-05-04 20:20:26', '2025-05-04 20:20:26');
INSERT INTO `online_shop_category` VALUES (15, '火锅食材', 2, 27, 5, '2025-05-04 20:20:26', '2025-05-04 20:20:26');
INSERT INTO `online_shop_category` VALUES (16, '牛羊肉', 2, 6, 1, '2025-05-04 20:20:26', '2025-05-04 20:20:26');
INSERT INTO `online_shop_category` VALUES (17, '冰淇淋', 2, 9, 1, '2025-05-04 20:20:26', '2025-05-04 20:20:26');
INSERT INTO `online_shop_category` VALUES (18, '蔬菜综合', 2, 10, 1, '2025-05-04 20:20:26', '2025-05-04 20:20:26');
INSERT INTO `online_shop_category` VALUES (19, '果冻橙', 3, 4, 1, '2025-05-04 20:20:26', '2025-05-04 20:20:26');
INSERT INTO `online_shop_category` VALUES (27, '美味菌菇', 1, 0, 7, '2025-05-04 20:20:26', '2025-05-04 20:20:26');
INSERT INTO `online_shop_category` VALUES (28, '其他水果', 2, 3, 4, '2025-05-04 20:20:26', '2025-05-04 20:20:26');
INSERT INTO `online_shop_category` VALUES (30, 'test', 2, 3, 10, '2025-05-06 00:29:32', '2025-05-06 00:33:08');
INSERT INTO `online_shop_category` VALUES (33, '鸭货2', 3, 6, 5, '2025-05-23 23:54:08', '2025-05-23 23:54:08');

-- ----------------------------
-- Table structure for online_shop_order
-- ----------------------------
DROP TABLE IF EXISTS `online_shop_order`;
CREATE TABLE `online_shop_order`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_num` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `user_id` int(11) NOT NULL COMMENT '所属用户id',
  `total_price` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `consignee` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人手机号',
  `address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人详细地址',
  `status` tinyint(4) NOT NULL DEFAULT 10 COMMENT '订单状态：0-已取消；10-待付款；20-已付款；30-已发货；40-已完成',
  `postage` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '运费',
  `pay_method` int(11) NULL DEFAULT NULL COMMENT '支付方式：1-支付宝；2-微信',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `cancel_reason` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '取消原因',
  `cancel_time` datetime NULL DEFAULT NULL COMMENT '取消时间',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '配送时间',
  `complete_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of online_shop_order
-- ----------------------------
INSERT INTO `online_shop_order` VALUES (1, '202505301928478623958962176', 8, 444.00, 'test地址2', '15885166185', '常州校区13号楼', 0, 0.00, 1, '2025-05-30 23:48:40', '买错了', '2025-05-30 23:48:53', NULL, NULL, '2025-05-30 23:48:28', '2025-05-30 23:48:28');
INSERT INTO `online_shop_order` VALUES (2, '202505301928478906785075200', 8, 464.00, 'test11', '15885166185', '常州市金坛区吾悦广场', 40, 0.00, 1, '2025-05-30 23:49:49', NULL, NULL, '2025-05-30 23:49:56', '2025-05-30 23:50:03', '2025-05-30 23:49:35', '2025-05-30 23:49:35');
INSERT INTO `online_shop_order` VALUES (3, '202505301928479215229997056', 8, 2131.00, 'test地址2', '15885166185', '常州校区13号楼', 10, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, '2025-05-30 23:50:49', '2025-05-30 23:50:49');
INSERT INTO `online_shop_order` VALUES (4, '202505301928479738146459648', 8, 222.00, 'test地址2', '15885166185', '常州校区13号楼', 0, 0.00, NULL, NULL, '不要了', '2025-05-30 23:53:30', NULL, NULL, '2025-05-30 23:52:53', '2025-05-30 23:52:53');
INSERT INTO `online_shop_order` VALUES (5, '202505301928479947245096960', 8, 100.00, 'test地址2', '15885166185', '常州校区13号楼', 20, 0.00, 1, '2025-05-30 23:54:11', NULL, NULL, NULL, NULL, '2025-05-30 23:53:43', '2025-05-30 23:53:43');

-- ----------------------------
-- Table structure for online_shop_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `online_shop_order_detail`;
CREATE TABLE `online_shop_order_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_num` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `product_id` int(11) NOT NULL COMMENT '商品id',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `product_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品图片',
  `unit_price` decimal(10, 0) NOT NULL COMMENT '单价（快照信息，即下单时的单价）',
  `quantity` int(11) NOT NULL COMMENT '数量',
  `total_price` decimal(10, 0) NOT NULL COMMENT '订单总金额',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of online_shop_order_detail
-- ----------------------------
INSERT INTO `online_shop_order_detail` VALUES (1, '202505301928478623958962176', 27, '内蒙古羔羊肉串 500g/袋（约20串）鲜冻羊肉串 BBQ烧烤食材', 'http://192.168.198.1:9002/images/83916258-c17a-4782-b2de-46ca7a8ffa8d.jpg', 222, 1, 222, '2025-05-30 23:48:28', '2025-05-30 23:48:28');
INSERT INTO `online_shop_order_detail` VALUES (2, '202505301928478623958962176', 28, '玛琪摩尔新西兰进口冰淇淋大桶装', 'http://192.168.198.1:9002/images/5d5e2063-18cc-4e45-98a7-be9d41b785e8.jpeg', 222, 1, 222, '2025-05-30 23:48:28', '2025-05-30 23:48:28');
INSERT INTO `online_shop_order_detail` VALUES (3, '202505301928478906785075200', 36, '四川果冻橙 吹弹可破', 'http://192.168.198.1:9002/images/40aec51e-b392-4cc7-810b-c4221ac20a3e.jpeg', 222, 2, 444, '2025-05-30 23:49:35', '2025-05-30 23:49:35');
INSERT INTO `online_shop_order_detail` VALUES (4, '202505301928478906785075200', 45, '樱桃', 'http://192.168.198.1:9002/images/5d32f9d2-9394-4461-aa11-70a4805d19a2.jpeg', 20, 1, 20, '2025-05-30 23:49:35', '2025-05-30 23:49:35');
INSERT INTO `online_shop_order_detail` VALUES (5, '202505301928479215229997056', 48, 'hei-小灰灰', 'http://192.168.198.1:9002/images/e512d741-a97d-47c2-bced-4e3de88f3885.jpeg', 2131, 1, 2131, '2025-05-30 23:50:49', '2025-05-30 23:50:49');
INSERT INTO `online_shop_order_detail` VALUES (6, '202505301928479738146459648', 37, '进口牛油果 中果6粒装 单果约130-160g', 'http://192.168.198.1:9002/images/2881fd09-f257-42f0-ae7e-3a23e8bc4f31.jpeg', 222, 1, 222, '2025-05-30 23:52:53', '2025-05-30 23:52:53');
INSERT INTO `online_shop_order_detail` VALUES (7, '202505301928479947245096960', 2, '澳洲进口大黑车厘子大樱桃包甜黑樱桃大果多汁 500g 特大果', 'http://192.168.198.1:9002/images/aa9cde81-5d46-4f1c-b375-15fcb8020ca5.jpeg', 50, 2, 100, '2025-05-30 23:53:43', '2025-05-30 23:53:43');

-- ----------------------------
-- Table structure for online_shop_product
-- ----------------------------
DROP TABLE IF EXISTS `online_shop_product`;
CREATE TABLE `online_shop_product`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `price` decimal(10, 2) NOT NULL COMMENT '商品价格',
  `category_id` int(11) NOT NULL COMMENT '所属分类id',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '商品描述',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品图片',
  `stock` int(11) NOT NULL COMMENT '商品库存',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '商品状态：1-在售；0-停售',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of online_shop_product
-- ----------------------------
INSERT INTO `online_shop_product` VALUES (2, '澳洲进口大黑车厘子大樱桃包甜黑樱桃大果多汁 500g 特大果', 50.00, 14, '商品毛重：1.0kg货号：608323093445原产地：智利类别：美早热卖时间：1月，11月，12月国产/进口：进口售卖方式：单品', 'http://192.168.198.1:9002/images/aa9cde81-5d46-4f1c-b375-15fcb8020ca5.jpeg', 88, 1, '2025-05-05 18:17:07', '2025-05-09 15:23:23');
INSERT INTO `online_shop_product` VALUES (3, '茶树菇 美味菌菇 东北山珍 500g', 1000.00, 15, '商品名：茶树菇 商品特点：美味菌菇 东北山珍 500g', 'http://192.168.198.1:9002/images/cc4c9cef-06c0-4107-85a2-8c7ceb85562c.jpeg', 10, 1, '2025-05-05 18:17:07', '2025-05-09 15:37:09');
INSERT INTO `online_shop_product` VALUES (14, 'Zespri佳沛 新西兰阳光金奇异果 6个装', 39.00, 12, '商品编号：4635056商品毛重：0.71kg商品产地：新西兰类别：金果包装：简装国产/进口：进口原产地：新西兰', 'http://192.168.198.1:9002/images/f5d1f2fe-08bc-474a-968f-82783c8a7750.jpeg', 77, 1, '2025-05-05 18:17:07', '2025-05-05 18:17:07');
INSERT INTO `online_shop_product` VALUES (17, '红颜奶油草莓 约重500g/20-24颗 新鲜水果', 99.00, 11, '商品毛重：0.58kg商品产地：丹东/南通/武汉类别：红颜草莓包装：简装国产/进口：国产', 'http://111.231.103.117:8081/images/caomei2.jpg', 82, 1, '2025-05-05 18:17:07', '2025-05-05 18:17:07');
INSERT INTO `online_shop_product` VALUES (21, '智利原味三文鱼排（大西洋鲑）240g/袋 4片装', 499.00, 8, '商品毛重：260.00g商品产地：中国大陆保存状态：冷冻国产/进口：进口包装：简装类别：三文鱼海水/淡水：海水烹饪建议：煎炸，蒸菜，烧烤原产地：智利', 'http://192.168.198.1:9002/images/b57de92a-4d5f-4c6b-9f35-1996032408da.jpeg', 1, 1, '2025-05-05 18:17:07', '2025-05-05 18:17:07');
INSERT INTO `online_shop_product` VALUES (22, '即食海参大连野生辽刺参 新鲜速食 特级生鲜海产 60~80G', 699.00, 13, '商品毛重：1.5kg商品产地：中国大陆贮存条件：冷冻重量：50-99g国产/进口：国产适用场景：养生滋补包装：袋装原产地：辽宁年限：9年以上等级：特级食品工艺：冷冻水产热卖时间：9月类别：即食海参固形物含量：70%-90%特产品类：大连海参售卖方式：单品', 'http://192.168.198.1:9002/images/666b0cae-5751-49f6-90ca-9aa8a709515e.jpg', 3, 1, '2025-05-05 18:17:07', '2025-05-05 18:17:07');
INSERT INTO `online_shop_product` VALUES (23, '澳大利亚直采鲜橙 精品澳橙12粒 单果130-180g', 12.00, 4, '商品毛重：2.27kg商品产地：澳大利亚类别：脐橙包装：简装国产/进口：进口原产地：澳大利亚', 'http://192.168.198.1:9002/images/bb6abbfe-29be-48b3-9c56-612208dd8e2e.jpg', 12, 1, '2025-05-05 18:17:07', '2025-05-05 18:17:07');
INSERT INTO `online_shop_product` VALUES (24, '智利帝王蟹礼盒装4.4-4.0斤/只 生鲜活鲜熟冻大螃蟹', 222.00, 7, '商品毛重：3.0kg商品产地：智利大闸蟹售卖方式：公蟹重量：2000-4999g套餐份量：5人份以上国产/进口：进口海水/淡水：海水烹饪建议：火锅，炒菜，烧烤，刺身，加热即食包装：简装原产地：智利保存状态：冷冻公单蟹重：5.5两及以上分类：帝王蟹特产品类：其它售卖方式：单品', 'http://192.168.198.1:9002/images/f441072c-33be-40c6-92ee-0f170f4c21da.jpg', 222, 1, '2025-05-05 18:17:07', '2025-05-05 18:17:07');
INSERT INTO `online_shop_product` VALUES (25, '新疆库尔勒克伦生无籽红提 国产新鲜红提葡萄 提子 5斤装', 222.00, 28, '商品毛重：2.5kg商品产地：中国大陆货号：XZL201909002重量：2000-3999g套餐份量：2人份国产/进口：国产是否有机：非有机单箱规格：3个装，4个装，5个装类别：红提包装：简装原产地：中国大陆售卖方式：单品', 'http://192.168.198.1:9002/images/5071099c-8109-4bae-a4d4-78faf0ca0d5c.jpg', 222, 1, '2025-05-05 18:17:07', '2025-05-05 18:17:07');
INSERT INTO `online_shop_product` VALUES (26, '越南进口红心火龙果 4个装 红肉中果 单果约330-420g', 222.00, 28, '商品毛重：1.79kg商品产地：越南重量：1000-1999g类别：红心火龙果包装：简装国产/进口：进口', 'http://192.168.198.1:9002/images/bd516423-bdbc-4ef6-9299-395bd7514aaf.jpg', 222, 1, '2025-05-05 18:17:07', '2025-05-05 18:17:07');
INSERT INTO `online_shop_product` VALUES (27, '内蒙古羔羊肉串 500g/袋（约20串）鲜冻羊肉串 BBQ烧烤食材', 222.00, 16, '商品毛重：0.585kg商品产地：内蒙古巴彦淖尔市保存状态：冷冻重量：500-999g套餐份量：3人份国产/进口：国产烹饪建议：烧烤原产地：内蒙古品种：其它热卖时间：4月，5月，6月，7月，8月，9月，10月，11月，12月饲养方式：圈养类别：羊肉串包装：简装套餐周期：12个月', 'http://192.168.198.1:9002/images/83916258-c17a-4782-b2de-46ca7a8ffa8d.jpg', 221, 1, '2025-05-05 18:17:07', '2025-05-05 18:17:07');
INSERT INTO `online_shop_product` VALUES (28, '玛琪摩尔新西兰进口冰淇淋大桶装', 222.00, 17, '商品毛重：1.04kg商品产地：新西兰国产/进口：进口包装：量贩装', 'http://192.168.198.1:9002/images/5d5e2063-18cc-4e45-98a7-be9d41b785e8.jpeg', 221, 1, '2025-05-05 18:17:07', '2025-05-05 18:17:07');
INSERT INTO `online_shop_product` VALUES (29, '西兰花沙拉菜 350g 甜玉米粒 青豆豌豆 胡萝卜冷冻方便蔬菜', 222.00, 18, '商品毛重：370.00g商品产地：浙江宁波重量：500g以下套餐份量：家庭装类别：速冻玉米/豌豆包装：简装烹饪建议：炒菜，炖菜，煎炸，蒸菜售卖方式：单品', 'http://192.168.198.1:9002/images/eb05b382-fc63-4d65-b0ee-1c713670b305.jpeg', 222, 1, '2025-05-05 18:17:07', '2025-05-05 18:17:07');
INSERT INTO `online_shop_product` VALUES (36, '四川果冻橙 吹弹可破', 222.00, 19, '商品毛重：370.00g商品产地：四川 重量：1000g', 'http://192.168.198.1:9002/images/40aec51e-b392-4cc7-810b-c4221ac20a3e.jpeg', 217, 1, '2025-05-05 18:17:07', '2025-05-09 19:16:31');
INSERT INTO `online_shop_product` VALUES (37, '进口牛油果 中果6粒装 单果约130-160g', 222.00, 28, '商品名称：京觅进口牛油果 6个装商品编号：3628240商品毛重：1.2kg商品产地：秘鲁、智利、墨西哥重量：1000g以下国产/进口：进口', 'http://192.168.198.1:9002/images/2881fd09-f257-42f0-ae7e-3a23e8bc4f31.jpeg', 218, 1, '2025-05-05 18:17:07', '2025-05-09 19:16:31');
INSERT INTO `online_shop_product` VALUES (38, '中街1946网红雪糕冰淇淋', 222.00, 17, '商品名称：中街1946网红雪糕冰淇淋乐享系列半巧*5牛乳*5阿棕*2冰激凌冷饮冰棍冰棒商品编号：52603405444店铺： 中街1946官方旗舰店商品毛重：1.3kg商品产地：中国大陆国产/进口：国产包装：量贩装售卖方式：组合', 'http://192.168.198.1:9002/images/a4e32ae1-6384-4643-b549-d0183c762a33.jpeg', 222, 1, '2025-05-05 18:17:07', '2025-05-05 18:17:07');
INSERT INTO `online_shop_product` VALUES (39, '福建六鳌红薯5斤', 40.00, 18, '商品名称：京觅福建六鳌红薯5斤商品编号：4087121商品毛重：2.8kg商品产地：福建省漳浦县六鳌镇重量：2500g及以上烹饪建议：煎炸，蒸菜，烧烤包装：简装分类：地瓜/红薯售卖方式：单品', 'http://192.168.198.1:9002/images/ff403b13-854f-41be-8c19-7eeb657fd791.jpg', 222, 1, '2025-05-05 18:17:07', '2025-05-05 18:17:07');
INSERT INTO `online_shop_product` VALUES (40, '胡萝卜', 222.00, 18, '商品名称：绿鲜知胡萝卜商品编号：4116192商品毛重：1.07kg商品产地：北京包装：简装分类：萝卜烹饪建议：火锅，炒菜，炖菜', 'http://192.168.198.1:9002/images/d874b271-14d7-4eac-97b4-4009f61b9524.jpeg', 222, 1, '2025-05-05 18:17:07', '2025-05-05 18:17:07');
INSERT INTO `online_shop_product` VALUES (41, '羊肉卷 内蒙羔羊肉 鲜嫩 500g/袋 首农出品 羊排肉卷 火锅食材', 222.00, 16, '商品名称：首食惠羊排片商品编号：4836347商品毛重：0.51kg商品产地：辽宁省大连市保存状态：冷冻品种：其它国产/进口：进口饲养方式：散养类别：羊肉片/卷包装：简装烹饪建议：火锅，炒菜，炖菜原产地：新西兰', 'http://192.168.198.1:9002/images/f3c4f9eb-b8a9-43b6-a32b-bfca8235d78b.jpg', 222, 1, '2025-05-05 18:17:07', '2025-05-05 18:17:07');
INSERT INTO `online_shop_product` VALUES (42, '甜玉米 切好 香甜', 240.00, 18, '品牌： 绿鲜知（greenseer）\n商品名称：绿鲜知甜玉米商品编号：4983604商品毛重：1.1kg商品产地：云南玉溪类别：玉米', 'http://192.168.198.1:9002/images/77d0ce50-f9fd-434c-922a-8923895ddb5f.jpeg', 218, 1, '2025-05-05 18:17:07', '2025-05-05 18:17:07');
INSERT INTO `online_shop_product` VALUES (45, '樱桃', 20.00, 3, 'test', 'http://192.168.198.1:9002/images/5d32f9d2-9394-4461-aa11-70a4805d19a2.jpeg', 0, 1, '2025-05-06 00:25:04', '2025-05-09 15:23:23');
INSERT INTO `online_shop_product` VALUES (48, 'hei-小灰灰', 2131.00, 13, 'ceshi', 'http://192.168.198.1:9002/images/e512d741-a97d-47c2-bced-4e3de88f3885.jpeg', 21, 1, '2025-05-30 16:53:29', '2025-05-30 16:53:29');

-- ----------------------------
-- Table structure for online_shop_user
-- ----------------------------
DROP TABLE IF EXISTS `online_shop_user`;
CREATE TABLE `online_shop_user`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码，MD5加密',
  `role` tinyint(4) NOT NULL DEFAULT 2 COMMENT '用户角色：1-管理员，2-普通用户',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of online_shop_user
-- ----------------------------
INSERT INTO `online_shop_user` VALUES (1, 'admin', '4CBZDw4YzWBT164OClB2CQ==', 1, '2025-05-04 20:07:19', '2025-05-04 21:07:11');
INSERT INTO `online_shop_user` VALUES (2, 'addd', '0BQKgkcCaLDA/IfmJnNYgQ==', 2, '2025-05-04 22:33:28', '2025-05-04 22:33:28');
INSERT INTO `online_shop_user` VALUES (3, 'addd2', 'yTzNeLIHZSg0YhazsvcB5g==', 2, '2025-05-23 01:34:19', '2025-05-23 01:34:19');
INSERT INTO `online_shop_user` VALUES (5, 'addd223', 'AZICOnu9cyUFFvBp3xi1AA==', 2, '2025-05-23 13:32:02', '2025-05-23 13:32:02');
INSERT INTO `online_shop_user` VALUES (6, '231623010042', '/LPkE5Vyr+jEu2kv2HBBKw==', 2, '2025-05-29 10:19:52', '2025-05-29 10:19:52');
INSERT INTO `online_shop_user` VALUES (7, 'xiaohuihui', 'P5lZCaRnFONhEBQo3SOlIQ==', 2, '2025-05-29 10:40:07', '2025-05-29 10:40:07');
INSERT INTO `online_shop_user` VALUES (8, 'test', '9pYoKqTNT2FKqZUZDPRC/g==', 2, '2025-05-29 10:57:12', '2025-05-29 10:57:12');
INSERT INTO `online_shop_user` VALUES (9, 'aaa', '6r2M6UBFB6qMInFNP16tqQ==', 2, '2025-05-29 22:48:48', '2025-05-29 22:48:48');

SET FOREIGN_KEY_CHECKS = 1;
