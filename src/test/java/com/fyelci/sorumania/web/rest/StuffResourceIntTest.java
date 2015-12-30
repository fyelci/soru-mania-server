package com.fyelci.sorumania.web.rest;

import com.fyelci.sorumania.Application;
import com.fyelci.sorumania.domain.Stuff;
import com.fyelci.sorumania.repository.StuffRepository;
import com.fyelci.sorumania.web.rest.dto.StuffDTO;
import com.fyelci.sorumania.web.rest.mapper.StuffMapper;

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
 * Test class for the StuffResource REST controller.
 *
 * @see StuffResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StuffResourceIntTest {

    private static final String DEFAULT_NAME_SURNAME = "AAAAA";
    private static final String UPDATED_NAME_SURNAME = "BBBBB";

    @Inject
    private StuffRepository stuffRepository;

    @Inject
    private StuffMapper stuffMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStuffMockMvc;

    private Stuff stuff;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StuffResource stuffResource = new StuffResource();
        ReflectionTestUtils.setField(stuffResource, "stuffRepository", stuffRepository);
        ReflectionTestUtils.setField(stuffResource, "stuffMapper", stuffMapper);
        this.restStuffMockMvc = MockMvcBuilders.standaloneSetup(stuffResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stuff = new Stuff();
        stuff.setNameSurname(DEFAULT_NAME_SURNAME);
    }

    @Test
    @Transactional
    public void createStuff() throws Exception {
        int databaseSizeBeforeCreate = stuffRepository.findAll().size();

        // Create the Stuff
        StuffDTO stuffDTO = stuffMapper.stuffToStuffDTO(stuff);

        restStuffMockMvc.perform(post("/api/stuffs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stuffDTO)))
                .andExpect(status().isCreated());

        // Validate the Stuff in the database
        List<Stuff> stuffs = stuffRepository.findAll();
        assertThat(stuffs).hasSize(databaseSizeBeforeCreate + 1);
        Stuff testStuff = stuffs.get(stuffs.size() - 1);
        assertThat(testStuff.getNameSurname()).isEqualTo(DEFAULT_NAME_SURNAME);
    }

    @Test
    @Transactional
    public void getAllStuffs() throws Exception {
        // Initialize the database
        stuffRepository.saveAndFlush(stuff);

        // Get all the stuffs
        restStuffMockMvc.perform(get("/api/stuffs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stuff.getId().intValue())))
                .andExpect(jsonPath("$.[*].nameSurname").value(hasItem(DEFAULT_NAME_SURNAME.toString())));
    }

    @Test
    @Transactional
    public void getStuff() throws Exception {
        // Initialize the database
        stuffRepository.saveAndFlush(stuff);

        // Get the stuff
        restStuffMockMvc.perform(get("/api/stuffs/{id}", stuff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stuff.getId().intValue()))
            .andExpect(jsonPath("$.nameSurname").value(DEFAULT_NAME_SURNAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStuff() throws Exception {
        // Get the stuff
        restStuffMockMvc.perform(get("/api/stuffs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStuff() throws Exception {
        // Initialize the database
        stuffRepository.saveAndFlush(stuff);

		int databaseSizeBeforeUpdate = stuffRepository.findAll().size();

        // Update the stuff
        stuff.setNameSurname(UPDATED_NAME_SURNAME);
        StuffDTO stuffDTO = stuffMapper.stuffToStuffDTO(stuff);

        restStuffMockMvc.perform(put("/api/stuffs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stuffDTO)))
                .andExpect(status().isOk());

        // Validate the Stuff in the database
        List<Stuff> stuffs = stuffRepository.findAll();
        assertThat(stuffs).hasSize(databaseSizeBeforeUpdate);
        Stuff testStuff = stuffs.get(stuffs.size() - 1);
        assertThat(testStuff.getNameSurname()).isEqualTo(UPDATED_NAME_SURNAME);
    }

    @Test
    @Transactional
    public void deleteStuff() throws Exception {
        // Initialize the database
        stuffRepository.saveAndFlush(stuff);

		int databaseSizeBeforeDelete = stuffRepository.findAll().size();

        // Get the stuff
        restStuffMockMvc.perform(delete("/api/stuffs/{id}", stuff.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Stuff> stuffs = stuffRepository.findAll();
        assertThat(stuffs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
