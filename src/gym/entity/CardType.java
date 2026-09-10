package gym.entity;

import java.io.Serializable;

/**
 * 实体类：会员卡类型（月卡/季卡/年卡/次卡）
 */
public class CardType implements Serializable {
    private int id;
    private String name;
    private double price;
    private int days;

    public CardType() {
    }

    public CardType(String name, double price, int days) {
        this.name = name;
        this.price = price;
        this.days = days;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "CardType[id=" + id + ", name=" + name + ", price=" + price + ", days=" + days + "]";
    }
}