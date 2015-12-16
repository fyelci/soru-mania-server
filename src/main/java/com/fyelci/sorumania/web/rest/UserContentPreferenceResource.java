package com.fyelci.sorumania.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fyelci.sorumania.domain.UserContentPreference;
import com.fyelci.sorumania.repository.UserContentPreferenceRepository;
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
 * REST controller for managing UserContentPreference.
 */
@RestController
@RequestMapping("/api")
public class UserContentPreferenceResource {

    private final Logger log = LoggerFactory.getLogger(UserContentPreferenceResource.class);
        
    @Inject
    private UserContentPreferenceRepository userContentPreferenceRepository;
    
    /**
     * POST  /userContentPreferences -> Create a new userContentPreference.
     */
    @RequestMapping(value = "/userContentPreferences",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserContentPreference> createUserContentPreference(@Valid @RequestBody UserContentPreference userContentPreference) throws URISyntaxException {
        log.debug("REST request to save UserContentPreference : {}", userContentPreference);
        if (userContentPreference.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userContentPreference", "idexists", "A new userContentPreference cannot already have an ID")).body(null);
        }
        UserContentPreference result = userContentPreferenceRepository.save(userContentPreference);
        return ResponseEntity.created(new URI("/api/userContentPreferences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userContentPreference", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /userContentPreferences -> Updates an existing userContentPreference.
     */
    @RequestMapping(value = "/userContentPreferences",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserContentPreference> updateUserContentPreference(@Valid @RequestBody UserContentPreference userContentPreference) throws URISyntaxException {
        log.debug("REST request to update UserContentPreference : {}", userContentPreference);
        if (userContentPreference.getId() == null) {
            return createUserContentPreference(userContentPreference);
        }
        UserContentPreference result = userContentPreferenceRepository.save(userContentPreference);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userContentPreference", userContentPreference.getId().toString()))
            .body(result);
    }

    /**
     * GET  /userContentPreferences -> get all the userContentPreferences.
     */
    @RequestMapping(value = "/userContentPreferences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserContentPreference>> getAllUserContentPreferences(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of UserContentPreferences");
        Page<UserContentPreference> page = userContentPreferenceRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userContentPreferences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /userContentPreferences/:id -> get the "id" userContentPreference.
     */
    @RequestMapping(value = "/userContentPreferences/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserContentPreference> getUserContentPreference(@PathVariable Long id) {
        log.debug("REST request to get UserContentPreference : {}", id);
        UserContentPreference userContentPreference = userContentPreferenceRepository.findOne(id);
        return Optional.ofNullable(userContentPreference)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userContentPreferences/:id -> delete the "id" userContentPreference.
     */
    @RequestMapping(value = "/userContentPreferences/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserContentPreference(@PathVariable Long id) {
        log.debug("REST request to delete UserContentPreference : {}", id);
        userContentPreferenceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userContentPreference", id.toString())).build();
    }
}
