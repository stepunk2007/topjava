package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            for (Role role : user.getRoles()) {
                jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?,?)", new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setInt(1, user.getId());
                        preparedStatement.setString(2, role.name());
                    }

                    @Override
                    public int getBatchSize() {
                        return user.getRoles().size();
                    }
                });
            }
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM users u JOIN user_roles ur on u.id = ur.user_id WHERE id=?", id);
        return DataAccessUtils.singleResult(getUsers(rows));
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM users u JOIN user_roles ur on u.id = ur.user_id WHERE email=?", email);
        return DataAccessUtils.singleResult(getUsers(rows));
    }

    @Override
    public List<User> getAll() {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM users u JOIN user_roles ur on u.id = ur.user_id ORDER BY name, email");
        return getUsers(rows);
    }
    //perhaps better to use ResultSetExtractor
    private List<User> getUsers(List<Map<String, Object>> rows) {
        Map<Integer, User> users = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            Integer id = (Integer) row.get("id");
            User user = users.getOrDefault(id, new User());
            if (user.getId() == null) {
                user.setId(id);
                user.setName((String) row.get("name"));
                user.setEmail((String) row.get("email"));
                user.setPassword((String) row.get("password"));
                user.setEnabled((Boolean) row.get("enabled"));
                user.setCaloriesPerDay((Integer) row.get("calories_per_day"));
                user.setRoles(new HashSet<>());
            }
            HashSet<Role> roles = new HashSet<>(user.getRoles());
            if (row.get("role") != null) {
                roles.add(Role.valueOf((String) row.get("role")));
            }
            user.setRoles(roles);
            users.put(id, user);
        }
        return new ArrayList<>(users.values());
    }
}
