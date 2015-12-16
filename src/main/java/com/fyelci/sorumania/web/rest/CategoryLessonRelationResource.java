package com.fyelci.sorumania.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fyelci.sorumania.domain.CategoryLessonRelation;
import com.fyelci.sorumania.repository.CategoryLessonRelationRepository;
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
 * REST controller for managing CategoryLessonRelation.
 */
@RestController
@RequestMapping("/api")
public class CategoryLessonRelationResource {

    private final Logger log = LoggerFactory.getLogger(CategoryLessonRelationResource.class);
        
    @Inject
    private CategoryLessonRelationRepository categoryLessonRelationRepository;
    
    /**
     * POST  /categoryLessonRelations -> Create a new categoryLessonRelation.
     */
    @RequestMapping(value = "/categoryLessonRelations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CategoryLessonRelation> createCategoryLessonRelation(@RequestBody CategoryLessonRelation categoryLessonRelation) throws URISyntaxException {
        log.debug("REST request to save CategoryLessonRelation : {}", categoryLessonRelation);
        if (categoryLessonRelation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("categoryLessonRelation", "idexists", "A new categoryLessonRelation cannot already have an ID")).body(null);
        }
        CategoryLessonRelation result = categoryLessonRelationRepository.save(categoryLessonRelation);
        return ResponseEntity.created(new URI("/api/categoryLessonRelations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("categoryLessonRelation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /categoryLessonRelations -> Updates an existing categoryLessonRelation.
     */
    @RequestMapping(value = "/categoryLessonRelations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CategoryLessonRelation> updateCategoryLessonRelation(@RequestBody CategoryLessonRelation categoryLessonRelation) throws URISyntaxException {
        log.debug("REST request to update CategoryLessonRelation : {}", categoryLessonRelation);
        if (categoryLessonRelation.getId() == null) {
            return createCategoryLessonRelation(categoryLessonRelation);
        }
        CategoryLessonRelation result = categoryLessonRelationRepository.save(categoryLessonRelation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("categoryLessonRelation", categoryLessonRelation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /categoryLessonRelations -> get all the categoryLessonRelations.
     */
    @RequestMapping(value = "/categoryLessonRelations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CategoryLessonRelation>> getAllCategoryLessonRelations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CategoryLessonRelations");
        Page<CategoryLessonRelation> page = categoryLessonRelationRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/categoryLessonRelations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /categoryLessonRelations/:id -> get the "id" categoryLessonRelation.
     */
    @RequestMapping(value = "/categoryLessonRelations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CategoryLessonRelation> getCategoryLessonRelation(@PathVariable Long id) {
        log.debug("REST request to get CategoryLessonRelation : {}", id);
        CategoryLessonRelation categoryLessonRelation = categoryLessonRelationRepository.findOne(id);
        return Optional.ofNullable(categoryLessonRelation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /categoryLessonRelations/:id -> delete the "id" categoryLessonRelation.
     */
    @RequestMapping(value = "/categoryLessonRelations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCategoryLessonRelation(@PathVariable Long id) {
        log.debug("REST request to delete CategoryLessonRelation : {}", id);
        categoryLessonRelationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("categoryLessonRelation", id.toString())).build();
    }
}
