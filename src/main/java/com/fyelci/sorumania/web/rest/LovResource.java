package com.fyelci.sorumania.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fyelci.sorumania.config.Constants;
import com.fyelci.sorumania.domain.Lov;
import com.fyelci.sorumania.repository.LovRepository;
import com.fyelci.sorumania.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Lov.
 */
@RestController
@RequestMapping("/api")
public class LovResource {

    private final Logger log = LoggerFactory.getLogger(LovResource.class);

    @Inject
    private LovRepository lovRepository;

    /**
     * POST  /lovs -> Create a new lov.
     */
    @RequestMapping(value = "/lovs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lov> createLov(@Valid @RequestBody Lov lov) throws URISyntaxException {
        log.debug("REST request to save Lov : {}", lov);
        if (lov.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lov", "idexists", "A new lov cannot already have an ID")).body(null);
        }
        Lov result = lovRepository.save(lov);
        return ResponseEntity.created(new URI("/api/lovs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lov", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lovs -> Updates an existing lov.
     */
    @RequestMapping(value = "/lovs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lov> updateLov(@Valid @RequestBody Lov lov) throws URISyntaxException {
        log.debug("REST request to update Lov : {}", lov);
        if (lov.getId() == null) {
            return createLov(lov);
        }
        Lov result = lovRepository.save(lov);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lov", lov.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lovs -> get all the lovs.
     */
    @RequestMapping(value = "/lovs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Lov> getAllLovs() {
        log.debug("REST request to get all Lovs");
        return lovRepository.findAll();
            }

    /**
     * GET  /lovs/:id -> get the "id" lov.
     */
    @RequestMapping(value = "/lovs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lov> getLov(@PathVariable Long id) {
        log.debug("REST request to get Lov : {}", id);
        Lov lov = lovRepository.findOne(id);
        return Optional.ofNullable(lov)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lovs/:id -> delete the "id" lov.
     */
    @RequestMapping(value = "/lovs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLov(@PathVariable Long id) {
        log.debug("REST request to delete Lov : {}", id);
        lovRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lov", id.toString())).build();
    }

    @RequestMapping(value = "/lovs/type",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Lov> getManagedLovs() {
        log.debug("REST request to get all Lovs");
        return lovRepository.findByTypeOrderBySequenceAsc(Constants.MANAGABLE_LOV_TYPES);
    }

    @RequestMapping(value = "/lovs/type/{type}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Lov> getByType(@PathVariable String type) {
        log.debug("REST request to get Lov by type: {}", type);
        return lovRepository.findByTypeOrderBySequenceAsc(type);
    }

}
