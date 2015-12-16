package com.fyelci.sorumania.repository;

import com.fyelci.sorumania.domain.QuestionRating;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the QuestionRating entity.
 */
public interface QuestionRatingRepository extends JpaRepository<QuestionRating,Long> {

    @Query("select questionRating from QuestionRating questionRating where questionRating.user.login = ?#{principal.username}")
    List<QuestionRating> findByUserIsCurrentUser();

}
