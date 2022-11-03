package com.example.fastcampusmysql.domain.member.repository;

import com.example.fastcampusmysql.domain.member.entity.Member;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    private static final RowMapper<Member> rowMapper = (ResultSet resultSet, int rowNum) -> Member.builder()
            .id(resultSet.getLong("id"))
            .email(resultSet.getString("email"))
            .nickname(resultSet.getString("nickname"))
            .birthday(resultSet.getObject("birthday", LocalDate.class))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<Member> findById(Long id) {
        String sql = String.format("SELECT * FROM %s WHERE id = :id", TABLE);
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource().addValue("id", id);
        return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, rowMapper));
    }

    public List<Member> findAllByMemberIdIn(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        String sql = String.format("SELECT * FROM %s WHERE id in (:ids)", TABLE);
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource().addValue("ids", ids);
        return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, rowMapper);
    }

    public Member save(Member member) {
        if (member.getId() == null) {
            return insert(member);
        }
        return update(member);
    }

    private Member insert(Member member) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(member);
        Long id = getSimpleJdbcInsert().executeAndReturnKey(sqlParameterSource).longValue();

        return Member.builder()
                .id(id)
                .email(member.getEmail())
                .nickname(member.getNickname())
                .grade(member.getGrade())
                .birthday(member.getBirthday())
                .createdAt(member.getCreatedAt())
                .build();
    }

    private Member update(Member member) {
        String sql = String.format(
                "UPDATE %s SET email = :email, nickname = :nickname, birthday = :birthday WHERE id = :id",
                TABLE);
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(member);
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);

        return member;
    }

    private SimpleJdbcInsert getSimpleJdbcInsert() {
        return new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate()).withTableName(TABLE)
                .usingGeneratedKeyColumns("id");
    }
}
