package com.fyelci.sorumania.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fyelci.sorumania.domain.Question;
import com.fyelci.sorumania.repository.QuestionRepository;
import com.fyelci.sorumania.security.AuthoritiesConstants;
import com.fyelci.sorumania.security.SecurityUtils;
import com.fyelci.sorumania.service.QuestionService;
import com.fyelci.sorumania.web.rest.util.HeaderUtil;
import com.fyelci.sorumania.web.rest.util.PaginationUtil;
import com.fyelci.sorumania.web.rest.dto.QuestionDTO;
import com.fyelci.sorumania.web.rest.mapper.QuestionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Question.
 */
@RestController
@RequestMapping("/api")
public class QuestionResource {

    private final Logger log = LoggerFactory.getLogger(QuestionResource.class);

    @Inject
    private QuestionRepository questionRepository;

    @Inject
    private QuestionMapper questionMapper;

    @Inject
    private QuestionService questionService;

    /**
     * POST  /questions -> Create a new question.
     */
    @RequestMapping(value = "/questions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO questionDTO) throws URISyntaxException {
        log.debug("REST request to save Question : {}", questionDTO);
        if (questionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("question", "idexists", "A new question cannot already have an ID")).body(null);
        }
        QuestionDTO result = questionService.saveQuestion(questionDTO);
        return ResponseEntity.created(new URI("/api/questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("question", result.getId().toString()))
            .body(result);
    }



    /**
     * PUT  /questions -> Updates an existing question.
     */
    @RequestMapping(value = "/questions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<QuestionDTO> updateQuestion(@RequestBody QuestionDTO questionDTO) throws URISyntaxException {
        log.debug("REST request to update Question : {}", questionDTO);
        if (questionDTO.getId() == null) {
            return createQuestion(questionDTO);
        }
        QuestionDTO result = questionService.saveQuestion(questionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("question", questionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /questions -> get all the questions.
     */
    @RequestMapping(value = "/questions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<QuestionDTO>> getAllQuestions(Pageable pageable,
                                                             @RequestParam(required = false) Long categoryId,
                                                             @RequestParam(required = false) Long lessonId,
                                                             @RequestParam(required = false) Integer listType)
        throws URISyntaxException {
        log.debug("REST request to get a page of Questions");
        Page<Question> page = questionService.getAllQuestions(pageable, categoryId, lessonId, listType);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/questions");
        return new ResponseEntity<>(page.getContent().stream()
            .map(questionMapper::questionToQuestionDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /questions/:id -> get the "id" question.
     */
    @RequestMapping(value = "/questions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<QuestionDTO> getQuestion(@PathVariable Long id) {
        QuestionDTO questionDTO = questionService.getQuestion(id);
        return Optional.ofNullable(questionDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /questions/:id -> delete the "id" question.
     */
    @RequestMapping(value = "/questions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        log.debug("REST request to delete Question : {}", id);
        if (!SecurityUtils.getCurrentUser().getAuthorities().contains(new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("question", "notAllowed", "You can not delete question")).body(null);
        }
        questionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("question", id.toString())).build();
    }


    /**
     * GET  /questions/asked/{userId} -> get user asked questions.
     */
    @RequestMapping(value = "/questions/asked/{userId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<QuestionDTO>> listUserAskedQuestions(@PathVariable Long userId,
                                                                    Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Questions");
        Page<Question> page = questionService.listUserAskedQuestions(userId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/questions/asked/" + userId.toString());
        return new ResponseEntity<>(page.getContent().stream()
            .map(questionMapper::questionToQuestionDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /questions/answered/{userId} -> get user asked questions.
     */
    @RequestMapping(value = "/questions/answered/{userId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<QuestionDTO>> listUserAnsweredQuestions(@PathVariable Long userId,
                                                                    Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Questions");
        Page<Question> page = questionService.listUserAnsweredQuestions(userId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/questions/answered/" + userId.toString());
        return new ResponseEntity<>(page.getContent().stream()
            .map(questionMapper::questionToQuestionDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }
    /**
     * GET  /questions/answered/{userId} -> get user asked questions.
     */
    @RequestMapping(value = "/questions/watched/{userId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<QuestionDTO>> listUserWatchingQuestions(@PathVariable Long userId,
                                                                    Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Questions");
        Page<Question> page = questionService.listUserWatchingQuestions(userId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/questions/watched/" + userId.toString());
        return new ResponseEntity<>(page.getContent().stream()
            .map(questionMapper::questionToQuestionDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }
}
