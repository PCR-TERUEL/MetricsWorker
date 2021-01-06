package com.URLShortener.worker.repository;

import com.URLShortener.worker.domain.ShortURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

@Repository
public class ShortURLRepository {

  private static final RowMapper<ShortURL> rowMapper =
      (rs, rowNum) -> new ShortURL(rs.getString("hash"), rs.getString("target"),
          null, rs.getString("sponsor"), rs.getDate("created"),rs.getDate("expiration"),
          rs.getLong("owner"), rs.getInt("mode"),
          rs.getBoolean("safe"), rs.getString("ip"),
          rs.getString("country"));

  private static final RowMapper<Long> rowMapperCount =
          (rs, rowNum) -> rs.getLong(1);

  private final JdbcTemplate jdbc;

  public ShortURLRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public List<ShortURL> findByUser(String userId) {
    try {

      List<ShortURL>  shortURLS = jdbc.query("SELECT * FROM shorturl WHERE owner = ?",
                                  new Object[] {userId}, rowMapper);

      for (ShortURL url : shortURLS) {
        url.setClicks(countClicks(url));
      }
      return  shortURLS;
    } catch (Exception e) {
      return Collections.emptyList();
    }
  }

  private Long countClicks(ShortURL su) {
    return jdbc.query("SELECT count(*) FROM click WHERE HASH = ?", new Object[] {su.getHash()},
            rowMapperCount).get(0);
  }
}
