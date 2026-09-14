package gym.ui;

import gym.entity.CardType;
import gym.entity.InOutRecord;
import gym.entity.Member;
import gym.service.CardTypeService;
import gym.service.MemberService;
import gym.service.RecordService;
import gym.service.StatisticService;
import gym.util.DateUtil;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 主菜单：CMD 交互入口，负责分发到各功能模块。
 */
public class MainMenu {

    private final Scanner sc;
    private final CardTypeService cardTypeService = new CardTypeService();
    private final MemberService memberService = new MemberService();
    private final RecordService recordService = new RecordService();
    private final StatisticService statisticService = new StatisticService();

    public MainMenu(Scanner sc) {
        this.sc = sc;
    }

    public void run() {
        while (true) {
            printMainMenu();
            System.out.print("请选择：");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    manageCardType();
                    break;
                case "2":
                    manageMember();
                    break;
                case "3":
                    manageRecord();
                    break;
                case "4":
                    expireRemind();
                    break;
                case "5":
                    statistics();
                    break;
                case "0":
                    System.out.println("感谢使用健身房管理系统，再见！");
                    return;
                default:
                    System.out.println("输入有误，请重新选择。");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n========== 健身房管理系统 ==========");
        System.out.println("1. 会员卡类型管理");
        System.out.println("2. 会员管理");
        System.out.println("3. 进出记录管理");
        System.out.println("4. 到期提醒");
        System.out.println("5. 统计信息");
        System.out.println("0. 退出系统");
        System.out.println("==================================");
    }

    private void manageCardType() {
        while (true) {
            System.out.println("\n--- 会员卡类型管理 ---");
            System.out.println("1. 查看全部卡类型  2. 新增卡类型  3. 删除卡类型  0. 返回上级");
            System.out.print("请选择：");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1":
                    List<CardType> list = cardTypeService.list();
                    if (list.isEmpty()) {
                        System.out.println("暂无卡类型");
                    } else {
                        list.forEach(System.out::println);
                    }
                    break;
                case "2": {
                    System.out.print("卡名称（如 月卡）：");
                    String name = sc.nextLine().trim();
                    System.out.print("价格：");
                    double price = readDouble();
                    System.out.print("有效天数：");
                    int days = readInt();
                    if (cardTypeService.add(new CardType(name, price, days))) {
                        System.out.println("新增成功");
                    } else {
                        System.out.println("新增失败");
                    }
                    break;
                }
                case "3": {
                    System.out.print("要删除的卡类型 id：");
                    int id = readInt();
                    if (cardTypeService.delete(id)) {
                        System.out.println("删除成功");
                    } else {
                        System.out.println("删除失败：该卡类型仍有会员在使用，无法删除");
                    }
                    break;
                }
                case "0":
                    return;
                default:
                    System.out.println("输入有误");
            }
        }
    }

    private void manageMember() {
        while (true) {
            System.out.println("\n--- 会员管理 ---");
            System.out.println("1. 查看全部会员  2. 新增会员  3. 按姓名搜索  4. 按手机号搜索");
            System.out.println("5. 修改会员  6. 删除会员  0. 返回上级");
            System.out.print("请选择：");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1":
                    List<Member> list = memberService.list();
                    if (list.isEmpty()) {
                        System.out.println("暂无会员");
                    } else {
                        list.forEach(System.out::println);
                    }
                    break;
                case "2":
                    addMember();
                    break;
                case "3": {
                    System.out.print("姓名关键字：");
                    String kw = sc.nextLine().trim();
                    List<Member> rs = memberService.searchByName(kw);
                    if (rs.isEmpty()) {
                        System.out.println("未找到");
                    } else {
                        rs.forEach(System.out::println);
                    }
                    break;
                }
                case "4": {
                    System.out.print("手机号：");
                    String phone = sc.nextLine().trim();
                    Member m = memberService.searchByPhone(phone);
                    System.out.println(m == null ? "未找到" : m);
                    break;
                }
                case "5":
                    updateMember();
                    break;
                case "6": {
                    System.out.print("要删除的会员 id：");
                    int id = readInt();
                    if (memberService.delete(id)) {
                        System.out.println("删除成功");
                    } else {
                        System.out.println("删除失败");
                    }
                    break;
                }
                case "0":
                    return;
                default:
                    System.out.println("输入有误");
            }
        }
    }

    private void addMember() {
        List<CardType> types = cardTypeService.list();
        if (types.isEmpty()) {
            System.out.println("请先添加会员卡类型！");
            return;
        }
        System.out.println("可用的会员卡类型：");
        types.forEach(System.out::println);
        System.out.print("会员姓名：");
        String name = sc.nextLine().trim();
        System.out.print("手机号：");
        String phone = sc.nextLine().trim();
        System.out.print("性别：");
        String gender = sc.nextLine().trim();
        System.out.print("选择卡类型 id：");
        int cardTypeId = readInt();
        Member m = new Member(name, phone, gender, cardTypeId, DateUtil.currentDate(), null);
        if (memberService.add(m)) {
            System.out.println("新增成功，到期日期：" + m.getExpireDate());
        } else if (memberService.searchByPhone(phone) != null) {
            System.out.println("新增失败：该手机号已存在会员，请勿重复添加");
        } else {
            System.out.println("新增失败");
        }
    }

    private void updateMember() {
        System.out.print("要修改的会员 id：");
        int id = readInt();
        Member m = memberService.findById(id);
        if (m == null) {
            System.out.println("会员不存在");
            return;
        }
        System.out.println("当前信息：" + m);
        System.out.print("新姓名（回车跳过）：");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) {
            m.setName(name);
        }
        System.out.print("新手机号（回车跳过）：");
        String phone = sc.nextLine().trim();
        if (!phone.isEmpty()) {
            m.setPhone(phone);
        }
        System.out.print("新性别（回车跳过）：");
        String gender = sc.nextLine().trim();
        if (!gender.isEmpty()) {
            m.setGender(gender);
        }
        if (memberService.update(m)) {
            System.out.println("修改成功");
        } else {
            System.out.println("修改失败");
        }
    }

    private void manageRecord() {
        while (true) {
            System.out.println("\n--- 进出记录管理 ---");
            System.out.println("1. 会员入场打卡  2. 会员离场  3. 按会员查询记录  4. 查看全部记录");
            System.out.println("5. 备份记录到文件（序列化）  6. 从文件恢复（反序列化）  0. 返回上级");
            System.out.print("请选择：");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1": {
                    System.out.print("会员 id：");
                    int id = readInt();
                    if (recordService.enter(id)) {
                        System.out.println("入场打卡成功：" + DateUtil.currentDateTime());
                    } else {
                        System.out.println("打卡失败：该会员已在场，请先离场");
                    }
                    break;
                }
                case "2": {
                    System.out.print("会员 id：");
                    int id = readInt();
                    if (recordService.leave(id)) {
                        System.out.println("离场登记成功");
                    } else {
                        System.out.println("离场登记失败（可能没有未离场的记录）");
                    }
                    break;
                }
                case "3": {
                    System.out.print("会员 id：");
                    int id = readInt();
                    List<InOutRecord> rs = recordService.recordsOfMember(id);
                    if (rs.isEmpty()) {
                        System.out.println("暂无记录");
                    } else {
                        rs.forEach(System.out::println);
                    }
                    break;
                }
                case "4":
                    List<InOutRecord> all = recordService.list();
                    if (all.isEmpty()) {
                        System.out.println("暂无记录");
                    } else {
                        all.forEach(System.out::println);
                    }
                    break;
                case "5":
                    recordService.backup();
                    System.out.println("已备份到 gym_records.dat");
                    break;
                case "6": {
                    List<InOutRecord> restored = recordService.restore();
                    System.out.println("从备份恢复到的记录：");
                    if (restored == null || restored.isEmpty()) {
                        System.out.println("（空）");
                    } else {
                        restored.forEach(System.out::println);
                    }
                    break;
                }
                case "0":
                    return;
                default:
                    System.out.println("输入有误");
            }
        }
    }

    private void expireRemind() {
        while (true) {
            System.out.println("\n--- 到期提醒 ---");
            System.out.println("1. 7 天内即将到期  2. 30 天内即将到期  3. 已过期会员  0. 返回上级");
            System.out.print("请选择：");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1":
                    showMembers(memberService.expiringWithin(7));
                    break;
                case "2":
                    showMembers(memberService.expiringWithin(30));
                    break;
                case "3":
                    showMembers(memberService.expired());
                    break;
                case "0":
                    return;
                default:
                    System.out.println("输入有误");
            }
        }
    }

    private void showMembers(List<Member> list) {
        if (list.isEmpty()) {
            System.out.println("没有符合条件的会员");
            return;
        }
        for (Member m : list) {
            long left = DateUtil.daysUntil(m.getExpireDate());
            System.out.println(m + "  [剩余 " + left + " 天]");
        }
    }

    private void statistics() {
        System.out.println("\n--- 统计信息 ---");
        System.out.println("会员总数：" + statisticService.totalMembers());
        Map<String, Integer> byType = statisticService.countByCardType();
        System.out.println("各卡类型会员人数：");
        if (byType.isEmpty()) {
            System.out.println("（暂无数据）");
        } else {
            byType.forEach((k, v) -> System.out.println("  " + k + "：" + v + " 人"));
        }
        System.out.println("平均停留时长：" + statisticService.averageStayMinutes() + " 分钟");
    }

    private int readInt() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("输入不是合法整数，按 0 处理");
            return 0;
        }
    }

    private double readDouble() {
        try {
            return Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("输入不是合法数字，按 0 处理");
            return 0;
        }
    }
}