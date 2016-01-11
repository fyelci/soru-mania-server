package com.fyelci.sorumania.repository;

import com.fyelci.sorumania.domain.QuestionRating;

import org.springframework.data.jpa.repository.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Spring Data JPA repository for the QuestionRating entity.
 */
public interface QuestionRatingRepository extends JpaRepository<QuestionRating,Long> {

    @Query("select questionRating from QuestionRating questionRating where questionRating.user.login = ?#{principal.username}")
    List<QuestionRating> findByUserIsCurrentUser();

    @Query("select questionRating from QuestionRating questionRating where questionRating.user.login = ?#{principal.username} and questionRating.question.id = ?1")
    QuestionRating getUsersRate(Long questionId);

    @Query("select sum(questionRating.rate), count(questionRating.rate) from QuestionRating questionRating where questionRating.question.id = ?1")
    Object[] calculateRatingAvg(Long questionId);

}
