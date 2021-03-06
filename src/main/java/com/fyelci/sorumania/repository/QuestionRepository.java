package com.fyelci.sorumania.repository;

import com.fyelci.sorumania.config.Constants;
import com.fyelci.sorumania.domain.Question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Question entity.
 */
public interface QuestionRepository extends JpaRepository<Question,Long> {

    @Query("select question from Question question where question.user.login = ?#{principal.username}")
    List<Question> findByUserIsCurrentUser();

    @Query("select question from Question question where question.category.id = ?1 and question.questionStatus.id = " + Constants.QuestionStatus.ACTIVE)
    Page<Question> listByCategory(Long categoryId, Pageable pageable);

    @Query("select question from Question question where question.lesson.id = ?1 and question.questionStatus.id = " + Constants.QuestionStatus.ACTIVE)
    Page<Question> listByLesson(Long lessonId, Pageable pageable);

    @Query("select question from Question question where question.category.id = ?1 and question.lesson.id = ?2 and question.questionStatus.id = " + Constants.QuestionStatus.ACTIVE)
    Page<Question> listByCategoryAndLesson(Long categoryId, Long lessonId, Pageable pageable);

    @Query("select question from Question question where question.questionStatus.id = " + Constants.QuestionStatus.ACTIVE)
    Page<Question> listAll(Pageable pageable);


    @Query("select question from Question question where question.user.id = ?1 and question.questionStatus.id = " + Constants.QuestionStatus.ACTIVE + " order by question.createDate desc")
    Page<Question> listUserAskedQuestions(Long userId, Pageable pageable);

    @Query("select distinct question from Question question, Comment comment where comment.user.id = ?1 and question.id = comment.question.id and question.questionStatus.id = " + Constants.QuestionStatus.ACTIVE + " order by question.createDate desc")
    Page<Question> listUserAnsweredQuestions(Long userId, Pageable pageable);

    @Query("select distinct question " +
           " from Question question, UserContentPreference ucp " +
           " where ucp.user.id = ?1 and ucp.contentType.id = " + Constants.ContentTypes.QUESTION +
           " and ucp.contentPreference.id = "+ Constants.ContentPreferences.WATCH +
           " and question.id = ucp.contentId " +
           " and question.questionStatus.id = " + Constants.QuestionStatus.ACTIVE +
           " order by question.createDate desc")
    Page<Question> listUserWatchingQuestions(Long userId, Pageable pageable);

}
