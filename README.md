# 健身房管理系统（GymSystem）

Java 课程大作业：健身房管理系统，采用 CMD 菜单交互，MySQL 存储数据。
参照课本"基于 C/S 架构的餐饮管理系统"仿写，改成健身房会员与进出打卡场景。

## 功能

- 会员卡类型管理（月卡 / 季卡 / 年卡 / 次卡）
- 会员管理（增删改查、按姓名 / 手机号搜索、自动计算到期日期）
- 进出记录管理（入场 / 离场打卡、按会员查询、序列化备份与恢复）
- 到期提醒（7 天内 / 30 天内即将到期、已过期会员）
- 统计信息（会员总数、各卡类型人数、平均停留时长）

## 技术点

- 集合（List / Map）
- 泛型（BaseDAO\<T\> 通用 DAO 层）
- 序列化（进出记录备份与恢复）
- JDBC + MySQL（数据持久化）
- 多线程（线程池并行统计）
- 常用类：String、Date / Calendar、Math

## 环境要求

- JDK 8+
- MySQL 8.0（默认账号 root，密码 123456）

## 运行步骤

1. 初始化数据库：执行 `gym.sql`
2. 用 IDEA 打开项目，给模块添加依赖 `lib/mysql-connector-j-8.0.33.jar`
3. 运行 `gym.Main`，登录账号：`admin`，密码：`123456`

## 目录结构

```
GymSystem/
├── gym.sql               # 建库建表脚本
├── lib/                  # MySQL 驱动
└── src/gym/
    ├── Main.java         # 程序入口
    ├── SmokeTest.java    # 自测 / 演示入口
    ├── entity/           # 实体类：Member, CardType, InOutRecord
    ├── dao/              # 泛型 DAO 层
    ├── service/          # 业务层
    ├── ui/               # CMD 菜单：LoginMenu, MainMenu
    └── util/             # 工具类：DbUtils, DateUtil, SerializeUtil
```