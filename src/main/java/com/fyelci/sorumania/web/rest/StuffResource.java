package com.fyelci.sorumania.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fyelci.sorumania.domain.Stuff;
import com.fyelci.sorumania.repository.StuffRepository;
import com.fyelci.sorumania.web.rest.util.HeaderUtil;
import com.fyelci.sorumania.web.rest.util.PaginationUtil;
import com.fyelci.sorumania.web.rest.dto.StuffDTO;
import com.fyelci.sorumania.web.rest.mapper.StuffMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
 * REST controller for managing Stuff.
 */
@RestController
@RequestMapping("/api")
public class StuffResource {

    private final Logger log = LoggerFactory.getLogger(StuffResource.class);
        
    @Inject
    private StuffRepository stuffRepository;
    
    @Inject
    private StuffMapper stuffMapper;
    
    /**
     * POST  /stuffs -> Create a new stuff.
     */
    @RequestMapping(value = "/stuffs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StuffDTO> createStuff(@RequestBody StuffDTO stuffDTO) throws URISyntaxException {
        log.debug("REST request to save Stuff : {}", stuffDTO);
        if (stuffDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("stuff", "idexists", "A new stuff cannot already have an ID")).body(null);
        }
        Stuff stuff = stuffMapper.stuffDTOToStuff(stuffDTO);
        stuff = stuffRepository.save(stuff);
        StuffDTO result = stuffMapper.stuffToStuffDTO(stuff);
        return ResponseEntity.created(new URI("/api/stuffs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("stuff", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stuffs -> Updates an existing stuff.
     */
    @RequestMapping(value = "/stuffs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StuffDTO> updateStuff(@RequestBody StuffDTO stuffDTO) throws URISyntaxException {
        log.debug("REST request to update Stuff : {}", stuffDTO);
        if (stuffDTO.getId() == null) {
            return createStuff(stuffDTO);
        }
        Stuff stuff = stuffMapper.stuffDTOToStuff(stuffDTO);
        stuff = stuffRepository.save(stuff);
        StuffDTO result = stuffMapper.stuffToStuffDTO(stuff);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("stuff", stuffDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stuffs -> get all the stuffs.
     */
    @RequestMapping(value = "/stuffs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<StuffDTO>> getAllStuffs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Stuffs");
        Page<Stuff> page = stuffRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stuffs");
        return new ResponseEntity<>(page.getContent().stream()
            .map(stuffMapper::stuffToStuffDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /stuffs/:id -> get the "id" stuff.
     */
    @RequestMapping(value = "/stuffs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StuffDTO> getStuff(@PathVariable Long id) {
        log.debug("REST request to get Stuff : {}", id);
        Stuff stuff = stuffRepository.findOne(id);
        StuffDTO stuffDTO = stuffMapper.stuffToStuffDTO(stuff);
        return Optional.ofNullable(stuffDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stuffs/:id -> delete the "id" stuff.
     */
    @RequestMapping(value = "/stuffs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStuff(@PathVariable Long id) {
        log.debug("REST request to delete Stuff : {}", id);
        stuffRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stuff", id.toString())).build();
    }
}
