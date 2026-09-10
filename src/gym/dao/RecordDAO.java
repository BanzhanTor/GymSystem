package gym.dao;

import gym.entity.InOutRecord;

import java.util.List;

/**
 * RecordDAO 接口：继承泛型接口 BaseDAO，指定具体类型 InOutRecord，并扩展按会员查询。
 */
public interface RecordDAO extends BaseDAO<InOutRecord> {

    List<InOutRecord> findByMemberId(int memberId);
}