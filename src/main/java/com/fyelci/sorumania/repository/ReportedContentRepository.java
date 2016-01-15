package com.fyelci.sorumania.repository;

import com.fyelci.sorumania.domain.Comment;
import com.fyelci.sorumania.domain.Question;
import com.fyelci.sorumania.domain.ReportedContent;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ReportedContent entity.
 */
public interface ReportedContentRepository extends JpaRepository<ReportedContent,Long> {

    @Query("select reportedContent from ReportedContent reportedContent where reportedContent.reporterUser.login = ?#{principal.username}")
    List<ReportedContent> findByReporterUserIsCurrentUser();

    @Query("select reportedContent from ReportedContent reportedContent " +
        " where reportedContent.reporterUser.login = ?#{principal.username}" +
        " and reportedContent.question.id = ?1" +
        " and reportedContent.comment.id = ?2")
    ReportedContent findUsersCommentReport(Long questionId, Long commentId);


    @Query("select reportedContent from ReportedContent reportedContent " +
        " where reportedContent.reporterUser.login = ?#{principal.username}" +
        " and reportedContent.question.id = ?1" +
        " and reportedContent.comment.id is null")
    ReportedContent findUsersQuestionReport(Long questionId);

    Long countByQuestionAndComment(Question question, Comment comment);

    Long countByQuestionAndCommentIsNull(Question question);

}
