package com.fyelci.sorumania.repository;

import com.fyelci.sorumania.domain.Comment;

import com.fyelci.sorumania.domain.Question;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Comment entity.
 */
public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("select comment from Comment comment where comment.user.login = ?#{principal.username}")
    List<Comment> findByUserIsCurrentUser();

    List<Comment> findByQuestion(Question q);

}
