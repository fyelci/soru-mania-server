package com.fyelci.sorumania.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fyelci.sorumania.domain.UserRelation;
import com.fyelci.sorumania.repository.UserRelationRepository;
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
 * REST controller for managing UserRelation.
 */
@RestController
@RequestMapping("/api")
public class UserRelationResource {

    private final Logger log = LoggerFactory.getLogger(UserRelationResource.class);
        
    @Inject
    private UserRelationRepository userRelationRepository;
    
    /**
     * POST  /userRelations -> Create a new userRelation.
     */
    @RequestMapping(value = "/userRelations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserRelation> createUserRelation(@RequestBody UserRelation userRelation) throws URISyntaxException {
        log.debug("REST request to save UserRelation : {}", userRelation);
        if (userRelation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userRelation", "idexists", "A new userRelation cannot already have an ID")).body(null);
        }
        UserRelation result = userRelationRepository.save(userRelation);
        return ResponseEntity.created(new URI("/api/userRelations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userRelation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /userRelations -> Updates an existing userRelation.
     */
    @RequestMapping(value = "/userRelations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserRelation> updateUserRelation(@RequestBody UserRelation userRelation) throws URISyntaxException {
        log.debug("REST request to update UserRelation : {}", userRelation);
        if (userRelation.getId() == null) {
            return createUserRelation(userRelation);
        }
        UserRelation result = userRelationRepository.save(userRelation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userRelation", userRelation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /userRelations -> get all the userRelations.
     */
    @RequestMapping(value = "/userRelations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserRelation>> getAllUserRelations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of UserRelations");
        Page<UserRelation> page = userRelationRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userRelations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /userRelations/:id -> get the "id" userRelation.
     */
    @RequestMapping(value = "/userRelations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserRelation> getUserRelation(@PathVariable Long id) {
        log.debug("REST request to get UserRelation : {}", id);
        UserRelation userRelation = userRelationRepository.findOne(id);
        return Optional.ofNullable(userRelation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userRelations/:id -> delete the "id" userRelation.
     */
    @RequestMapping(value = "/userRelations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserRelation(@PathVariable Long id) {
        log.debug("REST request to delete UserRelation : {}", id);
        userRelationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userRelation", id.toString())).build();
    }
}
