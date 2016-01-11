package com.fyelci.sorumania.web.rest;

import com.fyelci.sorumania.Application;
import com.fyelci.sorumania.domain.QuestionRating;
import com.fyelci.sorumania.repository.QuestionRatingRepository;
import com.fyelci.sorumania.service.QuestionRatingService;
import com.fyelci.sorumania.web.rest.dto.QuestionRatingDTO;
import com.fyelci.sorumania.web.rest.mapper.QuestionRatingMapper;

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
 * Test class for the QuestionRatingResource REST controller.
 *
 * @see QuestionRatingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class QuestionRatingResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final Integer DEFAULT_RATE = 1;
    private static final Integer UPDATED_RATE = 2;

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATE_DATE);

    private static final ZonedDateTime DEFAULT_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_MODIFIED_DATE_STR = dateTimeFormatter.format(DEFAULT_LAST_MODIFIED_DATE);

    @Inject
    private QuestionRatingRepository questionRatingRepository;

    @Inject
    private QuestionRatingMapper questionRatingMapper;

    @Inject
    private QuestionRatingService questionRatingService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restQuestionRatingMockMvc;

    private QuestionRating questionRating;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QuestionRatingResource questionRatingResource = new QuestionRatingResource();
        ReflectionTestUtils.setField(questionRatingResource, "questionRatingService", questionRatingService);
        ReflectionTestUtils.setField(questionRatingResource, "questionRatingMapper", questionRatingMapper);
        this.restQuestionRatingMockMvc = MockMvcBuilders.standaloneSetup(questionRatingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        questionRating = new QuestionRating();
        questionRating.setRate(DEFAULT_RATE);
        questionRating.setCreateDate(DEFAULT_CREATE_DATE);
        questionRating.setLastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createQuestionRating() throws Exception {
        int databaseSizeBeforeCreate = questionRatingRepository.findAll().size();

        // Create the QuestionRating
        QuestionRatingDTO questionRatingDTO = questionRatingMapper.questionRatingToQuestionRatingDTO(questionRating);

        restQuestionRatingMockMvc.perform(post("/api/questionRatings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionRatingDTO)))
                .andExpect(status().isCreated());

        // Validate the QuestionRating in the database
        List<QuestionRating> questionRatings = questionRatingRepository.findAll();
        assertThat(questionRatings).hasSize(databaseSizeBeforeCreate + 1);
        QuestionRating testQuestionRating = questionRatings.get(questionRatings.size() - 1);
        assertThat(testQuestionRating.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testQuestionRating.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testQuestionRating.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void checkRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionRatingRepository.findAll().size();
        // set the field null
        questionRating.setRate(null);

        // Create the QuestionRating, which fails.
        QuestionRatingDTO questionRatingDTO = questionRatingMapper.questionRatingToQuestionRatingDTO(questionRating);

        restQuestionRatingMockMvc.perform(post("/api/questionRatings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionRatingDTO)))
                .andExpect(status().isBadRequest());

        List<QuestionRating> questionRatings = questionRatingRepository.findAll();
        assertThat(questionRatings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuestionRatings() throws Exception {
        // Initialize the database
        questionRatingRepository.saveAndFlush(questionRating);

        // Get all the questionRatings
        restQuestionRatingMockMvc.perform(get("/api/questionRatings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(questionRating.getId().intValue())))
                .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE)))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
                .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE_STR)));
    }

    @Test
    @Transactional
    public void getQuestionRating() throws Exception {
        // Initialize the database
        questionRatingRepository.saveAndFlush(questionRating);

        // Get the questionRating
        restQuestionRatingMockMvc.perform(get("/api/questionRatings/{id}", questionRating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(questionRating.getId().intValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingQuestionRating() throws Exception {
        // Get the questionRating
        restQuestionRatingMockMvc.perform(get("/api/questionRatings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestionRating() throws Exception {
        // Initialize the database
        questionRatingRepository.saveAndFlush(questionRating);

		int databaseSizeBeforeUpdate = questionRatingRepository.findAll().size();

        // Update the questionRating
        questionRating.setRate(UPDATED_RATE);
        questionRating.setCreateDate(UPDATED_CREATE_DATE);
        questionRating.setLastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        QuestionRatingDTO questionRatingDTO = questionRatingMapper.questionRatingToQuestionRatingDTO(questionRating);

        restQuestionRatingMockMvc.perform(put("/api/questionRatings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionRatingDTO)))
                .andExpect(status().isOk());

        // Validate the QuestionRating in the database
        List<QuestionRating> questionRatings = questionRatingRepository.findAll();
        assertThat(questionRatings).hasSize(databaseSizeBeforeUpdate);
        QuestionRating testQuestionRating = questionRatings.get(questionRatings.size() - 1);
        assertThat(testQuestionRating.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testQuestionRating.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testQuestionRating.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void deleteQuestionRating() throws Exception {
        // Initialize the database
        questionRatingRepository.saveAndFlush(questionRating);

		int databaseSizeBeforeDelete = questionRatingRepository.findAll().size();

        // Get the questionRating
        restQuestionRatingMockMvc.perform(delete("/api/questionRatings/{id}", questionRating.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<QuestionRating> questionRatings = questionRatingRepository.findAll();
        assertThat(questionRatings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
