package com.fyelci.sorumania.web.rest;

import com.fyelci.sorumania.Application;
import com.fyelci.sorumania.domain.Lov;
import com.fyelci.sorumania.repository.LovRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the LovResource REST controller.
 *
 * @see LovResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LovResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_STR_PARAM1 = "AAAAA";
    private static final String UPDATED_STR_PARAM1 = "BBBBB";
    private static final String DEFAULT_STR_PARAM2 = "AAAAA";
    private static final String UPDATED_STR_PARAM2 = "BBBBB";

    private static final Integer DEFAULT_INT_PARAM1 = 1;
    private static final Integer UPDATED_INT_PARAM1 = 2;
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final Integer DEFAULT_SEQUENCE = 1;
    private static final Integer UPDATED_SEQUENCE = 2;

    @Inject
    private LovRepository lovRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLovMockMvc;

    private Lov lov;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LovResource lovResource = new LovResource();
        ReflectionTestUtils.setField(lovResource, "lovRepository", lovRepository);
        this.restLovMockMvc = MockMvcBuilders.standaloneSetup(lovResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        lov = new Lov();
        lov.setType(DEFAULT_TYPE);
        lov.setName(DEFAULT_NAME);
        lov.setStrParam1(DEFAULT_STR_PARAM1);
        lov.setStrParam2(DEFAULT_STR_PARAM2);
        lov.setIntParam1(DEFAULT_INT_PARAM1);
        lov.setDescription(DEFAULT_DESCRIPTION);
        lov.setStatus(DEFAULT_STATUS);
        lov.setSequence(DEFAULT_SEQUENCE);
    }

    @Test
    @Transactional
    public void createLov() throws Exception {
        int databaseSizeBeforeCreate = lovRepository.findAll().size();

        // Create the Lov

        restLovMockMvc.perform(post("/api/lovs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lov)))
                .andExpect(status().isCreated());

        // Validate the Lov in the database
        List<Lov> lovs = lovRepository.findAll();
        assertThat(lovs).hasSize(databaseSizeBeforeCreate + 1);
        Lov testLov = lovs.get(lovs.size() - 1);
        assertThat(testLov.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testLov.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLov.getStrParam1()).isEqualTo(DEFAULT_STR_PARAM1);
        assertThat(testLov.getStrParam2()).isEqualTo(DEFAULT_STR_PARAM2);
        assertThat(testLov.getIntParam1()).isEqualTo(DEFAULT_INT_PARAM1);
        assertThat(testLov.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLov.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLov.getSequence()).isEqualTo(DEFAULT_SEQUENCE);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = lovRepository.findAll().size();
        // set the field null
        lov.setType(null);

        // Create the Lov, which fails.

        restLovMockMvc.perform(post("/api/lovs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lov)))
                .andExpect(status().isBadRequest());

        List<Lov> lovs = lovRepository.findAll();
        assertThat(lovs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lovRepository.findAll().size();
        // set the field null
        lov.setName(null);

        // Create the Lov, which fails.

        restLovMockMvc.perform(post("/api/lovs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lov)))
                .andExpect(status().isBadRequest());

        List<Lov> lovs = lovRepository.findAll();
        assertThat(lovs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLovs() throws Exception {
        // Initialize the database
        lovRepository.saveAndFlush(lov);

        // Get all the lovs
        restLovMockMvc.perform(get("/api/lovs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lov.getId().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].strParam1").value(hasItem(DEFAULT_STR_PARAM1.toString())))
                .andExpect(jsonPath("$.[*].strParam2").value(hasItem(DEFAULT_STR_PARAM2.toString())))
                .andExpect(jsonPath("$.[*].intParam1").value(hasItem(DEFAULT_INT_PARAM1)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
                .andExpect(jsonPath("$.[*].sequence").value(hasItem(DEFAULT_SEQUENCE)));
    }

    @Test
    @Transactional
    public void getLov() throws Exception {
        // Initialize the database
        lovRepository.saveAndFlush(lov);

        // Get the lov
        restLovMockMvc.perform(get("/api/lovs/{id}", lov.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(lov.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.strParam1").value(DEFAULT_STR_PARAM1.toString()))
            .andExpect(jsonPath("$.strParam2").value(DEFAULT_STR_PARAM2.toString()))
            .andExpect(jsonPath("$.intParam1").value(DEFAULT_INT_PARAM1))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.sequence").value(DEFAULT_SEQUENCE));
    }

    @Test
    @Transactional
    public void getNonExistingLov() throws Exception {
        // Get the lov
        restLovMockMvc.perform(get("/api/lovs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLov() throws Exception {
        // Initialize the database
        lovRepository.saveAndFlush(lov);

		int databaseSizeBeforeUpdate = lovRepository.findAll().size();

        // Update the lov
        lov.setType(UPDATED_TYPE);
        lov.setName(UPDATED_NAME);
        lov.setStrParam1(UPDATED_STR_PARAM1);
        lov.setStrParam2(UPDATED_STR_PARAM2);
        lov.setIntParam1(UPDATED_INT_PARAM1);
        lov.setDescription(UPDATED_DESCRIPTION);
        lov.setStatus(UPDATED_STATUS);
        lov.setSequence(UPDATED_SEQUENCE);

        restLovMockMvc.perform(put("/api/lovs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lov)))
                .andExpect(status().isOk());

        // Validate the Lov in the database
        List<Lov> lovs = lovRepository.findAll();
        assertThat(lovs).hasSize(databaseSizeBeforeUpdate);
        Lov testLov = lovs.get(lovs.size() - 1);
        assertThat(testLov.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testLov.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLov.getStrParam1()).isEqualTo(UPDATED_STR_PARAM1);
        assertThat(testLov.getStrParam2()).isEqualTo(UPDATED_STR_PARAM2);
        assertThat(testLov.getIntParam1()).isEqualTo(UPDATED_INT_PARAM1);
        assertThat(testLov.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLov.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLov.getSequence()).isEqualTo(UPDATED_SEQUENCE);
    }

    @Test
    @Transactional
    public void deleteLov() throws Exception {
        // Initialize the database
        lovRepository.saveAndFlush(lov);

		int databaseSizeBeforeDelete = lovRepository.findAll().size();

        // Get the lov
        restLovMockMvc.perform(delete("/api/lovs/{id}", lov.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Lov> lovs = lovRepository.findAll();
        assertThat(lovs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
