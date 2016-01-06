package com.fyelci.sorumania.service;

import com.fyelci.sorumania.config.Constants;
import com.fyelci.sorumania.domain.Comment;
import com.fyelci.sorumania.domain.Question;
import com.fyelci.sorumania.repository.CommentRepository;
import com.fyelci.sorumania.repository.QuestionRepository;
import com.fyelci.sorumania.security.AuthoritiesConstants;
import com.fyelci.sorumania.security.SecurityUtils;
import com.fyelci.sorumania.web.rest.dto.CommentDTO;
import com.fyelci.sorumania.web.rest.dto.QuestionDTO;
import com.fyelci.sorumania.web.rest.errors.CustomParameterizedException;
import com.fyelci.sorumania.web.rest.mapper.QuestionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by fatih on 20/12/15.
 */
@Service
@Transactional
public class QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionService.class);

    @Inject
    private QuestionRepository questionRepository;

    @Inject
    private CommentService commentService;

    @Inject
    private QuestionMapper questionMapper;

    public QuestionDTO saveQuestion(QuestionDTO questionDTO) {
        if (questionDTO.getUserId() == null) {
            throw new CustomParameterizedException("Soru oluştururken kullanıcı boş olamaz");
        } else if(questionDTO.getCategoryId() == null) {
            throw new CustomParameterizedException("Soru oluştururken kategori seçilmek zorundadır!");
        } else if(questionDTO.getLessonId() == null) {
            throw new CustomParameterizedException("Soru oluştururken ders seçilmek zorundadır!");
        } else if(questionDTO.getMediaUrl() == null) {
            throw new CustomParameterizedException("Soru oluştururken resim yüklemek zorunludur!");
        }

        if(questionDTO.getId() == null){
            //Insert
            questionDTO.setCreateDate(ZonedDateTime.now());
            questionDTO.setLastModifiedDate(ZonedDateTime.now());
            questionDTO.setCommentCount(0);
            questionDTO.setQuestionStatusId(Constants.QuestionStatus.ACTIVE);
        } else {
            //Update
            //Kullanıcının soruyu guncellemeye yetkisi var mı bak
            if (!SecurityUtils.getCurrentUser().getAuthorities().contains(AuthoritiesConstants.ADMIN)) {
                Question currentQuestion = questionRepository.findOne(questionDTO.getId());
                if (currentQuestion.getUser().getId().longValue() != questionDTO.getUserId().longValue()) {
                    throw new CustomParameterizedException("Bu soruyu siz olusturmadiginiz icin guncelleyemezsiniz!");
                }
            }

            questionDTO.setLastModifiedDate(ZonedDateTime.now());
        }
        Question question = questionMapper.questionDTOToQuestion(questionDTO);
        question = questionRepository.save(question);
        return questionMapper.questionToQuestionDTO(question);
    }

    @Transactional(readOnly = true)
    public QuestionDTO getQuestion(Long id) {
        Question question = questionRepository.findOne(id);
        QuestionDTO questionDTO = questionMapper.questionToQuestionDTO(question);

        if (question != null) {
            List<CommentDTO> commentList = commentService.findQuestionComments(question);
            questionDTO.setCommentList(commentList);
        }

        return questionDTO;
    }

    @Transactional(readOnly = true)
    public Page<Question> getAllQuestions(Pageable pageable, Long categoryId , Long lessonId, Integer listType) {
        Page<Question> page;

        if (categoryId != null
            && lessonId == null
            && listType == null) {
            page = questionRepository.listByCategory(categoryId, pageable);
        } else if (categoryId == null
            && lessonId != null
            && listType == null) {
            page = questionRepository.listByLesson(lessonId, pageable);
        } else if (categoryId != null
            && lessonId != null
            && listType == null) {
            page = questionRepository.listByCategoryAndLesson(categoryId, lessonId, pageable);
        } else {
            page = questionRepository.findAll(pageable);
        }

        return page;
    }
}
