package gym.dao;

import gym.entity.InOutRecord;

import java.util.List;

/**
 * RecordDAOImpl：继承泛型实现类 BaseDAOImpl<InOutRecord>，实现 RecordDAO 接口。
 * 进出记录表名覆写为 record。
 */
public class RecordDAOImpl extends BaseDAOImpl<InOutRecord> implements RecordDAO {

    public RecordDAOImpl() {
        super(InOutRecord.class);
    }

    @Override
    protected String tableName() {
        return "record";
    }

    @Override
    public List<InOutRecord> findByMemberId(int memberId) {
        return findByColumn("member_id", memberId);
    }
}