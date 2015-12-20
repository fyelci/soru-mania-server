package com.fyelci.sorumania.service;

import com.fyelci.sorumania.domain.Comment;
import com.fyelci.sorumania.domain.Question;
import com.fyelci.sorumania.repository.CommentRepository;
import com.fyelci.sorumania.repository.QuestionRepository;
import com.fyelci.sorumania.web.rest.dto.QuestionDTO;
import com.fyelci.sorumania.web.rest.mapper.QuestionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
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
    private CommentRepository commentRepository;

    @Inject
    private QuestionMapper questionMapper;

    public QuestionDTO getQuestion(Long id) {
        Question question = questionRepository.findOne(id);
        QuestionDTO questionDTO = questionMapper.questionToQuestionDTO(question);

        if (question != null) {
            List<Comment> commentList = commentRepository.findByQuestion(question);
            questionDTO.setCommentList(commentList);
        }

        return questionDTO;
    }
}
