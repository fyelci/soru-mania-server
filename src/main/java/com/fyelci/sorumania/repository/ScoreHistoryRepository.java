package com.fyelci.sorumania.repository;

import com.fyelci.sorumania.domain.ScoreHistory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ScoreHistory entity.
 */
public interface ScoreHistoryRepository extends JpaRepository<ScoreHistory,Long> {

    @Query("select scoreHistory from ScoreHistory scoreHistory where scoreHistory.user.login = ?#{principal.username}")
    List<ScoreHistory> findByUserIsCurrentUser();

}
