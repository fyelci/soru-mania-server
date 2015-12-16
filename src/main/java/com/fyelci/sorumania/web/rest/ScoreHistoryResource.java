package com.fyelci.sorumania.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fyelci.sorumania.domain.ScoreHistory;
import com.fyelci.sorumania.repository.ScoreHistoryRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ScoreHistory.
 */
@RestController
@RequestMapping("/api")
public class ScoreHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ScoreHistoryResource.class);
        
    @Inject
    private ScoreHistoryRepository scoreHistoryRepository;
    
    /**
     * POST  /scoreHistorys -> Create a new scoreHistory.
     */
    @RequestMapping(value = "/scoreHistorys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ScoreHistory> createScoreHistory(@RequestBody ScoreHistory scoreHistory) throws URISyntaxException {
        log.debug("REST request to save ScoreHistory : {}", scoreHistory);
        if (scoreHistory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("scoreHistory", "idexists", "A new scoreHistory cannot already have an ID")).body(null);
        }
        ScoreHistory result = scoreHistoryRepository.save(scoreHistory);
        return ResponseEntity.created(new URI("/api/scoreHistorys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("scoreHistory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scoreHistorys -> Updates an existing scoreHistory.
     */
    @RequestMapping(value = "/scoreHistorys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ScoreHistory> updateScoreHistory(@RequestBody ScoreHistory scoreHistory) throws URISyntaxException {
        log.debug("REST request to update ScoreHistory : {}", scoreHistory);
        if (scoreHistory.getId() == null) {
            return createScoreHistory(scoreHistory);
        }
        ScoreHistory result = scoreHistoryRepository.save(scoreHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("scoreHistory", scoreHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scoreHistorys -> get all the scoreHistorys.
     */
    @RequestMapping(value = "/scoreHistorys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ScoreHistory>> getAllScoreHistorys(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ScoreHistorys");
        Page<ScoreHistory> page = scoreHistoryRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scoreHistorys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /scoreHistorys/:id -> get the "id" scoreHistory.
     */
    @RequestMapping(value = "/scoreHistorys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ScoreHistory> getScoreHistory(@PathVariable Long id) {
        log.debug("REST request to get ScoreHistory : {}", id);
        ScoreHistory scoreHistory = scoreHistoryRepository.findOne(id);
        return Optional.ofNullable(scoreHistory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /scoreHistorys/:id -> delete the "id" scoreHistory.
     */
    @RequestMapping(value = "/scoreHistorys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteScoreHistory(@PathVariable Long id) {
        log.debug("REST request to delete ScoreHistory : {}", id);
        scoreHistoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("scoreHistory", id.toString())).build();
    }
}
