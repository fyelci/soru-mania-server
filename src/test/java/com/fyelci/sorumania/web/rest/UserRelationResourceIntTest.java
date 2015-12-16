package com.fyelci.sorumania.web.rest;

import com.fyelci.sorumania.Application;
import com.fyelci.sorumania.domain.UserRelation;
import com.fyelci.sorumania.repository.UserRelationRepository;

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
 * Test class for the UserRelationResource REST controller.
 *
 * @see UserRelationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserRelationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATE_DATE);

    private static final ZonedDateTime DEFAULT_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_MODIFIED_DATE_STR = dateTimeFormatter.format(DEFAULT_LAST_MODIFIED_DATE);

    @Inject
    private UserRelationRepository userRelationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserRelationMockMvc;

    private UserRelation userRelation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserRelationResource userRelationResource = new UserRelationResource();
        ReflectionTestUtils.setField(userRelationResource, "userRelationRepository", userRelationRepository);
        this.restUserRelationMockMvc = MockMvcBuilders.standaloneSetup(userRelationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userRelation = new UserRelation();
        userRelation.setCreateDate(DEFAULT_CREATE_DATE);
        userRelation.setLastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createUserRelation() throws Exception {
        int databaseSizeBeforeCreate = userRelationRepository.findAll().size();

        // Create the UserRelation

        restUserRelationMockMvc.perform(post("/api/userRelations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userRelation)))
                .andExpect(status().isCreated());

        // Validate the UserRelation in the database
        List<UserRelation> userRelations = userRelationRepository.findAll();
        assertThat(userRelations).hasSize(databaseSizeBeforeCreate + 1);
        UserRelation testUserRelation = userRelations.get(userRelations.size() - 1);
        assertThat(testUserRelation.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testUserRelation.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllUserRelations() throws Exception {
        // Initialize the database
        userRelationRepository.saveAndFlush(userRelation);

        // Get all the userRelations
        restUserRelationMockMvc.perform(get("/api/userRelations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userRelation.getId().intValue())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
                .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE_STR)));
    }

    @Test
    @Transactional
    public void getUserRelation() throws Exception {
        // Initialize the database
        userRelationRepository.saveAndFlush(userRelation);

        // Get the userRelation
        restUserRelationMockMvc.perform(get("/api/userRelations/{id}", userRelation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userRelation.getId().intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingUserRelation() throws Exception {
        // Get the userRelation
        restUserRelationMockMvc.perform(get("/api/userRelations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserRelation() throws Exception {
        // Initialize the database
        userRelationRepository.saveAndFlush(userRelation);

		int databaseSizeBeforeUpdate = userRelationRepository.findAll().size();

        // Update the userRelation
        userRelation.setCreateDate(UPDATED_CREATE_DATE);
        userRelation.setLastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restUserRelationMockMvc.perform(put("/api/userRelations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userRelation)))
                .andExpect(status().isOk());

        // Validate the UserRelation in the database
        List<UserRelation> userRelations = userRelationRepository.findAll();
        assertThat(userRelations).hasSize(databaseSizeBeforeUpdate);
        UserRelation testUserRelation = userRelations.get(userRelations.size() - 1);
        assertThat(testUserRelation.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testUserRelation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void deleteUserRelation() throws Exception {
        // Initialize the database
        userRelationRepository.saveAndFlush(userRelation);

		int databaseSizeBeforeDelete = userRelationRepository.findAll().size();

        // Get the userRelation
        restUserRelationMockMvc.perform(delete("/api/userRelations/{id}", userRelation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserRelation> userRelations = userRelationRepository.findAll();
        assertThat(userRelations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
