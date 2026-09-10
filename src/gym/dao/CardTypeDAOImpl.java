package gym.dao;

import gym.entity.CardType;

/**
 * CardTypeDAOImpl：继承泛型实现类 BaseDAOImpl<CardType>，实现 CardTypeDAO 接口。
 */
public class CardTypeDAOImpl extends BaseDAOImpl<CardType> implements CardTypeDAO {

    public CardTypeDAOImpl() {
        super(CardType.class);
    }
}