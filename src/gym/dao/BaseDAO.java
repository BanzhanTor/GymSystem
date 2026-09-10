package gym.dao;

import java.util.List;

/**
 * 泛型接口：定义通用的增删改查（CRUD）方法。
 */
public interface BaseDAO<T> {

    boolean save(T entity);

    boolean delete(int id);

    boolean update(T entity);

    T findById(int id);

    List<T> findAll();
}