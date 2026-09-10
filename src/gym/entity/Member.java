package gym.entity;

import java.io.Serializable;

/**
 * 实体类：健身房会员
 */
public class Member implements Serializable {
    private int id;
    private String name;
    private String phone;
    private String gender;
    private int cardTypeId;
    private String joinDate;
    private String expireDate;

    public Member() {
    }

    public Member(String name, String phone, String gender, int cardTypeId, String joinDate, String expireDate) {
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.cardTypeId = cardTypeId;
        this.joinDate = joinDate;
        this.expireDate = expireDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(int cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public String toString() {
        return "Member[id=" + id + ", name=" + name + ", phone=" + phone
                + ", gender=" + gender + ", cardTypeId=" + cardTypeId
                + ", joinDate=" + joinDate + ", expireDate=" + expireDate + "]";
    }
}