package com.fyelci.sorumania.web.rest;

import com.fyelci.sorumania.Application;
import com.fyelci.sorumania.domain.ScoreHistory;
import com.fyelci.sorumania.repository.ScoreHistoryRepository;

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
 * Test class for the ScoreHistoryResource REST controller.
 *
 * @see ScoreHistoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ScoreHistoryResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final Integer DEFAULT_SCORE = 1;
    private static final Integer UPDATED_SCORE = 2;

    private static final Long DEFAULT_CONTENT_ID = 1L;
    private static final Long UPDATED_CONTENT_ID = 2L;

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATE_DATE);

    @Inject
    private ScoreHistoryRepository scoreHistoryRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restScoreHistoryMockMvc;

    private ScoreHistory scoreHistory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ScoreHistoryResource scoreHistoryResource = new ScoreHistoryResource();
        ReflectionTestUtils.setField(scoreHistoryResource, "scoreHistoryRepository", scoreHistoryRepository);
        this.restScoreHistoryMockMvc = MockMvcBuilders.standaloneSetup(scoreHistoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        scoreHistory = new ScoreHistory();
        scoreHistory.setScore(DEFAULT_SCORE);
        scoreHistory.setContentId(DEFAULT_CONTENT_ID);
        scoreHistory.setCreateDate(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void createScoreHistory() throws Exception {
        int databaseSizeBeforeCreate = scoreHistoryRepository.findAll().size();

        // Create the ScoreHistory

        restScoreHistoryMockMvc.perform(post("/api/scoreHistorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scoreHistory)))
                .andExpect(status().isCreated());

        // Validate the ScoreHistory in the database
        List<ScoreHistory> scoreHistorys = scoreHistoryRepository.findAll();
        assertThat(scoreHistorys).hasSize(databaseSizeBeforeCreate + 1);
        ScoreHistory testScoreHistory = scoreHistorys.get(scoreHistorys.size() - 1);
        assertThat(testScoreHistory.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testScoreHistory.getContentId()).isEqualTo(DEFAULT_CONTENT_ID);
        assertThat(testScoreHistory.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllScoreHistorys() throws Exception {
        // Initialize the database
        scoreHistoryRepository.saveAndFlush(scoreHistory);

        // Get all the scoreHistorys
        restScoreHistoryMockMvc.perform(get("/api/scoreHistorys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(scoreHistory.getId().intValue())))
                .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)))
                .andExpect(jsonPath("$.[*].contentId").value(hasItem(DEFAULT_CONTENT_ID.intValue())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)));
    }

    @Test
    @Transactional
    public void getScoreHistory() throws Exception {
        // Initialize the database
        scoreHistoryRepository.saveAndFlush(scoreHistory);

        // Get the scoreHistory
        restScoreHistoryMockMvc.perform(get("/api/scoreHistorys/{id}", scoreHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(scoreHistory.getId().intValue()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE))
            .andExpect(jsonPath("$.contentId").value(DEFAULT_CONTENT_ID.intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingScoreHistory() throws Exception {
        // Get the scoreHistory
        restScoreHistoryMockMvc.perform(get("/api/scoreHistorys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScoreHistory() throws Exception {
        // Initialize the database
        scoreHistoryRepository.saveAndFlush(scoreHistory);

		int databaseSizeBeforeUpdate = scoreHistoryRepository.findAll().size();

        // Update the scoreHistory
        scoreHistory.setScore(UPDATED_SCORE);
        scoreHistory.setContentId(UPDATED_CONTENT_ID);
        scoreHistory.setCreateDate(UPDATED_CREATE_DATE);

        restScoreHistoryMockMvc.perform(put("/api/scoreHistorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scoreHistory)))
                .andExpect(status().isOk());

        // Validate the ScoreHistory in the database
        List<ScoreHistory> scoreHistorys = scoreHistoryRepository.findAll();
        assertThat(scoreHistorys).hasSize(databaseSizeBeforeUpdate);
        ScoreHistory testScoreHistory = scoreHistorys.get(scoreHistorys.size() - 1);
        assertThat(testScoreHistory.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testScoreHistory.getContentId()).isEqualTo(UPDATED_CONTENT_ID);
        assertThat(testScoreHistory.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void deleteScoreHistory() throws Exception {
        // Initialize the database
        scoreHistoryRepository.saveAndFlush(scoreHistory);

		int databaseSizeBeforeDelete = scoreHistoryRepository.findAll().size();

        // Get the scoreHistory
        restScoreHistoryMockMvc.perform(delete("/api/scoreHistorys/{id}", scoreHistory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ScoreHistory> scoreHistorys = scoreHistoryRepository.findAll();
        assertThat(scoreHistorys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
