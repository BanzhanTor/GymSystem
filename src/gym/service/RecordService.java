package gym.service;

import gym.dao.RecordDAO;
import gym.dao.RecordDAOImpl;
import gym.entity.InOutRecord;
import gym.util.DateUtil;
import gym.util.SerializeUtil;

import java.util.List;

/**
 * 进出记录业务类：入场/离场打卡、查询，以及序列化备份与恢复。
 */
public class RecordService {

    private static final String BACKUP_FILE = "d:/summerclass/GymSystem/gym_records.dat";

    private final RecordDAO dao = new RecordDAOImpl();

    /** 入场打卡：记录进入时间 */
    public boolean enter(int memberId) {
        InOutRecord record = new InOutRecord(memberId, DateUtil.currentDateTime(), null);
        return dao.save(record);
    }

    /** 离场：更新最近一条未离场记录的离开时间 */
    public boolean leave(int memberId) {
        List<InOutRecord> records = dao.findByMemberId(memberId);
        for (int i = records.size() - 1; i >= 0; i--) {
            InOutRecord r = records.get(i);
            if (r.getLeaveTime() == null || r.getLeaveTime().isEmpty()) {
                r.setLeaveTime(DateUtil.currentDateTime());
                return dao.update(r);
            }
        }
        return false;
    }

    public List<InOutRecord> list() {
        return dao.findAll();
    }

    public List<InOutRecord> recordsOfMember(int memberId) {
        return dao.findByMemberId(memberId);
    }

    /** 序列化备份全部记录到本地文件 */
    public void backup() {
        SerializeUtil.write(dao.findAll(), BACKUP_FILE);
    }

    /** 从本地文件反序列化恢复记录 */
    @SuppressWarnings("unchecked")
    public List<InOutRecord> restore() {
        Object obj = SerializeUtil.read(BACKUP_FILE);
        return (obj instanceof List) ? (List<InOutRecord>) obj : null;
    }
}