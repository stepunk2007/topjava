package ru.javawebinar.topjava.extractors;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserResultSetExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, User> users = new LinkedHashMap<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            User user = users.computeIfAbsent(id, v -> createUser(v, rs));
            Set<Role> roles = new HashSet<>(user.getRoles());
            if (rs.getString("role") != null) {
                roles.add(Role.valueOf(rs.getString("role")));
            }
            user.setRoles(roles);
            users.put(id, user);
        }

        return new ArrayList<>(users.values());
    }

    private User createUser(Integer id, ResultSet rs) throws IllegalArgumentException {
        User user;
        try {
            user = BeanPropertyRowMapper.newInstance(User.class).mapRow(rs, 0);
            user.setRoles(EnumSet.noneOf(Role.class));
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return user;
    }
}
