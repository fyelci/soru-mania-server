package com.fyelci.sorumania.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fyelci.sorumania.domain.QuestionRating;
import com.fyelci.sorumania.repository.QuestionRatingRepository;
import com.fyelci.sorumania.web.rest.util.HeaderUtil;
import com.fyelci.sorumania.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing QuestionRating.
 */
@RestController
@RequestMapping("/api")
public class QuestionRatingResource {

    private final Logger log = LoggerFactory.getLogger(QuestionRatingResource.class);
        
    @Inject
    private QuestionRatingRepository questionRatingRepository;
    
    /**
     * POST  /questionRatings -> Create a new questionRating.
     */
    @RequestMapping(value = "/questionRatings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<QuestionRating> createQuestionRating(@Valid @RequestBody QuestionRating questionRating) throws URISyntaxException {
        log.debug("REST request to save QuestionRating : {}", questionRating);
        if (questionRating.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("questionRating", "idexists", "A new questionRating cannot already have an ID")).body(null);
        }
        QuestionRating result = questionRatingRepository.save(questionRating);
        return ResponseEntity.created(new URI("/api/questionRatings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("questionRating", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /questionRatings -> Updates an existing questionRating.
     */
    @RequestMapping(value = "/questionRatings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<QuestionRating> updateQuestionRating(@Valid @RequestBody QuestionRating questionRating) throws URISyntaxException {
        log.debug("REST request to update QuestionRating : {}", questionRating);
        if (questionRating.getId() == null) {
            return createQuestionRating(questionRating);
        }
        QuestionRating result = questionRatingRepository.save(questionRating);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("questionRating", questionRating.getId().toString()))
            .body(result);
    }

    /**
     * GET  /questionRatings -> get all the questionRatings.
     */
    @RequestMapping(value = "/questionRatings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<QuestionRating>> getAllQuestionRatings(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of QuestionRatings");
        Page<QuestionRating> page = questionRatingRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/questionRatings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /questionRatings/:id -> get the "id" questionRating.
     */
    @RequestMapping(value = "/questionRatings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<QuestionRating> getQuestionRating(@PathVariable Long id) {
        log.debug("REST request to get QuestionRating : {}", id);
        QuestionRating questionRating = questionRatingRepository.findOne(id);
        return Optional.ofNullable(questionRating)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /questionRatings/:id -> delete the "id" questionRating.
     */
    @RequestMapping(value = "/questionRatings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteQuestionRating(@PathVariable Long id) {
        log.debug("REST request to delete QuestionRating : {}", id);
        questionRatingRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("questionRating", id.toString())).build();
    }
}
