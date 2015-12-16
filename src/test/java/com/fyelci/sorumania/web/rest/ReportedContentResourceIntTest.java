package com.fyelci.sorumania.web.rest;

import com.fyelci.sorumania.Application;
import com.fyelci.sorumania.domain.ReportedContent;
import com.fyelci.sorumania.repository.ReportedContentRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ReportedContentResource REST controller.
 *
 * @see ReportedContentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ReportedContentResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATE_DATE);

    private static final ZonedDateTime DEFAULT_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_MODIFIED_DATE_STR = dateTimeFormatter.format(DEFAULT_LAST_MODIFIED_DATE);

    @Inject
    private ReportedContentRepository reportedContentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restReportedContentMockMvc;

    private ReportedContent reportedContent;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReportedContentResource reportedContentResource = new ReportedContentResource();
        ReflectionTestUtils.setField(reportedContentResource, "reportedContentRepository", reportedContentRepository);
        this.restReportedContentMockMvc = MockMvcBuilders.standaloneSetup(reportedContentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        reportedContent = new ReportedContent();
        reportedContent.setCreateDate(DEFAULT_CREATE_DATE);
        reportedContent.setLastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createReportedContent() throws Exception {
        int databaseSizeBeforeCreate = reportedContentRepository.findAll().size();

        // Create the ReportedContent

        restReportedContentMockMvc.perform(post("/api/reportedContents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reportedContent)))
                .andExpect(status().isCreated());

        // Validate the ReportedContent in the database
        List<ReportedContent> reportedContents = reportedContentRepository.findAll();
        assertThat(reportedContents).hasSize(databaseSizeBeforeCreate + 1);
        ReportedContent testReportedContent = reportedContents.get(reportedContents.size() - 1);
        assertThat(testReportedContent.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testReportedContent.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllReportedContents() throws Exception {
        // Initialize the database
        reportedContentRepository.saveAndFlush(reportedContent);

        // Get all the reportedContents
        restReportedContentMockMvc.perform(get("/api/reportedContents?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(reportedContent.getId().intValue())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
                .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE_STR)));
    }

    @Test
    @Transactional
    public void getReportedContent() throws Exception {
        // Initialize the database
        reportedContentRepository.saveAndFlush(reportedContent);

        // Get the reportedContent
        restReportedContentMockMvc.perform(get("/api/reportedContents/{id}", reportedContent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(reportedContent.getId().intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingReportedContent() throws Exception {
        // Get the reportedContent
        restReportedContentMockMvc.perform(get("/api/reportedContents/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReportedContent() throws Exception {
        // Initialize the database
        reportedContentRepository.saveAndFlush(reportedContent);

		int databaseSizeBeforeUpdate = reportedContentRepository.findAll().size();

        // Update the reportedContent
        reportedContent.setCreateDate(UPDATED_CREATE_DATE);
        reportedContent.setLastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restReportedContentMockMvc.perform(put("/api/reportedContents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reportedContent)))
                .andExpect(status().isOk());

        // Validate the ReportedContent in the database
        List<ReportedContent> reportedContents = reportedContentRepository.findAll();
        assertThat(reportedContents).hasSize(databaseSizeBeforeUpdate);
        ReportedContent testReportedContent = reportedContents.get(reportedContents.size() - 1);
        assertThat(testReportedContent.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testReportedContent.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void deleteReportedContent() throws Exception {
        // Initialize the database
        reportedContentRepository.saveAndFlush(reportedContent);

		int databaseSizeBeforeDelete = reportedContentRepository.findAll().size();

        // Get the reportedContent
        restReportedContentMockMvc.perform(delete("/api/reportedContents/{id}", reportedContent.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ReportedContent> reportedContents = reportedContentRepository.findAll();
        assertThat(reportedContents).hasSize(databaseSizeBeforeDelete - 1);
    }
}
