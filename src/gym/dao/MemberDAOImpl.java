package gym.dao;

import gym.entity.Member;

/**
 * MemberDAOImpl：继承泛型实现类 BaseDAOImpl<Member>，实现 MemberDAO 接口。
 */
public class MemberDAOImpl extends BaseDAOImpl<Member> implements MemberDAO {

    public MemberDAOImpl() {
        super(Member.class);
    }
}