package gym.service;

import gym.dao.CardTypeDAO;
import gym.dao.CardTypeDAOImpl;
import gym.entity.CardType;

import java.util.List;

/**
 * 会员卡类型业务类。
 */
public class CardTypeService {

    private final CardTypeDAO dao = new CardTypeDAOImpl();

    public boolean add(CardType cardType) {
        return dao.save(cardType);
    }

    public boolean delete(int id) {
        return dao.delete(id);
    }

    public CardType findById(int id) {
        return dao.findById(id);
    }

    /** 返回全部卡类型（集合 List） */
    public List<CardType> list() {
        return dao.findAll();
    }

    public String typeName(int id) {
        CardType ct = dao.findById(id);
        return ct == null ? "未知" : ct.getName();
    }
}