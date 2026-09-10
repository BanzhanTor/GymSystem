package gym.service;

import gym.dao.CardTypeDAOImpl;
import gym.dao.MemberDAO;
import gym.dao.MemberDAOImpl;
import gym.entity.CardType;
import gym.entity.Member;
import gym.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员业务类：增删改查、搜索、到期提醒，大量使用集合与日期计算。
 */
public class MemberService {

    private final MemberDAO dao = new MemberDAOImpl();
    private final CardTypeDAOImpl cardTypeDAO = new CardTypeDAOImpl();

    /** 新增会员：办卡日期默认为今天，到期日期按卡类型天数自动计算 */
    public boolean add(Member member) {
        if (member.getJoinDate() == null || member.getJoinDate().isEmpty()) {
            member.setJoinDate(DateUtil.currentDate());
        }
        CardType ct = cardTypeDAO.findById(member.getCardTypeId());
        if (ct != null) {
            member.setExpireDate(DateUtil.addDays(member.getJoinDate(), ct.getDays()));
        }
        return dao.save(member);
    }

    public boolean delete(int id) {
        return dao.delete(id);
    }

    public boolean update(Member member) {
        return dao.update(member);
    }

    public Member findById(int id) {
        return dao.findById(id);
    }

    /** 全部会员列表（集合 List） */
    public List<Member> list() {
        return dao.findAll();
    }

    /** 按姓名模糊搜索（集合过滤） */
    public List<Member> searchByName(String keyword) {
        List<Member> result = new ArrayList<>();
        for (Member m : dao.findAll()) {
            if (m.getName() != null && m.getName().contains(keyword)) {
                result.add(m);
            }
        }
        return result;
    }

    /** 按手机号精确搜索 */
    public Member searchByPhone(String phone) {
        for (Member m : dao.findAll()) {
            if (m.getPhone() != null && m.getPhone().equals(phone)) {
                return m;
            }
        }
        return null;
    }

    /** 查询 days 天内即将到期的会员 */
    public List<Member> expiringWithin(int days) {
        List<Member> result = new ArrayList<>();
        for (Member m : dao.findAll()) {
            long left = DateUtil.daysUntil(m.getExpireDate());
            if (left >= 0 && left <= days) {
                result.add(m);
            }
        }
        return result;
    }

    /** 查询已过期的会员 */
    public List<Member> expired() {
        List<Member> result = new ArrayList<>();
        for (Member m : dao.findAll()) {
            if (DateUtil.daysUntil(m.getExpireDate()) < 0) {
                result.add(m);
            }
        }
        return result;
    }
}