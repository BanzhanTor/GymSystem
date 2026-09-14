-- 健身房管理系统 数据库初始化脚本
DROP DATABASE IF EXISTS gym;
CREATE DATABASE gym DEFAULT CHARSET utf8mb4;
USE gym;

-- 会员卡类型表
CREATE TABLE card_type (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    price DOUBLE,
    days INT
);

-- 会员表
CREATE TABLE member (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    phone VARCHAR(20),
    gender VARCHAR(10),
    card_type_id INT,
    join_date VARCHAR(20),
    expire_date VARCHAR(20)
);

-- 进出记录表
CREATE TABLE record (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id INT,
    enter_time VARCHAR(30),
    leave_time VARCHAR(30)
);

-- 初始会员卡类型
INSERT INTO card_type(name, price, days) VALUES
('月卡', 350, 30),
('季卡', 800, 90),
('年卡', 2800, 365),
('次卡', 15, 1);

SELECT 'gym 数据库初始化完成' AS result;