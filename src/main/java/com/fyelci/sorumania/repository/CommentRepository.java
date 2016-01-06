package com.fyelci.sorumania.repository;

import com.fyelci.sorumania.config.Constants;
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

    List<Comment> findByQuestionOrderByCreateDateDesc(Question q);

    @Query("select count(comment.id) from Comment comment where comment.question.id = ?1 and comment.parentId is null and comment.commentStatus.id = " + Constants.CommentStatus.ACTIVE)
    Long countQuestionComment(Long questionId);

}
