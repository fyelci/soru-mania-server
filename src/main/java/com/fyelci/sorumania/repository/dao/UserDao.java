package com.fyelci.sorumania.repository.dao;

import com.fyelci.sorumania.config.Constants;
import com.fyelci.sorumania.domain.User;
import com.fyelci.sorumania.util.DateUtil;
import com.fyelci.sorumania.web.rest.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by fatih on 11/2/16.
 */

@Repository
public class UserDao {

    protected static final Logger logger = LoggerFactory.getLogger(UserDao.class);


    @Autowired
    private DataSource dataSource;

    @Transactional(readOnly = true)
    public List<UserDTO> getLeaderBoard(Integer listType, Integer pageNum, Integer pageSize) {

        List<UserDTO> userList = new ArrayList<>();

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        MapSqlParameterSource params = new MapSqlParameterSource();

        StringBuffer sql = new StringBuffer();

        sql.append(" select user_score_list.user_score ,  ");
        sql.append("        u.id, ");
        sql.append("        u.login, ");
        sql.append("        u.first_name, ");
        sql.append("        u.last_name, ");
        sql.append("        u.profile_image_url ");
        sql.append("   from jhi_user u, ");
        sql.append("   (select s.user_id, sum(score) user_score ");
        sql.append("      from score_history s ");
        sql.append("        where s.create_date > :paramDate ");
        sql.append(" 	group by s.user_id ");
        sql.append("   ) user_score_list ");
        sql.append(" where u.id = user_score_list.user_id ");
        sql.append(" order by 1 desc ");
        sql.append(" LIMIT :pageNum, :pageSize ");

        Date paramDate = new Date();
        if(listType.intValue() == Constants.LeaderboardListTypes.DAILY) {
            paramDate = DateUtil.subtractDays(paramDate, 1);
        } else if(listType.intValue() == Constants.LeaderboardListTypes.WEEKLY) {
            paramDate = DateUtil.subtractDays(paramDate, 7);
        } else if(listType.intValue() == Constants.LeaderboardListTypes.MONTHLY) {
            paramDate = DateUtil.subtractDays(paramDate, 30);
        }

        params.addValue("paramDate", new Timestamp(paramDate.getTime()), Types.TIMESTAMP);
        //params.addValue("paramDate", new java.sql.Date(paramDate.getTime()), Types.TIMESTAMP);
        params.addValue("pageNum", (pageNum -1) * pageSize );
        params.addValue("pageSize", pageSize);


        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql.toString(), params);
        for (Map<String, Object> row : rows) {
            User user = new User();
            user.setTotalScore(((BigDecimal) row.get("user_score")).longValue());
            user.setId((Long) row.get("id"));
            user.setLogin((String) row.get("login"));
            user.setFirstName((String) row.get("first_name"));
            user.setLastName((String) row.get("last_name"));
            user.setProfileImageUrl((String) row.get("profile_image_url"));
            userList.add(new UserDTO(user));
        }

        return userList;
    }

}
