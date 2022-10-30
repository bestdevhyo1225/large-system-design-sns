package com.example.fastcampusmysql.domain.member.repository;

import com.example.fastcampusmysql.domain.member.entity.Member;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private static final String TABLE = "Member";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<Member> findById(Long id) {
        String sql = String.format("SELECT * FROM %s WHERE id = :id", TABLE);
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource().addValue("id", id);
        RowMapper<Member> rowMapper = (ResultSet resultSet, int rowNum) -> Member.of(resultSet.getLong("id"),
                resultSet.getString("email"), resultSet.getString("nickname"),
                resultSet.getObject("birthday", LocalDate.class),
                resultSet.getObject("createdAt", LocalDateTime.class));

        return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, rowMapper));
    }

    public Member save(Member member) {
        if (member.getId() == null) {
            return insert(member);
        }
        return update(member);
    }

    private Member insert(Member member) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(
                namedParameterJdbcTemplate.getJdbcTemplate()).withTableName(TABLE).usingGeneratedKeyColumns("id");
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(member);
        Long id = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();

        return Member.of(id, member.getEmail(), member.getNickname(), member.getBirthday(), member.getCreatedAt());
    }

    private Member update(Member member) {
        return member;
    }
}
