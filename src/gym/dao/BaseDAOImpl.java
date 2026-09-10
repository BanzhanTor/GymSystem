package gym.dao;

import gym.util.DbUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 泛型实现类：通过 JDBC + 反射实现通用的 CRUD 操作。
 * 约定：表名由类名驼峰转下划线得到（特殊情况可由子类覆写 tableName()），主键列名为 id。
 */
public class BaseDAOImpl<T> implements BaseDAO<T> {

    private final Class<T> clazz;

    public BaseDAOImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    /** 表名：类名驼峰转下划线（如 CardType -> card_type），子类可覆写 */
    protected String tableName() {
        return camelToSnake(clazz.getSimpleName());
    }

    /** 驼峰转下划线：cardTypeId -> card_type_id，首字母不处理 */
    private String camelToSnake(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (i > 0 && Character.isUpperCase(c)) {
                sb.append('_').append(Character.toLowerCase(c));
            } else {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }

    private Field[] fields() {
        List<Field> list = new ArrayList<>();
        for (Field f : clazz.getDeclaredFields()) {
            if (!Modifier.isStatic(f.getModifiers())) {
                f.setAccessible(true);
                list.add(f);
            }
        }
        return list.toArray(new Field[0]);
    }

    private Object getValue(Field f, T entity) {
        try {
            return f.get(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean save(T entity) {
        StringBuilder cols = new StringBuilder();
        StringBuilder marks = new StringBuilder();
        List<Object> values = new ArrayList<>();
        for (Field f : fields()) {
            if ("id".equals(f.getName())) {
                continue;
            }
            if (cols.length() > 0) {
                cols.append(", ");
                marks.append(", ");
            }
            cols.append(camelToSnake(f.getName()));
            marks.append("?");
            values.add(getValue(f, entity));
        }
        String sql = "INSERT INTO " + tableName() + " (" + cols + ") VALUES (" + marks + ")";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                ps.setObject(i + 1, values.get(i));
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM " + tableName() + " WHERE id = ?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(T entity) {
        StringBuilder sets = new StringBuilder();
        List<Object> values = new ArrayList<>();
        Object idValue = null;
        for (Field f : fields()) {
            Object v = getValue(f, entity);
            if ("id".equals(f.getName())) {
                idValue = v;
                continue;
            }
            if (sets.length() > 0) {
                sets.append(", ");
            }
            sets.append(camelToSnake(f.getName())).append(" = ?");
            values.add(v);
        }
        String sql = "UPDATE " + tableName() + " SET " + sets + " WHERE id = ?";
        values.add(idValue);
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                ps.setObject(i + 1, values.get(i));
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public T findById(int id) {
        String sql = "SELECT * FROM " + tableName() + " WHERE id = ?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 通用按列查询，供子类扩展使用 */
    protected List<T> findByColumn(String column, Object value) {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName() + " WHERE " + column + " = ?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private T mapRow(ResultSet rs) throws Exception {
        T obj = clazz.getDeclaredConstructor().newInstance();
        for (Field f : fields()) {
            Object value = rs.getObject(camelToSnake(f.getName()));
            if (value != null) {
                f.set(obj, value);
            }
        }
        return obj;
    }
}