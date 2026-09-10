package gym.entity;

import java.io.Serializable;

/**
 * 实体类：进出（打卡）记录
 */
public class InOutRecord implements Serializable {
    private int id;
    private int memberId;
    private String enterTime;
    private String leaveTime;

    public InOutRecord() {
    }

    public InOutRecord(int memberId, String enterTime, String leaveTime) {
        this.memberId = memberId;
        this.enterTime = enterTime;
        this.leaveTime = leaveTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(String enterTime) {
        this.enterTime = enterTime;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    @Override
    public String toString() {
        return "InOutRecord[id=" + id + ", memberId=" + memberId
                + ", enterTime=" + enterTime + ", leaveTime=" + leaveTime + "]";
    }
}