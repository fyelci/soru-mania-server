package com.fyelci.sorumania.web.rest;

import com.fyelci.sorumania.Application;
import com.fyelci.sorumania.domain.UserContentPreference;
import com.fyelci.sorumania.repository.UserContentPreferenceRepository;

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
 * Test class for the UserContentPreferenceResource REST controller.
 *
 * @see UserContentPreferenceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserContentPreferenceResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_CONTENT_ID = 1L;
    private static final Long UPDATED_CONTENT_ID = 2L;

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATE_DATE);

    private static final ZonedDateTime DEFAULT_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_MODIFIED_DATE_STR = dateTimeFormatter.format(DEFAULT_LAST_MODIFIED_DATE);

    @Inject
    private UserContentPreferenceRepository userContentPreferenceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserContentPreferenceMockMvc;

    private UserContentPreference userContentPreference;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserContentPreferenceResource userContentPreferenceResource = new UserContentPreferenceResource();
        ReflectionTestUtils.setField(userContentPreferenceResource, "userContentPreferenceRepository", userContentPreferenceRepository);
        this.restUserContentPreferenceMockMvc = MockMvcBuilders.standaloneSetup(userContentPreferenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userContentPreference = new UserContentPreference();
        userContentPreference.setContentId(DEFAULT_CONTENT_ID);
        userContentPreference.setCreateDate(DEFAULT_CREATE_DATE);
        userContentPreference.setLastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createUserContentPreference() throws Exception {
        int databaseSizeBeforeCreate = userContentPreferenceRepository.findAll().size();

        // Create the UserContentPreference

        restUserContentPreferenceMockMvc.perform(post("/api/userContentPreferences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userContentPreference)))
                .andExpect(status().isCreated());

        // Validate the UserContentPreference in the database
        List<UserContentPreference> userContentPreferences = userContentPreferenceRepository.findAll();
        assertThat(userContentPreferences).hasSize(databaseSizeBeforeCreate + 1);
        UserContentPreference testUserContentPreference = userContentPreferences.get(userContentPreferences.size() - 1);
        assertThat(testUserContentPreference.getContentId()).isEqualTo(DEFAULT_CONTENT_ID);
        assertThat(testUserContentPreference.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testUserContentPreference.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void checkContentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userContentPreferenceRepository.findAll().size();
        // set the field null
        userContentPreference.setContentId(null);

        // Create the UserContentPreference, which fails.

        restUserContentPreferenceMockMvc.perform(post("/api/userContentPreferences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userContentPreference)))
                .andExpect(status().isBadRequest());

        List<UserContentPreference> userContentPreferences = userContentPreferenceRepository.findAll();
        assertThat(userContentPreferences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserContentPreferences() throws Exception {
        // Initialize the database
        userContentPreferenceRepository.saveAndFlush(userContentPreference);

        // Get all the userContentPreferences
        restUserContentPreferenceMockMvc.perform(get("/api/userContentPreferences?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userContentPreference.getId().intValue())))
                .andExpect(jsonPath("$.[*].contentId").value(hasItem(DEFAULT_CONTENT_ID.intValue())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
                .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE_STR)));
    }

    @Test
    @Transactional
    public void getUserContentPreference() throws Exception {
        // Initialize the database
        userContentPreferenceRepository.saveAndFlush(userContentPreference);

        // Get the userContentPreference
        restUserContentPreferenceMockMvc.perform(get("/api/userContentPreferences/{id}", userContentPreference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userContentPreference.getId().intValue()))
            .andExpect(jsonPath("$.contentId").value(DEFAULT_CONTENT_ID.intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingUserContentPreference() throws Exception {
        // Get the userContentPreference
        restUserContentPreferenceMockMvc.perform(get("/api/userContentPreferences/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserContentPreference() throws Exception {
        // Initialize the database
        userContentPreferenceRepository.saveAndFlush(userContentPreference);

		int databaseSizeBeforeUpdate = userContentPreferenceRepository.findAll().size();

        // Update the userContentPreference
        userContentPreference.setContentId(UPDATED_CONTENT_ID);
        userContentPreference.setCreateDate(UPDATED_CREATE_DATE);
        userContentPreference.setLastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restUserContentPreferenceMockMvc.perform(put("/api/userContentPreferences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userContentPreference)))
                .andExpect(status().isOk());

        // Validate the UserContentPreference in the database
        List<UserContentPreference> userContentPreferences = userContentPreferenceRepository.findAll();
        assertThat(userContentPreferences).hasSize(databaseSizeBeforeUpdate);
        UserContentPreference testUserContentPreference = userContentPreferences.get(userContentPreferences.size() - 1);
        assertThat(testUserContentPreference.getContentId()).isEqualTo(UPDATED_CONTENT_ID);
        assertThat(testUserContentPreference.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testUserContentPreference.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void deleteUserContentPreference() throws Exception {
        // Initialize the database
        userContentPreferenceRepository.saveAndFlush(userContentPreference);

		int databaseSizeBeforeDelete = userContentPreferenceRepository.findAll().size();

        // Get the userContentPreference
        restUserContentPreferenceMockMvc.perform(delete("/api/userContentPreferences/{id}", userContentPreference.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserContentPreference> userContentPreferences = userContentPreferenceRepository.findAll();
        assertThat(userContentPreferences).hasSize(databaseSizeBeforeDelete - 1);
    }
}
