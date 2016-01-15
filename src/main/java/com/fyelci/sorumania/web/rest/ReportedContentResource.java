package com.fyelci.sorumania.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fyelci.sorumania.domain.ReportedContent;
import com.fyelci.sorumania.repository.ReportedContentRepository;
import com.fyelci.sorumania.service.ReportedContentService;
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
 * REST controller for managing ReportedContent.
 */
@RestController
@RequestMapping("/api")
public class ReportedContentResource {

    private final Logger log = LoggerFactory.getLogger(ReportedContentResource.class);

    @Inject
    private ReportedContentRepository reportedContentRepository;

    @Inject
    private ReportedContentService reportedContentService;

    /**
     * POST  /reportedContents -> Create a new reportedContent.
     */
    @RequestMapping(value = "/reportedContents",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReportedContent> createReportedContent(@RequestBody ReportedContent reportedContent) throws URISyntaxException {
        log.debug("REST request to save ReportedContent : {}", reportedContent);
        if (reportedContent.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("reportedContent", "idexists", "A new reportedContent cannot already have an ID")).body(null);
        }
        ReportedContent result = reportedContentService.save(reportedContent);
        return ResponseEntity.created(new URI("/api/reportedContents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("reportedContent", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reportedContents -> Updates an existing reportedContent.
     */
    @RequestMapping(value = "/reportedContents",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReportedContent> updateReportedContent(@RequestBody ReportedContent reportedContent) throws URISyntaxException {
        log.debug("REST request to update ReportedContent : {}", reportedContent);
        if (reportedContent.getId() == null) {
            return createReportedContent(reportedContent);
        }
        ReportedContent result = reportedContentService.save(reportedContent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("reportedContent", reportedContent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reportedContents -> get all the reportedContents.
     */
    @RequestMapping(value = "/reportedContents",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ReportedContent>> getAllReportedContents(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ReportedContents");
        Page<ReportedContent> page = reportedContentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reportedContents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reportedContents/:id -> get the "id" reportedContent.
     */
    @RequestMapping(value = "/reportedContents/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReportedContent> getReportedContent(@PathVariable Long id) {
        log.debug("REST request to get ReportedContent : {}", id);
        ReportedContent reportedContent = reportedContentRepository.findOne(id);
        return Optional.ofNullable(reportedContent)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /reportedContents/:id -> delete the "id" reportedContent.
     */
    @RequestMapping(value = "/reportedContents/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteReportedContent(@PathVariable Long id) {
        log.debug("REST request to delete ReportedContent : {}", id);
        reportedContentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("reportedContent", id.toString())).build();
    }
}
