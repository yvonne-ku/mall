-- 建库脚本

-- 基础数据数据库
CREATE
DATABASE IF NOT EXISTS mall_basic_data;

-- 购物车数据库
CREATE
DATABASE IF NOT EXISTS mall_cart;

-- 用户数据库
CREATE
DATABASE IF NOT EXISTS mall_customer;

-- 消息数据库
CREATE
DATABASE IF NOT EXISTS mall_message;

-- 订单数据库
CREATE
DATABASE IF NOT EXISTS mall_order;

-- 支付数据库
CREATE
DATABASE IF NOT EXISTS mall_pay;

-- 商品数据库
CREATE
DATABASE IF NOT EXISTS mall_product;

-- BFF聚合层数据库
CREATE
DATABASE IF NOT EXISTS mall_bff;
