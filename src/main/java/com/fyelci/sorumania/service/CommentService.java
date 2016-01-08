package com.fyelci.sorumania.service;

import com.fyelci.sorumania.config.Constants;
import com.fyelci.sorumania.domain.Comment;
import com.fyelci.sorumania.domain.Question;
import com.fyelci.sorumania.repository.CommentRepository;
import com.fyelci.sorumania.repository.QuestionRepository;
import com.fyelci.sorumania.web.rest.dto.CommentDTO;
import com.fyelci.sorumania.web.rest.errors.CustomParameterizedException;
import com.fyelci.sorumania.web.rest.mapper.CommentMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Comment.
 */
@Service
@Transactional
public class CommentService {

    private final Logger log = LoggerFactory.getLogger(CommentService.class);

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private QuestionRepository questionRepository;

    @Inject
    private CommentMapper commentMapper;

    @Inject
    private ScoreService scoreService;

    /**
     * Save a comment.
     * @return the persisted entity
     */
    public CommentDTO save(CommentDTO commentDTO) {
        log.debug("Request to save Comment : {}", commentDTO);
        if (commentDTO.getUserId() == null) {
            throw new CustomParameterizedException("Cevap oluştururken kullanıcı boş olamaz");
        } else if (StringUtils.isBlank(commentDTO.getMediaUrl()) && StringUtils.isBlank(commentDTO.getText())) {
            throw new CustomParameterizedException("Cevap vermek icin aciklama ya da resim eklemeniz gerekir!");
        }

        if(commentDTO.getId() == null){
            //Insert
            commentDTO.setCreateDate(ZonedDateTime.now());
            commentDTO.setLastModifiedDate(ZonedDateTime.now());
            commentDTO.setCommentStatusId(Constants.CommentStatus.ACTIVE);
        } else {
            //Update
            commentDTO.setLastModifiedDate(ZonedDateTime.now());
        }
        Comment comment = commentMapper.commentDTOToComment(commentDTO);
        comment = commentRepository.save(comment);

        //Update comment count
        Long commentCount = commentRepository.countQuestionComment(comment.getQuestion().getId());
        Question question = questionRepository.findOne(comment.getQuestion().getId());
        question.setCommentCount(commentCount.intValue());
        questionRepository.save(question);

        //Kullaniciya yaptigi islem icin paun ver
        scoreService.addScoreToUser(comment.getUser(), comment.getId(), Constants.ContentTypes.COMMENT, Constants.ScoreTypes.COMMENT);

        CommentDTO result = commentMapper.commentToCommentDTO(comment);
        return result;
    }

    /**
     *  get all the comments.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Comment> findAll(Pageable pageable) {
        log.debug("Request to get all Comments");
        Page<Comment> result = commentRepository.findAll(pageable);
        return result;
    }

    /**
     * Gets all comments in a question
     * @param q
     * @return
     */
    @Transactional(readOnly = true)
    public List<CommentDTO> findQuestionComments(Question q) {
        log.debug("Getting Question Comments");
        List<Comment> result = commentRepository.findByQuestionOrderByCreateDateDesc(q);
        return result.stream()
            .map(commentMapper::commentToCommentDTO)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  get one comment by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CommentDTO findOne(Long id) {
        log.debug("Request to get Comment : {}", id);
        Comment comment = commentRepository.findOne(id);
        CommentDTO commentDTO = commentMapper.commentToCommentDTO(comment);
        return commentDTO;
    }

    /**
     *  delete the  comment by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Comment : {}", id);
        commentRepository.delete(id);
    }
}
