package gym;

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

/**
 * 自测类：不经过交互菜单，直接调用业务层验证各功能，便于快速冒烟测试。
 */
public class SmokeTest {

    public static void main(String[] args) {
        CardTypeService cardTypeService = new CardTypeService();
        MemberService memberService = new MemberService();
        RecordService recordService = new RecordService();
        StatisticService statisticService = new StatisticService();

        System.out.println("===== 1. 会员卡类型列表 =====");
        for (CardType ct : cardTypeService.list()) {
            System.out.println(ct);
        }

        System.out.println("\n===== 2. 新增会员（自动计算到期日期） =====");
        Member m1 = new Member("张三", "13800001111", "男", 1, null, null);
        memberService.add(m1);
        System.out.println("新增 张三，到期日期=" + m1.getExpireDate());

        Member m2 = new Member("李四", "13800002222", "女", 3, null, null);
        memberService.add(m2);
        System.out.println("新增 李四，到期日期=" + m2.getExpireDate());

        System.out.println("\n===== 3. 全部会员列表 =====");
        for (Member m : memberService.list()) {
            System.out.println(m);
        }

        System.out.println("\n===== 4. 按姓名模糊搜索 =====");
        for (Member m : memberService.searchByName("张")) {
            System.out.println(m);
        }

        System.out.println("\n===== 5. 进出记录（入场 + 离场） =====");
        Member zhang = memberService.searchByPhone("13800001111");
        recordService.enter(zhang.getId());
        recordService.leave(zhang.getId());
        for (InOutRecord r : recordService.recordsOfMember(zhang.getId())) {
            System.out.println(r);
        }

        System.out.println("\n===== 6. 序列化备份与恢复 =====");
        recordService.backup();
        System.out.println("备份完成");
        List<InOutRecord> restored = recordService.restore();
        System.out.println("从备份恢复的记录数：" + (restored == null ? 0 : restored.size()));

        System.out.println("\n===== 7. 到期提醒（30 天内） =====");
        for (Member m : memberService.expiringWithin(30)) {
            System.out.println(m + "  剩余 " + DateUtil.daysUntil(m.getExpireDate()) + " 天");
        }

        System.out.println("\n===== 8. 统计信息（多线程 + Map） =====");
        System.out.println("会员总数：" + statisticService.totalMembers());
        Map<String, Integer> byType = statisticService.countByCardType();
        System.out.println("各卡类型人数：" + byType);

        System.out.println("\n===== 9. 平均停留时长（Math 取整） =====");
        System.out.println("平均停留：" + statisticService.averageStayMinutes() + " 分钟");

        System.out.println("\n全部功能验证完成！");
    }
}