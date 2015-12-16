package com.fyelci.sorumania.web.rest;

import com.fyelci.sorumania.Application;
import com.fyelci.sorumania.domain.CategoryLessonRelation;
import com.fyelci.sorumania.repository.CategoryLessonRelationRepository;

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
 * Test class for the CategoryLessonRelationResource REST controller.
 *
 * @see CategoryLessonRelationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CategoryLessonRelationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATE_DATE);

    private static final ZonedDateTime DEFAULT_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_MODIFIED_DATE_STR = dateTimeFormatter.format(DEFAULT_LAST_MODIFIED_DATE);

    @Inject
    private CategoryLessonRelationRepository categoryLessonRelationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCategoryLessonRelationMockMvc;

    private CategoryLessonRelation categoryLessonRelation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CategoryLessonRelationResource categoryLessonRelationResource = new CategoryLessonRelationResource();
        ReflectionTestUtils.setField(categoryLessonRelationResource, "categoryLessonRelationRepository", categoryLessonRelationRepository);
        this.restCategoryLessonRelationMockMvc = MockMvcBuilders.standaloneSetup(categoryLessonRelationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        categoryLessonRelation = new CategoryLessonRelation();
        categoryLessonRelation.setCreateDate(DEFAULT_CREATE_DATE);
        categoryLessonRelation.setLastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createCategoryLessonRelation() throws Exception {
        int databaseSizeBeforeCreate = categoryLessonRelationRepository.findAll().size();

        // Create the CategoryLessonRelation

        restCategoryLessonRelationMockMvc.perform(post("/api/categoryLessonRelations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categoryLessonRelation)))
                .andExpect(status().isCreated());

        // Validate the CategoryLessonRelation in the database
        List<CategoryLessonRelation> categoryLessonRelations = categoryLessonRelationRepository.findAll();
        assertThat(categoryLessonRelations).hasSize(databaseSizeBeforeCreate + 1);
        CategoryLessonRelation testCategoryLessonRelation = categoryLessonRelations.get(categoryLessonRelations.size() - 1);
        assertThat(testCategoryLessonRelation.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testCategoryLessonRelation.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllCategoryLessonRelations() throws Exception {
        // Initialize the database
        categoryLessonRelationRepository.saveAndFlush(categoryLessonRelation);

        // Get all the categoryLessonRelations
        restCategoryLessonRelationMockMvc.perform(get("/api/categoryLessonRelations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(categoryLessonRelation.getId().intValue())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
                .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE_STR)));
    }

    @Test
    @Transactional
    public void getCategoryLessonRelation() throws Exception {
        // Initialize the database
        categoryLessonRelationRepository.saveAndFlush(categoryLessonRelation);

        // Get the categoryLessonRelation
        restCategoryLessonRelationMockMvc.perform(get("/api/categoryLessonRelations/{id}", categoryLessonRelation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(categoryLessonRelation.getId().intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingCategoryLessonRelation() throws Exception {
        // Get the categoryLessonRelation
        restCategoryLessonRelationMockMvc.perform(get("/api/categoryLessonRelations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategoryLessonRelation() throws Exception {
        // Initialize the database
        categoryLessonRelationRepository.saveAndFlush(categoryLessonRelation);

		int databaseSizeBeforeUpdate = categoryLessonRelationRepository.findAll().size();

        // Update the categoryLessonRelation
        categoryLessonRelation.setCreateDate(UPDATED_CREATE_DATE);
        categoryLessonRelation.setLastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCategoryLessonRelationMockMvc.perform(put("/api/categoryLessonRelations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categoryLessonRelation)))
                .andExpect(status().isOk());

        // Validate the CategoryLessonRelation in the database
        List<CategoryLessonRelation> categoryLessonRelations = categoryLessonRelationRepository.findAll();
        assertThat(categoryLessonRelations).hasSize(databaseSizeBeforeUpdate);
        CategoryLessonRelation testCategoryLessonRelation = categoryLessonRelations.get(categoryLessonRelations.size() - 1);
        assertThat(testCategoryLessonRelation.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testCategoryLessonRelation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void deleteCategoryLessonRelation() throws Exception {
        // Initialize the database
        categoryLessonRelationRepository.saveAndFlush(categoryLessonRelation);

		int databaseSizeBeforeDelete = categoryLessonRelationRepository.findAll().size();

        // Get the categoryLessonRelation
        restCategoryLessonRelationMockMvc.perform(delete("/api/categoryLessonRelations/{id}", categoryLessonRelation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CategoryLessonRelation> categoryLessonRelations = categoryLessonRelationRepository.findAll();
        assertThat(categoryLessonRelations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
